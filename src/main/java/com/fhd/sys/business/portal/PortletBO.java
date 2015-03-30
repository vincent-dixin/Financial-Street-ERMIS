/**
 * PortletBO.java
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

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.support.Page;
import com.fhd.sys.business.log.BusinessLogBO;
import com.fhd.sys.dao.portal.PortletDAO;
import com.fhd.sys.entity.portal.Portlet;

/**
 * PortletBO类.
 * @author   wudefu
 * @version V1.0  创建时间：2010-10-20
 * @since    Ver 1.1
 * @Date	 2010-10-20		下午12:45:33
 * Company FirstHuiDa.
 * @see 	 
 */
@Service
@SuppressWarnings("unchecked")
public class PortletBO {

	@Autowired
	private PortletDAO o_portletDAO;
	@Autowired
	private BusinessLogBO o_businessLogBO;
	
	/**
	 * 添加portlet.
	 * @author 吴德福
	 * @param portlet
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void savePortlet(Portlet portlet){
		try {
			o_portletDAO.merge(portlet);
			o_businessLogBO.saveBusinessLogInterface("新增", "Portlet", "成功", portlet.getId());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.saveBusinessLogInterface("新增", "Portlet", "失败", portlet.getId());
		}
	}
	/**
	 * 更新portlet.
	 * @author 吴德福
	 * @param portlet
	 * @throws Exception 
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void mege(Portlet portlet) throws Exception{
		try {
			o_portletDAO.merge(portlet);
			o_businessLogBO.modBusinessLogInterface("修改", "Portlet", "成功", portlet.getId());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.modBusinessLogInterface("修改", "Portlet", "失败", portlet.getId());
			throw e;
		}
	}
	/**
	 * 删除portlet.
	 * @author 吴德福
	 * @param id
	 * @throws Exception 
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void removePortlet(String id) throws Exception{
		try {
			o_portletDAO.removeById(id);
			o_businessLogBO.delBusinessLogInterface("删除", "Portlet", "成功", id);
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.delBusinessLogInterface("删除", "Portlet", "失败", id);
			throw e;
		}
	}
	/**
	 * 根据id查询portlet.
	 * @author 吴德福
	 * @param id
	 * @return Portlet
	 * @since  fhd　Ver 1.1
	 */
	public Portlet queryPortletById(String id){
		return o_portletDAO.get(id);
	}
	/**
	 * 查询所有的portlet.
	 * @author 吴德福
	 * @return List<Portlet>
	 * @since  fhd　Ver 1.1
	 */
	public List<Portlet> queryAllPortlet(){
		return o_portletDAO.getAll();
	}
	/**
	 * 查询当前用户的所有portlets.
	 * @author 吴德福
	 * @param userid
	 * @return List<Portlet>
	 * @since  fhd　Ver 1.1
	 */
	public List<Portlet> queryPortletsByUserid(String userid){
		Criteria criteria = o_portletDAO.createCriteria();
		criteria.createAlias("Portal", "p");
		criteria.add(Restrictions.eq("p.sysUser.id", userid));
		criteria.addOrder(Order.asc("sort"));
		return criteria.list();
	}
	/**
	 * 更新面板信息
	 * @author 杨鹏
	 * @param portlet
	 * @throws Exception 
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void updatePortlet(Portlet portlet) throws Exception{
		try {
			o_portletDAO.update(portlet);
			o_businessLogBO.modBusinessLogInterface("修改", "面板", "成功", portlet.getId());
		} catch (Exception e) {
			e.printStackTrace();
			o_businessLogBO.modBusinessLogInterface("修改", "面板", "失败", portlet.getId());
			throw e;
		}
	}
	/**
	 * 根据条件查询面板信息
	 * @author 杨鹏
	 * @param userid,title,url
	 * @return List<Portlet>
	 * @since  fhd　Ver 1.1
	 */
	public Page<Portlet> queryPortletsBySome(Page<Portlet> page,String title,String url){
		DetachedCriteria criteria = DetachedCriteria.forClass(Portlet.class);
		if(StringUtils.isNotBlank(title)){
			criteria.add(Restrictions.like("title",title,MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(url)){
			criteria.add(Restrictions.like("url",url,MatchMode.ANYWHERE));
		}
		return o_portletDAO.pagedQuery(criteria,page);
	}
}

