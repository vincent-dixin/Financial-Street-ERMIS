/**
 * EmpGridBO.java
 * com.fhd.sys.business.organization
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-1-21 		黄晨曦
 *
 * Copyright (c) 2013, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.business.organization;

import java.util.ArrayList;
import java.util.List;
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
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.dao.autho.SysoRoleDAO;
import com.fhd.sys.dao.autho.SysoRoleUsersDAO;
import com.fhd.sys.dao.autho.SysoUserDAO;
import com.fhd.sys.dao.duty.DutyDAO;
import com.fhd.sys.dao.organization.SysOrgDAO;
import com.fhd.sys.dao.organization.SysOrgEmpDAO;
import com.fhd.sys.dao.orgstructure.SysEmployeeDAO;
import com.fhd.sys.entity.auth.SysRole;
import com.fhd.sys.entity.auth.SysRoleUser;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.duty.Duty;
import com.fhd.sys.entity.orgstructure.SysEmpOrg;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;
import com.fhd.sys.interfaces.IEmpGridBO;

/**
 * @author   王再冉
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-1-21		上午11:25:47
 *
 * @see 	 
 */
@Service
public class EmpGridBO implements IEmpGridBO{
	
	@Autowired
	private SysEmployeeDAO o_sysEmployeeDAO;
	@Autowired
	private DutyDAO dutyDAO;
	@Autowired
	private SysoUserDAO o_sysoUserDAO;
	@Autowired
	private OrgGridBO o_orgGridBO;
	@Autowired
	private SysOrgEmpDAO o_sysOrgEmpDAO;
	@Autowired
	private SysoRoleDAO o_sysRoleDAO;
	@Autowired 
	private SysoRoleUsersDAO o_sysRoleUserDAO;
	@Autowired
	private SysOrgDAO o_sysOrgDAO;
	
	
	/**
	 * 查询当前登录员工所在公司的所有员工
	 * @author 
	 * @param empName
	 * @param page
	 * @param sort
	 * @param dir
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public Page<SysEmployee> findEmpBySome(String empName, Page<SysEmployee> page, String sort, String dir, String orgIds,String positionIds) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysEmployee.class);
		String companyId =  UserContext.getUser().getCompanyid();// 所在公司id
		List<SysOrganization> orgList = new ArrayList<SysOrganization>();
		List<String> idSet = new ArrayList<String>();
		if(null != orgIds){
			List<SysOrganization> sysOrgList = o_orgGridBO.findOrgsAndChildOrgsByOrgIds(orgIds);
			for(SysOrganization org : sysOrgList){
				idSet.add(org.getId());
			}
		}else{
			if(null != companyId){
				orgList = o_orgGridBO.findOrganizationByCompanyIds(companyId);
			}else{
				orgList = o_orgGridBO.findAllOrganizations();
			}
			for(SysOrganization org : orgList){
				idSet.add(org.getId());
			}
		}
		if(null != positionIds){
			dc.createAlias("sysEmpPosis", "ep");
			dc.add(Restrictions.eq("ep.sysPosition.id", positionIds));
		}
		
		dc.createAlias("sysEmpOrgs", "emporg");
		dc.add(Restrictions.in("emporg.sysOrganization.id", idSet));
		
		//dc.add(Restrictions.in("sysOrganization.id",idSet));
		dc.add(Restrictions.eq("empStatus","1"));
		if(StringUtils.isNotBlank(empName)){
			dc.add(Property.forName("empname").like(empName,MatchMode.ANYWHERE));
		}
		
		if("ASC".equalsIgnoreCase(dir)) {
			dc.addOrder(Order.asc(sort));
		} else {
			dc.addOrder(Order.desc(sort));
		}
		return o_sysEmployeeDAO.findPage(dc, page, false);
	}
	
	/**
	 * 查询角色Grid分页
	 * @param roleName	角色名
	 * @param page
	 * @param sort
	 * @param dir
	 * @param orgIds
	 * @return
	 */
	public Page<SysRole> findEmpRoleBySome(String roleName, Page<SysRole> page, String sort, String dir) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysRole.class);
		if(StringUtils.isNotBlank(roleName)){
			dc.add(Property.forName("roleName").like(roleName,MatchMode.ANYWHERE));
		}
		
		if("ASC".equalsIgnoreCase(dir)) {
			dc.addOrder(Order.asc(sort));
		} else {
			dc.addOrder(Order.desc(sort));
		}
		return o_sysRoleDAO.findPage(dc, page, false);
	}
	
	
	
	/**
	 * 根据员工id查询员工实体
	 * @author 
	 * @param empId
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	public SysEmployee findEmpEntryByEmpId(String empId) {
		Criteria c = o_sysEmployeeDAO.createCriteria();
		List<SysEmployee> list = null;
		
		if (StringUtils.isNotBlank(empId)) {
			c.add(Restrictions.eq("id", empId));
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
	
	
	/**
	 * 保存员工
	 * @author 黄晨曦
	 * @param emp
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void saveEmp(SysEmployee emp) {
		o_sysEmployeeDAO.merge(emp);
	}
	
	/**
	 * 更新员工
	 * 
	 * @author 
	 * @param emp
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void mergeEmp(SysEmployee emp) {
		o_sysEmployeeDAO.merge(emp);
	}
	
	/**
	 * 删除员工
	 * @author 
	 * @param ids
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	 public void removeEmpEntrys(String ids) {
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			SysEmployee empEntry = findEmpEntryByEmpId(id);
			//o_sysEmployeeDAO.delete(empEntry);
			empEntry.setEmpStatus("0");//逻辑删除
			o_sysEmployeeDAO.merge(empEntry);
		}
	}	
	
	
	/**
	 * 查找职务By CompanyId
	 * @author 
	 * @param companyId
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Duty> findDutyByCompanyId(String companyId) {
		Criteria c = dutyDAO.createCriteria();
		List<Duty> list = null;
		
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
	 * 根据id查询职务实体
	 * @author 
	 * @param dutyId
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public Duty findDutyEntryBydutyId(String dutyId) {
		Criteria c = dutyDAO.createCriteria();
		List<Duty> list = null;
		
		if (StringUtils.isNotBlank(dutyId)) {
			c.add(Restrictions.eq("id", dutyId));
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
	
	/**
	 * 根据用户名查询用户实体
	 * @author 
	 * @param userId
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@SuppressWarnings({ "unchecked" })
	public SysUser findUserByuserName(String username) {
		Criteria c = o_sysoUserDAO.createCriteria();
		List<SysUser> list = null;
		
		if (StringUtils.isNotBlank(username)) {
			c.add(Restrictions.eq("username", username));
		} 
		
		list = c.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 保存用户
	 * @author 
	 * @param emp
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void saveUser(SysUser user) {
		o_sysoUserDAO.merge(user);
	}
	
	/**
	 * 保存员工部门
	 * @param org
	 * @param emp
	 * @param orgIds
	 */
	@Transactional
	public void saveEmpOrgs(SysEmployee emp,String[] orgIds) {
		List<SysEmpOrg> empOrgList = findEmpOrgListByEmpId(emp.getId());
		if(!empOrgList.isEmpty()){
			for(SysEmpOrg oldempOrg : empOrgList){
					o_sysOrgEmpDAO.delete(oldempOrg);
				}
			}
		//保存辅助部门
		if(null != orgIds){
			for(String orgId : orgIds){
				SysEmpOrg empOrg = new SysEmpOrg();
				empOrg.setId(Identities.uuid());
				empOrg.setSysEmployee(emp);
				empOrg.setSysOrganization(o_sysOrgDAO.get(orgId));
				empOrg.setIsmain(false);//设置主部门属性
				o_sysOrgEmpDAO.merge(empOrg);
			}
		}
	}
	/**
	 * 保存员工主部门
	 * @param org
	 * @param emp
	 */
	@Transactional
	public void saveEmpOrgMain(SysOrganization org,SysEmployee emp) {
		SysEmpOrg empOrgMain = findEmpOrgByEmpId(emp.getId());
		if(null != empOrgMain){
			empOrgMain.setIsmain(true);
			empOrgMain.setSysOrganization(org);
		}else{
			//保存主部门
			empOrgMain = new SysEmpOrg();
			empOrgMain.setId(Identities.uuid());
			empOrgMain.setSysEmployee(emp);
			empOrgMain.setSysOrganization(org);
			empOrgMain.setIsmain(true);//设置主部门属性
		}
		o_sysOrgEmpDAO.merge(empOrgMain);
	}
	
	
	/**
	 * 根据员工id查找员工辅助部门关联实体集合
	 * @param empId
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public List<SysEmpOrg> findEmpOrgListByEmpId(String empId) {
		Criteria c = o_sysOrgEmpDAO.createCriteria();
		List<SysEmpOrg> list = null;
		if (StringUtils.isNotBlank(empId)) {
			c.add(Restrictions.eq("sysEmployee.id", empId));
			c.add(Restrictions.eq("ismain", false));
		} 
		list = c.list();
		return list;
	}
	/**
	 * 根据员工id查找员工主部门关联实体
	 * @param empId
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public SysEmpOrg findEmpOrgByEmpId(String empId) {
		Criteria c = o_sysOrgEmpDAO.createCriteria();
		List<SysEmpOrg> list = null;
		if (StringUtils.isNotBlank(empId)) {
			c.add(Restrictions.eq("sysEmployee.id", empId));
			c.add(Restrictions.eq("ismain", true));
		} 
		list = c.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	/**
	 * 保存员工角色
	 * @param userId
	 * @param roleSet
	 */
	@Transactional
	public void saveSysUserRole(String userId,Set<SysRole> roleSet){
		//deleteUserRoleEntry(userId);
		//清空当前所有关系
		SysUser sysUser= o_sysoUserDAO.get(userId);
		sysUser.getSysRoles().removeAll(sysUser.getSysRoles());
		for(SysRole role : roleSet){
			if(StringUtils.isNotBlank(userId)){
				sysUser.getSysRoles().add(role);
				o_sysoUserDAO.merge(sysUser);
			}
		}

	}
	/**
	 * 删除userId的角色关联实体
	 * @param userId
	 */
	@Transactional
	public void deleteUserRoleEntry(String userId) {
		List<SysRoleUser> roleUserlist = findUserRoleByuserId(userId);
		if(roleUserlist.size()>0){
			for(SysRoleUser roleUser : roleUserlist){
				o_sysRoleUserDAO.delete(roleUser);
			}
		}
	}
	/**
	 * 查询用户角色关联实体By userId
	 * @param userId
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public List<SysRoleUser> findUserRoleByuserId(String userId) {
		Criteria c = o_sysRoleUserDAO.createCriteria();
		List<SysRoleUser> list = null;
		if (StringUtils.isNotBlank(userId)) {
			c.add(Restrictions.eq("sysUser.id", userId));
		} 
		list = c.list();
		if (list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}

}

