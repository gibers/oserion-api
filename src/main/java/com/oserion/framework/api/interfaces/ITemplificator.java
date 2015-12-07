package com.oserion.framework.api.interfaces;

import org.springframework.stereotype.Component;

@Component
public interface ITemplificator {

	ITemplate createTemplateFromHTML(String fluxTemplate, String templateName);

}
