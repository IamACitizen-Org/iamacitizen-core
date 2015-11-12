package com.iamacitizen.core.datasource.mapper;

import com.iamacitizen.core.SerigyConstant;
import com.iamacitizen.core.datasource.Session;
import com.iamacitizen.core.datasource.SessionManager;
import com.iamacitizen.core.exception.SerigyDataSourceException;
import com.iamacitizen.core.exception.SerigyDatabaseException;
import com.iamacitizen.core.model.DomainObject;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Classe..: AbstractMapper Objetivo:
 *
 * @author Felipe Cardoso
 */
public abstract class AbstractMapper<T extends DomainObject<K>, K> {

	protected PreparedStatement ps;
	protected Session session;

	public AbstractMapper() {
	}

	/**
	 *
	 * @param domainObject
	 * @throws SerigyDatasourceException
	 * @throws SerigyDatabaseException
	 */
	public void insertOrUpdate(T domainObject) throws SerigyDataSourceException {
		if (update(domainObject) == 0) {
			insert(domainObject);
		}
	}

	/**
	 * Insere o objeto no banco de dados.
	 *
	 * @param domainObject
	 * @throws SerigyDatasourceException
	 * @throws SerigyDatabaseException
	 */
	public void insert(T domainObject) throws SerigyDataSourceException {

		if (domainObject == null) {
			throw new IllegalArgumentException(SerigyConstant.DS_NULL_OBJECT);
		}

		ps = null;
		session = SessionManager.getActiveSession();

		try {
			ps = session.prepareStatement(getInsertSQL());
			setInsertParameters(ps, domainObject);

			ps.executeUpdate();
		} catch (SQLException e) {
			handleError(e);
		}
	}

	/**
	 * Atualiza o objeto no banco de dados.
	 *
	 * @param domainObject
	 * @return a quantidade de registros atualizados.
	 * @throws SerigyDatasourceException
	 * @throws SerigyDatabaseException
	 */
	public int update(T domainObject) throws SerigyDataSourceException {

		if (domainObject == null) {
			throw new IllegalArgumentException(SerigyConstant.DS_NULL_OBJECT);
		}

		int result = 0;
		ps = null;
		session = SessionManager.getActiveSession();

		try {
			ps = session.prepareStatement(getUpdateSQL());
			setUpdateParameters(ps, domainObject);

			result = ps.executeUpdate();
		} catch (SQLException e) {
			handleError(e);
		}

		return result;
	}

	/**
	 * Exclui o objeto do banco de dados.
	 *
	 * @param domainObject
	 * @return a quantidade de registros excluídos.
	 * @throws SerigyDatasourceException
	 * @throws SerigyDatabaseException
	 */
	public int delete(T domainObject) throws SerigyDataSourceException {

		if (domainObject == null) {
			throw new IllegalArgumentException(SerigyConstant.DS_NULL_OBJECT);
		}

		int result = 0;
		ps = null;
		session = SessionManager.getActiveSession();

		try {
			ps = session.prepareStatement(getDeleteSQL());
			setDeleteParameters(ps, domainObject);

			result = ps.executeUpdate();
		} catch (SQLException e) {
			handleError(e);
		}

		return result;
	}

	/**
	 * Trata o SQLException, realizando umrollback na transação e a tradução da mensagem de erro.
	 *
	 * @param e
	 * @throws SerigyDataSourceException
	 */
	protected void handleError(SQLException e) throws SerigyDataSourceException {
		SessionManager.rollbackSession();

		String errorMessage = session.handleSQLError(e);
		throw new SerigyDataSourceException(errorMessage);
	}

	/**
	 *
	 * @param ps
	 * @param domainObject
	 * @return
	 * @throws SQLException
	 */
	protected abstract void setInsertParameters(PreparedStatement ps, final T domainObject) throws SQLException;

	/**
	 *
	 * @param ps
	 * @param domainObject
	 * @return
	 * @throws SQLException
	 */
	protected abstract void setUpdateParameters(PreparedStatement ps, final T domainObject) throws SQLException;

	/**
	 *
	 * @param ps
	 * @param domainObject
	 * @return
	 * @throws SQLException
	 */
	protected abstract void setDeleteParameters(PreparedStatement ps, final T domainObject) throws SQLException;

	/**
	 *
	 * @return Comando SQL referente à operação de insert.
	 */
	protected abstract String getInsertSQL();

	/**
	 *
	 * @return Comando SQL referente à operação de insert.
	 */
	protected abstract String getUpdateSQL();

	/**
	 *
	 * @return Comando SQL referente à operação de insert.
	 */
	protected abstract String getDeleteSQL();
}
