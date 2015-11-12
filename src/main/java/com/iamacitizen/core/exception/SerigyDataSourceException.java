package com.iamacitizen.core.exception;

/**
 * Classe que representa uma exceção da camada de fonte de dados. Exceções de fonte de dados podem apenas ser
 * lançadas a partir da camada Data Source (i.e. classes Mapper e Finder).
 * @author felipe
 */
public class SerigyDataSourceException extends SerigyException {

    public SerigyDataSourceException(String message) {
        super(message);
    }
}
