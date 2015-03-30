/**
 * OrgEmpTreeBO.java
 * com.fhd.sys.business.organization
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-1-11 		黄晨曦
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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.dao.organization.SysPosiDAO;
import com.fhd.sys.dao.orgstructure.SysOrgDao;
import com.fhd.sys.entity.orgstructure.SysOrganization;
import com.fhd.sys.entity.orgstructure.SysPosition;
import com.fhd.sys.interfaces.IOrgEmpTreeBO;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * 机构，岗位，员工树查询
 * 
 * @author   王再冉
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-1-11		上午9:23:05
 *
 * @see 	 
 */
@Service
public class OrgEmpTreeBO implements IOrgEmpTreeBO{
	
	@Autowired
	private SysOrgDao o_sysorgDao;
	@Autowired
	private OrgGridBO o_orgGridBO;
	@Autowired
	private SysPosiDAO o_sysPosiDAO;

	
	/**
	 * 查询机构树 
	 * @author 
	 * @param searchName
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	 public Map<String, Object> findOrgTreeBySearchNameAndCompanyId(String searchName, String companyId) {
		 List<SysOrganization> list = new ArrayList<SysOrganization>();
			 list = findOrgTreeByCompanyId(companyId);
			if(null != searchName){
		 		List<SysOrganization> searchOrgs = findOrgTreeBySearchName(searchName);
		 		for(SysOrganization org : searchOrgs){
		 			list.add(org);
		 		}
		 	}
			return findOrgTreeBySome(list, null, searchName);
	    }
	 
	 /**
	  * 搜索树节点By SearchName
	  * @author 
	  * @param searchName
	  * @return
	  * @since  fhd　Ver 1.1
	  */
	 @SuppressWarnings("unchecked")
		public List<SysOrganization> findOrgTreeBySearchName(String searchName) {
			Criteria criteria = o_sysorgDao.createCriteria();
			criteria.setCacheable(true);
			criteria.add(Restrictions.like("orgname", searchName, MatchMode.ANYWHERE));
			return criteria.list();
	    }
	 
	 	/**
	 	 * 加载当前Company的树节点
	 	 * @author 
	 	 * @param companyId
	 	 * @return
	 	 * @since  fhd　Ver 1.1
	 	 */
	 	@SuppressWarnings("unchecked")
		public List<SysOrganization> findOrgTreeByCompanyId( String companyId) {
			Criteria criteria = o_sysorgDao.createCriteria();
			criteria.setCacheable(true);
			criteria.add(Restrictions.eq("company.id", companyId));
			//criteria.add(Restrictions.or(Restrictions.eq("company.id", companyId), Restrictions.isNull("parentOrg")));
			//修改
			//criteria.add(Restrictions.or(Restrictions.eq("parentOrg.id", companyId), Restrictions.isNull("parentOrg")));
			return criteria.list();
	    }
	 
	 /**
	  * 查询机构和下级机构树
	  * @author 
	  * @param orgList
	  * @param parentOrg
	  * @param searchName
	  * @return
	  * @since  fhd　Ver 1.1
	  */
	 	 public Map<String, Object> findOrgTreeBySome(List<SysOrganization> orgList, SysOrganization parentOrg, String searchName) {
				Map<String, Object> item = new HashMap<String, Object>();
				List<SysOrganization> subList = new ArrayList<SysOrganization>();
				Set<String> idSet = this.findOrgIdsBySearchName(searchName);
				if (null != orgList && orgList.size() > 0) {
				    for (SysOrganization org : orgList) {	
				    	if (null == parentOrg) {
				    		if(null == org.getParentOrg()){
				    			 if (idSet.contains(org.getId())){
						    		  subList.add(org);
						    	}
				    		}
						} else {
						    if (null != org.getParentOrg() && org.getParentOrg().getId().equals(parentOrg.getId())) {
						    	if (idSet.contains(org.getId())){
						    		  subList.add(org);
						    	}
						    }
						}
				    }
				}
				if (null != parentOrg) {
				    item.put("id", parentOrg.getId());
				    item.put("text", parentOrg.getOrgname());
				    if (subList.size() > 0) {
						item.put("expanded", true);
						item.put("leaf", false);
				    } else {
				    	item.put("leaf", true);
				    }
				}

				List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
				for (SysOrganization org : subList) {
				    Map<String, Object> subItem = new HashMap<String, Object>();
				    subItem.put("id", org.getId());
				    subItem.put("text", org.getOrgname());
				    Map<String, Object> posis = new HashMap<String, Object>();
				    if (null != org.getChildrenOrg() && org.getChildrenOrg().size() > 0) {
				    	subItem.put("leaf", false);
				    	if(!org.getSysPositions().isEmpty()){
				    		for(SysPosition posi : org.getSysPositions()){
				    			posis.put("id", posi.getId());
				    			posis.put("text", posi.getPosiname());
				    			posis.put("leaf", true);
				    		}
				    	}
				    	Map<String, Object> map = findOrgTreeBySome(new ArrayList<SysOrganization>(org.getChildrenOrg()),
								org, searchName);
							if (map != null) {
							    children.add(map);
							    if(children.size() > 0){
							    	children.add(posis);
							    }
							}
				    } else {
				    	if(null != org.getSysPositions()){
				    		subItem.put("leaf", false);
				    		for(SysPosition posi : org.getSysPositions()){
				    			posis.put("id", posi.getId());
				    			posis.put("text", posi.getPosiname());
				    			posis.put("leaf", true);
				    		}
				    	}else{
				    		subItem.put("leaf", true);
				    	}
				    	//subItem.put("leaf", true);
						children.add(subItem);
						children.add(posis);
				    }
				}
				if (children.size() > 0)
				    item.put("children", children);
				return item;
		 }
	 
	 /**
	  * 查询机构Id集合
	  * @author 
	  * @param searchName
	  * @return
	  * @since  fhd　Ver 1.1
	  */
	 @SuppressWarnings("unchecked")
		public Set<String> findOrgIdsBySearchName(String searchName) {
			List<SysOrganization> list = new ArrayList<SysOrganization>();
			Set<String> idSet = new HashSet<String>();
			String companyId = UserContext.getUser().getCompanyid();
			Criteria criteria = o_sysorgDao.createCriteria();
			criteria.setCacheable(true);
			if (StringUtils.isNotBlank(searchName)) {
				criteria.add(Restrictions.like("orgname", searchName, MatchMode.ANYWHERE));
			}
			if(null != companyId){
				criteria.add(Restrictions.eq("company.id", companyId));
			}
			//显示分公司
			//criteria.add(Restrictions.or(Restrictions.eq("parentOrg.id", companyId), Restrictions.eq("company.id", companyId)));
			list = criteria.list();
			for (SysOrganization org : list) {
				if(null != org.getOrgseq()){
					String[] idsTemp = org.getOrgseq().split("\\.");
				    idSet.addAll(Arrays.asList(idsTemp));
				}
			}
			//idSet.add(companyId);
			return idSet;
	    }
	 
	 /**
	  * 查询机构根节点,当前登录人员的公司为根节点
	  * @return
	  */
	 @SuppressWarnings("unchecked")
	 public SysOrganization findOrgTreeRoot(){
		 Criteria criteria = o_sysorgDao.createCriteria();
		 criteria.setCacheable(true);
		 String companyId = UserContext.getUser().getCompanyid();
		 if(null != companyId){
			 criteria.add(Restrictions.eq("id", companyId));
		 }else{
			 criteria.add(Restrictions.isNull("parentOrg"));//集团为根节点
		 }
		 List<SysOrganization> list = criteria.list();
		 return list.get(0);
	 }
	 
	 /**
	  * 查询机构节点
	  * @param id
	  * @param query
	  * @return
	  */
	 @SuppressWarnings("unchecked")
	public List<Map<String, Object>> treeLoader(String id,String query) {
			List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
			//String companyId = UserContext.getUser().getCompanyid();
			Set<String> idSet = this.findOrgIdsBySearchName(query);
			if(null!=query && idSet.isEmpty()){
				SysPosition queryPosi = findPosiByPosiName(query);
				if(null != queryPosi){
					String[] idsTemp = queryPosi.getSysOrganization().getOrgseq().split("\\.");
				    idSet.addAll(Arrays.asList(idsTemp));
					//idSet.add(queryPosi.getSysOrganization().getId());
				}
			}
			if (StringUtils.isNotBlank(id)) {
				List<SysOrganization> orgList = findOrgTreeByParentId(id);
				if(null != orgList){
					//查询机构
					for (SysOrganization sysOrg : orgList) {
						if(idSet.contains(sysOrg.getId())){
							Map<String, Object> item = new HashMap<String, Object>();
							item.put("id", sysOrg.getId());
							item.put("text", sysOrg.getOrgname());
							//判断机构下是否有岗位
							if(sysOrg.getSysPositions().size()>=1){
								item.put("leaf", false);
							}else{
								item.put("leaf", sysOrg.getIsLeaf());
							}
							//item.put("leaf", sysOrg.getIsLeaf());
							item.put("expanded", false);
							item.put("type", "jg");
							item.put("keys", sysOrg.getId()+"jg");
							nodes.add(item);
						}
					}
					SysOrganization org = o_orgGridBO.findOrganizationByOrgId(id);
					//查询岗位
					if(idSet.contains(org.getId())){
						Set<SysPosition> subPosis = org.getSysPositions();
						for (SysPosition subPosi : subPosis) {
							Map<String, Object> item = new HashMap<String, Object>();
							item.put("id", subPosi.getId());
							item.put("text", subPosi.getPosiname());
							item.put("leaf", true);
							item.put("iconCls", "icon-group");
							item.put("type", "gw");
							item.put("keys", subPosi.getId()+"gw");
							nodes.add(item);
						}
					}
				}
				
			}
			
			return nodes;
		}
	 /**
	  * 根据父id查询机构
	  * @param parentId
	  * @return
	  */
	 @SuppressWarnings("unchecked")
		public List<SysOrganization> findOrgTreeByParentId( String parentId) {
			Criteria criteria = o_sysorgDao.createCriteria();
			criteria.setCacheable(true);
			criteria.add(Restrictions.eq("parentOrg.id", parentId));
			return criteria.list();
	    }
	
	 /**
	  * 查询岗位名称
	  * @param query
	  * @return
	  */
	 @SuppressWarnings({ "unchecked" })
	 public SysPosition findPosiByPosiName(String query){
		 Criteria criteria = o_sysPosiDAO.createCriteria();
		 criteria.setCacheable(true);
		 criteria.add(Restrictions.eq("posiname", query));
		 List<SysPosition> list = criteria.list();
		// return list.get(0);
		 if (list.size() > 0) {
				return list.get(0);
			} else {
				return null;
			}
	 }
}









