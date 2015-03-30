package com.fhd.jrj.business.chart;

public class FCAlarmRangeVO {
    private Double greenStartValue;

    private Double greenEndValue;

    private Double orangeStartValue;

    private Double orangeEndValue;

    private Double redStartValue;

    private Double redEndValue;

    private Double yellowStartValue;

    private Double yellowEndValue;

    public Double getYellowStartValue() {
        return yellowStartValue;
    }

    public void setYellowStartValue(Double yellowStartValue) {
        this.yellowStartValue = yellowStartValue;
    }

    public Double getYellowEndValue() {
        return yellowEndValue;
    }

    public void setYellowEndValue(Double yellowEndValue) {
        this.yellowEndValue = yellowEndValue;
    }

    public Double getGreenStartValue() {
        return greenStartValue;
    }

    public void setGreenStartValue(Double safeStartValue) {
        this.greenStartValue = safeStartValue;
    }

    public Double getGreenEndValue() {
        return greenEndValue;
    }

    public void setGreenEndValue(Double safeEndValue) {
        this.greenEndValue = safeEndValue;
    }

    public Double getOrangeStartValue() {
        return orangeStartValue;
    }

    public void setOrangeStartValue(Double alarmStartValue) {
        this.orangeStartValue = alarmStartValue;
    }

    public Double getOrangeEndValue() {
        return orangeEndValue;
    }

    public void setOrangeEndValue(Double alarmEndValue) {
        this.orangeEndValue = alarmEndValue;
    }

    public Double getRedStartValue() {
        return redStartValue;
    }

    public void setRedStartValue(Double dangerStartValue) {
        this.redStartValue = dangerStartValue;
    }

    public Double getRedEndValue() {
        return redEndValue;
    }

    public void setRedEndValue(Double dangerEndValue) {
        this.redEndValue = dangerEndValue;
    }

}
