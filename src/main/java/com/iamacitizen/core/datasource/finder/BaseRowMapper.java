package com.iamacitizen.core.datasource.finder;

import com.iamacitizen.core.model.DomainObject;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Abstração para um RowMapper de uma classe Abstrata. 
 * @author felipe
 */
public abstract class BaseRowMapper<T extends DomainObject<K>, K> {

	public abstract T mapRow(ResultSet rs, T t) throws SQLException;
}
