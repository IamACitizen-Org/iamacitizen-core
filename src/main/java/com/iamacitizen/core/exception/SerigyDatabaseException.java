package com.iamacitizen.core.exception;

/**
 * Classe que representa uma exceção de banco de dados.
 * @author felipe
 */
public class SerigyDatabaseException extends RuntimeException {

    public SerigyDatabaseException(String message) {
        super(message);
    }
}
