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
	String calculo_alerta = request.getParameter("calculo_alerta");
	JSONObject jso = new JSONObject(calculo_alerta);

	int alumno = Integer.parseInt(jso.optString("alumno"));
	int asignatura = Integer.parseInt(jso.optString("asignatura"));
	String curso = jso.optString("curso").replace(" ", "");
	JSONObject respuesta = new JSONObject();

	double riesgo = Manager.get().calculoAlertaAsig(alumno, asignatura, curso)*100;

	respuesta.put("riesgo", riesgo);
	response.setContentType("application/json");
	response.getWriter().write(respuesta.toString());
%>