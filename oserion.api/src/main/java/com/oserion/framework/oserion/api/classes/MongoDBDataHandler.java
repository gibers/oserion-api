package com.oserion.framework.oserion.api.classes;

import org.springframework.stereotype.Component;

import com.oserion.framework.oserion.api.interfaces.IDataHandler;

//@Component
public class MongoDBDataHandler implements IDataHandler {

	public String insertOrUpdateTemplate(String string) {
		return "insertionTemplateOK";
	}

}
