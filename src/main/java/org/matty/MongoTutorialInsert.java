package org.matty;

import static java.util.Arrays.asList;
import static org.matty.Helpers.printJson;

import org.bson.Document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Hands on MongoDB 
 *
 */
public class MongoTutorialInsert {
    
    public static void main( String[] args ) throws JsonProcessingException {
    	MongoClient client = new MongoClient();
    	MongoDatabase db = client.getDatabase("MongoTutorial");
    	MongoCollection<Document> coll = db.getCollection("MongoInsert");
    	
    	coll.drop();
    	
    	Document Matty = new Document("name","Matty").append("age","44").append("profission", "programmer");
    	Document Emily = new Document("name","Emily").append("age","5").append("profission", "student");
    	
    	//Inserting one document
    	//coll.insertOne(Matty);
    	
    	//Inserting many document
    	coll.insertMany(asList(Matty, Emily));
    	
    	printJson(Matty);
    	printJson(Emily);
    }
}