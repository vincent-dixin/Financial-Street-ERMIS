package com.fhd.kpi.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.kpi.entity.Kpi;
import com.fhd.kpi.entity.KpiGatherResult;

@Service
public class KpiGatherReultUtilsBO {

	@Autowired
    private KpiGatherResultBO o_kpiGatherResultBO;
	
	public String getTargetValue(Kpi kpi, List<Double> targetValueList){
		String targetValue = "";
		//目标值
		if(kpi.getTargetSumMeasure().getId().equalsIgnoreCase("kpi_sum_measure_avg")){
			targetValue = o_kpiGatherResultBO.calculateAccumulateValue(targetValueList, "kpi_sum_measure_avg").toString();
		}if(kpi.getTargetSumMeasure().getId().equalsIgnoreCase("kpi_sum_measure_max")){
			targetValue = o_kpiGatherResultBO.calculateAccumulateValue(targetValueList, "kpi_sum_measure_max").toString();
		}if(kpi.getTargetSumMeasure().getId().equalsIgnoreCase("kpi_sum_measure_min")){
			targetValue = o_kpiGatherResultBO.calculateAccumulateValue(targetValueList, "kpi_sum_measure_min").toString();
		}if(kpi.getTargetSumMeasure().getId().equalsIgnoreCase("kpi_sum_measure_first")){
			targetValue = o_kpiGatherResultBO.calculateAccumulateValue(targetValueList, "kpi_sum_measure_first").toString();
		}if(kpi.getTargetSumMeasure().getId().equalsIgnoreCase("kpi_sum_measure_last")){
			targetValue = o_kpiGatherResultBO.calculateAccumulateValue(targetValueList, "kpi_sum_measure_last").toString();
		}if(kpi.getTargetSumMeasure().getId().equalsIgnoreCase("kpi_sum_measure_sum")){
			targetValue = o_kpiGatherResultBO.calculateAccumulateValue(targetValueList, "kpi_sum_measure_sum").toString();
		}
		
		return targetValue;
	}
	
	public String getFinishValue(Kpi kpi, List<Double> finishValueList){
		String finishValue = "";
		//实际值
		if(kpi.getResultSumMeasure().getId().equalsIgnoreCase("kpi_sum_measure_avg")){
			finishValue = o_kpiGatherResultBO.calculateAccumulateValue(finishValueList, "kpi_sum_measure_avg").toString();
		}if(kpi.getResultSumMeasure().getId().equalsIgnoreCase("kpi_sum_measure_max")){
			finishValue = o_kpiGatherResultBO.calculateAccumulateValue(finishValueList, "kpi_sum_measure_max").toString();
		}if(kpi.getResultSumMeasure().getId().equalsIgnoreCase("kpi_sum_measure_min")){
			finishValue = o_kpiGatherResultBO.calculateAccumulateValue(finishValueList, "kpi_sum_measure_min").toString();
		}if(kpi.getResultSumMeasure().getId().equalsIgnoreCase("kpi_sum_measure_first")){
			finishValue = o_kpiGatherResultBO.calculateAccumulateValue(finishValueList, "kpi_sum_measure_first").toString();
		}if(kpi.getResultSumMeasure().getId().equalsIgnoreCase("kpi_sum_measure_last")){
			finishValue = o_kpiGatherResultBO.calculateAccumulateValue(finishValueList, "kpi_sum_measure_last").toString();
		}if(kpi.getResultSumMeasure().getId().equalsIgnoreCase("kpi_sum_measure_sum")){
			finishValue = o_kpiGatherResultBO.calculateAccumulateValue(finishValueList, "kpi_sum_measure_sum").toString();
		}
		
		return finishValue;
	}
	
	public String getAssessmentValue(Kpi kpi, List<Double> assessmentValueList){
		String assessmentValue = "";
		//评估值
		if(kpi.getAssessmentSumMeasure().getId().equalsIgnoreCase("kpi_sum_measure_avg")){
			assessmentValue = o_kpiGatherResultBO.calculateAccumulateValue(assessmentValueList, "kpi_sum_measure_avg").toString();
		}if(kpi.getAssessmentSumMeasure().getId().equalsIgnoreCase("kpi_sum_measure_max")){
			assessmentValue = o_kpiGatherResultBO.calculateAccumulateValue(assessmentValueList, "kpi_sum_measure_max").toString();
		}if(kpi.getAssessmentSumMeasure().getId().equalsIgnoreCase("kpi_sum_measure_min")){
			assessmentValue = o_kpiGatherResultBO.calculateAccumulateValue(assessmentValueList, "kpi_sum_measure_min").toString();
		}if(kpi.getAssessmentSumMeasure().getId().equalsIgnoreCase("kpi_sum_measure_first")){
			assessmentValue = o_kpiGatherResultBO.calculateAccumulateValue(assessmentValueList, "kpi_sum_measure_first").toString();
		}if(kpi.getAssessmentSumMeasure().getId().equalsIgnoreCase("kpi_sum_measure_last")){
			assessmentValue = o_kpiGatherResultBO.calculateAccumulateValue(assessmentValueList, "kpi_sum_measure_last").toString();
		}if(kpi.getAssessmentSumMeasure().getId().equalsIgnoreCase("kpi_sum_measure_sum")){
			assessmentValue = o_kpiGatherResultBO.calculateAccumulateValue(assessmentValueList, "kpi_sum_measure_sum").toString();
		}
		
		return assessmentValue;
	}
	
	public String getMapToParams(HashMap<String, String> kpiGatherResultMap, String params){
		params = "";
		String str = null;
		Object object[] = kpiGatherResultMap.keySet().toArray();
		for(int i = 0; i < kpiGatherResultMap.size(); i++) {
			str = kpiGatherResultMap.get(object[i]);
			String strs[] = str.split(",");
			String mapFinshValue = "";
			String mapTargetValue = "";
			String mapAssessmentValue = "";
			
//			if(strs.length >= 2){
//				if(strs.length == 2){
//					if(strs[1] != null && !strs[1].equalsIgnoreCase("null")){
//						mapFinshValue = String.valueOf(strs[1]);
//					}
//				}else{
//					if(strs[1] != null && !strs[1].equalsIgnoreCase("null")){
//						mapFinshValue = String.valueOf(strs[1]);
//					}if(strs[2] != null && !strs[2].equalsIgnoreCase("null")){
//						mapTargetValue = String.valueOf(strs[2]);
//					}if(strs[3] != null && !strs[3].equalsIgnoreCase("null")){
//						mapAssessmentValue = String.valueOf(strs[3]);
//					}
//				}
//			}
			
			
			if(strs.length == 4){
				//3项值全部输入
				mapFinshValue = String.valueOf(strs[1]);
				mapTargetValue = String.valueOf(strs[2]);
				mapAssessmentValue = String.valueOf(strs[3]);
			}else if(strs.length == 2){
				//实际值输入
				mapFinshValue = String.valueOf(strs[1]);
				mapTargetValue = String.valueOf("");
				mapAssessmentValue = String.valueOf("");
			}else if(strs.length == 1){
				//3项值全部清空
				mapFinshValue = String.valueOf("");
				mapTargetValue = String.valueOf("");
				mapAssessmentValue = String.valueOf("");
			}else if(strs.length == 3){
				//目标值输入
				if(strs[1].equalsIgnoreCase("")){
					mapFinshValue = String.valueOf("");
				}else{
					mapFinshValue = String.valueOf(strs[1]);
				}
				
				mapTargetValue = String.valueOf(strs[2]);
				mapAssessmentValue = String.valueOf("");
			}
			
			
			
			
			params += "{\"" + strs[0] + "\":\"" + mapFinshValue + "," + 
					mapTargetValue + "," + mapAssessmentValue + "\"},";
		}
		
		params = params + "|";
		params = params.replace(",|", "");
		params = "[" + params + "]";
		
		return params;
	}
	
	public List<Double> addTargetValueList(List<KpiGatherResult> list){
		List<Double> targetValueList = new ArrayList<Double>();
		for (KpiGatherResult kpiGatherResult : list) {
			targetValueList.add(kpiGatherResult.getTargetValue());//存放目标值
		}
		
		return targetValueList;
	}
	
	public List<Double> addFinishValueList(List<KpiGatherResult> list){
		List<Double> finishValueList = new ArrayList<Double>();
		for (KpiGatherResult kpiGatherResult : list) {
			finishValueList.add(kpiGatherResult.getFinishValue());//存放实际值
		}
		
		return finishValueList;
	}
	
	public List<Double> addAssessmentValueList(List<KpiGatherResult> list){
		List<Double> assessmentValueList = new ArrayList<Double>();
		for (KpiGatherResult kpiGatherResult : list) {
			assessmentValueList.add(kpiGatherResult.getAssessmentValue());//存放评估值
		}
		
		return assessmentValueList;
	}
	
	public List<KpiGatherResult> addKpiGatherResultList(List<KpiGatherResult> list, HashMap<String, String> kpiGatherResultMap, String key){
		KpiGatherResult kpiGatherResult = new KpiGatherResult();

		
		if(kpiGatherResultMap.get(key).split(",").length == 4){
			//3项值全部输入
			if(StringUtils.isBlank(kpiGatherResultMap.get(key).split(",")[1])){
				kpiGatherResult.setFinishValue(null);
			}else{
				kpiGatherResult.setFinishValue(Double.parseDouble(kpiGatherResultMap.get(key).split(",")[1]));
			}
			
			if(StringUtils.isBlank(kpiGatherResultMap.get(key).split(",")[2])){
				kpiGatherResult.setTargetValue(null);
			}else{
				kpiGatherResult.setTargetValue(Double.parseDouble(kpiGatherResultMap.get(key).split(",")[2]));
			}
			kpiGatherResult.setAssessmentValue(Double.parseDouble(kpiGatherResultMap.get(key).split(",")[3]));
		}else if(kpiGatherResultMap.get(key).split(",").length == 2){
			//实际值输入
			kpiGatherResult.setFinishValue(Double.parseDouble(kpiGatherResultMap.get(key).split(",")[1]));
			kpiGatherResult.setTargetValue(null);
			kpiGatherResult.setAssessmentValue(null);
		}else if(kpiGatherResultMap.get(key).split(",").length == 1){
			//3项值全部清空
			kpiGatherResult.setFinishValue(null);
			kpiGatherResult.setTargetValue(null);
			kpiGatherResult.setAssessmentValue(null);
		}else if(kpiGatherResultMap.get(key).split(",").length == 3){
			//目标值输入
			kpiGatherResult.setFinishValue(null);
			kpiGatherResult.setTargetValue(Double.parseDouble(kpiGatherResultMap.get(key).split(",")[2]));
			kpiGatherResult.setAssessmentValue(null);
		}
		
		
//		if(kpiGatherResultMap.get(key).split(",").length >= 2){
//			if(kpiGatherResultMap.get(key).split(",").length == 2){
//				kpiGatherResult.setFinishValue(Double.parseDouble(kpiGatherResultMap.get(key).split(",")[1]));
//				kpiGatherResult.setTargetValue(null);
//				kpiGatherResult.setAssessmentValue(null);
//			}else{
//				if(StringUtils.isBlank(kpiGatherResultMap.get(key).split(",")[1])){
//					kpiGatherResult.setFinishValue(null);
//				}else{
//					kpiGatherResult.setFinishValue(Double.parseDouble(kpiGatherResultMap.get(key).split(",")[1]));
//				}
//				
//				if(StringUtils.isBlank(kpiGatherResultMap.get(key).split(",")[2])){
//					kpiGatherResult.setFinishValue(null);
//				}else{
//					kpiGatherResult.setTargetValue(Double.parseDouble(kpiGatherResultMap.get(key).split(",")[2]));
//				}
//				
//				if(StringUtils.isBlank(kpiGatherResultMap.get(key).split(",")[3])){
//					kpiGatherResult.setFinishValue(null);
//				}else{
//					kpiGatherResult.setAssessmentValue(Double.parseDouble(kpiGatherResultMap.get(key).split(",")[3]));
//				}
//			}
//		}else{
//			kpiGatherResult.setFinishValue(null);
//			kpiGatherResult.setTargetValue(null);
//			kpiGatherResult.setAssessmentValue(null);
//		}
		
		
		
		list.add(kpiGatherResult);
		
		return list;
	}
	
	public String getMonth(String month){
		if(Integer.parseInt(month) < 10){
			month = "0" + month;
		}
		
		return month; 
	}
	
	public HashMap<String, String> filterMap(HashMap<String, String> kpiGatherResultMap){
		String str = null;
		List<String> strList = new ArrayList<String>();
		Object object[] = kpiGatherResultMap.keySet().toArray();
		for(int i = 0; i < kpiGatherResultMap.size(); i++) {
			str = kpiGatherResultMap.get(object[i]);
			String strs[] = str.split(",");
			String mapFinshValue = strs[1];
			
			if("null".equalsIgnoreCase(mapFinshValue)){
				strList.add(strs[0]);
			}
		}
		
		for (String key : strList) {
			kpiGatherResultMap.remove(key);
		}
		
		return kpiGatherResultMap;
	}
}