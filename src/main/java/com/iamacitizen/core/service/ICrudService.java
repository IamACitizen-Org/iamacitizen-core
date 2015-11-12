package com.iamacitizen.core.service;

import com.iamacitizen.core.exception.SerigyBusinessException;
import com.iamacitizen.core.exception.SerigyDataSourceException;
import com.iamacitizen.core.model.DomainObject;

import java.util.List;

/**
 * Contrato que serviços do tipo CRUD podem implementar.
 *
 * @author felipe
 * @param <T> é uma classe do domínio que estende DomainObject (e.g. Cliente, Fornecedor, etc)
 * @param <K> é o tipo de dados do atributo identificador da classe do domínio (e.g. Long).
 */
public interface ICrudService<T extends DomainObject<K>, K> {

	T insert(T domainObject) throws SerigyBusinessException, SerigyDataSourceException;

	int update(T domainObject) throws SerigyBusinessException, SerigyDataSourceException;

	int delete(T domainObject) throws SerigyBusinessException, SerigyDataSourceException;

	T find(K key) throws SerigyBusinessException, SerigyDataSourceException;

	List<T> findAll() throws SerigyBusinessException, SerigyDataSourceException;

	List<T> findByFilter(T filter) throws SerigyBusinessException, SerigyDataSourceException;
}
