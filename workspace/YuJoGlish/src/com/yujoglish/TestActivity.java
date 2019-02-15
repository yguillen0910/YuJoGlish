/*
 * @(#) TestActivity.java  1 27/02/15
 * 
 * Copyrigth (c) 2015 Jorge Hern�ndez, Yuzmhar Guill�n
 * Caracas, Venezuela
 * Todos los derechos reservados.
 *   
 */
package com.yujoglish;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

/**
 * Clase que se encarga de mostrar opci�n de Test de voz (Test Voice)
 * o Test de palabra (Test Word)
 *  
 * @author Yuzmhar Guill�n
 * @version 1, 27/02/2015 
 */
public class TestActivity extends Activity{
	
	/*
	 * ActionBar
	 */
	ActionBar actionBar;
	
	/*
	 * Ruta y Nombre de la actividad a la cual
	 * se le enviar� la informaci�n
	 */
	final static String INFO_WORD = "com.yujoglish.WordTestActivity";
	
	/*
	 * Ruta y Nombre de la actividad a la cual
	 * se le enviar� la informaci�n
	 */
	final static String INFO_VOICE = "com.yujoglish.VoiceTestActivity";
	
	/*
	 * Informaci�n recibida de la pantalla Menu (MenuActivity)
	 */
	public String infoRecibidaMenu ;
	
	/**
	 * M�todo que se llama cuando se cre� por primera vez la actividad.
	 * Se crean la vistas
	 * Se inicializan las variables
	 *  
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		actionBar = getActionBar();

		// Hide the action bar title
		actionBar.setDisplayShowTitleEnabled(true);
		Intent mensaje = getIntent();
		infoRecibidaMenu = mensaje.getStringExtra(MenuActivity.LEVEL_INFO_TEST);
		
		TextView tvNombreLeccion = (TextView) findViewById(R.id.textViewNameLevel);
		String nombreLeccion = infoRecibidaMenu.substring(2, infoRecibidaMenu.length());
		tvNombreLeccion.setText(nombreLeccion);
	}
	
	/**
	 * M�todo que se ejecuta al hacer click en el bot�n 
	 * con id voice_test_image_button, el mismo se encarga de
	 * enviar informaci�n del nombre de la lecci�n 
	 * seleccionada y la posici�n de la palabra por la 
	 * que empezar� el recorrido 
	 * 
	 * @param v vista actual
	 */
	public void onVoiceTestButtonClick(View v)
	{
		Intent myIntent = new Intent (this , VoiceTestActivity.class);
		String infoEnviar = infoRecibidaMenu; 
		myIntent.putExtra(INFO_VOICE, infoEnviar);
		startActivity(myIntent);
	}
	
	/**
	 * M�todo que se ejecuta al hacer click en el bot�n 
	 * con id word_test_image_button, el mismo se encarga de
	 * enviar informaci�n de el nombre de la lecci�n 
	 * seleccionada y la posici�n de la palabra por la 
	 * que empezar� el recorrido 
	 * 
	 * @param v vista actual
	 */
	public void onWordTestButtonClick(View v) {
		
		Intent myIntent = new Intent (this , WordTestActivity.class);
		String infoEnviar = infoRecibidaMenu; 
		myIntent.putExtra(INFO_WORD, infoEnviar);
		startActivity(myIntent);
		
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
