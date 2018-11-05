<%@ page language="java" contentType="application/json; charset=UTF-8"
	pageEncoding="UTF-8" %>
<%@ page import="edu.uclm.esi.tfg.dominio.*, org.json.*" %>

<%
  JSONObject jso=new JSONObject();
  Profesor usuario=(Profesor) session.getAttribute("user");
  if (usuario==null)
    jso.put("result", "ERROR");
  else
    jso.put("result", "OK");

  out.print(jso.toString());
  %>