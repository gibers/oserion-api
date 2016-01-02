package com.oserion.framework.api.business.impl.jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oserion.framework.api.business.ITemplate;
import com.oserion.framework.api.business.beans.ContentElement;
import com.oserion.framework.api.business.beans.PageReference;

import org.springframework.stereotype.Component;

//@Component
public class JsoupTemplate implements ITemplate {
	
	private String name = null;
	private String html = null;
	
	private List<ContentElement> listTemplateElement  = new ArrayList<ContentElement>();
	private List<ContentElement> listVariableElement  = new ArrayList<ContentElement>();
	private List<PageReference> listPage   = new ArrayList<PageReference>();;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public List<ContentElement> getListTemplateElement() {
		return listTemplateElement;
	}
	public void setListTemplateElement(List<ContentElement> listTemplateElement) {
		this.listTemplateElement = listTemplateElement;
	}
	public List<ContentElement> getListVariableElement() {
		return listVariableElement;
	}
	public void setListVariableElement(List<ContentElement> listVariableElement) {
		this.listVariableElement = listVariableElement;
	}
	public List<PageReference> getListPage() {
		return listPage;
	}
	public void setListPage(List<PageReference> listPage) {
		this.listPage = listPage;
	}
	
	
//	/**
//	 * Converti un list<ContentElement> en Map, afin de l'insérer en bdd.
//	 * 
//	 */
//	public Map<String, Object> listTemplateMap(List<ContentElement> listElement) {
//		Map<String, Object> listTypeEtRef = new HashMap<String, Object>();
//		for(ContentElement cte : listElement) {
//			listTypeEtRef.put(cte.getType() , cte.getRef());
//		}
//		return listTypeEtRef;
//	}
	
//	public Map<String, Object> listTemplateMapWithContenu(List<ContentElement> listElement) {
//		Map<String, Object> listTypeRefContenu = new HashMap<String, Object>();
//		for(ContentElement cte : listElement) {
//			listTypeRefContenu.put("ref" , cte.getRef());
//			listTypeRefContenu.put("type" , cte.getType());
//			listTypeRefContenu.put("value" , cte.getValue());
//		}
//		return listTypeRefContenu;
//	}

	/**
	 * Les constructeurs
	 * @param name
	 */
	public JsoupTemplate(String name) {
		this.name = name;
	}
	public JsoupTemplate() {
	}

	/* DEBUG */
	public void afficheTemplateEle () {
		for(ContentElement cte : listTemplateElement)
			cte.affiche();
	}
	public void afficheVariableTemplateEle () {
		for(ContentElement cte : listVariableElement)
			cte.affiche();
	}


}



