/**
 * SmRelaKpiDAO.java
 * com.fhd.kpi.dao
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-17 		陈晓哲
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.kpi.dao;

import org.springframework.stereotype.Repository;

import com.fhd.core.dao.hibernate.HibernateDao;
import com.fhd.kpi.entity.SmRelaKpi;

/**
 * 目标和kpi指标关联对象DAO
 * ClassName:SmRelaKpiDAO
 *
 * @author   陈晓哲
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-17		下午05:19:22
 *
 * @see 	 
 */
@Repository
public class SmRelaKpiDAO extends HibernateDao<SmRelaKpi, String>
{

}
