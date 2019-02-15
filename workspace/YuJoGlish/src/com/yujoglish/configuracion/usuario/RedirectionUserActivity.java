/*
 * @(#) RedirectionUserActivity.java  1 01/03/15
 * 
 * Copyrigth (c) 2015 Jorge Hernández, Yuzmhar Guillén
 * Caracas, Venezuela
 * Todos los derechos reservados.
 *   
 */
package com.yujoglish.configuracion.usuario;

import java.util.Timer;
import java.util.TimerTask;
import com.yujoglish.LevelActivity;
import com.yujoglish.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Clase que muestra usuario seleccionado
 *  
 * @author Jorge Hernández
 * @version 1, 01/03/2015 
 */
public class RedirectionUserActivity extends Activity {
	
	private TextView textViewRedireccion;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_redirection_user);
		
		textViewRedireccion = (TextView) findViewById(R.id.textView_Redireccion);
		
		Bundle extras = getIntent().getExtras();
		
		
		if (extras != null) {
			textViewRedireccion.setText(extras.getString("textoInformativo"));
		}
		else
			textViewRedireccion.setText("Redireccionando...");

		int timeout = 1000; 

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				 finish();
			        Intent homepage = new Intent(RedirectionUserActivity.this,LevelActivity.class);
			        startActivity(homepage);
			}
		}, timeout);
		
		
	
	}

}
