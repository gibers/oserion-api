package com.oserion.framework.api.business.impl.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.oserion.framework.api.business.IDBConnection;

//@Component
public class MongoDBConnection implements IDBConnection {

	private static final String PROPERTY_DB_HOST = "database.host";
	private static final String PROPERTY_DB_PORT = "database.port";
	private static final String PROPERTY_DB_SCHEMA = "database.schema";

	private MongoDatabase database ;

	public MongoDBConnection() {

		MongoClient mongoClient = new MongoClient(System.getProperty(PROPERTY_DB_HOST),
				Integer.parseInt(System.getProperty(PROPERTY_DB_PORT)));
		this.database = mongoClient.getDatabase(System.getProperty(PROPERTY_DB_SCHEMA));
	}

	public Object getDatabase() {
		return database;
	}
}

