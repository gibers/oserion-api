package com.oserion.framework.api.configurationbeans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.oserion.framework.api.implementation.Api418Facade;
import com.oserion.framework.api.interfaces.IDataHandler;
import com.oserion.framework.api.interfaces.ITemplificator;
import com.oserion.framework.api.jsoup.JsoupTemplificator;
import com.oserion.framework.api.mongo.MongoDBDataHandler;

@Configuration
@ComponentScan(basePackages={"com.oserion.framework.oserion.api.classes", "com.oserion.framework.oserion.api.interfaces"})
public class CentraleBean {
	
	@Bean
	public ITemplificator Iitemplificator() {
		return new JsoupTemplificator();
	}
	
	@Bean
	public IDataHandler idataHandler() {
		return new MongoDBDataHandler();
	}

	@Bean
	public Api418Facade api418Facade() {
		return new Api418Facade();
	}
	
}

