/**
 * TempDAO.java
 * com.fhd.sys.dao.st
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-11 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.dao.st;

import org.springframework.stereotype.Repository;

import com.fhd.core.dao.hibernate.HibernateDao;
import com.fhd.sys.entity.st.Temp;

/**
 * 任务计划模版数据处理
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-11		下午03:12:43
 *
 * @see 	 
 */

@Repository
public class TempDAO extends HibernateDao<Temp, String>{
}