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
 * 计划任务--定时提醒对象设置.
 * @author 吴德福
 * @version V1.0  创建时间：2012-5-15
 * Company FirstHuiDa.
 */
@Entity
@Table(name = "T_ST_PLAN_TARGET")
public class ScheduledTaskTarget extends IdEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	//计划任务
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAN_ID")
	private ScheduledTask task;
	//处理对象
	@Column(name = "DEAL_TARGET")
	private String dealTarget;
	//处理对象类别:ST_TARGET_TYPE,风险类别；风险事件。。。
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TARGET_TYPE")
	private DictEntry targetType;
	
	public ScheduledTaskTarget() {
	}

	public ScheduledTaskTarget(ScheduledTask task, String dealTarget,
			DictEntry targetType) {
		super();
		this.task = task;
		this.dealTarget = dealTarget;
		this.targetType = targetType;
	}

	public ScheduledTask getTask() {
		return task;
	}

	public void setTask(ScheduledTask task) {
		this.task = task;
	}

	public String getDealTarget() {
		return dealTarget;
	}

	public void setDealTarget(String dealTarget) {
		this.dealTarget = dealTarget;
	}

	public DictEntry getTargetType() {
		return targetType;
	}

	public void setTargetType(DictEntry targetType) {
		this.targetType = targetType;
	}
}
