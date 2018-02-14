<%@ page language="java" contentType="application/json; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import=" org.json.*, edu.uclm.esi.disoft.dominio.Manager"%>
<%
response.setHeader("Access-Control-Allow-Origin", "*");
String p=request.getParameter("p");
JSONObject resultado=new JSONObject();

try{
  JSONObject jso=new JSONObject(p);
  
  if(!jso.getString("tipo").equals("Logout")){
    resultado.put("tipo", "error");
    resultado.put("texto", "Mensaje inesperado");
  }else{
    
    String jugador=jso.getString("jugador");
    Manager.get().logout(jugador);
    resultado.put("tipo", "OK");
  
    }
  }
  catch(Exception e){
    resultado.put("tipo", "error");
    resultado.put("texto", e.getMessage());
}
%>
<%=resultado.toString()%>