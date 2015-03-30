/**
 * OrganizationBO.java
 * com.fhd.fdc.commons.business.sys
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-7-30 		胡迪新
 *
 * Copyright (c) 2010, FirstHuida All Rights Reserved.
 */

package com.fhd.sys.business.orgstructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.support.Page;
import com.fhd.core.dao.utils.PropertyFilter;
import com.fhd.core.utils.DigestUtils;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.commons.orm.sql.SqlBuilder;
import com.fhd.fdc.utils.UserContext;
import com.fhd.fdc.utils.excel.importexcel.ReadExcel;
import com.fhd.sys.business.file.FileUploadBO;
import com.fhd.sys.business.log.BusinessLogBO;
import com.fhd.sys.dao.auth.SysRoleDAOold;
import com.fhd.sys.dao.auth.SysUserDAO;
import com.fhd.sys.dao.orgstructure.SysOrganizationDAO;
import com.fhd.sys.dao.orgstructure.SysPositionDAO;
import com.fhd.sys.entity.auth.SysRole;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.duty.Duty;
import com.fhd.sys.entity.file.FileUploadEntity;
import com.fhd.sys.entity.group.SysGroup;
import com.fhd.sys.entity.orgstructure.SysEmpOrg;
import com.fhd.sys.entity.orgstructure.SysEmpPosi;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;
import com.fhd.sys.entity.orgstructure.SysPosition;

/**
 * 机构BO类.
 * 
 * @author wudefu
 * @version V1.0 创建时间：2010-9-19
 * @since Ver 1.1
 * @Date 2010-9-19 下午12:45:33 Company FirstHuiDa.
 * @see
 */
@Service 
@SuppressWarnings({"unchecked","rawtypes"})
public class OrganizationBO {

	@Autowired
	private SysOrganizationDAO o_sysOrganizationDAO;
	@Autowired
	private EmpolyeeBO o_empolyeeBO;
	@Autowired
	private PositionBO o_positionBO;
	@Autowired
	private BusinessLogBO o_businessLogBO;
	@Autowired
	private FileUploadBO o_fileUploadBO;
	@Autowired
	private SysUserDAO o_sysUserDAO;
	@Autowired
	private SysRoleDAOold o_sysRoleDAO;
	@Autowired
	private SysPositionDAO o_sysPositionDAO;
	
	/**
	 * 根据一个组织的ID得到它的IDSEQ(不用getSeq是为了避免导入数据或更新时idseq有问题).
	 * @author 吴德福
	 * @param orgId
	 * @return String id序列
	 * @since  fhd　Ver 1.1
	 */
	public String getOrgIdSeq(String orgId){
		SysOrganization organization = o_sysOrganizationDAO.get(orgId);
		StringBuilder sb = new StringBuilder();
		while(null!=organization.getParentOrg()){
			sb.append(organization.getParentOrg().getId()).append(".").append(orgId);
			organization=organization.getParentOrg();
		}
		return sb.toString();
	}
	/**
	 * 构造根结点的全部树结点：所有子机构、下级岗位、下级员工的全部结点.
	 * 
	 * @author 吴德福
	 * @param id
	 *            机构id.
	 * @param contextPath
	 *            发布应用路径.
	 * @return List<Map<String, Object>> 根结点的树结点集合.
	 * @since fhd　Ver 1.1
	 */
	public List<Map<String, Object>> loadOrgTree(String id, String contextPath) {
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
		if(StringUtils.isNotBlank(id)){
			SysOrganization org = o_sysOrganizationDAO.get(id);
			if(null != org){
				Set<SysOrganization> subOrgs = org.getSubOrg();
				for (SysOrganization subOrg : subOrgs) {
					if (!"admin".equals(UserContext.getUsername())) {
						// 0orgtype_c:数据字典条目指定机构类型：总公司id，不允许改变
						// 0orgtype_sc:数据字典条目指定机构类型：分公司id，不允许改变
						String type = subOrg.getOrgType();
						if ("0orgtype_c".equals(type) || "0orgtype_sc".equals(type)) {
							continue;
						}
					}
					Map<String, Object> node = new HashMap<String, Object>();
					node.put("id", subOrg.getId() + RandomUtils.nextInt(9999));
					node.put("dbid", subOrg.getId());
					node.put("name", subOrg.getId());
					node.put("text", subOrg.getOrgname());
					node.put("leaf", subOrg.getIsLeaf());
					node.put("href", contextPath + "/sys/orgstructure/org/tabs.do?id=" + subOrg.getId());
					node.put("hrefTarget", "mainframe");
					String orgicon = ("0orgtype_c".equals(subOrg.getOrgType()) || "0orgtype_sc".equals(subOrg.getOrgType()) )?"icon-org":"icon-orgsub";
					
					//String orgicon = "0orgtype_c".equals(subOrg.getOrgType())?"org-tree-icon":
					node.put("iconCls", orgicon);
					node.put("cls", "org");
					node.put("draggable", false);
					nodes.add(node);
				}
				Set<SysPosition> subPosis = org.getSysPositions();
				for (SysPosition subPosi : subPosis) {
					Map<String, Object> node = new HashMap<String, Object>();
					node.put("id", subPosi.getId() + RandomUtils.nextInt(9999));
					node.put("dbid", subPosi.getId());
					node.put("text", subPosi.getPosiname());
					node.put("leaf", false);
					node.put("href", contextPath + "/sys/orgstructure/posi/tabs.do?id=" + subPosi.getId());
					node.put("hrefTarget", "mainframe");
					node.put("iconCls", "icon-group");
					node.put("cls", "posi");
					node.put("draggable", false);
					nodes.add(node);
				}
				// Set<SysEmployee> subEmps = org.getSysEmployees();
				List<SysEmployee> subEmps = o_empolyeeBO.queryEmpsByOrgid(org.getId());
				for (SysEmployee subEmp : subEmps) {
					Map<String, Object> node = new HashMap<String, Object>();
					node.put("id", subEmp.getId() + RandomUtils.nextInt(9999));
					node.put("dbid", subEmp.getId());
					node.put("text", subEmp.getEmpname());
					node.put("leaf", true);
					node.put("href", contextPath + "/sys/orgstructure/emp/tabs.do?id=" + subEmp.getId());
					node.put("hrefTarget", "mainframe");
					// 402881b22b0f8594012b0f87bdb70004:数据字典条目指定性别：男，不允许改变
					node.put("iconCls", "402881b22b0f8594012b0f87bdb70004".equals(subEmp.getGender()) ? "icon-male" : "icon-female");
					node.put("cls", "emp");
					node.put("draggable", true);
					nodes.add(node);
				}
			}
		}
		return nodes;
	}
	/**
	 * 构造根结点的全部树结点：所有子机构、下级岗位.
	 * 
	 * @author wanye
	 * @param id
	 *            机构id.
	 * @param contextPath
	 *            发布应用路径.
	 * @return List<Map<String, Object>> 根结点的树结点集合.
	 * @since fhd　Ver 1.1
	 */
	public List<Map<String, Object>> loadOrgTree(Serializable id, String contextPath, String components, String single,String abName) {
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
		SysOrganization org = o_sysOrganizationDAO.get(id);
		if(null != org){
			Set<SysOrganization> subOrgs = org.getSubOrg();
			for (SysOrganization subOrg : subOrgs) {
				if (!"admin".equals(UserContext.getUsername())) {
					// 0orgtype_c:数据字典条目指定机构类型：总公司id，不允许改变
					// 0orgtype_sc:数据字典条目指定机构类型：分公司id，不允许改变
					if ("0orgtype_c".equals(subOrg.getOrgType()) || "0orgtype_sc".equals(subOrg.getOrgType())) {
						continue;
					}
				}
				Map<String, Object> node = new HashMap<String, Object>();
				node.put("id", subOrg.getId() + RandomUtils.nextInt(9999));
				node.put("name", subOrg.getId());
				node.put("text", subOrg.getOrgname());
				node.put("leaf", subOrg.getIsLeaf());
				node.put("href", contextPath + "/components/org/empsEx.do?type="+single+"&parentType=org&abName="+abName+"&id=" + subOrg.getId());
				node.put("hrefTarget", "mainframe");
				String orgicon = ("0orgtype_c".equals(subOrg.getOrgType()) || "0orgtype_sc".equals(subOrg.getOrgType()) )?"icon-org":"icon-orgsub";
				
				//String orgicon = "0orgtype_c".equals(subOrg.getOrgType())?"org-tree-icon":
				node.put("iconCls", orgicon);
				node.put("cls", "org");
				node.put("draggable", false);
				nodes.add(node);
			}
			Set<SysPosition> subPosis = org.getSysPositions();
			for (SysPosition subPosi : subPosis) {
				Map<String, Object> node = new HashMap<String, Object>();
				node.put("id", subPosi.getId() + RandomUtils.nextInt(9999));
				node.put("text", subPosi.getPosiname());
				node.put("leaf", false);
				node.put("href", contextPath + "/components/org/empsEx.do?type="+single+"&parentType=posi&abName="+abName+"&id=" + subPosi.getId());
				node.put("hrefTarget", "mainframe");
				node.put("iconCls", "icon-group");
				node.put("cls", "posi");
				node.put("draggable", false);
				nodes.add(node);
			}
		}
		return nodes;
	}

	/**
	 * 构造根结点的全部树结点：所有子机构、下级岗位、下级员工的全部结点.
	 * 
	 * @author David.Niu
	 * @param id
	 *            机构id.
	 * @param contextPath
	 *            发布应用路径.
	 * @param selects
	 * @param selects
	 * @param choose 
	 * @param checkNode 
	 * @return List<Map<String, Object>> 根结点的树结点集合.
	 * @since fhd　Ver 1.1
	 */
	public List<Map<String, Object>> loadPositionTree(Serializable id, String contextPath, String leaf, 
			String selects,String empfilter,String defaultOrg, Boolean checkNode, String choose) {
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
		SysOrganization org = o_sysOrganizationDAO.get(id);
		if (org == null)
			return nodes;
		
		Set<SysOrganization> subOrgs = org.getSubOrg();
		for (SysOrganization subOrg : subOrgs) {
			if (!"admin".equals(UserContext.getUsername())) {
				// 0orgtype_c:数据字典条目指定机构类型：总公司id，不允许改变
				// 0orgtype_sc:数据字典条目指定机构类型：分公司id，不允许改变
				if ("0orgtype_c".equals(subOrg.getOrgType()) || "0orgtype_sc".equals(subOrg.getOrgType())) {
					continue;
				}
			}
			//默认只生成当前登录人员所在部门的树
			if(StringUtils.isNotBlank(defaultOrg) && "true".equals(defaultOrg)){
				SysOrganization currentOrg = o_empolyeeBO.getDepartmentByEmpId(UserContext.getUser().getEmpid());
				if(null != currentOrg){
					if(subOrg.getOrgseq().indexOf(currentOrg.getId()) == -1){
						continue;
					}
				}
			}
			
			Map<String, Object> node = new HashMap<String, Object>();
			node.put("id", subOrg.getId());
			node.put("name", subOrg.getId());
			node.put("text", subOrg.getOrgname());
			if ("org".equalsIgnoreCase(leaf) && subOrg.getSubOrg().size() == 0)
				node.put("leaf", true);
			else
				node.put("leaf", false);
			node.put("hrefTarget", "mainframe");
			
			if (checkNode!=null && checkNode) {
				if(StringUtils.isNotBlank(choose)) {
					String[] split = StringUtils.split(choose,",");
					for (String s : split) {
						if (subOrg.getId().equals(s)) {
							node.put("checked", "true");
							break;
						}
					}
				}
			}
			String orgicon = ("0orgtype_c".equals(subOrg.getOrgType()) || "0orgtype_sc".equals(subOrg.getOrgType()) )?"icon-org":"icon-orgsub";
			node.put("iconCls", orgicon);
			node.put("cls", "org");
			node.put("startDate", subOrg.getStartDate());
			node.put("endDate", subOrg.getEndDate());
			node.put("status", subOrg.getOrgStatus());
			node.put("draggable", false);
			nodes.add(node);
		}
		if (!"org".equalsIgnoreCase(leaf)) {
			Set<SysPosition> subPosis = org.getSysPositions();
			for (SysPosition subPosi : subPosis) {
				Map<String, Object> node = new HashMap<String, Object>();
				node.put("id", subPosi.getId());
				node.put("text", subPosi.getPosiname());
				if ("position".equalsIgnoreCase(leaf))
					node.put("leaf", true);
				else
					node.put("leaf", false);
				node.put("hrefTarget", "mainframe");
				
				if (checkNode) {
					if(StringUtils.isNotBlank(choose)) {
						String[] split = StringUtils.split(choose,",");
						for (String s : split) {
							if (subPosi.getId().equals(s)) {
								node.put("checked", "true");
								break;
							}
						}
					}
				}
				node.put("iconCls", "icon-group");
				node.put("cls", "posi");
				node.put("startDate", subPosi.getStartDate());
				node.put("endDate", subPosi.getEndDate());
				node.put("status", subPosi.getPosiStatus());
				node.put("draggable", false);
				nodes.add(node);
			}
			if (!"position".equalsIgnoreCase(leaf)) {
				List<SysEmployee> subEmps = o_empolyeeBO.queryEmpsByOrgid(org.getId());
				
				
				for (SysEmployee subEmp : subEmps) {
					//已存在过滤
					if (("," + selects + ",").indexOf("," + subEmp.getId() + ",") > -1)// 过滤已经选择的人员
						continue;
					//条件过滤
					if(StringUtils.isNotBlank(empfilter)){
						String fhead = empfilter.split("/")[0];
						String fitem = empfilter.split("/")[1];
						if(fhead.equalsIgnoreCase("role")){
							//SysRole role = this.o_sysRoleDAO.getSysRoleByCode(fitem);
							boolean flagisexsit=false;
							//没有用户信息存在就不装载
							if(StringUtils.isBlank(subEmp.getUserid())){
								continue;
							}
							Set<SysRole> roles = o_sysUserDAO.get(subEmp.getUserid()).getSysRoles();
							for(SysRole role:roles){
								if(role.getRoleCode().equalsIgnoreCase(fitem)){
									flagisexsit=true;
									break;
								}
							}
							//没有这样的角色就过滤掉
							if(!flagisexsit){
								continue;
							}
						}
						
					}
					
					Map<String, Object> node = new HashMap<String, Object>();
					node.put("id", subEmp.getId());
					node.put("text", subEmp.getEmpname());
					node.put("leaf", true);
//					在选择控件中不需要弹出tab -- 胡迪新
//					node.put("href", contextPath + "/sys/orgstructure/emp/tabs.do?id=" + subEmp.getId());
					node.put("hrefTarget", "mainframe");
					if (checkNode) {
						if(StringUtils.isNotBlank(choose)) {
							String[] split = StringUtils.split(choose,",");
							for (String s : split) {
								if (subEmp.getId().equals(s)) {
									node.put("checked", "true");
									break;
								}
							}
						}
					}
					// 402881b22b0f8594012b0f87bdb70004:数据字典条目指定性别：男，不允许改变
					node.put("iconCls", "402881b22b0f8594012b0f87bdb70004".equals(subEmp.getGender()) ? "icon-male" : "icon-female");
					node.put("cls", "emp");
					//部门id和名称
					SysOrganization department = o_empolyeeBO.getDepartmentByEmpId(subEmp.getId());
					if(null != department){
						node.put("orgId", department.getId());
						node.put("orgName", department.getOrgname());
					}else{
						node.put("orgId", "");
						node.put("orgName", "");
					}
					//岗位id和名称
					SysPosition sysPosition = o_empolyeeBO.getPositionByEmpId(subEmp.getId());
					if(null != sysPosition){
						node.put("posiId", sysPosition.getId());
						node.put("posiName", sysPosition.getPosiname());
					}else{
						node.put("posiId", "");
						node.put("posiName", "");
					}
					node.put("draggable", true);
					nodes.add(node);
				}
			}
		}

		return nodes;
	}

	/**
	 * 取得根结点.
	 * @author 吴德福
	 * @return SysOrganization
	 * @since fhd　Ver 1.1
	 */
	public SysOrganization getRootOrg() {
		SysOrganization org = null;
		if(null != UserContext.getUser()){
			SysEmployee emp = o_empolyeeBO.getEmployee(UserContext.getUserid());
			if (null != emp) {
				org = getOrgByOrgtype(emp.getSysOrganization());
			} else {
				DetachedCriteria dc = DetachedCriteria.forClass(SysOrganization.class);
				dc.add(Restrictions.isNull("parentOrg"));
				List<SysOrganization> sysOrganizationList = o_sysOrganizationDAO.findByCriteria(dc);
				if(null != sysOrganizationList && sysOrganizationList.size()>0){
					org = sysOrganizationList.get(0);
				}
			}
		}
		return org;
	}
	
	/**
	 * 
	 * <pre>
	 * getRootOrgByCompanyId:取得指定公司的根节点
	 * </pre>
	 * 
	 * @author David
	 * @param companyId
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@SuppressWarnings("deprecation")
	public SysOrganization getRootOrgByCompanyId(String companyId){
		if(StringUtils.isBlank(companyId))
			return null;
		return o_sysOrganizationDAO.get(companyId);
	}

	/**
	 * 查询机构类型为总公司或者分公司的最近机构.
	 * 
	 * @author 吴德福
	 * @param org
	 *            机构
	 * @return SysOrganization 机构.
	 * @since fhd　Ver 1.1
	 */
	public SysOrganization getOrgByOrgtype(SysOrganization org) {
		// 0orgtype_c:数据字典条目指定机构类型：总公司id，不允许改变
		// 0orgtype_sc:数据字典条目指定机构类型：分公司id，不允许改变
		String  type = org.getOrgType();
		if ("0orgtype_c".equals(type) || "0orgtype_sc".equals(type)) {
			return org;
		} else {
			return getOrgByOrgtype(org.getParentOrg());
		}
	}

	/**
	 * 根据机构id查询机构信息.
	 * 
	 * @author 胡迪新
	 * @param id
	 *            机构id.
	 * @return SysOrganization 机构对象
	 * @since fhd　Ver 1.1
	 */
	public SysOrganization get(Serializable id) {
		return o_sysOrganizationDAO.get(id);
	}

	/**
	 * 更新机构信息.
	 * 
	 * @author 胡迪新
	 * @param organization
	 * @return Object 机构对象.
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public Object merge(SysOrganization organization) {
		try {
			o_businessLogBO.modBusinessLogInterface("修改", "机构", "成功", organization.getId());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.modBusinessLogInterface("修改", "机构", "失败", organization.getId());
		}
		return o_sysOrganizationDAO.merge(organization);
	}

	/**
	 * 查询选择机构的所有的下级机构.
	 * 
	 * @author 胡迪新
	 * @param id
	 *            选择的机构id.
	 * @return List<SysOrganization> 机构集合.
	 * @since fhd　Ver 1.1
	 */
	public List<SysOrganization> query(String id, List<PropertyFilter> filters) {
		if (null != id && !"".equals(id)) {
			return o_sysOrganizationDAO.find(filters, "sn", true, Restrictions.like("orgseq", "." + id + ".", MatchMode.ANYWHERE), Restrictions.not(Restrictions.eq("id", id)));
		} else {
			return o_sysOrganizationDAO.find(filters, "sn", true);
		}
	}
	/**
	 * 新增机构：更新父机构的叶子属性，同时设置机构序列、级别、叶子字段.
	 * @author 吴德福
	 * @param org 机构.
	 * @return Object 机构.
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public Object save(SysOrganization org) {
		try {
			SysOrganization parentOrg = get(org.getParentOrg().getId());
			parentOrg.setIsLeaf(false);
			merge(parentOrg);
			
			org.setIsLeaf(true);//设置自身节点为叶子节点
			org.setParentOrg(parentOrg);
			org.setOrgLevel(parentOrg.getOrgLevel() + 1);
			org.setOrgseq(org.getParentOrg().getOrgseq() + org.getId() + ".");
			merge(org);
			
			//新增一个总公司或者分公司的机构时，要默认初始化5个树根
			if("0orgtype_c".equals(org.getOrgType()) || "0orgtype_sc".equals(org.getOrgType())){
				//新建该公司风险树根  TODO
				
				
				/*RmRisk risk = new RmRisk();
				risk.setId(Identities.uuid2());
				risk.setRiskName("整体风险");
				risk.setRiskSeq("."+risk.getId()+".");
				risk.setParentRisk(null);
				risk.setDealStatus("1");
				risk.setStatus("3");
				risk.setIsLeaf(false);
				risk.setIsRiskClass("4");
				risk.seteLevel(1);
				risk.setSort(1);
				risk.setArchiveStatus("1");
				risk.setRiskWeight(1.0);
				Template template = new Template();
				template.setId("1");
				risk.setTemplate(template);
				risk.setOrg(org);
				o_riskDAO.merge(risk);
				
				//新建该公司指标树根
				Target target = new Target();
				target.setId(Identities.uuid2());
				target.setIsCatalog("1");
				target.setParentTarget(null);
				target.setIsLeaf(false);
				target.setParentIdSeq("."+target.getId()+".");
				target.setSort(1);
				target.setStatus("1");
				target.setTargetLevel(1);
				target.setKpiName("指标库");
				target.setCompany(org);
				o_targetDAO.merge(target);
				
				//新建该公司变量树根
				VariableKpi variable = new VariableKpi();
				variable.setId(Identities.uuid2());
				variable.setParentVariable(null);
				variable.setSort(1);
				variable.setIsCatalog("1");
				variable.setVariableName("变量库");
				variable.setStatus("1");
				variable.setParentIdSeq("."+variable.getId()+".");
				variable.seteLevel(1);
				variable.setIsDisabled("1");
				variable.setCompany(org);
				o_variableDAO.merge(variable);
				
				//新建该公司流程树根
				Processure processure = new Processure();
				processure.setId(Identities.uuid2());
				processure.setParentIdSeq("."+processure.getId()+".");
				processure.setDealStatus("1");
				processure.setIsLeaf(false);
				processure.setStatus("3");
				processure.setSort(1);
				processure.setProcessureLevel(1);
				processure.setProcessureName("流程库");
				processure.setParentProcessure(null);
				processure.setCompany(org);
				o_processureDAO.merge(processure);
				
				//新建该公司资产权限
				Assets assets = new Assets();
				assets.setId(Identities.uuid2());
				assets.setParentAssets(null);
				assets.setParentIdSeq("."+assets.getId()+".");
				assets.setSort(1);
				assets.setStatus("3");
				assets.setAssetsLevel(1);
				assets.setIsLeaf(false);
				assets.setDealStatus("1");
				assets.setAssetsName("资产库");
				assets.setParentAssets(null);
				assets.setCompany(org);
				o_assetsDAO.merge(assets);
				
				//获取系统默认图谱
				List<ChartColor> colors = o_chartColorBO.sys_getChartColors(); 
				List<ChartMatrix> matrixes = o_chartColorBO.sys_getChartMatrixs();
				
				//初始化该公司图谱
				//1.保存颜色
				for(ChartColor cc:colors){
					ChartColor color = new ChartColor();
					BeanUtils.copyProperties(cc, color);
					
					color.setId(Identities.uuid2());
					color.setType(null);
					color.setCompany(org);
					o_chartColorBO.saveChartColor(color);
				}
				
				//2. 保存图谱矩阵
				for(ChartMatrix cm:matrixes){
					ChartMatrix matrix = new ChartMatrix();
					BeanUtils.copyProperties(cm, matrix);
					
					matrix.setId(Identities.uuid2());
					matrix.setType(null);
					matrix.setCompany(org);
					o_chartColorBO.saveChartMatrix(matrix);
				}*/
				
			}
			o_businessLogBO.saveBusinessLogInterface("新增", "机构", "成功", org.getOrgcode(), org.getOrgname(), org.getOrgType(), String.valueOf(org.getSn()), org.getOrgStatus());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.saveBusinessLogInterface("新增", "机构", "失败", org.getOrgcode(), org.getOrgname(), org.getOrgType(), String.valueOf(org.getSn()), org.getOrgStatus());
		}
		return merge(org);
	}

	/**
	 * 根据机构id删除机构信息.
	 * @author 吴德福
	 * @param id
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void delete(String id) {
		try {
			o_sysOrganizationDAO.removeById(id);
			o_businessLogBO.delBusinessLogInterface("删除", "机构", "成功", id);
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.delBusinessLogInterface("删除", "机构", "失败", id);
		}
	}

	/**
	 * 根据机构id删除该机构的所有岗位.
	 * @author 吴德福
	 * @param id
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void deletePosi(String id) {
		List<SysPosition> posis = o_positionBO.queryPosiByOrgid(id);
		if (null != posis && posis.size() > 0) {
			for (SysPosition posi : posis) {
				o_positionBO.delete(posi.getId());
			}
		}
	}

	/**
	 * 根据机构id删除机构信息.
	 * 
	 * @author 吴德福
	 * @param id
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void deleteOrg(String id) {
		List<SysOrganization> orgs = queryOrgsByOrgid(id);
		if (null != orgs && orgs.size() > 0) {
			for (SysOrganization org : orgs) {
				// 删除机构下的所有的岗位
				deletePosi(id);
				// 删除机构
				delete(org.getId());
			}
		}
	}

	/**
	 * 根据机构id查询机构和所有的子机构.
	 * 
	 * @author 吴德福
	 * @param id
	 * @return List<SysOrganization> 子机构集合.
	 * @since fhd　Ver 1.1
	 */
	public List<SysOrganization> queryOrgsByOrgid(String id) {
		StringBuilder hql = new StringBuilder();
		hql.append("From SysOrganization where orgseq like '%." + id + ".%' order by orgseq");
		return o_sysOrganizationDAO.find(hql.toString());
	}

	/**
	 * 
	 * <pre>
	 * getRootOrgs:获取所有根节点(多个根节点)
	 * </pre>
	 * 
	 * @author David
	 * @return
	 * @since fhd　Ver 1.1
	 */
	public List<SysOrganization> getRootOrgs() {
		List<SysOrganization> orgs = new ArrayList<SysOrganization>();
		SysEmployee emp = o_empolyeeBO.getEmployee(UserContext.getUserid());
		if (null != emp) {
			SysOrganization org = getOrgByOrgtype(emp.getSysOrganization());
			orgs.add(org);
		} else {
			DetachedCriteria dc = DetachedCriteria.forClass(SysOrganization.class);
			dc.add(Restrictions.isNull("parentOrg"));
			orgs = o_sysOrganizationDAO.findByCriteria(dc);
		}
		return orgs;
	}

	/**
	 * <pre>
	 * getChildrenByParentId:根据父节点获取其子节点
	 * </pre>
	 * 
	 * @author David
	 * @param parentId
	 * @return
	 * @since fhd　Ver 1.1
	 */
	public List<SysOrganization> getChildrenByParentId(String parentId) {
		StringBuilder hql = new StringBuilder("From SysOrganization so where so.parentOrg.id ='").append(parentId).append("'");
		return o_sysOrganizationDAO.find(hql.toString());
	}

	/**
	 * <pre>
	 * getAllDepartmentsByCompanyId:根据公司ID获取所有部门
	 * </pre>
	 * 
	 * @author David
	 * @param companyId
	 *            公司ID
	 * @return
	 * @since fhd　Ver 1.1
	 */
	public List<SysOrganization> getAllDepartmentsByCompanyId(String companyId) {
		SysOrganization company = o_sysOrganizationDAO.get(companyId);
		String companyType = company.getOrgType();
		if ("0orgtype_c".equals(companyType)) {// 总公司
			return o_sysOrganizationDAO.find("from SysOrganization so where so.orgseq like '%." + companyId + ".%' and so.orgType='0orgtype_d' order by orgseq");
		} else if ("0orgtype_sc".equals(companyType)) {// 分公司
			return o_sysOrganizationDAO.find("from SysOrganization so where so.orgseq like '%." + companyId + ".%' and so.orgType='0orgtype_sd' order by orgseq");
		}

		return null;
	}
	
	/**
	 * 查看所有公司下的所有部门.
	 * @return List<SysOrganization>
	 */
	public List<SysOrganization> getAllDepartments() {
		DetachedCriteria dc = DetachedCriteria.forClass(SysOrganization.class);
		String[] orgTypeArray = new String[]{"0orgtype_sd","0orgtype_d"};
		dc.add(Restrictions.in("orgType", orgTypeArray));
		dc.add(Restrictions.eq("orgLevel", 2));
		return o_sysOrganizationDAO.findByCriteria(dc);
	}
	
	/**
	 * 根据公司查询该公司的直接下级部门.
	 * @param org
	 * @return List<SysOrganization>
	 */
	public List<SysOrganization> queryDepartmentByCompany(SysOrganization org){
		DetachedCriteria dc = DetachedCriteria.forClass(SysOrganization.class);
		dc.add(Restrictions.eq("parentOrg.id", org.getId()));
		dc.add(Restrictions.eq("orgLevel", org.getOrgLevel()+1));
		dc.add(Restrictions.in("orgType", new String[]{"0orgtype_sd","0orgtype_d"}));
		return o_sysOrganizationDAO.findByCriteria(dc);
	}
	
	/**
	 * 查询指定公司下的指定名称的部门
	 * @author 陈燕杰
	 * @param depName:部门的名称
	 * @return SysOrganization
	 * @since  fhd　Ver 1.1
	 */
	public SysOrganization getSysOrgByName(String depName){
		DetachedCriteria dc=DetachedCriteria.forClass(SysOrganization.class);
		dc.add(Restrictions.eq("orgname", depName));
		List<SysOrganization> result=o_sysOrganizationDAO.findByCriteria(dc);
		if(result.size()>0){
			return result.get(0);
		}
		return null;
	}
	/**
	 * <pre>
	 * getOrgsByOrgName:根据部门名称和公司ID获取实体
	 * </pre>
	 * 
	 * @author David
	 * @param orgName
	 * @param companyId
	 * @return
	 * @since fhd　Ver 1.1
	 */
	public List<SysOrganization> getOrgsByOrgName(String orgName, String companyId) {
		// "from SysOrganization a where not exists(select 1 from SysOrganization b where b.orgseq like '%." + companyId + ".%'  and b.orgType in ('0orgtype_sc','0orgtype_c') and a.orgseq like b.orgseq+'%' and b.id<>'" + companyId + "') and orgseq like  '%." + companyId + ".%' and a.orgname='" + orgName + "' order by orgseq";
		Map<String, String> model = new HashMap<String, String>();
		model.put("companyId", companyId);
		model.put("orgName", orgName);
		String sql = SqlBuilder.getSql("select_OrganizationBO_getOrgsByOrgName", model);
		return o_sysOrganizationDAO.find(sql);
	}
	


	/**
	 * 解析excel文件转成对象
	 * @param fileId 文件ID
	 * @param importType
	 * @return
	 * @throws Exception
	 * @return List[] 0 good 1 bad
	 */
	public List[] getImportData(String fileId, String importType) throws Exception {
		if (StringUtils.isBlank(fileId))
			return null;
		List[] datas = null;
		FileUploadEntity excel = o_fileUploadBO.queryFileById(fileId);
		
//		InputStream excelInputStream = new FileInputStream(excel.getFileAddress());
//		Workbook workbook = Workbook.getWorkbook(excelInputStream);
		//如果上传文件中只有一个就以它为准，如果是多个就按顺序号来读取
//		int sheetsum = workbook.getNumberOfSheets();
		
		if (excel != null) {
			if ("1".equals(importType)) {
				ReadExcel<SysOrganization> readRiskExcel = new ReadExcel<SysOrganization>(SysOrganization.class);
				//datas = checkOrg(readRiskExcel.readExcel(excel.getFileAddress(),sheetsum>1?0:0));
				datas = checkOrg(readRiskExcel.readExcel(excel.getFileAddress(),"机构"));
			}
			if ("2".equals(importType)) {
				ReadExcel<SysPosition> readRiskExcel = new ReadExcel<SysPosition>(SysPosition.class);
				//datas = checkSysPosition(readRiskExcel.readExcel(excel.getFileAddress(),sheetsum>1?1:0));
				datas = checkSysPosition(readRiskExcel.readExcel(excel.getFileAddress(),"岗位"));
			}
			if ("3".equals(importType)) {
				ReadExcel<Duty> readRiskExcel = new ReadExcel<Duty>(Duty.class);
				//datas = checkDuty(readRiskExcel.readExcel(excel.getFileAddress(),sheetsum>1?2:0));
				datas = checkDuty(readRiskExcel.readExcel(excel.getFileAddress(),"职务"));
				
			}
			if ("4".equals(importType)) {
				ReadExcel<SysEmployee> readRiskExcel = new ReadExcel<SysEmployee>(SysEmployee.class);
				//datas = checkSysEmployee(readRiskExcel.readExcel(excel.getFileAddress(),sheetsum>1?3:0));
				datas = checkSysEmployee(readRiskExcel.readExcel(excel.getFileAddress(),"员工"));
			}
			if ("5".equals(importType)) {
				ReadExcel<SysGroup> readRiskExcel = new ReadExcel<SysGroup>(SysGroup.class);
				//datas = checkSysSysGroup(readRiskExcel.readExcel(excel.getFileAddress(),sheetsum>1?4:0));
				datas = checkSysSysGroup(readRiskExcel.readExcel(excel.getFileAddress(),"工作组"));
			}
		}
		return datas;
	}

	/**
	 * 检查工作组
	 * @author none
	 * @param groups
	 * @return
	 */
	private List[] checkSysSysGroup(List<SysGroup> groups) {
		List[] returnList = new List[2];
		List<SysGroup> bads = new ArrayList<SysGroup>();

		// 验证数据完成性
		for (int i = groups.size() - 1; i >= 0; i--) {
			SysGroup group = groups.get(i);
			if (StringUtils.isBlank(group.getGroupCode()) || StringUtils.isBlank(group.getGroupName()) || StringUtils.isBlank(group.getGroupDesc())) {
				group.setImpErrorInfo("必填项不完整");
				bads.add(groups.remove(i));
			}
		}
		// 验证编号及名称重复
		for (int i = groups.size() - 1; i >= 0; i--) {
			SysGroup group = groups.get(i);
			if (group.getGroupDesc().equals(group.getGroupName())) {
				group.setImpErrorInfo("不能以自己为上级工作组");
				bads.add(groups.remove(i));
			}
		}
		// 本地验证
		for (int i = groups.size() - 1; i >= 0; i--) {
			SysGroup group = groups.get(i);
			for (int j = groups.size() - 1; j >= 0; j--) {
				SysGroup t = groups.get(j);
				if (t != group && (group.getGroupCode().equals(t.getGroupCode()) || group.getGroupName().equals(t.getGroupName()))) {
					group.setImpErrorInfo("工作组重复");
					bads.add(groups.remove(i));
					break;
				}

			}
		}
		// 数据库验证
		for (int i = groups.size() - 1; i >= 0; i--) {
			SysGroup group = groups.get(i);
			if (this.o_sysOrganizationDAO.createQuery("from SysGroup where groupCode=? or groupName=?", group.getGroupCode(), group.getGroupName()).list().size() > 0) {
				group.setImpErrorInfo("工作组重复");
				bads.add(groups.remove(i));
			}
		}
		// 验证上级工作组是否存在
		for (int i = groups.size() - 1; i >= 0; i--) {
			SysGroup group = groups.get(i);
			boolean isFind = false;
			for (int j = groups.size() - 1; j >= 0; j--) {// 本地验证
				SysGroup t = groups.get(j);
				if (group.getGroupDesc().equals(t.getGroupName())) {
					group.setParentGroup(t);
					isFind = true;
					break;
				}
			}
			if (!isFind) {// 验证数据库
				List<SysGroup> list = this.o_sysOrganizationDAO.createQuery("from SysGroup where groupName=?", group.getGroupDesc()).list();
				if (list.size() == 0) {
					group.setImpErrorInfo("无法找到上级工作组");
					bads.add(groups.remove(i));
				} else
					group.setParentGroup(list.get(0));
			}
		}
		returnList[0] = groups;
		returnList[1] = bads;
		return returnList;
	}

	/**
	 * 验证员工信息
	 * 
	 * @param readExcel
	 * @return
	 * @return List[]
	 */
	private List[] checkSysEmployee(List<SysEmployee> emps) {
		
		List[] returnList = new List[2];
		List<SysEmployee> bads = new ArrayList<SysEmployee>();
		List<SysEmployee> temps = new ArrayList<SysEmployee>();
		// 验证必填项
		for (int i = emps.size() - 1; i >= 0; i--) {
			SysEmployee emp = emps.get(i);
			if (StringUtils.isBlank(emp.getEmpcode()) || StringUtils.isBlank(emp.getEmpname()) || StringUtils.isBlank(emp.getUsername()) || StringUtils.isBlank(emp.getUserid()) || StringUtils.isBlank(emp.getRealname()) || StringUtils.isBlank(emp.getEmpStatus())
			// ||StringUtils.isBlank(emp.getCardtype())
					|| StringUtils.isBlank(emp.getCardno())) {
				
				temps.add(emp);
				bads.add(emps.remove(i));
				String info="";
				if(StringUtils.isBlank(emp.getEmpcode()))
					info+="员工编号为空,";
				if(StringUtils.isBlank(emp.getEmpname()))
					info+="员工名称为空,";
				if(StringUtils.isBlank(emp.getUsername()))
					info+="登录名为空,";
				if(StringUtils.isBlank(emp.getUserid()))
					info+="所属职务为空,";
				if(StringUtils.isBlank(emp.getRealname()))
					info+="所属机构为空,";
				if(StringUtils.isBlank(emp.getEmpStatus()))
					info+="所属部门（如果是多个部门，以“，”分隔，默认第一个部门为主部门）为空,";
				if(StringUtils.isBlank(emp.getCardno()))
						info+="所属角色（如果是多个角色，以“，”分隔）为空,";
				emp.setImpErrorInfo(info+"信息填写不完整");
			}
		}
		// 验证登录名
		for (int i = emps.size() - 1; i >= 0; i--) {
			SysEmployee emp = emps.get(i);
			
			//登录名
			String userNameSql = "from SysUser where username='" + emp.getUsername() + "' ";
			if (this.o_sysOrganizationDAO.createQuery(userNameSql).list().size() > 0) {
				bads.add(emps.remove(i));
				emp.setImpErrorInfo("登录名重复");
			}
		}
		
		// 验证员工编号
		for (int i = emps.size() - 1; i >= 0; i--) {
			SysEmployee emp = emps.get(i);
			
			//员工编号
			String empCodeSql = "from  SysEmployee where empcode='" + emp.getEmpcode() + "' ";
			if (this.o_sysOrganizationDAO.createQuery(empCodeSql).list().size() > 0) {
				bads.add(emps.remove(i));
				emp.setImpErrorInfo("员工编号重复");
			}
		}
		
		//缓存查到的机构 减少数据库的查询
		Map<String, SysOrganization> orgCache = new HashMap<String, SysOrganization>();
		// 验证所属机构 机构名不重
		for (int i = emps.size() - 1; i >= 0; i--) {
			SysEmployee emp = emps.get(i);
			boolean find = false;
			if (orgCache.get(emp.getRealname()) != null) {
				SysOrganization org = orgCache.get(emp.getRealname());
				if (org.getOrgType().equals("0orgtype_c") || org.getOrgType().equals("0orgtype_sc"))
					find = true;
			}else{
				//select org from SysOrganization org where orgname='"+emp.getEmpStatus()+"' and org.parentOrg.orgname='"+ emp.getRealname() +"' and (org.parentOrg.orgType='0orgtype_c' or org.parentOrg.orgType='0orgtype_sc')"
				String hql = "from  SysOrganization where orgname='" + emp.getRealname() + "' and (orgType='0orgtype_c' or orgType='0orgtype_sc')";
				
				List<SysOrganization> list = this.o_sysOrganizationDAO.createQuery(hql).list();
				if (list.size() == 1){
					orgCache.put(list.get(0).getOrgname(), list.get(0));
					find=true;
				}
			}
			
			if(!find){
				bads.add(emps.remove(i));
				emp.setHaddress("所属机构不存在");
			}else{
				emp.setSysOrganization(orgCache.get(emp.getRealname()));
			}
			
		}
		// 验证所属部门
		//key:部门名+所属机构名
		Map<String, SysOrganization> depCache = new HashMap<String, SysOrganization>();
		for (int i = emps.size() - 1; i >= 0; i--) {
			SysEmployee emp = emps.get(i);
			String[] orgs = emp.getEmpStatus().split("，");
			String info = "";
			for (String orgName : orgs) {
				boolean find = false;
				if (depCache.get(orgName+emp.getRealname()) != null) {
					SysOrganization org = depCache.get(orgName+emp.getRealname());
					if (org.getOrgseq().indexOf("." + emp.getSysOrganization().getId()) > -1)
						find = true;
				}
				if (!find) {
					//String hql = "from  SysOrganization where orgname='" + orgName + "' and orgseq like '%" + emp.getSysOrganization().getId() + "%'";
					//List<SysOrganization> list = getOrgsByOrgName(orgName,emp.getSysOrganization().getId());//this.o_sysOrganizationDAO.createQuery(hql).list();
					String hql="select org from SysOrganization org,SysOrganization corp where org.orgseq like CONCAT(corp.orgseq,'%') and org.orgname='"+orgName+"' and corp.orgname='"+emp.getRealname()+"'";
					List<SysOrganization> list = this.o_sysOrganizationDAO.createQuery(hql).list();
					
					if (list.size() == 1)
						depCache.put(list.get(0).getOrgname()+emp.getRealname(), list.get(0));
				}
				if (depCache.get(orgName+emp.getRealname()) != null) {
					SysEmpOrg seo = new SysEmpOrg();
					seo.setSysOrganization(depCache.get(orgName+emp.getRealname()));
					seo.setSysEmployee(emp);
					
					if (orgName.equals(orgs[0]))//墨认第一个为主机构
						seo.setIsmain(true);
					else
						seo.setIsmain(false);
					seo.setId(Identities.uuid2());
					emp.getSysEmpOrgs().add(seo);
				} else {
					info += "部门 '" + orgName + "' 不存在,";
				}
			}
			if (info.length() > 0) {
				bads.add(emps.remove(i));
				emp.setImpErrorInfo(info);
			}
		}
		
		// 验证所属职务
		//key:职务名+机构名
		Map<String, Duty> dutyCache = new HashMap<String, Duty>();
		for (int i = emps.size() - 1; i >= 0; i--) {
			SysEmployee emp = emps.get(i);
			if (dutyCache.get(emp.getUserid()+emp.getRealname()) == null) {
				String hql = "from Duty where dutyName='" + emp.getUserid() + "' and company.id='" + emp.getSysOrganization().getId()  + "'";
				List<Duty> list = this.o_sysOrganizationDAO.createQuery(hql).list();
				if (list.size() == 1)
					dutyCache.put(list.get(0).getDutyName()+emp.getRealname(), list.get(0));
			}
			if (dutyCache.get(emp.getUserid()+emp.getRealname()) != null) {
				emp.setDuty(dutyCache.get(emp.getUserid()+emp.getRealname()));
			} else{
				emp.setImpErrorInfo("职务'" + emp.getUserid() + "' 不存在");
				bads.add(emps.remove(i));
			}
		}
		
		// 验证所属岗位
		//key:所属机构名+部门名+岗位名  只用主部门,
		Map<String, SysPosition> psoiCache = new HashMap<String, SysPosition>();
		for (int i = emps.size() - 1; i >= 0; i--) {
			SysEmployee emp = emps.get(i);
			if(StringUtils.isBlank(emp.getCardtype())){
				continue;
			}
			
			String[] posis = emp.getCardtype().split("，");//所有岗位
			String[] orgs = emp.getEmpStatus().split("，");//所有部门
			
			String info = "";
			for (int index=0;index<posis.length;index++) {
				boolean find = false;
				if(orgs.length==1){//多个部门 分析为一一对应，一个部门 分析为一个部门多个岗位
					
					if (depCache.get(emp.getRealname()+orgs[0]+posis[index]) != null) {
						SysPosition posi = psoiCache.get(emp.getRealname()+orgs[0]+posis[index]);
						if(null!=posi){
							find = true;						
						}
					}
					if (!find) {
						String hql="select posi from SysPosition posi where posi.posiname='"+ posis[index] +"'" +
						" and posi.sysOrganization.orgname='"+orgs[0]+"'" +
						" and posi.sysOrganization.parentOrg.orgname='"+emp.getRealname()+"'";
						List<SysPosition> list = this.o_sysOrganizationDAO.createQuery(hql).list();
						
						if (list.size() == 1)
							psoiCache.put(emp.getRealname()+orgs[0]+posis[index], list.get(0));
					}
					if (psoiCache.get(emp.getRealname()+orgs[0]+posis[index]) != null) {
						SysEmpPosi sep=new SysEmpPosi();
						sep.setSysPosition(psoiCache.get(emp.getRealname()+orgs[0]+posis[index]));
						sep.setSysEmployee(emp);
						if(0==index){
							sep.setIsmain(true);
						}
						sep.setId(Identities.uuid2());
						emp.getSysEmpPosis().add(sep);
						
						
					} else {
						info += "岗位 '" + posis[index] + "' 不存在,";
					}
				}else{//多个部门 分析为一一对应，
					if (depCache.get(emp.getRealname()+orgs[index]+posis[index]) != null) {
						SysPosition posi = psoiCache.get(emp.getRealname()+orgs[0]+posis[index]);
						if(null!=posi){
							find = true;						
						}
					}
					if (!find) {
						String hql="select posi from SysPosition posi where posi.posiname='"+ posis[index] +"'" +
						" and posi.sysOrganization.orgname='"+emp.getRealname()+"'" +
						" and posi.sysOrganization.parentOrg.orgname='"+orgs[index]+"'";
						List<SysPosition> list = this.o_sysOrganizationDAO.createQuery(hql).list();
						
						if (list.size() == 1)
							psoiCache.put(emp.getRealname()+orgs[index]+posis[index], list.get(0));
					}
					if (psoiCache.get(emp.getRealname()+orgs[index]+posis[index]) != null) {
						SysEmpPosi sep=new SysEmpPosi();
						sep.setSysPosition(psoiCache.get(emp.getRealname()+orgs[index]+posis[index]));
						sep.setSysEmployee(emp);
						if(0==index){
							sep.setIsmain(true);
						}
						sep.setId(Identities.uuid2());
						emp.getSysEmpPosis().add(sep);
						
						
					} else {
						info += "岗位 '" + posis[index] + "' 不存在,";
					}
					
				}
			}
			if (info.length() > 0) {
				bads.add(emps.remove(i));
				emp.setImpErrorInfo(info);
			}
		}
		
		// 验证所属角色
		Map<String, SysRole> roleCache = new HashMap<String, SysRole>();
		for (int i = emps.size() - 1; i >= 0; i--) {
			SysEmployee emp = emps.get(i);
			String[] roles = emp.getCardno().split("，");
			String info = "";
			for (String roleName : roles) {
				boolean find = false;
				if (roleCache.get(roleName) != null)
					find = true;
				if (!find) {
					String hql = "from SysRole where roleName='" + roleName + "'";
					List<SysRole> list = this.o_sysOrganizationDAO.createQuery(hql).list();
					if (list.size() == 1)
						roleCache.put(list.get(0).getRoleName(), list.get(0));
				}
				if (roleCache.get(roleName) != null) {
					
				} else {
					info += "角色 '" + roleName + "' 不存在,";
				}
			}
			if (info.length() > 0) {
				bads.add(emps.remove(i));
				emp.setImpErrorInfo(info);
			}
		}
		

		returnList[0] = emps;
		returnList[1] = bads;
		return returnList;
	}

	/**
	 * 验证职务
	 * 
	 * @param readExcel
	 * @return
	 * @return List[]
	 */
	private List[] checkDuty(List<Duty> dutys) {
		List[] returnList = new List[2];
		List<Duty> bads = new ArrayList<Duty>();
		// 验证必填项
		for (int i = dutys.size() - 1; i >= 0; i--) {
			Duty duty = dutys.get(i);
			if (StringUtils.isBlank(duty.getDutyName()) || StringUtils.isBlank(duty.getRemark()) || StringUtils.isBlank(duty.getId())) {
				bads.add(dutys.remove(i));
				String info="";
				if(StringUtils.isBlank(duty.getDutyName()))
					info+="职务名称为空,";
				if(StringUtils.isBlank(duty.getRemark()))
					info+="权重名称为空,";
				if(StringUtils.isBlank(duty.getId()))
					info+="所属公司名称为空,";
				duty.setImpErrorInfo(info+"必填项不完整");
			}
		}
		// 验证权限数字
		for (int i = dutys.size() - 1; i >= 0; i--) {
			Duty duty = dutys.get(i);
			try {
				duty.setWeight(Double.parseDouble(duty.getRemark()));
			} catch (Exception e) {
				bads.add(dutys.remove(i));
				duty.setImpErrorInfo("权重数据错误");
			}
		}
		// 验证是否已经存在
		for (int i = dutys.size() - 1; i >= 0; i--) {
			Duty duty = dutys.get(i);
			String hql = "from Duty where dutyName='" + duty.getDutyName() + "' and company.orgname='" + duty.getId() + "'";
			List list = this.o_sysOrganizationDAO.createQuery(hql).list();
			if (list.size() > 0) {
				bads.add(dutys.remove(i));
				duty.setImpErrorInfo("此职务已经存在");
			}
		}
		// 验证所属公司
		for (int i = dutys.size() - 1; i >= 0; i--) {
			Duty duty = dutys.get(i);
			//总公司或分公司都可以有职位,部门不行
			String hql = "from SysOrganization where orgname='" + duty.getId() + "' and (orgType='0orgtype_c' or orgType='0orgtype_sc')";
			List<SysOrganization> list = this.o_sysOrganizationDAO.createQuery(hql).list();
			if (list.size() == 1) {
				duty.setCompany(list.get(0));
			} else {
				bads.add(dutys.remove(i));
				duty.setImpErrorInfo("无法找到所属公司");
			}
		}
		returnList[0] = dutys;
		returnList[1] = bads;
		return returnList;
	}

	private List[] checkSysPosition(List<SysPosition> positions) {
		List[] returnList = new List[2];
		List<SysPosition> bads = new ArrayList<SysPosition>();
		// 验证必填项
		for (int i = positions.size() - 1; i >= 0; i--) {
			SysPosition pos = positions.get(i);
			if (StringUtils.isBlank(pos.getPosicode()) || StringUtils.isBlank(pos.getPosiname()) || StringUtils.isBlank(pos.getPosiStatus()) || StringUtils.isBlank(pos.getRemark())) {
				bads.add(positions.remove(i));
				String info="";
				if(StringUtils.isBlank(pos.getPosicode()))
					info+="岗位编号为空,";
				if(StringUtils.isBlank(pos.getPosiname()))
					info+="岗位名称为空,";
				if(StringUtils.isBlank(pos.getPosiStatus()))
					info+="所属部门为空,";
				if(StringUtils.isBlank(pos.getRemark()))
					info+="所属公司为空,";
				pos.setImpErrorInfo(info+"信息填写不完整");
			}
		}
		
		// 验证所属部门
		for (int i = positions.size() - 1; i >= 0; i--) {
			SysPosition pos = positions.get(i);
			//数据逻辑分析：认为公司下有部门，部门不再嵌套，岗位只能
			//select org from SysOrganization org where org.orgname='"+pos.getPosiStatus()+" and org.parentOrg.orgname='"+ org.getRemark() +"'";
			//String hql = "from SysOrganization where orgname='" + pos.getPosiStatus() + "'";
			String hql = "select org from SysOrganization org where org.orgname='"+pos.getPosiStatus()+"' and org.parentOrg.orgname='"+ pos.getRemark() +"'";
			List<SysOrganization> list = this.o_sysOrganizationDAO.createQuery(hql).list();
			if(1!=list.size()){
				pos.setImpErrorInfo("所属部门，公司信息不正确");
				bads.add(positions.remove(i));
			}else{
				pos.setSysOrganization(list.get(0));
			}
		}
		//能验证是否已经存在
		for (int i = positions.size() - 1; i >= 0; i--) {
			SysPosition pos = positions.get(i);
			String hql="select pos from SysPosition pos where pos.posiname='"+pos.getPosiname()+"'" +
					" and pos.sysOrganization.orgname='"+pos.getPosiStatus()+"'" +
					" and pos.sysOrganization.parentOrg.orgname='"+pos.getRemark() +"'";
			List<SysOrganization> list = this.o_sysOrganizationDAO.createQuery(hql).list();
			if(list.size()>0){
				pos.setImpErrorInfo("岗位已经存在");
				bads.add(positions.remove(i));
			}
		}
		
		
		returnList[0] = positions;
		returnList[1] = bads;
		return returnList;
	}

	/**
	 * 验证组织导入数据
	 * 
	 * @param orgs
	 * @return
	 * @return List[]
	 */
	private List[] checkOrg(List<SysOrganization> orgs) {
		List[] returnList = new List[2];//
		List<SysOrganization> bads = new ArrayList<SysOrganization>();
		// 判空
		for (int i = orgs.size() - 1; i >= 0; i--) {
			SysOrganization org = orgs.get(i);
			org.setOrgLevel(1);//默认为总结点
			if (StringUtils.isBlank(org.getOrgcode()) || StringUtils.isBlank(org.getOrgname()) || StringUtils.isBlank(org.getOrgType())) {
				orgs.remove(i);
				String info="";
				if(StringUtils.isBlank(org.getOrgcode()))
					info+="机构编号为空,";
				if(StringUtils.isBlank(org.getOrgname()))
					info+="机构名称为空,";
				if(StringUtils.isBlank(org.getOrgType()))
					info+="机构类型（总公司、总公司部门、分公司、分公司部门）为空,";
				
				org.setImpErrorInfo(info+"必填项不完整");
				
				bads.add(org);
			}
		}
		for (int i = orgs.size() - 1; i >= 0; i--) {
			SysOrganization org = orgs.get(i);
			if (org.getOrgname().equals(org.getOrgseq())) {
				org.setId("-1");
				org.setImpErrorInfo("不能以自己作为上级组织");
				bads.add(orgs.remove(i));
			}
		}
		// 判断父节点是否存在
		for (int i = orgs.size() - 1; i >= 0; i--) {
			SysOrganization org = orgs.get(i);
			//org.setOrgLevel(1);//默认为总结点
			boolean isFind = false;
			if (org.getOrgType().equals("总公司"))
				isFind = true;
			else {
				// 本数据中判断
				for (SysOrganization t : orgs) {
					if (org.getOrgseq().equals(t.getOrgname())) {
						if (org.getOrgType().equals("分公司") && (t.getOrgType().equals("总公司") || t.getOrgType().equals("分公司")))
							isFind = true;
						if (org.getOrgType().equals("总公司部门") && t.getOrgType().equals("总公司"))
							isFind = true;
						if (org.getOrgType().equals("分公司部门") && (t.getOrgType().equals("分公司") || (!t.getForum().equals("") && t.getOrgType().equals("分公司部门") && t.getForum().equals(org.getForum()))))
							isFind = true;
						if (isFind){
							org.setParentOrg(t);
							
						}
							
					}
				}
				// 数据库中查找
				if (!isFind) {
					String hql = "from SysOrganization where orgname='" + org.getOrgseq() + "'";
					if (org.getOrgType().equals("分公司"))// 父节点是总公司
						hql += " and (orgType='0orgtype_c' or orgType='0orgtype_sc')";
					if (org.getOrgType().equals("总公司部门"))// 父节点是 总公司部门 或 总公司
						hql += " and (orgType='0orgtype_d' or orgType='0orgtype_c')";
					if (org.getOrgType().equals("分公司部门"))// 父节点是 分公司 或 分公司部门
						hql += " and (orgType='0orgtype_sc' or orgType='0orgtype_sd')";
					List<SysOrganization> list = this.o_sysOrganizationDAO.createQuery(hql).list();
					if (org.getOrgType().equals("分公司部门")) {
						for (SysOrganization so : list) {
							while (!so.getOrgType().equals("0orgtype_sc")) {
								so = so.getParentOrg();
							}
							String comName = so.getOrgname();
							if (comName.equals(org.getForum())) {
								org.setParentOrg(so);
								isFind = true;
								break;
							}
						}
					} else if (list.size() > 0) {
						org.setParentOrg(list.get(0));
						isFind = true;
					}
				}
			}
			if (!isFind) {
				orgs.remove(i);
				org.setId("-1");
				org.setImpErrorInfo("上级节点不存在");
				bads.add(org);
			} else {
				String hql = "from SysOrganization where (orgType='" + typeToCode(org.getOrgType()) + "' and orgname='" + org.getOrgname() + "' " + (org.getParentOrg() == null ? "" : "and parentOrg.id='" + org.getParentOrg().getId() + "'") + " ) or orgcode='" + org.getOrgcode() + "'";
				List<SysOrganization> list = this.o_sysOrganizationDAO.createQuery(hql).list();
				if (list.size() > 0) {
					orgs.remove(i);
					org.setImpErrorInfo("已经存在");
					org.setId(list.get(0).getId());
					bads.add(org);
				}
			}
		}
		
		for (int i = orgs.size() - 1; i >= 0; i--) {
			SysOrganization org = orgs.get(i);
			org.setOrgLevel(getlevel(org));
		}
		
		returnList[0] = orgs;
		returnList[1] = bads;
		return returnList;
	}
	
	private int getlevel(SysOrganization org){
		if(org.getParentOrg()!=null){
			return getlevel(org.getParentOrg())+1;
		}else{
			return 1;
		}
	}

	private String typeToCode(String type) {
		String comType = "";
		if (type.equals("总公司")){
			comType = "0orgtype_c";
		}else if (type.equals("分公司")){
			comType = "0orgtype_sc";
		}else if (type.equals("总公司部门")){
			comType = "0orgtype_d";
		}else if (type.equals("分公司部门")){
			comType = "0orgtype_sd";
		}else{
			comType = type;
		}
		return comType;
	}

	/**
	 * 导入机构
	 * @param datas
	 * @param edata
	 */
	@Transactional
	public void importOrg(List<SysOrganization> datas, List<SysOrganization> edata) {
		for (SysOrganization org : datas) {
			org.setForum("");
			org.setOrgType(typeToCode(org.getOrgType()));
			org.setOrgseq("");
		}
		
		for (SysOrganization org : datas) {
			//如果机构存在，则不插入
			//需要补充实现
			if (null == org.getId()) {
				org.setId(Identities.uuid2());
				this.save(org);
			}

			String pseq="";
			if(null!=org.getParentOrg()){
				if(StringUtils.isBlank(org.getParentOrg().getOrgseq())){
					pseq=getOrgSeq(org);
				}else{
					pseq=org.getParentOrg().getOrgseq().substring(0, org.getParentOrg().getOrgseq().length()-1);
				}
			}
			org.setOrgseq(pseq+"."+org.getId()+".");
				
			o_sysOrganizationDAO.merge(org);
		}
	}
	/**
	 * 根据id递归生seq
	 * @author 万业
	 * @param org
	 * @return
	 */
	public String getOrgSeq(SysOrganization org){
		if(null==org.getParentOrg()){
			return "."+org.getId()+".";
		}
		String temp=getOrgSeq(org.getParentOrg());
		return temp.substring(0, temp.length()-1)+"."+org.getId()+".";
		
	}
	/**
	 * 导入机构 批量导入
	 * @author 万业
	 * @param datas
	 * @param edata
	 */
	@Transactional
	public void importOrg(List<SysOrganization> datas, List<SysOrganization> edata, int batch) {
		for (SysOrganization org : datas) {
			org.setForum("");
			org.setOrgType(typeToCode(org.getOrgType()));
			org.setOrgseq("");
		}
		int i=0;
		for (SysOrganization org : datas) {
			if (null == org.getId()) {
				//saveOrg(org);
				org.setId(Identities.uuid2());
				o_sysOrganizationDAO.save(org);
			}

			StringBuilder seq = new StringBuilder(".");
			seq.insert(0,org.getParentOrg().getOrgseq()+org.getId());
			
			o_sysOrganizationDAO.merge(org);
			
			i++;
			if(0==i%batch){
				Session session=o_sysOrganizationDAO.getHibernateTemplate().getSessionFactory().getCurrentSession();
				session.flush();
				session.clear();
			}
		}
	}
	/**
	 * 可能以后会用
	 * 
	 * 验证角色导入数据 
	 * list[0]正确数据,list[1]:错误数据
	 * @author 万业
	 * @param roles
	 * @return
	 */
	@SuppressWarnings("unused")
	private List[] checkSysRole(List<SysRole> roles){
		List[] returnList = new List[2];//
		List<SysRole> bads = new ArrayList<SysRole>();
		// 判空
		for (int i = roles.size() - 1; i >= 0; i--) {
			SysRole role = roles.get(i);
			if (!(StringUtils.isNotBlank(role.getRoleCode()) && StringUtils.isNotBlank(role.getRoleName()))) {
				roles.remove(i);
				role.setId("必填项不完整");
				bads.add(role);
			}
		}
		// 验证是否已经存在
		for (int i = roles.size() - 1; i >= 0; i--) {
			SysRole role = roles.get(i);
			String hql = "from SysRole where roleCode='" + role.getRoleCode() + "' and roleName='" + role.getRoleName() + "'";
			List list = this.o_sysOrganizationDAO.createQuery(hql).list();
			if (list.size() > 0) {
				bads.add(roles.remove(i));
				role.setId("此职务已经存在");
			}
		}
		returnList[0] = roles;
		returnList[1] = bads;
		return returnList;
	}
	/**
	 * 可能以后会用
	 * 验证角色and用户关系导入数据
	 * list[0]正确数据,list[1]:错误数据
	 * 数据用SysUser装配:username->username;realname->rolename
	 * @author 万业
	 * @param roles
	 * @return
	 */
	@SuppressWarnings("unused")
	private List[] checkSysRoleUser(List<SysUser> roleAndusers){
		List[] returnList = new List[2];//
		List<SysUser> bads = new ArrayList<SysUser>();
		// 判空
		for (int i = roleAndusers.size() - 1; i >= 0; i--) {
			SysUser roleAnduser = roleAndusers.get(i);
			if (!(StringUtils.isNotBlank(roleAnduser.getUsername()) && StringUtils.isNotBlank(roleAnduser.getRealname()))) {
				roleAndusers.remove(i);
				roleAnduser.setUserStatus("必填项不完整");
				bads.add(roleAnduser);
			}
		}
		// 验证数据库中是否已经存在
		for (int i = roleAndusers.size() - 1; i >= 0; i--) {
			SysUser roleAnduser = roleAndusers.get(i);
			String hql = "select user from SysUser user, sysRole role where user.username='" + roleAnduser.getUsername() + "' and role.roleName='" + roleAnduser.getRealname() + "'";//distinct 
			List list = this.o_sysOrganizationDAO.createQuery(hql).list();
			if (list.size() > 0) {
				bads.add(roleAndusers.remove(i));
				roleAnduser.setUserStatus("此职务已经存在");
			}
		}
		//验证本在数据是否重复
		
		returnList[0] = roleAndusers;
		returnList[1] = bads;
		return returnList;
	}
	/**
	 * 批量导入角色
	 * @author 万业
	 * @param data
	 * @param edata
	 * @param batch
	 */
	public void importRole(List<SysRole> data, List<SysRole> edata, int batch) {
		int i=0;
		for (SysRole role : data) {
			role.setId(Identities.uuid2());
			this.o_sysOrganizationDAO.merge(role);
			i++;
			if(0 == i%batch){
				Session session=o_sysOrganizationDAO.getHibernateTemplate().getSessionFactory().getCurrentSession();
				session.flush();
				session.clear();
			}
			
		}
	}
	/**
	 * 批量更新user_roel
	 * @author 万业
	 * @param data 正确数据
	 * @param edata 错误数据
	 * @param batch
	 */
	public void importRoleUser(List<SysUser> data, List<SysUser> edata, int batch){
		int i=0;
		for (SysUser roleuser:data){
			SysUser user=o_sysUserDAO.findUniqueBy("username", roleuser.getUsername());
			SysRole role=o_sysRoleDAO.findUniqueBy("roleName", roleuser.getRealname());
			user.getSysRoles().add(role);
			
			o_sysUserDAO.update(user);
			
			i++;
			if(0 == i%batch){
				Session session=o_sysOrganizationDAO.getHibernateTemplate().getSessionFactory().getCurrentSession();
				session.flush();
				session.clear();
			}
		}
	}
	
	
	/**
	 * 导入全部数据
	 * @author 万业
	 * @param fileId 文件ID
	 */
	@Transactional
	public void importAllData(String fileId) throws Exception{
		FileUploadEntity excel = o_fileUploadBO.queryFileById(fileId);
		int batch=50;
		//机构
		ReadExcel<SysOrganization> readRiskExcelOrg = new ReadExcel<SysOrganization>(SysOrganization.class);
		List[] datasOrg = checkOrg(readRiskExcelOrg.readExcel(excel.getFileAddress(),0));
		this.importOrg(datasOrg[0], datasOrg[1], batch);
		//员工
		ReadExcel<SysEmployee> readRiskExcelEmp = new ReadExcel<SysEmployee>(SysEmployee.class);
		List[] datasEmp = checkSysEmployee(readRiskExcelEmp.readExcel(excel.getFileAddress(), 1));
		this.importSysEmployee(datasEmp[0], datasEmp[1], batch);
		//职务
		ReadExcel<Duty> readRiskExcelDuty = new ReadExcel<Duty>(Duty.class);
		List[] datasDuty = checkDuty(readRiskExcelDuty.readExcel(excel.getFileAddress(), 2));
		this.importDuty(datasDuty[0], datasDuty[1], batch);
		//岗位
		ReadExcel<SysPosition> readRiskExcelPos = new ReadExcel<SysPosition>(SysPosition.class);
		List[] datasPos = checkSysPosition(readRiskExcelPos.readExcel(excel.getFileAddress(), 3));
		this.importPosi(datasPos[0], datasPos[1], batch);
		//工作组
		ReadExcel<SysGroup> readRiskExcelGro = new ReadExcel<SysGroup>(SysGroup.class);
		List[] datasGro = checkSysSysGroup(readRiskExcelGro.readExcel(excel.getFileAddress(), 4));
		this.importSysGroup(datasGro[0], datasGro[1], batch);
		
	}
	
	
	/**
	 * 不知道previous author 想做什么，保留 
	 * @author 万业
	 * 
	 * 
	 * 导入-保存组织机构
	 * 
	 * @param org
	 * @return
	 * @return boolean
	 */
	@SuppressWarnings("unused")
	private boolean saveOrg(SysOrganization org) {
		if (org.getParentOrg() != null)
			if (!saveOrg(org.getParentOrg()))
				return false;
		if ("-1".equals(org.getId()))
			return false;
		if (org.getId() == null) {
			org.setId(Identities.uuid2());
			this.o_sysOrganizationDAO.save(org);
		}
		return true;
	}

	/**
	 * 导入岗位
	 * 
	 * @param data
	 * @param edata
	 * @return void
	 */
	@Transactional
	public void importPosi(List<SysPosition> data, List<SysPosition> edata) {
		for (SysPosition pos : data) {
			SysOrganization org = pos.getSysOrganization();
			org.setIsLeaf(false);
			o_sysOrganizationDAO.merge(org);
			pos.setId(Identities.uuid2());
			pos.setRemark(null);
			pos.setPosiStatus(null);
			o_sysPositionDAO.merge(pos);
		}
	}
	/**
	 * 导入岗位 批量导入
	 * @author 万业
	 * @param data
	 * @param edata
	 * @return void
	 */
	@Transactional
	public void importPosi(List<SysPosition> data, List<SysPosition> edata, int batch) {
		int i=0;
		for (SysPosition pos : data) {
			pos.setId(Identities.uuid2());
			pos.setRemark(null);
			pos.setPosiStatus(null);
			this.o_sysOrganizationDAO.merge(pos);
			i++;
			if(0 == i%batch){
				Session session=o_sysOrganizationDAO.getHibernateTemplate().getSessionFactory().getCurrentSession();
				session.flush();
				session.clear();
			}
			
		}

	}
	/**
	 * 导入职务
	 * 
	 * @param data
	 * @param edata
	 * @return void
	 */
	@Transactional
	public void importDuty(List<Duty> data, List<Duty> edata) {
		for (Duty duty : data) {
			duty.setId(Identities.uuid2());
			//设置状态可用
			duty.setStatus("E598CCCAD8AFC3EE0354719300E20313");
			duty.setRemark(null);
			this.o_sysOrganizationDAO.merge(duty);
		}

	}
	/**
	 * 导入职务 批量导入
	 * @author 万业
	 * @param data
	 * @param edata
	 * @return void
	 */
	@Transactional
	public void importDuty(List<Duty> data, List<Duty> edata,int batch) {
		int i=0;
		for (Duty duty : data) {
			duty.setId(Identities.uuid2());
			//状态：可用
			duty.setStatus("E598CCCAD8AFC3EE0354719300E20313");
			duty.setRemark(null);
			this.o_sysOrganizationDAO.merge(duty);
			i++;
			if(0 == i%batch){
				Session session=o_sysOrganizationDAO.getHibernateTemplate().getSessionFactory().getCurrentSession();
				session.flush();
				session.clear();
			}
		}

	}

	/**
	 * 导入员工
	 * 
	 * @param data 有效数据
	 * @param edata 无效数据
	 * @return void
	 */
	@Transactional
	public void importSysEmployee(List<SysEmployee> data, List<SysEmployee> edata) {
		Map<String, SysRole> roleCache = new HashMap<String, SysRole>();
		
		for (SysEmployee emp : data) {
			// 新增用户
			SysUser user = new SysUser();
			user.setId(Identities.uuid2());
			user.setUsername(emp.getUsername());
			user.setPassword(DigestUtils.md5ToHex(emp.getUsername()));
			user.setEnable(true);
			user.setLockstate(false);
			user.setUserStatus("30270F7E87566A24C94B5FD0D90672C4");//正常
			user.setRealname(emp.getEmpname());
			// 关联用户角色
			//System.out.println("*******" + emp.getEmpname() + " __" + emp.getCardno());
			
			String[] roles = emp.getCardno().split("，");
			for (String roleName : roles) {
				if (roleCache.get(roleName) == null) {
					String hql = "from SysRole where roleName='" + roleName + "'";
					List<SysRole> list = this.o_sysOrganizationDAO.createQuery(hql).list();
					if(null!=list && list.size()>0){
						roleCache.put(list.get(0).getRoleName(), list.get(0));
					}
				}
				user.getSysRoles().add(roleCache.get(roleName));
			}
			this.o_sysOrganizationDAO.save(user);

			// 保存员工信息
			emp.setId(Identities.uuid2());
			emp.setGender("402881b22b0f8594012b0f87bdb70004");//男的
			this.o_sysOrganizationDAO.save(emp);
			
			
			
			// 保存员工部门
			for (SysEmpOrg seo : emp.getSysEmpOrgs())
				this.o_sysOrganizationDAO.save(seo);
			
			// 保存员工岗位
			for (SysEmpPosi sep : emp.getSysEmpPosis())
				this.o_sysOrganizationDAO.save(sep);

			/*//关联岗位
			String[] posi=emp.getCardtype().split("，");
			//Map<String, SysPosition> posiCache = new HashMap<String, SysPosition>();
			for(String posiName : posi){
				String hql = "from SysPosition where posiname ='" + posiName +"' and sysOrganization.orgname='"+ emp.getEmpStatus() +"'";
				List<SysPosition> list = this.o_sysOrganizationDAO.find(hql);
				if(null!=list && list.size() > 0){
					SysEmpPosi empPosi = new SysEmpPosi();
					empPosi.setId(GuidGeneratorUtils.getUUID32());
					empPosi.setSysEmployee(emp);
					empPosi.setSysPosition(list.get(0));
					
					this.o_sysOrganizationDAO.save(empPosi);
				}
					
			}*/
			
			emp.setUserid(user.getId());
			emp.setRealname(emp.getEmpname());
			emp.setEmpStatus(null);
			emp.setCardtype(null);
			emp.setCardno(null);
			this.o_sysOrganizationDAO.save(emp);
		}

	}
	/**
	 * 导入员工_批量导入
	 * 
	 * @param data 有效数据
	 * @param edata 无效数据
	 * @return void
	 */
	@Transactional
	public void importSysEmployee(List<SysEmployee> data, List<SysEmployee> edata,int batch) {
		int i=0;
		Map<String, SysRole> roleCache = new HashMap<String, SysRole>();
		for (SysEmployee emp : data) {
			// 新增用户
			SysUser user = new SysUser();
			user.setId(Identities.uuid2());
			user.setUsername(emp.getUsername());
			user.setPassword(DigestUtils.md5ToHex(emp.getUsername()));
			user.setEnable(true);
			user.setLockstate(false);
			user.setRealname(emp.getEmpname());
			// 关联用户角色
			String[] roles = emp.getCardno().split(",");
			for (String roleName : roles) {
				if (roleCache.get(roleName) == null) {
					String hql = "from SysRole where roleName='" + roleName + "'";
					List<SysRole> list = this.o_sysOrganizationDAO.createQuery(hql).list();
					roleCache.put(list.get(0).getRoleName(), list.get(0));
				}
				user.getSysRoles().add(roleCache.get(roleName));
			}
			this.o_sysOrganizationDAO.save(user);

			// 保存员工信息
			emp.setId(Identities.uuid2());
			this.o_sysOrganizationDAO.save(emp);
			// 保存员工部门
			for (SysEmpOrg seo : emp.getSysEmpOrgs())
				this.o_sysOrganizationDAO.save(seo);
			// 保存员工岗位
			for (SysEmpPosi sep : emp.getSysEmpPosis())
				this.o_sysOrganizationDAO.save(sep);

			emp.setUserid(user.getId());
			emp.setRealname(emp.getEmpname());
			emp.setEmpStatus(null);
			emp.setCardtype(null);
			emp.setCardno(null);
			this.o_sysOrganizationDAO.save(emp);
			//清空session
			i++;
			if(0 == i%batch){
				Session session=o_sysOrganizationDAO.getHibernateTemplate().getSessionFactory().getCurrentSession();
				session.flush();
				session.clear();
			}
		}

	}
	/**
	 * 导入工作组
	 * 
	 * @param data
	 * @param edata
	 * @return void
	 */
	@Transactional
	public void importSysGroup(List<SysGroup> data, List<SysGroup> edata) {
		for (SysGroup group : data) {
			if (group.getId() == null)
				saveGroup(group);
		}
	}

	private void saveGroup(SysGroup group) {
		if (group.getParentGroup().getId() == null)
			saveGroup(group.getParentGroup());
		group.setId(Identities.uuid2());
		this.o_sysOrganizationDAO.save(group);
	}

	/**
	 * 导入工作组 批量导入
	 * @author 万业
	 * @param data
	 * @param edata
	 * @return void
	 */
	@Transactional
	public void importSysGroup(List<SysGroup> data, List<SysGroup> edata,int batch) {
		int i=0;
		for (SysGroup group : data) {
			if (group.getId() == null){
				saveGroup(group);
			}
			i++;
			if(0 == i%batch){
				Session session=o_sysOrganizationDAO.getHibernateTemplate().getSessionFactory().getCurrentSession();
				session.flush();
				session.clear();
			}
		}
	}
	/**
	 * 获取本机构的下级机构 查询 分页
	 * @author 万业
	 * @param page
	 * @param id 机构ID
	 * @param sysOrg
	 * @return
	 */
	public Page<SysOrganization> queryNextSysOrgByIdAndCond(Page<SysOrganization> page, String id, SysOrganization sysOrg){
		DetachedCriteria dc = DetachedCriteria.forClass(SysOrganization.class);
		dc.add(Property.forName("parentOrg.id").eq(id));
		if(StringUtils.isNotBlank(sysOrg.getOrgcode())){
			dc.add(Property.forName("orgcode").like(sysOrg.getOrgcode(),MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(sysOrg.getOrgname())){
			dc.add(Property.forName("orgname").like(sysOrg.getOrgname(),MatchMode.ANYWHERE));
		}
		dc.addOrder(Order.asc("orgname"));
		return o_sysOrganizationDAO.pagedQuery(dc, page);
	}
	/**
	 * 根据查询条件查询机构--导出使用.
	 * @author 吴德福
	 * @param id
	 * @param orgcode
	 * @param orgname
	 * @return List<SysOrganization>
	 * @since  fhd　Ver 1.1
	 */
	public List<SysOrganization> findNextSysOrgBySome(String id,String orgcode,String orgname){
		DetachedCriteria dc = DetachedCriteria.forClass(SysOrganization.class);
		if(StringUtils.isNotBlank(id)){
			dc.add(Restrictions.eq("parentOrg.id",id));
		}
		if(StringUtils.isNotBlank(orgcode)){
			dc.add(Restrictions.like("orgcode",orgcode,MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(orgname)){
			dc.add(Restrictions.like("orgname",orgname,MatchMode.ANYWHERE));
		}
		dc.addOrder(Order.asc("orgcode"));
		return o_sysOrganizationDAO.findByCriteria(dc);
	}
	/**
	 * 根据机构层级查找
	 * @author 杨鹏
	 * @param orgLevel
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<SysOrganization> findByOrgLevel(int orgLevel){
		DetachedCriteria criterions=DetachedCriteria.forClass(SysOrganization.class);
		criterions.add(Restrictions.eq("orgLevel", orgLevel));
		return o_sysOrganizationDAO.findByCriteria(criterions);
	}
	
	/**
	 * 
	 * <pre>
	 * 初始化风险、流程、指标、资产相关数据
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param companyid 公司ID
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void initData(String companyid) {
		
		// 删除风险及关联表
		String delete_risksTemp = "delete from RmRiskTemp rrt where rrt.rmRisk.id in (select id from RmRisk rr where rr.org.id = ?)";
		String delete_risksOrg = "delete RiskOrg ro where ro.risk.id in (select id from RmRisk rr where rr.org.id = ?)";
		String delete_t_kpi_result_relate_r = "delete TargetGatherResultRelateRisk tgrrr where tgrrr.risk.id in (select id from RmRisk rr where rr.org.id = ?)";
		String delete_t_rm_riskhistoryadjustment = "delete RiskHistoryAdjustment rha where rha.rmRisks.id in (select id from RmRisk rr where rr.org.id = ?)";
		String delete_t_rm_statistics_emp_weight = "delete EmpWeight ew where ew.risk.id in (select id from RmRisk rr where rr.org.id = ?)";
		String delete_t_rm_statistics_org_weight = "delete OrgWeight ow where ow.risk.id in (select id from RmRisk rr where rr.org.id = ?)";
		String delete_t_rm_statistics_result = "delete RmProgramResult rpr where rpr.risk.id in (select id from RmRisk rr where rr.org.id = ?)";
		String delete_t_rm_his_risks = "delete RmHisRisk rhr where rhr.parentRisk.id in (select id from RmRisk rr where rr.org.id = ?)";
		String delete_t_processure_risk_processure = "delete ProcessureRisk rp where rp.risk.id in (select id from RmRisk rr where rr.org.id = ?)";
		String delete_t_kpi_target_risk = "delete TargetRisk tr where tr.risk.id in (select id from RmRisk rr where rr.org.id = ?)";
		String delete_t_rm_amendconceit = "delete ImpRiskAmendconceit ira where ira.rmRisks.id in (select id from RmRisk rr where rr.org.id = ?)";
		String delete_t_rm_programresponse = "delete RiskProgram pp where pp.rmRisk.id in (select id from RmRisk rr where rr.org.id = ?)";
		String delete_t_rm_riskcauseanalyze = "delete RiskCauseAnalyze rca where rca.rmRisks.id in (select id from RmRisk rr where rr.org.id = ?)";
		String delete_t_rm_hisevent = "delete HisEvent he where he.rmRisk.id in (select id from RmRisk rr where rr.org.id = ?)";
		String delete_t_assets_risk_assets = "delete RiskAssets ar where ar.risk.id in (select id from RmRisk rr where rr.org.id = ?)";
		String delete_t_dim_templatedim = "delete TemplateDim td where td.template.id in (select id from Template t where t.risk.id in (select id from RmRisk rr where rr.org.id = ?))";
		String update_t_rm_risks = "update RmRisk rr set template = NULL where rr.org.id = ?";
		String delete_t_dim_template = "delete Template t where t.risk.id in (select id from RmRisk rr where rr.org.id = ?)";
		String delete_t_rm_risks_plates = "delete RmRisksPlates rrp where rrp.risk.id in (select id from RmRisk rr where rr.org.id = ?)";
		String delete_t_rm_risks_positions = "delete RmRisksPositions rrp where rrp.risk.id in (select id from RmRisk rr where rr.org.id = ?)";
		String delete_t_rm_risks = "delete RmRisk rr where rr.org.id = ?)";
		
		o_sysOrganizationDAO.createQuery(delete_risksTemp, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_risksOrg, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_kpi_result_relate_r, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_rm_riskhistoryadjustment, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_rm_statistics_emp_weight, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_rm_statistics_org_weight, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_rm_statistics_result, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_rm_his_risks, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_processure_risk_processure, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_kpi_target_risk, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_rm_amendconceit, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_rm_programresponse, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_rm_riskcauseanalyze, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_rm_hisevent, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_assets_risk_assets, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_dim_templatedim, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(update_t_rm_risks, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_dim_template, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_rm_risks_plates, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_rm_risks_positions, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_rm_risks, companyid).executeUpdate();
		
		// 删除流程及关联表
		String delete_t_assets_processure_assets = "delete ProcessureAssets pa where pa.processure.id in (select id from Processure p where p.company.id = ?)";
		String delete_t_processure_kpi_processure = "delete ProcessureTarget tp where tp.processure.id in (select id from Processure p where p.company.id = ?)";
		String delete_t_processure_org_processure = "delete ProcessureOrg op where op.processure.id in (select id from Processure p where p.company.id = ?)";
		String delete_t_processure_processure = "delete from Processure p where p.company.id = ?";
		
		o_sysOrganizationDAO.createQuery(delete_t_assets_processure_assets, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_processure_kpi_processure, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_processure_org_processure, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_processure_processure, companyid).executeUpdate();
		
		// 删除指标及关联
		String delete_t_kpi_target_gather_result = "delete TargetGatherResult tgr where tgr.target.id in (select id from Target t where t.company.id = ?)";
		String delete_t_kpi_target_monitor_plan = "delete TargetMonitorPlan tmp where tmp.target.id in (select id from Target t where t.company.id = ?)";
		String delete_t_kpi_warning_region = "delete WarningRegion wr where wr.warningPlan.id in (select id from WarningPlan wp where wp.target.id in (select id from Target t where t.company.id = ?))";
		String delete_t_kpi_warning_region_instance = "delete WarningRegionInstance wri where wri.warningPlanInstance.id in (select id from WarningPlanInstance wpi where wpi.modelWarningPlan.id in (select id from WarningPlan wp where wp.target.id in (select id from Target t where t.company.id = ?)))";
		String delete_t_kpi_warning_plan_instance = "delete WarningPlanInstance wpi where wpi.modelWarningPlan.id in (select id from WarningPlan wp where wp.target.id in (select id from Target t where t.company.id = ?))";
		String delete_t_kpi_warning_plan = "delete WarningPlan wp where wp.target.id in (select id from Target t where t.company.id = ?)";
		String delete_t_kpi_warning_region_instance2 = "delete WarningRegionInstance wri where wri.warningPlanInstance.id in (select id from WarningPlanInstance wpi where wpi.target.id in (select id from Target t where t.company.id = ?))";
		String delete_t_kpi_warning_plan_instance2 = "delete WarningPlanInstance wpi where wpi.target.id in (select id from Target t where t.company.id = ?)";
		String delete_t_kpi_paramter_region = "delete EfficacyCoefficient ec where ec.target.id in (select id from Target t where t.company.id = ?)";
		String delete_t_kpi_targetowner = "delete TargetOwner to where to.target.id in (select id from Target t where t.company.id = ?)";
		String delete_t_assets_kpi_assets = "delete TargetAssets ta where ta.target.id in (select id from Target t where t.company.id = ?)";
		String delete_t_kpi_target_org = "delete TargetOrg to where to.target.id in (select id from Target t where t.company.id = ?)";
		String delete_t_kpi_kpi = "delete from Target t where t.company.id = ?";
		
		o_sysOrganizationDAO.createQuery(delete_t_kpi_target_gather_result, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_kpi_target_monitor_plan, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_kpi_warning_region, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_kpi_warning_region_instance, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_kpi_warning_plan_instance, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_kpi_warning_plan, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_kpi_warning_region_instance2, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_kpi_warning_plan_instance2, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_kpi_paramter_region, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_kpi_targetowner, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_assets_kpi_assets, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_kpi_target_org, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_kpi_kpi, companyid).executeUpdate();
		
		// 删除资产及关联表
		String delete_t_assets_org_assets = "delete OrgAssets oa where oa.assets.id in (select id from Assets a where a.company.id = ?)";
		String delete_t_assets_assets = "delete from Assets a where a.company.id = ?)";
		
		o_sysOrganizationDAO.createQuery(delete_t_assets_org_assets, companyid).executeUpdate();
		o_sysOrganizationDAO.createQuery(delete_t_assets_assets, companyid).executeUpdate();

		// TODO  初始化需要重写
		/*SysOrganization company = new SysOrganization(companyid);

		
		
		// 初始化资产树根  
		Assets assets = new Assets();
		
		String assetsId = Identities.uuid2();
		
		assets.setId(assetsId);
		assets.setAssetsCode("1");
		assets.setAssetsName("资产库");
		assets.setAssetsDesc("资产库");
		assets.setAssetsLevel(1);
		assets.setDealStatus("1");
		assets.setIsLeaf(false);
		assets.setParentIdSeq("." + assetsId + ".");
		assets.setSort(1);
		assets.setStatus("3");
		assets.setCompany(company);
		
		o_assetsDAO.merge(assets);
		
		// 初始化指标树根
		Target target = new Target();
		
		String targetId = Identities.uuid2();
		
		target.setId(targetId);
		target.setIsCatalog("1");
		target.setIsLeaf(false);
		target.setKpiCode("1");
		target.setKpiDesc("指标库");
		target.setKpiName("指标库");
		target.setParentIdSeq("." + targetId + ".");
		target.setSort(1);
		target.setStatus("1");
		target.setTargetLevel(1);
		target.setCompany(company);
		
		o_targetDAO.merge(target);
		
		// 初始化风险树根
		RmRisk rmRisk = new RmRisk();
		
		String rmRiskId = Identities.uuid2();
		
		rmRisk.setId(rmRiskId);
		rmRisk.setArchiveStatus("1");
		rmRisk.setDealStatus("1");
		rmRisk.seteLevel(1);
		rmRisk.setIsLeaf(false);
		rmRisk.setIsRiskClass("4");
		rmRisk.setManagementEffectiveness(0D);
		rmRisk.setManagementUrgency(0D);
		rmRisk.setRiskCode("1");
		rmRisk.setRiskDesc("整体风险");
		rmRisk.setRiskName("整体风险");
		rmRisk.setRiskSeq("." + rmRiskId + ".");
		rmRisk.setRiskWeight(1D);
		rmRisk.setSort(1);
		rmRisk.setStatus("3");
		Template template = new Template();
		template.setId("1");
		rmRisk.setTemplate(template);
		rmRisk.setOrg(company);
		
		o_riskDAO.merge(rmRisk);
		
		// 初始化流程树根
		Processure processure = new Processure();
		
		String processureId = Identities.uuid2();
		
		processure.setId(processureId);
		processure.setDealStatus("1");
		processure.setIsLeaf(false);
		processure.setParentIdSeq("." + processureId + ".");
		processure.setProcessureCode("1");
		processure.setProcessureDesc("流程库");
		processure.setProcessureName("流程库");
		processure.setProcessureLevel(1);
		processure.setSort(1);
		processure.setStatus("3");
		processure.setCompany(company);
		
		o_processureDAO.merge(processure);*/
	}
	
	/**
	 * <pre>
	 * getPathsBySearchName:根据查询内容得到风险树的Path集合
	 * </pre>
	 * 
	 * @author David
	 * @param searchName
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<String> getPathsBySearchName(String searchName){
		DetachedCriteria dc = DetachedCriteria.forClass(SysOrganization.class);
		dc.add(Restrictions.ilike("orgname", searchName, MatchMode.ANYWHERE));
		dc.addOrder(Order.asc("orgLevel"));
		List<SysOrganization> orgList=o_sysOrganizationDAO.findByCriteria(dc);
		List<String> pathList=new ArrayList<String>();
		for(SysOrganization org: orgList){
			pathList.add(org.getOrgseq().replace(".", "/").substring(0, org.getOrgseq().length()-1));
		}
		return pathList;
	}
}
