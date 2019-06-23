<%@page import="org.json.JSONObject, edu.uclm.esi.tfg.dominio.*"%>
<%@ page language="java" contentType="application/json; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	String p = request.getParameter("p");
	JSONObject jso = new JSONObject(p);
	JSONObject respuesta = new JSONObject();
	try {
		String id = jso.getString("id");
		String pwd1 = jso.getString("pwd1");
		String pwd2 = jso.getString("pwd2");
		String nombre = jso.getString("nombre");

		comprobarCredenciales(id, pwd1, pwd2);
		Profesor user = Manager.get().registrar(id, pwd1, nombre);
		if (user != null) {
			session.setAttribute("user", user);
			respuesta.put("result", "OK");
			respuesta.put("mensaje", "Usuario registrado correctamente");
		} else {
			throw new Exception("No se pudo registrar el usuario");
		}
	
	} catch (Exception e) {
		//No devolver el error directamente sino captar el code y devolver un mensaje predefinido
		respuesta.put("result", "ERROR");
		respuesta.put("mensaje", e.getMessage());

	}
	out.println(respuesta.toString());
%>

<%!private void comprobarCredenciales(String id, String pwd1, String pwd2) throws Exception {
		if (id.length() == 0) {
			throw new Exception("El ID no puede estar vacío");
		}
		if (!pwd1.equals(pwd2)) {
			throw new Exception("Las contraseñas no coindicen");
		}
		if (pwd1.length() < 4) {
			throw new Exception("La contraseña tiene que tener al menos 4 caracteres");
		}
	}%>