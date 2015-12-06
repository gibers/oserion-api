package com.oserion.framework.oserion.api.classes;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static com.mongodb.client.model.Filters.*;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

/**
 * Hello world!
 *
 */
public class App  {
    public static void main( String[] args ) {
    	
//    	ApplicationContext  context = 
//    			new ClassPathXmlApplicationContext("oserion-spring.xml");
    	
    	Api418Facade a418f = new Api418Facade();
    	a418f.aficheDonne();
//    	a418f.uploadTemplateFromHtml("monFlux");

    }
}


