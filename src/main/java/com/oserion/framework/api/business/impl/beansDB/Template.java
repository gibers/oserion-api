package com.oserion.framework.api.business.impl.beansDB;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.oserion.framework.api.business.beans.ContentElement;


@Document
public class Template {

	@Id
    private String id;
	
	@Indexed(unique = true)
	private String name;
    
	private String html;

	@DBRef
	private List<ContentElement> listTemplateElement  = new ArrayList<ContentElement>();
	@DBRef	
	private List<ContentElement> listVariableElement  = new ArrayList<ContentElement>();

//	private List<String> listPageReference  = new ArrayList<String>();
	
	
	public String getId() {
		return id;
	}
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
	
	
	public List<ContentElement> getListTemplateElement() {
		return listTemplateElement;
	}
	public void setListTemplateElement(List<ContentElement> listTemplateElement) {
		this.listTemplateElement = listTemplateElement;
	}
	public List<ContentElement> getListVariableElement() {
		return listVariableElement;
	}
	public void setListVariableElement(List<ContentElement> listVariableElement) {
		this.listVariableElement = listVariableElement;
	}
//	public List<String> getListPageReference() {
//		return listPageReference;
//	}
//	public void setListPageReference(List<String> listPageReference) {
//		this.listPageReference = listPageReference;
//	}
	
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
