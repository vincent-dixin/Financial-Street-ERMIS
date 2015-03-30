package com.fhd.icm.web.controller.standard;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.core.dao.Page;
import com.fhd.fdc.utils.UserContext;
import com.fhd.icm.business.standard.StandardBO;
import com.fhd.icm.business.standard.StandardTreeBO;
import com.fhd.icm.entity.standard.Standard;
import com.fhd.icm.web.form.StandardForm;

/**
 * STANDARDTree_内控Controller ClassName:StandardController
 * 
 * @author 刘中帅
 * @version
 * @since Ver 1.1
 * @Date 2012 2012-12-11 上午10:35:34
 */
@Controller
public class StandardTreeControl {

	@Autowired
	private StandardTreeBO o_standardTreeBO;
	@Autowired
	private StandardBO  o_standardBO;
	
	/**
	 * <pre>
	 * 内控树，查询根节点下的所有子节点
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param node 
	 * @param canChecked 是否有复选框
	 * @param query查询条件
     * @param myType 类型
	 * @return List<Map<String, Object>>
	 * @throws UnsupportedEncodingException
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/standard/standardTree/findStandardTreeLoader.do")
	public List<Map<String, Object>> findStandardTreeLoader(String node, boolean canChecked, String query, String myType){
		return o_standardTreeBO.standardTreeLoader(node, canChecked, query, myType, UserContext.getUser().getCompanyid());
	}

	/**
	 * 根绝被选中的叶子节点读取Etype类型为0的数据
	 * @author 刘中帅
	 * @param checkedNodeIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/standard/standardGrid/findStandardByPage.f")
	public Map<String,Object> findStandardByPage(int limit, int start, String query,String clickedNodeId,String isLeaf){
		
		Map<String,Object> resultMap;
		Page<Standard> page = new Page<Standard>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		String companyId = UserContext.getUser().getCompanyid();
		resultMap=o_standardBO.findStandardListByPage(page, query, clickedNodeId, isLeaf,companyId);
		return resultMap;
		
		
	}
	/**
	 * <pre>
	 * 内控维护，修改数据的时候，将信息读取到form表单
	 * </pre>
	 * @author 刘中帅
	 * @param standardId 内控标准Id
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/standard/standardTree/findStandardByIdToJson.do")
	public Map<String, Object> findStandardByIdToJson(String standardId){
		Map<String,Object> resultMap=null;
		resultMap=o_standardTreeBO.findStandardByIdToJson(standardId);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("data", resultMap);
		map.put("success", true);
		return map;
	}
	
	
	/**
	 * <pre>
	 * 内控选择组件load值
	 * </pre>
	 * @author 刘中帅
	 * @param standardId 内控标准Id
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/standard/standardTree/findStandardByIds.f")
	public List<Map<String, Object>> findStandardByIds(String standardIds){
		List<Map<String,Object>> resultMap=new ArrayList<Map<String,Object>>();
		String[] standardArray=standardIds.split(",");
		resultMap=o_standardBO.findStandardByIds(standardArray);
		return resultMap;
	}
	/**
	 * 
	 * <pre>
	 * 内控维护，右键菜单删除功能
	 * </pre>
	 * @author 刘中帅
	 * @param standardId
	 *            :内控标准Id
	 * @return
	 * @throws IOException
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/standard/standardTree/removeStandards.do")
	public Map<String,Object> removeStandards(String standardIds, HttpServletResponse response)
			throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		String ret = o_standardTreeBO.removeStandardsById(standardIds);
		result.put("result", ret);
        return result;
	}

	/**
	 * 
	 * <pre>
	 * 保存数据之前做编号重复验证
	 * </pre>
	 * @author 刘中帅，张雷
	 * @param id 当前ID
	 * @param code 当前编号
	 * @param response
	 * @return
	 * @throws IOException
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/standard/standardTree/validateStandard.do")
	public Map<String, Object> validateStandard(String id, String code,
			HttpServletResponse response) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		String flagString = o_standardTreeBO.validateStandard(id, code);
		result.put("success", true);
		result.put("flagStr", flagString);
		return result;
	}

	/**
	 * 
	 * <pre>
	 * (这里用一句话描述这个方法的作用)
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param form
	 *            表单 封装成的StandardFrom 对象
	 * @param nodeId
	 * @param idSeq
	 * @param standardId
	 * @return
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/standard/standardTree/saveStandard.do")
	public Map<String, Object> saveStandard(StandardForm form, String nodeId, String addType,String idSeq, String standardId) {
		Map<String, Object> result = new HashMap<String, Object>();
		o_standardTreeBO.saveStandard(form, nodeId,idSeq,standardId);
		result.put("success", true);
		return result;
	}
    
	/**
	 * 
	 * <pre>
	 * 修改内控标准
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param form 已经封装的实体
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/standard/standardTree/editStandard.do")
    public Map<String,Object>  editStandard(StandardForm form){
		Map<String, Object> result = new HashMap<String, Object>();
		o_standardTreeBO.editStandard(form);
		result.put("success", true);
		return result;
    }
	
	
	/**
	 * 
	 * <pre>
	 *内控标准维护，创建内控编号
	 * </pre>
	 * @author 刘中帅
	 * @param nodeId：内控树节点Id
	 * @return 内控编号
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/standard/standardTree/createStandardCode.f")
	public Map<String,Object> createCode(String nodeId){
		String standardCode="";
		Map<String,Object> resultMap=new HashMap<String,Object>();
		standardCode=o_standardTreeBO.createStandardcodeById(nodeId);
		resultMap.put("code", standardCode);
		resultMap.put("success",true);
		return resultMap;
		
	}
	
	
	/**
	 * 
	 * <pre>
	 *内控标准维护,创建导航条菜单
	 * </pre>
	 * @author 刘中帅
	 * @param nodeId：内控树节点Id
	 * @return 内控编号
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/standard/standardTree/createNavigation.f")
	public Map<String,Object> createNavigation(String nodeId){
		String standardCode="";
		Map<String,Object> resultMap=new HashMap<String,Object>();
		standardCode=o_standardTreeBO.createStandardcodeById(nodeId);
		resultMap.put("code", standardCode);
		resultMap.put("success",true);
		return resultMap;
		
	}
	
}
