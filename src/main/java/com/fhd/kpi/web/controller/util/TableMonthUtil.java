package com.fhd.kpi.web.controller.util;

public class TableMonthUtil {

    public static String getStartTableHtml(boolean isEdit, String kpiName) {
        StringBuffer startTableHtml = new StringBuffer();
        startTableHtml
                .append("<fieldset style='margin-left:10px;margin-right:10px;' class='x-fieldset x-fieldset-with-title x-fieldset-with-legend x-fieldset-default'>\n");
        startTableHtml.append("<legend class='x-fieldset-header x-fieldset-header-default' id='legendId'>" + kpiName + "</legend>\n");
        startTableHtml.append("<table width='100%' height='80%' border='1' cellpadding='0' cellspacing='0' class='fhd_add'>\n");
        startTableHtml.append("<tbody></tr><tr id='historyChartHeader'><td class='cen' width='5%' align='center'>年份</td>\n");
        startTableHtml.append("<td width='5%' class='cen' align='center'>季度</td><td class='cen' width='5%'>月份</td>\n");
        startTableHtml.append("<td width='5%' class='cen' align='center'>状态</td>\n");
        startTableHtml.append("<td width='5%' class='cen' align='center'>趋势</td>\n");
        startTableHtml.append("<td class='cen'width='20%' align='center'>实际值</td>\n");
        startTableHtml.append("<td class='cen' width='20%' align='center'>目标值</td>\n");
        startTableHtml.append("<td class='cen' width='20%' align='center'>评估值</td>");
        if (!isEdit) {
            startTableHtml.append("<td class='cen' width='5%' align='center'>操作</td>\n");
        }
        startTableHtml.append("</tr>\n");

        return startTableHtml.toString();
    }

    public static String getYear(String year) {
        StringBuffer yearHtml = new StringBuffer();
        yearHtml.append("<tr>\n");
        yearHtml.append("<td nowrap='true'  hi='1' rowspan='22'>" + year + "</td>\n");
        //yearHtml.append("</tr>\n");

        return yearHtml.toString();
    }

    public static String getQuarterHtml(String quarter) {
        StringBuffer quarterHtml = new StringBuffer();

        quarterHtml.append("<td nowrap='true'  hi='1' rowspan='3' align='center'>第" + quarter + "季</td>\n");

        return quarterHtml.toString();
    }

    public static String getMonthHtml(String contextPath, String yearId, boolean isEdit, String kpiId, String timeId, String month,
            boolean isInputReality, boolean isInputTarget, boolean isInputAssess, String inputId, String inputName, String realityValue,
            String targetValue, String assessValue, String statusImg, String trendImg) {
        realityValue = Utils.getValue(realityValue);
        targetValue = Utils.getValue(targetValue);
        assessValue = Utils.getValue(assessValue);

        StringBuffer monthHtml = new StringBuffer();
        //实际
        String inputReality = "<input type='text' value='" + realityValue + "' name='" + inputName + "reality' id='" + inputId
                + "reality' style='font-size: 100%;'>";
        //目标
        String inputTarget = "<input type='text' value='" + targetValue + "' name='" + inputName + "target' id='" + inputId
                + "target' style='font-size: 100%;'>";
        //评估
        String inpuAssess = "<input type='text' value='" + assessValue + "' name='" + inputName + "assess' id='" + inputId
                + "assess'  style='font-size: 100%;'>";

        if (!isInputReality) {
            inputReality = realityValue;
        }
        if (!isInputTarget) {
            inputTarget = targetValue;
        }

        if (!isInputAssess) {
            inpuAssess = assessValue;
        }

        String trendimgHtml = "";
        if (trendImg.equalsIgnoreCase("")) {
            trendimgHtml = "";
        }
        else {
            trendimgHtml = "<div style='width: 32px; height: 19px; background-repeat: no-repeat;background-position: center top;' class='" + trendImg
                    + "'/>";
        }

        //monthHtml.append("<tr>\n");
        monthHtml.append("<td nowrap='true' align='center'>" + month + "</td>\n");
        monthHtml
                .append("<td nowrap='true' align='center'><div style='width: 32px; height: 19px; background-repeat: no-repeat;background-position: center top;' class='"
                        + statusImg + "'/></td>\n");
        monthHtml.append("<td nowrap='true' align='center' class='normalTabLevel3' hi='3' id='RT_trend_1'>" + trendimgHtml + "</td>\n");
        monthHtml.append("<td nowrap='true' align='center' hi='3' id='RT_actual_1'>" + inputReality + "</td>\n");
        monthHtml.append("<td nowrap='true' align='center' hi='3' id='RT_target_1'>" + inputTarget + "</td>\n");
        monthHtml.append("<td nowrap='true' align='center' hi='3' id='RT_tolerance_1'>" + inpuAssess + "</td>\n");
        if (!isEdit) {
            if (!isInputReality && !isInputTarget && !isInputAssess) {
                monthHtml.append("<td nowrap='true' align='center' hi='3' id='RT_tolerance_1' class='cen'>" + "<input type='image' src='"
                        + contextPath + "/images/icons/edit.gif' title='编辑' onclick=\"Ext.getCmp('tablePanel').oneInput('" + timeId + "','" + yearId
                        + "');\"/></td>\n");
            }
            else {
                monthHtml.append("<td nowrap='true' align='center' hi='3' id='RT_tolerance_1' class='cen'>");
                monthHtml.append("<input type='image' src='" + contextPath
                        + "/images/icons/save.gif' title='保存' onclick=\"Ext.getCmp('tablePanel').oneSave('" + yearId + "','" + kpiId + "','" + timeId
                        + "',document.getElementById('" + inputId + "reality').value,document.getElementById('" + inputId
                        + "target').value,document.getElementById('" + inputId + "assess').value);\"/>");
                monthHtml.append("</td>\n");
            }
        }
        monthHtml.append("</tr>\n");

        return monthHtml.toString();
    }

    public static String getQuarterSunHtml(String contextPath, String yearId, boolean isEdit, String kpiId, String timeId, boolean isInputReality,
            boolean isInputTarget, boolean isInputAssess, String inputId, String inputName, String realityValue, String targetValue,
            String assessValue, String statusImg, String trendImg, boolean isShow) {
        realityValue = Utils.getValue(realityValue);
        targetValue = Utils.getValue(targetValue);
        assessValue = Utils.getValue(assessValue);

        StringBuffer quarterSunHtml = new StringBuffer();

        //实际
        String inputReality = "<input type='text' value='" + realityValue + "' name='" + inputName + "reality' id='" + inputId
                + "reality' style='font-size: 100%;'>";
        //目标
        String inputTarget = "<input type='text' value='" + targetValue + "' name='" + inputName + "target' id='" + inputId
                + "target' style='font-size: 100%;'>";
        //评估
        String inpuAssess = "<input type='text' value='" + assessValue + "' name='" + inputName + "assess' id='" + inputId
                + "assess' style='font-size: 100%;'>";

        if (!isInputReality) {
            inputReality = realityValue;
        }
        if (!isInputTarget) {
            inputTarget = targetValue;
        }

        if (!isInputAssess) {
            inpuAssess = assessValue;
        }

        String trendimgHtml = "";
        if (trendImg.equalsIgnoreCase("")) {
            trendimgHtml = "";
        }
        else {
            trendimgHtml = "<div style='width: 32px; height: 19px; background-repeat: no-repeat;background-position: center top;' class='" + trendImg
                    + "'/>";
        }

        quarterSunHtml.append("<tr>\n");
        quarterSunHtml.append("<td nowrap='true' hi='1' colspan='2' class='cen' align='center'>季汇总</td>\n");
        quarterSunHtml
                .append("<td nowrap='true' align='center' class='cen'><div style='width: 32px; height: 19px; background-repeat: no-repeat;background-position: center top;' class='"
                        + statusImg + "'/></td>\n");
        quarterSunHtml.append("<td nowrap='true' align='center' class='cen' hi='3' id='RT_trend_1'>" + trendimgHtml + "</td>\n");
        quarterSunHtml.append("<td nowrap='true' align='center' hi='2' id='RT_actual_4' class='cen'>" + inputReality + "</td>\n");
        quarterSunHtml.append("<td nowrap='true' align='center' hi='2' id='RT_target_4' class='cen'>" + inputTarget + "</td>\n");
        quarterSunHtml.append("<td nowrap='true' align='center' hi='2' id='RT_tolerance_4' class='cen'>" + inpuAssess + "</td>\n");

        if(isShow){
	        if (!isEdit) {
	            if (!isInputReality && !isInputTarget && !isInputAssess) {
	                quarterSunHtml.append("<td nowrap='true' align='center' hi='3' id='RT_tolerance_1' class='cen'>" + "<input type='image' src='"
	                        + contextPath + "/images/icons/edit.gif' title='编辑' onclick=\"Ext.getCmp('tablePanel').oneInput('" + timeId + "','" + yearId
	                        + "');\"/></td>\n");
	            }
	            else {
	                quarterSunHtml.append("<td nowrap='true' align='center' hi='3' id='RT_tolerance_1' class='cen'>");
	                quarterSunHtml.append("<input type='image' src='" + contextPath
	                        + "/images/icons/save.gif' title='保存' onclick=\"Ext.getCmp('tablePanel').oneSave('" + yearId + "','" + kpiId + "','" + timeId
	                        + "',document.getElementById('" + inputId + "reality').value,document.getElementById('" + inputId
	                        + "target').value,document.getElementById('" + inputId + "assess').value);\"/>");
	                quarterSunHtml.append("</td>\n");
	            }
	        }
        }else{
        	quarterSunHtml.append("<td nowrap='true' align='center' hi='3' id='RT_tolerance_1' class='cen'>" + "&nbsp;</td>\n");
        }

        quarterSunHtml.append("</tr>\n");

        return quarterSunHtml.toString();
    }

    public static String getYearSunHtml(String contextPath, boolean isEdit, String kpiId, String timeId, boolean isInputReality,
            boolean isInputTarget, boolean isInputAssess, String realityId, String targetId, String assessId, String realityValue,
            String targetValue, String assessValue, String statusImg, String trendImg) {

        realityValue = Utils.getValue(realityValue);
        targetValue = Utils.getValue(targetValue);
        assessValue = Utils.getValue(assessValue);

        StringBuffer YearSunHtml = new StringBuffer();

        //实际
        String inputReality = "<input type='text' value='" + realityValue + "' name='" + realityId + "reality' id='" + realityId
                + "reality'style='size: 100%;'>";
        //目标
        String inputTarget = "<input type='text' value='" + targetValue + "' name='" + targetId + "target' id='" + targetId
                + "target' style='font-size: 100%;'>";
        //评估
        String inpuAssess = "<input type='text' value='" + assessValue + "' name='" + assessId + "assess' id='" + assessId
                + "assess' style='font-size: 100%;'>";

        if (!isInputReality) {
            inputReality = realityValue;
        }
        if (!isInputTarget) {
            inputTarget = targetValue;
        }

        if (!isInputAssess) {
            inpuAssess = assessValue;
        }

        String trendimgHtml = "";
        if (trendImg.equalsIgnoreCase("")) {
            trendimgHtml = "";
        }
        else {
            trendimgHtml = "<div style='width: 32px; height: 19px; background-repeat: no-repeat;background-position: center top;' class='" + trendImg
                    + "'/>";
        }

        YearSunHtml.append("<tr>\n");
        YearSunHtml.append("<td nowrap='true' hi='1' colspan='2' class='cen' align='center'>年汇总</td>\n");
        YearSunHtml
                .append("<td nowrap='true' align='center' class='cen'><div style='width: 32px; height: 19px; background-repeat: no-repeat;background-position: center top;' class='"
                        + statusImg + "'/></td>\n");
        YearSunHtml.append("<td nowrap='true' align='center' class='cen' hi='3' id='RT_trend_1'>" + trendimgHtml + "</td>\n");
        YearSunHtml.append("<td nowrap='true' align='center' hi='2' id='RT_actual_4' class='cen'>" + inputReality + "</td>\n");
        YearSunHtml.append("<td nowrap='true' align='center' hi='2' id='RT_target_4' class='cen'>" + inputTarget + "</td>\n");
        YearSunHtml.append("<td nowrap='true' align='center' hi='2' id='RT_tolerance_4' class='cen'>" + inpuAssess + "</td>\n");

        if (!isEdit) {
            if (!isInputReality && !isInputTarget && !isInputAssess) {
                YearSunHtml.append("<td nowrap='true' align='center' hi='3' id='RT_tolerance_1' class='cen'>" + "<input type='image' src='"
                        + contextPath + "/images/icons/edit.gif' title='编辑' onclick=\"Ext.getCmp('tablePanel').oneInput('yearId','" + timeId
                        + "');\"/></td>\n");
            }
            else {
                YearSunHtml.append("<td nowrap='true' align='center' hi='3' id='RT_tolerance_1' class='cen'>");
                YearSunHtml.append("<input type='image' src='" + contextPath
                        + "/images/icons/save.gif' title='保存' onclick=\"Ext.getCmp('tablePanel').oneSave('" + timeId + "','" + kpiId + "','" + timeId
                        + "',document.getElementById('" + realityId + "reality').value,document.getElementById('" + targetId
                        + "target').value,document.getElementById('" + assessId + "assess').value);\"/>");
                YearSunHtml.append("</td>\n");
            }
        }

        YearSunHtml.append("</tr>");

        return YearSunHtml.toString();
    }

    public static String getEndTableHtml() {
        StringBuffer endTableHtml = new StringBuffer();

        endTableHtml.append("</tbody></table></fieldset>\n");

        return endTableHtml.toString();
    }
}