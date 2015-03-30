package com.fhd.icm.business.chart;

import java.util.List;

import com.fhd.fdc.utils.Contents;

/**
 * FUNSIONCHARTS 2D柱形图
 * @author   吴德福
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-4-15		下午20:15:15
 * @see 	 
 */
public class PieDoughnut {

	/**
	 * 2D饼图XML.
	 * @author 吴德福
	 * @param title
	 * @param list xml数据集合
	 * @return String
	*/
	public static String getXml(String title, List<Object[]> list) {
		StringBuffer xml = new StringBuffer();
		
		/*
		<chart caption="Marketing Expenses" bgColor="FFFFFF,CCCCCC" showPercentageValues="1" 
				plotBorderColor="FFFFFF" numberPrefix="$" isSmartLineSlanted="0" showValues="0" 
				showLabels="0" showLegend="1">
	        <set value="212000" label="Banners" color="99CC00" alpha="60"/>
	        <set value="96800" label="Print Ads" color="333333" alpha="60"/>
	        <set value="26400" label="Service" color="99CC00" alpha="30" />
	        <set value="29300" label="Others" color="333333" alpha="30"/>
		</chart>
		*/
		
		xml.append("<chart caption=\"").append(title).append("\" ")
			//.append("subCaption=\"1111111111\" ")
			//.append("showPercentageValues=\"1\" ")
			//.append("plotBorderColor=\"FFFFFF\" ")
			//.append("numberPrefix=\"$\" ")
			//.append("isSmartLineSlanted=\"0\" ")
			//.append("showValues=\"0\" ")
			//.append("showLabels =\"0\" ")
			//.append("showLegend=\"1\" ")
			//.append("legendPosition =\"right\" ")
			//.append("palette=\"2\" ")
			//.append("decimalPrecision=\"0\" ")
			.append("formatNumberScale=\"0\" ")
			.append("bgColor=\"FFFFFF\" ")
			.append("borderColor =\"FFFFFF\" ")
			.append("canvasBorderColor=\"C0C0C0\" ")
			.append("outCnvbaseFontSize=\"13\" ")
			//.append("canvasBottomMargin=\"70\" ")
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
			//<set value="212000" label="Banners" color="99CC00" alpha="60"/>
			xml.append("<set label=\"").append(labelName).append("\" value=\"").append(value).append("\" />");
		}
		xml.append("<styles>")
			.append("<definition>")
				.append("<style type=\"font\"  name=\"captionFont\" bold=\"0\" size=\"18\"/>")
			.append("</definition>")
			.append("<application>")
	        	.append("<apply toObject=\"caption\" styles=\"captionFont\"/>")
	        .append("</application>")
	    .append("</styles>");
		xml.append("</chart>");
		return xml.toString();
	}
}
