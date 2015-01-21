package com.redoute.datamap.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.redoute.datamap.dao.IDatamapLocationTypeDAO;
import com.redoute.datamap.database.DatabaseSpring;
import com.redoute.datamap.util.DAOUtil;

@Repository
public class DatamapLocationTypeDAO implements IDatamapLocationTypeDAO {

	/** Associated {@link Logger} to this class */
	private static final Logger LOG = Logger.getLogger(DatamapDAO.class);

	/** Datamap location type table name */
	private static final String TABLE_NAME = "datamap_location_type";

	@Autowired
	private DatabaseSpring databaseSpring;

	@Override
	public List<String> findDistinctValuesfromColumn(String colName) {
		Connection connection = null;
		PreparedStatement preStat = null;
		ResultSet resultSet = null;

		try {
			// Prepare and execute query
			String query = String.format("SELECT DISTINCT %s FROM %s ORDER BY %s ASC", colName, TABLE_NAME, colName);
			connection = databaseSpring.connect();
			preStat = connection.prepareStatement(query.toString());
			resultSet = preStat.executeQuery();

			// Retrieve and send result
			List<String> result = new ArrayList<String>();
			while (resultSet.next()) {
				result.add(resultSet.getString(colName) == null ? "" : resultSet.getString(colName));
			}
			return result;
		} catch (SQLException e) {
			LOG.error(e);
		} finally {
			DAOUtil.closeResources(resultSet, preStat, connection);
		}

		return null;
	}

}
