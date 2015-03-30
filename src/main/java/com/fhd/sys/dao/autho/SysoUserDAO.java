/**
 * SysUserDAO.java
 * com.fhd.fdc.commons.dao.sys
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-7-29 		胡迪新
 *
 * Copyright (c) 2010, TNT All Rights Reserved.
*/

package com.fhd.sys.dao.autho;



import org.springframework.stereotype.Repository;

import com.fhd.core.dao.hibernate.HibernateDao;
import com.fhd.sys.entity.auth.SysUser;

/**
 * 用户DAO类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-8-30
 * Company FirstHuiDa.
 */

@Repository
public class SysoUserDAO extends HibernateDao<SysUser,String> {

}

