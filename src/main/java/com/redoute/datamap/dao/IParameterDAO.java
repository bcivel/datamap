package com.redoute.datamap.dao;

public interface IParameterDAO {

	/**
	 * Gets the {@link String} value of the given parameter key from the
	 * database
	 * 
	 * @param key
	 *            the parameter key to find
	 * @return the {@link String} value of the given parameter key or
	 *         <code>null</code> if key does not exist
	 */
	String getString(String key);

}
