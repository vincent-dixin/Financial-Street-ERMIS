package com.fhd.kpi.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.kpi.entity.Kpi;
import com.fhd.kpi.entity.KpiGatherResult;

@Service
public class KpiGatherResultHalfYearBO {

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
		if(params.toString().indexOf("hf") != -1){
			//点击的是周
			year = Integer.parseInt(params.toString().split(":")[0].toString().replace("[{\"", "").replace("\"", "").split("hf")[0].toString());
		
		
			kpiGatherResultMap = o_kpiGatherResultBO.findMapKpiGatherResultListByKpiId(kpiId, String.valueOf(year));
			kpiGatherResultMap = o_kpiGatherReultUtilsBO.filterMap(kpiGatherResultMap);
			
			yearKey = String.valueOf(year);
			
			
			whereIn = new String[2];
			whereIn[0] = year + "hf0";
			whereIn[1] = year + "hf1";
			list = o_kpiGatherResultBO.findKpiGatherResultListByTimePeriodId(kpiId, whereIn);
			
			//单条修改
			kpiGatherResultMap.put(key, key + "," + finishValue + "," + targetValue + "," + assessmentValue);
			
			//得到指标实体
			Kpi kpi = o_kpiBO.findKpiById(kpiId);
			
			List<Double> finishValueList = null;
			List<Double> targetValueList = null;
			List<Double> assessmentValueList = null;
			
			if(list.size() >= 1){
				if(list.size() < 2){
					list = o_kpiGatherReultUtilsBO.addKpiGatherResultList(list, kpiGatherResultMap, key);
				}
				
				finishValueList = o_kpiGatherReultUtilsBO.addFinishValueList(list);
				targetValueList = o_kpiGatherReultUtilsBO.addTargetValueList(list);
				assessmentValueList = o_kpiGatherReultUtilsBO.addAssessmentValueList(list);
				
    			yearTargetValue = o_kpiGatherReultUtilsBO.getTargetValue(kpi, targetValueList);
    			yearFinishValue = o_kpiGatherReultUtilsBO.getFinishValue(kpi, finishValueList);
    			yearAssessmentValue = o_kpiGatherReultUtilsBO.getAssessmentValue(kpi, assessmentValueList);
    			//年度
    			kpiGatherResultMap.put(yearKey, yearKey + "," + yearFinishValue + "," + yearTargetValue + "," + yearAssessmentValue);
			}else{
				if(kpiGatherResultMap.get(yearKey+"hf0") != null){
					if(kpiGatherResultMap.get(yearKey+"hf1") != null){
						list = o_kpiGatherReultUtilsBO.addKpiGatherResultList(list, kpiGatherResultMap, yearKey+"hf1");
						
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
		
    	return params;
	}
}