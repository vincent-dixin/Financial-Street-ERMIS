/**
 * SysAuthUserDAO.java
 * com.fhd.sys.dao.auth
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-1-16 		黄晨曦
 *
 * Copyright (c) 2013, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.dao.autho;

import org.springframework.stereotype.Repository;

import com.fhd.core.dao.hibernate.HibernateDao;
import com.fhd.sys.entity.auth.SysUser;

/**
 * @author   翟辉
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-1-16		上午9:26:21
 *
 * @see 	 
 */
@Repository
public class SysoAuthUserDAO  extends HibernateDao<SysUser, String> {

}

