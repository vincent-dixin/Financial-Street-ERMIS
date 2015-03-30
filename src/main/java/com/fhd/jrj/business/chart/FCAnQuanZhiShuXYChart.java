package com.fhd.jrj.business.chart;

import java.util.List;
import java.util.Map;

public class FCAnQuanZhiShuXYChart {
    public static String getXml(Map<Integer,List<FCXYVo>> dataMap,int step,String caption,String unit){
        StringBuffer xmlBuf = new StringBuffer();
        StringBuffer categoryBuf = new StringBuffer();
        StringBuffer dataSetStartBuf = new StringBuffer();
        StringBuffer dataSetEndBuf = new StringBuffer();
        StringBuffer setBuf = new StringBuffer();
        StringBuffer categoryTitleBufStart = new StringBuffer();
        StringBuffer categoryTitleBufEnd = new StringBuffer();
        int dsize = dataMap.size();
        xmlBuf.append("<chart caption='").append(caption).append("' subCaption='单位:").append(unit).append("' alternateHGridAlpha='5' canvasBorderColor='F7F2EA'  canvasBorderThickness='0' bgcolor='FFFFFF'  baseFontColor='666666' baseFontSize='12'  lineColor='666666' numDivlines='-1' showAlternateVGridColor='1'  showYAxisValues ='0' showBorder='0' canvasBorderColor='F7F2EA' palette='2' xAxisMaxValue='").append(dsize*step).append("' xAxisMinValue='0' yAxisMinValue='1' yAxisMaxValue='6'>");
        categoryTitleBufStart.append("<categories verticalLineColor='666666' verticalLineThickness='0'>");
        categoryTitleBufEnd.append("</categories>");
        dataSetStartBuf.append("<dataset  color='009900' anchorSides='4' anchorRadius='4' anchorBgColor='D5FFD5' anchorBorderColor='009900'>");
        dataSetEndBuf.append("</dataset>");
        int i=0;
        
        for (Map.Entry<Integer, List<FCXYVo>> m : dataMap.entrySet()) {     
            Integer time = m.getKey();
            List<FCXYVo> datas = m.getValue();
            int dataSize = datas.size();
            int dotStep = step/dataSize;
            int y=0;
            for (FCXYVo fcxyVo : datas) {
                if(fcxyVo.getValue()!=0){
                    setBuf.append("<set y='").append(fcxyVo.getValue()).append("' x='").append(i*step+dotStep*y).append("' toolText='").append(fcxyVo.getDesc()).append("' />");
                }
                y++;
               
            }
            categoryBuf.append("<category label='").append(time).append("月' ").append(" showVerticalLine='0' ").append(" x='").append(i*step).append("' ").append(" />");
            i++;
            
        } 
        xmlBuf.append(categoryTitleBufStart).append(categoryBuf).append(categoryTitleBufEnd).append(dataSetStartBuf).append(setBuf).append(dataSetEndBuf);
        xmlBuf.append("<trendlines>");
        xmlBuf.append("<line startValue='5' displayValue='5' />");
        xmlBuf.append("<line startValue='4' displayValue='4' />");
        xmlBuf.append("<line startValue='3' displayValue='3' />");
        xmlBuf.append("<line startValue='5' endValue='6' color='FF654F' isTrendZone='1' showOnTop='0' displayValue='危险' lineThickness='2' valueOnRight='1' dashed='1' dashGap='5' />");
        xmlBuf.append("<line startValue='4' endValue='5' color='F59630' isTrendZone='1' showOnTop='0' displayValue='风险' lineThickness='2' valueOnRight='1' dashed='1' dashGap='5' />");
        xmlBuf.append("<line startValue='3' endValue='4' color='ffe600' isTrendZone='1' showOnTop='0' displayValue='警告' lineThickness='2' valueOnRight='1' dashed='1' dashGap='5' />");
        xmlBuf.append("<line startValue='1' endValue='3' isTrendZone='1' showOnTop='0' displayValue='安全' lineThickness='2' color='8BBA00' valueOnRight='1' dashed='1' dashGap='5' />");
        xmlBuf.append("</trendlines>");
        xmlBuf.append("</chart>");
        return xmlBuf.toString();
    }
    public static void main(String[] args) {
        
    }
}
