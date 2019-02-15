/*
 * @(#) AgeManager.java  1 01/032/15
 * 
 * Copyrigth (c) 2015 Jorge Hern�ndez, Yuzmhar Guill�n
 * Caracas, Venezuela
 * Todos los derechos reservados.
 *   
 */
package com.yujoglish.configuracion.usuario;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import android.net.ParseException;

/**
 * Clase que se encarga de realizar c�lculos de fechas y tiempos
 *  
 * @author Yuzmhar Guill�n
 * @author Jorge Hern�ndez
 * @version 1, 01/03/2015 
 */
public class AgeManager {
	
	/**
	 * 
	 * @param fechaNacimiento
	 * @return
	 */
	public static String edad(String fechaNacimiento) {
				
		String arregloFecha[] = fechaNacimiento.split("/");
		int dia=Integer.parseInt(arregloFecha[0]);
		int mes=Integer.parseInt(arregloFecha[1]);
		int ano=Integer.parseInt(arregloFecha[2]);
		
		LocalDate birthdate = new LocalDate (ano, mes, dia);  //Birth date
		LocalDate now = new LocalDate(); //Today's date
		//Years age = Years.yearsBetween(birthdate, now);
         
		Period period = new Period(birthdate, now, PeriodType.yearMonthDay());
		//Now access the values as below
		//System.out.println(period.getDays());
		//System.out.println(period.getMonths());
		String edad = Integer.toString(period.getYears());
		
		return edad;
	}
	
	/**
	 * M�todo para obtener hora actual
	 * @return hora actual en formato HH:mm:ss
	 */
	public static String obtenerTiempo(){
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		
		return dateFormat.format(cal.getTime());
	}
	
	/**
	 * M�todo que c�lcula diferendia de tiempos
	 * 
	 * @param tiempoInicio
	 * @param tiempoFin
	 * @return diferencia de tiempos
	 */
	public static long diferenciaEntreTiempos(String tiempoInicio, String tiempoFin){	
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("HH:mm:ss");
		long diferencia = 0;
		try {
	 
			DateTime dateInicio = dateFormat.parseDateTime(tiempoInicio);
			DateTime dateFin = dateFormat.parseDateTime(tiempoFin);
					
			Interval interval = new Interval(dateInicio, dateFin);
			Duration duration = interval.toDuration();
			diferencia = duration.getStandardSeconds();
			
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		return diferencia;		
	}

}
