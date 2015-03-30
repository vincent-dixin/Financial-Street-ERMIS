/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */


package com.fhd.sys.business.autho;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.sys.dao.autho.SysoAuthorityDAO;
import com.fhd.sys.dao.autho.SysoRoleDAO;
import com.fhd.sys.dao.autho.SysoUserDAO;
import com.fhd.sys.entity.auth.SysAuthority;
import com.fhd.sys.entity.auth.SysRole;
import com.fhd.sys.entity.auth.SysUser;

import edu.emory.mathcs.backport.java.util.Arrays;

@Service
public class SysoAuthorityBO {
	@Autowired
	private SysoAuthorityDAO o_sysoAuthorityDAO;
	@Autowired
	private SysoRoleDAO o_sysoRoleDAO;
	@Autowired
	private SysoUserDAO o_sysoUserDAO;
	/**
	 * 权限树(功能菜单树)
	 * 翟辉
	 * @param canChecked
	 * 是否有复选框
	 * @param node
	 * 节点id
	 * @param query
	 * 查询条件
	 * @return
	 * @since fhd　Ver 1.1
	 */
	public List<Map<String, Object>> treeLoader(String node,String query, String id,String authorityType) {
		
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
		if(StringUtils.isNotBlank(id)){
		Map<String, SysAuthority> allSysAuthorityItem = new HashMap<String, SysAuthority>();
		List<SysAuthority> allSysAuthority = allSysAuthority();
		for(SysAuthority allSysAuthorityList : allSysAuthority){
			allSysAuthorityItem.put(allSysAuthorityList.getId(), allSysAuthorityList);
		}
		//if(StringUtils.isNotBlank(roleId)){
			List<String> authorityIds = this.queryAuthorityByRole(id,authorityType);
			if(StringUtils.isNotBlank(id)){
				//判断节点有子节点就删除，没有子节点就留下
				 for(int i=authorityIds.size()-1;i>=0;i--){
					List<SysAuthority> authorityList = findChildrenByParent(authorityIds.get(i),allSysAuthorityItem);
					 if(authorityList.size() > 0){
						 authorityIds.remove(i);
					 }
				 }	
			}
			if(query == null){
				if (StringUtils.isNotBlank(node)) {
					List<SysAuthority> auList = findBySome(node,id,allSysAuthorityItem,authorityType);//得到node的所有子id
					for(int j=0;j<auList.size();j++){
						boolean checked = false;
						SysAuthority sysAuthority = auList.get(j);
						Map<String, Object> item = new HashMap<String, Object>();
						if(authorityIds!=null){	
							for(int i=0;i<authorityIds.size();i++){
								if(authorityIds.get(i) == sysAuthority.getId()){
									checked=true;//不显示该节点、如果父节点的所有子节点checked=true;父节点不显示
									break;
								}
							}
						}
						if(!checked){
							item.put("id", sysAuthority.getId());
							item.put("text", sysAuthority.getAuthorityName());
							item.put("leaf", sysAuthority.getIsLeaf());
							item.put("checked", checked);
							item.put("expanded", false);
							nodes.add(item);	
						}
					}
				}	
			}else{
				if (StringUtils.isNotBlank(node)) {
					Set<String> idSet = this.findRoleIdsBySearchName(query);
					//List<SysAuthority> roleList = findRoleTreeByParentId(node);
					List<SysAuthority> roleList = findBySome(node,id,allSysAuthorityItem,authorityType);
					for (SysAuthority sysAuthority : roleList) {
						boolean checked = false;
						for(int i=0;i<authorityIds.size();i++){		
							if(authorityIds.get(i) == sysAuthority.getId()){
								checked=true;
							}
						}
						if(idSet.contains(sysAuthority.getId())){
							if(!checked){
								Map<String, Object> item = new HashMap<String, Object>();
								item.put("id", sysAuthority.getId());
								item.put("text", sysAuthority.getAuthorityName());
								item.put("leaf", sysAuthority.getIsLeaf());
								item.put("checked", checked);
								item.put("expanded", true);
								nodes.add(item);
							}
						}
					}
				}
			}
		}
		return nodes;
	}
	
	 /**
	  * 得到所有权限实体
	  * @return
	  */
	 @SuppressWarnings("unchecked")
		public List<SysAuthority> allSysAuthority() {
			Criteria criteria = o_sysoAuthorityDAO.createCriteria();
			return criteria.list();
	 }

	/**
	 * 查找子节点Map形式
	 * @param parentId
	 * @return
	 */
		private List<SysAuthority> findChildrenByParent(String parentId,Map<String, SysAuthority> allSysAuthorityItem) {
			List<SysAuthority> authorityList = new ArrayList<SysAuthority>();
			for (String key : allSysAuthorityItem.keySet()) {
				SysAuthority sysAuthority = allSysAuthorityItem.get(key).getParentAuthority();
				try{
					if(sysAuthority==null){
						continue;
					}
					if(sysAuthority.getId().equals(parentId)){
						authorityList.add(allSysAuthorityItem.get(key));
					}
				}
				catch (ObjectNotFoundException e) {
					System.out.println(e.toString());
				}
			}
			return authorityList;
		}
	/**
	  * 查询角色Id集合
	  * @author 
	  * @param searchName
	  * @return
	  * @since  fhd　Ver 1.1
	  */
	 @SuppressWarnings("unchecked")
		public Set<String> findRoleIdsBySearchName(String searchName) {
			List<SysAuthority> list = new ArrayList<SysAuthority>();
			Set<String> idSet = new HashSet<String>();
			
			Criteria criteria = o_sysoAuthorityDAO.createCriteria();
			criteria.setCacheable(true);
			if (StringUtils.isNotBlank(searchName)) {
				criteria.add(Restrictions.like("authorityName", searchName, MatchMode.ANYWHERE));
			}
			list = criteria.list();
			for (SysAuthority autho : list) {
			    String[] idsRole = autho.getSeqNo().split("\\.");
			    idSet.addAll(Arrays.asList(idsRole));
			}
			//idSet.add(companyId);
			return idSet;
	    }
	 /**
	  * 查找父亲节点
	  * @param parentId
	  * @return
	  */
	 @SuppressWarnings("unchecked")
		public List<SysAuthority> findRoleTreeByParentId( String parentId) {
			Criteria criteria = o_sysoAuthorityDAO.createCriteria();
			criteria.setCacheable(true);
			criteria.add(Restrictions.eq("parentAuthority.id", parentId));
			return criteria.list();
	 }

	/**
	 * 权限树(显示树)
	 * 翟辉
	 * @param canChecked
	 * 是否有复选框
	 * @param node
	 * 节点id
	 * @param query
	 * 查询条件
	 * @return
	 * @since fhd　Ver 1.1
	 */
	public List<Map<String, Object>> treeLoaderShow(String node,String id,String query,String authorityType) {
		List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
		if(StringUtils.isNotBlank(id)){
		Map<String, SysAuthority> allSysAuthorityItem = new HashMap<String, SysAuthority>();
		List<SysAuthority> allSysAuthority = allSysAuthority();
		for(SysAuthority allSysAuthorityList : allSysAuthority){
			allSysAuthorityItem.put(allSysAuthorityList.getId(), allSysAuthorityList);
		}
		List<String> authorityIds = this.queryAuthorityByRole(id,authorityType);
		
		
		if(query == null){
			if (StringUtils.isNotBlank(node)) {
				List<SysAuthority> auList = findChildrenByParent(node,allSysAuthorityItem);
				for(int j=0;j<auList.size();j++){
					for(int i=0;i<authorityIds.size();i++){
						SysAuthority sysAuthority = auList.get(j);
						if(authorityIds.get(i) == sysAuthority.getId()){
							Map<String, Object> item = new HashMap<String, Object>();
							item.put("id", sysAuthority.getId());
							item.put("text", sysAuthority.getAuthorityName());
							item.put("leaf", sysAuthority.getIsLeaf());
							item.put("checked", false);
							item.put("expanded", false);
							nodes.add(item);
						}
					}
				}
			}
		}else{
			if (StringUtils.isNotBlank(node)) {
				Set<String> idSet = this.findRoleIdsBySearchName(query);
				//List<SysAuthority> roleList = findRoleTreeByParentId(node);
				List<SysAuthority> roleList = findChildrenByParent(node,allSysAuthorityItem);
				for (SysAuthority sysAuthority : roleList) {
					boolean checked = true;
					for(int i=0;i<authorityIds.size();i++){		
						if(authorityIds.get(i) == sysAuthority.getId()){
							checked=false;
						}
					}
					if(idSet.contains(sysAuthority.getId())){
						if(!checked){	
							Map<String, Object> item = new HashMap<String, Object>();
							item.put("id", sysAuthority.getId());
							item.put("text", sysAuthority.getAuthorityName());
							item.put("leaf", sysAuthority.getIsLeaf());
							item.put("checked", checked);
							item.put("expanded", true);
							nodes.add(item);
						}
					}
				}
				
			}
		}
		}
		return nodes;
	}
	
	/**
	 * 过滤父节点(其子节点的数量与子节点带权限的数量相等);
	 * 翟辉
	 * @param parentId
	 * @return
	 */
	private List<SysAuthority> findBySome(String parentId,String roleId,Map<String, SysAuthority> allSysAuthorityItem,String authorityType) {
		/*Criteria criteria = o_sysoAuthorityDAO.createCriteria();
		if (StringUtils.isNotBlank(parentId)) {
			criteria.add(Restrictions.eq("parentAuthority.id", parentId));
		}
		criteria.addOrder(Order.asc("authorityName"));*/
		List<SysAuthority> listSysAuthorityReturn = findChildrenByParent(parentId,allSysAuthorityItem);
		//List<SysAuthority> listSysAuthorityReturn = criteria.list();//子id集合
		for(int j=listSysAuthorityReturn.size()-1;j>=0;j--){
			List<SysAuthority> listSysAuthoritys = new ArrayList<SysAuthority>();
			List<SysAuthority> listSysAuthority =new ArrayList<SysAuthority>();
			listSysAuthority = childrenIdsfindByParentId(listSysAuthorityReturn.get(j).getId(),listSysAuthoritys);//得到传入parentId的所有子id
			if(queryParentIdIsShow(listSysAuthority,roleId,authorityType)){
				listSysAuthorityReturn.remove(j);
			}
		}
		return listSysAuthorityReturn;
	}
	
	/**
 	 * 查找父亲节点是否显示
 	 * 翟辉
 	 * @param auList
 	 * @param id
 	 * @param roleId
 	 * @return
 	 */
	
	public boolean queryParentIdIsShow(List<SysAuthority> listSysAuthority,String roleId,String authorityType) {
		if(listSysAuthority.size()==0){
			return false;
		}
		List<String> authorityIds = this.queryAuthorityByRole(roleId,authorityType);//得到所有权限
		int sum = 0;
		for(int i=0;i<listSysAuthority.size();i++){
			for(int j=0;j<authorityIds.size();j++){
				if(listSysAuthority.get(i).getId()==authorityIds.get(j)){
					sum++;
				}
			}
		}
		if(sum == listSysAuthority.size()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 多条件查询查询 findBySome:通过Id查询子节点
	 * 翟辉
	 * @param ids
	 * 目标id
	 * @param parentAuthority
	 *            父节点id
	 * @return
	 * @since fhd　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	private List<SysAuthority> findBySome(String parentId) {
		Criteria criteria = o_sysoAuthorityDAO.createCriteria();
		if (StringUtils.isNotBlank(parentId)) {
			criteria.add(Restrictions.eq("parentAuthority.id", parentId));
		}
		criteria.addOrder(Order.asc("authorityName"));
		return criteria.list();
	}
	
	/**
	 * 多条件查询查询 findBySome:通过Id查询子节点
	 * 翟辉
	 * @param ids
	 *            目标id
	 * @param parentAuthority
	 *            父节点id
	 * @return
	 * @since fhd　Ver 1.1
	 */
	private List<SysAuthority> childrenIdsfindByParentId(String parentId,List<SysAuthority> sysAuthorityAll) {
			List<SysAuthority> authorityList = findBySome(parentId);
			sysAuthorityAll.addAll(authorityList);
			for(int i =0;i<authorityList.size();i++){//遍历根节点的第一层子节点
				SysAuthority sysAuthority = authorityList.get(i);
					if(sysAuthority.getIsLeaf()){//如果是子叶节点
							continue;
					}else{
						childrenIdsfindByParentId(sysAuthority.getId(),sysAuthorityAll);
					}
			}
		return sysAuthorityAll;
	}

	/**
	 * 翟辉
	 * 查询权限树的根节点
	 */
	@SuppressWarnings("unchecked")
	public SysAuthority findAuthorityTreeRoot() {
		SysAuthority sysAuthority = new SysAuthority();
		Criteria criteria = o_sysoAuthorityDAO.createCriteria();
		criteria.add(Restrictions.isNull("parentAuthority"));
		criteria.add(Restrictions.eq("rank", 1));
		List<SysAuthority> sysAuthorityList = criteria.list();
		if (null != sysAuthorityList && sysAuthorityList.size() > 0) {
			sysAuthority = sysAuthorityList.get(0);
		}
		return sysAuthority;
	} 
	
	/**
	 * 查询角色当前拥有的权限.
	 * @author 翟辉
	 * @param roleid 角色id.
	 * @return List<String> 选中的权限集合.
	 * @since fhd　Ver 1.1
	 */
	public List<String> queryAuthorityByRole(String id,String authorityType) {
		List<String> authorityIds = new ArrayList<String>();
		//得到角色权限
		if(authorityType.equals("role")){
			if(StringUtils.isNotBlank(id)){
				SysRole sysRole = o_sysoRoleDAO.get(id);
				for (SysAuthority authority : sysRole.getSysAuthorities()) {
					authorityIds.add(authority.getId());
				}
			}
		}
		 //得到用户权限
		if(authorityType.equals("user")){
			if(StringUtils.isNotBlank(id)){
				SysUser sysUser = o_sysoUserDAO.get(id);
				for (SysAuthority authority : sysUser.getSysAuthorities()) {
					authorityIds.add(authority.getId());
				}
			}
		}
		return authorityIds;
	}
	
	
	
	
	

	/**
	 * 根据id查询功能权限
	 * @author 翟辉
	 * @param id
	 * @return SysAuthority 功能权限.
	 * @since  fhd　Ver 1.1
	 */
	public SysAuthority queryAuthorityById(String id){
		return o_sysoAuthorityDAO.get(id);
	}

	/**
	 * 删除权限时,是否携带根节点
	 * @author 翟辉
	 * @param authorityId
	 * @param roleId
	 * @return
	 */
	public boolean queryAuthorityIdIsTure(String authorityId,String roleId,JSONArray jsarr,String authorityType){
		SysAuthority sysAuthority = this.queryAuthorityById(authorityId);
		if(sysAuthority.getIsLeaf()){//叶子节点不删除
			return true;
		}
		List<String> authorityIdChildren = new ArrayList<String>();//authorityId的所有带权限的子节点集合
		List<String> authorityIds = this.queryAuthorityByRole(roleId,authorityType);//得到所有权限
		for(int i=0;i<authorityIds.size();i++){
			List<SysAuthority> sysAuthorityAll = new ArrayList<SysAuthority>();
			List<SysAuthority> listSysAuthority = childrenIdsfindByParentId(authorityId,sysAuthorityAll);//authorityId的所有子节点集合
			for(int j=0;j<listSysAuthority.size();j++){
				if(authorityIds.get(i)==listSysAuthority.get(j).getId()){
					authorityIdChildren.add(authorityIds.get(i));
				}
			}
		}
		int sum = 0;
		for (int i = 0; i < jsarr.size(); i++){
			JSONObject jsobj = jsarr.getJSONObject(i);
			String  jsarrAuthorityId = jsobj.getString("checkId");
			for(int j=0;j<authorityIdChildren.size();j++){
				if(jsarrAuthorityId.equals(authorityIdChildren.get(j))){
					sum++;
				}
			}
		}
		if(authorityIdChildren.size()==sum){
			return true;//删除
		}else{
			return false;
		}
	}
}

