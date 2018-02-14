<%@ page language="java" contentType="application/json; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.json.*, edu.uclm.esi.disoft.dominio.Manager"%>

<%
response.setHeader("Access-Control-Allow-Origin", "*");
	String p = request.getParameter("p");
	JSONObject resultado = new JSONObject();

	try {
		JSONObject jso = new JSONObject(p);
		if (!jso.getString("tipo").equals("RecibirMovimiento")) { 
			resultado.put("tipo", "error");
			resultado.put("texto", "Mensaje inesperado");
		} else if(jso.getString("tipo").equals("RecibirMovimiento")){
			String jugador= jso.getString("jugador");
			int columna = jso.getInt("columna");
			int fila=jso.getInt("fila");
			Manager.get().recibirMovimiento(jugador, fila, columna);
			resultado.put("tipo", "OK");
		}
	} catch (Exception e) {
		resultado.put("tipo", "error");
		resultado.put("texto", "Mensaje inesperado2");
	}
%>

<%= resultado.toString()%>
