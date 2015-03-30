/**
 * SysUserBO.java
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
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.support.Page;
import com.fhd.sys.business.log.BusinessLogBO;
import com.fhd.sys.business.orgstructure.EmpolyeeBO;
import com.fhd.sys.dao.auth.SysRoleDAOold;
import com.fhd.sys.dao.auth.SysUserDAO;
import com.fhd.sys.entity.auth.SysAuthority;
import com.fhd.sys.entity.auth.SysRole;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * 用户BO类.
 * 
 * @author wudefu
 * @version V1.0 创建时间：2010-8-30 Company FirstHuiDa.
 */

@Service
@SuppressWarnings("unchecked")
public class SysUserBO {

	
	@Autowired
	private SysUserDAO o_sysUserDAO;
	@Autowired
	private EmpolyeeBO o_empolyeeBO;
	@Autowired
	private BusinessLogBO o_businessLogBO;
//	@Autowired
//	private SysRoleUserDAO o_sysRoleUserDAO;
	@Autowired
	private SysRoleDAOold o_sysRoleDAO;

	/**
	 * 新增用户.
	 * @author 吴德福
	 * @param sysUser
	 * @return Object
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public Object saveUser(SysUser sysUser) {
		SysUser user = new SysUser();
		try {
			user = (SysUser) o_sysUserDAO.merge(sysUser);
			/*
			// 新增用户时默认为每个用户插入3个portlet：任务列表、新闻、公告，以后维护portal时修改
			if(null != sysUser.getPortal() && "1".equals(sysUser.getPortal())){
				OperatorDetails operator = UserContext.getUser();
				for(int i=1;i<4;i++){
					//新增portal
					Portal portal = new Portal();
					portal.setId(GuidGeneratorUtils.getUUID32());
					portal.setSort(i);
					portal.setCol(i);
					portal.setUpdateTime(new Date());
					portal.setSysUser(sysUser);
					portal.setOperator(operator.getUsername());
					o_portalBO.updatePortal(portal);
					
					//新增portlet
					Portlet portlet = new Portlet();
					portlet.setId(GuidGeneratorUtils.getUUID32());
					portlet.setSort(i);
					portlet.setHeight("300");
					if(i==1){
						portlet.setTitle("任务列表");
						portlet.setUrl("/jbpm/ifarme.do");
					}else if(i==2){
						portlet.setTitle("通知公告");
						portlet.setUrl("/sys/portal/advicesPublish.do");
					}else if(i==3){
						portlet.setTitle("新闻");
						portlet.setUrl("/sys/portal/newsPublish.do");
					}
//					portlet.setPortal(portal);
					portlet.setOperator(operator.getUsername());
					portlet.setUpdateTime(new Date());
					o_portletBO.mege(portlet);
				}
			}
			*/
			o_businessLogBO.saveBusinessLogInterface("新增", "用户", "成功", sysUser.getUsername(), sysUser.getRealname(), sysUser.getPassword());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.saveBusinessLogInterface("新增", "用户", "失败", sysUser.getUsername(), sysUser.getRealname(),sysUser.getPassword());
		}
		return user;
	}

	/**
	 * 修改用户.
	 * @author 吴德福
	 * @param sysUser 用户.
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void updateUser(SysUser sysUser) {
		try {
			o_sysUserDAO.merge(sysUser);
			o_businessLogBO.modBusinessLogInterface("修改", "用户", "成功", sysUser.getId(), sysUser.getUsername());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.modBusinessLogInterface("修改", "用户", "失败", sysUser.getId(), sysUser.getUsername());
		}
	}
	/**
	 * 删除用户.
	 * @author 吴德福
	 * @param id 用户id.
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void removeUser(String id) {
		try {
			//删除人时逻辑删除，不要物理删除，关联太多
			SysUser user = o_sysUserDAO.get(id);
			user.setEnable(false);
			o_sysUserDAO.merge(user);
//			o_sysUserDAO.removeById(id);
			o_businessLogBO.delBusinessLogInterface("删除", "用户", "成功", id);
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.delBusinessLogInterface("删除", "用户", "失败", id);
		}
	}

	/**
	 * 根据id查询用户.
	 * @author 吴德福
	 * @param id 用户id.
	 * @return SysUser 用户.
	 * @since fhd　Ver 1.1
	 */
	public SysUser queryUserById(String id) {
		Criteria criteria = o_sysUserDAO.createCriteria(Restrictions.eq("id",id));
		criteria.setFetchMode("sysRoles", FetchMode.SELECT);
		criteria.setFetchMode("sysAuthorities", FetchMode.SELECT);
		List<SysUser> list = criteria.list();
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	/**
	 * 查询所有用户.
	 * @author 吴德福
	 * @return List<SysUser> 用户集合.
	 * @since fhd　Ver 1.1
	 */
	public List<SysUser> queryAllUser() {
		return o_sysUserDAO.find("from SysUser where enable=true");
	}

	/**
	 * 根据查询条件查询用户.
	 * @author 吴德福
	 * @param username 用户名称.
	 * @return List<SysUser> 用户集合.
	 * @since fhd　Ver 1.1
	 */
	public List<SysUser> query(String username) {
		Criteria criteria = o_sysUserDAO.createCriteria();
		criteria.setFetchMode("sysRoles", FetchMode.SELECT);
		criteria.setFetchMode("sysAuthorities", FetchMode.SELECT);
		criteria.add(Restrictions.like("username", username, MatchMode.ANYWHERE));
		criteria.add(Restrictions.eq("enable", true));//过滤无效的
		return criteria.list();
	}
	public List<SysUser> queryUnique(String username) {
		Criteria criteria = o_sysUserDAO.createCriteria();
		criteria.setFetchMode("sysRoles", FetchMode.SELECT);
		criteria.setFetchMode("sysAuthorities", FetchMode.SELECT);
		criteria.add(Restrictions.eq("username", username));
		criteria.add(Restrictions.eq("enable", true));//过滤无效的
		return criteria.list();
	}
	/**
	 * 根据查询条件查询用户. 有分页功能
	 * @param username
	 * @param page
	 */
	public String queryPage(String username,String realname, String roleId, Page<SysUser> page,String sort,String dir) {
		
		DetachedCriteria criteria =DetachedCriteria.forClass(SysUser.class);
		if(StringUtils.isNotBlank(username)){
			criteria.add(Restrictions.like("username", username, MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(realname)){
			criteria.add(Restrictions.like("realname", realname, MatchMode.ANYWHERE));
		}
		criteria.add(Restrictions.eq("enable", true));
		if("ASC".equalsIgnoreCase(dir)) {
			if(!"id".equals(sort)){
				criteria.addOrder(Order.asc(sort));
			}else if("id".equals(sort)){
				criteria.addOrder(Order.asc("username"));
			}
		}else {
			if(!"id".equals(sort)){
				criteria.addOrder(Order.desc(sort));
			}else if("id".equals(sort)){
				criteria.addOrder(Order.desc("username"));
			}
		}
		o_sysUserDAO.pagedQuery(criteria, page);
		
		//查函有此Role的用户
		StringBuffer userIdinRole=new StringBuffer("");
		if(StringUtils.isNotBlank(roleId)){
			SysRole role= o_sysRoleDAO.get(roleId);
			Set<SysUser> userSet=role.getSysUsers();
			for (SysUser user : page.getList()){
				if(userSet.contains(user)){
					//userIdinRole.append(user.getId()+",");
					//作为前台是否为已经选定的标记
					user.setPassword("checked");
				}
				//String hql="from SysRoleUser ru where ru.sysUser.id='"+user.getId()+"' and ru.sysRole.id='"+roleId+"'";
				//String hql="from u from SysUser where "
				
				/*List list=this.o_sysRoleUserDAO.createQuery(hql).list();
				if(list.size()>0){
				}*/
			}
			
		}
		return userIdinRole.toString();
		
	}

	/**
	 * 登录时判断用户名是否可用.
	 * @author 吴德福
	 * @param username
	 * @return List<SysUser>
	 * @since fhd　Ver 1.1
	 */
	public List<SysUser> queryLoginUsername(String username) {
		Criteria criteria = o_sysUserDAO.createCriteria();
		criteria.setFetchMode("sysRoles", FetchMode.SELECT);
		criteria.setFetchMode("sysAuthorities", FetchMode.SELECT);
		criteria.add(Restrictions.eq("username", username));
		return criteria.list();
	}

	/**
	 * 登录时判断密码是否正确.
	 * @author 吴德福
	 * @param username
	 * @param password
	 * @return List<SysUser>
	 * @since fhd　Ver 1.1
	 */
	public List<SysUser> queryLoginUser(String username, String password) {
		Criteria criteria = o_sysUserDAO.createCriteria();
		criteria.setFetchMode("sysRoles", FetchMode.SELECT);
		criteria.setFetchMode("sysAuthorities", FetchMode.SELECT);
		criteria.add(Restrictions.eq("username", username));
		criteria.add(Restrictions.eq("password", password));
		return criteria.list();
	}

	/**
	 * 根据员工id查询操作员.
	 * @author 吴德福
	 * @param id 员工id.
	 * @return SysUser 用户.
	 * @since fhd　Ver 1.1
	 */
	public SysUser getSysUserByEmpId(String id) {
		SysUser sysUser = null;
		SysEmployee sysEmployee = o_empolyeeBO.get(id);
		if (null != sysEmployee && !"".equals(sysEmployee.getUserid())) {
			sysUser = o_sysUserDAO.get(sysEmployee.getUserid());
		}
		return sysUser;
	}

	/**
	 * 根据用户名查询用户.
	 * @author 吴德福
	 * @param username 用户名.
	 * @return SysUser 用户.
	 * @since fhd　Ver 1.1
	 */
	public SysUser getByUsername(String username) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysUser.class);
		dc.add(Restrictions.eq("username", StringUtils.lowerCase(username.trim())));
		dc.add(Restrictions.eq("enable", true));
		List<SysUser> sysUserList = o_sysUserDAO.findByCriteria(dc);
		if(null != sysUserList && sysUserList.size()>0){
			return sysUserList.get(0);
		}
		return null;
	}

	/**
	 * 查询用户当前拥有的权限.
	 * @author 吴德福
	 * @param userid 用户id.
	 * @return List<String> 选中的权限集合.
	 * @since fhd　Ver 1.1
	 */
	public List<String> queryAuthorityByUser(String userid) {
		List<String> authorityIds = new ArrayList<String>();
		SysUser sysUser = o_sysUserDAO.get(userid);
		for (SysAuthority authority : sysUser.getSysAuthorities()) {
			if(authority.getIsLeaf())
				authorityIds.add(authority.getId());
		}
		return authorityIds;
	}
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public SysUser get(String userId){
		return this.o_sysUserDAO.get(userId);
		
	}
	
	/**
	 * 修改用户权限.
	 * @author David
	 * @param sysUser 用户.
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void updateUserPermissions(SysUser sysUser,String operatorId,String roleStr) {
		try {
			o_sysUserDAO.merge(sysUser);
			o_businessLogBO.addModBusinessLog(operatorId,"修改", "用户权限", "成功", sysUser.getId(), roleStr);
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.addModBusinessLog(operatorId,"修改", "用户权限", "失败", sysUser.getId(), roleStr);
		}
	}
}
