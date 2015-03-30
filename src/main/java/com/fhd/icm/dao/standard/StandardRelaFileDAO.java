/**
 * StandardRelaFile.java
 * com.fhd.icm.dao.standard
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-12-22 		刘中帅
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.icm.dao.standard;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fhd.core.dao.hibernate.HibernateDao;
import com.fhd.icm.entity.standard.StandardRelaFile;

/**
 * @author   刘中帅
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-22		下午6:36:31
 *
 * @see 	 
 */
@Repository
public class StandardRelaFileDAO  extends HibernateDao<StandardRelaFile, String> {
	
	
	/**
	 * 
	 * <pre>
	 *通过Id查询实体，可以是主键，standardId，fileId
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param id
	 * @param value
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	public List<StandardRelaFile> findStandardRelaFileById(String id,String value){
		List<StandardRelaFile> standardRelaFileListr=null;
		Criteria criteria=this.createCriteria();
		criteria.add(Restrictions.eq(id, value));
		standardRelaFileListr=criteria.list();
		return standardRelaFileListr;
	}

}

