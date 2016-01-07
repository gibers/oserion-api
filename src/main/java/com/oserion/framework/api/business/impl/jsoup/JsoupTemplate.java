package com.oserion.framework.api.business.impl.jsoup;

import java.util.ArrayList;
import java.util.List;

import com.oserion.framework.api.business.ITemplate;
import com.oserion.framework.api.business.beans.ContentElement;
import com.oserion.framework.api.business.beans.PageReference;


public class JsoupTemplate implements ITemplate {
	
	private String name = null;
	private String html = null;
	
	private List<ContentElement> listTemplateElement  = new ArrayList<>();
	private List<ContentElement> listVariableElement  = new ArrayList<>();
	private List<PageReference> listPage   = new ArrayList<>();
	
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
	
	/**
	 * Les constructeurs
	 * @param name
	 */
	public JsoupTemplate(String name) {
		this.name = name;
	}
	public JsoupTemplate() {
	}



}



