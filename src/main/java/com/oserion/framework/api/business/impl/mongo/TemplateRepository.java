package com.oserion.framework.api.business.impl.mongo;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import com.oserion.framework.api.business.beans.ContentElement;
import com.oserion.framework.api.business.impl.beansDB.Template;


public interface TemplateRepository extends MongoRepository<Template, String> {
	
	public Template findByName(String name);
		
	
}

