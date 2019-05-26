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

	String datos_prueba = request.getParameter("datos_prueba");
	JSONObject jso = new JSONObject(datos_prueba);
	
	String prueba = jso.optString("prueba");
	int asignatura = Integer.parseInt(jso.optString("asignatura"));
	JSONObject respuesta = new JSONObject();

	double aprobados = Manager.get().getCursos(asignatura)
	
	respuesta.put("aprobados", String.format("%.2f", aprobados));
	response.setContentType("application/json");
	response.getWriter().write(respuesta.toString());
%>