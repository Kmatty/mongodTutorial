package org.matty;

import static com.mongodb.client.model.Filters.*;
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
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;

/**
 * Hands on MongoDB 
 *
 */
public class MongoTutorial {
	
	private static MongoClient client = new MongoClient();
	//This is a nother way to create a MongoClient 
	//public static MongoClient client = new MongoClient(new ServerAddress("localhost",27017));
	private static MongoDatabase db = client.getDatabase("employees");
	private static MongoCollection<Document> coll = db.getCollection("employee");
	
	private static String str1 = "*****************************************************************************";
	private static String str2 = "******************  Next part is a list of all databases  *******************";
	private static String str3 = "*** Next part is printing only the first document in employee collection  ***";
	private static String str4 = "********  Next part is printing all document in employee collection  ********";
	private static String str5 = "**********************  Next part is about filtering  ***********************";
	private static String str6 = "*************  Next part is about projection(Include/Exclude)  **************";
	
    //Bson filter = new Document("gender","male");
    //Bson filter = new Document("age", new Document("$gt", 30));
    // eq=Equal, gt=Greater Than, lt=Less Than
    //Bson filter = new Document("gender","male").append("age", new Document("$gt",43).append("$lt", "50"));
    static Bson filter = and(eq("gender", "male"), gt("age", "43"), lt("age", "50"));
    //Bson filter = and(eq("lastName", "Matty"), gt("age", "43"));
    //Bson filter = Filters.eq("age", "44");	//or you can use the static way: -->Bson filter=eq("age","44");<--
    
    public static void main( String[] args ) throws JsonProcessingException {
    	
		//MongoClient client = new MongoClient(new ServerAddress("localhost",27017));	// or this way
		//MongoClient client = new MongoClient();	// or this way
		//DB db = client.getDB("employees");
		//MongoClient client = new MongoClient();
		//MongoDatabase db = client.getDatabase("employees");
		//MongoCollection<Document> coll = db.getCollection("employee");
		
		//List all databases
		MongoIterable<String> dbs = client.listDatabaseNames();
		printDecoration(str2);
		for(String dbset:dbs){
			System.out.println(dbset);
		}
		
		coll.drop();
		
		Document doc1 = new Document()
				.append("firstName", "Khaled")
				.append("lastName", "Matty")
        		.append("gender", "male")
        		.append("age", "44")
				.append("address", new Document()
						.append("street", "Boelijn")
						.append("houseNo", "88")
						.append("zipcode", "1319CS")	
						.append("city", "ALmere"));
		
        Document doc2 = new Document()
        		.append("firstName", "Emily")
    			.append("lastName", "Matty")
        		.append("gender", "female")
        		.append("age", "5")
    			.append("address", new Document()
						.append("street", "Boelijn")
						.append("houseNo", "88")
						.append("zipcode", "1319CS")	
						.append("city", "ALmere"));
        
        Document doc3 = new Document()
        		.append("firstName", "Anthony")
    			.append("lastName", "Matty")
        		.append("gender", "male")
        		.append("age", "3")
    			.append("address", new Document()
						.append("street", "Boelijn")
						.append("houseNo", "88")
						.append("zipcode", "1319CS")	
						.append("city", "ALmere"));
        
        //Drop collection (employee)
        coll.drop();
        
        //Insert one document
		//coll.insertOne(doc1);
        
        //Fill collection (employee) with three documents(doc1, doc2, doc3)
        coll.insertMany(asList(doc1, doc2, doc3));
        //You can use this in place of printJson method
        //ObjectMapper om = new ObjectMapper();
		//System.out.println(om.writerWithDefaultPrettyPrinter().writeValueAsString(doc1));
        
        //Print first document in the collection
        printDecoration(str3);
        Document firstDoc = coll.find().first();
        printJson(firstDoc);
        
        //Print all document in the employee collection
        printDecoration(str4);
        MongoCursor<Document> cursor = coll.find().iterator();
        try{
        	while(cursor.hasNext()){
        		Document cur = cursor.next();
        		 printJson(cur);
        	}
        }finally{
        	cursor.close();
        }
        
        //Print number of documents in the collection
        long count = coll.count();
        System.out.println("Total No. of documents are: "+count);
        
        //////////////////////////////   Filter on doc/documents   ///////////////////////////////////////
        printDecoration(str5);
        
        List<Document> all = coll.find(filter).into(new ArrayList<Document>());
        long filterCount = coll.count(filter);
        System.out.println("No. of documents that match the filter are: "+filterCount);
        //Print results
        printDocs(all);
        
        ///////////////////////////////////////   Projection   ///////////////////////////////////////////
        /* this will exclude field/s from being show in the result, for example from the filtering
           section we filtered on gender=male, so we can use projection to exclude this field from being
           shown in the result cause we already know that the results will be all document with gender=male
        */
        printDecoration(str6);
        
        /*
         * The 0 in the "new Document("gender",0)" means exclude the field from being shown in the results in this
         * case is "gender".
         * You can exclude more fields by doing: "Bson projection = new Document("gender",0).append("lastName",0);"
         * You can exclude the _id field that's been created by MongoDB engine by adding it to the exclude criteria:
         * Document("age",0).append("_id",0);
         */
        Bson projection1 = new Document("age",0);
        //Bson projection1 = new Document("age",0).append("lastName",0);
        List<Document> allDocs1 = fetchDocuments(projection1, filter);
        printDocs(allDocs1);
        
        /*
         * The 1 in the "new Document("gender",1)" means include, in other words show only this field in the results,
         * in this case is "gender"
        */
        Bson projection2 = new Document("gender",1);
        List<Document> allDocs2= fetchDocuments(projection2, filter);
        printDocs(allDocs2);
        
        /*
         * You can use the builder "Projections" to do the include/exclude in place of using row "Document".
         * for example the next will exclude lastName, _id, firstName from the result.
         */
        Bson projection3 = Projections.exclude("lastName","_id","firstName");
        List<Document> allDocs3= fetchDocuments(projection3, filter);
        printDocs(allDocs3);
    }
    
    //////////////////////////////////////  Some helper functions  /////////////////////////////////////////
    private static List<Document> fetchDocuments(Bson projection, Bson filter){
    	MongoDatabase db = new MongoClient().getDatabase("employees");
    	MongoCollection<Document> coll = db.getCollection("employee");
    	List<Document> docs = coll.find(filter).projection(projection).into(new ArrayList<Document>());
    	return docs;
    }
    
    private static void printDocs(List<Document> docs){
    	for(Document doc:docs){
        	printJson(doc);
        }
    }
    
    private static void printDecoration(String str){
    	System.out.println("\n");
        System.out.println(str1);
        System.out.println(str);
        System.out.println(str1);
    }
}
