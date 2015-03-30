/**
 * OrgGridBO.java
 * com.fhd.sys.business.organization
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-1-14 		黄晨曦
 *
 * Copyright (c) 2013, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.business.organization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.Page;
import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.dao.orgstructure.SysOrgDao;
import com.fhd.sys.entity.orgstructure.SysOrganization;
import com.fhd.sys.interfaces.IOrgGridBO;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * @author   王再冉
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-1-14		下午1:25:04
 *
 * @see 	 
 */
@Service
public class OrgGridBO implements IOrgGridBO{
	
	@Autowired
	private SysOrgDao o_sysOrgDao;
	
	/**
	 *	查询机构实体对象分页 
	 * @author 
	 * @param orgLevel	 机构层级
	 * @param page	coreDAOPage
	 * @param sort	排序字段
	 * @param dir	排序方式
	 * @param orgStatus	机构状态
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public Page<SysOrganization> findOrgPageBySome(String orgName, Page<SysOrganization> page, String sort, String dir, String orgId) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysOrganization.class);
		String companyId =  UserContext.getUser().getCompanyid();// 所在公司id
		if(null != companyId){
			dc.add(Restrictions.eq("company.id", companyId));
		}
		if(StringUtils.isNotBlank(orgName)){
			dc.add(Property.forName("orgname").like(orgName,MatchMode.ANYWHERE));	
		}
		if(StringUtils.isNotBlank(orgId)){
			if(null != companyId){
			    dc.add(Restrictions.and(Restrictions.or(Restrictions.eq("id", orgId),
			            Restrictions.eq("parentOrg.id", orgId)), Restrictions.eq("company.id", companyId)));
			}else{
				//dc.add(Restrictions.eq("id", orgId));
				 dc.add(Restrictions.or(Restrictions.eq("id", orgId),Restrictions.eq("parentOrg.id", orgId)));
			}
			
		}
		
		if("ASC".equalsIgnoreCase(dir)) {
			dc.addOrder(Order.asc(sort));
		} else {
			dc.addOrder(Order.desc(sort));
		}
		return o_sysOrgDao.findPage(dc, page, false);
	}
	
	/**
	 * 
	 * 根据机构id查询机构实体
	 * 
	 * @author 
	 * @param planId
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	public SysOrganization findOrganizationByOrgId(String orgId) {
		Criteria c = o_sysOrgDao.createCriteria();
		List<SysOrganization> list = null;
		
		if (StringUtils.isNotBlank(orgId)) {
			c.add(Restrictions.eq("id", orgId));
		} else {
			return null;
		}
		
		list = c.list();
			return list.get(0);
	}
	
	/**
	 * 根据id查询机构集合
	 * @author 
	 * @param orgId
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	public List<SysOrganization> findOrganizationByOrgIds(List<String> orgIds) {
		Criteria c = o_sysOrgDao.createCriteria();
		List<SysOrganization> list = null;
		
		if (orgIds.size()>0) {
			c.add(Restrictions.in("id", orgIds));
		} else {
			return null;
		}
		
		list = c.list();
		if (list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}
	/**
	 * 根据id查询机构及其下级机构的集合
	 * @param orgId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SysOrganization> findOrgsAndChildOrgsByOrgIds(String orgId) {
		Criteria c = o_sysOrgDao.createCriteria();
		c.setCacheable(true);
		List<SysOrganization> list = null;
		
		if (StringUtils.isNotBlank(orgId)) {
			//c.add(Restrictions.eq("id", orgId));
			c.add(Restrictions.or(Restrictions.eq("id", orgId),Restrictions.eq("parentOrg.id", orgId)));
		}
		list = c.list();
		if (list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}
	
	/**
	 * 根据公司id查询机构
	 * @author 
	 * @param companyId
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	public List<SysOrganization> findOrganizationByCompanyIds(String companyId) {
		Criteria c = o_sysOrgDao.createCriteria();
		c.setCacheable(true);
		List<SysOrganization> list = null;
		if (StringUtils.isNotBlank(companyId)) {
			c.add(Restrictions.eq("company.id", companyId));
		} else {
			return null;
		}
		list = c.list();
		if (list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}
	
	/**
	 * 查询所有机构
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SysOrganization> findAllOrganizations() {
		Criteria c = o_sysOrgDao.createCriteria();
		c.setCacheable(true);
		List<SysOrganization> list = null;
		list = c.list();
		if (list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}
	
	/**
	 * 查询公司所有机构的id集合
	 * @param companyId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Set<String> findOrgIdsByCompanyId(String companyId) {
		List<SysOrganization> list = new ArrayList<SysOrganization>();
		Set<String> idSet = new HashSet<String>();
		Criteria c = o_sysOrgDao.createCriteria();
		if (StringUtils.isNotBlank(companyId)) {
			c.add(Restrictions.eq("company.id", companyId));
		} else {
			return null;
		}
		list = c.list();
		for (SysOrganization org : list) {
		    String[] idsTemp = org.getOrgseq().split("\\.");
		    idSet.addAll(Arrays.asList(idsTemp));
		}
		return idSet;
	}
	
	 
	/**
	 * 查询下级机构
	 * @param orgId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SysOrganization> findChildOrgByOrgId(String orgId) {
		Criteria c = o_sysOrgDao.createCriteria();
		c.setCacheable(true);
		if (StringUtils.isNotBlank(orgId)) {
			c.add(Restrictions.eq("company.id", orgId));
		} 
		return c.list();
				
	}
		
	
	/**
	 * 根据机构名称查询机构实体
	 * @author 
	 * @param orgName 机构名称
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	public SysOrganization findOrganizationByorgName(String orgName) {
		Criteria c = o_sysOrgDao.createCriteria();
		List<SysOrganization> list = null;
		
		if (StringUtils.isNotBlank(orgName)) {
			c.add(Restrictions.eq("orgname", orgName));
		}
		
		list = c.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 删除机构
	 * @author 
	 * @param ids
	 * @since  fhd　Ver 1.1
	 */
	 @Transactional
	 public void removeOrganizationByIds(List<String> ids) {
		//String[] idArray = ids.split(",");
		for (String id : ids) {
			SysOrganization orgEntry = findOrganizationByOrgId(id);
			if(orgEntry.getParentOrg().getChildrenOrg().size()<=1){//如果父机构不存在子集，设置叶子节点为true
				orgEntry.getParentOrg().setIsLeaf(true);
				mergeOrganization(orgEntry.getParentOrg());
			}
			removeOrganizationBySome(orgEntry, orgEntry.getChildrenOrg());
		}
	}	
	 
	 /**
	  * 从数据库删除选中机构及下级机构
	  * @author 
	  * @param orgEntry
	  * @param orgChildeSnet
	  * @since  fhd　Ver 1.1
	  */
	 public void removeOrganizationBySome(SysOrganization orgEntry, Set<SysOrganization> orgChildeSnet) {
			if (orgChildeSnet != null) {
			    for (SysOrganization children: orgChildeSnet) {
					if (children.getChildrenOrg() != null
						&& children.getChildrenOrg().size() > 0) {
						removeOrganizationBySome(children, children.getChildrenOrg());
					} else {
						removeOrganizationBySome(children, null);
					}
			    }
			}
			if (orgEntry != null) {
			    //orgEntry.setOrgStatus("0");
			    //o_sysOrgDao.merge(orgEntry);
				o_sysOrgDao.delete(orgEntry);
			}
	    }
	
	/**
	 * 保存机构实体对象
	 * @author 
	 * @param org
	 * @since  fhd　Ver 1.1
	 */
	 @Transactional
		public void saveOrganization(SysOrganization org) {
			o_sysOrgDao.merge(org);
		}
	
	 /**
	  * 更新机构对象
	  * 
	  * @author 
	  * @param org
	  * @since  fhd　Ver 1.1
	  */
	 @Transactional
		public void mergeOrganization(SysOrganization org) {
			o_sysOrgDao.merge(org);
		}
	 
	 /**
	  * 自动生成机构编号
	  * @param orgID
	  * @param orgParentId
	  * @return
	  */
	@SuppressWarnings("unchecked")
	public Map<String, Object> findOrgCode(String orgID,String orgParentId){
			Map<String, Object> orgMap = new HashMap<String, Object>();
			SysOrganization parentOrg = new SysOrganization();
			
			List<String> list=o_sysOrgDao.find("Select max(orgcode) from SysOrganization");
			String maxorgid=(String) list.get(0);
			String[] maxorgidnum=maxorgid.split("-");
			int i= Integer.parseInt(maxorgidnum[1]);
			String orgcode=new String();
			if(i<10){
				int newOrgCoderNumber=i+1;
				orgcode="0"+Integer.toString(newOrgCoderNumber);
			}else{
				orgcode=Integer.toString(i+1);
			}
			orgMap.put("orgcode", "FHD-"+orgcode);
			
			Criteria criteria = o_sysOrgDao.createCriteria();
			criteria.add(Restrictions.eq("id", orgParentId));
			List<SysOrganization> orglist=criteria.list();
			if(orglist.size()>0){
				parentOrg=orglist.get(0);
				//orgMap.put("parentOrg.orgname", parentOrg.getOrgname());
			}
			
			Map<String, Object> node=new HashMap<String, Object>();
			//orgMap.put("orgcode", "FHD-TEST");
			node.put("data", orgMap);
			node.put("success", true);
			return node;
		}
	/**
	 * <pre>
	 * 根据公司ID获得其子公司列表
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param companyId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@SuppressWarnings("unchecked")
	public List<SysOrganization> findSubCompanyList(String companyId){
		Criteria criteria = o_sysOrgDao.createCriteria();
		criteria.add(Restrictions.eq("parentOrg.id", UserContext.getUser().getCompanyid()));
		criteria.add(Restrictions.in("orgType", new String[]{"0orgtype_c","0orgtype_sc"}));
		return criteria.list();
	}

}

