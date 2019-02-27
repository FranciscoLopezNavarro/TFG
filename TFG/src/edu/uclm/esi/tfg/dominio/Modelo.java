package edu.uclm.esi.tfg.dominio;

import java.util.ArrayList;

public class Modelo {

	private ArrayList<Double> desviacionesAlumno = new ArrayList<Double>();
	private ArrayList<Double> desviacionesAsignatura = new ArrayList<Double>();
	private ArrayList<Double> probabilidades = new ArrayList<Double>();

	int DNI = 333333333;
	public double calcularAlerta(Integer alumno) {
		return alerta();
	}
	//PRIMER CALCULO///////// DESVIACION DEL ALUMNO FRENTE A LA NOTA DE CORTE
	public double desviacionAlumno(double nota_alumno, double nota_min, double nota_corte, double nota_max){
		double D = 0.0;
		if(nota_alumno >= nota_corte) {
			D = (nota_alumno - nota_corte)/(nota_max - nota_corte);
		}else {
			D= (nota_alumno - nota_corte)/(nota_corte - nota_min);
		}
		desviacionesAlumno.add(D);
		return D;
	}
	public double desviacionAcumuladaAlumno() {
		//Falta estructura de datos de desviaciones por prueba respecto a la nota de corte
		Double D = 0.0;
		Double D_aux = 0.0;
		int n = desviacionesAlumno.size();

		for(int i = 0; i<n;i++) {
			D_aux += desviacionesAlumno.get(i);
		}
		D = D_aux/n;
		return D;
	}

	//SEGUNDO CALCULO///////// DESVIACION DEL ALUMNO FRENTE A LA MEDIA DE OTROS ALUMNOS (HISTORICOS)
	public double desviacionAsignatura(double nota_alumno, double nota_min, double nota_max, double media_alumnos) {
		double D = 0.0;

		D = (nota_alumno - media_alumnos)/(nota_max - nota_min);
		desviacionesAsignatura.add(D);
		return D;
	}
	public double desviacionAcumuladaAsignatura() {
		//Falta estructura de datos de desviaciones por prueba respecto a la media de alumnos
		Double D = 0.0;
		Double D_aux = 0.0;
		int n = desviacionesAsignatura.size();

		for(int i = 0; i<n;i++) {
			D_aux += desviacionesAsignatura.get(i);
		}
		D = D_aux/n;
		return D;
	}

	//TERCER CALCULO/////////

	public double probabilidadAsignatura(int aprobadosPrueba,int aprobadosAsig, int totalAlumnos) {
		double P = 0.0;
		P = (aprobadosAsig/totalAlumnos)/(aprobadosPrueba/totalAlumnos);
		return P;
	}

	public double probabilidadIntervalos(int aprobadosIntervalo, int aprobadosAsig, int totalAlumnos) {
		double P = 0.0;
		P = (aprobadosAsig/totalAlumnos)/(aprobadosIntervalo/totalAlumnos);
		return P;
	}


	private double compararProbabilidad() {
		Double probAsig = probabilidadAsignatura(0, 0, 0);
		Double probInterv = probabilidadIntervalos(0, 0, 0);
		if(probAsig > probInterv) {
			return probAsig;
		}else {
			return probInterv;
		}
	}
	//CUARTO CALCULO///////// GRADO DE ALERTA DE SUSPENDER DEL ALUMNO (CONJUNTO DE PRUEBAS)


	public double alerta() {
		double A = 0.0;
		double d_estudiante;
		double d_asignatura;
		double probabilidad;
		
		d_estudiante = (d_estudiante + 1)/2;
		d_asignatura = (d_asignatura + 1)/2;

		A = 1- (d_estudiante + d_asignatura + probabilidad)/3;
		return A;
	}






















}
