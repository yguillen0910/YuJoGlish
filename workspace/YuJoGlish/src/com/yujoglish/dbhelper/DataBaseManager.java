/*
 * @(#) DataBaseManager.java  1 01/03/15
 * 
 * Copyrigth (c) 2015 Jorge Hernández, Yuzmhar Guillén
 * Caracas, Venezuela
 * Todos los derechos reservados.
 *   
 */
package com.yujoglish.dbhelper;

import com.yujoglish.configuracion.usuario.AgeManager;
import com.yujoglish.model.Leccion;
import com.yujoglish.model.Nivel;
import com.yujoglish.model.Palabra;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * Clase para manejar la base de datos
 * 
 * @author Jorge Hernández
 * @author Yuzmhar Guillén
 * @version 1, 01/03/2015 
 */
public class DataBaseManager {

	/*
	 * numero de la lección
	 */
	private String numeroLeccion = null;
	
	/*
	 * cursor para recorrer resultados de la consulta
	 */
	private Cursor cursor = null;
	
	/*
	 * Nombre Tablas
	 */
    public static final String TABLE_PALABRA = "palabra";
    public static final String TABLE_NIVEL = "nivel";
    public static final String TABLE_LECCION = "leccion";
    public static final String TABLE_USUARIO = "usuario";
    public static final String TABLE_USUARIO_PALABRA = "usuario_palabra";
    
    /*
     *	Columnas PALABRA
     */
    public static final String CN_ID_PALABRA = "id_palabra";
    public static final String CN_NOMBRE_PALABRA = "nombre_palabra";
    public static final String CN_POSICION_PALABRA = "posicion_palabra";
    public static final String CN_FK_ID_LECCION_PALABRA = "fk_id_leccion";
    
    /*
     *	Columnas USUARIO
     */
    public static final String CN_ID_USUARIO = "id_usuario"; 
    public static final String CN_NOMBRE_USUARIO = "nombre_usuario";
    public static final String CN_FECHA_NAC_USUARIO = "fecha_nac_usuario";
    public static final String CN_SEXO_USUARIO = "sexo_usuario";
    public static final String CN_ESTADO_USUARIO = "estado_usuario";
    public static final String CN_FK_ID_NIVEL_USUARIO = "fk_id_nivel";
    
    /*
     * Columnas NIVEL
     */
    public static final String CN_ID_NIVEL = "id_nivel"; 
    public static final String CN_NUMERO_NIVEL = "numero_nivel";
    public static final String CN_CANTIDAD_PALABRAS_NIVEL = "cantidad_palabras";
    
    /*
     * Columnas LECCION
     */
    public static final String CN_ID_LECCION = "id_leccion"; 
    public static final String CN_NUMERO_LECCION = "numero_leccion";
    public static final String CN_NOMBRE_LECCION = "nombre_leccion";
    
    /*
     * Columnas USUARIO_PALABRA
     */
    public static final String CN_ID_USUARIO_PALABRA = "id_usuario_palabra";
    public static final String CN_FK_ID_USUARIO = "fk_id_usuario";
    public static final String CN_FK_ID_PALABRA = "fk_id_palabra";
    public static final String CN_CONTADOR_ACIERTOS = "contador_aciertos";
    public static final String CN_CONTADOR_INTENTOS = "contador_intentos";
    public static final String CN_TIEMPO_LEARN = "tiempo_learn";
    public static final String CN_TIEMPO_PRACT = "tiempo_pract";
    public static final String CN_TIPO_TEST = "tipo";
    
    /*
     * CREATE TABLE NIVEL
     */
    public static final String CREATE_TABLE_NIVEL = "create table " + TABLE_NIVEL + " (" 
                                                     + CN_ID_NIVEL + " integer primary key autoincrement,"
                                                     + CN_NUMERO_NIVEL + " integer not null,"
                                                     + CN_CANTIDAD_PALABRAS_NIVEL + " integer not null);";
    
    /*
     * CREATE TABLE USUARIO  FOREIGN KEY(trackartist) REFERENCES artist(artistid)
     */
    public static final String CREATE_TABLE_USUARIO = "create table " + TABLE_USUARIO + " (" 
                                                     + CN_ID_USUARIO + " integer primary key autoincrement,"
                                                     + CN_NOMBRE_USUARIO + " text not null,"
                                                     + CN_FECHA_NAC_USUARIO + " text not null,"
                                                     + CN_SEXO_USUARIO + " text not null," 
                                                     + CN_ESTADO_USUARIO + " boolean not null," 
                                                     + CN_FK_ID_NIVEL_USUARIO + " integer not null," 
                                                     + " FOREIGN KEY("+CN_FK_ID_NIVEL_USUARIO+") REFERENCES "+TABLE_NIVEL+"("+CN_ID_NIVEL+"));";
    
    /*
     * CREATE TABLE LECCION
     */
    public static final String CREATE_TABLE_LECCION = "create table " + TABLE_LECCION + " (" 
                                                     + CN_ID_LECCION + " integer primary key autoincrement,"
                                                     + CN_NUMERO_LECCION + " integer not null,"
                                                     + CN_NOMBRE_LECCION + " text not null);";
    
    /*
     * CREATE TABLE PALABRA
     */
    public static final String CREATE_TABLE_PALABRA = "create table " + TABLE_PALABRA + " (" 
                                                     + CN_ID_PALABRA + " integer primary key autoincrement,"
                                                     + CN_NOMBRE_PALABRA + " text not null,"
                                                     + CN_POSICION_PALABRA + " integer not null,"
                                                     + CN_FK_ID_LECCION_PALABRA + " integer not null," 
                                                     + " FOREIGN KEY("+CN_FK_ID_LECCION_PALABRA+") REFERENCES "+TABLE_LECCION+"("+CN_ID_LECCION+"));";
    
    /*
     * CREATE TABLE_USUARIO_PALABRA
     */
    public static final String CREATE_TABLE_USUARIO_PALABRA = "create table " + TABLE_USUARIO_PALABRA + " (" 
                                                         + CN_ID_USUARIO_PALABRA + " integer primary key autoincrement,"
                                                         + CN_FK_ID_USUARIO + " integer not null,"
                                                         + CN_FK_ID_PALABRA + " integer not null,"
                                                         + CN_CONTADOR_ACIERTOS + " integer not null," 
                                                         + CN_CONTADOR_INTENTOS + " integer not null," 
                                                         + CN_TIPO_TEST + " text not null,"
                                                         + CN_TIEMPO_LEARN + " integer not null,"
                                                         + CN_TIEMPO_PRACT + " integer not null,"
                                                         + " FOREIGN KEY("+CN_FK_ID_USUARIO+") REFERENCES "+TABLE_USUARIO+"("+CN_ID_USUARIO+"),"
                                                         + " FOREIGN KEY("+CN_FK_ID_PALABRA+") REFERENCES "+TABLE_PALABRA+"("+CN_ID_PALABRA+"));";
    
    /*
     * Data base Helper
     */
    private DataBaseHelper helper;
    
    /*
     * Base de datos 
     */
    private SQLiteDatabase db;
    
    /*
     * Valores a insertar 
     */
    private ContentValues valores;
    
    /**
     * Método para inicializar variables
     */
    public DataBaseManager(Context context){
    	helper = new DataBaseHelper(context);
        db = helper.getWritableDatabase();
        valores = new ContentValues();
    }
    
    /**
     * Método para retornar valores a insertar
     * @param valores
     * @param campo
     * @param valor
     * @return
     */
    private ContentValues contenedorValores(ContentValues valores,String campo, String valor){
        if (valores==null){
            valores = new ContentValues();
            valores.put(campo, valor);    
            return valores;
        }
        else{
            valores.put(campo, valor);    
            return valores;
        }        
    }
 
    /*
     * Inicio Metodos de TABLA PALABRA 
     */
    /**
     * Método para insertar palabras en la base de datos
     * @param palabra
     */
    public void insertarPalabra(Palabra palabra){
          
          ContentValues valores = null;
          valores = contenedorValores(valores,CN_NOMBRE_PALABRA, palabra.getNombre());
          valores = contenedorValores(valores,CN_POSICION_PALABRA, Integer.toString(palabra.getPosicion()));
          valores = contenedorValores(valores,CN_FK_ID_LECCION_PALABRA,Integer.toString(palabra.getFkIdLeccion()));
          
          //en el null tiene que ir los campos que pueden se null en la tabla
          db.insert(TABLE_PALABRA, null, valores); 
      }
    
    /**
     * Método para eliminar palabra según su 
     * identificador 
     * 
     * @param idPalabra identificador de la palabra
     */
    public void eliminarPalabraPorID(int idPalabra){
        
        db.delete(TABLE_PALABRA, CN_ID_PALABRA+"=?", new String[] { String.valueOf(idPalabra) });
    }
    
    /**
     * Método para actualizar el nombre de una palabra
     * 
     * @param nombrePalabra nombre anterior de palabra
     * @param nuevoNombrePalabra nuevo nombre de palabra
     */
    public void actualizarPalabraPorNombre (String nombrePalabra, String nuevoNombrePalabra)
    {
    	ContentValues valores = null;
        valores= contenedorValores(valores, CN_NOMBRE_PALABRA, nuevoNombrePalabra);
         db.update(TABLE_PALABRA, valores, CN_NOMBRE_PALABRA+"=?", new String[] {nombrePalabra});
    }
    
    /**
     * Método para obtener toda la data de 
     * la tabla "Palabra"
     * @return 
     */
    public Cursor cargarCursorPalabra(){
        
        String columnas[] ={CN_ID_PALABRA,CN_NOMBRE_PALABRA,CN_POSICION_PALABRA, CN_FK_ID_LECCION_PALABRA};
         
        return db.query(TABLE_PALABRA,columnas, null, null, null, null, null);
    }
    
  /**
   * Retorna el identificador de la palabra y el nombre de la palabra
   * según parámetro
   * 
   * @param nombrePalabra nombre de la palabra a buscar
   * @return
   */
    public Cursor buscarPalabraPorNombre(String nombrePalabra){
        
        String [] columnas = new String [] {CN_ID_PALABRA,CN_NOMBRE_PALABRA};
         
        return db.query(TABLE_PALABRA, columnas, CN_NOMBRE_PALABRA + "=?",new String[] {nombrePalabra}, null, null, null);
    }
    
    /**
     * Retorna el identificador de la palabra y el nombre de la palabra
     * según parámetro
     * 
     * @param posicionPalabra posicion de la palabra a buscar
     * @return
     */
    public Cursor buscarPalabraPorPosicion(String posicionPalabra){
        
        String [] columnas = new String [] {CN_ID_PALABRA,CN_NOMBRE_PALABRA};
    
        return db.query(TABLE_PALABRA, columnas, CN_POSICION_PALABRA + "=?",new String[] {posicionPalabra}, null, null, null);
    
    }
    
    /**
     * Retorna palabra según posicion y lección a la que pertenece
     * 
     * @param posicionPalabra posicion de palabra
     * @param idLeccionPalabra identificador de la leccion
     * @return
     */
    public Palabra buscarPalabraPorPosicionLeccion(String posicionPalabra, String idLeccionPalabra){
        
        String [] columnas = new String [] {CN_ID_PALABRA,CN_NOMBRE_PALABRA};
        Palabra palabra = null;
       	
        cursor = db.query(TABLE_PALABRA, columnas, CN_POSICION_PALABRA + "=?" + " and " + CN_FK_ID_LECCION_PALABRA + "=?",new String[] {posicionPalabra,idLeccionPalabra}, null, null, null);
		
        int columnNombrePalabra = cursor.getColumnIndex(CN_NOMBRE_PALABRA);
		int columnIdPalabra = cursor.getColumnIndex(CN_ID_PALABRA);		
		
		if (cursor.moveToNext()) {
			
			palabra = new Palabra(cursor.getInt(columnIdPalabra), cursor.getString(columnNombrePalabra), Integer.parseInt(posicionPalabra), Integer.parseInt(idLeccionPalabra));
		}
        
		return palabra; 
    }
    
    /**
     * Retorna todas las palabras de una lección
     * 
     * @param idLeccionPalabra identificador de la lección
     * @return
     */
    public Cursor buscarPalabraPorLeccion(int idLeccionPalabra){
        
        String [] columnas = new String [] {CN_ID_PALABRA,CN_NOMBRE_PALABRA};
    
        return db.query(TABLE_PALABRA, columnas, CN_FK_ID_LECCION_PALABRA + "=?", new String[] {String.valueOf(idLeccionPalabra)}, null, null, null);
    }
   
    /*
     * Inicio Metodos de TABLA NIVEL 
     */
    
    /**
     *  Método para insertar un nuevo nivel
     * @param nivel 
     */
    public void insertarNivel (Nivel nivel)
    {
          ContentValues valores = null; 
          valores = contenedorValores(valores,CN_NUMERO_NIVEL, Integer.toString(nivel.getNumeroNivel()));
          valores = contenedorValores(valores,CN_CANTIDAD_PALABRAS_NIVEL, Integer.toString(nivel.getCantidadPalabras()));
          //en el null tiene que ir los campos que pueden se null en la tabla
          db.insert(TABLE_NIVEL, null, valores);
    }
    
    /**
     * Método para obtener la data de todos los niveles
     * existentes en la base de datos
     * @return
     */
    public Cursor cargarCursorNivel()
    {
        String columnas[] ={CN_NUMERO_NIVEL,CN_CANTIDAD_PALABRAS_NIVEL};
        
        return db.query(TABLE_NIVEL,columnas, null, null, null, null, null);
    }
    
    /**
     * Método para obtener la cantidad de palabras por nivel
     * @param numeroNivel
     * @return
     */
    public Cursor buscarCantPalabraPorNumeroNivel(String numeroNivel){    
        
    	String columnas[] ={CN_ID_NIVEL,CN_CANTIDAD_PALABRAS_NIVEL};
        return db.query(TABLE_NIVEL,columnas, CN_ID_NIVEL + "=?", new String[]{numeroNivel}, null, null, null);        
    }
    
      
   /*
    * Inicio Metodos de TABLA LECCION  
    */
    
    /**
     * Método para insertar una nueva Lección 
     * @param leccion
     */
    public void insertarLeccion (Leccion leccion){
             
        ContentValues valores = null;
        valores = contenedorValores(valores,CN_NUMERO_LECCION, Integer.toString(leccion.getNumeroLeccion()));
        valores = contenedorValores(valores,CN_NOMBRE_LECCION, leccion.getNombre());
        //en el null tiene que ir los campos que pueden se null en la tabla
        db.insert(TABLE_LECCION, null, valores);                    
    }
  
    /**
     * Método para obtener la data de todos las lecciones
     * existentes en la base de datos
     * @return
     */
    public Cursor cargarCursorLeccion(){
        
        String columnas[] ={CN_NUMERO_LECCION,CN_NOMBRE_LECCION};
         
        return db.query(TABLE_LECCION,columnas, null, null, null, null, null);
    }
    
    /**
     * Retorna número de la lección según nombre de una lección
     * @param nombreLeccion
     * @return
     */
    public String buscarLeccionPorNombre(String nombreLeccion)
    {
        String columnas[] ={CN_NUMERO_LECCION};
        Cursor cursorLeccion=db.query(TABLE_LECCION,columnas, CN_NOMBRE_LECCION + " like ?", new String[]{nombreLeccion+"%"}, null, null, null);
		int columnNumLeccion=cursorLeccion.getColumnIndex(CN_NUMERO_LECCION);					
		if (cursorLeccion.moveToNext()) {
				numeroLeccion = cursorLeccion.getString(columnNumLeccion);
		} 		
		return numeroLeccion; 
    }
    
    /**
     * Retorna identificador de la lección según nombre de una lección
     * @param nombreLeccion
     * @return
     */
    public String buscarIdLeccionPorNombre(String nombreLeccion)
    {
        String columnas[] ={CN_ID_LECCION};
        String idLeccion = null;
        Cursor cursorLeccion=db.query(TABLE_LECCION,columnas, CN_NOMBRE_LECCION + " like ?", new String[]{nombreLeccion+"%"}, null, null, null);
        int columnNumLeccion=cursorLeccion.getColumnIndex(CN_ID_LECCION);					
		if (cursorLeccion.moveToNext()) {
				idLeccion = cursorLeccion.getString(columnNumLeccion);
		} 		
		return idLeccion; 
    }
    
    /**
     * Retorna nombre de la lección 
     * @param nombreLeccion
     * @return
     */
    public String buscarNombreLeccionPorId(String idLeccion)
    {
        String columnas[] ={CN_NOMBRE_LECCION};
        String nombreLeccion = null;
        Cursor cursorLeccion=db.query(TABLE_LECCION,columnas, CN_ID_LECCION + "=?", new String[]{idLeccion}, null, null, null);
		int columnNombreLeccion=cursorLeccion.getColumnIndex(CN_NOMBRE_LECCION);					
		if (cursorLeccion.moveToNext()) {
			nombreLeccion = cursorLeccion.getString(columnNombreLeccion);
		} 		
		return nombreLeccion; 
    }
    
    /**
	*Devuelve cantidad de palabras de una leccion 
	*@param nombreLeccion nombre de la leccion
	* 
	*/
	public int obtenerCantidadPalabraPorNombreLeccion (String nombreLeccion){
		int cantidad = 0;
		String idLeccion = buscarIdLeccionPorNombre(nombreLeccion);
		String Select = "SELECT count(fk_id_leccion) as cantidadPalabras FROM palabra where fk_id_leccion="+idLeccion+" ;";
		
		Cursor cantidadPalabras = db.rawQuery(Select, null);
				         
         if(cantidadPalabras.moveToNext()) {
             return cantidad = Integer.parseInt(cantidadPalabras.getString(cantidadPalabras.getColumnIndex("cantidadPalabras")));
         }
		
		return 0;
	}

    /*
     * Inicio Metodos de TABLA USUARIO 
     */
    /**
     * Método para desactivar usuario que se encuentra activo
     */
    private void desactivarUsuarios(){
    	ContentValues valores = contenedorValores(null,CN_ESTADO_USUARIO,"false");
        db.update(TABLE_USUARIO,valores,CN_ESTADO_USUARIO+"=?",new String[]{"true"});
    }
    
    /**
     * Retorna usuario Activo
     * @return
     */
    public Cursor cargarCursorUsuarioActivo() {
        String[] columnas = new String[]{CN_ID_USUARIO,CN_NOMBRE_USUARIO,CN_FECHA_NAC_USUARIO,CN_ESTADO_USUARIO,CN_SEXO_USUARIO,CN_FK_ID_NIVEL_USUARIO};
        
        return db.query(TABLE_USUARIO, columnas, CN_ESTADO_USUARIO+"=?", new String[]{"true"}, null, null, null);
    }
    
    /**
     * Retorna todos los usuarios creados en orden alfabético
     * @return
     */
    public Cursor cargarCursorUsuario() {
    	String[] columnas = new String[]{CN_ID_USUARIO,CN_NOMBRE_USUARIO,CN_FECHA_NAC_USUARIO,CN_ESTADO_USUARIO,CN_SEXO_USUARIO};
        
        return db.query(TABLE_USUARIO, columnas, null, null, null, null,CN_NOMBRE_USUARIO+" ASC");
    }
    
    /**
     * Método para insertar un nuevo usuario
     * 
     * @param nombre nombre de usuario
     * @param fechaNacimiento fecha de nacimiento del usuario
     * @param sexo sexo del usuario
     * @return
     */
    public boolean insertarUsuario(String nombre, String fechaNacimiento, String sexo) {
        String Edad = AgeManager.edad(fechaNacimiento);
        int nivel;
        switch (Edad) {
            case "3": nivel=1; break;
            case "4": nivel=2; break;
            case "5": nivel=3; break;
            default:  nivel=3; break;
        }
        ContentValues valores = null;
        desactivarUsuarios();
        valores = contenedorValores(valores,CN_NOMBRE_USUARIO, nombre);
        valores = contenedorValores(valores,CN_FECHA_NAC_USUARIO, fechaNacimiento);
        valores = contenedorValores(valores,CN_ESTADO_USUARIO, "true");
        valores = contenedorValores(valores,CN_SEXO_USUARIO, sexo);
        valores = contenedorValores(valores,CN_FK_ID_NIVEL_USUARIO, nivel+"");
        
        db.insert(TABLE_USUARIO, "CN_NOMBRE_USUARIO",valores);
        
        return true;
    }
   
    /**
     * Recibe el id del usuario que va a activar y desactiva el usuario actualmente activo en la bd
     * 
     */
    public void activarUsuario(String id) {
        
    	Cursor aux = cargarCursorUsuarioActivo();
        if (aux.moveToFirst())
        {
            if (!aux.getString(0).matches(id)){
                desactivarUsuarios();
                ContentValues valores = simpleContenedorValores(CN_ESTADO_USUARIO,"true");
                db.update(TABLE_USUARIO,valores,CN_ID_USUARIO+"=?",new String[]{id});
            }
        }
    }
	
	/**
     * Retorna el id del usuario ACTIVO como string
     * 
     * @return
     */
	public String idUsuarioActivo(){
  		
  		String[] columnas = new String[]{CN_ID_USUARIO};
		
		 Cursor cursor = db.query(TABLE_USUARIO, columnas, CN_ESTADO_USUARIO+"=?", new String[]{"true"}, null, null, null);
		 String id = "1";
		 
		 if (cursor.moveToFirst()) {
			 id = cursor.getString(0);
		 } 
  		
  		return id;
  	}
	
	/**
	 * Retorna nivel del usuario activo
	 */
	public int obtenerNivelUsuarioActivo(){
		int columnNivelUsuario = 0;
		int nivelUsuario =0;
		Cursor auxUsuario = cargarCursorUsuarioActivo();
        columnNivelUsuario=auxUsuario.getColumnIndex(CN_FK_ID_NIVEL_USUARIO);
        
        if (auxUsuario.moveToNext()) {
        	return nivelUsuario = auxUsuario.getInt(columnNivelUsuario);
        } else{
        	return nivelUsuario;
        } 
	}
    
    /**
     * Retorna la cantidad de palabras que tiene 
     * disponible el usuario que se encuentra activo
     * 
     * @return
     */
    public int obtenerCantPalabrasUsuarioActivo(String nombreLeccion) {
       
    	int cantidadPalabras = 0;
        int columnNivelUsuario = 0;
       	int nivelUsuario = obtenerNivelUsuarioActivo();
        
       	Cursor auxNivel = buscarCantPalabraPorNumeroNivel(Integer.toString(nivelUsuario));
        if (nivelUsuario!=3){
        	if(auxNivel.moveToNext()) {
        		return cantidadPalabras = Integer.parseInt(auxNivel.getString(auxNivel.getColumnIndex(CN_CANTIDAD_PALABRAS_NIVEL)));
            }
        }else {
        	return cantidadPalabras = obtenerCantidadPalabraPorNombreLeccion(nombreLeccion);
        }
        return 0; 
    }

    /**
     * 
     * @param campo
     * @param valor
     * @return
     */
    private ContentValues simpleContenedorValores(String campo, String valor){
        ContentValues valores = new ContentValues();
        valores.put(campo, valor);    
        return valores;
    }
    
    /**
     * Elimina un usuario según el nombre
     * @param nombreUsuario
     */
    public void eliminarUsuario(String nombreUsuario){
                
        String[] vector = new String[]{nombreUsuario};
        db.delete(TABLE_USUARIO,CN_NOMBRE_USUARIO+"=?", vector );     
    }
    
    /**
     * Elimina usuario según su identificador
     * @param idUsuario
     */
    public void eliminarUsuario(int idUsuario){
        
        String Myid = Integer.toString(idUsuario);
        String[] vector = new String[]{Myid};
        db.delete(TABLE_USUARIO,CN_ID_USUARIO+"=?", vector );
    }
    
    /**
     * Modificar un usuario
     * @param id
     * @param nombre
     */
    public void modificarUsuario(int id, String nombre){
        
        ContentValues valores = simpleContenedorValores(CN_ID_USUARIO, ""+id);
        //Nombre tabla, Valores a actualizar, Clausula Where y Valor del where
        db.update(TABLE_USUARIO,contenedorValores(valores, CN_NOMBRE_USUARIO, nombre) , CN_ID_USUARIO+"=?", new String[]{""+id});
    }
    
    /**
     * Modificar un usuario activo
     * 
     * @param nombre nombre del usuario
     * @param fechaNac fecha de nacimiento del usuario
     * @param sexo sexo del usuario
     */
    public void modificarUsuarioActivo(String nombre, String fechaNac, String sexo){
        
        ContentValues valores = simpleContenedorValores(CN_NOMBRE_USUARIO, nombre);
        valores = contenedorValores(valores,CN_FECHA_NAC_USUARIO,fechaNac);
        valores = contenedorValores(valores,CN_SEXO_USUARIO,sexo);
        db.update(TABLE_USUARIO,valores,CN_ESTADO_USUARIO+"=?",new String[]{"true"});
    }
   
    /**
     * Modifica el estado de un usuario
     * @param nombre nombre del usuario
     * @param estado estado del usuario true = activo false = inactivo
     */
    public void modificarEstadoUsuarioPorNombre(String nombre, String estado){
        
    	ContentValues valores  = new ContentValues();
        valores.put(CN_ESTADO_USUARIO, estado);
        db.update(TABLE_USUARIO, valores, CN_NOMBRE_USUARIO+"=?",  new String[]{nombre});
    }
    
    /**
     * Modifica nivel de usuario activo
     */
    public void modificarNivelUsuarioActivo(){
        
    	int nivelUsuarioActivo = obtenerNivelUsuarioActivo()+1;
    	String idUsuarioActivo = idUsuarioActivo();
    	ContentValues valores  = new ContentValues();
        valores.put(CN_FK_ID_NIVEL_USUARIO, nivelUsuarioActivo);
        db.update(TABLE_USUARIO, valores, CN_ID_USUARIO+"=?",  new String[]{idUsuarioActivo});
    }
    
    /**
     * 
     * @param usuario
     * @return
     */
    public Cursor cargarCursorUsuarioEspecifico(String usuario) {
        
        String[] columnas = new String[]{CN_ID_USUARIO,CN_NOMBRE_USUARIO,CN_FECHA_NAC_USUARIO};
        
        return db.query(TABLE_USUARIO, columnas, CN_NOMBRE_USUARIO+"=?", new String[]{usuario}, null, null, null);
    }
    
    /**
     * 
     * @param usuario
     * @return
     */
    public Cursor cargarCursorUsuariosParecidos(String usuario) {
        
        String[] columnas = new String[]{CN_ID_USUARIO,CN_NOMBRE_USUARIO,CN_FECHA_NAC_USUARIO};
        
        return db.query(TABLE_USUARIO, columnas, CN_NOMBRE_USUARIO+" like ?", new String[]{"%"+usuario+"%"}, null, null, null);
    }
    
    /**
     * Método para insertar data que relacione el usuario a una palabra
     * @param fkIdUsuario ideentificador del usuario
     * @param fkIdPalabra identificador de palabra
     * @param contadorAciertos cantidad de veces que ha respondido correctamente esa palabra
     * @param contadorIntentos cantidad de veces que ha presentado las pruebas
     * @param tipoTest word = test de palabra, voice = test de voz 
     * @param tiempoLearn tiempo que ha invertido en aprender la palabra
     * @param tiempoPract tiempo que ha invertido en practicar la palabra
     */
    public void insertarUsuarioPalabra(String fkIdUsuario, String fkIdPalabra, String contadorAciertos, String contadorIntentos, String tipoTest, String tiempoLearn, String tiempoPract){
          
          ContentValues valores = null;
          
          valores = contenedorValores(valores,CN_FK_ID_USUARIO, fkIdUsuario);
          valores = contenedorValores(valores,CN_FK_ID_PALABRA, fkIdPalabra);
          valores = contenedorValores(valores,CN_CONTADOR_ACIERTOS, contadorAciertos);
          valores = contenedorValores(valores,CN_CONTADOR_INTENTOS, contadorIntentos);
          valores = contenedorValores(valores,CN_TIEMPO_LEARN , tiempoLearn);
          valores = contenedorValores(valores,CN_TIEMPO_PRACT, tiempoPract);
          valores = contenedorValores(valores,CN_TIPO_TEST, tipoTest);
          
          //en el null tiene que ir los campos que pueden se null en la tabla
          db.insert(TABLE_USUARIO_PALABRA, null, valores);
          
      }
    
    /**
     * 
     * @param fkIdUsuario
     * @param fkIdPalabra
     * @param tipoTest
     * @return
     */
    public Cursor obtenerUsuarioPalabraPorTipo (String fkIdUsuario, String fkIdPalabra, String tipoTest){
        
        String [] columnas = new String [] {CN_ID_USUARIO_PALABRA,CN_CONTADOR_ACIERTOS,CN_CONTADOR_INTENTOS,CN_TIEMPO_LEARN,CN_TIEMPO_PRACT};
         
        return db.query(TABLE_USUARIO_PALABRA, columnas, CN_FK_ID_USUARIO + "=?" + " and " + CN_FK_ID_PALABRA + "=?" + " and " + CN_TIPO_TEST + "=?",new String[] {fkIdUsuario,fkIdPalabra,tipoTest}, null, null, null);

    }
    
    /**
     * 
     * @param idUsuarioPalabra
     * @param contadorAciertos
     * @param contadorIntentos
     */
    public void actualizarContadoresUsuarioPalabra (String idUsuarioPalabra, String contadorAciertos, String contadorIntentos){
        
        ContentValues valores = null;
        
        valores= contenedorValores(valores, CN_CONTADOR_ACIERTOS, contadorAciertos);
        valores= contenedorValores(valores, CN_CONTADOR_INTENTOS, contadorIntentos);
        
        db.update(TABLE_USUARIO_PALABRA, valores, CN_ID_USUARIO_PALABRA+"=?", new String[] {idUsuarioPalabra});
    }
    
    /**
     * 
     * @param idUsuarioPalabra
     * @param tiempoLearn
     */
    public void actualizarTiempoLearnUsuarioPalabra (String idUsuarioPalabra, String tiempoLearn){
        
        ContentValues valores = null; 
        valores= contenedorValores(valores, CN_TIEMPO_LEARN, tiempoLearn);
            
        db.update(TABLE_USUARIO_PALABRA, valores, CN_ID_USUARIO_PALABRA+"=?", new String[] {idUsuarioPalabra});
    }
    
    /**
     * 
     * @param idUsuarioPalabra
     * @param tiempoPract
     */
    public void actualizarTiempoPracticeUsuarioPalabra (String idUsuarioPalabra, String tiempoPract){
        
        ContentValues valores = null; 
        valores= contenedorValores(valores, CN_TIEMPO_PRACT, tiempoPract);
               
        db.update(TABLE_USUARIO_PALABRA, valores, CN_ID_USUARIO_PALABRA+"=?", new String[] {idUsuarioPalabra});
    }
	
	
	/**
	*Devuelve todos los registros de usuario palabra pertenecientes a un usario y una leccion.
	*@param idUsuario Id de un usuario
	*@param idLeccion Id de una leccion
	* 
	*/
	public Cursor obtenerUsuarioPalabraPorLeccion (String idUsuario, String idLeccion){
		
		String Select = "SELECT UP.*, P."+CN_NOMBRE_PALABRA+" FROM "+TABLE_LECCION+" AS L,"+TABLE_USUARIO+" AS U,"+TABLE_PALABRA+" AS P,"+
						TABLE_USUARIO_PALABRA+" AS UP WHERE "+
						"U."+CN_ID_USUARIO+"=UP."+CN_FK_ID_USUARIO+" AND P."+CN_ID_PALABRA+"=UP."+CN_FK_ID_PALABRA+" AND L."+CN_ID_LECCION+"=P."+CN_FK_ID_LECCION_PALABRA+" AND U."+CN_ID_USUARIO+"="+idUsuario+" AND L."+CN_ID_LECCION+"="+idLeccion+";";
		
		System.out.println(Select);
		
		return db.rawQuery(Select, null);
	}
	
	/**
	*Devuelve todos los registros de usuario palabra pertenecientes a un usario y una leccion.
	*@param idUsuario Id de un usuario
	*@param idLeccion Id de una leccion
	* 
	*/
	public Cursor obtenerDetalleUsuarioPalabraPorLeccion (String idUsuario, String idLeccion){
		//SELECT SUM(UP.contador_aciertos) AS aciertos, SUM(UP.contador_intentos) AS intentos, P.nombre_palabra 
		String Select = "SELECT SUM(UP."+CN_CONTADOR_ACIERTOS+") AS aciertos, SUM(UP."+CN_CONTADOR_INTENTOS+") AS intentos, P."+CN_NOMBRE_PALABRA+" FROM "+TABLE_LECCION+" AS L,"+TABLE_USUARIO+" AS U,"+TABLE_PALABRA+" AS P,"+
						TABLE_USUARIO_PALABRA+" AS UP WHERE "+
						"U."+CN_ID_USUARIO+"=UP."+CN_FK_ID_USUARIO+" AND P."+CN_ID_PALABRA+"=UP."+CN_FK_ID_PALABRA+" AND L."+CN_ID_LECCION+"=P."+CN_FK_ID_LECCION_PALABRA+" AND U."+CN_ID_USUARIO+"="+idUsuario+" AND L."+CN_ID_LECCION+"="+idLeccion+" GROUP BY P."+CN_NOMBRE_PALABRA+" ;";
		
		System.out.println(Select);
		
		return db.rawQuery(Select, null);
	}
	
	/**
	*Devuelve cantidad de aciertos 
	*@param idUsuario Id de un usuario
	*@param idLeccion Id de una leccion
	* 
	*/
	public int obtenerCantidadAciertosUsuarioPalabra (String idUsuario, String idPalabra){
		int cantidad = 0;
		String Select = "SELECT SUM(contador_aciertos) as cantidad FROM usuario_palabra where fk_id_usuario="+idUsuario+" and fk_id_palabra="+idPalabra+" ;";
		
		Cursor cantidadAciertos = db.rawQuery(Select, null);
				         
         if(cantidadAciertos.moveToNext()) {
             return cantidad = Integer.parseInt(cantidadAciertos.getString(cantidadAciertos.getColumnIndex("cantidad")));
         }
		
		return 0;
	}
	
	/**
	*Devuelve cantidad de aciertos 
	*@param idUsuario Id de un usuario
	*@param idLeccion Id de una leccion
	* 
	*/
	public int obtenerCantidadRegistrosUsuarioPalabra (String idUsuario){
		int cantidad = 0;
		String Select = "SELECT count(id_usuario_palabra) as cantidadRegistros FROM usuario_palabra where fk_id_usuario = "+idUsuario+" and  contador_aciertos >= 1 ;";
		
		Cursor cantidadAciertos = db.rawQuery(Select, null);
				         
         if(cantidadAciertos.moveToNext()) {
             return cantidad = Integer.parseInt(cantidadAciertos.getString(cantidadAciertos.getColumnIndex("cantidadRegistros")));
         }
		
		return 0;
	}
	
	/**
	 * Devuelve la cantidad de palabras aprendidas por un usuario
	 * */
	public Integer obtenerPalabrasAprendidas(String idUsuario){
		
		String Select = "Select count(*) AS palabras_aprendidas from (SELECT COUNT(UP."+CN_FK_ID_USUARIO+") FROM "+TABLE_USUARIO_PALABRA+" AS UP where UP."+CN_FK_ID_USUARIO+"="+idUsuario+" GROUP BY UP."+CN_FK_ID_PALABRA+");";
		System.out.println(Select);
		Cursor cursor = db.rawQuery(Select, null);
		Integer palabras_aprendidas = 0;
		if (cursor.moveToFirst()){
			palabras_aprendidas = cursor.getInt(0);
		}
		
		return palabras_aprendidas;
	}
	
	/**
	 * Devuelve la cantidad Total de palabras que se pueden aprender
	 * */
	public Integer obtenerCantidadPalabras(){
		
		String Select = "SELECT count(*) AS cantidad_palabras from "+TABLE_PALABRA+";";
		System.out.println(Select);
		Cursor cursor =  db.rawQuery(Select, null);
		Integer cantidadPalabras=0;
		if(cursor.moveToFirst()) cantidadPalabras=cursor.getInt(0);
		
		return cantidadPalabras;
	}
	
	/**
	 * Devuelve el id de la ultima palabra insertada
	 */
	public Integer obtenerIdUltimaPalabra(){
		
		String Select = "SELECT MAX(id_palabra) AS id FROM "+TABLE_PALABRA+";";
		System.out.println(Select);
		Cursor cursor =  db.rawQuery(Select, null);
		Integer idPalabra=0;
		if(cursor.moveToFirst()) idPalabra=cursor.getInt(0);
		
		return idPalabra;
	}
	
	public boolean verificarUsuario(String nombreUsuario, String fechaUsuairo){
		
		 String[] columnas = new String[]{CN_ID_USUARIO};
		 Cursor consulta = db.query(TABLE_USUARIO, columnas, CN_NOMBRE_USUARIO+"=? and "+CN_FECHA_NAC_USUARIO+"=?", new String[]{nombreUsuario,fechaUsuairo}, null, null, null);   
	     //return db.query(TABLE_USUARIO, columnas, CN_ESTADO_USUARIO+"=?", new String[]{"true"}, null, null, null);
		 
		 if (consulta.moveToFirst()) return true;
		 else return false;
	}
	
	/**
	 * Devuelve nombre de la palabra si existe
	 */
	public boolean existeNombrePalabra(String nombrePalabra){
		
		String Select = "SELECT nombre_palabra AS nombre FROM "+TABLE_PALABRA+" where nombre_palabra = '"+nombrePalabra+"';";
		System.out.println(Select);
		Cursor cursor =  db.rawQuery(Select, null);
		String existeNombrePalabra= "" ;
		if(cursor.moveToFirst()) 
			existeNombrePalabra=cursor.getString(0);
		
		if (existeNombrePalabra.equalsIgnoreCase(""))
			return false;
		else 
			return true;
	}
	
	/**
	 * Devuelve nombre de la palabra si existe
	 */
	public boolean existeIDPalabra (int idPalabra){
		
		String Select = "SELECT id_palabra AS id FROM "+TABLE_PALABRA+" where id_palabra ="+idPalabra +";";
		System.out.println(Select);
		Cursor cursor =  db.rawQuery(Select, null);
		Integer existeIdPalabra= 0 ;
		if(cursor.moveToFirst()) 
			existeIdPalabra=cursor.getInt(0);
		
		if (existeIdPalabra == 0)
			return false;
		else 
			return true;
	}
	
	/**
	 * edita el nombre de la palabra
	 */
	
	 public void editarNombrePalabra (int idPalabra, String nuevoNombre){
	        
	        ContentValues valores = simpleContenedorValores(CN_NOMBRE_PALABRA, ""+nuevoNombre);
	        //Nombre tabla, Valores a actualizar, Clausula Where y Valor del where
	        db.update(TABLE_PALABRA,valores, CN_ID_PALABRA+"=?", new String[]{""+idPalabra});
	    }
	
}