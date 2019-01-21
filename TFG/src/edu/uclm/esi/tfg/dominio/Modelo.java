package edu.uclm.esi.tfg.dominio;

public class Modelo {

	//PRIMER CALCULO/////////
	public double desviacionAsignatura(double nota_alumno, double nota_min, double nota_corte, double nota_max){
		double D = 0.0;
		if(nota_alumno >= nota_corte) {
			D = (nota_alumno - nota_corte)/(nota_max - nota_corte);
		}else {
			D= (nota_alumno - nota_corte)/(nota_corte - nota_min);
		}
		//desviacionesAsigs.add(D);
		return D;
	}
	public double desviacionAcumuladaAsignatura() {
		//Falta estructura de datos de desviaciones por prueba respecto a la nota de corte
		Double D = 0.0;
		Double D_aux = 0.0;
		int n = desviacionesAsigs.size();

		for(int i = 0; i<n;i++) {
			D_aux += desviacionesAsig(i);
		}
		D = D_aux/n;
		return D;
	}

	//SEGUNDO CALCULO/////////
	public double desviacionAlumnos(double nota_alumno, double nota_min, double nota_max, double media_alumnos) {
		double D = 0.0;

		D = (nota_alumno - media_alumnos)/(nota_max - nota_min);
		//desviacionesAlumnos.add(D);
		return D;
	}
	public double desviacionAcumuladaAlumnos() {
		//Falta estructura de datos de desviaciones por prueba respecto a la media de alumnos
		Double D = 0.0;
		Double D_aux = 0.0;
		int n = desviacionesAlumnos.size();

		for(int i = 0; i<n;i++) {
			D_aux += desviacionesAlumnos(i);
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

	public double probabilidadNota() {
		
	}







	//CUARTO CALCULO/////////


	public double alerta(double d_asig, double d_estudiantes, double probabilidad) {
		double A = 0.0;
		
		d_asig = (d_asig + 1)/2;
		d_estudiantes = (d_asig + 1)/2;
		
		A = 1- (d_asig + d_estudiantes + probabilidad)/3;
		return A;
	}





















}
