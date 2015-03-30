package com.fhd.assess.entity.formulatePlan;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * 打分范围实体
 * @author 王再冉
 *
 */
@Entity
//@Table(name = "T_RM_RISK_SCORE_RANGE")
public class RiskScoreRange extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 评估计划ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASSESS_PLAN_ID")
	private RiskAssessPlan assessPlan;
	/**
	 * 范围类型
	 */
	@Column(name = "RANGE_TYPE")
	private String rangeType;
	/**
	 * 范围类型ID
	 */
	@Column(name = "RANGE_TYPE_ID")
	private String rangeTypeId;

	public RiskScoreRange() {
	}


	public RiskScoreRange( RiskAssessPlan assessPlan, String rangeType,
			String rangeTypeId) {
		this.assessPlan = assessPlan;
		this.rangeType = rangeType;
		this.rangeTypeId = rangeTypeId;
	}

	

	public RiskAssessPlan getAssessPlan() {
		return assessPlan;
	}


	public void setAssessPlan(RiskAssessPlan assessPlan) {
		this.assessPlan = assessPlan;
	}


	public String getRangeType() {
		return this.rangeType;
	}

	public void setRangeType(String rangeType) {
		this.rangeType = rangeType;
	}

	public String getRangeTypeId() {
		return this.rangeTypeId;
	}

	public void setRangeTypeId(String rangeTypeId) {
		this.rangeTypeId = rangeTypeId;
	}

}

