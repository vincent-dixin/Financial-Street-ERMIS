package com.fhd.imports.interfaces;

import java.io.IOException;
import java.io.InputStream;

/**
 * 导入接口
 * @author 邓广义
 * @date 2013-6-17
 * @since  fhd　Ver 1.1
 */
public interface IExcelImportBO {
	
	/**
	 * 获得每个sheet页对象的方法
	 * @return
	 * @throws IOException 
	 */
	public Object createWorkBook(String path,InputStream excelfile) throws IOException;
	/**
	 * 获得xml对象的方法
	 * @return
	 */
	public Object createXMLObject(String xmlName);
}
