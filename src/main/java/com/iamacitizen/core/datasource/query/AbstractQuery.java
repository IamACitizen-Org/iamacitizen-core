package com.iamacitizen.core.datasource.query;

import com.iamacitizen.core.datasource.Session;
import com.iamacitizen.core.datasource.SessionManager;
import com.iamacitizen.core.exception.SerigyDataSourceException;
import com.iamacitizen.core.model.DomainObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma abstração de uma consulta a um banco de dados relacional.
 *
 * @author felipe
 */
public abstract class AbstractQuery<T extends DomainObject> {

	private DomainObject filter;
	
	public AbstractQuery(DomainObject filter) {
		this.filter = filter;
	}

	protected abstract String prepareSQL();

	protected abstract void prepareParameters(PreparedStatement ps);
	
	protected abstract T mapRow(ResultSet rs) throws SQLException;

	/**
	 * Executa a consulta SQL.
	 *
	 * @return a lista de objetos do domínio.
	 */
	public List<T> execute() throws SerigyDataSourceException {
		List<T> result = new ArrayList<>();

		try {
			String SQL = prepareSQL();

			Session session = SessionManager.getActiveSession();

			PreparedStatement ps = session.prepareStatement(SQL);
			prepareParameters(ps);

			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				T object = mapRow(rs);
				
				result.add(object);
			}
			
			return result;
		
		} catch (SQLException ex) {
			String message = ex.getMessage();
			SessionManager.rollbackSession();

			throw new SerigyDataSourceException(message);
		}
	}
}
