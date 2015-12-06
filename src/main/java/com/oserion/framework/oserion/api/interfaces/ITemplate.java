package com.oserion.framework.oserion.api.interfaces;

import java.util.List;

import com.oserion.framework.oserion.api.classes.ContentElement;

public interface ITemplate {

	public String getName() ;
	public void setName(String name) ;
	public String getHtml() ;
	public void setHtml(String html) ;
	public List<ContentElement> getListTemplateElement() ;
	public void setListTemplateElement(List<ContentElement> listTemplateElement) ;
	public List<ContentElement> getListVariableElement() ;
	public void setListVariableElement(List<ContentElement> listVariableElement) ;

	public void affiche();
	
}
