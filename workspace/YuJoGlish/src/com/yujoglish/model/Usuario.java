package com.yujoglish.model;

public class Usuario {

	private int idUsuario;
	private String nombre;
	private String edad;
	private String sexo;
	private boolean estado;
	private int fkIdNivel;
	
	
	public Usuario(int id, String nombre, String edad, String sexo, String estado, int idNivel) {
		super();
		this.idUsuario = id;
		this.nombre = nombre;
		this.edad = edad;
	    this.sexo = sexo;	
	    this.estado = true;	
	    this.fkIdNivel = idNivel;
	}
	
	public Usuario(String nombre, String edad, String sexo, String estado, int idNivel) {
		super();
		this.nombre = nombre;
		this.edad = edad;
	    this.sexo = sexo;	
	    this.estado = true;	
	    this.estado = false;	
	    this.fkIdNivel = idNivel;
	}
	
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
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
	
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public int getFkIdNivel() {
		return fkIdNivel;
	}

	public void setFkIdNivel(int fkIdNivel) {
		this.fkIdNivel = fkIdNivel;
	}
	
    
	
}
