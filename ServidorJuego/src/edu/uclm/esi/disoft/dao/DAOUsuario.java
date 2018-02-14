package edu.uclm.esi.disoft.dao;

import java.util.Random;
import java.util.Vector;

import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.bson.Document;
import org.json.JSONObject;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Sorts;

import edu.uclm.esi.disoft.dominio.Usuario;

public class DAOUsuario {

	public static void insert(Usuario usuario) {
		BsonDocument bso=new BsonDocument();
		bso.append("nombre", new BsonString(usuario.getNombre()));
		String pwd = "1234";
		bso.append("pwd", new BsonString(pwd));
		MongoBroker broker=MongoBroker.get();
		MongoCollection<BsonDocument> usuarios=broker.getCollection("Usuarios");
		usuarios.insertOne(bso);
	}

	public static void insert(Usuario usuario, String pwd) {
		BsonDocument bso=new BsonDocument();
		bso.append("nombre", new BsonString(usuario.getNombre()));
		pwd=""+pwd.hashCode();
		bso.append("pwd", new BsonString(pwd));
		bso.append("victorias", new BsonInt32(0));

		MongoBroker broker=MongoBroker.get();
		MongoCollection<BsonDocument> usuarios=broker.getCollection("Usuarios");
		usuarios.insertOne(bso);
	}

	public static Usuario select(String nombre, String pwd) throws Exception{
		MongoBroker broker=MongoBroker.get();
		MongoCollection<BsonDocument> usuarios=broker.getCollection("Usuarios");
		BsonDocument criterio=new BsonDocument();
		criterio.append("nombre", new BsonString(nombre));
		pwd=""+pwd.hashCode();
		criterio.append("pwd", new BsonString(pwd));
		FindIterable<BsonDocument> resultado = usuarios.find(criterio);
		BsonDocument usuario=resultado.first();
		if(usuario==null)
			throw new Exception("Credenciales incorrectas");
		Usuario result=new Usuario(nombre,pwd);
		return result;

	}
	public static Usuario select(String nombre) throws Exception{
		MongoBroker broker=MongoBroker.get();
		MongoCollection<BsonDocument> usuarios=broker.getCollection("Usuarios");
		BsonDocument criterio=new BsonDocument();
		criterio.append("nombre", new BsonString(nombre));
		FindIterable<BsonDocument> resultado = usuarios.find(criterio);
		BsonDocument usuario=resultado.first();
		if(usuario==null)
			throw new Exception("Credenciales incorrectas");
		Usuario result=new Usuario(nombre);
		return result;

	}
	public static String update(String nombre) throws Exception{
		MongoBroker broker=MongoBroker.get();
		MongoCollection<BsonDocument> usuarios=broker.getCollection("Usuarios");
		BsonDocument criterio=new BsonDocument();

		criterio.append("nombre", new BsonString(nombre));

		int resultadopwd = new Random().nextInt(900)+1000;
		String newPwd=String.valueOf(resultadopwd);
		String newPwdDB =""+newPwd.hashCode();

		BsonDocument criterio2=new BsonDocument();
		criterio2.append("nombre", new BsonString(nombre));
		criterio2.append("pwd", new BsonString(newPwdDB));

		usuarios.findOneAndReplace(criterio, criterio2);

		return newPwd;
	}
	public static void delete(String nombre,String pwd) throws Exception{
		Usuario usuario=select(nombre, pwd);
		BsonDocument bso=new BsonDocument();
		bso.append("nombre", new BsonString(usuario.getNombre()));
		bso.append("pwd", new BsonString(usuario.getPwd()));

		MongoBroker broker=MongoBroker.get();
		MongoCollection<BsonDocument> usuarios=broker.getCollection("Usuarios");
		usuarios.deleteOne(bso);
	}
	public static void insertVictorias(String nombre) throws Exception{
		MongoBroker broker = MongoBroker.get();
		MongoCollection<BsonDocument> usuarios = broker.getCollection("Usuarios");
		BsonDocument criterio = new BsonDocument();
		criterio.append("nombre", new BsonString(nombre));
		BsonDocument bso = new BsonDocument();
		bso.append("$inc", new BsonDocument().append("victorias", new BsonInt32(1)));
		usuarios.updateOne(criterio, bso);   
	}
	public static Vector<Usuario> rankingVictorias(){
		
		MongoCollection<BsonDocument> dbusuarios = MongoBroker.get().getCollection("Usuarios");
		BsonDocument criterioOrdenacion= new BsonDocument("victorias", new BsonInt32(1));
		
		MongoCursor<BsonDocument> usuarios = dbusuarios.find().sort(criterioOrdenacion).limit(3).iterator();
		Vector<Usuario> result =new Vector<>();
		int victorias;
		String nombre;
		Usuario usuario;
		BsonDocument bso;
		
		while(usuarios.hasNext()){
			bso=usuarios.next();
			nombre=bso.get("nombre").asString().getValue();
			victorias=bso.get("victorias").asInt32().getValue();
			usuario=new Usuario(nombre, victorias);
			result.add(usuario);
			
		}
		
		return result;
	}
}