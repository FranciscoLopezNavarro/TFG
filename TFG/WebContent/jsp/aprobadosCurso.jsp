<%@page import="com.google.gson.JsonArray"%>
<%@page import="com.google.gson.*"%>
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
	int asignatura = Integer.parseInt(jso.optString("asignatura"));

	JSONObject respuesta = new JSONObject();
	JSONObject json = new JSONObject();
	Map<String, Double> mapa = Manager.get().getAprobadosCurso(asignatura);
	
	Map<String, Double> treeMap = new TreeMap<>(); 
    treeMap.putAll(mapa); 

	Iterator<Map.Entry<String, Double>> it = treeMap.entrySet().iterator();

	while (it.hasNext()) {
		Map.Entry<String, Double> dupla = (Map.Entry<String, Double>) it.next();
		String curso = dupla.getKey();
		String aprobados = String.format("%.2f", dupla.getValue());
		json.accumulate("years", curso); 
        json.accumulate("aprobados", aprobados); 
	}
	
	respuesta.put("aprobados", json);
	response.setContentType("application/json");
	response.getWriter().write(respuesta.toString());
%>