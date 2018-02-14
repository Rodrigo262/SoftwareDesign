package edu.uclm.esi.disoft.dominio;

public abstract class Movimiento {
	protected String jugador;
	
	public Movimiento(String jugador) {
		this.jugador=jugador;
	}

	public String getJugador() {
		return this.jugador;
	}
	protected abstract void realizarMovimiento(Partida partida, Movimiento m) throws Exception;

	public int getFila() {
		return 0;
	}

	public int getColumna() {
		return 0;
	}

}
