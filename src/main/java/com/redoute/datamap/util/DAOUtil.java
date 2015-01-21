package com.redoute.datamap.util;

import org.apache.log4j.Logger;

/**
 * Set of utility class for DAO classes
 * 
 * @author abourdon
 */
public final class DAOUtil {

	/** The associated {@link Logger} to this class */
	private static final Logger LOG = Logger.getLogger(DAOUtil.class);

	/** The <code>NULL</code> SQL value */
	public static final String NULL_VALUE = "NULL";

	/**
	 * Free the given resources by calling its <code>close()</code> method.
	 * 
	 * <p>
	 * Resources will be closed in the order they are declared into the
	 * <code>resources</code> array
	 * </p>
	 * 
	 * @param resources
	 *            Resources to close
	 */
	public static void closeResources(Object... resources) {
		for (Object resource : resources) {
			if (resource != null) {
				try {
					resource.getClass().getMethod("close").invoke(resource);
				}
				// We need to catch the global Exception class as we don't know
				// if and what kind of exception can be thrown by the current
				// close method
				catch (Exception e) {
					LOG.error("Unable to close resource due to " + e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * Checks if the given database {@link String} value is empty
	 * 
	 * @param value
	 *            the database {@link String} value to check
	 * @return <code>true</code> if the given value is empty, <code>false</code>
	 *         otherwise
	 */
	public static boolean isEmpty(String value) {
		return value == null || value.equals(NULL_VALUE) || value.equals("");
	}

	/**
	 * Utility class then private constructor
	 */
	private DAOUtil() {
	}

}
