/*
 * @(#) DataBaseHelper.java  1 01/03/15
 * 
 * Copyrigth (c) 2015 Jorge Hernández, Yuzmhar Guillén
 * Caracas, Venezuela
 * Todos los derechos reservados.
 *   
 */
package com.yujoglish.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Clase donde se crea la base de dato y sus tablas
 * 
 * @author Jorge Hernández
 * @author Yuzmhar Guillén
 * @version 1, 01/03/2015 
 */
public class DataBaseHelper extends SQLiteOpenHelper {
	
	/*
	 * nombre de la base de datos
	 */
	private static final String DB_NAME="yujoglish.sqlite";
	
	/*
	 * version de la base de datos
	 */
	private static final int DB_SCHEME_VERSION = 1;
	
	/*
	 * constructor
	 */
	public DataBaseHelper(Context context) {
		super(context, DB_NAME, null, DB_SCHEME_VERSION);
	}
	
	/** 
	 * Método que se llama cuando se creó por primera vez la actividad.
	 * Se crea la base de datos
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(DataBaseManager.CREATE_TABLE_NIVEL);
		db.execSQL(DataBaseManager.CREATE_TABLE_LECCION);
		db.execSQL(DataBaseManager.CREATE_TABLE_PALABRA);
		db.execSQL(DataBaseManager.CREATE_TABLE_USUARIO);
		db.execSQL(DataBaseManager.CREATE_TABLE_USUARIO_PALABRA);
			
	}
	/**
	 * (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
