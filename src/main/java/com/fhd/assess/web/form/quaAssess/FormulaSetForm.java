package com.fhd.assess.web.form.quaAssess;

import com.fhd.assess.entity.quaAssess.FormulaSet;

public class FormulaSetForm extends FormulaSet{
	
	private static final long serialVersionUID = 1L;
	
	public FormulaSetForm(){
		
	}
	
	public FormulaSetForm(FormulaSet formula){
		this.setDeptRiskFormula(formula.getDeptRiskFormula());
		this.setId(formula.getId());
		this.setKpiRiskFormula(formula.getKpiRiskFormula());
		this.setProcessRiskFormula(formula.getProcessRiskFormula());
		this.setRiskTypeHappen(formula.getRiskTypeHappen());
		this.setRiskTypeImpact(formula.getRiskTypeImpact());
		this.setStrategyRiskFormula(formula.getStrategyRiskFormula());
	}
	
	

}
