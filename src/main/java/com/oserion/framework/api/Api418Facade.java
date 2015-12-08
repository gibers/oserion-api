package com.oserion.framework.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.oserion.framework.api.business.IDataHandler;
import com.oserion.framework.api.business.ITemplate;
import com.oserion.framework.api.business.ITemplificator;
import com.oserion.framework.api.business.beans.ContentElement;
import com.oserion.framework.api.business.impl.jsoup.JsoupTemplificator;
import com.oserion.framework.api.business.impl.mongo.MongoDBDataHandler;


//@ComponentScan(basePackages={"com.oserion.framework.api.business","com.oserion.framework.api.business.impl.mongo", "com.oserion.framework.api.business.impl.jsoup"})
public class Api418Facade {
	
	@Autowired
	public ITemplificator ijst = null;
	
	@Autowired
	public IDataHandler idh = null;
	
	
	public String uploadTemplateFromHtml( String fluxTemplate, String templateName ) {
		ITemplate template1 = ijst.createTemplateFromHTML(fluxTemplate, templateName);
		idh.insertOrUpdateTemplate(template1);
		idh.displayContentBase();
		return templateName;
	}

	
	public String getHTMLPage(String nameTemplate) {
		return idh.selectHTMLTemplate(nameTemplate);
	}

	
	public void setContent(ContentElement e) {
		ContentElement cte = ijst.majContenu(e);
		idh.insertOrUpdateContent(cte);
	}

	//String getHTMLPage(String URL);
	
//	public void aficheDonne() {
//		System.out.println(ijst.createTemplateFromHTML("sting1", "string2"));
//		System.out.println(idh.insertOrUpdateTemplate("template1"));
//		
//	}
	
}
