package com.oserion.framework.api;

import com.oserion.framework.api.business.IDBConnection;
import com.oserion.framework.api.util.OserionBuilder;

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


public class Api418Facade {
	
//	@Autowired
	private ITemplificator ijst;
	private IDataHandler idh;
	private OserionBuilder builder;

	public Api418Facade(IDBConnection c){
		this.builder = new OserionBuilder();
		this.idh = this.builder.buildDataHandler(c);
		this.ijst = new JsoupTemplificator();
	}

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

	public boolean addPageUrl(String templateName, String url){
		return idh.insertPageURL(templateName,url);
	}

	
}
