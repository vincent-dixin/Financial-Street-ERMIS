package com.fhd.assess.web.form.formulatePlan;

import java.util.List;

import com.fhd.assess.entity.formulatePlan.RiskScoreDept;
import com.fhd.assess.entity.formulatePlan.RiskScoreObject;
/**
 * 打分对象Form
 * @author 王再冉
 *
 */
public class RiskScoreObjectForm extends RiskScoreObject{
	private static final long serialVersionUID = 1L;
	private String riskName;//风险名称
	private String riskId;//风险id
	private String parentRiskName;//上级风险
	private String mainOrgName;//责任部门
	private String relaOrgName;//相关部门
	private String joinOrgName;//参与部门
	private String planId;
	
	public RiskScoreObjectForm(){
		
	}
	
	public RiskScoreObjectForm(RiskScoreObject scoreObj, List<RiskScoreDept> scoreDepts){
		this.setId(scoreObj.getId());
		this.setRiskId(scoreObj.getRisk().getId());
		this.setRiskName(scoreObj.getRisk().getName());
		this.setPlanId(scoreObj.getAssessPlan().getId());
		if(null != scoreObj.getRisk().getParent()){//上级风险
			this.setParentRiskName(scoreObj.getRisk().getParent().getName());
		}
		for(RiskScoreDept dept : scoreDepts){
			if(dept.getOrgType().equals("M")){//责任部门
				this.setMainOrgName(dept.getOrganization().getOrgname());
			}else if(dept.getOrgType().equals("A")){//相关部门
				this.setRelaOrgName(dept.getOrganization().getOrgname());
			}
		}
	}

	public String getRiskName() {
		return riskName;
	}

	public void setRiskName(String riskName) {
		this.riskName = riskName;
	}

	public String getParentRiskName() {
		return parentRiskName;
	}

	public void setParentRiskName(String parentRiskName) {
		this.parentRiskName = parentRiskName;
	}

	public String getMainOrgName() {
		return mainOrgName;
	}

	public void setMainOrgName(String mainOrgName) {
		this.mainOrgName = mainOrgName;
	}

	public String getRelaOrgName() {
		return relaOrgName;
	}

	public void setRelaOrgName(String relaOrgName) {
		this.relaOrgName = relaOrgName;
	}

	public String getJoinOrgName() {
		return joinOrgName;
	}

	public void setJoinOrgName(String joinOrgName) {
		this.joinOrgName = joinOrgName;
	}

	public String getRiskId() {
		return riskId;
	}

	public void setRiskId(String riskId) {
		this.riskId = riskId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}
	
	
}
