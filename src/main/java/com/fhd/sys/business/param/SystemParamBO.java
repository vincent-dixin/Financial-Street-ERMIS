package com.fhd.sys.business.param;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;
import java.util.Set;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

/**
 * 系统基本参数设置BO； 
 * ClassName:SystemParamBO
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 * @author   陈燕杰
 * @since    Ver 1.1
 * @Date	 2011	2011-3-24		下午05:38:39
 */
@Service
@SuppressWarnings({"rawtypes" })
public class SystemParamBO {
	/**
	 * 修改系统的属性值； 
	 * @author 陈燕杰
	 * @param keyArray:属性名列表，以逗号分隔；
	 * @param valueArray:修改后的属性值，以逗号分隔；
	 * @return String
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	 */
	public String updateSystemParam(String keyArray, String valueArray) throws IOException {
		String[] keys=keyArray.split(",");
		String[] values=valueArray.split(",");
		String result="false";
		Properties properties=new Properties();
		properties.load(SystemParamBO.class.getClassLoader().getResourceAsStream("application.properties"));
		for(int i=0;i<keys.length;i++)
		{
			properties.put(keys[i], values[i]);
		}
		URL url=SystemParamBO.class.getClassLoader().getResource("application.properties");
		String propFilePath=url.toString().substring(url.toString().indexOf("/")+1);
		OutputStream os=new FileOutputStream(propFilePath);
		properties.store(os, "");
		os.close();
		result="true";
		return result;
	}
	/**
	 * 读取配置文件，返回由其构成的JSONObject; 
	 * @author 陈燕杰
	 * @return JSONObject
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	 */
	
	@SuppressWarnings("deprecation")
	public JSONObject getPropJSON() throws IOException
	{
		JSONObject result=new JSONObject();
		Properties properties=new Properties();
		URL url=SystemParamBO.class.getClassLoader().getResource("application.properties");
		String propFilePath=url.toString().substring(url.toString().indexOf("/")+1);
		String path=URLDecoder.decode(propFilePath);
		File f=new File(path);
		InputStream fileInputStream=new FileInputStream(f);
		Reader reader=new InputStreamReader(fileInputStream);
		properties.load(reader);
//		properties.load(SystemParamBO.class.getClassLoader().getResourceAsStream("application.properties"));
		Set keys=properties.keySet();
		for(Object item:keys)
		{
			result.put(item.toString(), properties.get(item));
		}
		return result;
	}
	/**
	 * 根据配置文件中的文件名的key，得到文件； 
	 * @author 陈燕杰
	 * @param fileNameProperty:配置文件中的文件名key;
	 * @throws IOException 
	 * @since  fhd　Ver 1.1
	 */
	public File getFile(String fileNameProperty) throws IOException
	{
		Properties properties=new Properties();
		properties.load(SystemParamBO.class.getClassLoader().getResourceAsStream("application.properties"));
		String fileName=properties.getProperty(fileNameProperty);
		String filePackageName=properties.getProperty("fileUploadPath");
		File f=new File(filePackageName+"\\"+fileName);
//		URL url=SystemParamBO.class.getClassLoader().getResource("application.properties");
//		File pf=new File(url.getPath().substring(1));
		return f;
	}
}
