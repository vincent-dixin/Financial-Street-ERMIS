/**
 * AlarmPlanDAO.java
 * com.fhd.kpi.dao
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-18 		陈晓哲
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.comm.dao;

import org.springframework.stereotype.Repository;

import com.fhd.comm.entity.AlarmPlan;
import com.fhd.core.dao.hibernate.HibernateDao;

/**
 * 告警计划DAO
 * ClassName:AlarmPlanDAO
 *
 * @author   陈晓哲
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-18		下午01:22:32
 *
 * @see 	 
 */
@Repository
public class AlarmPlanDAO extends HibernateDao<AlarmPlan, String>
{

}
