package com.oserion.framework.api.mongo;

public class ContentElement {
	
	public enum Type { EDITABLE, REPEATABLE }
	
	private String ref;
	private Enum Type ;
	private String value;
	
	public ContentElement(String ref, Enum type, String value) {
		this.ref = ref;
		this.Type = type;
		this.value = value;
	}
	
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public Enum getType() {
		return Type;
	}
	public void setType(Enum type) {
		Type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}

