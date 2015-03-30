package com.fhd.assess.entity.riskTidy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.assess.entity.formulatePlan.RiskAssessPlan;
import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.risk.entity.Dimension;

/**
 * 定性评估结果
 */
@Entity
@Table(name = "T_RM_ADJUST_HISTORY_RESULT")
public class AdjustHistoryResult extends IdEntity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	
	/**维度*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCORE_DIM_ID")
	private Dimension dimension;
	
	/**历史调整记录ID*/
	@Column(name = "ADJUST_HISTORY_ID")
	private String adjustHistoryId;
	
	/**分值*/
	@Column(name = "SCORE")
	private String score;
	
	/**评估计划*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASSESS_PLAN_ID")
	private RiskAssessPlan riskAssessPlan;


	public Dimension getDimension() {
		return dimension;
	}


	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}


	public String getAdjustHistoryId() {
		return adjustHistoryId;
	}


	public void setAdjustHistoryId(String adjustHistoryId) {
		this.adjustHistoryId = adjustHistoryId;
	}


	public String getScore() {
		return score;
	}


	public void setScore(String score) {
		this.score = score;
	}


	public RiskAssessPlan getRiskAssessPlan() {
		return riskAssessPlan;
	}


	public void setRiskAssessPlan(RiskAssessPlan riskAssessPlan) {
		this.riskAssessPlan = riskAssessPlan;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}