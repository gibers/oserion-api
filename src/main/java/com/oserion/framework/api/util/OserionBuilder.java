package com.oserion.framework.api.util;

import com.oserion.framework.api.business.IDBConnection;
import com.oserion.framework.api.business.impl.mongo.MongoDBConnection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.oserion.framework.api.business.IDataHandler;
import com.oserion.framework.api.business.ITemplificator;
import com.oserion.framework.api.business.impl.jsoup.JsoupTemplificator;
import com.oserion.framework.api.business.impl.mongo.MongoDBDataHandler;

@Configuration
//@EnableMongoRepositories
public class OserionBuilder {
	
	@Bean
	public ITemplificator buildTemplificator() {
		return new JsoupTemplificator();
	}

	@Bean
	public IDBConnection buildDBConnection() {
		return MongoDBConnection.getInstance();
	}

	@Bean
	public IDataHandler buildDataHandler() {
		return new MongoDBDataHandler();
	}

}




