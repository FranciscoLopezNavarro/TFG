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
	public double getProbabilidadAsignatura(ArrayList<Prueba> pruebas_asig, ArrayList<Calificacion> 
	calificaciones_alumno_curso){

		ArrayList<Calificacion> calificaciones_asig = Manager.get().getCalificacionesPruebas(pruebas_asig);
		Map<Integer, HashMap<String, HashMap<Integer, Double>>> map_asignatura = Manager.get().obtenerHashMap(calificaciones_asig);
		Map<Integer, HashMap<String, HashMap<Integer, Double>>> mismaSituacion = alumnosMismaSituacion(map_asignatura,pruebas_asig, calificaciones_alumno_curso);
		Map<Integer, HashMap<String, HashMap<Integer, Double>>> mismaSituacionIntervalo = alumnosMismaSituacionIntervalo(map_asignatura,pruebas_asig, calificaciones_alumno_curso);

		//Calculos de probabilidad (Aprueba/Suspende)
		int situacion_actual = mismaSituacion.size();
		int aprueban_asig = apruebaAsig(mismaSituacion);
		double probabilidad = ((aprueban_asig*1.0)/situacion_actual);

		//Calculos de probabilidad (Aprueba en intervalo/Suspende)		
		int situacion_actual_intervalo = mismaSituacionIntervalo.size();
		int aprueban_asig_intervalo = apruebaAsig(mismaSituacionIntervalo);
		double probabilidad_intervalo = ((aprueban_asig_intervalo*1.0)/situacion_actual_intervalo);

		return compararProbabilidad(probabilidad, probabilidad_intervalo);
	}

	private Map<Integer, HashMap<String, HashMap<Integer, Double>>> alumnosMismaSituacion(
			Map<Integer, HashMap<String, HashMap<Integer, Double>>> map_asignatura, 
			ArrayList<Prueba> pruebas_asig, ArrayList<Calificacion> calificaciones_alumno_curso) {

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

	private Map<Integer, HashMap<String, HashMap<Integer, Double>>> alumnosMismaSituacionIntervalo(
			Map<Integer, HashMap<String, HashMap<Integer, Double>>> map_asignatura, ArrayList<Prueba> pruebas_asig,
			ArrayList<Calificacion> calificaciones_alumno_curso) {

		Map<Integer, HashMap<String, HashMap<Integer, Double>>> temp = new HashMap<Integer, HashMap<String, HashMap<Integer, Double>>>();
		temp.putAll(map_asignatura);

		for (int i = 0; i<pruebas_asig.size();i++) {
			for (int j= 0; j< calificaciones_alumno_curso.size();j++){

				if(pruebas_asig.get(i).getId() == calificaciones_alumno_curso.get(j).getPrueba()) {
					int prueba = pruebas_asig.get(i).getId();
					double nota_corte = pruebas_asig.get(i).getN_corte();
					double nota_max = pruebas_asig.get(i).getN_max();
					double nota_alumno = calificaciones_alumno_curso.get(j).getNota();

					Iterator<Map.Entry<Integer, HashMap<String, HashMap<Integer, Double>>>> it = temp.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<Integer, HashMap<String, HashMap<Integer, Double>>> alumno_year = (Map.Entry<Integer, HashMap<String, HashMap<Integer, Double>>>) it.next();
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

									//Si hemos aprobado y el alumno ha suspendido se elimina el alumno del calculo
									if(nota < nota_corte)
										it.remove();

									// 1 if para delimitar si el alumno se encuentra en el intervalo 1 una vez ha aprobado la prueba
									if(nota_alumno <= (nota_corte + (nota_max-nota_corte/3))) {
										if(nota > (nota_corte + (nota_max-nota_corte/3)))
											it.remove();
										// 2 if para delimitar si el alumno se encuentra en el intervalo 3 una vez ha aprobado la prueba
									}else if(nota_alumno >= (nota_max - (nota_max-nota_corte/3))) {
										if(nota < (nota_max - (nota_max-nota_corte/3)))
											it.remove();
										//En este else se trata cuando la nota del alumno se encuentra en el 2 intervalo

									}else {
										// alumno se encuentra en el intervalo 2 una vez ha aprobado la prueba
										if(nota <= (nota_corte + (nota_max-nota_corte/3)) || nota >= (nota_max - (nota_max-nota_corte/3)))
											it.remove();
									}
								} 
								if((prueba == pr) && (nota_alumno < nota_corte) && (nota_alumno != -1.0)) {
									//Si hemos suspendido y el alumno ha aprobado se elimina el alumno del calculo
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



	///////////////// CALCULO DE PROBABILIDAD DE SUSPENDER PRUEBAS ////////////////////////////
	public double calcularAlertaPruebas(ArrayList<Calificacion> calificaciones_alumno_curso,
			ArrayList<Prueba> pruebas_asig, String prueba1, String prueba2) {
		ArrayList<Calificacion> calificaciones_asig = Manager.get().getCalificacionesPruebas(pruebas_asig);
		Map<Integer, HashMap<String, HashMap<Integer, Double>>> map_asignatura = Manager.get().obtenerHashMap(calificaciones_asig);
		Map<Integer, HashMap<String, HashMap<Integer, Double>>> mismaSituacionPrueba = alumnosMismaSituacionPrueba1(map_asignatura,pruebas_asig, calificaciones_alumno_curso,prueba1);

		int situacion_actual_prueba = mismaSituacionPrueba.size();
		int aprueban_prueba = apruebaPrueba2(mismaSituacionPrueba,map_asignatura,pruebas_asig, calificaciones_alumno_curso,prueba2);
		double probabilidadprueba = ((aprueban_prueba*1.0)/situacion_actual_prueba);

		return 1.0 - probabilidadprueba;
	}


	private Map<Integer, HashMap<String, HashMap<Integer, Double>>> alumnosMismaSituacionPrueba1(
			Map<Integer, HashMap<String, HashMap<Integer, Double>>> map_asignatura, 
			ArrayList<Prueba> pruebas_asig, ArrayList<Calificacion> calificaciones_alumno_curso, String prueba1) {

		Map<Integer, HashMap<String, HashMap<Integer, Double>>> temp = new HashMap<Integer, HashMap<String, HashMap<Integer, Double>>>();
		temp.putAll(map_asignatura);

		for (int i = 0; i<pruebas_asig.size();i++) {
			for (int j= 0; j< calificaciones_alumno_curso.size();j++){

				if(pruebas_asig.get(i).getId() == calificaciones_alumno_curso.get(j).getPrueba() && pruebas_asig.get(i).getTitulo().equals(prueba1)) {
					int prueba = pruebas_asig.get(i).getId();
					double nota_corte = pruebas_asig.get(i).getN_corte();
					double nota_alumno = calificaciones_alumno_curso.get(j).getNota();

					Iterator<Map.Entry<Integer, HashMap<String, HashMap<Integer, Double>>>> it = temp.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<Integer, HashMap<String, HashMap<Integer, Double>>> alumno_year = (Map.Entry<Integer, HashMap<String, HashMap<Integer, Double>>>) it.next();
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
	private int apruebaPrueba2(Map<Integer, HashMap<String, HashMap<Integer, Double>>> mismaSituacionPrueba,Map<Integer, HashMap<String, HashMap<Integer, Double>>> map_asignatura, 
			ArrayList<Prueba> pruebas_asig, ArrayList<Calificacion> calificaciones_alumno_curso, String prueba2) {

		Map<Integer, HashMap<String, HashMap<Integer, Double>>> temp1 = new HashMap<Integer, HashMap<String, HashMap<Integer, Double>>>();
		temp1.putAll(mismaSituacionPrueba);
		for (int i = 0; i<pruebas_asig.size();i++) {
			for (int j= 0; j< calificaciones_alumno_curso.size();j++){

				if(pruebas_asig.get(i).getTitulo().equals(prueba2)) {
					int prueba = pruebas_asig.get(i).getId();
					double nota_corte = pruebas_asig.get(i).getN_corte();
					Iterator<Map.Entry<Integer, HashMap<String, HashMap<Integer, Double>>>> it = temp1.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<Integer, HashMap<String, HashMap<Integer, Double>>> dupla = (Map.Entry<Integer, HashMap<String, HashMap<Integer, Double>>>) it.next();
						HashMap<String, HashMap<Integer, Double>> years = dupla.getValue();
						Iterator<Map.Entry<String, HashMap<Integer, Double>>> it2 = years.entrySet().iterator();

						while (it2.hasNext()) {
							Map.Entry<String, HashMap<Integer, Double>> dupla2 = (Map.Entry<String, HashMap<Integer, Double>>) it2.next();
							HashMap<Integer, Double> notas = dupla2.getValue();

							Iterator<Map.Entry<Integer, Double>> it3 = notas.entrySet().iterator();
							while (it3.hasNext()) {
								Map.Entry<Integer, Double> pruebas_notas = (Map.Entry<Integer, Double>) it3.next();
								int pr = pruebas_notas.getKey();
								double nota = pruebas_notas.getValue();

								if((prueba == pr) && (nota < nota_corte)) {
										it.remove();
								} 
							}
						}
					}
				}
			}
		}
		return temp1.size();
	}
}
