package com.oserion.framework.oserion.api.interfaces;

import org.springframework.stereotype.Component;

//@Component
public interface ITemplificator {

	String createTemplateFromHTML(String fluxTemplate, String templateName);

}
