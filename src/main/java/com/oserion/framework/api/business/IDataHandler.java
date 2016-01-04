package com.oserion.framework.api.business;

import java.util.List;

import com.oserion.framework.api.exceptions.OserionDatabaseException;
import org.springframework.stereotype.Component;

import com.oserion.framework.api.business.beans.ContentElement;

@Component
public interface IDataHandler {

	boolean insertOrUpdateTemplate(ITemplate template);
	boolean insertTemplate(ITemplate template);
	boolean updateTemplate(String templateName, String fluxHtml) throws OserionDatabaseException;

	boolean insertOrUpdateManyContent(List<ContentElement> listElement);
	boolean insertOrUpdateContent(ContentElement ele);
	boolean insertPageURL(String templateName, String URL);

	boolean deletePageURL(String URL);
	boolean deleteContent(String contentId, String contentType);
	boolean deleteTemplate(String templateName);

	String selectHTMLTemplate(String templateName);

	IPage selectFullPage(String Url);
	List<ITemplate> selectTemplates(String templateName, boolean withUrl, boolean withElements, boolean withHtml);
	ContentElement selectContent(String contentId, String contentType);


	/** DEBUG */
	void displayContentBase();
	
}
