package com.fhd.kpi.entity;

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
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fhd.fdc.commons.orm.hibernate.AuditableEntity;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 战略目标
 *
 * @author   杨鹏
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-8-15		上午11:29:12
 *
 * @see
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable(true)
@Table(name = "T_KPI_STRATEGY_MAP")
public class StrategyMap extends AuditableEntity implements java.io.Serializable
{

    /**
     *
     * @author 杨鹏
     * @since  fhd　Ver 1.1
     */

    private static final long serialVersionUID = 7215164038071849189L;

    /**
     * 父节点
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "PARENT_ID")
    private StrategyMap parent;

    /**
     * 子节点
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private Set<StrategyMap> childrens;

    /**
     * 名称
     */
    @Column(name = "STRATEGY_MAP_NAME")
    private String name;

    /**
     * 预警公式
     */
    @Column(name = "FORECAST_FORMULA")
    private String warningFormula;
    
    /**
     * 评估值公式
     */
    @Column(name = "ASSESSMENT_FORMULA")
    private String assessmentFormula;

    /**
     * 编号：
     */
    @Column(name = "STRATEGY_MAP_CODE")
    private String code = "";

    /**
     * 简称
     */
    @Column(name = "SHORT_NAME")
    private String shortName;

    /**
     * ID层级序列：例.parentId.id.
     */
    @Column(name = "ID_SEQ")
    private String idSeq;

    /**
     * 层级：最高层为1
     */
    @Column(name = "ELEVEL")
    private Integer level;

    /**
     * 排序
     */
    @Column(name = "ESORT")
    private Integer sort;

    /**
     * 是否叶子节点
     */
    @Column(name = "IS_LEAF")
    private Boolean isLeaf;

    /**
     * 说明
     */
    @Column(name = "EDESC")
    private String desc;
    
    /**
     * 图表类型
     */
    @Column(name = "CHART_TYPE")
    private String chartType;


    /**
     * 所属公司
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "company_id")
    private SysOrganization company;

    /**
     * 状态：
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "estatus")
    private DictEntry status;

    /**
     * 删除状态：true：未删除，false：已删除
     */
    @Column(name = "DELETE_STATUS")
    private Boolean deleteStatus;

    /**
     * 当前数据趋势相对于
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RELATIVE_TO")
    private DictEntry relativeTo;

    /*
     * 目标关联预警\告警
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "strategyMap")
    private Set<SmRelaAlarm> smRelaAlarms = new HashSet<SmRelaAlarm>(0);

    /*
     * 目标关联部门
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "strategyMap")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @BatchSize(size = 10)
    private Set<SmRelaOrgEmp> smRelaOrgEmps = new HashSet<SmRelaOrgEmp>(0);

    /*
     * 目标关联维度
     */
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "strategyMap")
    private Set<SmRelaDim> smRelaDims = new HashSet<SmRelaDim>();

    /*
     * 目标关联主题
     */
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "strategyMap")
    private Set<SmRelaTheme> smRelaThemes = new HashSet<SmRelaTheme>();

    /*
     * 目标关联指标
     */
    //@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "strategyMap")
    private Set<SmRelaKpi> smRelaKpi = new HashSet<SmRelaKpi>(0);

    /*
     * 目标关联评分结果
     */
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "objectId")
    private Set<RelaAssessResult> relaAssessResults = new HashSet<RelaAssessResult>(0);

    
    public String getAssessmentFormula()
    {
        return assessmentFormula;
    }

    public void setAssessmentFormula(String assessmentFormula)
    {
        this.assessmentFormula = assessmentFormula;
    }

    
    public Set<SmRelaAlarm> getSmRelaAlarms()
    {
        return smRelaAlarms;
    }

    public void setSmRelaAlarms(Set<SmRelaAlarm> smRelaAlarms)
    {
        this.smRelaAlarms = smRelaAlarms;
    }

    public Set<SmRelaDim> getSmRelaDims()
    {
        return smRelaDims;
    }

    public void setSmRelaDims(Set<SmRelaDim> smRelaDims)
    {
        this.smRelaDims = smRelaDims;
    }

    public Set<RelaAssessResult> getRelaAssessResults() {
		return relaAssessResults;
	}
    public String getChartType()
    {
        return chartType;
    }

    public void setChartType(String chartType)
    {
        this.chartType = chartType;
    }
	public void setRelaAssessResults(Set<RelaAssessResult> relaAssessResults) {
		this.relaAssessResults = relaAssessResults;
	}

	public String getWarningFormula()
    {
        return warningFormula;
    }

    public void setWarningFormula(String warningFormula)
    {
        this.warningFormula = warningFormula;
    }

    public Set<SmRelaKpi> getSmRelaKpi()
    {
        return smRelaKpi;
    }

    public Set<SmRelaTheme> getSmRelaThemes()
    {
        return smRelaThemes;
    }

    public void setSmRelaThemes(Set<SmRelaTheme> smRelaThemes)
    {
        this.smRelaThemes = smRelaThemes;
    }

    public void setSmRelaKpi(Set<SmRelaKpi> smRelaKpi)
    {
        this.smRelaKpi = smRelaKpi;
    }

    public Set<SmRelaOrgEmp> getSmRelaOrgEmps()
    {
        return smRelaOrgEmps;
    }

    public void setSmRelaOrgEmps(Set<SmRelaOrgEmp> smRelaOrgEmps)
    {
        this.smRelaOrgEmps = smRelaOrgEmps;
    }

    public StrategyMap getParent()
    {
        return parent;
    }

    public void setParent(StrategyMap parent)
    {
        this.parent = parent;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getIdSeq()
    {
        return idSeq;
    }

    public void setIdSeq(String idSeq)
    {
        this.idSeq = idSeq;
    }

    public Integer getLevel()
    {
        return level;
    }

    public void setLevel(Integer level)
    {
        this.level = level;
    }

    public Integer getSort()
    {
        return sort;
    }

    public void setSort(Integer sort)
    {
        this.sort = sort;
    }

    public Set<StrategyMap> getChildrens()
    {
        return childrens;
    }

    public void setChildrens(Set<StrategyMap> childrens)
    {
        this.childrens = childrens;
    }

    public Boolean getIsLeaf()
    {
        return isLeaf;
    }

    public void setIsLeaf(Boolean isLeaf)
    {
        this.isLeaf = isLeaf;
    }

    public SysOrganization getCompany()
    {
        return company;
    }

    public void setCompany(SysOrganization company)
    {
        this.company = company;
    }

    public DictEntry getStatus()
    {
        return status;
    }

    public void setStatus(DictEntry status)
    {
        this.status = status;
    }

    public Boolean getDeleteStatus()
    {
        return deleteStatus;
    }

    public void setDeleteStatus(Boolean deleteStatus)
    {
        this.deleteStatus = deleteStatus;
    }

    public String getShortName()
    {
        return shortName;
    }

    public void setShortName(String shortName)
    {
        this.shortName = shortName;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public DictEntry getRelativeTo()
    {
        return relativeTo;
    }

    public void setRelativeTo(DictEntry relativeTo)
    {
        this.relativeTo = relativeTo;
    }

}