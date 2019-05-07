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
					<th><%=prueba + " (" + nota_max + ")"%></th>
					<%
						}
					%>
				</tr>

			</thead>
			<!--FIN CABECERA-->
			<!-- CONTENIDO EN BASE A LA TABLA CALIFICACIONES -->
			<tbody>
				<%
					Map<Integer, String> alumno_year = obtenerHashMap(calificaciones);
					System.out.println(alumno_year.size());
					Iterator<Map.Entry<Integer, String>> it = alumno_year.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<Integer, String> dupla = (Map.Entry<Integer, String>) it.next();
						Integer alumno = dupla.getKey();
						String year = dupla.getValue();
				%>
				<tr>
					<td><%=alumno%></td>
					<td><%=year%></td>

					<%
						double[] notas = devolverNotas(alumno, year, pruebas.size(), calificaciones);
							for (int j = 0; j < notas.length; j++) {
								if (notas[j] == -1.0) {
					%>
					<td>NP</td>
					<%
						} else {
					%>
					<td><%=notas[j]%></td>
					<%
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