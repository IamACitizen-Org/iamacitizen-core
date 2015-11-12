package com.iamacitizen.core.database;

import com.iamacitizen.core.SerigyConstant;
import com.iamacitizen.core.exception.SerigyDatabaseException;
import com.iamacitizen.core.exception.SerigyException;
import com.iamacitizen.core.util.ConfigFileLoader;
import com.iamacitizen.core.util.Logger;

import java.util.Properties;

/**
 * Classe..: DatabaseFactory Objetivo:
 *
 * @author Felipe Cardoso
 */
public class DatabaseFactory {

	private static DatabaseFactory instance;

	private DatabaseFactory() {
	}

	public static synchronized DatabaseFactory getDatabaseFactory() {
		if (instance == null) {
			instance = new DatabaseFactory();
		}
		return instance;
	}

	public AbstractDatabase getDefaultDatabase() throws SerigyDatabaseException, SerigyException {
		try {
			String dbName = getDefaultDatabaseName();

			if (DatabaseEnum.ORACLE.equals(DatabaseEnum.fromValue(dbName))) {
				return getOracleDatabase();
			} else if (DatabaseEnum.POSTGRES.equals(DatabaseEnum.fromValue(dbName))) {
				return getPostgresDatabase();
			} else {
				throw new SerigyDatabaseException(SerigyConstant.CONFIG_FILE_INVALID);
			}

		} catch (RuntimeException rte) {
			throw new SerigyDatabaseException(rte.getMessage());
		}
	}

	private String getDefaultDatabaseName() throws SerigyDatabaseException, SerigyException {
		Properties properties = ConfigFileLoader.load();
		String databaseName = properties.getProperty("Database");

		if (databaseName == null) {
			throw new SerigyDatabaseException(SerigyConstant.CONFIG_FILE_INVALID);
		}

		return databaseName;
	}

	private AbstractDatabase getOracleDatabase() {
		AbstractDatabase database = null;

		try {
			Class c = Class.forName("com.serigylabs.database.oracle.OracleDatabase");
			Object object = c.newInstance();
			database = (AbstractDatabase) object;

		} catch (ClassNotFoundException e) {
			Logger.error(e.getMessage());

		} catch (InstantiationException e) {
			Logger.error(e.getMessage());

		} catch (IllegalAccessException e) {
			Logger.error(e.getMessage());
		}

		return database;
	}

	private AbstractDatabase getPostgresDatabase() {
		AbstractDatabase database = null;

		try {
			Class c = Class.forName("com.serigylabs.database.postgres.PostgresDatabase");
			Object object = c.newInstance();
			database = (AbstractDatabase) object;

		} catch (ClassNotFoundException e) {
			Logger.error(e.getMessage());

		} catch (InstantiationException e) {
			Logger.error(e.getMessage());

		} catch (IllegalAccessException e) {
			Logger.error(e.getMessage());
		}

		return database;
	}
}
