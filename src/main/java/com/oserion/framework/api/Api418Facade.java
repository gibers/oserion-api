package com.oserion.framework.api;

import org.springframework.beans.factory.annotation.Autowired;

import com.oserion.framework.api.business.IDataHandler;
import com.oserion.framework.api.business.ITemplate;
import com.oserion.framework.api.business.ITemplificator;
import com.oserion.framework.api.business.impl.bean.ContentElement;


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
		idh.displayContentBase();
		return templateName;
	}

	
	public String getHTMLPage(String nameTemplate) {
		return idh.selectHTMLTemplate(nameTemplate);
	}

	
	public void insertOrUpdateContenue(String ref, String type, String contenue ) {
		ContentElement cte = ijst.majContenu(ref, type, contenue );
		idh.insertOrUpdateContent(cte);
	}

	//String getHTMLPage(String URL);
	
//	public void aficheDonne() {
//		System.out.println(ijst.createTemplateFromHTML("sting1", "string2"));
//		System.out.println(idh.insertOrUpdateTemplate("template1"));
//		
//	}
	
}
