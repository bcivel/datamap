package com.redoute.datamap.service;

import java.util.List;

/**
 * Services to handle datamap location types
 * 
 * @author abourdon
 */
public interface IDatamapLocationTypeService {

	/**
	 * Finds distinct values from the given column name
	 * 
	 * <p>
	 * Values are sorted alphabetically
	 * </p>
	 * 
	 * @param colName
	 *            A column name from the datamap_location_type table
	 * @return List of distinct properties of the given column name
	 */
	List<String> findDistinctValuesfromColumn(String colName);

}
