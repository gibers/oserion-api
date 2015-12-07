package com.oserion.framework.api.business;

import java.util.List;

import org.springframework.stereotype.Component;

import com.oserion.framework.api.business.impl.bean.ContentElement;

@Component
public interface IDataHandler {

	boolean insertOrUpdateTemplate(ITemplate template);
	boolean insertOrUpdateManyContent(List<ContentElement> listElement);
	boolean insertOrUpdateContent(ContentElement ele);
	boolean insertPageURL(String templateName, String URL);

	boolean deletePageURL(String URL);
	boolean deleteContent(String contentId, String contentType);
	boolean deleteTemplate(String templateName);

	String selectHTMLTemplate(String templateName);

	IPage selectFullPage(String Url);
	List<ITemplate> selectTemplates(boolean withUrl, boolean withElements, boolean withHtml);
	ContentElement selectContent(String contentId, String contentType);


	/** DEBUG */
	void displayContentBase();
	
}
