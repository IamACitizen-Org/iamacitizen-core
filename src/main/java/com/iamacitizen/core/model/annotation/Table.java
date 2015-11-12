package com.iamacitizen.core.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação que descreve uma classe a qual representa uma tabela no modelo relacional.
 * @author felipe
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface Table {

    String name();

    String alias();

    String sequenceName() default "NONE";
}
