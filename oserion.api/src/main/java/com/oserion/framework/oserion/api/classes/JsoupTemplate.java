package com.oserion.framework.oserion.api.classes;

import java.util.List;

import org.springframework.stereotype.Component;

import com.oserion.framework.oserion.api.interfaces.ITemplate;

@Component
public class JsoupTemplate implements ITemplate {
	
	private String name = null;
	private String html = null;
	
	private List<ContentElement> listTemplateContentElement = null;
	private List<ContentElement> listPageContentElement = null;
	
	
	
}
