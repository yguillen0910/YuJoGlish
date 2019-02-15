/*
 * @(#) VoicePracticeActivity.java  1 27/02/15
 * 
 * Copyrigth (c) 2015 Jorge Hernández, Yuzmhar Guillén
 * Caracas, Venezuela
 * Todos los derechos reservados.
 *   
 */
package com.yujoglish;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.TrackingValues;
import com.metaio.sdk.jni.TrackingValuesVector;
import com.metaio.sdk.jni.Vector3d;
import com.metaio.tools.io.AssetsManager;
import com.yujoglish.configuracion.usuario.AgeManager;
import com.yujoglish.dbhelper.DataBaseManager;
import com.yujoglish.model.Palabra;
import com.yujoglish.util.Util;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * Clase que se encarga de mostrar un objeto en tercera dimensión
 * una vez se haya escaneado un target, además graba un audio 
 * para ser posteriormente corregido
 *  
 * @author Yuzmhar Guillén
 * @author Jorge Hernandez
 * @version 1, 27/02/2015 
 */
public class VoicePracticeActivity extends ARViewActivity implements RecognitionListener{
	
	/*
	 * Metaio SDK callback handler
	 */
	private MetaioSDKCallbackHandler mCallbackHandler;
	
	/*
	 * TextView para mostrar errores
	 */
	private TextView returnedText;
	
	/*
	 * Botón para obtener sonido
	 */
	private ToggleButton toggleButton;
	
	/*
	 * ProgressBar 
	 */
	private ProgressBar progressBar;
	
	/*
	 * SpeechRecognizer proporciona acceso al 
	 * servicio de reconocimiento de voz
	 */
	private SpeechRecognizer speech = null;
	
	/*
	 * 
	 */
	private Intent recognizerIntent;
	
	/*
	 * 
	 */
	private String LOG_TAG = "VoiceRecognitionActivity";
	
	/*
	 * Tiempo de espera
	 */
	private int leadTime = 2000;
	
	/*
	 * Ruta y Nombre de la actividad a la cual
	 * se le enviará la información
	 */
	final static String POSICION_VOICE = "com.yujoglish.WordPracticeActivity";
	
	/*
	 * Tiempo en que inicia la actividad
	 */
	private String tiempoInicio = null;
	
	/*
	 * Tiempo en que termina la actividad
	 */
	private String tiempoFin = null; 
	
	/*
	 * Posicion de la palabra actual  
	 */
	private String posicionPalabra = null;
	
	/*
	 * Nombre de la lección 
	 */
	private String nombreLeccion = null;
	
	/*
	 * Número de lección 
	 */
	private String numeroLeccion = null;
	
	/*
	 * Nombre de palabra Correcta
	 */
	private String respuestaCorrecta = null;
	
	/*
	 * Identificador de palabra correcta
	 */
	private Integer idPalabraCorrecta = 0;
	
	/*
	 * Manejador de Base de datos
	 */	
	DataBaseManager manager;
	
	/*
	 * Modelo que se representará en tercera dimensión 
	 */
	private IGeometry model;
	
	/*
	 * Imagen a mostrar según la respuesta 
	 * del usuario
	 */
	private ImageView imageCorrecion;
	
	/*
	 * Url para acceder al webService
	 */
	private String serverURL;
	
	/*
	 * 
	 */
	private ArrayList<String> matches;
	
	/** 
	 * Metodo que se llama cuando se creó por primera vez la actividad.
	 * Se crean la vistas
	 * Se inicializan las variables
	 * Se obtiene el tiempo en que se inicia la actividad
	 * 
	 * @see com.metaio.sdk.ARViewActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice_test);
		mCallbackHandler = new MetaioSDKCallbackHandler();
		inicializarVariables();
		tiempoInicio = AgeManager.obtenerTiempo();
	}

	/**
	 * Metodo para inicializar variables a utilizar
	 */
	private void inicializarVariables() {
		
		String infoRecibidaVoice = null;
		String[] partes = null;
		Palabra palabraCorrecta;
		manager = new DataBaseManager(this);
		
		//TextView al que se le seteará la palabra seleccionada actualmente
		TextView palabraTv = (TextView) mGUIView.findViewById(R.id.tVWord);
		
		imageCorrecion = (ImageView) mGUIView.findViewById(R.id.iVCorrecion);
		returnedText = (TextView) mGUIView.findViewById(R.id.tVPlay);
		progressBar = (ProgressBar) mGUIView.findViewById(R.id.progressBar1);
		toggleButton = (ToggleButton) mGUIView.findViewById(R.id.toggleButton1);
		
		toggleButton.setText(null);
		toggleButton.setTextOn(null);
		toggleButton.setTextOff(null);	
		imageCorrecion.setVisibility(View.INVISIBLE);
		progressBar.setVisibility(View.INVISIBLE);
		
		speech = SpeechRecognizer.createSpeechRecognizer(this);
		speech.setRecognitionListener(this);
		
		recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"en_US");
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en_US");
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

		toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			/**
			 * Definición de interfaz para una devolución de llamada 
			 * que se invoca cuando el estado de activación de un botón compuesto cambió.
			 */
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				if (isChecked) {
					progressBar.setVisibility(View.VISIBLE);
					progressBar.setIndeterminate(true);
					speech.startListening(recognizerIntent);
				} else {
					progressBar.setIndeterminate(false);
					progressBar.setVisibility(View.INVISIBLE);
					speech.stopListening();
				}
			}
		});
		
		Intent mensaje = getIntent();
		infoRecibidaVoice = mensaje.getStringExtra(WordPracticeActivity.POSICION_WORD);
		if( infoRecibidaVoice != null) {
			partes = infoRecibidaVoice.split("-");
		}
		posicionPalabra = partes[0]; 
		nombreLeccion = partes[1]; 
		numeroLeccion = manager.buscarLeccionPorNombre(nombreLeccion);
		
		palabraCorrecta = manager.buscarPalabraPorPosicionLeccion(posicionPalabra, numeroLeccion);
		respuestaCorrecta = palabraCorrecta.getNombre();
		idPalabraCorrecta = palabraCorrecta.getIdPalabra();
		
		//se setea el nombre de la palabra al textView creado anteriormente
				String wordStr = WordUtils.capitalizeFully(respuestaCorrecta);
				palabraTv.setText(wordStr);	
	}
	
	/**
	 * (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
	}
	
	/**
	 *  Método para enviar información a siguiente actividad
	 *  acerca de que objeto debe mostrar y finaliza la pantalla actual
	 */
	private void nextWordPractice()
	{
		Integer p = Integer.parseInt(posicionPalabra)+1;
		int cantidadPalabras = manager.obtenerCantPalabrasUsuarioActivo(nombreLeccion);
		
		if(p<=cantidadPalabras){
			Intent myIntent = new Intent (this, WordPracticeActivity.class);
			String infoEnvio = p.toString()+"-"+nombreLeccion;
			myIntent.putExtra(POSICION_VOICE, infoEnvio);
			startActivity(myIntent);
		}else {
			//Intent myIntent = new Intent (this, LessonActivity.class);
			//startActivity(myIntent);
			Intent myIntent = new Intent (this , FinishActivity.class);
			myIntent.putExtra(POSICION_VOICE, "Has Finalizado La Practica!");
			startActivity(myIntent);
		}
		tiempoFin = AgeManager.obtenerTiempo();
		Long diferencia = AgeManager.diferenciaEntreTiempos(tiempoInicio, tiempoFin);
		
		guardarTiempoPracticePalabraUsuario(diferencia.intValue(), "voice", idPalabraCorrecta);
		
		this.finish();
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.metaio.sdk.ARViewActivity#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
	}

	/**
	 * (non-Javadoc)
	 * @see com.metaio.sdk.ARViewActivity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		if (speech != null) {
			speech.destroy();
			Log.i(LOG_TAG, "destroy");
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.metaio.sdk.ARViewActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		this.finish();
	}
	
	/**
	 * Método que utiliza el MetaioSDK para obtener el
	 * Layout de la actividad
	 * 
	 * @see com.metaio.sdk.ARViewActivity#getGUILayout()
	 */
	@Override
	protected int getGUILayout() 
	{
		return R.layout.activity_voice_test; 
	}
	
	/**
	 * Método que se ejecuta al hacer click, en el botón exit
	 * @param v vista actual
	 */
	public void onButtonClick(View v)
	{
		finish();
	}
	
	/**
	 * Método encargado de cargar el contenido de MetaioSDK
	 *  por ejemplo, datos de seguimiento,Geometrías etc.
	 *  
	 * @see com.metaio.sdk.ARViewActivity#loadContents()
	 */
	@Override
	protected void loadContents() 
	{
		try	{
			String trackingConfigFile = null;
			String modelPath;
			String rutaXml = nombreLeccion.toLowerCase()+"/"+nombreLeccion.toLowerCase();
			String rutaObj = nombreLeccion.toLowerCase()+"/"+respuestaCorrecta;
			
			int cantidadPalabras = manager.obtenerCantPalabrasUsuarioActivo(nombreLeccion);
			/*
			 * obteniendo data del archivo xml que contiene los targets a usar			
			 */
			if(cantidadPalabras <= 6){
				
				trackingConfigFile = AssetsManager.getAssetPath(getApplicationContext(), rutaXml+".xml");
			} else if (cantidadPalabras > 6){
				
				trackingConfigFile = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/YuJoGlish/"+rutaXml+".xml";
			}
						
			final boolean result = metaioSDK.setTrackingConfiguration(trackingConfigFile);
			MetaioDebug.log(Log.INFO, "data loaded: " + result);
				
			//cargando path del modelo 	
			modelPath = AssetsManager.getAssetPath(getApplicationContext(), rutaObj+".obj");
			
			if(modelPath == null){
				modelPath = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/YuJoGlish/"+rutaObj+".obj";
			}
			
			File fichero = new File(modelPath);
					
			System.out.println("modelPath "+modelPath);
			if (fichero.exists()){
				
				//creando la geometría
				model = metaioSDK.createGeometry(modelPath);
								MetaioDebug.log(Log.INFO, "model loaded " + modelPath);
				try	{
					MetaioDebug.log(Log.INFO, "nombre del modelo " + model.getName());
				}catch (Exception e) {
					MetaioDebug.log(Log.ERROR, "error obteniendo nombre del modelo");
				}
				
				//se asigna el target con el que se mostrará el modelo 
				model.setCoordinateSystemID(Integer.parseInt(posicionPalabra));
				
				if (model != null) {
					model.setScale(2.f);
				}else{
					MetaioDebug.log(Log.ERROR, "Error loading geometry: " + modelPath);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			MetaioDebug.log(Log.ERROR, "Falla Load Contents: "+e.getMessage());
		}
	}

	/**
	 * Método que se llama cuando la geometría es tocada
	 * @see com.metaio.sdk.ARViewActivity#onGeometryTouched(com.metaio.sdk.jni.IGeometry)
	 */
	@Override
	protected void onGeometryTouched(IGeometry geometry) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Clase implementada para el uso y modificación del método onTrackingEvent
	 * lo que permitió la asociación de un modelo con un target. 
	 * 
	 * @author Yuzmhar Guillén
	 */
	final private class MetaioSDKCallbackHandler extends IMetaioSDKCallback 
	{
		/**
		 * Muestra GUI después de que el SDK este listo
		 * @see com.metaio.sdk.jni.IMetaioSDKCallback#onSDKReady()
		 */
		@Override
		public void onSDKReady() 
		{		
			runOnUiThread(new Runnable(){
				@Override
				public void run(){
					mGUIView.setVisibility(View.VISIBLE);
				}
			});
		}
		
		/**
		 * Método para detectar los targets
		 * si detectamos cualquier objetivo, ligamos la geometría cargado a este objetivo
		 * 
		 * @see com.metaio.sdk.jni.IMetaioSDKCallback#onTrackingEvent(com.metaio.sdk.jni.TrackingValuesVector)
		 */
		@Override
		public void onTrackingEvent(TrackingValuesVector trackingValues)
		{
			if(model != null){
				for (int i=0; i<trackingValues.size(); i++)	{
					final TrackingValues tv = trackingValues.get(i);
					if (tv.isTrackingState()) {
						System.out.println("COORDINATE SYSTEM ID "+tv.getCoordinateSystemID());
						break;
					}
				}
			}
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see android.speech.RecognitionListener#onBeginningOfSpeech()
	 */
	@Override
	public void onBeginningOfSpeech() {
		Log.i(LOG_TAG, "onBeginningOfSpeech");
		progressBar.setIndeterminate(false);
		progressBar.setMax(10);
	}

	/**
	 * (non-Javadoc)
	 * @see android.speech.RecognitionListener#onBufferReceived(byte[])
	 */
	@Override
	public void onBufferReceived(byte[] buffer) {
		Log.i(LOG_TAG, "onBufferReceived: " + buffer);
	}

	/**
	 * (non-Javadoc)
	 * @see android.speech.RecognitionListener#onEndOfSpeech()
	 */
	@Override
	public void onEndOfSpeech() {
		Log.i(LOG_TAG, "onEndOfSpeech");
		progressBar.setIndeterminate(true);
		toggleButton.setChecked(false);
	}

	/**
	 * (non-Javadoc)
	 * @see android.speech.RecognitionListener#onError(int)
	 */
	@Override
	public void onError(int errorCode) {
		String errorMessage = getErrorText(errorCode);
		Log.d(LOG_TAG, "FAILED " + errorMessage);
		returnedText.setText(errorMessage);
		toggleButton.setChecked(false);
	}

	/**
	 * (non-Javadoc)
	 * @see android.speech.RecognitionListener#onEvent(int, android.os.Bundle)
	 */
	@Override
	public void onEvent(int arg0, Bundle arg1) {
		Log.i(LOG_TAG, "onEvent");
	}

	/**
	 * (non-Javadoc)
	 * @see android.speech.RecognitionListener#onPartialResults(android.os.Bundle)
	 */
	@Override
	public void onPartialResults(Bundle arg0) {
		Log.i(LOG_TAG, "onPartialResults");
	}

	/**
	 * (non-Javadoc)
	 * @see android.speech.RecognitionListener#onReadyForSpeech(android.os.Bundle)
	 */
	@Override
	public void onReadyForSpeech(Bundle arg0) {
		Log.i(LOG_TAG, "onReadyForSpeech");
	}
	
	/**
	 * (non-Javadoc)
	 * @see android.speech.RecognitionListener#onResults(android.os.Bundle)
	 */
	@Override
	public void onResults(Bundle results) {
		Log.i(LOG_TAG, "onResults");
		matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		String text = "";
		String palabras = "";
		
		String palabra = null;
		boolean flag = false;
	
		for (int i=0;i<=matches.size()-1;i++){
			palabra = matches.get(i);
			if(i != matches.size()-1){
				palabras += palabra.replace(" ", "") + ",";
			} else {
				palabras += palabra.replace(" ", "");
			}
			
		}
		
		serverURL = crearURL(palabras);
	    
		
	   //new LongOperation().execute(serverURL);
		for (String result : matches){
				palabra = result;
				text += result + "\n";
				
				if ( respuestaCorrecta.equalsIgnoreCase(palabra) ) {
					flag=true;
					break;
				}
		}
		System.out.println("text "+text);
		if(flag){
			correcto(); 
		}else {
			incorrecto();
		}
		
		imageCorrecion.setVisibility(View.VISIBLE);
		runActivityTime();
	}

	/**
	 * (non-Javadoc)
	 * @see android.speech.RecognitionListener#onRmsChanged(float)
	 */
	@Override
	public void onRmsChanged(float rmsdB) {
		Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
		progressBar.setProgress((int) rmsdB);
	}
	
	/**
	 * Método para obtener el tipo de error que
	 * se produjo  
	 */
	public static String getErrorText(int errorCode) {
		String message;
		switch (errorCode) {
			case SpeechRecognizer.ERROR_AUDIO:
			message = "Audio recording error";
			break;
			case SpeechRecognizer.ERROR_CLIENT:
			message = "Client side error";
			break;
			case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
			message = "Insufficient permissions";
			break;
			case SpeechRecognizer.ERROR_NETWORK:
			message = "Network error";
			break;
			case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
			message = "Network timeout";
			break;
			case SpeechRecognizer.ERROR_NO_MATCH:
			message = "No match";
			break;
			case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
			message = "RecognitionService busy";
			break;
			case SpeechRecognizer.ERROR_SERVER:
			message = "error from server";
			break;
			case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
			message = "No speech input";
			break;
			default:
			message = "Didn't understand, please try again.";
			break;
		}
		return message;
	}
	
	/**
	 * Método que se ejecuta al hacer click en botón next 
	 * el mismo me enviará a la próxima pantalla Voice Practice
	 * @param v vista actual
	 */
	public void onNextVoiceButtonClick(View v) 
	{	
		ImageButton next = (ImageButton) mGUIView.findViewById(R.id.IBNextVoice);
		next.setBackgroundResource(R.drawable.grey_bg_oval);
		next.setEnabled(false);
		next.setClickable(false); 
		runActivityTime();
	}
	
	/**
	 *Método que reproduce sonido relacionado 
	 *con que la respuesta es correcta
	 */
	public void correcto () 
	{
		playSound("goodJob");
	}
	
	/**
	 *Método que reproduce sonido relacionado 
	 *con que la respuesta es incorrecta y carga 
	 *imagen con icono referente a que la respuesta 
	 *es incorrecta
	 */
	public void incorrecto() {
		imageCorrecion.setImageResource(R.drawable.incorrect_ic);
		playSound("tryagain");
	}

	/**
	 * Método para setear tiempo de espera 
	 * a la ejecución de la tarea onRestart
	 */
	public void runActivityTime(){
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				nextWordPractice();
			}
		}, leadTime);
	}

	/**
	 * Proporciona controlador callback SDK si se desea.
	 * @return Regreso controlador callback sdk
	 * 
	 * @see com.metaio.sdk.ARViewActivity#getMetaioSDKCallbackHandler()
	 */
	@Override
	protected IMetaioSDKCallback getMetaioSDKCallbackHandler() 
	{
		return mCallbackHandler;
	}
	
	/**
	 * Método encargado de reproducir sonidos
	 * @param ruta ruta en donde se encuentra el archivo .mp3 a reproducir
	 */
	public void playSound(String ruta){
		String mp3PathInternal = null; 
		String mp3PathExternal = null; 
		FileDescriptor fd = null;
		MediaPlayer player = new MediaPlayer();
		
		mp3PathInternal = AssetsManager.getAssetPath(getApplicationContext(), ruta+".mp3");
		
		if(mp3PathInternal == null){
			mp3PathExternal = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/YuJoGlish/"+ruta+".mp3";
			File fichero = new File(mp3PathExternal);
			
			if(fichero.exists()){
				
				try {
					 File baseDir = Environment.getExternalStorageDirectory();
				     String audioPath = baseDir.getAbsolutePath() + "/YuJoGlish/"+ruta+".mp3";
				    FileInputStream fis;
					fis = new FileInputStream(audioPath);
					fd = fis.getFD();
					player.setDataSource(fd);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
			}
			
		} else {
			AssetFileDescriptor sonido;
			try {
				sonido = getResources().getAssets().openFd(ruta+".mp3");
				fd = sonido.getFileDescriptor();
				player.setDataSource(fd, sonido.getStartOffset(),sonido.getLength());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		}
		if (fd != null){
			try {
				player.setLooping(false);
				player.prepare();
				player.start();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Método para guardar tiempo en que estuvo activa la actividad
	 * 
	 * @param tiempo Tiempo que estuvo activa la actividad
	 * @param tipo puede ser voice o word indica el tipo de practica
	 * @param idPalabraCorrecta identificador de la palabra correcta
	 */
	public void guardarTiempoPracticePalabraUsuario(Integer tiempo, String tipo, Integer idPalabraCorrecta) {
		
		Integer tiempoPractice = null;
		Integer	idUsuarioPalabra = 0;
		String idUsuarioActivo = null;
		manager = new DataBaseManager(this);
		
		Cursor cursorUsuarioActivo = manager.cargarCursorUsuarioActivo();
		if (cursorUsuarioActivo.moveToNext()){
			idUsuarioActivo = cursorUsuarioActivo.getString(cursorUsuarioActivo.getColumnIndex(manager.CN_ID_USUARIO));
		}
		
		Cursor cusorPalabraUsuario = manager.obtenerUsuarioPalabraPorTipo(idUsuarioActivo, idPalabraCorrecta.toString(), tipo);
		if (cusorPalabraUsuario.moveToNext()) {
			tiempoPractice = cusorPalabraUsuario.getInt(cusorPalabraUsuario.getColumnIndex(manager.CN_TIEMPO_PRACT))+tiempo;
			idUsuarioPalabra = cusorPalabraUsuario.getInt(cusorPalabraUsuario.getColumnIndex(manager.CN_ID_USUARIO_PALABRA));
			manager.actualizarTiempoPracticeUsuarioPalabra(idUsuarioPalabra.toString(), tiempoPractice.toString());
		}else{
			manager.insertarUsuarioPalabra(idUsuarioActivo, idPalabraCorrecta.toString(), "0", "0", tipo, "0",tiempo.toString());
		}
	}
	
private class LongOperation  extends AsyncTask<String, Void, Void> {
		
		private String Content;
	    
		protected Void doInBackground(String... urls) {
			BufferedReader reader=null;
	        try	{
	            URL url = new URL(urls[0]);
	            
	            // Send POST data request
		        URLConnection conn = url.openConnection(); 
		        
		        // Get the server response 
		        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		        StringBuilder sb = new StringBuilder();
		        String line = null;
		            
		        // Read Server Response
			    while((line = reader.readLine()) != null)  {
			        	 sb.append(line + "\n");
			    }
		        // Append Server Response To Content String 
		        Content = sb.toString();
		        }	catch(Exception ex)   {
		        		ex.getMessage();
		        }	finally   {
		        				try	{
		        					reader.close();
		        				} catch(Exception ex) {
		        					
		        				}
		        }
	           return null;
	        }
	         
	        protected void onPostExecute(Void unused) {
	        	
	        	if(Content == null){
	        		String palabra = "";
	        		boolean flag = false;
	        	
	        		for (String result : matches){
	        			palabra = result;
						if ( respuestaCorrecta.equalsIgnoreCase(palabra) ) {
	        				flag=true;
	        				break;
	        			}
	        		}
					if(flag){
						correcto(); 
					}else {
						incorrecto();
					}
					imageCorrecion.setVisibility(View.VISIBLE);
					runActivityTime();	
	        	} else if(Content != null){
	        		String correccion = traducirJson(Content);
		            if (correccion.equalsIgnoreCase("si")){
		            	System.out.println("CORRECTO" );
		            	correcto ();
		            }else if (correccion.equalsIgnoreCase("no")){
		            	System.out.println("INCORRECTO" );
		            	incorrecto();
		            }
		            	imageCorrecion.setVisibility(View.VISIBLE);
		        		runActivityTime();
	            }
	          }             
	     }

	 public String traducirJson (String response){
		 String resultado = "";
	     JSONObject jsonResponse;
	     try   {	        	
	        jsonResponse = new JSONObject(response);
	        resultado = jsonResponse.optJSONObject("correction").optJSONObject("Correction").optString("respuesta").toString();
	     } catch (JSONException e) {
	 		// TODO Auto-generated catch block
	 		e.printStackTrace();
	 	}
		return resultado;
	 }
	 
	 public String crearURL(String palabra){
			
			String URL ="";
					
			URL = "http://"+Util.IP_SERVIDOR+":8080/YuJoGlishWebSite/corrections/response.json?id="+numeroLeccion+"&palabras="+palabra+"&posicion="+posicionPalabra;
			System.out.println("URL "+URL);
			return URL;
			
		}
}
