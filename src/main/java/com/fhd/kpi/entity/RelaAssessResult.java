package com.fhd.kpi.entity;

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

import com.fhd.comm.entity.AlarmPlan;
import com.fhd.comm.entity.TimePeriod;
import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 记分卡，指标类型，战略目标相关评估结果分值表.
 * @author   吴德福
 * @version  
 * @since    Ver 1.1
 * @Date     2013-1-7  下午5:25:44
 * @see	 
 */
@Entity
@Table(name = "T_KPI_SM_ASSESS_RESULT")
public class RelaAssessResult extends IdEntity implements Serializable{

	private static final long serialVersionUID = -6399018057361914308L;
	/**
	 * 对象id
	 */
    @Column(name = "OBJECT_ID")
	private String objectId;
    /**
     * 对象名称
     */
    @Column(name = "OBJECT_NAME")
	private String objectName;
	/**
	 * 时间区间维度
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIME_PERIOD_ID")
	private TimePeriod timePeriod;
	/**
	 * 开始时间
	 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "BEGIN_TIME")
	private Date beginTime;
	/**
	 * 结束时间
	 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_TIME")
	private Date endTime;
	/**
	 * 评估值
	 */
	@Column(name = "ASSESSMENT_VALUE")
	private Double assessmentValue;
	/**
	 * 评估状态
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ASSESSMENT_STATUS")
	private DictEntry assessmentStatus;
	/**
	 * 评估值预警方案
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ASSESSMENT_ALARM_ID")
	private AlarmPlan assessmentAlarmPlan;
	/**
	 * 预警值
	 */
	@Column(name = "FORECAST_VALUE")
	private Double forecastValue;
	/**
	 * 预警状态
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FORECAST_STATUS")
	private DictEntry forecastStatus;
    /**
     * 预期值预警方案
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FORECAST_ALARM_ID")
	private AlarmPlan forcastAlarmPlan;
	/**
	 * 趋势
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TREND")
	private DictEntry trend;
	/**
	 * 数据类型：记分卡-C，指标类型-K，战略目标-S
	 */
    @Column(name = "DATA_TYPE")
	private String dataType;

	public RelaAssessResult() {
		super();
	}

	public RelaAssessResult(String objectId, String objectName,
			TimePeriod timePeriod, Date beginTime, Date endTime,
			Double assessmentValue, DictEntry assessmentStatus,
			AlarmPlan assessmentAlarmPlan, Double forecastValue,
			DictEntry forecastStatus, AlarmPlan forcastAlarmPlan,
			DictEntry trend, String dataType) {
		super();
		this.objectId = objectId;
		this.objectName = objectName;
		this.timePeriod = timePeriod;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.assessmentValue = assessmentValue;
		this.assessmentStatus = assessmentStatus;
		this.assessmentAlarmPlan = assessmentAlarmPlan;
		this.forecastValue = forecastValue;
		this.forecastStatus = forecastStatus;
		this.forcastAlarmPlan = forcastAlarmPlan;
		this.trend = trend;
		this.dataType = dataType;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public TimePeriod getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(TimePeriod timePeriod) {
		this.timePeriod = timePeriod;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Double getAssessmentValue() {
		return assessmentValue;
	}

	public void setAssessmentValue(Double assessmentValue) {
		this.assessmentValue = assessmentValue;
	}

	public DictEntry getAssessmentStatus() {
		return assessmentStatus;
	}

	public void setAssessmentStatus(DictEntry assessmentStatus) {
		this.assessmentStatus = assessmentStatus;
	}

	public AlarmPlan getAssessmentAlarmPlan() {
		return assessmentAlarmPlan;
	}

	public void setAssessmentAlarmPlan(AlarmPlan assessmentAlarmPlan) {
		this.assessmentAlarmPlan = assessmentAlarmPlan;
	}

	public Double getForecastValue() {
		return forecastValue;
	}

	public void setForecastValue(Double forecastValue) {
		this.forecastValue = forecastValue;
	}

	public DictEntry getForecastStatus() {
		return forecastStatus;
	}

	public void setForecastStatus(DictEntry forecastStatus) {
		this.forecastStatus = forecastStatus;
	}

	public AlarmPlan getForcastAlarmPlan() {
		return forcastAlarmPlan;
	}

	public void setForcastAlarmPlan(AlarmPlan forcastAlarmPlan) {
		this.forcastAlarmPlan = forcastAlarmPlan;
	}

	public DictEntry getTrend() {
		return trend;
	}

	public void setTrend(DictEntry trend) {
		this.trend = trend;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
}
