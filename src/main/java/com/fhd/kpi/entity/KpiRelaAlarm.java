package com.fhd.kpi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fhd.comm.entity.AlarmPlan;
import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * 
 * 指标选择预警方案
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-9-17		上午11:01:57
 *
 * @see
 */
@Entity
@Table(name = "t_kpi_kpi_rela_alarm")
public class KpiRelaAlarm extends IdEntity implements java.io.Serializable {

	/**
	 * 
	 * @author 胡迪新
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = 1L;
	// Fields
	

	/**
	 * 预警方案
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FC_ALARM_PLAN_ID")
	private AlarmPlan fcAlarmPlan;
	
	/**
	 * 告警方案
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "R_ALARM_PLAN_ID")
	private AlarmPlan rAlarmPlan;
	
	/**
	 * 开始日期
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE")
	private Date startDate;
	
	/**
	 * 指标
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "KPI_ID")
	private Kpi kpi;
	
	// Constructors

	/** default constructor */
	public KpiRelaAlarm() {
	}

	/** minimal constructor */
	public KpiRelaAlarm(String id) {
		setId(id);
	}



	public AlarmPlan getFcAlarmPlan() {
		return fcAlarmPlan;
	}

	public void setFcAlarmPlan(AlarmPlan fcAlarmPlan) {
		this.fcAlarmPlan = fcAlarmPlan;
	}

	public AlarmPlan getrAlarmPlan() {
		return rAlarmPlan;
	}

	public void setrAlarmPlan(AlarmPlan rAlarmPlan) {
		this.rAlarmPlan = rAlarmPlan;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	



	public Kpi getKpi() {
		return kpi;
	}

	public void setKpi(Kpi kpi) {
		this.kpi = kpi;
	}

	
}