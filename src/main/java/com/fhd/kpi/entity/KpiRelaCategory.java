/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.kpi.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.comm.entity.Category;
import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * 指标分类
 *
 * @author   胡迪新
 * @since    fhd Ver 4.5
 * @Date	 2012-10-25  下午3:03:46
 *
 * @see 	 
 */
@Entity
@Table(name = "T_KPI_KPI_RELA_CATEGORY")
public class KpiRelaCategory extends IdEntity implements Serializable
{

    private static final long serialVersionUID = -4083833849282768263L;

    /**
     * 分类
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    /**
     * 指标
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KPI_ID")
    private Kpi kpi;

    /**
     * 权重
     */
    @Column(name = "EWEIGHT")
    private Double weight;

    /**
     * 是否创建者
     */
    @Column(name = "IS_CREATOR")
    private Boolean isCreator;

    public Boolean getIsCreator()
    {
        return isCreator;
    }

    public void setIsCreator(Boolean isCreator)
    {
        this.isCreator = isCreator;
    }

    public Category getCategory()
    {
        return category;
    }

    public void setCategory(Category category)
    {
        this.category = category;
    }

    public Kpi getKpi()
    {
        return kpi;
    }

    public void setKpi(Kpi kpi)
    {
        this.kpi = kpi;
    }

    public Double getWeight()
    {
        return weight;
    }

    public void setWeight(Double weight)
    {
        this.weight = weight;
    }

}
