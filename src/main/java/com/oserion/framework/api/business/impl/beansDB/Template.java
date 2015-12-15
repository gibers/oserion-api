package com.oserion.framework.api.business.impl.beansDB;

import org.springframework.data.annotation.Id;


public class Template {

	@Id
    private String id;
	private String name;
    private String html;
    
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	
	
	public Template () {}
    public Template (String name, String html) {
    	this.name= name;
    	this.html= html;
    }
    
    
    @Override
    public String toString() {
        return String.format(
                "Template[name=%s, html='%s']",
                name, html);
    }
    
}
