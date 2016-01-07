package com.oserion.framework.api.business.impl.mongo.beans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oserion.framework.api.business.IPageReference;
import com.oserion.framework.api.business.ITemplate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.oserion.framework.api.business.beans.ContentElement;


@Document
public class MongoTemplate {

	@Id
    private String id;
	
	@Indexed(unique = true)
	private String name;
    
	private String html;

	//@DBRef
	private List<ContentElement> listTemplateElement  = new ArrayList<ContentElement>();
	//@DBRef
	private List<ContentElement> listVariableElement  = new ArrayList<ContentElement>();
	private List<IPageReference> listPage   = new ArrayList<IPageReference>();

	public MongoTemplate() {}
	public MongoTemplate(ITemplate t) {
		this.name = t.getName();
		this.html = t.getHtml();
		this.listPage = t.getListPage();
		this.listTemplateElement = t.getListTemplateElement();
		this.listVariableElement = t.getListVariableElement();
	}

	@JsonIgnore
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

    @Override
    public String toString() {
        return String.format(
                "MongoTemplate[name=%s, html='%s']",
                name, html);
    }
    
}
