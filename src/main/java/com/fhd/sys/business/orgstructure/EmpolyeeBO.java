/**
 * EmpManagementBusiness.java
 * com.fhd.fdc.commons.business.sys
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-12 		胡迪新
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.business.orgstructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SQLQuery;
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
import com.fhd.core.utils.Identities;
import com.fhd.fdc.commons.exception.FHDException;
import com.fhd.fdc.utils.UserContext;
import com.fhd.sys.business.auth.SysUserBO;
import com.fhd.sys.business.log.BusinessLogBO;
import com.fhd.sys.dao.orgstructure.SysEmpOrgDAO;
import com.fhd.sys.dao.orgstructure.SysEmpPosiDAO;
import com.fhd.sys.dao.orgstructure.SysEmployeeDAO;
import com.fhd.sys.dao.orgstructure.SysEmployeeDAOold;
import com.fhd.sys.dao.orgstructure.SysOrganizationDAO;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.duty.Duty;
import com.fhd.sys.entity.orgstructure.SysEmpOrg;
import com.fhd.sys.entity.orgstructure.SysEmpPosi;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;
import com.fhd.sys.entity.orgstructure.SysPosition;
import com.fhd.sys.interfaces.IEmployeeBO;
import com.fhd.sys.web.form.orgstructure.EmpForm;


/**
 * 员工BO类.
 * @author   wudefu
 * @version V1.0  创建时间：2010-9-19 
 * @since    Ver 1.1
 * @Date	 2010-9-19		下午12:45:33
 * Company FirstHuiDa.
 * @see 	 
 */
@Service
@SuppressWarnings({"unchecked","deprecation"})
public class EmpolyeeBO implements IEmployeeBO {
	@Autowired
	private SysEmployeeDAOold o_sysEmployeeDAO2;
	@Autowired
	private SysEmployeeDAO o_sysEmployeeDAO;
	@Autowired
	private SysEmpPosiDAO o_sysEmpPosiDAO;
	@Autowired
	private SysEmpPosiBO o_sysEmpPosiBO;
	@Autowired
	private SysEmpOrgDAO o_sysEmpOrgDAO;
	@Autowired
	private SysEmpOrgBO o_sysEmpOrgBO;
	@Autowired
	private OrganizationBO o_organizationBO;
	@Autowired
	private BusinessLogBO o_businessLogBO;
	@Autowired
	private PositionBO o_positionBO;
	@Autowired
	private SysUserBO o_sysUserBO;
	@Autowired
	private SysOrganizationDAO o_sysOrganizationDAO;
	
	/**
	 * 根据id查询员工.
	 * @author 胡迪新
	 * @param id
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public SysEmployee get(Serializable id) {
		return o_sysEmployeeDAO2.get(id);
	}
	/**
	 * 更新员工.
	 * @author 吴德福
	 * @param employee
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void updateEmployee(SysEmployee employee){
		try {
			o_sysEmployeeDAO2.merge(employee);
			o_businessLogBO.modBusinessLogInterface("修改", "员工", "成功", employee.getId());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.modBusinessLogInterface("修改", "员工", "失败", employee.getId());
		}
	}
	/**
	 * 添加员工，同时维护机构与员工的关系.  如果从岗位列表中来同时维护岗位与员工的关系
	 * @author 胡迪新
	 * @param id 添加员工时的机构
	 * @param posiId 岗位的ID
	 * @param employee 员工实体
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void saveEmpO(String id, SysEmployee employee,String posiId){
		try {
			SysOrganization organization = employee.getSysOrganization();
			organization.setIsLeaf(false);
			o_organizationBO.merge(organization);
			o_sysEmployeeDAO2.save(employee);
			//维护机构与员工的关系
			SysEmpOrg sysEmpOrg = new SysEmpOrg();
			sysEmpOrg.setId(Identities.uuid2());
			sysEmpOrg.setSysOrganization(o_organizationBO.get(id));
			sysEmpOrg.setSysEmployee(employee);
			sysEmpOrg.setIsmain(false);
			o_sysEmpOrgBO.save(sysEmpOrg);
			if(null!=posiId){
				SysEmpPosi ep=new SysEmpPosi();
				SysPosition position=o_positionBO.get(posiId);
				ep.setId(Identities.uuid2());
				ep.setSysEmployee(employee);
				ep.setSysPosition(position);
				ep.setIsmain(false);
				o_sysEmpPosiBO.save(ep);
			}
			o_businessLogBO.saveBusinessLogInterface("新增", "员工", "成功", employee.getEmpcode(),employee.getEmpname(),employee.getEmpStatus(),employee.getGender());
		} catch (Exception e) { 
			System.out.println();
			e.printStackTrace();
			o_businessLogBO.saveBusinessLogInterface("新增", "员工", "失败", employee.getEmpcode(),employee.getEmpname(),employee.getEmpStatus(),employee.getGender());
		}
	}
	
	/**
	 * 修改员工.
	 * @author 胡迪新
	 * @param employee
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public String merge(SysEmployee employee) {
		try {
			o_sysEmployeeDAO2.merge(employee);
			o_businessLogBO.modBusinessLogInterface("修改", "员工", "成功", employee.getId());
//			SysUser sysUser = new SysUser();
//			sysUser.setId(employee.getUserid());
//			sysUser.setUsername(employee.getUsername());
//			sysUser.setRealname(employee.getEmpname());
//			o_sysUserBO.updateUser(sysUser);
			
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.modBusinessLogInterface("修改", "员工", "失败", employee.getId());
			return "0";
		}
	}
	
	/**
	 * 根据岗位id查询岗位下的所属员工.
	 * @author 胡迪新
	 * @param id
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<SysEmployee> queryByPosi(String id){
		List<SysEmployee> emps = new ArrayList<SysEmployee>(); 
		List<SysEmpPosi> sysEmpPosis = o_sysEmpPosiDAO.findByCriteria(Restrictions.eq("sysPosition.id", id));
		for (SysEmpPosi sysEmpPosi : sysEmpPosis) {
			emps.add(sysEmpPosi.getSysEmployee());
		}
		return emps;
	}
	/**
	 * 根据机构id查询机构下的所有员工(不包括岗位下的员工)--加载本机构员工时使用.
	 * @author 吴德福
	 * @param id 机构id.
	 * @return List<SysEmployee> 员工集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<SysEmployee> queryEmpsByOrgid(String id){
		//子查询sql
		/*
		SELECT * 
		FROM t_sys_emp_org eo
		WHERE (emp_id NOT IN
        	      (SELECT ep.emp_id
				   FROM t_sys_emp_posi ep INNER JOIN
                      t_sys_position p ON ep.posi_id = p.id
				   WHERE p.org_id = eo.org_id)
			   ) 
			  AND org_id = '737090D66F7542889EAD3C1B08314281'
		ORDER BY org_id
		
		 */
		
		DetachedCriteria dc = DetachedCriteria.forClass(SysEmpOrg.class, "eo");
		dc.add(Property.forName("sysEmployee.id").notIn(
				DetachedCriteria
				.forClass(SysEmpPosi.class,"ep")
				.setProjection(Property.forName("sysEmployee"))
				.createAlias("ep.sysPosition", "p")
				.add(Restrictions.eqProperty("eo.sysOrganization.id",
						"p.sysOrganization.id"))
		));
		dc.add(Restrictions.eq("sysOrganization.id", id));
		dc.addOrder(Order.asc("id"));
		
		//机构下的员工
		List<SysEmployee> emps = new ArrayList<SysEmployee>(); 
		//List<SysEmpOrg> sysEmpOrgs = o_sysEmpOrgDAO.findByCriteria(Restrictions.eq("sysOrganization.id", id));
		List<SysEmpOrg> sysEmpOrgs = o_sysEmpOrgDAO.findByCriteria(dc);
		for (SysEmpOrg sysEmpOrg : sysEmpOrgs) {
			emps.add(sysEmpOrg.getSysEmployee());
		}
		return emps;
	}
	/**
	 * 根据机构id查询机构下的所有员工--查询本机构的所有员工时使用.
	 * @author 吴德福
	 * @param id 机构id.
	 * @return List<SysEmployee> 员工集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<SysEmployee> queryEmployeesByOrgid(String id){
		SysOrganization org = o_organizationBO.get(id);
		List<SysEmployee> emps = new ArrayList<SysEmployee>(); 
		
		//机构下的员工
		List<SysEmpOrg> sysEmpOrgs = o_sysEmpOrgDAO.findByCriteria(Restrictions.eq("sysOrganization.id", id));
		for (SysEmpOrg sysEmpOrg : sysEmpOrgs) {
			if(!emps.contains(sysEmpOrg.getSysEmployee())){
				emps.add(sysEmpOrg.getSysEmployee());
			}
		}
		//机构下的岗位的员工
		for(SysPosition subPosition : org.getSysPositions()){
			List<SysEmpPosi> sysEmpPositions = o_sysEmpPosiBO.querySysEmpPosiByPosiId(subPosition.getId());
			for(SysEmpPosi sysEmpPosi : sysEmpPositions){
				if(!emps.contains(sysEmpPosi.getSysEmployee())){
					emps.add(sysEmpPosi.getSysEmployee());
				}
			}
		}
		
		//机构下的子机构所有员工
		for(SysOrganization subOrg : org.getSubOrg()){
			//402881b22afad3b1012afae5a4200004:数据字典条目指定机构类型：总公司id，不允许改变
			//402881b22afad3b1012afae5e33d0005:数据字典条目指定机构类型：分公司id，不允许改变
			if("402881b22afad3b1012afae5a4200004".equals(subOrg.getOrgType()) || "402881b22afad3b1012afae5e33d0005".equals(subOrg.getOrgType())){
				continue;
			}
			//机构下的子机构下的员工
			List<SysEmpOrg> sysEmpOrgList = o_sysEmpOrgBO.querySysEmpOrgByOrgid(subOrg.getId());
			for (SysEmpOrg empOrg : sysEmpOrgList) {
				if(!emps.contains(empOrg.getSysEmployee())){
					emps.add(empOrg.getSysEmployee());
				}
			}
			//机构下的子机构的岗位下的员工
			for(SysPosition subPosition : subOrg.getSysPositions()){
				List<SysEmpPosi> sysEmpPositions = o_sysEmpPosiBO.querySysEmpPosiByPosiId(subPosition.getId());
				for(SysEmpPosi sysEmpPosi : sysEmpPositions){
					if(!emps.contains(sysEmpPosi.getSysEmployee())){
						emps.add(sysEmpPosi.getSysEmployee());
					}
				}
			}
		}
		
		return emps;
	}	
	/**
	 * 根据岗位id查询机构下的所有员工.
	 * @author 吴德福
	 * @param posiId 岗位id.
	 * @return List<SysEmployee> 员工集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<SysEmployee> queryEmployees(String posiId){
		List<SysEmployee> emps = new ArrayList<SysEmployee>();
		//添加岗位员工时，可添加的员工包括：当前岗位所在的总公司或者分公司(机构类型判断)的所在员工     去除    已经在这个岗位上的员工
		SysOrganization org = getOrgByPosiId(StringUtils.substring(posiId, 0, 32));
		
		List<SysOrganization> orgs = new ArrayList<SysOrganization>();
		//查询出当前机构及其所有子机构
		//集团可以调派子公司员工到某岗位，否则需要过滤子公司及其下属子公司
		orgs = o_organizationBO.queryOrgsByOrgid(org.getId());
		
		for(SysOrganization subOrg : orgs){
			//机构下的所有机构人员
			List<SysEmpOrg> sysEmpOrgs = o_sysEmpOrgDAO.findByCriteria(Restrictions.eq("sysOrganization.id", subOrg.getId()));
			for (SysEmpOrg sysEmpOrg : sysEmpOrgs) {
				List<SysEmpPosi> list = o_sysEmpPosiBO.querySysEmpPosiByUnionid(StringUtils.substring(posiId, 0, 32), sysEmpOrg.getSysEmployee().getId());
				if (null != list && list.size() > 0) {
					continue;
				}
				emps.add(sysEmpOrg.getSysEmployee());
			}
			//岗位下的所有岗位人员
			List<SysPosition> posis = o_positionBO.queryPosiByOrgid(subOrg.getId());
			for(SysPosition posi : posis){
				List<SysEmpPosi> empList = o_sysEmpPosiBO.querySysEmpPosiByPosiId(posi.getId());
				for(SysEmpPosi ep : empList){
					List<SysEmpPosi> list = o_sysEmpPosiBO.querySysEmpPosiByUnionid(StringUtils.substring(posiId, 0, 32), ep.getSysEmployee().getId());
					if (null != list && list.size() > 0) {
						continue;
					}
					emps.add(ep.getSysEmployee());
				}
			}
		}
		
		return emps;
	}
	
	/**
	 * 根据岗位id查找最近的分公司或者总公司.
	 * @author 吴德福
	 * @param posiId
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	private SysOrganization getOrgByPosiId(String posiId){
		SysOrganization org = o_positionBO.get(posiId).getSysOrganization();
		return getOrgByOrgtype(org);
	}
	/**
	 * 查询机构类型为总公司或者分公司的最近机构.
	 * @author 吴德福
	 * @param org 机构
	 * @return SysOrganization 机构.
	 * @since  fhd　Ver 1.1
	 */
	private SysOrganization getOrgByOrgtype(SysOrganization org){
		//402881b22afad3b1012afae5a4200004:数据字典条目指定机构类型：总公司id，不允许改变
		//402881b22afad3b1012afae5e33d0005:数据字典条目指定机构类型：分公司id，不允许改变
		if("402881b22afad3b1012afae5a4200004".equals(org.getOrgType()) || "402881b22afad3b1012afae5e33d0005".equals(org.getOrgType())){
			return org;
		}else{
			return getOrgByOrgtype(org.getParentOrg());
		}
	}
	
	/**
	 * 根据机构id查询所有员工.
	 * @author 胡迪新
	 * @param id
	 * @param filters
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<SysEmployee> queryEmployeeByOrgid(String id,List<PropertyFilter> filters){
		return o_sysEmployeeDAO2.find(filters, "id", true, Restrictions.eq("sysOrganization.id", id));
		
	}
	/**
	 * 删除岗位与员工的关系.
	 * @author 吴德福
	 * @param id 岗位id与选择的员工id集合.
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void deleteEmpPosi(String posiId, String empid){
		try {
			//删除员工与岗位的关系
			o_sysEmpPosiBO.removeEmpPosi(posiId, empid);
			
			List<SysEmpOrg> resultList = new ArrayList<SysEmpOrg>();
			resultList = o_sysEmpOrgBO.querySysEmpOrgByEmpid(empid);
			if(null != resultList && resultList.size()>0){	
				//删除岗位下员工时，如果员工和机构有关系，员工不删除
			}else{
				//删除岗位下员工时，如果员工和机构没有关系，删除员工
				o_sysEmployeeDAO2.removeById(empid);
				o_businessLogBO.delBusinessLogInterface("删除", "员工", "成功", empid);
			}
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.delBusinessLogInterface("删除", "员工", "失败", empid);
		}
	}
	/**
	 * 删除员工.
	 * @author 吴德福
	 * @param id 员工id.
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void delete(String id){
		try {
			o_sysEmployeeDAO2.removeById(id);
			o_businessLogBO.delBusinessLogInterface("删除", "员工", "成功", id);
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.delBusinessLogInterface("删除", "员工", "失败", id);
		}
	}
	/**
	 * 根据查询条件查询员工.
	 * @author 吴德福
	 * @param filters 页面查询条件传递的参数集合.
	 * @return List<SysEmployee> 员工集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<SysEmployee> query(EmpForm empForm) {
		Criteria criteria = o_sysEmployeeDAO2.createCriteria();
		criteria.createAlias("sysEmpPosis", "ep");
		criteria.createAlias("ep.sysPosition", "p");
		criteria.setFetchMode("sysEmpPosis.sysPosition.sysRoles", FetchMode.SELECT);
		criteria.setFetchMode("sysEmpPosis.sysPosition.sysAuthorities", FetchMode.SELECT);
		if(empForm != null && !"".equals(empForm.getEmpcode()) && empForm.getEmpcode() != null){
			criteria.add(Restrictions.eq("empcode", empForm.getEmpcode()));
		}
		if(empForm != null && !"".equals(empForm.getEmpname()) && empForm.getEmpname() != null){
			criteria.add(Restrictions.eq("empname", empForm.getEmpname()));
		}
		
		return criteria.list();
	}
	/**
	 * ajax获取员工名empname是否可用.
	 * @author 吴德福
	 * @param empname 员工名.
	 * @return List<SysEmployee> 员工list
	 * @since  fhd　Ver 1.1
	 */
	public List<SysEmployee> queryEmployeeByEmpname(String empname){
		return o_sysEmployeeDAO2.findBy("empname", empname);
	}
	/**
	 * 根据用户id查询员工.
	 * @author 吴德福
	 * @param userid 用户id.
	 * @return SysEmployee 员工.
	 * @since  fhd　Ver 1.1
	 */
	public SysEmployee getEmployee(String userid){
		SysEmployee emp = null;
		SysUser sysUser = o_sysUserBO.queryUserById(userid);
		if(null != sysUser){
			if(!"admin".equals(sysUser.getUsername())){
				emp = o_sysEmployeeDAO2.findUniqueBy("userid", userid);
			}
		}
		return emp;
	}
	/**
	 * 
	 * <pre>
	 * getEmployeeByUserName:根据username查询员工
	 * </pre>
	 * 
	 * @author 张芳
	 * @param username
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public SysEmployee getEmployeeByUserName(String username){
		SysEmployee emp = null;
		SysUser sysUser = o_sysUserBO.getByUsername(username);
		if(null != sysUser){
			if(!"admin".equals(sysUser.getUsername())){
				List<SysEmployee> list = o_sysEmployeeDAO2.findBy("username", username);
				if(null != list && list.size()>0){
					emp = list.get(0);
				}
			}
		}
		return emp;
	}
	/**
	 * Ext拖拽移动node是否成功.
	 * @author 吴德福
	 * @param currentId 当前拖拽的结点id.
	 * @param pid 当前拖拽的父结点id.
	 * @param targetId 当前拖拽的结点的目标结点id.
	 * @param ptype 当前拖拽的结点的父结点的类型.
	 * @param type 当前拖拽的结点的目标结点的类型.
	 * @return Boolean 操作是否成功.
	 * @since  fhd　Ver 1.1
	 */
	public Boolean moveNode(String currentId,String pid,String targetId,String ptype,String type){
		try {
			if("emp".equals(type)){
				return false;
			}
			
			if("org".equals(type) || "root".equals(type)){
				//插入结点(员工)与目标结点(机构)的关系
				SysEmpOrg eo = new SysEmpOrg();
				eo.setId(Identities.uuid2());
				eo.setIsmain(false);
				eo.setSysEmployee(o_sysEmployeeDAO2.get(currentId));
				eo.setSysOrganization(o_organizationBO.get(targetId));
				List<SysEmpOrg> eos = o_sysEmpOrgBO.querySysEmpOrgByUnionid(eo.getSysOrganization().getId(),eo.getSysEmployee().getId());
				if(null != eos && eos.size()>0){
					return false;
				}else{
					o_sysEmpOrgBO.save(eo);
					if("org".equals(ptype) || "root".equals(ptype)){
						//删除结点(员工)与父结点(机构)的关系
						o_sysEmpOrgBO.removeEmpOrg(pid, currentId);
					}
					if("posi".equals(ptype)){
						//删除结点(员工)与父结点(岗位)的关系
						o_sysEmpPosiBO.removeEmpPosi(pid, currentId);
					}
				}
			}
			if("posi".equals(type)){
				//插入结点(员工)与目标结点(岗位)的关系
				SysEmpPosi ep = new SysEmpPosi();
				ep.setId(Identities.uuid2());
				ep.setIsmain(false);
				ep.setSysEmployee(o_sysEmployeeDAO2.get(currentId));
				ep.setSysPosition(o_positionBO.get(targetId));
				List<SysEmpPosi> eps = o_sysEmpPosiBO.querySysEmpPosiByUnionid(ep.getSysPosition().getId(),ep.getSysEmployee().getId());
				if(null != eps && eps.size()>0){
					return false;
				}else{
					o_sysEmpPosiBO.save(ep);
					/*
					 * 员工从部门移动到岗位时，不删除员工与部门的关系
					 * if("org".equals(ptype) || "root".equals(ptype)){
					 *	 //删除结点(员工)与父结点(机构)的关系
					 *	 o_sysEmpOrgBO.removeEmpOrg(pid, currentId);
					 * }
					 */
					if("posi".equals(ptype)){
						//删除结点(员工)与父结点(岗位)的关系
						o_sysEmpPosiBO.removeEmpPosi(pid, currentId);
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Ext拖拽复制node是否成功.
	 * @author 吴德福
	 * @param currentId 当前拖拽的结点id.
	 * @param pid 当前拖拽的父结点id.
	 * @param targetId 当前拖拽的结点的目标结点id.
	 * @param ptype 当前拖拽的结点的父结点的类型.
	 * @param type 当前拖拽的结点的目标结点的类型.
	 * @return Boolean 操作是否成功.
	 * @since  fhd　Ver 1.1
	 */
	public Boolean copyNode(String currentId,String targetId,String type){
		try {
			if("emp".equals(type)){
				return false;
			}
			if("org".equals(type) || "root".equals(type)){
				//插入结点(员工)与目标结点(机构)的关系
				SysEmpOrg eo = new SysEmpOrg();
				eo.setId(Identities.uuid2());
				eo.setIsmain(false);
				eo.setSysEmployee(o_sysEmployeeDAO2.get(currentId));
				eo.setSysOrganization(o_organizationBO.get(targetId));
				List<SysEmpOrg> eos = o_sysEmpOrgBO.querySysEmpOrgByUnionid(eo.getSysOrganization().getId(),eo.getSysEmployee().getId());
				if(null != eos && eos.size()>0){
					return false;
				}else{
					o_sysEmpOrgBO.save(eo);
				}
			}
			if("posi".equals(type)){
				//插入结点(员工)与目标结点(岗位)的关系
				SysEmpPosi ep = new SysEmpPosi();
				ep.setId(Identities.uuid2());
				ep.setIsmain(false);
				ep.setSysEmployee(o_sysEmployeeDAO2.get(currentId));
				ep.setSysPosition(o_positionBO.get(targetId));
				List<SysEmpPosi> eps = o_sysEmpPosiBO.querySysEmpPosiByUnionid(ep.getSysPosition().getId(),ep.getSysEmployee().getId());
				if(null != eps && eps.size()>0){
					return false;
				}else{
					o_sysEmpPosiBO.save(ep);
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 根据机构id查询机构下的所有员工(包括岗位下的员工)--查询本机构的所有员工时使用.
	 * @author 吴德福
	 * @param orgId 机构id.
	 * @return List<SysEmployee> 员工集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<SysEmployee> querySysEmployeesByOrgid(String orgId){
		List<SysEmployee> emps = new ArrayList<SysEmployee>();
		SysOrganization org = o_organizationBO.get(orgId);
		
		//机构下的员工
		List<SysEmpOrg> sysEmpOrgs = o_sysEmpOrgDAO.findByCriteria(Restrictions.eq("sysOrganization.id", orgId));
		for (SysEmpOrg sysEmpOrg : sysEmpOrgs) {
			if(!emps.contains(sysEmpOrg.getSysEmployee())){
				emps.add(sysEmpOrg.getSysEmployee());
			}
		}
		//机构下的岗位的员工
		for(SysPosition subPosition : org.getSysPositions()){
			List<SysEmpPosi> sysEmpPositions = o_sysEmpPosiBO.querySysEmpPosiByPosiId(subPosition.getId());
			for(SysEmpPosi sysEmpPosi : sysEmpPositions){
				if(!emps.contains(sysEmpPosi.getSysEmployee())){
					emps.add(sysEmpPosi.getSysEmployee());
				}
			}
		}
		return emps;
	}
	/**
	 * getEmpsByDeptIdAndDutyId:跟据部门和职务获取员工(不包括下属部门下的员工).
	 * @author David
	 * @param deptIds
	 * @param dutyIds
	 * @return List<String>
	 * @since  fhd　Ver 1.1
	 */
	public List<String> getEmpsByDeptIdAndDutyId(String[] deptIds,String[] dutyIds){
		List<String> results = new ArrayList<String>();
		for(String deptId : deptIds){
			if(deptId==null)
				continue;
			
			List<SysEmployee> emps = querySysEmployeesByOrgid(deptId);//某部门员工列表(不包括下属部门下的员工)
			
			Iterator<SysEmployee> empIts = emps.iterator();
			while(empIts.hasNext()){//将指定职务范围内的员工加入返回集合
				SysEmployee emp = empIts.next();
				Duty duty = emp.getDuty();
				if(duty!=null && containsString(dutyIds,duty.getId())){
					results.add(deptId + "," + emp.getId()+","+duty.getId());
				}
			}
		}
		
		return results;
	}
	/**
	 * 根据部门id或多部门id查询员工
	 * @author 杨鹏
	 * @param deptIds
	 * @return
	 */
	public List<SysEmployee> findByDeptIds(String...deptIds){
		DetachedCriteria criterions=DetachedCriteria.forClass(SysEmployee.class);
		criterions.add(Restrictions.in("sysOrganization.id", deptIds));
		return o_sysEmployeeDAO2.findByCriteria(criterions);
	}
	/**
	 * containsString:判断某个字符串数组中是否含有指定字符串.
	 * @author David
	 * @param stringArray
	 * @param aString
	 * @return boolean
	 * @since  fhd　Ver 1.1
	 */
	public boolean containsString(String[] stringArray, String aString){
		boolean contains = false;
		for(String stringInside : stringArray){
			if(stringInside.equals(aString)){
				contains = true;
				break;
			}
		}
			
		return contains;
	}
	/**
	 * 查询当前问卷下的所有符合职务的人员
	 * @author 吴德福
	 * @param questSetId
	 * @return List<SysEmployee>
	 * @since  fhd　Ver 1.1
	 */
	public List<SysEmployee> querySysEmployeesByQuestSetId(String questSetId){
		DetachedCriteria dc = DetachedCriteria.forClass(SysEmployee.class);
		dc.createAlias("duty", "d");
		dc.createAlias("d.questSetDuty", "qd");
		dc.createAlias("qd.questSet", "q");
		dc.add(Restrictions.eq("q.id", questSetId));
		return o_sysEmpOrgDAO.findByCriteria(dc);
	}
	/**
	 * 根据userid查询员工--问卷答题时使用，能登录查看答题的用户一定是该公司的员工.
	 * @author 吴德福
	 * @param userid
	 * @return SysEmployee
	 * @since  fhd　Ver 1.1
	 */
	public SysEmployee questEmployeeByUserid(String userid){
		SysEmployee emp = null;
		DetachedCriteria dc = DetachedCriteria.forClass(SysEmployee.class);
		dc.add(Restrictions.eq("userid", userid));
		List<SysEmployee> empList = o_sysEmployeeDAO2.findByCriteria(dc);
		if(null != empList && empList.size()>0){
			emp = empList.get(0);
		}
		return emp;
	}
	/**
	 * 根据员工id获取所属岗位.
	 * @author 吴德福
	 * @param empId
	 * @return SysPosition
	 */
	public SysPosition getPositionByEmpId(String empId){
		SysPosition position = null;
		List<SysEmpPosi> seps = o_sysEmpPosiDAO.find("from SysEmpPosi sep where sep.sysEmployee.id=?",empId);
		if(null != seps && seps.size()>0){
			position = seps.get(0).getSysPosition();
		}
		return position;
	}
	/**
	 * 根据员工id获取所属部门.
	 * @author David
	 * @param empId
	 * @return SysOrganization
	 */
	public SysOrganization getDepartmentByEmpId(String empId){
		List<SysEmpOrg> seos = o_sysEmpOrgDAO.find("from SysEmpOrg seo where seo.sysEmployee.id=?",empId);
		if(null != seos && seos.size()>0){
			return seos.get(0).getSysOrganization();
		}
		
		List<SysEmpPosi> seps = o_sysEmpPosiDAO.find("from SysEmpPosi sep where sep.sysEmployee.id=?",empId);
		if(null != seps && seps.size()>0){
			return seps.get(0).getSysPosition().getSysOrganization();
		}
		
		return null;
	}
	/**
	 * 递归本机构下，包括子机构下的所有机构ID
	 * @author wanye
	 * @param orgId
	 * @return
	 */
	public String getSubOrgIds(String orgId){
		String orgsubids=new String();
		
		SysOrganization org = this.o_organizationBO.get(orgId);
		if(org.getSubOrg().size()>0){
			for(SysOrganization suborg:org.getSubOrg()){
				orgsubids = getSubOrgIds(suborg.getId())+","+orgsubids;
			}
			return orgId+","+orgsubids;
		}else{
			
			return orgId;
		}
	}
	/**
	 * 查询整理递归后本机构下，包括子机构下的所有机构ID
	 * @author wanye
	 * @param orgId
	 * @param flag
	 * @return
	 */
	public String[] getSubOrgIds(String orgId, String flag){
		SysOrganization org = this.o_organizationBO.get(orgId);
		if(null==org.getParentOrg()){//顶级机构
			return this.o_sysOrganizationDAO.getAllIds();
		}else{
			
			String ids = this.getSubOrgIds(orgId);//使用递归
			return ids.replaceAll(",{2,}", ",").replaceFirst(",$", "").replaceFirst("^,", "").split(",");
		}
		
		
	}
	
	/**
	 * 查询本机构下，包括子机构下所的岗位的ID
	 * @author wanye
	 * @param orgId
	 * @return
	 */
	public ArrayList<String> getSubPosiIds(String orgId){
		String[] orgids=this.getSubOrgIds(orgId,"flag");
		ArrayList<String> posiIds = new ArrayList<String>();
		for(String orgid:orgids){
			SysOrganization org = this.o_organizationBO.get(orgid);
			if(null==org)
				continue;
			Set<SysPosition> set = org.getSysPositions();
			for(SysPosition posi:set){
				posiIds.add(posi.getId());
			}
		}
		return posiIds;
	}
	/**
	 * 分页 查询当前机构下包括子机构和下属岗位下所有的员工
	 * @author wanye
	 * @param page
	 * @param id
	 * @param emp
	 * @return
	 * @throws Exception
	 */
	public com.fhd.core.dao.Page<SysEmployee> querySysAllEmployeeByOrgId(com.fhd.core.dao.Page<SysEmployee> page,String id, SysEmployee emp) throws FHDException{
				
		StringBuilder hql = new StringBuilder();
		hql.append("from SysEmployee emp where (exists(select org.id from SysEmpOrg eo,SysOrganization org ")
		.append("where org.orgseq like concat(concat('%.',:orgid),'.%') and org.id=eo.sysOrganization.id ")
		.append("and eo.sysEmployee.id=emp.id) ")
		.append("or exists(select org.id from SysEmpPosi ep,SysPosition pos,SysOrganization org ")
		.append("where emp.id=ep.sysEmployee.id and ep.sysPosition.id=pos.id and pos.sysOrganization.id=org.id ")
		.append("and org.orgseq like concat(concat('%.',:orgid),'.%'))) ");
		Map<String,String> values = new HashMap<String, String>();
		
		if(StringUtils.isNotBlank(emp.getEmpcode())){
			hql.append(" and emp.empcode like concat(concat('%',:empcode),'%') ");
			values.put("empcode", emp.getEmpcode());
		}
		if(StringUtils.isNotBlank(emp.getEmpname())){
			hql.append(" and emp.empname like concat(concat('%',:empname),'%') ");
			values.put("empname", emp.getEmpname());
		}
		values.put("orgid", id);
		hql.append(" order by emp.empcode");
		return o_sysEmployeeDAO.findPage(page, hql.toString(),false, values);
		
		
	}
	
	/**
	 * <pre>
	 * 不分页 查询当前机构下包括子机构和下属岗位下所有的员工
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param orgId 机构ID
	 * @param employee 人员
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public Set<SysEmployee> findAllEmployeeBySome(String orgId, SysEmployee emp){
		Set<SysEmployee> allEmpSet = new HashSet<SysEmployee>();
		StringBuilder hqlSEO = new StringBuilder();
		Map<String,String> values = new HashMap<String, String>();
		values.put("orgid", orgId);
		hqlSEO.append("select seo.sysEmployee from SysEmpOrg seo left join seo.sysOrganization org ")
			.append("where org.orgseq like concat(concat('%.',:orgid),'.%') ");
		if(null != emp && StringUtils.isNotBlank(emp.getEmpname())){//查询条件模糊匹配编号和姓名
			hqlSEO.append(" and (seo.sysEmployee.empcode like concat(concat('%',:empcode),'%') ");
			values.put("empcode", emp.getEmpname());
			hqlSEO.append(" or seo.sysEmployee.empname like concat(concat('%',:empname),'%') )");
			values.put("empname", emp.getEmpname());
		}
		hqlSEO.append(" order by seo.sysEmployee.empcode");
		List<SysEmployee> empSEOList = o_sysEmployeeDAO.find(hqlSEO.toString(), values);
		allEmpSet.addAll(empSEOList);
		StringBuilder hqlSEP = new StringBuilder();
		hqlSEP.append("select sep.sysEmployee from SysEmpPosi sep left join sep.sysPosition sp left join sp.sysOrganization org ")
			.append("where org.orgseq like concat(concat('%.',:orgid),'.%') ");
		if(null != emp && StringUtils.isNotBlank(emp.getEmpname())){//查询条件模糊匹配编号和姓名
			hqlSEP.append(" and (sep.sysEmployee.empcode like concat(concat('%',:empcode),'%') ");
			values.put("empcode", emp.getEmpname());
			hqlSEP.append(" or sep.sysEmployee.empname like concat(concat('%',:empname),'%') )");
			values.put("empname", emp.getEmpname());
		}
		hqlSEP.append(" order by sep.sysEmployee.empcode");
		List<SysEmployee> empSEPList = o_sysEmployeeDAO.find(hqlSEP.toString(), values);
		allEmpSet.addAll(empSEPList);
		
		return allEmpSet;
		
	}
	
	/**
	 * 根据部门id集合查询指定roleName下的人员及相关信息.
	 * @param orgIdSet
	 * @param roleName
	 * @return List<Object[]>
	 */
	public List<Object[]> findEmployeeListByOrgIdsAndRoleName(Set<String> orgIdSet, String roleName){
		
		StringBuilder selectBuf = new StringBuilder();
		StringBuilder fromBuf = new StringBuilder();
		StringBuilder whereBuf = new StringBuilder();
		StringBuilder orderBuf = new StringBuilder();
		
		selectBuf.append("select e.id,e.EMP_NAME,u.id,u.USER_NAME,r.id,r.ROLE_NAME,o.id,o.ORG_NAME ");
		fromBuf.append("from t_sys_employee e,t_sys_user u,t_sys_user_role ur,t_sys_role r,t_sys_emp_org eo,t_sys_organization o ");
		whereBuf.append("where e.USER_ID=u.id and u.id=ur.USER_ID and ur.ROLE_ID=r.id and e.id=eo.EMP_ID and eo.ORG_ID=o.id ");
		if(StringUtils.isNotBlank(roleName)){
			whereBuf.append(" and r.ROLE_NAME='").append(roleName).append("' ");
		}
		if(null != orgIdSet && orgIdSet.size()>0){
			StringBuffer inBuf = new StringBuffer("");
			int i=0;
			for (String orgId : orgIdSet) {
				if(StringUtils.isNotBlank(orgId)){
					inBuf.append("'").append(orgId).append("'");
				}
				if(i != orgIdSet.size()-1){
					inBuf.append(",");
				}
				i++;
			}
			whereBuf.append(" and o.id in(").append(inBuf).append(") ");
		}
		orderBuf.append(" order by o.id ");
		SQLQuery sqlquery = o_sysEmployeeDAO.createSQLQuery(selectBuf.append(fromBuf).append(whereBuf).append(orderBuf).toString());
		return sqlquery.list();
	}
	/**
	 * <pre>
	 * 根据组织机构查询员工（分页）
	 * </pre>
	 * @author 万业
	 * @param page
	 * @param id 机构ID
	 * @return
	 * @throws Exception
	 */
	public Page<SysEmpOrg> querySysEmpByOrgIdPage(Page<SysEmpOrg> page,String id, SysEmployee emp) throws Exception{
		
		//子查询sql
		/*
		SELECT * 
		FROM t_sys_emp_org eo
		WHERE (emp_id NOT IN
        	      (SELECT ep.emp_id
				   FROM t_sys_emp_posi ep INNER JOIN
                      t_sys_position p ON ep.posi_id = p.id
				   WHERE p.org_id = eo.org_id)
			   ) and (emp_id in (select emp_id from t_sys_emp_org eo2 inner join t_sys_emp emp on eo2.org_id=emp.id where empcode like %abc% and empname like %abc%))
			  AND org_id = '737090D66F7542889EAD3C1B08314281' 
		ORDER BY org_id
		
		
		 */
		
		
		DetachedCriteria dc = DetachedCriteria.forClass(SysEmpOrg.class, "eo");
		dc.add(Property.forName("sysEmployee.id").notIn(
				DetachedCriteria
				.forClass(SysEmpPosi.class,"ep")
				.setProjection(Property.forName("sysEmployee"))
				.createAlias("ep.sysPosition", "p")
				.add(Restrictions.eqProperty("eo.sysOrganization.id",
						"p.sysOrganization.id"))
		));
		dc.add(Restrictions.eq("sysOrganization.id", id));//只在本机构下
		
		
		
		//dc.add(Property.forName("sysOrganization.id").in(values));
		
		if(StringUtils.isNotBlank(emp.getEmpcode()) || StringUtils.isNotBlank(emp.getEmpname())) {
				DetachedCriteria dc2 = DetachedCriteria.forClass(SysEmpOrg.class);
				dc2.setProjection(Property.forName("sysEmployee"));
				dc2.createAlias("sysEmployee", "se");
				if(StringUtils.isNotBlank(emp.getEmpcode())){
					dc2.add(Property.forName("se.empcode").like(emp.getEmpcode(),MatchMode.ANYWHERE));
				}
				if(StringUtils.isNotBlank(emp.getEmpname())){
					
					dc2.add(Property.forName("se.empname").like(emp.getEmpname(),MatchMode.ANYWHERE));
				}
				
				dc.add(Property.forName("sysEmployee.id").in(dc2));
		}
		dc.addOrder(Order.asc("id"));
		return o_sysEmpOrgDAO.pagedQuery(dc, page);
		
	}
	/**
	 * 查询岗位下的sysempposi 分页
	 * @author 万业
	 * @param page
	 * @param id 岗位ID
	 * @param emp
	 * @return
	 * @throws Exception
	 */
	public com.fhd.core.dao.Page<SysEmployee> querySysEmpByPosiIdPage(com.fhd.core.dao.Page<SysEmployee> page,String id, SysEmployee emp) {
		/*
		 * 原生SQL 保留
		 * select emp.* from ermis4test2.dbo.t_sys_employee emp 
		 * inner join ermis4test2.dbo.t_sys_emp_posi pp 
		 * on emp.id=pp.emp_id where pp.posi_id='18271D3DAB9D3BF31A1C8675FDA2A0C4'
		 */
		
		DetachedCriteria dc=DetachedCriteria.forClass(SysEmployee.class);
		dc.createAlias("sysEmpPosis", "ep");
		dc.add(Restrictions.eq("ep.sysPosition.id", id));
		if(StringUtils.isNotBlank(emp.getEmpcode())){
			dc.add(Restrictions.like("empcode", emp.getEmpcode(), MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(emp.getEmpname())){
			dc.add(Restrictions.like("empname", emp.getEmpname(), MatchMode.ANYWHERE));
		}
		
		return o_sysEmployeeDAO.findPage(dc, page, true);
	}
	
	/**
	 * 查询当前问卷下的所有符合职务的人员
	 * @author Sword
	 * @param IDS人员ID的集合
	 * @return List<SysEmployee>
	 * @since  fhd　Ver 1.1
	 */
	public String querySysEmployeesByIds(String ids){
		if(null == ids || "".equals(ids))
			return null;
		DetachedCriteria dc = DetachedCriteria.forClass(SysEmployee.class);
		dc.add(Property.forName("id").in(ids.split(",")));
		List<SysEmployee> sysEmployeeLists = o_sysEmpOrgDAO.findByCriteria(dc);
		String empName = "";
		if(null != sysEmployeeLists && sysEmployeeLists.size() > 0){
			for(int i = 0 ; i < sysEmployeeLists.size(); i ++){
				SysEmployee sysEmployee = sysEmployeeLists.get(i);
				if(i == sysEmployeeLists.size() - 1){
					empName +=sysEmployee.getUsername();
				}else{
					empName +=sysEmployee.getUsername()+",";
				}
			}
		}
		return empName;
	}
	/**
	 * 根据公司查询其下的所有员工；
	 * @author 陈燕杰
	 * @param company:员工的所在公司；
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<SysEmployee> getAllEmployeeByCompany(SysOrganization company)
	{
		List<SysOrganization> allDep=this.o_organizationBO.getAllDepartmentsByCompanyId(company.getId());
		List<String> allDepIds=new ArrayList<String>();
		for(SysOrganization item:allDep)
		{
			allDepIds.add(item.getId());
		}
		allDepIds.add(company.getId());
		DetachedCriteria dc=DetachedCriteria.forClass(SysEmployee.class);
		dc.add(Restrictions.in("sysOrganization.id", allDepIds));
		List<SysEmployee> result=this.o_sysEmployeeDAO2.findByCriteria(dc);
		return result;
	}
	/**
	 * 查询指定公司下指标名称的人员
	 * @author 陈燕杰
	 * @param name:人员的名称
	 * @param company:所有的公司；
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public SysEmployee getSysEmpByName(String name,SysOrganization company)
	{
		List<SysOrganization> allDep=this.o_organizationBO.getAllDepartmentsByCompanyId(company.getId());
		List<String> allDepIds=new ArrayList<String>();
		for(SysOrganization item:allDep)
		{
			allDepIds.add(item.getId());
		}
		allDepIds.add(company.getId());
		DetachedCriteria dc=DetachedCriteria.forClass(SysEmployee.class);
		dc.add(Restrictions.in("sysOrganization.id", allDepIds));
		dc.add(Restrictions.eq("realname", name));
		List<SysEmployee> result=this.o_sysEmployeeDAO2.findByCriteria(dc);
		if(result.size()>0)
		{
			return result.get(0);
		}
		else
		{
			return null;
		}
	}
	
	
	/**
	 * 根据username查询员工--问卷答题时使用，能登录查看答题的用户一定是该公司的员工.
	 * @param username
	 * @return SysEmployee
	 * @since  fhd　Ver 1.1
	 */
	public SysEmployee questEmployeeByUsername(String username){
		SysEmployee emp = null;
		DetachedCriteria dc = DetachedCriteria.forClass(SysEmployee.class);
		dc.add(Restrictions.eq("username", username));
		List<SysEmployee> empList = o_sysEmployeeDAO2.findByCriteria(dc);
		if(null != empList && empList.size()>0){
			emp = empList.get(0);
		}
		return emp;
	}
	
	/**
	 * 根据查询内容得到员工树的Path集合
	 * 
	 * @author David
	 * @param request
	 * @param response
	 * @return List<String> Path集合
	 * @since fhd Ver 1.1
	 */
	public List<String> getPathsBySearchName(String searchName){
		DetachedCriteria dc = DetachedCriteria.forClass(SysEmployee.class);
		dc.add(Restrictions.ilike("empname", searchName, MatchMode.ANYWHERE));
		dc.add(Restrictions.eq("sysOrganization.id", UserContext.getUser().getCompanyid()));
		dc.addOrder(Order.asc("empname"));
		List<SysEmployee> empList = o_sysEmployeeDAO2.findByCriteria(dc);
		List<String> pathList=new ArrayList<String>();
		for(SysEmployee emp: empList){
			pathList.addAll(getEmpOrgSeqById(emp));
		}
		return pathList;
	}
	
	/**
	 * <pre>
	 * getEmpOrgSeqById:根据员工ID获取组织树SEQ
	 * </pre>
	 * 
	 * @author David
	 * @param empId
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public List<String> getEmpOrgSeqById(SysEmployee emp){
		List<String> rev = new ArrayList<String>();
		
		//1.员工-岗位-部门-公司
		StringBuffer sb1 = new StringBuffer();
		List<SysEmpPosi> eps = o_sysEmpPosiBO.querySysEmpPosiByEmpid(emp.getId());
		if(eps!=null && eps.size()>0){
			for(SysEmpPosi ep:eps){
				SysPosition sp = ep.getSysPosition();
				sb1.append(sp.getSysOrganization().getOrgseq().replace(".", "/")).append(sp.getId()).append("/").append(emp.getId());
				rev.add(sb1.toString());
			}
		}
		
		//2.员工-部门-公司
		StringBuffer sb2 = new StringBuffer();
		List<SysEmpOrg> seos = o_sysEmpOrgBO.querySysEmpOrgByEmpid(emp.getId());
		if(seos!=null && seos.size()>0){
			for(SysEmpOrg seo:seos){
				SysOrganization department = seo.getSysOrganization();
				sb2.append(department.getOrgseq().replace(".", "/")).append(emp.getId());
				rev.add(sb2.toString());
			}
		}
		
		//3.员工-公司
		if((eps==null||eps.size()<1)&&(seos==null || seos.size()<1)){
			StringBuffer sb3 = new StringBuffer();
			sb3.append(emp.getSysOrganization().getOrgseq().replace(".", "/")).append(emp.getId());
			rev.add(sb3.toString());
		}
		
		return rev;
	}
	
	public List<Object[]> findMainDeptByEmpid(String empid) {
		
		String hql1 = "select o.id,o.orgname from SysEmpOrg eo join eo.sysOrganization o where eo.sysEmployee.id=:empid and eo.ismain = 1";
		String hql2 = "select o.id,o.orgname from SysEmpPosi ep join ep.sysPosition p join p.sysOrganization o where ep.sysEmployee.id=:empid";
		
		List<Object[]> list1 = o_sysEmployeeDAO2.createQuery(hql1).setParameter("empid", empid).list();
		List<Object[]> list2 = o_sysEmployeeDAO2.createQuery(hql2).setParameter("empid", empid).list();
		list1.addAll(list2);
		return list1;
		
	}
	
	public String  findDivisionManager(String orgid) {
		String sql = "select emp.id,emp.emp_name from  t_sys_emp_org eo inner join t_sys_employee emp on emp.id = eo.emp_id " +
				"inner join t_sys_user_role ur on ur.user_id = emp.user_id inner join t_sys_role r on r.id = ur.role_id " +
				"where eo.org_id =:orgid and r.role_code =:rolecode";
		List<Object[]> list = o_sysEmployeeDAO2.getSessionFactory().getCurrentSession().createSQLQuery(sql).setParameter("rolecode", "部门经理").setParameter("orgid", orgid).list();
		if(null != list && list.size() > 0) {
			Object[] objects = list.get(0);
			return objects[0].toString();
		} 
		return null;
	}
	/**
	 * <pre>
	 * 根据角色id分页查询员工
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param page
	 * @param roleid
	 * @param sysEmployee
	 * @since  fhd　Ver 1.1
	*/
	//TODO  
	public void findEmpByRoleId(com.fhd.core.dao.Page<SysEmployee> page,
			String roleid, SysEmployee emp) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(SysEmployee.class);
		dc.createCriteria("sysUser", "u");
		dc.createCriteria("u.sysRoles", "r");
		dc.add(Restrictions.eq("r.id", roleid));
		if(StringUtils.isNotBlank(emp.getEmpcode())){
			dc.add(Restrictions.like("empcode", emp.getEmpcode()));
		}
		if(StringUtils.isNotBlank(emp.getEmpname())){
			dc.add(Restrictions.like("empname", emp.getEmpname()));
		}
		
		o_sysEmployeeDAO.findPage(dc , page, false);
		
	}
	
	/**
	 * <pre>
	 * 根据角色查询本公司的所有员工
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param companyId
	 * @param roleId
	 * @param emp
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<SysEmployee> findAllEmpByRoleId(String companyId, String roleId, SysEmployee emp) {
		
		Criteria criteria = o_sysEmployeeDAO.createCriteria();
		criteria.createCriteria("sysUser", "u");
		criteria.createCriteria("u.sysRoles", "r");
		criteria.add(Restrictions.eq("r.id", roleId));
		criteria.add(Restrictions.eq("sysOrganization.id", companyId));
		if(StringUtils.isNotBlank(emp.getEmpname())){
			criteria.add(Restrictions.or(Restrictions.like("empcode", emp.getEmpname(), MatchMode.ANYWHERE), Restrictions.like("empname", emp.getEmpname(), MatchMode.ANYWHERE)));
		}
		
		return criteria.list();
		
	}
	
	
	
}

