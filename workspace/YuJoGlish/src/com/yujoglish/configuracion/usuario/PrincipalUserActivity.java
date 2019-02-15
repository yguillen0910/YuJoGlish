/*
 * @(#) PrincipalUserActivity.java  1 01/03/15
 * 
 * Copyrigth (c) 2015 Jorge Hernández, Yuzmhar Guillén
 * Caracas, Venezuela
 * Todos los derechos reservados.
 *   
 */
package com.yujoglish.configuracion.usuario;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yujoglish.AcercaDeActivity;
import com.yujoglish.FTPActivity;
import com.yujoglish.FinishActivity;
import com.yujoglish.LearnActivity;
import com.yujoglish.LessonActivity;
import com.yujoglish.LevelActivity;
import com.yujoglish.R;
import com.yujoglish.customadapter.CustomItemActivity;
import com.yujoglish.dbhelper.DataBaseManager;
import com.yujoglish.model.Palabra;
import com.yujoglish.util.Util;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Clase que muestra opciones disponibles en la configuración
 * de usuarios
 *  
 * @author Jorge Hernández
 * @version 1, 01/03/2015 
 */
public class PrincipalUserActivity extends Activity implements OnClickListener {
	
	/*
	 * Ruta y Nombre de la actividad a la cual
	 * se le enviará la información
	 */
	public final static String INFO_PRINCIPAL = "com.yujoglish.configuracion.usuario.PrincipalUserActivity"; 
	
	/*
	 * ActionBar
	 */
	private ActionBar actionBar;
	private ListView listViewUsuarioActivo;
	private ArrayList<CustomItemActivity> items = new ArrayList<CustomItemActivity>();
	private ImageButton buttonModificarUsuario;
	private ImageButton buttonAgregarUsuario;
	private ImageButton buttonSeleccionarUsuario;
	private ImageButton buttonDescargarPalabra;
	private DataBaseManager managerDB;
	private Cursor cursor;
	private Intent nuevaActividad;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal_user);
		
		actionBar = getActionBar();
		
		actionBar.setTitle("Configurar Usuario");
		actionBar.setDisplayUseLogoEnabled(false);
		
		listViewUsuarioActivo = (ListView) findViewById(R.id.listView_usuarioActivo);
		
		
		buttonModificarUsuario = (ImageButton) findViewById(R.id.button_modificar_usuario);
		buttonAgregarUsuario = (ImageButton) findViewById(R.id.button_agregar_usuario);
		buttonSeleccionarUsuario = (ImageButton) findViewById(R.id.button_cambiar_usuario);
		buttonDescargarPalabra = (ImageButton) findViewById(R.id.button_descargar_palabra);
				
		
		managerDB = new DataBaseManager(this);
		cursor = managerDB.cargarCursorUsuarioActivo();
		Integer progressBar[]; 
		
		if(cursor.moveToFirst()){
			String nombre=cursor.getString(1);
			String edad = AgeManager.edad(cursor.getString(2));
			progressBar = Util.progressbarUsuario(cursor.getString(0), this);
			
			items.add(new CustomItemActivity(cursor.getString(0),nombre," Edad: "+edad+" años", progressBar[1], progressBar[0],cursor.getString(4)));
			ArrayAdapter<CustomItemActivity> adapter = new MyListAdapter();
			listViewUsuarioActivo.setAdapter(adapter);
			
		}
		else {
			
			managerDB.insertarUsuario("Mi Prueba", "27/01/2010","M");
		}
		
		buttonModificarUsuario.setOnClickListener(this);
		buttonAgregarUsuario.setOnClickListener(this);
		buttonSeleccionarUsuario.setOnClickListener(this);
		buttonDescargarPalabra.setOnClickListener(this);
		
	}
	
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.button_modificar_usuario:
			
			nuevaActividad =  new Intent(v.getContext(),ModifyUserActivity.class);
			startActivity(nuevaActividad);
			
			break;
			
		case R.id.button_agregar_usuario:
			
			nuevaActividad =  new Intent(v.getContext(),AddUserActivity.class);
			startActivity(nuevaActividad);
			
			break;
			
		case R.id.button_cambiar_usuario:
			
			nuevaActividad =  new Intent(v.getContext(),SelectUserActivity.class);
			startActivity(nuevaActividad);
			
			break;
		
		case R.id.button_descargar_palabra:
			
			int idUltimaPalabra = managerDB.obtenerIdUltimaPalabra();
			buttonDescargarPalabra.setBackgroundResource(R.drawable.gray_button);
			buttonDescargarPalabra.setEnabled(false);
			System.out.println("ID Ultima Palabra "+idUltimaPalabra);
			String serverURL = "http://"+Util.IP_SERVIDOR+":8080/YuJoGlishWebSite/words/sync/72.json";
			new LongOperation().execute(serverURL);

		default:
			break;
		}
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private class MyListAdapter extends ArrayAdapter<CustomItemActivity>{

		public MyListAdapter() {
			super(PrincipalUserActivity.this, R.layout.activity_custom_items, items);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View view = convertView;
			
			if(view == null){
				view = getLayoutInflater().inflate(R.layout.activity_custom_items, null);
			}
			
			CustomItemActivity item = items.get(position);
			
			TextView userName = (TextView) view.findViewById(R.id.tVUserName);
			userName.setText(item.getNombre());
			
			TextView userAge = (TextView) view.findViewById(R.id.tVUserAge);
			userAge.setText(item.getEdad());
			
			TextView userWords = (TextView) view.findViewById(R.id.tVUserWords);
			userWords.setText(item.getPalabras());
			
			ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.pBUser);
			progressBar.setMax(item.getTotalPalabra());
			progressBar.setProgress(item.getCantidadPalabra());
			
			ImageView image = (ImageView) view.findViewById(R.id.iVUserIconItem);
			if(item.getSexo().matches("F")) image.setImageResource(R.drawable.female_ic);
			if(item.getSexo().matches("M")) image.setImageResource(R.drawable.male_ic);
			else image.setVisibility(0);
							
			return view;
		}
		
		
	}
	
		
	// Class with extends AsyncTask class
    private class LongOperation  extends AsyncTask<String, Void, Void> {
         
    	private String Content;
               
        // Call after onPreExecute method
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
		        System.out.println("Content "+Content);
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
        	
        	if(Content != null){
	        	ArrayList<String> palabrasFTP = traducirJson(Content);
	        	if (palabrasFTP.size() != 0){
	        		mensaje("La descarga fue exitosa");
	        	}else if(palabrasFTP.size() == 0){
	        		mensaje("No hay palabras disponibles");
	        	}
	        	System.out.println("palabrasFTP "+palabrasFTP.toString());
        	}else {
        		mensaje("No se pudo conectar al servidor");
        	}
        	
        	
        	
        	//TODO llamar a descargar por FTP
        }
         
    }
    
    public ArrayList<String> traducirJson (String response){
		 String resultado = "";
		 JSONObject jsonResponse;
		 ArrayList<String> palabras = new ArrayList<String>();
		 FTPActivity ftp = new FTPActivity();
		 boolean statusMp3 = false;
		 boolean statusMtl = false;
		 boolean statusBmp = false;
		 boolean statusObj = false;
		 boolean statusXml = false;
		 
         
         try {
             jsonResponse = new JSONObject(response);
                                   
             JSONArray jsonMainNode = jsonResponse.optJSONArray("words");
             
             int lengthJsonArr = jsonMainNode.length();  

              for(int i=0; i < lengthJsonArr; i++) 
              {
                  
                  JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                   
                 
                  String palabra       = jsonChildNode.optString("Word").toString();
                  String id     = jsonChildNode.optJSONObject("Word").optString("id").toString();
                  String nombre     = jsonChildNode.optJSONObject("Word").optString("nombre").toString();
                  String leccion     = jsonChildNode.optJSONObject("Word").optString("leccion").toString();
                  String posicion     = jsonChildNode.optJSONObject("Word").optString("posicion").toString();
                  
                  resultado += " Palabra 		    : "+ palabra +" \n "
              		 	+ "Id 		: "+ id +" \n "
              		 	+ "nombre 		: "+ nombre +" \n "
              		 	+ "leccion 		: "+ leccion +" \n "
              		 	+ "posicion 		: "+ posicion +" \n "
                         
                           +"--------------------------------------------------\n";
                  System.out.println("Resultado "+resultado);
                 
                  
                  String nombreLeccion = managerDB.buscarNombreLeccionPorId(leccion);
                  System.out.println("Leccion "+nombreLeccion);
                  
                  boolean existId = managerDB.existeIDPalabra(Integer.parseInt(id));
                  boolean existPalabra = managerDB.existeNombrePalabra(nombre);
                  
                  if ( existId == true ){
                	  
                	  if ( existPalabra == false) {
                		  
                		  statusMp3 = ftp.downloadStart(nombre+".mp3", nombreLeccion);
                          statusMtl = ftp.downloadStart(nombre+".mtl", nombreLeccion);
                          statusBmp = ftp.downloadStart(nombre+".bmp", nombreLeccion);
                          statusObj = ftp.downloadStart(nombre+".obj", nombreLeccion);
                          statusXml = ftp.downloadStart(nombreLeccion+".xml", nombreLeccion);
                          
                          System.out.println("Status : mp3"+statusMp3+" mtl "+statusMtl+ " bmp "+statusBmp +" obj "+statusObj+" xml "+statusXml);
                          
                          if (statusMp3 == true && statusMtl == true && statusBmp == true && statusObj == true && statusXml == true){
                        	 
                              managerDB.editarNombrePalabra(Integer.parseInt(id), nombre);
                              palabras.add(nombre);
                          } 
                		  
                	  }
                  } else if ( (existId == false) && (existPalabra == false)){
                	  
                	  statusMp3 = ftp.downloadStart(nombre+".mp3", nombreLeccion);
                      statusMtl = ftp.downloadStart(nombre+".mtl", nombreLeccion);
                      statusBmp = ftp.downloadStart(nombre+".bmp", nombreLeccion);
                      statusObj = ftp.downloadStart(nombre+".obj", nombreLeccion);
                      statusXml = ftp.downloadStart(nombreLeccion+".xml", nombreLeccion);
                      
                      System.out.println("Status : mp3"+statusMp3+" mtl "+statusMtl+ " bmp "+statusBmp +" obj "+statusObj+" xml "+statusXml);
                      
                      if (statusMp3 == true && statusMtl == true && statusBmp == true && statusObj == true && statusXml == true){
                    	  Palabra palabraNueva = new Palabra(nombre,Integer.parseInt(posicion), Integer.parseInt(leccion));
                          managerDB.insertarPalabra(palabraNueva);
                          palabras.add(nombre);
                      } 
                	  
                  }
    
                  
             }
              return palabras;     
               
          } catch (JSONException e) {
   
              e.printStackTrace();
          }

		return palabras;
	 }
    
    public void mensaje(String infoEnvio){
    	
    	nuevaActividad = new Intent (this , FinishActivity.class);
		
    	nuevaActividad.putExtra(INFO_PRINCIPAL, infoEnvio);
		startActivity(nuevaActividad);
    	
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main_actions, menu);

		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * On selecting action bar icons
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case R.id.action_menu:
			Intent level = new Intent(this, LevelActivity.class);
			startActivity(level);
			return true;
		case R.id.action_acerca:
			Intent acerca = new Intent(this, AcercaDeActivity.class);
			startActivity(acerca);
			return true;
		case R.id.action_leccion:
			Intent lesson = new Intent(this, LessonActivity.class);
			startActivity(lesson);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
