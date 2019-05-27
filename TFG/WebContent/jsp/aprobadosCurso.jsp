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
	Map<String, Integer> mapa = Manager.get().getAprobadosCurso(asignatura);
	
	Map<String, Integer> treeMap = new TreeMap<>(); 
    treeMap.putAll(mapa); 

    
	Iterator<Map.Entry<String, Integer>> it = treeMap.entrySet().iterator();

	while (it.hasNext()) {
		Map.Entry<String, Integer> dupla = (Map.Entry<String, Integer>) it.next();
		String curso = dupla.getKey();
		String aprobados = dupla.getValue().toString();
		json.accumulate("years", curso); 
        json.accumulate("aprobados", aprobados); 
	}
	
	//Gson gson = new Gson(); // com.google.gson.Gson
	//String jsonFromMap = gson.toJson(map);
	System.out.println(json); // 
	respuesta.put("aprobados", json);
	response.setContentType("application/json");
	response.getWriter().write(respuesta.toString());
%>