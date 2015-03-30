/**
 * AssessResultDAO.java
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
import com.fhd.icm.entity.assess.AssessResult;

/**
 * 评价结果DAO，评价执行
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-1-15		上午10:00:27
 *
 * @see 	 
 */
@Repository
public class AssessResultDAO extends HibernateDao<AssessResult, String> {

}

