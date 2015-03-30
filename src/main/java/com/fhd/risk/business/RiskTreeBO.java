/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */

package com.fhd.risk.business;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.fdc.utils.UserContext;
import com.fhd.risk.entity.Risk;
import com.fhd.risk.entity.RiskOrg;
import com.fhd.risk.interfaces.IRiskTreeBO;
import com.fhd.sys.business.orgstructure.OrganizationBO;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 风险树控件业务处理
 * 
 * @author 金鹏祥
 * @version
 * @since Ver 1.1
 * @Date 2012 2012-7-13 下午7:27:19
 * 
 * @see
 */
@Service
public class RiskTreeBO implements IRiskTreeBO {
	@Autowired
	private OrganizationBO o_organizationBO;
	
	@Autowired
	private RiskOrgBO o_riskOrgBO;
	
	@Autowired
	private RiskBO o_riskBO;

	public Map<String, Object> objectPackage(Object obj, Boolean canChecked, Boolean isLeaf, Boolean expanded) {
		Map<String, Object> item = new HashMap<String, Object>();

		if (obj instanceof SysOrganization) {
			SysOrganization sysOrganization = (SysOrganization) obj;
			item.put("id", sysOrganization.getId());
			item.put("dbid", sysOrganization.getId());
			item.put("text", sysOrganization.getOrgname());
			item.put("code", sysOrganization.getOrgcode());
			item.put("type", "orgRisk");
			if (isLeaf) {
				item.put("leaf", sysOrganization.getIsLeaf());
			}
		} else if (obj instanceof Risk) {
			Risk riskTree = (Risk) obj;
			String id = "";
			String dbId = "";
			String code = "";
			String name = "";
			
			if (riskTree.getIsRiskClass().equalsIgnoreCase("RBS")) {
				dbId = "RBS_" + riskTree.getId();
			} else if (riskTree.getIsRiskClass().equalsIgnoreCase("RE")) {
				dbId = "RE_" + riskTree.getId();
			} else {
				dbId = riskTree.getId();
			}
			id = riskTree.getId();

			code = riskTree.getCode();
			name = riskTree.getName();
			if (riskTree.getParent() != null)
				item.put("parentid", riskTree.getParent().getId());
			if (isLeaf) {
				item.put("leaf", riskTree.getIsLeaf());
			}
			item.put("id", id);
			item.put("dbid", dbId);
			item.put("code", code);
			item.put("text", name);
			item.put("type", "risk");
		}
		if (!isLeaf) {
			item.put("leaf", false);
		}
		if (canChecked) {
			item.put("checked", false);
		}
		if (expanded) {
			item.put("expanded", true);
		}
		return item;
	}
	
	public Set<String> findStrategyMapBySearchName(String searchName, String type, Boolean idseq, Boolean rbs) {
		List<RiskOrg> RiskOrgList = new ArrayList<RiskOrg>();
		List<Risk> riskList = new ArrayList<Risk>();
		Set<String> idSet = new HashSet<String>();

		String companyId = null;
		// String empId = null;
		String orgId = null;

		if (UserContext.getUser() != null) {
			
			companyId = UserContext.getUser().getCompanyid();
		}

		if (StringUtils.isNotBlank(type)) {
			// empId = UserContext.getUser().getUserid();
			orgId = UserContext.getUser().getMajorDeptId();
			RiskOrgList = o_riskOrgBO.findRiskOrgBySome(searchName, companyId, orgId, "1", rbs);

			if (idseq) {
				for (RiskOrg riskOrg : RiskOrgList) {
					Risk entity = riskOrg.getRisk();
					String[] idsTemp = entity.getIdSeq().split("\\.");
					idSet.addAll(Arrays.asList(idsTemp));
				}
			} else {
				for (RiskOrg riskOrg : RiskOrgList) {
					Risk entity = riskOrg.getRisk();
					idSet.add(entity.getId());
				}
			}
		} else {
			riskList = o_riskBO.finRiskBySome(searchName, companyId, "1");
			if (idseq) {
				for (Risk entity : riskList) {
					if(null==entity.getIdSeq()){
						continue;
					}
					String[] idsTemp = entity.getIdSeq().split("\\.");
					idSet.addAll(Arrays.asList(idsTemp));
				}
			} else {
				for (Risk entity : riskList) {
					idSet.add(entity.getId());
				}
			}
		}
		return idSet;
	}

	
	public Map<String, Object> wrapStrategyMapNode(Risk risk, Boolean canChecked, Boolean isLeaf, Boolean expanded) {
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("id", risk.getId());
		item.put("dbid", risk.getId());
		item.put("code", risk.getCode());
		item.put("text", risk.getName());
		item.put("type", "other");
		if (risk.getParent() != null)
			item.put("parentid", "risk_" + risk.getParent().getId());
		if (isLeaf) {
			item.put("leaf", risk.getIsLeaf());
		}
		if (!isLeaf) {
			item.put("leaf", false);
		}
		if (canChecked) {
			item.put("checked", false);
		}
		if (expanded) {
			item.put("expanded", true);
		}
		return item;
	}

	public SysOrganization findParentNodeById(SysOrganization org,String orgId) {
		SysOrganization sysOrg = null;
		SysOrganization parentOrg = org.getParentOrg();
		if (parentOrg != null) {
			if (parentOrg.getId().equals(orgId)) {
				sysOrg = org;
			} else {
				sysOrg = findParentNodeById(parentOrg, orgId);
			}
		}
		return sysOrg;
	}
	
	public List<Map<String, Object>> findRootByCompanyId() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> item = new HashMap<String, Object>(); // 存放个对象
		Map<String, Object> items = new HashMap<String, Object>(); // 存放
		
		String companyId = UserContext.getUser().getCompanyid();// 所在公司id
		Risk risk = o_riskBO.findRiskByCompanyId(companyId);//获取风险树、我的风险树根目录
		SysOrganization sysOrganization = o_organizationBO.getRootOrgByCompanyId(companyId);//获取机构树根目录

		item = new HashMap<String, Object>();
		item = this.objectPackage(risk, false, false, false);
		items.put("risk", item);//风险树

		item = new HashMap<String, Object>();
		item = this.objectPackage(sysOrganization, false, false, false);
		items.put("orgRisk", item);//机构风险树

		item = new HashMap<String, Object>();
		item = this.objectPackage(risk, false, false, false);
		items.put("myRisk", item);//我的风险树

		list.add(items);
		return list;
	}

	public List<Map<String, Object>> treeLoader(String id, Boolean canChecked, String query, String type, Boolean rbs) {
		long start  = System.currentTimeMillis();
		Set<String> idsSet = null; //保存根据查询条件查出来的所有的目标id
		Map<String, Object> item = null;//存放tree节点
		List<Risk> riskList = null; //保存根据父节点查出的目标
		//boolean expanded = StringUtils.isNotBlank(query) ? true : false;//是否展开节点
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();//呈现tree节点
		
		String companyId = UserContext.getUser().getCompanyid();//所在公司id
		
		if (StringUtils.isNotBlank(id)) {//IS-呈现节点ID不为空
			if (type == null) {//IS-风险树
				riskList = o_riskBO.findRiskBySome(companyId, id, "1", rbs);//当前节点下的叶子
				idsSet = this.findStrategyMapBySearchName(query, type, true, rbs);//所在公司ID下的风险及事件ID
				HashMap<String, Risk> riskMap = o_riskBO.findRiskMapByCompanyId(companyId);//所在公司ID下的风险及事件
				
				for (Risk entity : riskList) {//遍历当前节点下的叶子实体
					if (idsSet.size() > 0 && idsSet.contains(entity.getId())) {//is-所在公司ID下的风险及事件ID等于遍历实体风险及事件ID
						item = new HashMap<String, Object>();
						if (riskMap != null) {//IS-所在公司ID下的风险及事件是否存在
							if (riskMap.get(entity.getId()) != null) {//IS-如果当前节点下的叶子节点还有节点的情况
								if(rbs){
									if(riskMap.get(entity.getId()).getIsRiskClass().equalsIgnoreCase("RBS")){
										item = this.objectPackage(entity, canChecked, false, false);//存放节点并且已+扩展呈现展示
									}else{
										item = this.objectPackage(entity, canChecked, true, true);//存放节点并不已+扩展呈现展示
									}
								}else{
									item = this.objectPackage(entity, canChecked, false, false);//存放节点并且已+扩展呈现展示
								}
								
							} else {
								item = this.objectPackage(entity, canChecked, true, true);//存放节点并不已+扩展呈现展示
							}
						}
						nodes.add(item);//将接节点添加到呈现变量中
					}
				}
			} else {//is-我的风险树
				riskList = o_riskBO.findRiskBySome(companyId, null, "1", rbs);//当前节点下的叶子
				idsSet = this.findStrategyMapBySearchName(query, type, false, rbs);//部门与风险及事件关联的ID

				for (Risk entity : riskList) {//遍历实体节点
					if (id.equalsIgnoreCase(entity.getCompany().getId())) {//IS-所在公司ID等实体所在公司ID
						if (idsSet.size() > 0 && idsSet.contains(entity.getId())) {//IS-部门与风险及事件关联的ID等于实体ID
							item = new HashMap<String, Object>();
							item = this.objectPackage(entity, canChecked, true,true);//存放节点并不已+扩展呈现展示（不呈现树型结构）
							nodes.add(item);//将接节点添加到呈现变量中
						}
					}
				}
			}
		}
		long end  = System.currentTimeMillis();
		System.out.println("树查询时间："+(end-start));
		return nodes;//将呈现变量返给前端
	}
	
	public List<Map<String, Object>> orgTreeLoader(String id, String type, Boolean canChecked, String query, Boolean rbs) {
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();//返回前台的nodelist
		
		String companyId = UserContext.getUser().getCompanyid();//所在公司id
		HashMap<String, Risk> riskMap = o_riskBO.findRiskMapFromNotRiskOrgByCompanyId(companyId);//刨除部门关联风险及事件ID的MAP集合
		
		if ("orgRisk".equals(type)) {//IS-集团部门节点
			Map<String, Object> node = null;//node临时对象
			SysOrganization org = null;// 机构实体临时对象
			Set<String> orgIdSet = new HashSet<String>(); //保存目标的所属机构ID
			boolean expanded = StringUtils.isNotBlank(query) ? true : false; //是否展开节点
			List<SysOrganization> orgList = new ArrayList<SysOrganization>();//子节点机构集合
			List<RiskOrg> riskOrgList = o_riskOrgBO.findRiskOrgBySome(companyId, query, "1", rbs);//取出符合条件的目标
			String smID = "";
			Set<String> smIdSet = new HashSet<String>();
			
			for (RiskOrg riskOrg : riskOrgList) {//循环每一个目标
				node = new HashMap<String, Object>();
				org = riskOrg.getSysOrganization();//机构部门实体
				if (org != null) {//IS-实体不为空
					if (org.getId().equals(id)) {//IS-部门ID等于当前节点ID
						if (riskMap != null) {//IS-MAP不为空
							if (riskMap.get(riskOrg.getRisk().getId()) != null) {//当前风险存在叶子节点
								if(rbs){
									if(riskMap.get(riskOrg.getRisk().getId()).getIsRiskClass().equalsIgnoreCase("RBS")){
										node = this.objectPackage(riskOrg.getRisk(), canChecked, false, false);//存放节点并且已+扩展呈现展示
									}else{
										node = this.objectPackage(riskOrg.getRisk(), canChecked, true, true);//存放节点并且已+扩展呈现展示
									}
								}else{
									node = this.objectPackage(riskOrg.getRisk(), canChecked, false, false);//存放节点并且已+扩展呈现展示
								}
							} else {
								node = this.objectPackage(riskOrg.getRisk(), canChecked, true, true);//存放节点不已+扩展呈现展示
							}
						}
						smID = id + "_" + riskOrg.getRisk().getId();
						if (!smIdSet.contains(smID)) {
							smIdSet.add(smID);
							node.put("id", smID);
							nodes.add(node);
						}
					} else {
						org = this.findParentNodeById(org, id);
						if (org != null) {
							orgList.add(org);
						}
					}
				}
				// 将机构节点集合包装为node节点
				for (SysOrganization sysOrg : orgList) {
					if (!orgIdSet.contains(sysOrg.getId())) {
						orgIdSet.add(sysOrg.getId());
						node = new HashMap<String, Object>();
						node = this.objectPackage(sysOrg, false, false,
								expanded);
						node.put("leaf", false);
						nodes.add(node);
					}
				}
			}
			/* 加载其它目标节点 */
			SysOrganization sysorg = o_organizationBO.get(id);
			if (sysorg != null && sysorg.getParentOrg() == null) {// 说明是根组织单元
				Risk rootsm = (Risk) o_riskBO.findRiskParentIsNull();
				if (rootsm != null) {
					// 加入其它节点
					Map<String, Object> item = new HashMap<String, Object>();
					item.put("id", "risk_" + rootsm.getId());
					item.put("dbid", rootsm.getId());
					item.put("code", rootsm.getCode());
					item.put("text", "其它");
					if (StringUtils.isNotBlank(query)) {
						item.put("expanded", true);
					} else {
						item.put("expanded", false);
					}
					item.put("type", "other");
					item.put("leaf", "false");
					nodes.add(item);
				}
			}
		} else {
			if ("other".equals(type)) {
				if (StringUtils.isBlank(query)) {
					List<Risk> strategyList = o_riskBO.findNotInOrgStrategyMap(id.replace("risk_", ""), "1", rbs);
					for (Risk risk : strategyList) {
						Map<String, Object> item = new HashMap<String, Object>();
						String dbId = "";
						if (risk.getIsRiskClass().equalsIgnoreCase("RBS")) {
							dbId = "RBS_" + risk.getId();
						} else if (risk.getIsRiskClass().equalsIgnoreCase("RE")) {
							dbId = "RE" + risk.getId();
						} else {
							dbId = risk.getId();
						}
						item.put("id", risk.getId());
						item.put("dbid", dbId);
						item.put("code", risk.getCode());
						item.put("text", risk.getName());
						item.put("checked", false);
						item.put("type", "other");
						if (risk.getParent() != null)
							item.put("parentid", risk.getParent().getId());

						if (riskMap != null) {
							if (riskMap.get(risk.getId()) == null) {
								item.put("leaf", risk.getIsLeaf());
								item.put("expanded", true);
							}else{
								if(rbs){
									if(!riskMap.get(risk.getId()).getIsRiskClass().equalsIgnoreCase("RBS")){
										item.put("leaf", risk.getIsLeaf());
										item.put("expanded", true);
									}
								}
								
							}
						}
						nodes.add(item);
					}
				}
			}
		}

		if (StringUtils.isNotBlank(query)) {//IS-查询情况
			if (nodes.size() != 0) {
				for (Map<String, Object> map : nodes) {
					if (map.get("text").toString().equalsIgnoreCase("其它")) {//IS-其他节点
						return nodes;
					}
				}
			}

			if (id.indexOf("risk_") != -1) {
				id = id.replace("risk_", "");//过滤节点ID
			}

			Set<String> oidSet = new HashSet<String>();
			List<Risk> smList = o_riskBO.findRiskBySome(companyId, id, "1", rbs);//取当风险总节点
			Set<String> idsSet = o_riskBO.findRiskMapFromNotRiskOrgBySome(id, query);//没有和组织机构关联的Id集合
			for (Risk entity : smList) {//遍历实体
				if (idsSet.size() > 0 && idsSet.contains(entity.getId())) {
					Map<String, Object> item = this.wrapStrategyMapNode(entity, canChecked, true, true);//存放变量
					item.put("id", entity.getId());
					oidSet.add(entity.getId());
					nodes.add(item);//存放呈现节点
				}
			}
		}

		return nodes;
	}
}