package com.fhd.jrj.business.chart;

import java.util.List;
import java.util.Map;

public class FCMSLineChart {
    
    public static String getXML(Map<String,List<FCMSLineVO>> dataMap ){
        StringBuffer xmlBuf = new StringBuffer();
        String[] color = new String[]{"1D8BD1","000079","6c3365","336666","ff00ff","00aeae"};
        xmlBuf.append("<chart bgAlpha='0,0' bgAngle='270' chartRightMargin='35' numvdivlines='5' shadowAlpha='40' alternateHGridColor='CC3300' ");
        xmlBuf.append(" alternateHGridAlpha='5' showAlternateHGridColor='1' divLineIsDashed='1' divLineColor='CC3300' divLineAlpha='20' anchorRadius='2' ");
        xmlBuf.append(" showValues='1' showBorder='0' lineThickness='1' canvasPadding='10' > ");
        xmlBuf.append(" <categories> ");
        xmlBuf.append("<category label='1月'/>");
        xmlBuf.append("<category label='2月'/>");
        xmlBuf.append("<category label='3月'/>");
        xmlBuf.append("<category label='4月'/>");
        xmlBuf.append("<category label='5月'/>");
        xmlBuf.append("<category label='6月'/>");
        xmlBuf.append("<category label='7月'/>");
        xmlBuf.append("<category label='8月'/>");
        xmlBuf.append("<category label='9月'/>");
        xmlBuf.append("<category label='10月'/>");
        xmlBuf.append("<category label='11月'/>");
        xmlBuf.append("<category label='12月'/>");
        xmlBuf.append(" </categories> ");
        int i=0;
        for (Map.Entry<String, List<FCMSLineVO>> data : dataMap.entrySet()) {
            String name = "";
            //String kpiId = data.getKey();
            List<FCMSLineVO> listVo = data.getValue();
            if(listVo.size()>0){
                name = listVo.get(0).getName();
            }
            xmlBuf.append("<dataset  seriesName='").append(name).append("' color='").append(color[i++]).append("'>");
            for (FCMSLineVO fcmsLineVO : listVo) {
                if(null!=fcmsLineVO.getRatioValue()){
                    xmlBuf.append("<set value='").append(-fcmsLineVO.getRatioValue()).append("' anchorBgColor='").append(fcmsLineVO.getDotColor()).append("' anchorBorderColor='").append(fcmsLineVO.getDotColor()).append("'/>");
                }
                /*else{
                    xmlBuf.append("<set value='").append(0).append("' anchorBgColor='").append(fcmsLineVO.getDotColor()).append("' anchorBorderColor='").append(fcmsLineVO.getDotColor()).append("'/>");
                }*/
            }
            xmlBuf.append("</dataset>");
            
        }
        xmlBuf.append("</chart>");
        return xmlBuf.toString();
    }

}
