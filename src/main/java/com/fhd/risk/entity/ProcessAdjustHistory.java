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
import com.fhd.process.entity.Process;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/** 
 * @author   zhengjunxiang	 
 */
@Entity
//@Table(name = "T_RM_PROCESSURE_ADJUST_HISTORY") 
public class ProcessAdjustHistory extends IdEntity implements Serializable{
	private static final long serialVersionUID = -560056895428600775L;
	
	/**
	 * 流程
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	//@JoinColumn(name = "PROCESSURE_ID")
	private Process process;
	
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
	
	public ProcessAdjustHistory(){
		
	}
	
	public ProcessAdjustHistory(String id){
		super.setId(id);
	}
	
	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
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

