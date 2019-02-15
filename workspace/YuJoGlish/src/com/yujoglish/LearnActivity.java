/*
 * @(#) LearnActivity.java  1 27/02/15
 * 
 * Copyrigth (c) 2015 Jorge Hern�ndez, Yuzmhar Guill�n
 * Caracas, Venezuela
 * Todos los derechos reservados.
 *   
 */

package com.yujoglish;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.text.WordUtils;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
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


/**
 * Clase que muestra objetos en tercera dimensi�n a partir 
 * de escanear un ID Marker de metaio, adem�s muestra el nombre
 * del objeto a escanear y reproduce el nombre del objeto.
 * 
 * @author Yuzmhar Guill�n
 * @version 1, 27/02/2015 
 */
public class LearnActivity extends ARViewActivity {
	
	/*
	 * Ruta y Nombre de la actividad a la cual
	 * se le enviar� la informaci�n
	 */
	final static String POSICION_lEARN = "com.yujoglish.LearnActivity"; 
	
	/*
	 * Modelo que se representar� en tercera dimensi�n 
	 */
	private IGeometry model = null;
	
	/*
	 * Metaio SDK callback handler
	 */
	private MetaioSDKCallbackHandler mCallbackHandler;
	
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
	 * Nombre de la lecci�n 
	 */
	private String nombreLeccion = null;
	
	/*
	 * N�mero de lecci�n 
	 */
	private String numeroLeccion = null;
	
	/*
	 * Nombre de palabra actual
	 */
	private String nombrePalabra = null;
	
	/*
	 * Identificador de palabra actual
	 */
	private Integer idPalabra;
	
	/*
	 * Manejador de Base de datos
	 */
	DataBaseManager manager;
		
	/** 
	 * Metodo que se llama cuando se cre� por primera vez la actividad.
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
		setContentView(R.layout.activity_learning);
				
		mCallbackHandler = new MetaioSDKCallbackHandler();
		inicializarVariables();
		tiempoInicio = AgeManager.obtenerTiempo();			
	}
	
	/**
	 * Metodo para inicializar variables a utilizar
	 */
	private void inicializarVariables() 
	{
		String infoRecibidaMenu = null;
		String infoRecibidaLearn = null;
		String[] partes;
		Palabra palabra;
		
		//objeto para acceder a la bd
		manager = new DataBaseManager(this);
		
		//TextView al que se le setear� la palabra seleccionada actualmente
		TextView palabraTv = (TextView) mGUIView.findViewById(R.id.tVWord);
		
		//Mensaje obtenido de la pantalla anterior	
		Intent mensaje = getIntent();
		infoRecibidaMenu = mensaje.getStringExtra(MenuActivity.LEVEL_INFO);
		infoRecibidaLearn = mensaje.getStringExtra(LearnActivity.POSICION_lEARN);
						
		if (infoRecibidaMenu != null){
			partes = infoRecibidaMenu.split("-");
		}
		else{
			partes = infoRecibidaLearn.split("-");
		}
		
		//se obtiene la posici�n de la palabra actual
		posicionPalabra = partes[0]; 
		
		//se obtiene la lecci�n actual
		nombreLeccion = partes[1]; 
		
		//se obtine el n�mnero de la lecci�n a partir del nombre de la lecci�n 
		numeroLeccion = manager.buscarLeccionPorNombre(nombreLeccion);
		
		/*
		 * se obtiene la palabra actual, la cual es buscada 
		 * a partir de la posici�n y el n�mero de la lecci�n
		 */
		palabra = manager.buscarPalabraPorPosicionLeccion(posicionPalabra, numeroLeccion);
		
		//se obtiene el nombre de la palabra
		nombrePalabra = palabra.getNombre();
		
		//se obtiene el id de la palabra
		idPalabra = palabra.getIdPalabra();
		
		//se setea el nombre de la palabra al textView creado anteriormente
		String wordStr = WordUtils.capitalizeFully(nombrePalabra);
		palabraTv.setText(wordStr);		
	}
	
	/**
	 *  M�todo que realiza la limpieza final, 
	 * antes de que se destruya la actividad.
	 * 
	 * @see com.metaio.sdk.ARViewActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		this.finish();
	}
	
	/**
	 * M�todo que utiliza el MetaioSDK para obtener el
	 * Layout de la actividad
	 * 
	 * @see com.metaio.sdk.ARViewActivity#getGUILayout()
	 */
	@Override
	protected int getGUILayout() 
	{
		return R.layout.activity_learning; 
	}
	
	/**
	 * M�todo que se ejecuta al hacer click, en el bot�n exit
	 * @param v vista actual
	 */
	public void onExitButtonClick(View v)
	{
		tiempoFin = AgeManager.obtenerTiempo();		
		Long diferencia = AgeManager.diferenciaEntreTiempos(tiempoInicio, tiempoFin);
		
		guardarTiempoLearnPalabraUsuario(diferencia.intValue());
		
		this.finish();
	}
	
	/**
	 * M�todo encargado de cargar el contenido de MetaioSDK
	 *  por ejemplo, datos de seguimiento,Geometr�as etc.
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
			String rutaObj = nombreLeccion.toLowerCase()+"/"+nombrePalabra;
			
			int cantidadPalabras = manager.obtenerCantPalabrasUsuarioActivo(nombreLeccion);
			/*
			 * obteniendo data del archivo xml que contiene los targets a usar			
			 */
			if(cantidadPalabras <= 6){
				System.out.println("cantidad de palabras es menor o igual a 6");
				trackingConfigFile = AssetsManager.getAssetPath(getApplicationContext(), rutaXml+".xml");
			} else if (cantidadPalabras > 6){
				System.out.println("cantidad de palabras es mayor a 6");
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
				
				//creando la geometr�a
				model = metaioSDK.createGeometry(modelPath);
								MetaioDebug.log(Log.INFO, "model loaded " + modelPath);
				try	{
					MetaioDebug.log(Log.INFO, "nombre del modelo " + model.getName());
				}catch (Exception e) {
					MetaioDebug.log(Log.ERROR, "error obteniendo nombre del modelo");
				}
				
				//se asigna el target con el que se mostrar� el modelo 
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
	 * Clase implementada para el uso y modificaci�n del m�todo onTrackingEvent
	 * lo que permiti� la asociaci�n de un modelo con un target. 
	 * 
	 * @author Yuzmhar Guill�n
	 */
	final private class MetaioSDKCallbackHandler extends IMetaioSDKCallback 
	{
		/**
		 * Muestra GUI despu�s de que el SDK este listo
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
		 * M�todo para detectar los targets
		 * si detectamos cualquier objetivo, ligamos la geometr�a cargado a este objetivo
		 * 
		 * @see com.metaio.sdk.jni.IMetaioSDKCallback#onTrackingEvent(com.metaio.sdk.jni.TrackingValuesVector)
		 */
		@Override
		public void onTrackingEvent(TrackingValuesVector trackingValues)
		{
			if(model != null){
				for (int i=0; i<trackingValues.size(); i++)	{
					final TrackingValues tv = trackingValues.get(i);
					if ((tv.isTrackingState()) 
							&& (tv.getCoordinateSystemID() == model.getCoordinateSystemID())) {
						System.out.println("COORDINATE SYSTEM ID "+tv.getCoordinateSystemID());
						break;
					}
				}
			}
		}
	}

	/**
	 * M�todo que se llama cuando la geometr�a es tocada
	 * @see com.metaio.sdk.ARViewActivity#onGeometryTouched(com.metaio.sdk.jni.IGeometry)
	 */
	@Override
	protected void onGeometryTouched(IGeometry geometry) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * M�todo que se ejecuta al hacer click en bot�n Listen 
	 * el mismo reproduce un sonido
	 * @param v vista actual
	 */
	public void onListenButtonClick(View v) {
		String rutaSonido = nombreLeccion.toLowerCase()+"/"+nombrePalabra;
		playSound(rutaSonido);
	}
	
	/**
	 * M�todo que se ejecuta al hacer click en bot�n next 
	 * el mismo me enviar� a la pr�xima pantalla Learn
	 * @param v vista actual
	 */
	public void onNextLearnButtonClick(View v) 
	{
		ImageButton next = (ImageButton) mGUIView.findViewById(R.id.IBNextLearn);
		next.setBackgroundResource(R.drawable.grey_bg_oval);
		next.setEnabled(false);
		next.setClickable(false); 
		nextLearn();
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
	 *  M�todo para enviar informaci�n a siguiente actividad
	 *  acerca de que objeto debe mostrar y finaliza la pantalla actual
	 */
	private void nextLearn(){
		
		Integer p = Integer.parseInt(posicionPalabra)+1;
		String infoEnvio = null;
		
		//obtengo la cantidad de palabras que puede visualizar el usuario Activo
		int cantidadPalabras = manager.obtenerCantPalabrasUsuarioActivo(nombreLeccion);
		if(p<=cantidadPalabras){
			Intent myIntent = new Intent (this , LearnActivity.class);
			infoEnvio = p.toString()+"-"+nombreLeccion;
			myIntent.putExtra(POSICION_lEARN, infoEnvio);
			startActivity(myIntent);
		}
		else{
			//Intent myIntent = new Intent (this , LessonActivity.class);
			//startActivity(myIntent);
			
			Intent myIntent = new Intent (this , FinishActivity.class);
			myIntent.putExtra(POSICION_lEARN, "Has Finalizado La Lecci�n");
			startActivity(myIntent);
			
			
		}
		tiempoFin = AgeManager.obtenerTiempo();
		Long diferencia = AgeManager.diferenciaEntreTiempos(tiempoInicio, tiempoFin);
		
		/*if (diferencia > 60){
			int min = (int) (diferencia/60);
			int seg = (int) (diferencia - (min * 60))/100;
			float b= min+seg;
			System.out.println("min "+min + " seg "+seg+" b "+b);
		}*/
		guardarTiempoLearnPalabraUsuario(diferencia.intValue());
		this.finish();
	}
	
	/**
	 * M�todo para guardar tiempo en que estuvo activa la actividad
	 * @param tiempo Tiempo que estuvo activa la actividad
	 */
	private void guardarTiempoLearnPalabraUsuario(Integer tiempo)
	{	
		/*
		 * Tiempo acumulado en que ha aprendido una palabra 
		 */
		Integer tiempoLearn = null;
		
		/*
		 * Identificador de la tabla Usuario_Palabra
		 */
		Integer idUsuarioPalabra = null;
		
		/*
		 * Cursor usuariActivo
		 */
		Cursor cursorUsuarioActivo = null;
		
		/*
		 * Identificador de usuario activo
		 */
		String idUsuarioActivo = null;
		
		cursorUsuarioActivo = manager.cargarCursorUsuarioActivo();	
		if (cursorUsuarioActivo.moveToNext()){ 
			idUsuarioActivo = cursorUsuarioActivo.getString(cursorUsuarioActivo.getColumnIndex(manager.CN_ID_USUARIO));
		}
		
		Cursor cusorPalabraUsuario = manager.obtenerUsuarioPalabraPorTipo(idUsuarioActivo, idPalabra.toString(), "word");
		if (cusorPalabraUsuario.moveToNext()) {
			tiempoLearn = cusorPalabraUsuario.getInt(cusorPalabraUsuario.getColumnIndex(manager.CN_TIEMPO_LEARN))+tiempo;
			idUsuarioPalabra = cusorPalabraUsuario.getInt(cusorPalabraUsuario.getColumnIndex(manager.CN_ID_USUARIO_PALABRA));
			
			manager.actualizarTiempoLearnUsuarioPalabra(idUsuarioPalabra.toString(), tiempoLearn.toString());
		}else{
			manager.insertarUsuarioPalabra(idUsuarioActivo, idPalabra.toString(), "0", "0", "word", tiempo.toString(),"0");
		}
	}
	
	/**
	 * M�todo encargado de reproducir sonidos
	 * @param ruta ruta en donde se encuentra el archivo .mp3 a reproducir
	 */
	/**
	 * M�todo encargado de reproducir sonidos
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

	
}