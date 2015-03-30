/**
 * PortalPortletDAO.java
 * com.fhd.fdc.commons.dao.sys.portal
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-10-20 		杨鹏
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.dao.portal;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fhd.core.dao.HibernateEntityDao;
import com.fhd.sys.entity.portal.PortalPortlet;

/**
 * PortalPortletDAO类.
 * @author  yangpeng
 * @version V1.0  创建时间：2011-03-16
 * Company FirstHuiDa.
 */

@Repository
public class PortalPortletDAO extends HibernateEntityDao<PortalPortlet>{
	@SuppressWarnings("unchecked")
	public void removeByPortalId(String portalId){
		DetachedCriteria criterions=DetachedCriteria.forClass(PortalPortlet.class);
		criterions.add(Restrictions.eq("portal.id", portalId));
		List<PortalPortlet> list = this.findByCriteria(criterions);
		if(null!=list){
			for (PortalPortlet portalPortlet : list) {
				this.remove(portalPortlet);
			}
		}
	}
}

