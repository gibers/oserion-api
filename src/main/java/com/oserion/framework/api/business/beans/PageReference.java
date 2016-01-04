package com.oserion.framework.api.business.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oserion.framework.api.business.IPageReference;
import com.oserion.framework.api.business.impl.mongo.beans.MongoTemplate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class PageReference implements IPageReference{

	@Id
    private String id;	
	
	@Indexed(unique=true)
	private String url;
	
	@Indexed(unique=true)
	private int key;

	@DBRef
	private MongoTemplate template ;
	
	public PageReference() {}
	public PageReference(MongoTemplate template, String url) {
		this.template = template;
		this.url = url;
	}
	public PageReference(MongoTemplate template, String url, int key) {
		this.template = template;
		this.url = url;
		this.key = key;
	}

	public PageReference(String newUrl) {
		this.url = newUrl;
	}

	@JsonIgnore
	public MongoTemplate getTemplate() {
		return template;
	}

	public void setTemplate(MongoTemplate template) {
		this.template = template;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
}

