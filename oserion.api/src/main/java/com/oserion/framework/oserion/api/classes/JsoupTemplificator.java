package com.oserion.framework.oserion.api.classes;

import org.springframework.stereotype.Component;

import com.oserion.framework.oserion.api.interfaces.ITemplificator;

@Component
public class JsoupTemplificator implements ITemplificator {

	String texte = "texte";
	
	public String createTemplateFromHTML(String fluxTemplate, String templateName) {
		// TODO utiliser JsoupTemplate.
		return "return-template";
	}

}
