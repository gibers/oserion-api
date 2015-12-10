package com.oserion.framework.api;

import com.oserion.framework.api.business.IDataHandler;
import com.oserion.framework.api.business.ITemplate;
import com.oserion.framework.api.business.ITemplificator;
import com.oserion.framework.api.business.beans.ContentElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Api418Facade {

	@Autowired
	private ITemplificator ijst;

	@Autowired
	private IDataHandler idh;


	public String uploadTemplateFromHtml( String fluxTemplate, String templateName ) {
		ITemplate template1 = ijst.createTemplateFromHTML(fluxTemplate, templateName);
		idh.insertOrUpdateTemplate(template1);
		idh.displayContentBase();
		return templateName;
	}


	public String getHTMLPage(String nameTemplate) {
		return idh.selectHTMLTemplate(nameTemplate);
	}


	public void setContent(ContentElement e) {
		ContentElement cte = ijst.majContenu(e);
		idh.insertOrUpdateContent(cte);
	}

	public boolean addPageUrl(String templateName, String url){
		return idh.insertPageURL(templateName, url);
	}

	public ITemplificator getIjst() {
		return ijst;
	}

	public void setIjst(ITemplificator ijst) {
		this.ijst = ijst;
	}

	public IDataHandler getIdh() {
		return idh;
	}

	public void setIdh(IDataHandler idh) {
		this.idh = idh;
	}
	
}
