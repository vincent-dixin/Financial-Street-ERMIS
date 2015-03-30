package com.fhd.kpi.business.dynamictable;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.fhd.comm.entity.AlarmRegion;

public class FCAngularGauge {

    public static String COLOR_R = "FF654F";

    public static String COLOR_C = "F59630";

    public static String COLOR_Y = "ffe600";

    public static String COLOR_G = "8BBA00";

    public static String getXml(List<AlarmRegion> AlarmRegionList, Double assessValue, String name, String id, boolean hasChild, String ctx) {
        StringBuffer xml = new StringBuffer();
        xml.append("<chart   gaugeInnerRadius='50' showTickMarks='0' showTickValues='0'  bgColor='ffffff' showBorder='0' lowerLimit='0' upperLimit='100'    gaugeFillRatio='8' ");
        /*xml.append("<chart showBorder='1' canvasBottomMargin='100' showBorder='0' bgColor='FFFFFF' manageResize='1' origW='400'");
        xml.append("origH='250' manageValueOverlapping='1' autoAlignTickValues='1' upperLimit='100'");
        xml.append("lowerLimit='0' majorTMNumber='10' majorTMHeight='8' showGaugeBorder='0' ");
        xml.append(" gaugeOuterRadius='140' gaugeOriginX='189' gaugeOriginY='160' gaugeInnerRadius='2' formatNumberScale='1' decmials='2'");*/
        if (hasChild) {
            xml.append(" ClickUrl='").append(ctx).append("/jrj/angulargauge.do?scid=" + id + "'>");
        }
        else {
            xml.append(" >");
        }

        xml.append("<colorRange>");
        for (AlarmRegion alarmRegion : AlarmRegionList) {
            if (StringUtils.isNotBlank(alarmRegion.getAlarmIcon().getId())) {
                if ("0alarm_startus_h".equals(alarmRegion.getAlarmIcon().getId())) {
                    xml.append("<color minValue='" + alarmRegion.getMinValue() + "' maxValue='" + alarmRegion.getMaxValue() + "' code='" + COLOR_R
                            + "'/>");
                }
                else if ("0alarm_startus_m".equals(alarmRegion.getAlarmIcon().getId())) {
                    xml.append("<color minValue='" + alarmRegion.getMinValue() + "' maxValue='" + alarmRegion.getMaxValue() + "' code='" + COLOR_C
                            + "'/>");
                    //xml.append("<color minValue='" + alarmRegion.getMinValue() + "' maxValue='" + alarmRegion.getMaxValue() + "' code='" + COLOR_G + "'/>");
                }
                else if ("0alarm_startus_l".equals(alarmRegion.getAlarmIcon().getId())) {
                    xml.append("<color minValue='" + alarmRegion.getMinValue() + "' maxValue='" + alarmRegion.getMaxValue() + "' code='" + COLOR_Y
                            + "'/>");
                }
                else if ("0alarm_startus_safe".equals(alarmRegion.getAlarmIcon().getId())) {
                    xml.append("<color minValue='" + alarmRegion.getMinValue() + "' maxValue='" + alarmRegion.getMaxValue() + "' code='" + COLOR_G
                            + "'/>");
                }
            }
        }
        //xml.append("<color minValue='" + 100 + "' maxValue='" + 120 + "' code='" + COLOR_G + "'/>");
        xml.append("</colorRange>");
        xml.append("<dials>");
        //xml.append("<dial value='" + assessValue + "' borderAlpha='0' baseWidth='28' topWidth='1' radius='130'/>");
        xml.append("<dial value='" + assessValue + "'    rearExtension='10'/>");
        xml.append("</dials>");
        /*xml.append("<annotations>");
        xml.append("<annotationGroup>");
        xml.append("<annotation type='text' x='120' y='130' label='" + name + "("+assessValue+")' align='center' bold='1' color='#3d7f9f' size='12'/>");
        xml.append("</annotationGroup>");
        xml.append("</annotations>");*/
        xml.append("</chart>");

        return xml.toString();
    }
}