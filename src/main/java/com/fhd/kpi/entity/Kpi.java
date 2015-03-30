/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.kpi.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fhd.comm.entity.TimePeriod;
import com.fhd.fdc.commons.orm.hibernate.AuditableEntity;
import com.fhd.risk.entity.KpiRelaRisk;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 指标基本信息实体
 *
 * @author   胡迪新
 * @since    fhd Ver 4.5
 * @Date     2012-11-5  下午4:14:06
 *
 * @see      
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable(true)
@Table(name = "T_KPI_KPI")
public class Kpi extends AuditableEntity implements java.io.Serializable, Comparable<Kpi> {

    private static final long serialVersionUID = 2756564806248230798L;

    /**
     * KPI:指标，KC:指标类型
     */
    @Column(name = "IS_KPI_CATEGORY", length = 2000)
    private String isKpiCategory;

    /**
     * 上级指标
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private Kpi parent;

    /**
     * id全路径
     */
    @Column(name = "ID_SEQ", length = 2000)
    private String idSeq;

    /**
     * 层次
     */
    @Column(name = "ELEVEL")
    private Integer level;

    /**
     * 是否叶子
     */
    @Column(name = "IS_LEAF")
    private Boolean isLeaf;

    /**
     * 是否默认名称
     */
    @Column(name = "IS_USE_DEFAULT_NAME")
    private Boolean isNameDefault;

    /**
     * 是否继承
     */
    @Column(name = "IS_INHERIT")
    private Boolean isInherit;

    /**
     * 公司
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID")
    private SysOrganization company;

    /**
     * 编号
     */
    @Column(name = "KPI_CODE")
    private String code;

    /**
     * 名称
     */
    @Column(name = "KPI_NAME")
    private String name;

    /**
     * 简称
     */
    @Column(name = "SHORT_NAME")
    private String shortName;

    /**
     * 说明
     */
    @Column(name = "EDESC", length = 2000)
    private String desc;

    /**
     * 采集频率   日、周、月、季、半年、每年
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GATHER_FREQUENCE")
    private DictEntry gatherFrequence;

    /**
     * 采集时间公式
     */
    @Column(name = "GATHER_DAY_FORMULR", length = 2000)
    private String gatherDayFormulr;

    /**
     * 结果采集扩充天数
     */
    @Column(name = "RESULT_COLLECT_INTERVAL")
    private Integer resultCollectInterval;

    /**
     * 结果值累计计算
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FINISH_VALUE_SUM_MEASURE")
    private DictEntry resultSumMeasure;

    /**
     * 目标值累计计算
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TARGET_VALUE_SUM_MEASURE")
    private DictEntry targetSumMeasure;

    /**
     * 评估值累计计算
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ASSESSMENT_VALUE_SUM_MEASURE")
    private DictEntry assessmentSumMeasure;

    /**
     * 目标收集频率
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TARGET_SET_FREQUENCE")
    private DictEntry targetSetFrequence;

    /**
     * 目标设定时间公式
     */
    @Column(name = "TARGET_SET_DAY_FORMULAR", length = 2000)
    private String targetSetDayFormular;

    /**
     * 采集报告频率
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GATHER_REPORT_FREQUENCE")
    private DictEntry reportFrequence;

    /**
     * 采集报告频率公式
     */
    @Column(name = "GATHER_REPORT_DAY_FORMULR")
    private String gatherReportDayFormulr;

    /**
     * 目标收集报告频率
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TARGET_SET_REPORT_FREQUENCE")
    private DictEntry targetSetReportFrequence;

    /**
     * 目标收集报告频率公式
     */
    @Column(name = "TARGET_SET_REPORT_DAY_FORMULR")
    private String targetSetReportDayFormulr;

    /**
     * 起始日期
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE", length = 0)
    private Date startDate;

    /**
     * 目标公式定义
     */
    @Column(name = "TARGET_FORMULA", length = 2000)
    private String targetFormula;

    /**
     * 结果公式定义
     */
    @Column(name = "RESULT_FORMULA", length = 2000)
    private String resultFormula;

    /**
     * 关联关系公式
     */
    @Column(name = "RELATION_FORMULA")
    private String relationFormula;

    /**
     * 目标设定扩充天数
     */
    @Column(name = "TARGET_SET_INTERVAL")
    private Integer targetSetInterval;

    /**
     * 排列顺序
     */
    @Column(name = "ESORT")
    private Integer sort;

    /**
     * 删除状态
     */
    @Column(name = "DELETE_STATUS", length = 100)
    private Boolean deleteStatus;

    /**
     * 状态：
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "IS_ENABLED")
    private DictEntry status;

    /**
     * 单位
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNITS")
    private DictEntry units;
    
    /**
     * 是否计算
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IS_CALC")
    private DictEntry calc;

    /**
     * 目标值别名
     */
    @Column(name = "TARGET_VALUE_ALIAS")
    private String targetValueAlias;

    /**
     * 结果值别名
     */
    @Column(name = "RESULT_VALUE_ALIAS")
    private String resultValueAlias;

    /**
     * 是否监控
     */
    @Column(name = "IS_MONITOR", length = 100)
    private Boolean isMonitor;

    /**
     * 监控状态
     */
    @Column(name = "MONITOR_STATUS")
    private String monitorStatus;

    /**
     * 预警依据
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ALARM_BASIS")
    private DictEntry alarmBasis;

    /**
     * 数据类型
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DATA_TYPE")
    private DictEntry dataType;

    /**
     * 指标性质
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KPI_TYPE")
    private DictEntry kpiType;

    /**
     * 当前数据趋势相对于
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RELATIVE_TO")
    private DictEntry relativeTo;

    /**
     * 评估公式
     */
    @Column(name = "ASSESSMENT_FORMULA")
    private String assessmentFormula;

    /**
     * 报表小数点位置
     */
    @Column(name = "ESCALE")
    private Integer scale;

    /**
     * 预警值公式
     */
    @Column(name = "FORECAST_FORMULA")
    private String forecastFormula;

    /**
     * 亮灯依据
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ALARM_MEASURE")
    private DictEntry alarmMeasure;

    /**
     * 指标所属类型
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BELONG_KPI_CATEGORY")
    private Kpi belongKpiCategory;

    /**
     * 类型   正向指标、逆向指标。。。
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ETYPE")
    private DictEntry type;

    /**
     * 目标值是否使用公式
     */
    @Column(name = "IS_TARGET_FORMULA")
    private String isTargetFormula;

    /**
     * 结果值是否使用公式
     */
    @Column(name = "IS_RESULT_FORMULA")
    private String isResultFormula;

    /**
     * 评估值是否使用公式
     */
    @Column(name = "IS_ASSESSMENT_FORMULA")
    private String isAssessmentFormula;

    /**
     * 标杆值
     */
    @Column(name = "MODEL_VALUE")
    private Double modelValue;

    /**
     * 最大值
     */
    @Column(name = "MAX_VALUE")
    private Double maxValue;

    /**
     * 最小值
     */
    @Column(name = "MIN_VALUE")
    private Double minValue;

    /**
     * 目标关联指标
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "kpi")
    private Set<SmRelaKpi> dmRelaKpis = new HashSet<SmRelaKpi>(0);

    /**
     * 部门人员
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "kpi")
    private Set<KpiRelaOrgEmp> kpiRelaOrgEmps = new HashSet<KpiRelaOrgEmp>(0);

    /**
     * 指标关联分类
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "kpi")
    private Set<KpiRelaCategory> KpiRelaCategorys = new HashSet<KpiRelaCategory>(0);

    /**
     * 指标关联目标
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "kpi")
    private Set<SmRelaKpi> kpiRelaSm = new HashSet<SmRelaKpi>(0);

    /**
     * 指标关联预警
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "kpi")
    private Set<KpiRelaAlarm> kpiRelaAlarms = new HashSet<KpiRelaAlarm>(0);

    /**
     * 指标关联维度
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "kpi")
    private Set<KpiRelaDim> kpiRelaDims = new HashSet<KpiRelaDim>(0);

    /**
     * 指标关联风险
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "kpi")
    private Set<KpiRelaRisk> kpiRelaRisks = new HashSet<KpiRelaRisk>();

    /**
     * 指标关联采集信息
     */
    //@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "kpi")
    @OrderBy("beginTime asc")
    private Set<KpiGatherResult> kpiGatherResult = new HashSet<KpiGatherResult>(0);

    /**
     * 最后采集时间频率
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LATEST_TIME_PERIOD_ID")
    private TimePeriod lastTimePeriod;

    /**
     * 结果收集频率显示
     */
    @Column(name = "GATHER_DAY_FORMULR_SHOW")
    private String gatherDayFormulrShow;

    /**
     * 目标收集频率显示
     */
    @Column(name = "TARGET_SET_DAY_FORMULAR_SHOW")
    private String targetSetDayFormularShow;

    /**
     * 结果收集报告频率显示
     */
    @Column(name = "GATHER_REPORT_DAY_FORMULR_SHOW")
    private String gatherReportDayFormulrShow;

    /**
     * 目标收集报告频率显示
     */
    @Column(name = "TARGET_REPORT_DAY_FORMULR_SHOW")
    private String targetReportDayFormulrShow;

    /**
     * 指标采集频率时间换算
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "CALCULATE_TIME")
    private Date calculatetime;

    /**
     * 指标目标频率时间换算
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "TARGET_CALCULATE_TIME")
    private Date targetCalculatetime;

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(name).append(super.getId()).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean flag = false;
        if (obj != null && Kpi.class.isAssignableFrom(obj.getClass())) {
            Kpi k = (Kpi) obj;
            flag = new EqualsBuilder().append(name, k.getName()).append(super.getId(), k.getId()).isEquals();
        }
        return flag;
    }

    @Override
    public int compareTo(Kpi o) {
        return this.getName().compareTo(o.getName());
    }

    public Kpi() {
        super();
    }

    public Set<KpiRelaRisk> getKpiRelaRisks() {
        return kpiRelaRisks;
    }

    public void setKpiRelaRisks(Set<KpiRelaRisk> kpiRelaRisks) {
        this.kpiRelaRisks = kpiRelaRisks;
    }

    public String getIsKpiCategory() {
        return isKpiCategory;
    }

    public void setIsKpiCategory(String isKpiCategory) {
        this.isKpiCategory = isKpiCategory;
    }

    public Kpi getParent() {
        return parent;
    }

    public void setParent(Kpi parent) {
        this.parent = parent;
    }

    public String getIdSeq() {
        return idSeq;
    }

    public String getTargetReportDayFormulrShow() {
        return targetReportDayFormulrShow;
    }

    public void setTargetReportDayFormulrShow(String targetReportDayFormulrShow) {
        this.targetReportDayFormulrShow = targetReportDayFormulrShow;
    }

    public void setIdSeq(String idSeq) {
        this.idSeq = idSeq;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Boolean getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public Boolean getIsNameDefault() {
        return isNameDefault;
    }

    public void setIsNameDefault(Boolean isNameDefault) {
        this.isNameDefault = isNameDefault;
    }

    public Boolean getIsInherit() {
        return isInherit;
    }

    public void setIsInherit(Boolean isInherit) {
        this.isInherit = isInherit;
    }

    public SysOrganization getCompany() {
        return company;
    }

    public void setCompany(SysOrganization company) {
        this.company = company;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public DictEntry getGatherFrequence() {
        return gatherFrequence;
    }

    public void setGatherFrequence(DictEntry gatherFrequence) {
        this.gatherFrequence = gatherFrequence;
    }

    public String getGatherDayFormulr() {
        return gatherDayFormulr;
    }

    public void setGatherDayFormulr(String gatherDayFormulr) {
        this.gatherDayFormulr = gatherDayFormulr;
    }

    public Integer getResultCollectInterval() {
        return resultCollectInterval;
    }

    public void setResultCollectInterval(Integer resultCollectInterval) {
        this.resultCollectInterval = resultCollectInterval;
    }

    public DictEntry getResultSumMeasure() {
        return resultSumMeasure;
    }

    public void setResultSumMeasure(DictEntry resultSumMeasure) {
        this.resultSumMeasure = resultSumMeasure;
    }

    public DictEntry getTargetSumMeasure() {
        return targetSumMeasure;
    }

    public void setTargetSumMeasure(DictEntry targetSumMeasure) {
        this.targetSumMeasure = targetSumMeasure;
    }

    public DictEntry getAssessmentSumMeasure() {
        return assessmentSumMeasure;
    }

    public void setAssessmentSumMeasure(DictEntry assessmentSumMeasure) {
        this.assessmentSumMeasure = assessmentSumMeasure;
    }

    public DictEntry getTargetSetFrequence() {
        return targetSetFrequence;
    }

    public void setTargetSetFrequence(DictEntry targetSetFrequence) {
        this.targetSetFrequence = targetSetFrequence;
    }

    public String getTargetSetDayFormular() {
        return targetSetDayFormular;
    }

    public void setTargetSetDayFormular(String targetSetDayFormular) {
        this.targetSetDayFormular = targetSetDayFormular;
    }

    public DictEntry getReportFrequence() {
        return reportFrequence;
    }

    public void setReportFrequence(DictEntry reportFrequence) {
        this.reportFrequence = reportFrequence;
    }

    public String getGatherReportDayFormulr() {
        return gatherReportDayFormulr;
    }

    public void setGatherReportDayFormulr(String gatherReportDayFormulr) {
        this.gatherReportDayFormulr = gatherReportDayFormulr;
    }

    public DictEntry getTargetSetReportFrequence() {
        return targetSetReportFrequence;
    }

    public void setTargetSetReportFrequence(DictEntry targetSetReportFrequence) {
        this.targetSetReportFrequence = targetSetReportFrequence;
    }

    public String getTargetSetReportDayFormulr() {
        return targetSetReportDayFormulr;
    }

    public void setTargetSetReportDayFormulr(String targetSetReportDayFormulr) {
        this.targetSetReportDayFormulr = targetSetReportDayFormulr;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getTargetFormula() {
        return targetFormula;
    }

    public void setTargetFormula(String targetFormula) {
        this.targetFormula = targetFormula;
    }

    public String getResultFormula() {
        return resultFormula;
    }

    public void setResultFormula(String resultFormula) {
        this.resultFormula = resultFormula;
    }

    public String getRelationFormula() {
        return relationFormula;
    }

    public void setRelationFormula(String relationFormula) {
        this.relationFormula = relationFormula;
    }

    public Integer getTargetSetInterval() {
        return targetSetInterval;
    }

    public void setTargetSetInterval(Integer targetSetInterval) {
        this.targetSetInterval = targetSetInterval;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Boolean getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Boolean deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public DictEntry getStatus() {
        return status;
    }

    public void setStatus(DictEntry status) {
        this.status = status;
    }

    public DictEntry getUnits() {
        return units;
    }

    public void setUnits(DictEntry units) {
        this.units = units;
    }

    public String getTargetValueAlias() {
        return targetValueAlias;
    }

    public void setTargetValueAlias(String targetValueAlias) {
        this.targetValueAlias = targetValueAlias;
    }

    public String getResultValueAlias() {
        return resultValueAlias;
    }

    public void setResultValueAlias(String resultValueAlias) {
        this.resultValueAlias = resultValueAlias;
    }

    public Boolean getIsMonitor() {
        return isMonitor;
    }

    public void setIsMonitor(Boolean isMonitor) {
        this.isMonitor = isMonitor;
    }

    public String getMonitorStatus() {
        return monitorStatus;
    }

    public void setMonitorStatus(String monitorStatus) {
        this.monitorStatus = monitorStatus;
    }

    public DictEntry getAlarmBasis() {
        return alarmBasis;
    }

    public void setAlarmBasis(DictEntry alarmBasis) {
        this.alarmBasis = alarmBasis;
    }

    public DictEntry getDataType() {
        return dataType;
    }

    public void setDataType(DictEntry dataType) {
        this.dataType = dataType;
    }

    public DictEntry getKpiType() {
        return kpiType;
    }

    public void setKpiType(DictEntry kpiType) {
        this.kpiType = kpiType;
    }

    public DictEntry getRelativeTo() {
        return relativeTo;
    }

    public void setRelativeTo(DictEntry relativeTo) {
        this.relativeTo = relativeTo;
    }

    public String getAssessmentFormula() {
        return assessmentFormula;
    }

    public void setAssessmentFormula(String assessmentFormula) {
        this.assessmentFormula = assessmentFormula;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public String getForecastFormula() {
        return forecastFormula;
    }

    public void setForecastFormula(String forecastFormula) {
        this.forecastFormula = forecastFormula;
    }

    public DictEntry getAlarmMeasure() {
        return alarmMeasure;
    }

    public void setAlarmMeasure(DictEntry alarmMeasure) {
        this.alarmMeasure = alarmMeasure;
    }

    public Kpi getBelongKpiCategory() {
        return belongKpiCategory;
    }

    public void setBelongKpiCategory(Kpi belongKpiCategory) {
        this.belongKpiCategory = belongKpiCategory;
    }

    public DictEntry getType() {
        return type;
    }

    public void setType(DictEntry type) {
        this.type = type;
    }

    public String getIsTargetFormula() {
        return isTargetFormula;
    }

    public void setIsTargetFormula(String isTargetFormula) {
        this.isTargetFormula = isTargetFormula;
    }

    public String getIsResultFormula() {
        return isResultFormula;
    }

    public void setIsResultFormula(String isResultFormula) {
        this.isResultFormula = isResultFormula;
    }

    public String getIsAssessmentFormula() {
        return isAssessmentFormula;
    }

    public void setIsAssessmentFormula(String isAssessmentFormula) {
        this.isAssessmentFormula = isAssessmentFormula;
    }

    public Double getModelValue() {
        return modelValue;
    }

    public void setModelValue(Double modelValue) {
        this.modelValue = modelValue;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public Double getMinValue() {
        return minValue;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    public Set<SmRelaKpi> getDmRelaKpis() {
        return dmRelaKpis;
    }

    public void setDmRelaKpis(Set<SmRelaKpi> dmRelaKpis) {
        this.dmRelaKpis = dmRelaKpis;
    }

    public Set<KpiRelaOrgEmp> getKpiRelaOrgEmps() {
        return kpiRelaOrgEmps;
    }

    public void setKpiRelaOrgEmps(Set<KpiRelaOrgEmp> kpiRelaOrgEmps) {
        this.kpiRelaOrgEmps = kpiRelaOrgEmps;
    }

    public Set<KpiRelaCategory> getKpiRelaCategorys() {
        return KpiRelaCategorys;
    }

    public void setKpiRelaCategorys(Set<KpiRelaCategory> kpiRelaCategorys) {
        KpiRelaCategorys = kpiRelaCategorys;
    }

    public Set<SmRelaKpi> getKpiRelaSm() {
        return kpiRelaSm;
    }

    public void setKpiRelaSm(Set<SmRelaKpi> kpiRelaSm) {
        this.kpiRelaSm = kpiRelaSm;
    }

    public Set<KpiRelaAlarm> getKpiRelaAlarms() {
        return kpiRelaAlarms;
    }

    public void setKpiRelaAlarms(Set<KpiRelaAlarm> kpiRelaAlarms) {
        this.kpiRelaAlarms = kpiRelaAlarms;
    }

    public Set<KpiRelaDim> getKpiRelaDims() {
        return kpiRelaDims;
    }

    public void setKpiRelaDims(Set<KpiRelaDim> kpiRelaDims) {
        this.kpiRelaDims = kpiRelaDims;
    }

    public Set<KpiGatherResult> getKpiGatherResult() {
        return kpiGatherResult;
    }

    public void setKpiGatherResult(Set<KpiGatherResult> kpiGatherResult) {
        this.kpiGatherResult = kpiGatherResult;
    }

    public TimePeriod getLastTimePeriod() {
        return lastTimePeriod;
    }

    public void setLastTimePeriod(TimePeriod lastTimePeriod) {
        this.lastTimePeriod = lastTimePeriod;
    }

    public String getGatherDayFormulrShow() {
        return gatherDayFormulrShow;
    }

    public void setGatherDayFormulrShow(String gatherDayFormulrShow) {
        this.gatherDayFormulrShow = gatherDayFormulrShow;
    }

    public String getTargetSetDayFormularShow() {
        return targetSetDayFormularShow;
    }

    public void setTargetSetDayFormularShow(String targetSetDayFormularShow) {
        this.targetSetDayFormularShow = targetSetDayFormularShow;
    }

    public String getGatherReportDayFormulrShow() {
        return gatherReportDayFormulrShow;
    }

    public void setGatherReportDayFormulrShow(String gatherReportDayFormulrShow) {
        this.gatherReportDayFormulrShow = gatherReportDayFormulrShow;
    }

    public Date getCalculatetime() {
        return calculatetime;
    }

    public void setCalculatetime(Date calculatetime) {
        this.calculatetime = calculatetime;
    }

    public Date getTargetCalculatetime() {
        return targetCalculatetime;
    }

    public void setTargetCalculatetime(Date targetCalculatetime) {
        this.targetCalculatetime = targetCalculatetime;
    }
    
    public DictEntry getCalc() {
        return calc;
    }

    public void setCalc(DictEntry calc) {
        this.calc = calc;
    }
    
}
