package com.fhd.icm.business.chart;

/**
 * FUNSIONCHARTS 仪表盘图
 * @author   吴德福
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-4-6		下午19:15:15
 * @see 	 
 */
public class AngularGauge {
    
	/**
	 * 2D仪表盘图XML.
	 * @param value
	 * @param name
	 * @param trend 趋势;0表示越大越好，1表示越小越好
	 * @return String
	 */
	public static String getXml(String value, String name, String trend){
		
		StringBuffer xml = new StringBuffer();
		String color = "3399FF";
		if(Double.valueOf(value)<50){
			if("0".equals(trend)){
				color = "FF6600";
			}else{
				color = "339900";
			}
		}else if(Double.valueOf(value)>=50 && Double.valueOf(value)<75){
			if("0".equals(trend)){
				color = "CC9900";
			}else{
				color = "CC9900";
			}
		}else{
			if("0".equals(trend)){
				color = "339900";
			}else{
				color = "FF6600";
			}
		}
		
		xml.append("'<chart lowerLimit=\"0\" upperLimit=\"100\" gaugeScaleAngle=\"360\" minorTMNumber=\"0\" majorTMNumber=\"0\" gaugeOuterRadius=\"35\" gaugeInnerRadius=\"25\" placeValuesInside=\"0\"  pivotRadius=\"1\" showGaugeBorder=\"0\"  majorTMColor=\"ffffff\" showBorder=\"0\" chartLeftMargin=\"10\" chartTopMargin=\"10\" chartBottomMargin=\"30\" placeValuesInside=\"1\" displayValueDistance=\"999\" basefontColor=\"000000\" toolTipBgColor=\"FFFFFF\"  majorTMHeight=\"10\" showShadow=\"0\" numberSuffix=\"%\" bgColor=\"FFFFFF\">")
			.append("<colorRange>")
				.append("<color minValue=\"0\" maxValue=\"").append(value).append("\" code=\"").append(color).append("\"/>")
				.append("<color minValue=\"").append(value).append("\" maxValue=\"100\" code=\"FDEBE3\"/>")
			.append("</colorRange>")
			.append("<dials>")
				.append("<dial value=\"").append(value).append("\" showValue=\"0\" valueY=\"45\" baseWidth=\"4\" radius=\"20\" topWidth=\"1\" animation=\"1\" borderAlpha=\"0\" />")
			.append("</dials>");
			
		/*xml.append("<styles>")
	   			.append("<definition>")
	   				.append("<style type=\"font\" color=\"").append(color).append("\" name=\"valueFont\" bold=\"1\" size=\"12\"/>")
	   			.append("</definition>")
	   			.append("<application>")
	            	.append("<apply toObject=\"value\" styles=\"valueFont\"/>")
	            .append("</application>")
	        .append("</styles>");*/

		xml.append("</chart>'");
		
		return xml.toString();
		
	}
}