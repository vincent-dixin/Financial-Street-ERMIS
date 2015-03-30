/**
 * SysRoleDAO.java
 * com.fhd.fdc.commons.dao.sys.auth
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-30 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.dao.auth;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.fhd.core.dao.HibernateEntityDao;
import com.fhd.sys.entity.auth.SysRole;

/**
 * 角色DAO类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-8-30
 * Company FirstHuiDa.
 */

@Repository
public class SysRoleDAOold extends HibernateEntityDao<SysRole>{
	
	public SysRole getSysRoleByCode(String code){
		String hqlstr="select role from SysRole role where role.roleCode='"+code+"'";
		Query query = this.getSession().createQuery(hqlstr);
		return (SysRole)query.uniqueResult();
		
	}

}

