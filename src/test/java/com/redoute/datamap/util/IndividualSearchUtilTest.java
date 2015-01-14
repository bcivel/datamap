package com.redoute.datamap.util;

import java.util.HashMap;

import junit.framework.Assert;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * {@link IndividualSearchUtil} test suite
 * 
 * @author abourdon
 */
public class IndividualSearchUtilTest {

	@Test
	public void testConstructIndividualSearchLineStringString() {
		String expected = "`foo` = 'bar'" + IndividualSearchUtil.AND_OPERATOR;
		String actual = IndividualSearchUtil.constructIndividualSearchLine("foo", "bar");
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testConstructIndividualSearchLineStringStringWithNullKey() {
		String expected = IndividualSearchUtil.LINE_EMPTY;
		String actual = IndividualSearchUtil.constructIndividualSearchLine(null, "bar");
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testConstructIndividualSearchLineStringStringWithNullValue() {
		String expected = IndividualSearchUtil.LINE_EMPTY;
		String actual = IndividualSearchUtil.constructIndividualSearchLine("foo", null);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testConstructIndividualSearchLineStringStringWithNullKeyAndNullValue() {
		String expected = IndividualSearchUtil.LINE_EMPTY;
		String actual = IndividualSearchUtil.constructIndividualSearchLine(null, null);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testValidateIndividualSearch() {
		String expected = "`foo` = 'bar'";
		String actual = IndividualSearchUtil.validateIndividualSearch(expected + IndividualSearchUtil.AND_OPERATOR);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testValidateIndividualSearchAgain() {
		String expected = "`foo` = 'bar'" + IndividualSearchUtil.AND_OPERATOR + "`plop` = 'plap'";
		String actual = IndividualSearchUtil.validateIndividualSearch(expected + IndividualSearchUtil.AND_OPERATOR);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testValidateIndividualSearchAndAgain() {
		String expected = "`foo` = 'bar'";
		String actual = IndividualSearchUtil.validateIndividualSearch(expected);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testValidateIndividualSearchAndSoOn() {
		String expected = "";
		String actual = IndividualSearchUtil.validateIndividualSearch(expected);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testValidateIndividualSearchWithNullParameter() {
		String expected = null;
		String actual = IndividualSearchUtil.validateIndividualSearch(null);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testConstructIndividualSearch() {
		String expected = "`foo` = 'bar'";
		@SuppressWarnings("serial")
		String actual = IndividualSearchUtil.constructIndividualSearch(new HashMap<String, String>() {
			{
				put("foo", "bar");
			}
		});
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testConstructIndividualSearchAgain() {
		String expected1 = "`foo` = 'bar'" + IndividualSearchUtil.AND_OPERATOR + "`plop` = 'plap'";
		String expected2 = "`plop` = 'plap'" + IndividualSearchUtil.AND_OPERATOR + "`foo` = 'bar'";
		@SuppressWarnings("serial")
		String actual = IndividualSearchUtil.constructIndividualSearch(new HashMap<String, String>() {
			{
				put("foo", "bar");
				put("plop", "plap");
			}
		});
		MatcherAssert.assertThat(actual, Matchers.isOneOf(expected1, expected2));
	}

	@Test
	public void testConstructIndividualSearchWithNullParameter() {
		String expected = null;
		String actual = IndividualSearchUtil.constructIndividualSearch(null);
		Assert.assertEquals(expected, actual);
	}

}
