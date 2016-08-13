package org.matty;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lt;
import static java.util.Arrays.asList;
import static org.matty.Helpers.printJson;
import static com.mongodb.client.model.Projections.*;

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
import com.mongodb.client.model.Projections;

/**
 * Hands on MongoDB 
 *
 */
public class MongoTutorialFindWithFilter {
    
    public static void main( String[] args ) throws JsonProcessingException {
    	MongoClient client = new MongoClient();
    	MongoDatabase db = client.getDatabase("MongoTutorial");
    	MongoCollection<Document> coll = db.getCollection("MongoFindWithFilter");
    	
    	coll.drop();
    	
    	//Insert 10 documents
    	for(int i = 0; i<10; i++){
    		coll.insertOne(new Document()
    					.append("x", new Random().nextInt(2))
    					.append("y", new Random().nextInt(100))
    					.append("i", i));
    	}

        Bson filter = and(eq("x", 0), gt("y", 10), lt("y", 90));
        
        //Excluding x from the result. 0=Excluding
        //Bson projection = new Document("x",0);
        
        //Including y and i only in the result. 1=Including
/*        Bson projection = new Document("y",1).append("i", 1).append("_id", 0);
        List<Document> all = coll.find(filter)
        					.projection(projection)
        					.into(new ArrayList<Document>());
*/
        
        /*
         * You can use both exclude and include in the same projection. for example the next projection will
         * Exclude "_id" from result and include "y" and "i" in the result. combination of Include and Exclude
         * can be done using fields method of the Projections class 
         */
        Bson projection = Projections.fields(Projections.include("y","i"),
        									Projections.exclude("_id"));
        //The same as the above projection, by importing the static methods. It's shorter n better to read.
        // Bson projection = fields(include("y","i"),excludeId());
        
        List<Document> all = coll.find(filter)
				.projection(projection)
				.into(new ArrayList<Document>());
        
    	for (Document cur : all){
    		printJson(cur);
    	}
    }
}