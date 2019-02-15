package com.yujoglish.model;

public class Nivel {
	
	private int idNivel;
	private int numeroNivel;
	private int cantidadPalabras;
	
	public Nivel(int idNivel, int numeroNivel, int cantidadPalabras) {
		super();
		this.idNivel = idNivel;
		this.numeroNivel = numeroNivel;
		this.cantidadPalabras = cantidadPalabras;
	}
	
	public Nivel(int numeroNivel, int cantidadPalabras) {
		super();
		this.numeroNivel = numeroNivel;
		this.cantidadPalabras = cantidadPalabras;
	}
	
	public int getIdNivel() {
		return idNivel;
	}
	public void setIdNivel(int idNivel) {
		this.idNivel = idNivel;
	}
	public int getNumeroNivel() {
		return numeroNivel;
	}
	public void setNumeroNivel(int numeroNivel) {
		this.numeroNivel = numeroNivel;
	}
	public int getCantidadPalabras() {
		return cantidadPalabras;
	}
	public void setCantidadPalabras(int cantidadPalabras) {
		this.cantidadPalabras = cantidadPalabras;
	}
	
	

}
