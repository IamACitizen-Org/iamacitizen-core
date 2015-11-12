package com.iamacitizen.core.model.database;

public enum ConstraintType {

    CHECK("C"),
    PRIMARY_KEY("P"),
    REFERENTIAL_INTEGRITY("R"),
    UNIQUE("U");
    private String id;

    private ConstraintType(String id) {
        this.id = id;
    }

    public static ConstraintType fromId(String value) {
        ConstraintType result = null;
        for (ConstraintType e : values()) {
            if (e.id.equals(value)) {
                return e;
            }
        }
        return result;
    }
}
