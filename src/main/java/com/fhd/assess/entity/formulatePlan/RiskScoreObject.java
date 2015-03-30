package com.fhd.assess.entity.formulatePlan;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.risk.entity.Risk;
/**
 * 打分对象实体
 * @author 王再冉
 *
 */
@Entity
@Table(name = "T_RM_RISK_SCORE_OBJECT") 
public class RiskScoreObject extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 打分范围ID
	 */
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCORE_RANGE_ID")
	private RiskScoreRange scoreRange;*/
	/**
	 * 评估计划ID 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASSESS_PLAN_ID")
	private RiskAssessPlan assessPlan;
	/**
	 * 风险ID
	 */
	//@ManyToOne(fetch = FetchType.LAZY)
	@OneToOne
	@JoinColumn(name = "RISK_ID")
	private Risk risk;
	/**
	 * 操作新增
	 */
	@Column(name = "OPER_ADD")
	private String operAdd;
	/**
	 * 操作修改
	 */
	@Column(name = "OPER_EDIT")
	private String operEdit;

	public RiskScoreObject() {
	}

	public RiskScoreObject(RiskAssessPlan assessPlan, Risk risk,
			String operAdd, String operEdit) {
		this.assessPlan = assessPlan;
		this.risk = risk;
		this.operAdd = operAdd;
		this.operEdit = operEdit;
	}

	

	public RiskAssessPlan getAssessPlan() {
		return assessPlan;
	}

	public void setAssessPlan(RiskAssessPlan assessPlan) {
		this.assessPlan = assessPlan;
	}

	public Risk getRisk() {
		return risk;
	}

	public void setRisk(Risk risk) {
		this.risk = risk;
	}

	public String getOperAdd() {
		return this.operAdd;
	}

	public void setOperAdd(String operAdd) {
		this.operAdd = operAdd;
	}

	public String getOperEdit() {
		return this.operEdit;
	}

	public void setOperEdit(String operEdit) {
		this.operEdit = operEdit;
	}

}

