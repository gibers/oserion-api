package com.oserion.framework.api.business.beans;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ContentElement {

	@Id
    private String id;

	public enum Type { EDITABLE, REPEATABLE }
	
	private String ref;
	private String type ;
	private String value;
	
	
//	public ContentElement(String ref, Enum type, String value) {
//		this.ref = ref;
//		this.type = type.name();
//		this.value = value;
//	}


	public ContentElement() {}
	public ContentElement(String ref, String type, String value) {
		this.ref = ref;
		this.type = type;
		this.value = value;
	}
	public ContentElement(String ref, String type) {
		this.ref = ref;
		this.type = type;
	}

	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
		
	public void affiche() {
		System.out.println("---------------------");
		System.out.println("ref => " + ref);
		System.out.println("type => " + type);
		System.out.println("value => " + value);
	}
	
	public boolean identique(ContentElement cteold) {
		if(!cteold.getRef().equalsIgnoreCase(ref)) return false;
		if(!cteold.getType().equalsIgnoreCase(type)) return false;
		return true ;
	}

}

