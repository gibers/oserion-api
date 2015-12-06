package com.oserion.framework.oserion.api.classes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.oserion.framework.oserion.api.interfaces.IDataHandler;
import com.oserion.framework.oserion.api.interfaces.ITemplificator;


@Configuration
@ComponentScan(basePackages={"com.oserion.framework.oserion.api.classes", "com.oserion.framework.oserion.api.interfaces"})
public class Api418Facade {
	
	@Autowired
	public ITemplificator ijst = null;
	@Autowired
	public IDataHandler idh = null;
	
	
	public ITemplificator getIjst() {
		return ijst;
	}
	public void setIjst(JsoupTemplificator ijst) {
		this.ijst = ijst;
	}


	public IDataHandler getIdh() {
		return idh;
	}
	public void setIdh(IDataHandler idh) {
		this.idh = idh;
	}


	public Api418Facade(/* JsoupTemplificator jst, MongoDBDataHandler mongodh */) {
//		ijst = jst;
//		idh = mongodh;
	}
	
	
	public String uploadTemplateFromHtml( /* String fluxTemplate, */ String templateName ) {
		return templateName;
//		tc.createTemplateFromHTML(fluxTemplate, templateName );
		
	}


	public void aficheDonne() {
		System.out.println(ijst.createTemplateFromHTML("sting1", "string2"));
		System.out.println(idh.insertOrUpdateTemplate("template1"));
		
	}
	
}
