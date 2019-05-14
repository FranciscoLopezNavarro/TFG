package edu.uclm.esi.tfg.dominio;

import java.util.ArrayList;

public class Modelo {

	private ArrayList<Double> desviacionesAlumno = new ArrayList<Double>();
	private ArrayList<Double> desviacionesAsignatura = new ArrayList<Double>();
	private ArrayList<Double> probabilidades = new ArrayList<Double>();


	public double calcularAlerta(ArrayList<Calificacion> calificaciones_alumno_curso, ArrayList<Prueba> pruebas_asig) {

		double desviacionAlumno = desviacionAlumno(pruebas_asig , calificaciones_alumno_curso);
		double desviacionAsignatura = desviacionAsignatura(pruebas_asig , calificaciones_alumno_curso);
		int alumnos_aprobados = Manager.get().alumnosApruebanAsig(pruebas_asig);
		
		double prob_asig = probabilidadAsignatura(pruebas_asig, calificaciones_alumno_curso)/alumnos_aprobados;
		double prob_intervalos =  probabilidadIntervalos(pruebas_asig, calificaciones_alumno_curso)/alumnos_aprobados;
		return alerta(desviacionAlumno, desviacionAsignatura, compararProbabilidad(prob_asig,prob_intervalos));
	}



	//PRIMER CALCULO///////// DESVIACION DEL ALUMNO FRENTE A LA NOTA DE CORTE DE CADA PRUEBA
	public double desviacionAlumno(ArrayList<Prueba> pruebas,ArrayList<Calificacion> calificaciones_alumno_curso){
		double D = 0.0;
		double D_aux = 0.0;
		for (int i = 0; i<pruebas.size();i++) {
			for (int j= 0; j< calificaciones_alumno_curso.size();j++){
				if(pruebas.get(i).getId() == calificaciones_alumno_curso.get(j).getPrueba())
					if(calificaciones_alumno_curso.get(j).getNota() >= pruebas.get(i).getN_corte()) {
						desviacionesAlumno.add((calificaciones_alumno_curso.get(j).getNota() - pruebas.get(i).getN_corte())/(pruebas.get(i).getN_max() - pruebas.get(i).getN_corte()));
					}else {
						desviacionesAlumno.add(calificaciones_alumno_curso.get(j).getNota() - pruebas.get(i).getN_corte()/(pruebas.get(i).getN_corte() - pruebas.get(i).getN_min()));
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
	public double desviacionAsignatura(ArrayList<Prueba> pruebas, ArrayList<Calificacion> calificaciones_alumno_curso) {
		double D = 0.0;
		double D_aux = 0.0;
		for (int i = 0; i< pruebas.size();i++) {
			for (int j= 0; j< calificaciones_alumno_curso.size();j++){
				if(pruebas.get(i).getId() == calificaciones_alumno_curso.get(j).getPrueba()) {
					desviacionesAsignatura.add((calificaciones_alumno_curso.get(j).getNota() - Manager.get().getMediaPrueba(pruebas.get(i).getId())) / (pruebas.get(i).getN_max()  -  pruebas.get(i).getN_min()));
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
	public double probabilidadAsignatura(ArrayList<Prueba> pruebas , ArrayList<Calificacion> calificaciones_alumno_curso) {
		//Calificaciones aux inicialmente son todas las calificaciones de una asignatura y se van quitando
		ArrayList<Calificacion> calificaciones_totales = Manager.get().getCalificacionesPruebas(pruebas);
		ArrayList<Calificacion> calificaciones_aux =  new ArrayList<Calificacion>();

		for(int i = 0; i<pruebas.size();i++) {
			for (int j= 0; j< calificaciones_alumno_curso.size();j++){
					if(pruebas.get(i).getId() == calificaciones_alumno_curso.get(j).getPrueba()) {
						if(calificaciones_alumno_curso.get(j).getNota() >= (pruebas.get(i).getN_corte())){

							
						}else {

						}
				}
			}
		}
		return 0.0;
	}

	private int probabilidadIntervalos(ArrayList<Prueba> pruebas_asig,
			ArrayList<Calificacion> calificaciones_alumno_curso) {
		// TODO Auto-generated method stub
		return 0;
	}


	private double compararProbabilidad(double probAsig, double probInterv) {
		if(probAsig >= probInterv) {
			return probAsig;
		}else {
			return probInterv;
		}
	}
	//CUARTO CALCULO///////// GRADO DE ALERTA DE SUSPENDER DEL ALUMNO (CONJUNTO DE PRUEBAS)


	public double alerta(double d_alumno, double d_asignatura, double d_probabilidad) {
		double A = 0.0;

		d_alumno = (d_alumno + 1)/2;
		d_asignatura = (d_asignatura + 1)/2;

		A = 1 - (d_alumno + d_asignatura + d_probabilidad )/3;
		return A;
	}






















}
