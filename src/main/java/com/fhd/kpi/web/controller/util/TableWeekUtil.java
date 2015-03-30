package com.fhd.kpi.web.controller.util;

public class TableWeekUtil {
//	public static String getTableWeek(){
//		String titleHtml = getTitleHtml();
//		String endHtml = getEndHtml();
////		int yearNumeral = 2012;
//		String montAndWeekHtml = "";
//		
////		System.out.println(titleHtml);
////		for (int i = 1; i < 1 + 1; i++) {
////			montAndWeekHtml += getMontAndWeekHtml(yearNumeral, i);
////		}
//		
//		return titleHtml + montAndWeekHtml + endHtml;
//	}
	
//	public static String getMontAndWeekHtml(int yearNumeral, int monthNumeral, List<TimePeriod> week){
//		String weekHtml = "";
//		String monthHtml = "";
//		String lastHtml = "";
//		int weekInt = week.size();
//			if(monthNumeral == 12){
//				monthHtml = getMonthHtml(weekInt + 1 + 1, yearNumeral, monthNumeral);
//			}else{
//				monthHtml = getMonthHtml(weekInt + 1, yearNumeral, monthNumeral);
//			}
//			
//			int i = 1;
//			for (TimePeriod timePeriod : week) {
//				weekHtml += getWeekHtml(i, true, true, true, timePeriod.getId() , timePeriod.getId(), "a", "1", "2");
//				
//				if((i + 1) == weekInt){
//					lastHtml = getLastHtml(false, false, false, "realityMonthId" + i, "targetMonthId" + i, "assessMonthId" + i, "1", "2", "3");
//				}
//				i++;
//			}
//		
//		if(monthNumeral == 12){
//			return monthHtml + weekHtml + lastHtml + getTableLastHtml(false, false, false, "realityYearId", "targetYearId", "assessYearId", null, null, null);
//		}else{
//			return monthHtml + weekHtml + lastHtml;
//		}
//	}
	
	
	public static String getStartTableHtml(boolean isEdit, String kpiName){
		StringBuffer startTableHtml = new StringBuffer();
		
		startTableHtml.append("<fieldset style='margin-left:10px;margin-right:10px;' class='x-fieldset x-fieldset-with-title x-fieldset-with-legend x-fieldset-default'>\n");
		startTableHtml.append("<legend class='x-fieldset-header x-fieldset-header-default' id='legendId'>" + kpiName + "</legend>\n");
		startTableHtml.append("<table width='100%' height='80%' border='1' cellpadding='0' cellspacing='0' class='fhd_add'>\n");
		startTableHtml.append("<tbody>");
		startTableHtml.append("<td class='cen' width='5%' align='center'>年</td>\n");
		startTableHtml.append("<td class='cen' width='5%' align='center'>月</td>\n");
		startTableHtml.append("<td class='cen' width='5%' align='center'>周</td>\n");
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
	
	public static String getMonthHtml(int rowSpan, int yearNumeral, int monthNumeral){
		StringBuffer monthHtml = new StringBuffer();
		
		monthHtml.append("<tr>");
		monthHtml.append("<td nowrap='true' class='normalTabLevel1' hi='1' rowspan='" + rowSpan + "' align='center'>" + yearNumeral + "</td><!--动态生成 -->\n");
		monthHtml.append("<td nowrap='true' class='normalTabLevel1' hi='1' rowspan='" + rowSpan + "' align='center'>" + monthNumeral + "月</td>\n");
		
		return monthHtml.toString();
	}
	
	public static String getWeekHtml(String contextPath, String yearId, boolean isEdit, String kpiId, String timeId,
			int weekNumeral, 
			boolean isManualInputReality, 
			boolean isManualInputTarget, 
			boolean isManualInputAssess,
			String inputId, 
			String inputName,
			String realityValue,
			String targetValue,
			String assessValue, String statusImg, String trendImg){
		realityValue = Utils.getValue(realityValue);
		targetValue = Utils.getValue(targetValue);
		assessValue = Utils.getValue(assessValue);
		
		//实际
		String inputReality = "<input type='text' value='" + realityValue + "' name='" + inputName + "reality' id='" + inputId + "reality' class='tableTextInputChanged' style='font-size: 100%;'>";
		//目标
		String inputTarget = "<input type='text' value='" + targetValue + "' name='" + inputName + "target' id='" + inputId + "target' class='tableTextInputChanged' style='font-size: 100%;'>";
		//评估
		String inpuAssess = "<input type='text' value='" + assessValue + "' name='" + inputName + "assess' id='" + inputId + "assess' class='tableTextInputChanged' style='font-size: 100%;'>";
//		String one = "<tr>\n";
		StringBuffer weekHtml = new StringBuffer();
		
		if(!isManualInputReality){
			inputReality = realityValue;
		}
		if(!isManualInputTarget){
			inputTarget = targetValue;
		}
		
		if(!isManualInputAssess){
			inpuAssess = assessValue;
		}
		
		String trendimgHtml = "";
		if(trendImg.equalsIgnoreCase("")){
			trendimgHtml = "";
		}else{
			trendimgHtml = "<div style='width: 32px; height: 19px; background-repeat: no-repeat;background-position: center top;' class='" + trendImg + "'/>";
		}
		
		weekHtml.append("<td nowrap='true' class='normalTabLevel1' hi='1' align='center'>" + weekNumeral + "周</td>\n");
		weekHtml.append("<input type='hidden' value='1140' name='time_period_sid_1'>\n");
		weekHtml.append("<td nowrap='true' align='center' hi='2' id='RT_status_4' class='cen'class='cen'><div style='width: 32px; height: 19px; background-repeat: no-repeat;background-position: center top;' class='" + statusImg + "'/></td>\n");
		weekHtml.append("<td nowrap='true' align='center' hi='2' id='RT_trend_4' class='cen' class='cen'>" + trendimgHtml + "</td>\n");
		weekHtml.append("<td nowrap='true' align='center' class='normalTabLevel3' hi='3' id='RT_actual_1'>" + inputReality + "</td>\n");
		weekHtml.append("<td nowrap='true' align='center' class='normalTabLevel3' hi='3' id='RT_target_1'>" + inputTarget + "</td>\n");
		weekHtml.append("<td nowrap='true' align='center' class='normalTabLevel3' hi='3' id='RT_tolerance_1'>" + inpuAssess + "</td>\n");
		
		if(!isEdit){
			if(!isManualInputReality && !isManualInputTarget && !isManualInputAssess){
				weekHtml.append("<td nowrap='true' align='center' hi='3' id='RT_tolerance_1' class='cen'>" +
						"<input type='image' src='" + contextPath + "/images/icons/edit.gif' title='编辑' onclick=\"Ext.getCmp('tablePanel').oneInput('" + timeId + "','" + yearId + "');\"/></td>\n");
			}else{
				weekHtml.append("<td nowrap='true' align='center' hi='3' id='RT_tolerance_1' class='cen'>");
				weekHtml.append("<input type='image' src='" + contextPath + "/images/icons/save.gif'	title='保存' onclick=\"Ext.getCmp('tablePanel').oneSave('" + yearId + "','" + kpiId + "','" + timeId + "',document.getElementById('" + inputId + "reality').value,document.getElementById('" + inputId + "target').value,document.getElementById('" + inputId + "assess').value);\"/>");
				weekHtml.append("</td>\n");
			}
		}
		
		
		weekHtml.append("</tr>");
		return weekHtml.toString();
//		if(isNoOne){
//			return weekHtml.toString();
//		}else{
//			one = one + weekHtml.toString();
//			return one;
//		}
	}
	
	public static String getLastHtml(String contextPath, String yearId, boolean isEdit, String kpiId, String timeId,
			boolean isManualInputRealitySun, 
			boolean isManualInputTargetSun, 
			boolean isManualInputAssessSun,
			String realityId,
			String targetId, 
			String assessId, 
			String realityValue,
			String targetValue,
			String assessValue, String statusImg, String trendImg){
		realityValue = Utils.getValue(realityValue);
		targetValue = Utils.getValue(targetValue);
		assessValue = Utils.getValue(assessValue);
		
		StringBuffer lastHtml = new StringBuffer();
		
		//实际总和
		String inputReality = "<input type='text' value='" + realityValue + "' name='" + realityId + "reality' id='" + realityId + "reality' class='tableTextInputChanged' style='font-size: 100%;'>";
		//目标总和
		String inputTarget = "<input type='text' value='" + targetValue + "' name='" + targetId + "target' id='" + targetId + "target' class='tableTextInputChanged' style='font-size: 100%;'>";
		//评估总和
		String inpuAssess = "<input type='text' value='" + assessValue + "' name='" + assessId + "assess' id='" + assessId + "assess' class='tableTextInputChanged' style='font-size: 100%;'>";
		
		if(!isManualInputRealitySun){
			inputReality = realityValue;
		}
		if(!isManualInputTargetSun){
			inputTarget = targetValue;
		}
		
		if(!isManualInputAssessSun){
			inpuAssess = assessValue;
		}
		
		String trendimgHtml = "";
		if(trendImg.equalsIgnoreCase("")){
			trendimgHtml = "";
		}else{
			trendimgHtml = "<div style='width: 32px; height: 19px; background-repeat: no-repeat;background-position: center top;' class='" + trendImg + "'/>";
		}
		
		lastHtml.append("<tr>");
		lastHtml.append("<td nowrap='true' class='cen' align='center'>月汇总</td>\n");
		lastHtml.append("<td nowrap='true' align='center' hi='2' id='RT_status_4' class='cen'class='cen'><div style='width: 32px; height: 19px; background-repeat: no-repeat;background-position: center top;' class='" + statusImg + "'/></td>\n");
		lastHtml.append("<td nowrap='true' align='center' hi='2' id='RT_trend_4' class='cen' class='cen'>" + trendimgHtml + "</td>\n");
		lastHtml.append("<td nowrap='true' align='center' class='cen' hi='3' id='RT_actual_1'>" + inputReality + "</td>\n");
		lastHtml.append("<td nowrap='true' align='center' class='cen' hi='3' id='RT_target_1'>" + inputTarget + "</td>\n");
		lastHtml.append("<td nowrap='true' align='center' class='cen' hi='3' id='RT_tolerance_1'>" + inpuAssess + "</td>\n");
		
		if(!isEdit){
			if(!isManualInputRealitySun && !isManualInputTargetSun && !isManualInputAssessSun){
				lastHtml.append("<td nowrap='true' align='center' hi='3' id='RT_tolerance_1' class='cen'>" +
						"<input type='image' src='" + contextPath + "/images/icons/edit.gif' title='编辑' onclick=\"Ext.getCmp('tablePanel').oneInput('" + timeId + "','" + yearId + "');\"/></td>\n");
			}else{
				lastHtml.append("<td nowrap='true' align='center' hi='3' id='RT_tolerance_1' class='cen'>");
				lastHtml.append("<input type='image' src='" + contextPath + "/images/icons/save.gif' title='保存' onclick=\"Ext.getCmp('tablePanel').oneSave('" + yearId + "','" + kpiId + "','" + timeId + "',document.getElementById('" + realityId + "reality').value,document.getElementById('" + targetId + "target').value,document.getElementById('" + assessId + "assess').value);\"/>");
				lastHtml.append("</td>\n");
			}
		}
		
		
		
		lastHtml.append("</tr>\n");

		return lastHtml.toString();						
	}
	
	public static String getTableLastHtml(String contextPath, boolean isEdit, String kpiId, String timeId,
			boolean isManualInputRealitySun, 
			boolean isManualInputTargetSun, 
			boolean isManualInputAssessSun,
			String realityId,
			String targetId, 
			String assessId, 
			String realityValue,
			String targetValue,
			String assessValue, String statusImg, String trendImg){
		
		realityValue = Utils.getValue(realityValue);
		targetValue = Utils.getValue(targetValue);
		assessValue = Utils.getValue(assessValue);
		
		StringBuffer tableLastHtml = new StringBuffer();
		
		//实际总和
		String inputReality = "<input type='text' value='" + realityValue + "' name='" + realityId + "reality' id='" + realityId + "reality' class='tableTextInputChanged' style='font-size: 100%;'>";
		//目标总和
		String inputTarget = "<input type='text' value='" + targetValue + "' name='" + targetId + "target' id='" + targetId + "target' class='tableTextInputChanged' style='font-size: 100%;'>";
		//评估总和
		String inpuAssess = "<input type='text' value='" + assessValue + "' name='" + assessId + "assess' id='" + assessId + "assess' class='tableTextInputChanged' style='font-size: 100%;'>";
		
		if(!isManualInputRealitySun){
			inputReality = realityValue;
		}
		if(!isManualInputTargetSun){
			inputTarget = targetValue;
		}
		
		if(!isManualInputAssessSun){
			inpuAssess = assessValue;
		}
		String trendimgHtml = "";
		if(trendImg.equalsIgnoreCase("")){
			trendimgHtml = "";
		}else{
			trendimgHtml = "<div style='width: 32px; height: 19px; background-repeat: no-repeat;background-position: center top;' class='" + trendImg + "'/>";
		}
		
		tableLastHtml.append("<tr><td nowrap='true' class='cen' hi='1' align='center'>年汇总</td>");
		tableLastHtml.append("<td nowrap='true' align='center' hi='2' id='RT_status_4' class='cen'class='cen'><div style='width: 32px; height: 19px; background-repeat: no-repeat;background-position: center top;' class='" + statusImg + "'/></td>\n");
		tableLastHtml.append("<td nowrap='true' align='center' hi='2' id='RT_trend_4' class='cen' class='cen'>" + trendimgHtml + "</td>\n");
		tableLastHtml.append("<td nowrap='true' align='center' class='cen' hi='3' id='RT_actual_1'>" + inputReality + "</td>");
		tableLastHtml.append("<td nowrap='true' align='center' class='cen' hi='3' id='RT_target_1'>" + inputTarget + "</td>");
		tableLastHtml.append("<td nowrap='true' align='center' class='cen' hi='3' id='RT_tolerance_1'>" + inpuAssess + "</td>");
		
		if(!isEdit){
			if(!isManualInputRealitySun && !isManualInputTargetSun && !isManualInputAssessSun){
				tableLastHtml.append("<td nowrap='true' align='center' hi='3' id='RT_tolerance_1' class='cen'>" +
						"<input type='image' src='" + contextPath + "/images/icons/edit.gif' title='编辑' onclick=\"Ext.getCmp('tablePanel').oneInput('yearId','" + timeId + "');\"/></td>\n");
			}else{
				tableLastHtml.append("<td nowrap='true' align='center' hi='3' id='RT_tolerance_1' class='cen'>");
				tableLastHtml.append("<input type='image' title='保存' src='" + contextPath + "/images/icons/save.gif' onclick=\"Ext.getCmp('tablePanel').oneSave('" + timeId + "','" + kpiId + "','" + timeId + "',document.getElementById('" + realityId + "reality').value,document.getElementById('" + targetId + "target').value,document.getElementById('" + assessId + "assess').value);\"/>");
				tableLastHtml.append("</td>\n");
			}
		}
		
		
		tableLastHtml.append("</tr>");
		
		return tableLastHtml.toString();
	}
	
	public static String getEndTableHtml(){
		StringBuffer endTableHtml = new StringBuffer();
		
		endTableHtml.append("</tbody></table></fieldset>\n");
		
		return endTableHtml.toString();
	}
}