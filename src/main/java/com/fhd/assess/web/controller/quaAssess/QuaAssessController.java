package com.fhd.assess.web.controller.quaAssess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.assess.business.formulatePlan.RiskAssessPlanBO;
import com.fhd.assess.business.quaAssess.ApprovalAssessBO;
import com.fhd.assess.business.quaAssess.CountAssessBO;
import com.fhd.assess.business.quaAssess.QuaAssessNextBO;
import com.fhd.assess.business.quaAssess.RiskTidyBO;
import com.fhd.assess.business.quaAssess.SaveAssessBO;
import com.fhd.assess.business.quaAssess.ShowAssessBO;
import com.fhd.assess.business.quaAssess.SubmitAssessBO;
import com.fhd.assess.entity.formulatePlan.RiskAssessPlan;
import com.fhd.risk.business.RiskService;

@Controller
public class QuaAssessController {

	@Autowired
	private ShowAssessBO o_showAssessBO;
	
	@Autowired
	private CountAssessBO o_countAssessBO;
	
	@Autowired
	private SaveAssessBO o_saveAssessBO;
	
	@Autowired
	private SubmitAssessBO o_submitAssessBO;
	
	@Autowired
	private QuaAssessNextBO o_quaAssessNextBO;
	
	@Autowired
	private RiskService o_RiskService;
	
	@Autowired
	private RiskAssessPlanBO o_riskAssessPlanBO;
	
	@Autowired
	private ApprovalAssessBO o_approvalAssessBO;
	
	@Autowired
	private RiskTidyBO o_riskTidyBO;
	
	/**
	 * 浏览评估GRID
	 * */
	@ResponseBody
	@RequestMapping(value="findAssessShowGrid.f")
	public ArrayList<HashMap<String, String>> findAssessShowGrid(String assessPlanId) {
		assessPlanId = "deptplanid";
		String scoreEmpId = "wangwenfeng";//员工ID
		
		//风险事件GRID
		return o_showAssessBO.findRiskByScoreEmpId(scoreEmpId, o_showAssessBO.getTemplateId(assessPlanId), true);
	}
	
	/**
	 * 定性评估GRID
	 * */
	@ResponseBody
	@RequestMapping(value="findAssessGrid.f")
	public ArrayList<HashMap<String, String>> findAssessGrid(String assessPlanId) {
		assessPlanId = "deptplanid";
		String scoreEmpId = "wangwenfeng";//员工ID
		
		//风险事件GRID
		return o_showAssessBO.findRiskByScoreEmpId(scoreEmpId, o_showAssessBO.getTemplateId(assessPlanId), false);
	}
	
	/**
	 * 定性评估列表下一步
	 * */
	@ResponseBody
	@RequestMapping(value="assessFindRiskInfoByIds.f")
	public Map<String, Object> assessFindRiskInfoByIds(String params) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<Integer, Map<String, Object>> map = o_RiskService.findRiskInfoByIds(params);
		HashMap<Integer, ArrayList<ArrayList<ArrayList<ArrayList<String>>>>> list = o_quaAssessNextBO.findTemplateRelaDimByTemplateId(params);
		
		returnMap.put("data", map);
		returnMap.put("success", true);
		returnMap.put("result", list);
		returnMap.put("totalCount", list.size());
		
		return returnMap;
	}
	
	
	/**
	 * 保存评估处理
	 * */
	@ResponseBody
	@RequestMapping(value="saveDicValue.f")
	public Map<String,Object> saveDicValue(String params) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(o_saveAssessBO.assessSaveOper(params)){
			result.put("success", true);
		}else{
			result.put("success", false);
		}
		
		return result;
	}
	
	/**
	 * 通过风险ID集合查询已评估风险总数、未评估总数、已评估总数
	 * */
	@ResponseBody
	@RequestMapping(value="findAssessCount.f")
	public Map<String,Object> findAssessCount(String assessPlanId, String params, String pageSize) {
		Map<String, Object> result = new HashMap<String, Object>();
		String scoreEmpId = "wangwenfeng";//员工ID
		assessPlanId = "deptplanid";
		RiskAssessPlan riskssessPlan = o_riskAssessPlanBO.findRiskAssessPlanById(assessPlanId);
		HashMap<String, Integer> map = o_countAssessBO.getAssessCountMap(scoreEmpId, params);
		
		result.put("assessPlanName", riskssessPlan.getPlanName());//计划名称
		result.put("isAssessCount", map.get("isAssessCount"));//已评估数
		result.put("isNotAssessCount", map.get("isNotAssessCount"));//未评估数
		result.put("totalCount", map.get("totalCount"));//评估总数
		result.put("totalPageCount", ((map.get("totalCount")%Integer.parseInt(pageSize)==0)
				?(map.get("totalCount")/Integer.parseInt(pageSize)):(map.get("totalCount")/Integer.parseInt(pageSize)+1)));//总页
		result.put("success", true);
		
		return result;
	}
	
	/**
	 * 提交评估
	 * */
	@ResponseBody
	@RequestMapping(value="submitAssess.f")
	public Map<String,Object> submitAssess(String params, String leaderId) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(o_submitAssessBO.submitAssess(params)){
			result.put("success", true);
		}else{
			result.put("success", false);
		}
		
		return result;
	}
	
	/**
	 * 领导审批
	 * */
	@ResponseBody
	@RequestMapping(value="approvalColl.f")
	public Map<String,Object> approvalColl(String leaderDeptId) {
		//领导批准工作流GRID显示
		String assessPlanId = "deptplanid";
		String orgId = "f4a9e5abc805469790a9765d9d950b7a";
		int needAssessRiskDeptCount = o_approvalAssessBO.findNeedAssessDeptRisk(assessPlanId, orgId);//该评估计划下某部门需要评估的风险事件数量
		int alreadyAssessDeptRiskCount = o_approvalAssessBO.findAlreadyAssessDeptRisk(assessPlanId, orgId);//该评估计划下某部门已经被评估的风险事件数量
		if(needAssessRiskDeptCount == alreadyAssessDeptRiskCount){
			//需要提醒某部门下的领导进行审批
			
			HashMap<String, String> riskOrgAllMap = o_approvalAssessBO.findRiskOrgAllMap();
			ArrayList<ArrayList<String>> scoreResultAllList = o_approvalAssessBO.finScoreResultAllList();
			ArrayList<ArrayList<String>> deptByRiskAllList = o_approvalAssessBO.finDeptByRiskAllList(assessPlanId, orgId);
			o_approvalAssessBO.findDeptByLeaderId(assessPlanId, orgId, deptByRiskAllList, riskOrgAllMap, scoreResultAllList);
		}else{
			//不走工作流
		}
		
		
//		//全部评估完结进行存储统计结果表工作流
//		String assessPlanId = "deptplanid";
//		int needAssessRiskCount = o_riskTidyBO.findNeedAssessRisk(assessPlanId);//该评估计划下某部门需要评估的风险事件数量
//		int alreadyAssessRiskCount = o_riskTidyBO.findAlreadyAssessRisk(assessPlanId);//该评估计划下某部门已经被评估的风险事件数量
//		if(needAssessRiskCount == alreadyAssessRiskCount){
//			//计算存储统计结果表,并进入风险整理工作流
//			o_riskTidyBO.saveStatisticsResult(assessPlanId);
//		}else{
//			//不存储统计结果表,也不进入风险整理工作流
//		}
		
		
		return null;
	}
}