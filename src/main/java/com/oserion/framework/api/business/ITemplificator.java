package com.oserion.framework.api.business;

import org.springframework.stereotype.Component;

import com.oserion.framework.api.business.impl.bean.ContentElement;

@Component
public interface ITemplificator {

	public ITemplate createTemplateFromHTML(String fluxTemplate, String templateName);
	public ContentElement majContenu(String ref, String type, String contenue); 

}
