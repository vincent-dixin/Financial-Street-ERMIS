package com.fhd.icm.business.chart;

import java.util.List;

import com.fhd.fdc.utils.Contents;

/**
 * FUNSIONCHARTS 2D柱形图
 * @author   吴德福
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-4-6		下午19:15:15
 * @see 	 
 */
public class ChartColumn2D {

	/**
	 * 2D柱形图XML.
	 * @author 吴德福
	 * @param title
	 * @param subTitle
	 * @param xAxisName
	 * @param yAxisName
	 * @param list xml数据集合
	 * @return String
	*/
	public static String getXml(String title, String subTitle, String xAxisName, String yAxisName, List<Object[]> list) {
		StringBuffer xml = new StringBuffer();
		xml.append("<chart caption=\"").append(title).append("\" ")
			.append("subcaption=\"").append(subTitle).append("\" ")
			.append("xAxisName=\"").append(xAxisName).append("\" ")
			.append("yAxisName=\"").append(yAxisName).append("\" ")
			.append("palette=\"2\" ")
			.append("decimalPrecision=\"0\" ")
			.append("formatNumberScale=\"0\" ")
			.append("showValues=\"1\" ")
			.append("yAxisValues =\"Font\" ")
			.append("decimals=\"0\" ")
			.append("labelDisplay=\"Rotate\" ")
			.append("rotateLabels=\"1\" ")
			.append("slantLabels=\"1\" ")
			.append("showAlternateHGridColor=\"0\" ")
			.append("bgColor=\"FFFFFF\" ")
			.append("borderColor =\"FFFFFF\" ")
			.append("showLabels=\"1\" ")
			.append("canvasBorderColor=\"C0C0C0\" ")
			.append("canvasBottomMargin=\"70\" ")
			.append("plotBorderColor=\"C0C0C0\" ")
			.append("plotGradientColor=\"\" ")
			.append(">");
		
		String labelName = "";
		String value = "";
		for (Object[] objects : list) {
			if(null != objects[0]){
				labelName = String.valueOf(objects[0]);
			}else{
				labelName = Contents.NONE;
			}
			if(null != objects[1]){
				value = String.valueOf(objects[1]);
			}else{
				value = "0";
			}
			
			xml.append("<set label=\"").append(labelName).append("\" value=\"").append(value).append("\" />");
		}
		
		xml.append("<styles>")
			.append("<definition>")
				.append("<style type=\"font\"  name=\"captionFont\" bold=\"0\" size=\"16\"/>")
			.append("</definition>")
			.append("<application>")
	        	.append("<apply toObject=\"caption\" styles=\"captionFont\"/>")
	        .append("</application>")
	    .append("</styles>");
		
		xml.append("</chart>");
		return xml.toString();
	}
}