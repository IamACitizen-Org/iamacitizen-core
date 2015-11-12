package com.iamacitizen.core.model.database;

/**
 * Classe que representa uma constraint de um SGBD.
 *
 * @author felipe
 */
public class Constraint {

    private String owner;
    private String name;
    private String table;
    private String indexName;
    private String indexedColumn;
    private ConstraintType type;
    private Constraint ref;

    public Constraint() {
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getTable() {
        return table;
    }

    public Constraint getRef() {
        return ref;
    }

    public String getIndexName() {
        return indexName;
    }

    public String getIndexedColumn() {
        return indexedColumn;
    }
    
    public ConstraintType getType() {
        return type;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public void setRef(Constraint ref) {
        this.ref = ref;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }
    
    public void setType(ConstraintType type) {
        this.type = type;
    }

    public void setIndexedColumn(String indexedColumn) {
        this.indexedColumn = indexedColumn;
    }
    
}
