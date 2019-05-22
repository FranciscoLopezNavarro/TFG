<%@page import="com.google.gson.JsonArray"%>
<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject, edu.uclm.esi.tfg.dominio.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.ArrayList"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	String delete = request.getParameter("delete");
	JSONObject jso = new JSONObject(delete);

	try {
		String alumno_delete = jso.optString("alumno");
		String year = jso.optString("year").replace(" ", "");;
		if (alumno_delete != null) {
			Manager.get().eliminarRegistro(Integer.parseInt(alumno_delete), year);
		}

	} catch (Exception e) {
		e.printStackTrace();
	}
%>