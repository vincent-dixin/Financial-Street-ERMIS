/**
 * AssessorDAO.java
 * com.fhd.icm.dao.assess
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-1-15 		张 雷
 *
 * Copyright (c) 2013, Firsthuida All Rights Reserved.
*/

package com.fhd.icm.dao.assess;

import org.springframework.stereotype.Repository;

import com.fhd.core.dao.hibernate.HibernateDao;
import com.fhd.icm.entity.assess.Assessor;

/**
 * 参评人DAO，评价执行
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-1-15		上午9:58:57
 *
 * @see 	 
 */
@Repository
public class AssessorDAO extends HibernateDao<Assessor, String> {

}

