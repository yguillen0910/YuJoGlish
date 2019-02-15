package com.yujoglish.util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import com.yujoglish.dbhelper.DataBaseManager;

public class Util extends Activity {
	
			
	/**
	 * Método para guardar tiempo en que estuvo activa la actividad
	 * 
	 * @param tiempo Tiempo que estuvo activa la actividad
	 * @param tipo puede ser voice o word indica el tipo de practica
	 * @param idPalabraCorrecta identificador de la palabra correcta
	 */
	public static String IP_SERVIDOR = "192.168.1.108";
	
	public static void guardarTiempoPracticePalabraUsuario(Integer tiempo, String tipo, String idPalabraCorrecta, Context context) {
		
		DataBaseManager manager;
		Integer tiempoPractice = null;
		Integer	idUsuarioPalabra = 0;
		String idUsuarioActivo = null;
		manager = new DataBaseManager(context);
		
		Cursor cursorUsuarioActivo = manager.cargarCursorUsuarioActivo();
		if (cursorUsuarioActivo.moveToNext()){
			idUsuarioActivo = cursorUsuarioActivo.getString(cursorUsuarioActivo.getColumnIndex(manager.CN_ID_USUARIO));
		}
		
		Cursor cusorPalabraUsuario = manager.obtenerUsuarioPalabraPorTipo(idUsuarioActivo, idPalabraCorrecta.toString(), tipo);
		if (cusorPalabraUsuario.moveToNext()) {
			tiempoPractice = cusorPalabraUsuario.getInt(cusorPalabraUsuario.getColumnIndex(manager.CN_TIEMPO_PRACT))+tiempo;
			idUsuarioPalabra = cusorPalabraUsuario.getInt(cusorPalabraUsuario.getColumnIndex(manager.CN_ID_USUARIO_PALABRA));
			manager.actualizarTiempoPracticeUsuarioPalabra(idUsuarioPalabra.toString(), tiempoPractice.toString());
		}else{
			manager.insertarUsuarioPalabra(idUsuarioActivo, idPalabraCorrecta.toString(), "0", "0", tipo, "0",tiempo.toString());
		}
	}
	
	/**
	 * Método para guardar Cantidad de Aciertos en los Test
	 * 
	 * @param cantAciertos cantidad de aciertos
	 * @param tipo Tipo de Test
	 * @param idPalabraCorrecta identificador de la palabra correcta
	 */
	public static void guardarCantidadAciertosPalabraUsuario(Integer cantAciertos, String tipo, Integer idPalabraCorrecta, Context context){
		 
		DataBaseManager manager;
		manager = new DataBaseManager(context);
		String idUsuarioActivo = null;
		Integer contadorAciertos = 0;
		Integer contadorIntentos = 0;
		Integer idPalabraUsuario = 0;
		
		Cursor cursorUsuarioActivo = manager.cargarCursorUsuarioActivo();
		if (cursorUsuarioActivo.moveToNext()) {
			idUsuarioActivo = cursorUsuarioActivo.getString(cursorUsuarioActivo.getColumnIndex(manager.CN_ID_USUARIO));
		}
		
		Cursor cusorPalabraUsuario = manager.obtenerUsuarioPalabraPorTipo(idUsuarioActivo, idPalabraCorrecta.toString(), tipo);
		if (cusorPalabraUsuario.moveToNext()) {
			contadorAciertos = cusorPalabraUsuario.getInt(cusorPalabraUsuario.getColumnIndex(manager.CN_CONTADOR_ACIERTOS))+cantAciertos;
			contadorIntentos = cusorPalabraUsuario.getInt(cusorPalabraUsuario.getColumnIndex(manager.CN_CONTADOR_INTENTOS))+1;
			idPalabraUsuario = cusorPalabraUsuario.getInt(cusorPalabraUsuario.getColumnIndex(manager.CN_ID_USUARIO_PALABRA));
			manager.actualizarContadoresUsuarioPalabra(idPalabraUsuario.toString(), contadorAciertos.toString(), contadorIntentos.toString());
		}else {
			manager.insertarUsuarioPalabra(idUsuarioActivo, idPalabraCorrecta.toString(), cantAciertos.toString(), "1", tipo, "0", "0");
		}
	}
	
	public static Integer[] progressbarUsuario(String idUsuario, Context context){
		
		DataBaseManager manager;
		manager = new DataBaseManager(context);
		Integer progressBar[] = new Integer[2];
		
		progressBar[0]=manager.obtenerCantidadPalabras();
		progressBar[1]=manager.obtenerPalabrasAprendidas(idUsuario);
		
		return progressBar;
	}

}
