package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.OrderDao;
import com.increff.employee.pojo.BrandMasterPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.util.StringUtil;

@Service
public class OrderService {

	@Autowired
	private OrderDao dao;

	@Transactional(rollbackOn = ApiException.class)
	public void add(OrderPojo o) throws ApiException {
		dao.insert(o);
	}

	@Transactional
	public void delete(int id) {
		dao.delete(id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public int getId(String brand, String category) throws ApiException {
		int id = dao.selectId(brand, category);
		if (id > 0) {
			return id;
		} else {
			throw new ApiException("Given Brand and Category pair dosen't exist");
		}
	}

	@Transactional(rollbackOn = ApiException.class)
	public BrandMasterPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<BrandMasterPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackOn = ApiException.class)
	public void update(int id, BrandMasterPojo p) throws ApiException {
		normalize(p);
		BrandMasterPojo ex = getCheck(id);
		ex.setCategory(p.getCategory());
		ex.setBrand(p.getBrand());
		dao.update(ex);
	}

	@Transactional
	public BrandMasterPojo getCheck(int id) throws ApiException {
		BrandMasterPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("Brand and Category not exist for id : " + id);
		}
		return p;
	}

	protected static void normalize(BrandMasterPojo p) {
		p.setBrand(StringUtil.toLowerCase(p.getBrand()));
		p.setCategory(StringUtil.toLowerCase(p.getCategory()));
	}

	public int getMax() {
		return dao.selectMax();
	}
}
