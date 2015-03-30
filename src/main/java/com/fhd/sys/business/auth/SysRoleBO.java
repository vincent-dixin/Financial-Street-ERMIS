/**
 * SysRoleBO.java
 * com.fhd.fdc.commons.business.sys.auth
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-30 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
 */

package com.fhd.sys.business.auth;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.support.Page;
import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.business.log.BusinessLogBO;
import com.fhd.sys.dao.auth.SysRoleDAOold;
import com.fhd.sys.dao.auth.SysUserDAO;
import com.fhd.sys.dao.orgstructure.SysEmployeeDAOold;
import com.fhd.sys.entity.auth.SysAuthority;
import com.fhd.sys.entity.auth.SysRole;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * 角色BO类.
 * 
 * @author wudefu
 * @version V1.0 创建时间：2010-8-30 Company FirstHuiDa.
 */

@Service
@SuppressWarnings({"unchecked","deprecation"})
public class SysRoleBO {

	@Autowired
	private SysRoleDAOold o_sysRoleDAO;
	@Autowired
	private BusinessLogBO o_businessLogBO;
	@Autowired
	private SysUserDAO o_sysUserDAO;
	@Autowired
	private SysEmployeeDAOold employeeDAO;

	/**
	 * 
	 * <pre>
	 * 根据公司ID和角色名称 查询员工
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param companyId 公司id
	 * @param roleName 角色名称
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<SysEmployee> getEmpByCorpAndRole(String companyId,String roleName) {
		Criteria criteria = employeeDAO.createCriteria();
		criteria.createAlias("sysUser", "u");
		criteria.createAlias("u.sysRoles", "r");
		criteria.add(Restrictions.eq("r.roleName", roleName));
		criteria.add(Restrictions.eq("sysOrganization.id", companyId));
		return (List<SysEmployee>) criteria.list();
	}
	
	/**
	 * 
	 * <pre>
	 * 根据所在公司和角色名称查询员工
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param roleName 角色名称
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<SysEmployee> getEmpByCorpAndRole(String roleName) {
		return getEmpByCorpAndRole(UserContext.getUser().getCompanyid(), roleName);
	}
	
	
	public Set<SysUser> getSysUserByRoleName(String roleName) {
		SysRole sysRole = o_sysRoleDAO.findUniqueBy("roleName", roleName);
		if(sysRole==null)
			return null;
		return sysRole.getSysUsers();
	}
	
	/**
	 * 新增角色.
	 * @author 吴德福
	 * @param sysRole
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void saveRole(SysRole sysRole) {
		try {
			o_sysRoleDAO.save(sysRole);
			o_businessLogBO.saveBusinessLogInterface("新增", "角色", "成功", sysRole.getRoleCode(), sysRole.getRoleName());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.saveBusinessLogInterface("新增", "角色", "失败", sysRole.getRoleCode(), sysRole.getRoleName());
		}
	}
	/**
	 * 修改角色.
	 * @author 吴德福
	 * @param sysRole
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void updateRole(SysRole sysRole) {
		try {
			o_sysRoleDAO.merge(sysRole);
			o_businessLogBO.modBusinessLogInterface("修改", "角色", "成功", sysRole.getId());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.modBusinessLogInterface("修改", "角色", "失败", sysRole.getId());
		}
	}
	/**
	 * 删除角色.
	 * @author 吴德福
	 * @param id
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void removeRole(String id) {
		try {
			o_sysRoleDAO.removeById(id);
			o_businessLogBO.delBusinessLogInterface("删除", "角色", "成功", id);
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.delBusinessLogInterface("删除", "角色", "失败", id);
		}
	}
	/**
	 * 根据id查询角色.
	 * @author 吴德福
	 * @param id
	 * @return SysRole
	 * @since fhd　Ver 1.1
	 */
	public SysRole queryRoleById(String id) {
		return o_sysRoleDAO.get(id);
	}
	/**
	 * 查询所有角色.
	 * @author 吴德福
	 * @return List<SysRole>
	 * @since fhd　Ver 1.1
	 */
	public List<SysRole> queryAllRole() {
		return o_sysRoleDAO.getAll();
	}
	/**
	 * 根据查询条件查询角色.
	 * @author 吴德福
	 * @param filters 页面查询条件传递的参数集合.
	 * @return List<SysRole> 模块集合.
	 * @since fhd　Ver 1.1
	 */
	public List<SysRole> query(String roleCode, String roleName) {
		Criteria criteria = o_sysRoleDAO.createCriteria();
		criteria.setFetchMode("sysUsers", FetchMode.SELECT);
		criteria.setFetchMode("sysAuthorities", FetchMode.SELECT);
		criteria.add(Restrictions.like("roleCode", roleCode, MatchMode.ANYWHERE));
		criteria.add(Restrictions.like("roleName", roleName, MatchMode.ANYWHERE));
		List<SysRole> list = criteria.list();
		return list;
	}
	/**
	 * 查询角色当前拥有的权限.
	 * @author 吴德福
	 * @param roleid 角色id.
	 * @return List<String> 选中的权限集合.
	 * @since fhd　Ver 1.1
	 */
	public List<String> queryAuthorityByRole(String roleid) {
		List<String> authorityIds = new ArrayList<String>();
		SysRole sysRole = o_sysRoleDAO.get(roleid);
		for (SysAuthority authority : sysRole.getSysAuthorities()) {
			authorityIds.add(authority.getId());
		}
		return authorityIds;
	}
	/**
	 * 获取未选中的角色信息.
	 * @param selects
	 * @return List<SysRole>
	 * @return List<SysRole>
	 */
	public List<SysRole> getUnselectedRole(String selects) {
		List<String> ids = new ArrayList<String>();
		if (selects != null) {
			String[] idarr = selects.split(",");
			for (String id : idarr) {
				ids.add(id);
			}
		}
		DetachedCriteria dc = DetachedCriteria.forClass(SysRole.class);
		if(ids.size() > 0)
			dc.add(Restrictions.not(Restrictions.in("id", ids)));
		return this.o_sysRoleDAO.findByCriteria(dc);
	}
	/**
	 * 分页Role.
	 * @author 万业
	 * @param page
	 * @param role
	 * @return Page<SysRole>
	 */
	public Page<SysRole> querySysRoleByPage(Page<SysRole> page, SysRole role,String sort,String dir){
		DetachedCriteria dc=DetachedCriteria.forClass(SysRole.class);
		if(StringUtils.isNotBlank(role.getRoleCode())){
			dc.add(Restrictions.like("roleCode", role.getRoleCode(), MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(role.getRoleName())){
			dc.add(Restrictions.like("roleName", role.getRoleName(), MatchMode.ANYWHERE));
		}
		if("ASC".equalsIgnoreCase(dir)) {
			if(!"id".equals(sort)){
				dc.addOrder(Order.asc(sort));
			}else{
				dc.addOrder(Order.asc("roleCode"));
			}
		} else {
			if(!"id".equals(sort)){
				dc.addOrder(Order.desc(sort));
			}else{
				dc.addOrder(Order.asc("roleCode"));
			}
		}
		return o_sysRoleDAO.pagedQuery(dc,page);
		
	}
	/**
	 * 新增角色
	 * @author 万业
	 * @param role
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void merge(SysRole role){
		try {
			o_sysRoleDAO.merge(role);
//			SysOrganization organization = position.getSysOrganization();
//			organization.setIsLeaf(false);
//			o_organizationBO.merge(organization);
//			o_sysPositionDAO.merge(position);
			o_businessLogBO.saveBusinessLogInterface("新增", "角色", "成功", role.getId());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.saveBusinessLogInterface("新增", "角色", "失败", role.getId());
		}
	}
	/**
	 * 为角色添加用户.
	 * @author 万业
	 * @param roleId 角色
	 * @param selectIds 已选ID
	 * @param allIds 所有ID
	 */
	@Transactional
	public void addBatchUserRole(String roleId,String[] selectIds,String[] allIds){
		if(allIds.length>0){
			
			SysRole role=o_sysRoleDAO.get(roleId);
			String hql = "select u from SysUser u where u.id in(:allIds)";
			Query query = o_sysUserDAO.getSessionFactory().getCurrentSession().createQuery(hql);
			query.setParameterList("allIds", allIds);
			List<SysUser> list = query.list();
			for (Iterator<SysUser> iterator = list.iterator(); iterator.hasNext();) {
				SysUser sysUser = iterator.next();
				//role.getSysUsers().remove(sysUser);
				sysUser.getSysRoles().remove(role);
				o_sysUserDAO.update(sysUser);//关系只交给user方来维护
			}
			//o_sysRoleDAO.update(role);//清空关系
			
			if(selectIds.length>0){
				for(String userId:selectIds){
					SysUser sysUser=this.o_sysUserDAO.get(userId);
					role.getSysUsers().add(sysUser);
					sysUser.getSysRoles().add(role);
					o_sysUserDAO.update(sysUser);
				}
			}
		}else{
			return;
		}
		
	}
	/**
	 * 为角色添加用户.
	 * @author 万业
	 * @param roleId 角色
	 * @param selectIds 已选ID employee id
	 */
	@Transactional
	public void addBatchUserRole(String roleId,String[] selectIds){
			
		SysRole role=o_sysRoleDAO.get(roleId);
		//清空当前所有关系
		for(SysUser sysUser:role.getSysUsers()){
			sysUser.getSysRoles().remove(role);
			o_sysUserDAO.update(sysUser);//关系只交给user方来维护
		}
		if(null==selectIds || selectIds.length==0){
			return;
		}
		//设置新关系
		/*
		for(String empId:selectIds){
			String userId=this.o_sysEmployeeDAO.get(empId).getUserid();
			if(StringUtils.isNotBlank(userId)){
				
				SysUser sysUser=this.o_sysUserDAO.get(userId);
				role.getSysUsers().add(sysUser);
				sysUser.getSysRoles().add(role);
				o_sysUserDAO.update(sysUser);
				
			}
		}*/
		for(String userId:selectIds){
			if(StringUtils.isNotBlank(userId)){
				
				SysUser sysUser=this.o_sysUserDAO.get(userId);
				role.getSysUsers().add(sysUser);
				sysUser.getSysRoles().add(role);
				o_sysUserDAO.update(sysUser);
				
			}
		}
	}
	/**
	 * 获取与roleid相关的employee的 ID
	 * @author 万业
	 * @param roleId
	 * @return
	 */
	public String getRoleEmpIds(String roleId){
		SysRole role = this.o_sysRoleDAO.get(roleId);
		Set<SysUser> users = role.getSysUsers();
		List<String> userIds=new ArrayList<String>();
		for(SysUser user:users){
			userIds.add(user.getId());
		}
		if(userIds.size() == 0){
			return"";
		}
		String hql = "select emp.id from SysEmployee emp where emp.userid in (:ids)";
		Query query= o_sysRoleDAO.getSessionFactory().getCurrentSession().createQuery(hql);
		query.setParameterList("ids", userIds);
		
		List<String> list = query.list();
		StringBuilder sb=new StringBuilder();
		for(String id:list){
			sb.append(id);
			sb.append(",");
		}
		return sb.toString();
	}
	/**
	 * 获取与roleid相关的sysuser的 ID
	 * @author 万业
	 * @param roleId
	 * @return
	 */
	public String getRoleUserIds(String roleId){
		SysRole role = this.o_sysRoleDAO.get(roleId);
		Set<SysUser> users = role.getSysUsers();
		StringBuilder sb=new StringBuilder();
		for(SysUser user:users){
			sb.append(user.getId());
			sb.append(",");
		}
		return sb.toString();
	}
	/**
	 * 查询角色id为roleId下的所有人员列表.
	 * @author 吴德福
	 * @param roleId
	 * @return Set<SysUser>
	 * @since  fhd　Ver 1.1
	 */
	public Set<SysUser> queryUsersByRoleId(String roleId){
		if(StringUtils.isNotBlank(roleId)){
			SysRole role = o_sysRoleDAO.get(roleId);
			return role.getSysUsers();
		}
		return null;
	}
	/**
	 * 根据角色编号判断角色是否存在.
	 * @author 吴德福
	 * @param roleCode
	 * @return List<SysRole>
	 * @since  fhd　Ver 1.1
	 */
	public List<SysRole> isExistByRoleCode(String roleCode){
		DetachedCriteria dc = DetachedCriteria.forClass(SysRole.class);
		dc.add(Restrictions.eq("roleCode", roleCode.trim()));
		return o_sysRoleDAO.findByCriteria(dc);
	}
	/**
	 * 根据角色名称判断角色是否存在.
	 * @author 吴德福
	 * @param roleName
	 * @return List<SysRole>
	 * @since  fhd　Ver 1.1
	 */
	public List<SysRole> isExistByRoleName(String roleName){
		DetachedCriteria dc = DetachedCriteria.forClass(SysRole.class);
		dc.add(Restrictions.eq("roleName", roleName.trim()));
		return o_sysRoleDAO.findByCriteria(dc);
	}
	/**
	 * 查询除超级管理员角色之外的所有角色.
	 * @return List<SysRole>
	 */
	public List<SysRole> queryRolesNotExistAdmin(){
		DetachedCriteria dc = DetachedCriteria.forClass(SysRole.class);
		dc.add(Restrictions.ne("roleCode", "SYSTEM_ADMIN"));
		return o_sysRoleDAO.findByCriteria(dc);
	}
}
