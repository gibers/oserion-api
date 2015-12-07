package com.oserion.framework.api.business;

import java.util.List;
import java.util.Map;

import com.oserion.framework.api.business.beans.ContentElement;

public interface ITemplate {

	public String getName() ;
	public void setName(String name) ;
	public String getHtml() ;
	public void setHtml(String html) ;
	public List<ContentElement> getListTemplateElement() ;
	public void setListTemplateElement(List<ContentElement> listTemplateElement) ;
	public List<ContentElement> getListVariableElement() ;
	public void setListVariableElement(List<ContentElement> listVariableElement) ;

	public void afficheTemplateEle();
	public void afficheVariableTemplateEle();
	public Map<String, Object> listTemplateMap(List<ContentElement> listElement);
	
}

