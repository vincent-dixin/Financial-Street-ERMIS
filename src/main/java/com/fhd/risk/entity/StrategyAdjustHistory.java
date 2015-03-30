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
//@Table(name = "T_RM_STRATEGY_ADJUST_HISTORY") 
public class StrategyAdjustHistory extends IdEntity implements Serializable{
	private static final long serialVersionUID = -560056895428600775L;
	
	/**
	 * 目标
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	//@JoinColumn(name = "STRATEGY_ID")
	private StrategyMap strategyMap;
	
	/**
	 * 调整方式
	 */
	//@Column(name = "ADJUST_TYPE")
	private String adjustType;
	
	/**
	 * 更改时间
	 */
	@Temporal(TemporalType.DATE)
	//@Column(name = "ADJUST_TIME")
	private Date adjustTime;
	
	/**
	 * 是否最新记录趋势
	 */
	//@Column(name = "IS_LATEST")
	private String isLatest;
	
	/**
	 * 状态
	 */
	//@Column(name = "ASSESSEMENT_STATUS")
	private String assessementStatus;
	
	/**
	 * 趋势
	 */
	//@Column(name = "ETREND")
	private String etrend;
	
	/**
	 * 时间区间维
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	//@JoinColumn(name = "TIME_PERIOD_ID")
	private TimePeriod timePeriod;
	
	public StrategyAdjustHistory(){
		
	}
	
	public StrategyAdjustHistory(String id){
		super.setId(id);
	}

	public StrategyMap getStrategyMap() {
		return strategyMap;
	}

	public void setStrategyMap(StrategyMap strategyMap) {
		this.strategyMap = strategyMap;
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

	public String getAssessementStatus() {
		return assessementStatus;
	}

	public void setAssessementStatus(String assessementStatus) {
		this.assessementStatus = assessementStatus;
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
	
}

