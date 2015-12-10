	package com.oserion.framework.api.business.impl.jsoup;

import java.util.Iterator;

import com.oserion.framework.api.business.ITemplate;
import com.oserion.framework.api.business.ITemplificator;
import com.oserion.framework.api.business.beans.ContentElement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class JsoupTemplificator implements ITemplificator {

	public String texte = "texte";
	
//	@Autowired
//	private ITemplate template = null;
	
	
	public ContentElement majContenu(ContentElement e) {
		ContentElement cte = new ContentElement(e.getRef(), ContentElement.Type.valueOf(e.getType()), e.getValue());
		return cte; 
	}
	
	public ITemplate createTemplateFromHTML(String fluxTemplate, String templateName) {
		ITemplate template = new JsoupTemplate(templateName);
		template.setHtml(fluxTemplate);
		splitContenu(fluxTemplate, template);
		
		template.afficheTemplateEle();
		template.afficheVariableTemplateEle();
		
		return template;
	}

	
	/**
	 * Rempli les champs listTemplateElement et listVariableElement de l'objet template qui est passé en paramètre. 
	 * Cet objet est un JsoupTemplate. 
	 * 
	 * @param fluxTemplate
	 * @param template
	 */
	private void splitContenu(String fluxTemplate, ITemplate template ) {
		Document docJsoup = Jsoup.parse(fluxTemplate);
		Elements ele = docJsoup.select(".editable");
		System.out.println("taille ele => " + ele.size());
		
		Iterator<Element> it = ele.iterator();
		while(it.hasNext()) {
			Element balise = it.next();
			if(balise.id().contains("ref:")) {
				ContentElement cte = new ContentElement(balise.id(), ContentElement.Type.EDITABLE , balise.html());
				template.getListVariableElement().add(cte);
			} else if (!balise.id().isEmpty()) {
				ContentElement cte = new ContentElement(balise.id(), ContentElement.Type.EDITABLE , balise.html());
				template.getListTemplateElement().add(cte);
			}
		}
	}
	
	
	
//	private String getSquelette(String fluxTemplate) {
//		return "squeltte";
//	}

}


