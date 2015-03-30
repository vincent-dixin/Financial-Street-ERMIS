package com.fhd.kpi.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.kpi.entity.Kpi;
import com.fhd.kpi.entity.KpiGatherResult;

@Service
public class KpiGatherResultMonthBO {

	@Autowired
    private KpiGatherResultBO o_kpiGatherResultBO;
	
	@Autowired
	private KpiGatherReultUtilsBO o_kpiGatherReultUtilsBO;
	
	@Autowired
    private KpiBO o_kpiBO;
	
	public String getParams(String params, String kpiId){
		//查询该指标全部数据存放到MAP中
		HashMap<String, String> kpiGatherResultMap = null;
    	//月
		List<KpiGatherResult> list = new ArrayList<KpiGatherResult>();
		
		String[] whereIn = null;
		int quarter = 0;
		String quarterKey = "";
		String quarterTargetValue = "";
		String quarterFinishValue = "";
		String quarterAssessmentValue = "";
		
		String yearKey = "";
		String yearTargetValue = "";
		String yearFinishValue = "";
		String yearAssessmentValue = "";
		
		String key = "";
		String targetValue = "";
		String finishValue = "";
		String assessmentValue = "";
		
		//得到当前点击的类型与值
		key = params.toString().split(":")[0].toString().replace("[{\"", "").replace("\"", "");
		finishValue = params.toString().split(":")[1].toString().split(",")[0].replace("\"", "");
		targetValue = params.toString().split(":")[1].toString().split(",")[1];
		assessmentValue = params.toString().split(":")[1].toString().split(",")[2].replace("\"}]", "");
		
		int year = 0;
		String monthStr = "";
		int month = 0;
		if(params.toString().indexOf("mm") != -1){
			//点击的是月
			year = Integer.parseInt(params.toString().split(":")[0].toString().replace("[{\"", "").replace("\"", "").split("mm")[0].toString());
			monthStr = params.toString().split(":")[0].toString().replace("[{\"", "").replace("\"", "").split("mm")[1].toString();
			month = Integer.parseInt(monthStr);
			
		
			kpiGatherResultMap = o_kpiGatherResultBO.findMapKpiGatherResultListByKpiId(kpiId, String.valueOf(year));
			kpiGatherResultMap = o_kpiGatherReultUtilsBO.filterMap(kpiGatherResultMap);
			
			yearKey = String.valueOf(year);
			
			if(month >= 1 && month <=3 ){
				quarter = 1;
			}else if(month > 3 && month <=6){
				quarter = 2;
			}else if(month > 6 && month <=9){
				quarter = 3;
			}else if(month > 9 && month <=12){
				quarter = 4;
			}
			
			if(quarter == 1){
				//第1季度
				whereIn = new String[3];
				whereIn[0] = year + "mm01";
				whereIn[1] = year + "mm02";
				whereIn[2] = year + "mm03";
				quarterKey = year + "Q1";
				list = o_kpiGatherResultBO.findKpiGatherResultListByTimePeriodId(kpiId, whereIn);
			}if(quarter == 2){
				//第2季度
				whereIn = new String[3];
				whereIn[0] = year + "mm04";
				whereIn[1] = year + "mm05";
				whereIn[2] = year + "mm06";
				quarterKey = year + "Q2";
				list = o_kpiGatherResultBO.findKpiGatherResultListByTimePeriodId(kpiId, whereIn);
			}if(quarter == 3){
				//第3季度
				whereIn = new String[3];
				whereIn[0] = year + "mm07";
				whereIn[1] = year + "mm08";
				whereIn[2] = year + "mm09";
				quarterKey = year + "Q3";
				list = o_kpiGatherResultBO.findKpiGatherResultListByTimePeriodId(kpiId, whereIn);
			}if(quarter == 4){
				//第4季度
				whereIn = new String[3];
				whereIn[0] = year + "mm10";
				whereIn[1] = year + "mm11";
				whereIn[2] = year + "mm12";
				quarterKey = year + "Q4";
				list = o_kpiGatherResultBO.findKpiGatherResultListByTimePeriodId(kpiId, whereIn);
			}
			
			//单条修改
			kpiGatherResultMap.put(key, key + "," + finishValue + "," + targetValue + "," + assessmentValue);
			
			if(list.size() >= 2){
				//求季度
				List<Double> finishValueList = null;
				List<Double> targetValueList = null;
				List<Double> assessmentValueList = null;
				
				
				if(list.size() < 3){
					list = o_kpiGatherReultUtilsBO.addKpiGatherResultList(list, kpiGatherResultMap, key);
				}
				
				finishValueList = o_kpiGatherReultUtilsBO.addFinishValueList(list);
				targetValueList = o_kpiGatherReultUtilsBO.addTargetValueList(list);
				assessmentValueList = o_kpiGatherReultUtilsBO.addAssessmentValueList(list);
				
				//得到指标实体
				Kpi kpi = o_kpiBO.findKpiById(kpiId);
				
				//得到季度汇总
				quarterTargetValue = o_kpiGatherReultUtilsBO.getTargetValue(kpi, targetValueList);
				quarterFinishValue = o_kpiGatherReultUtilsBO.getFinishValue(kpi, finishValueList);
				quarterAssessmentValue = o_kpiGatherReultUtilsBO.getAssessmentValue(kpi, assessmentValueList);
				
				//季度
				kpiGatherResultMap.put(quarterKey, quarterKey + "," + quarterFinishValue + "," + quarterTargetValue + "," + quarterAssessmentValue);
				
				//得到年度汇总
				whereIn = new String[4];
				whereIn[0] = year + "Q1";
				whereIn[1] = year + "Q2";
				whereIn[2] = year + "Q3";
				whereIn[3] = year + "Q4";
				quarterKey = year + "Q4";
				list = o_kpiGatherResultBO.findKpiGatherResultListByTimePeriodId(kpiId, whereIn);
				
				if(list.size() > 2 ){
					finishValueList = o_kpiGatherReultUtilsBO.addFinishValueList(list);
					targetValueList = o_kpiGatherReultUtilsBO.addTargetValueList(list);
					assessmentValueList = o_kpiGatherReultUtilsBO.addAssessmentValueList(list);
					
	    			yearTargetValue = o_kpiGatherReultUtilsBO.getTargetValue(kpi, targetValueList);
	    			yearFinishValue = o_kpiGatherReultUtilsBO.getFinishValue(kpi, finishValueList);
	    			yearAssessmentValue = o_kpiGatherReultUtilsBO.getAssessmentValue(kpi, assessmentValueList);
	    			//年度
	    			kpiGatherResultMap.put(yearKey, yearKey + "," + yearFinishValue + "," + yearTargetValue + "," + yearAssessmentValue);
				}else{
					if(kpiGatherResultMap.get(yearKey+"Q2") != null){
						if(kpiGatherResultMap.get(yearKey+"Q3") == null){
							list = o_kpiGatherReultUtilsBO.addKpiGatherResultList(list, kpiGatherResultMap, yearKey+"Q2");
							
							finishValueList = o_kpiGatherReultUtilsBO.addFinishValueList(list);
							targetValueList = o_kpiGatherReultUtilsBO.addTargetValueList(list);
							assessmentValueList = o_kpiGatherReultUtilsBO.addAssessmentValueList(list);
							
			    			yearTargetValue = o_kpiGatherReultUtilsBO.getTargetValue(kpi, targetValueList);
			    			yearFinishValue = o_kpiGatherReultUtilsBO.getFinishValue(kpi, finishValueList);
			    			yearAssessmentValue = o_kpiGatherReultUtilsBO.getAssessmentValue(kpi, assessmentValueList);
			    			//年度
			    			kpiGatherResultMap.put(yearKey, yearKey + "," + yearFinishValue + "," + yearTargetValue + "," + yearAssessmentValue);
						}
					}
				}
				
				params = o_kpiGatherReultUtilsBO.getMapToParams(kpiGatherResultMap, params);
	    	}
		}
    	return params;
	}
}