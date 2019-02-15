package com.yujoglish.model;

public class Leccion {

	private int idLeccion;
	private int numeroLeccion;
	private String nombre;
	
	public Leccion(int idLeccion, int numeroLeccion, String nombre) {
		super();
		this.idLeccion = idLeccion;
		this.numeroLeccion = numeroLeccion;
		this.nombre = nombre;
	}
	public Leccion(int numeroLeccion, String nombre) {
		super();
		this.numeroLeccion = numeroLeccion;
		this.nombre = nombre;
	}
	public int getIdLeccion() {
		return idLeccion;
	}
	public void setIdLeccion(int idLeccion) {
		this.idLeccion = idLeccion;
	}
	public int getNumeroLeccion() {
		return numeroLeccion;
	}
	public void setNumeroLeccion(int numeroLeccion) {
		this.numeroLeccion = numeroLeccion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
