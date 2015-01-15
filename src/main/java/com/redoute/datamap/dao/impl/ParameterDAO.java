package com.redoute.datamap.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.redoute.datamap.dao.IParameterDAO;
import com.redoute.datamap.database.DatabaseSpring;
import com.redoute.datamap.util.DAOUtil;

@Repository
public class ParameterDAO implements IParameterDAO {

	/** The associated {@link Logger} to this class */
	private static final Logger LOG = Logger.getLogger(ParameterDAO.class);

	public static final String TABLE = "parameter";

	/**
	 * Set of column to use from the table
	 * 
	 * @author abourdon
	 */
	public static enum COLUMN {
		Key("key"), Value("value");

		private String name;

		private COLUMN(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	@Autowired
	private DatabaseSpring databaseSpring;

	@Override
	public String getString(String key) {
		// Prepare resources
		Connection connection = databaseSpring.connect();
		String query = String.format("SELECT `%s` FROM `%s` WHERE `%s` = ?", COLUMN.Value.getName(), TABLE, COLUMN.Key.getName());
		PreparedStatement preStat = null;
		ResultSet resultSet = null;

		// Execute query
		try {
			preStat = connection.prepareStatement(query);
			preStat.setString(1, key);
			resultSet = preStat.executeQuery();
			return resultSet.first() ? resultSet.getString(COLUMN.Value.getName()) : null;
		} catch (SQLException e) {
			LOG.error("An error occurred when finding the parameters key " + key, e);
		} finally {
			DAOUtil.closeResources(resultSet, preStat, connection);
		}
		return null;
	}
}
