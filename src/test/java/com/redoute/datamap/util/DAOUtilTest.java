package com.redoute.datamap.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * {@link DAOUtil} test suite
 * 
 * @author abourdon
 */
public class DAOUtilTest {

	/**
	 * A class with a close method
	 * 
	 * @author abourdon
	 */
	public static class ClassWithCloseMethod {
		private boolean closeCalled = false;

		public void close() {
			closeCalled = true;
		}

		public boolean isCloseCalled() {
			return closeCalled;
		}
	}

	@Test
	public void testIsEmptyWhenEmpty() {
		Assert.assertTrue(DAOUtil.isEmpty(""));
	}

	@Test
	public void testIsEmptyWhenNull() {
		Assert.assertTrue(DAOUtil.isEmpty(null));
	}

	@Test
	public void testIsEmptyWhenNullValue() {
		Assert.assertTrue(DAOUtil.isEmpty(DAOUtil.NULL_VALUE));
	}

	@Test
	public void testIsEmptyWhenNotEmpty() {
		Assert.assertFalse(DAOUtil.isEmpty("foo"));
	}

	@Test
	public void testCloseResources() {
		ClassWithCloseMethod object = new ClassWithCloseMethod();
		Assert.assertFalse(object.isCloseCalled());
		DAOUtil.closeResources(object);
		Assert.assertTrue(object.isCloseCalled());
	}

}
