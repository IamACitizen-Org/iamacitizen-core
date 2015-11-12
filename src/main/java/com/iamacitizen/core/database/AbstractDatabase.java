package com.iamacitizen.core.database;

import com.iamacitizen.core.SerigyConstant;
import com.iamacitizen.core.exception.SerigyDatabaseException;
import com.iamacitizen.core.exception.SerigyException;
import com.iamacitizen.core.model.database.Constraint;
import com.iamacitizen.core.util.ConfigFileLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Classe..: AbstractDatabase Objetivo:
 *
 * @author Felipe Cardoso
 */
public abstract class AbstractDatabase {

	private static ConnectionManager connectionManager;

	public AbstractDatabase() {

		if (connectionManager == null) {
			try {
				createConnectionManager();
			} catch (InstantiationException ex) {
				throw new SerigyDatabaseException(ex.getMessage());
			} catch (IllegalAccessException ex) {
				throw new SerigyDatabaseException(ex.getMessage());
			} catch (ClassNotFoundException ex) {
				throw new SerigyDatabaseException(SerigyConstant.DB_CONNECTION_MAN_CLASS_NOT_FOUND);
			} catch (SerigyException e) {
				throw new SerigyDatabaseException(e.getMessage());
			}
		}
	}

	private void createConnectionManager() throws InstantiationException, ClassNotFoundException, SerigyException,
			IllegalAccessException {

		Properties properties = ConfigFileLoader.load();
		String className = properties.getProperty("ConnectionManager");

		if (className == null) {
			throw new SerigyDatabaseException(SerigyConstant.CONFIG_FILE_INVALID);
		}

		connectionManager = (ConnectionManager) Class.forName(className).newInstance();
	}

	/**
	 *
	 * @param SQL
	 * @return
	 * @throws SerigyDatabaseException
	 */
	protected long generateIdentity(String SQL) {
		long result = 0;

		DatabaseFactory factory = DatabaseFactory.getDatabaseFactory();
		AbstractDatabase database = null;

		try {
			database = factory.getDefaultDatabase();
		} catch (SerigyException ex) {
			throw new SerigyDatabaseException(ex.getMessage());
		}

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {


			conn = database.createConnection();
			ps = conn.prepareStatement(SQL);
			rs = ps.executeQuery();

			if (rs.next()) {
				result = mapSequenceValue(rs);
			}
		} catch (SQLException ex) {
			throw new SerigyDatabaseException(ex.getMessage());
		} finally {
			database.cleanUp(rs, ps, conn);
		}

		return result;
	}

	/**
	 *
	 * @return @throws SerigyDatabaseException
	 */
	public Connection createConnection() {
		if (connectionManager == null) {
			throw new SerigyDatabaseException(SerigyConstant.DB_CONNECTION_MAN_NO_INSTANCE);
		}

		return connectionManager.getConnection();
	}

	/**
	 *
	 * @param conn
	 * @throws SerigyDatabaseException
	 */
	public void beginTransaction(Connection conn) {
		DatabaseUtils.beginTransaction(conn);
	}

	/**
	 *
	 * @param conn
	 * @throws SerigyDatabaseException
	 */
	public void rollback(Connection conn) {
		DatabaseUtils.rollback(conn);
	}

	/**
	 * 
	 * @param conn 
	 */
	public void closeConnection(Connection conn) {
		DatabaseUtils.cleanUp(conn);
	}

	/**
	 *
	 * @param conn
	 * @throws SerigyDatabaseException
	 */
	public void commitTransaction(Connection conn) {
		DatabaseUtils.commitTransaction(conn);
	}

	public void cleanUp(Connection conn) {
		DatabaseUtils.cleanUp(conn);
	}

	/**
	 *
	 * @param ps
	 * @param conn
	 */
	public void cleanUp(PreparedStatement ps, Connection conn) {
		DatabaseUtils.cleanUp(ps, conn);
	}

	/**
	 *
	 * @param rs
	 * @param ps
	 * @param conn
	 */
	public void cleanUp(ResultSet rs, PreparedStatement ps, Connection conn) {
		DatabaseUtils.cleanUp(rs, ps, conn);
	}

	/**
	 *
	 * @param owner
	 * @param name
	 * @return
	 * @throws SerigyDatabaseException
	 */
	public abstract Constraint getConstraint(String owner, String name);

	/**
	 *
	 * @param owner
	 * @param sequenceName
	 * @return
	 * @throws SerigyDatabaseException
	 */
	public abstract long getIdentityNextVal(String sequenceName);

	/**
	 *
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected abstract long mapSequenceValue(ResultSet rs) throws SQLException;

	/**
	 * Mapeia uma exceção SQL para uma mensagem mais amigável para o usuário
	 *
	 * @param e exceção lançada.
	 * @return Mensagem de erro amigável.
	 * @throws SerigyDatabaseException
	 */
	public abstract String handleSQLError(SQLException e);
}
