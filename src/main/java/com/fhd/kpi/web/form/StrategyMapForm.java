/**
 * StrategyMapForm.java
 * com.fhd.kpi.web.form
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-16 		陈晓哲
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.kpi.web.form;

import com.fhd.kpi.entity.StrategyMap;

/**
 * 目标对象Form
 * ClassName:StrategyMapForm
 *
 * @author   陈晓哲
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-16		下午10:24:38
 *
 * @see 	 
 */
public class StrategyMapForm extends StrategyMap
{
 
	private static final long serialVersionUID = 2793466604803218586L;

	/*
     * 当前目标ID
     */
    private String currentSmId;

    /*
     * 我的目标
     */
    private String myflag;

    /*
     * 父目标ID
     */
    private String parentId;

    /*
     * 主维度
     */
    private String mainDim;

    /*
     * 辅助维度
     */
    private String otherDim;

    /*
     * 主题
     */
    private String mainTheme;

    /*
     * 辅助主题
     */
    private String otherTheme;

    /*
     * 状态
     */
    private String estatus;

    /*
     * 所属部门
     */
    private String ownDept;

    /*
     * 所属人员
     */
    private String ownEmp;

    /*
     * 报告部门
     */
    private String reportDept;

    /*
     * 报告人员
     */
    private String reportEmp;

    /*
     * 查看部门
     */
    private String viewDept;

    /*
     * 查看人员
     */
    private String viewEmp;

    /*
     * 机构ID
     */
    private String orgId;

    /*
     * 报告人员
     */
    private String reportPerson;

    /*
     * 我的目标树添加标志
     */
    private String myadd;
    
    /*
     * 图表类型
     */
    private String chartTypeStr;
    

    public String getChartTypeStr()
    {
        return chartTypeStr;
    }

    public void setChartTypeStr(String chartTypeStr)
    {
        this.chartTypeStr = chartTypeStr;
    }

    public String getMyadd()
    {
        return myadd;
    }

    public void setMyadd(String myadd)
    {
        this.myadd = myadd;
    }

    public String getReportPerson()
    {
        return reportPerson;
    }

    public void setReportPerson(String reportPerson)
    {
        this.reportPerson = reportPerson;
    }

    private String viewPerson;

    public String getViewPerson()
    {
        return viewPerson;
    }

    public void setViewPerson(String viewPerson)
    {
        this.viewPerson = viewPerson;
    }

    public String getOwnEmp()
    {
        return ownEmp;
    }

    public void setOwnEmp(String ownEmp)
    {
        this.ownEmp = ownEmp;
    }

    public String getReportEmp()
    {
        return reportEmp;
    }

    public void setReportEmp(String reportEmp)
    {
        this.reportEmp = reportEmp;
    }

    public String getViewEmp()
    {
        return viewEmp;
    }

    public void setViewEmp(String viewEmp)
    {
        this.viewEmp = viewEmp;
    }

    public String getMyflag()
    {
        return myflag;
    }

    public void setMyflag(String myflag)
    {
        this.myflag = myflag;
    }

    public String getOrgId()
    {
        return orgId;
    }

    public void setOrgId(String orgId)
    {
        this.orgId = orgId;
    }

    public String getMainDim()
    {
        return mainDim;
    }

    public String getCurrentSmId()
    {
        return currentSmId;
    }

    public void setCurrentSmId(String currentSmId)
    {
        this.currentSmId = currentSmId;
    }

    public void setMainDim(String mainDim)
    {
        this.mainDim = mainDim;
    }

    public String getOtherDim()
    {
        return otherDim;
    }

    public void setOtherDim(String otherDim)
    {
        this.otherDim = otherDim;
    }

    public String getMainTheme()
    {
        return mainTheme;
    }

    public void setMainTheme(String mainTheme)
    {
        this.mainTheme = mainTheme;
    }

    public String getOtherTheme()
    {
        return otherTheme;
    }

    public void setOtherTheme(String otherTheme)
    {
        this.otherTheme = otherTheme;
    }

    public String getEstatus()
    {
        return estatus;
    }

    public void setEstatus(String estatus)
    {
        this.estatus = estatus;
    }

    public String getOwnDept()
    {
        return ownDept;
    }

    public void setOwnDept(String ownDept)
    {
        this.ownDept = ownDept;
    }

    public String getReportDept()
    {
        return reportDept;
    }

    public void setReportDept(String reportDept)
    {
        this.reportDept = reportDept;
    }

    public String getViewDept()
    {
        return viewDept;
    }

    public void setViewDept(String viewDept)
    {
        this.viewDept = viewDept;
    }

    public String getParentId()
    {
        return parentId;
    }

    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }

}
