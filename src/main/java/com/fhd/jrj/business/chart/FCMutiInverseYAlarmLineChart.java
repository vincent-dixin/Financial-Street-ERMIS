package com.fhd.jrj.business.chart;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class FCMutiInverseYAlarmLineChart {

    
    public static String getXML(Map<String, List<FCSingleColumnVO>> dataMap,List<String> ylist, FCAlarmRangeVO range, Double maxValue, Double minValue, String clickUrl,String unit, String caption,String rotateLabels) {
    	String[] sides = {"3","3","3","3"};
    	String[] colors = {"4E5E7C","088EFC","A70BF0","EB0CEB"};
        DecimalFormat df = new DecimalFormat("####.##");
        DecimalFormat alarmDf = new DecimalFormat("####");
        StringBuffer xmlbuf = new StringBuffer();
        xmlbuf.append("<chart rotateLabels='").append(rotateLabels).append("' palette='2' animation='1' valuePosition='ABOVE' ");
        xmlbuf.append(" showBorder='0' labelDisplay='warp' labelPadding='8' showYAxisValues ='0'  divLineAlpha='5' yAxisMaxValue='").append(maxValue).append("' ").append(" yAxisMinValue='").append(minValue)
                .append("' ");
        xmlbuf.append(" alternateHGridAlpha='5' canvasBorderColor='F7F2EA'  canvasPadding='15'  canvasBorderThickness='0' bgcolor='FFFFFF'  baseFontColor='666666' ");
        xmlbuf.append(" baseFontSize='12'  lineColor='B9B4B4' numVDivlines='7' showAlternateVGridColor='1' anchorSides='3' anchorRadius='5' showValues='1' ");
        if(StringUtils.isNotBlank(unit)){
            xmlbuf.append(" subCaption='").append("").append(unit).append("' ");
        }
        if (StringUtils.isNotBlank(clickUrl)) {
            xmlbuf.append(" ClickUrl='n-").append(clickUrl).append("'");
        }
        xmlbuf.append(" caption='").append(caption).append("' ");
        xmlbuf.append(" > ");
        xmlbuf.append("<categories>");
        for(int j=0;j<ylist.size();j++){
        	xmlbuf.append("<category label='").append(ylist.get(j)).append("' />");
        }
        xmlbuf.append("</categories>");
        int k = 0;
        int h = 0;
        for (Map.Entry<String, List<FCSingleColumnVO>> m : dataMap.entrySet()) { 
        	 String name = m.getKey();
        	 xmlbuf.append("<dataset seriesName='").append(name).append("' color='").append(colors[k]).append("' ").append(" anchorBorderColor='").append(colors[k]).append("' ").append(" anchorBgColor='").append(colors[k]).append("' ").append(" >");
        	 List<FCSingleColumnVO> datas = m.getValue();
             for (FCSingleColumnVO valueVo : datas) {
            	 if(null!=valueVo.getColumnValue()){
                     if(valueVo.getColumnValue()<minValue){
                         xmlbuf.append("<set value='").append(df.format(minValue)).append("'").append(" displayValue='").append(df.format(valueVo.getColumnValue())).append("' ")
                         .append(" toolText='").append(valueVo.getCategory()).append(", ").append(df.format(valueVo.getColumnValue())).append("' ")
                         .append(" anchorSides='").append(5).append("' ").append(" anchorRadius='7' anchorBorderColor='FF654F' anchorBorderThickness='3'  />");
                     }
                     else if(valueVo.getColumnValue()>maxValue){
                         if((h+1)%2==0){
                             xmlbuf.append("<set value='").append(df.format(maxValue)).append("'").append(" displayValue='").append(df.format(valueVo.getColumnValue())).append("' ")
                             .append(" toolText='").append(valueVo.getCategory()).append(", ").append(df.format(valueVo.getColumnValue())).append("'")
                             .append(" anchorSides='").append(5).append("' ").append("  anchorRadius='7' anchorBorderColor='FF654F' anchorBorderThickness='3'  valuePosition='BELOW' />");
                         }else{
                             xmlbuf.append("<set value='").append(df.format(maxValue)).append("'").append(" displayValue='").append(df.format(valueVo.getColumnValue())).append("' ")
                             .append(" toolText='").append(valueVo.getCategory()).append(", ").append(df.format(valueVo.getColumnValue())).append("' ")
                             .append(" anchorSides='").append(5).append("' ").append(" anchorRadius='7' anchorBorderColor='FF654F' anchorBorderThickness='3'  valuePosition='ABOVE' />");
                         }
                     }
                     else{
                         xmlbuf.append(" <set  ").append(" value='").append(df.format(valueVo.getColumnValue())).append("'").append(" anchorSides='").append(sides[k]).append("' ")
                         .append(" />");
                     }
                     
                 }else{
                     xmlbuf.append(" <set  ").append(" value='").append(valueVo.getColumnValue()).append("'")
                     .append(" />");
                 }
            	 h++;
             }
             xmlbuf.append("</dataset>");
             k++;
        	
        }
        
        xmlbuf.append("<trendlines>");
        if(null!=range.getYellowStartValue()){
            xmlbuf.append("<line startvalue='").append(range.getYellowStartValue()).append("' ").append(" displayValue='").append(alarmDf.format(range.getYellowStartValue())).append("' /> ");
            xmlbuf.append("<line startvalue='").append(range.getYellowStartValue()).append("' ").append(" endValue='").append(range.getYellowEndValue())
            .append("' ").append(" displayValue='警告' color='ffe600' isTrendZone='1' showOnTop='0' alpha='50' valueOnRight='1'/> ");
        }
       
        
        if(null!=range.getOrangeStartValue()){
            xmlbuf.append("<line startvalue='").append(range.getOrangeStartValue()).append("' ").append(" endValue='").append(range.getOrangeEndValue())
            .append("' ").append(" displayValue='风险' color='F59630' isTrendZone='1' showOnTop='0' alpha='50' valueOnRight='1'/>");
        }
       
        if(null!=range.getRedStartValue()){
            xmlbuf.append("<line startvalue='").append(range.getRedEndValue()).append("' ").append(" endValue='").append(minValue)
            .append("' ").append("  displayValue='危险' color='FF654F' isTrendZone='1' showOnTop='0' alpha='50' valueOnRight='1'/>");
        }
        
        if (null != range.getGreenStartValue()) {
            xmlbuf.append("<line startvalue='").append(range.getGreenStartValue()).append("' ").append(" displayValue='").append(alarmDf.format(range.getGreenStartValue())).append("' /> ");
            xmlbuf.append("<line startvalue='").append(range.getGreenStartValue()).append("' ").append(" endValue='")
                    .append(maxValue).append("' ")
                    .append("  displayValue='安全' color='8BBA00' isTrendZone='1' showOnTop='0' alpha='50' valueOnRight='1'/>");
        }

        xmlbuf.append("</trendlines>");
        xmlbuf.append("</chart>");
        return xmlbuf.toString();
    }
        
   
}
