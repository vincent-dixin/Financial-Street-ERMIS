package com.fhd.risk.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Where;
import com.fhd.comm.entity.AlarmPlan;
import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.process.entity.ProcessRelaRisk;
import com.fhd.sys.entity.dic.DictEntryRelation;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 风险表实体
 * 
 * @author zhengjunxiang
 * @version
 * @since Ver 1.1
 * @Date 2012 2013-5-31
 * 
 * @see
 */
@Entity
@Table(name = "T_RM_RISKS")
public class Risk extends IdEntity implements Serializable {
	private static final long serialVersionUID = -4083772855355763270L;

	/**
	 * rbs:风险分类，re：风险事件
	 */
	@Column(name = "IS_RISK_CLASS")
	private String isRiskClass;

	/**
	 * 上级风险
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PARENT_ID")
	private Risk parent;

	/**
	 * 上级风险名称
	 */
	@Column(name = "PARENT_NAME")
	private String parentName;

	/**
	 * 下级风险
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	private Set<Risk> children = new HashSet<Risk>(0);

	/**
	 * 层级
	 */
	@Column(name = "ELEVEL")
	private Integer level;

	/**
	 * 主键全路径
	 */
	@Column(name = "ID_SEQ")
	private String idSeq;

	/**
	 * 是否是叶子
	 */
	@Column(name = "IS_LEAF")
	private Boolean isLeaf;

	/**
	 * 删除状态 0：不可用；1：可用
	 */
	@Column(name = "DELETE_ESTATUS")
	private String deleteStatus;

	/**
	 * 排列顺序
	 */
	@Column(name = "ESORT")
	private Integer sort;

	/**
	 * 所属公司
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPANY_ID")
	private SysOrganization company;

	/**
	 * 风险编号
	 */
	@Column(name = "RISK_CODE")
	private String code;

	/**
	 * 风险名称
	 */
	@Column(name = "RISK_NAME", length = 4000)
	private String name;

	/**
	 * 动因描述
	 */
	@Column(name = "EDESC", length = 4000)
	private String desc;

	/**
	 * 风险关联部门，包括责任部门和相关部门
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "risk")
	private Set<RiskOrg> riskOrgs = new HashSet<RiskOrg>(0);

	/**
	 * 风险关联人员，包括责任人员和相关人员
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "risk")
	private Set<RiskOrg> riskEmp = new HashSet<RiskOrg>(0);

	/**
	 * 风险关联流程，包括影响流程和控制流程
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "risk")
	private Set<ProcessRelaRisk> riskProcessures = new HashSet<ProcessRelaRisk>(
			0);

	/**
	 * 风险关联指标,包括影响指标和控制指标
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "risk")
	private Set<KpiRelaRisk> kpiRelaRisks = new HashSet<KpiRelaRisk>(0);

	/**
	 * 风险类别，关联字典数据
	 */
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "businessId")
	@Where(clause="TYPE_ID=1")
	private Set<DictEntryRelation> riskKinds = new HashSet<DictEntryRelation>(0);
	
	/**
	 * 影响风险
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "relaRisk")
	@Where(clause="ETYPE=I")
    private Set<RiskRelaRisk> iRisk = new HashSet<RiskRelaRisk>(0);
	
	/**
	 * 风险动因
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "relaRisk")
    @Where(clause="ETYPE=R")
    private Set<RiskRelaRisk> rRisk = new HashSet<RiskRelaRisk>(0);
	

	public Set<RiskRelaRisk> getiRisk() {
        return iRisk;
    }

    public void setiRisk(Set<RiskRelaRisk> iRisk) {
        this.iRisk = iRisk;
    }

    public Set<RiskRelaRisk> getrRisk() {
        return rRisk;
    }

    public void setrRisk(Set<RiskRelaRisk> rRisk) {
        this.rRisk = rRisk;
    }

    /**
	 * 是否定量
	 */
	@Column(name = "IS_FIX")
	private String isFix;

	/**
	 * 是否启用
	 */
	@Column(name = "IS_USE")
	private String isUse;

	/**
	 * 告警方案
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ALARM_SCENARIO")
	private AlarmPlan alarmScenario;

	/**
	 * 监控频率，这块先用数据库中的搜集频率字典
	 */
	@Column(name = "GATHER_FREQUENCE")
	private String monitorFrequence;

	/**
	 * 是否继承上级评估模板
	 */
	@Column(name = "IS_INHERIT")
	private String isInherit;

	/**
	 * 风险关联模板
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEMPLATE_ID")
	private Template template;

	/**
	 * 公式定义
	 */
	@Column(name = "FORMULA_DEFINE")
	private String formulaDefine;

	/**
	 * 涉及版块，关联字典数据
	 */
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "businessId")
	@Where(clause="TYPE_ID=2")
	private Set<DictEntryRelation> relePlates = new HashSet<DictEntryRelation>(0);
	
	/**
	 * 风险关联岗位
	 */
	 @OneToMany(fetch = FetchType.LAZY, mappedBy = "risk")
	 private Set<RiskOrg> riskPosition = new HashSet<RiskOrg>(0);
	
	 /**
	  * 风险评估记录
	  */
	 @OneToMany(fetch = FetchType.LAZY,mappedBy = "risk")
	 private Set<RiskAdjustHistory> adjustHistory = new HashSet<RiskAdjustHistory>(0);
	 
	// /**
	// * 是否应对
	// */
	// @Column(name = "IS_ANSWER")
	// private String isAnswer;
	//
	// /**
	// * 创建部门
	// */
	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "CREATE_ORG_ID")
	// private SysOrganization createOrg;
	//
	// /**
	// * 风险评估方式
	// */
	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "ASSESSMENT_MEASURE")
	// private DictEntry assessmentMeasure;
	//
	// /**
	// * 应对策略
	// */
	// @Column(name = "RESPONSE_STRATEGY")
	// private String responseStrategy;
	//
	// /**
	// * 风险关联定性评估记录
	// */
	//
	// /**
	// * 趋势相对于
	// */
	// @Column(name = "RELATIVE_TO")
	// private String relativeTo;
	//
	// /**
	// * 搜集频率
	// */
	// @Column(name = "GATHER_FREQUENCE")
	// private String gatherFrequence;
	//
	// /**
	// * 亮灯依据
	// */
	// @Column(name = "ALARM_MEASURE")
	// private String alarmMeasure;
	//
	// /**
	// * 影响期间
	// */
	// @Column(name = "IMPACT_TIME")
	// private String impactTime;
	//
	// /**
	// * 结果采集延期天数/扩充天数
	// */
	// @Column(name = "RESULT_COLLECT_INTERVAL")
	// private Integer resultCollectInterval;

	public Risk() {

	}

	public Risk(String id) {
		super.setId(id);
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

	public String getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(String deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getFormulaDefine() {
		return formulaDefine;
	}

	public void setFormulaDefine(String formulaDefine) {
		this.formulaDefine = formulaDefine;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getIsRiskClass() {
		return isRiskClass;
	}

	public void setIsRiskClass(String isRiskClass) {
		this.isRiskClass = isRiskClass;
	}

	public Risk getParent() {
		return parent;
	}

	public void setParent(Risk parent) {
		this.parent = parent;
	}

	public Set<Risk> getChildren() {
		return children;
	}

	public void setChildren(Set<Risk> children) {
		this.children = children;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getIdSeq() {
		return idSeq;
	}

	public void setIdSeq(String idSeq) {
		this.idSeq = idSeq;
	}

	public Boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public Set<RiskOrg> getRiskOrgs() {
		return riskOrgs;
	}

	public void setRiskOrgs(Set<RiskOrg> riskOrgs) {
		this.riskOrgs = riskOrgs;
	}

	public Set<RiskOrg> getRiskEmp() {
		return riskEmp;
	}

	public void setRiskEmp(Set<RiskOrg> riskEmp) {
		this.riskEmp = riskEmp;
	}

	public Set<KpiRelaRisk> getKpiRelaRisks() {
		return kpiRelaRisks;
	}

	public void setKpiRelaRisks(Set<KpiRelaRisk> kpiRelaRisks) {
		this.kpiRelaRisks = kpiRelaRisks;
	}

	public Set<ProcessRelaRisk> getRiskProcessures() {
		return riskProcessures;
	}

	public void setRiskProcessures(Set<ProcessRelaRisk> riskProcessures) {
		this.riskProcessures = riskProcessures;
	}

	public AlarmPlan getAlarmScenario() {
		return alarmScenario;
	}

	public void setAlarmScenario(AlarmPlan alarmScenario) {
		this.alarmScenario = alarmScenario;
	}

	public String getIsInherit() {
		return isInherit;
	}

	public void setIsInherit(String isInherit) {
		this.isInherit = isInherit;
	}

	public String getIsFix() {
		return isFix;
	}

	public void setIsFix(String isFix) {
		this.isFix = isFix;
	}

	public String getIsUse() {
		return isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	public String getMonitorFrequence() {
		return monitorFrequence;
	}

	public void setMonitorFrequence(String monitorFrequence) {
		this.monitorFrequence = monitorFrequence;
	}

	public Set<DictEntryRelation> getRiskKinds() {
		return riskKinds;
	}

	public void setRiskKinds(Set<DictEntryRelation> riskKinds) {
		this.riskKinds = riskKinds;
	}

	public Set<DictEntryRelation> getRelePlates() {
		return relePlates;
	}

	public void setRelePlates(Set<DictEntryRelation> relePlates) {
		this.relePlates = relePlates;
	}

	public Set<RiskOrg> getRiskPosition() {
		return riskPosition;
	}

	public void setRiskPosition(Set<RiskOrg> riskPosition) {
		this.riskPosition = riskPosition;
	}

	public Set<RiskAdjustHistory> getAdjustHistory() {
		return adjustHistory;
	}

	public void setAdjustHistory(Set<RiskAdjustHistory> adjustHistory) {
		this.adjustHistory = adjustHistory;
	}
}
