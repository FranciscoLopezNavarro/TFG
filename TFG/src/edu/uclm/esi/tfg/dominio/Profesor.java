package edu.uclm.esi.tfg.dominio;


public class Profesor {

	private int id;
	private String pwd;
	private String nombre;

	public Profesor(int id, String pwd) {
		this.id = id;
		this.pwd = pwd;
	}
	public Profesor(int id, String pwd, String nombre) {
		this.id = id;
		this.pwd = pwd;
		this.nombre = nombre;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getNombre() {
		return nombre;
	}
}
