package com.fhd.assess.entity.quaAssess;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
/**
 * 公式计算
 * @author 王再冉
 *
 */
@Entity
@Table(name = "T_SYS_FOMULASET") 
public class FormulaSet extends IdEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 组织风险公式
	 */
	@Column(name = "DEPT_RISK_FORMULA")
	private String deptRiskFormula;
	/**
	 * 目标风险公式
	 */
	@Column(name = "STRATEGY_RISK_FORMULA")
	private String strategyRiskFormula;
	/**
	 * 指标风险公式
	 */
	@Column(name = "KPI_RISK_FORMULA")
	private String kpiRiskFormula;
	/**
	 * 流程风险公式
	 */
	@Column(name = "PROC2ESS_RISK_FORMULA")
	private String processRiskFormula;
	/**
	 * 风险分类发生可能性公式
	 */
	@Column(name = "RISK_TYPE_HAPPEN")
	private String riskTypeHappen;
	/**
	 * 风险分类影响程度公式
	 */
	@Column(name = "RISK_TYPE_IMPACT")
	private String riskTypeImpact;
	
	public FormulaSet(){
		
	}
	
	public String getDeptRiskFormula() {
		return deptRiskFormula;
	}
	public void setDeptRiskFormula(String deptRiskFormula) {
		this.deptRiskFormula = deptRiskFormula;
	}
	public String getStrategyRiskFormula() {
		return strategyRiskFormula;
	}
	public void setStrategyRiskFormula(String strategyRiskFormula) {
		this.strategyRiskFormula = strategyRiskFormula;
	}
	public String getKpiRiskFormula() {
		return kpiRiskFormula;
	}
	public void setKpiRiskFormula(String kpiRiskFormula) {
		this.kpiRiskFormula = kpiRiskFormula;
	}
	public String getProcessRiskFormula() {
		return processRiskFormula;
	}
	public void setProcessRiskFormula(String processRiskFormula) {
		this.processRiskFormula = processRiskFormula;
	}
	public String getRiskTypeHappen() {
		return riskTypeHappen;
	}
	public void setRiskTypeHappen(String riskTypeHappen) {
		this.riskTypeHappen = riskTypeHappen;
	}
	public String getRiskTypeImpact() {
		return riskTypeImpact;
	}
	public void setRiskTypeImpact(String riskTypeImpact) {
		this.riskTypeImpact = riskTypeImpact;
	}
	
	

}
