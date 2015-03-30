package com.fhd.risk.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.fhd.process.dao.ProcessRelaRiskDAO;
import com.fhd.process.entity.Process;
import com.fhd.kpi.entity.Kpi;
import com.fhd.process.entity.ProcessRelaRisk;
import com.fhd.risk.dao.KpiRelaRiskDAO;
import com.fhd.risk.dao.RiskDAO;
import com.fhd.risk.dao.RiskOrgDAO;
import com.fhd.risk.entity.KpiRelaRisk;
import com.fhd.risk.entity.Risk;
import com.fhd.risk.entity.RiskOrg;
import com.fhd.sys.entity.dic.DictEntryRelation;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;

@Service
public class RiskService {

	@Autowired
	private RiskDAO o_riskDAO;
	
	@Autowired
	private RiskOrgDAO o_riskOrgDAO;

	@Autowired
	private KpiRelaRiskDAO o_kpiRelaRiskDAO;
	
	@Autowired
	private ProcessRelaRiskDAO o_processRelaRiskDAO;
	
	/**
	 * 添加风险，并且添加实体的相关信息
	 * @author 郑军祥		2013-06-18
	 * @param risk					风险实体
	 * @param respOrgIds			风险责任部门
     * @param relaOrgIds			风险相关部门
	 * @param influenceKpiIds		影响指标
	 * @param influeceProcessIds	影响流程
	 * @return true:成功 false:失败
	 */
	@Transactional
	public boolean saveRisk(Risk risk,List<String> respOrgIds,List<String> relaOrgIds,
		            List<String> influenceKpiIds,List<String> influeceProcessIds){
		o_riskDAO.merge(risk);
		
//		//添加责任部门
//		for(String id : respOrgIds){
//			RiskOrg riskOrg = new RiskOrg();
//			riskOrg.setId(UUID.randomUUID().toString());
//			riskOrg.setRisk(risk);
//			riskOrg.setType("M");
//			SysOrganization sysOrganization = new SysOrganization();
//			sysOrganization.setId(id);
//			riskOrg.setSysOrganization(sysOrganization);
//			o_riskOrgDAO.merge(riskOrg);
//		}
//		
//		//添加相关部门
//		for(String id : relaOrgIds){
//			RiskOrg riskOrg = new RiskOrg();
//			riskOrg.setId(UUID.randomUUID().toString());
//			riskOrg.setRisk(risk);
//			riskOrg.setType("A");
//			SysOrganization sysOrganization = new SysOrganization();
//			sysOrganization.setId(id);
//			riskOrg.setSysOrganization(sysOrganization);
//			o_riskOrgDAO.merge(riskOrg);
//		}
//		
//		//添加影响指标
//		for(String id : influenceKpiIds){
//			KpiRelaRisk kpiRelaRisk = new KpiRelaRisk();
//			kpiRelaRisk.setId(UUID.randomUUID().toString());
//			kpiRelaRisk.setRisk(risk);
//			kpiRelaRisk.setType("I");
//			Kpi kpi = new Kpi();
//			kpi.setId(id);
//			kpiRelaRisk.setKpi(kpi);
//			o_kpiRelaRiskDAO.merge(kpiRelaRisk);
//		}
//		
//		//添加影响流程
//		for(String id : influeceProcessIds){
//			ProcessRelaRisk processRelaRisk = new ProcessRelaRisk();
//			processRelaRisk.setId(UUID.randomUUID().toString());
//			processRelaRisk.setRisk(risk);
//			processRelaRisk.setType("I");
//			Process process = new Process();
//			process.setId(id);
//			processRelaRisk.setProcess(process);
//			o_processRelaRiskDAO.merge(processRelaRisk);
//		}
//				
		return true;
	}

	/**
	 * 修改风险
	 * @author 郑军祥
	 * @param risk					风险实体
	 * @param respOrgIds			风险责任部门
     * @param relaOrgIds			风险相关部门
	 * @param influenceKpiIds		影响指标
	 * @param influeceProcessIds	影响流程
	 * @return true:成功 false:失败
	 */
	@Transactional
	public boolean mergeRiskEvent(Risk risk,List<String> respOrgIds,List<String> relaOrgIds,
            List<String> influenceKpiIds,List<String> influeceProcessIds) {
		
		//删除责任部门
		for(String id : respOrgIds){
			o_riskOrgDAO.delete(id);
		}
		//删除相关部门
		for(String id : relaOrgIds){
			o_riskOrgDAO.delete(id);
		}

		//删除影响指标
		for(String id : influenceKpiIds){
			o_kpiRelaRiskDAO.delete(id);
		}
		
		//删除影响流程
		for(String id : influeceProcessIds){
			o_processRelaRiskDAO.delete(id);
		}
				
		o_riskDAO.merge(risk);
		
		//添加责任部门
		for(String id : respOrgIds){
			RiskOrg riskOrg = new RiskOrg();
			riskOrg.setId(UUID.randomUUID().toString());
			riskOrg.setRisk(risk);
			riskOrg.setType("M");
			SysOrganization sysOrganization = new SysOrganization();
			sysOrganization.setId(id);
			riskOrg.setSysOrganization(sysOrganization);
			o_riskOrgDAO.merge(riskOrg);
		}
		
		//添加相关部门
		for(String id : relaOrgIds){
			RiskOrg riskOrg = new RiskOrg();
			riskOrg.setId(UUID.randomUUID().toString());
			riskOrg.setRisk(risk);
			riskOrg.setType("A");
			SysOrganization sysOrganization = new SysOrganization();
			sysOrganization.setId(id);
			riskOrg.setSysOrganization(sysOrganization);
			o_riskOrgDAO.merge(riskOrg);
		}
		
		//添加影响指标
		for(String id : influenceKpiIds){
			KpiRelaRisk kpiRelaRisk = new KpiRelaRisk();
			kpiRelaRisk.setId(UUID.randomUUID().toString());
			kpiRelaRisk.setRisk(risk);
			kpiRelaRisk.setType("I");
			Kpi kpi = new Kpi();
			kpi.setId(id);
			kpiRelaRisk.setKpi(kpi);
			o_kpiRelaRiskDAO.merge(kpiRelaRisk);
		}
		
		//添加影响流程
		for(String id : influeceProcessIds){
			ProcessRelaRisk processRelaRisk = new ProcessRelaRisk();
			processRelaRisk.setId(UUID.randomUUID().toString());
			processRelaRisk.setRisk(risk);
			processRelaRisk.setType("I");
			Process process = new Process();
			process.setId(id);
			processRelaRisk.setProcess(process);
			o_processRelaRiskDAO.merge(processRelaRisk);
		}
		
		return true;
	}

	/**
	 * 删除风险
	 * @author 郑军祥
	 * @param id
	 * @return
	 */
	@Transactional
	public boolean removeRiskById(String id) {
		Risk risk = findRiskById(id);
		risk.setDeleteStatus("0");
		o_riskDAO.merge(risk);
		return true;
	}

	/**
	 * 批量删除风险
	 * @author 郑军祥
	 * @param ids
	 * @return
	 */
	@Transactional
	public boolean removeRiskByIds(String[] ids) {
		for (String id : ids) {
			Risk risk = findRiskById(id);
			risk.setDeleteStatus("0");
			o_riskDAO.merge(risk);
		}
		return true;
	}
	
	/**
	 * 查询所有风险信息
	 * @author 郑军祥 2013-6-25
	 * @return Map map的key值是riskId,value值是risk对象
	 */
	public Map<String, Risk> getAllRiskInfo() {
		Map<String, Risk> map = new HashMap<String,Risk>();
		List<Risk> riskList = getAllRisk();
		for(Risk risk : riskList){
			map.put(risk.getId(), risk);
		}
		return map;
	}
	
	public Map<String, Object> findRiskInfoById(String riskId) {
		Risk risk = findRiskById(riskId);
		
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
	 * 查询风险信息
	 * @author 郑军祥
	 * @param ids
	 * @return Map map的key值是riskId,value值是risk对象
	 */
	public Map<Integer, Map<String, Object>> findRiskInfoByIds(String params) {
		List<Risk> risks = null;
		Map<Integer, Map<String, Object>> mapsCount = new HashMap<Integer, Map<String, Object>>();
		JSONArray jsonarr = JSONArray.fromObject(params);
		
		String[] riskIds = new String[jsonarr.size()];
		int j = 0;
		for (Object objects : jsonarr) {
			JSONObject jsobjs = (JSONObject) objects;
			riskIds[j] = jsobjs.getString("riskId");
			j++;
		}
		risks = this.findRisksByIds(riskIds);
		
		int count = 0;
		for (Risk risk : risks) {
			//Risk risk = risks.get(count);
			Map<String, Object> map = new HashMap<String, Object>();

			// 上级风险
			// JSONArray jsonArray = new JSONArray();
			String parentName = "";
			if (risk.getParent() != null) {
				// JSONObject object = new JSONObject();
				// object.put("id", risk.getParent().getId());
				parentName = risk.getParent().getName();
				// object.put("name", risk.getParent().getName());
				// jsonArray.add(object);
			}
			map.put("parentRiskName", parentName);
			map.put("code", risk.getCode());
			map.put("riskName", risk.getName());
			map.put("desc", risk.getDesc());

			// 责任部门/人和相关部门/人
			JSONObject object = new JSONObject();
			JSONArray respDeptName = new JSONArray();
			JSONArray relaDeptName = new JSONArray();
			Set<RiskOrg> riskOrgs = risk.getRiskOrgs();
			for (RiskOrg riskOrg : riskOrgs) {
				if (riskOrg.getType().equals("M")) { // 责任部门/人
					object = new JSONObject();
					SysOrganization sysOrganizationIte = riskOrg
							.getSysOrganization();
					if (sysOrganizationIte != null) {
						object.put("deptid", sysOrganizationIte.getOrgname());
					}
					SysEmployee empIte = riskOrg.getEmp();
					if (empIte != null) {
						object.put("empid", empIte.getEmpname());
					} else {
						object.put("empid", "");
					}
					respDeptName.add(object);
				}
				if (riskOrg.getType().equals("A")) { // 相关部门/人
					object = new JSONObject();
					SysOrganization sysOrganizationIte = riskOrg
							.getSysOrganization();
					if (sysOrganizationIte != null) {
						object.put("deptid", sysOrganizationIte.getOrgname());
					}
					SysEmployee empIte = riskOrg.getEmp();
					if (empIte != null) {
						object.put("empid", empIte.getEmpname());
					} else {
						object.put("empid", "");
					}
					relaDeptName.add(object);
				}
			}

			String respDept = "";
			String relaDept = "";
			for (int i = 0; i < respDeptName.size(); i++) {
				JSONObject jsobj = respDeptName.getJSONObject(i);
				respDept += jsobj.get("deptid") + ",";
			}

			for (int i = 0; i < relaDeptName.size(); i++) {
				JSONObject jsobj = relaDeptName.getJSONObject(i);
				relaDept += jsobj.get("deptid") + ",";
			}
			respDept = respDept + ";";
			relaDept = relaDept + ";";
			map.put("respDeptName", respDept.replace(",;", ""));
			map.put("relaDeptName", relaDept.replace(",;", ""));

			// 风险类别
			String riskKind = "";
			Set<DictEntryRelation> dictEntryRelations = risk.getRiskKinds();
			for (DictEntryRelation relation : dictEntryRelations) {
				riskKind += relation.getDictEntry().getId() + ",";
			}
			if (!riskKind.equals("")) {
				riskKind = riskKind.substring(0, riskKind.length() - 1);
			}
			map.put("riskKind", riskKind);

			// 定量分析
			map.put("isFix", risk.getIsFix());

			// 是否启用
			map.put("isUse", risk.getIsUse());

			// 影响指标，风险指标
			String riskKpiName = "";
			String influKpiName = "";
			Set<KpiRelaRisk> kpiRelaRisks = risk.getKpiRelaRisks();
			for (KpiRelaRisk kpi : kpiRelaRisks) {
				if (kpi.getType().equals("RM")) { // 风险指标
					riskKpiName += kpi.getKpi().getId() + ",";
				}
				if (kpi.getType().equals("I")) { // 影响指标
					influKpiName += kpi.getKpi().getName() + ",";
				}
			}
			if (!riskKpiName.equals("")) {
				riskKpiName = riskKpiName
						.substring(0, riskKpiName.length() - 1);
			}
			if (!influKpiName.equals("")) {
				influKpiName = influKpiName.substring(0,
						influKpiName.length() - 1);
			}

			map.put("riskKpiName", riskKpiName);
			map.put("influKpiName", influKpiName);

			// 影响流程，控制流程
			String controlProcessureName = "";
			String influProcessureName = "";
			Set<ProcessRelaRisk> ProcessRelaRisks = risk.getRiskProcessures();
			for (ProcessRelaRisk processure : ProcessRelaRisks) {
				if (processure.getType().equals("C")) { // 控制流程
					controlProcessureName += processure.getProcess().getId()
							+ ",";
				}
				if (processure.getType().equals("I")) { // 影响指标
					influProcessureName += processure.getProcess().getName()
							+ ",";
				}
			}
			if (!controlProcessureName.equals("")) {
				controlProcessureName = controlProcessureName.substring(0,
						controlProcessureName.length() - 1);
			}
			if (!influProcessureName.equals("")) {
				influProcessureName = influProcessureName.substring(0,
						influProcessureName.length() - 1);
			}
			map.put("controlProcessureName", controlProcessureName);
			map.put("influProcessureName", influProcessureName);

			// 是否继承
			map.put("isInherit", risk.getIsInherit());
			// 模板下拉框
			map.put("templateId", risk.getTemplate() == null ? "" : risk
					.getTemplate().getId());
			// 告警方案
			map.put("alarmPlanId", risk.getAlarmScenario() == null ? "" : risk
					.getAlarmScenario().getId());
			// 监控频率
			map.put("monitorFrequence", risk.getMonitorFrequence());
			// 公式计算
			map.put("formulaDefine", risk.getFormulaDefine());
			// 涉及版块
			String relePlate = "";
			Set<DictEntryRelation> palteRelations = risk.getRelePlates();
			for (DictEntryRelation relation : palteRelations) {
				relePlate += relation.getDictEntry().getId() + ",";
			}
			if (!relePlate.equals("")) {
				relePlate = relePlate.substring(0, relePlate.length() - 1);
			}
			map.put("relePlate", relePlate);
			mapsCount.put(count, map);
			count++;
		}

		return mapsCount;

	}
	
	/**
	 * 根据风险事件id数组，查询合并的风险事件。用于合并风险事件
	 * @author 郑军祥
	 * @param id
	 * @return map{parentRisk:risk对象，riskCodes:List<String>,respDeptIds:List<String>,relaDeptIds:List<String>,influKpiIds:List<String>,influProcessIds:List<String>}
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> findRiskByEventsId(String[] ids) {
		Map<String, Object> map = new HashMap<String,Object>();
		Criteria criteria = o_riskDAO.createCriteria();
		
		criteria.add(Restrictions.in("id", ids));
		List<Risk> risks = criteria.list();
		
		Risk parentRisk = risks.get(0).getParent();
		List<String> riskCodes = new ArrayList<String>();
		List<String> respDeptIds = new ArrayList<String>();
		List<String> relaDeptIds = new ArrayList<String>();
		List<String> influKpiIds = new ArrayList<String>();
		List<String> influProcessIds = new ArrayList<String>();
		for(Risk risk : risks){
			//风险编号
			riskCodes.add(risk.getCode());
			
			//责任相关部门
			Set<RiskOrg> riskOrgs = risk.getRiskOrgs();
			for(RiskOrg riskOrg : riskOrgs){
				if(riskOrg.getType().equals("M")){
					String id = riskOrg.getSysOrganization().getId();
					if(!respDeptIds.contains(id)){
						respDeptIds.add(id);
					}
				}
				if(riskOrg.getType().equals("A")){
					String id = riskOrg.getSysOrganization().getId();
					if(!relaDeptIds.contains(id)){
						relaDeptIds.add(id);
					}
				}
			}
			
			//影响指标
			Set<KpiRelaRisk> kpiRelaRisks = risk.getKpiRelaRisks();
			for(KpiRelaRisk kpiRelaRisk : kpiRelaRisks){
				if(kpiRelaRisk.getType().equals("I")){
					String id = kpiRelaRisk.getKpi().getId();
					if(!influKpiIds.contains(id)){
						influKpiIds.add(id);
					}
				}
			}
			
			//影响流程
			Set<ProcessRelaRisk> processRelaRisks = risk.getRiskProcessures();
			for(ProcessRelaRisk processRelaRisk : processRelaRisks){
				if(processRelaRisk.getType().equals("I")){
					String id = processRelaRisk.getProcess().getId();
					if(!influProcessIds.contains(id)){
						influProcessIds.add(id);
					}
				}
			}
		}
		map.put("parentRisk", parentRisk);
		map.put("riskCodes", riskCodes);
		map.put("respDeptIds", respDeptIds);
		map.put("relaDeptIds", relaDeptIds);
		map.put("influKpiIds", influKpiIds);
		map.put("influProcessIds", influProcessIds);
		
		return map;
	}
	
	/**
	 * 按Id查询风险
	 * @author 郑军祥
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Risk findRiskById(String id) {
		Criteria criteria = o_riskDAO.createCriteria();
		List<Risk> list = null;
		
		criteria.add(Restrictions.eq("id", id));
		//加这一段是为了查询关联表是不发送查询请求，否则添加编辑后查询出现hibnate问题，现在已经把关联的缓存去掉了还有问题，所以必须这么加
		criteria.createAlias("riskOrgs", "riskOrgs",CriteriaSpecification.LEFT_JOIN);
		criteria.setFetchMode("riskOrgs", FetchMode.JOIN);
		criteria.createAlias("riskPosition", "riskPosition",CriteriaSpecification.LEFT_JOIN);
		criteria.setFetchMode("riskPosition", FetchMode.JOIN);
		criteria.createAlias("riskEmp", "riskEmp",CriteriaSpecification.LEFT_JOIN);
		criteria.setFetchMode("riskEmp", FetchMode.JOIN);
		criteria.createAlias("kpiRelaRisks", "kpiRelaRisks",CriteriaSpecification.LEFT_JOIN);
		criteria.setFetchMode("kpiRelaRisks", FetchMode.JOIN);
		
		list = criteria.list();
		if(list.size() != 0)
			return (Risk) criteria.list().get(0);
		else
			return null;
	}
	
	/**
	 * 按Id数组查询风险数组
	 * @author 郑军祥
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Risk> findRisksByIds(String[] ids) {
		Criteria criteria = o_riskDAO.createCriteria();
		criteria.add(Restrictions.in("id", ids));
		
		return criteria.list();
	}
	
	/**
	 * 查询所有风险
	 * @author 郑军祥
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Risk> getAllRisk() {
		Criteria criteria = o_riskDAO.createCriteria();
		criteria.add(Restrictions.eq("deleteStatus", "1"));
		
		return criteria.list();
	}
}
