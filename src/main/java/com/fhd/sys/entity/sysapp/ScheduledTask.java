package com.fhd.sys.entity.sysapp;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * 计划任务--任务.
 * @author 吴德福
 * @version V1.0  创建时间：2012-5-15
 * Company FirstHuiDa.
 */
@Entity
@Table(name = "T_ST_PLAN")
public class ScheduledTask extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	//任务名称
	@Column(name = "PLAN_NAME")
	private String planName;
	//标题
	@Column(name = "PLAN_TITLE")
	private String planTitle;
	//触发时间
	@Column(name = "TRIGGER_TIME")
	private String triggerTime;
	//触发条件
	@Column(name = "TRIGGER_TIME_TEXT")
	private String triggerTimeText;
	//触发类型:ST_TRIGGER_TYPE，其内容为 ：事件触发、时间触发
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TRIGGER_TYPE")
	private DictEntry triggerType;
	//触发类型详细:ST_TRIGGER_SUB_TYPE,如：风险的增删改，工作流的发起、提交、结束；
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TRIGGER_SUB_TYPE")
	private DictEntry triggerSubType;
	//开始时间
	@Temporal(TemporalType.DATE)
	@Column(name = "START_TIME")
	private Date startTime;
	//结束时间
	@Temporal(TemporalType.DATE)
	@Column(name = "END_TIME")
	private Date endTime;
	//是否有效
	@Column(name = "IS_ENABLED")
	private String isEnabled;
	//创建时间
	@Temporal(TemporalType.DATE)
	@Column(name = "CREATE_TIME")
	private Date createTime;
	//创建人
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATE_BY")
	private SysEmployee createBy;
	/**
	 * 提醒对象集合.
	 */
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "task")
	private Set<ScheduledTaskTarget> scheduledTaskTargets = new HashSet<ScheduledTaskTarget>(0);
	/**
	 * 触发方式集合.
	 */
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "task")
	private Set<DealMeasure> dealMeasures = new HashSet<DealMeasure>(0);
	/**
	 * 涉及人员集合.
	 */
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "task")
	private Set<ScheduledTaskEmp> scheduledTaskEmps = new HashSet<ScheduledTaskEmp>(0);
	
	public ScheduledTask() {
	}

	public ScheduledTask(String planName, String planTitle, String triggerTime,
			String triggerTimeText, DictEntry triggerType,
			DictEntry triggerSubType, Date startTime, Date endTime,
			String isEnabled, Date createTime, SysEmployee createBy,
			Set<ScheduledTaskTarget> scheduledTaskTargets,
			Set<DealMeasure> dealMeasures,
			Set<ScheduledTaskEmp> scheduledTaskEmps) {
		super();
		this.planName = planName;
		this.planTitle = planTitle;
		this.triggerTime = triggerTime;
		this.triggerTimeText = triggerTimeText;
		this.triggerType = triggerType;
		this.triggerSubType = triggerSubType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.isEnabled = isEnabled;
		this.createTime = createTime;
		this.createBy = createBy;
		this.scheduledTaskTargets = scheduledTaskTargets;
		this.dealMeasures = dealMeasures;
		this.scheduledTaskEmps = scheduledTaskEmps;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getPlanTitle() {
		return planTitle;
	}

	public void setPlanTitle(String planTitle) {
		this.planTitle = planTitle;
	}

	public String getTriggerTime() {
		return triggerTime;
	}

	public void setTriggerTime(String triggerTime) {
		this.triggerTime = triggerTime;
	}

	public String getTriggerTimeText() {
		return triggerTimeText;
	}

	public void setTriggerTimeText(String triggerTimeText) {
		this.triggerTimeText = triggerTimeText;
	}

	public DictEntry getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(DictEntry triggerType) {
		this.triggerType = triggerType;
	}

	public DictEntry getTriggerSubType() {
		return triggerSubType;
	}

	public void setTriggerSubType(DictEntry triggerSubType) {
		this.triggerSubType = triggerSubType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public SysEmployee getCreateBy() {
		return createBy;
	}

	public void setCreateBy(SysEmployee createBy) {
		this.createBy = createBy;
	}

	public Set<ScheduledTaskTarget> getScheduledTaskTargets() {
		return scheduledTaskTargets;
	}

	public void setScheduledTaskTargets(
			Set<ScheduledTaskTarget> scheduledTaskTargets) {
		this.scheduledTaskTargets = scheduledTaskTargets;
	}

	public Set<DealMeasure> getDealMeasures() {
		return dealMeasures;
	}

	public void setDealMeasures(Set<DealMeasure> dealMeasures) {
		this.dealMeasures = dealMeasures;
	}

	public Set<ScheduledTaskEmp> getScheduledTaskEmps() {
		return scheduledTaskEmps;
	}

	public void setScheduledTaskEmps(Set<ScheduledTaskEmp> scheduledTaskEmps) {
		this.scheduledTaskEmps = scheduledTaskEmps;
	}
}
