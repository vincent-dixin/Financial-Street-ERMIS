package com.fhd.jrj.business.chart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class FCJiTuanNeiBuChuFaXYChart {
    
    public static String getXml(Map<String,List<FCXYVo>> dataMap,int step,String caption,String unit){
        StringBuffer xmlBuf = new StringBuffer();
        StringBuffer categoryBuf = new StringBuffer();
        StringBuffer dataSetStartBuf = new StringBuffer();
        StringBuffer dataSetEndBuf = new StringBuffer();
        StringBuffer dataSetSeries2StartBuf = new StringBuffer();
        StringBuffer dataSetSeries2EndBuf = new StringBuffer();
        
        StringBuffer setBuf = new StringBuffer();
        StringBuffer setSeries2Buf = new StringBuffer();
        StringBuffer categoryTitleBufStart = new StringBuffer();
        StringBuffer categoryTitleBufEnd = new StringBuffer();
        int dsize = dataMap.size();
        xmlBuf.append("<chart  showValues='1' startValues='2012'numberSuffix='%' alternateHGridAlpha='5' canvasBorderColor='F7F2EA' valuePadding='3'canvasPadding='20' canvasBorderThickness='0' bgcolor='FFFFFF'  baseFontColor='666666' baseFontSize='12'  lineColor='666666' numDivlines='-1' showAlternateVGridColor='1'  showYAxisValues ='0' showBorder='0' canvasBorderColor='F7F2EA' palette='2' xAxisMaxValue='").append(dsize*step).append("' xAxisMinValue='0'  yAxisMinValue='-100' yAxisMaxValue='100'>");
        categoryTitleBufStart.append("<categories verticalLineColor='666666' verticalLineThickness='1'>");
        categoryTitleBufEnd.append("</categories>");
        dataSetStartBuf.append("<dataset  seriesName='处罚数量同比' valuePadding='3' color='009900' anchorSides='4' anchorRadius='4' anchorBgColor='D5FFD5' anchorBorderColor='009900'>");
        dataSetEndBuf.append("</dataset>");
        dataSetSeries2StartBuf.append("<dataset seriesName='处罚金额同比'  valuePadding='3'  color='0000FF' anchorSides='4' anchorRadius='4' anchorBgColor='C6C6FF' anchorBorderColor='0000FF'>");
        dataSetSeries2EndBuf.append("</dataset>");
        int i=0;
        
        for (Map.Entry<String, List<FCXYVo>> m : dataMap.entrySet()) {     
            String time = m.getKey();
            List<FCXYVo> datas = m.getValue();
            int dataSize = datas.size();
            int dotStep = step/dataSize;
            int y=0;
            for (FCXYVo fcxyVo : datas) {
                if(null!=fcxyVo.getValue()){
                    setBuf.append("<set y='").append(fcxyVo.getValue()*100).append("' x='").append(i*step+dotStep*y).append("' />");
                }else{
                    setBuf.append("<set y='").append(fcxyVo.getValue()).append("' x='").append(i*step+dotStep*y).append("' />");
                }
                if(null!=fcxyVo.getTwoValue()){
                    setSeries2Buf.append("<set y='").append(fcxyVo.getTwoValue()*100).append("' x='").append(i*step+dotStep*y).append("' />");
                }else{
                    setSeries2Buf.append("<set y='").append(fcxyVo.getTwoValue()).append("' x='").append(i*step+dotStep*y).append("' />");
                }
                y++;
               
            }
            categoryBuf.append("<category label='").append(time).append("' ").append(" showVerticalLine='0' ").append(" x='").append(i*step).append("' ").append(" />");
            i++;
            
        } 
        xmlBuf.append(categoryTitleBufStart).append(categoryBuf).append(categoryTitleBufEnd).append(dataSetStartBuf).append(setBuf).append(dataSetEndBuf).append(dataSetSeries2StartBuf).append(setSeries2Buf).append(dataSetSeries2EndBuf);
        xmlBuf.append("<trendlines>");
        xmlBuf.append("<line startValue='0' displayValue='0' />");
        xmlBuf.append("<line startValue='0'  endValue='100' color='FF654F' isTrendZone='1' showOnTop='0'  displayValue='危险' lineThickness='2'  valueOnRight='1' dashed='1' dashGap='5'/>");
        xmlBuf.append("<line startValue='0' endValue='-100' isTrendZone='1' showOnTop='0'  displayValue='安全' lineThickness='2' color='8BBA00' valueOnRight='1' dashed='1' dashGap='5'/>");
        xmlBuf.append("</trendlines>");
        xmlBuf.append("</chart>");
        return xmlBuf.toString();
    }

    public static void main(String[] args) {
        Map<String,List<FCXYVo>> dataMap = new TreeMap<String, List<FCXYVo>>();
        List<FCXYVo> d1 = new ArrayList<FCXYVo>();
        FCXYVo v1 = new FCXYVo();
        FCXYVo v11 = new FCXYVo();
        v1.setValue(-89.47);
        v1.setTwoValue(-83.3);
        v11.setValue(-20.47);
        v11.setTwoValue(-33.3);
        d1.add(v1);
        d1.add(v11);
        List<FCXYVo> d2 = new ArrayList<FCXYVo>();
        FCXYVo v2 = new FCXYVo();
        v2.setValue(-89.47);
        v2.setTwoValue(-83.3);
        d2.add(v2);
        dataMap.put("2012", d1);
        dataMap.put("2013", d2);
       
        String xml = getXml(dataMap,35,"","");
        System.out.println(xml);
    }  

}
