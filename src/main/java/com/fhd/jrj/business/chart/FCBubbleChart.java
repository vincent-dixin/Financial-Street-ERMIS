package com.fhd.jrj.business.chart;

import java.util.List;

import org.apache.commons.lang.StringUtils;

public class FCBubbleChart {

    public static String getXml(List<FCBubbleVO> datas,String clickUrl){
        StringBuffer xmlBuf = new StringBuffer();
        StringBuffer categoriesStartBuf = new StringBuffer();
        StringBuffer dataSetStartBuf = new StringBuffer();
        StringBuffer categoriesEndBuf = new StringBuffer();
        StringBuffer dataSetEndBuf = new StringBuffer();
        StringBuffer categorieBuf = new StringBuffer();
        StringBuffer setBuf = new StringBuffer();
        
        xmlBuf.append("<chart palette='3' showBorder='0' is3D='1' yAxisLabelDisplay='auto' xAxisLabelDisplay='auto' showPlotBorder='0' ");
        xmlBuf.append(" xAxisName='年度' yAxisName='金额' chartRightMargin='30' yAxisMaxValue='500' ");
        if(StringUtils.isNotBlank(clickUrl)){
            xmlBuf.append(" ClickUrl='n-/fdc/jrj/safetyindex.do").append("'");
        }
        xmlBuf.append(" > ");
        categoriesStartBuf.append("<categories>");
        categoriesEndBuf.append("</categories>");
        dataSetStartBuf.append("<dataSet showValues='1'>");
        dataSetEndBuf.append("</dataSet>");
        categorieBuf.append("<category label='0%' x='0' />");
        for (FCBubbleVO fcBubbleVO : datas) {
            categorieBuf.append("<category label='").append(fcBubbleVO.getYear()).append("年' ").append(" x='").append(fcBubbleVO.getYear()).append("' ").append(" showVerticalLine='1'/>");
            setBuf.append(" <set x='").append(fcBubbleVO.getYear()).append("' ").append(" y='").append(fcBubbleVO.getMoney()).append("' ").append(" z='").append(fcBubbleVO.getNumber()).append("' />");
        }
        xmlBuf.append(categoriesStartBuf.append(categorieBuf).append(categoriesEndBuf).append(dataSetStartBuf).append(setBuf).append(dataSetEndBuf));
        xmlBuf.append("</chart>");
        return xmlBuf.toString();
    }
}
