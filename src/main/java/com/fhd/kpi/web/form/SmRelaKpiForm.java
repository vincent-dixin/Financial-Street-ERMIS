/**
 * SmRelaKpiForm.java
 * com.fhd.kpi.web.form
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-17 		陈晓哲
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.kpi.web.form;

import com.fhd.kpi.entity.SmRelaKpi;

/**
 * 目标关联kpi指标Form
 * ClassName:SmRelaKpiForm
 *
 * @author   陈晓哲
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-17		下午05:26:24
 *
 * @see 	 
 */
public class SmRelaKpiForm
{
    /*
     * 序
     */
    private Integer sort;

    /*
     * 名称
     */
    private String name;

    /*
     * 权重
     */
    private Double weight;

    /*
     * 主键
     */
    private String id;

    /*
     * 部门名称
     */
    private String dept;

    public String getDept()
    {
        return dept;
    }

    public void setDept(String dept)
    {
        this.dept = dept;
    }

    public String getName()
    {
        return name;
    }

    public Double getWeight()
    {
        return weight;
    }

    public void setWeight(Double weight)
    {
        this.weight = weight;
    }

    public Integer getSort()
    {
        return sort;
    }

    public void setSort(Integer sort)
    {
        this.sort = sort;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public SmRelaKpiForm(SmRelaKpi smRelaKpi)
    {
        //this.id = smRelaKpi.getId();
        this.id = smRelaKpi.getKpi().getId();
        this.name = smRelaKpi.getKpi().getName();
        this.sort = smRelaKpi.getKpi().getSort();
        this.weight = smRelaKpi.getWeight();

    }
}
