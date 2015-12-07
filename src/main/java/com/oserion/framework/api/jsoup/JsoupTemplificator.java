package com.oserion.framework.api.jsoup;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oserion.framework.api.interfaces.ITemplate;
import com.oserion.framework.api.interfaces.ITemplificator;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//@Component
public class JsoupTemplificator implements ITemplificator {

	public String texte = "texte";
	
//	@Autowired
//	private ITemplate template = null;
	
	public ITemplate createTemplateFromHTML(String fluxTemplate, String templateName) {

		ITemplate template = new JsoupTemplate(templateName);
		template.setHtml(fluxTemplate);
		splitContenu(fluxTemplate);
		
		template.affiche();
		
		return template;
	}
	
	
	private void splitContenu(String fluxTemplate) {
		Document docJsoup = Jsoup.parse(fluxTemplate);
		
		Elements ele = docJsoup.select(".editable");
				
//		Elements ele = docJsoup.getElementsByClass("editable");
		
		System.out.println("taille ele => " + ele.size());
		
		Iterator<Element> it = ele.iterator();
		while(it.hasNext()) {
			System.out.println(it.next().toString());
		}
	}
	
	
	
//	private String getSquelette(String fluxTemplate) {
//		return "squeltte";
//	}

}


