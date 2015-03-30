package com.fhd.kpi.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.kpi.entity.Kpi;
import com.fhd.kpi.entity.KpiGatherResult;

@Service
public class KpiGatherResultQuarterBO {


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
		if(params.toString().indexOf("Q") != -1){
			//点击的是季度
			year = Integer.parseInt(params.toString().split(":")[0].toString().replace("[{\"", "").replace("\"", "").split("Q")[0].toString());
		
			kpiGatherResultMap = o_kpiGatherResultBO.findMapKpiGatherResultListByKpiId(kpiId, String.valueOf(year));
			kpiGatherResultMap = o_kpiGatherReultUtilsBO.filterMap(kpiGatherResultMap);
			
			yearKey = String.valueOf(year);
			
			//单条修改
			kpiGatherResultMap.put(key, key + "," + finishValue + "," + targetValue + "," + assessmentValue);
			
			//求季度
			List<Double> finishValueList = null;
			List<Double> targetValueList = null;
			List<Double> assessmentValueList = null;
			
			//得到指标实体
			Kpi kpi = o_kpiBO.findKpiById(kpiId);
			
			//得到年度汇总
			whereIn = new String[4];
			whereIn[0] = year + "Q1";
			whereIn[1] = year + "Q2";
			whereIn[2] = year + "Q3";
			whereIn[3] = year + "Q4";
			list = o_kpiGatherResultBO.findKpiGatherResultListByTimePeriodId(kpiId, whereIn);
			
			if(list.size() >= 3 ){
				if(list.size() < 4){
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
			}
				
			params = o_kpiGatherReultUtilsBO.getMapToParams(kpiGatherResultMap, params);
		}
    	return params;
	}
}