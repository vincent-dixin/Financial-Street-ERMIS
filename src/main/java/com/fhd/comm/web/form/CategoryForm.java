/**
 * CategoryForm.java
 * com.fhd.comm.web.form
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-14 		陈晓哲
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.comm.web.form;

import com.fhd.comm.entity.Category;

/**
 * 指标维度Form
 *
 * @author   陈晓哲
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-14		下午06:17:35
 *
 * @see 	 
 */
public class CategoryForm extends Category
{
    private static final long serialVersionUID = 5504040012848742809L;

    /*
     * 父维度Id
     */
    private String parentStr;

    /*
     * 状态
     */
    private String statusStr;

    /*
     * 所属部门
     */
    private String ownDept;

    /*
     * 目标部门
     */
    private String targetDept;

    /*
     * 图表类型
     */
    private String chartTypeStr;
    
    /*
     * 数据类型
     */
    private String dataTypeStr;
    /*
     * 是否生成度量指标
     */
    private String createKpiStr;
    /*
     * 评估值公式
     */
    private String assessmentFormula;
    /*
     * 预警值公式
     */
    private String forecastFormula;
    
    /*
     * 是否计算
     */
    private String calcStr;
    
    

    public String getCalcStr() {
        return calcStr;
    }

    public void setCalcStr(String calcStr) {
        this.calcStr = calcStr;
    }

    public String getDataTypeStr()
    {
        return dataTypeStr;
    }

    public void setDataTypeStr(String dataTypeStr)
    {
        this.dataTypeStr = dataTypeStr;
    }

    public String getCreateKpiStr()
    {
        return createKpiStr;
    }

    public void setCreateKpiStr(String createKpiStr)
    {
        this.createKpiStr = createKpiStr;
    }

    public String getAssessmentFormula()
    {
        return assessmentFormula;
    }

    public void setAssessmentFormula(String assessmentFormula)
    {
        this.assessmentFormula = assessmentFormula;
    }

    public String getForecastFormula()
    {
        return forecastFormula;
    }

    public void setForecastFormula(String forecastFormula)
    {
        this.forecastFormula = forecastFormula;
    }

    public String getChartTypeStr()
    {
        return chartTypeStr;
    }

    public void setChartTypeStr(String chartTypeStr)
    {
        this.chartTypeStr = chartTypeStr;
    }

    public String getParentStr()
    {
        return parentStr;
    }

    public void setParentStr(String parentStr)
    {
        this.parentStr = parentStr;
    }

    public String getStatusStr()
    {
        return statusStr;
    }

    public void setStatusStr(String statusStr)
    {
        this.statusStr = statusStr;
    }

    public String getOwnDept()
    {
        return ownDept;
    }

    public void setOwnDept(String ownDept)
    {
        this.ownDept = ownDept;
    }

    public String getTargetDept()
    {
        return targetDept;
    }

    public void setTargetDept(String targetDept)
    {
        this.targetDept = targetDept;
    }

}
