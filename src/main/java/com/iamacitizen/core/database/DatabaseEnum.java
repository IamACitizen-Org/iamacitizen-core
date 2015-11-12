package com.iamacitizen.core.database;

/**
 *
 * @author felipe
 */
enum DatabaseEnum {

	ORACLE("oracle"), POSTGRES("postgres");
	private String id;

	private DatabaseEnum(String id) {
		this.id = id;
	}

	public static DatabaseEnum fromValue(String value) {

		for (DatabaseEnum e : DatabaseEnum.values()) {
			if (e.id.equals(value)) {
				return e;
			}
		}

		return null;
	}
}
