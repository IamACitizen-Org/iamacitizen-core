package com.iamacitizen.core.model.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Classe que serve como um descritor das anotações das classes filhas de
 * DomainObject.
 *
 * @author felipe
 */
public class AnnotationDescriptor {

    private Class c;

    public AnnotationDescriptor(Class c) {
        this.c = c;
    }

    public String getTableAlias() {
        Table annotation = (Table) c.getAnnotation(Table.class);

        if (annotation != null) {
            return annotation.alias();
        }

        return null;
    }

    public String getTableName() {
        Table annotation = (Table) c.getAnnotation(Table.class);

        if (annotation != null) {
            return annotation.name();
        }

        return null;
    }

    public String getSequenceName() {
        Table annotation = (Table) c.getAnnotation(Table.class);

        if (annotation != null) {
            return annotation.sequenceName();
        }

        return null;
    }

    public String getPrimaryKeyAlias() {
        PK annotation = (PK) c.getAnnotation(PK.class);

        if (annotation != null) {
            return Arrays.toString(annotation.alias()).replace("[", "").replace("]", "");
        }

        return null;
    }

    public String getPrimaryKeyAlias(String keyAttribute) {
        PK annotation = (PK) c.getAnnotation(PK.class);
        int index = 0;

        for (String name : annotation.name()) {
            if (name.equals(keyAttribute)) {
                if (annotation.alias().length >= index + 1) {
                    return annotation.alias()[index];
                }
            }

            index++;
        }

        return null;
    }

    public String getForeignKeyAlias(String foreignAttribute) {
        FK annotation = (FK) c.getAnnotation(FK.class);
        int index = 0;

        for (String name : annotation.name()) {
            if (name.equals(foreignAttribute)) {
                if (annotation.alias().length >= index + 1) {
                    return annotation.alias()[index];
                }
            }

            index++;
        }

        return null;
    }

    public String getAliasFromColumnName(String columnName) {
        for (Field f : c.getDeclaredFields()) {
            for (Annotation annotation : f.getAnnotations()) {
                if (annotation instanceof Column) {
                    if (((Column) annotation).name().toUpperCase().equals(columnName.toUpperCase())) {
                        return ((Column) annotation).alias();
                    }
                }
            }
        }

        return null;
    }

    public boolean hasAnnotationName(String name) {
        boolean result = false;

        String tableName = getTableName();
        String primaryKeyAlias = getPrimaryKeyAlias();

        if (tableName != null && tableName.toUpperCase().equals(name.toUpperCase())) {
            return true;
        } else if (getAliasFromColumnName(name) != null) {
            return true;
        } else if (primaryKeyAlias != null && primaryKeyAlias.toUpperCase().equals(name.toUpperCase())) {
            return true;
        }

        return result;
    }
}
