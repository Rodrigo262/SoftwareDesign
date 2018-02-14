package edu.uclm.esi.disoft.dominio;

import org.json.JSONException;

public class MovimientoTER extends Movimiento {
	private int fila;
	private int columna;
	private Partida partida;

	public MovimientoTER(Partida partida, String jugador, int fila, int columna){
		super(jugador);
		this.columna=columna;
		this.fila=fila;
		this.partida=partida;
	
	}
	public int getFila(){
		return this.fila;
	}
	public int getColumna(){
		return this.columna;
		
	}
	protected void realizarMovimiento(Partida partida, Movimiento m) throws Exception {
		partida.comprobarlegalidad(m);
	}
}
