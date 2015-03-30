package com.fhd.jrj.business.chart;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.record.chart.ChartTitleFormatRecord;

/**
 * 单柱单折
 */
public class FCSingleColumnLineChart {

    public static String getXml(List<FCSingleColumnVO> datas, String clickUrl,String lineName,boolean flag,String unit,String showYAxisValues,String caption) {
        FCSingleColumnVO vo = null;
        StringBuffer columnBufStartTitle = new StringBuffer();
        StringBuffer lineBufStartTitle = new StringBuffer();
        StringBuffer categoriesBufStartTitle = new StringBuffer();
        StringBuffer columnBufEndTitle = new StringBuffer();
        StringBuffer lineBufEndTitle = new StringBuffer();
        StringBuffer categoriesBufEndTitle = new StringBuffer();
        StringBuffer columnBuf = new StringBuffer();
        StringBuffer lineBuf = new StringBuffer();
        StringBuffer categoriesBuf = new StringBuffer();
        StringBuffer chartTitelStartBuf = new StringBuffer();
        StringBuffer chartTitelEndBuf = new StringBuffer();
        chartTitelEndBuf.append("</chart>");
        chartTitelStartBuf
                .append("<chart formatNumberScale='0' placeValuesInside='1' rotateYAxisName='0' showBorder='0' showValues='1' canvasbgColor='ffffff' canvasbgAlpha='0' canvasPadding='10' canvasBaseDepth='1'  baseFontSize='10' legendPosition='right' legendPosition='right' ");
        //chartTitelStartBuf.append(" legendBgColor='ffffff' legendShadow='0' legendBorderColor='ffffff' placeValuesInside='0' numberSuffix='").append(unit).append("'").append(" showYAxisValues='").append(showYAxisValues).append("' ");
        chartTitelStartBuf.append(" legendBgColor='ffffff' legendShadow='0' legendBorderColor='ffffff' placeValuesInside='0' ").append(" showYAxisValues='").append(showYAxisValues).append("' ");
        
        if (StringUtils.isNotBlank(clickUrl)) {
            chartTitelStartBuf.append(" ClickUrl='n-").append(clickUrl).append("'");
        }
        if(StringUtils.isNotBlank(unit)){
            chartTitelStartBuf.append(" subCaption='").append("单位:").append(unit).append("' ").append(" rotateYAxisName='1' ");
        }
        chartTitelStartBuf.append(" caption='").append(caption).append("' ");
        chartTitelStartBuf.append(" > ");

        categoriesBufStartTitle.append("<categories>");
        //lineBufStartTitle
        //        .append("<dataset seriesname='实际值").append(lineName).append("变化' renderAs='Line' color='BBDA00' anchorSides='4' anchorRadius='5' anchorBgColor='BBDA00' anchorBorderColor='FFFFFF' anchorBorderThickness='2'>");
        columnBufStartTitle.append("<dataset seriesName=''>");
        if (null != datas) {
            for (int i = 0; i < datas.size(); i++) {
                vo = datas.get(i);
                categoriesBuf.append("<category name='").append(vo.getCategory()).append("' />");
                if(flag){
                    columnBuf.append("<set value='").append(vo.getColumnValue()).append("' />");
                }
                else{
                    columnBuf.append("<set value='").append(vo.getColumnValue()).append("' />");
                }
                lineBuf.append("<set value='").append(vo.getLineValue()).append("' anchorBgColor='").append(vo.getLineStatus()).append("' anchorBorderColor='").append(vo.getLineStatus()).append("' />");
            }
        }
        columnBufEndTitle.append("</dataset>");
        //lineBufEndTitle.append("</dataset>");
        categoriesBufEndTitle.append("</categories>");

        return chartTitelStartBuf.append(categoriesBufStartTitle).append(categoriesBuf).append(categoriesBufEndTitle).append(columnBufStartTitle)
                .append(columnBuf).append(columnBufEndTitle).append(lineBufStartTitle).append(lineBuf).append(lineBufEndTitle)
                .append(chartTitelEndBuf).toString();
    }

}
