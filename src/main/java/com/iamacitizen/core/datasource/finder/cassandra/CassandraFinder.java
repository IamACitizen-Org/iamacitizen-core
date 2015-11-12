package com.iamacitizen.core.datasource.finder.cassandra;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.QueryExecutionException;
import com.datastax.driver.core.exceptions.QueryValidationException;
import com.iamacitizen.core.SerigyConstant;
import com.iamacitizen.core.database.cassandra.CassandraDatabase;
import com.iamacitizen.core.exception.SerigyDataSourceException;
import com.iamacitizen.core.model.DomainObject;

public abstract class CassandraFinder<T extends DomainObject<K>, K> implements
		CassandraRowMapper<T, K> {

	protected PreparedStatement ps;
	protected ResultSet rs;
	protected Session session;

	public CassandraFinder() {
	}

	/**
	 * 
	 * @param key
	 * @return
	 * @throws SerigyDataSourceException
	 */
	public T find(K key) throws SerigyDataSourceException {

		if (key == null) {
			throw new IllegalArgumentException(SerigyConstant.MSG_NULL_KEY);
		}

		T result = null;

		ps = null;
		rs = null;
		session = CassandraDatabase.getSession();

		try {
			ps = session.prepare(getfindSQL());

			BoundStatement boundStatement = new BoundStatement(ps);

			setFindParameters(boundStatement, key);

			rs = session.execute(boundStatement);

			for (Row row : rs) {
				result = mapRow(row);
			}
		} catch (NoHostAvailableException | QueryExecutionException
				| QueryValidationException | IllegalStateException e) {
			handleError(e);
		}

		return result;
	}

	/**
	 * 
	 * @return @throws SerigyDataSourceException
	 */
	public List<T> findAll() throws SerigyDataSourceException {
		List<T> result = new ArrayList<>();

		ps = null;
		rs = null;
		session = CassandraDatabase.getSession();

		try {
			ps = session.prepare(getfindAllSQL());

			BoundStatement boundStatement = new BoundStatement(ps);

			rs = session.execute(boundStatement);
			T object = null;

			for (Row row : rs) {
				object = mapRow(row);
				result.add(object);
			}

		} catch (NoHostAvailableException | QueryExecutionException
				| QueryValidationException | IllegalStateException e) {
			handleError(e);
		}

		return result;
	}

	/**
	 * 
	 * @param filter
	 *            Objeto representando o filtro da consulta.
	 * @return Lista dos objetos que satisfazem a consulta.
	 * @throws SerigyDataSourceException
	 */
	public List<T> findByFilter(final T filter)
			throws SerigyDataSourceException {

		if (filter == null) {
			throw new IllegalArgumentException(SerigyConstant.MSG_NULL_FILTER);
		}

		List<T> result = new ArrayList<>();

		ps = null;
		rs = null;
		session = CassandraDatabase.getSession();

		try {
			ps = session.prepare(getfindByFilterSQL(filter));

			BoundStatement boundStatement = new BoundStatement(ps);

			setFindByFilterParameters(boundStatement, filter);

			rs = session.execute(boundStatement);

			T object = null;

			for (Row row : rs) {
				object = mapRow(row);
				result.add(object);
			}

		} catch (NoHostAvailableException | QueryExecutionException
				| QueryValidationException | IllegalStateException e) {
			handleError(e);
		}

		return result;
	}

	/**
	 * Trata o <code>SQLException</code>, realizando rollback na conexão e
	 * tradução da mensagem de erro.
	 * 
	 * @param e
	 * @throws SerigyDataSourceException
	 */
	protected void handleError(Exception e) throws SerigyDataSourceException {
		String message = e.getMessage();
		throw new SerigyDataSourceException(message);
	}

	/**
	 * 
	 * @return
	 */
	protected abstract String getfindSQL();

	/**
	 * 
	 * @return
	 */
	protected abstract String getfindAllSQL();

	/**
	 * 
	 * @param filter
	 * @return
	 */
	protected abstract String getfindByFilterSQL(T filter);

	/**
	 * 
	 * @param ps
	 * @param key
	 * @return
	 * @throws SQLException
	 */
	protected abstract void setFindParameters(BoundStatement bs, final K key);

	/**
	 * 
	 * @param ps
	 * @param filter
	 * @return
	 * @throws SQLException
	 */
	protected abstract void setFindByFilterParameters(BoundStatement bs,
			final T filter);

}
