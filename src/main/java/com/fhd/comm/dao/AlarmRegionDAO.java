/**
 * AlarmRegionDAO.java
 * com.fhd.kpi.dao
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-26 		陈晓哲
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.comm.dao;

import org.springframework.stereotype.Repository;

import com.fhd.comm.entity.AlarmRegion;
import com.fhd.core.dao.hibernate.HibernateDao;

/**
 * 告警关联区间值DAO
 * ClassName:AlarmRegionDAO
 *
 * @author   陈晓哲
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-26		下午07:25:28
 *
 * @see 	 
 */
@Repository
public class AlarmRegionDAO extends HibernateDao<AlarmRegion, String>
{

}
