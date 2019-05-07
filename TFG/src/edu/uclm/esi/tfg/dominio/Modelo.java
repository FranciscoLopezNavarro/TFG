package edu.uclm.esi.tfg.dominio;

import java.util.ArrayList;

public class Modelo {

	private ArrayList<Double> desviacionesAlumno = new ArrayList<Double>();
	private ArrayList<Double> desviacionesAsignatura = new ArrayList<Double>();
	private ArrayList<Double> probabilidades = new ArrayList<Double>();


	public double calcularAlerta(ArrayList<Calificacion> calificaciones_alumno, ArrayList<Prueba> pruebas_asig) {

		double desviacionAlumno = desviacionAlumno(pruebas_asig , calificaciones_alumno);
		double desviacionAsignatura = desviacionAsignatura(pruebas_asig , calificaciones_alumno);
		double probabilidad = 0.0;
		
		return alerta(desviacionAlumno, desviacionAsignatura);
	}

	//PRIMER CALCULO///////// DESVIACION DEL ALUMNO FRENTE A LA NOTA DE CORTE DE CADA PRUEBA
	public double desviacionAlumno(ArrayList<Prueba> pruebas,ArrayList<Calificacion> calificaciones_alumno){
		double D = 0.0;
		double D_aux = 0.0;
		for (int i = 0; i<pruebas.size();i++) {
			for (int j= 0; j< calificaciones_alumno.size();j++){
				if(pruebas.get(i).getId() == calificaciones_alumno.get(j).getPrueba())
					if(calificaciones_alumno.get(j).getNota() >= pruebas.get(i).getN_corte()) {
						desviacionesAlumno.add((calificaciones_alumno.get(j).getNota() - pruebas.get(i).getN_corte())/(pruebas.get(i).getN_max() - pruebas.get(i).getN_corte()));
					}else {
						desviacionesAlumno.add(calificaciones_alumno.get(j).getNota() - pruebas.get(i).getN_corte()/(pruebas.get(i).getN_corte() - pruebas.get(i).getN_min()));
					}
			}
		}
		int n = desviacionesAlumno.size();
		for(int i = 0; i<n;i++) {
			D_aux += desviacionesAlumno.get(i);
		}
		D = D_aux/n;
		return D;
	}

	//SEGUNDO CALCULO///////// DESVIACION DEL ALUMNO FRENTE A LA MEDIA DE OTROS ALUMNOS (HISTORICOS)
	public double desviacionAsignatura(ArrayList<Prueba> pruebas, ArrayList<Calificacion> calificaciones_alumno) {
		double D = 0.0;
		double D_aux = 0.0;
		for (int i = 0; i< pruebas.size();i++) {
			for (int j= 0; j< calificaciones_alumno.size();j++){
				if(pruebas.get(i).getId() == calificaciones_alumno.get(j).getPrueba()) {
					desviacionesAsignatura.add((calificaciones_alumno.get(j).getNota() - Manager.get().getMediaPrueba(pruebas.get(i).getId())) / (pruebas.get(i).getN_max()  -  pruebas.get(i).getN_min()));
				}
			}
		}
		int n = desviacionesAsignatura.size();
		for(int i = 0; i<n;i++) {
			D_aux += desviacionesAsignatura.get(i);
		}
		D = D_aux/n;
		return D;
	}

	//TERCER CALCULO/////////
//	public double probabilidadAsignatura(ArrayList<Prueba> pruebas) {
//		double P = 0.0;
//		for (int i = 0; i< pruebas.size();i++) {
//			////Con este bucle se obtiene la posibilidad de superar cada prueba
//			double prob_prueba = Manager.get().getAprobadosPrueba(pruebas.get(i).getId()) / Manager.get().getTotalAlumnosPrueba(pruebas.get(i).getId());
//		}
//		
//		P = (aprobadosAsig/totalAlumnos)/(aprobadosPrueba/totalAlumnos);
//		return P;
//	}
//
//	public double probabilidadIntervalos(int aprobadosIntervalo, int aprobadosAsig, int totalAlumnos) {
//		double P = 0.0;
//		P = (aprobadosAsig/totalAlumnos)/(aprobadosIntervalo/totalAlumnos);
//		return P;
//	}
//
//
//	private double compararProbabilidad(double probAsig, double probInterv) {
//		if(probAsig >= probInterv) {
//			return probAsig;
//		}else {
//			return probInterv;
//		}
//	}
	//CUARTO CALCULO///////// GRADO DE ALERTA DE SUSPENDER DEL ALUMNO (CONJUNTO DE PRUEBAS)


	public double alerta(double d_alumno, double d_asignatura) {
		double A = 0.0;
		
		d_alumno = (d_alumno + 1)/2;
		d_asignatura = (d_asignatura + 1)/2;

		A = 1 - (d_alumno + d_asignatura )/2;
		return A;
	}






















}
