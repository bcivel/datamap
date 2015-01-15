package com.redoute.datamap.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

import com.redoute.datamap.database.DatabaseSpring;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = { "/applicationContextTest.xml" })
public class ParameterDAOTest {

	@Mock
	private ResultSet resultSet;

	@Mock
	private PreparedStatement preparedStatement;

	@Mock
	private Connection connection;

	@Mock
	private DatabaseSpring databaseSpring;

	@InjectMocks
	private ParameterDAO parameterDAO;

	private void prepareGetString(String key) throws SQLException {
		String query = String.format("SELECT `%s` FROM `%s` WHERE `%s` = ?", ParameterDAO.COLUMN.Value.getName(), ParameterDAO.TABLE, ParameterDAO.COLUMN.Key.getName(), key);
		Mockito.when(databaseSpring.connect()).thenReturn(connection);
		Mockito.when(connection.prepareStatement(query)).thenReturn(preparedStatement);
		Mockito.doNothing().when(preparedStatement).setString(1, key);
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
	}

	@Test
	public void testGetString() throws SQLException {
		String expected = "bar";
		prepareGetString("foo");
		Mockito.when(resultSet.first()).thenReturn(true);
		Mockito.when(resultSet.getString(ParameterDAO.COLUMN.Value.getName())).thenReturn(expected);

		Assert.assertEquals(expected, parameterDAO.getString("foo"));
	}

	@Test
	public void testGetStringWhenMissingParameter() throws SQLException {
		String expected = null;
		prepareGetString("foo");
		Mockito.when(resultSet.first()).thenReturn(false);

		Assert.assertEquals(expected, parameterDAO.getString("foo"));
	}

}
