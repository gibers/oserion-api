package com.oserion.framework.api.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.oserion.framework.api.business.beans.ContentElement;
import com.oserion.framework.api.business.impl.beansDB.Template;

@Component
public class Supervision {
	
	@Autowired 	
	public MongoOperations mongoOperation;

	
	public String listContentElementFromTemplateName(String templateName) {
		Query q1 = new Query(Criteria.where("name").is(templateName));
		Template t1 = (Template) mongoOperation.findOne(q1, Template.class);
		if(t1 == null) {
			return CodeReturn.error22;
		}

		System.out.println("Pour le template " + templateName + " : ");
		System.out.println("ListTemplateElement : ");
		for(ContentElement ct1 : t1.getListTemplateElement()) {
			System.out.println(" ref : " + ct1.getRef() );
			System.out.println(" type : " + ct1.getType() );
			System.out.println(" value : " + ct1.getValue() );
			System.out.println(" -------------- ");
		}
		System.out.println("ListVariableElement : ");
		System.out.println(" ******************* ");
		for(ContentElement ct1 : t1.getListVariableElement()) {
			System.out.println(" ref : " + ct1.getRef() );
			System.out.println(" type : " + ct1.getType() );
			System.out.println(" value : " + ct1.getValue() );
			System.out.println(" -------------- ");
		}
		
		return null;
	}
	

}
