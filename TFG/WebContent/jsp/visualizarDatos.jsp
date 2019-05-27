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
<script src="../js/visualizarDatos.js"></script>

<!-- Load -->
<script src="../js/Load.js"></script>
<!-- Connection Manager -->
<script src="../js/ConnectionManager.js"></script>
<!-- Menu -->
<script src="../js/MenuManager.js"></script>

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
					for (int i = 0; i < asignaturas.size(); i++) {
						String asignatura = (String) asignaturas.get(i).getNombre();
						Integer asigID = (Integer) asignaturas.get(i).getId();
				%>
				<option value="<%=asigID%>"><%=asignatura%></option>
				<%
					}
				%>
			</select>
		</div>
	</div>
	<br />
	<div id="tituloAsignatura" style="display: none">
		<h3 id="asigElegida"></h3>
	</div>
	<div class="tabs">
		<div class="tab-button-outer">
			<ul id="tab-button">
				<li><a href="#historico">Histórico</a></li>
				<li><a href="#curso_actual">Curso actual</a></li>
			</ul>
		</div>
		<div class="tab-select-outer">
			<select id="tab-select">
				<option value="#historico">Histórico</option>
				<option value="#curso_actual">Curso actual</option>
			</select>
		</div>

		<div id="historico" class="tab-contents">
			<h2>Histórico</h2>
			<p id="p_historico">Aquí se muestran todas las notas de los
				alumnos de años anteriores pertenecientes a la asignatura
				seleccionada. Por favor, seleccione una asignatura.</p>
			<div id="divtablaHistorico"
				style="height: 550px; overflow-y: scroll;"></div>
		</div>
		<div id="curso_actual" class="tab-contents">
			<h2>Curso actual</h2>
			<p id="p_actual">Aquí se muestran las notas de los alumnos que
				cursan la asignatura seleccionada en el curso actual. Por favor,
				seleccione una asignatura.</p>
			<div><p id="info_prueba"></p></div>
			<div id="divtablaCursoActual"
				style="height: 550px; overflow-y: scroll;"></div>
		</div>
	</div>
</body>
</html>