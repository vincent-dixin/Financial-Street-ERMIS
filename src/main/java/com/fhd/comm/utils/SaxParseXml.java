package com.fhd.comm.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

/**
 * sax解析xml
 * @author 吴德福
 * @since 2013-4-17 14:47
 */
public class SaxParseXml {

	/**
	 * 获取测试报告模板内容.
	 * @param request
	 * @return String
	 * @throws FileNotFoundException
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static String findReportContentsByXml(HttpServletRequest request) throws FileNotFoundException, JDOMException, IOException{
		String reportContents = "";
		
		//String fileName = "D:/workspace/fhd-fdc_4_5/src/main/webapp/app/view/comm/report/assess/TestReportTpl.xml";
		//配置文件读取文件路径
		//String fileName = ResourceBundle.getBundle("application").getString("testReport");
		
		String rootPath = request.getServletContext().getRealPath("/") + "app\\view\\comm\\report\\assess\\TestReportTpl.xml";
		
		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build(new InputSource(new FileReader(rootPath)));
		
		//SaxReader reader = new SaxReader();
		//Document doc = reader.read(new File("class.xml"));
		//取出整个根
		Element root = document.getRootElement();
		Content content = root.getContent(1);
		reportContents = content.getValue();
		
		return reportContents;
	}
	/**
	 * 获取测试报告模板内容.
	 * @param request
	 * @return String
	 * @throws FileNotFoundException
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static String findReportContentsByXml(HttpServletRequest request,String path) throws FileNotFoundException, JDOMException, IOException{
		String reportContents = "";
		
		//String fileName = "D:/workspace/fhd-fdc_4_5/src/main/webapp/app/view/comm/report/assess/TestReportTpl.xml";
		//配置文件读取文件路径
		//String fileName = ResourceBundle.getBundle("application").getString("testReport");
		
		String rootPath = request.getServletContext().getRealPath("/") +  path;//"app\\view\\comm\\report\\assess\\TestReportTpl.xml";
		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build(new InputSource(new FileReader(rootPath)));
		
		//SaxReader reader = new SaxReader();
		//Document doc = reader.read(new File("class.xml"));
		//取出整个根
		Element root = document.getRootElement();
		Content content = root.getContent(1);
		reportContents = content.getValue();
		
		return reportContents;
	}
}
