/*
 * 北京第一会达风险管理有限公司 版权所有 2013
 * Copyright(C) 2013 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.wp.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysOrganization;
import com.fhd.wp.business.MilestoneImprovementBO;
import com.fhd.wp.entity.Milestone;
import com.fhd.wp.entity.MilestoneImprovement;

/**
 * 里程碑关联计划
 *
 * @author   胡迪新
 * @since    fhd Ver 4.5
 * @Date	 2013-3-12  下午6:01:27
 *
 * @see 	 
 */
@Controller
public class MilestoneImprovementControl {

	@Autowired
	private MilestoneImprovementBO o_milestoneImprovementBO;
	
	/*
	 * 添加
	 */

	/**
	 * 
	 * <pre>
	 * 保存里程碑和计划关系
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param milestoneData
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping("/wp/savemilestoneimprovement.f")
	public Map<String, Object> saveMilestoneImprovement(String milestoneData) {
		
		if (StringUtils.isNotBlank(milestoneData)) {
			List<MilestoneImprovement> mis = convertMilestoneImprovement(milestoneData);
			o_milestoneImprovementBO.saveMilestoneImprovementBatch(mis);
		}
		
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		return map;
	}
	/**
	 * 
	 * <pre>
	 * 保存里程碑和计划关系
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param milestoneData
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping("/wp/savemilestoneimprovementsubmit.f")
	public Map<String, Object> saveMilestoneImprovementSubmit(String milestoneData,String processInstanceId) {
		
		if (StringUtils.isNotBlank(milestoneData)) {
			List<MilestoneImprovement> mis = convertMilestoneImprovement(milestoneData);
			o_milestoneImprovementBO.saveMilestoneImprovementBatchSubmit(mis,processInstanceId);
		}
		
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		return map;
	}
	
	
	/*
	 * 修改
	 */

	/*
	 * 删除
	 */

	/*
	 * 查询
	 */
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param milestoneData
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	
	private List<MilestoneImprovement> convertMilestoneImprovement(
			String milestoneData) {
		JSONArray jsonarr = JSONArray.fromObject(milestoneData);
		List<MilestoneImprovement> mis = new ArrayList<MilestoneImprovement>(0);
		for (int i = 0; i < jsonarr.size(); i++) {
			
			MilestoneImprovement mi = new MilestoneImprovement();
			JSONObject jsonObject = jsonarr.getJSONObject(i);
			String subPlanId = jsonObject.getString("subPlan");
			if(StringUtils.isNotEmpty(subPlanId) && !"null".equals(subPlanId)) {
				MilestoneImprovement miOld = o_milestoneImprovementBO.findMilestoneImprovementBySubPlanId(jsonObject.getString("subPlanId"));
				if(null == miOld) {
					mi.setId(Identities.uuid());
				}else {
					mi.setId(miOld.getId());
				}
				
				Milestone milestone = new Milestone();
				milestone.setId(jsonObject.getString("id"));
				mi.setMilestone(milestone);
				mi.setOrg(new SysOrganization(UserContext.getUser().getCompanyid()));
				mi.setType(new DictEntry(jsonObject.getString("milestoneType")));
				mi.setSubPlanId(subPlanId);
				mi.setFinishCondition(0d);
				mi.setStatus(new DictEntry("unfinish"));
				mis.add(mi);
			}
			
		}
		return mis;
	}
}

