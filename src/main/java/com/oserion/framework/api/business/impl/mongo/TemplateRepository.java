package com.oserion.framework.api.business.impl.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.oserion.framework.api.business.impl.mongo.beans.MongoTemplate;


public interface TemplateRepository extends MongoRepository<MongoTemplate, String> {
	
	public MongoTemplate findByName(String name);
		
	
}

