package com.increff.employee.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.fop.apps.FOPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.BillData;
import com.increff.employee.model.OrderData;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.OrderItemService;
import com.increff.employee.service.OrderService;
import com.increff.employee.util.GeneratePDF;
import com.increff.employee.util.GenerateXML;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderApiController {

	@Autowired
	private OrderService oService;
	@Autowired
	private OrderItemService iService;

	// CRUD operations for customer order

	@Transactional(rollbackOn = ApiException.class)
	@ApiOperation(value = "Adds Order")
	@RequestMapping(path = "/api/order", method = RequestMethod.POST)
	public void add(@RequestBody OrderItemForm[] orderItemForms, HttpServletResponse response)
			throws ApiException, ParserConfigurationException, TransformerException, FOPException, IOException {
		// Check entered inventory with available inventory
		List<OrderItemForm> orderItems = oService.groupItemsByBarcode(orderItemForms);
		oService.checkInventory(orderItems);
		OrderPojo op = new OrderPojo();
		op.setDatetime(getDateTime());
		// Add order if inventory is available
		oService.add(op);
		// Convert input to required format
		List<OrderItemPojo> list = oService.getOrderItemObject(orderItems, op);
		// Decrease inventory according to the entered quantity
		oService.updateInventory(list);
		// Add each order item
		iService.add(list);

		// Convert OrderItemPojo to BillData
		List<BillData> billItemList = oService.getBillDataObject(list);
		// Generate XML file using BillData list
		GenerateXML.createXml(billItemList, op.getId());
		// Create PDF from generated XML
		byte[] encodedBytes = GeneratePDF.createPDF();
		// Create response
		response = createResponse(response, encodedBytes);
		// send to output stream
		ServletOutputStream servletOutputStream = response.getOutputStream();
		servletOutputStream.write(encodedBytes);
		servletOutputStream.flush();
		servletOutputStream.close();
	}

	private HttpServletResponse createResponse(HttpServletResponse response, byte[] encodedBytes) {
		String pdfFileName = "output.pdf";
		response.reset();
		response.addHeader("Pragma", "public");
		response.addHeader("Cache-Control", "max-age=0");
		response.setHeader("Content-disposition", "attachment;filename=" + pdfFileName);
		response.setContentType("application/pdf");

		// avoid "byte shaving" by specifying precise length of transferred data
		response.setContentLength(encodedBytes.length);
		return response;
	}

	@ApiOperation(value = "Deletes Order")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		oService.delete(id);
	}

	@ApiOperation(value = "Gets a Order")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.GET)
	public OrderData get(@PathVariable int id) throws ApiException {
		OrderPojo p = oService.get(id);
		return convert(p);
	}

	@ApiOperation(value = "Gets list of all Orders")
	@RequestMapping(path = "/api/order", method = RequestMethod.GET)
	public List<OrderData> getAll() {
		List<OrderPojo> list = oService.getAll();
		List<OrderData> list2 = new ArrayList<OrderData>();
		for (OrderPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	private static OrderData convert(OrderPojo o) {
		OrderData d = new OrderData();
		d.setId(o.getId());
		d.setDatetime(o.getDatetime());
		return d;
	}

	// Returns date time in required format
	private String getDateTime() {

		DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Date dateobj = new Date();
		String datetime = df.format(dateobj);
		return datetime;
	}
}
