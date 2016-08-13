package org.matty;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
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
public class MongoTutorialDelete {
    
    public static void main( String[] args ) throws JsonProcessingException {
    	MongoClient client = new MongoClient();
    	MongoDatabase db = client.getDatabase("MongoTutorial");
    	MongoCollection<Document> coll = db.getCollection("MongoDelete");
    	
    	coll.drop();
    	
    	//insert 8 documents, with both _id and x set to the value of the loop variable
    	for (int i = 0; i<8; i++){
    		coll.insertOne(new Document().append("_id", i));
    	}
    	
    	//Delete the document with _id=5
    	coll.deleteOne(eq("_id",5));
    	//Or you can use greater than to do the job
    	//coll.deleteOne(gt("_id",4));
    	
    	//Delete Many documents. delete all that have greater _id than 4
    	coll.deleteMany(gt("_id",4));
    	
    	//Printing documents
    	for (Document cur : coll.find().into(new ArrayList<Document>())){
    		printJson(cur);
    	}
    	
    }
}