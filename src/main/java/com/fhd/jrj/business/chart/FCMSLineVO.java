package com.fhd.jrj.business.chart;

public class FCMSLineVO {
    private Double ratioValue;

    private String dotColor;
    
    private String name;

    public String getName() {
        return name;
    }
    
    public String categoryName;
    

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRatioValue() {
        return ratioValue;
    }

    public void setRatioValue(Double ratioValue) {
        this.ratioValue = ratioValue;
    }

    public String getDotColor() {
        return dotColor;
    }

    public void setDotColor(String dotColor) {
        this.dotColor = dotColor;
    }
}
