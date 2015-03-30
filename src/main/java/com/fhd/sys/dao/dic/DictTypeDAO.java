/**
 * DictTypeDAO.java
 * com.fhd.fdc.commons.dao.dic
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-14 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.dao.dic;

import org.springframework.stereotype.Repository;
import com.fhd.core.dao.hibernate.HibernateDao;
import com.fhd.sys.entity.dic.DictType;

/**
 * 数据字典类型DAO类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-8-9
 * Company FirstHuiDa.
 */

@Repository
public class DictTypeDAO extends HibernateDao<DictType,String>{	
}