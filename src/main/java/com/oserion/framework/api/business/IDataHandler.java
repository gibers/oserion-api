package com.oserion.framework.api.business;

import java.util.List;

import org.springframework.stereotype.Component;

import com.oserion.framework.api.business.beans.ContentElement;
import com.oserion.framework.api.business.beans.ContentElement.Type;
import com.oserion.framework.api.business.beans.PageReference;
import com.oserion.framework.api.business.impl.beansDB.Template;

//@Component
public interface IDataHandler {

	boolean isTemplateInDB(String templateName);
	Template getTemplateFromDB(String templateName);
	PageReference getPageReference(String url);

	void insertListTemplateElementToTemplate(Template t1, List<ContentElement> listTemplateElement);

	void insertListVariableElementToTemplate(Template t1, List<ContentElement> listTemplateElement);

	Template createTemplate(String templateName, String fluxTemplate,
			List<ContentElement> listTemplateElement,
			List<ContentElement> listVariableElement);
	
	PageReference createPageReference(String newUrl, Template t1);
	void removePageReference(String url);
	ContentElement modifyValueOfContentElement(List<ContentElement> listComplete,
			String ref, String type, String newValue);
	int removeTemplate(Template t1);
	
	
//	boolean insertOrUpdateTemplate(ITemplate template);
//	boolean insertOrUpdateManyContent(List<ContentElement> listElement);
//	boolean insertOrUpdateContent(ContentElement ele);
//	boolean insertPageURL(String templateName, String URL);
//
//	boolean deletePageURL(String URL);
//	boolean deleteContent(String contentId, String contentType);
//	boolean deleteTemplate(String templateName);
//
//	String selectHTMLTemplate(String templateName);
//
//	IPage selectFullPage(String Url);
//	List<ITemplate> selectTemplates(String templateName, boolean withUrl, boolean withElements, boolean withHtml);
//	ContentElement selectContent(String contentId, String contentType);
//
//
//	/** DEBUG */
//	void displayContentBase();
	
}
