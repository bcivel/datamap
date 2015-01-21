package com.redoute.datamap.util;

/**
 * Set of utility method for the {@link String} class
 * 
 * @author abourdon
 */
public final class StringUtil {

	/**
	 * Checks if the given {@link String} value is empty
	 * 
	 * @param value
	 *            the {@link String} value to check
	 * @return <code>true</code> if the given value is empty, <code>false</code>
	 *         otherwise
	 */
	public static boolean isEmpty(String value) {
		return value == null || value.equals("");
	}

}
