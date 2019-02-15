package com.yujoglish.stadistics;

import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import com.yujoglish.customadapter.DetailStadisticsCustomItem;
import com.yujoglish.dbhelper.DataBaseManager;

public class UtilStadistics {
	
	public static String aciertosLeccionUsuario(String nombreLeccion, Context v){
		Cursor cursor;
		DataBaseManager managerDB;
		
		managerDB = new DataBaseManager(v);
		
		cursor = managerDB.obtenerUsuarioPalabraPorLeccion(managerDB.idUsuarioActivo(),managerDB.buscarLeccionPorNombre(nombreLeccion));
		Integer contadorAciertos = 0;
		Integer contadorIntentos = 0;
		Integer contadorTiempoPract = 0;
		Integer contadorTiempoAprend = 0;
		
		if (cursor.moveToFirst()){
			do{
				//System.out.println("UNTIL STADISTICS - id:"+cursor.getString(0)+" UsuarioID: "+cursor.getString(1)+" PalabraID: "+cursor.getString(2)+" Aciertos: "+cursor.getString(3)+" Intentos: "+cursor.getString(4));
				contadorAciertos = contadorAciertos + Integer.parseInt(cursor.getString(3));
				contadorIntentos = contadorIntentos + Integer.parseInt(cursor.getString(4));
				contadorTiempoAprend = contadorTiempoAprend + Integer.parseInt(cursor.getString(6));
				contadorTiempoPract = contadorTiempoPract + Integer.parseInt(cursor.getString(7));
			}while(cursor.moveToNext());
		}
		contadorTiempoAprend = contadorTiempoAprend/60;
		contadorTiempoPract = contadorTiempoPract/60;
		
		//Cursor cursor = manager.buscarPalabraPorLeccion(IDLeccion);	
		return contadorAciertos.toString() +":"+ contadorIntentos.toString() +":"+ contadorTiempoAprend.toString() +":"+ contadorTiempoPract.toString();
	}
	
	public static ArrayList<DetailStadisticsCustomItem> DetallePalabrasUsuario(String nombreLeccion, Context v){
		Cursor cursor;
		DataBaseManager managerDB;
		
		managerDB = new DataBaseManager(v);
		DetailStadisticsCustomItem auxDetailStadisticsCustomItem = new DetailStadisticsCustomItem();
		ArrayList<DetailStadisticsCustomItem> lista = new ArrayList<DetailStadisticsCustomItem>(); 
		
		cursor = managerDB.obtenerDetalleUsuarioPalabraPorLeccion(managerDB.idUsuarioActivo(),managerDB.buscarLeccionPorNombre(nombreLeccion));

		
		if (cursor.moveToFirst()){
			do{				
				auxDetailStadisticsCustomItem.setAciertos(Integer.parseInt(cursor.getString(0)));
				auxDetailStadisticsCustomItem.setIntentos(Integer.parseInt(cursor.getString(1)));
				auxDetailStadisticsCustomItem.setNombre(cursor.getString(2));
				lista.add(auxDetailStadisticsCustomItem);	
				System.out.println("UNTIL STADISTICS DETAILS - "+ " Nombre palabra:"+auxDetailStadisticsCustomItem.getNombre()+" Aciertos:"+auxDetailStadisticsCustomItem.getAciertos()+" Intentos:"+auxDetailStadisticsCustomItem.getIntentos());
				auxDetailStadisticsCustomItem = new DetailStadisticsCustomItem();
				
			}while(cursor.moveToNext());
		}
	
		return lista;
	}
	
	
	
}
