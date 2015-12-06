package com.oserion.framework.oserion.api.classes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oserion.framework.oserion.api.interfaces.ITemplate;
import com.oserion.framework.oserion.api.interfaces.ITemplificator;

//@Component
public class JsoupTemplificator implements ITemplificator {

	public String texte = "texte";
	
//	@Autowired
//	private ITemplate template = null;
	
	public ITemplate createTemplateFromHTML(String fluxTemplate, String templateName) {

		ITemplate template = new JsoupTemplate(templateName);
		template.setHtml(getSquelette(fluxTemplate));
		
		
		template.affiche();
		
		return template;
	}
	
	private String getSquelette(String fluxTemplate) {
		return "squeltte";
	}

}


