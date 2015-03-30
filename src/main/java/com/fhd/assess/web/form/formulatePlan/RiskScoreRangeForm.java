package com.fhd.assess.web.form.formulatePlan;

import com.fhd.assess.entity.formulatePlan.RiskScoreRange;
import com.fhd.sys.entity.orgstructure.SysOrganization;

public class RiskScoreRangeForm extends RiskScoreRange{
	private static final long serialVersionUID = 1L;
	
	private String deptName;//部门名称

	public RiskScoreRangeForm(){
		
	}
	
	public RiskScoreRangeForm(RiskScoreRange scoreRange, SysOrganization org){
		this.setId(scoreRange.getId());
		this.setRangeType(scoreRange.getRangeType());
		this.setDeptName(org.getOrgname());
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	
	
}
