package com.yujoglish;

import java.util.Timer;
import java.util.TimerTask;

import com.yujoglish.configuracion.usuario.PrincipalUserActivity;
import com.yujoglish.configuracion.usuario.SelectUserActivity;
import com.yujoglish.dbhelper.DataBaseManager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class FinishActivity extends Activity{
	
	/*
	 * Tiempo de espera
	 */
	private int leadTime = 3000;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finish);
		
		String infoRecibidaTestVoice = null;
		String infoRecibidaTestWord = null;
		String infoRecibidaPracticeVoice = null;
		String infoRecibidaPracticeWord = null;
		String infoRecibidaLearn = null;
		String infoRecibidaPrincipalUser = null;
		
		
		Intent mensaje = getIntent();
		infoRecibidaTestWord = mensaje.getStringExtra(WordTestActivity.POSICION_WORD);
		infoRecibidaTestVoice = mensaje.getStringExtra(VoiceTestActivity.POSICION_VOICE_TEST);
		infoRecibidaPracticeVoice = mensaje.getStringExtra(VoicePracticeActivity.POSICION_VOICE);
		infoRecibidaPracticeWord = mensaje.getStringExtra(WordPracticeActivity.POSICION_WORD);
		infoRecibidaLearn = mensaje.getStringExtra(LearnActivity.POSICION_lEARN);
		infoRecibidaPrincipalUser = mensaje.getStringExtra(PrincipalUserActivity.INFO_PRINCIPAL);
		
		TextView mensajePublicar = (TextView) findViewById(R.id.tVCustom);
		TextView felicitacion = (TextView) findViewById(R.id.tVFelicidades);
		
		if (infoRecibidaLearn != null) {
			mensajePublicar.setText(infoRecibidaLearn);
			runActivityTime();
			
			
		}
		
		if (infoRecibidaTestWord != null) {
			mensajePublicar.setText(infoRecibidaTestWord);
			runActivityTime();
			
			
		}
		
		if (infoRecibidaTestVoice != null) {
			mensajePublicar.setText(infoRecibidaTestVoice);
			runActivityTime();
			
			
		}
		
		if (infoRecibidaPracticeVoice != null) {
			mensajePublicar.setText(infoRecibidaPracticeVoice);
			runActivityTime();
			
			
		}
		
		if (infoRecibidaPracticeWord != null) {
			mensajePublicar.setText(infoRecibidaPracticeWord);
			runActivityTime();
			
			
		}
		
		if(infoRecibidaPrincipalUser != null){
			mensajePublicar.setText(infoRecibidaPrincipalUser);
			felicitacion.setText("");
			runActivityTime();
			
			
		}
		
	}
	
	public void selectLeccion() {
		Intent select = new Intent(this,LessonActivity.class);
		startActivity(select);
		
		this.finish();
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
				selectLeccion();
			}
		}, leadTime);

	}
}
