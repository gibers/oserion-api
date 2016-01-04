package com.oserion.framework.api.util;

import com.mongodb.MongoClient;
import com.oserion.framework.api.OserionApiFacade;

import com.oserion.framework.api.business.IDBConnection;
import com.oserion.framework.api.business.IDataHandler;
import com.oserion.framework.api.business.ITemplate;
import com.oserion.framework.api.business.impl.jsoup.JsoupTemplate;
import com.oserion.framework.api.business.impl.mongo.MongoDBConnection;
import com.oserion.framework.api.business.impl.mongo.MongoDBDataHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@Configuration
@ComponentScan(
		basePackages={"com.oserion.framework.api.business.impl.mongo",
		"com.oserion.framework.api.business.impl.jsoup", 
		"com.oserion.framework.api.business.beans", "com.oserion.framework.api.util"})
public class OserionBuilder {

	
//	@Bean
//	public ITemplificator buildTemplificator() {
//		return new JsoupTemplificator();
//	}

	public ITemplate buildTemplate(){
		return new JsoupTemplate();
	}

	@Bean
	public IDBConnection buildDBConnection() {
		return MongoDBConnection.getInstance();
	}

	@Bean
	public IDataHandler buildDataHandler() {
		return new MongoDBDataHandler();
	}

	@Bean
	public OserionApiFacade buildApiFacade() {
		return new OserionApiFacade();
	}

}




