/**
 * RiskBO.java
 * com.fhd.risk.business
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-19 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * RiskBO.java
 * com.fhd.risk.business
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-19        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
*/


package com.fhd.risk.business;

import static org.hibernate.criterion.Restrictions.in;
import static org.hibernate.criterion.Restrictions.like;
import static org.hibernate.criterion.Restrictions.ne;
import static org.hibernate.criterion.Restrictions.not;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fhd.core.dao.Page;
import com.fhd.fdc.utils.UserContext;
import com.fhd.kpi.business.KpiBO;
import com.fhd.kpi.entity.Kpi;
import com.fhd.kpi.entity.RelaAssessResult;
import com.fhd.risk.dao.OrgAdjustHistoryDAO;
import com.fhd.risk.dao.ProcessAdjustHistoryDAO;
import com.fhd.risk.dao.RiskAdjustHistoryDAO;
import com.fhd.risk.dao.RiskDAO;
import com.fhd.risk.dao.StrategyAdjustHistoryDAO;
import com.fhd.risk.entity.KpiRelaRisk;
import com.fhd.risk.entity.OrgAdjustHistory;
import com.fhd.risk.entity.ProcessAdjustHistory;
import com.fhd.risk.entity.Risk;
import com.fhd.risk.entity.RiskAdjustHistory;
import com.fhd.risk.entity.RiskOrg;
import com.fhd.risk.entity.StrategyAdjustHistory;
import com.fhd.risk.interfaces.IKpiRelaRiskBO;
import com.fhd.risk.interfaces.IRiskBO;
import com.fhd.sys.business.dic.DictBO;
import com.fhd.sys.business.dic.DictEntryRelationBO;
import com.fhd.sys.dao.orgstructure.SysEmployeeDAO;
import com.fhd.sys.dao.orgstructure.SysOrgDao;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.dic.DictEntryRelation;
import com.fhd.sys.entity.dic.DictEntryRelationType;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;
import com.fhd.process.business.ProcessBO;
import com.fhd.process.entity.Process;
import com.fhd.process.entity.ProcessRelaRisk;
import com.fhd.process.interfaces.IProcessBO;

/**
 * 风险操作业务处理
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-19		上午09:27:46
 *
 * @see 	 
 */
@Service
public class RiskBO implements IRiskBO{
	
	@Autowired
	private RiskDAO o_riskDAO;
	
	@Autowired
	private RiskOrgBO o_riskOrgBO;
	
	@Autowired
	private SysOrgDao o_sysOrgDao;
	
	@Autowired
	private SysEmployeeDAO o_sysEmployeeDao;
	
	//流程
	@Autowired
	private ProcessBO o_processBo;
	@Autowired
	private ProcessRelaRiskBO o_processRelaRiskBo;
	
	//字典
	@Autowired
	private DictBO o_dictBo;
	@Autowired
	private DictEntryRelationBO o_dictEntryRelationBo;
	
	/**
	 * 历史记录
	 */
	@Autowired
	private RiskAdjustHistoryDAO o_riskAdjustHistoryDAO;
	@Autowired
	private OrgAdjustHistoryDAO o_orgAdjustHistoryDAO;
	@Autowired
	private ProcessAdjustHistoryDAO o_processAdjustHistoryDAO;
	@Autowired
	private StrategyAdjustHistoryDAO o_strategyAdjustHistoryDAO;
	
	/**
	 * 指标相关
	 */
	@Autowired
	private KpiBO o_kpiBO;
	
	@Autowired
	private IKpiRelaRiskBO o_kpiRelaRiskBO;	
	
	public Risk findRiskByCompanyId(String id) {
		Criteria criteria = o_riskDAO.createCriteria();
		if (StringUtils.isNotBlank(id)) {
			criteria.add(Restrictions.eq("company.id", id));
		}
		criteria.add(Restrictions.isNull("parent"));
		criteria.add(Restrictions.eq("deleteStatus", "1"));
		criteria.add(Restrictions.eq("isRiskClass", "RBS"));
		return (Risk) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Risk> findRiskBySome(String companyId, String deleteStatus) {
		Criteria criteria = o_riskDAO.createCriteria();
		if (StringUtils.isNotBlank(companyId)) {
			criteria.add(Restrictions.eq("company.id", companyId));
		}
		criteria.add(Restrictions.eq("deleteStatus", deleteStatus));
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Risk> findRiskBySome(String companyId,
			String parentId, String deleteStatus, Boolean rbs) {
		Criteria criteria = o_riskDAO.createCriteria();
		if (StringUtils.isNotBlank(companyId)) {
			criteria.add(Restrictions.eq("company.id", companyId));
		}
		if (StringUtils.isNotBlank(parentId)) {
			if(parentId.equalsIgnoreCase("root")){
				criteria.add(Restrictions.isNull("parent.id"));
			}else{
				criteria.add(Restrictions.eq("parent.id", parentId));
			}
			
		}
		criteria.add(Restrictions.eq("deleteStatus", deleteStatus));
		
		if(rbs){
			criteria.add(Restrictions.eq("isRiskClass", "RBS"));
		}
		//不做left join
		criteria.setFetchMode("createBy", FetchMode.SELECT);
		criteria.setFetchMode("lastModifyBy", FetchMode.SELECT);
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Risk> findRiskMapByCompanyId(String companyId) {
		List<Risk> riskList = null;
		HashMap<String, Risk> riskMap = new HashMap<String, Risk>();

		Criteria criteria = o_riskDAO.createCriteria();
		if (StringUtils.isNotBlank(companyId)) {
			criteria.add(Restrictions.eq("company.id", companyId));
		}
		criteria.add(Restrictions.eq("deleteStatus", "1"));
		
		//不做left join
		criteria.setFetchMode("createBy", FetchMode.SELECT);
		criteria.setFetchMode("lastModifyBy", FetchMode.SELECT);
		
		riskList = criteria.list();

		for (Risk risk : riskList) {
			if (risk.getParent() != null) {
				riskMap.put(risk.getParent().getId(), risk);
			}
		}

		return riskMap;
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Risk> findRiskMapFromNotRiskOrgByCompanyId(String companyId) {
		List<Risk> riskList = null;
		HashMap<String, Risk> riskMap = new HashMap<String, Risk>();
		Set<String> otheridSet = o_riskOrgBO.findRiskOrg();
		Criteria criteria = o_riskDAO.createCriteria();
		if (otheridSet.size() > 0) {
			criteria.add(Restrictions.not(Restrictions.in("id", otheridSet)));
		}
		if (StringUtils.isNotBlank(companyId)) {
			criteria.add(Restrictions.eq("company.id", companyId));
		}
		criteria.add(Restrictions.eq("deleteStatus", "1"));

		riskList = criteria.list();

		for (Risk risk : riskList) {
			if (risk.getParent() != null) {
				riskMap.put(risk.getParent().getId(), risk);
			}
		}

		return riskMap;
	}
	
	@SuppressWarnings("unchecked")
	public Set<String> findRiskMapFromNotRiskOrgBySome(String id, String query) {
		Set<String> idSet = new HashSet<String>();
		Set<String> otheridSet = o_riskOrgBO.findRiskOrg();
		Criteria criteria = this.o_riskDAO.createCriteria();
		List<Risk> strategyList = null;
		
		
		criteria.add(not(in("id", otheridSet)));
		if (StringUtils.isNotBlank(query)) {
			criteria.add(like("name", query, MatchMode.ANYWHERE)).add(
					ne("deleteStatus", "0"));
		}
		strategyList = criteria.list();
		for (Risk entity : strategyList) {
			String[] idsTemp = entity.getIdSeq().split("\\.");
			idSet.addAll(Arrays.asList(idsTemp));
		}
		
		return idSet;
	}
	
	@SuppressWarnings("unchecked")
	public List<Risk> findNotInOrgStrategyMap(String id, String deleteStatus, Boolean rbs) {
		Set<String> otheridSet =o_riskOrgBO.findRiskOrg();
		Criteria criteria = o_riskDAO.createCriteria();
		
		if (otheridSet.size() > 0) {
			criteria.add(Restrictions.not(Restrictions.in("id", otheridSet)));
		}
		
		criteria.add(Restrictions.eq("parent.id", id));
		criteria.add(Restrictions.eq("deleteStatus", "1"));
		
		if(rbs){
			criteria.add(Restrictions.eq("isRiskClass", "RBS"));
		}
		
		List<Risk> riskList = criteria.list();
		
		return riskList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Risk> finRiskBySome(String searchName, String companyId, String deleteStatus){
		List<Risk> riskList = new ArrayList<Risk>();
		Criteria criteria = o_riskDAO.createCriteria();
		
		if (StringUtils.isNotBlank(searchName)) {
			criteria.add(Restrictions.like("name", searchName, MatchMode.ANYWHERE));
		}
		criteria.add(Restrictions.eq("company.id", companyId));
		riskList = criteria.list();
		
		return riskList;
	}
	
	public Risk findRiskParentIsNull(){
		return (Risk)o_riskDAO.createCriteria().add(Restrictions.isNull("parent")).uniqueResult();
	}

	public List<Risk> findRiskByIdAndCompanyIdAndDeleteStatus(String id,
			String companyId, boolean deleteStatus) {
		List<Risk> list = null;
		List<Risk> tempList = new ArrayList<Risk>();
		
		if(deleteStatus){
			list = this.findRiskBySome(companyId, "1");
		}else{
			list = this.findRiskBySome(companyId, "0");
		}
		
		if(StringUtils.isNotBlank(id)){
			for (Risk risk : list) {
				if(StringUtils.isNotBlank(risk.getIdSeq()))
				if(risk.getIdSeq().contains(id)){
					if(!risk.getId().equalsIgnoreCase(id)){
						tempList.add(risk);
					}
				}
			}
		}else{
			return list;
		}
		
		return tempList;
	}

	@SuppressWarnings("unchecked")
	public Risk findRiskById(String id, boolean deleteStatus) {
		Criteria criteria = o_riskDAO.createCriteria();
		List<Risk> list = null;
		
		criteria.add(Restrictions.eq("id", id));
		if(deleteStatus){
			criteria.add(Restrictions.eq("deleteStatus", "1"));
		}else{
			criteria.add(Restrictions.eq("deleteStatus", "0"));
		}
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
	 * 根据公司id查询机构
	 * @author zhengjunxiang
	 * @param companyId
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	public SysOrganization findOrganizationByOrgId(String companyId) {
		Criteria c = o_sysOrgDao.createCriteria();
		List<SysOrganization> list = null;
		if (StringUtils.isNotBlank(companyId)) {
			c.add(Restrictions.eq("id", companyId));
		} else {
			return null;
		}
		list = c.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public SysEmployee findEmployeeById(String employeeId) {
		Criteria c = o_sysEmployeeDao.createCriteria();
		List<SysEmployee> list = null;
		if (StringUtils.isNotBlank(employeeId)) {
			c.add(Restrictions.eq("id", employeeId));
		} else {
			return null;
		}
		list = c.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	@Transactional
	public void saveRisk(Risk risk){
		o_riskDAO.merge(risk);
	}
	
	@Transactional
	public void addRisk(Risk risk,String respDeptName,String relaDeptName,
			String respPositionName, String relaPositionName, String riskKind,
			String relePlate, String riskKpiName, String influKpiName,
			String controlProcessureName, String influProcessureName){
		o_riskDAO.merge(risk);
		
		//添加责任部门/人和相关部门/人
		JSONObject object = new JSONObject();
		RiskOrg riskOrg = new RiskOrg();
		if(null!=respDeptName && !"".equals(respDeptName)){
			JSONArray respDeptArray = JSONArray.fromObject(respDeptName);
			for(int i=0;i<respDeptArray.size();i++){
				object = (JSONObject)respDeptArray.get(i);
				//保存责任部门/责任人
				SysOrganization org = findOrganizationByOrgId(object.get("deptid").toString());
				SysEmployee emp = findEmployeeById(object.get("empid").toString());
				riskOrg = new RiskOrg();
				riskOrg.setId(UUID.randomUUID().toString());
				riskOrg.setRisk(risk);
				riskOrg.setType("M");
				riskOrg.setSysOrganization(org);
				riskOrg.setEmp(emp);
				o_riskOrgBO.saveRiskOrg(riskOrg);
			}
		}
		if(null!=relaDeptName && !"".equals(relaDeptName)){
			JSONArray relaDeptArray = JSONArray.fromObject(relaDeptName);
			for(int i=0;i<relaDeptArray.size();i++){
				object = (JSONObject)relaDeptArray.get(i);
				//保存相关部门
				SysOrganization org = findOrganizationByOrgId(object.get("deptid").toString());
				SysEmployee emp = findEmployeeById(object.get("empid").toString());
				riskOrg = new RiskOrg();
				riskOrg.setId(UUID.randomUUID().toString());
				riskOrg.setRisk(risk);
				riskOrg.setType("A");
				riskOrg.setSysOrganization(org);
				riskOrg.setEmp(emp);
				o_riskOrgBO.saveRiskOrg(riskOrg);
			}
		}
		
		//添加风险指标和影响指标
		KpiRelaRisk kpiRelaRisk = new KpiRelaRisk();
		if(null!=riskKpiName && !"".equals(riskKpiName)){
			String[] riskKpiArray = riskKpiName.split(",");
			for(int i=0;i<riskKpiArray.length;i++){
				String id = riskKpiArray[i];
				//保存风险指标
				Kpi kpi = o_kpiBO.findKpiById(id);
				kpiRelaRisk = new KpiRelaRisk();
				kpiRelaRisk.setId(UUID.randomUUID().toString());
				kpiRelaRisk.setRisk(risk);
				kpiRelaRisk.setType("RM");
				kpiRelaRisk.setKpi(kpi);
				o_kpiRelaRiskBO.saveKpiRelaRisk(kpiRelaRisk);
			}
		}
		if(null!=influKpiName && !"".equals(influKpiName)){
			String[] influKpiArray = influKpiName.split(",");
			for(int i=0;i<influKpiArray.length;i++){
				String id = influKpiArray[i];
				//保存影响指标
				Kpi kpi = o_kpiBO.findKpiById(id);
				kpiRelaRisk = new KpiRelaRisk();
				kpiRelaRisk.setId(UUID.randomUUID().toString());
				kpiRelaRisk.setRisk(risk);
				kpiRelaRisk.setType("I");
				kpiRelaRisk.setKpi(kpi);
				o_kpiRelaRiskBO.saveKpiRelaRisk(kpiRelaRisk);
			}
		}
		
		//添加控制流程和影响流程
		ProcessRelaRisk processRelaRisk = new ProcessRelaRisk();
		if(null!=controlProcessureName && !"".equals(controlProcessureName)){
			String[] processArray = controlProcessureName.split(",");
			for(int i=0;i<processArray.length;i++){
				String id = processArray[i];
				//保存控制流程
				Process process = o_processBo.findProcessById(id);
				processRelaRisk = new ProcessRelaRisk();
				processRelaRisk.setId(UUID.randomUUID().toString());
				processRelaRisk.setRisk(risk);
				processRelaRisk.setType("C");
				processRelaRisk.setProcess(process);
				o_processRelaRiskBo.saveProcessRelaRisk(processRelaRisk);
			}
		}
		if(null!=influProcessureName && !"".equals(influProcessureName)){
			String[] processArray = influProcessureName.split(",");
			for(int i=0;i<processArray.length;i++){
				String id = processArray[i];
				//保存影响流程
				Process process = o_processBo.findProcessById(id);
				processRelaRisk = new ProcessRelaRisk();
				processRelaRisk.setId(UUID.randomUUID().toString());
				processRelaRisk.setRisk(risk);
				processRelaRisk.setType("I");
				processRelaRisk.setProcess(process);
				o_processRelaRiskBo.saveProcessRelaRisk(processRelaRisk);
			}
		}
		
		//添加风险类别和涉及版块
		DictEntryRelation dictRelation = new DictEntryRelation();
		if(null!=riskKind && !"".equals(riskKind)){
			String[] dictArray = riskKind.split(",");
			for(int i=0;i<dictArray.length;i++){
				String id = dictArray[i];
				//保存风险类别
				DictEntry dictEntry = o_dictBo.findDictEntryById(id);
				DictEntryRelationType relationType = o_dictEntryRelationBo.findDictEntryRelationTypeByTypeId("1");	//1代表风险类别
				dictRelation = new DictEntryRelation();
				dictRelation.setId(UUID.randomUUID().toString());
				dictRelation.setBusinessId(risk.getId());	//业务id
				dictRelation.setRelationType(relationType);
				dictRelation.setDictEntry(dictEntry);
				o_dictEntryRelationBo.saveDictEntryRelation(dictRelation);
			}
		}
		if(null!=relePlate && !"".equals(relePlate)){
			String[] dictArray = relePlate.split(",");
			for(int i=0;i<dictArray.length;i++){
				String id = dictArray[i];
				//保存涉及版块
				DictEntry dictEntry = o_dictBo.findDictEntryById(id);
				DictEntryRelationType relationType = o_dictEntryRelationBo.findDictEntryRelationTypeByTypeId("2");	//1代表涉及版块
				dictRelation = new DictEntryRelation();
				dictRelation.setId(UUID.randomUUID().toString());
				dictRelation.setBusinessId(risk.getId());	//业务id
				dictRelation.setRelationType(relationType);
				dictRelation.setDictEntry(dictEntry);
				o_dictEntryRelationBo.saveDictEntryRelation(dictRelation);
			}
		}
		
		//更新上级风险，修改叶子节点
		Risk parent = risk.getParent();
		if(null!=parent){
			parent.setIsLeaf(false);
			saveRisk(parent);
		}
	}
	
	@Transactional
	public void updateRisk(Risk risk,String respDeptName,String relaDeptName,
			String respPositionName, String relaPositionName, String riskKind,
			String relePlate, String riskKpiName, String influKpiName,
			String controlProcessureName, String influProcessureName){
		
		//删除责任部门/人和相关部门/人
		Set<RiskOrg> orgs = risk.getRiskOrgs();
		for(RiskOrg org : orgs){
			if(org.getType().equals("M") || org.getType().equals("A")){
				o_riskOrgBO.removeRiskOrgById(org);
			}
		}
		//删除风险指标和影响指标
		Set<KpiRelaRisk> kpis = risk.getKpiRelaRisks();
		for(KpiRelaRisk kpi : kpis){
			if(kpi.getType().equals("RM") || kpi.getType().equals("I")){
				o_kpiRelaRiskBO.removeKpiRelaRiskById(kpi.getId());
			}
		}
		//删除控制流程和影响流程
		Set<ProcessRelaRisk> processes = risk.getRiskProcessures();
		for(ProcessRelaRisk process : processes){
			if(process.getType().equals("C") || process.getType().equals("I")){
				o_processRelaRiskBo.removeProcessRelaRiskById(process.getId());
			}
		}
		
		o_riskDAO.merge(risk);
		
		//添加责任部门/人和相关部门/人
		JSONObject object = new JSONObject();
		RiskOrg riskOrg = new RiskOrg();
		if(null!=respDeptName && !"".equals(respDeptName)){
			JSONArray respDeptArray = JSONArray.fromObject(respDeptName);
			for(int i=0;i<respDeptArray.size();i++){
				object = (JSONObject)respDeptArray.get(i);
				//保存责任部门/人
				SysOrganization org = findOrganizationByOrgId(object.get("deptid").toString());
				SysEmployee emp = findEmployeeById(object.get("empid").toString());
				riskOrg = new RiskOrg();
				riskOrg.setId(UUID.randomUUID().toString());
				riskOrg.setRisk(risk);
				riskOrg.setType("M");
				riskOrg.setSysOrganization(org);
				riskOrg.setEmp(emp);
				o_riskOrgBO.saveRiskOrg(riskOrg);
			}
		}
		if(null!=relaDeptName && !"".equals(relaDeptName)){
			JSONArray relaDeptArray = JSONArray.fromObject(relaDeptName);
			for(int i=0;i<relaDeptArray.size();i++){
				object = (JSONObject)relaDeptArray.get(i);
				//保存相关部门
				SysOrganization org = findOrganizationByOrgId(object.get("deptid").toString());
				SysEmployee emp = findEmployeeById(object.get("empid").toString());
				riskOrg = new RiskOrg();
				riskOrg.setId(UUID.randomUUID().toString());
				riskOrg.setRisk(risk);
				riskOrg.setType("A");
				riskOrg.setSysOrganization(org);
				riskOrg.setEmp(emp);
				o_riskOrgBO.saveRiskOrg(riskOrg);
			}
		}
		
		//添加风险指标和影响指标
		KpiRelaRisk kpiRelaRisk = new KpiRelaRisk();
		if(null!=riskKpiName && !"".equals(riskKpiName)){
			String[] riskKpiArray = riskKpiName.split(",");
			for(int i=0;i<riskKpiArray.length;i++){
				String id = riskKpiArray[i];
				//保存风险指标
				Kpi kpi = o_kpiBO.findKpiById(id);
				kpiRelaRisk = new KpiRelaRisk();
				kpiRelaRisk.setId(UUID.randomUUID().toString());
				kpiRelaRisk.setRisk(risk);
				kpiRelaRisk.setType("RM");
				kpiRelaRisk.setKpi(kpi);
				o_kpiRelaRiskBO.saveKpiRelaRisk(kpiRelaRisk);
			}
		}
		if(null!=influKpiName && !"".equals(influKpiName)){
			String[] influKpiArray = influKpiName.split(",");
			for(int i=0;i<influKpiArray.length;i++){
				String id = influKpiArray[i];
				//保存影响指标
				Kpi kpi = o_kpiBO.findKpiById(id);
				kpiRelaRisk = new KpiRelaRisk();
				kpiRelaRisk.setId(UUID.randomUUID().toString());
				kpiRelaRisk.setRisk(risk);
				kpiRelaRisk.setType("I");
				kpiRelaRisk.setKpi(kpi);
				o_kpiRelaRiskBO.saveKpiRelaRisk(kpiRelaRisk);
			}
		}

		//添加控制流程和影响流程
		ProcessRelaRisk processRelaRisk = new ProcessRelaRisk();
		if(null!=controlProcessureName && !"".equals(controlProcessureName)){
			String[] processArray = controlProcessureName.split(",");
			for(int i=0;i<processArray.length;i++){
				String id = processArray[i];
				//保存控制流程
				Process process = o_processBo.findProcessById(id);
				processRelaRisk = new ProcessRelaRisk();
				processRelaRisk.setId(UUID.randomUUID().toString());
				processRelaRisk.setRisk(risk);
				processRelaRisk.setType("C");
				processRelaRisk.setProcess(process);
				o_processRelaRiskBo.saveProcessRelaRisk(processRelaRisk);
			}
		}
		if(null!=influProcessureName && !"".equals(influProcessureName)){
			String[] processArray = influProcessureName.split(",");
			for(int i=0;i<processArray.length;i++){
				String id = processArray[i];
				//保存影响流程
				Process process = o_processBo.findProcessById(id);
				processRelaRisk = new ProcessRelaRisk();
				processRelaRisk.setId(UUID.randomUUID().toString());
				processRelaRisk.setRisk(risk);
				processRelaRisk.setType("I");
				processRelaRisk.setProcess(process);
				o_processRelaRiskBo.saveProcessRelaRisk(processRelaRisk);
			}
		}
		
		//添加风险类别和涉及版块
		DictEntryRelation dictRelation = new DictEntryRelation();
		if(null!=riskKind && !"".equals(riskKind)){
			String[] dictArray = riskKind.split(",");
			for(int i=0;i<dictArray.length;i++){
				String id = dictArray[i];
				//保存风险类别
				DictEntry dictEntry = o_dictBo.findDictEntryById(id);
				DictEntryRelationType relationType = o_dictEntryRelationBo.findDictEntryRelationTypeByTypeId("1");	//1代表风险类别
				dictRelation = new DictEntryRelation();
				dictRelation.setId(UUID.randomUUID().toString());
				dictRelation.setBusinessId(risk.getId());	//业务id
				dictRelation.setRelationType(relationType);
				dictRelation.setDictEntry(dictEntry);
				o_dictEntryRelationBo.saveDictEntryRelation(dictRelation);
			}
		}
		if(null!=relePlate && !"".equals(relePlate)){
			String[] dictArray = relePlate.split(",");
			for(int i=0;i<dictArray.length;i++){
				String id = dictArray[i];
				//保存涉及版块
				DictEntry dictEntry = o_dictBo.findDictEntryById(id);
				DictEntryRelationType relationType = o_dictEntryRelationBo.findDictEntryRelationTypeByTypeId("2");	//1代表涉及版块
				dictRelation = new DictEntryRelation();
				dictRelation.setId(UUID.randomUUID().toString());
				dictRelation.setBusinessId(risk.getId());	//业务id
				dictRelation.setRelationType(relationType);
				dictRelation.setDictEntry(dictEntry);
				o_dictEntryRelationBo.saveDictEntryRelation(dictRelation);
			}
		}
	}
	
	@Transactional
	public void removeRiskByIds(String ids){
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			Risk risk = findRiskById(id,true);
			risk.setDeleteStatus("0");
			o_riskDAO.merge(risk);
			//removeRiskBySome(risk, risk.getChildren());
			
			//如果上级没有孩子，修改is_leaf属性
			Risk parent = risk.getParent();
			Criteria c = o_riskDAO.createCriteria();
			if(parent == null){
				c.add(Restrictions.isNull("parent.id"));
			}else{
				c.add(Restrictions.eq("parent.id", parent.getId()));
			}
			c.add(Restrictions.eq("deleteStatus", "1"));
			c.setProjection(Projections.rowCount());
			Long size = (Long)c.uniqueResult();
			if(size==0){	//risk是没删除前缓存的状态
				parent.setIsLeaf(true);
				o_riskDAO.merge(parent);
			}
		}
	}
	/**
	 * 从数据库删除选中风险及下级风险,丢弃
	 */
	 public void removeRiskBySome(Risk risk, Set<Risk> children) {
		if (children != null) {
		    for (Risk r: children) {
				if (r.getChildren() != null	&& r.getChildren().size() > 0) {
					removeRiskBySome(r, r.getChildren());
				} else {
					removeRiskBySome(r, null);
				}
		    }
		}
		if (risk != null) {
			risk.setDeleteStatus("0");
			o_riskDAO.merge(risk);
		}
    }
	 
	@SuppressWarnings("unchecked")
	public Page<RiskAdjustHistory> findRiskAdjustHistoryBySome(String id,Page<RiskAdjustHistory> page,String sort, String dir,String query){
		
		DetachedCriteria dc = DetachedCriteria.forClass(RiskAdjustHistory.class);
        dc.add(Restrictions.eq("risk.id", id));
        if(StringUtils.isNotBlank(query)){	//按年份查询
        	dc.createAlias("timePeriod", "timePeriod", CriteriaSpecification.LEFT_JOIN);
			dc.add(Restrictions.like("timePeriod.year",query,MatchMode.ANYWHERE));
		}
        String sortstr = sort;
        if ("year".equals(sort)) {
            dc.createAlias("timePeriod", "timePeriod", CriteriaSpecification.LEFT_JOIN);
            sortstr = "timePeriod.year";
        }else if ("month".equals(sort)) {
        	dc.createAlias("timePeriod", "timePeriod", CriteriaSpecification.LEFT_JOIN);
            sortstr = "timePeriod.month";
        }else if ("probability".equals(sort)) {
            sortstr = "probability";
        }else if ("impacts".equals(sort)) {
            sortstr = "impacts";
        }else if ("status".equals(sort)) {
            sortstr = "status";
        }else{
        }
        
        if ("ASC".equalsIgnoreCase(dir)) {
            dc.addOrder(Order.asc(sortstr));
        }
        else {
            dc.addOrder(Order.desc(sortstr));
        }
 
        return o_riskAdjustHistoryDAO.findPage(dc, page, false);
	}
	
	@SuppressWarnings("unchecked")
	public Page<Risk> findRiskEventBySome(String id,Page<Risk> page,String sort, String dir,String query){
		DetachedCriteria dc = DetachedCriteria.forClass(Risk.class);
		if(id.equalsIgnoreCase("root")){
			dc.add(Restrictions.isNull("parent.id"));
		}else{
			dc.add(Restrictions.eq("parent.id", id));
		}
        dc.add(Restrictions.eq("parent.id", id));
        dc.add(Restrictions.eq("deleteStatus", "1"));
        dc.add(Restrictions.eq("isRiskClass", "re"));
        if (StringUtils.isNotBlank(query)) {
			dc.add(Restrictions.like("name", query, MatchMode.ANYWHERE));
		}
 
        return o_riskDAO.findPage(dc, page, false);
	}
	
	@SuppressWarnings("unchecked")
	public RiskAdjustHistory findLatestRiskAdjustHistoryByRiskId(String id){
		
		Criteria criteria = o_riskAdjustHistoryDAO.createCriteria();
		
		criteria.add(Restrictions.eq("risk.id", id));
		//
		criteria.addOrder(Order.desc("adjustTime"));  
		criteria.setMaxResults(1);

		return (RiskAdjustHistory) criteria.uniqueResult();
	}
	/**
	 * 根据ID获得实体
	 * @param id
	 * @return
	 */
	public Risk findRiskById(String id){
		return this.o_riskDAO.get(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> riskTreeLoader(String id, String query, Boolean rbs, Boolean canChecked, String chooseId){
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		Criteria criteria = o_riskDAO.createCriteria();
		Set<String> idSet = queryObjectBySearchName(query,rbs);
		if(StringUtils.isNotBlank(query)&&idSet.size()==0){
			return nodes;
		}

		if("root".equals(id)){
			criteria.add(Restrictions.isNull("parent.id"));
		}else{
			criteria.add(Restrictions.eq("parent.id", id));
		}
		String companyId = UserContext.getUser().getCompanyid();
		criteria.add(Restrictions.eq("company.id", companyId));
		criteria.add(Restrictions.eq("deleteStatus", "1"));
		if(rbs){
			criteria.add(Restrictions.eq("isRiskClass", "RBS"));
		}
		criteria.addOrder(Order.asc("sort"));
		List<Risk> list = criteria.list();

		for (Risk risk : list) {
			if(!idSet.contains(risk.getId())){
				continue;
			}
			map = new HashMap<String, Object>();
			map.put("id", risk.getId());
			map.put("dbid", risk.getId());
			map.put("text", risk.getName());
			map.put("code", risk.getCode());
			map.put("type", "risk");
			//展示出不同的灯图标
			String iconCls = "icon-ibm-symbol-5-sm";	//分为高中低，默认中
			RiskAdjustHistory history = findLatestRiskAdjustHistoryByRiskId(risk.getId());
			if(history!=null){
				iconCls = history.getAssessementStatus();
			}
			map.put("iconCls", iconCls);//icon-org
			map.put("cls", "org");
			map.put("leaf", !riskHasChildren(risk.getId(),rbs));
			if(canChecked) {
				if(StringUtils.isNotEmpty(chooseId)){
					for (String choose : StringUtils.split(chooseId,",")) {
						if(map.get("id").toString().equals(choose)){
							map.put("checked", true);
							break;
						}
						map.put("checked", false);
					}
				}else{
					map.put("checked", false);
				}
			}
			nodes.add(map);
		}
//		long end  = System.currentTimeMillis();
//		System.out.println("树查询时间："+(end-start));
		return nodes;
	}
	@SuppressWarnings("unchecked")
	protected Boolean riskHasChildren(String parentId,Boolean rbs){
		boolean hasChildren = false;
		
		Criteria criteria = o_riskDAO.createCriteria();
		if("root".equals(parentId)){
			criteria.add(Restrictions.isNull("parent.id"));
		}else{
			criteria.add(Restrictions.eq("parent.id", parentId));
		}
		String companyId = UserContext.getUser().getCompanyid();
		criteria.add(Restrictions.eq("company.id", companyId));
		criteria.add(Restrictions.eq("deleteStatus", "1"));
		if(rbs){
			criteria.add(Restrictions.eq("isRiskClass", "RBS"));
		}
		criteria.setProjection(Projections.rowCount());
		int count = Integer.parseInt(criteria.uniqueResult().toString());
		if(count>0){
			hasChildren = true;
		}
		
		return hasChildren;
	}
	@SuppressWarnings("unchecked")
	protected Set<String> queryObjectBySearchName(String query,boolean rbs){
		List<Risk> list = new ArrayList<Risk>();
		Set<String> idSet = new HashSet<String>();
		Criteria criteria = o_riskDAO.createCriteria();
		if(StringUtils.isNotBlank(query)){
			criteria.add(Restrictions.like("name",query,MatchMode.ANYWHERE));
		}
		String companyId = UserContext.getUser().getCompanyid();
		criteria.add(Restrictions.eq("company.id", companyId));
		criteria.add(Restrictions.eq("deleteStatus", "1"));
		if(rbs){
			criteria.add(Restrictions.eq("isRiskClass", "RBS"));
		}
		list = criteria.list();
		
		for (Risk entity : list) {
			if(null==entity.getIdSeq()){
				continue;
			}
			String[] idsTemp = entity.getIdSeq().split("\\.");
			idSet.addAll(Arrays.asList(idsTemp));
		}
		return idSet;
	}
	
	@Transactional
	public void enableRisk(String ids,String isUsed){
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			Risk risk = findRiskById(id,true);
			risk.setIsUse(isUsed);
			o_riskDAO.merge(risk);
		}
	}
	
	public Page<Risk> findRiskEventByStrateMapId(String id,Page<Risk> page,String sort, String dir,String query){
		DetachedCriteria dc = DetachedCriteria.forClass(Risk.class);
		dc = dc.createAlias("kpiRelaRisks", "kpiRelaRisks");
		dc = dc.createAlias("kpiRelaRisks.kpi", "kpi");
		dc = dc.createAlias("kpi.dmRelaKpis", "dmRelaKpis");
		dc.add(Restrictions.eq("dmRelaKpis.strategyMap.id", id));
 
        return o_riskDAO.findPage(dc, page, false);
	}
	
	public Page<StrategyAdjustHistory> findStrategyHistoryByStrateMapId(String id,Page<StrategyAdjustHistory> page,String sort, String dir,String query){
		DetachedCriteria dc = DetachedCriteria.forClass(StrategyAdjustHistory.class);
		dc.add(Restrictions.eq("strategyMap.id", id));
 
        return o_strategyAdjustHistoryDAO.findPage(dc, page, false);
	}
	
	public Page<Risk> findRiskEventByOrgId(String id,Page<Risk> page,String sort, String dir,String query){
		DetachedCriteria dc = DetachedCriteria.forClass(Risk.class);
		dc = dc.createAlias("riskOrgs", "riskOrgs");
		dc = dc.createAlias("riskOrgs.sysOrganization", "sysOrganization");
		dc.add(Restrictions.eq("isRiskClass", "re"));
		dc.add(Restrictions.eq("sysOrganization.id", id));
		if (StringUtils.isNotBlank(query)) {
			dc.add(Restrictions.like("name", query, MatchMode.ANYWHERE));
		}
 
        return o_riskDAO.findPage(dc, page, false);
	}
	
	public Page<OrgAdjustHistory> findOrgHistoryByOrgId(String id,Page<OrgAdjustHistory> page,String sort, String dir,String query){
		DetachedCriteria dc = DetachedCriteria.forClass(OrgAdjustHistory.class);
		dc.add(Restrictions.eq("organization.id", id));
 
        return o_orgAdjustHistoryDAO.findPage(dc, page, false);
	}
	
	public Page<Risk> findRiskEventByProcessId(String id,Page<Risk> page,String sort, String dir,String query){
		DetachedCriteria dc = DetachedCriteria.forClass(Risk.class);
		dc = dc.createAlias("ProcessRelaRisk", "ProcessRelaRisk");
		dc = dc.createAlias("ProcessRelaRisk.process", "process");
		dc.add(Restrictions.eq("process.id", id));
		if (StringUtils.isNotBlank(query)) {
			dc.add(Restrictions.like("name", query, MatchMode.ANYWHERE));
		}
 
        return o_riskDAO.findPage(dc, page, false);
	}
	
	public Page<ProcessAdjustHistory> findProcessHistoryByProcessId(String id,Page<ProcessAdjustHistory> page,String sort, String dir,String query){
		DetachedCriteria dc = DetachedCriteria.forClass(ProcessAdjustHistory.class);
		dc.add(Restrictions.eq("process.id", id));
 
        return o_processAdjustHistoryDAO.findPage(dc, page, false);
	}
	
	public Page<Risk> findRiskEventByKpiId(String id,Page<Risk> page,String sort, String dir,String query){
		DetachedCriteria dc = DetachedCriteria.forClass(Risk.class);
		dc = dc.createAlias("kpiRelaRisks", "kpiRelaRisks");
		dc = dc.createAlias("kpiRelaRisks.kpi", "kpi");
		dc.add(Restrictions.eq("kpi.id", id));
		if (StringUtils.isNotBlank(query)) {
			dc.add(Restrictions.like("name", query, MatchMode.ANYWHERE));
		}
 
        return o_riskDAO.findPage(dc, page, false);
	}
}

