/**
 * TimePeriodDAO.java
 * com.fhd.comm.dao
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-29 		陈晓哲
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.comm.dao;

import org.springframework.stereotype.Repository;

import com.fhd.comm.entity.TimePeriod;
import com.fhd.core.dao.hibernate.HibernateDao;

/**
 * 时间维度DAO
 *
 * @author   陈晓哲
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-29		下午05:39:08
 *
 * @see 	 
 */
@Repository
public class TimePeriodDAO extends HibernateDao<TimePeriod, String>
{

}
