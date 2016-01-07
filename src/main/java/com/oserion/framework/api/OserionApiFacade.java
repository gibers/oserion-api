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

@Component
public class OserionApiFacade {

	private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


	@Autowired
	private IDataHandler dataHandler;

	public void createTemplate(String name, String html) throws OserionDatabaseException {
		dataHandler.insertTemplate(name, html);
	}

	public List<ITemplate> getTemplates() throws OserionDatabaseException {
		return dataHandler.selectTemplates();
	}

	public void addPageUrl(String templateName, String url) throws OserionDatabaseException {
		dataHandler.insertPageUrl(templateName, url);
	}




}

