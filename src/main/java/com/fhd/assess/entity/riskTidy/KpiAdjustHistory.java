package com.fhd.assess.entity.riskTidy;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.comm.entity.TimePeriod;
import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.kpi.entity.Kpi;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 指标定性评估记录
 */
@Entity
@Table(name = "T_RM_KPI_ADJUST_HISTORY")
public class KpiAdjustHistory extends IdEntity implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**维度*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "KPI_ID")
	private Kpi kpiId;
	
	/**调整方式(0:问卷统计回写1:手工调整)*/
	@Column(name = "ADJUST_TYPE")
	private String adjustType;
	
	/**更改时间*/
	@Column(name = "ADJUST_TIME")
	private Date adjustTime;
	
	/**是否最新记录*/
	@Column(name = "IS_LATEST")
	private String isLatest;
	
	/**趋势*/
	@Column(name = "ETREND")
	private String etrend;
	
	/**维度*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TIME_PERIOD_ID")
	private TimePeriod timePeriod;
	
	/**趋势*/
	@Column(name = "ASSESSEMENT_STATUS")
	private String assessementStatus;
	
	/**锁定状态*/
	@Column(name = "LOCK_STATUS")
	private String lockStatus;
	
	/**公司ID*/
	@Column(name = "COMPANY_ID")
	private SysOrganization companyId;

	public Kpi getKpiId() {
		return kpiId;
	}

	public void setKpiId(Kpi kpiId) {
		this.kpiId = kpiId;
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

	public SysOrganization getCompanyId() {
		return companyId;
	}

	public void setCompanyId(SysOrganization companyId) {
		this.companyId = companyId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}