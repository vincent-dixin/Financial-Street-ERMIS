/**
 * BusinessLogDAO.java
 * com.fhd.fdc.commons.dao.sys.log
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-27 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.dao.log;

import org.springframework.stereotype.Repository;

import com.fhd.core.dao.HibernateEntityDao;
import com.fhd.sys.entity.log.BusinessLog;

/**
 * 业务日志DAO类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-8-27
 * Company FirstHuiDa.
 */

@Repository
public class BusinessLogDAO extends HibernateEntityDao<BusinessLog>{

}

