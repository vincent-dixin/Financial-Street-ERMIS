/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */
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
import com.fhd.sys.entity.file.FileUploadEntity;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 指标采集结果
 *
 * @author   胡迪新
 * @since    fhd Ver 4.5
 * @Date	 2012-10-25  下午2:28:47
 *
 * @see 	 
 */
@Entity
@Table(name = "T_KPI_KPI_GATHER_RESULT")
public class KpiGatherResult extends IdEntity implements Serializable {

    private static final long serialVersionUID = -7499607582729661779L;

    /**
     * 公司ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID")
    private SysOrganization company;

    /**
     * 累积值
     */
    @Column(name = "ACCUMULATED_VALUE")
    private Double accumulatedValue;

    /**
     * 年度目标值
     */
    @Column(name = "ANUAL_VALUE")
    private Double anualValue;

    /**
     * 评估值
     */
    @Column(name = "ASSESSMENT_VALUE", precision = 23, scale = 4)
    private Double assessmentValue;

    /**
     * 告警状态
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ASSESSMENT_STATUS")
    private DictEntry assessmentStatus;

    /**
     * 告警方案
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ASSESSMENT_ALARM_ID")
    private AlarmPlan assessmentAlarm;

    /**
     * 开始时间
     */
    //@Temporal(TemporalType.DATE)
    @Column(name = "BEGIN_TIME")
    private Date beginTime;

    /**
     * 结束时间
     */
    //@Temporal(TemporalType.DATE)
    @Column(name = "END_TIME")
    private Date endTime;

    /**
     * 趋势
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DIRECTION")
    private DictEntry direction;

    /**
     * 趋势
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILE_ID")
    private FileUploadEntity file;

    /**
     * 完成值
     */
    @Column(name = "FINISH_VALUE", precision = 23, scale = 4)
    private Double finishValue;

    /**
     * 预警方案
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FORECAST_ALARM_ID")
    private AlarmPlan forecastAlarm;

    /**
     * 预警状态
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FORECAST_STATUS")
    private DictEntry forecastStatus;

    /**
     * 采集时间
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "GATHER_TIME")
    private Date gatherTime;

    /**
     * 指标
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KPI_ID")
    private Kpi kpi;

    /**
     * 标杆值
     */
    @Column(name = "MODEL_VALUE", precision = 23, scale = 4)
    private Double modelValue;

    /**
     * 目标值
     */
    @Column(name = "TARGET_VALUE", precision = 23, scale = 4)
    private Double targetValue;

    /**
     * (实际值)同比值
     */
    @Column(name = "SAME_VALUE", precision = 23, scale = 4)
    private Double sameValue;

    /**
     * (目标值)同比值
     */
    @Column(name = "TARGET_SAME_VALUE", precision = 23, scale = 4)
    private Double targetSameValue;

    /**
     * (实际值)环比值
     */
    @Column(name = "RATIO_VALUE", precision = 23, scale = 4)
    private Double ratioValue;

    /**
     * (目标值)环比值
     */
    @Column(name = "TARGET_RATIO_VALUE", precision = 23, scale = 4)
    private Double targetRatioValue;

    /**
     * 时间区间
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIME_PERIOD_ID")
    private TimePeriod timePeriod;

    /**
     * 临时目标值
     */
    @Column(name = "TEMP_TARGET_VALUE")
    private Double tempTargetValue;

    /**
     * 临时完成值
     */
    @Column(name = "TEMP_FINISH_VALUE")
    private Double tempFinishValue;

    /**
     * 目标值设置状态：保存/提交
     */
    @Column(name = "TARGET_SET_STATUS")
    private String targetSetStatus;

    /**
     * 完成值设置状态：保存/提交
     */
    @Column(name = "RESULT_GATHER_STATUS")
    private String finishSetStatus;

    /**
     * 目标值录入人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INPUT_EMP")
    private SysEmployee targetValueInputEmp;

    /**
     * 结果值录入人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESULT_INPUT_EMP")
    private SysEmployee resultValueInputEmp;

    public Double getTargetSameValue() {
        return targetSameValue;
    }

    public void setTargetSameValue(Double targetSameValue) {
        this.targetSameValue = targetSameValue;
    }

    public Double getTargetRatioValue() {
        return targetRatioValue;
    }

    public void setTargetRatioValue(Double targetRatioValue) {
        this.targetRatioValue = targetRatioValue;
    }

    public KpiGatherResult() {
        super();
    }

    public Double getSameValue() {
        return sameValue;
    }

    public void setSameValue(Double sameValue) {
        this.sameValue = sameValue;
    }

    public Double getRatioValue() {
        return ratioValue;
    }

    public void setRatioValue(Double ratioValue) {
        this.ratioValue = ratioValue;
    }

    public KpiGatherResult(SysOrganization company, Double accumulatedValue, Double anualValue, Double assessmentValue, DictEntry assessmentStatus,
            AlarmPlan assessmentAlarm, Date beginTime, Date endTime, DictEntry direction, FileUploadEntity file, Double finishValue,
            AlarmPlan forecastAlarm, DictEntry forecastStatus, Date gatherTime, Kpi kpi, Double modelValue, Double targetValue,
            TimePeriod timePeriod, Double tempTargetValue, Double tempFinishValue, String targetSetStatus, String finishSetStatus,
            SysEmployee targetValueInputEmp, SysEmployee resultValueInputEmp) {
        super();
        this.company = company;
        this.accumulatedValue = accumulatedValue;
        this.anualValue = anualValue;
        this.assessmentValue = assessmentValue;
        this.assessmentStatus = assessmentStatus;
        this.assessmentAlarm = assessmentAlarm;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.direction = direction;
        this.file = file;
        this.finishValue = finishValue;
        this.forecastAlarm = forecastAlarm;
        this.forecastStatus = forecastStatus;
        this.gatherTime = gatherTime;
        this.kpi = kpi;
        this.modelValue = modelValue;
        this.targetValue = targetValue;
        this.timePeriod = timePeriod;
        this.tempTargetValue = tempTargetValue;
        this.tempFinishValue = tempFinishValue;
        this.targetSetStatus = targetSetStatus;
        this.finishSetStatus = finishSetStatus;
        this.targetValueInputEmp = targetValueInputEmp;
        this.resultValueInputEmp = resultValueInputEmp;
    }

    public SysOrganization getCompany() {
        return company;
    }

    public void setCompany(SysOrganization company) {
        this.company = company;
    }

    public Double getAccumulatedValue() {
        return accumulatedValue;
    }

    public void setAccumulatedValue(Double accumulatedValue) {
        this.accumulatedValue = accumulatedValue;
    }

    public Double getAnualValue() {
        return anualValue;
    }

    public void setAnualValue(Double anualValue) {
        this.anualValue = anualValue;
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

    public AlarmPlan getAssessmentAlarm() {
        return assessmentAlarm;
    }

    public void setAssessmentAlarm(AlarmPlan assessmentAlarm) {
        this.assessmentAlarm = assessmentAlarm;
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

    public DictEntry getDirection() {
        return direction;
    }

    public void setDirection(DictEntry direction) {
        this.direction = direction;
    }

    public FileUploadEntity getFile() {
        return file;
    }

    public void setFile(FileUploadEntity file) {
        this.file = file;
    }

    public Double getFinishValue() {
        return finishValue;
    }

    public void setFinishValue(Double finishValue) {
        this.finishValue = finishValue;
    }

    public AlarmPlan getForecastAlarm() {
        return forecastAlarm;
    }

    public void setForecastAlarm(AlarmPlan forecastAlarm) {
        this.forecastAlarm = forecastAlarm;
    }

    public DictEntry getForecastStatus() {
        return forecastStatus;
    }

    public void setForecastStatus(DictEntry forecastStatus) {
        this.forecastStatus = forecastStatus;
    }

    public Date getGatherTime() {
        return gatherTime;
    }

    public void setGatherTime(Date gatherTime) {
        this.gatherTime = gatherTime;
    }

    public Kpi getKpi() {
        return kpi;
    }

    public void setKpi(Kpi kpi) {
        this.kpi = kpi;
    }

    public Double getModelValue() {
        return modelValue;
    }

    public void setModelValue(Double modelValue) {
        this.modelValue = modelValue;
    }

    public Double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Double targetValue) {
        this.targetValue = targetValue;
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }

    public Double getTempTargetValue() {
        return tempTargetValue;
    }

    public void setTempTargetValue(Double tempTargetValue) {
        this.tempTargetValue = tempTargetValue;
    }

    public Double getTempFinishValue() {
        return tempFinishValue;
    }

    public void setTempFinishValue(Double tempFinishValue) {
        this.tempFinishValue = tempFinishValue;
    }

    public String getTargetSetStatus() {
        return targetSetStatus;
    }

    public void setTargetSetStatus(String targetSetStatus) {
        this.targetSetStatus = targetSetStatus;
    }

    public String getFinishSetStatus() {
        return finishSetStatus;
    }

    public void setFinishSetStatus(String finishSetStatus) {
        this.finishSetStatus = finishSetStatus;
    }

    public SysEmployee getTargetValueInputEmp() {
        return targetValueInputEmp;
    }

    public void setTargetValueInputEmp(SysEmployee targetValueInputEmp) {
        this.targetValueInputEmp = targetValueInputEmp;
    }

    public SysEmployee getResultValueInputEmp() {
        return resultValueInputEmp;
    }

    public void setResultValueInputEmp(SysEmployee resultValueInputEmp) {
        this.resultValueInputEmp = resultValueInputEmp;
    }
}
