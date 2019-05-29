package edu.uclm.esi.tfg.dominio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Modelo {

	private ArrayList<Double> desviacionesAlumno = new ArrayList<Double>();
	private ArrayList<Double> desviacionesAsignatura = new ArrayList<Double>();
	private ArrayList<Double> probabilidades = new ArrayList<Double>();
	private ArrayList<Calificacion> calificaciones_asig =new ArrayList<Calificacion>(); 

	public double calcularAlerta(ArrayList<Calificacion> calificaciones_alumno_curso, ArrayList<Prueba> pruebas_asig) {

		double desviacionAlumno = desviacionAlumno(pruebas_asig , calificaciones_alumno_curso);
		double desviacionAsignatura = desviacionAsignatura(pruebas_asig , calificaciones_alumno_curso);


		double prob_asig = getProbabilidadAsignatura(pruebas_asig, calificaciones_alumno_curso);
		//double prob_intervalos =  probabilidadIntervalos(pruebas_asig, calificaciones_alumno_curso)/alumnos_aprobados;

		double alerta = alerta(desviacionAlumno, desviacionAsignatura, prob_asig);
		return alerta;
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
	public double getProbabilidadAsignatura(ArrayList<Prueba> pruebas_asig, ArrayList<Calificacion> calificaciones_alumno_curso){

		ArrayList<Calificacion> calificaciones_asig = Manager.get().getCalificacionesPruebas(pruebas_asig);
		Map<Integer, HashMap<String, HashMap<Integer, Double>>> map_asignatura = Manager.get().obtenerHashMap(calificaciones_asig);
		Map<Integer, HashMap<String, HashMap<Integer, Double>>> mismaSituacion = alumnosMismaSituacion(map_asignatura,pruebas_asig, calificaciones_alumno_curso);


		int situacion_actual = mismaSituacion.size();
		int aprueban_asig = apruebaAsig(mismaSituacion);

		System.out.println("Alumno: "+ aprueban_asig +" / " +situacion_actual);

		double probabilidad = ((aprueban_asig*1.0)/situacion_actual);
		return probabilidad;
	}

	private Map<Integer, HashMap<String, HashMap<Integer, Double>>> alumnosMismaSituacion(Map<Integer, HashMap<String, HashMap<Integer, Double>>> map_asignatura, ArrayList<Prueba> pruebas_asig, ArrayList<Calificacion> calificaciones_alumno_curso) {

		Map<Integer, HashMap<String, HashMap<Integer, Double>>> temp = new HashMap<Integer, HashMap<String, HashMap<Integer, Double>>>();
		temp.putAll(map_asignatura);

		for (int i = 0; i<pruebas_asig.size();i++) {
			for (int j= 0; j< calificaciones_alumno_curso.size();j++){

				if(pruebas_asig.get(i).getId() == calificaciones_alumno_curso.get(j).getPrueba()) {
					int prueba = pruebas_asig.get(i).getId();
					double nota_corte = pruebas_asig.get(i).getN_corte();
					double nota_alumno = calificaciones_alumno_curso.get(j).getNota();

					Iterator<Map.Entry<Integer, HashMap<String, HashMap<Integer, Double>>>> it = temp.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<Integer, HashMap<String, HashMap<Integer, Double>>> alumno_year = (Map.Entry<Integer, HashMap<String, HashMap<Integer, Double>>>) it.next();
						//int alumno = alumno_year.getKey();
						HashMap<String, HashMap<Integer, Double>> years = alumno_year.getValue();
						Iterator<Map.Entry<String, HashMap<Integer, Double>>> it2 = years.entrySet().iterator();

						while (it2.hasNext()) {
							Map.Entry<String, HashMap<Integer, Double>> year_nota = (Map.Entry<String, HashMap<Integer, Double>>) it2.next();
							HashMap<Integer, Double> notas = year_nota.getValue();
							Iterator<Map.Entry<Integer, Double>> it3 = notas.entrySet().iterator();

							while (it3.hasNext()) {
								Map.Entry<Integer, Double> pruebas_notas = (Map.Entry<Integer, Double>) it3.next();
								int pr = pruebas_notas.getKey();
								double nota = pruebas_notas.getValue();

								if((prueba == pr) && (nota_alumno >= nota_corte)) {
									if(nota < nota_corte)
										it.remove();
								} 
								if((prueba == pr) && (nota_alumno < nota_corte) && (nota_alumno != -1.0)) {
									if(nota >= nota_corte)
										it.remove();
								}
							}
						}
					}
				}
			}
		}

		return temp;
	}

	private int apruebaAsig(Map<Integer, HashMap<String, HashMap<Integer, Double>>> mismaSituacion) {

		Map<Integer, HashMap<String, HashMap<Integer, Double>>> temp1 = new HashMap<Integer, HashMap<String, HashMap<Integer, Double>>>();
		temp1.putAll(mismaSituacion);
		
		Iterator<Map.Entry<Integer, HashMap<String, HashMap<Integer, Double>>>> it = temp1.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, HashMap<String, HashMap<Integer, Double>>> dupla = (Map.Entry<Integer, HashMap<String, HashMap<Integer, Double>>>) it.next();
			HashMap<String, HashMap<Integer, Double>> years = dupla.getValue();
			Iterator<Map.Entry<String, HashMap<Integer, Double>>> it2 = years.entrySet().iterator();

			while (it2.hasNext()) {
				Map.Entry<String, HashMap<Integer, Double>> dupla2 = (Map.Entry<String, HashMap<Integer, Double>>) it2.next();
				String year = dupla2.getKey();
				HashMap<Integer, Double> notas = dupla2.getValue();

				Iterator<Map.Entry<Integer, Double>> it3 = notas.entrySet().iterator();
				Double nota = 0.0;
				while (it3.hasNext()) {
					Map.Entry<Integer, Double> dupla3 = (Map.Entry<Integer, Double>) it3.next();
					if(dupla3.getValue() != (-1))
						nota+=dupla3.getValue();

				}
				if(nota < 5.0) {
					it.remove();
				}
			}
		}
		return temp1.size();
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
