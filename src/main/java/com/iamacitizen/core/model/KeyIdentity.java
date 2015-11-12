package com.iamacitizen.core.model;

/**
 * Classe que abstrai um atributo-chave (i.e identificador) de um objeto.
 * @author felipe
 * @param <T> tido do atributo-chave.
 */
public final class KeyIdentity<T> {

    private T value;

    public KeyIdentity() {
    }

    public KeyIdentity(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Identificador não pode ser nulo.");
        }
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Identificador não pode ser nulo.");
        }
        this.value = value;
    }
}
