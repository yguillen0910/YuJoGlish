/*
 * @(#) LevelActivity.java  1.0.2 11/04/15
 * 
 * Copyrigth (c) 2015 Jorge Hernández, Yuzmhar Guillén
 * Caracas, Venezuela
 * Todos los derechos reservados.
 *   
 */
package com.yujoglish;

import com.yujoglish.configuracion.usuario.PrincipalUserActivity;
import com.yujoglish.dbhelper.DataBaseManager;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
//import android.support.v7.internal.widget.ActionBarOverlayLayout.ActionBarVisibilityCallback;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * Clase que se encarga de mostrar opción de jugar
 * o ir a configuración de usuario
 *  
 * @author Yuzmhar Guillén
 * @version 1.0.2, 11/04/2015 
 */

public class LevelActivity extends Activity  {
	
	/*
	 * ActionBar
	 */
	private ActionBar actionBar;
	
	/*
	 * Manejador de Base de datos
	 */
	DataBaseManager manager;
	
	/**
	 * Metodo que se llama cuando se creó por primera vez la actividad.
	 * Se crean la vistas
	 * Se inicializan las variables
	 *
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level2);
		
		actionBar = getActionBar();

		// Hide the action bar title
		actionBar.setDisplayShowTitleEnabled(true);
		
		/*
		 * Cursor usuarioActivo
		 */
		Cursor cursorUsuarioActivo;
		
		/*
		 * Nombre de Usuario Activo
		 */
		String nombreUsuario;
		
		/*
		 * TextView al que se le seteará el
		 * usuario que se encuentra activo
		 */
		TextView usuario = (TextView) findViewById(R.id.tVNombreUsuario);
		
		manager = new DataBaseManager(this);
		cursorUsuarioActivo = manager.cargarCursorUsuarioActivo();
		int columnNombreUsuario = cursorUsuarioActivo.getColumnIndex(manager.CN_NOMBRE_USUARIO);
		
		if (cursorUsuarioActivo.moveToNext()) {
			nombreUsuario = cursorUsuarioActivo.getString(columnNombreUsuario);
			usuario.setText("Hola! "+nombreUsuario);
		}
		
		
	}
	
	/**
	 * Método que se ejecuta al hacer click en el botón 
	 * con id play_image_button
	 * 
	 * @param v vista actual
	 */
	public void onPlayButtonClick(View v)
	{
		Intent myIntent = new Intent (this , LessonActivity.class);
		startActivity(myIntent);
	}
	
	/**
	 * Método que se ejecuta al hacer click en el botón 
	 * con id settings_image_button 
	 * 
	 * @param v vista actual
	 */
	public void onSettingsButtonClick(View v) 
	{
		Intent myIntent = new Intent (this , PrincipalUserActivity.class);
		startActivity(myIntent);
	}
	
	public void onTutorialButtonClick(View v) 
	{
		Intent myIntent = new Intent (this , TutorialActivity.class);
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
