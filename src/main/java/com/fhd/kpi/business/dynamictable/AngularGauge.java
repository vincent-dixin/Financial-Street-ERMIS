package com.fhd.kpi.business.dynamictable;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.fhd.comm.entity.AlarmRegion;

public class AngularGauge {
    
    public static String COLOR_R = "aa363d";
    public static String COLOR_Y = "ffe600";
    public static String COLOR_G = "1d953f";
    

	public static String getXml(List<AlarmRegion> AlarmRegionList, Double assessValue, String name){
		StringBuffer xml = new StringBuffer();
		xml.append("<chart canvasBottomMargin='100' showBorder='0' bgColor='FFFFFF' manageResize='1' origW='400'");
		xml.append("origH='250' manageValueOverlapping='1' autoAlignTickValues='1' upperLimit='100'");
		xml.append("lowerLimit='0' majorTMNumber='10' majorTMHeight='8' showGaugeBorder='0' gaugeOuterRadius='140'");
		xml.append("gaugeOriginX='189' gaugeOriginY='160' gaugeInnerRadius='2' formatNumberScale='1' decmials='2'");
		xml.append("tickMarkDecimals='1' pivotRadius='17' showPivotBorder='1' pivotBorderThickness='5' tickValueDistance='10' >");
		xml.append("<colorRange>");
		for (AlarmRegion alarmRegion : AlarmRegionList) {
		    if(StringUtils.isNotBlank(alarmRegion.getAlarmIcon().getId())){
		        if("0alarm_startus_h".equals(alarmRegion.getAlarmIcon().getId())){
		            xml.append("<color minValue='" + alarmRegion.getMinValue() + "' maxValue='" + alarmRegion.getMaxValue() + "' code='" + COLOR_R + "'/>");
		        }else if("0alarm_startus_m".equals(alarmRegion.getAlarmIcon().getId())){
                    xml.append("<color minValue='" + alarmRegion.getMinValue() + "' maxValue='" + alarmRegion.getMaxValue() + "' code='" + COLOR_Y + "'/>");
                }else if("0alarm_startus_l".equals(alarmRegion.getAlarmIcon().getId())){
                    xml.append("<color minValue='" + alarmRegion.getMinValue() + "' maxValue='" + alarmRegion.getMaxValue() + "' code='" + COLOR_G + "'/>");
                }
		    }
		}
		xml.append("</colorRange>");
		xml.append("<dials>");
		xml.append("<dial value='" + assessValue + "' borderAlpha='0' baseWidth='28' topWidth='1' radius='130'/>");
		xml.append("</dials>");
		xml.append("<annotations>");
		xml.append("<annotationGroup>");
		xml.append("<annotation type='text' x='157' y='160' label='" + name + "' align='center' bold='1' color='#3d7f9f' size='12'/>");
		xml.append("</annotationGroup>");
		xml.append("</annotations>");
		xml.append("</chart>");
		
		return xml.toString();
	}
}