/*
 * @(#) MenuActivity.java  1 27/02/15
 * 
 * Copyrigth (c) 2015 Jorge Hernández, Yuzmhar Guillén
 * Caracas, Venezuela
 * Todos los derechos reservados.
 *   
 */
package com.yujoglish;

import com.yujoglish.stadistics.StadisticsMainActivity;

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
 * Clase para mostrar las opciones disponibles en una 
 * lección como: learn, practice, statistic ó test
 *   
 * @author Yuzmhar Guillén
 * @version 1, 27/02/2015 
 */
public class MenuActivity extends Activity  {
	
	/*
	 * ActionBar
	 */
	ActionBar actionBar;
	
	/*
	 * Ruta y Nombre de la actividad a la cual
	 * se le enviará la información
	 */
	final static String LEVEL_INFO = "com.yujoglish.LearnActivity";
	
	/*
	 * Ruta y Nombre de la actividad a la cual
	 * se le enviará la información
	 */
	final static String LEVEL_INFO_TEST = "com.yujoglish.TestActivity";
	
	/*
	 * Ruta y Nombre de la actividad a la cual
	 * se le enviará la información
	 */
	final static String LEVEL_INFO_PRACTICE = "com.yujoglish.WordPracticeActivity";
	
	/*
	 * Ruta y Nombre de la actividad de Estadisticas a la cual
	 * se le enviará la información
	 */
	public final static String LEVEL_INFO_STADISTICS = "com.yujoglish.StadisticsMainActivity";
	
	/*
	 * Nombre de la Leccion seleccionada 
	 */
	private String nombreLeccion;
	
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
		setContentView(R.layout.activity_menu);
		
		actionBar = getActionBar();

		// Hide the action bar title
		actionBar.setDisplayShowTitleEnabled(true);
		/*
		 * TextView al que se le seteará el nombre de la leccion seleccionada
		 */	
		TextView lessionName = (TextView) findViewById(R.id.textViewNameLeccion);
		Intent mensaje = getIntent();
		nombreLeccion = mensaje.getStringExtra(LessonActivity.LESSON_INFO);
		lessionName.setText(nombreLeccion);
	}
	
	/**
	 * Método que se ejecuta al hacer click en el botón 
	 * con id test_image_button, el mismo se encarga de
	 * enviar información del nombre de la lección 
	 * seleccionada y la posición de la palabra por la 
	 * que empezará el recorrido 
	 * 
	 * @param v vista actual
	 */
	public void onTestButtonClick(View v) {
		Intent myIntent = new Intent (v.getContext() , TestActivity.class);
		
		/*
		 * Información a enviar :
		 * el "1" representa la posicion de la palabra a mostrar
		 * y nombreLeccion el nombre de la lección seleccionada
		 */
		String infoEnviar = "1-"+nombreLeccion; 
		myIntent.putExtra(LEVEL_INFO_TEST, infoEnviar);
		startActivity(myIntent);
		
		this.finish();
	}
	
	/**
	 * Método que se ejecuta al hacer click en el botón 
	 * con id learn_image_button, el mismo se encarga de
	 * enviar información del nombre de la lección 
	 * seleccionada y la posición de la palabra por la 
	 * que empezará el recorrido 
	 * 
	 * @param v vista actual
	 */
	public void onLearnButtonClick(View v) {
		Intent myIntent = new Intent (this , LearnActivity.class);
		
		/*
		 * Información a enviar :
		 * el "1" representa la posicion de la palabra a mostrar
		 * y nombreLeccion el nombre de la lección seleccionada
		 */
		String infoEnviar = "1-"+nombreLeccion; 
		myIntent.putExtra(LEVEL_INFO, infoEnviar);
		startActivity(myIntent);
		
		this.finish();
	}
	
	/**
	 * Método que se ejecuta al hacer click en el botón 
	 * con id practice_image_button, el mismo se encarga de
	 * enviar información de el nombre de la lección 
	 * seleccionada y la posición de la palabra por la 
	 * que empezará el recorrido 
	 * 
	 * @param v vista actual
	 */
	public void onPracticeButtonClick(View v) {
		Intent myIntent = new Intent (this , WordPracticeActivity.class);
		
		/*
		 * Información a enviar :
		 * el "1" representa la posicion de la palabra a mostrar
		 * y nombreLeccion el nombre de la lección seleccionada
		 */
		String infoEnviar = "1-"+nombreLeccion; 
		myIntent.putExtra(LEVEL_INFO_PRACTICE, infoEnviar);
		startActivity(myIntent);
		
		this.finish();
	}
	
	/**
	 * Método que se ejecuta al hacer click en el botón 
	 * con id statictis_image_button, el mismo se encarga de
	 * enviar información de el nombre de la lección 
	 * seleccionada y la posición de la palabra por la 
	 * que empezará el recorrido 
	 * 
	 * @param v vista actual
	 */
	public void onStatisticButtonClick(View v){
		
		Intent myIntent = new Intent (v.getContext() , StadisticsMainActivity.class);
		myIntent.putExtra(LEVEL_INFO_STADISTICS, nombreLeccion);
		startActivity(myIntent);
		
		this.finish();
		
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
