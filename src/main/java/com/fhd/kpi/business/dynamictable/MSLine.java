package com.fhd.kpi.business.dynamictable;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.fhd.kpi.entity.RelaAssessResult;

public class MSLine {

	public static String getXml(List<RelaAssessResult> relaAssessResultByDataTypeList){
		int count = 0;
		StringBuffer xml = new StringBuffer();
		xml.append("<chart canvasBottomMargin='90' showBorder='0' canvasBorderColor='C0C0C0' showAlternateHGridColor='0'");
		xml.append("showAlternateVGridColor='0' bgColor='FFFFFF' numdivlines='4' lineThickness='2' showValues='0'");
		xml.append("decimals='2' showLegend='0' legendNumColumns='1' formatNumberScale='1' labelDisplay='Rotate'");
		xml.append("slantLabels='1' numVDivLines='0' anchorRadius='2' anchorBgAlpha='50' anchorAlpha='100'");
		xml.append("limitsDecimalPrecision='0' divLineDecimalPrecision='1'>");
		xml.append("<categories>");
		for (RelaAssessResult relaAssessResult : relaAssessResultByDataTypeList) {
			if(relaAssessResultByDataTypeList.size() > 12){
				if(count < 12){
					String str[] = StringUtils.split(relaAssessResult.getTimePeriod().getId(), "mm");
					xml.append("<category label='" + str[0].toString() + "/" + str[1].toString() + "'/>");
				}else{
					break;
				}
			}else{
				String str[] = StringUtils.split(relaAssessResult.getTimePeriod().getId(), "mm");
				xml.append("<category label='" + str[0].toString() + "/" + str[1].toString() + "'/>");
			}
			
		}		
		xml.append("</categories>");
		xml.append("<dataset seriesName='评估值' color='800080' anchorBorderColor='800080'>");
		for (RelaAssessResult relaAssessResult : relaAssessResultByDataTypeList) {
			if(relaAssessResultByDataTypeList.size() > 12){
				if(count < 12){
					xml.append("<set value='" + relaAssessResult.getAssessmentValue() + "'/>");
				}else{
					break;
				}
			}else{
				xml.append("<set value='" + relaAssessResult.getAssessmentValue() + "'/>");
			}
			
		}
		xml.append("</dataset>");
		xml.append("</chart>");
		
		return xml.toString();
	}
}