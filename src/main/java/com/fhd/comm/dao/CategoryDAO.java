/**
 * CategoryDAO.java
 * com.fhd.kpi.dao
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-14 		陈晓哲
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.comm.dao;

import org.springframework.stereotype.Repository;

import com.fhd.comm.entity.Category;
import com.fhd.core.dao.hibernate.HibernateDao;

/**
 * 指标维度DAO
 *
 * @author   陈晓哲
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-14		下午01:20:43
 *
 * @see 	 
 */
@Repository
public class CategoryDAO extends HibernateDao<Category, String>
{

}
