package com.oserion.framework.api.interfaces;

import java.util.List;

import org.springframework.stereotype.Component;

import com.oserion.framework.api.mongo.ContentElement;

@Component
public interface IDataHandler {

	public void insertOrUpdateTemplate(ITemplate template);
	public void insertOrUpdateManyContenue(List<ContentElement> listElement);
	public void insertOrUpdateContenue(ContentElement ele);
	public String getTemplate(String nameTemplate);
	
	
	/** DEBUG */
	public void afficheContenuBase();
	
}
