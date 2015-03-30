/**
 * RiskControl.java
 * com.fhd.risk.web.controller
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-1 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
 */
/**
 * RiskControl.java
 * com.fhd.risk.web.controller
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-1        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
 */

package com.fhd.risk.web.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.risk.business.RiskTreeBO;

/**
 * 风险树控件控制分发
 * 
 * @author 金鹏祥
 * @version
 * @since Ver 1.1
 * @Date 2012-11-1 上午10:52:03
 * 
 * @see
 */
@Controller
public class RiskTreeControl {
	@Autowired
	private RiskTreeBO o_riskTreeBO;
	
	/**
	 * 
	 * 根据当前登陆人所在公司id查询风险id、机构ID、流程ID
	 * @author 金鹏祥
	 * @return List<Map<String, Object>>
	 */
	@ResponseBody
	@RequestMapping(value = "/risk/findRootByCompanyId.f")
	public List<Map<String, Object>> findRootByCompanyId() {
		return o_riskTreeBO.findRootByCompanyId();
	}
	
	/**
     * 机构风险树
     * @param id 节点id
     * @param canChecked 是否有复选框
     * @param query 查询条件
     * @author 金鹏祥
     * @return List<Map<String, Object>>
     */
    @ResponseBody
    @RequestMapping(value = "/risk/orgRiskTree.f")
    public List<Map<String, Object>> orgTreeLoader(String node, String type, Boolean canChecked, String query, Boolean rbs){
        return o_riskTreeBO.orgTreeLoader(node, type, canChecked, query, rbs);
    }
	
	/**
     * 风险树
     * @param id 节点id
     * @param canChecked 是否有复选框
     * @param query 查询条件
     * @author 金鹏祥
     * @return List<Map<String, Object>>
     */
    @ResponseBody
	@RequestMapping(value = "/risk/riskTree.f")
    public List<Map<String, Object>> riskTreeLoader(String node, Boolean canChecked, String query, Boolean rbs){
    	return o_riskTreeBO.treeLoader(node, canChecked, query, null, rbs);
    }
    
    /**
     * 我的风险树
     * @param id 节点id
     * @param canChecked 是否有复选框
     * @param query 查询条件
     * @author 金鹏祥
     * @return List<Map<String, Object>>
     */
    @ResponseBody
	@RequestMapping(value = "/risk/myRiskTree.f")
    public List<Map<String, Object>> myRiskTreeLoader(String node, Boolean canChecked, String query, Boolean rbs){
    	return o_riskTreeBO.treeLoader(node, canChecked, query, "emp", rbs);
    }
}