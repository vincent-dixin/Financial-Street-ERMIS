package com.fhd.risk.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fhd.core.dao.Page;
import com.fhd.risk.business.PotentialRiskEventBO;
import com.fhd.risk.business.RiskBO;
import com.fhd.risk.entity.Risk;

@Controller
public class PotentialRiskEventControl {
	
	@Autowired
	private RiskBO o_riskBO;
	
	@Autowired
	private PotentialRiskEventBO o_potentialRiskEventBO;
	
	/**
	 * 查询组织下的潜在风险事件
	 * 
	 * @author 郑军祥
	 * @param id
	 *            组织id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/potentialRiskEvent/findPotentialRiskEventByOrgId")
	public Map<String, Object> findPotentialRiskEventByOrgId(String id,
			String query) {
		Map<String, Object> map = new HashMap<String, Object>();

		Page<Risk> page = new Page<Risk>();
		page.setPageNo(1);
		page.setPageSize(1000000);
		page = o_riskBO.findRiskEventByOrgId(id, page, null, null, query);
		List<Risk> list = page.getResult();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		for (Risk risk : list) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", risk.getId());
			item.put("code", risk.getCode());
			item.put("name", risk.getName());
			datas.add(item);
		}
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);

		return map;
	}

	/**
	 * 查询指标下的潜在风险事件
	 * 
	 * @author 郑军祥
	 * @param id
	 *            组织id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/potentialRiskEvent/findPotentialRiskEventByKpiId")
	public Map<String, Object> findPotentialRiskEventByKpiId(String id,
			String query) {
		Map<String, Object> map = new HashMap<String, Object>();

		Page<Risk> page = new Page<Risk>();
		page.setPageNo(1);
		page.setPageSize(1000000);
		page = o_riskBO.findRiskEventByKpiId(id, page, null, null, query);
		List<Risk> list = page.getResult();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		for (Risk risk : list) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", risk.getId());
			item.put("code", risk.getCode());
			item.put("name", risk.getName());
			datas.add(item);
		}
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);

		return map;
	}

	/**
	 * 查询流程下的潜在风险事件
	 * 
	 * @author 郑军祥
	 * @param id
	 *            组织id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/potentialRiskEvent/findPotentialRiskEventByProcessId")
	public Map<String, Object> findPotentialRiskEventByProcessId(String id,
			String query) {
		Map<String, Object> map = new HashMap<String, Object>();

		Page<Risk> page = new Page<Risk>();
		page.setPageNo(1);
		page.setPageSize(1000000);
		page = o_riskBO.findRiskEventByProcessId(id, page, null, null, query);
		List<Risk> list = page.getResult();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		for (Risk risk : list) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", risk.getId());
			item.put("code", risk.getCode());
			item.put("name", risk.getName());
			datas.add(item);
		}
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);

		return map;
	}

	/**
	 * 查询风险下的潜在风险事件
	 * 
	 * @author 郑军祥
	 * @param id
	 *            组织id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/potentialRiskEvent/findPotentialRiskEventByRiskId")
	public Map<String, Object> findPotentialRiskEventByRiskId(String id,
			String query) {
		Map<String, Object> map = new HashMap<String, Object>();

		Page<Risk> page = new Page<Risk>();
		page.setPageNo(1);
		page.setPageSize(1000000);
		page = o_riskBO.findRiskEventBySome(id, page, null, null, query);
		List<Risk> list = page.getResult();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		for (Risk risk : list) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", risk.getId());
			item.put("code", risk.getCode());
			item.put("name", risk.getName());
			datas.add(item);
		}
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);

		return map;
	}
	
	/**
	 * 风险树,按风险事件id数组过滤
	 * @author 郑军祥
	 * @param node			nodeId
	 * @param query		  	查询条件
	 * @param ids			风险id串 “2,3”
	 * @return
	 */
    @ResponseBody
	@RequestMapping(value = "/potentialRiskEvent/getRiskTreeRecordByEventIds")
    public List<Map<String, Object>> getRiskTreeRecordByEventIds(String node, String query,String ids){
    	String[] eventIds = ids.split(",");
    	return o_potentialRiskEventBO.getRiskTreeRecordByEventIds(node, query, eventIds);
    }
    
    /**
	 * 组织树,按风险事件id数组过滤
	 * @author 郑军祥
	 * @param node			nodeId
	 * @param query		  	查询条件
	 * @param ids			风险id串 “2,3”
	 * @return
	 */
    @ResponseBody
    @RequestMapping("/potentialRiskEvent/getOrgTreeRecordByEventIds")
    public List<Map<String, Object>> getOrgTreeRecordByEventIds(String node, String query,String ids) {
    	String[] eventIds = ids.split(",");
    	return o_potentialRiskEventBO.getOrgTreeRecordByEventIds(node, query, eventIds);
    }
    
	/**
	 * 目标树,按风险事件id数组过滤
	 * @author 郑军祥
	 * @param node			nodeId
	 * @param query		  	查询条件
	 * @param ids			风险id串 “2,3”
	 * @return
	 */
    @ResponseBody
    @RequestMapping("/potentialRiskEvent/getStrategyMapTreeRecordByEventIds")
    public List<Map<String, Object>> getStrategyMapTreeRecordByEventIds(String node, String query,String ids) {
    	String[] eventIds = ids.split(",");
    	return o_potentialRiskEventBO.getStrategyMapTreeRecordByEventIds(node, query, eventIds);
    }
    
    /**
	 * 流程树,按风险事件id数组过滤
	 * @author 郑军祥
	 * @param node			nodeId
	 * @param query		  	查询条件
	 * @param ids			风险id串 “2,3”
	 * @return
	 */
    @ResponseBody
    @RequestMapping("/potentialRiskEvent/getProcessTreeRecordByEventIds")
    public List<Map<String, Object>> getProcessTreeRecordByEventIds(String node, String query,String ids) {
    	String[] eventIds = ids.split(",");
    	return o_potentialRiskEventBO.getProcessTreeRecordByEventIds(node, query, eventIds);
    }
}
