package com.oserion.framework.api.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.oserion.framework.api.interfaces.IDataHandler;
import com.oserion.framework.api.interfaces.ITemplate;
import com.oserion.framework.api.interfaces.ITemplificator;


//@Configuration
//@ComponentScan(basePackages={"com.oserion.framework.oserion.api.classes", "com.oserion.framework.oserion.api.interfaces"})
public class Api418Facade {
	
	@Autowired
	public ITemplificator ijst = null;
	
	@Autowired
	public IDataHandler idh = null;
	
	
	public String uploadTemplateFromHtml( String fluxTemplate, String templateName ) {
		
		ITemplate template1 = ijst.createTemplateFromHTML(fluxTemplate, templateName);
		
		return templateName;
//		tc.createTemplateFromHTML(fluxTemplate, templateName );
	}

//	public void aficheDonne() {
//		System.out.println(ijst.createTemplateFromHTML("sting1", "string2"));
//		System.out.println(idh.insertOrUpdateTemplate("template1"));
//		
//	}
	
}
