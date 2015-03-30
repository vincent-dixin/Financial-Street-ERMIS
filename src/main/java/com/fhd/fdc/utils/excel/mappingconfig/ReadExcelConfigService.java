package com.fhd.fdc.utils.excel.mappingconfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


/**
 * ClassName:ReadExcelConfigService
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   David
 * @version  
 * @since    Ver 1.1
 * @Date	 2011	2011-2-15		下午06:56:02
 *
 * @see
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class ReadExcelConfigService {

	public ExcelConfig loadExcelConfig(Class clazz) {
		ExcelConfig config = new ExcelConfig();
		String fileName = "excelmapping.xml";
		SAXBuilder builder = new SAXBuilder();
		Document document;
		try {
			//读取XML
			//document = builder.build(fileName);
			document = builder.build(ReadExcelConfigService.class.getClassLoader().getResource(fileName));
			//取出整个根
			Element root = document.getRootElement();
			//将配置文件中值封到treeConfig中
			getMappingConfig(root, clazz, config);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return config;
	}

	/**
	 * <pre>
	 * getMappingConfig:获取配置
	 * </pre>
	 * 
	 * @author David
	 * @param root
	 * @param clazz
	 * @param config
	 * @since  fhd　Ver 1.1
	 */
	private void getMappingConfig(Element root, Class clazz, ExcelConfig config) {

		//根节点下 treeConfig 节点列表
		List<Element> treeConfigs = root.getChildren("class");

		Element currentConfig = null;

		Iterator<Element> itr = treeConfigs.iterator();

		while (itr.hasNext()) {
			Element mapping = (Element) itr.next();
			//取出置配文件 treeConfig 的类型。
			String mappingName = mapping.getAttribute("name").getValue();
			//通过传入的类型来取出配置文件中相对应的信息
			if (mappingName.equals(clazz.getName())) {
				currentConfig = mapping;
				config.setMappingName(mappingName);
				break;
			}

		}

		if (currentConfig != null) {
			List<Element> pop = currentConfig.getChildren("pop");
			List<MappingBean> mappingList = new ArrayList<MappingBean>();
			for (int i = 0; i < pop.size(); i++) {

				Element pops = (Element) pop.get(i);
				MappingBean mapping = new MappingBean();
				String excelPop = pops.getAttribute("excelPop").getValue();
				String classPop = pops.getAttribute("classPop").getValue();

				mapping.setExcelPop(excelPop);
				mapping.setClassPop(classPop);
				mappingList.add(mapping);
			}

			config.setMappingList(mappingList);

		}
	}

}
