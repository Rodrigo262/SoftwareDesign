package edu.uclm.esi.disoft.auxiliares;
import java.io.*;
import org.bson.*;
import com.mongodb.*;
import com.mongodb.client.*;
import edu.uclm.esi.disoft.dao.DAOUsuario;
import edu.uclm.esi.disoft.dominio.Usuario;

public class Cargadora {
	public static void main(String[]args)throws IOException{
		MongoClient mongoClient= new MongoClient("localhost", 27017);
		MongoDatabase db=mongoClient.getDatabase("Juegos");
		if(db.getCollection("Usuarios")==null)
			db.createCollection("Usuarios");
		MongoCollection<BsonDocument> usuarios=db.getCollection("Usuarios", BsonDocument.class);
		BsonDocument lola=new BsonDocument();
		
		FileInputStream fis = new FileInputStream("/Users/rodrigo/Documents/UNI/2Cuatrimestre/Diseno/nombres.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String nombre;
		while((nombre=br.readLine())!=null){
			Usuario usuario = new Usuario(nombre);
			DAOUsuario.insert(usuario); 
		}
		fis.close();
	}

}
