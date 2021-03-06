package com.increff.employee.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.pojo.BrandMasterPojo;
import com.increff.employee.spring.AbstractUnitTest;

public class BrandServiceTest extends AbstractUnitTest {
	@Autowired
	private BrandService service;

	// test brand service
	@Test(expected = ApiException.class)
	public void testAdd() throws ApiException {
		BrandMasterPojo b = getBrandMasterPojoTest();
		// Add one time
		service.add(b);
		// Throw exception while entering second time
		service.add(b);
	}

	@Test
	public void testDelete() throws ApiException {
		BrandMasterPojo b = getBrandMasterPojoTest();
		// Add data
		service.add(b);
		int id = service.getId(b.getBrand(), b.getCategory());
		assertEquals(id, b.getId());
		// Delete should be successful and should not throw exception as data exists
		service.delete(id);
	}

	@Test(expected = ApiException.class)
	public void testGetId() throws ApiException {
		BrandMasterPojo b = getBrandMasterPojoTest();
		service.add(b);
		// select data for given brand and category
		int id = service.getId(b.getBrand(), b.getCategory());
		assertEquals(id, b.getId());
		service.delete(id);
		// After delete throw exception while getting data
		service.getId(b.getBrand(), b.getCategory());
	}

	@Test
	public void testGet() throws ApiException {
		BrandMasterPojo b = getBrandMasterPojoTest();
		service.add(b);
		BrandMasterPojo p = service.get(b.getId());
		// check for inserted data
		assertEquals("viram", p.getBrand());
		assertEquals("shah", p.getCategory());
	}

	@Test
	public void testGetAll() throws ApiException {
		// test select all
		service.getAll();
	}

	@Test
	public void testUpdate() throws ApiException {
		BrandMasterPojo b = getBrandMasterPojoTest();
		service.add(b);
		BrandMasterPojo p = service.get(b.getId());
		p.setBrand("increff");
		p.setCategory("pos");
		service.update(p.getId(), p);
		BrandMasterPojo m = service.get(p.getId());
		// test updated data
		assertEquals("increff", m.getBrand());
		assertEquals("pos", m.getCategory());
	}

	@Test(expected = ApiException.class)
	public void testGetCheck() throws ApiException {
		BrandMasterPojo b = getBrandMasterPojoTest();
		service.add(b);
		// test getCheck function
		BrandMasterPojo p = service.getCheck(b.getId());
		service.delete(p.getId());
		// After delete throw exception while getting data
		service.getCheck(p.getId());

	}

	@Test
	public void testNormalize() throws ApiException {
		BrandMasterPojo b = getBrandMasterPojoTest();
		BrandService.normalize(b);
		// test for normalized data
		assertEquals("viram", b.getBrand());
		assertEquals("shah", b.getCategory());

	}
	@Test(expected = ApiException.class)
	public void testCheckData() throws ApiException {
		BrandMasterPojo b = getBrandMasterPojoTest();
		service.checkData(b);
		// throw exception
		b.setBrand("");
		service.checkData(b);
	}
	public BrandMasterPojo getBrandMasterPojoTest() throws ApiException {
		BrandMasterPojo b = new BrandMasterPojo();
		// create data
		b.setBrand(" viram ");
		b.setCategory("ShaH");
		return b;
	}
}
