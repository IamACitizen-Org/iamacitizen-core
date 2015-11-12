package com.iamacitizen.core.exception;

/**
 * Classe que representa uma exceção de negócio. Exceções de negócio podem apenas ser lançadas
 * a partir do Domain Model (i.e. classe do pacote Model) ou a partir da camada de serviço 
 * (i.e. classe do pacote Service)
 * @author felipe
 */
@SuppressWarnings("serial")
public class SerigyBusinessException extends SerigyException {

    public SerigyBusinessException(String message) {
        super(message);
    }
}
