package com.fhd.jrj.business.chart;

/**
 * 单柱单折值对象
 *
 */
public class FCSingleColumnVO {
    /**
     * 柱值
     */
    private Double columnValue;

    /**
     * 折线值
     */
    private Double lineValue;
    
    /**
     * 折线点状态
     */
    private String lineStatus;
    
    /**
     * 频率
     */
    private String category;
    

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getLineValue() {
        return lineValue;
    }

    public void setLineValue(Double lineValue) {
        this.lineValue = lineValue;
    }

    public Double getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(Double columnValue) {
        this.columnValue = columnValue;
    }
    public String getLineStatus() {
        return lineStatus;
    }

    public void setLineStatus(String lineStatus) {
        this.lineStatus = lineStatus;
    }


}
