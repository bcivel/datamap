package com.redoute.datamap.util;

import java.util.Map;

/**
 * Set of utility methods to construct the individual search from all the
 * IXXXService#findDatamapListByCriteria(int, int, String, String, String,
 * String) methods
 * 
 * @author abourdon
 */
public final class IndividualSearchUtil {

	/** The associated format for an individual search line */
	public static final String LINE_FORMAT = "`%s` = '%s'";

	/** Value of an empty individual search line */
	public static final String LINE_EMPTY = "";

	/** The AND operator to link several individual seach lines */
	public static final String AND_OPERATOR = " AND ";

	/**
	 * Constructs an individual search based on the given {@link String} value
	 * parameters.
	 * 
	 * @param parameters
	 *            Map of {@link String} parameters
	 * @return the individual search based on the given {@link String} value
	 *         parameters or <code>null</code> in case of <code>null</code>
	 *         parameters
	 */
	public static String constructIndividualSearch(Map<String, String> parameters) {
		if (parameters == null) {
			return null;
		}

		StringBuilder individualSearch = new StringBuilder();
		for (Map.Entry<String, String> parameter : parameters.entrySet()) {
			individualSearch.append(constructIndividualSearchLine(parameter.getKey(), parameter.getValue()));
		}
		return validateIndividualSearch(individualSearch.toString());
	}

	/**
	 * Creates an individual search line based on the given key/value pair
	 * 
	 * @param key
	 *            the associated key
	 * @param value
	 *            the associated value
	 * @return an individual search line based on the given key/value pair
	 */
	public static String constructIndividualSearchLine(String key, String value) {
		if (key == null || key == "" || value == null || value.length() == 0) {
			return LINE_EMPTY;
		}

		StringBuilder line = new StringBuilder();
		line.append(String.format(LINE_FORMAT, key, value));
		line.append(AND_OPERATOR);
		return line.toString();
	}

	/**
	 * Formats the given individual search to be conform for the
	 * IXXXService#findDatamapListByCriteria(int, int, String, String, String,
	 * String) method
	 * 
	 * @param individualSearch
	 *            the individual search to be validated
	 * @return a valid individual search or <code>null</code> in case of
	 *         <code>null</code> parameter
	 */
	public static String validateIndividualSearch(String individualSearch) {
		if (individualSearch == null) {
			return null;
		}

		return individualSearch.endsWith(AND_OPERATOR) ? individualSearch.substring(0, individualSearch.length() - AND_OPERATOR.length()) : individualSearch;
	}

	/**
	 * Utility class then private constructor
	 */
	private IndividualSearchUtil() {
	}

}
