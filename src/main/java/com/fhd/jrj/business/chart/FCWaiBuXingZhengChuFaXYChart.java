package com.fhd.jrj.business.chart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class FCWaiBuXingZhengChuFaXYChart {
    
    public static String getXml(Map<Integer,List<FCXYVo>> dataMap,int step,String caption,String unit){
        StringBuffer xmlBuf = new StringBuffer();
        StringBuffer categoryBuf = new StringBuffer();
        StringBuffer dataSetStartBuf = new StringBuffer();
        StringBuffer dataSetEndBuf = new StringBuffer();
        StringBuffer setBuf = new StringBuffer();
        StringBuffer categoryTitleBufStart = new StringBuffer();
        StringBuffer categoryTitleBufEnd = new StringBuffer();
        int dsize = dataMap.size();
        xmlBuf.append("<chart caption='").append(caption).append("' subCaption='单位:").append(unit).append("' alternateHGridAlpha='5' canvasBorderColor='F7F2EA'  canvasBorderThickness='0' bgcolor='FFFFFF'  baseFontColor='666666' baseFontSize='12'  lineColor='666666' numDivlines='-1' showAlternateVGridColor='1'  showYAxisValues ='0' showBorder='0' canvasBorderColor='F7F2EA' palette='2' xAxisMaxValue='").append(dsize*step).append("' xAxisMinValue='0'  yAxisMinValue='-20' yAxisMaxValue='100'>");
        categoryTitleBufStart.append("<categories verticalLineColor='666666' verticalLineThickness='0'>");
        categoryTitleBufEnd.append("</categories>");
        dataSetStartBuf.append("<dataset anchorSides='4' anchorRadius='4' color='009900'  anchorBgColor='D5FFD5' anchorBorderColor='009900'>");
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
                    setBuf.append("<set  y='").append(fcxyVo.getValue()).append("' x='").append(i*step+dotStep*y).append("' toolText='").append(fcxyVo.getDesc()).append("' />");
                }
                y++;
            }
            categoryBuf.append("<category label='").append(time).append("月' ").append(" showVerticalLine='0' ").append(" x='").append(i*step).append("' ").append(" />");
            i++;
            
        } 
        xmlBuf.append(categoryTitleBufStart).append(categoryBuf).append(categoryTitleBufEnd).append(dataSetStartBuf).append(setBuf).append(dataSetEndBuf);
        xmlBuf.append("<trendlines>");
        xmlBuf.append("<line startValue='10' displayValue='10' />");
        xmlBuf.append("<line startValue='10'  endValue='100' color='FF654F' isTrendZone='1' showOnTop='0'  displayValue='危险' lineThickness='2'  valueOnRight='1' dashed='1' dashGap='5'/>");
        xmlBuf.append("<line startValue='10' endValue='-20' isTrendZone='1' showOnTop='0'  displayValue='安全' lineThickness='2' color='8BBA00' valueOnRight='1' dashed='1' dashGap='5'/>");
        xmlBuf.append("</trendlines>");
        xmlBuf.append("</chart>");
        System.out.println(xmlBuf);
        return xmlBuf.toString();
    }

    public static void main(String[] args) {
        Map<String,List<FCXYVo>> dataMap = new TreeMap<String, List<FCXYVo>>();
        List<FCXYVo> d1 = new ArrayList<FCXYVo>();
        FCXYVo v1 = new FCXYVo();
        FCXYVo v11 = new FCXYVo();
        FCXYVo v111 = new FCXYVo();
        FCXYVo v112 = new FCXYVo();
        FCXYVo v113 = new FCXYVo();
        FCXYVo v114 = new FCXYVo();
        FCXYVo v115 = new FCXYVo();
        FCXYVo v116 = new FCXYVo();
        v1.setValue(11.0);
        v11.setValue(13.0);
        v111.setValue(15.0);
        v112.setValue(16.0);
        v113.setValue(17.0);
        v114.setValue(18.0);
        v115.setValue(19.0);
        v116.setValue(20.0);
        d1.add(v1);
        d1.add(v11);
        d1.add(v111);
        d1.add(v112);
        d1.add(v113);
        d1.add(v114);
        d1.add(v115);
        d1.add(v116);
        dataMap.put("2012.6", d1);
        List<FCXYVo> d2 = new ArrayList<FCXYVo>();
        FCXYVo v2 = new FCXYVo();
        FCXYVo v22 = new FCXYVo();
        v2.setValue(15.0);
        v22.setValue(17.0);
        d2.add(v2);
        d2.add(v22);
        dataMap.put("2012.7", d2);
        //String xml = getXml(dataMap,50,"截止目前,未发生需要预警的外部行政处罚","万元");
       // System.out.println(xml);
    }

}
