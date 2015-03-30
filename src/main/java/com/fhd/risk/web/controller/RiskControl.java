/**
 * RiskControl.java
 * com.fhd.risk.web.controller
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-1 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.risk.web.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.comm.business.AlarmPlanBO;
import com.fhd.comm.entity.AlarmPlan;
import com.fhd.core.dao.Page;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.UserContext;
import com.fhd.kpi.entity.RelaAssessResult;
import com.fhd.risk.business.RiskBO;
import com.fhd.risk.business.RiskOrgBO;
import com.fhd.risk.business.TemplateBO;
import com.fhd.risk.dao.TemplateDAO;
import com.fhd.risk.entity.KpiRelaRisk;
import com.fhd.risk.entity.OrgAdjustHistory;
import com.fhd.risk.entity.ProcessAdjustHistory;
import com.fhd.risk.entity.Risk;
import com.fhd.risk.entity.RiskAdjustHistory;
import com.fhd.risk.entity.ScoreInstance;
import com.fhd.risk.entity.StrategyAdjustHistory;
import com.fhd.risk.entity.Template;
//import com.fhd.risk.entity.RiskAdjustHistory;
import com.fhd.risk.entity.RiskOrg;
import com.fhd.process.entity.ProcessRelaRisk;
import com.fhd.risk.web.form.RiskForm;
import com.fhd.sys.business.dic.DictBO;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.dic.DictEntryRelation;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * ClassName:风险控制分发
 *
 * @author   zhengjunxiang
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-1		上午10:52:03
 *
 * @see 	 
 */
@Controller
public class RiskControl {
	
	@Autowired
	private RiskBO o_riskBo;
	
	@Autowired
	private RiskOrgBO o_riskOrgBo;
	
	/**
	 * 字典表
	 */
	@Autowired
	private DictBO o_dicBo;
	
	/**
	 * 模板
	 */
	@Autowired
	private TemplateBO o_templateBO;
	
	/**
	 * 告警方案
	 */
	@Autowired
	private AlarmPlanBO o_alarmPlanBO;
	
	@ResponseBody
	@RequestMapping(value="/risk/findRiskById.f")
	public Map<String, Object> findRiskById(String riskId) {
		Risk risk = o_riskBo.findRiskById(riskId, true);
		@SuppressWarnings("unchecked")
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", risk.getCode());
		map.put("name", risk.getName());
		map.put("parentName", risk.getParent()==null?"无":risk.getParent().getName());
		map.put("desc", risk.getDesc());
		
		String mDeptName = "";//责任部门
		String aDeptName = "";//相关部门
		String respPositionName = "";//责任岗位
		String relaPositionName = "";//相关岗位
		String respName = "";//责任人
		String relaName = "";//相关人
		
		Set<RiskOrg> riskOrgs = risk.getRiskOrgs();
		//如何通过criteria去直接查询优化？，先通过遍历实现
		for(RiskOrg org : riskOrgs){
			if(org.getType().equals("M")){	//责任部门
				if(null!=org.getSysOrganization()){
					mDeptName += org.getSysOrganization().getOrgname() + ",";
				}
			}
			if(org.getType().equals("A")){	//相关部门
				if(null!=org.getSysOrganization()){
					aDeptName += org.getSysOrganization().getOrgname() + ",";
				}
			}
		}

		Set<RiskOrg> riskEmps = risk.getRiskEmp();
		for(RiskOrg org : riskEmps){
			if(org.getType().equals("M")){	//责任部门
				if(null!=org.getEmp()){
					respName += org.getEmp().getEmpname() + ",";
				}
			}
			if(org.getType().equals("A")){	//相关部门
				if(null!=org.getEmp()){
					relaName += org.getEmp().getEmpname() + ",";
				}
			}
		}
		if(!mDeptName.equals("")){
			mDeptName = mDeptName.substring(0, mDeptName.length()-1);
		}
		if(!aDeptName.equals("")){
			aDeptName = aDeptName.substring(0, aDeptName.length()-1);
		}
		if(!respPositionName.equals("")){
			respPositionName = respPositionName.substring(0, respPositionName.length()-1);
		}
		if(!relaPositionName.equals("")){
			relaPositionName = relaPositionName.substring(0, relaPositionName.length()-1);
		}
		if(!respName.equals("")){
			respName = respName.substring(0, respName.length()-1);
		}
		if(!relaName.equals("")){
			relaName = relaName.substring(0, relaName.length()-1);
		}
		
		map.put("respDeptName", mDeptName);
		map.put("relaDeptName", aDeptName);
		map.put("respPositionName", respPositionName);	//责任岗位
		map.put("relaPositionName", relaPositionName);	//相关岗位
		map.put("respName", respName); //责任人
		map.put("relaName", relaName); //相关人
		
		String riskKpiName = "";//风险指标
		String influKpiName = "";//影响指标
		Set<KpiRelaRisk> kpiRelaRisks = risk.getKpiRelaRisks();
		for(KpiRelaRisk kpi : kpiRelaRisks){
			if(kpi.getType().equals("RM")){	//风险指标
				riskKpiName += kpi.getKpi().getName() + ",";
			}
			if(kpi.getType().equals("I")){	//影响指标
				influKpiName += kpi.getKpi().getName() + ",";
			}
		}
		if(!riskKpiName.equals("")){
			riskKpiName = riskKpiName.substring(0, riskKpiName.length()-1);
		}
		if(!influKpiName.equals("")){
			influKpiName = influKpiName.substring(0, influKpiName.length()-1);
		}
		map.put("riskKpiName", riskKpiName);
		map.put("influKpiName", influKpiName);

		String controlProcessureName = "";//控制流程
		String influProcessureName = "";//影响流程
		Set<ProcessRelaRisk> ProcessRelaRisks = risk.getRiskProcessures();
		for(ProcessRelaRisk processure : ProcessRelaRisks){
			if(processure.getType().equals("C")){	//控制流程
				controlProcessureName += processure.getProcess().getName() + ",";
			}
			if(processure.getType().equals("I")){	//影响指标
				influProcessureName += processure.getProcess().getName() + ",";
			}
		}
		if(!controlProcessureName.equals("")){
			controlProcessureName = controlProcessureName.substring(0, controlProcessureName.length()-1);
		}
		if(!influProcessureName.equals("")){
			influProcessureName = influProcessureName.substring(0, influProcessureName.length()-1);
		}
		map.put("controlProcessureName", controlProcessureName);
		map.put("influProcessureName", influProcessureName);
		
		//是否继承上级模板
		if(null == risk.getIsInherit()){
			map.put("isInherit","");
		}else{
			map.put("isInherit", o_dicBo.findDictEntryById(risk.getIsInherit()).getName());
		}
		
		//评估模板
		map.put("templeteName", risk.getTemplate()==null?"":risk.getTemplate().getName());
		
		//公式
		map.put("formulaDefine", risk.getFormulaDefine());
		
		//告警方案  下拉框
		map.put("alarmScenario",risk.getAlarmScenario()==null?"":risk.getAlarmScenario().getName());
		//监控频率
		map.put("gatherFrequence", risk.getMonitorFrequence());

		//是否定量
		if(null == risk.getIsFix()){
			map.put("isFix","");
		}else{
			map.put("isFix", o_dicBo.findDictEntryById(risk.getIsFix()).getName());
		}
		
		//是否启用
		if(null == risk.getIsUse()){
			map.put("isUse","");
		}else{
			map.put("isUse", o_dicBo.findDictEntryById(risk.getIsUse()).getName());
		}

		return map;
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value="/risk/findRiskEditInfoById.f")
	public Map<String, Object> findRiskEditInfoById(String riskId) {
		Risk risk = o_riskBo.findRiskById(riskId, true);
		
		Map<String, Object> map = new HashMap<String, Object>();
		//上级风险
		JSONArray jsonArray = new JSONArray();
		if(risk.getParent()!=null){
			JSONObject object = new JSONObject();
			object.put("id", risk.getParent().getId());
			jsonArray.add(object);
		}
		map.put("parentId", jsonArray.toString());
		map.put("code", risk.getCode());
		map.put("name", risk.getName());
		map.put("desc", risk.getDesc());
		
		//责任部门/人和相关部门/人
		JSONObject object = new JSONObject();
		JSONArray respDeptName = new JSONArray();
		JSONArray relaDeptName = new JSONArray();
		Set<RiskOrg> riskOrgs = risk.getRiskOrgs();
		for(RiskOrg riskOrg : riskOrgs){
			if(riskOrg.getType().equals("M")){	//责任部门/人
				object = new JSONObject();
				SysOrganization sysOrganizationIte = riskOrg.getSysOrganization();
				if(sysOrganizationIte!=null){
					object.put("deptid", sysOrganizationIte.getId());
				}
				SysEmployee empIte = riskOrg.getEmp();
				if(empIte!=null){
					object.put("empid", empIte.getId());
				}else{
					object.put("empid", "");
				}
				respDeptName.add(object);
			}
			if(riskOrg.getType().equals("A")){	//相关部门/人
				object = new JSONObject();
				SysOrganization sysOrganizationIte = riskOrg.getSysOrganization();
				if(sysOrganizationIte!=null){
					object.put("deptid", sysOrganizationIte.getId());
				}
				SysEmployee empIte = riskOrg.getEmp();
				if(empIte!=null){
					object.put("empid", empIte.getId());
				}else{
					object.put("empid", "");
				}
				relaDeptName.add(object);
			}
		}
		map.put("respDeptName", respDeptName.toString());
		map.put("relaDeptName", relaDeptName.toString());
		
		//风险类别
		String riskKind = "";
		Set<DictEntryRelation> dictEntryRelations = risk.getRiskKinds();
		for(DictEntryRelation relation : dictEntryRelations){
			riskKind += relation.getDictEntry().getId() + ",";
		}
		if(!riskKind.equals("")){
			riskKind = riskKind.substring(0, riskKind.length()-1);
		}
		map.put("riskKind", riskKind);
		
		//定量分析
		map.put("isFix", risk.getIsFix());	
		
		//是否启用
		map.put("isUse", risk.getIsUse());

		//影响指标，风险指标
		String riskKpiName = "";
		String influKpiName = "";
		Set<KpiRelaRisk> kpiRelaRisks = risk.getKpiRelaRisks();
		for(KpiRelaRisk kpi : kpiRelaRisks){
			if(kpi.getType().equals("RM")){	//风险指标
				riskKpiName += kpi.getKpi().getId() + ",";
			}
			if(kpi.getType().equals("I")){	//影响指标
				influKpiName += kpi.getKpi().getId() + ",";
			}
		}
		if(!riskKpiName.equals("")){
			riskKpiName = riskKpiName.substring(0, riskKpiName.length()-1);
		}
		if(!influKpiName.equals("")){
			influKpiName = influKpiName.substring(0, influKpiName.length()-1);
		}
		map.put("riskKpiName", riskKpiName);
		map.put("influKpiName", influKpiName);

		//影响流程，控制流程
		String controlProcessureName = "";
		String influProcessureName = "";
		Set<ProcessRelaRisk> ProcessRelaRisks = risk.getRiskProcessures();
		for(ProcessRelaRisk processure : ProcessRelaRisks){
			if(processure.getType().equals("C")){	//控制流程
				controlProcessureName += processure.getProcess().getId() + ",";
			}
			if(processure.getType().equals("I")){	//影响指标
				influProcessureName += processure.getProcess().getId() + ",";
			}
		}
		if(!controlProcessureName.equals("")){
			controlProcessureName = controlProcessureName.substring(0, controlProcessureName.length()-1);
		}
		if(!influProcessureName.equals("")){
			influProcessureName = influProcessureName.substring(0, influProcessureName.length()-1);
		}
		map.put("controlProcessureName", controlProcessureName);
		map.put("influProcessureName", influProcessureName);
		
		//是否继承
		map.put("isInherit", risk.getIsInherit());
		//模板下拉框
		map.put("templateId", risk.getTemplate()==null?"":risk.getTemplate().getId());
		//告警方案
		map.put("alarmPlanId", risk.getAlarmScenario()==null?"":risk.getAlarmScenario().getId());
		//监控频率
		map.put("monitorFrequence", risk.getMonitorFrequence());
		//公式计算
		map.put("formulaDefine", risk.getFormulaDefine());
		//涉及版块
		String relePlate = "";
		Set<DictEntryRelation> palteRelations = risk.getRelePlates();
		for(DictEntryRelation relation : palteRelations){
			relePlate += relation.getDictEntry().getId() + ",";
		}
		if(!relePlate.equals("")){
			relePlate = relePlate.substring(0, relePlate.length()-1);
		}
		map.put("relePlate", relePlate);
		
		return map;
	}
	
	/**
	 * 保存风险
	 * @author zhengjunxiang
	 * @param riskForm
	 * @param id
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/risk/risk/saveRiskInfo.f")
	public void saveRiskInfo(RiskForm riskForm,String id,HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		String isSave = "false";
		
		Risk risk = new Risk();
		if(StringUtils.isNotBlank(id)){//更新
			risk = o_riskBo.findRiskById(id, true);
		}else{//添加
			String makeId = Identities.uuid();
			risk.setId(makeId);
			String companyId = UserContext.getUser().getCompanyid();
			SysOrganization company = o_riskBo.findOrganizationByOrgId(companyId);
			risk.setCompany(company);
			String parentIdStr = riskForm.getParentId();
			if(parentIdStr!=null && !parentIdStr.equals("")){
				JSONArray parentIdArr =JSONArray.fromObject(parentIdStr);
				String parentId = ((JSONObject)parentIdArr.get(0)).get("id").toString();
				Risk parent = o_riskBo.findRiskById(parentId, true);
				if(null==parent){
					risk.setParent(null);
					risk.setParentName("");
					risk.setIdSeq("."+makeId+".");
				}else{
					risk.setParent(parent);
					risk.setParentName(parent.getName());
					risk.setIdSeq(parent.getIdSeq()+makeId+".");
				}
			}else{
				risk.setParent(null);
				risk.setParentName("");
				risk.setIdSeq("."+makeId+".");
			}

			risk.setDeleteStatus("1");
			risk.setIsLeaf(true);
		}
		risk.setIsRiskClass(riskForm.getIsRiskClass());
		//编码，名称，描述
		risk.setCode(riskForm.getCode());
		risk.setName(riskForm.getName());
		risk.setDesc(riskForm.getDesc());
		risk.setSort(riskForm.getSort());
		
		//是否定量，是否启用
		risk.setIsFix(riskForm.getIsFix());
		risk.setIsUse(riskForm.getIsUse());
		
		//告警方案
		if(null!=riskForm.getAlarmPlanId() && !"".equals(riskForm.getAlarmPlanId())){
			AlarmPlan alarmPlan = o_alarmPlanBO.findAlarmPlanById(riskForm.getAlarmPlanId());
			risk.setAlarmScenario(alarmPlan);
		}else{
			risk.setAlarmScenario(null);
		}
		//监控频率
		risk.setMonitorFrequence(riskForm.getMonitorFrequence());
		//是否继承
		risk.setIsInherit(riskForm.getIsInherit());
		//评估模板
		if(null!=riskForm.getTemplateId() && !"".equals(riskForm.getTemplateId())){
			Template template = o_templateBO.findTemplateById(riskForm.getTemplateId());
			risk.setTemplate(template);
		}else{
			risk.setTemplate(null);
		}
		//公式
		risk.setFormulaDefine(riskForm.getFormulaDefine());
				
		//风险类别，涉及版块
		String riskKind = riskForm.getRiskKind();
		String relePlate = riskForm.getRelePlate();
		
		//责任部门，相关部门，责任人，相关人，风险指标，影响指标，控制流程，影响流程
		String respDeptName = riskForm.getRespDeptName();
		String relaDeptName = riskForm.getRelaDeptName();
		String riskKpiName = riskForm.getRiskKpiName();
		String influKpiName = riskForm.getInfluKpiName();
		String controlProcessureName = riskForm.getControlProcessureName();
		String influProcessureName = riskForm.getInfluProcessureName();
		
		//下面2个暂时没用到，责任岗位，相关岗位
		String respPositionName = riskForm.getRespPositionName();
		String relaPositionName = riskForm.getRelaPositionName();
		
		if(StringUtils.isNotBlank(id)){
			//更新
			o_riskBo.updateRisk(risk, respDeptName, relaDeptName,respPositionName,relaPositionName,riskKind,relePlate,riskKpiName,influKpiName,controlProcessureName,influProcessureName);
			isSave = "true";
		}else{
			//保存
			o_riskBo.addRisk(risk, respDeptName, relaDeptName,respPositionName,relaPositionName,riskKind,relePlate,riskKpiName,influKpiName,controlProcessureName,influProcessureName);
			isSave = "true";
		}
		out.write(isSave);
		out.close();

	}
	
	@ResponseBody
	@RequestMapping(value = "/risk/risk/removeRiskById.f")
	public boolean removeRiskById(String ids){
		o_riskBo.removeRiskByIds(ids);
		return true;
	}
	
	/**
	 * 根据风险id获取风险历史记录
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/risk/findRiskAdjustHistoryById.f")
	public Map<String, Object> findRiskAdjustHistoryById(int start, int limit, String query, String sort,String riskId){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
		
		if (StringUtils.isNotBlank(riskId)) {
	        String dir = "DESC";
	        String sortColumn = "status";
			if (StringUtils.isNotBlank(sort)) {
                JSONArray jsonArray = JSONArray.fromObject(sort);
                if (jsonArray.size() > 0) {
                    JSONObject jsobj = jsonArray.getJSONObject(0);
                    sortColumn = jsobj.getString("property");//按照哪个字段排序
                    dir = jsobj.getString("direction");//排序方向
                }
            }
			
			Page<RiskAdjustHistory> page = new Page<RiskAdjustHistory>();
	        page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
	        page.setPageSize(limit);
	        page = o_riskBo.findRiskAdjustHistoryBySome(riskId,page,sortColumn,dir,query);
	        
			List<RiskAdjustHistory> list = page.getResult();
			Map<String, Object> item = null;
			for (RiskAdjustHistory history : list) {
				item = new HashMap<String, Object>();
				item.put("etrend",history.getEtrend());
				item.put("assessementStatus",history.getAssessementStatus());
				item.put("year", history.getTimePeriod()==null?"":history.getTimePeriod().getYear());
				item.put("month", history.getTimePeriod()==null?"":history.getTimePeriod().getMonth());
				
				if(history.getTimePeriod()==null){
					item.put("dateRange","");
				}else{
					String dataRange = history.getTimePeriod().getYear()+"年"+history.getTimePeriod().getMonth()+"月";
					item.put("dateRange",dataRange);
				}
				item.put("probability", history.getProbability());
				item.put("impacts", history.getImpacts());
				item.put("status", history.getStatus());
				datas.add(item);
			}
			map.put("totalCount", page.getTotalItems());
			map.put("datas", datas);
		}

		return map;
	}
	
	/**
	 * 根据风险id获取风险历史记录
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/risk/findEventById.f")
	public Map<String, Object> findRiskEventById(int start, int limit, String query, String sort,String riskId){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
		
		if (StringUtils.isNotBlank(riskId)) {
	        String dir = "ASC";
	        String sortColumn = "sort";
			if (StringUtils.isNotBlank(sort)) {
                JSONArray jsonArray = JSONArray.fromObject(sort);
                if (jsonArray.size() > 0) {
                    JSONObject jsobj = jsonArray.getJSONObject(0);
                    sortColumn = jsobj.getString("property");//按照哪个字段排序
                    dir = jsobj.getString("direction");//排序方向
                }
            }
			
			Page<Risk> page = new Page<Risk>();
	        page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
	        page.setPageSize(limit);
	        page = o_riskBo.findRiskEventBySome(riskId,page,sortColumn,dir,query);
	        
			List<Risk> list = page.getResult();
			Map<String, Object> item = null;
			for (Risk event: list) {
				item = new HashMap<String, Object>();
				item.put("id",event.getId().toString());
				item.put("name",event.getName());
				//风险的责任部门
				String respDeptName = "";
				Risk risk =o_riskBo.findRiskById(event.getId(), true);
				Set<RiskOrg> riskOrgs = risk.getRiskOrgs();
				for(RiskOrg org : riskOrgs){
					if(org.getType().equals("M")){	//责任部门
						if(null!=org.getSysOrganization()){
							respDeptName += org.getSysOrganization().getOrgname() + ",";
						}
					}
				}
				if(!respDeptName.equals("")){
					respDeptName = respDeptName.substring(0,respDeptName.length()-1);
				}
				item.put("respDeptName",respDeptName);
				//最新评估记录
				RiskAdjustHistory latestAdjustHistory = o_riskBo.findLatestRiskAdjustHistoryByRiskId(event.getId());
				if(latestAdjustHistory==null){
					item.put("assessementStatus","");
					item.put("etrend","");
					item.put("status","");
					item.put("adjustTime","");
				}else{
					item.put("assessementStatus",latestAdjustHistory.getAssessementStatus());
					item.put("etrend",latestAdjustHistory.getEtrend());
					item.put("status",latestAdjustHistory.getStatus());
					item.put("adjustTime",latestAdjustHistory.getAdjustTime()==null?"":latestAdjustHistory.getAdjustTime().toLocaleString());
				}
				
				datas.add(item);
			}
			
			map.put("totalCount",page.getTotalItems());
			map.put("datas", datas);
		}
		return map;
	}
	
	/**
	 * 风险树的构建
	 * @author 郑军祥
	 * @param id			nodeId
	 * @param canChecked  	是否多选
	 * @param query		  	查询条件
	 * @param rbs			true表示值查询风险，不带有风险事件；false带有风险事件
	 * @return
	 */
    @ResponseBody
	@RequestMapping(value = "/risk/riskTreeLoader")
    public List<Map<String, Object>> riskTreeLoader(String node, String query, Boolean rbs, Boolean canChecked,String chooseId){
    	return o_riskBo.riskTreeLoader(node, query, rbs, canChecked, chooseId);
    }
    
    @ResponseBody
	@RequestMapping(value = "/risk/enableRisk")
    public void enableRisk(String ids,String isUsed,HttpServletResponse response) throws Exception {
    	PrintWriter out = response.getWriter();
		String isSave = "false";
		o_riskBo.enableRisk(ids, isUsed);
		isSave = "true";
		out.write(isSave);
		out.close();
    }
    
    /**
	 * 查询目标下的风险列表
	 * @author 郑军祥 2013-6-6
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/risk/findRiskByStrategyMapId")
	public Map<String, Object> findRiskByStrategyMapId(int start, int limit, String query, String sort,String id){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
		
		if (StringUtils.isNotBlank(id)) {
	        String dir = "DESC";
	        String sortColumn = "id";
			if (StringUtils.isNotBlank(sort)) {
                JSONArray jsonArray = JSONArray.fromObject(sort);
                if (jsonArray.size() > 0) {
                    JSONObject jsobj = jsonArray.getJSONObject(0);
                    sortColumn = jsobj.getString("property");//按照哪个字段排序
                    dir = jsobj.getString("direction");//排序方向
                }
            }
			
			Page<Risk> page = new Page<Risk>();
	        page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
	        page.setPageSize(limit);
	        page = o_riskBo.findRiskEventByStrateMapId(id, page, sortColumn, dir, query);
	        
			List<Risk> list = page.getResult();
			Map<String, Object> item = null;
			
			for (Risk event: list) {
				item = new HashMap<String, Object>();
				item.put("id",event.getId().toString());
				item.put("name",event.getName());				
				//风险的责任部门
				String respDeptName = "";
				Risk risk =o_riskBo.findRiskById(event.getId(), true);
				Set<RiskOrg> riskOrgs = risk.getRiskOrgs();
				for(RiskOrg org : riskOrgs){
					if(org.getType().equals("M")){	//责任部门
						if(null!=org.getSysOrganization()){
							respDeptName += org.getSysOrganization().getOrgname() + ",";
						}
					}
				}
				if(!respDeptName.equals("")){
					respDeptName = respDeptName.substring(0,respDeptName.length()-1);
				}
				item.put("respDeptName",respDeptName);
				//最新评估记录
				RiskAdjustHistory latestAdjustHistory = o_riskBo.findLatestRiskAdjustHistoryByRiskId(event.getId());
				if(latestAdjustHistory==null){
					item.put("assessementStatus","");
					item.put("etrend","");
					item.put("status","");
					item.put("adjustTime","");
				}else{
					item.put("assessementStatus",latestAdjustHistory.getAssessementStatus());
					item.put("etrend",latestAdjustHistory.getEtrend());
					item.put("status",latestAdjustHistory.getStatus());
					item.put("adjustTime",latestAdjustHistory.getAdjustTime()==null?"":latestAdjustHistory.getAdjustTime().toLocaleString());
				}
				
				datas.add(item);
			}
			
			map.put("totalCount",page.getTotalItems());
			map.put("datas", datas);
		}
		return map;
	}
	
	/**
	 * 根据目标id获取目标历史记录
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/risk/findStrategyAdjustHistoryByStrategyMapId")
	public Map<String, Object> findStrategyAdjustHistoryByStrategyMapId(int start, int limit, String query, String sort,String riskId){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
		
		if (StringUtils.isNotBlank(riskId)) {
	        String dir = "DESC";
	        String sortColumn = "id";
			if (StringUtils.isNotBlank(sort)) {
                JSONArray jsonArray = JSONArray.fromObject(sort);
                if (jsonArray.size() > 0) {
                    JSONObject jsobj = jsonArray.getJSONObject(0);
                    sortColumn = jsobj.getString("property");//按照哪个字段排序
                    dir = jsobj.getString("direction");//排序方向
                }
            }
			
			Page<StrategyAdjustHistory> page = new Page<StrategyAdjustHistory>();
	        page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
	        page.setPageSize(limit);
	        page = o_riskBo.findStrategyHistoryByStrateMapId(riskId,page,sortColumn,dir,query);
	        
			List<StrategyAdjustHistory> list = page.getResult();
			Map<String, Object> item = null;
			for (StrategyAdjustHistory history : list) {
				item = new HashMap<String, Object>();
				item.put("etrend",history.getEtrend());
				item.put("assessementStatus",history.getAssessementStatus());
				item.put("year", history.getTimePeriod()==null?"":history.getTimePeriod().getYear());
				item.put("month", history.getTimePeriod()==null?"":history.getTimePeriod().getMonth());
				
				if(history.getTimePeriod()==null){
					item.put("dateRange","");
				}else{
					String dataRange = history.getTimePeriod().getYear()+"年"+history.getTimePeriod().getMonth()+"月";
					item.put("dateRange",dataRange);
				}
				item.put("probability", "空");
				item.put("impacts", "空");
				datas.add(item);
			}
			map.put("totalCount", page.getTotalItems());
			map.put("datas", datas);
		}

		return map;
	}
	
	/**
	 * 查询组织下的风险列表
	 * @author 郑军祥 2013-6-6
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/risk/findRiskByOrgId")
	public Map<String, Object> findRiskByOrgId(int start, int limit, String query, String sort,String id){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
		
		if (StringUtils.isNotBlank(id)) {
	        String dir = "DESC";
	        String sortColumn = "id";
			if (StringUtils.isNotBlank(sort)) {
                JSONArray jsonArray = JSONArray.fromObject(sort);
                if (jsonArray.size() > 0) {
                    JSONObject jsobj = jsonArray.getJSONObject(0);
                    sortColumn = jsobj.getString("property");//按照哪个字段排序
                    dir = jsobj.getString("direction");//排序方向
                }
            }
			
			Page<Risk> page = new Page<Risk>();
	        page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
	        page.setPageSize(limit);
	        page = o_riskBo.findRiskEventByOrgId(id, page, sortColumn, dir, query);
	        
			List<Risk> list = page.getResult();
			Map<String, Object> item = null;
			
			for (Risk event: list) {
				item = new HashMap<String, Object>();
				item.put("id",event.getId().toString());
				item.put("name",event.getName());				
				//风险的责任部门
				String respDeptName = "";
				Risk risk =o_riskBo.findRiskById(event.getId(), true);
				Set<RiskOrg> riskOrgs = risk.getRiskOrgs();
				for(RiskOrg org : riskOrgs){
					if(org.getType().equals("M")){	//责任部门
						if(null!=org.getSysOrganization()){
							respDeptName += org.getSysOrganization().getOrgname() + ",";
						}
					}
				}
				if(!respDeptName.equals("")){
					respDeptName = respDeptName.substring(0,respDeptName.length()-1);
				}
				item.put("respDeptName",respDeptName);
				//最新评估记录
				RiskAdjustHistory latestAdjustHistory = o_riskBo.findLatestRiskAdjustHistoryByRiskId(event.getId());
				if(latestAdjustHistory==null){
					item.put("assessementStatus","");
					item.put("etrend","");
					item.put("status","");
					item.put("adjustTime","");
				}else{
					item.put("assessementStatus",latestAdjustHistory.getAssessementStatus());
					item.put("etrend",latestAdjustHistory.getEtrend());
					item.put("status",latestAdjustHistory.getStatus());
					item.put("adjustTime",latestAdjustHistory.getAdjustTime()==null?"":latestAdjustHistory.getAdjustTime().toLocaleString());
				}
				
				datas.add(item);
			}
			
			map.put("totalCount",page.getTotalItems());
			map.put("datas", datas);
		}
		return map;
	}
	
	/**
	 * 根据目标id获取目标历史记录
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/risk/findOrgAdjustHistoryByOrgId")
	public Map<String, Object> findOrgAdjustHistoryByOrgId(int start, int limit, String query, String sort,String riskId){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
		
		if (StringUtils.isNotBlank(riskId)) {
	        String dir = "DESC";
	        String sortColumn = "id";
			if (StringUtils.isNotBlank(sort)) {
                JSONArray jsonArray = JSONArray.fromObject(sort);
                if (jsonArray.size() > 0) {
                    JSONObject jsobj = jsonArray.getJSONObject(0);
                    sortColumn = jsobj.getString("property");//按照哪个字段排序
                    dir = jsobj.getString("direction");//排序方向
                }
            }
			
			Page<OrgAdjustHistory> page = new Page<OrgAdjustHistory>();
	        page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
	        page.setPageSize(limit);
	        page = o_riskBo.findOrgHistoryByOrgId(riskId,page,sortColumn,dir,query);
	        
			List<OrgAdjustHistory> list = page.getResult();
			Map<String, Object> item = null;
			for (OrgAdjustHistory history : list) {
				item = new HashMap<String, Object>();
				item.put("etrend",history.getEtrend());
				item.put("assessementStatus",history.getAssessementStatus());
				item.put("year", history.getTimePeriod()==null?"":history.getTimePeriod().getYear());
				item.put("month", history.getTimePeriod()==null?"":history.getTimePeriod().getMonth());
				
				if(history.getTimePeriod()==null){
					item.put("dateRange","");
				}else{
					String dataRange = history.getTimePeriod().getYear()+"年"+history.getTimePeriod().getMonth()+"月";
					item.put("dateRange",dataRange);
				}
				item.put("probability", "空");
				item.put("impacts", "空");
				datas.add(item);
			}
			map.put("totalCount", page.getTotalItems());
			map.put("datas", datas);
		}

		return map;
	}
	
	/**
	 * 查询流程下的风险列表
	 * @author 郑军祥 2013-6-6
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/risk/findRiskByProcessId")
	public Map<String, Object> findRiskByPrcessId(int start, int limit, String query, String sort,String id){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
		
		if (StringUtils.isNotBlank(id)) {
	        String dir = "DESC";
	        String sortColumn = "id";
			if (StringUtils.isNotBlank(sort)) {
                JSONArray jsonArray = JSONArray.fromObject(sort);
                if (jsonArray.size() > 0) {
                    JSONObject jsobj = jsonArray.getJSONObject(0);
                    sortColumn = jsobj.getString("property");//按照哪个字段排序
                    dir = jsobj.getString("direction");//排序方向
                }
            }
			
			Page<Risk> page = new Page<Risk>();
	        page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
	        page.setPageSize(limit);
	        page = o_riskBo.findRiskEventByProcessId(id, page, sortColumn, dir, query);
	        
			List<Risk> list = page.getResult();
			Map<String, Object> item = null;
			
			for (Risk event: list) {
				item = new HashMap<String, Object>();
				item.put("id",event.getId().toString());
				item.put("name",event.getName());				
				//风险的责任部门
				String respDeptName = "";
				Risk risk =o_riskBo.findRiskById(event.getId(), true);
				Set<RiskOrg> riskOrgs = risk.getRiskOrgs();
				for(RiskOrg org : riskOrgs){
					if(org.getType().equals("M")){	//责任部门
						if(null!=org.getSysOrganization()){
							respDeptName += org.getSysOrganization().getOrgname() + ",";
						}
					}
				}
				if(!respDeptName.equals("")){
					respDeptName = respDeptName.substring(0,respDeptName.length()-1);
				}
				item.put("respDeptName",respDeptName);
				//最新评估记录
				RiskAdjustHistory latestAdjustHistory = o_riskBo.findLatestRiskAdjustHistoryByRiskId(event.getId());
				if(latestAdjustHistory==null){
					item.put("assessementStatus","");
					item.put("etrend","");
					item.put("status","");
					item.put("adjustTime","");
				}else{
					item.put("assessementStatus",latestAdjustHistory.getAssessementStatus());
					item.put("etrend",latestAdjustHistory.getEtrend());
					item.put("status",latestAdjustHistory.getStatus());
					item.put("adjustTime",latestAdjustHistory.getAdjustTime()==null?"":latestAdjustHistory.getAdjustTime().toLocaleString());
				}
				
				datas.add(item);
			}
			
			map.put("totalCount",page.getTotalItems());
			map.put("datas", datas);
		}
		return map;
	}
	
	/**
	 * 根据目标id获取目标历史记录
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/risk/findProcessAdjustHistoryByProcessId")
	public Map<String, Object> findProcessAdjustHistoryByProcessId(int start, int limit, String query, String sort,String riskId){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
		
		if (StringUtils.isNotBlank(riskId)) {
	        String dir = "DESC";
	        String sortColumn = "id";
			if (StringUtils.isNotBlank(sort)) {
                JSONArray jsonArray = JSONArray.fromObject(sort);
                if (jsonArray.size() > 0) {
                    JSONObject jsobj = jsonArray.getJSONObject(0);
                    sortColumn = jsobj.getString("property");//按照哪个字段排序
                    dir = jsobj.getString("direction");//排序方向
                }
            }
			
			Page<ProcessAdjustHistory> page = new Page<ProcessAdjustHistory>();
	        page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
	        page.setPageSize(limit);
	        page = o_riskBo.findProcessHistoryByProcessId(riskId,page,sortColumn,dir,query);
	        
			List<ProcessAdjustHistory> list = page.getResult();
			Map<String, Object> item = null;
			for (ProcessAdjustHistory history : list) {
				item = new HashMap<String, Object>();
				item.put("etrend",history.getEtrend());
				item.put("assessementStatus",history.getAssessementStatus());
				item.put("year", history.getTimePeriod()==null?"":history.getTimePeriod().getYear());
				item.put("month", history.getTimePeriod()==null?"":history.getTimePeriod().getMonth());
				
				if(history.getTimePeriod()==null){
					item.put("dateRange","");
				}else{
					String dataRange = history.getTimePeriod().getYear()+"年"+history.getTimePeriod().getMonth()+"月";
					item.put("dateRange",dataRange);
				}
				item.put("probability", "空");
				item.put("impacts", "空");
				datas.add(item);
			}
			map.put("totalCount", page.getTotalItems());
			map.put("datas", datas);
		}

		return map;
	}
}