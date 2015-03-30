package com.fhd.jrj.business.chart;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class FCMutiLineChart {

    public static String getXml(Map<String, List<FCXYVo>> dataMap, Integer minValue, Integer maxValue,String unit) {
        DecimalFormat df = new DecimalFormat("####.##");
        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append("<chart canvasPadding='15' showYAxisValues ='0' animation='0' valuePosition='ABOVE'  showBorder='0'  divLineAlpha='5' yAxisMinValue='")
                .append(minValue)
                .append("' yAxisMaxValue='")
                .append(maxValue)
                .append("' alternateHGridAlpha='5' canvasBorderColor='F7F2EA'  canvasBorderThickness='0' bgcolor='FFFFFF'  baseFontColor='666666' baseFontSize='12' ");
        if(StringUtils.isNotBlank(unit)){
            xmlBuf.append(" subCaption='").append("").append(unit).append("' ");
        }
        xmlBuf.append(" lineColor='666666' numVDivlines='7' showAlternateVGridColor='1'  anchorRadius='6' showValues='1' > ");
        StringBuffer cate = new StringBuffer();
        StringBuffer cateStart = new StringBuffer();
        StringBuffer cateEnd = new StringBuffer();
        StringBuffer serOneStart = new StringBuffer("<dataset anchorBorderColor='807D7D' anchorSides='4' anchorBgColor='807D7D'  color='807D7D' seriesName='处罚数量同比'>");
        StringBuffer serOne = new StringBuffer();
        StringBuffer serOneEnd = new StringBuffer("</dataset>");
        StringBuffer serTwoStart = new StringBuffer("<dataset anchorBgColor='529CCB' anchorSides='3' anchorBorderColor='529CCB'  color='529CCB' seriesName='处罚金额同比'>");
        StringBuffer serTwo = new StringBuffer();
        StringBuffer serTwoEnd = new StringBuffer("</dataset>");
        
        cateStart.append("<categories>");
        cateEnd.append("</categories>");
        for (Map.Entry<String, List<FCXYVo>> m : dataMap.entrySet()) { 
            String time = m.getKey();
            List<FCXYVo> datas = m.getValue();
            for (FCXYVo fcxyVo : datas) {
                cate.append("<category label='").append(time).append("'/>");
                if(null!=fcxyVo.getValue()){
                    Double v = fcxyVo.getValue()*100;
                    if(v<minValue){
                        serOne.append("<set value='").append(df.format(minValue)).append("'").append(" displayValue='").append(df.format(v)).append("' ")
                        .append(" toolText='").append(time).append(", ").append(df.format(v)).append("' ")
                        .append(" anchorSides='5' anchorRadius='7'  anchorBorderThickness='3' valuePosition='ABOVE' />");
                    }
                    else if(v>maxValue){
                        serOne.append("<set value='").append(df.format(maxValue)).append("'").append(" displayValue='").append(df.format(v)).append("' ")
                        .append(" toolText='").append(time).append(", ").append(df.format(v)).append("' ")
                        .append(" anchorSides='5' anchorRadius='7'  anchorBorderThickness='3' valuePosition='ABOVE' />");
                    }
                    else{
                        serOne.append("<set anchorRadius='6'   value='").append(df.format(v)).append("' valuePosition='ABOVE'/>");
                    }
                }else{
                    serOne.append("<set value='").append(fcxyVo.getValue()).append("' valuePosition='ABOVE' />");
                }
                if(null!=fcxyVo.getTwoValue()){
                    Double value = fcxyVo.getTwoValue()*100;
                    if(value<minValue){
                        serTwo.append("<set value='").append(df.format(minValue)).append("'").append(" displayValue='").append(df.format(value)).append("' ")
                        .append(" toolText='").append(time).append(", ").append(df.format(value)).append("' ")
                        .append(" anchorSides='5' anchorRadius='7'  anchorBorderThickness='3' valuePosition='BELOW' />");
                    }
                    else if(value>maxValue){
                        serTwo.append("<set value='").append(df.format(maxValue)).append("'").append(" displayValue='").append(df.format(value)).append("' ")
                        .append(" toolText='").append(time).append(", ").append(df.format(value)).append("' ")
                        .append(" anchorSides='5' anchorRadius='7'   anchorBorderThickness='3' valuePosition='BELOW' />");
                    }else{
                        serTwo.append("<set anchorRadius='6'  value='").append(df.format(value)).append("'  valuePosition='BELOW'/>");
                    }
                }else{
                    serTwo.append("<set value='").append(fcxyVo.getTwoValue()).append("' valuePosition='BELOW'/>");
                }
                
            }
        }
        
        xmlBuf.append(cateStart).append(cate).append(cateEnd).append(serOneStart).append(serOne).append(serOneEnd).append(serTwoStart).append(serTwo).append(serTwoEnd);
        xmlBuf.append("<trendlines>").append("<line startValue='0' displayValue='0' />").append("<line startvalue='0'  endValue='").append(maxValue).append("'   displayValue='危险' color='FF654F' isTrendZone='1' showOnTop='0' alpha='50' valueOnRight='1'/>");
        xmlBuf.append("<line startvalue='0'  endValue='").append(minValue).append("'   displayValue='安全' color='8BBA00' isTrendZone='1' showOnTop='0' alpha='50' valueOnRight='1'/>");
        xmlBuf.append("</trendlines>");
        xmlBuf.append("</chart>");
        return xmlBuf.toString();
    }

}
