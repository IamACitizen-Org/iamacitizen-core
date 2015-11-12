package com.iamacitizen.core.model.domain;

import com.iamacitizen.core.util.StringUtils;

/**
 * Enum com os valores S e N, correspondendo a Sim e Não, respectivamente.
 *
 * @author felipe
 */
public enum SimNaoEnum {

	SIM("S"), NÃO("N");
	private String id;

	private SimNaoEnum(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public static SimNaoEnum fromValue(String id) {
		for (SimNaoEnum e : SimNaoEnum.values()) {
			if (e.getId().equals(id)) {
				return e;
			}
		}

		return null;
	}

	@Override
	public String toString() {
		return StringUtils.initCap(super.toString());
	}
}
