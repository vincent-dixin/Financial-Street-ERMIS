package com.fhd.icm.web.controller.defect;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.core.dao.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.fdc.utils.Contents;
import com.fhd.icm.business.defect.DefectBO;
import com.fhd.icm.business.defect.DefectTreeBO;
import com.fhd.icm.entity.defect.Defect;
import com.fhd.icm.entity.defect.DefectRelaImprove;
import com.fhd.icm.entity.defect.DefectRelaOrg;
import com.fhd.icm.entity.rectify.Improve;
import com.fhd.icm.web.form.DefectForm;
import com.fhd.sys.dao.dic.DictEntryDAO;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 缺陷的控制类，缺陷的新增，编辑，删除，查询方法的跳转
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-1-8		下午3:08:47
 *
 * @see 	 
 */
@Controller
public class DefectControl {

	@Autowired
	private DefectBO o_defectBO;
	@Autowired
	private DefectTreeBO o_defectTreeBO;
	@Autowired
	private DictEntryDAO o_dictEntryDAO;
	
	/**
	 * 缺陷清单保存.
	 * @param jsonString
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/icm/defect/saveDefects.f")
	public boolean saveDefects(String jsonString){
		o_defectBO.saveDefects(jsonString);
		return true;
	}
	/**
	 * <pre>
	 * 新增缺陷
	 * </pre>
	 * 
	 * @author 张 雷
	 * @author 元杰修改
	 * @param defectForm 缺陷表单
	 * @param response
	 * @throws IOException
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping(value="/icm/defect/saveDefect.f")
	public Map<String, Object> saveDefect(DefectForm defectForm,String submittype, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();
		Defect defect = o_defectBO.saveDefect(defectForm, submittype);
		result.put("success", true);
		result.put("defectId", defect.getId());
		return result;
	}
	
	@RequestMapping(value="/icm/defect/saveDefectFollow.f")
	public void saveImproveFile(DefectForm defectForm,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		String flag="false";
		try{
			out = response.getWriter();
			o_defectBO.saveDefectFollow(defectForm);
			flag="true";
		}finally{
			out.write(flag);
			out.close();
		}
	}
	/**
	 * <pre>
	 * 编辑缺陷
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param defectForm 缺陷表单
	 * @param response
	 * @throws IOException
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping(value="/icm/defect/mergeDefect.f")
	public void mergeDefect(DefectForm defectForm,HttpServletResponse response) throws IOException{
		PrintWriter out = null;
		String flag="false";
		try{
			out = response.getWriter();
			o_defectBO.mergeDefect(defectForm);
			flag="true";
		}finally{
			out.write(flag);
			out.close();
		}
	}
	
	/**
	 * <pre>
	 * 逻辑删除缺陷
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param defectIds 缺陷ID串，多个以“,”隔开
	 * @param response
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping(value = "/icm/defect/removeDefectByIdBatch.f")
	public void removeDefectByIdBatch(String defectIds,HttpServletResponse response) throws Exception{
		PrintWriter out = null;
		String flag = "false";
		try{
			out = response.getWriter();
			o_defectBO.removeDefectByIdBatch(defectIds);
			flag = "true";
		} finally {
			out.write(flag);
			out.close();
		}
	}
	/**
	 * <pre>
	 * 删除缺陷跟踪
	 * </pre>
	 * 
	 * @author 李克东
	 * @param defectIds
	 * @param response
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping(value = "/icm/defect/removeDefectFollowByIdBatch.f")
	public void removeDefectFollowByIdBatch(String defectIds,HttpServletResponse response) throws Exception{
		PrintWriter out = null;
		String flag = "false";
		try{
			out = response.getWriter();
			o_defectBO.removeDefectFollowByIdBatch(defectIds);
			flag = "true";
		} finally {
			out.write(flag);
			out.close();
		}
	}
	
	/**
	 * <pre>
	 * 分页查询缺陷列表
	 * 可以如下条件：
	 * 	查询条件模糊匹配编号和名称
	 *  评价计划ID
	 *  整改计划ID
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param limit 页面容量
	 * @param start 起始值
	 * @param query 查询条件	
	 * @param assessPlanId 评价计划ID
	 * @param improveId 整改计划ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/defect/findDefectListBypage.f")
	public Map<String,Object> findDefectListbyPage(int limit, int start, String query, String companyId, String assessPlanId, String improveId, String dealStatus, String status){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		Page<Defect> page = new Page<Defect>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		page = o_defectBO.findCompanyDefectListbyPage(page, query, assessPlanId, improveId, companyId, dealStatus, status);
		List<Defect> defectList = page.getResult();
		if(null != defectList && defectList.size()>0){
			Map<String, Object> defectMap = null;
			for(Defect defect : defectList){
				defectMap = new HashMap<String, Object>();
				defectMap.put("id", defect.getId());
				defectMap.put("code", defect.getCode());
				defectMap.put("desc", defect.getDesc());
				defectMap.put("createTime", DateUtils.formatShortDate(defect.getCreateTime()));
				if(null != defect.getDefectRelaOrg()){
					Set<DefectRelaOrg> dro = defect.getDefectRelaOrg();
					for (DefectRelaOrg defectRelaOrg : dro) {
						if(Contents.ORG_RESPONSIBILITY.equals(defectRelaOrg.getType())){
							defectMap.put("defectRelaOrg", defectRelaOrg.getOrg().getOrgname());
						}
					}
				}
				if(defect.getLevel()!=null){
					defectMap.put("controlRequirement", defect.getLevel().getName());
				}
				if(defect.getType()!=null){
					defectMap.put("type", defect.getType().getName());
				}
				defectMap.put("dealstatus",defect.getDealStatus());
				defectMap.put("status", defect.getStatus());
				datas.add(defectMap);
			}
			map.put("datas", datas);
			map.put("totalCount", page.getTotalItems());
		}else{
			map.put("datas", new Object[0]);
			map.put("totalCount", "0");
		}
		return map;
	} 
	
	/**
	 * <pre>
	 * 缺陷跟踪
	 * </pre>
	 * 
	 * @author 李克东
	 * @param limit
	 * @param start
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/defect/findDefectFollowListBypage.f")
	public Map<String,Object> findDefectListbyPage(int limit, int start, String defectId,String improveId){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		Page<DefectRelaImprove> page = new Page<DefectRelaImprove>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		Improve improve = o_defectBO.findImproveById(improveId);
		List<Defect> defectList = o_defectBO.findDefectFollowListByAll();
		page = o_defectBO.findDefectRelaImproveListbyPage(page);
		List<DefectRelaImprove> defectRelaImproveList = page.getResult();
		if(null != defectRelaImproveList && defectRelaImproveList.size()>0){
			Map<String,Object> defectFollowMap = new HashMap<String,Object>();
			for(Defect defect:defectList){
				if(defect.getDesc()!=null){
					defectFollowMap.put("defect", defect.getDesc());
				}
				
			}
			for(DefectRelaImprove defectRelaImprove:defectRelaImproveList){
				defectFollowMap.put("improve", improve.getName());
				defectFollowMap.put("id",defectRelaImprove.getId());
				datas.add(defectFollowMap);
			}
			map.put("datas", datas);
			map.put("totalCount", page.getTotalItems());
		}else{
			map.put("datas", new Object[0]);
			map.put("totalCount", "0");
		}
		return map;
	}
	
	/**
	 * <pre>
	 * 涉及缺陷组件
	 * </pre>
	 * 
	 * @author 李克东
	 * @param defectIds
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/defect/defectDesc/findDefectByIds.f")
	public List<Map<String,Object>> findDefectByIds(String defectIds){
		List<Map<String,Object>> defectList= new ArrayList<Map<String,Object>>();
		defectList=o_defectBO.findDefectByIds(defectIds);
		return defectList;
	}
	
	/**
	 * <pre>
	 * 加载缺陷表单
	 * </pre>
	 * 
	 * @author 李克东
	 * @param defectId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/defect/findDefectForForm.f")
	public Map<String,Object> findDefectForForm(String defectId){
 		Map<String,Object> defectMap = new HashMap<String,Object>();
		Map<String,Object> map= new HashMap<String, Object>();
		Defect defect = o_defectBO.findDefectById(defectId);
		defectMap.put("defectId", defect.getId());
		defectMap.put("code", defect.getCode());
		if(defect.getCompany()!=null){
			defectMap.put("company", defect.getCompany().getId());
		}
		if(defect.getLevel()!=null){
			defectMap.put("level", defect.getLevel().getId());
		}
		if(defect.getType()!=null){
			defectMap.put("type", defect.getType().getId());
		}
		
		List<DefectRelaOrg> defectRelaOrgList= o_defectBO.findDefectRelaOrgById(defectId);
		
		defectMap.put("org","[{\"id\":\""+defectRelaOrgList.get(0).getOrg().getId()+"\",\"deptno\":\"\",\"deptname\":\"\"}]");
		defectMap.put("desc", defect.getDesc());
		defectMap.put("designDefect", defect.getDesignDefect());
		defectMap.put("executeDefect", defect.getExecuteDefect());
		defectMap.put("defectAnalyze", defect.getDefectAnalyze());
		defectMap.put("improveAdivce", defect.getImproveAdivce());
	    map.put("data", defectMap);
	    map.put("success", true);
	    return map;
	
	}
	
	/**
	 * <pre>
	 * 加载缺陷跟踪
	 * </pre>
	 * 
	 * @author 李克东
	 * @param defectId
	 * @param improveId
	 * @param defectRelaImproveId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/defect/findDefectFollow.f")
	public Map<String,Object> findImproveAdviceBySome(String defectId,String improveId,String defectRelaImproveId){
		Map<String, Object> map=new HashMap<String,Object>();
		Map<String,Object> defectImproveMap = new HashMap<String,Object>();
		Improve improve = o_defectBO.findImproveById(improveId);
		List<Defect> defectList = o_defectBO.findDefectFollowListByAll();
		for(Defect defect:defectList){
			if(defect.getDesc()!=null){
				
			}
			defectImproveMap.put("defect", defect.getDesc());
		}
		DefectRelaImprove defectRelaImprove = o_defectBO.findDefectRelaImprovebyId(defectRelaImproveId);
		defectImproveMap.put("improve", improve.getName());
		
		defectImproveMap.put("id", defectRelaImprove.getId());
		map.put("data", defectImproveMap);
		map.put("success", true);
	    return map;
	}
	
	/**
	 * <pre>
	 * 缺陷选择
	 * </pre>
	 * 
	 * @author 李克东
	 * @param node
	 * @param canChecked
	 * @param query
	 * @param type
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/defect/defectTree/findrootDefectTreeLoader.f")
	public List<Map<String, Object>> findDefectTreeLoader(String node,
			boolean canChecked, String query, String type) {
		List<Map<String, Object>> result=o_defectTreeBO.defectTreeLoader(node, canChecked, query, type);
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/defect/defectTree/findrootDefectLevelTreeLoader.f")
	public List<Map<String, Object>> findDefectLevelTreeLoader(String node,
			boolean canChecked, String query, String type) {
		List<Map<String, Object>> result=o_defectTreeBO.defectLevelTreeLoader(node, canChecked, query, type);
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/icm/defect/findAllDefect.f")
	public List<Map<String,Object>> findAllDefect(){
		List<Map<String,Object>> datalist=new ArrayList<Map<String,Object>>();
		List<Defect> list = o_defectBO.findAllDefect();
		for(Defect defect:list){
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("id", defect.getId());
			data.put("name", defect.getDesc());
			datalist.add(data);
		}
		return datalist;
	}
	
	/**
	 * 缺陷列表批量保存.
	 * @author 吴德福
	 * @param jsonString
	 */
	@ResponseBody
	@RequestMapping("/icm/assess/mergeDefectBatch.f")
	public Boolean mergeDefectBatch(String jsonString){
		o_defectBO.mergeDefectBatch(jsonString);
		return true;
	}
	
	/**
	 * 缺陷信息预览.
	 * @param defectId
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/icm/defect/findDefectById.f")
	public Map<String,Object> findDefectById(String defectId){
 		Map<String,Object> defectMap = new HashMap<String,Object>();
		Map<String,Object> map= new HashMap<String, Object>();
		Defect defect = o_defectBO.findDefectById(defectId);
		defectMap.put("defectId", defect.getId());
		defectMap.put("code", defect.getCode());
		if(null != defect.getCompany()){
			defectMap.put("companyId", defect.getCompany().getId());
			defectMap.put("companyName", defect.getCompany().getOrgname());
		}
		if(null != defect.getLevel()){
			defectMap.put("level", defect.getLevel().getName());
		}
		if(null != defect.getType()){
			defectMap.put("type", defect.getType().getName());
		}
		
		List<DefectRelaOrg> defectRelaOrgList= o_defectBO.findDefectRelaOrgById(defectId);
		
		defectMap.put("orgId", defectRelaOrgList.get(0).getOrg().getId());
		defectMap.put("orgName", defectRelaOrgList.get(0).getOrg().getOrgname());
		defectMap.put("desc", defect.getDesc());
		String designDefect = "";
		if(StringUtils.isNotBlank(defect.getDesignDefect())){
			String[] designDefectArray = defect.getDesignDefect().split(",");
			int i=0;
			for (String designDefectStr : designDefectArray) {
				DictEntry dictEntry = o_dictEntryDAO.get(designDefectStr);
				if(null != dictEntry){
					designDefect += dictEntry.getName();
				}
				if(i != designDefectArray.length-1){
					designDefect += ",";
				}
				i++;
			}
		}
		defectMap.put("designDefect", designDefect);
		String executeDefect = "";
		if(StringUtils.isNotBlank(defect.getExecuteDefect())){
			String[] executeDefectArray = defect.getExecuteDefect().split(",");
			int i=0;
			for (String executeDefectStr : executeDefectArray) {
				DictEntry dictEntry = o_dictEntryDAO.get(executeDefectStr);
				if(null != dictEntry){
					executeDefect += dictEntry.getName();
				}
				if(i != executeDefectArray.length-1){
					executeDefect += ",";
				}
				i++;
			}
		}
		defectMap.put("executeDefect", executeDefect);
		defectMap.put("defectAnalyze", defect.getDefectAnalyze());
		defectMap.put("improveAdivce", defect.getImproveAdivce());
	    map.put("data", defectMap);
	    map.put("success", true);
	    return map;
	}
}