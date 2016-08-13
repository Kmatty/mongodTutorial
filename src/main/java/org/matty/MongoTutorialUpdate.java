package org.matty;

import static com.mongodb.client.model.Filters.gte;
import static org.matty.Helpers.printJson;

import java.util.ArrayList;

import org.bson.Document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Hands on MongoDB 
 *
 */
public class MongoTutorialUpdate {
    
    public static void main( String[] args ) throws JsonProcessingException {
    	MongoClient client = new MongoClient();
    	MongoDatabase db = client.getDatabase("MongoTutorial");
    	MongoCollection<Document> coll = db.getCollection("MongoUpdate");
    	
    	coll.drop();
    	
    	//insert 8 documents, with both _id and x set to the value of the loop variable
    	for (int i = 0; i<8; i++){
    		coll.insertOne(new Document().append("_id", i).append("x", i));
    	}
    	
    	//Update one document
    	//coll.replaceOne(eq("x", 5),new Document("_id",5).append("x", 20).append("update", true));
    	//coll.updateOne(eq("x",5), new Document("$set", new Document("x",20)));
    	
    	//Update many document, increment all x's by 1 from the document with _id=5 
    	coll.updateMany(gte("_id",5), new Document("$inc", new Document("x",1)));
    	
    	//Printing documents
    	for (Document cur : coll.find().into(new ArrayList<Document>())){
    		printJson(cur);
    	}
    	
    }
}