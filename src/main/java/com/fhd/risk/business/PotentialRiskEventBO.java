package com.fhd.risk.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fhd.kpi.dao.StrategyMapDAO;
import com.fhd.kpi.entity.StrategyMap;
import com.fhd.process.dao.ProcessDAO;
import com.fhd.risk.dao.RiskDAO;
import com.fhd.risk.entity.Risk;
import com.fhd.risk.entity.RiskAdjustHistory;
import com.fhd.sys.dao.orgstructure.SysOrgDao;
import com.fhd.sys.entity.orgstructure.SysOrganization;
import com.fhd.process.entity.Process;

@Service
public class PotentialRiskEventBO {
	
	@Autowired
	private RiskDAO o_riskDAO;
	
	@Autowired
	private SysOrgDao o_sysOrgDAO;
	
	@Autowired
	private StrategyMapDAO o_StrategyMapDAO;
	
	@Autowired
	private ProcessDAO o_ProcessDAO;
	
	/**
	 * 根据风险事件构建风险树
	 * @author 郑军祥
	 * @param node			nodeId
	 * @param query		  	查询条件
	 * @param eventIds		不能为空
	 * @return
	 */
	public List<Map<String, Object>> getRiskTreeRecordByEventIds(String node, String query, String[] eventIds){
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
		
		Set<String> idSet = queryObjectBySearchName(query,eventIds);
		if(StringUtils.isNotBlank(query)&&idSet.size()==0){
			return nodes;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();

		Criteria criteria = o_riskDAO.createCriteria();
		if (StringUtils.isNotBlank(node)) {
			if(node.equalsIgnoreCase("root")){
				criteria.add(Restrictions.isNull("parent.id"));
			}else{
				criteria.add(Restrictions.eq("parent.id", node));
			}
			
		}
		//自动查询历史记录
		criteria.createAlias("adjustHistory", "adjustHistory", CriteriaSpecification.LEFT_JOIN, Restrictions.eq("adjustHistory.isLatest", "1"));
		//关联风险事件
		criteria.createAlias("children", "riskEvent",CriteriaSpecification.INNER_JOIN);
		criteria.add(Restrictions.in("riskEvent.id", eventIds));
		criteria.add(Restrictions.eq("riskEvent.isRiskClass", "re"));
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
			Set<RiskAdjustHistory> historys = risk.getAdjustHistory();
			if(historys!=null && historys.size()==1){
				Iterator<RiskAdjustHistory> iterator = historys.iterator();
				if(iterator.hasNext()){
					iconCls = iterator.next().getAssessementStatus();
				}
			}
			map.put("iconCls", iconCls);
			map.put("cls", "org");
			map.put("leaf",risk.getIsLeaf());
			nodes.add(map);
		}
		return nodes;
	}
	@SuppressWarnings("unchecked")
	protected Set<String> queryObjectBySearchName(String query,String[] eventIds){
		List<Risk> list = new ArrayList<Risk>();
		Set<String> idSet = new HashSet<String>();
		Criteria criteria = o_riskDAO.createCriteria();
		//关联风险事件
		criteria.createAlias("children", "riskEvent",CriteriaSpecification.INNER_JOIN);
		criteria.add(Restrictions.in("riskEvent.id", eventIds));
		if(StringUtils.isNotBlank(query)){
			criteria.add(Restrictions.like("riskEvent.name",query,MatchMode.ANYWHERE));
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
	
	/**
	 * 根据风险事件构建组织树
	 * @author 郑军祥
	 * @param node			nodeId
	 * @param query		  	查询条件
	 * @param eventIds		不能为空
	 * @return
	 */
	public List<Map<String, Object>> getOrgTreeRecordByEventIds(String node, String query, String[] eventIds){
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
		
		Set<String> idSet = queryOrgObjectBySearchName(query,eventIds);
		if(StringUtils.isNotBlank(query)&&idSet.size()==0){
			return nodes;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		Criteria criteria = o_sysOrgDAO.createCriteria();
		if (StringUtils.isNotBlank(node)) {
			if(node.equalsIgnoreCase("root")){
				criteria.add(Restrictions.isNull("parentOrg.id"));
			}else{
				criteria.add(Restrictions.eq("parentOrg.id", node));
			}
			
		}
		//关联风险事件
		criteria.createAlias("orgRisks", "orgRisks");
		criteria.createAlias("orgRisks.risk", "riskEvent");
		String[] orgType = new String[]{"0orgtype_d","0orgtype_sd"};
		criteria.add(Restrictions.in("orgType", orgType));
		criteria.add(Restrictions.in("riskEvent.id", eventIds));
		criteria.addOrder(Order.asc("sn"));
		
		List<SysOrganization> list = criteria.list();

		for (SysOrganization org : list) {
			if(!idSet.contains(org.getId())){
				continue;
			}
			map = new HashMap<String, Object>();
			map.put("id", org.getId());
			map.put("text", org.getOrgname());
			map.put("leaf",org.getIsLeaf());
			nodes.add(map);
		}
		return nodes;
	}
	@SuppressWarnings("unchecked")
	protected Set<String> queryOrgObjectBySearchName(String query,String[] eventIds){
		List<SysOrganization> list = new ArrayList<SysOrganization>();
		Set<String> idSet = new HashSet<String>();
		Criteria criteria = o_sysOrgDAO.createCriteria();
		//关联风险事件
		criteria.createAlias("orgRisks", "orgRisks");
		criteria.createAlias("orgRisks.risk", "riskEvent");
		String[] orgType = new String[]{"0orgtype_d","0orgtype_sd"};
		criteria.add(Restrictions.in("orgType", orgType));
		criteria.add(Restrictions.in("riskEvent.id", eventIds));
		if(StringUtils.isNotBlank(query)){
			criteria.add(Restrictions.like("riskEvent.name",query,MatchMode.ANYWHERE));
		}
		list = criteria.list();
		
		for (SysOrganization entity : list) {
			if(null==entity.getOrgseq()){
				continue;
			}
			String[] idsTemp = entity.getOrgseq().split("\\.");
			idSet.addAll(Arrays.asList(idsTemp));
		}
		return idSet;
	}
	
	/**
	 * 根据风险事件构建组织树
	 * @author 郑军祥
	 * @param node			nodeId
	 * @param query		  	查询条件
	 * @param eventIds		不能为空
	 * @return
	 */
	public List<Map<String, Object>> getStrategyMapTreeRecordByEventIds(String node, String query, String[] eventIds){
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
		
		Set<String> idSet = queryOrgObjectBySearchName(query,eventIds);
		if(StringUtils.isNotBlank(query)&&idSet.size()==0){
			return nodes;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		Criteria criteria = o_StrategyMapDAO.createCriteria();
		if (StringUtils.isNotBlank(node)) {
			if(node.equalsIgnoreCase("root")){
				criteria.add(Restrictions.isNull("parent.id"));
			}else{
				criteria.add(Restrictions.eq("parent.id", node));
			}
			
		}
		//关联风险事件
		criteria.createAlias("smRelaKpi", "smRelaKpi");
		criteria.createAlias("smRelaKpi.kpi", "kpi");
		criteria.createAlias("kpi.kpiRelaRisks", "kpiRelaRisks");
		criteria.createAlias("kpiRelaRisks.risk", "riskevent");
		criteria.add(Restrictions.in("riskEvent.id", eventIds));
		criteria.addOrder(Order.asc("sort"));
		
		List<StrategyMap> list = criteria.list();

		for (StrategyMap sm : list) {
			if(!idSet.contains(sm.getId())){
				continue;
			}
			map = new HashMap<String, Object>();
			map.put("id", sm.getId());
			map.put("text", sm.getName());
			map.put("leaf",sm.getIsLeaf());
			nodes.add(map);
		}
		return nodes;
	}
	@SuppressWarnings("unchecked")
	protected Set<String> queryStrategyMapObjectBySearchName(String query,String[] eventIds){
		List<StrategyMap> list = new ArrayList<StrategyMap>();
		Set<String> idSet = new HashSet<String>();
		Criteria criteria = o_StrategyMapDAO.createCriteria();
		//关联风险事件
		criteria.createAlias("smRelaKpi", "smRelaKpi");
		criteria.createAlias("smRelaKpi.kpi", "kpi");
		criteria.createAlias("kpi.kpiRelaRisks", "kpiRelaRisks");
		criteria.createAlias("kpiRelaRisks.risk", "riskevent");
		criteria.add(Restrictions.in("riskEvent.id", eventIds));
		if(StringUtils.isNotBlank(query)){
			criteria.add(Restrictions.like("riskEvent.name",query,MatchMode.ANYWHERE));
		}
		list = criteria.list();
		
		for (StrategyMap entity : list) {
			if(null==entity.getIdSeq()){
				continue;
			}
			String[] idsTemp = entity.getIdSeq().split("\\.");
			idSet.addAll(Arrays.asList(idsTemp));
		}
		return idSet;
	}
	
	/**
	 * 根据风险事件构建组织树
	 * @author 郑军祥
	 * @param node			nodeId
	 * @param query		  	查询条件
	 * @param eventIds		不能为空
	 * @return
	 */
	public List<Map<String, Object>> getProcessTreeRecordByEventIds(String node, String query, String[] eventIds){
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
		
		Set<String> idSet = queryOrgObjectBySearchName(query,eventIds);
		if(StringUtils.isNotBlank(query)&&idSet.size()==0){
			return nodes;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		Criteria criteria = o_ProcessDAO.createCriteria();
		if (StringUtils.isNotBlank(node)) {
			if(node.equalsIgnoreCase("root")){
				criteria.add(Restrictions.isNull("parent.id"));
			}else{
				criteria.add(Restrictions.eq("parent.id", node));
			}
			
		}
		//关联风险事件
		criteria.createAlias("processRelaRisks", "processRelaRisks");
		criteria.createAlias("processRelaRisks.risk", "riskevent");
		criteria.add(Restrictions.in("riskEvent.id", eventIds));
		criteria.addOrder(Order.asc("sort"));
		
		List<Process> list = criteria.list();

		for (Process ps : list) {
			if(!idSet.contains(ps.getId())){
				continue;
			}
			map = new HashMap<String, Object>();
			map.put("id", ps.getId());
			map.put("text", ps.getName());
			map.put("leaf",ps.getIsLeaf());
			nodes.add(map);
		}
		return nodes;
	}
	@SuppressWarnings("unchecked")
	protected Set<String> queryProcessObjectBySearchName(String query,String[] eventIds){
		List<Process> list = new ArrayList<Process>();
		Set<String> idSet = new HashSet<String>();
		Criteria criteria = o_ProcessDAO.createCriteria();
		//关联风险事件
		criteria.createAlias("processRelaRisks", "processRelaRisks");
		criteria.createAlias("processRelaRisks.risk", "riskevent");
		criteria.add(Restrictions.in("riskEvent.id", eventIds));
		if(StringUtils.isNotBlank(query)){
			criteria.add(Restrictions.like("riskEvent.name",query,MatchMode.ANYWHERE));
		}
		list = criteria.list();
		
		for (Process entity : list) {
			if(null==entity.getIdSeq()){
				continue;
			}
			String[] idsTemp = entity.getIdSeq().split("\\.");
			idSet.addAll(Arrays.asList(idsTemp));
		}
		return idSet;
	}
}
