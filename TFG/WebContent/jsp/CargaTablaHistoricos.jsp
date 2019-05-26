<%@page import="org.json.JSONObject, edu.uclm.esi.tfg.dominio.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.ArrayList"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	String seleccionado = request.getParameter("asignatura");
	int asignatura = Integer.parseInt(seleccionado);

	//Cogemos solo las pruebas de esa asignatura
	ArrayList<Prueba> pruebas = Manager.get().getPruebasAsignatura(asignatura);

	//Cogemos las calificaciones donde estén esas pruebas
	ArrayList<Calificacion> calificaciones = Manager.get().getCalificacionesPruebas(pruebas);
%>
<html>
<body>
	<form>
		<table border="2">
			<!-- CABECERA EN BASE A LA TABLA PRUEBAS -->
			<thead>
				<tr>
					<th>Alumno</th>
					<th>año</th>
					<%
						for (int i = 0; i < pruebas.size(); i++) {
							String prueba = (String) pruebas.get(i).getTitulo();
							String nota_max = Double.toString(pruebas.get(i).getN_max());
					%>
					<th><%=prueba%>
						<div class="nota_max">
							[0 -
							<%=nota_max%>]
						</div></th>

					<%
						}
					%>
				</tr>

			</thead>
			<!--FIN CABECERA-->
			<!-- CONTENIDO EN BASE A LA TABLA CALIFICACIONES -->
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
					<td><%=alumno%></td>
					<td><%=year%></td>

					<%
						//double[] notas = Manager.get().devolverNotas(alumno, year, pruebas.size(), calificaciones);
								//	for (int j = 0; j < notas.length; j++) {
								while (it3.hasNext()) {
									Map.Entry<Integer, Double> dupla3 = (Map.Entry<Integer, Double>) it3.next();
									Double nota = dupla3.getValue();

									if (nota == -1.0) {
					%>
					<td>NP</td>
					<%
						} else {
					%>
					<td><%=nota%></td>
					<%
						}
								}
							}
						}
					%>
				</tr>
			</tbody>
		</table>
	</form>
</body>
</html>
