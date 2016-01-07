package com.oserion.framework.api;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.oserion.framework.api.exceptions.OserionDatabaseException;
import com.oserion.framework.api.util.OserionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.oserion.framework.api.business.IDataHandler;
import com.oserion.framework.api.business.ITemplate;
import com.oserion.framework.api.business.ITemplificator;
import com.oserion.framework.api.business.beans.ContentElement;
import com.oserion.framework.api.business.beans.ContentElement.Type;
import com.oserion.framework.api.business.beans.PageReference;
import com.oserion.framework.api.business.impl.jsoup.JsoupTemplate;
import com.oserion.framework.api.util.CodeReturn;


@Component
public class OserionApiFacade {

	private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


	@Autowired
	private IDataHandler dataHandler;

	public void createTemplate(String name, String html) throws OserionDatabaseException {
		dataHandler.insertTemplate(name, html);
	}

	public void getListTemplate() throws OserionDatabaseException {
		//
		// dataHandler.insertTemplate(name, html);
	}




}

