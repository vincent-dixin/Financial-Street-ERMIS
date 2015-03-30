package com.fhd.jrj.business.chart;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;


public class FCMutiColumnLineChart {
    public static String getXml(Map<String,List<FCSingleColumnVO>> dataMap,List<String> categorys, String unit,String caption) {
        
        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append("<chart placeValuesInside='1' canvasbgColor='ffffff' canvasbgAlpha='0' canvasBaseDepth='1' canvasPadding='10' caption='").append(caption).append("' showValues='1' PYAxisName='' SYAxisName='' legendPosition='right' yAxisMaxValue='100'> ");
        if(StringUtils.isNotBlank(unit)){
            xmlBuf.append(" subCaption='").append("单位:").append(unit).append("' ").append(" rotateYAxisName='1' ");
        }
        
        for (Map.Entry<String, List<FCSingleColumnVO>> m : dataMap.entrySet()) {      
            String name = m.getKey();
            xmlBuf.append("<dataset seriesName='").append(name).append("'>");
            List<FCSingleColumnVO> datas = m.getValue();
            for (FCSingleColumnVO fcSingleColumnVO : datas) {
                xmlBuf.append("<set value='").append(fcSingleColumnVO.getColumnValue()).append("'/>");
            }
            xmlBuf.append("</dataset>");
        }  
        xmlBuf.append("<categories>");
        for (String category : categorys) {
            xmlBuf.append("<category label='").append(category).append("' />");
        }
        xmlBuf.append("</categories>");
        xmlBuf.append("</chart>");
        return xmlBuf.toString();
    }
}
