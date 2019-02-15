package com.yujoglish.stadistics;

import java.util.ArrayList;

import com.yujoglish.AcercaDeActivity;
import com.yujoglish.LessonActivity;
import com.yujoglish.LevelActivity;
import com.yujoglish.MenuActivity;
import com.yujoglish.R;
import com.yujoglish.configuracion.usuario.AgeManager;
import com.yujoglish.customadapter.CustomItemActivity;
import com.yujoglish.dbhelper.DataBaseManager;
import com.yujoglish.util.Util;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class StadisticsMainActivity extends Activity {

	//private TextView textViewNombre;
	//private TextView textViewEdad;
	private ListView listViewUsuarioActivo;
	private ArrayList<CustomItemActivity> items = new ArrayList<CustomItemActivity>();
	/*
	 * ActionBar
	 */
	private ActionBar actionBar;
	private TextView textViewAciertos;
	private TextView textViewIntentos;
	private TextView textViewTiempoPractica;
	private TextView textViewPromedio;
	private TextView textViewTiempoAprendizaje;
	private TextView textViewStadisticsTitulo;
	
	private DataBaseManager managerDB;
	private Cursor cursor;
	
	public final static String INFO_DETALLES_STADISTICS = "com.yujoglish.StadisticsDetailsActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stadistic); 
		
		actionBar = getActionBar();
		
		
		actionBar.setDisplayUseLogoEnabled(false);
		
		inicializarComponentes();
		
		cargarVistaUsuarioActual();
		
		String[] resultado=UtilStadistics.aciertosLeccionUsuario(getNombreLeccion(), this).split(":");
		UtilStadistics.DetallePalabrasUsuario(getNombreLeccion(), this);
		textViewAciertos.setText("Aciertos: "+resultado[0]);
		
		textViewIntentos.setText("Intentos: "+resultado[1]);
		
		Float promedio = (float) Integer.parseInt(resultado[0]) / Integer.parseInt(resultado[1]);
		promedio = promedio*100;
		textViewPromedio.setText("Promedio: "+promedio.intValue()+"% de Aciertos.");
		
		textViewTiempoPractica.setText("Tiempo dedicado a Practica: "+resultado[3]+ " min.");
		
		textViewTiempoAprendizaje.setText("Tiempo dedicado a Aprender: "+resultado[2]+ " min.");
	}
	
	private void inicializarComponentes(){
		
		listViewUsuarioActivo = (ListView) findViewById(R.id.listView_usuarioActivo);
		textViewAciertos = (TextView) findViewById(R.id.estadistica_textView1);
		textViewIntentos = (TextView) findViewById(R.id.estadistica_textView2);
		textViewPromedio = (TextView) findViewById(R.id.estadistica_textView3);
		textViewTiempoPractica = (TextView) findViewById(R.id.estadistica_textView4);
		textViewTiempoAprendizaje = (TextView) findViewById(R.id.estadistica_textView5);
		//textViewStadisticsTitulo = (TextView) findViewById(R.id.textView_titulo_estadisticas);
		
	}
	
	private void cargarVistaUsuarioActual(){
		
		managerDB = new DataBaseManager(this);
		cursor = managerDB.cargarCursorUsuarioActivo();
		Integer progressBar[]; 
		if(cursor.moveToFirst()){
			progressBar = Util.progressbarUsuario(cursor.getString(0), this);
			String nombre=cursor.getString(1);
			String edad = AgeManager.edad(cursor.getString(2));
			items.add(new CustomItemActivity(cursor.getString(0),nombre," Edad: "+edad+" años",progressBar[1], progressBar[0],cursor.getString(4)));
			ArrayAdapter<CustomItemActivity> adapter = new MyListAdapter();
			listViewUsuarioActivo.setAdapter(adapter);
		}
	}
	
	private String getNombreLeccion(){
		//String info = "com.yujoglish.StadisticsMainActivity";
		Intent mensaje = getIntent();
		String nombreLeccion = mensaje.getStringExtra(MenuActivity.LEVEL_INFO_STADISTICS);
		actionBar.setTitle("Estadísticas  > "+nombreLeccion);
		//textViewStadisticsTitulo.setText("Stadistics -> "+nombreLeccion);
		return nombreLeccion;
	}
	
	public void detallesLeccion(View v){
		Intent detallesActivity = new Intent(v.getContext(),StadisticsDetailsActivity.class);
		detallesActivity.putExtra(INFO_DETALLES_STADISTICS, getNombreLeccion());
		startActivity(detallesActivity);
		
	}
	
	private class MyListAdapter extends ArrayAdapter<CustomItemActivity>{

		public MyListAdapter() {
			super(StadisticsMainActivity.this, R.layout.activity_custom_items, items);
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
