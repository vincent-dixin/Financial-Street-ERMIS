package com.fhd.jrj.business.chart;

import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 单柱单折
 */
public class FCSingleLineChart {

    public static String getXml(List<FCSingleColumnVO> datas, String clickUrl,String lineName,String unit,String showYAxisValues,String caption) {
        FCSingleColumnVO vo = null;
        StringBuffer lineBufStartTitle = new StringBuffer();
        StringBuffer categoriesBufStartTitle = new StringBuffer();
        StringBuffer lineBufEndTitle = new StringBuffer();
        StringBuffer categoriesBufEndTitle = new StringBuffer();
        StringBuffer lineBuf = new StringBuffer();
        StringBuffer categoriesBuf = new StringBuffer();
        StringBuffer chartTitelStartBuf = new StringBuffer();
        StringBuffer chartTitelEndBuf = new StringBuffer();
        chartTitelEndBuf.append("</chart>");
        chartTitelStartBuf
                .append("<chart placeValuesInside='1' formatNumberScale='0' placeValuesInside='1' labelDispaly='warp' rotateYAxisName='0' showBorder='0' showValues='1' canvasbgColor='ffffff' canvasbgAlpha='0' canvasBaseDepth='1' canvasPadding='21'  baseFontSize='10' legendPosition='right' legendPosition='right' ");
        chartTitelStartBuf.append(" legendBgColor='ffffff' legendShadow='0' legendBorderColor='ffffff' placeValuesInside='0' ").append(" showYAxisValues='").append(showYAxisValues).append("' ");
        chartTitelStartBuf.append(" bgAlpha='0,0' bgAngle='270' chartRightMargin='35' numvdivlines='5' shadowAlpha='40' alternateHGridColor='CC3300' ");
        chartTitelStartBuf.append(" alternateHGridAlpha='5' showAlternateHGridColor='1' divLineIsDashed='1' divLineColor='CC3300' divLineAlpha='20' anchorRadius='2' ");
        
        if (StringUtils.isNotBlank(clickUrl)) {
            chartTitelStartBuf.append(" ClickUrl='n-").append(clickUrl).append("'");
        }
        if(StringUtils.isNotBlank(unit)){
            chartTitelStartBuf.append(" subCaption='").append("单位:").append(unit).append("' ").append(" rotateYAxisName='1' ");
        }
        chartTitelStartBuf.append(" caption='").append(caption).append("' ");
        chartTitelStartBuf.append(" > ");

        categoriesBufStartTitle.append("<categories>");
        lineBufStartTitle.append("<dataset seriesname='").append(lineName).append("' renderAs='Line' color='BBDA00' anchorSides='4' anchorRadius='5' anchorBgColor='BBDA00' anchorBorderColor='FFFFFF' anchorBorderThickness='2'>");
        if (null != datas) {
            for (int i = 0; i < datas.size(); i++) {
                vo = datas.get(i);
                categoriesBuf.append("<category name='").append(vo.getCategory()).append("' />");
                if((i+1)%2==0){
                    lineBuf.append("<set value='").append(vo.getLineValue()).append("' anchorBgColor='").append(vo.getLineStatus()).append("' anchorBorderColor='").append(vo.getLineStatus()).append("' valuePosition='ABOVE'/>");
                }else{
                    lineBuf.append("<set value='").append(vo.getLineValue()).append("' anchorBgColor='").append(vo.getLineStatus()).append("' anchorBorderColor='").append(vo.getLineStatus()).append("' valuePosition='BLOW'/>");
                }
            }
        }
        lineBufEndTitle.append("</dataset>");
        categoriesBufEndTitle.append("</categories>");

        return chartTitelStartBuf.append(categoriesBufStartTitle).append(categoriesBuf).append(categoriesBufEndTitle).
                append(lineBufStartTitle).append(lineBuf).append(lineBufEndTitle)
                .append(chartTitelEndBuf).toString();
    }

}
