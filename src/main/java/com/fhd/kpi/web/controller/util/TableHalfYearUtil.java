package com.fhd.kpi.web.controller.util;

public class TableHalfYearUtil {
//	public static void main(String[] args) {
//		String startTableHtml = getStartTableHtml();
//		String YearHtml = getYear("2012");
//		String halfYearHtml = "";
//		boolean isYearSunInputReality = false;
//		boolean isYearSunInputTarget = false;
//		boolean isYearSunInputAssess = false;
//		
//		boolean isHalfYearInputReality = false; 
//		boolean isHalfYearInputTarget = false; 
//		boolean isHalfYearInputAssess = false;
//		
//		for (int i = 1; i < 2 + 1; i++) {
//			halfYearHtml += getHalfYearHtml("半年",
//					isHalfYearInputReality, isHalfYearInputTarget, isHalfYearInputAssess,
//					"半年ID", "半年NAME",
//					"11", "22", "33");
//		}
//		
//		String yearSunHtml = getYearSunHtml(isYearSunInputReality, isYearSunInputTarget, isYearSunInputAssess,
//				"年ID", "年NAME",
//				"a", "b", "c");
//		String endTableHtml = getEndTableHtml();
//		
//		String html = startTableHtml + YearHtml + halfYearHtml + yearSunHtml + endTableHtml;
//		System.out.println(html);
//	}
	
	
	public static String getStartTableHtml(boolean isEdit, String kpiName){
		StringBuffer startTableHtml = new StringBuffer();
		
		startTableHtml.append("<fieldset style='margin-left:10px;margin-right:10px;' class='x-fieldset x-fieldset-with-title x-fieldset-with-legend x-fieldset-default'>\n");
		startTableHtml.append("<legend class='x-fieldset-header x-fieldset-header-default' id='legendId'>" + kpiName + "</legend>\n");
		startTableHtml.append("<table width='100%' height='80%' border='1' cellpadding='0' cellspacing='0' class='fhd_add'>\n");
		startTableHtml.append("<tbody>");
		startTableHtml.append("<tr id='historyChartHeader'>\n");
		startTableHtml.append("<td class='cen' nowrap='nowrap' width='5%' align='center'>年份</td>\n");
		startTableHtml.append("<td class='cen' width='5%' align='center'>半年</td>\n");
		startTableHtml.append("<td class='cen' width='5%' align='center'>状态</td>\n");
		startTableHtml.append("<td class='cen' width='5%' align='center'>趋势</td>\n");
		startTableHtml.append("<td hi='1' id='rollup_4' class='cen' width='20%' align='center'>实际值</td>\n");
		startTableHtml.append("<td hi='1' id='rollup_5' class='cen' width='20%' align='center'>目标值</td>\n");
		startTableHtml.append("<td hi='1' id='rollup_6' class='cen' width='20%' align='center'>评估值</td>\n");
		if(!isEdit){
			startTableHtml.append("<td class='cen' width='5%' align='center'>操作</td>\n");
		}
		startTableHtml.append("</tr>\n");
		
		return startTableHtml.toString();
	}
	
	public static String getYear(String year){
		StringBuffer yearHtml = new StringBuffer();
		yearHtml.append("<tr>\n");
		yearHtml.append("<td nowrap='true' align='center' hi='1' rowspan='6'>" + year + "</td>\n");
		//yearHtml.append("</tr>\n");
		
		return yearHtml.toString();
	}
	
	public static String getHalfYearHtml(String contextPath, String yearId, boolean isEdit, String kpiId, String timeId,
			String halfYear,
			boolean isInputReality, 
			boolean isInputTarget, 
			boolean isInputAssess,
			String inputId, 
			String inputName,
			String realityValue,
			String targetValue,
			String assessValue, String statusImg, String trendImg){
		realityValue = Utils.getValue(realityValue);
		targetValue = Utils.getValue(targetValue);
		assessValue = Utils.getValue(assessValue);
		
		StringBuffer quarterHtml = new StringBuffer();
		
		//实际
		String inputReality = "<input type='text' value='" + realityValue + "' name='" + inputId + "reality' id='" + inputName + "reality' class='tableTextInputChanged' style='font-size: 100%;'>";
		//目标
		String inputTarget = "<input type='text' value='" + targetValue + "' name='" + inputId + "target' id='" + inputName + "target' class='tableTextInputChanged' style='font-size: 100%;'>";
		//评估
		String inpuAssess = "<input type='text' value='" + assessValue + "' name='" + inputId + "assess' id='" + inputName + "assess' class='tableTextInputChanged' style='font-size: 100%;'>";
		
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
		
		//quarterHtml.append("<tr>\n");
		quarterHtml.append("<td nowrap='true' align='center' hi='1' rowspan='1'>" + halfYear + "</td>\n");
		quarterHtml.append("<input type='hidden' value='1140' name='time_period_sid_1'>\n");
		quarterHtml.append("<td nowrap='true' align='center' class='normalTabLevel3' hi='3' id='RT_status_1'><div style='width: 32px; height: 19px; background-repeat: no-repeat;background-position: center top;' class='" + statusImg + "'/></td>\n");
		quarterHtml.append("<td nowrap='true' align='center' class='normalTabLevel3' hi='3' id='RT_trend_1'>" + trendimgHtml + "</td>\n");
		quarterHtml.append("<td nowrap='true' align='center' class='normalTabLevel3' hi='3' id='RT_actual_1'>" + inputReality + "</td>\n");
		quarterHtml.append("<td nowrap='true' align='center' class='normalTabLevel3' hi='3' id='RT_target_1'>" + inputTarget + "</td>\n");
		quarterHtml.append("<td nowrap='true' align='center' class='normalTabLevel3' hi='3' id='RT_tolerance_1'>" + inpuAssess + "</td>\n");
		
		if(!isEdit){
			if(!isInputReality && !isInputTarget && !isInputAssess){
				quarterHtml.append("<td nowrap='true' align='center' hi='3' id='RT_tolerance_1' class='cen'>" +
						"<input type='image' src='" + contextPath + "/images/icons/edit.gif' title='编辑' onclick=\"Ext.getCmp('tablePanel').oneInput('" + timeId + "','" + yearId + "');\"/></td>\n");
			}else{
				quarterHtml.append("<td nowrap='true' align='center' hi='3' id='RT_tolerance_1' class='cen'>");
				quarterHtml.append("<input type='image' src='" + contextPath + "/images/icons/save.gif' title='保存' onclick=\"Ext.getCmp('tablePanel').oneSave('" + yearId + "','" + kpiId + "','" + timeId + "',document.getElementById('" + inputId + "reality').value,document.getElementById('" + inputId + "target').value,document.getElementById('" + inputId + "assess').value);\"/>");
				quarterHtml.append("</td>\n");
			}
		}
		
		
		quarterHtml.append("</tr>\n");
		
		return quarterHtml.toString();
	}
	
	public static String getYearSunHtml(String contextPath, boolean isEdit, String kpiId, String timeId,
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
		
		StringBuffer YearSunHtml = new StringBuffer();
		
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
		
		YearSunHtml.append("<tr>\n");
		YearSunHtml.append("<td nowrap='true' class='cen' hi='1' rowspan='1' align='center'>年汇总</td>\n");
		YearSunHtml.append("<input type='hidden' value='1152' name='time_period_sid_4'>\n");
		YearSunHtml.append("<td nowrap='true' align='center' hi='2' id='RT_status_4' class='cen'class='cen'><div style='width: 32px; height: 19px; background-repeat: no-repeat;background-position: center top;' class='" + statusImg + "'/></td>\n");
		YearSunHtml.append("<td nowrap='true' align='center' hi='2' id='RT_trend_4' class='cen' class='cen'>" + trendimgHtml + "</td>\n");
		YearSunHtml.append("<td nowrap='true' align='center' class='cen' hi='3' id='RT_actual_4'>" + inputReality + "</td>\n");
		YearSunHtml.append("<td nowrap='true' align='center' class='cen' hi='3' id='RT_target_4'>" + inputTarget + "</td>\n");
		YearSunHtml.append("<td nowrap='true' align='center' class='cen' hi='3' id='RT_tolerance_4'>" + inpuAssess + "</td>\n");
		
		if(!isEdit){
			if(!isInputReality && !isInputTarget && !isInputAssess){
				YearSunHtml.append("<td nowrap='true' align='center' hi='3' id='RT_tolerance_1' class='cen'>" +
						"<input type='image' src='" + contextPath + "/images/icons/edit.gif' title='编辑' onclick=\"Ext.getCmp('tablePanel').oneInput('yearId','" + timeId + "');\"/></td>\n");
			}else{
				YearSunHtml.append("<td nowrap='true' align='center' hi='3' id='RT_tolerance_1' class='cen'>");
				YearSunHtml.append("<input type='image' src='" + contextPath + "/images/icons/save.gif' title='保存' onclick=\"Ext.getCmp('tablePanel').oneSave('" + timeId + "','" + kpiId + "','" + timeId + "',document.getElementById('" + realityId + "reality').value,document.getElementById('" + targetId + "target').value,document.getElementById('" + assessId + "assess').value);\"/>");
				YearSunHtml.append("</td>\n");
			}
		}
		
		YearSunHtml.append("</tr>\n");
		
		return YearSunHtml.toString();
	}
	
	public static String getEndTableHtml(){
		StringBuffer endTableHtml = new StringBuffer();
		
		endTableHtml.append("</tbody></table></fieldset>\n");
		
		return endTableHtml.toString();
	}
}