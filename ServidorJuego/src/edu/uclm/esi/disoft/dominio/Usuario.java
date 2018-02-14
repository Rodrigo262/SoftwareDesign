package edu.uclm.esi.disoft.dominio;

import javax.websocket.Session;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uclm.esi.disoft.websocket.WSServer;

public class Usuario {
	private String nombre;
	private String pwd;
	private Partida partida;
	private Session session;
	private int id;
	private String juego;
	private int victoria;

	public Usuario(String nombre){
		this.nombre = nombre;
		this.pwd = pwd;
		this.partida= partida;
		this.juego=juego;
		this.victoria=victoria;
	}
	
	public Usuario(String nombre,String pwd){
		this.nombre = nombre;
		this.pwd = pwd;
		this.partida= partida;
		this.juego=juego;
		//this.victoria=victoria;
	}

	public Usuario(String nombre, int victoria) {
		this.nombre = nombre;
		this.victoria=victoria;
	}

	public String getNombre() {
		return nombre;
	}
	public void setPartida(Partida partida){
		this.partida=partida;
	}
	public Partida getPartida(){
		return partida;
	}

	public void salirDePartida() throws JSONException {
		this.partida.sacarJugador(this, "Abandono");
	}

	public void insert(String pwd1) {
			
	}

	public void setSession(Session session) {
		this.session=session;
	}
	public Session getSession(){
		return session;
	}
	public void enviarMensaje(JSONObject mensaje) throws JSONException{
		WSServer.send(this.session.getId(), mensaje);
	}

	public String getJuego() {
		return juego;
	}

	public void setJuego(String juego) {
		this.juego=juego;
		
	}

	public String getPwd() {
		return this.pwd;
	}

	public void setPwd(String pwd) {
		this.pwd=pwd;
	}
	public void setVictoria(int n){
		this.victoria=victoria;
	}
	public int getVictoria(){
		return this.victoria;
	}
}
