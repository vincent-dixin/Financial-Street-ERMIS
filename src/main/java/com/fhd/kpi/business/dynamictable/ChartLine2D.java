package com.fhd.kpi.business.dynamictable;

import java.util.HashMap;
import java.util.List;

import com.fhd.comm.entity.TimePeriod;
import com.fhd.kpi.entity.KpiGatherResult;

/**
 * FUNSIONCHARTS 2D折线图
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2013	2013-1-6		下午12:15:15
 *
 * @see 	 
 */
public class ChartLine2D {

	/**
	 * 得到XML
	 * 
	 * @author 金鹏祥
	 * @param title 主标题
	 * @param timeTitle 副标题 
	 * @param list 指标结果实体集合
	 * @return String
	*/
	public static String getXml(String title, String title2, List<KpiGatherResult> list, HashMap<String, TimePeriod> map) {
		StringBuffer xml = new StringBuffer();
		xml.append("<graph caption='" + title + "' ");
		xml.append("subcaption='" + title2 + "' ");
		xml.append("hovercapbg='FFECAA' hovercapborder='F47E00' ");
		xml.append("formatNumberScale='0' ");
		xml.append("decimalPrecision='0' ");
		xml.append("showvalues='0' ");
		xml.append("numdivlines='3' ");
		xml.append("numVdivlines='0' ");
		xml.append("yaxisminvalue='1000' ");
		xml.append("yaxismaxvalue='1800'  ");
		xml.append("rotateNames='1'>");
		xml.append("<categories >");
		for (KpiGatherResult kpiGatherResult : list) {
			xml.append("<category name='" + map.get(kpiGatherResult.getTimePeriod().getId()).getTimePeriodFullName() + "' />");
		}
		xml.append("</categories>");
		
		xml.append("<dataset seriesName='目标值' color='1D8BD1' anchorBorderColor='1D8BD1' anchorBgColor='1D8BD1'>");
		for (KpiGatherResult kpiGatherResult : list) {
			xml.append("<set value='" + kpiGatherResult.getTargetValue() + "' />");
		}
		xml.append("</dataset>");

		xml.append("<dataset seriesName='实际值' color='F1683C' anchorBorderColor='F1683C' anchorBgColor='F1683C'>");
		for (KpiGatherResult kpiGatherResult : list) {
			xml.append("<set value='" + kpiGatherResult.getFinishValue() + "' />");
		}
		xml.append("</dataset>");

		xml.append("<dataset seriesName='评估值' color='2AD62A' anchorBorderColor='2AD62A' anchorBgColor='2AD62A'>");
		for (KpiGatherResult kpiGatherResult : list) {
			xml.append("<set value='" + kpiGatherResult.getAssessmentValue() + "' />");
		}
		xml.append("</dataset>");
		xml.append("</graph>");
		
		return xml.toString();
	}
}