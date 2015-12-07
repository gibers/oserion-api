package com.oserion.framework.api.business;

import org.springframework.stereotype.Component;

import com.oserion.framework.api.business.beans.ContentElement;

@Component
public interface ITemplificator {

	ITemplate createTemplateFromHTML(String fluxTemplate, String templateName);
	ContentElement majContenu(ContentElement e);

}
