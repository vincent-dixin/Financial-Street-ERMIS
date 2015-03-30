package com.fhd.sys.business.param;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.springframework.stereotype.Service;

/**
 * 系统Properties文件的属性值提供类;
 * ClassName:SysParamProvider
 * @author   陈燕杰
 * @since    Ver 1.1
 * @Date	 2011	2011-3-28		下午03:46:45
 */
@Service
public class SysParamProviderBO {
	/**
	 * 根据属性名得到properties文件中的属性值；
	 * @author 陈燕杰
	 * @param key：属性名；
	 * @return String
	 * @throws ConfigurationException 
	 * @since  fhd　Ver 1.1
	 */
	public String getProperty(String key) throws ConfigurationException{
		//URL url=ClassLoader.getSystemResource("application.properties");
		PropertiesConfiguration config=new PropertiesConfiguration("application.properties");
		//Commons Configuration集成了这个加载机制, 如果需要使用自动加载，只需要在id配置信息里声明一个自动重载策略
		config.setReloadingStrategy(new FileChangedReloadingStrategy());
		String result=config.getString(key);
		return result;
	}
}
