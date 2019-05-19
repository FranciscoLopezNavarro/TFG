<%@page import="org.json.JSONObject, edu.uclm.esi.tfg.dominio.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.ArrayList"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>MAFD</title>
<!-- CSS -->
<link href="../css/bootstrap.css" rel="stylesheet">
<link href="../css/bootstrap.min.css" rel="stylesheet">
<link href="../css/visualizarDatos.css" rel="stylesheet">
<link
	href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css"
	rel="stylesheet" id="bootstrap-css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link href="../css/tabpanels.css" rel="stylesheet">
<link href="../css/graficos.css" rel="stylesheet">

<!-- Scripts -->
<script src="../js/jquery.js"></script>
<script src="../js/bootstrap.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/Aintro.js"></script>
<script type="text/javascript"
	src="https://code.jquery.com/jquery-1.12.0.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"
	integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS"
	crossorigin="anonymous"></script>
<script src="../js/tabpanels.js"></script>
<script src="../js/graficos.js"></script>

<!-- Load -->
<script src="../js/Load.js"></script>
<!-- Connection Manager -->
<script src="../js/ConnectionManager.js"></script>
<!-- Menu -->
<script src="../js/MenuManager.js"></script>

<!-- LIBRERÍAS AMCHARTS -->
<script src="//www.amcharts.com/lib/4/core.js"></script>
<script src="//www.amcharts.com/lib/4/charts.js"></script>
<script src="//www.amcharts.com/lib/4/maps.js"></script>

</head>
<body>
	<div id="menu"></div>
	<div class="row">
		<div class="col-md-6 mb-3">
			<label for="validationCustom03">Asignatura:</label> <select
				class="form-control form-control-lg" name="category"
				id="comboAsignaturas" onchange="seleccion(this)">
				<%
					ArrayList<Asignatura> asignaturas = Manager.get().getAsignaturas();
				%>

				<option value="" disabled selected hidden>Selecciona una
					asignatura</option>
				<%
					int asigID = 0;
					for (int i = 0; i < asignaturas.size(); i++) {
						String asignatura = (String) asignaturas.get(i).getNombre();
						asigID = (Integer) asignaturas.get(i).getId();
				%>
				<option value="<%=asigID%>"><%=asignatura%></option>
				<%
					}
					ArrayList<Prueba> pruebas_asignatura = Manager.get().getPruebasAsignatura(asigID);
				%>
			</select>
		</div>
	</div>
	<br />
	<div id="tituloAsignatura" style="display: none">
		<h3 id="asigElegida"></h3>
	</div>
	<div id="graficos" style="display: none">
		<div class="bloque">
			<div id="grafico_curso_actual">
				<div id="info_curso_actual"></div>
			</div>

			<div id="grafico_historico"></div>
		</div>
		<div class="tabs">
			<div class="tab-button-outer">
				<ul id="tab-button">
					<%
						for (int i = 0; i < pruebas_asignatura.size(); i++) {
							String nombre_prueba = (String) pruebas_asignatura.get(i).getTitulo();
					%>
					<li><a href="#<%=nombre_prueba%>"><%=nombre_prueba%></a></li>
					<%
						}
					%>
				</ul>
			</div>

			<div class="tab-select-outer">
				<select id="tab-select">
					<%
						for (int i = 0; i < pruebas_asignatura.size(); i++) {
							String nombre_prueba = (String) pruebas_asignatura.get(i).getTitulo();
					%>
					<option value="#<%=nombre_prueba%>"><%=nombre_prueba%></option>
					<%
						}
					%>
				</select>
			</div>
			<%
				for (int i = 0; i < pruebas_asignatura.size(); i++) {
					String nombre_prueba = (String) pruebas_asignatura.get(i).getTitulo();
			%>
			<div id="<%=nombre_prueba%>" class="tab-contents">
				<h2><%=nombre_prueba%></h2>
			</div>
			<%
				}
			%>
		</div>
	</div>
</body>
</html>