package edu.uclm.esi.disoft.websocket;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uclm.esi.disoft.dominio.Manager;
@ServerEndpoint ("/WSServer")
public class WSServer {
	private static Hashtable<String, Session> sesiones = new Hashtable<String, Session>();

	@OnOpen
	public void open (Session session) throws JSONException{
		String sessionID = session.getId();
		Map<String,List<String>>parametros=session.getRequestParameterMap();
		String nombreJugador = parametros.get("jugador").get(0);
		Manager.get().setSession(nombreJugador, session);
		sesiones.put(session.getId(), session);
		System.out.println("Creada sesión de websocket: "+ sessionID);
		JSONObject jso= new JSONObject();
		try{
			jso.put("tipo", "sessionId");
			jso.put("sessionId", session.getId());
		} catch(Exception e){}
		send(session, jso);
		Manager.get().quizasCrearPartida(nombreJugador);
	}
	@OnClose
	public void close(Session session){
		String sessionId=session.getId();
		System.out.println("Conexion Cerrada. Id: " + sessionId);
		sesiones.remove(sessionId);
			
	}

	public static void send(Session session, JSONObject jso){
		try{
			session.getBasicRemote().sendText(jso.toString());
		}catch(IOException e){
			sesiones.remove(session.getId());
		}
	}
	@OnMessage
	public static void send(String sessionId, JSONObject jso){
		try{
			Session session = sesiones.get(sessionId);
			session.getBasicRemote().sendText(jso.toString());
		}catch(IOException e){
			sesiones.remove(sessionId);
		}
	}	
	public static void broadcast(String string) {
		System.out.println(string);
	}

}
