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
import com.fhd.kpi.web.controller.util.TableWeekUtil;
import com.fhd.sys.entity.dic.DictEntry;

@Service
public class TableWeekBO {

	@Autowired
    private KpiGatherResultBO o_KpiGatherResultBO;
	
	@Autowired
	private KpiBO o_kpiBO;
	 
	@Autowired
	private KpiTableBO o_kpiTableBO;
	
	/**
     * 得到周动态TABLE
     * 
     * @author 金鹏祥
     * @param year 年
     * @param isEdit 是否编辑
     * @param kpiId 指标ID
     * @return String
     * @since  fhd　Ver 1.1
    */
    public String getWeekHtml(String contextPath, String year, boolean isEdit, String kpiId, String kpiName, String timeId){
    	HashMap<String, KpiGatherResult> map = o_KpiGatherResultBO.findMapKpiGatherResultListByKpiId(kpiId);
    	List<TimePeriod> monthList = o_kpiTableBO.findTimePeriodById(year, "0frequecy_month");
	    List<TimePeriod> weekList = o_kpiTableBO.findTimePeriodByEType("0frequecy_week");
	    Kpi kpi = o_kpiBO.findKpiById(kpiId);
	    Map<String, DictEntry> dictMap = o_kpiTableBO.findMapDictEntryAll();
	    kpiName = "采集信息";
	    String titleHtml = TableWeekUtil.getStartTableHtml(isEdit, kpiName);
		String endHtml = TableWeekUtil.getEndTableHtml();
		String montAndWeekHtml = "";
		String html = "";
		int yearNumeral = 0;
		int monthNumeral = 0;
		String weekHtml = "";
		String monthHtml = "";
		String lastHtml = "";
		String alarmStatusImgPath = "";
		String assImgPath = "";
		List<TimePeriod> weekByIdList = null;
		String tableLastHtml = "";
		String imgNull = "icons/underconstruction_small.gif";
		KpiGatherResult kpiGatherResultYear = null;
		KpiGatherResult kpiGatherResultMonth = null;
		KpiGatherResult kpiGatherResultWeek = null;
		boolean isFinishYear = false;
		boolean isTargetYear = false;
		boolean isAssessmentYear = false;
		boolean isFinishMonth = false;
		boolean isTargetMonth = false;
		boolean isAssessmentMonth = false;
		boolean isFinishWeek = false;
		boolean isTargetWeek = false;
		boolean isAssessmentWeek = false;
		
		if(isEdit){
			isFinishYear = true;
			isTargetYear = true;
			isAssessmentYear = true;
			
			isFinishMonth = true;
			isTargetMonth = true;
			isAssessmentMonth = true;
			
			isFinishWeek = true;
			isTargetWeek = true;
			isAssessmentWeek = true;
		}
		
//		//不是全部编辑状态、KPI呈现、不呈现---年
//		if(!isEdit){
//			if(kpi.getIsResultFormula() == null){
//				isFinishYear = false;
//			}if(kpi.getIsResultFormula() == null){
//				isTargetYear = false;
//			}if(kpi.getIsResultFormula() == null){
//				isAssessmentYear = false;
//			}if(kpi.getIsResultFormula() != null){
//				if(kpi.getIsResultFormula().equalsIgnoreCase("0sys_use_formular_formula")){
//					isFinishYear = true;//false;
//				}
//			}if(kpi.getIsTargetFormula() != null){
//				if(kpi.getIsTargetFormula().equalsIgnoreCase("0sys_use_formular_formula")){
//					isTargetYear = true;//false;
//				}
//			}if(kpi.getIsAssessmentFormula() != null){
//				if(kpi.getIsAssessmentFormula().equalsIgnoreCase("0sys_use_formular_formula")){
//					isAssessmentYear = true;//false;
//				}
//			}if(kpi.getIsResultFormula() != null){
//				if(kpi.getIsResultFormula().equalsIgnoreCase("0sys_use_formular_manual")){
//					isFinishYear = true;
//				}
//			}if(kpi.getIsTargetFormula() != null){
//				if(kpi.getIsTargetFormula().equalsIgnoreCase("0sys_use_formular_manual")){
//					isTargetYear = true;
//				}
//			}if(kpi.getIsAssessmentFormula() != null){
//				if(kpi.getIsAssessmentFormula().equalsIgnoreCase("0sys_use_formular_manual")){
//					isAssessmentYear = true;
//				}
//			}
//		}
		
		//不是全部编辑状态、单点
		if(!isEdit){
			if(!timeId.equalsIgnoreCase("")){
				if(timeId.equalsIgnoreCase("yearId")){
					isFinishYear = true;
					isTargetYear = true;
					isAssessmentYear = true;
				}else{
					isFinishYear = false;
					isTargetYear = false;
					isAssessmentYear = false;
				}
			}
			
//			else{
//				if(Integer.parseInt(year) < o_kpiTableBO.getYear()){
//					isFinishYear = false;
//					isTargetYear = false;
//					isAssessmentYear = false;
//				}
//			}
		}
		
		kpiGatherResultYear = map.get(year);
		//等到告警状态、趋势图片
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
		
		
		
		//创建年度总值
		tableLastHtml = TableWeekUtil.getTableLastHtml(contextPath, isEdit, kpiId, year, isFinishYear, isTargetYear, isAssessmentYear, 
				"realityYearId" + yearNumeral, "targetYearId" + yearNumeral, "assessYearId" + yearNumeral, 
				kpiGatherResultYear == null || kpiGatherResultYear.getFinishValue() == null?"":kpiGatherResultYear.getFinishValue().toString(), 
				kpiGatherResultYear == null || kpiGatherResultYear.getTargetValue() == null?"":kpiGatherResultYear.getTargetValue().toString(), 
				kpiGatherResultYear == null || kpiGatherResultYear.getAssessmentValue() == null?"":kpiGatherResultYear.getAssessmentValue().toString(),
				alarmStatusImgPath, assImgPath);
		
		for (TimePeriod monthEn : monthList) {
			yearNumeral = Integer.parseInt(monthEn.getYear());
			monthNumeral = Integer.parseInt(monthEn.getMonth());
			weekByIdList = o_kpiTableBO.getWeek(weekList, monthEn.getId());
			kpiGatherResultMonth = map.get(monthEn.getId());
			
			weekHtml = "";
			monthHtml = "";
			lastHtml = "";
			int weekInt = weekByIdList.size();
			
			//创建月
			if(monthNumeral == 12){
				monthHtml = TableWeekUtil.getMonthHtml(weekInt + 1 + 1, yearNumeral, monthNumeral);
			}else{
				monthHtml = TableWeekUtil.getMonthHtml(weekInt + 1, yearNumeral, monthNumeral);
			}
			
			//创建周
			int i = 1;
			for (TimePeriod timePeriod : weekByIdList) {
				kpiGatherResultWeek = map.get(timePeriod.getId());
				
//				//不是全部编辑状态、KPI呈现、不呈现---年
//				if(!isEdit){
//					if(kpi.getIsResultFormula() == null){
//						isFinishWeek = false;
//					}if(kpi.getIsResultFormula() == null){
//						isTargetWeek = false;
//					}if(kpi.getIsResultFormula() == null){
//						isAssessmentWeek = false;
//					}if(kpi.getIsResultFormula() != null){
//						if(kpi.getIsResultFormula().equalsIgnoreCase("0sys_use_formular_formula")){
//							isFinishWeek = true;//false;
//						}
//					}if(kpi.getIsTargetFormula() != null){
//						if(kpi.getIsTargetFormula().equalsIgnoreCase("0sys_use_formular_formula")){
//							isTargetWeek = true;//false;
//						}
//					}if(kpi.getIsAssessmentFormula() != null){
//						if(kpi.getIsAssessmentFormula().equalsIgnoreCase("0sys_use_formular_formula")){
//							isAssessmentWeek = true;//false;
//						}
//					}if(kpi.getIsResultFormula() != null){
//						if(kpi.getIsResultFormula().equalsIgnoreCase("0sys_use_formular_manual")){
//							isFinishWeek = true;
//						}
//					}if(kpi.getIsTargetFormula() != null){
//						if(kpi.getIsTargetFormula().equalsIgnoreCase("0sys_use_formular_manual")){
//							isTargetWeek = true;
//						}
//					}if(kpi.getIsAssessmentFormula() != null){
//						if(kpi.getIsAssessmentFormula().equalsIgnoreCase("0sys_use_formular_manual")){
//							isAssessmentWeek = true;
//						}
//					}
//				}
				
				//不是全部编辑状态、单点
				if(!isEdit){
					if(!timeId.equalsIgnoreCase("")){
						if(timeId.equalsIgnoreCase(timePeriod.getId())){
							isFinishWeek = true; 
							isTargetWeek = true; 
							isAssessmentWeek = true;
						}else{
							isFinishWeek = false; 
							isTargetWeek = false; 
							isAssessmentWeek = false;
						}
					}
					
//					else{
//						//如果DB周小于当前周,不显示input
//						if(o_kpiTableBO.isDate(timePeriod.getEndTime().toString())){
//							isFinishWeek = false; 
//							isTargetWeek = false; 
//							isAssessmentWeek = false;
//						}
//					}
				}
				
				//等到告警状态、趋势图片
				alarmStatusImgPath ="";
				assImgPath = "";
				if(kpiGatherResultWeek != null){
					if(kpiGatherResultWeek.getDirection() != null){
						assImgPath= dictMap.get(kpiGatherResultWeek.getDirection().getId()).getValue();
					}if(kpiGatherResultWeek.getAssessmentStatus() != null){
						alarmStatusImgPath = dictMap.get(kpiGatherResultWeek.getAssessmentStatus().getId()).getValue();
					}
				}
				
				if(assImgPath.equalsIgnoreCase("")){
					//assImgPath = imgNull;
				}if(StringUtils.isBlank(alarmStatusImgPath)){
					alarmStatusImgPath = imgNull;
				}
				
				weekHtml += TableWeekUtil.getWeekHtml(contextPath, year, isEdit, kpiId, timePeriod.getId(), i, isFinishWeek, isTargetWeek, isAssessmentWeek, 
						timePeriod.getId() , timePeriod.getId(), 
						kpiGatherResultWeek == null || kpiGatherResultWeek.getFinishValue() == null?"":kpiGatherResultWeek.getFinishValue().toString(), 
						kpiGatherResultWeek == null || kpiGatherResultWeek.getTargetValue() == null?"":kpiGatherResultWeek.getTargetValue().toString(), 
						kpiGatherResultWeek == null || kpiGatherResultWeek.getAssessmentValue() == null?"":kpiGatherResultWeek.getAssessmentValue().toString(),
						alarmStatusImgPath, assImgPath);
				
				if((i + 1) == weekInt){
//					//不是全部编辑状态、KPI呈现、不呈现---年
//					if(!isEdit){
//						if(kpi.getIsResultFormula() == null){
//							isFinishMonth = false;
//						}if(kpi.getIsResultFormula() == null){
//							isTargetMonth = false;
//						}if(kpi.getIsResultFormula() == null){
//							isAssessmentMonth = false;
//						}if(kpi.getIsResultFormula() != null){
//							if(kpi.getIsResultFormula().equalsIgnoreCase("0sys_use_formular_formula")){
//								isFinishMonth = true;//false;
//							}
//						}if(kpi.getIsTargetFormula() != null){
//							if(kpi.getIsTargetFormula().equalsIgnoreCase("0sys_use_formular_formula")){
//								isTargetMonth = true;//false;
//							}
//						}if(kpi.getIsAssessmentFormula() != null){
//							if(kpi.getIsAssessmentFormula().equalsIgnoreCase("0sys_use_formular_formula")){
//								isAssessmentMonth = true;//false;
//							}
//						}if(kpi.getIsResultFormula() != null){
//							if(kpi.getIsResultFormula().equalsIgnoreCase("0sys_use_formular_manual")){
//								isFinishMonth = true;
//							}
//						}if(kpi.getIsTargetFormula() != null){
//							if(kpi.getIsTargetFormula().equalsIgnoreCase("0sys_use_formular_manual")){
//								isTargetMonth = true;
//							}
//						}if(kpi.getIsAssessmentFormula() != null){
//							if(kpi.getIsAssessmentFormula().equalsIgnoreCase("0sys_use_formular_manual")){
//								isAssessmentMonth = true;
//							}
//						}
//					}
					
					//不是全部编辑状态、单点
					if(!isEdit){
						if(!timeId.equalsIgnoreCase("")){
							if(timeId.equalsIgnoreCase(year + "mm" + o_kpiTableBO.getMonthStr(monthNumeral))){
								isFinishMonth = true; 
								isTargetMonth = true; 
								isAssessmentMonth = true;
							}else{
								isFinishMonth = false; 
								isTargetMonth = false; 
								isAssessmentMonth = false;
							}
						}
						
//						else{
//							//如果DB月份小于当前月份,不显示input
//							if(Integer.parseInt(year) < o_kpiTableBO.getYear()){
//								isFinishMonth = false; 
//								isTargetMonth = false; 
//								isAssessmentMonth = false;
//							}
//							if(monthNumeral < o_kpiTableBO.getMonth()){
//								isFinishMonth = false; 
//								isTargetMonth = false; 
//								isAssessmentMonth = false;
//							}
//						}
					}
					
					//等到告警状态、趋势图片
					alarmStatusImgPath ="";
					assImgPath = "";
					if(kpiGatherResultMonth != null){
						if(kpiGatherResultMonth.getDirection() != null){
							assImgPath= dictMap.get(kpiGatherResultMonth.getDirection().getId()).getValue();
						}if(kpiGatherResultMonth.getAssessmentStatus() != null){
							alarmStatusImgPath = dictMap.get(kpiGatherResultMonth.getAssessmentStatus().getId()).getValue();
						}
					}
					
					if(assImgPath.equalsIgnoreCase("")){
						//assImgPath = imgNull;
					}if(StringUtils.isBlank(alarmStatusImgPath)){
						alarmStatusImgPath = imgNull;
					}
					
					lastHtml = TableWeekUtil.getLastHtml(contextPath, year, isEdit, kpiId, year + "mm" + o_kpiTableBO.getMonthStr(monthNumeral), isFinishMonth, isTargetMonth, isAssessmentMonth, 
							"realityMonthId" + monthNumeral, "targetMonthId" + monthNumeral, "assessMonthId" + monthNumeral,
							kpiGatherResultMonth == null || kpiGatherResultMonth.getFinishValue() == null?"":kpiGatherResultMonth.getFinishValue().toString(), 
							kpiGatherResultMonth == null || kpiGatherResultMonth.getTargetValue() == null?"":kpiGatherResultMonth.getTargetValue().toString(), 
							kpiGatherResultMonth == null || kpiGatherResultMonth.getAssessmentValue() == null?"":kpiGatherResultMonth.getAssessmentValue().toString(),
							alarmStatusImgPath, assImgPath);
				}
				i++;
			}
			
			if(monthNumeral == 12){
				montAndWeekHtml += monthHtml + weekHtml + lastHtml + tableLastHtml;
			}else{
				montAndWeekHtml += monthHtml + weekHtml + lastHtml;
			}
		} 
	    html = titleHtml + montAndWeekHtml + endHtml;
	    return html;
    }
}
