/**
 * StandardRelaOrgBO.java
 * com.fhd.icm.business.standard
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-12-25 		刘中帅
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.icm.business.standard;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fhd.icm.dao.standard.StandardRelaProcessureDAO;
import com.fhd.icm.entity.standard.StandardRelaProcessure;
import com.fhd.icm.interfaces.standard.IStandardRelaOrg;

/**
 * @author   元杰
 * @version  
 * @see 	 
 */
@Service
public class StandardRelaProcessureBO implements IStandardRelaOrg{
	
	@Autowired
	private StandardRelaProcessureDAO o_standardRelaOrgDAO;

	/**
	 * <pre>
	 *通过Id集合删除内控标准与流程的关联实体
	 * </pre>
	 * 
	 * @author 元杰
	 * @param standardId
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void delStandardRelaProcessureByStandardId(String standardId) {
		if(StringUtils.isNotBlank(standardId)){
			Criteria criteria=o_standardRelaOrgDAO.createCriteria();
			criteria.add(Restrictions.eq("standard.id", standardId));
			List<StandardRelaProcessure> srpList = criteria.list();
			for(StandardRelaProcessure srp : srpList){
				o_standardRelaOrgDAO.delete(srp);
			}
		}
	}


	/**
	 * <pre>
	 * 保存标准关联流程
	 * </pre>
	 * 
	 * @author 元杰
	 */
	@Transactional
	public void saveStandardRelaProcessure(StandardRelaProcessure standardRelaProcessure) {
		if(null != standardRelaProcessure){
			o_standardRelaOrgDAO.merge(standardRelaProcessure);
		}
	}
}

