package com.iamacitizen.core.util;

/**
 *
 * @author felipe
 */
public class Assert {

	public static void notNull(Object object) {
		if (object == null) {
			throw new NullPointerException("Objeto da classe " + object.getClass().getName() + "não pode ser nulo");
		}
	}

	public static void isNull(Object object) {
		if (object != null) {
			throw new IllegalArgumentException("Objeto da classe " + object.getClass() + " deve ser nulo");
		}
	}

	public static void isTrue(boolean condition) {
		if (!condition) {
			throw new IllegalArgumentException("Condicional com valor falso");
		}
	}
}
