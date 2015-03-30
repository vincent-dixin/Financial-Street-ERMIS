/**
 * RiskOrgBO.java
 * com.fhd.risk.business
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-19 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * RiskOrgBO.java
 * com.fhd.risk.business
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-19        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
*/


package com.fhd.risk.business;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.risk.dao.RiskOrgDAO;
import com.fhd.risk.entity.RiskOrg;
import com.fhd.risk.interfaces.IRiskOrgBO;


@Service
public class RiskOrgBO implements IRiskOrgBO{
	@Autowired
	private RiskOrgDAO o_riskOrgDAO;

	@SuppressWarnings("unchecked")
	public List<RiskOrg> findRiskOrgBySome(String searchName, String companyId, String orgId, String deleteStatus, Boolean rbs) {
		List<RiskOrg> RiskOrgList = new ArrayList<RiskOrg>();
		Criteria criteria = o_riskOrgDAO.createCriteria();
		
		if (StringUtils.isNotBlank(searchName)) {
			criteria.createAlias("risk", "r")
					.add(Restrictions.like("r.name", searchName, MatchMode.ANYWHERE))
					.add(Restrictions.eq("r.company.id", companyId))
					.add(Restrictions.eq("r.deleteStatus", deleteStatus));
		} else {
			criteria.createAlias("risk", "r").add(
					Restrictions.eq("r.company.id", companyId));
		}
		criteria.createAlias("sysOrganization", "s").add(
				Restrictions.eq("s.id", orgId));

		if(rbs){
			criteria.add(Restrictions.eq("r.isRiskClass", "RBS"));
		}
		
		RiskOrgList = criteria.list();
		
		return RiskOrgList;
	}
	
	@SuppressWarnings("unchecked")
	public List<RiskOrg> findRiskOrgBySome(String companyId, String query, String deleteStatus, Boolean rbs){
		List<RiskOrg> riskOrgList = null;
		Criteria criteria = o_riskOrgDAO.createCriteria()
		.createAlias("risk", "r")
		.add(Restrictions.eq("r.company.id", companyId))
		.add(Restrictions.eq("r.deleteStatus", deleteStatus));
		
		if (StringUtils.isNotBlank(query)) {
			criteria.add(Restrictions.like("r.name", query,
					MatchMode.ANYWHERE));
		}
		
		if(rbs){
			criteria.add(Restrictions.eq("r.isRiskClass", "RBS"));
		}
		
		riskOrgList = criteria.list();
		
		return riskOrgList;
	}
	
	@SuppressWarnings("unchecked")
	public Set<String> findRiskOrg(){
		Set<String> otheridSet = new HashSet<String>();
		List<RiskOrg> riskOrgList = o_riskOrgDAO.createCriteria().list();
		for (RiskOrg riskOrg : riskOrgList) {
			otheridSet.add(riskOrg.getRisk().getId());
		}
		
		return otheridSet;
	}
	
	public void removeRiskOrgById(RiskOrg riskOrg){
		o_riskOrgDAO.delete(riskOrg);
	}
	
	public void saveRiskOrg(RiskOrg riskOrg){
		o_riskOrgDAO.merge(riskOrg);
	}
}

