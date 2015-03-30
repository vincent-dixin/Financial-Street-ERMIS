package com.fhd.comm.interfaces;

import java.util.List;
import java.util.Map;

public interface ICategoryTreeBO {
	
	/**
	 * 得到我的记分卡、机构记分卡、记分卡根目录
	 * 
	 * @author 金鹏祥
	 * @param companyId 公司ID
	 * @param deleteStatus 删除状态(true/false	已删除/未删除)
	 * @return List<Map<String, Object>>
	*/
	public List<Map<String, Object>> findRootBO();
	
	/**
	 * 得到风险树
	 * 
	 * @author 金鹏祥
	 * @param canChecked 是否有复选框
	 * @param node 节点id
	 * @param query 查询条件
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> findCategoryTreeNodeBO(String node, Boolean canChecked, String query);
	
	/**
	 * 得到我的风险树
	 * 
	 * @author 金鹏祥
	 * @param canChecked 是否有复选框
	 * @param node 节点id
	 * @param query 查询条件
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> findCategoryTreeMyNodeBO(String node, Boolean canChecked, String query);
	
	/**
	 * 得到机构风险树节点
	 * 
	 * @author 金鹏祥
	 * @param canChecked 是否有复选框
	 * @param id 节点id
	 * @param query 查询条件
	 * @param type (orgCategory/other 机构/其他)
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> findOrgCategoryTreeNodeBO(String node, String type, Boolean canChecked, String query);
	
	
}