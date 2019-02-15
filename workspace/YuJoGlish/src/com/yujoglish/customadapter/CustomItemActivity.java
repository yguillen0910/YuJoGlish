/*
 * @(#) CustomItemActivity.java  1 01/03/15
 * 
 * Copyrigth (c) 2015 Jorge Hernández, Yuzmhar Guillén
 * Caracas, Venezuela
 * Todos los derechos reservados.
 *   
 */
package com.yujoglish.customadapter;

/**
 * Clase que define un CustomItemActivity
 * 
 * @author Jorge Hernández
 * @author Yuzmhar Guillén
 * @version 1, 01/03/2015 
 */
public class CustomItemActivity {
	
	String nombre;
	String edad;
	String imagen;
	int cantidadPalabra;
	int totalPalabra;
	String palabras;
	String id;
	String sexo;
	
	public CustomItemActivity(String id,String nombre, String edad, String imagen,
			int cantidadPalabra, int totalPalabra, String sexo) {
		super();
		
		this.id =id;
		this.nombre = nombre;
		this.edad = edad;
		this.imagen = imagen;
		this.cantidadPalabra = cantidadPalabra;
		this.totalPalabra = 72;
		this.palabras = cantidadPalabra+" de "+totalPalabra+" palabras";
		this.sexo = sexo;
	}
	
	public CustomItemActivity(String id,String nombre, String edad,
			int cantidadPalabra, int totalPalabra, String sexo) {
		super();
		
		this.id =id;
		this.nombre = nombre;
		this.edad = edad;
		this.imagen = imagen;
		this.cantidadPalabra = cantidadPalabra;
		this.totalPalabra = 72;
		this.palabras = cantidadPalabra+" de "+totalPalabra+" palabras";
		this.sexo = sexo;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEdad() {
		return edad;
	}
	public void setEdad(String edad) {
		this.edad = edad;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	public int getCantidadPalabra() {
		return cantidadPalabra;
	}
	public void setCantidadPalabra(int cantidadPalabra) {
		this.cantidadPalabra = cantidadPalabra;
	}
	public int getTotalPalabra() {
		return totalPalabra;
	}
	public void setTotalPalabra(int totalPalabra) {
		this.totalPalabra = totalPalabra;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getSexo() {
		return sexo;
	}
	
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	
	public String getPalabras() {
		return palabras;
	}

	public void setPalabras(String palabras) {
		this.palabras = palabras;
	}
	
	
}
