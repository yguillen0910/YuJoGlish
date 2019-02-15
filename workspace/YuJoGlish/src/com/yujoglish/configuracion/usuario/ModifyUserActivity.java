/*
 * @(#) AgeManager.java  1 01/032/15
 * 
 * Copyrigth (c) 2015 Jorge Hernández, Yuzmhar Guillén
 * Caracas, Venezuela
 * Todos los derechos reservados.
 *   
 */
package com.yujoglish.configuracion.usuario;

import java.util.Calendar;

import com.yujoglish.AcercaDeActivity;
import com.yujoglish.LessonActivity;
import com.yujoglish.LevelActivity;
import com.yujoglish.R;
import com.yujoglish.dbhelper.DataBaseManager;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Clase para modificar usuario
 *  
 * @author Jorge Hernádez
 * @version 1, 27/02/2015 
 */
public class ModifyUserActivity extends Activity{
	
	/*
	 * ActionBar
	 */
	private ActionBar actionBar;

	// Variables OnCreate
	private EditText EditTextNombre;
	private EditText EditTextFechaNacimiento;
	private TextView TextViewInformativoModificarUsuario;
	private RadioButton RadioButtonMale;
	private RadioButton RadioButtonFemale;
	private DataBaseManager managerDB;
	private Cursor cursor;
	/**/ //Variables para el metodo dialog 
	private Calendar cal;
	private int day;
	private int month;
	private int year;
	/**/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_user);
		

		actionBar = getActionBar();
		
		actionBar.setTitle("Editar Usuario");
		actionBar.setDisplayUseLogoEnabled(false);
		
	//	(Button) findViewById(R.id.button_guardar_modificar_usuario);
		EditTextNombre = (EditText) findViewById(R.id.editText_nombre_usuario);
		EditTextFechaNacimiento = (EditText) findViewById(R.id.editText_fechanac_usuario);
		TextViewInformativoModificarUsuario = (TextView) findViewById(R.id.textView_informativo_agregar_modificar_usuario);
		RadioButtonMale = (RadioButton) findViewById(R.id.radio0);
		RadioButtonFemale = (RadioButton) findViewById(R.id.radio1);
		
		managerDB = new DataBaseManager(this);
		cursor = managerDB.cargarCursorUsuarioActivo();
		if(cursor.moveToFirst()){
			EditTextNombre.setText(cursor.getString(1));
			EditTextFechaNacimiento.setText(cursor.getString(2));
			if (cursor.getString(3).matches("M")) RadioButtonMale.setChecked(true); else RadioButtonFemale.setChecked(true);
		}

		EditTextFechaNacimiento.setFocusable(false);
		/**/
	      cal = Calendar.getInstance();
	      day = cal.get(Calendar.DAY_OF_MONTH);
	      month = cal.get(Calendar.MONTH);
	      year = cal.get(Calendar.YEAR);        
	}
	
	// Dialogo para colocar la fecha de nacimiento
	public void DateDialog(){
	    OnDateSetListener listener=new OnDateSetListener() {
	        @Override
	        public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth)
	        {
	        	monthOfYear++;
	        	EditTextFechaNacimiento.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
	        }};
	    DatePickerDialog dpDialog=new DatePickerDialog(this, listener, year, month, day);
	    dpDialog.show();
	}
	
	// Metodo al presionar el boton modificar
	public void onclickGuardarModificarUsuario(View v){
		//TextViewInformativoModificarUsuario.setText("Su usuario ha sido modificado a "+EditTextNombre.getText().toString());
		String sexo;
		int edad = Integer.parseInt(AgeManager.edad(EditTextFechaNacimiento.getText().toString()));
		if(edad>2){
			
			if(RadioButtonMale.isChecked()) sexo = "M"; else sexo = "F";
			
			
			if(!managerDB.verificarUsuario(EditTextNombre.getText().toString(), EditTextFechaNacimiento.getText().toString())){
			managerDB.modificarUsuarioActivo(EditTextNombre.getText().toString(), EditTextFechaNacimiento.getText().toString(),sexo);
			Intent redireecion = new Intent(v.getContext(),RedirectionUserActivity.class);
			Toast.makeText(getApplicationContext(),EditTextNombre.getText().toString()+ " se ha Modificado con Exito! :)" ,Toast.LENGTH_LONG).show();
			startActivity(redireecion);
			this.finish();
			}
			else Toast.makeText(getApplicationContext(), "No puedes Modificar un Usuario que sea igual a Otro! :(" ,Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(getApplicationContext(), "Solo se pueden tener usuarios mayores a 3 años! :(" ,Toast.LENGTH_LONG).show();
			//TextViewInformativoAgregarUsuario.setText("Solo de pueden tener niños entre 3 a 6 años");
		}
	}
	
	// Metodo al presionar el edit text de la fecha
	public void onclickEditFechaNac(View v){		
		DateDialog();
	}
	      
	@Override
	protected void onRestart() {
		super.onRestart();
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
