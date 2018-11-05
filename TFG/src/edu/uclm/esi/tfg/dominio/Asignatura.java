package edu.uclm.esi.tfg.dominio;

public class Asignatura {
	private int id;
	private String nombre;
	private int curso;
	
	public Asignatura(String asignatura, int curso) {
		this.nombre = asignatura;
		this.curso = curso;
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
	public int getCurso() {
		return curso;
	}
	public void setCurso(int curso) {
		this.curso = curso;
	}
	
}
