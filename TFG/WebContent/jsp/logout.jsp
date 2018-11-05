<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="edu.uclm.esi.tfg.dominio.*, org.json.*" %>
    
<%
  JSONObject jso=new JSONObject();
  Profesor usuario=(Profesor) session.getAttribute("user");
  if (usuario==null)
    jso.put("result", "ERROR");
  else{
    try{
      if(Manager.get().logout(usuario.getNombre()).getNombre() == usuario.getNombre()){
        jso.put("result", "OK");
        session.setAttribute("user", null);
      }else{
        jso.put("result", "ERROR");
      }
    }catch(Exception e){
      jso.put("result", "ERROR");
    }
  }
  out.print(jso.toString());
%>