/*
 * @(#) PrincipalUserActivity.java  1 01/03/15
 * 
 * Copyrigth (c) 2015 Jorge Hernández, Yuzmhar Guillén
 * Caracas, Venezuela
 * Todos los derechos reservados.
 *   
 */
package com.yujoglish.configuracion.usuario;

import java.util.ArrayList;

import com.yujoglish.AcercaDeActivity;
import com.yujoglish.LessonActivity;
import com.yujoglish.LevelActivity;
import com.yujoglish.R;
import com.yujoglish.customadapter.CustomItemActivity;
import com.yujoglish.dbhelper.DataBaseManager;
import com.yujoglish.util.Util;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Clase que para seleccionar un usuario
 * 
 * @author Jorge Hernández
 * @author Yuzmhar Guillén
 * @version 1, 01/03/2015 
 */
public class SelectUserActivity extends Activity{
	
	/*
	 * ActionBar
	 */
	private ActionBar actionBar;
		
	/*
	 * Lista de CustomItemActivity 
	 * se utiliza para mostrar cada item 
	 * de la lista con un estilo ue fue creado 
	 */
	private ArrayList<CustomItemActivity> items = new ArrayList<CustomItemActivity>();
	
	/**
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_user);
		actionBar = getActionBar();
		
		actionBar.setTitle("Seleccionar Usuario");
		actionBar.setDisplayUseLogoEnabled(false);
		
		llenarLista();
		llenarListView();
		clickItem();
	}
	
	/**
	 * Método para llenar lista de usuarios a mostrar
	 */
	private void llenarLista() {
		
		DataBaseManager manager = new DataBaseManager(this);
		String edad = "";
		String nombre = "";
		Integer progressBar[]; 
		
		Cursor cursor = manager.cargarCursorUsuario(); 
		if (!cursor.moveToFirst() ) {
			manager.insertarUsuario("Pedro Perez", "01/01/2012","M");
			manager.insertarUsuario("Maria Gonzales", "01/01/2010","F");
			cursor = manager.cargarCursorUsuario(); 
		}
		
		if(cursor.moveToFirst())
			do{
				edad = "Edad: " + AgeManager.edad(cursor.getString(2))+" años";
				progressBar = Util.progressbarUsuario(cursor.getString(0), this);
				if (cursor.getString(3).matches("true")){
					nombre = cursor.getString(1) + " (Activo)";
					items.add(new CustomItemActivity(cursor.getString(0),nombre, edad, "", progressBar[1], progressBar[0],cursor.getString(4) ));
				}
				else 
					items.add(new CustomItemActivity(cursor.getString(0),cursor.getString(1), edad, "", progressBar[1], progressBar[0],cursor.getString(4)));
			}while(cursor.moveToNext());
		else {
			//items.add(new CustomItemActivity("0","NO HAY USUARIO CREADO", "", "", 0, 72,"N"));
		}
	}
	
	/**
	 * Método para llenar lista de la vista
	 */
	private void llenarListView() {
		ArrayAdapter<CustomItemActivity> adapter = new MyListAdapter();
		ListView users = (ListView) findViewById(R.id.lVSelectUser);
		users.setAdapter(adapter);	
	}
	
	/**
	 * Método que muesta mensaje de usuario seleccionado
	 */
	private void clickItem() {
		
		ListView users = (ListView) findViewById(R.id.lVSelectUser);
		users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked,
			int position, long id) {
					CustomItemActivity clicked = items.get(position);
					String message = "Has Seleccionado a " /*+ position
					+ " su nombre es " */+ clicked.getNombre()/*+" el ID es -> "+clicked.getId()*/;
					Toast.makeText(SelectUserActivity.this, message, Toast.LENGTH_LONG).show();
					DataBaseManager manager =  new DataBaseManager(parent.getContext());
					manager.activarUsuario(clicked.getId());
					Intent redireecion = new Intent(SelectUserActivity.this,RedirectionUserActivity.class);
					//redireecion.putExtra("textoInformativo", "El usuario "+clicked.getNombre()+" de id "+clicked.getId()+" ha sido seleccionado");
					startActivity(redireecion);
					SelectUserActivity.this.finish();
				}
			});
	}
	
	/**
	 * Método para validar la cantidad de usuarios 
	 * que pueden ser creados
	 * 
	 * @param v vista actual
	 */
	public void onAddUserButtonClick(View v)
	{
		DataBaseManager manager = new DataBaseManager(this);
		Cursor cursor = manager.cargarCursorUsuario();
		if(cursor.getCount() < 5){
			Intent myIntent = new Intent(v.getContext(), AddUserActivity.class);
			startActivity(myIntent);
			this.finish();
		}else {
			String message = "Ha llegado al límite de usuarios, no es posible crear otro usuario";
			Toast.makeText(SelectUserActivity.this, message, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Clase para usar y setear data al CustomItemActivity
	 *  
	 * @author Yuzmhar Guillén
	 * @author Jorge Hernádez
	 * @version 1, 01/03/2015 
	 */
	private class MyListAdapter extends ArrayAdapter<CustomItemActivity>{

		public MyListAdapter() {
			super(SelectUserActivity.this, R.layout.activity_custom_items, items);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			
			if(view == null) {
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
