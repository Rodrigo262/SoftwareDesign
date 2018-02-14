package edu.uclm.esi.disoft.dominio;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.websocket.Session;

import org.bson.BsonDocument;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.MongoWriteException;
import edu.uclm.esi.disoft.dao.DAOUsuario;
import edu.uclm.esi.disoft.dominio.HundirLaFlota.HundirLaFlota;
import edu.uclm.esi.disoft.dominio.TresEnRaya.TresEnRaya;
import edu.uclm.esi.disoft.websocket.WSServer;

public class Manager {
	private static Manager yo;
	private Hashtable<String, Usuario> activos;
	private Hashtable<String, Usuario> ociosos;
	private Hashtable<Integer, Partida> partidas;
	//private String juego = "TresEnRaya";

	private Manager(){
		this.activos = new Hashtable<String, Usuario>();
		this.ociosos = new Hashtable<String, Usuario>();
		this.partidas = new Hashtable<Integer, Partida>();
	}

	public static Manager get(){
		if (yo == null)
			yo = new Manager();
		return yo;    
	}

	public void login(String nombre, String pwd, String juego) throws Exception {  
		System.out.println("Se ha unido " + nombre);
		Usuario jugador=DAOUsuario.select(nombre, pwd);
		jugador.setJuego(juego);
		if(ociosos.get(nombre)!=null)
			return;
		this.ociosos.put(nombre, jugador);
	}

	private void crearPartida(Usuario jugador, Usuario contrincante, String juego) throws JSONException {

		Partida partida=null;

		if(juego.equals("TresEnRaya")){
			partida=new TresEnRaya(jugador,contrincante);
			jugador.setPartida(partida);
			contrincante.setPartida(partida);
			this.ociosos.remove(contrincante.getNombre());
			this.activos.put(contrincante.getNombre(), contrincante);
			this.activos.put(jugador.getNombre(), jugador);
			partida.insertTER();
			this.partidas.put(partida.getIdPartida(), partida);
			System.out.println("Se ha creado la partida " + partida.getIdPartida());    
		}else if(juego.contentEquals("HundirLaFlota")){
			partida=new HundirLaFlota(jugador,contrincante);
			jugador.setPartida(partida);
			contrincante.setPartida(partida);
			this.ociosos.remove(contrincante.getNombre());
			this.activos.put(contrincante.getNombre(), contrincante);
			this.activos.put(jugador.getNombre(), jugador);
			partida.insertHLF();
			this.partidas.put(partida.getIdPartida(), partida);
			System.out.println("Se ha creado la partida " + partida.getIdPartida());
		}
	}

	public void logout(String nombre) throws JSONException{    
		System.out.println("Se ha ido " + nombre);
		if(this.ociosos.get(nombre)!=null){
			this.ociosos.remove(nombre);
			return;
		}

		Usuario jugador=this.activos.get(nombre);
		if(jugador==null)
			return;
		this.activos.remove(nombre);
		jugador.salirDePartida();
	}

	public void recibirMovimiento(String jugador, int columna, int fila) throws Exception{
		Enumeration<Usuario>jugadoresPartidas = this.activos.elements();
		Usuario jugadorTurno=null;
		while(jugadoresPartidas.hasMoreElements()){
			jugadorTurno=jugadoresPartidas.nextElement();
			if(jugador.equals(jugadorTurno.getNombre())){
				Partida partida = jugadorTurno.getPartida();
				Movimiento m = new MovimientoTER(partida, jugadorTurno.getNombre(), fila, columna);
				m.realizarMovimiento(partida, m);
			}
		}

	}
	public void colocarBarco(String jugador, int columna, int fila) throws Exception{
		Enumeration<Usuario>jugadoresPartidas = this.activos.elements();
		Usuario jugadorTurno=null;
		while(jugadoresPartidas.hasMoreElements()){
			jugadorTurno=jugadoresPartidas.nextElement();
			if(jugador.equals(jugadorTurno.getNombre())){
				Partida partida = jugadorTurno.getPartida();
				Movimiento m = new MovimientoHLF(partida, jugadorTurno.getNombre(), fila, columna);
				m.realizarMovimiento(partida, m);
			}
		}

	}

	public void registrarse(String nombre, String pwd1, String pwd2) throws Exception{
		if(!pwd1.equals(pwd2))
			throw new Exception("Las password no coinciden");
		if(pwd1.length()<4)
			throw new Exception("La contraseña debe de tener al menos 4 caracteres");
		Usuario usuario=new Usuario(nombre);
		try{
			DAOUsuario.insert(usuario,pwd1);
		}catch(MongoWriteException e){
			if(e.getCode()==11000)
				throw new Exception("Por favor, elige otro nombre poque ese ya está cogido");
			throw new Exception("Algo ha pasado que no se decirte");
		}
	}

	public void quitarActivo(Usuario jugador) {
		this.activos.remove(jugador.getNombre());
	}

	public void addOcioso(Usuario jugador) {
		this.ociosos.put(jugador.getNombre(), jugador);
	}

	public void setSession(String nombreJugador, Session session) {
		Usuario usuario=this.ociosos.get(nombreJugador);
		if(usuario!=null){
			usuario.setSession(session);
			return;
		}else{
			usuario= this.activos.get(nombreJugador);
		}
	}
	public String recuperarContrasena(String nombreJugador) throws Exception{
		String contra =	DAOUsuario.update(nombreJugador);
		return contra;
	}

	public void quizasCrearPartida(String nombreJugador) throws JSONException {
		if(ociosos.size()>=2){

			Usuario jugador = this.ociosos.elements().nextElement();
			Usuario contrincante=this.ociosos.get(nombreJugador);
			System.out.println("Contrincante: "+contrincante.getNombre() + " esta ocioso");
			WSServer.broadcast("Ha llegado "+nombreJugador);
			if(jugador.getJuego().equals(contrincante.getJuego()))
				this.crearPartida(jugador, contrincante,jugador.getJuego());
		}
	}
	public JSONArray rankingJuego(String nombre) throws Exception{
		//Usuario usuario=this.ociosos.get(nombre);
		Vector<Usuario>jugadores= new Vector<>();
		jugadores = DAOUsuario.rankingVictorias();
		
		JSONArray ja=new JSONArray();
		ja.put(jugadores);

		return ja;
	}
}