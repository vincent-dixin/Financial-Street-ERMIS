/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */


package com.fhd.sys.business.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.sys.dao.auth.SysRoleDAO;
import com.fhd.sys.entity.auth.SysRole;

/**
 * ClassName:SysRoleTreeBO
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-1-8		上午11:32:00
 *
 * @see 	 
 */
@Service
public class SysRoleTreeBO {

	@Autowired
	private SysRoleDAO o_sysRoleDAO;
	
	
	
	
	/**
	 * 
	 * <pre>
	 * 角色树
	 * 角色名称模糊匹配
	 * </pre>
	 * 
	 * @author 胡迪新，张雷
	 * @param id
	 * @param query 查询条件
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<Map<String, Object>> treeLoader(String id,String query){
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
		
		List<SysRole> sysRoles = o_sysRoleDAO.getAll();
		for (SysRole sysRole : sysRoles) {
			Map<String, Object> node = new HashMap<String, Object>();
			node.put("id", sysRole.getId() + "_" + RandomUtils.nextInt(9999));
			node.put("dbid", sysRole.getId());
			node.put("text", sysRole.getRoleName());
			node.put("leaf", true);

			node.put("iconCls", "");
			node.put("cls", "role");
			if(StringUtils.isNotBlank(query)){
				if(sysRole.getRoleName().contains(query)){
					nodes.add(node);
				}
			}else{
				nodes.add(node);
			}
			
		}
		
		return nodes;
	}
	
}

