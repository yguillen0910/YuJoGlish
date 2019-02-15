/*
 * @(#) MainActivity.java  1 27/02/15
 * 
 * Copyrigth (c) 2015 Jorge Hernández, Yuzmhar Guillén
 * Caracas, Venezuela
 * Todos los derechos reservados.
 *   
 */

package com.yujoglish;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import com.metaio.sdk.MetaioDebug;
import com.metaio.tools.io.AssetsManager;
import com.yujoglish.configuracion.usuario.SelectUserActivity;
import com.yujoglish.dbhelper.DataBaseManager;
import com.yujoglish.model.Leccion;
import com.yujoglish.model.Nivel;
import com.yujoglish.model.Palabra;

/**
 * Clase que se encarga de Insertar en la base de datos 
 * data necesaria para el funcionamiento de la aplicación * 
 * @author Yuzmhar Guillén
 * @version 1, 27/02/2015 
 */
public class AcercaDeActivity extends Activity {
	
	/*
	 * Tiempo de espera
	 */
	private int leadTime = 800;
   
		/** 
	 * Metodo que se llama cuando se creó por primera vez la actividad.
	 * Se crean la vistas
	 * Se inicializan las variables
	 * Se Inserta data en la BD
	 * 
	 * @see com.metaio.sdk.ARViewActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_acerca_de);
	
		
	}
	
	public void menu() {
		Intent select = new Intent(this,LevelActivity.class);
		startActivity(select);
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
				menu();
			}
		}, leadTime);
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
