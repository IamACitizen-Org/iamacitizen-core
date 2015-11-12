package com.iamacitizen.core.exception;

/**
 * Classe que representa uma exceção do framework Serigy.
 * @author Felipe Cardoso
 */
@SuppressWarnings("serial")
public class SerigyException extends RuntimeException {

    public SerigyException(String message) {
        super(message);
    }
}
