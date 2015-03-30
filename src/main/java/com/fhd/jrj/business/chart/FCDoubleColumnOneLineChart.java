package com.fhd.jrj.business.chart;

import java.util.List;

import org.apache.commons.lang.StringUtils;

public class FCDoubleColumnOneLineChart {

    public static String getXml(List<FCDoubleColumnOneLineVO> datas, String clickUrl, String lineName,String unit) {
        FCDoubleColumnOneLineVO vo = null;
        StringBuffer oneColumnBufStartTitle = new StringBuffer();
        StringBuffer twoColumnBufStartTitle = new StringBuffer();
        StringBuffer oneLineBufStartTitle = new StringBuffer();
        StringBuffer categoriesBufStartTitle = new StringBuffer();
        StringBuffer oneColumnBufEndTitle = new StringBuffer();
        StringBuffer twoColumnBufEndTitle = new StringBuffer();
        StringBuffer oneLineBufEndTitle = new StringBuffer();
        StringBuffer categoriesBufEndTitle = new StringBuffer();
        StringBuffer oneColumnBuf = new StringBuffer();
        StringBuffer twoColumnBuf = new StringBuffer();
        StringBuffer oneLineBuf = new StringBuffer();
        StringBuffer categoriesBuf = new StringBuffer();
        StringBuffer chartTitelStartBuf = new StringBuffer();
        StringBuffer chartTitelEndBuf = new StringBuffer();
        chartTitelEndBuf.append("</chart>");
        chartTitelStartBuf
                .append("<chart showBorder='0' showValues='1' canvasbgColor='ffffff' canvasbgAlpha='0' canvasBaseDepth='1' canvasPadding='10'  baseFontSize='10' legendPosition='right' legendPosition='right' ");
        chartTitelStartBuf.append(" legendBgColor='ffffff' legendShadow='0' legendBorderColor='ffffff' placeValuesInside='0' numberSuffix='").append(unit).append("'");
        if (StringUtils.isNotBlank(clickUrl)) {
            chartTitelStartBuf.append(" ClickUrl='n-").append(clickUrl).append("'");
        }
        chartTitelStartBuf.append(" > ");

        categoriesBufStartTitle.append("<categories>");
        oneLineBufStartTitle
                .append("<dataset seriesname='")
                .append(lineName)
                .append("变化' renderAs='Line' color='BBDA00' anchorSides='4' anchorRadius='5' anchorBgColor='BBDA00' anchorBorderColor='FFFFFF' anchorBorderThickness='2'>");

        oneColumnBufStartTitle.append("<dataset seriesName='实际值'>");

        twoColumnBufStartTitle.append("<dataset seriesName='目标值'  color='FE6F97'>");

        if (null != datas) {
            for (int i = 0; i < datas.size(); i++) {
                vo = datas.get(i);
                categoriesBuf.append("<category name='").append(vo.getCategory()).append("' />");
                oneColumnBuf.append("<set value='").append(vo.getColumnOneValue()).append("' />");
                twoColumnBuf.append("<set value='").append(vo.getColumnTwoValue()).append("' />");
                oneLineBuf.append("<set value='").append(vo.getCompareValue()).append("' anchorBgColor='").append(vo.getDotColor())
                        .append("' anchorBorderColor='").append(vo.getDotColor()).append("' />");
            }
        }
        oneColumnBufEndTitle.append("</dataset>");
        twoColumnBufEndTitle.append("</dataset>");
        oneLineBufEndTitle.append("</dataset>");
        categoriesBufEndTitle.append("</categories>");

        return chartTitelStartBuf.append(categoriesBufStartTitle).append(categoriesBuf).append(categoriesBufEndTitle).append(oneColumnBufStartTitle)
                .append(oneColumnBuf).append(oneColumnBufEndTitle).append(twoColumnBufStartTitle).append(twoColumnBuf).append(twoColumnBufEndTitle)
                .append(oneLineBufStartTitle).append(oneLineBuf).append(oneLineBufEndTitle).append(chartTitelEndBuf).toString();
    }

}
