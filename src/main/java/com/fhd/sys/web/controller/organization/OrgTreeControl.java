/**
 * OrgTreeControl.java
 * com.fhd.sys.web.controller.organization
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-1-11 		黄晨曦
 *
 * Copyright (c) 2013, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.controller.organization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.business.organization.OrgEmpTreeBO;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * @author   
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-1-11		下午1:04:06
 *
 * @see 	 
 */
@Controller
public class OrgTreeControl {
	
	@Autowired
	private OrgEmpTreeBO o_orgEmpTreeBO;
	
	
	/**
	 * 查询机构树
	 * @author 
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/organization/findOrgTreeBySome.f")
	public Map<String, Object> findOrgTree(String query){
		String companyId = UserContext.getUser().getCompanyid();// 所在公司id
		return o_orgEmpTreeBO.findOrgTreeBySearchNameAndCompanyId(query, companyId);
	}
	
	/**
	 * 查询根节点
	 * @param node
	 * @param query
	 * @return
	 */
	@ResponseBody
    @RequestMapping("/sys/organization/findTreeRoot.f")
    public Map<String, Object> findTreeRoot(String node,String query){ 
        Map<String, Object> rootNode = new HashMap<String, Object>();
        Map<String, Object> items = new HashMap<String, Object>(); // 存放
        SysOrganization root = o_orgEmpTreeBO.findOrgTreeRoot();
        rootNode.put("id",root.getId());
        rootNode.put("text", root.getOrgname());
        rootNode.put("leaf", false);
        rootNode.put("expanded", true);
        rootNode.put("type", "jg");
        rootNode.put("keys", root.getId()+"jg");
        items.put("organization", rootNode);
        return items;
        
    }
	
	/**
	 * 查询机构树的所有节点
	 * @param node
	 * @param query
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/sys/organization/treeLoader.f")
	public List<Map<String, Object>> treeLoader(String node,String query, String id)
	{
	    return o_orgEmpTreeBO.treeLoader(node,query);
	}

}

