package com.redoute.datamap.dao;

import java.util.List;

/**
 * DatamapLocationType DAO
 * 
 * @author abourdon
 */
public interface IDatamapLocationTypeDAO {

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
