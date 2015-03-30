package com.fhd.jrj.business.chart;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class FCAlarmLineChart {

    public static String getXML(List<FCAlarmLineValueVO> values, FCAlarmRangeVO range, Double maxValue, Double minValue, String clickUrl,String unit, String caption,String rotateLabels,boolean lineFlag) {
        DecimalFormat df = new DecimalFormat("####.##");
        StringBuffer xmlbuf = new StringBuffer();
        xmlbuf.append("<chart rotateLabels='").append(rotateLabels).append("' palette='2' animation='0' valuePosition='ABOVE' ");
        xmlbuf.append(" showBorder='0' labelDisplay='warp' labelPadding='8' showYAxisValues ='0'  divLineAlpha='5' yAxisMaxValue='").append(maxValue).append("' ").append(" yAxisMinValue='").append(minValue)
                .append("' ");
        xmlbuf.append(" alternateHGridAlpha='5' canvasBorderColor='F7F2EA'  canvasPadding='15'  canvasBorderThickness='0' bgcolor='FFFFFF'  baseFontColor='666666' ");
        //xmlbuf.append(" baseFontSize='12'  lineColor='666666' numVDivlines='7' showAlternateVGridColor='1' anchorSides='3' anchorRadius='5' showValues='1' numberSuffix='").append(unit).append("'");
        xmlbuf.append(" baseFontSize='12'  lineColor='B9B4B4' numVDivlines='7' showAlternateVGridColor='1' anchorSides='3' anchorRadius='5' showValues='1' ");
        if(StringUtils.isNotBlank(unit)){
            xmlbuf.append(" subCaption='").append("").append(unit).append("' ");
        }
        if (StringUtils.isNotBlank(clickUrl)) {
            xmlbuf.append(" ClickUrl='n-").append(clickUrl).append("'");
        }
        xmlbuf.append(" caption='").append(caption).append("' ");
        xmlbuf.append(" > ");

        for (int i = 0; i < values.size(); i++) {
            FCAlarmLineValueVO valueVo = values.get(i);
            if(null!=valueVo.getValue()){
                if(valueVo.getValue()<minValue){
                    xmlbuf.append("<set value='").append(df.format(minValue)).append("'").append(" displayValue='").append(df.format(valueVo.getValue())).append("' ")
                    .append(" toolText='").append(valueVo.getTime()).append(", ").append(df.format(valueVo.getValue())).append("' label='").append(valueVo.getTime()).append("'")
                    .append(" anchorSides='5' anchorRadius='7' anchorBorderColor='FF654F' anchorBorderThickness='3' />");
                }
                else if(valueVo.getValue()>maxValue){
                    if((i+1)%2==0){
                        xmlbuf.append("<set value='").append(df.format(maxValue)).append("'").append(" displayValue='").append(df.format(valueVo.getValue())).append("' ")
                        .append(" toolText='").append(valueVo.getTime()).append(", ").append(df.format(valueVo.getValue())).append("' label='").append(valueVo.getTime()).append("'")
                        .append(" anchorSides='5' anchorRadius='7' anchorBorderColor='FF654F' anchorBorderThickness='3' valuePosition='BELOW' />");
                    }else{
                        xmlbuf.append("<set value='").append(df.format(maxValue)).append("'").append(" displayValue='").append(df.format(valueVo.getValue())).append("' ")
                        .append(" toolText='").append(valueVo.getTime()).append(", ").append(df.format(valueVo.getValue())).append("' label='").append(valueVo.getTime()).append("'")
                        .append(" anchorSides='5' anchorRadius='7' anchorBorderColor='FF654F' anchorBorderThickness='3' valuePosition='ABOVE' />");
                    }
                }
                else{
                    xmlbuf.append(" <set label='").append(valueVo.getTime()).append("'").append(" value='").append(df.format(valueVo.getValue())).append("'")
                    .append(" />");
                }
                
            }else{
                xmlbuf.append(" <set label='").append(valueVo.getTime()).append("'").append(" value='").append(valueVo.getValue()).append("'")
                .append(" />");
            }
            
        }
        xmlbuf.append("<trendlines>");
        //xmlbuf.append("<line startvalue='").append(maxValue).append("' ").append(" displayValue='").append(maxValue).append("' /> ");
        if(null!=range.getYellowStartValue()){
            xmlbuf.append("<line startvalue='").append(df.format(range.getYellowStartValue())).append("' ").append(" displayValue='").append(df.format(range.getYellowStartValue())).append("' /> ");
            xmlbuf.append("<line startvalue='").append(range.getYellowStartValue()).append("' ").append(" endValue='").append(range.getYellowEndValue())
            .append("' ").append(" displayValue='警告' color='ffe600' isTrendZone='1' showOnTop='0' alpha='50' valueOnRight='1'/> ");
        }
       
        
        if(null!=range.getOrangeStartValue()){
            xmlbuf.append("<line startvalue='").append(df.format(range.getOrangeStartValue())).append("' ").append(" displayValue='").append(df.format(range.getOrangeStartValue())).append("' /> ");
            xmlbuf.append("<line startvalue='").append(range.getOrangeStartValue()).append("' ").append(" endValue='").append(range.getOrangeEndValue())
            .append("' ").append(" displayValue='风险' color='F59630' isTrendZone='1' showOnTop='0' alpha='50' valueOnRight='1'/>");
        }
       
        if(null!=range.getRedStartValue()){
            xmlbuf.append("<line startvalue='").append(df.format(range.getRedStartValue())).append("' ").append(" displayValue='").append(df.format(range.getRedStartValue())).append("' /> ");
            xmlbuf.append("<line startvalue='").append(range.getRedStartValue()).append("' ").append(" endValue='").append(range.getRedEndValue())
            .append("' ").append("  displayValue='危险' color='FF654F' isTrendZone='1' showOnTop='0' alpha='50' valueOnRight='1'/>");
        }
        
        if (null != range.getGreenStartValue()) {
            if(lineFlag){
                xmlbuf.append("<line startvalue='").append(range.getGreenStartValue()).append("' ").append(" displayValue='").append(range.getGreenStartValue()).append("' /> ");
            }
            xmlbuf.append("<line startvalue='").append(range.getGreenStartValue()).append("' ").append(" endValue='")
                    .append(range.getGreenEndValue()).append("' ")
                    .append("  displayValue='安全' color='8BBA00' isTrendZone='1' showOnTop='0' alpha='50' valueOnRight='1'/>");
        }

        xmlbuf.append("</trendlines>");
        xmlbuf.append("<styles>");
        xmlbuf.append("<definition>");
        xmlbuf.append("<style name='Anim1' type='animation' param='_alpha' start='0' duration='1' />");
        xmlbuf.append("</definition>");
        xmlbuf.append("<application>");
        xmlbuf.append("<apply toObject='TRENDLINES' styles='Anim1' />");
        xmlbuf.append("</application>");
        xmlbuf.append("</styles>");
        xmlbuf.append("</chart>");
        return xmlbuf.toString();
    }
}
