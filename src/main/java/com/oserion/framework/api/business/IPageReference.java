package com.oserion.framework.api.business;


import java.io.Serializable;

public interface IPageReference extends Serializable {

	String getUrl();
	void setUrl(String url);
	int getKey();
	void setKey(int key);

}

