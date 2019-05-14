<%@page import="com.google.gson.JsonArray"%>
<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject, edu.uclm.esi.tfg.dominio.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.ArrayList"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String save = request.getParameter("save");
	JSONObject jso = new JSONObject(save);

	try {
		int alumno = Integer.parseInt(jso.optString("alumno"));
		String curso = jso.optString("curso").replace(" ", "");
		String asignatura = jso.optString("asignatura");
		JSONArray notas = jso.optJSONArray("pruebas");

		crearAlumno(alumno);
	
		ArrayList<String> list = new ArrayList<String>();
		if (notas != null) {
			ArrayList<Prueba> pruebas = Manager.get().getPruebasAsignatura(Integer.parseInt(asignatura));

			for (int i = 0; i < pruebas.size(); i++) {
				String prueba = (String) pruebas.get(i).getTitulo();
				String nota = notas.getJSONObject(0).getString(prueba);

				if (nota.equals("") || nota.equals(" ")) {
					System.out.println(prueba + " sin calificacion");
					
				}else if(nota.toUpperCase().equals("NP")){
					Manager.get().registrarCalificacion(alumno, pruebas.get(i).getId(), curso, (-1.0));
				} else {
					Manager.get().registrarCalificacion(alumno, pruebas.get(i).getId(), curso, Double.parseDouble(nota));
				}
				System.out.println(prueba + " : " + nota);

			}

		}

	} catch (Exception e) {
		e.printStackTrace();
	}
%>

<%!public void crearAlumno(int alumno) {
		try {
			Manager.get().registrarAlumno(alumno);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}%>