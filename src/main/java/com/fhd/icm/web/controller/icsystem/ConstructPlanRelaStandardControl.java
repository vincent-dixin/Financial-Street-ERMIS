package com.fhd.icm.web.controller.icsystem;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.core.dao.Page;
import com.fhd.fdc.utils.Contents;
import com.fhd.icm.business.icsystem.ConstructPlanBO;
import com.fhd.icm.business.icsystem.ConstructPlanRelaStandardBO;
import com.fhd.icm.entity.icsystem.ConstructPlanRelaOrg;
import com.fhd.icm.entity.icsystem.ConstructPlanRelaStandard;
import com.fhd.icm.entity.icsystem.ConstructPlanRelaStandardEmp;
import com.fhd.icm.entity.standard.StandardRelaOrg;
import com.fhd.process.entity.ProcessRelaStandard;
import com.fhd.sys.dao.dic.DictEntryDAO;

/**
 * @author   黄晨曦
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-1-9		上午11:14:40
 *
 * @see 	 
 */
@Controller
public class ConstructPlanRelaStandardControl {
	
	@Autowired
	private ConstructPlanRelaStandardBO o_constructPlanRelaStandardBO;
	
	@Autowired
	private ConstructPlanBO o_constructPlanBO;
	@Autowired
	private DictEntryDAO o_dictEntryDAO;

	
	/**
	 * 
	 * <pre>
	 *   批量保存标准
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param constructPlanJson :grid中修改过的数据集合
	 * @return
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/icsystem/mergeconstructplanrelastandardbatch.f")
	public Map<String, Object> mergeConstructPlanRelaStandardBatch(String constructPlanId, String modifiedRecord,String defaultValue) {
		Map<String, Object> constructPlanMap = new HashMap<String, Object>();
		o_constructPlanRelaStandardBO.mergeConstructPlanRelaStandardBatch(modifiedRecord,defaultValue);
		constructPlanMap.put("success", true);
		return constructPlanMap;
	}
	/**
	 * <pre>
	 * 内控维护流程范围列表中添加流程功能
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param assessMeasureId :穿行，抽样，全部
	 * @param processIds  ：流程Ids，多选是以‘,’间隔的字符串
	 * @param assessPlanId 相关的内控评价计划Id
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/icsystem/saveconstructplanrelastandard.f")
	public Map<String, Object> saveConstructPlanRelaStandard(String standardIds, String constructPlanId) {
		Map<String, Object> constructPlanMap = new HashMap<String, Object>();
		o_constructPlanRelaStandardBO.saveConstructPlanRelaStandard(standardIds,constructPlanId);
		constructPlanMap.put("success", true);
		return constructPlanMap;
	}
	/**
	 * <pre>
	 * 内控维护流程范围列表中添加流程功能
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param assessMeasureId :穿行，抽样，全部
	 * @param processIds  ：流程Ids，多选是以‘,’间隔的字符串
	 * @param assessPlanId 相关的内控评价计划Id
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/icsystem/saveconstructplanreladefect.f")
	public Map<String, Object> saveConstructPlanRelaDefect(String defectIds, String constructPlanId) {
		Map<String, Object> constructPlanMap = new HashMap<String, Object>();
		o_constructPlanRelaStandardBO.saveConstructPlanRelaDefect(defectIds,constructPlanId);
		constructPlanMap.put("success", true);
		return constructPlanMap;
	}
	
	/**
	 * 
	 * <pre>
	 * 内控评价流程范围列表,删除功能
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @return
	 * @throws IOException
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/icsystem/removeconstructplanrelastandard.f")
	public Map<String,Object> removeConstructPlanRelaStandard(String constructPlanRelaStandardIds) throws IOException {
		if(StringUtils.isNotBlank(constructPlanRelaStandardIds)){
			List<String> idList = new ArrayList<String>();
			String[] idArray = constructPlanRelaStandardIds.split(",");
			for (String id : idArray) {
				idList.add(id);
			}
			o_constructPlanRelaStandardBO.removeConstructPlanRelaStandardByConstructPlanId(idList);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", "sucess");
		return result;
	}
	
	/**
	 * <pre>
	 * 查询评价计划关联的流程范围
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param limit 页面容量
	 * @param start 起始值
	 * @param query 查询条件
	 * @param constructPlanId 评价计划ID
	 * @return
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/icsystem/findconstructplanrelastandardlistbypage.f")
	public Map<String, Object> findConstructPlanRelaStandardListByPage(int limit,int start, String query, String constructPlanId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		Page<ConstructPlanRelaStandard> page = new Page<ConstructPlanRelaStandard>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		page = o_constructPlanRelaStandardBO.findConstructPlanRelaStandardListByPage(page, query,constructPlanId);
		List<ConstructPlanRelaStandard> constructPlanRelaStandardList = page.getResult();
		if (null != constructPlanRelaStandardList
				&& constructPlanRelaStandardList.size() > 0) {
			Map<String, Object> constructPlanRelaStandardMap = null;
			for (ConstructPlanRelaStandard constructPlanRelaStandard : constructPlanRelaStandardList) {
				constructPlanRelaStandardMap = new HashMap<String, Object>();
				constructPlanRelaStandardMap.put("id",constructPlanRelaStandard.getId());
				constructPlanRelaStandardMap.put("dbid",constructPlanRelaStandard.getId());
				//通过标准Id 找到对应关系 
				ProcessRelaStandard processRelaStandard = o_constructPlanRelaStandardBO.getProcessRelaStandard(constructPlanRelaStandard.getStandard().getId());
//				constructPlanRelaStandardMap.put("orgName", StringUtils.join(orgNameSet, ","));
				constructPlanRelaStandardMap.put("isProcessEdit",constructPlanRelaStandard.getIsProcessEdit());
				if (null != processRelaStandard) {// 显示末级流程名称
					constructPlanRelaStandardMap.put("processName",processRelaStandard.getProcess().getName());
					constructPlanRelaStandardMap.put("text", processRelaStandard.getProcess().getName());
					constructPlanRelaStandardMap.put("processId",processRelaStandard.getProcess().getId());// 末级流程Id
				}else{
					constructPlanRelaStandardMap.put("isProcessEdit",false);
					}
				Set<StandardRelaOrg> orgList = constructPlanRelaStandard.getStandard().getStandardRelaOrg();
				for(Iterator<StandardRelaOrg> it = orgList.iterator();it.hasNext();){
					StandardRelaOrg org = (StandardRelaOrg) it.next();
					if(Contents.ORG_RESPONSIBILITY.equals(org.getType())){
						constructPlanRelaStandardMap.put("standardRelaOrg",org.getOrg().getOrgname());
					}
				}
				//根据计划标准id找到对应的责任人
				ConstructPlanRelaStandardEmp constructPlanRelaStandardEmp = o_constructPlanRelaStandardBO.getPlanStandardEmp(constructPlanRelaStandard.getId());
				if(constructPlanRelaStandardEmp != null){
					constructPlanRelaStandardMap.put("constructPlanRelaOrgEmpId", constructPlanRelaStandardEmp.getId());
					constructPlanRelaStandardMap.put("constructPlanEmp", constructPlanRelaStandardEmp.getConstructPlanRelaOrg().getId());
				}
				constructPlanRelaStandardMap.put("standardName",constructPlanRelaStandard.getStandard().getParent().getName());
				constructPlanRelaStandardMap.put("controlRequirement",constructPlanRelaStandard.getStandard().getName());
				constructPlanRelaStandardMap.put("isNormallyDiagnosis",constructPlanRelaStandard.getIsNormallyDiagnosis());
				
				if(!StringUtils.isBlank(constructPlanRelaStandard.getStandard().getControlPoint())){
					constructPlanRelaStandardMap.put("controlPoint",o_dictEntryDAO.get(constructPlanRelaStandard.getStandard().getControlPoint()).getName());
				}
				datas.add(constructPlanRelaStandardMap);
			}
			map.put("datas", datas);
			map.put("totalCount", page.getTotalItems());
		} else {
			map.put("datas", new Object[0]);
			map.put("totalCount", "0");
		}
		return map;
	}
	/**
	 * <pre>
	 * 查询预览
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param limit 页面容量
	 * @param start 起始值
	 * @param query 查询条件
	 * @param constructPlanId 评价计划ID
	 * @return
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/icsystem/findconstructplanrelastandardlistbypageforview.f")
	public Map<String, Object> findConstructPlanRelaStandardListByPageForView(int limit,int start, String query, String constructPlanId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		Page<ConstructPlanRelaStandard> page = new Page<ConstructPlanRelaStandard>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		page = o_constructPlanRelaStandardBO.findConstructPlanRelaStandardListByPage(page, query,constructPlanId);
		List<ConstructPlanRelaStandard> constructPlanRelaStandardList = page.getResult();
		if (null != constructPlanRelaStandardList
				&& constructPlanRelaStandardList.size() > 0) {
			Map<String, Object> constructPlanRelaStandardMap = null;
			for (ConstructPlanRelaStandard constructPlanRelaStandard : constructPlanRelaStandardList) {
				constructPlanRelaStandardMap = new HashMap<String, Object>();
				//通过标准Id 找到对应关系 
				ProcessRelaStandard processRelaStandard = o_constructPlanRelaStandardBO.getProcessRelaStandard(constructPlanRelaStandard.getStandard().getId());
//				constructPlanRelaStandardMap.put("orgName", StringUtils.join(orgNameSet, ","));
				if (null != processRelaStandard) {// 显示末级流程名称
					constructPlanRelaStandardMap.put("processName",processRelaStandard.getProcess().getName());
				}
				Set<StandardRelaOrg> orgList = constructPlanRelaStandard.getStandard().getStandardRelaOrg();
				for(Iterator<StandardRelaOrg> it = orgList.iterator();it.hasNext();){
					StandardRelaOrg org = (StandardRelaOrg) it.next();
					if(Contents.ORG_RESPONSIBILITY.equals(org.getType())){
						constructPlanRelaStandardMap.put("standardRelaOrg",org.getOrg().getOrgname());
					}
				}
				//根据计划标准id找到对应的责任人
				ConstructPlanRelaStandardEmp constructPlanRelaStandardEmp = o_constructPlanRelaStandardBO.getPlanStandardEmp(constructPlanRelaStandard.getId());
				if(constructPlanRelaStandardEmp != null){
					constructPlanRelaStandardMap.put("constructPlanRelaOrgEmpId", constructPlanRelaStandardEmp.getId());
					constructPlanRelaStandardMap.put("constructPlanEmp", constructPlanRelaStandardEmp.getConstructPlanRelaOrg().getEmp().getEmpname()+"("+constructPlanRelaStandardEmp.getConstructPlanRelaOrg().getEmp().getEmpcode()+")");
				}
				constructPlanRelaStandardMap.put("standardName",constructPlanRelaStandard.getStandard().getParent().getName());
				constructPlanRelaStandardMap.put("controlRequirement",constructPlanRelaStandard.getStandard().getName());
				if(StringUtils.isNotBlank(constructPlanRelaStandard.getStandard().getControlPoint())){
					constructPlanRelaStandardMap.put("controlPoint",o_dictEntryDAO.get(constructPlanRelaStandard.getStandard().getControlPoint()).getName());
				}
				if(constructPlanRelaStandard.getIsNormallyDiagnosis()){
				constructPlanRelaStandardMap.put("isNormallyDiagnosis","是");
				}
				else{
					constructPlanRelaStandardMap.put("isNormallyDiagnosis","否");
				}
				if(constructPlanRelaStandard.getIsProcessEdit()){
					constructPlanRelaStandardMap.put("isProcessEdit","是");
				}else{
					constructPlanRelaStandardMap.put("isProcessEdit","否");
				}
				datas.add(constructPlanRelaStandardMap);
			}
			map.put("datas", datas);
			map.put("totalCount", page.getTotalItems());
		} else {
			map.put("datas", new Object[0]);
			map.put("totalCount", "0");
		}
		return map;
	}
//	/**
//	 * <pre>
//	 * 查询计划对应的部门列表
//	 * </pre>
//	 * 
//	 * @author 黄晨曦
//	 * @param limit 每夜显示条数
//	 * @param start 起始条数
//	 * @param query 查询条件
//	 * @param assessPlanID 流程ID
//	 * @return
//	 * @since fhd　Ver 1.1
//	 */
//	@ResponseBody
//	@RequestMapping("icm/assess/findOrgListByPage.f")
//	public Map<String, Object> findOrgListByPage(int limit, int start,String query, String assessPlanId) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
//
//		Page<AssessPlanRelaProcess> page = new Page<AssessPlanRelaProcess>();
//		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
//		page.setPageSize(limit);
//		page = o_constructPlanRelaStandardBO.findAssessPlanRelaProcessListByPage(page, query, assessPlanId);
//		List<AssessPlanRelaProcess> assessPlanRelaProcessList = page.getResult();
//		if (null != assessPlanRelaProcessList&& assessPlanRelaProcessList.size() > 0) {
//			Map<String, Object> assessPlanRelaProcessMap = null;
//			Set<SysOrganization> sysOrganizationSet = new HashSet<SysOrganization>();
//			for (AssessPlanRelaProcess assessPlanRelaProcess : assessPlanRelaProcessList) {
//				Set<ProcessRelaOrg> processRelaOrgSet = assessPlanRelaProcess.getProcess().getProcessRelaOrg();
//				for (ProcessRelaOrg processRelaOrg : processRelaOrgSet) {
//					if (null != processRelaOrg.getOrg()) {
//						sysOrganizationSet.add(processRelaOrg.getOrg());
//					}
//				}
//			}
//			for (SysOrganization org : sysOrganizationSet) {
//				assessPlanRelaProcessMap = new HashMap<String, Object>();
//				assessPlanRelaProcessMap.put("orgId", org.getId());
//				assessPlanRelaProcessMap.put("orgName", org.getOrgname());
//				datas.add(assessPlanRelaProcessMap);
//			}
//			map.put("datas", datas);
//			map.put("totalCount", page.getTotalItems());
//		} else {
//			map.put("datas", new Object[0]);
//			map.put("totalCount", "0");
//		}
//		return map;
//	}
//	/**
//	 * <pre>
//	 * 查询计划对应的流程列表
//	 * </pre>
//	 * 
//	 * @author 黄晨曦
//	 * @param limit 每夜显示条数
//	 * @param start 起始条数
//	 * @param query 查询条件
//	 * @param assessPlanID流程ID
//	 * @return
//	 * @since fhd　Ver 1.1
//	 */
//	@ResponseBody
//	@RequestMapping("icm/assess/findProcessListByPage.f")
//	public Map<String, Object> findAssessPlanRelaPointListbyPage(int limit,int start, String query, String assessPlanID) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
//
//		Page<AssessPlanRelaProcess> page = new Page<AssessPlanRelaProcess>();
//		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
//		page.setPageSize(limit);
//		page = o_constructPlanRelaStandardBO.findAssessPlanRelaProcessListByPage(page, query,assessPlanID);
//		List<AssessPlanRelaProcess> assessPlanRelaProcessList = page.getResult();
//		if (null != assessPlanRelaProcessList&& assessPlanRelaProcessList.size() > 0) {
//			Map<String, Object> assessPlanRelaProcessMap = null;
//			for (AssessPlanRelaProcess assessPlanRelaProcess : assessPlanRelaProcessList) {
//				assessPlanRelaProcessMap = new HashMap<String, Object>();
//				assessPlanRelaProcessMap.put("processId", assessPlanRelaProcess.getProcess().getId());
//				assessPlanRelaProcessMap.put("processName",assessPlanRelaProcess.getProcess().getName());
//				datas.add(assessPlanRelaProcessMap);
//			}
//			map.put("datas", datas);
//			map.put("totalCount", page.getTotalItems());
//		} else {
//			map.put("datas", new Object[0]);
//			map.put("totalCount", "0");
//		}
//		return map;
//	}

	
	/**
	 * <pre>
	 * 分页查询AssessPlanRelaProcess
	 * 计划创建完成之后，分配任务时，查询计划相关的流程信息
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param limit 每页显示数目
	 * @param start 起始数目
	 * @param query 查询条件
	 * @param assessPlanID 计划ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
//	@ResponseBody
//	@RequestMapping("/icm/assess/findAssessPlanRelaProcessListByPageForAllocation.f")
//	public Map<String,Object> findAssessPlanRelaProcessListByPageForAllocation(int limit, int start, String query,String assessPlanId){
//		Map<String, Object> map = new HashMap<String, Object>();
//		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
//		
//		Page<AssessPlanRelaProcess> page = new Page<AssessPlanRelaProcess>();
//		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
//		page.setPageSize(limit);
//		page = o_constructPlanRelaStandardBO.findAssessPlanRelaProcessListByPage(page, query, assessPlanId);
//		List<AssessPlanRelaProcess> assessPlanRelaProcessList = page.getResult();
//		if(null != assessPlanRelaProcessList && assessPlanRelaProcessList.size()>0){
//			Map<String, Object> assessPlanRelaProcessMap = null;
//			for(AssessPlanRelaProcess assessPlanRelaProcess : assessPlanRelaProcessList){
//				assessPlanRelaProcessMap = new HashMap<String, Object>();
//				assessPlanRelaProcessMap.put("assessPlanRelaProcessId", assessPlanRelaProcess.getId());
//				
//				if (null != assessPlanRelaProcess.getProcess()) {// 显示末级流程名称
//					assessPlanRelaProcessMap.put("processId",assessPlanRelaProcess.getProcess().getId());// 末级流程Id
//					assessPlanRelaProcessMap.put("processName",assessPlanRelaProcess.getProcess().getName());
//					if (null != assessPlanRelaProcess.getProcess().getParent()) {// 显示二级流程分类名称
//						assessPlanRelaProcessMap.put("parentProcessName",assessPlanRelaProcess.getProcess().getParent().getName());
//						if (null != assessPlanRelaProcess.getProcess().getParent().getParent()) {// 显示一级流程分类名称
//							assessPlanRelaProcessMap.put("firstProcessName",assessPlanRelaProcess.getProcess().getParent().getParent().getName());
//						}
//					}
//				}
//				
//				Set<ProcessRelaOrg> processRelaOrgSet = assessPlanRelaProcess.getProcess().getProcessRelaOrg();
//				Set<String> orgNameSet = new HashSet<String>();
//				for (ProcessRelaOrg processRelaOrg : processRelaOrgSet) {
//					if(Contents.ORG_RESPONSIBILITY.equals(processRelaOrg.getType()) && null != processRelaOrg.getOrg()){
//						orgNameSet.add(processRelaOrg.getOrg().getOrgname());
//					}
//				}
//				assessPlanRelaProcessMap.put("orgName", StringUtils.join(orgNameSet, ","));
//				Set<AssessPlanProcessRelaOrgEmp> assessPlanProcessRelaOrgEmpSet = assessPlanRelaProcess.getAssessPlanProcessRelaOrgEmp();
//				for (AssessPlanProcessRelaOrgEmp assessPlanProcessRelaOrgEmp : assessPlanProcessRelaOrgEmpSet) {
//					if(Contents.EMP_HANDLER.equals(assessPlanProcessRelaOrgEmp.getType()) && null !=assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp()){
//						assessPlanRelaProcessMap.put("handlerId", assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp().getId());
//					}
//					if(Contents.EMP_REVIEW_PERSON.equals(assessPlanProcessRelaOrgEmp.getType()) && null !=assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp()){
//						assessPlanRelaProcessMap.put("reviewerId", assessPlanProcessRelaOrgEmp.getAssessPlanRelaOrgEmp().getId());
//					}
//				}
//				assessPlanRelaProcessMap.put("planStartDate", assessPlanRelaProcess.getAssessPlan().getPlanStartDate());
//				assessPlanRelaProcessMap.put("planEndDate", assessPlanRelaProcess.getAssessPlan().getPlanEndDate());
//				datas.add(assessPlanRelaProcessMap);
//			}
//			map.put("datas", datas);
//			map.put("totalCount", page.getTotalItems());
//		}else{
//			map.put("datas", new Object[0]);
//			map.put("totalCount", "0");
//		}
//		return map;
//	} 
	/**
	 * 查询出流程中所包含所有的节点，作为数据字典展示
	 * @param typeId
	 * @author 宋佳
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/icm/icsystem/findallempbyconstructplanid.f")
	public List<Map<String,String>> findEmpByConstructPlanId(String constructPlanId){
		//查询出计划所对应的的负责人，作为stroe中的数据
		List<ConstructPlanRelaOrg> list=o_constructPlanRelaStandardBO.findConstructPlanEmp(constructPlanId);
		List<Map<String, String>> l=new ArrayList<Map<String, String>>();
		for(ConstructPlanRelaOrg empEntry : list)
		{
			Map<String,String> mapEmp=new HashMap<String,String>();
			mapEmp.put("id", empEntry.getId());
			mapEmp.put("name",empEntry.getEmp().getEmpname());
	    	//l.add(map);
			l.add(mapEmp);
		 }
		
		return l;
	}
}

