package com.fhd.comm.web.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.comm.business.CategoryTreeBO;

@Controller
public class CategoryTreeControl {

	@Autowired
	public CategoryTreeBO o_categoryTreeBO;
	
	/**
	 * 
	 * 根据当前登陆人所在公司id查询风险id、机构ID、流程ID
	 * @author 金鹏祥
	 * @return List<Map<String, Object>>
	 */
	@ResponseBody
	@RequestMapping(value = "/category/findRootBO.f")
	public List<Map<String, Object>> findRootByCompanyId() {
		return o_categoryTreeBO.findRootBO();
	}
	
	/**
	 * 我的记分卡
	 * 
	 * @author 金鹏祥
	 * @return List<Map<String, Object>>
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping(value = "/category/findMyCategoryTree.f")
	public List<Map<String, Object>> findMyCategoryTree(String node, Boolean canChecked, String query) {
		return o_categoryTreeBO.findCategoryTreeMyNodeBO(node, canChecked, query);
	}
	
	/**
	 * 机构记分卡
	 * 
	 * @author 金鹏祥
	 * @return List<Map<String, Object>>
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping(value = "/category/findOrgCategoryTree.f")
	public List<Map<String, Object>> findOrgCategoryTree(String node, String type, Boolean canChecked, String query){
        return o_categoryTreeBO.findOrgCategoryTreeNodeBO(node, type, canChecked, query);
    }
	
	/**
	 * 记分卡
	 * 
	 * @author 金鹏祥
	 * @return List<Map<String, Object>>
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping(value = "/category/findCategoryTree.f")
	public List<Map<String, Object>> findCategoryTree(String node, Boolean canChecked, String query){
		return o_categoryTreeBO.findCategoryTreeNodeBO(node, canChecked, query);
	}
}