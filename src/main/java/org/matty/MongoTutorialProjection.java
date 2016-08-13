package org.matty;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lt;
import static java.util.Arrays.asList;
import static org.matty.Helpers.printJson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

/**
 * Hands on MongoDB 
 *
 */
public class MongoTutorialProjection {
    
    public static void main( String[] args ) throws JsonProcessingException {
    	MongoClient client = new MongoClient();
    	MongoDatabase db = client.getDatabase("MongoTutorial");
    	MongoCollection<Document> coll = db.getCollection("MongoFindWithFilter");
    	
    	coll.drop();
    	
    	//Insert 10 documents
    	for(int i = 0; i<10; i++){
    		coll.insertOne(new Document()
    					.append("x", i)
    					.append("y", i+20));
    	}
    	
    	//Making filter using row document
//    	Bson filter = new Document("x",0)
//    					.append("y", new Document("$gt",10).append("$lt", 90));

    	// Making filter using builder for query filter, and there are static methods in the Filters class
    	// that comes with the com.mongodb.client.model
    	// eq=Equal, gt=Greater Than, lt=Less Than, these are the static methods of the Filters class.
        Bson filter = and(eq("x", 0), gt("y", 10), lt("y", 90));
    	List<Document> all = coll.find(filter).into(new ArrayList<Document>());
    	
    	for (Document cur : all){
    		printJson(cur);
    	}
    }
}