package edu.uclm.esi.disoft.dominio.TresEnRaya;


import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uclm.esi.disoft.dao.DAOUsuario;
import edu.uclm.esi.disoft.dominio.*;

public class TresEnRaya extends Partida {
	private Usuario jugadorA;
	private Usuario jugadorB;
	private Usuario jugadorConElTurno;
	private char[][] tablero;
	private static char X='X', O='O', WHITE = ' ';
	public TresEnRaya(Usuario a, Usuario b) throws JSONException{
		super();
		this.jugadorA=a;
		this.jugadorB=b;	
		this.add(a);
		this.add(b);
		this.jugadorConElTurno=a;
		notificarInicioDePartida();
		iniciarTablero();
	}
	@Override

	protected void actualizarTablero(Movimiento m) throws Exception {
		JSONObject resultado = new JSONObject();
		boolean ganar;
		boolean empate = false;
		System.out.println(jugadorConElTurno.getNombre());
		if (this.jugadorConElTurno.getNombre().equals(jugadorA.getNombre())) {
			this.tablero[m.getFila()][m.getColumna()]=X;
			resultado.put("tipo", "movimiento");
			resultado.put("ficha", "X");
			resultado.put("fila", m.getFila());
			resultado.put("columna", m.getColumna());
			jugadorConElTurno.enviarMensaje(resultado);
			jugadorB.enviarMensaje(resultado);
			ganar = ganar(X);
			empate=comprobarEmpate();
			imprimirTablero();
			if(ganar==true){
				jugadorConElTurno.setVictoria(jugadorConElTurno.getVictoria()+1);
				DAOUsuario.insertVictorias(jugadorConElTurno.getNombre()); 
				resultado.put("tipo", "victoria");
				resultado.put("jugador", jugadorConElTurno.getNombre());
				jugadorConElTurno.enviarMensaje(resultado);
				jugadorB.enviarMensaje(resultado);
				System.out.println(jugadorConElTurno.getNombre()+ " ha ganado!!!");
				return;
			}
			if(empate==true){
				resultado.put("tipo", "empate");
				resultado.put("empate", "Empate");
				jugadorConElTurno.enviarMensaje(resultado);
				jugadorB.enviarMensaje(resultado);
				System.out.println("Ha habido un empate");
				return;
			}
			setTurno();
		} else {
			this.tablero[m.getFila()][m.getColumna()]=O;
			resultado.put("tipo", "movimiento");
			resultado.put("ficha", "O");
			resultado.put("fila", m.getFila());
			resultado.put("columna", m.getColumna());
			jugadorConElTurno.enviarMensaje(resultado);
			jugadorA.enviarMensaje(resultado);
			ganar = ganar(O);
			imprimirTablero();
			if(ganar==true){
				resultado.put("tipo", "victoria");
				resultado.put("jugador", jugadorConElTurno.getNombre());
				jugadorConElTurno.enviarMensaje(resultado);
				jugadorA.enviarMensaje(resultado);
				jugadorConElTurno.setVictoria(jugadorConElTurno.getVictoria()+1);
				DAOUsuario.insertVictorias(jugadorConElTurno.getNombre()); 
				System.out.println(jugadorConElTurno.getNombre()+ " ha ganado!!!");
				return;
			}
			if(empate==true){
				resultado.put("tipo", "empate");
				resultado.put("empate", "Empate");
				jugadorConElTurno.enviarMensaje(resultado);
				jugadorA.enviarMensaje(resultado);
				System.out.println("Ha habido un empate");
				return;
			}
			setTurno();
		}
	}
	private void imprimirTablero() {
		for(int i=0; i<tablero.length;i++){
			for(int j=0; j<tablero[i].length;j++){
				System.out.print("["+tablero[i][j]+"]");
			}
			System.out.println();
		}

	}
	private boolean comprobarEmpate() {
		int contador=0;
		for(int i=0; i<tablero.length;i++){
			for(int j=0; j<tablero[i].length;j++){
				if(tablero[i][j]!=WHITE){
					contador++;
				}
			}
			System.out.println();
		}
		if(contador==9)
		return true;
		else 
		return false;
	}
	

	private void iniciarTablero() {
		tablero=new char[3][3];
		for (int fila=0; fila<3; fila++)
			for (int columna=0; columna<3; columna++)
				tablero[fila][columna]=WHITE;
	}
	@Override
	protected void comprobarlegalidad(Movimiento m) throws Exception {
		JSONObject resultado = new JSONObject();
		boolean legal = true;
		if (!this.jugadorConElTurno.getNombre().equals(m.getJugador())) {
			legal=false;
			resultado.put("tipo", "legalidad");
			resultado.put("error", "No es tu turno");
			jugadorConElTurno.enviarMensaje(resultado);
			System.out.println("Casilla Ocupada");
			return;

		} else if (this.tablero[m.getFila()][m.getColumna()]!=WHITE) {
			legal=false;
			resultado.put("tipo", "legalidad");
			resultado.put("error", "La casilla ya esta ocupada");
			jugadorConElTurno.enviarMensaje(resultado);
			System.out.println("Casilla Ocupada");
			return;
		}
		if(legal==true)
			actualizarTablero(m);

	}

	@Override
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
	@Override
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
	public boolean ganar(char marca ){
		//busqueda de ganador por filas
		for (int i=0; i<this.tablero.length; i++){
			int count=0;
			for (int j=0 ; j<this.tablero.length; j++)
				if(this.tablero[i][j]==marca)
					count+=1;
			if( count == 3)
				return true;
		}
		//busqueda de ganador por columnas
		for (int j=0 ; j<this.tablero.length; j++){
			int count=0;
			for ( int i=0 ; i< this.tablero.length; i++)
				if( this.tablero[i][j]==marca )
					count+=1;
			if( count == 3)
				return true;
		}
		//diagonales
		if(this.tablero[0][0]==marca && this.tablero[1][1]==marca && this.tablero[2][2]==marca)
			return true;
		if(this.tablero[0][2]==marca && this.tablero[1][1]==marca && this.tablero[2][0]==marca)
			return true;
		return false;
	}
}
