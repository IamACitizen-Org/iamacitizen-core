package com.iamacitizen.core.datasource.mapper.cassandra;

import java.sql.SQLException;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.QueryExecutionException;
import com.datastax.driver.core.exceptions.QueryValidationException;
import com.iamacitizen.core.SerigyConstant;
import com.iamacitizen.core.database.cassandra.CassandraDatabase;
import com.iamacitizen.core.exception.SerigyDataSourceException;
import com.iamacitizen.core.exception.SerigyDatabaseException;
import com.iamacitizen.core.model.DomainObject;

/**
 * Classe..: AbstractMapper Objetivo:
 *
 * @author Felipe Cardoso
 */
public abstract class CassandraMapper<T extends DomainObject<K>, K> {

	protected PreparedStatement ps;
	protected Session session;

	public CassandraMapper() {
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
		session = CassandraDatabase.getSession();

		try {
			ps = session.prepare(getInsertSQL());

			BoundStatement boundStatement = new BoundStatement(ps);

			setInsertParameters(boundStatement, domainObject);

			session.execute(boundStatement);

		} catch (NoHostAvailableException | QueryExecutionException
				| QueryValidationException | IllegalStateException e) {
			CassandraDatabase.handleSQLError(e);
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
	public void update(T domainObject) throws SerigyDataSourceException {

		if (domainObject == null) {
			throw new IllegalArgumentException(SerigyConstant.DS_NULL_OBJECT);
		}

		ps = null;
		session = CassandraDatabase.getSession();
		
		try {
			ps = session.prepare(getUpdateSQL());
			
			BoundStatement boundStatement = new BoundStatement(ps);

			setUpdateParameters(boundStatement, domainObject);

			session.execute(boundStatement);
		} catch (NoHostAvailableException | QueryExecutionException
				| QueryValidationException | IllegalStateException e) {
			CassandraDatabase.handleSQLError(e);
		}

	}

	/**
	 * Exclui o objeto do banco de dados.
	 *
	 * @param domainObject
	 * @return a quantidade de registros excluídos.
	 * @throws SerigyDatasourceException
	 * @throws SerigyDatabaseException
	 */
	public void delete(T domainObject) throws SerigyDataSourceException {

		if (domainObject == null) {
			throw new IllegalArgumentException(SerigyConstant.DS_NULL_OBJECT);
		}

		ps = null;
		session = CassandraDatabase.getSession();
		
		try {
			ps = session.prepare(getDeleteSQL());
			
			BoundStatement boundStatement = new BoundStatement(ps);
			
			setDeleteParameters(boundStatement, domainObject);

			session.execute(boundStatement);
		} catch (NoHostAvailableException | QueryExecutionException
				| QueryValidationException | IllegalStateException e) {
			CassandraDatabase.handleSQLError(e);
		}

	}

	/**
	 *
	 * @param ps
	 * @param domainObject
	 * @return
	 * @throws SQLException
	 */
	protected abstract void setInsertParameters(BoundStatement bs, final T domainObject);

	/**
	 *
	 * @param ps
	 * @param domainObject
	 * @return
	 * @throws SQLException
	 */
	protected abstract void setUpdateParameters(BoundStatement ps, final T domainObject);

	/**
	 *
	 * @param ps
	 * @param domainObject
	 * @return
	 * @throws SQLException
	 */
	protected abstract void setDeleteParameters(BoundStatement ps, final T domainObject);
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
