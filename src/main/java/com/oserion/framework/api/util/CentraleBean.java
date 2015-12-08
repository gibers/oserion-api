package com.oserion.framework.api.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.oserion.framework.api.Api418Facade;
import com.oserion.framework.api.business.IDataHandler;
import com.oserion.framework.api.business.ITemplificator;
import com.oserion.framework.api.business.impl.jsoup.JsoupTemplificator;
import com.oserion.framework.api.business.impl.mongo.MongoDBDataHandler;

@Configuration
//@ComponentScan(basePackages={"com.oserion.framework.api.business.interfaces"})
public class CentraleBean {
	
	@Bean
	public ITemplificator Iitemplificator() {
		return new JsoupTemplificator();
	}
	
	@Bean
	public IDataHandler idataHandler() {
		return new MongoDBDataHandler(/* "localhost", 27017, "oserionBD" */);
	}

	@Bean
	public Api418Facade api418Facade() {
		return new Api418Facade();
	}
	
}




