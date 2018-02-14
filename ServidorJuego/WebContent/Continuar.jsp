<%@ page language="java" contentType="application/json; charset=UTF-8"
 pageEncoding="UTF-8"%>
<%@ page import="org.json.*, edu.uclm.esi.disoft.dominio.Manager"%>

<%
 String p = request.getParameter("p");
 JSONObject resultado = new JSONObject();

 try {
  JSONObject jso = new JSONObject(p);
  if (jso.getString("tipo").equals("Continuar")) { 
   resultado.put("tipo", "error");
   resultado.put("texto", "Mensaje inesperado");
  } else {
	  //String jugador=jso.getString("jugador");
		//String pwd1=jso.getString("pwd1");
		//String pwd2=jso.getString("pwd2");
		//Manager.get().registrarse(jugador, pwd1, pwd2);
		//resultado.put("tipo", "OK");
 
   
  }
 } catch (Exception e) {
  resultado.put("tipo", "error");
  resultado.put("texto", "Mensaje inesperado");
 }
%>

<%= resultado.toString()%>
