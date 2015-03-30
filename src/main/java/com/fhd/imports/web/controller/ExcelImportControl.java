package com.fhd.imports.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.imports.business.ExcelImportBO;

/**
 * excel导入control
 * @author 邓广义
 * @date 2013-6-17
 * @since  fhd　Ver 1.1
 */
@Controller
public class ExcelImportControl {
	
	@Autowired
	private ExcelImportBO o_excelImportBO;
	/**
	 * excel导入的方法入口 
	 * @throws  
	 */
	@ResponseBody
	@RequestMapping("/imports/doimport.f")
	public void doImport(HttpServletRequest req,String xmlpath) {
		try {
			this.o_excelImportBO.doImport(req,xmlpath);
		} catch (IOException e) {
		}
	}
}
