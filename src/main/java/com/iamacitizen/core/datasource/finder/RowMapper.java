package com.iamacitizen.core.datasource.finder;

import com.iamacitizen.core.model.DomainObject;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface utilizada para mapear um ResultSet em um objeto do domínio.
 * @author felipe
 * @param <T> Uma subclasse de @link{DomainObjetct}
 * @param <K> Atributo-chave da classe do domínio.
 */
public interface RowMapper<T extends DomainObject<K>, K> {

    T mapRow(ResultSet rs) throws SQLException;
}
