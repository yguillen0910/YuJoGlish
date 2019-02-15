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
public class MainActivity extends Activity {
	
	/*
	 * Manejador de Base de datos
	 */
	DataBaseManager dbManager;
	
	/*
	 * Tarea que extraerá todos los activos
	 */
	AssetsExtracter mTask;
	
	/*
	 * Tiempo de espera
	 */
	private int leadTime = 800;
   
	/**
	 * Clase que se encarga de extraer todos los objetos que se encuentran en la 
	 * carpeta assets y sobrescribir los archivos existentes
	 * 
	 * @author Yuzmhar Guillén
	 */
	private class AssetsExtracter extends AsyncTask<Integer, Integer, Boolean>
	{
		/**
		 * Método que extraerá los archivos de la clase assets en 
		 * Background
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Boolean doInBackground(Integer... params)
		{
			try{
				AssetsManager.extractAllAssets(getApplicationContext(), BuildConfig.DEBUG);
				//AssetsManager.extractAllAssets(getApplicationContext(), true);
			}catch (IOException e){
				MetaioDebug.printStackTrace(Log.ERROR, e);
				return false;
			}
			return true;
		}
	}

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
		setContentView(R.layout.activity_main);
						
		mTask = new AssetsExtracter();
		mTask.execute(0);
		
		dbManager = new DataBaseManager(this);
		Cursor cursorPalabra = dbManager.cargarCursorPalabra(); 
		Cursor cursorNivel = dbManager.cargarCursorNivel();
		Cursor cursorLeccion = dbManager.cargarCursorLeccion();
		
		if(!cursorNivel.moveToFirst()){
			
			Nivel nivel1 = new Nivel(1,4);
			Nivel nivel2 = new Nivel(2,5);
			Nivel nivel3 = new Nivel(3,6);
			
			dbManager.insertarNivel(nivel1);
			dbManager.insertarNivel(nivel2);
			dbManager.insertarNivel(nivel3);
										
		}
		
		if (!cursorLeccion.moveToFirst()) {
			
			//Creando lecciones
			Leccion family = new Leccion (1,"Family");
			Leccion colors = new Leccion (2,"Colors");
			Leccion shapes = new Leccion (3,"Shapes");
			Leccion numbers = new Leccion (4,"Numbers");
			Leccion animals = new Leccion (5,"Animals");
			Leccion classroom = new Leccion (6,"Fruits");
			Leccion face = new Leccion (7,"Face");
			Leccion body = new Leccion (8,"Body");
			Leccion toys = new Leccion (9,"Clothes");
			Leccion clothes = new Leccion (10,"Toys");
			Leccion birthday = new Leccion (11,"Birthday");
			Leccion fruits = new Leccion (12,"Classroom");
			
			dbManager.insertarLeccion(family);
			dbManager.insertarLeccion(colors);
			dbManager.insertarLeccion(shapes);
			dbManager.insertarLeccion(numbers);
			dbManager.insertarLeccion(animals);
			dbManager.insertarLeccion(classroom);
			dbManager.insertarLeccion(face);
			dbManager.insertarLeccion(body);
			dbManager.insertarLeccion(toys);
			dbManager.insertarLeccion(clothes);
			dbManager.insertarLeccion(birthday);
			dbManager.insertarLeccion(fruits);
									
			if (!cursorPalabra.moveToFirst() ) {
				
				// Creando palabras
				
				//Familia leccion 1
				Palabra father = new Palabra("father", 1, 1);
				Palabra mother = new Palabra("mother", 2, 1);
				Palabra sister = new Palabra("sister", 3, 1);
				Palabra brother = new Palabra("brother", 4, 1);
				Palabra grandfather = new Palabra("grandfather", 5, 1);
				Palabra grandmother = new Palabra("grandmother", 6, 1); 
				
				dbManager.insertarPalabra(father);
				dbManager.insertarPalabra(mother);
				dbManager.insertarPalabra(sister);
				dbManager.insertarPalabra(brother);
				dbManager.insertarPalabra(grandfather);
				dbManager.insertarPalabra(grandmother);
				
				//Colores leccion 2 
				Palabra red = new Palabra("red", 1, 2);
				Palabra blue = new Palabra("blue", 2, 2);
				Palabra yellow = new Palabra("yellow", 3, 2);
				Palabra green = new Palabra("green", 4, 2);
				Palabra purple = new Palabra("purple", 5, 2);
				Palabra orange = new Palabra("orange", 6, 2); 
				
				dbManager.insertarPalabra(red);
				dbManager.insertarPalabra(blue);
				dbManager.insertarPalabra(yellow);
				dbManager.insertarPalabra(green);
				dbManager.insertarPalabra(purple);
				dbManager.insertarPalabra(orange);
				
				//Shapes leccion 3 
				Palabra circle = new Palabra("circle", 1, 3);
				Palabra square = new Palabra("square", 2, 3);
				Palabra triangle = new Palabra("triangle", 3, 3);
				Palabra rectangle = new Palabra("rectangle", 4, 3);
				Palabra rhombus = new Palabra("rhombus", 5, 3);
				Palabra star = new Palabra("star", 6, 3);
								
				dbManager.insertarPalabra(circle);
				dbManager.insertarPalabra(square);
				dbManager.insertarPalabra(triangle);
				dbManager.insertarPalabra(rectangle);
				dbManager.insertarPalabra(rhombus);
				dbManager.insertarPalabra(star);
				
				//Numbers leccion 4 
				Palabra one = new Palabra("one", 1, 4);
				Palabra two = new Palabra("two", 2, 4);
				Palabra three = new Palabra("three", 3, 4);
				Palabra four = new Palabra("four", 4, 4);
				Palabra five = new Palabra("five", 5, 4);
				Palabra six = new Palabra("six", 6, 4); 
				
				dbManager.insertarPalabra(one);
				dbManager.insertarPalabra(two);
				dbManager.insertarPalabra(three);
				dbManager.insertarPalabra(four);
				dbManager.insertarPalabra(five);
				dbManager.insertarPalabra(six);
				
				//Animals leccion 5 
				Palabra dog = new Palabra("dog", 1, 5);
				Palabra cat = new Palabra("cat", 2, 5);
				Palabra rabbit = new Palabra("rabbit", 3, 5);
				Palabra horse = new Palabra("horse", 4, 5);
				Palabra cow = new Palabra("cow", 5, 5);
				Palabra bird = new Palabra("bird", 6, 5); 
				
				dbManager.insertarPalabra(dog);
				dbManager.insertarPalabra(cat);
				dbManager.insertarPalabra(rabbit);
				dbManager.insertarPalabra(horse);
				dbManager.insertarPalabra(cow);
				dbManager.insertarPalabra(bird);
				
				//Fruits leccion 6 
				Palabra apple = new Palabra("apple", 1, 6);
				Palabra grape = new Palabra("grape", 2, 6);
				Palabra pear = new Palabra("pear", 3, 6);
				Palabra oranges = new Palabra("orange", 4, 6);
				Palabra lemon = new Palabra("lemon", 5, 6);
				Palabra banana = new Palabra("banana", 6, 6); 
				
				dbManager.insertarPalabra(apple);
				dbManager.insertarPalabra(grape);
				dbManager.insertarPalabra(pear);
				dbManager.insertarPalabra(oranges);
				dbManager.insertarPalabra(lemon);
				dbManager.insertarPalabra(banana);
				
				//Face leccion 7 
				Palabra eyes = new Palabra("eyes", 1, 7);
				Palabra nose = new Palabra("nose", 2, 7);
				Palabra mouth = new Palabra("mouth", 3, 7);
				Palabra ears = new Palabra("ears", 4, 7);
				Palabra hair = new Palabra("hair", 5, 7);
				Palabra facePalabra = new Palabra("face", 6, 7); 
				
				dbManager.insertarPalabra(eyes);
				dbManager.insertarPalabra(nose);
				dbManager.insertarPalabra(mouth);
				dbManager.insertarPalabra(ears);
				dbManager.insertarPalabra(hair);
				dbManager.insertarPalabra(facePalabra);
				
				//Body leccion 8 
				Palabra head = new Palabra("head", 1, 8);
				Palabra legs = new Palabra("legs", 2, 8);
				Palabra hands = new Palabra("hands", 3, 8);
				Palabra feet = new Palabra("feet", 4, 8);
				Palabra chest = new Palabra("chest", 5, 8);
				Palabra bodyPalabra = new Palabra("body", 6, 8); 
				
				dbManager.insertarPalabra(head);
				dbManager.insertarPalabra(legs);
				dbManager.insertarPalabra(hands);
				dbManager.insertarPalabra(feet);
				dbManager.insertarPalabra(chest);
				dbManager.insertarPalabra(bodyPalabra);
				
				//Clothes leccion 9 
				Palabra shirt = new Palabra("shirt", 1, 9);
				Palabra pants = new Palabra("pants", 2, 9);
				Palabra shoes = new Palabra("shoes", 3, 9);
				Palabra socks = new Palabra("socks", 4, 9);
				Palabra gloves = new Palabra("gloves", 5, 9);
				Palabra skirt = new Palabra("skirt", 6, 9); 
				
				dbManager.insertarPalabra(shirt);
				dbManager.insertarPalabra(pants);
				dbManager.insertarPalabra(shoes);
				dbManager.insertarPalabra(socks);
				dbManager.insertarPalabra(gloves);
				dbManager.insertarPalabra(skirt);
				
				//Toys leccion 10 
				Palabra teddyBear = new Palabra("teddy", 1, 10);
				Palabra car = new Palabra("car", 2, 10);
				Palabra ball = new Palabra("ball", 3, 10);
				Palabra blocks = new Palabra("blocks", 4, 10);
				Palabra doll = new Palabra("doll", 5, 10);
				Palabra puzzle = new Palabra("puzzle", 6, 10); 
				
				dbManager.insertarPalabra(teddyBear);
				dbManager.insertarPalabra(car);
				dbManager.insertarPalabra(ball);
				dbManager.insertarPalabra(blocks);
				dbManager.insertarPalabra(doll);
				dbManager.insertarPalabra(puzzle);
				
				//Birthday leccion 11 
				Palabra cake = new Palabra("cake", 1, 11);
				Palabra balloon = new Palabra("balloon", 2, 11);
				Palabra candy = new Palabra("candy", 3, 11);
				Palabra present = new Palabra("present", 4, 11);
				Palabra cookie = new Palabra("cookie", 5, 11);
				Palabra candle = new Palabra("candle", 6, 11); 
				
				dbManager.insertarPalabra(cake);
				dbManager.insertarPalabra(balloon);
				dbManager.insertarPalabra(candy);
				dbManager.insertarPalabra(present);
				dbManager.insertarPalabra(cookie);
				dbManager.insertarPalabra(candle);
				
				//Classroom leccion 12 
				Palabra table = new Palabra("table", 1, 12);
				Palabra chair = new Palabra("chair", 2, 12);
				Palabra pencil = new Palabra("pencil", 3, 12);
				Palabra chalk = new Palabra("chalk", 4, 12);
				Palabra bag = new Palabra("bag", 5, 12);
				Palabra book = new Palabra("book", 6, 12); 
				 
				
				dbManager.insertarPalabra(table);
				dbManager.insertarPalabra(chair);
				dbManager.insertarPalabra(pencil);
				dbManager.insertarPalabra(chalk);
				dbManager.insertarPalabra(bag);
				dbManager.insertarPalabra(book);
				
				
			}
			
		}
		
		runActivityTime();
		
	}
	
	public void selectUser() {
		Intent select = new Intent(this,SelectUserActivity.class);
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
				selectUser();
			}
		}, leadTime);
	}
}
