package org.matty;

import static java.util.Arrays.asList;
import static org.matty.Helpers.printJson;

import org.bson.Document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

/**
 * Hands on MongoDB 
 *
 */
public class MongoTutorialFind {
    
    public static void main( String[] args ) throws JsonProcessingException {
    	MongoClient client = new MongoClient();
    	MongoDatabase db = client.getDatabase("MongoTutorial");
    	MongoCollection<Document> coll = db.getCollection("MongoFind");
    	
    	coll.drop();
    	
    	//Insert 10 documents
    	for(int i = 0; i<10; i++){
    		coll.insertOne(new Document("X", i));
    	}
    	
    	//Find one document
    	Document doc = coll.find().first();
    	printJson(doc);
    	
    	//Find all documents with iteration
        MongoCursor<Document> cursor = coll.find().iterator();
        try{
        	while(cursor.hasNext()){
        		Document cur = cursor.next();
        		 printJson(cur);
        	}
        }finally{
        	cursor.close();
        }
        
        //document count
        long count = coll.count();
        System.out.println("No. of document in the collection are: " + count);
    	
    }
}