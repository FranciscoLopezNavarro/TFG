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
	String header = request.getParameter("header");
	JSONObject jso = new JSONObject(header);
	JSONArray pruebas = new JSONArray();
	JSONObject respuesta = new JSONObject();
	try {
		String prueba = jso.optString("prueba");
		String asignatura = jso.optString("asignatura");
		if (prueba != null) {
			ArrayList<Prueba> pruebasRelacionadas = Manager.get().getRelacionesPrueba(prueba, Integer.parseInt(asignatura));
			
			for (int i = 0; i < pruebasRelacionadas.size(); i++) {
				pruebas.put(pruebasRelacionadas.get(i).getTitulo());
			}
			respuesta.put("Pruebas", pruebas);
			response.setContentType("application/json");
			response.getWriter().write(respuesta.toString());
		}

	} catch (Exception e) {
		e.printStackTrace();
	}
%>