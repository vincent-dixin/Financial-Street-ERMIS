package com.fhd.risk.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.risk.dao.RiskEventDAO;
import com.fhd.risk.entity.RiskEvent;

@Service
public class RiskEventService {

	@Autowired
	private RiskEventDAO o_riskEventDAO;
	
	@Transactional
	public boolean saveRiskEvent(RiskEvent riskEvent) {
		o_riskEventDAO.merge(riskEvent);
		return true;
	}

	@Transactional
	public boolean mergeRiskEvent(RiskEvent riskEvent) {
		o_riskEventDAO.merge(riskEvent);
		return true;
	}

	@Transactional
	public boolean removeRiskEventById(String id) {
		o_riskEventDAO.delete(id);
		return true;
	}

	@Transactional
	public boolean removeRiskEventByIds(String[] ids) {
		for (String id : ids) {
			o_riskEventDAO.delete(id);
		}
		return true;
	}

	public RiskEvent findRiskEventById(String id) {
		return o_riskEventDAO.get(id);
	}

	
}
