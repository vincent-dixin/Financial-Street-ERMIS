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

import com.fhd.risk.dao.KpiRelaRiskDAO;
import com.fhd.risk.dao.RiskOrgDAO;
import com.fhd.risk.entity.KpiRelaRisk;
import com.fhd.risk.entity.RiskOrg;
import com.fhd.risk.interfaces.IKpiRelaRiskBO;
import com.fhd.risk.interfaces.IRiskOrgBO;


@Service
public class KpiRelaRiskBO implements IKpiRelaRiskBO{
	@Autowired
	private KpiRelaRiskDAO o_kpiRelaRiskDAO;

	public void saveKpiRelaRisk(KpiRelaRisk kpiRelaRisk){
		o_kpiRelaRiskDAO.merge(kpiRelaRisk);
	}
	
	public void removeKpiRelaRiskById(String id){
		o_kpiRelaRiskDAO.delete(id);
	}
}

