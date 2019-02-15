package com.yujoglish.stadistics;

import java.util.ArrayList;

import com.yujoglish.AcercaDeActivity;
import com.yujoglish.LessonActivity;
import com.yujoglish.LevelActivity;
import com.yujoglish.R;
import com.yujoglish.customadapter.DetailStadisticsCustomItem;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class StadisticsDetailsActivity extends Activity {

	private ArrayList<DetailStadisticsCustomItem> items = new ArrayList<DetailStadisticsCustomItem>();
	private ListView itemList;
	/*
	 * ActionBar
	 */
	private ActionBar actionBar;
	
	public StadisticsDetailsActivity() {
		
	}
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stadistic_details);
		actionBar = getActionBar();
		
		actionBar.setTitle("Detalle Estadísticas");
		actionBar.setDisplayUseLogoEnabled(false);
		inicializarComponentes();
		llenarLista();
		llenarListView();
	}
	
	/**
	 * Metodo encargado de llenar la lista usando el metodo DetallePalabrasUsuario de util stadistics
	 * 
	 * */
	private void llenarLista(){
		items = UtilStadistics.DetallePalabrasUsuario(getNombreLeccion(), this);
	}
	
	/**
	 * Inicializo los componentes de la vista
	 * */
	private void inicializarComponentes(){
		
		itemList = (ListView) findViewById(R.id.listView_StadisticsDetails);
	}
	
	/**
	 * Devuelve el nombre de la leccion respectiva.
	 * */
	private String getNombreLeccion(){
		
		Intent mensaje = getIntent();
		return mensaje.getStringExtra(StadisticsMainActivity.INFO_DETALLES_STADISTICS);
	}
	
	/**
	 * Método para llenar lista de la vista
	 */
	private void llenarListView() {
		ArrayAdapter<DetailStadisticsCustomItem> adapter = new MyListAdapter();
		itemList.setAdapter(adapter);	
	}
	
	/**
	 * Clase interna encargada de trnasformar a un adaptador la lista de objetos de DetallePalabrasUsuario
	 * **/
	private class MyListAdapter extends ArrayAdapter<DetailStadisticsCustomItem>{

		public MyListAdapter() {
			super(StadisticsDetailsActivity.this, R.layout.activity_custom_items_stadistic_details, items);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			
			if(view == null) {
				view = getLayoutInflater().inflate(R.layout.activity_custom_items_stadistic_details, null);
			}
			
			DetailStadisticsCustomItem item = items.get(position);
			
			TextView wordName = (TextView) view.findViewById(R.id.tv_stadistic_details_nombrePalabra);
			TextView aciertos = (TextView) view.findViewById(R.id.tv_stadistic_details_Aciertos);
			TextView intentos = (TextView) view.findViewById(R.id.tv_stadistic_details_Intentos);

			wordName.setText(item.getNombre().toUpperCase());
			
			aciertos.setText("Aciertos: "+Integer.toString(item.getAciertos()));
			intentos.setText("Intentos: "+Integer.toString(item.getIntentos()));
					
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
