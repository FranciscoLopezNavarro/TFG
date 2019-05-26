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
							double nota_max = pruebas.get(i).getN_max();
							String puntos = Double.toString(nota_max);
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
					Map<Integer, HashMap<String, HashMap<Integer, Double>>> alumno_year = Manager.get()
							.obtenerHashMap(calificaciones);
					Iterator<Map.Entry<Integer, HashMap<String, HashMap<Integer, Double>>>> it = alumno_year.entrySet()
							.iterator();

					while (it.hasNext()) {
						Map.Entry<Integer, HashMap<String, HashMap<Integer, Double>>> dupla = (Map.Entry<Integer, HashMap<String, HashMap<Integer, Double>>>) it.next();
						Integer alumno = dupla.getKey();
						HashMap<String, HashMap<Integer, Double>> years = dupla.getValue();
						Iterator<Map.Entry<String, HashMap<Integer, Double>>> it2 = years.entrySet().iterator();

						while (it2.hasNext()) {
							Map.Entry<String, HashMap<Integer, Double>> dupla2 = (Map.Entry<String, HashMap<Integer, Double>>) it2.next();
							String year = dupla2.getKey();
							HashMap<Integer, Double> notas = dupla2.getValue();
							Iterator<Map.Entry<Integer, Double>> it3 = notas.entrySet().iterator();
				%>

				<tr>
					<td contenteditable="true" class="alumno"><%=alumno%></td>
					<%
						while (it3.hasNext()) {
									Map.Entry<Integer, Double> dupla3 = (Map.Entry<Integer, Double>) it3.next();
									Double nota = dupla3.getValue();
									if (nota == -1.0) {
					%>
					<td contenteditable="true" class="nota">NP
						<div class="progress" style="display: none">
							<div
								class="progress-bar progress-bar-striped bg-success progress-bar-animated"
								style="width: 0%"></div>
						</div>
					</td>
					<%
						} else {
					%>
					<td contenteditable="true" class="nota"><%=nota%>
						<div class="progress" style="display: none">
							<div
								class="progress-bar progress-bar-striped bg-success progress-bar-animated"
								style="width: 0%"></div>
						</div></td>
					<%
						}
								}
					%>
					<td class="alerta">
						<div class="progress">
							<div class="progress-bar progress-bar-success" style="width: 0%">
								<span class="sr-only">35% completado (éxito)</span>
							</div>
							<div class="progress-bar progress-bar-warning" style="width: 0%">
								<span class="sr-only">20% completado (aviso)</span>
							</div>
							<div class="progress-bar progress-bar-danger" style="width: 0%">
								<span class="sr-only">10% completado (peligro)</span>
							</div>
						</div>
						<div class="grado_riesgo"></div>
					</td>
					<td><a><i class='fa fa-save' onclick='saveRow(this)'></i></a><a><i
							class='fa fa-trash' onclick='deleteRow(this)'></i></a></td>
					<%
						}
						}
					%>
				</tr>
			</tbody>
		</table>
	</form>
</body>
</html>
