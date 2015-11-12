package com.iamacitizen.core.util;

/**
 *
 * @author felipe
 */
public class StringUtils {

	public static String initCap(String source) {

		source = source.toLowerCase();
		
		if (source != null && source.length() > 0) {
			char[] charArray = source.toCharArray();
			charArray[0] = Character.toUpperCase(charArray[0]);

			return new String(charArray);
		} else {
			return "";
		}
	}
}
