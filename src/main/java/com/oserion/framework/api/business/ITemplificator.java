package com.oserion.framework.api.business;


import org.springframework.stereotype.Component;

@Component
public interface ITemplificator {

	ITemplate createTemplateFromHTML(String name, String html);
	/*ContentElement majContenu(ContentElement e);
	String construireFlux(MongoTemplate t1, int key);
	
	List<ITemplate> selectTemplates();*/
	
}
