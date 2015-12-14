package com.oserion.framework.api.util;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.oserion.framework.api.Api418Facade;
import com.oserion.framework.api.business.IDBConnection;
import com.oserion.framework.api.business.impl.mongo.MongoDBConnection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.oserion.framework.api.business.IDataHandler;
import com.oserion.framework.api.business.ITemplificator;
import com.oserion.framework.api.business.impl.jsoup.JsoupTemplificator;
import com.oserion.framework.api.business.impl.mongo.MongoDBDataHandler;

@EnableMongoRepositories
@Configuration
@ComponentScan(basePackages={"com.oserion.framework.api.business.impl.mongo", "com.oserion.framework.api.business.impl.jsoup"})
public class OserionBuilder {
	
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
	public Api418Facade buildApi418Facade() {
		return new Api418Facade();
	}
	
	
	// -----CONFIG MONGO-------
	
	@Bean
	public MongoDbFactory mongoDbFactory() throws Exception {
		return new SimpleMongoDbFactory(new MongoClient(), "oserionDB");
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate( mongoDbFactory() );
		return mongoTemplate;
	}
	
	

}




