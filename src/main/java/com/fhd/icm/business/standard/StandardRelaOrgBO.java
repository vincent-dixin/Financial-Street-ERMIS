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

import com.fhd.icm.dao.standard.StandardRelaOrgDAO;
import com.fhd.icm.entity.standard.StandardRelaOrg;
import com.fhd.icm.interfaces.standard.IStandardRelaOrg;

/**
 * @author   刘中帅
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-25		下午6:25:05
 *
 * @see 	 
 */
@Service
public class StandardRelaOrgBO implements IStandardRelaOrg{
	
	@Autowired
	private StandardRelaOrgDAO o_standardRelaOrgDAO;

	/**
	 * 
	 * <pre>
	 *通过Id查询内控标准与机构的关联实体
	 * </pre>
	 * 
	 * @author 刘中帅
	 * @param standardId
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public StandardRelaOrg findStandardRelaOrgById(String standardId) {
		StandardRelaOrg  standardRelaOrg=null;
		Criteria criteria=o_standardRelaOrgDAO.createCriteria();
		criteria.add(Restrictions.eq("standard.id",standardId));
		standardRelaOrg=(StandardRelaOrg)criteria.uniqueResult();
		return standardRelaOrg;
	}
	
	/**
	 * <pre>
	 *通过Id集合删除内控标准与机构的关联实体
	 * </pre>
	 * 
	 * @author 元杰
	 * @param standardId
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void delStandardRelaOrgByStandardId(String standardId) {
		if(StringUtils.isNotBlank(standardId)){
			Criteria criteria=o_standardRelaOrgDAO.createCriteria();
			criteria.add(Restrictions.eq("standard.id", standardId));
			List<StandardRelaOrg> sroList = criteria.list();
			for(StandardRelaOrg sro : sroList){
				o_standardRelaOrgDAO.delete(sro);
			}
		}
	}
	
	/**
	 * <pre>
	 *通过Id查询内控标准与机构的关联实体
	 * </pre>
	 * 
	 * @author 元杰
	 * @param standardId
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public StandardRelaOrg findStandardRelaOrgByStandardId(String standardId) {
		List<StandardRelaOrg> sroList = null;
		if(StringUtils.isNotBlank(standardId)){
			Criteria criteria=o_standardRelaOrgDAO.createCriteria();
			criteria.add(Restrictions.eq("standard.id", standardId));
			sroList = criteria.list();
		}
		return sroList.get(0);
	}
}

