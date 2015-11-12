package com.iamacitizen.core.datasource;

import com.iamacitizen.core.database.AbstractDatabase;
import com.iamacitizen.core.exception.SerigyDataSourceException;
import com.iamacitizen.core.exception.SerigyDatabaseException;
import com.iamacitizen.core.model.annotation.AnnotationDescriptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class ActiveSession implements Session {

	private final long id;
	private AbstractDatabase database;
	private Connection conn;

	/**
	 *
	 * @param id
	 * @throws SerigyDataSourceException
	 */
	ActiveSession(long id) throws SerigyDataSourceException {
		this.id = id;
		database = DatabasePlugin.getDatabase();
		createConnection();
	}

	/**
	 * Cria a conexão com o banco de dados
	 *
	 * @throws SerigyDatabaseException
	 */
	private void createConnection() {
		conn = database.createConnection();
	}

	/**
	 * Retorna o identificador da sessão.
	 *
	 * @return identificador da sessão.
	 */
	public long getId() {
		return id;
	}

	/**
	 *
	 * @throws SerigyDatabaseException
	 */
	public void beginTransaction() {
		database.beginTransaction(conn);
	}

	/**
	 *
	 * @throws SerigyDatabaseException
	 */
	 void rollback() {
		database.rollback(conn);
	}

	/**
	 *
	 * @throws SerigyDatabaseException
	 */
	public void commit() {
		database.commitTransaction(conn);
	}

	/**
	 *
	 * @param SQL
	 * @return
	 * @throws SerigyDatabaseException
	 */
	public PreparedStatement prepareStatement(String SQL) {
		try {
			PreparedStatement ps = getConnection().prepareStatement(SQL);

			return ps;
		} catch (SQLException ex) {
			throw new SerigyDatabaseException(ex.getMessage());
		}
	}

	/**
	 * Recupera a conexão associada à sessão
	 *
	 * @return Conexão
	 */
	Connection getConnection() {
		return conn;
	}

	/**
	 * Fecha a sessão, isto é, a conexão com o banco de dados.
	 */
	void close() {
		database.closeConnection(getConnection());
	}

	/**
	 *
	 * @param e
	 * @return
	 * @throws SerigyDatabaseException
	 */
	public String handleSQLError(SQLException e) {
		return database.handleSQLError(e);
	}

	/**
	 *
	 * @param owner
	 * @param sequenceName
	 * @return
	 * @throws SerigyDatabaseException
	 */
	public long getIdentityNextVal(String sequenceName) {
		return database.getIdentityNextVal(sequenceName);
	}

	/**
	 *
	 * @param domainObjectClass
	 * @return Um long representando o identificador.
	 * @throws SerigyDatabaseException
	 */
	public long getIdentityNextVal(Class domainObjectClass) {
		AnnotationDescriptor ad = new AnnotationDescriptor(domainObjectClass);

		return database.getIdentityNextVal(ad.getSequenceName());
	}
}
