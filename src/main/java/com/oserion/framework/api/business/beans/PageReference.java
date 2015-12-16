package com.oserion.framework.api.business.beans;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class PageReference {

	@Id
    private String id;	
	
	@Indexed(unique=true)
	private int key = -1;
	
	private String url;

	
	public PageReference() {}
	public PageReference(int key, String url) {
		this.key = key;
		this.url = url;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}

