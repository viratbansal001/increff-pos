package com.increff.employee.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.spring.AbstractUnitTest;

public class AboutAppServiceTest extends AbstractUnitTest {

	@Autowired
	private AboutAppService service;

	@Test
	public void testAboutAppService() {
		assertEquals("PoS Application", service.getName());
		assertEquals("1.0", service.getVersion());
	}

}