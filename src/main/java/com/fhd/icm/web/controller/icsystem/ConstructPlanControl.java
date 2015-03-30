package com.fhd.icm.web.controller.icsystem;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;

import com.fhd.bpm.business.JBPMBO;
import com.fhd.bpm.entity.JbpmHistProcinst;
import com.fhd.core.dao.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.fdc.utils.Contents;
import com.fhd.icm.business.chart.AngularGauge;
import com.fhd.icm.business.chart.ChartColumn2D;
import com.fhd.icm.business.icsystem.ConstructPlanBO;
import com.fhd.icm.dao.icsystem.ConstructPlanDAO;
import com.fhd.icm.entity.icsystem.ConstructPlan;
import com.fhd.icm.entity.icsystem.ConstructPlanRelaOrg;
import com.fhd.icm.entity.icsystem.ConstructRelaProcess;
import com.fhd.icm.entity.icsystem.Diagnoses;
import com.fhd.icm.utils.DaysUtils;
import com.fhd.icm.web.form.ConstructPlanForm;
/**
 * @author 宋佳
 * @version
 * @since Ver 1.1
 * @Date 2013-4-6 下午6:39:05
 * 
 * @see
 */
@Controller
public class ConstructPlanControl {
    
	@Autowired
	private ConstructPlanBO o_constructPlanBO;
	@Autowired
	private JBPMBO o_jbpmBO;
	
	/**
	 * 自动获取建设计划编号
	 * @author 宋佳
	 * @return Map<String, Object> 建设计划编号
	 * @since fhd　Ver 1.1
	 */
	
	@ResponseBody
	@RequestMapping("/icm/icsystem/createconstructplancode.f")
	public Map<String, Object> createconstructPlanCode() {
		String planCode = o_constructPlanBO.getPlanCode();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", planCode);
		resultMap.put("success", true);
		return resultMap;
	}
	
	/**
	 * 建设计划基本信息--保存功能.
	 * @author 宋佳
	 * @param form  建设计划form
	 * @param result
	 * @return Map<String, Object>
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/icsystem/mergeconstructplanbyform.f")
	public Map<String, Object> mergeConstructPlanByForm(ConstructPlanForm form,BindingResult result) {
		Map<String, Object> ret = new HashMap<String, Object>();
		String constructPlanId = o_constructPlanBO.mergeConstructPlanByForm(form);
		ret.put("success", true);
		ret.put("groupPersIdHid", form.getGroupPersId());
		ret.put("groupLeaderIdHid", form.getGroupLeaderId());
		ret.put("constructPlanId", constructPlanId);
		return ret;
}
	/**
	 * <pre>
	 *  工作流调用
	 *  工作流汇总
	 * </pre>
	 * 
	 * @author 宋佳
	 * @param constructPlanId
	 * @since  fhd　Ver 1.1
	*/
	public void mergeConstructPlanStatus(String id) {
		//将内控标准和内控标准下要求的处理状态置为已完成
		ConstructPlanDAO constructPlanDAO = ContextLoader.getCurrentWebApplicationContext().getBean(ConstructPlanDAO.class);
		ConstructPlan constructPlan = constructPlanDAO.get(id);
		constructPlan.setDealStatus(Contents.DEAL_STATUS_FINISHED);
		constructPlan.setStatus(Contents.STATUS_SOLVED);
		constructPlanDAO.merge(constructPlan);
	}
	/**
	 * 建设计划列表.
	 * @author 宋佳
	 * @param limit
	 * @param start
	 * @param sort
	 * @param dir
	 * @param query 查询条件
	 * @return Map<String, Object>
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/icsystem/constructplan/findconstructplansbypage.f")
	public Map<String, Object> findConstructPlansByPage(int limit, int start, String sort, String dir, String query,String status) {
		Map<String,Object> resultMap=new HashMap<String,Object>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Page<ConstructPlan> page = new Page<ConstructPlan>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		page = o_constructPlanBO.findConstructPlansByPage(page, sort, dir, query ,status);
		List<ConstructPlan> constructPlanList=page.getResult();
		if (null != constructPlanList && constructPlanList.size()> 0) {
			Map<String, Object> row = null;
			for (ConstructPlan constructPlan : constructPlanList) {
				row = new HashMap<String, Object>();
				row.put("id", constructPlan.getId());
				row.put("companyName", constructPlan.getCompany().getOrgname());
				row.put("code", constructPlan.getCode());
				row.put("name", constructPlan.getName());
				// 评价类型
				if (null != constructPlan.getType()) {
					row.put("type", constructPlan.getType().getName());
				}else{
					row.put("type", "");
				}
				//计划进度
				if(null != constructPlan.getPlanStartDate() && null != constructPlan.getPlanEndDate()){
					row.put("schedule", DaysUtils.calculateRate(constructPlan.getPlanStartDate(), new Date(), constructPlan.getPlanEndDate()));
				}else{
					row.put("schedule", "");
				}
				//实际进度
				JbpmHistProcinst jhp = o_jbpmBO.findJbpmHistProcinstByBusinessId(constructPlan.getId());
				if(null != jhp){
					Integer processPercent = o_jbpmBO.processRateOfProgress(jhp.getId_());
					row.put("actualProgress", processPercent);
				}
				//起止时间
				if(null!=constructPlan.getPlanStartDate() && null!=constructPlan.getPlanEndDate()){
					row.put("targetStartDate", DateUtils.formatDate(constructPlan.getPlanStartDate(),"yyyy-MM-dd"));
					row.put("targetEndDate", DateUtils.formatDate(constructPlan.getPlanEndDate(),"yyyy-MM-dd"));
				}else{
					row.put("targetStartDate", "");
					row.put("targetEndDate", "");
				}
				// 状态
				if(StringUtils.isNotBlank(constructPlan.getStatus())){
					row.put("status", constructPlan.getStatus());
				}else{
					row.put("status", "");
				}
				//执行状态
				if(StringUtils.isNotBlank(constructPlan.getDealStatus())){
					row.put("dealStatus", constructPlan.getDealStatus());
				}else{
					row.put("dealStatus", "");
				}
				//创建时间
				if(null != constructPlan.getCreateTime()){
					row.put("createTime", DateUtils.formatDate(constructPlan.getCreateTime(),"yyyy-MM-dd"));
				}else{
					row.put("createTime", "");
				}
				
				resultList.add(row);
			}
		}
		resultMap.put("datas", resultList);
		resultMap.put("totalCount", page.getTotalItems());
		return resultMap;
	}
	
	/**
	 * 建设计划预览 --所有字段为只读.
	 * @author 宋佳
	 * @param constructPlanId 建设计划ID
	 * @return Map<String, Object>
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/icsystem/findconstructplanforview.f")
	public Map<String, Object> findConstructPlanForView(String constructPlanId) {
		Map<String, Object> map=new HashMap<String,Object>();
		Map<String, Object> formMap = new HashMap<String, Object>();
		if(!StringUtils.isBlank(constructPlanId)){
			ConstructPlan constructPlan=o_constructPlanBO.findConstructPlanById(constructPlanId);
			formMap.put("codeView", constructPlan.getCode());// 设置编码
			formMap.put("nameView", constructPlan.getName());// 设置内控名称
			formMap.put("requirementView", constructPlan.getRequirement());//时间要求
			formMap.put("workTargetView", constructPlan.getWorkTarget());//目标
			formMap.put("planStartDateView", constructPlan.getPlanStartDate());//评价期始
			formMap.put("planEndDateView", constructPlan.getPlanEndDate());//评价期始
			if(null != constructPlan.getType()){// 评价类型:自评、他评
				formMap.put("typeView", constructPlan.getType().getName());
				formMap.put("typeViewHid", constructPlan.getType().getId());
			}
			if(null!=constructPlan.getModifyReason()){//评价方式：穿行、抽样、全部
				formMap.put("modifyReasonView", constructPlan.getModifyReason().getName());
			}
			//查询相应的机构关联表，并组装成json串
			List<ConstructPlanRelaOrg> constructPlanListGroupLeader=o_constructPlanBO.findAssessPlanRelaOrgEmpBySome(constructPlanId,true,false,Contents.EMP_RESPONSIBILITY);	
			List<ConstructPlanRelaOrg> constructListGroupPers=o_constructPlanBO.findAssessPlanRelaOrgEmpBySome(constructPlanId,true,false,Contents.EMP_HANDLER);
			String groupLeader = "";
			StringBuffer groupPers = new StringBuffer();
			if(constructPlanListGroupLeader.size()>0){
				//组长
				for(ConstructPlanRelaOrg emp:constructPlanListGroupLeader){
					if(null != emp.getEmp()){
						groupLeader = emp.getEmp().getEmpname()+"("+emp.getEmp().getEmpcode()+")";
					}
				}
			}
			if(constructListGroupPers.size()>0){
				//组成员
				for(ConstructPlanRelaOrg pers:constructListGroupPers){
					if(null != pers.getEmp()){
						groupPers.append(pers.getEmp().getEmpname()+"("+pers.getEmp().getEmpcode()+")");
					}
					groupPers.append("、");
				}
			}
			formMap.put("responsibilityEmp",groupLeader);//组长
			if(!StringUtils.isBlank(groupLeader)){
				formMap.put("handlerEmp",groupPers.toString().substring(0, groupPers.toString().length()-1));//组成员
			}
		}
		map.put("data", formMap);
		map.put("success", true);
		return map;
	}
	
	/**
	 * 初始化form.
	 * @author 宋佳
	 * @param constructPlanId
	 * @return Map<String, Object>
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/icsystem/findconstructplan.f")
	public Map<String, Object> findConstructPlan(String constructPlanId) {
		ConstructPlan constructPlan=o_constructPlanBO.findConstructPlanById(constructPlanId);
		Map<String, Object> map=new HashMap<String,Object>();
		Map<String, Object> formMap = new HashMap<String, Object>();
		formMap.put("id", constructPlan.getId());// id
		formMap.put("code", constructPlan.getCode());// 设置编码
		formMap.put("name", constructPlan.getName());// 设置内控名称
		formMap.put("requirement", constructPlan.getRequirement());//时间要求
		formMap.put("workTarget", constructPlan.getWorkTarget());//目标
		formMap.put("planStartDate", constructPlan.getPlanStartDate());//评价期始
		formMap.put("planEndDate", constructPlan.getPlanEndDate());//评价期始
		if(null != constructPlan.getType()){// 评价类型:自评、他评
			formMap.put("type", constructPlan.getType().getId());
		}
		if(null!=constructPlan.getModifyReason()){//评价方式：穿行、抽样、全部
			formMap.put("modifyReason", constructPlan.getModifyReason().getId());
		}
		//查询相应的机构关联表，并组装成json串
		List<ConstructPlanRelaOrg> constructPlanListGroupLeader=o_constructPlanBO.findAssessPlanRelaOrgEmpBySome(constructPlanId,true,false,Contents.EMP_RESPONSIBILITY);	
		List<ConstructPlanRelaOrg> constructListGroupPers=o_constructPlanBO.findAssessPlanRelaOrgEmpBySome(constructPlanId,true,false,Contents.EMP_HANDLER);
		List<Map<String,Object>> groupLeaderJsonArray=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> groupPersJsonArray=new ArrayList<Map<String,Object>>();
		if(constructPlanListGroupLeader.size()>0){
			//组长
			for(ConstructPlanRelaOrg emp:constructPlanListGroupLeader){
				Map<String,Object> groupLeaderMap=new HashMap<String,Object>();
				if(null != emp.getEmp()){
					groupLeaderMap.put("id", emp.getEmp().getId());
				}
				groupLeaderJsonArray.add(groupLeaderMap);
			}
		}
		if(constructListGroupPers.size()>0){
			//组成员
			for(ConstructPlanRelaOrg pers:constructListGroupPers){
				Map<String,Object> groupPersMap=new HashMap<String,Object>();
				if(null != pers.getEmp()){
					groupPersMap.put("id", pers.getEmp().getId());
				}
				groupPersJsonArray.add(groupPersMap);
			}
		}
		JSONArray jsonArrayGroupLeader=JSONArray.fromObject(groupLeaderJsonArray);
		JSONArray jsonArrayGroupPers=JSONArray.fromObject(groupPersJsonArray);
		formMap.put("groupLeaderId",jsonArrayGroupLeader.toString());//组长
		formMap.put("groupLeaderIdHid",jsonArrayGroupLeader.toString());//组长
		formMap.put("groupPersId",jsonArrayGroupPers.toString());//组成员
		formMap.put("groupPersIdHid",jsonArrayGroupPers.toString());//组成员
		map.put("data", formMap);
		map.put("success", true);
		return map;
	}
	
	/**
	 * 内控评价计划列表--删除功能.
	 * @author 宋佳
	 * @param constructPlanIds
	 * @param response
	 * @throws IOException
	 * @since fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/icm/icsystem/removeconstructplanbyids.f")
	public Map<String, Object> removeConstructPlanByIds(String constructPlanIds){
		int delStatus = 0;
		Map<String, Object> returnMp = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(constructPlanIds)){
			String[] idArray = constructPlanIds.split(",");
			delStatus = o_constructPlanBO.removeConstructPlanByIdList(idArray);
		}
		if(delStatus!=0){
			returnMp.put("success", true);
		}else{
			returnMp.put("failure", false);
		}
		return returnMp;
	}
	/**
	 * <pre>
	 * 根据公司ID查询监控指标的xml
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param companyId 公司ID
	 * @return
	 * @since  fhd　Ver 1.1
	*/
    @ResponseBody
	@RequestMapping("/icm/icsystem/findconstructplanchartxmlbycomanyid.f")
	public Map<String, Object> findConstructPlanChartXmlByComanyId(String companyId,String constructPlanId) {
		Map<String, Object> map = new HashMap<String, Object>();
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		String finishRate = "0.00";
		//获取所有流程梳理类的任务
		List<Diagnoses> diagnosesList = o_constructPlanBO.findDiagnosesListBySome(companyId, constructPlanId);
		//获取所有合规诊断的任务
		List<ConstructRelaProcess> constructRelaProcessList = o_constructPlanBO.findProcessEditListBySome(companyId, constructPlanId);
//		List<ConstructPlan> constructPlanList = o_constructPlanBO.findConstructPlanListBySome(companyId);
		int finishDiagnoseCount = 0;   // 
		int finishProcessCount = 0;
		int diagnoseCount = 0;
		int processCount = 0;
		for (Diagnoses diagnoses : diagnosesList){
			diagnoseCount++;
			if(StringUtils.isNotBlank(diagnoses.getEstatus())){
				if(diagnoses.getEstatus().equals(Contents.DEAL_STATUS_FINISHED)){   //已经提交的计划
					finishDiagnoseCount ++;
				}
			}
		}
		for (ConstructRelaProcess constructRelaProcess : constructRelaProcessList){
			processCount ++;
			if(StringUtils.isNotBlank(constructRelaProcess.getEstatus())){
				if(constructRelaProcess.getEstatus().equals(Contents.DEAL_STATUS_FINISHED)){   //已经提交的计划
					finishProcessCount ++;
				}
			}
		}
		if((diagnoseCount+processCount) == 0){
			
		}else{
			finishRate = decimalFormat.format(((double)(finishDiagnoseCount+finishProcessCount)/(diagnoseCount+processCount))*100);
		}
		//流程信息
		List<Object[]> processInfoList = new ArrayList<Object[]>();
		//合规诊断
		List<Object[]> diagnosisInfoList = new ArrayList<Object[]>();
		
		map.put("finishRateXml", AngularGauge.getXml(finishRate, "","0"));
		map.put("finishRate", finishRate);
		map.put("processXml", ChartColumn2D.getXml("流程信息", "", "", "", processInfoList));
		map.put("diagnosisXml", ChartColumn2D.getXml("合规诊断", "", "", "", diagnosisInfoList));
		
		return map;
	}
}
