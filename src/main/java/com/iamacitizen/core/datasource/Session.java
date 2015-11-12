package com.iamacitizen.core.datasource;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author felipe
 */
public interface Session {

	void beginTransaction();

	void commit();

	PreparedStatement prepareStatement(String SQL);

	String handleSQLError(SQLException e);

	long getIdentityNextVal(String sequenceName);

	long getIdentityNextVal(Class domainObjectClass);
}
