package edu.uclm.esi.tfg.dominio;

public class Calificacion {
	private int alumno;
	private int prueba;
	private double nota;
	private String year;
	
	public Calificacion(Integer alumno,Integer prueba, Double nota, String year) {
		this.alumno = alumno;
		this.prueba = prueba;
		this.nota = nota;
		this.year = year;
	}
	public Calificacion() {
		// TODO Auto-generated constructor stub
	}
	public int getAlumno() {
		return alumno;
	}
	public void setAlumno(int alumno) {
		this.alumno = alumno;
	}
	public int getPrueba() {
		return prueba;
	}
	public void setPrueba(int prueba) {
		this.prueba = prueba;
	}
	public double getNota() {
		return nota;
	}
	public void setNota(double nota) {
		this.nota = nota;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
}
