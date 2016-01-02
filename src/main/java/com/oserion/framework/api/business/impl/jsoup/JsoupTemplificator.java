package com.oserion.framework.api.business.impl.jsoup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.oserion.framework.api.business.ITemplate;
import com.oserion.framework.api.business.ITemplificator;
import com.oserion.framework.api.business.beans.ContentElement;
import com.oserion.framework.api.business.beans.ContentElement.Type;
import com.oserion.framework.api.business.beans.PageReference;
import com.oserion.framework.api.business.impl.beansDB.Template;
import com.oserion.framework.api.util.CodeReturn;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class JsoupTemplificator implements ITemplificator {

    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
	public String texte = "texte";

	@Autowired 	
	public MongoOperations mongoOperation;

	//	@Autowired
	//	private ITemplate template = null;

	
	public List<ITemplate> selectTemplates() {
		List<Template> listTemplate = mongoOperation.findAll(Template.class);
		List<ITemplate> listITemplate = new ArrayList<ITemplate>();
		
		for(Template t1 : listTemplate ) {
			ITemplate itemp = new JsoupTemplate(t1.getName());
			
			List<PageReference> listPageReference = getPageReferenceFromTemplate(t1);
			if(listPageReference != null)
				itemp.getListPage().addAll(listPageReference);
				
			listITemplate.add(itemp);
		}
		return listITemplate;
	}

	
	private List<PageReference> getPageReferenceFromTemplate(Template t1) {
		Query q1 = new Query(Criteria.where("template").is(t1));
		List<PageReference> listPageRef = (List<PageReference>) mongoOperation.find(q1, PageReference.class);
		return listPageRef;
	}


	public ContentElement majContenu(ContentElement e) {
		ContentElement cte = new ContentElement(e.getRef(), e.getType() , e.getValue());
		//		ContentElement cte = new ContentElement(e.getRef(), ContentElement.Type.valueOf(e.getType()), e.getValue());
		return cte; 
	}

	public JsoupTemplate createTemplateFromHTML(String fluxTemplate, String templateName) {
		JsoupTemplate template = new JsoupTemplate(templateName);
		template.setHtml(fluxTemplate);
		splitContenu(fluxTemplate, template);

//		template.afficheTemplateEle();
//		template.afficheVariableTemplateEle();

		return template;
	}


	/**
	 * Rempli les champs listTemplateElement et listVariableElement de l'objet template qui est passé en paramètre. 
	 * Cet objet est un JsoupTemplate. 
	 * 
	 * @param fluxTemplate
	 * @param template
	 */
	public void splitContenu(String fluxTemplate, JsoupTemplate template ) {
		Document docJsoup = Jsoup.parse(fluxTemplate);
		Elements ele = getAllElement(docJsoup);
		System.out.println("taille ele => " + ele.size());

		Iterator<Element> it = ele.iterator();
		while(it.hasNext()) {
			Element balise = it.next();
			String type = getClassBalise(balise);
			if(balise.id().contains("ref:")) {
				ContentElement cte = new ContentElement(balise.id(), type , balise.html());
				//				ContentElement cte = new ContentElement(balise.id(), ContentElement.Type.EDITABLE.toString());
				template.getListVariableElement().add(cte);
			} else if (!balise.id().isEmpty()) {
				ContentElement cte = new ContentElement(balise.id(), type , balise.html());
				//				ContentElement cte = new ContentElement(balise.id(), ContentElement.Type.EDITABLE.toString());
				template.getListTemplateElement().add(cte);
			}
		}
	}
	
	
	/**
	 * la fct retourne la liste de toutes les balises qui contiennent au moins une des classes css
	 * contenue dans l'enumération {@link Type} . 
	 * 
	 * @param docJsoup
	 * @return
	 */
	private Elements getAllElement(Document docJsoup) {
		Elements listEle = null;
		Type[] lesTypes = ContentElement.Type.values();
		for(int i=0;i<lesTypes.length;i++) {
			if(listEle == null)
				listEle = docJsoup.select("."+lesTypes[i].name());
			else 
				listEle.addAll(docJsoup.select("."+lesTypes[i].name()));
		}
		return listEle;
	}
	

	/**
	 * La fct parcourt l'ensemble des {@link Type} 
	 * afin de le comparer avec celui de la balise passée en paramètre.
	 * Si la balise contient un des types, celui-ci est retourné sous forme de String.
	 * @param ele
	 * @return
	 */
	private String getClassBalise(Element ele) {
		Type[] lesTypes = ContentElement.Type.values();
		for(int i=0;i<lesTypes.length;i++) {
			if(ele.hasClass(lesTypes[i].name())) 
				return lesTypes[i].name();
		}
		return null;
	}


	public String construireFlux(Template t1, int key) {
		List<ContentElement> listTemplateElement = t1.getListTemplateElement();
		List<ContentElement> listVariableElement = t1.getListVariableElement();
		String htmlVariabiliser = t1.getHtml().replaceAll("ref:page", String.valueOf(key));
		Document docJsoup = Jsoup.parse(htmlVariabiliser);
		Elements ele = docJsoup.select(".editable");
		Iterator<Element> it = ele.iterator();
		while(it.hasNext()) {
			Element balise = it.next();
			String value = getContentFromRef(listVariableElement, balise.id(), Type.EDITABLE.name());
			if(value != null) {
				docJsoup.getElementById(balise.id()).html(value);
			} else {
				value = getContentFromRef(listTemplateElement, balise.id(), Type.EDITABLE.name());
				if(value!=null)
					docJsoup.getElementById(balise.id()).html(value);
				else 					
					return CodeReturn.error25(t1.getName(), balise.id() );
			}
		}
		
		return docJsoup.toString();
	}

	private String getContentFromRef(List<ContentElement> listElement, String ref, String type) {
		for(ContentElement cte : listElement) {
			if(cte.getType().equalsIgnoreCase(type) && cte.getRef().equalsIgnoreCase(ref)) {
				System.out.println("ref => " + ref + " , cte.getValue() => " + cte.getValue() );
				return cte.getValue();
			}
		}
		return null;
	}


	//	private String getSquelette(String fluxTemplate) {
	//		return "squeltte";
	//	}

}


