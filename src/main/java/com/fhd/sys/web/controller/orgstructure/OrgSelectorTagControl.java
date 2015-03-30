/**
 * OrgSelectorTagControl.java
 * com.fhd.fdc.commons.web.controller.sys.orgstructure
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-10-11 		David.Niu
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/
/**
 * OrgSelectorTagControl.java
 * com.fhd.fdc.commons.web.controller.sys.orgstructure
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-10-11        David.Niu
 *
 * Copyright (c) 2010, FirstHuida All Rights Reserved.
*/


package com.fhd.sys.web.controller.orgstructure;


import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fhd.sys.business.orgstructure.OrganizationBO;

/**
 * ClassName:OrgSelectorTagControl
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   David.Niu
 * @version  
 * @since    Ver 1.1
 * @Date	 2010-10-11		下午03:25:29
 *
 * @see 	 
 */
@Controller
public class OrgSelectorTagControl {
	
	@Autowired
	private OrganizationBO o_organizationBO;
	
	/**
	 * 打开选择组织标签演示页面.
	 * @author David.Niu
	 * @return String 跳转到orgSelectorTest.jsp页面.
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/org/orgMultiSelectorTagTest.do")
	public String testTagPage() {
		return "sys/orgstructure/tag/orgSelectorTest";
	}
	
	/**
	 * 打开选择组织树页面.
	 * @author David.Niu
	 * @param type 打开页面类型；如果为"multiple",打开多选页面；如果为"single",打开单选页面
	 * @param checkNode 是否有复选框
	 * @param onlyLeafCheckable 是否只是叶子节点加复选框
	 * @param checkModel 多选: 'multiple'(默认)、单选: 'single'、级联多选: 'cascade'(同时选父和子);'parentCascade'(选父);'childCascade'(选子)
	 * @param companyId 指定公司的树
	 * @return String 如果输入参数type为为"multiple",打开orgSelectorTree_mutiple.jsp；如果为"single",打开orgSelectorTree_single.jsp.
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/orgstructure/org/openSelectorTreePage.do")
	public String openSelectorTreePage(Model model,HttpServletRequest request,String type,String checkNode,String onlyLeafCheckable,String checkModel,String tag,String companyId) {
		if(StringUtils.isNotBlank(companyId)){
			model.addAttribute("orgRoot", o_organizationBO.getRootOrgByCompanyId(companyId));
		}else{
			model.addAttribute("orgRoot", o_organizationBO.getRootOrg());
		}
		model.addAttribute("showType", request.getParameter("showType"));
		model.addAttribute("checkNode", checkNode);
		model.addAttribute("onlyLeafCheckable", onlyLeafCheckable);
		model.addAttribute("checkModel", checkModel);
		model.addAttribute("tag", tag);
		
		if("mutiple".equals(type))
			return "sys/orgstructure/tag/orgSelectorTree_mutiple";
		return "sys/orgstructure/tag/orgSelectorTree_single";
	}
}

