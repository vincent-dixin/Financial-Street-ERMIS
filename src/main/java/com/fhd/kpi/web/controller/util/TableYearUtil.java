package com.fhd.kpi.web.controller.util;

public class TableYearUtil {
//	public static void main(String[] args) {
//		String startTableHtml = getStartTableHtml();
//		String year = "2012";
//		String halfYearHtml = "";
//		
//		boolean isHalfYearInputReality = false; 
//		boolean isHalfYearInputTarget = false; 
//		boolean isHalfYearInputAssess = false;
//		
//		halfYearHtml += getYearHtml(year,
//				isHalfYearInputReality, isHalfYearInputTarget, isHalfYearInputAssess,
//				"年ID", "年NAME",
//				"11", "22", "33");
//		
//		String endTableHtml = getEndTableHtml();
//		
//		String html = startTableHtml + halfYearHtml + endTableHtml;
//		System.out.println(html);
//	}
	
	
	public static String getStartTableHtml(boolean isEdit, String kpiName){
		StringBuffer startTableHtml = new StringBuffer();
		
		startTableHtml.append("<fieldset style='margin-left:10px;margin-right:10px;' class='x-fieldset x-fieldset-with-title x-fieldset-with-legend x-fieldset-default'>\n");
		startTableHtml.append("<legend class='x-fieldset-header x-fieldset-header-default' id='legendId'>" + kpiName + "</legend>\n");
		startTableHtml.append("<table width='100%' height='80%' border='1' cellpadding='0' cellspacing='0' class='fhd_add'>\n");
		startTableHtml.append("<tbody>\n");
		startTableHtml.append("<tr id='historyChartHeader'>\n");
		startTableHtml.append("<td class='cen' nowrap='nowrap' width='5%' align='center'>年份</td>\n");
		startTableHtml.append("<td class='cen' width='5%' align='center'>状态</td>\n");
		startTableHtml.append("<td class='cen' width='5%' align='center'>趋势</td>\n");
		startTableHtml.append("<td hi='1' id='rollup_4' class='cen' width='20%' align='center'>实际值</td>\n");
		startTableHtml.append("<td hi='1' id='rollup_5' class='cen' width='20%' align='center'>目标值</td>\n");
		startTableHtml.append("<td hi='1' id='rollup_6' class='cen' width='20%'v>评估值</td>\n");
		if(!isEdit){
			startTableHtml.append("<td class='cen' width='5%' align='center'>操作</td>\n");
		}
		startTableHtml.append("</tr>\n");
		
		return startTableHtml.toString();
	}
	
	public static String getYearHtml(String contextPath, boolean isEdit, String kpiId, String timeId,
			String year,
			boolean isInputReality, 
			boolean isInputTarget, 
			boolean isInputAssess,
			String realityId,
			String targetId, 
			String assessId, 
			String realityValue,
			String targetValue,
			String assessValue, String statusImg, String trendImg){
		realityValue = Utils.getValue(realityValue);
		targetValue = Utils.getValue(targetValue);
		assessValue = Utils.getValue(assessValue);
		
		StringBuffer yearHtml = new StringBuffer();
		
		//实际
		String inputReality = "<input type='text' value='" + realityValue + "' name='" + realityId + "reality' id='" + realityId + "reality' class='tableTextInputChanged' style='font-size: 100%;'>";
		//目标
		String inputTarget = "<input type='text' value='" + targetValue + "' name='" + targetId + "target' id='" + targetId + "target' class='tableTextInputChanged' style='font-size: 100%;'>";
		//评估
		String inpuAssess = "<input type='text' value='" + assessValue + "' name='" + assessId + "assess' id='" + assessId + "assess' class='tableTextInputChanged' style='font-size: 100%;'>";
		
		if(!isInputReality){
			inputReality = realityValue;
		}
		if(!isInputTarget){
			inputTarget = targetValue;
		}
		
		if(!isInputAssess){
			inpuAssess = assessValue;
		}
		
		String trendimgHtml = "";
		if(trendImg.equalsIgnoreCase("")){
			trendimgHtml = "";
		}else{
			trendimgHtml = "<div style='width: 32px; height: 19px; background-repeat: no-repeat;background-position: center top;' class='" + trendImg + "'/>";
		}	
		
		yearHtml.append("<tr>\n");
		yearHtml.append("<td nowrap='true' class='normalTabLevel1' hi='1' rowspan='1' align='center'>" + year + "</td>\n");
		yearHtml.append("<input type='hidden' value='1139' name='time_period_sid_1'>\n");
		yearHtml.append("<td nowrap='true' align='center' class='normalTabLevel3' hi='3' id='RT_status_1'><div style='width: 32px; height: 19px; background-repeat: no-repeat;background-position: center top;' class='" + statusImg + "'/></td>\n");
		yearHtml.append("<td nowrap='true' align='center' class='normalTabLevel3' hi='3' id='RT_trend_1'>" + trendimgHtml + "</td>\n");
		yearHtml.append("<td nowrap='true' align='center' class='normalTabLevel3' hi='3' id='RT_actual_1'>" + inputReality + "</td>\n");
		yearHtml.append("<td nowrap='true' align='center' class='normalTabLevel3' hi='3' id='RT_target_1'>" + inputTarget + "</td>\n");
		yearHtml.append("<td nowrap='true' align='center' class='normalTabLevel3' hi='3' id='RT_tolerance_1'>" + inpuAssess + "</td>\n");
		
		if(!isEdit){
			if(!isInputReality && !isInputTarget && !isInputAssess){
				yearHtml.append("<td nowrap='true' align='center' hi='3' id='RT_tolerance_1' class='cen'>" +
						"<input type='image' src='" + contextPath + "/images/icons/edit.gif' title='编辑' onclick=\"Ext.getCmp('tablePanel').oneInput('yearId','" + timeId + "');\"/></td>\n");
			}else{
				yearHtml.append("<td nowrap='true' align='center' hi='3' id='RT_tolerance_1' class='cen'>");
				yearHtml.append("<input type='image' src='" + contextPath + "/images/icons/save.gif' title='保存' onclick=\"Ext.getCmp('tablePanel').oneSave('" + timeId + "','" + kpiId + "','" + timeId + "',document.getElementById('" + realityId + "reality').value,document.getElementById('" + targetId + "target').value,document.getElementById('" + assessId + "assess').value);\"/>");
				yearHtml.append("</td>\n");
			}
		}
		
		
		yearHtml.append("</tr>\n");
		
		return yearHtml.toString();
	}
	
	public static String getEndTableHtml(){
		StringBuffer endTableHtml = new StringBuffer();
		
		endTableHtml.append("</tbody></table></fieldset>\n");
		
		return endTableHtml.toString();
	}
}
