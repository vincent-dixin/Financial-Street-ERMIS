/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.comm.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.kpi.entity.KpiRelaCategory;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 分类
 *
 * @author   胡迪新
 * @since    fhd Ver 4.5
 * @Date	 2012-10-25  下午2:14:44
 *
 * @see 	 
 */
@Entity
@Table(name = "T_COM_CATEGORY")
public class Category extends IdEntity implements Serializable
{

    private static final long serialVersionUID = -8279404858381319479L;

    /**
     * 名称
     */
    @Column(name = "CATEGORY_NAME")
    private String name;

    /**
     * 代码
     */
    @Column(name = "CATEGORY_CODE")
    private String code;

    /**
     * 上级指标
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private Category parent;

    /**
     * 预警公式
     */
    @Column(name = "FORECAST_FORMULA")
    private String forecastFormula;

    /**
     * 状态：
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IS_ENABLED")
    private DictEntry status;

    /**
     * 是否叶子节点
     */
    @Column(name = "IS_LEAF")
    private Boolean isLeaf;

    /**
     * 说明
     */
    @Column(name = "EDESC", length = 2000)
    private String desc;

    /**
     * 层次
     */
    @Column(name = "ELEVEL")
    private Integer level;

    /**
     * 排序
     */
    @Column(name = "ESORT")
    private Integer sort;

    /**
     * id全路径
     */
    @Column(name = "ID_SEQ", length = 2000)
    private String idSeq;

    /**
     * 删除状态
     */
    @Column(name = "DELETE_STATUS", length = 100)
    private Boolean deleteStatus;

    /**
     * 部门人员
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "category")
    private Set<CategoryRelaOrgEmp> categoryRelaOrgEmps = new HashSet<CategoryRelaOrgEmp>(0);

    /**
     * 预警
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    private Set<CategoryRelaAlarm> categoryRelaAlarms = new HashSet<CategoryRelaAlarm>(0);

    /**
     * 维度关联的指标信息
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    private Set<KpiRelaCategory> kpiRelaCategorys = new HashSet<KpiRelaCategory>(0);

    /**
     * 所属公司
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "company_id")
    private SysOrganization company;

    /**
     * 图表类型
     */
    @Column(name = "CHART_TYPE")
    private String chartType;
    
    /**
     * 评估值公式
     */
    @Column(name = "ASSESSMENT_FORMULA", length = 100)
    private String assessmentFormula;
    
    /**
     * 是否生成度量指标：
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IS_GENERATE_KPI")
    private DictEntry createKpi;
    
    /**
     * 是否计算
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IS_CALC")
    private DictEntry calc;
   

    /**
     * 数据类型
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DATA_TYPE")
    private DictEntry dateType;

    public DictEntry getCalc() {
        return calc;
    }

    public void setCalc(DictEntry calc) {
        this.calc = calc;
    }
    
    public String getAssessmentFormula()
    {
        return assessmentFormula;
    }

    public void setAssessmentFormula(String assessmentFormula)
    {
        this.assessmentFormula = assessmentFormula;
    }

    public DictEntry getCreateKpi()
    {
        return createKpi;
    }

    public void setCreateKpi(DictEntry createKpi)
    {
        this.createKpi = createKpi;
    }

    public DictEntry getDateType()
    {
        return dateType;
    }

    public void setDateType(DictEntry dateType)
    {
        this.dateType = dateType;
    }

    public String getChartType()
    {
        return chartType;
    }

    public void setChartType(String chartType)
    {
        this.chartType = chartType;
    }

    public SysOrganization getCompany()
    {
        return company;
    }

    public void setCompany(SysOrganization company)
    {
        this.company = company;
    }

    public Set<KpiRelaCategory> getKpiRelaCategorys()
    {
        return kpiRelaCategorys;
    }

    public void setKpiRelaCategorys(Set<KpiRelaCategory> kpiRelaCategorys)
    {
        this.kpiRelaCategorys = kpiRelaCategorys;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public Set<CategoryRelaAlarm> getCategoryRelaAlarms()
    {
        return categoryRelaAlarms;
    }

    public void setCategoryRelaAlarms(Set<CategoryRelaAlarm> categoryRelaAlarms)
    {
        this.categoryRelaAlarms = categoryRelaAlarms;
    }

    public Set<CategoryRelaOrgEmp> getCategoryRelaOrgEmps()
    {
        return categoryRelaOrgEmps;
    }

    public void setCategoryRelaOrgEmps(Set<CategoryRelaOrgEmp> categoryRelaOrgEmps)
    {
        this.categoryRelaOrgEmps = categoryRelaOrgEmps;
    }

    public String getForecastFormula()
    {
        return forecastFormula;
    }

    public void setForecastFormula(String forecastFormula)
    {
        this.forecastFormula = forecastFormula;
    }

    public DictEntry getStatus()
    {
        return status;
    }

    public void setStatus(DictEntry status)
    {
        this.status = status;
    }

    public Boolean getIsLeaf()
    {
        return isLeaf;
    }

    public void setIsLeaf(Boolean isLeaf)
    {
        this.isLeaf = isLeaf;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public Boolean getDeleteStatus()
    {
        return deleteStatus;
    }

    public void setDeleteStatus(Boolean deleteStatus)
    {
        this.deleteStatus = deleteStatus;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Category getParent()
    {
        return parent;
    }

    public void setParent(Category parent)
    {
        this.parent = parent;
    }

    public Integer getLevel()
    {
        return level;
    }

    public void setLevel(Integer level)
    {
        this.level = level;
    }

    public String getIdSeq()
    {
        return idSeq;
    }

    public void setIdSeq(String idSeq)
    {
        this.idSeq = idSeq;
    }

    public Integer getSort()
    {
        return sort;
    }

    public void setSort(Integer sort)
    {
        this.sort = sort;
    }

}
