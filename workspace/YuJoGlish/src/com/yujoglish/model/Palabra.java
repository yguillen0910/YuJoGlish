package com.yujoglish.model;

public class Palabra {
	
	private int idPalabra;
	private String nombre;
	private int posicion;
	private int fkIdLeccion;
	
	public Palabra(int idPalabra, String nombre, int posicion, int fkIdLeccion) {
		super();
		this.idPalabra = idPalabra;
		this.nombre = nombre;
		this.posicion = posicion;
		this.fkIdLeccion = fkIdLeccion;
	}
	public Palabra(String nombre, int posicion, int fkIdLeccion) {
		super();
		this.nombre = nombre;
		this.posicion = posicion;
		this.fkIdLeccion = fkIdLeccion;
	}
	public int getIdPalabra() {
		return idPalabra;
	}
	public void setIdPalabra(int idPalabra) {
		this.idPalabra = idPalabra;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getPosicion() {
		return posicion;
	}
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}
	public int getFkIdLeccion() {
		return fkIdLeccion;
	}
	public void setFkIdLeccion(int fkIdLeccion) {
		this.fkIdLeccion = fkIdLeccion;
	}
}
