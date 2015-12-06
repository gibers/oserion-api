package com.oserion.framework.oserion.api.classes;

import java.util.List;

import org.springframework.stereotype.Component;

import com.oserion.framework.oserion.api.interfaces.ITemplate;

//@Component
public class JsoupTemplate implements ITemplate {
	
	private String name = null;
	private String html = null;
	
	private List<ContentElement> listTemplateElement  = null;
	private List<ContentElement> listVariableElement  = null;
//	private List<ContentElement> listPage   = null;
	
	
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
	
	
	public JsoupTemplate(String name) {
		this.name = name;
	}
	public JsoupTemplate() {
	}
	
	public void affiche () {
		System.out.println("template name => " + name );
	}
	
}
