package com.fhd.sys.entity.sysapp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 计划任务--信息处理方式.
 * @author 吴德福
 * @version V1.0  创建时间：2012-5-15
 * Company FirstHuiDa.
 */
@Entity
@Table(name = "T_ST_DEAL_MEASURE")
public class DealMeasure extends IdEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	//计划任务
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAN_ID")
	private ScheduledTask task;
	//触发方式:ST_DEAL_MEASURE,短信；邮件；其他
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEAL_MEASURE")
	private DictEntry dealMeasure;
	//模板
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEMP_ID")
	private ScheduledTaskTemp temp;
	//其它操作
	@Column(name = "DEAL_MEASURE_DETAIL")
	private String dealMeasureDetail;
	
	public DealMeasure() {
	}

	public DealMeasure(ScheduledTask task, DictEntry dealMeasure,
			ScheduledTaskTemp temp, String dealMeasureDetail) {
		super();
		this.task = task;
		this.dealMeasure = dealMeasure;
		this.temp = temp;
		this.dealMeasureDetail = dealMeasureDetail;
	}

	public ScheduledTask getTask() {
		return task;
	}

	public void setTask(ScheduledTask task) {
		this.task = task;
	}

	public DictEntry getDealMeasure() {
		return dealMeasure;
	}

	public void setDealMeasure(DictEntry dealMeasure) {
		this.dealMeasure = dealMeasure;
	}

	public ScheduledTaskTemp getTemp() {
		return temp;
	}

	public void setTemp(ScheduledTaskTemp temp) {
		this.temp = temp;
	}

	public String getDealMeasureDetail() {
		return dealMeasureDetail;
	}

	public void setDealMeasureDetail(String dealMeasureDetail) {
		this.dealMeasureDetail = dealMeasureDetail;
	}
}
