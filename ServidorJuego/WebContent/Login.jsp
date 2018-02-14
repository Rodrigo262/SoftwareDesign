<%@ page language="java" contentType="application/json; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.json.*, edu.uclm.esi.disoft.dominio.Manager"%>

<%
	response.setHeader("Access-Control-Allow-Origin", "*");
	String p = request.getParameter("p");
	JSONObject resultado = new JSONObject();

	try {
		JSONObject jso = new JSONObject(p);
		if (!jso.getString("tipo").equals("Login")) {
			resultado.put("tipo", "error");
			resultado.put("texto", "Mensaje inesperado");
		} else {
			String jugador = jso.getString("jugador");
			String pwd = jso.getString("pwd");
			String juego= jso.getString("juego");
			Manager.get().login(jugador, pwd, juego);
			resultado.put("tipo", "OK");
		}
	} catch (Exception e) {
		resultado.put("tipo", "error");
		resultado.put("texto", "Mensaje inesperado2");
	}
%>

<%=resultado.toString()%>
