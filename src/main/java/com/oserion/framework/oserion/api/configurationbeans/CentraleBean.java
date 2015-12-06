package com.oserion.framework.oserion.api.configurationbeans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.oserion.framework.oserion.api.classes.Api418Facade;
import com.oserion.framework.oserion.api.classes.JsoupTemplificator;
import com.oserion.framework.oserion.api.classes.MongoDBDataHandler;
import com.oserion.framework.oserion.api.interfaces.IDataHandler;
import com.oserion.framework.oserion.api.interfaces.ITemplificator;

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

