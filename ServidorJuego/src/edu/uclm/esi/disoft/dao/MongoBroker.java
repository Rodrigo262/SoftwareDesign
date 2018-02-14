package edu.uclm.esi.disoft.dao;

import org.bson.BsonDocument;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoBroker {
	
	private static MongoBroker yo;
	private MongoClient mongoClient;
	private MongoDatabase db;
	
	private MongoBroker(){
		this.mongoClient= new MongoClient("localhost",27017);
		this.db=mongoClient.getDatabase("Juegos");
	}
	public static MongoBroker get(){
		if(yo==null)
			yo=new MongoBroker();
		return yo;
	}
	public MongoCollection<BsonDocument> getCollection(String collection){
		MongoCollection<BsonDocument> result=db.getCollection(collection, BsonDocument.class);
		if(result==null){
			db.createCollection(collection);
			result=db.getCollection(collection,BsonDocument.class);
		}
		return result;
	}

}