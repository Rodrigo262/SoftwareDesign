<%@ page language="java" contentType="application/json; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.json.*, edu.uclm.esi.disoft.dominio.Manager"%>

<%
	response.setHeader("Access-Control-Allow-Origin", "*");
	String p = request.getParameter("p");
	JSONObject resultado = new JSONObject();
	JSONArray resultadoArray = new JSONArray();

	try {
		JSONObject jso = new JSONObject(p);
		if (!jso.getString("tipo").equals("Ranking")) {
			resultado.put("tipo", "error");
			resultado.put("texto", "Mensaje inesperado");
		} else {
			String jugador = jso.getString("jugador");
			resultadoArray=Manager.get().rankingJuego(jugador);
			System.out.print(resultadoArray.toString());
		}
	} catch (Exception e) {
		resultado.put("tipo", "error");
		resultado.put("texto", "Mensaje inesperado2");
	}
%>

<%=resultadoArray.toString()%>
