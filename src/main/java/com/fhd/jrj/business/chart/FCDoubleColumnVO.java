package com.fhd.jrj.business.chart;

/**
 * 双柱双折值对象
 *
 */
public class FCDoubleColumnVO {
    /**
     * 柱值
     */
    private Double columnOneValue;

    /**
     * 柱值
     */
    private Double columnTwoValue;

    /**
     * 折线值
     */
    private Double lineOneValue;

    /**
     * 折线值
     */
    private Double lineTwoValue;

    /**
     * 频率
     */
    private String category;
    
    /**
     * 点颜色
     */
    private String dotColor;
    

    public String getDotColor() {
        return dotColor;
    }

    public void setDotColor(String dotColor) {
        this.dotColor = dotColor;
    }

    public Double getColumnOneValue() {
        return columnOneValue;
    }

    public void setColumnOneValue(Double columnOneValue) {
        this.columnOneValue = columnOneValue;
    }

    public Double getColumnTwoValue() {
        return columnTwoValue;
    }

    public void setColumnTwoValue(Double columnTwoValue) {
        this.columnTwoValue = columnTwoValue;
    }

    public Double getLineOneValue() {
        return lineOneValue;
    }

    public void setLineOneValue(Double lineOneValue) {
        this.lineOneValue = lineOneValue;
    }

    public Double getLineTwoValue() {
        return lineTwoValue;
    }

    public void setLineTwoValue(Double lineTwoValue) {
        this.lineTwoValue = lineTwoValue;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
