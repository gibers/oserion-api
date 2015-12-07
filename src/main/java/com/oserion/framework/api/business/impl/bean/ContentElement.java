package com.oserion.framework.api.business.impl.bean;

public class ContentElement {
	
	public enum Type { EDITABLE, REPEATABLE }
	
	private String ref;
	private String type ;
	private String value;
	
	public ContentElement(String ref, Enum type, String value) {
		this.ref = ref;
		this.type = type.name();
		this.value = value;
	}
	
	public String getRef() {
		return ref;
	}
//	public void setRef(String ref) {
//		this.ref = ref;
//	}
	public String getType() {
		return type;
	}
//	public void setType(String type) {
//		this.type = type;
//	}
	public String getValue() {
		return value;
	}
//	public void setValue(String value) {
//		this.value = value;
//	}
	
	
	public void affiche() {
		System.out.println("---------------------");
		System.out.println("ref => " + ref);
		System.out.println("type => " + type);
		System.out.println("value => " + value);
	}

}

