package com.yujoglish.webservices;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONException;
import org.json.JSONObject;

import com.yujoglish.R;
import com.yujoglish.model.Correction;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CorrectionActivity extends Activity {
	
	public String respuestaWS = "";
	public Correction correccion =  new Correction();

	
   //Called when the activity is first created. 
    @Override
    public void onCreate(Bundle savedInstanceState) {
     
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rest_ful_webservice);  
        
        final Button GetServerData = (Button) findViewById(R.id.GetServerData);
         
        GetServerData.setOnClickListener(new OnClickListener() {
            
			@Override
			public void onClick(View arg0) {
				
				String f = corregir("4", "prueba,one", "1");
					        
			}
        });    
         
    }
    
    public String corregir(String idleccion, String palabras , String posicion){
    	
    	String respuesta = "";
    	    	   	
    	// WebServer Request URL
		String serverURL = "http://192.168.1.103:8080/YuJoGlishWebSite/corrections/response.json?id="+idleccion+"&palabras="+palabras+"&posicion="+posicion;
		
		// Use AsyncTask execute Method To Prevent ANR Problem
        new LongOperation().execute(serverURL);
                
        respuesta = correccion.getRepuesta();
        
        System.out.println("corregir "+ respuesta);
    	
        return respuesta;
    }
          
     private class LongOperation  extends AsyncTask<String, Void, Void> {
       
        private String Content;
        private String Error = null;
        TextView uiUpdate = (TextView) findViewById(R.id.output);
        TextView jsonParsed = (TextView) findViewById(R.id.jsonParsed);
        
        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
        	
        	/************ Make Post Call To Web Server ***********/
        	BufferedReader reader=null;
            // Send data 
        	try	{
        	    // Defined URL  where to send data
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
	            } catch(Exception ex)   {
	            	Error = ex.getMessage();
	            }
	            finally
	            {
	                try
	                {
	     
	                    reader.close();
	                }
	   
	                catch(Exception ex) {}
	            }
        	
         return null;
        }
         
        protected void onPostExecute(Void unused) {
           // NOTE: You can call UI Element here.
           // Close progress dialog
           // Dialog.dismiss();
             
          if (Error != null) {
                 
              //  uiUpdate.setText("Output : "+Error);
                 
            } else {
              
            	// Show Response Json On Screen (activity)
            	uiUpdate.setText( Content );
           
            		String h = traducirJson(Content);
           
                     jsonParsed.setText( h );
                 
             }
        }
               
     }

	public String traducirJson (String response){
		
		 //****************** Start Parse Response JSON Data *************
    	String resultado = "";
        JSONObject jsonResponse;
        
        //****** Creates a new JSONObject with name/value mappings from the JSON string. ********
        try {
        	
        	jsonResponse = new JSONObject(response);
    
            resultado = jsonResponse.optJSONObject("correction").optJSONObject("Correction").optString("respuesta").toString();
      
            //****************** End Parse Response JSON Data *************    
            correccion.setRepuesta(resultado);
            
            System.out.println("GET "+correccion.getRepuesta());
             //Show Parsed Output on screen (activity)
            } catch (JSONException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
			return resultado;
     	}
     
     
	
}
