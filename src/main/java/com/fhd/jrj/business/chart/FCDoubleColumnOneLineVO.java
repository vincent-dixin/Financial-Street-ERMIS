package com.fhd.jrj.business.chart;

public class FCDoubleColumnOneLineVO {
    /**
     * 实际值柱值
     */
    private Double columnOneValue;

    /**
     * 目标柱值
     */
    private Double columnTwoValue;

    /**
     * 比较折线值[(实际值-目标值)/目标值]
     */
    private Double compareValue;

    /**
     * 频率
     */
    private String category;

    /**
     * 点颜色
     */
    private String dotColor;

    public Double getCompareValue() {
        return compareValue;
    }

    public void setCompareValue(Double compareValue) {
        this.compareValue = compareValue;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDotColor() {
        return dotColor;
    }

    public void setDotColor(String dotColor) {
        this.dotColor = dotColor;
    }

}
