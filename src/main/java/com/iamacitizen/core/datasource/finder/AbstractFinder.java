package com.iamacitizen.core.datasource.finder;

import com.iamacitizen.core.SerigyConstant;
import com.iamacitizen.core.datasource.Session;
import com.iamacitizen.core.datasource.SessionManager;
import com.iamacitizen.core.exception.SerigyDataSourceException;
import com.iamacitizen.core.model.DomainObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFinder<T extends DomainObject<K>, K> implements RowMapper<T, K> {

	protected PreparedStatement ps;
	protected ResultSet rs;
	protected Session session;
	private FetchMode fetchMode = FetchMode.NONE;

	public AbstractFinder() {
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
		session = SessionManager.getActiveSession();

		try {
			ps = session.prepareStatement(getfindSQL());
			setFindParameters(ps, key);
			rs = ps.executeQuery();
			if (rs.next()) {
				result = mapRow(rs);
			}
		} catch (SQLException e) {
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
		session = SessionManager.getActiveSession();

		try {
			ps = session.prepareStatement(getfindAllSQL());
			rs = ps.executeQuery();
			T object = null;

			while (rs.next()) {
				object = mapRow(rs);
				result.add(object);
			}
		} catch (SQLException e) {
			handleError(e);
		}

		return result;
	}

	/**
	 *
	 * @param filter Objeto representando o filtro da consulta.
	 * @return Lista dos objetos que satisfazem a consulta.
	 * @throws SerigyDataSourceException
	 */
	public List<T> findByFilter(final T filter) throws SerigyDataSourceException {

		if (filter == null) {
			throw new IllegalArgumentException(SerigyConstant.MSG_NULL_FILTER);
		}

		List<T> result = new ArrayList<>();

		ps = null;
		rs = null;
		session = SessionManager.getActiveSession();

		try {
			ps = session.prepareStatement(getfindByFilterSQL(filter));
			setFindByFilterParameters(ps, filter);
			rs = ps.executeQuery();
			T object = null;

			while (rs.next()) {
				object = mapRow(rs);
				result.add(object);
			}

		} catch (SQLException e) {
			handleError(e);
		}

		return result;
	}

	/**
	 * Trata o
	 * <code>SQLException</code>, realizando rollback na conexão e tradução da mensagem de erro.
	 *
	 * @param e
	 * @throws SerigyDataSourceException
	 */
	protected void handleError(SQLException e) throws SerigyDataSourceException {
		String message = e.getMessage();
		SessionManager.rollbackSession();

		throw new SerigyDataSourceException(message);
	}

	/**
	 * Retorna a opção de fetch do finder.
	 * @return 
	 */
	public FetchMode getFetchMode() {
		return fetchMode;
	}

	/**
	 * Adiciona a opção de fetch ao finder
	 * @param fetchMode 
	 */
	public void setFetchMode(FetchMode fetchMode) {
		this.fetchMode = fetchMode;
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
	protected abstract void setFindParameters(PreparedStatement ps, final K key) throws SQLException;

	/**
	 *
	 * @param ps
	 * @param filter
	 * @return
	 * @throws SQLException
	 */
	protected abstract void setFindByFilterParameters(PreparedStatement ps, final T filter) throws SQLException;
}
