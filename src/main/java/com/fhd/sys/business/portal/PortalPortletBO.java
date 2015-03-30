/**
 * PortalPortletBO.java
 * com.fhd.fdc.commons.business.sys.portal
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2011-03-16 		杨鹏
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.business.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.sys.business.log.BusinessLogBO;
import com.fhd.sys.dao.portal.PortalPortletDAO;
import com.fhd.sys.entity.portal.PortalPortlet;

/**
 * 面板棉棒布局关系BO类
 * ClassName:PortalPortletBO
 * @author   杨鹏
 * @version  
 * @since    Ver 1.0
 * @Date	 2011-03-16		下午11:53:33
 *
 * @see
 */
@Service
public class PortalPortletBO {

	@Autowired
	private PortalPortletDAO o_portalPortletDAO;
	@Autowired
	private BusinessLogBO o_businessLogBO;
	
	/**
	 * savePortalPortlet:新增面板关系
	 * @author 杨鹏
	 * @param portalPortlet
	 * @throws Exception 
	 * @since  fhd　Ver 1.0
	 */
	@Transactional
	public void savePortalPortlet(PortalPortlet portalPortlet) throws Exception{
		try {
			o_portalPortletDAO.merge(portalPortlet);
			o_businessLogBO.saveBusinessLogInterface("新增", "portalPortlet", "成功",portalPortlet.getId());
		} catch (Exception e) {
			o_businessLogBO.saveBusinessLogInterface("新增", "portalPortlet", "失败", portalPortlet.getId());
			throw e;
		}
	}
	/**
	 * updatePortalPortlet:更新面板关系
	 * @author 杨鹏
	 * @param portalPortlet
	 * @throws Exception 
	 * @since  fhd　Ver 1.0
	 */
	@Transactional
	public void updatePortalPortlet(PortalPortlet portalPortlet) throws Exception{
		try {
			o_portalPortletDAO.merge(portalPortlet);
			o_businessLogBO.modBusinessLogInterface("更新", "portalPortlet", "成功",portalPortlet.getId());
		} catch (Exception e) {
			o_businessLogBO.modBusinessLogInterface("更新", "portalPortlet", "失败", portalPortlet.getId());
			throw e;
		}
	}
	/**
	 * removePortalPortlet:删除面板关系
	 * @Transactional
	 * @author 杨鹏
	 * @param id
	 * @throws Exception 
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void removePortalPortlet(String id) throws Exception{
		try {
			o_portalPortletDAO.removeById(id);
			o_businessLogBO.delBusinessLogInterface("删除", "portalPortlet", "成功", id);
		} catch (Exception e) {
			o_businessLogBO.delBusinessLogInterface("删除", "portalPortlet", "失败", id);
			throw e;
		}
	}
}
