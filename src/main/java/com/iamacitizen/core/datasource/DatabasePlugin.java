package com.iamacitizen.core.datasource;

import com.iamacitizen.core.database.AbstractDatabase;
import com.iamacitizen.core.database.DatabaseFactory;
import com.iamacitizen.core.exception.SerigyDatabaseException;
import com.iamacitizen.core.exception.SerigyException;

public class DatabasePlugin {

    /**
     *
     * @return AbstractDatabase
     */
    public static AbstractDatabase getDatabase() {
		try {
			DatabaseFactory factory = DatabaseFactory.getDatabaseFactory();
			AbstractDatabase database = factory.getDefaultDatabase();
		
			return database;

		} catch (SerigyException ex) {
			throw new SerigyDatabaseException(ex.getMessage());
		}
    }
}
