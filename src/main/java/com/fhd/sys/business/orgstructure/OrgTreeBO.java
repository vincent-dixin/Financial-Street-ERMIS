/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */


package com.fhd.sys.business.orgstructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.dao.orgstructure.SysOrgDao;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;
import com.fhd.sys.entity.orgstructure.SysPosition;

/**
 * 组织结构树查询
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-1-4		上午9:02:48
 *
 * @see 	 
 */
@Service
@SuppressWarnings("unchecked")
public class OrgTreeBO {

	@Autowired
	private SysOrgDao o_sysOrgDao;
	@Autowired
	private EmpolyeeBO o_empolyeeBO;
	
	/**
	 * 
	 * <pre>
	 * 公司、部门、岗位、员工 树查询
	 * 填加部门的模糊查询
	 * </pre>
	 * 
	 * @author 胡迪新,张雷
	 * @param id 树节点id
	 * @param posiVisible 是否显示岗位
	 * @param empVisible 是否显示员工
	 * @param isdisplayCompany 是否显示公司
	 * @param query 查询条件
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<Map<String, Object>> treeLoader(String id,Boolean posiVisible,Boolean empVisible, Boolean subCompany, String query){
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
		SysOrganization org = null;
		// 判断是否是集团公司
		if("root".equals(id)){
			org = o_sysOrgDao.findUnique(Restrictions.isNull("parentOrg"));
			Map<String, Object> node = new HashMap<String, Object>();
			node.put("id", org.getId() + "_" + RandomUtils.nextInt(9999));
			node.put("dbid", org.getId());
			node.put("text", org.getOrgname());
			node.put("leaf", org.getIsLeaf());
			String orgicon = ("0orgtype_c".equals(org.getOrgType()) || "0orgtype_sc".equals(org.getOrgType()) )?"icon-org":"icon-orgsub";
			node.put("iconCls", orgicon);
			node.put("cls", "org");
			nodes.add(node);
			return nodes;
		}else {
			org = o_sysOrgDao.get(id);
		}
		
		// 使用断言验证组织
		Assert.notNull(org, "组织机构数据未初始化，请与管理员联系");
		
		// 加载公司和部门
		String[] orgType = new String[]{"0orgtype_d","0orgtype_sd"};
		if(null!=subCompany&&subCompany){
			orgType = new String[]{"0orgtype_d","0orgtype_sd","0orgtype_sc"};
		}
		Set<String> idSet = queryOrgBySearchName(query,org.getId(),subCompany,orgType);
		Set<SysOrganization> subOrgs = org.getSubOrg();
		if(StringUtils.isNotBlank(query)&&idSet.size()==0){
			return nodes;
		}
		for (SysOrganization subOrg : subOrgs) {
			if(!"admin".equals(UserContext.getUsername()) && !idSet.contains(subOrg.getId())){
				continue;
			}
			Map<String, Object> node = new HashMap<String, Object>();
			node.put("id", subOrg.getId() + "_" + RandomUtils.nextInt(9999));
			node.put("dbid", subOrg.getId());
			node.put("text", subOrg.getOrgname());
			node.put("leaf", subOrg.getIsLeaf());
			String orgicon = ("0orgtype_c".equals(subOrg.getOrgType()) || "0orgtype_sc".equals(subOrg.getOrgType()) )?"icon-org":"icon-orgsub";
			node.put("iconCls", orgicon);
			node.put("cls", "org");
			nodes.add(node);
		}
		
		// 加载岗位
		if(posiVisible) {
			Set<SysPosition> subPosis = org.getSysPositions();
			for (SysPosition subPosi : subPosis) {
				Map<String, Object> node = new HashMap<String, Object>();
				node.put("id", subPosi.getId() + "_" + RandomUtils.nextInt(9999));
				node.put("dbid", subPosi.getId());
				node.put("text", subPosi.getPosiname());
				node.put("leaf", false);
				node.put("iconCls", "icon-group");
				node.put("cls", "posi");
				nodes.add(node);
			}
		}
		
		// 加载人员
		if(empVisible) {
			List<SysEmployee> subEmps = o_empolyeeBO.queryEmpsByOrgid(org.getId());
			for (SysEmployee subEmp : subEmps) {
				Map<String, Object> node = new HashMap<String, Object>();
				node.put("id", subEmp.getId() + "_" + RandomUtils.nextInt(9999));
				node.put("dbid", subEmp.getId());
				node.put("text", subEmp.getEmpname());
				node.put("leaf", true);
				// TODO 数据字典ID未修改
				node.put("iconCls", "0gender_m".equals(subEmp.getGender()) ? "icon-male" : "icon-female");
				node.put("cls", "emp");
				nodes.add(node);
			}
		}
		
		return nodes;
	}
	
	
	/**
	 * 
	 * 公司所属部门树查询
	 * 
	 * @author 胡迪新
	 * @param checkable 是否有复选框
	 * @param id 节点id
	 * @param subCompany 是否显示子公司
	 * @since  fhd　Ver 1.1
	 */
	/**
	 * <pre>
	 * deptTreeLoader:(这里用一句话描述这个方法的作用)
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param checkable
	 * @param id
	 * @param subCompany
	 * @param chooseId
	 * @param searchName 查询参数
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<Map<String, Object>> deptTreeLoader(Boolean checkable, String id,Boolean subCompany,String chooseId,String searchName,Boolean companyOnly){
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
		Criteria criteria = o_sysOrgDao.createCriteria();
		String[] orgType = null;
		
		criteria.add(Restrictions.eq("parentOrg.id", id));
		if(null != companyOnly && !companyOnly){
			 orgType = new String[]{"0orgtype_d","0orgtype_sd"};
		}else{
			 orgType = new String[]{"0orgtype_c","0orgtype_sc"};
			 criteria.add(Restrictions.in("orgType", orgType));
		}
		Set<String> idSet = queryOrgBySearchName(searchName,id,subCompany,orgType);
		if(StringUtils.isNotBlank(searchName)&&idSet.size()==0){
			return nodes;
		}
		
		if(null != subCompany && !subCompany){
			criteria.add(Restrictions.in("orgType", orgType));
		}
		criteria.addOrder(Order.desc("orgType"));
		List<SysOrganization> organizations = criteria.list();
		for (SysOrganization org : organizations) {
			if(!idSet.contains(org.getId())){
				continue;
			}
			Map<String, Object> node = new HashMap<String, Object>();
			node.put("id", org.getId());
			node.put("dbid", org.getId());
			node.put("text", org.getOrgname());
			node.put("code", org.getOrgcode());
			node.put("leaf", org.getIsLeaf());
			
			if("0orgtype_c".equals(org.getOrgType()) || "0orgtype_sc".equals(org.getOrgType())) {
				node.put("iconCls", "icon-org");
				node.put("leaf", org.getIsLeaf());
			} else{
				node.put("iconCls", "icon-orgsub");
			}
			if(checkable) {
				if(StringUtils.isNotEmpty(chooseId)){
					for (String choose : StringUtils.split(chooseId,",")) {
						if(org.getId().equals(choose)){
							node.put("checked", true);
							break;
						}
						node.put("checked", false);
					}
				}else{
					node.put("checked", false);
				}
			}
			node.put("cls", "org");
			nodes.add(node);
		}
		return nodes;
	}


	/**
	 * <pre>
	 * 公司树 查询
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param id 节点id
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	

	public List<Map<String, Object>> companyTreeLoader(String id) {
		
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
		
		Criteria criteria = o_sysOrgDao.createCriteria();
		criteria.add(Restrictions.eq("parentOrg.id", id));
		criteria.add(Restrictions.in("orgType", new String[]{"0orgtype_c","0orgtype_sc"}));
		
		List<SysOrganization> organizations = criteria.list();
		
		for (SysOrganization org : organizations) {
			Map<String, Object> node = new HashMap<String, Object>();
			node.put("id", org.getId());
			node.put("dbid", org.getId());
			node.put("text", org.getOrgname());
			node.put("code", org.getOrgcode());
			node.put("leaf", org.getIsLeaf());
			node.put("iconCls", "icon-org");
			node.put("cls", "org");
			nodes.add(node);
		}
		
		return nodes;
		
	}
	/**
	 * <pre>
	 * 模糊匹配机构名称
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param searchName query关键字
	 * @param parentId 父ID
	 * @param subCompany 是否包含子公司
	 * @param orgType 机构类型
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	protected Set<String> queryOrgBySearchName(String searchName, String parentId,Boolean subCompany, String[] orgType){
		List<SysOrganization> list = new ArrayList<SysOrganization>();
		Set<String> idSet = new HashSet<String>();
		Criteria criteria = o_sysOrgDao.createCriteria();
		criteria.setCacheable(true);
		if(StringUtils.isNotBlank(searchName)){
			criteria.add(Restrictions.like("orgname",searchName,MatchMode.ANYWHERE));
		}
		criteria.add(Restrictions.eq("parentOrg.id", parentId));
		if(null != subCompany && !subCompany){
			criteria.add(Restrictions.in("orgType", orgType));
		}
		list = criteria.list();
		for (SysOrganization org : list) {
			String[] idsTemp = org.getOrgseq().split("\\.");
			idSet.addAll(Arrays.asList(idsTemp));
		}
		return idSet;
	}
	
}

