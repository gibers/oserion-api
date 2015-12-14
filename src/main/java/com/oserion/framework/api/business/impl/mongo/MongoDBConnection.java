package com.oserion.framework.api.business.impl.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.oserion.framework.api.business.IDBConnection;
import org.springframework.stereotype.Component;

@Component
public class MongoDBConnection implements IDBConnection {

	private static MongoDBConnection instance;

	private static final String PROPERTY_DB_HOST = "database.host";
	private static final String PROPERTY_DB_PORT = "database.port";
	private static final String PROPERTY_DB_SCHEMA = "database.schema";

	private MongoDatabase database;

	private MongoDBConnection() {
		MongoClient mongoClient = new MongoClient(System.getProperty(PROPERTY_DB_HOST),
				Integer.parseInt(System.getProperty(PROPERTY_DB_PORT)));
		this.database = mongoClient.getDatabase(System.getProperty(PROPERTY_DB_SCHEMA));
	}

	public static MongoDBConnection getInstance() {
		if (instance == null) {
			// Thread Safe. Might be costly operation in some case
			synchronized (MongoDBConnection.class) {
				if (instance == null) {
					instance = new MongoDBConnection();
				}
			}
		}
		return instance;
	}

	public Object getDatabase() {
		return database;
	}
}

