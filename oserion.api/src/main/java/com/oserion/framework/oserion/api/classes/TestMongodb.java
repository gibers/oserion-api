package com.oserion.framework.oserion.api.classes;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lte;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class TestMongodb {

	public TestMongodb() {
    	MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
    	MongoDatabase database = mongoClient.getDatabase("test");
    	MongoCollection<Document> collection = database.getCollection("collectionTest1");

    	Document doc = new Document("name", "MongoDB")
        .append("type", "database")
        .append("count", 1)
        .append("info", new Document("x", 203).append("y", 102));

//    	collection.insertOne(doc);

    	List<Document> documents = new ArrayList<Document>();
    	for (int i = 0; i < 100; i++) {
    	    documents.add(new Document("i", i));
    	}
//    	collection.insertMany(documents);
    	System.out.println(collection.count());

    	Document myDoc = collection.find().first();
    	System.out.println(myDoc.toJson());
    	
    	MongoCursor<Document> cursor = collection.find().iterator();
    	try {
    	    while (cursor.hasNext()) {
    	        System.out.println(cursor.next().toJson());
    	    }
    	} finally {
    	    cursor.close();
    	}
    	
    	myDoc = collection.find(eq("i", 71)).first();
    	System.out.println(myDoc.toJson());

    	Block<Document> printBlock = new Block<Document>() {
    	     public void apply(final Document document) {
    	         System.out.println( document.get("name") );
    	     }
    	};
    	collection.find(and(gt("i", 50), lte("i", 59))).forEach(printBlock);
    	
    	
    	mongoClient.close();		
	}
	
}
