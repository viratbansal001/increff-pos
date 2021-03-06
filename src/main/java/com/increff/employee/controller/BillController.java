package com.increff.employee.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.increff.employee.util.IOUtil;

@Controller
public class BillController {

	// Spring ignores . (dot) in the path. So we need fileName:.+
	@RequestMapping(value = "/api/bill/{fileName:.+}", method = RequestMethod.GET)
	public void getBillFile(@PathVariable("fileName") String fileName, HttpServletResponse response)
			throws IOException {
		// get your file as InputStream
		response.setContentType("application/pdf");
		response.addHeader("Content-disposition:", "attachment; filename=" + fileName);
		String fileClasspath = "/com/increff/employee/" + fileName;
		System.out.println(fileClasspath);
		InputStream is = BillController.class.getResourceAsStream(fileClasspath);
		// copy it to response's OutputStream
		try {
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			throw e;
		} finally {
			IOUtil.closeQuietly(is);
		}

	}

}
