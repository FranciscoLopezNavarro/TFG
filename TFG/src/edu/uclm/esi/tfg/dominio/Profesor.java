package edu.uclm.esi.tfg.dominio;

import java.io.IOException;

import javax.websocket.Session;

import org.json.JSONObject;

public class Profesor {
	private int id;
	private String pwd;
	private String nombre;
	private String apellidos;
	
	private Session session;
	
	
	public Profesor(int id, String pwd) {
	
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
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getNombre() {
		return nombre;
	}
	public void enviar(JSONObject jso) throws IOException{
		this.session.getBasicRemote().sendText(jso.toString());
	}
}
