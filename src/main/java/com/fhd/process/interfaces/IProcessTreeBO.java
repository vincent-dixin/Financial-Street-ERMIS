package com.fhd.process.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 
 * 流程树对象
 * 
 * @author 李克东
 * @since fhd Ver 1.1
 * @Date 2012-12-11 上午11:16:09
 * 
 * @see
 */
public interface IProcessTreeBO {

	/**
	 * <pre>
	 * 根据流程标识获取所有下级内控
	 * </pre>
	 * 
	 * @author 李克东
	 * @param id流程
	 * @param self是否包含自己
	 * @return
	 * @since fhd　Ver 1.1
	 */
	public List<Map<String, Object>> processTreeLoader(String id,
			Boolean canChecked, String query,String type);

}
