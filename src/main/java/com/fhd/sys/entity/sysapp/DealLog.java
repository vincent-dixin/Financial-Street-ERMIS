package com.fhd.sys.entity.sysapp;

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

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 计划任务--处理记录.
 * @author 吴德福
 * @version V1.0  创建时间：2012-5-15
 * Company FirstHuiDa.
 */
@Entity
@Table(name = "T_ST_DEAL_LOG")
public class DealLog extends IdEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	//计划任务
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAN_EMP_ID")
	private ScheduledTaskEmp emp;
	//处理方式:ST_DEAL_MEASURE,短信；邮件；其他
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEAL_MEASURE")
	private DictEntry dealMeasure;
	//处理时间
	@Temporal(TemporalType.DATE)
	@Column(name = "DEAL_TIME")
	private Date dealTime;
	//处理状态
	@Column(name = "ESTATUS")
	private String eStatus;
	
	public DealLog() {
	}

	public DealLog(ScheduledTaskEmp emp, DictEntry dealMeasure, Date dealTime,
			String eStatus) {
		super();
		this.emp = emp;
		this.dealMeasure = dealMeasure;
		this.dealTime = dealTime;
		this.eStatus = eStatus;
	}

	public ScheduledTaskEmp getEmp() {
		return emp;
	}

	public void setEmp(ScheduledTaskEmp emp) {
		this.emp = emp;
	}

	public DictEntry getDealMeasure() {
		return dealMeasure;
	}

	public void setDealMeasure(DictEntry dealMeasure) {
		this.dealMeasure = dealMeasure;
	}

	public Date getDealTime() {
		return dealTime;
	}

	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}

	public String geteStatus() {
		return eStatus;
	}

	public void seteStatus(String eStatus) {
		this.eStatus = eStatus;
	}
}
