package com.oserion.framework.api.business.beans;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.oserion.framework.api.business.impl.beansDB.Template;



public interface ContentElementRepository extends MongoRepository<ContentElement, String> {
	
	public ContentElement findByRef(String name);
		
	
}

