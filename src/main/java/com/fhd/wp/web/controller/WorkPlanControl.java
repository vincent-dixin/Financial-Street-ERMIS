/*
 * 北京第一会达风险管理有限公司 版权所有 2013
 * Copyright(C) 2013 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.wp.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.core.dao.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.sys.business.orgstructure.OrganizationBO;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.wp.business.WorkPlanBO;
import com.fhd.wp.entity.EnforcementOrgs;
import com.fhd.wp.entity.Milestone;
import com.fhd.wp.entity.WorkPlan;
import com.fhd.wp.web.form.WorkPlanForm;

/**
 * 工作计划
 *
 * @author   胡迪新
 * @since    fhd Ver 4.5
 * @Date	 2013-2-20  下午5:48:44
 *
 * @see 	 
 */
@Controller
public class WorkPlanControl {

	@Autowired
	private WorkPlanBO o_workPlanBO;
	@Autowired
	private OrganizationBO o_organizationBO;
	
	/**
	 * 
	 * <pre>
	 * 检查编号是否重复
	 * </pre> 
	 * 
	 * @author 胡迪新
	 * @param code
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/wp/validatecode.f")
	public Boolean validateCode(String code,String workPlanId) {
		Boolean flag = false;
		
		WorkPlan plan = o_workPlanBO.findWorkPlanByCode(code,workPlanId);
		if (null == plan) {
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 
	 * <pre>
	 * 保存工作计划及里程碑
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param workPlanForm
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/wp/saveworkplan.f")
	public Map<String, Object> saveWorkPlan(WorkPlanForm workPlanForm,String milestoneData) throws Exception {
		WorkPlan workPlan = convertWorkPlan(workPlanForm, milestoneData);
		
		workPlan.setStatus(new DictEntry(Contents.SAVE));
		
		o_workPlanBO.saveWorkPlan(workPlan);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		return map;
	}

	
	/**
	 * 
	 * <pre>
	 * 保存工作计划及里程碑
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param workPlanForm
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/wp/saveworkplansubmit.f")
	public Map<String, Object> saveWorkPlanSubmit(WorkPlanForm workPlanForm,String milestoneData,
			String transition,String opinion ,String processInstanceId,String superior) throws Exception {
		WorkPlan workPlan = convertWorkPlan(workPlanForm, milestoneData);
		
		workPlan.setStatus(new DictEntry(Contents.SUBMIT));
		
		o_workPlanBO.saveWorkPlanSubmit(workPlan,transition,opinion, processInstanceId, superior);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		return map;
	}
	
	/**
	 * 
	 * <pre>
	 * 删除工作计划（逻辑删除）
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param ids
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/wp/removeworkplan.f")
	public Map<String, Object> removeWorkPlan(String ids) {
		
		if(StringUtils.isNotEmpty(ids)) {
			o_workPlanBO.removeWorkPlan(ids);
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		return map;
	}
	
	
	
	/**
	 * 
	 * <pre>
	 * 工作计划列表
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param query 名称或编号
	 * @param start 
	 * @param limit 
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/wp/findworkplanlist.f")
	public Map<String, Object> findWorkPlanList(String query, int start, int limit) {
		
		Page<WorkPlan> page = new Page<WorkPlan>();
		page.setPageNo((limit == 0 ? 0 : start / limit)+1);
		page.setPageSize(limit);
		
		o_workPlanBO.findWorkPlanByPage(page,query);
		List<WorkPlan> result = page.getResult();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		for (WorkPlan workPlan : result) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("id", workPlan.getId());
			row.put("name", workPlan.getName());
			row.put("code", workPlan.getCode());
			row.put("startDate", DateUtils.formatShortDate(workPlan.getStartDate()));
			row.put("endDate", DateUtils.formatShortDate(workPlan.getEndDate()));
			row.put("createTime", DateUtils.formatShortDate(workPlan.getCreateTime()));
			row.put("status", workPlan.getStatus() != null ?  workPlan.getStatus().getName() : "");
			datas.add(row);
		}	
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);
		
		return map;
	}
	
	/**
	 * 
	 * <pre>
	 * 预览一个计划的基本信息
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param workPlanId
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/wp/findworkplanview.f")
	public Map<String, Object> findWorkPlanView(String workPlanId) {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> jsonMap = new HashMap<String, Object>();
		WorkPlan workPlan = o_workPlanBO.findWorkPlanById(workPlanId);

		jsonMap.put("id", workPlan.getId());
		jsonMap.put("company", workPlan.getCompany() != null ? workPlan.getCompany().getOrgname() : "");
		jsonMap.put("code", workPlan.getCode());
        jsonMap.put("name", workPlan.getName());
        jsonMap.put("startEndDate", DateUtils.formatShortDate(workPlan.getStartDate()) + "~" + DateUtils.formatShortDate(workPlan.getEndDate()));
        jsonMap.put("target", workPlan.getTarget());
        jsonMap.put("content", workPlan.getContent());
        jsonMap.put("assessRequirement", workPlan.getAssessRequirement());
        jsonMap.put("contributeTargetAmount", workPlan.getContributeTargetAmount());
        // 责任人
        jsonMap.put("responsilePersion", workPlan.getResponsilePersion() != null ? workPlan.getResponsilePersion().getEmpname() : "");
        
        // 实施部门
        Set<EnforcementOrgs> enforcementOrgs = workPlan.getEnforcementOrgses();
        if(null != enforcementOrgs){
        	List<String> orgNames = new ArrayList<String>();
        	for (EnforcementOrgs org : enforcementOrgs) {
        		orgNames.add(org.getOrg().getOrgname());
    		}
        	jsonMap.put("orgs", StringUtils.join(orgNames, ","));
        	
        }
        
		map.put("data", jsonMap);
		map.put("success", true);
		
		return map;
	}
	
	/**
	 * 
	 * <pre>
	 * 查询一个计划基本信息
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param workPlanId
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/wp/findworkplanbyid.f")
	public Map<String, Object> findWorkPlanById(String workPlanId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> jsonMap = new HashMap<String, Object>();
		WorkPlan workPlan = o_workPlanBO.findWorkPlanById(workPlanId);
		
		jsonMap.put("id", workPlan.getId());
		jsonMap.put("code", workPlan.getCode());
        jsonMap.put("name", workPlan.getName());
        jsonMap.put("startDateStr", DateUtils.formatShortDate(workPlan.getStartDate()));
        jsonMap.put("endDateStr", DateUtils.formatShortDate(workPlan.getEndDate()));
        jsonMap.put("target", workPlan.getTarget());
        jsonMap.put("content", workPlan.getContent());
        jsonMap.put("assessRequirement", workPlan.getAssessRequirement());
        jsonMap.put("contributeTargetAmount", workPlan.getContributeTargetAmount());
        
        // 责任人
        if(null != workPlan.getResponsilePersion() ) {
			String empid = workPlan.getResponsilePersion().getId() ;
			JSONArray emplist = new JSONArray();
			JSONObject emp = new JSONObject();
			emp.put("id", empid);
			emplist.add(emp);
			jsonMap.put("responsilePersionId", emplist.toString());
        }
      
        
        // 实施部门
        Set<EnforcementOrgs> enforcementOrgs = workPlan.getEnforcementOrgses();
        if(null != enforcementOrgs){
        	List<String> orgIds = new ArrayList<String>(); 
        	for (EnforcementOrgs org : enforcementOrgs) {
        		orgIds.add(org.getOrg().getId());
    		}
        	jsonMap.put("orgs", StringUtils.join(orgIds, ","));
        }
         
		map.put("data", jsonMap);
		map.put("success", true);
		
		return map;
	}
	
	
	/**
	 * <pre>
	 * 私有方法
	 * 转换工作计划
	 * 把request中的参数和formbean转换为workplan对象
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param workPlanForm
	 * @param milestoneData
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	private WorkPlan convertWorkPlan(WorkPlanForm workPlanForm,
			String milestoneData) {
		WorkPlan workPlan = new WorkPlan();
		BeanUtils.copyProperties(workPlanForm, workPlan);
		
		// id 如果存在就不生成id
		if(StringUtils.isBlank(workPlan.getId())) {
			workPlan.setId(Identities.uuid());
		}
		
		// 开始时间
		String startDateStr = workPlanForm.getStartDateStr();
		if (StringUtils.isNotBlank(startDateStr)) {
			workPlan.setStartDate(DateUtils.parse(startDateStr));
		}
		
		// 结束时间
		String endDateStr = workPlanForm.getEndDateStr();
		if (StringUtils.isNotBlank(endDateStr)) {
			workPlan.setEndDate(DateUtils.parse(endDateStr));
		}
		
		// 负责人
		String responsilePersionId = workPlanForm.getResponsilePersionId();
		if (StringUtils.isNotBlank(responsilePersionId)) {
			JSONArray jsonarr = JSONArray.fromObject(responsilePersionId);
			SysEmployee emp = new SysEmployee();
			emp.setId(String.valueOf(jsonarr.getJSONObject(0).get("id")));
			workPlan.setResponsilePersion(emp);
		}
		 
		// 实施部门
		String orgs = workPlanForm.getOrgs();
		if (StringUtils.isNotBlank(orgs)) {
			String[] orgSplit = StringUtils.split(orgs, ",");
			Set<EnforcementOrgs> enforcementOrgses = new HashSet<EnforcementOrgs>(0);
			for (String orgId : orgSplit) {
				EnforcementOrgs enforcementOrgs = new EnforcementOrgs();
				enforcementOrgs.setId(Identities.uuid());
				enforcementOrgs.setWorkPlan(workPlan);
				enforcementOrgs.setOrg(o_organizationBO.get(orgId));
				enforcementOrgses.add(enforcementOrgs);
			}
			workPlan.setEnforcementOrgses(enforcementOrgses);
		}
		
		// 添加里程碑
		if (StringUtils.isNotBlank(milestoneData)) {
			JSONArray jsonarr = JSONArray.fromObject(milestoneData);
			Set<Milestone> milestones = new HashSet<Milestone>(0);
			for (int i = 0; i < jsonarr.size(); i++) {
				Milestone milestone = new Milestone();
				JSONObject jsonObject = jsonarr.getJSONObject(i);
				milestone.setId(Identities.uuid());
				milestone.setDesc(jsonObject.getString("milestoneDesc"));
				milestone.setFinishDate(DateUtils.parse(StringUtils.substringBefore(jsonObject.getString("milestoneDate"), "T") ));
				milestone.setName(jsonObject.getString("milestoneType"));
				milestone.setPlan(workPlan);
				milestones.add(milestone);
			}
			workPlan.setMilestones(milestones);
		}
		return workPlan;
	}

	
	
}

