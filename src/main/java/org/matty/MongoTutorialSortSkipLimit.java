package org.matty;

import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Sorts.orderBy;
import static java.util.Arrays.asList;
import static org.matty.Helpers.printJson;

import java.util.ArrayList;
import java.util.List;

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
public class MongoTutorialSortSkipLimit {
    
    public static void main( String[] args ) throws JsonProcessingException {
    	MongoClient client = new MongoClient();
    	MongoDatabase db = client.getDatabase("MongoTutorial");
    	MongoCollection<Document> coll = db.getCollection("MongoFind");
    	
    	coll.drop();
    	
    	//Insert 10 documents
    	for(int i = 0; i<10; i++){
    		for(int j=0; j<10; j++){
    			coll.insertOne(new Document().append("i", i).append("j", j));
    		}
    	}
    	
        //1 means ascending and -1 descending, we are not using filter in the find() method, so it'll show all documents.
        //Bson sort = new Document("i",1);
        //Or by using the static way
        //Bson sort = ascending("i");
        //Bson sort = descending("i","j");
        
        //To combine ascending and descending we must use the "orderBy"
        //skip is used to skip the first x docs(in this case x=1), limit is to limit result to x docs(in this case x=2)
        Bson sort = orderBy(ascending("i"),descending("j"));
        
        Bson projection = Projections.fields(Projections.include("i","j"),
				Projections.exclude("_id"));
        
        List<Document> all = coll.find()
        							.projection(projection)
        							.sort(sort)
        							.skip(1)
        							.limit(2)
        							.into(new ArrayList<Document>());
        for(Document doc:all){
        	printJson(doc);
        }
    }
}