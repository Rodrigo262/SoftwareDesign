package edu.uclm.esi.disoft.dominio.HundirLaFlota;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uclm.esi.disoft.dao.DAOUsuario;
import edu.uclm.esi.disoft.dominio.Movimiento;
import edu.uclm.esi.disoft.dominio.Partida;
import edu.uclm.esi.disoft.dominio.Usuario;

public class HundirLaFlota extends Partida {
	private Usuario jugadorA;
	private Usuario jugadorB;
	private Usuario jugadorConElTurno;
	private char[][] tableroA;
	private char[][] tableroB;

	private static char B='B', D='D', T='T', WHITE = ' ';
	public HundirLaFlota(Usuario a, Usuario b) throws JSONException{
		super();
		this.jugadorA=a;
		this.jugadorB=b;	
		this.add(a);
		this.add(b);
		this.jugadorConElTurno=a;
		notificarInicioDePartida();
		iniciarTablero();
	}
	private void iniciarTablero() {
		tableroA=new char[6][3];
		tableroB=new char[6][3];
		for (int fila=0; fila<6; fila++)
			for (int columna=0; columna<3; columna++){
				tableroA[fila][columna]=WHITE;
				tableroB[fila][columna]=WHITE;
			}
	}
	private void imprimirTablero() {
		System.out.println("Tablero jugador A");
		for(int i=0; i<tableroA.length;i++){
			for(int j=0; j<tableroA[i].length;j++){
				System.out.print("["+tableroA[i][j]+"]");
			}
			System.out.println();
		}
		System.out.println("Tablero jugador B");
		for(int i=0; i<tableroB.length;i++){
			for(int j=0; j<tableroB[i].length;j++){
				System.out.print("["+tableroB[i][j]+"]");
			}
			System.out.println();
		}
	}
	protected void notificarInicioDePartida() throws JSONException {
		JSONObject jso=new JSONObject();
		jso.put("IdPartida", this.IdPartida);
		jso.put("jugador1", jugadorA.getNombre());
		jso.put("jugador2", jugadorB.getNombre());
		jso.put("jugadorConElTurno",jugadorConElTurno.getNombre());
		jso.put("tipo", "CrearPartida");
		jugadorA.enviarMensaje(jso);
		jugadorB.enviarMensaje(jso);
	}
	protected void actualizarTablero(Movimiento m) throws Exception {
		boolean tocado=false;
		JSONObject resultado = new JSONObject();
		System.out.println(jugadorConElTurno.getNombre());
		if (this.jugadorConElTurno.getNombre().equals(jugadorA.getNombre())) {
			if(m.getFila()<3){
				this.tableroA[m.getFila()][m.getColumna()]=B;
				resultado.put("tipo", "colocarBarco");
				resultado.put("marca", "B");
				resultado.put("fila", m.getFila());
				resultado.put("columna", m.getColumna());
				jugadorConElTurno.enviarMensaje(resultado);
				setTurno();
			}else{
				tocado = comprobarTocado(tableroB, m.getFila()-3, m.getColumna());
				if(tocado==true){
					this.tableroA[m.getFila()][m.getColumna()]=T;
					resultado.put("tipo", "tocado");
					resultado.put("marca", "T");
					resultado.put("fila", m.getFila());
					resultado.put("columna", m.getColumna());
					resultado.put("jugador", jugadorConElTurno.getNombre());
					jugadorConElTurno.enviarMensaje(resultado);
					jugadorConElTurno.setVictoria(jugadorConElTurno.getVictoria()+1);
					DAOUsuario.insertVictorias(jugadorConElTurno.getNombre()); 
					imprimirTablero();
					return;
				}else{
					this.tableroA[m.getFila()][m.getColumna()]=D;
					resultado.put("tipo", "disparo");
					resultado.put("marca", "D");
					resultado.put("fila", m.getFila());
					resultado.put("columna", m.getColumna());
					jugadorConElTurno.enviarMensaje(resultado);
					setTurno();
				}
			}
			imprimirTablero();
		} else {
			if(m.getFila()<3){
				this.tableroB[m.getFila()][m.getColumna()]=B;
				resultado.put("tipo", "colocarBarco");
				resultado.put("marca", "B");
				resultado.put("fila", m.getFila());
				resultado.put("columna", m.getColumna());
				jugadorConElTurno.enviarMensaje(resultado);
				setTurno();
			}else{
				tocado = comprobarTocado(tableroA, m.getFila()-3, m.getColumna());
				if(tocado==true){
					this.tableroA[m.getFila()][m.getColumna()]=T;
					resultado.put("marca", "T");
					resultado.put("fila", m.getFila());
					resultado.put("columna", m.getColumna());
					resultado.put("tipo", "tocado");
					jugadorConElTurno.enviarMensaje(resultado);
					jugadorConElTurno.setVictoria(jugadorConElTurno.getVictoria()+1);
					DAOUsuario.insertVictorias(jugadorConElTurno.getNombre()); 
					imprimirTablero();
				}else{
					this.tableroB[m.getFila()][m.getColumna()]=D;
					resultado.put("tipo", "disparo");
					resultado.put("marca", "D");
					resultado.put("fila", m.getFila());
					resultado.put("columna", m.getColumna());
					jugadorConElTurno.enviarMensaje(resultado);
					setTurno();
				}
			}
			imprimirTablero();	
			return;
		}

	}
	private boolean comprobarTocado(char[][] tablero, int fila, int columna) {
		if(tablero[fila][columna]==B)
			return true;
		
		return false;
	}
	protected void comprobarlegalidad(Movimiento m) throws JSONException, Exception {
		JSONObject resultado = new JSONObject();
		boolean legal = true;
		if (!this.jugadorConElTurno.getNombre().equals(m.getJugador())) {
			legal=false;
			resultado.put("tipo", "legalidad");
			resultado.put("error", "No es tu turno");
			jugadorConElTurno.enviarMensaje(resultado);
			System.out.println("No es tu turno");
			return;
		} 
		if(legal==true)
			actualizarTablero(m);
	}
	protected void setTurno() throws JSONException {
		JSONObject resultado = new JSONObject();
		if(this.jugadorA.getNombre().equals(jugadorConElTurno.getNombre())){
			this.jugadorConElTurno=this.jugadorB;
			System.out.println("Turno de: "+jugadorConElTurno.getNombre());
			resultado.put("tipo","turno");
			resultado.put("turno", this.jugadorB.getNombre());
			jugadorA.enviarMensaje(resultado);
		}else{
			this.jugadorConElTurno=this.jugadorA;
			System.out.println("Turno de: "+jugadorConElTurno.getNombre());
			resultado.put("tipo","turno");
			resultado.put("turno", this.jugadorA.getNombre());
			jugadorB.enviarMensaje(resultado);
		}
	}
	public Usuario getJugadorA() {
		return this.jugadorA;
	}
	public Usuario getJugadorB() {
		return this.jugadorB;
	}
	public void setId(String string) {
	}
}
