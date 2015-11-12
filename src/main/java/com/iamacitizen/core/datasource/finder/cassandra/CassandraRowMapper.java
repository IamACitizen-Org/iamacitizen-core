package com.iamacitizen.core.datasource.finder.cassandra;

import com.datastax.driver.core.Row;
import com.iamacitizen.core.model.DomainObject;

public interface CassandraRowMapper<T extends DomainObject<K>, K> {

    T mapRow(Row row);

}
