package com.iamacitizen.core.model;

/**
 * Abstração que representa um objeto do domínio (i.e uma entidade do domain model).
 * @author felipe
 * @param <K> Classe do atributo-chave do objeto, por exemplo, Integer.
 */
public abstract class DomainObject<K> {

    protected KeyIdentity<K> key;

    /**
     * 
     * @return o objeto do tipo KeyIdentity, que representa o atributo-chave do DomainObject
     */
    public KeyIdentity<K> getKey() {
        return key;
    }

    /**
     * 
     * @param id
     */
    public void setKey(KeyIdentity<K> id) {
        if (id == null) {
            throw new IllegalArgumentException("Identificador não pode ser nulo.");
        }
        this.key = id;
    }
}
