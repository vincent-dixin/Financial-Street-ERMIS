/**
 * ImprovePlanControl.java
 * com.fhd.icm.web.controller.rectify
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-3-4 		张 雷
 *
 * Copyright (c) 2013, Firsthuida All Rights Reserved.
*/

package com.fhd.icm.web.controller.rectify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.core.dao.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.icm.business.bpm.ImproveBpmBO;
import com.fhd.icm.business.defect.DefectRelaImproveBO;
import com.fhd.icm.business.rectify.ImprovePlanBO;
import com.fhd.icm.business.rectify.ImprovePlanRelaDefectBO;
import com.fhd.icm.entity.defect.Defect;
import com.fhd.icm.entity.defect.DefectRelaImprove;
import com.fhd.icm.entity.defect.DefectRelaOrg;
import com.fhd.icm.entity.rectify.DefectTrace;
import com.fhd.icm.entity.rectify.ImprovePlan;
import com.fhd.icm.entity.rectify.ImprovePlanFile;
import com.fhd.icm.entity.rectify.ImprovePlanRelaDefect;
import com.fhd.icm.entity.rectify.ImprovePlanRelaOrg;
import com.fhd.icm.entity.rectify.ImproveRelaPlan;
import com.fhd.icm.entity.rectify.ImproveTrace;
import com.fhd.icm.web.controller.bpm.ImproveBpmObject;
import com.fhd.icm.web.form.DefectTraceForm;
import com.fhd.icm.web.form.rectify.ImproveTraceForm;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-3-4		下午3:29:26
 *
 * @see 	 
 */
@Controller
public class ImprovePlanControl {
	@Autowired
	private DefectRelaImproveBO o_defectRelaImproveBO;
	@Autowired
	private ImprovePlanBO o_improvePlanBO;
	@Autowired
	private ImprovePlanRelaDefectBO o_improvePlanRelaDefectBO;
	@Autowired
	private ImproveBpmBO o_improveBpmBO;
	
	
	/**
	 * <pre>
	 *保存整改进度
	 * </pre>
	 * 
	 * @author 李克东
	 * @param improveForm
	 * @param response
	 * @throws IOException
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping(value="/icm/rectify/saveRectifySchedule.f")
	public Map<String, Object> saveRectifySchedule(ImproveTraceForm improveTraceForm,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		ImproveTrace improveTrace = new ImproveTrace();
		improveTrace.setImprovePlan(new ImprovePlan(improveTraceForm.getImprovePlanId()));
		improveTrace.setImproveResult(improveTraceForm.getImproveResult());
		improveTrace.setFinishRate(improveTraceForm.getFinishRate());
		o_improvePlanBO.saveImproveTrace(improveTrace);
        result.put("success", true);
        return result;
	}
	
	/**
	 * <pre>
	 * 保存整改复核
	 * </pre>
	 * 
	 * @author 李克东,张雷
	 * @param defectTraceForm
	 * @param response
	 * @throws IOException
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping(value="/icm/rectify/saveRectifyCheck.f")
	public Map<String, Object> saveRectifyCheck(DefectTraceForm defectTraceForm, BindingResult result, HttpServletResponse response){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		DefectTrace defectTrace = new DefectTrace(Identities.uuid());
		if(StringUtils.isNotBlank(defectTraceForm.getImprovePlanRelaDefectId())){
			defectTrace.setImprovePlanRelaDefect(new ImprovePlanRelaDefect(defectTraceForm.getImprovePlanRelaDefectId()));
			if(StringUtils.isNotBlank(defectTraceForm.getIsPassId())){
				defectTrace.setIsPass(new DictEntry(defectTraceForm.getIsPassId()));
			}
			if(StringUtils.isNotBlank(defectTraceForm.getAfterDefectLevelId())){
				defectTrace.setAfterDefectLevel(new DictEntry(defectTraceForm.getAfterDefectLevelId()));
			}
			defectTrace.setCompensationControl(defectTraceForm.getCompensationControl());
			defectTrace.setNextCheckDate(defectTraceForm.getNextCheckDate());
			defectTrace.setCheckResult(defectTraceForm.getCheckResult());
			o_improvePlanBO.mergeDefectTrace(defectTrace);
		}
		
		resultMap.put("success", true);
        return resultMap;
		
	}
	
	/**
	 * <pre>
	 * 批量保存整改方案信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param modifiedRecord 要保存数据
	 * @param improveId 整改计划ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
    @RequestMapping("/icm/improve/mergeimproveplanbatch.f")
    public Map<String, Object> mergeImprovePlanBatch(String modifiedRecord, String improveId){
        Map<String, Object> result = new HashMap<String, Object>();
        o_improvePlanBO.mergeImprovePlanBatch(improveId, modifiedRecord);
        result.put("success", true);
        return result;
    }
	
	/**
	 * <pre>
	 * 保存复核人信息
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param empId 人员信息
	 * @param improvePlanIds 整改方案信息ID的字符串
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
    @RequestMapping("/icm/improve/mergeimproveplansetreviewerbatch.f")
    public Map<String, Object> mergeImprovePlanSetReviewerBatch(String empId, String improvePlanIds){
        Map<String, Object> result = new HashMap<String, Object>();
        o_improvePlanBO.mergeImprovePlanSetReviewerBatch(empId, improvePlanIds);
        result.put("success", true);
        return result;
    }
	
	
	/**
	 * <pre>
	 * 删除已经设置了的复核人
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param improvePlanIds
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
    @RequestMapping("/icm/improve/removeImprovePlanReviewer.f")
    public Map<String, Object> removeImprovePlanReviewer(String improvePlanIds){
        Map<String, Object> result = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(improvePlanIds)){
			String[] improvePlanIdArray = improvePlanIds.split(",");
			List<String> improvePlanIdList = new ArrayList<String>();
			for (String improvePlanId : improvePlanIdArray) {
				improvePlanIdList.add(improvePlanId);
			}
			if(null != improvePlanIdList && improvePlanIdList.size()>0){//删除已经保存的整改方案的复核人
				o_improvePlanBO.removeImprovePlanRelaOrgByImprovePlanIdList(improvePlanIdList);
			}
			 result.put("success", true);
        }
        result.put("success", false);
        return result;
    }
	
	/**
	 * 查询整改计划ID为improveId的整改计划对应的整改方案
	 * </pre>
	 ** <pre>
	  
	 * @author 张 雷
	 * @param limit 页面容量
	 * @param start 起始值
	 * @param improveId 整改计划
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/improve/findimproveplanlistbyimproveid.f")
	public Map<String, Object> findImprovePlanListByImproveId(int limit, int start,String improveId) {
		Map<String,Object> resultMap=new HashMap<String,Object>();
		if(StringUtils.isBlank(improveId)){
			return resultMap;
		}
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Set<ImprovePlanRelaDefect> improvePlanRelaDefectSet = new HashSet<ImprovePlanRelaDefect>();
		Set<String> improvePlanIdSet = new HashSet<String>();
		Page<DefectRelaImprove> page = new Page<DefectRelaImprove>();
		limit = 9999;
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		page = o_defectRelaImproveBO.findDefectRelaImprovePageBySome(page, improveId);
		List<DefectRelaImprove> defectRelaImproveList = page.getResult();
		List<ImproveRelaPlan> improveRelaPlanList = o_improvePlanBO.findImproveRelaPlanListBySome(improveId,null);
		for (DefectRelaImprove defectRelaImprove : defectRelaImproveList) {
			Set<ImprovePlanRelaDefect> improvePlanRelaDefectTempSet = defectRelaImprove.getDefect().getImprovePlanRelaDefect();
			for (ImprovePlanRelaDefect improvePlanRelaDefect : improvePlanRelaDefectTempSet) {
				for (ImproveRelaPlan improveRelaPlan : improveRelaPlanList) {
					Set<DefectRelaOrg> defectRelaOrgSet = improvePlanRelaDefect.getDefect().getDefectRelaOrg();
					String onlineDeptId = UserContext.getUser().getMajorDeptId();
					String orgId = null;
					for (DefectRelaOrg defectRelaOrg : defectRelaOrgSet) {
						if(Contents.ORG_RESPONSIBILITY.equals(defectRelaOrg.getType())){
							orgId = defectRelaOrg.getOrg().getId();
							break;
						}
					}
					if(improvePlanRelaDefect.getImprovePlan().getId().equals(improveRelaPlan.getImprovePlan().getId()) && onlineDeptId.equals(orgId)){
						improvePlanIdSet.add(improvePlanRelaDefect.getImprovePlan().getId());
						improvePlanRelaDefectSet.add(improvePlanRelaDefect);
					}
				}
			}
		}
		
		List<ImprovePlanFile> improvePlanFileList = o_improvePlanBO.findImprovePlanFileListBySome(improvePlanIdSet);
		
		for (ImprovePlanRelaDefect improvePlanRelaDefect : improvePlanRelaDefectSet) {
			Defect defect = improvePlanRelaDefect.getDefect();
			ImprovePlan improvePlan = improvePlanRelaDefect.getImprovePlan();
			for (ImproveRelaPlan improveRelaPlan : improveRelaPlanList) {
				if(improveRelaPlan.getImprovePlan().getId().equals(improvePlan.getId())){
					Map<String, Object> defectRelaImproveMap = new HashMap<String, Object>();
					defectRelaImproveMap.put("desc", defect.getDesc());
					Set<DefectRelaOrg> defectRelaOrgSet = defect.getDefectRelaOrg();
					for (DefectRelaOrg defectRelaOrg : defectRelaOrgSet) {
						if(Contents.ORG_RESPONSIBILITY.equals(defectRelaOrg.getType())){
							defectRelaImproveMap.put("orgName", defectRelaOrg.getOrg().getOrgname());
							break;
						}
					}
					defectRelaImproveMap.put("id", improvePlan.getId());
					defectRelaImproveMap.put("content", improvePlan.getContent());
					defectRelaImproveMap.put("planStartDate", DateUtils.formatShortDate(improvePlan.getPlanStartDate()));
					defectRelaImproveMap.put("planEndDate", DateUtils.formatShortDate(improvePlan.getPlanEndDate()));
					for(int i=0;i<improvePlanFileList.size();i++){
						for (ImprovePlanFile improvePlanFile : improvePlanFileList) {
							if(improvePlan.getId().equals(improvePlanFile.getImprovePlan().getId())){
								defectRelaImproveMap.put("file", improvePlanFile.getFile().getNewFileName());	
								defectRelaImproveMap.put("fileId", improvePlanFile.getFile().getId());
							}
						}
					}
					resultList.add(defectRelaImproveMap);
				}
			}
		}
		//排序
		Collections.sort(resultList, new Comparator<Map<String,Object>>(){
			@Override 
			public int compare(Map<String,Object> arg0, Map<String,Object> arg1) { 
				int flag = arg0.get("id").toString().compareTo(arg1.get("id").toString());
				if(null != arg0.get("orgName") && null != arg1.get("orgName")){
					flag = arg0.get("orgName").toString().compareTo(arg1.get("orgName").toString());
					if(flag==0 && null != arg0.get("desc") && null != arg1.get("desc")){
						return arg0.get("desc").toString().compareTo(arg1.get("desc").toString()); 
					}else{
						return flag;
					}
				}else{
					return flag;
				}
				
		 	}
		});
		
		resultMap.put("datas", resultList);
		resultMap.put("totalCount", resultList.size());
		return resultMap;
	}
	/**
	 * 查询整改计划ID为improveId的整改计划对应的整改方案和整改复核人
	 * </pre>
	 ** <pre>
	  
	 * @author 张 雷
	 * @param limit 页面容量
	 * @param start 起始值
	 * @param improveId 整改计划
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/improve/findimproveplanlistandreviewerbyimproveid.f")
	public Map<String, Object> findImprovePlanListAndReviewerByImproveId(int limit, int start, String improveId) {
		Map<String,Object> resultMap=new HashMap<String,Object>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Page<DefectRelaImprove> page = new Page<DefectRelaImprove>();
		Set<ImprovePlanRelaDefect> improvePlanRelaDefectSet = new HashSet<ImprovePlanRelaDefect>();
		Set<String> improvePlanIdSet = new HashSet<String>();
		limit = 9999;
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		page = o_defectRelaImproveBO.findDefectRelaImprovePageBySome(page, improveId);
		List<DefectRelaImprove> defectRelaImproveList = page.getResult();
		List<ImproveRelaPlan> improveRelaPlanList = o_improvePlanBO.findImproveRelaPlanListBySome(improveId,null);
		for (DefectRelaImprove defectRelaImprove : defectRelaImproveList) {
			Set<ImprovePlanRelaDefect> improvePlanRelaDefectTempSet = defectRelaImprove.getDefect().getImprovePlanRelaDefect();
			for (ImprovePlanRelaDefect improvePlanRelaDefect : improvePlanRelaDefectTempSet) {
				for (ImproveRelaPlan improveRelaPlan : improveRelaPlanList) {
					if(improvePlanRelaDefect.getImprovePlan().getId().equals(improveRelaPlan.getImprovePlan().getId())){
						improvePlanIdSet.add(improvePlanRelaDefect.getImprovePlan().getId());
						improvePlanRelaDefectSet.add(improvePlanRelaDefect);
					}
				}
			}
		}
	
		List<ImprovePlanFile> improvePlanFileList = o_improvePlanBO.findImprovePlanFileListBySome(improvePlanIdSet);
		if(improvePlanRelaDefectSet.size()>0){
			for (ImprovePlanRelaDefect improvePlanRelaDefect : improvePlanRelaDefectSet) {
				Defect defect = improvePlanRelaDefect.getDefect();
				ImprovePlan improvePlan = improvePlanRelaDefect.getImprovePlan();
				for (ImproveRelaPlan improveRelaPlan : improveRelaPlanList) {
					if(improveRelaPlan.getImprovePlan().getId().equals(improvePlan.getId())){
						Map<String, Object> defectRelaImproveMap = new HashMap<String, Object>();
						defectRelaImproveMap.put("desc", defect.getDesc());
						Set<DefectRelaOrg> defectRelaOrgSet = defect.getDefectRelaOrg();
						for (DefectRelaOrg defectRelaOrg : defectRelaOrgSet) {
							if(Contents.ORG_RESPONSIBILITY.equals(defectRelaOrg.getType())){
								defectRelaImproveMap.put("orgName", defectRelaOrg.getOrg().getOrgname());
								break;
							}
						}
						defectRelaImproveMap.put("id", improvePlan.getId());
						StringBuffer empName = new StringBuffer();
						if(null != improvePlan.getLastModifyBy()){
							empName.append(improvePlan.getLastModifyBy().getEmpname()).append(" ( ").append(improvePlan.getLastModifyBy().getEmpcode()).append(" ) "); 
						}
						defectRelaImproveMap.put("lastModifyBy", empName.toString());
						defectRelaImproveMap.put("content", improvePlan.getContent());
						defectRelaImproveMap.put("planStartDate", DateUtils.formatShortDate(improvePlan.getPlanStartDate()));
						defectRelaImproveMap.put("planEndDate", DateUtils.formatShortDate(improvePlan.getPlanEndDate()));
						List<ImproveTrace> imporveTraceList =  o_improvePlanBO.findImproveTraceBySome(improvePlan.getId());
						if(null != imporveTraceList && imporveTraceList.size()>0){
							defectRelaImproveMap.put("finishRate", imporveTraceList.get(0).getFinishRate());
							defectRelaImproveMap.put("improveResult", imporveTraceList.get(0).getImproveResult());
						}
						for(int i=0;i<improvePlanFileList.size();i++){
							for (ImprovePlanFile improvePlanFile : improvePlanFileList) {
								if(improvePlan.getId().equals(improvePlanFile.getImprovePlan().getId())){
									defectRelaImproveMap.put("file", improvePlanFile.getFile().getNewFileName());	
									defectRelaImproveMap.put("fileId", improvePlanFile.getFile().getId());
								}
								
							}
						}
						Set<ImprovePlanRelaOrg> improvePlanRelaOrgSet = improvePlan.getImprovePlanRelaOrg();
						for (ImprovePlanRelaOrg improvePlanRelaOrg : improvePlanRelaOrgSet) {
							if(Contents.EMP_REVIEW_PERSON.equals(improvePlanRelaOrg.getType())){
								StringBuffer reviewer = new StringBuffer();
								if(null != improvePlanRelaOrg.getEmp()){
									reviewer.append(improvePlanRelaOrg.getEmp().getEmpname()).append(" ( ").append(improvePlanRelaOrg.getEmp().getEmpcode()).append(" ) "); 
								}
								defectRelaImproveMap.put("reviewer", reviewer.toString());
								defectRelaImproveMap.put("empId", improvePlanRelaOrg.getEmp().getId());
							}
						}
						resultList.add(defectRelaImproveMap);
						break;
					}
				}
			}
		}else{
			for (DefectRelaImprove defectRelaImprove : defectRelaImproveList) {
				Defect defect = defectRelaImprove.getDefect();
				Map<String, Object> defectRelaImproveMap = new HashMap<String, Object>();
				defectRelaImproveMap.put("desc", defect.getDesc());
				Set<DefectRelaOrg> defectRelaOrgSet = defect.getDefectRelaOrg();
				for (DefectRelaOrg defectRelaOrg : defectRelaOrgSet) {
					if(Contents.ORG_RESPONSIBILITY.equals(defectRelaOrg.getType())){
						defectRelaImproveMap.put("orgName", defectRelaOrg.getOrg().getOrgname());
						break;
					}
				}
				defectRelaImproveMap.put("id", defectRelaImprove.getId());
				resultList.add(defectRelaImproveMap);
			}
		}

		//排序
		Collections.sort(resultList, new Comparator<Map<String,Object>>(){
			@Override 
			public int compare(Map<String,Object> arg0, Map<String,Object> arg1) { 
				int flag = arg0.get("id").toString().compareTo(arg1.get("id").toString());
				if(null != arg0.get("orgName") && null != arg1.get("orgName")){
					flag = arg0.get("orgName").toString().compareTo(arg1.get("orgName").toString());
					if(flag==0 && null != arg0.get("desc") && null != arg1.get("desc")){
						return arg0.get("desc").toString().compareTo(arg1.get("desc").toString()); 
					}else{
						return flag;
					}
				}else{
					return flag;
				}
				
		 	}
		});
		
		resultMap.put("datas", resultList);
		resultMap.put("totalCount", resultList.size());
		return resultMap;
	}
	
	
	/**
	 * <pre>
	 * 查询整改进度表单
	 * </pre>
	 * 
	 * @author 李克东
	 * @param improveId 暂无用
	 * @param executionId 流程实例ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/improve/findimproveplanFormbyimproveid.f")
	public Map<String, Object> findImprovePlanFormByImproveId(String improveId, String executionId) {
		Map<String,Object> resultMap=new HashMap<String,Object>();
		ImproveBpmObject bpmObject = o_improveBpmBO.findBpmObjectByExecutionId(executionId);
		ImprovePlanRelaDefect improvePlanRelaDefect = o_improvePlanRelaDefectBO.findImprovePlanRelaDefectById(bpmObject.getImprovePlanRelaDefectId());
		Set<String> improvePlanIdSet = new HashSet<String>();
		improvePlanIdSet.add(improvePlanRelaDefect.getImprovePlan().getId());
		List<ImprovePlanFile> improvePlanFileList = o_improvePlanBO.findImprovePlanFileListBySome(improvePlanIdSet);
		Defect defect = improvePlanRelaDefect.getDefect();
		ImprovePlan improvePlan = improvePlanRelaDefect.getImprovePlan();
		Map<String, Object> defectRelaImproveMap = new HashMap<String, Object>();
		if(null != defect && null != improvePlan){
			defectRelaImproveMap.put("desc", defect.getDesc());
			Set<DefectRelaOrg> defectRelaOrgSet = defect.getDefectRelaOrg();
			for (DefectRelaOrg defectRelaOrg : defectRelaOrgSet) {
				if(Contents.ORG_RESPONSIBILITY.equals(defectRelaOrg.getType())){
					defectRelaImproveMap.put("orgName", defectRelaOrg.getOrg().getOrgname());
					break;
				}
			}
			defectRelaImproveMap.put("improvePlanId", improvePlan.getId());
			defectRelaImproveMap.put("content", improvePlan.getContent());
			defectRelaImproveMap.put("planStartDate", DateUtils.formatShortDate(improvePlan.getPlanStartDate()));
			defectRelaImproveMap.put("planEndDate", DateUtils.formatShortDate(improvePlan.getPlanEndDate()));
			
			StringBuffer empName = new StringBuffer();
			if(null != improvePlan.getLastModifyBy()){
				empName.append(improvePlan.getLastModifyBy().getEmpname()).append(" ( ").append(improvePlan.getLastModifyBy().getEmpcode()).append(" ) "); 
			}
			
			defectRelaImproveMap.put("planCreateBy", empName.toString());
			defectRelaImproveMap.put("planCreateTime", (null != improvePlan.getLastModifyTime())?DateUtils.formatShortDate(improvePlan.getLastModifyTime()):(DateUtils.formatShortDate(improvePlan.getCreateTime())));
			List<ImproveTrace> imporveTraceList =  o_improvePlanBO.findImproveTraceBySome(improvePlan.getId());
			if(null != imporveTraceList && imporveTraceList.size()>0){
				defectRelaImproveMap.put("finishRate", imporveTraceList.get(0).getFinishRate());
				defectRelaImproveMap.put("improveResult", imporveTraceList.get(0).getImproveResult());
			}
			for(int i=0;i<improvePlanFileList.size();i++){
				for (ImprovePlanFile improvePlanFile : improvePlanFileList) {
					if(improvePlan.getId().equals(improvePlanFile.getImprovePlan().getId())){
						defectRelaImproveMap.put("file", improvePlanFile.getFile().getNewFileName());	
						defectRelaImproveMap.put("fileId", improvePlanFile.getFile().getId());
					}
					
				}
				
			}
			resultMap.put("data", defectRelaImproveMap);
			resultMap.put("success", true);
			return resultMap;
		}else{
			resultMap.put("data", defectRelaImproveMap);
			resultMap.put("success", false);
			return resultMap;
		}
	}
	
	
	
	
	/**
	 * <pre>
	 * 查询最终的整改进度情况，以备复核用
	 * </pre>
	 * 
	 * @author 李克东
	 * @param improveId 整改计划ID
	 * @param executionId 流程实例ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("/icm/improve/findimproveplanCheckbyimproveid.f")
	public Map<String, Object> findImprovePlanCheckByImproveId(String improveId ,String executionId) {
		Map<String,Object> resultMap=new HashMap<String,Object>();
		ImproveBpmObject bpmObject = o_improveBpmBO.findBpmObjectByExecutionId(executionId);
		ImprovePlanRelaDefect improvePlanRelaDefect = o_improvePlanRelaDefectBO.findImprovePlanRelaDefectById(bpmObject.getImprovePlanRelaDefectId());
		Set<String> improvePlanIdSet = new HashSet<String>();
		improvePlanIdSet.add(improvePlanRelaDefect.getImprovePlan().getId());
		Defect defect = improvePlanRelaDefect.getDefect();
		ImprovePlan improvePlan = improvePlanRelaDefect.getImprovePlan();
		Map<String, Object> defectRelaImproveMap = new HashMap<String, Object>();
		if(null != defect && null != improvePlan){
			List<ImproveTrace> imporveTraceList =  o_improvePlanBO.findImproveTraceBySome(improvePlan.getId());
			List<DefectTrace> defectTraceList = o_improvePlanBO.findDefectTraceBySome(bpmObject.getImprovePlanRelaDefectId(),improveId);
			defectRelaImproveMap.put("improvePlanRelaDefectId", bpmObject.getImprovePlanRelaDefectId());
			if(null != imporveTraceList && imporveTraceList.size()>0){
				defectRelaImproveMap.put("finishRate", imporveTraceList.get(0).getFinishRate());
				defectRelaImproveMap.put("improveResult", imporveTraceList.get(0).getImproveResult());
				StringBuffer empName = new StringBuffer();
				if(null != imporveTraceList.get(0).getCreateBy()){
					empName.append(imporveTraceList.get(0).getCreateBy().getEmpname()).append(" ( ").append(imporveTraceList.get(0).getCreateBy().getEmpcode()).append(" ) "); 
				}
				defectRelaImproveMap.put("imporveTraceEmpId", empName.toString());
				defectRelaImproveMap.put("imporveTraceCreateTime", DateUtils.formatShortDate(imporveTraceList.get(0).getCreateTime()));
			}
			if(null != defectTraceList && defectTraceList.size()>0){
				defectRelaImproveMap.put("afterDefectLevelId", null != defectTraceList.get(0).getAfterDefectLevel()?defectTraceList.get(0).getAfterDefectLevel().getId():"");
				defectRelaImproveMap.put("afterDefectLevelName", null != defectTraceList.get(0).getAfterDefectLevel()?defectTraceList.get(0).getAfterDefectLevel().getName():"");
				defectRelaImproveMap.put("checkResult", defectTraceList.get(0).getCheckResult());
				defectRelaImproveMap.put("compensationControl", defectTraceList.get(0).getCompensationControl());
				defectRelaImproveMap.put("isPassId", null != defectTraceList.get(0).getIsPass()?defectTraceList.get(0).getIsPass().getId():"");
				defectRelaImproveMap.put("isPassName", null != defectTraceList.get(0).getIsPass()?defectTraceList.get(0).getIsPass().getName():"");
				defectRelaImproveMap.put("nextCheckDate", DateUtils.formatShortDate(defectTraceList.get(0).getNextCheckDate()));
				//defectRelaImproveMap.put("", defectTraceList.get(0).get)//实际贡献值
			}else{
				defectRelaImproveMap.put("afterDefectLevelId", null != defect.getLevel()?defect.getLevel().getId():"");
			}
		}
		resultMap.put("data", defectRelaImproveMap);
		resultMap.put("success", true);
		return resultMap;
	}
	
}

