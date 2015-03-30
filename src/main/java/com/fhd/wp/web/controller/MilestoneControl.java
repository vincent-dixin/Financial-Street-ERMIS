/*
 * 北京第一会达风险管理有限公司 版权所有 2013
 * Copyright(C) 2013 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.wp.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.core.utils.DateUtils;
import com.fhd.sys.business.dic.DictBO;
import com.fhd.wp.business.MilestionBO;
import com.fhd.wp.entity.Milestone;

/**
 * 
 * 里程碑
 *
 * @author   胡迪新
 * @since    fhd Ver 4.5
 * @Date	 2013-2-21  下午2:43:20
 *
 * @see 	 
 */
@Controller
public class MilestoneControl {

	@Autowired
	private MilestionBO o_milestionBO;
	@Autowired
	private DictBO o_dictBO;
	
	/**
	 * 
	 * <pre>
	 * 根据工作计划ID查询里程碑列表
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param workPlanId 工作计划ID
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/wp/findMilestoneList.f")
	public Map<String, Object> findMilestoneList(String workPlanId) {
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		
		if(StringUtils.isNotBlank(workPlanId)) {
			List<Milestone> milestones = o_milestionBO.findMilestoneByWorkPlanId(workPlanId);
			for (Milestone milestone : milestones) {
				Map<String, Object> row = new HashMap<String, Object>();
				row.put("id", milestone.getId());
				
				row.put("milestoneName", milestone.getName());
				row.put("milestoneType", milestone.getName());
				row.put("milestoneDesc", milestone.getDesc());
				row.put("milestoneDate", DateUtils.formatShortDate(milestone.getFinishDate()));
				datas.add(row);
			}
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("datas", datas);
		
		return map;
	}
	
	
	/**
	 * 
	 * <pre>
	 * 根据工作计划ID查询里程碑列表
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param workPlanId 工作计划ID
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@ResponseBody
	@RequestMapping("/wp/findmilestonelistforexecute.f")
	public Map<String, Object> findMilestoneListForExecute(String workPlanId) {
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		
		if(StringUtils.isNotBlank(workPlanId)) {
			List<Object[]> milestones = o_milestionBO.findMilestoneForExecuteByWorkPlanId(workPlanId);
			for (Object[] milestone : milestones) {
				Map<String, Object> row = new HashMap<String, Object>();
				row.put("id", milestone[2]);
				
				row.put("milestoneName", milestone[4]);
				row.put("milestoneType", milestone[3]);
				row.put("milestoneDesc", milestone[5]);
				row.put("milestoneDate", DateUtils.formatShortDate((Date)milestone[6]));
				row.put("subPlan", milestone[9]);
				row.put("subPlanId", milestone[9]);
				datas.add(row);
			}
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("datas", datas);
		
		return map;
	}
	
	
	
	@ResponseBody
	@RequestMapping("/wp/findmilestionretaplan.f")
	public Map<String, Object> findMilestionRetaPlan(String milestoneType) {
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		
		
		
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("datas", datas);
		
		return map;
	}
}

