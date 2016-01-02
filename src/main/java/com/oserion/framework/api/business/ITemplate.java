package com.oserion.framework.api.business;

import java.util.List;
import java.util.Map;

import com.oserion.framework.api.business.beans.ContentElement;

public interface ITemplate {

	String getName() ;
	void setName(String name) ;
	String getHtml() ;
	void setHtml(String html) ;
	List<ContentElement> getListTemplateElement() ;
	void setListTemplateElement(List<ContentElement> listTemplateElement) ;
	List<ContentElement> getListVariableElement() ;
	void setListVariableElement(List<ContentElement> listVariableElement) ;

	void afficheTemplateEle();
	void afficheVariableTemplateEle();
	Map<String, Object> listTemplateMap(List<ContentElement> listElement);
	void setListUrl(List<String> listUrl);
	List<String> getListUrl();
	
}

