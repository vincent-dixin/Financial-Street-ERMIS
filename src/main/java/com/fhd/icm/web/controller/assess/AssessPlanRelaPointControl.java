package com.fhd.icm.web.controller.assess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.core.dao.Page;
import com.fhd.fdc.utils.Contents;
import com.fhd.icm.business.assess.AssessPlanRelaPointBO;
import com.fhd.icm.entity.assess.AssessPlanPointRelaOrgEmp;
import com.fhd.icm.entity.assess.AssessPlanRelaPoint;
import com.fhd.process.entity.ProcessRelaOrg;

/**
 * @author   黄晨曦
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-1-9		上午11:14:40
 *
 * @see 	 
 */
@Controller
public class AssessPlanRelaPointControl {

	@Autowired
	private AssessPlanRelaPointBO o_assessPlanRelaPointBO;
	
	/**
	 * <pre>
	 *保存AssessPlanRelaOrgEmp
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param jsonString AssessPlanRelaOrgEmp信息，格式[{assessPlanRelaPointId:'11111',empIdHandler:'dfasdf'，……},{...}]
	 * @param assessPlanId 评价计划ID
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping("icm/assess/mergeAssessPlanPointRelaOrgEmpBatch.f")
	public void mergeAssessPlanPointRelaOrgEmpBatch(String jsonString){
		o_assessPlanRelaPointBO.mergeAssessPlanPointRelaOrgEmpBatch(jsonString);
		
		
	}
	
	/**
	 * <pre>
	 * 批量保存参与评价的人：
	 * 按流程批量保存参与评价的人的信息，当batchType为process，时，例如：[{processId:'23311',empIdHandler:'daf3dfv'},{...}]；
	 * 按部门批量保存参与评价的人的信息，当batchType为org时，例如：[{orgId:'23311',empIdHandler:'daf3dfv'},{...}]
	 * </pre>
	 * 
	 * @author 黄晨曦
	 * @param assessPlanId 评价计划ID
	 * @param jsonString 保存信息的字符串
	 * @param batchType 批量保持的依据：process,org
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/assess/mergerAssessPlanPointRelaOrgEmpBatch.f")
	public Boolean mergerAssessPlanPointRelaOrgEmpBatch(String assessPlanId,String jsonString, String batchType) {
		o_assessPlanRelaPointBO.mergerAssessPlanPointRelaOrgEmpBatch(assessPlanId,jsonString, batchType);
		return true;
	}


	
	/**
	 * <pre>
	 * 分页查询AssessPlanRelaPoint
	 * 计划创建完成之后，分配任务时，查询计划相关的评价点信息
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
	@ResponseBody
	@RequestMapping("icm/assess/findAssessPlanRelaPointListByPage.f")
	public Map<String,Object> findAssessPlanRelaPointListbyPage(int limit, int start, String query,String assessPlanID,String assessPlanRelaProcessId){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		
		Page<AssessPlanRelaPoint> page = new Page<AssessPlanRelaPoint>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		page = o_assessPlanRelaPointBO.findAssessPlanRelaPointListByPage(page, query, assessPlanID,assessPlanRelaProcessId);
		List<AssessPlanRelaPoint> assessPlanRelaPointList = page.getResult();
		if(null != assessPlanRelaPointList && assessPlanRelaPointList.size()>0){
			Map<String, Object> assessPlanRelaPointMap = null;
			for(AssessPlanRelaPoint assessPlanRelaPoint : assessPlanRelaPointList){
				assessPlanRelaPointMap = new HashMap<String, Object>();
				assessPlanRelaPointMap.put("assessPlanRelaPointId", assessPlanRelaPoint.getId());
				assessPlanRelaPointMap.put("processName", assessPlanRelaPoint.getProcess().getName());
				Set<ProcessRelaOrg> processRelaOrgSet = assessPlanRelaPoint.getProcess().getProcessRelaOrg();
				Set<String> orgNameSet = new HashSet<String>();
				for (ProcessRelaOrg processRelaOrg : processRelaOrgSet) {
					if(Contents.ORG_RESPONSIBILITY.equals(processRelaOrg.getType()) && null != processRelaOrg.getOrg()){
						orgNameSet.add(processRelaOrg.getOrg().getOrgname());
					}
				}
				assessPlanRelaPointMap.put("orgName", StringUtils.join(orgNameSet, ","));
				assessPlanRelaPointMap.put("assessPointName",assessPlanRelaPoint.getAssessPoint().getDesc());
				Set<AssessPlanPointRelaOrgEmp> assessPlanPointRelaOrgEmpSet = assessPlanRelaPoint.getAssessPlanPointRelaOrgEmp();
				if(null != assessPlanPointRelaOrgEmpSet && assessPlanPointRelaOrgEmpSet.size()>0){
					for (AssessPlanPointRelaOrgEmp assessPlanPointRelaOrgEmp : assessPlanPointRelaOrgEmpSet) {
						if(Contents.EMP_HANDLER.equals(assessPlanPointRelaOrgEmp.getType()) && null !=assessPlanPointRelaOrgEmp.getAssessPlanRelaOrgEmp()){
							assessPlanRelaPointMap.put("handlerId", assessPlanPointRelaOrgEmp.getAssessPlanRelaOrgEmp().getEmp().getEmpname()+"("+assessPlanPointRelaOrgEmp.getAssessPlanRelaOrgEmp().getEmp().getEmpcode()+")");
						}
						if(Contents.EMP_REVIEW_PERSON.equals(assessPlanPointRelaOrgEmp.getType()) && null !=assessPlanPointRelaOrgEmp.getAssessPlanRelaOrgEmp()){
							assessPlanRelaPointMap.put("reviewerId", assessPlanPointRelaOrgEmp.getAssessPlanRelaOrgEmp().getEmp().getEmpname()+"("+assessPlanPointRelaOrgEmp.getAssessPlanRelaOrgEmp().getEmp().getEmpcode()+")");
						}
					}
				}
				datas.add(assessPlanRelaPointMap);
			}
			map.put("datas", datas);
			map.put("totalCount", page.getTotalItems());
		}else{
			map.put("datas", new Object[0]);
			map.put("totalCount", "0");
		}
		return map;
	} 
	
}

