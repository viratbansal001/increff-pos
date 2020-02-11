package com.increff.employee.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.employee.spring.AbstractUnitTest;

public class InfoDataTest extends AbstractUnitTest {

	@Test
	public void testInfoData() {
		InfoData i = new InfoData();
		String email = "shahviram308@gmail.com";
		String message = "valid";
		i.setEmail(email);
		i.setMessage(message);
		assertEquals(email, i.getEmail());
		assertEquals(message, i.getMessage());

	}

}
