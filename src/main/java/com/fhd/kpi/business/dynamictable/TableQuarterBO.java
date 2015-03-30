package com.fhd.kpi.business.dynamictable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.comm.entity.TimePeriod;
import com.fhd.kpi.business.KpiBO;
import com.fhd.kpi.business.KpiGatherResultBO;
import com.fhd.kpi.business.KpiTableBO;
import com.fhd.kpi.entity.Kpi;
import com.fhd.kpi.entity.KpiGatherResult;
import com.fhd.kpi.web.controller.util.TableQuarterUtil;
import com.fhd.sys.entity.dic.DictEntry;

@Service
public class TableQuarterBO {

	@Autowired
    private KpiGatherResultBO o_KpiGatherResultBO;
	
	@Autowired
	private KpiBO o_kpiBO;
	 
	@Autowired
	private KpiTableBO o_kpiTableBO;
	
	/**
     * 得到季度动态TABLE
     * 
     * @author 金鹏祥
     * @param year 年
     * @param isEdit 是否编辑
     * @param kpiId 指标ID
     * @return String
     * @since  fhd　Ver 1.1
    */
    public String getQuarterHtml(String contextPath, String year, boolean isEdit, String kpiId, String kpiName, String timeId){
    	HashMap<String, KpiGatherResult> map = o_KpiGatherResultBO.findMapKpiGatherResultListByKpiId(kpiId);
    	List<TimePeriod> quarterList = o_kpiTableBO.findTimePeriodById(year, "0frequecy_quarter");
	    Kpi kpi = o_kpiBO.findKpiById(kpiId);
	    Map<String, DictEntry> dictMap = o_kpiTableBO.findMapDictEntryAll();
    	String html = "";
    	KpiGatherResult kpiGatherResultYear = null;
	    KpiGatherResult kpiGatherResultQuarter = null;
	    kpiName = "采集信息";
    	String startTableHtml = TableQuarterUtil.getStartTableHtml(isEdit, kpiName);
		String YearHtml = TableQuarterUtil.getYear(year);
		String quarterHtml = "";
		String endTableHtml = TableQuarterUtil.getEndTableHtml();
		String alarmStatusImgPath = "";
		String assImgPath = "";
		String imgNull = "icons/underconstruction_small.gif";
		boolean isYearSunInputReality = false;
		boolean isYearSunInputTarget = false;
		boolean isYearSunInputAssess = false;
		boolean isQuarterInputReality = false; 
		boolean isQuarterInputTarget = false; 
		boolean isQuarterInputAssess = false;
		
		if(isEdit){
			isYearSunInputReality = true;
			isYearSunInputTarget = true;
			isYearSunInputAssess = true;
			
			isQuarterInputReality = true; 
			isQuarterInputTarget = true; 
			isQuarterInputAssess = true;
		}
		
		int i = 1;
		for (TimePeriod timePeriod : quarterList) {
//			//不是全部编辑状态、KPI呈现、不呈现---年
//			if(!isEdit){
//				if(kpi.getIsResultFormula() == null){
//					isQuarterInputReality = false;
//				}if(kpi.getIsResultFormula() == null){
//					isQuarterInputTarget = false;
//				}if(kpi.getIsResultFormula() == null){
//					isQuarterInputAssess = false;
//				}if(kpi.getIsResultFormula() != null){
//					if(kpi.getIsResultFormula().equalsIgnoreCase("0sys_use_formular_formula")){
//						isQuarterInputReality = true;//false;
//					}
//				}if(kpi.getIsTargetFormula() != null){
//					if(kpi.getIsTargetFormula().equalsIgnoreCase("0sys_use_formular_formula")){
//						isQuarterInputTarget = true;//false;
//					}
//				}if(kpi.getIsAssessmentFormula() != null){
//					if(kpi.getIsAssessmentFormula().equalsIgnoreCase("0sys_use_formular_formula")){
//						isQuarterInputAssess = true;//false;
//					}
//				}if(kpi.getIsResultFormula() != null){
//					if(kpi.getIsResultFormula().equalsIgnoreCase("0sys_use_formular_manual")){
//						isQuarterInputReality = true;
//					}
//				}if(kpi.getIsTargetFormula() != null){
//					if(kpi.getIsTargetFormula().equalsIgnoreCase("0sys_use_formular_manual")){
//						isQuarterInputTarget = true;
//					}
//				}if(kpi.getIsAssessmentFormula() != null){
//					if(kpi.getIsAssessmentFormula().equalsIgnoreCase("0sys_use_formular_manual")){
//						isQuarterInputAssess = true;
//					}
//				}
//			}
			
			//不是全部编辑状态、单点
			if(!isEdit){
				if(!timeId.equalsIgnoreCase("")){
					if(timeId.equalsIgnoreCase(timePeriod.getId())){
						isQuarterInputReality = true; 
						isQuarterInputTarget = true; 
						isQuarterInputAssess = true;
					}else{
						isQuarterInputReality = false; 
						isQuarterInputTarget = false; 
						isQuarterInputAssess = false;
					}
				}
//				else{
//					
//					if(Integer.parseInt(year) < o_kpiTableBO.getYear()){
//						isQuarterInputReality = false;
//						isQuarterInputTarget = false;
//						isQuarterInputAssess = false;
//					}
//					
//					//如果DB季度小于当前季度,不显示input
//					if(Integer.parseInt(timePeriod.getQuarter()) < o_kpiTableBO.getQuarter(o_kpiTableBO.getMonth())){
//						isQuarterInputReality = false; 
//						isQuarterInputTarget = false; 
//						isQuarterInputAssess = false;
//					}
//				}
			}
			
			kpiGatherResultQuarter = map.get(timePeriod.getId());
			//等到告警状态、趋势图片
			alarmStatusImgPath ="";
			assImgPath = "";
			if(kpiGatherResultQuarter != null){
				if(kpiGatherResultQuarter.getDirection() != null){
					assImgPath= dictMap.get(kpiGatherResultQuarter.getDirection().getId()).getValue();
				}if(kpiGatherResultQuarter.getAssessmentStatus() != null){
					alarmStatusImgPath = dictMap.get(kpiGatherResultQuarter.getAssessmentStatus().getId()).getValue();
				}
			}
			
			if(assImgPath.equalsIgnoreCase("")){
				//assImgPath = imgNull;
			}if(StringUtils.isBlank(alarmStatusImgPath)){
				alarmStatusImgPath = imgNull;
			}
			
			
			
			
			quarterHtml += TableQuarterUtil.getQuarterHtml(contextPath, year, isEdit, kpiId, timePeriod.getId(), String.valueOf(i),
					isQuarterInputReality, isQuarterInputTarget, isQuarterInputAssess,
					timePeriod.getId(), timePeriod.getId() ,
					kpiGatherResultQuarter == null || kpiGatherResultQuarter.getFinishValue() == null?"":kpiGatherResultQuarter.getFinishValue().toString(), 
					kpiGatherResultQuarter == null || kpiGatherResultQuarter.getTargetValue() == null?"":kpiGatherResultQuarter.getTargetValue().toString(), 
					kpiGatherResultQuarter == null || kpiGatherResultQuarter.getAssessmentValue() == null?"":kpiGatherResultQuarter.getAssessmentValue().toString(),
					alarmStatusImgPath,assImgPath);
			i++;
		}
		
//		//不是全部编辑状态、KPI呈现、不呈现---年
//				if(!isEdit){
//					if(kpi.getIsResultFormula() == null){
//						isYearSunInputReality = false;
//					}if(kpi.getIsResultFormula() == null){
//						isYearSunInputTarget = false;
//					}if(kpi.getIsResultFormula() == null){
//						isYearSunInputAssess = false;
//					}if(kpi.getIsResultFormula() != null){
//						if(kpi.getIsResultFormula().equalsIgnoreCase("0sys_use_formular_formula")){
//							isYearSunInputReality = true;//false;
//						}
//					}if(kpi.getIsTargetFormula() != null){
//						if(kpi.getIsTargetFormula().equalsIgnoreCase("0sys_use_formular_formula")){
//							isYearSunInputTarget = true;//false;
//						}
//					}if(kpi.getIsAssessmentFormula() != null){
//						if(kpi.getIsAssessmentFormula().equalsIgnoreCase("0sys_use_formular_formula")){
//							isYearSunInputAssess = true;//false;
//						}
//					}if(kpi.getIsResultFormula() != null){
//						if(kpi.getIsResultFormula().equalsIgnoreCase("0sys_use_formular_manual")){
//							isYearSunInputReality = true;
//						}
//					}if(kpi.getIsTargetFormula() != null){
//						if(kpi.getIsTargetFormula().equalsIgnoreCase("0sys_use_formular_manual")){
//							isYearSunInputTarget = true;
//						}
//					}if(kpi.getIsAssessmentFormula() != null){
//						if(kpi.getIsAssessmentFormula().equalsIgnoreCase("0sys_use_formular_manual")){
//							isYearSunInputAssess = true;
//						}
//					}
//				}
				
				//不是全部编辑状态、单点
				if(!isEdit){
					if(!timeId.equalsIgnoreCase("")){
						if(timeId.equalsIgnoreCase("yearId")){
							isYearSunInputReality = true;
							isYearSunInputTarget = true;
							isYearSunInputAssess = true;
						}else{
							isYearSunInputReality = false;
							isYearSunInputTarget = false;
							isYearSunInputAssess = false;
						}
					}
//					else{
//						if(Integer.parseInt(year) < o_kpiTableBO.getYear()){
//							isYearSunInputReality = false;
//							isYearSunInputTarget = false;
//							isYearSunInputAssess = false;
//						}
//					}
				}
		
		kpiGatherResultYear = map.get(year);
		alarmStatusImgPath ="";
		assImgPath = "";
		if(kpiGatherResultYear != null){
			if(kpiGatherResultYear.getDirection() != null){
				assImgPath= dictMap.get(kpiGatherResultYear.getDirection().getId()).getValue();
			}if(kpiGatherResultYear.getAssessmentStatus() != null){
				alarmStatusImgPath = dictMap.get(kpiGatherResultYear.getAssessmentStatus().getId()).getValue();
			}
		}
		
		if(assImgPath.equalsIgnoreCase("")){
			//assImgPath = imgNull;
		}if(StringUtils.isBlank(alarmStatusImgPath)){
			alarmStatusImgPath = imgNull;
		}
		
		String yearSunHtml = TableQuarterUtil.getYearSunHtml(contextPath, isEdit, kpiId, year, isYearSunInputReality, isYearSunInputTarget, isYearSunInputAssess,
				"realityYearId" + year, "targetYearId" + year, "assessYearId" + year, 
				kpiGatherResultYear == null || kpiGatherResultYear.getFinishValue() == null?"":kpiGatherResultYear.getFinishValue().toString(), 
				kpiGatherResultYear == null || kpiGatherResultYear.getTargetValue() == null?"":kpiGatherResultYear.getTargetValue().toString(), 
				kpiGatherResultYear == null || kpiGatherResultYear.getAssessmentValue() == null?"":kpiGatherResultYear.getAssessmentValue().toString(),
				alarmStatusImgPath,assImgPath);
		
		html = startTableHtml + YearHtml + quarterHtml + yearSunHtml + endTableHtml;
		
		return html;
    }
}