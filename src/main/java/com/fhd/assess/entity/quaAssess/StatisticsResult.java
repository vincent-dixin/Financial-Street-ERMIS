package com.fhd.assess.entity.quaAssess;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.assess.entity.formulatePlan.RiskAssessPlan;
import com.fhd.assess.entity.formulatePlan.RiskScoreObject;
import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.risk.entity.Dimension;

/**
 * 问卷统计结果
 */
@Entity
@Table(name = "T_RM_STATISTICS_RESULT")
public class StatisticsResult extends IdEntity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	/**维度*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCORE_DIM_ID")
	private Dimension dimension;
	
	/**评估计划*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASSESS_PLAN_ID")
	private RiskAssessPlan riskAssessPlan;
	
	/**打分对象*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCORE_OBJECT_ID")
	private RiskScoreObject riskScoreObject;
	
	/**分值*/
	@Column(name = "SCORE")
	private String score;
	
	/**状态*/
	@Column(name = "STATUS")
	private String status;

	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public RiskAssessPlan getRiskAssessPlan() {
		return riskAssessPlan;
	}

	public void setRiskAssessPlan(RiskAssessPlan riskAssessPlan) {
		this.riskAssessPlan = riskAssessPlan;
	}

	public RiskScoreObject getRiskScoreObject() {
		return riskScoreObject;
	}

	public void setRiskScoreObject(RiskScoreObject riskScoreObject) {
		this.riskScoreObject = riskScoreObject;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}