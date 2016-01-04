package com.oserion.framework.api.business;

import java.util.List;

import com.oserion.framework.api.business.beans.ContentElement;
import com.oserion.framework.api.business.impl.mongo.beans.MongoTemplate;
import com.oserion.framework.api.business.impl.jsoup.JsoupTemplate;

//@Component
public interface ITemplificator {

	JsoupTemplate createTemplateFromHTML(String fluxTemplate, String templateName);
	ContentElement majContenu(ContentElement e);
	String construireFlux(MongoTemplate t1, int key);
	
	List<ITemplate> selectTemplates();
	
}
