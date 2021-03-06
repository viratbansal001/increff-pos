package com.increff.employee.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringUtilTest {
	// test String Util
	@Test
	public void testStringUtil() {
		StringUtil s = new StringUtil();
		s.getClass();
		String t = StringUtil.toLowerCase(null);
		// lower case of null string is null
		assertEquals(null, t);
		t = StringUtil.toLowerCase(" VirAm  ");
		// trimmed and lower case testing
		assertEquals("viram", t);
		t = StringUtil.getAlphaNumericString();
		// length should be 8
		assertEquals(8, t.length());
	}

}
