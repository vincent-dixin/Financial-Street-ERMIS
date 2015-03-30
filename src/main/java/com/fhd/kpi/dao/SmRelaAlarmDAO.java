/**
 * SmRelaAlarmDAO.java
 * com.fhd.kpi.dao
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-18 		陈晓哲
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.kpi.dao;

import org.springframework.stereotype.Repository;

import com.fhd.core.dao.hibernate.HibernateDao;
import com.fhd.kpi.entity.SmRelaAlarm;

/**
 * 目标和告警关联对象DAO
 * ClassName:SmRelaAlarmDAO
 *
 * @author   陈晓哲
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-18		下午01:01:10
 *
 * @see 	 
 */
@Repository
public class SmRelaAlarmDAO extends HibernateDao<SmRelaAlarm, String>
{

}
