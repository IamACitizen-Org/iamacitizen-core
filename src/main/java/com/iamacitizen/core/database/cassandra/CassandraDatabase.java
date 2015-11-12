package com.iamacitizen.core.database.cassandra;

import java.util.Properties;

import com.datastax.driver.core.Session;
import com.iamacitizen.core.SerigyConstant;
import com.iamacitizen.core.exception.SerigyDataSourceException;
import com.iamacitizen.core.exception.SerigyDatabaseException;
import com.iamacitizen.core.exception.SerigyException;
import com.iamacitizen.core.util.ConfigFileLoader;

public class CassandraDatabase {

	private static CassandraConnectionManager connectionManager;

	static {

		if (connectionManager == null) {
			try {
				createConnectionManager();
			} catch (InstantiationException ex) {
				throw new SerigyDatabaseException(ex.getMessage());
			} catch (IllegalAccessException ex) {
				throw new SerigyDatabaseException(ex.getMessage());
			} catch (ClassNotFoundException ex) {
				throw new SerigyDatabaseException(
						SerigyConstant.DB_CONNECTION_MAN_CLASS_NOT_FOUND);
			} catch (SerigyException e) {
				throw new SerigyDatabaseException(e.getMessage());
			}
		}
	}

	private static void createConnectionManager() throws InstantiationException,
			ClassNotFoundException, SerigyException, IllegalAccessException {

		Properties properties = ConfigFileLoader.load();
		String node = properties.getProperty("Node");

		if (node == null) {
			throw new SerigyDatabaseException(
					SerigyConstant.CONFIG_FILE_INVALID);
		}

		connectionManager = new CassandraConnectionManager(node);
	}

	/**
	 * Gets a new time uuid.
	 * 
	 * @return the time uuid
	 */
	public static java.util.UUID getTimeUUID() {
		return java.util.UUID.fromString(new com.eaio.uuid.UUID().toString());
	}

	/**
	 * Returns an instance of uuid.
	 * 
	 * @param uuid
	 *            the uuid
	 * @return the java.util. uuid
	 */
	public static java.util.UUID toUUID(byte[] uuid) {
		long msb = 0;
		long lsb = 0;
		assert uuid.length == 16;
		for (int i = 0; i < 8; i++) {
			msb = (msb << 8) | (uuid[i] & 0xff);
		}
		for (int i = 8; i < 16; i++) {
			lsb = (lsb << 8) | (uuid[i] & 0xff);
		}

		com.eaio.uuid.UUID u = new com.eaio.uuid.UUID(msb, lsb);
		return java.util.UUID.fromString(u.toString());
	}

	/**
	 * As byte array.
	 * 
	 * @param uuid
	 *            the uuid
	 * 
	 * @return the byte[]
	 */
	public static byte[] asByteArray(java.util.UUID uuid) {
		long msb = uuid.getMostSignificantBits();
		long lsb = uuid.getLeastSignificantBits();
		byte[] buffer = new byte[16];

		for (int i = 0; i < 8; i++) {
			buffer[i] = (byte) (msb >>> 8 * (7 - i));
		}
		for (int i = 8; i < 16; i++) {
			buffer[i] = (byte) (lsb >>> 8 * (7 - i));
		}

		return buffer;
	}

	/**
	 * 
	 * @return @throws SerigyDatabaseException
	 */
	public static Session getSession() {
		if (connectionManager == null) {
			throw new SerigyDatabaseException(
					SerigyConstant.DB_CONNECTION_MAN_NO_INSTANCE);
		}

		return connectionManager.getSession();
	}

	/**
	 * 
	 * @param session
	 */
	public static void closeSession() {
		connectionManager.closeSession();
	}
	
	public static String handleSQLError(Exception e) throws SerigyDataSourceException {
		throw new SerigyDataSourceException(e.getMessage());
	}

}
