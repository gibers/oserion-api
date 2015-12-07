package com.oserion.framework.api.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.oserion.framework.api.interfaces.IDataHandler;
import com.oserion.framework.api.interfaces.ITemplate;
import com.oserion.framework.api.interfaces.ITemplificator;
import com.oserion.framework.api.mongo.ContentElement;
import com.oserion.framework.api.mongo.ContentElement.Type;


//@Configuration
//@ComponentScan(basePackages={"com.oserion.framework.oserion.api.classes", "com.oserion.framework.oserion.api.interfaces"})
public class Api418Facade {
	
	@Autowired
	public ITemplificator ijst = null;
	
	@Autowired
	public IDataHandler idh = null;
	
	
	public String uploadTemplateFromHtml( String fluxTemplate, String templateName ) {
		ITemplate template1 = ijst.createTemplateFromHTML(fluxTemplate, templateName);
		idh.insertOrUpdateTemplate(template1);
		idh.afficheContenuBase();
		return templateName;
	}

	
	public String getHTMLPage(String nameTemplate) {
		return idh.getTemplate(nameTemplate);
	}

	
	public void insertOrUpdateContenue(String ref, String type, String contenue ) {
		ContentElement cte = ijst.majContenu(ref, type, contenue );
		idh.insertOrUpdateContenue(cte);
	}
	
	
//	public void aficheDonne() {
//		System.out.println(ijst.createTemplateFromHTML("sting1", "string2"));
//		System.out.println(idh.insertOrUpdateTemplate("template1"));
//		
//	}
	
}
