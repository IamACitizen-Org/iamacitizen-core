package com.iamacitizen.core.datasource.query.cassandra;

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
import com.iamacitizen.core.database.cassandra.CassandraDatabase;
import com.iamacitizen.core.exception.SerigyDataSourceException;
import com.iamacitizen.core.model.DomainObject;

/**
 * Classe que representa uma abstração de uma consulta a um banco de dados
 * relacional.
 * 
 * @author felipe
 */
public abstract class CassandraQuery<T extends DomainObject<?>> {

	protected abstract String prepareCQL();

	protected abstract void prepareParameters(BoundStatement bs);

	protected abstract T mapRow(Row row);

	/**
	 * Executa a consulta SQL.
	 * 
	 * @return a lista de objetos do domínio.
	 */
	public List<T> execute() throws SerigyDataSourceException {
		List<T> result = new ArrayList<>();

		try {
			String CQL = prepareCQL();

			Session session = CassandraDatabase.getSession();

			PreparedStatement ps = session.prepare(CQL);
			BoundStatement bs = new BoundStatement(ps);
			prepareParameters(bs);

			ResultSet rs = session.execute(bs);

			for (Row row : rs) {
				T object = mapRow(row);
				result.add(object);
			}

			return result;

		} catch (NoHostAvailableException | QueryExecutionException
				| QueryValidationException | IllegalStateException ex) {
			String message = ex.getMessage();
			throw new SerigyDataSourceException(message);
		}
	}
}
