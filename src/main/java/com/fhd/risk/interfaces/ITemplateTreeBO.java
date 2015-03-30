package com.fhd.risk.interfaces;

import java.util.Map;

/**
 * 模板树BO的树加载
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-22		下午1:12:11
 *
 * @see 	 
 */
public interface ITemplateTreeBO {
	/**
	 * <pre>
	 * 加载TemplateRelaDimensionTree
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param query 查询条件
	 * @param templateId 模板ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Map<String,Object> templateRelaDimensionTreeLoader(String query, String templateId);
}

