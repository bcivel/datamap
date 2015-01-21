package com.redoute.datamap.util;

import junit.framework.Assert;

import org.junit.Test;

/**
 * {@link StringUtil} test suite
 * 
 * @author abourdon
 */
public class StringUtilTest {

	@Test
	public void testIsEmptyWhenEmpty() {
		Assert.assertTrue(StringUtil.isEmpty(""));
	}

	@Test
	public void testIsEmptyWhenNull() {
		Assert.assertTrue(StringUtil.isEmpty(null));
	}

	@Test
	public void testIsEmptyWhenNotEmpty() {
		Assert.assertFalse(StringUtil.isEmpty("foo"));
	}

}
