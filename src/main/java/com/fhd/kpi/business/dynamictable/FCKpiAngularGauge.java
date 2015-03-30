package com.fhd.kpi.business.dynamictable;

public class FCKpiAngularGauge {

    public static String COLOR_R = "FF654F";

    public static String COLOR_C = "F59630";

    public static String COLOR_Y = "ffe600";

    public static String COLOR_G = "8BBA00";

    public static String getXml(Double assessValue, String name, String id, boolean hasChild, String ctx) {
        StringBuffer xml = new StringBuffer();
        xml.append("<chart showTickMarks='0' showTickValues='0'  bgColor='ffffff' showBorder='0' lowerLimit='0' upperLimit='100'    gaugeFillRatio='3' ");
        if (hasChild) {
            xml.append(" ClickUrl='").append(ctx).append("/jrj/angulargauge.do?scid=" + id + "'>");
        }
        else {
            xml.append(" >");
        }
        xml.append("<colorRange>");
        xml.append("<color minValue='" + "0" + "' maxValue='" + "25" + "' code='" + COLOR_R + "'/>");
        xml.append("<color minValue='" + "25" + "' maxValue='" + "50" + "' code='" + COLOR_C + "'/>");
        xml.append("<color minValue='" + "50" + "' maxValue='" + "75" + "' code='" + COLOR_Y + "'/>");
        xml.append("<color minValue='" + "75" + "' maxValue='" + "100" + "' code='" + COLOR_G + "'/>");
        xml.append("</colorRange>");
        xml.append("<dials>");
        xml.append("<dial value='" + assessValue + "'    rearExtension='10'/>");
        xml.append("</dials>");
        xml.append("</chart>");
        return xml.toString();
    }
}