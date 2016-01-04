package com.oserion.framework.api.business.beans;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface ContentElementRepository extends MongoRepository<ContentElement, String> {
	
	public ContentElement findByRef(String name);
		
	
}

