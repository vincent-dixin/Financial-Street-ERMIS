package com.fhd.risk.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fhd.comm.entity.TimePeriod;
import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.kpi.entity.Kpi;
import com.fhd.kpi.entity.StrategyMap;

/** 
 * @author   zhengjunxiang	 
 */
@Entity
@Table(name = "T_RM_RISK_ADJUST_HISTORY") 
public class RiskAdjustHistory extends IdEntity implements Serializable{
	private static final long serialVersionUID = -560056895428600775L;
	
	/**
	 * 风险
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RISK_ID")
	private Risk risk;
	
	/**
	 * 风险水平
	 */
	@Column(name = "RISK_STATUS")
	private Double status;
	
	/**
	 * 影响程度
	 */
	@Column(name = "IMPACTS")
	private Double impacts;
	
	/**
	 * 发生可能性
	 */
	@Column(name = "PROBABILITY")
	private Double probability;
	
	/**
	 * 风险水平风险管理迫切性
	 */
	@Column(name = "MANAGEMENT_URGENCY")
	private Double managementUrgency;
	
	/**
	 * 调整方式
	 */
	@Column(name = "ADJUST_TYPE")
	private String adjustType;
	
	/**
	 * 更改时间
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "ADJUST_TIME")
	private Date adjustTime;
	
	/**
	 * 是否最新记录趋势
	 */
	@Column(name = "IS_LATEST")
	private String isLatest;
	
	/**
	 * 趋势
	 */
	@Column(name = "ETREND")
	private String etrend;
	
	/**
	 * 时间区间维
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TIME_PERIOD_ID")
	private TimePeriod timePeriod;
	
	/**
	 * 评估状态
	 */
	@Column(name = "ASSESSEMENT_STATUS")
	private String assessementStatus;
	
	/**
	 * 锁定状态
	 */
	@Column(name = "LOCK_STATUS")
	private String lockStatus;

	public RiskAdjustHistory(){
		
	}
	
	public RiskAdjustHistory(String id){
		super.setId(id);
	}

	public Risk getRisk() {
		return risk;
	}

	public void setRisk(Risk risk) {
		this.risk = risk;
	}

	public Double getStatus() {
		return status;
	}

	public void setStatus(Double status) {
		this.status = status;
	}

	public Double getImpacts() {
		return impacts;
	}

	public void setImpacts(Double impacts) {
		this.impacts = impacts;
	}

	public Double getProbability() {
		return probability;
	}

	public void setProbability(Double probability) {
		this.probability = probability;
	}

	public Double getManagementUrgency() {
		return managementUrgency;
	}

	public void setManagementUrgency(Double managementUrgency) {
		this.managementUrgency = managementUrgency;
	}

	public String getAdjustType() {
		return adjustType;
	}

	public void setAdjustType(String adjustType) {
		this.adjustType = adjustType;
	}

	public Date getAdjustTime() {
		return adjustTime;
	}

	public void setAdjustTime(Date adjustTime) {
		this.adjustTime = adjustTime;
	}

	public String getIsLatest() {
		return isLatest;
	}

	public void setIsLatest(String isLatest) {
		this.isLatest = isLatest;
	}

	public String getEtrend() {
		return etrend;
	}

	public void setEtrend(String etrend) {
		this.etrend = etrend;
	}

	public TimePeriod getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(TimePeriod timePeriod) {
		this.timePeriod = timePeriod;
	}

	public String getAssessementStatus() {
		return assessementStatus;
	}

	public void setAssessementStatus(String assessementStatus) {
		this.assessementStatus = assessementStatus;
	}

	public String getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(String lockStatus) {
		this.lockStatus = lockStatus;
	}

}

