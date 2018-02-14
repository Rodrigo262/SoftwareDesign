package edu.uclm.esi.disoft.dao;
import org.bson.BsonDocument;
import org.bson.BsonString;

import edu.uclm.esi.disoft.dominio.*;
import edu.uclm.esi.disoft.dominio.TresEnRaya.*;

public class DAOTresEnRaya {
	public static void insert(TresEnRaya partida){
		Usuario jugadorA = partida.getJugadorA();
		Usuario jugadorB = partida.getJugadorB();
		
		BsonDocument bso = new BsonDocument();
		bso.append("nombreJugadorA", new BsonString(jugadorA.getNombre()));
		bso.append("nombreJugadorB", new BsonString(jugadorB.getNombre()));
		
		MongoBroker.get().getCollection("TresEnRaya").insertOne(bso);
		partida.setId(bso.getObjectId("_id").getValue().toString());
	}
}
