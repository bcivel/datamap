package com.redoute.datamap.service.impl;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

import com.redoute.datamap.dao.IParameterDAO;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = { "/applicationContextTest.xml" })
public class ParameterTest {

	@Mock
	private IParameterDAO parameterDAO;

	@InjectMocks
	private Parameter parameter;

	@Test
	public void testGetPicturePath() {
		String expected = "foo";
		Mockito.when(parameterDAO.getString(Parameter.Key.PicturePath.getName())).thenReturn(expected);

		Assert.assertEquals(expected, parameter.getPicturePath());
	}

	@Test
	public void testGetPicturePathWithEmptyDatabaseValue() {
		String expected = Parameter.Key.PicturePath.getDefaultValue();
		Mockito.when(parameterDAO.getString(Parameter.Key.PicturePath.getName())).thenReturn("");

		Assert.assertEquals(expected, parameter.getPicturePath());
	}

	@Test
	public void testGetPicturePathWithNullDatabaseValue() {
		String expected = Parameter.Key.PicturePath.getDefaultValue();
		Mockito.when(parameterDAO.getString(Parameter.Key.PicturePath.getName())).thenReturn(null);

		Assert.assertEquals(expected, parameter.getPicturePath());
	}

}
