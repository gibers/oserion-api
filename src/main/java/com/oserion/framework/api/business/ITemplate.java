package com.oserion.framework.api.business;

import java.util.List;
import java.util.Map;

import com.oserion.framework.api.business.beans.ContentElement;
import com.oserion.framework.api.business.beans.PageReference;

public interface ITemplate {

	public String getName() ;
	public void setName(String name) ;
	public String getHtml() ;
	public void setHtml(String html) ;
//	public List<ContentElement> getListTemplateElement() ;
//	public void setListTemplateElement(List<ContentElement> listTemplateElement) ;
//	public List<ContentElement> getListVariableElement() ;
//	public void setListVariableElement(List<ContentElement> listVariableElement) ;
//
	
	List<PageReference> getListPage();
	void setListPage(List<PageReference> listPage);
		
//	public void afficheTemplateEle();
//	public void afficheVariableTemplateEle();
//	public Map<String, Object> listTemplateMap(List<ContentElement> listElement);
	
	
}

