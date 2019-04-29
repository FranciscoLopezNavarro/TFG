<%@page import="org.json.JSONObject, edu.uclm.esi.tfg.dominio.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.ArrayList"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	String seleccionado = request.getParameter("asig_actual");
	JSONObject jso = new JSONObject(seleccionado);

	int asignatura = Integer.parseInt(jso.optString("asig"));
	String curso_actual = jso.optString("curso").replace(" ", "");

	//Cogemos solo las pruebas de esa asignatura
	ArrayList<Prueba> pruebas = Manager.get().getPruebasAsignatura(asignatura);

	//Cogemos las calificaciones donde estén esas pruebas
	ArrayList<Calificacion> calificaciones = Manager.get().getCalificacionesPruebasCursoActual(pruebas,
			curso_actual);
%>

<body>
	<form>
		<table border="2" id="tablaCursoActual">
			<!-- CABECERA EN BASE A LA TABLA PRUEBAS -->
			<thead>
				<tr>
					<th id="header_alumno">alumno</th>
					<%
						for (int i = 0; i < pruebas.size(); i++) {
							String prueba = (String) pruebas.get(i).getTitulo();
					%>
					<th class="header_prueba" scope="col"><%=prueba%></th>
					<%
						}
					%>
					<th id="riesgo">Grado de riesgo</th>
					<th><a onclick="addRow()" class="btn btn-primary btn-default"><span
							class="glyphicon glyphicon-plus"></span> Añadir alumno</a></th>
				</tr>
			</thead>
			<!--FIN CABECERA-->
			<tbody>
				<%
					Map<Integer, String> alumno_year = obtenerHashMap(calificaciones);
					if(!alumno_year.isEmpty()){
					Iterator<Map.Entry<Integer, String>> it = alumno_year.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<Integer, String> dupla = (Map.Entry<Integer, String>) it.next();
						Integer alumno = dupla.getKey();
						String year = dupla.getValue();
				%>
				
				<tr>
					<td contenteditable="true" class="alumno"><%=alumno%></td>
					<%
						double[] notas = devolverNotas(alumno, year, pruebas.size(), calificaciones);
							for (int j = 0; j < notas.length; j++) {
								if (notas[j] == -1.0) {
					%>
					<td contenteditable="true" class="nota">NP</td>
					<%
						} else {
					%>
					<td contenteditable="true" class="nota"><%=notas[j]%></td>
					<%
						}
							}
					%>
					<td><div class="progress">
							<div class="progress-bar progress-bar-striped" style="width: 30%">30%</div>
						</div></td>
					<td><a><i class='fa fa-save' onclick='saveRow(this)'></i></a><a><i
							class='fa fa-trash' onclick='deleteRow(this)'></i></a></td>
					<%
					}	
					}else{
						
					}
					%>
				</tr>
			</tbody>
		</table>
	</form>
</body>
</html>
<%!public double[] devolverNotas(Integer alumno, String year, Integer n_pruebas,
			ArrayList<Calificacion> calificaciones) {
		double[] notas = new double[n_pruebas];
		Integer aux = 0;
		for (int i = 0; i < calificaciones.size(); i++) {
			if (calificaciones.get(i).getAlumno() == alumno && calificaciones.get(i).getYear().equals(year)) {
				notas[aux] = calificaciones.get(i).getNota();
				aux++;
			}
		}

		return notas;
	}%>
<%!public Map<Integer, String> obtenerHashMap(ArrayList<Calificacion> calificaciones) {

		Map<Integer, String> mapa = new HashMap<Integer, String>();
		Iterator<Map.Entry<Integer, String>> it = mapa.entrySet().iterator();
		for (int i = 0; i < calificaciones.size(); i++) {
			int alumno = calificaciones.get(i).getAlumno();
			String year = calificaciones.get(i).getYear();

			mapa.put(alumno, year);
		}
		return mapa;
	}%>