package edu.uclm.esi.disoft.dominio;

import java.util.Enumeration;
import java.util.Hashtable;
import org.json.JSONException;
import org.json.JSONObject;

import edu.uclm.esi.disoft.dao.DAOHundirLaFlota;
import edu.uclm.esi.disoft.dao.DAOTresEnRaya;
import edu.uclm.esi.disoft.dominio.HundirLaFlota.HundirLaFlota;
import edu.uclm.esi.disoft.dominio.TresEnRaya.TresEnRaya;

public abstract class Partida {
	protected int IdPartida;
	protected Hashtable<String, Usuario> jugadores;
	//protected Usuario jugadorConElTurno;
	//private Usuario jugadorA;
	//private Usuario jugadorB;
	
	
	public Partida(){
		this.IdPartida = new java.util.Random().nextInt();
		this.jugadores = new Hashtable<String, Usuario> ();
	}
	
	public void add(Usuario jugador){
		this.jugadores.put(jugador.getNombre(), jugador);
		jugador.setPartida(this);
	}
	
	public void mover(Movimiento m) throws Exception{
		comprobarlegalidad(m);
		actualizarTablero(m);
		setTurno();
	}
	
	public int getIdPartida() {
		return IdPartida;
	
	}
	//public Usuario getJugadorConElTurno(){
		//return jugadorConElTurno;
	//}
	
	
	protected abstract void notificarInicioDePartida() throws JSONException;
	protected abstract void actualizarTablero(Movimiento m) throws Exception;
	protected abstract void comprobarlegalidad(Movimiento m) throws JSONException, Exception;
	protected abstract void setTurno() throws JSONException;

	public void sacarJugador(Usuario usuario, String motivo) throws JSONException {
		JSONObject jso = new JSONObject();
		jso.put("tipo", motivo);
		jso.put("texto", usuario.getNombre() + " se ha ido");
		Enumeration<Usuario>jugadores=this.jugadores.elements();
		while(jugadores.hasMoreElements()){
			Usuario jugador=jugadores.nextElement();
			if(jugador!= usuario){
				Manager.get().quitarActivo(jugador);
				Manager.get().addOcioso(jugador);
				System.out.println(jso.toString());
				
			}
		}
	}

	public void insertTER() {
		DAOTresEnRaya.insert((TresEnRaya) this);
	}
	public void insertHLF(){
		DAOHundirLaFlota.insert((HundirLaFlota) this);
	}
}
