/**
 * PortalBO.java
 * com.fhd.fdc.commons.business.sys.portal
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-10-20 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.business.portal;

import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.utils.Identities;
import com.fhd.sys.business.log.BusinessLogBO;
import com.fhd.sys.dao.portal.PortalDAO;
import com.fhd.sys.dao.portal.PortalPortletDAO;
import com.fhd.sys.entity.portal.Portal;
import com.fhd.sys.entity.portal.PortalPortlet;

/**
 * PortalBO类.
 * @author   wudefu
 * @version V1.0  创建时间：2010-10-20
 * @since    Ver 1.1
 * @Date	 2010-10-20		下午12:45:33
 * Company FirstHuiDa.
 * @see 	 
 */
@Service
public class PortalBO {

	@Autowired
	private PortalDAO o_portalDAO;
	@Autowired
	private PortalPortletDAO o_portalPortletDAO;
	@Autowired
	private BusinessLogBO o_businessLogBO;
	
	/**
	 * 添加portal.
	 * @author 吴德福
	 * @param portal
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void savePortal(Portal portal){
		try {
			o_portalDAO.merge(portal);
			o_businessLogBO.saveBusinessLogInterface("新增", "portal", "成功", portal.getId());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.saveBusinessLogInterface("新增", "portal", "失败", portal.getId());
		}
	}
	/**
	 * 删除portal.
	 * @author 吴德福
	 * @param id
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void removePortal(String id){
		try {
			o_portalDAO.removeById(id);
			o_businessLogBO.delBusinessLogInterface("删除", "portal", "成功", id);
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.delBusinessLogInterface("删除", "portal", "失败", id);
		}
	}
	/**
	 * 根据id查询portal.
	 * @author 吴德福
	 * @param id
	 * @return Portal
	 * @since  fhd　Ver 1.1
	 */
	public Portal queryPortalById(String id){
		return o_portalDAO.get(id);
	}
	/**
	 * 查询所有的portal.
	 * @author 吴德福
	 * @return List<Portal>
	 * @since  fhd　Ver 1.1
	 */
	public List<Portal> queryAllPortal(){
		return o_portalDAO.getAll();
	}
	/**
	 * 查询用户id的portal.
	 * @author 吴德福
	 * @param userid
	 * @return List<Portal>
	 * @since  fhd　Ver 1.1
	 */
	public List<Portal> queryPortalsByUserid(String userid){
		return o_portalDAO.findBy("sysUser.id", userid, "sort", true);
	}
	/**
	 * 查询角色id下的所有的portals.
	 * @author 吴德福
	 * @param roleid
	 * @return List<Portal>
	 * @since  fhd　Ver 1.1
	 */
	public List<Portal> queryPortalsByRoleid(String roleid){
		return o_portalDAO.findBy("sysRole.id", roleid, "sort", true);
	}
	/**
	 * 根据用户ID查询当前使用的面板布局，如果不存在，则查询系统默认的面板布局.
	 * @author 杨鹏
	 * @param userid
	 * @return Portal
	 */
	@SuppressWarnings("unchecked")
	public Portal queryUsePortalsByUserid(String userid){
		Portal portal=new Portal();
		List<Portal> list = o_portalDAO.findBy("sysUser.id", userid, "sort", true);
		if(list.size()==0){
			DetachedCriteria criteria=DetachedCriteria.forClass(portal.getClass());
			criteria.add(Restrictions.isNull("sysUser.id"));
			list = o_portalDAO.findByCriteria(criteria);
		}
		if(list.size()>0){
			portal=list.get(0);
		}
		return portal;
	}
	/**
	 * 判断当前用户是否存在portal.
	 * @author 吴德福
	 * @param userid
	 * @return Portal
	 * @since  fhd　Ver 1.1
	 */
	public Portal isExistPortalByUserid(String userid){
		Portal portal = null;
		List<Portal> list = o_portalDAO.findBy("sysUser.id", userid, "sort", true);
		if(null != list && list.size()>0){
			portal=list.get(0);
		}
		return portal;
	}
	/**
	 * 保存或更新面板布局
	 * @author 杨鹏
	 * @param portal
	 * @throws Exception 
	 */
	@Transactional
	public void saveOrUpdatePortal(Portal portal1) throws Exception{
		Portal portal2 = this.queryPortalById(portal1.getId());
		Portal portal=null;
		if(null == portal2.getSysUser()){
			portal1.setId(Identities.uuid());
			portal=portal1;
		}else{
			portal=portal2;
			BeanUtils.copyProperties(portal1, portal);
		}
		try {
			o_portalPortletDAO.removeByPortalId(portal.getId());
			Set<PortalPortlet> subPortalPortlet = portal.getSubPortalPortlet();
			portal.setSubPortalPortlet(null);
			o_portalDAO.merge(portal);
			for (PortalPortlet portalPortlet : subPortalPortlet) {
				o_portalPortletDAO.save(portalPortlet);
			}
			o_businessLogBO.modBusinessLogInterface("更改", "面板布局", "成功", portal.getId());
		} catch (Exception e) {
			o_businessLogBO.modBusinessLogInterface("更改", "面板布局", "失败", portal.getId());
			throw e;
		}
	}
	/**
	 * 更新面板布局
	 * @author 杨鹏
	 * @param portal
	 * @throws Exception 
	 */
	@Transactional
	public void updatePortal(Portal portal) throws Exception{
		try {
			o_portalPortletDAO.removeByPortalId(portal.getId());
			o_portalDAO.update(portal);
			Set<PortalPortlet> subPortalPortlet = portal.getSubPortalPortlet();
			for (PortalPortlet portalPortlet : subPortalPortlet) {
				portalPortlet.setId(Identities.uuid());
				o_portalPortletDAO.save(portalPortlet);
			}
			o_businessLogBO.modBusinessLogInterface("更改", "面板布局", "成功", portal.getId());
		} catch (Exception e) {
			o_businessLogBO.modBusinessLogInterface("更改", "面板布局", "失败", portal.getId());
			throw e;
		}
	}
	/**
	 * 查询系统面板布局
	 * @author 杨鹏
	 * @param userid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Portal querySysPortal(){
		Portal portal=new Portal();
		DetachedCriteria criteria=DetachedCriteria.forClass(portal.getClass());
		criteria.add(Restrictions.isNull("sysUser.id"));
		List<Portal> list = o_portalDAO.findByCriteria(criteria);
		if(list.size()>0){
			portal=list.get(0);
		}
		return portal;
	}
	/**
	 * 删除非系统面板布局
	 * @author 杨鹏
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void removeUserPortal() throws Exception{
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(Portal.class);
			criteria.add(Restrictions.isNotNull("sysUser.id"));
			List<Portal> list = o_portalDAO.findByCriteria(criteria);
			for (Portal portal : list) {
				this.removePortal(portal);
			}
			o_businessLogBO.delBusinessLogInterface("更改", "面板布局", "成功", "非系统");
		} catch (Exception e) {
			o_businessLogBO.delBusinessLogInterface("更改", "面板布局", "失败", "非系统");
			throw e;
		}
	}
	/**
	 * 级联删除面板布局
	 * @author 杨鹏
	 * @throws Exception 
	 */
	@Transactional
	public void removePortal(Portal portal) throws Exception{
		try {
			Set<PortalPortlet> subPortalPortlet = portal.getSubPortalPortlet();
			o_portalPortletDAO.removeAll(subPortalPortlet);
			o_portalDAO.remove(portal);
			o_businessLogBO.delBusinessLogInterface("更改", "面板布局", "成功", portal.getId());
		} catch (Exception e) {
			o_businessLogBO.delBusinessLogInterface("更改", "面板布局", "失败", portal.getId());
			throw e;
		}
	}
	/**
	 * 新增或者修改portal信息.
	 * @author 吴德福
	 * @param portal
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void mergePortal(Portal portal) throws Exception{
		try {
			Portal isExistPortal = this.queryUsePortalsByUserid(portal.getSysUser().getId());
			if(null != isExistPortal){
				String hql = "delete from PortalPortlet where portal.id = ?";
				//先删除当前用户已经存在的portal与portlet关联信息
				o_portalPortletDAO.createQuery(hql, isExistPortal.getId()).executeUpdate();
			}
			Set<PortalPortlet> subPortalPortlet = portal.getSubPortalPortlet();
			portal.setSubPortalPortlet(null);
			//保存portal信息
			o_portalDAO.merge(portal);
			for (PortalPortlet portalPortlet : subPortalPortlet) {
				//保存portal和portlet关联信息
				o_portalPortletDAO.save(portalPortlet);
			}
			o_businessLogBO.modBusinessLogInterface("更改", "面板布局", "成功", portal.getId());
		} catch (Exception e) {
			o_businessLogBO.modBusinessLogInterface("更改", "面板布局", "失败", portal.getId());
			throw e;
		}
	}
}

