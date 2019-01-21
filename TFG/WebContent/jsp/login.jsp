<%@page import="org.json.JSONObject, edu.uclm.esi.tfg.dominio.*"%>
<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	String login = request.getParameter("login");
	JSONObject jso = new JSONObject(login);
	JSONObject respuesta = new JSONObject();
	try {
		String id = jso.optString("id");
		String pwd = jso.optString("pwd");
		Profesor user = Manager.get().login(id, pwd);
		if (user != null) {
			session.setAttribute("user", user);
			respuesta.put("result", "OK");
			respuesta.put( id , " conectado");
			
			Manager.get().cargarDatos();
		} else {
			respuesta.put("result", "ERROR");
			respuesta.put("mensaje", "Usuario o contraseña incorrectos");
		}

	} catch (Exception e) {
		respuesta.put("result", "ERROR");
		respuesta.put("mensaje", "Usuario o contraseña incorrectos");
	}
	out.println(respuesta.toString());
%>