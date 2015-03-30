package com.fhd.risk.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fhd.process.dao.ProcessRelaRiskDAO;
import com.fhd.process.entity.ProcessRelaRisk;

@Service
public class ProcessRelaRiskBO {
	@Autowired
	private ProcessRelaRiskDAO  o_processRelaRiskDAO;

	public void saveProcessRelaRisk(ProcessRelaRisk processRelaRisk){
		o_processRelaRiskDAO.merge(processRelaRisk);
	}
	
	public void removeProcessRelaRiskById(String id){
		o_processRelaRiskDAO.delete(id);
	}
}

