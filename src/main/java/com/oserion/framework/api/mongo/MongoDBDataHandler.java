package com.oserion.framework.api.mongo;

import org.springframework.stereotype.Component;

import com.oserion.framework.api.interfaces.IDataHandler;

//@Component
public class MongoDBDataHandler implements IDataHandler {

	public String insertOrUpdateTemplate(String string) {
		return "insertionTemplateOK";
	}

}
