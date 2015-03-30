/**
 * IRiskTreeBO.java
 * com.fhd.risk.interfaces
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-19 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * IRiskTreeBO.java
 * com.fhd.risk.interfaces
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-19        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
*/


package com.fhd.risk.interfaces;

import java.util.List;
import java.util.Map;

import com.fhd.risk.entity.Risk;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 风险树控件接口
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-19		上午09:26:45
 *
 * @see 	 
 */
public interface IRiskTreeBO {

	/**
	 * 封装指标、目标、组织对象到Map中
	 * 
	 * @author 金鹏祥
	 * @param obj 操作对象
	 * @param canChecked 是否带复选框
	 * @param isLeaf 是否带有叶子节点
	 * @param expanded 是否展开
	 * @return Map<String, Object>
	 */
	public Map<String, Object> objectPackage(Object obj, Boolean canChecked, Boolean isLeaf, Boolean expanded);
	
	/**
	 * 封装目标对象到Map中,给树提供数据
	 * 
	 * @author 金鹏祥
	 * @param obj 目标对象
	 * @param canChecked 是否带复选框
	 * @param isLeaf 是否带有叶子节点
	 * @param expanded 是否展开
	 * @return
	 * @since fhd　Ver 1.1
	 */
	public Map<String, Object> wrapStrategyMapNode(Risk risk, Boolean canChecked, Boolean isLeaf, Boolean expanded);
	
	/**
	 * 查找ORG的父机构,条件为ORG的父机构的ID要和当前传入的机构ID相同
	 * 
	 * @author 金鹏祥
	 * @param org 机构实体
	 * @param orgId 机构ID(当点击节点时传入的机构ID)
	 * @return SysOrganization
	 */
	public SysOrganization findParentNodeById(SysOrganization org,String orgId);
	
	/**
	 * 根据当前登陆人所在公司id查询风险id、机构ID、流程ID
	 * 
	 * @author 金鹏祥
	 */
	public List<Map<String, Object>> findRootByCompanyId();
	
	/**
	 * 风险树、我的风险树
	 * 
	 * @author 金鹏祥
	 * @param canChecked 是否有复选框
	 * @param node 节点id
	 * @param query 查询条件
	 * @param type (null/notNull 风险树/我的风险树)
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> treeLoader(String id, Boolean canChecked, String query, String type, Boolean rbs);
	
	/**
	 * 机构风险树
	 * 
	 * @author 金鹏祥
	 * @param canChecked 是否有复选框
	 * @param id 节点id
	 * @param query 查询条件
	 * @param type (orgRisk/other 机构/其他)
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> orgTreeLoader(String id, String type, Boolean canChecked, String query, Boolean rbs);
}

