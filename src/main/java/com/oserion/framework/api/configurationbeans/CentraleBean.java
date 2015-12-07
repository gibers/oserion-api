package com.oserion.framework.api.configurationbeans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.oserion.framework.api.implementation.Api418Facade;
import com.oserion.framework.api.interfaces.IDataHandler;
import com.oserion.framework.api.interfaces.ITemplificator;
import com.oserion.framework.api.jsoup.JsoupTemplificator;
import com.oserion.framework.api.mongo.MongoDBDataHandler;

@Configuration
@ComponentScan(basePackages={ "com.oserion.framework.api.interfaces"})
public class CentraleBean {
	
	@Bean
	public ITemplificator Iitemplificator() {
		return new JsoupTemplificator();
	}
	
	@Bean
	public IDataHandler idataHandler() {
		return new MongoDBDataHandler("localhost", 27017, "oserionBD");
	}

	@Bean
	public Api418Facade api418Facade() {
		return new Api418Facade();
	}
	
	
	/**
	 * les beans de la bdd 
	 * @return
	 */
//	@Bean
//	public MongoClient getConn() {
//		return new MongoClient( "localhost" , 27017 );
//	}
	
	
}



//MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
//MongoDatabase database = mongoClient.getDatabase("oserionBD");


