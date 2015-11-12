package com.iamacitizen.core.database.postgres;

import com.iamacitizen.core.database.AbstractDatabase;
import com.iamacitizen.core.exception.SerigyDatabaseException;
import com.iamacitizen.core.model.database.Constraint;
import com.iamacitizen.core.model.database.ConstraintType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe..: PostgresDatabase Objetivo:
 *
 * @author Felipe Cardoso
 */
public class PostgresDatabase extends AbstractDatabase {

	public PostgresDatabase() {
		super();
	}
	private static String CONSTRAINT_SQL = "SELECT UC1.TABLE_NAME, UC1.CONSTRAINT_TYPE AS CT1, UC1.R_CONSTRAINT_NAME, "
			+ "UC2.TABLE_NAME, UC2.CONSTRAINT_TYPE AS CT2, UC1.INDEX_NAME FROM ALL_CONSTRAINTS UC1 "
			+ "LEFT JOIN ALL_CONSTRAINTS UC2 ON (UC1.R_CONSTRAINT_NAME = UC2.CONSTRAINT_NAME) "
			+ "WHERE UPPER(UC1.OWNER) = ? AND UPPER(UC1.CONSTRAINT_NAME) = ?";
	private static String INDEX_SQL = "SELECT COLUMN_NAME FROM all_ind_columns WHERE INDEX_NAME = ?";

	@Override
	public Constraint getConstraint(String owner, String name) {
		Constraint result = null;
		Connection conn = createConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(CONSTRAINT_SQL);
			ps.setString(1, owner.toUpperCase());
			ps.setString(2, name.toUpperCase());
			rs = ps.executeQuery();

			if (rs.next()) {
				result = new Constraint();
				result.setOwner(owner.toUpperCase());
				result.setName(name.toUpperCase());
				result.setTable(rs.getString("TABLE_NAME"));
				ConstraintType ct = ConstraintType.fromId(rs.getString("CT1"));
				result.setType(ct);
				if (ct.equals(ConstraintType.REFERENTIAL_INTEGRITY)) {
					Constraint ref = new Constraint();
					ref.setName(rs.getString("R_CONSTRAINT_NAME"));
					ref.setOwner(owner.toUpperCase());
					ref.setTable(rs.getString("TABLE_NAME"));
					ref.setType(ConstraintType.fromId(rs.getString("CT2")));
					result.setRef(ref);
				} else if (ct.equals(ConstraintType.UNIQUE)) {
					String indexName = rs.getString("INDEX_NAME");
					result.setIndexName(indexName);
					ps = conn.prepareStatement(INDEX_SQL);
					ps.setString(1, indexName);
					rs = ps.executeQuery();
					if (rs.next()) {
						result.setIndexedColumn(rs.getString("COLUMN_NAME"));
					}
				}
			}
		} catch (SQLException e) {
			throw new SerigyDatabaseException(e.getMessage());
		} finally {
			cleanUp(rs, ps, conn);
		}
		return result;
	}

	@Override
	public long getIdentityNextVal(String sequenceName) {
		StringBuilder SQL = new StringBuilder();
		SQL.append("SELECT NEXTVAL('").append(sequenceName).append("') AS ID;");
		return super.generateIdentity(SQL.toString());
	}

	@Override
	public long mapSequenceValue(ResultSet rs) throws SQLException {
		return rs.getLong(1);
	}

	@Override
	public String handleSQLError(SQLException e) throws SerigyDatabaseException {
		return new PostgresSQLErrorMapper(this).SQLErrorHandler(e);
	}
}