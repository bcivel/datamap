package com.redoute.datamap.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redoute.datamap.dao.IParameterDAO;
import com.redoute.datamap.service.IParameter;

/**
 * {@link IParameter} implementation that always find a new value for a
 * requested parameter. So there is no cache behaviour.
 * 
 * @author abourdon
 */
@Service
public class Parameter implements IParameter {

	/**
	 * Set of available parameter keys with their associated default values
	 * 
	 * @author abourdon
	 */
	public static enum Key {
		PicturePath("picture_path", System.getProperty("user.home") + "/.datamap/picture");

		private String name;
		private String defaultValue;

		private Key(String name, String defaultValue) {
			this.name = name;
			this.defaultValue = defaultValue;
		}

		public String getName() {
			return name;
		}

		public String getDefaultValue() {
			return defaultValue;
		}
	}

	/**
	 * Checks if the given parameter value is defined
	 * 
	 * @param value
	 *            the value to check
	 * @return <code>true</code> if the given parameter value is defines,
	 *         <code>false</code> otherwise
	 */
	private static boolean isValueDefined(String value) {
		return value != null && value.length() > 0;
	}

	/** The associated {@link IParameterDAO} */
	@Autowired
	private IParameterDAO parameterDAO;

	/**
	 * Gets the {@link String} value of the given parameter key
	 * 
	 * @param key
	 *            the parameter key to find
	 * @return the {@link String} value of the given parameter key or
	 *         <code>null</code> if key does not exist
	 */
	public String getString(String key) {
		return parameterDAO.getString(key);
	}

	@Override
	public String getPicturePath() {
		String picturePath = getString(Key.PicturePath.getName());
		return isValueDefined(picturePath) ? picturePath : Key.PicturePath.getDefaultValue();
	}
}
