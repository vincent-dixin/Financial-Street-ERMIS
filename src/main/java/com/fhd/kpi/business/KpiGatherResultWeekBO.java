package com.fhd.kpi.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.comm.business.TimePeriodBO;
import com.fhd.comm.entity.TimePeriod;
import com.fhd.kpi.entity.Kpi;
import com.fhd.kpi.entity.KpiGatherResult;

@Service
public class KpiGatherResultWeekBO {

	@Autowired
    private KpiGatherResultBO o_kpiGatherResultBO;
	
	@Autowired
	private KpiGatherReultUtilsBO o_kpiGatherReultUtilsBO;
	
	@Autowired
    private KpiBO o_kpiBO;
	
	@Autowired
	private TimePeriodBO o_timePeriodBO;
	
	public String getParams(String params, String kpiId){
		//查询该指标全部数据存放到MAP中
		HashMap<String, String> kpiGatherResultMap = null;
    	//月
		List<KpiGatherResult> list = new ArrayList<KpiGatherResult>();
		
		String[] whereIn = null;
		
		String monthKey = "";
		String monthTargetValue = "";
		String monthFinishValue = "";
		String monthAssessmentValue = "";
		
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
		if(params.toString().indexOf("w") != -1){
			//点击的是周
			year = Integer.parseInt(params.toString().split(":")[0].toString().replace("[{\"", "").replace("\"", "").split("w")[0].toString());

			kpiGatherResultMap = o_kpiGatherResultBO.findMapKpiGatherResultListByKpiId(kpiId, String.valueOf(year));
			kpiGatherResultMap = o_kpiGatherReultUtilsBO.filterMap(kpiGatherResultMap);
			
			yearKey = String.valueOf(year);
			
			TimePeriod timePeriod = o_timePeriodBO.findTimePeriodById(key);
			//得到是几月
			List<TimePeriod> timePeriodList = o_timePeriodBO.findTimePeriodByParentId(timePeriod.getParent().getId());
			monthKey = timePeriod.getParent().getId();
			
			
			
			List<String> lists = new ArrayList<String>();
			for (TimePeriod timePeriod2 : timePeriodList) {
				lists.add(timePeriod2.getId());
			}
			
			list = o_kpiGatherResultBO.findKpiGatherResultListByTimePeriodId(kpiId, lists.toArray());
			
			//单条修改
			kpiGatherResultMap.put(key, key + "," + finishValue + "," + targetValue + "," + assessmentValue);
			
			if(list.size() >= (timePeriodList.size() - 1)){
				//求季度
				List<Double> finishValueList = null;
				List<Double> targetValueList = null;
				List<Double> assessmentValueList = null;
				
				if(list.size() < ((timePeriodList.size() - 1) + 1)){
					list = o_kpiGatherReultUtilsBO.addKpiGatherResultList(list, kpiGatherResultMap, key);
				}
				
				finishValueList = o_kpiGatherReultUtilsBO.addFinishValueList(list);
				targetValueList = o_kpiGatherReultUtilsBO.addTargetValueList(list);
				assessmentValueList = o_kpiGatherReultUtilsBO.addAssessmentValueList(list);
				
				//得到指标实体
				Kpi kpi = o_kpiBO.findKpiById(kpiId);
				
				//得到季度汇总
				monthTargetValue = o_kpiGatherReultUtilsBO.getTargetValue(kpi, targetValueList);
				monthFinishValue = o_kpiGatherReultUtilsBO.getFinishValue(kpi, finishValueList);
				monthAssessmentValue = o_kpiGatherReultUtilsBO.getAssessmentValue(kpi, assessmentValueList);
				
				//季度
				kpiGatherResultMap.put(monthKey, monthKey + "," + monthFinishValue + "," + monthTargetValue + "," + monthAssessmentValue);
				
				//得到年度汇总
				whereIn = new String[12];
				for (int i = 0; i < whereIn.length; i++) {
					int months = i + 1;
					whereIn[i] = year + "mm" + o_kpiGatherReultUtilsBO.getMonth(String.valueOf(months)); 
				}
				
				list = o_kpiGatherResultBO.findKpiGatherResultListByTimePeriodId(kpiId, whereIn);
				
				if(list.size() >= 2 ){
					finishValueList = o_kpiGatherReultUtilsBO.addFinishValueList(list);
					targetValueList = o_kpiGatherReultUtilsBO.addTargetValueList(list);
					assessmentValueList = o_kpiGatherReultUtilsBO.addAssessmentValueList(list);
					
	    			yearTargetValue = o_kpiGatherReultUtilsBO.getTargetValue(kpi, targetValueList);
	    			yearFinishValue = o_kpiGatherReultUtilsBO.getFinishValue(kpi, finishValueList);
	    			yearAssessmentValue = o_kpiGatherReultUtilsBO.getAssessmentValue(kpi, assessmentValueList);
	    			//年度
	    			kpiGatherResultMap.put(yearKey, yearKey + "," + yearFinishValue + "," + yearTargetValue + "," + yearAssessmentValue);
				}else{
					if(kpiGatherResultMap.get(yearKey+"mm02") != null){
						if(kpiGatherResultMap.get(yearKey+"mm03") == null){
							list = o_kpiGatherReultUtilsBO.addKpiGatherResultList(list, kpiGatherResultMap, yearKey+"mm02");
							
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