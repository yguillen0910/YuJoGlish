/*
 * @(#) TestActivity.java  1 27/02/15
 * 
 * Copyrigth (c) 2015 Jorge Hernández, Yuzmhar Guillén
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
 * Clase que se encarga de mostrar opción de Test de voz (Test Voice)
 * o Test de palabra (Test Word)
 *  
 * @author Yuzmhar Guillén
 * @version 1, 27/02/2015 
 */
public class TestActivity extends Activity{
	
	/*
	 * ActionBar
	 */
	ActionBar actionBar;
	
	/*
	 * Ruta y Nombre de la actividad a la cual
	 * se le enviará la información
	 */
	final static String INFO_WORD = "com.yujoglish.WordTestActivity";
	
	/*
	 * Ruta y Nombre de la actividad a la cual
	 * se le enviará la información
	 */
	final static String INFO_VOICE = "com.yujoglish.VoiceTestActivity";
	
	/*
	 * Información recibida de la pantalla Menu (MenuActivity)
	 */
	public String infoRecibidaMenu ;
	
	/**
	 * Método que se llama cuando se creó por primera vez la actividad.
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
	 * Método que se ejecuta al hacer click en el botón 
	 * con id voice_test_image_button, el mismo se encarga de
	 * enviar información del nombre de la lección 
	 * seleccionada y la posición de la palabra por la 
	 * que empezará el recorrido 
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
	 * Método que se ejecuta al hacer click en el botón 
	 * con id word_test_image_button, el mismo se encarga de
	 * enviar información de el nombre de la lección 
	 * seleccionada y la posición de la palabra por la 
	 * que empezará el recorrido 
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
