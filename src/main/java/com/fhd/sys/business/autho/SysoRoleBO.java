/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */


package com.fhd.sys.business.autho;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.Page;
import com.fhd.sys.dao.autho.SysoRoleDAO;
import com.fhd.sys.dao.autho.SysoUserDAO;
import com.fhd.sys.dao.orgstructure.SysEmpPosiDAO;
import com.fhd.sys.dao.orgstructure.SysEmployeeDAO;
import com.fhd.sys.dao.orgstructure.SysOrgDao;
import com.fhd.sys.dao.orgstructure.SysPositionDAO;
import com.fhd.sys.entity.auth.SysRole;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.orgstructure.SysEmpPosi;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;
import com.fhd.sys.entity.orgstructure.SysPosition;



/**
 * ClassName:SysRoleTreeBO
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   翟辉
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-1-8		上午11:32:00
 *
 * @see 	 
 */
@Service
public class SysoRoleBO {

	@Autowired
	private SysoRoleDAO o_sysoRoleDAO;


	@Autowired
	private SysoUserDAO o_sysoUserDAO;
	
	@Autowired
	private SysEmployeeDAO o_sysEmployeeDAO;
	
	@Autowired
	private SysOrgDao o_sysOrgDao;
	
	@Autowired
	private SysPositionDAO o_sysPositionDAO;
	
	@Autowired
	private SysEmpPosiDAO o_sysEmpPosiDAO;
	
	/**
	 * 角色树
	 * @author 翟辉
	 * @param query
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  Map<String, Object> treeLoader(String query){
	Map<String, Object> item = new HashMap<String, Object>();
		List<SysRole> sysRoleList = null;
		List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
		if(query == null)
			sysRoleList = o_sysoRoleDAO.getAll();
		else{				
			Criteria criteria = o_sysoRoleDAO.createCriteria();
			criteria.add(Restrictions.like("roleName", query, MatchMode.ANYWHERE));
			sysRoleList = criteria.list();
		}
		for (SysRole sysRole : sysRoleList) {
			Map<String, Object> node = new HashMap<String, Object>();
			node.put("id", sysRole.getId());
			node.put("text", sysRole.getRoleName());
			node.put("leaf", true);
			node.put("cls", "role");
			children.add(node);
		}
		item.put("children", children);
		return item;
	}

	/**
	 * 查询分页
	 * @author 翟辉
	 * @param start 开始条数
	 * @param limit 结束条数
	 * @param query 查询条件
	 * @param sort 排序字段及方式
	 * @return Map<String, Object>
	 * @throws Exception
	*/
	public Page<SysEmployee> findrolePage(int start, int limit, String query, String sort, String roleId){
		String property = "";
		String direction = "";
		Page<SysEmployee> page = new Page<SysEmployee>();
//		Page<SysUser> page = new Page<SysUser>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		if (StringUtils.isNotBlank(sort)){
            JSONArray jsonArray = JSONArray.fromObject(sort);
            if (jsonArray.size() > 0){
                JSONObject jsobj = jsonArray.getJSONObject(0);
                property = jsobj.getString("property");
                direction = jsobj.getString("direction");
                
                if(property.equalsIgnoreCase("zq")){
                	property = "isRecycle";
    			}else if(property.equalsIgnoreCase("statusName")){
    				property = "status";
    			}else if(property.equalsIgnoreCase("triggerName")){
    				property = "triggerType";
    			}
            }
        }else{
        	property = "username";
        	direction = "ASC";
        }
		//page = this.findPlanBySome(query, page, property, direction, roleId);
		//long l1 = System.currentTimeMillis();
		page = this.findPlanBySomes(query, page, property, direction, roleId);
		//List<SysEmployee> employees = findPlanBySomes(start,query, limit, property, direction, roleId);
		//long l2 = System.currentTimeMillis();
		//System.out.println("====="+(l2-l1));
		return page;	
	}
	
	

	public Page<SysEmployee> findPlanBySomes(String query, Page<SysEmployee> pages, String sort, String dir, String roleId) {
		DetachedCriteria dcEmployee = DetachedCriteria.forClass(SysEmployee.class);
		dcEmployee.createAlias("sysUser", "sysUser",CriteriaSpecification.LEFT_JOIN);
		dcEmployee.createAlias("sysUser.sysRoles", "sysRoles",CriteriaSpecification.LEFT_JOIN);
		dcEmployee.createAlias("sysOrganization", "company",CriteriaSpecification.LEFT_JOIN);
		dcEmployee.setFetchMode("company", FetchMode.JOIN);
		dcEmployee.createAlias("sysEmpPosis", "sysEmpPosis",CriteriaSpecification.LEFT_JOIN);
		dcEmployee.createAlias("sysEmpPosis.sysPosition", "position",CriteriaSpecification.LEFT_JOIN);
		dcEmployee.createAlias("position.sysOrganization", "pos",CriteriaSpecification.LEFT_JOIN);
		dcEmployee.setFetchMode("pos", FetchMode.JOIN);
		dcEmployee.createAlias("sysEmpOrgs", "sysEmpOrgs",CriteriaSpecification.LEFT_JOIN);
		dcEmployee.createAlias("sysEmpOrgs.sysOrganization", "dept",CriteriaSpecification.LEFT_JOIN);
		dcEmployee.setFetchMode("dept", FetchMode.JOIN);
		dcEmployee.add(Restrictions.eq("sysRoles.id", roleId));
		if(query !=null){
			dcEmployee.add(Restrictions.like("realname", query, MatchMode.ANYWHERE));
		}
		if("ASC".equalsIgnoreCase(dir)) {
			dcEmployee.addOrder(Order.asc(sort));
		} else {
			dcEmployee.addOrder(Order.desc(sort));
		}	
		return o_sysEmployeeDAO.findPage(dcEmployee, pages, false);
		
	}
	

	
	/**
	 * 添加角色
	 * @author 翟辉
	 * @param role
	 */
	@Transactional
	public void saveRole(SysRole role) {
		o_sysoRoleDAO.merge(role);
	}
	/**
	 * 删除角色
	 * @author 翟辉
	 * @param roleId
	 */
	@Transactional
	public void removeRole(String roleId) {
		if (StringUtils.isEmpty(roleId)) {
		    return;
		}
		 SysRole sysRole = o_sysoRoleDAO.get(roleId);
		 o_sysoRoleDAO.delete(sysRole);
	}
	
	/**
	 *修改角色
	 * @author 翟辉
	 * @param sysRole角色实体
	 */
	@Transactional
	public void mergeRole(SysRole sysRole) {
		o_sysoRoleDAO.merge(sysRole);
	}
	/**
	 *修改角色
	 * @author 翟辉
	 * @param sysRole角色实体
	 */
	@Transactional
	public void mergeUser(SysUser sysUser) {
		o_sysoUserDAO.merge(sysUser);
	}
	
	/**
	 * 根据角色编号判断角色是否存在.
	 * @author 翟辉
	 * @param roleCode
	 * @return List<SysRole>
	 */
	@SuppressWarnings("unchecked")
	public List<SysRole> isExistByRoleCode(String roleCode){
		Criteria criteria = o_sysoRoleDAO.createCriteria();
		criteria.add(Restrictions.eq("roleCode", roleCode.trim()));
		return criteria.list();
	}
	/**
	 * 根据角色名称判断角色是否存在.
	 * @author 翟辉
	 * @param roleName
	 * @return List<SysRole>
	 * @since  fhd　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	public List<SysRole> isExistByRoleName(String roleName){
		Criteria criteria = o_sysoRoleDAO.createCriteria();
		criteria.add(Restrictions.eq("roleName", roleName.trim()));
		return criteria.list();
	}
	
	
	/**
	 *通过ID查询角色
	 * @author 翟辉
	 * @param id
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public SysRole queryRoleById(String id) {
		return o_sysoRoleDAO.get(id);
	}
	
	/**
	 *通过ID查询用户
	 * @author 翟辉
	 * @param id
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public SysUser queryUserById(String id) {
		return o_sysoUserDAO.get(id);
	}
	
	/**
	 * 为角色添加用户.
	 * @author 翟辉
	 * @param roleId 
	 * @param roleArray
	 */
	@Transactional
	public void addUserRole(String roleId,JSONArray roleArray){
			
		SysRole role=o_sysoRoleDAO.get(roleId);
		//清空当前所有关系
		for(SysUser sysUser:role.getSysUsers()){
			sysUser.getSysRoles().remove(role);
			//o_sysUserDAO.update(sysUser);//关系只交给user方来维护
		}
		if(null==roleArray || roleArray.size()==0){
			return;
		}
		String EmployeeId="";
		for(int i=0;i<roleArray.size();i++){
			EmployeeId = (String)JSONObject.fromObject(roleArray.get(i)).get("id");
			String roleUserId =  this.querySysEmployeeById(EmployeeId).getUserid();
			if(StringUtils.isNotBlank(roleUserId)){
				SysUser sysUser= o_sysoUserDAO.get(roleUserId);
				//role.getSysUsers().add(sysUser);
				sysUser.getSysRoles().add(role);
				o_sysoUserDAO.merge(sysUser);
			}
		}
	}
	
	/**
	 * 通过角色Id查询用户信息
	 * @author 翟辉
	 * @param roleId
	 * @return
	 */
	@Transactional
	public Set<SysUser> fingUserByRoleId(String roleId){
		SysRole role = o_sysoRoleDAO.get(roleId);
		Set<SysUser> users = role.getSysUsers();
		return users;
	}

	/**
	 * 通过字符串删除角色下的人员
	 * @author 翟辉
	 * @param roleId 角色
	 * @param selectIds 已选ID employee id
	 */
	@Transactional
	public void deleteRoleUser(String roleItem){
		JSONArray jsarr = JSONArray.fromObject(roleItem);
		 for (int i = 0; i < jsarr.size(); i++){
			 JSONObject jsobj = jsarr.getJSONObject(i);
			String  roleId = jsobj.getString("roleId");
			String  userId = jsobj.getString("userId"); 
			deleteRoleUserById(roleId,userId);
		 }
	}
	
	/**
	 * 通过角色id和用户id删除角色用户关联表关联数据
	 * @author 翟辉
	 * @param roleId角色Id
	 * @param userId用户Id
	 */
	@Transactional
	public void deleteRoleUserById(String roleId,String userId){
		String sql = "delete from t_sys_user_role where ROLE_ID = ? and USER_ID= ? ";
		SQLQuery sqlquery = o_sysoRoleDAO.createSQLQuery(sql, roleId,userId);
		sqlquery.executeUpdate();
	}
	
	
	/**
	 *通过字符串添加角色下的人员
	 * @author 翟辉
	 * @param roleId
	 * @param roleItem
	 */
	@Transactional
	public void addRoleUser(String roleId,String roleItem){
		JSONArray jsarr = JSONArray.fromObject(roleItem);
		SysRole role=o_sysoRoleDAO.get(roleId);
		 for (int i = 0; i < jsarr.size(); i++){
			JSONObject jsobj = jsarr.getJSONObject(i);
			String  employeeId = jsobj.getString("id");
			String roleUserId =  this.querySysEmployeeById(employeeId).getUserid();
			SysUser sysUser= o_sysoUserDAO.get(roleUserId);
			//role.getSysUsers().add(sysUser);
			sysUser.getSysRoles().add(role);
			o_sysoUserDAO.merge(sysUser);
		 } 
	}
	
	
	/**
	 * 通过用户得到公司机构
	 * @author 翟辉
	 * @param sysUser
	 *	
	 */
	public String  queryOrgByUserId(SysUser sysUser){
		String sysOrganizationName = null;
		SysEmployee sysEmployee = this.queryEmpObjByUserId(sysUser);
		if(sysEmployee==null){
			return sysOrganizationName = "";
		}
		SysOrganization sysOrganization =  sysEmployee.getSysOrganization();
		if(sysOrganization == null){
			sysOrganizationName = "";
		}else{
			sysOrganization = this.queryOrganizationIdById(sysOrganization.getId());
			sysOrganizationName = sysOrganization.getOrgname();
		}
		return sysOrganizationName;
	}
	
	/**
	 * 通过用户得到岗位
	 * @author 翟辉
	 * @param sysUser
	 *	
	 *//*
	@SuppressWarnings("deprecation")
	public String  queryPosiByUserId(SysUser sysUser){
		SysEmployee sysEmployee = queryEmpObjByUserId(sysUser);//得到员工实体
		String sysPositionName = null;
		if(sysEmployee ==null){
			return sysPositionName = "";
		}
		List<SysEmpPosi> sysEmpPosiList = null;
		String sysEmployeeId = sysEmployee.getId();//员工实体id
		Criteria criteria = o_sysEmpPosiDAO.createCriteria();
		criteria.add(Restrictions.eq("sysEmployee.id", sysEmployeeId));
		sysEmpPosiList = criteria.list();
		if(sysEmpPosiList.size()==0){
			 sysPositionName ="";
		}else{
			String sysEmpPosiId = sysEmpPosiList.get(0).getId();
			SysEmpPosi sysEmpPosi = querySysEmpPosiById(sysEmpPosiId);//得到关联表实体
			String posiId = sysEmpPosi.getSysPosition().getId();
			SysPosition sysPosition	= querySysPositionById(posiId);
			sysPositionName = sysPosition.getPosiname();
		}
		return sysPositionName;
	}*/
	
	
	/**
	 * 通过用户得到机构实体
	 * @author 翟辉
	 * @param sysUser
	 *	
	 */
	@SuppressWarnings("unchecked")
	public SysEmployee  queryEmpObjByUserId(SysUser sysUser){
		String sysUserId = sysUser.getId();
		List<SysEmployee> sysEmployeeList = null;
		Criteria criteria = o_sysEmployeeDAO.createCriteria();
		criteria.add(Restrictions.eq("userid", sysUserId));
		sysEmployeeList = criteria.list();
		if(sysEmployeeList.size()==0){
			return null;
		}else{
			String   sysEmployeeId  = sysEmployeeList.get(0).getId();
			SysEmployee sysEmployee	 = this.querySysEmployeeById(sysEmployeeId);
			return sysEmployee;
		}
	}
		
	/**
	 * 通过ID查询SysOrganization
	 * @author 翟辉
	 * @param id
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public SysOrganization queryOrganizationIdById(String id) {
		return o_sysOrgDao.get(id);
	}
	
	/**
	 * 通过ID查询SysEmployee
	 * @author 翟辉
	 * @param id
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public SysEmployee querySysEmployeeById(String id) {
		return o_sysEmployeeDAO.get(id);
	}
	
	/**
	 * 通过ID查询SysPosition
	 * @author 翟辉
	 * @param id
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@SuppressWarnings("deprecation")
	public SysPosition querySysPositionById(String id) {
		return o_sysPositionDAO.get(id);
	}
	
	/**
	 * 通过ID查询SysEmpPosi
	 * @author 翟辉
	 * @param id
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@SuppressWarnings("deprecation")
	public SysEmpPosi querySysEmpPosiById(String id) {
		return o_sysEmpPosiDAO.get(id);
	}
	
	/**
	 * 查询SysEmployee实体以map展现
	 * @author 翟辉
	 * @param id
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@SuppressWarnings("deprecation")
	public SysEmpPosi querySysEmployee(String id) {
		return o_sysEmpPosiDAO.get(id);
	}
	
	
	 /**
	  * 得到所有Emp实体
	  * @return
	  */
	 @SuppressWarnings("unchecked")
		public List<SysEmployee> allSysEmployee() {
			Criteria criteria = o_sysEmployeeDAO.createCriteria();
			return criteria.list();
	 }
	
}

