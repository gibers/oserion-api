package com.oserion.framework.api.util;

import com.mongodb.MongoClient;
import com.oserion.framework.api.OserionApiFacade;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@Configuration
@ComponentScan(basePackages={"com.oserion.framework.api.business.impl.mongo", 
		"com.oserion.framework.api.business.impl.jsoup", 
		"com.oserion.framework.api.business.beans", "com.oserion.framework.api.util"})
public class OserionBuilder {

	private static final String PROPERTY_DB_HOST = "database.host";
	private static final String PROPERTY_DB_PORT = "database.port";
	private static final String PROPERTY_DB_SCHEMA = "database.schema";

	
//	@Bean
//	public ITemplificator buildTemplificator() {
//		return new JsoupTemplificator();
//	}

//	@Bean
//	public IDBConnection buildDBConnection() {
//		return MongoDBConnection.getInstance();
//	}

//	@Bean
//	public IDataHandler buildDataHandler() {
//		return new MongoDBDataHandler();
//	}

	@Bean
	public OserionApiFacade buildApi418Facade() {
		return new OserionApiFacade();
	}

//	@Bean
//	public MongoDatabase theConnectionMongo() {
//		MongoClient mongoClient = new MongoClient(System.getProperty(PROPERTY_DB_HOST),
//				Integer.parseInt(System.getProperty(PROPERTY_DB_PORT)));
//		MongoDatabase database = mongoClient.getDatabase(System.getProperty(PROPERTY_DB_SCHEMA));
//		return database;
//	}
	
	
	// -----CONFIG MONGO-------
	
	@Bean
	public MongoDbFactory mongoDbFactory() throws Exception {
		return new SimpleMongoDbFactory(new MongoClient(System.getProperty(PROPERTY_DB_HOST), 
				Integer.parseInt(System.getProperty(PROPERTY_DB_PORT))), 
				System.getProperty(PROPERTY_DB_SCHEMA));
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate( mongoDbFactory() );
		return mongoTemplate;
	}
	
	

}




