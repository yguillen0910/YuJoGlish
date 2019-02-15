/*
 * @(#) WordPracticeActivity.java  1 01/03/15
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
import java.util.Collections;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.TrackingValues;
import com.metaio.sdk.jni.TrackingValuesVector;
import com.metaio.tools.io.AssetsManager;
import com.yujoglish.configuracion.usuario.AgeManager;
import com.yujoglish.dbhelper.DataBaseManager;
import com.yujoglish.model.Palabra;
import com.yujoglish.util.Util;

/**
 * Clase que se encarga de mostrar un objeto en tercera dimensión
 * una vez se haya escaneado un target
 *  
 * @author Yuzmhar Guillén
 * @version 1, 01/03/2015 
 */
public class WordPracticeActivity extends ARViewActivity {
	
	/*
	 * Ruta y Nombre de la actividad a la cual
	 * se le enviará la información
	 */
	final static String POSICION_WORD = "com.yujoglish.VoicePracticeActivity";
		
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
	 * Tiempo en que inicia la actividad
	 */
	private String tiempoInicio = null;
	
	/*
	 * Tiempo en que termina la actividad
	 */
	private String tiempoFin = null;
	
	/*
	 * Imagen a mostrar según la respuesta 
	 * del usuario
	 */
	private ImageView imageCorrecion;
		
	/*
	 * Metaio SDK callback handler
	 */
	private MetaioSDKCallbackHandler mCallbackHandler;
	
	/*
	 * Tiempo de espera
	 */
	private int leadTime = 2000;
	
	/*
	 * Cursor para obtener palabras de la lección
	 */
	private Cursor cursorPalabra;
	
	/*
	 * Url para acceder al webService
	 */
	private String serverURL;
	/*
	 * Repuesta seleccionada
	 */
	private String respuestaSeleccionada;
	
	/**
	 * Método que se llama cuando la geometría es tocada
	 * @see com.metaio.sdk.ARViewActivity#onGeometryTouched(com.metaio.sdk.jni.IGeometry)
	 */
	@Override
	protected void onGeometryTouched(IGeometry geometry) {
		// TODO Auto-generated method stub
	}

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
		setContentView(R.layout.activity_word_practice);
		mCallbackHandler = new MetaioSDKCallbackHandler();
		cargarPalabrasButtons();
		tiempoInicio = AgeManager.obtenerTiempo();			
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
		return R.layout.activity_word_test; 
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
	 * Metodo que se encarga de cargar las palabras de la leccion a los botones
	 * que se usan para capturar la respuesta del usuario
	 */
	private void cargarPalabrasButtons() 
	{
		int i=0;
		String infoRecibidaVoicePractice = null;
		String infoRecibidaMenu = null;
		String[] partes = null;
		Palabra palabraCorrecta;
		manager = new DataBaseManager(this);
		ArrayList<Button> idButton = new ArrayList<Button>(); //Lista que contiene los id de los botones a utilizar para mostar las posibles respuestas 
		
		imageCorrecion = (ImageView) mGUIView.findViewById(R.id.iVCorrecion);
		imageCorrecion.setVisibility(View.INVISIBLE);
				
		//Se obtienen los id de los botones
		Button uno = (Button) mGUIView.findViewById(R.id.bOpcionUno);
		Button dos = (Button) mGUIView.findViewById(R.id.bOpcionDos);
		Button tres = (Button) mGUIView.findViewById(R.id.bOpcionTres);
		Button cuatro = (Button) mGUIView.findViewById(R.id.bOpcionCuatro);
						
		idButton.add(uno);
		idButton.add(dos);
		idButton.add(tres);
		idButton.add(cuatro);
		
		habilitarDeshabilitarButtons(false, false, false, false);
		
		//se obtiene mensaje de la pantalla anterior
		Intent mensaje = getIntent();
		infoRecibidaMenu = mensaje.getStringExtra(MenuActivity.LEVEL_INFO_PRACTICE);
		infoRecibidaVoicePractice = mensaje.getStringExtra(VoicePracticeActivity.POSICION_VOICE);
		
		if( infoRecibidaMenu != null){
			partes = infoRecibidaMenu.split("-");
		}else {
			partes = infoRecibidaVoicePractice.split("-");
		}
		
		posicionPalabra = partes[0]; 
		nombreLeccion = partes[1]; 
			
		numeroLeccion = manager.buscarLeccionPorNombre(nombreLeccion); 
		palabraCorrecta = manager.buscarPalabraPorPosicionLeccion(posicionPalabra, numeroLeccion);
		respuestaCorrecta = palabraCorrecta.getNombre();
		idPalabraCorrecta = palabraCorrecta.getIdPalabra();
		cursorPalabra = manager.buscarPalabraPorLeccion(Integer.parseInt(numeroLeccion));
			
		ArrayList idButtonAleatorio = listaNumerosAleatorios(4);
		ArrayList<String> palabrasLeccion = listaPalabraAleatoria();
				
		Iterator<String> it = palabrasLeccion.iterator();
		while (it.hasNext()) {
			String respuesta = "";
			String wordStr = "";
			Button miBoton = (Button) idButton.get((int) idButtonAleatorio.get(i));
			
			if (i == 0) {
				wordStr = WordUtils.capitalizeFully(respuestaCorrecta);
				miBoton.setText(wordStr);
				i++;
			} else if( i != 0 ) {
				respuesta = it.next();
				if ( !respuesta.equalsIgnoreCase(respuestaCorrecta) ) {
					wordStr = WordUtils.capitalizeFully(respuesta);
					miBoton.setText(wordStr); 
					i++;
				}		
			}
			if(i==4){
				break;
			}
		}
	}
	
	/**
	 * Método que se ejecuta al hacer click en el botón 
	 * con id bOpcionUno la acción que ejecuta 
	 * es desactivar el resto de los botones y llamar a 
	 * método correcto o incorrecto que se encargan de guardar 
	 * la información
	 * 
	 * @param v vista actual
	 */
	public void onOpcionUnoButtonClick(View v)
	{		
		Button uno = (Button) mGUIView.findViewById(R.id.bOpcionUno);
		habilitarDeshabilitarButtons(true, false, false, false);
		respuestaSeleccionada = uno.getText().toString();
		serverURL = crearURL(uno.getText().toString());
	    //new LongOperation().execute(serverURL);
		if(respuestaCorrecta.equalsIgnoreCase((String) uno.getText())){
			correcto();
		}else {
			incorrecto();
		}
		imageCorrecion.setVisibility(View.VISIBLE);
		runActivityTime();
	}
	
	/**
	 * Método que se ejecuta al hacer click en el botón 
	 * con id bOpcionDos la acción que ejecuta 
	 * es desactivar el resto de los botones y llamar a 
	 * método correcto o incorrecto que se encargan de guardar 
	 * la información
	 * 
	 * @param v vista actual
	 */
	public void onOpcionDosButtonClick(View v)
	{		
		Button dos = (Button) mGUIView.findViewById(R.id.bOpcionDos);
		habilitarDeshabilitarButtons(false, true, false, false);
		respuestaSeleccionada = dos.getText().toString();
		serverURL = crearURL(dos.getText().toString());
	    //new LongOperation().execute(serverURL);
		if(respuestaCorrecta.equalsIgnoreCase((String) dos.getText())){
			correcto();
		}else {
			System.out.println("esto es incorrecto");
			incorrecto();
		}
		imageCorrecion.setVisibility(View.VISIBLE);
		runActivityTime();
	}
	
	/**
	 * Método que se ejecuta al hacer click en el botón 
	 * con id bOpcionTres la acción que ejecuta 
	 * es desactivar el resto de los botones y llamar a 
	 * método correcto o incorrecto que se encargan de guardar 
	 * la información
	 * 
	 * @param v vista actual
	 */
	public void onOpcionTresButtonClick(View v)
	{		
		Button tres = (Button) mGUIView.findViewById(R.id.bOpcionTres);
		habilitarDeshabilitarButtons(false, false, true, false);
		respuestaSeleccionada = tres.getText().toString();
		serverURL = crearURL(tres.getText().toString());
	    //new LongOperation().execute(serverURL);
		if(respuestaCorrecta.equalsIgnoreCase((String) tres.getText())){
			correcto();	
		}else {
			incorrecto();
		}
		imageCorrecion.setVisibility(View.VISIBLE);
		runActivityTime();
		
	}
	
	/**
	 * Método que se ejecuta al hacer click en el botón 
	 * con id bOpcionCuatro la acción que ejecuta 
	 * es desactivar el resto de los botones y llamar a 
	 * método correcto o incorrecto que se encargan de guardar 
	 * la información
	 * 
	 * @param v vista actual
	 */
	public void onOpcionCuatroButtonClick(View v)
	{
		Button cuatro = (Button) mGUIView.findViewById(R.id.bOpcionCuatro);
		habilitarDeshabilitarButtons(false, false, false, true);
		respuestaSeleccionada = cuatro.getText().toString();
		serverURL = crearURL(cuatro.getText().toString());
	    //new LongOperation().execute(serverURL);
		if(respuestaCorrecta.equalsIgnoreCase((String) cuatro.getText())){
			correcto();
		}else {
			incorrecto();
		}
		imageCorrecion.setVisibility(View.VISIBLE);
		runActivityTime();
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
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
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
			if (model != null)	{
				for (int i=0; i<trackingValues.size(); i++)	{
					final TrackingValues tv = trackingValues.get(i);
					if (tv.isTrackingState()) {
						System.out.println("COORDINATE SYSTEM ID "+tv.getCoordinateSystemID());
						if(tv.getCoordinateSystemID()==model.getCoordinateSystemID()){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									habilitarDeshabilitarButtons(true, true, true, true);
								}
							});
						}
						break;
					}
				}
			}
		}
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
	private void nextWordPractice(){
		Integer posicionSiguiente = Integer.parseInt(posicionPalabra);
		manager = new DataBaseManager(this);
		int cantidadPalabras = manager.obtenerCantPalabrasUsuarioActivo(nombreLeccion);
				
		if(posicionSiguiente<=cantidadPalabras){
			Intent myIntent = new Intent (this , VoicePracticeActivity.class);
			String infoEnvio = posicionSiguiente.toString()+"-"+nombreLeccion;
			myIntent.putExtra(POSICION_WORD, infoEnvio);
			startActivity(myIntent);
		}else {
			Intent myIntent = new Intent (this , FinishActivity.class);
			myIntent.putExtra(POSICION_WORD, "Has Finalizado la Practica!");
			startActivity(myIntent);
		}
		tiempoFin = AgeManager.obtenerTiempo();
		Long diferencia = AgeManager.diferenciaEntreTiempos(tiempoInicio, tiempoFin);
		
		guardarTiempoPracticePalabraUsuario(diferencia.intValue(), "word", idPalabraCorrecta);
		this.finish();
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

	/**
	 * Método que retorna una lista con números en orden 
	 * aleatorio
	 * @param cantNumeros tamaño de la lista
	 * @return lista de números
	 */
	public ArrayList<Integer> listaNumerosAleatorios(int cantNumeros){
	    int pos;
	    ArrayList< Integer > numeros = new ArrayList < Integer > ();
	   
	    for (int i = 0; i < cantNumeros ; i++) {
	      pos = (int) Math.floor(Math.random() * cantNumeros );
	      while (numeros.contains(pos)) {
	        pos = (int) Math.floor(Math.random() * cantNumeros );
	      }
	      numeros.add(pos);
	    }
		return numeros;
	} 
	
	/**
	 * Método que retorna una lista con las 
	 * palabras de la lección en orden
	 * aleatorio
	 * @return lista de palabras
	 */
	public ArrayList<String> listaPalabraAleatoria() {
		ArrayList palabras = new ArrayList<>();
		manager = new DataBaseManager(this);
		
		cursorPalabra = manager.buscarPalabraPorLeccion(Integer.parseInt(numeroLeccion));
		int	resp=cursorPalabra.getColumnIndex(manager.CN_NOMBRE_PALABRA);
		while (cursorPalabra.moveToNext()) {
			String palabra = cursorPalabra.getString(resp);
			palabras.add(palabra);
		}
		
		Collections.shuffle(palabras);
	    return palabras;
	}

	/**
	 * Método para habilitar o deshabilitar botones 
	 * de la pantalla
	 * 
	 * @param booleanUno
	 * @param booleanDos
	 * @param booleanTres
	 * @param booleanCuatro
	 */
	public void habilitarDeshabilitarButtons(boolean booleanUno, boolean booleanDos, boolean booleanTres, boolean booleanCuatro){
		
		Button uno = (Button) mGUIView.findViewById(R.id.bOpcionUno);
		Button dos = (Button) mGUIView.findViewById(R.id.bOpcionDos);
		Button tres = (Button) mGUIView.findViewById(R.id.bOpcionTres);
		Button cuatro = (Button) mGUIView.findViewById(R.id.bOpcionCuatro);
		
		int buttonGray = R.drawable.gray_button;
		int buttonStyleText = R.style.button_text_small;
		int buttonStyleTextGray = R.style.button_text_gray;
		
		if(booleanUno){
			uno.setBackgroundResource(R.drawable.purple_button);
			uno.setTextAppearance(this, buttonStyleText);
		}else {
			uno.setBackgroundResource(buttonGray);
			uno.setTextAppearance(this, buttonStyleTextGray);
		}
		if(booleanDos){
			dos.setBackgroundResource(R.drawable.blue_button);
			dos.setTextAppearance(this, buttonStyleText);
		}else {
			dos.setBackgroundResource(buttonGray);
			dos.setTextAppearance(this, buttonStyleTextGray);
		}
		if(booleanTres){
			tres.setBackgroundResource(R.drawable.red_button);
			tres.setTextAppearance(this, buttonStyleText);
		}else {
			tres.setBackgroundResource(buttonGray);
			tres.setTextAppearance(this, buttonStyleTextGray);
		}
		if(booleanCuatro){
			cuatro.setBackgroundResource(R.drawable.green_button);
			cuatro.setTextAppearance(this, buttonStyleText);
		}else {
			cuatro.setBackgroundResource(buttonGray);
			cuatro.setTextAppearance(this, buttonStyleTextGray);
		}
		uno.setEnabled(booleanUno);
		dos.setEnabled(booleanDos);
		tres.setEnabled(booleanTres);
		cuatro.setEnabled(booleanCuatro);
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
	 *Método que reproduce sonido relacionado 
	 *con que la respuesta es correcta y llama
	 *al método que guadará la respuesta correcta 
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
	public void incorrecto() 
	{
		imageCorrecion.setImageResource(R.drawable.incorrect_ic);
		playSound("tryagain");
	}
	
	/**
	 * Método para setear tiempo de espera 
	 * a la ejecución de la tarea onRestart
	 */
	public void runActivityTime()
	{
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				nextWordPractice();
			}
		}, leadTime);
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
	        		System.out.println("Content es null");
	        		if(respuestaCorrecta.equalsIgnoreCase(respuestaSeleccionada)){
	        			correcto();
	        		}else {
	        			incorrecto();
	        		}
	        		imageCorrecion.setVisibility(View.VISIBLE);
	        		runActivityTime();
	        		
	        	}else if(Content != null){
	        		System.out.println("Content es distinto de null"); 
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
			
			return URL;
			
		}
}

