/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */


package com.fhd.sys.web.controller.orgstructure;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.sys.business.orgstructure.OrgTreeBO;

/**
 * 人员选择控件
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-1-3		上午8:44:35
 *
 * @see 	 
 */
@Controller
public class EmployeeCmpControl {

	@Autowired
	private OrgTreeBO o_orgTreeBO;
	
	/**
	 * 
	 * <pre>
	 * 员工树查询
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param node 树根id
	 * @param posiVisible 是否显示岗位
	 * @param empVisible 是否显示员工
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/sys/emp/cmp/treeloader")
	public List<Map<String, Object>> treeLoader( String node, Boolean posiVisible, Boolean empVisible, Boolean subCompany,String query) {
		String[] strings = StringUtils.split(node,"_");
		return o_orgTreeBO.treeLoader(strings[0],posiVisible,empVisible,subCompany, query);
	}
	
}

