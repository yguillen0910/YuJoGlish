/*
 * @(#) LessonActivity.java  1 27/02/15
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
import android.view.View;
import android.widget.TextView;

/**
 * Clase que se encarga de mostrar las lecciones que se encuentran en la aplicaci�n
 * y envia a MenuActivity la lecci�n seleccionada por el usuario
 *  
 * @author Yuzmhar Guill�n
 * @version 1, 27/02/2015 
 */
public class LessonActivity extends Activity{
	
	/*
	 * ActionBar
	 */
	private ActionBar actionBar;
	
	/*
	 * Ruta y Nombre de la actividad a la cual
	 * se le enviar� la informaci�n
	 */
	final static String LESSON_INFO = "com.yujoglish.MenuActivity"; 
	
	/**
	 * Metodo que se llama cuando se cre� por primera vez la actividad.
	 * Se crean la vistas
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lesson);
		actionBar = getActionBar();

		// Hide the action bar title
		actionBar.setDisplayShowTitleEnabled(true);
	}

	/**
	 * M�todo que se ejecuta al hacer click en el bot�n 
	 * con id family_image_button la acci�n que ejecuta 
	 * es enviar a la actividad MenuActivity un mensaje 
	 * que contiene la lecci�n seleccionada
	 * 
	 * @param v vista actual
	 */
	public void onLesson1ButtonClick(View v) {
		Intent myIntent = new Intent (this , MenuActivity.class);
		myIntent.putExtra(LESSON_INFO, "Family");
		startActivity(myIntent);
		finish();
	}
	
	/**
	 * M�todo que se ejecuta al hacer click en el bot�n 
	 * con id colors_image_button la acci�n que ejecuta 
	 * es enviar a la actividad MenuActivity un mensaje 
	 * que contiene la lecci�n seleccionada
	 * 
	 * @param v vista actual
	 */
	public void onLesson2ButtonClick(View v) {
		Intent myIntent = new Intent (this , MenuActivity.class);
		myIntent.putExtra(LESSON_INFO, "Colors");
		startActivity(myIntent);
		finish();
	}
	
	/**
	 * M�todo que se ejecuta al hacer click en el bot�n 
	 * con id shapes_image_button la acci�n que ejecuta 
	 * es enviar a la actividad MenuActivity un mensaje 
	 * que contiene la lecci�n seleccionada
	 * 
	 * @param v vista actual
	 */
	public void onLesson3ButtonClick(View v) {
		Intent myIntent = new Intent (this , MenuActivity.class);
		myIntent.putExtra(LESSON_INFO, "Shapes");
		startActivity(myIntent);
		finish();
	}
	
	/**
	 * M�todo que se ejecuta al hacer click en el bot�n 
	 * con id numbers_image_button la acci�n que ejecuta 
	 * es enviar a la actividad MenuActivity un mensaje 
	 * que contiene la lecci�n seleccionada
	 * 
	 * @param v vista actual
	 */
	public void onLesson4ButtonClick(View v) {
		Intent myIntent = new Intent (this , MenuActivity.class);
		myIntent.putExtra(LESSON_INFO, "Numbers");
		startActivity(myIntent);
		finish();
	}
	
	/**
	 * M�todo que se ejecuta al hacer click en el bot�n 
	 * con id animals_image_button la acci�n que ejecuta 
	 * es enviar a la actividad MenuActivity un mensaje 
	 * que contiene la lecci�n seleccionada
	 * 
	 * @param v vista actual
	 */
	public void onLesson5ButtonClick(View v) {
		Intent myIntent = new Intent (this , MenuActivity.class);
		myIntent.putExtra(LESSON_INFO, "Animals");
		startActivity(myIntent);
		finish();
	}
	
	/**
	 * M�todo que se ejecuta al hacer click en el bot�n 
	 * con id fruits_image_button la acci�n que ejecuta 
	 * es enviar a la actividad MenuActivity un mensaje 
	 * que contiene la lecci�n seleccionada
	 * 
	 * @param v vista actual
	 */
	public void onLesson6ButtonClick(View v) {
		Intent myIntent = new Intent (this , MenuActivity.class);
		myIntent.putExtra(LESSON_INFO, "Fruits");
		startActivity(myIntent);
		finish();
	}
	
	/**
	 * M�todo que se ejecuta al hacer click en el bot�n 
	 * con id face_image_button la acci�n que ejecuta 
	 * es enviar a la actividad MenuActivity un mensaje 
	 * que contiene la lecci�n seleccionada
	 * 
	 * @param v vista actual
	 */
	public void onLesson7ButtonClick(View v) {
		Intent myIntent = new Intent (this , MenuActivity.class);
		myIntent.putExtra(LESSON_INFO, "Face");
		startActivity(myIntent);
		finish();
	}
	
	/**
	 * M�todo que se ejecuta al hacer click en el bot�n 
	 * con id body_image_button la acci�n que ejecuta 
	 * es enviar a la actividad MenuActivity un mensaje 
	 * que contiene la lecci�n seleccionada
	 * 
	 * @param v vista actual
	 */
	public void onLesson8ButtonClick(View v) {
		Intent myIntent = new Intent (this , MenuActivity.class);
		myIntent.putExtra(LESSON_INFO, "Body");
		startActivity(myIntent);
		finish();
	}
	
	/**
	 * M�todo que se ejecuta al hacer click en el bot�n 
	 * con id clothes_image_button la acci�n que ejecuta 
	 * es enviar a la actividad MenuActivity un mensaje 
	 * que contiene la lecci�n seleccionada
	 * 
	 * @param v vista actual
	 */
	public void onLesson9ButtonClick(View v) {
		Intent myIntent = new Intent (this , MenuActivity.class);
		myIntent.putExtra(LESSON_INFO, "Clothes");
		startActivity(myIntent);
		finish();
	}
	
	/**
	 * M�todo que se ejecuta al hacer click en el bot�n 
	 * con id toys_image_button la acci�n que ejecuta 
	 * es enviar a la actividad MenuActivity un mensaje 
	 * que contiene la lecci�n seleccionada
	 * 
	 * @param v vista actual
	 */
	public void onLesson10ButtonClick(View v) {
		Intent myIntent = new Intent (this , MenuActivity.class);
		myIntent.putExtra(LESSON_INFO, "Toys");
		startActivity(myIntent);
		finish();
	}
	
	/**
	 * M�todo que se ejecuta al hacer click en el bot�n 
	 * con id birthday_image_button la acci�n que ejecuta 
	 * es enviar a la actividad MenuActivity un mensaje 
	 * que contiene la lecci�n seleccionada
	 * 
	 * @param v vista actual
	 */
	public void onLesson11ButtonClick(View v) {
		Intent myIntent = new Intent (this , MenuActivity.class);
		myIntent.putExtra(LESSON_INFO, "Birthday");
		startActivity(myIntent);
		finish();
	}
	
	/**
	 * M�todo que se ejecuta al hacer click en el bot�n 
	 * con id classroom_image_button la acci�n que ejecuta 
	 * es enviar a la actividad MenuActivity un mensaje 
	 * que contiene la lecci�n seleccionada
	 * 
	 * @param v vista actual
	 */
	public void onLesson12ButtonClick(View v) {
		Intent myIntent = new Intent (this , MenuActivity.class);
		myIntent.putExtra(LESSON_INFO, "Classroom");
		startActivity(myIntent);
		finish();
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
