package edu.uclm.esi.disoft.dominio;

public class MovimientoHLF extends Movimiento {
	private int fila;
	private int columna;
	private Partida partida;
	public MovimientoHLF(Partida partida, String jugador, int fila, int columna) {
		super(jugador);
		this.columna=columna;
		this.fila=fila;
		this.partida=partida;
	}

	@Override
	protected void realizarMovimiento(Partida partida, Movimiento m) throws Exception {
		partida.comprobarlegalidad(m);
		
	}
	public int getFila(){
		return this.fila;
	}
	public int getColumna(){
		return this.columna;
		
	}

}
