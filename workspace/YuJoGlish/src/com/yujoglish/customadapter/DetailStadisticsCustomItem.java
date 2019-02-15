package com.yujoglish.customadapter;

public class DetailStadisticsCustomItem {
	
	private int id;
	private String nombre;
	private int aciertos;
	private int intentos;
	
	public DetailStadisticsCustomItem() {
		// TODO Auto-generated constructor stub
	}

	public DetailStadisticsCustomItem(int id, String nombre, int aciertos,
			int intentos) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.aciertos = aciertos;
		this.intentos = intentos;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getAciertos() {
		return aciertos;
	}

	public void setAciertos(int aciertos) {
		this.aciertos = aciertos;
	}

	public int getIntentos() {
		return intentos;
	}

	public void setIntentos(int intentos) {
		this.intentos = intentos;
	}
	
}
