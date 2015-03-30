package com.fhd.comm.business.formula;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.comm.business.CategoryBO;
import com.fhd.comm.entity.Category;
import com.fhd.comm.interfaces.IFormulaCalculateBO;
import com.fhd.fdc.utils.Contents;
import com.fhd.kpi.business.KpiBO;
import com.fhd.kpi.business.KpiGatherResultBO;
import com.fhd.kpi.business.RelaAssessResultBO;
import com.fhd.kpi.business.StrategyMapBO;
import com.fhd.kpi.entity.KpiGatherResult;
import com.fhd.kpi.entity.RelaAssessResult;
import com.fhd.kpi.entity.StrategyMap;

@Service
public class FormulaCalculateBO implements IFormulaCalculateBO {

    @Autowired
    private FunctionCalculateBO o_functionCalculateBO;
    @Autowired
    private KpiGatherResultBO o_kpiGatherResultBO;
    @Autowired
    private RelaAssessResultBO o_relaAssessResultBO;
    @Autowired
    private CategoryBO o_categroyBO;
    @Autowired
    private StrategyMapBO o_strategyMapBO;
    @Autowired
    private KpiBO o_kpiBO;

    /**
     * 计算公式.
     * @param targetName 对象名称
     * @param type 类型:kpi/risk
     * @param formula 公式内容
     * @param timePeriodId 时间区间维id
     * @return String
     */
    public String calculate(String targetName, String type, String formula, String timePeriodId) {
        if(StringUtils.isBlank(formula)){
            return "公式为空";
        }
        if (StringUtils.isNotBlank(formula)) {
            formula = formula.replaceAll("\\$", "");
        }
        DecimalFormat df = new DecimalFormat("0.00");
        if (Contents.OBJECT_KPI.equals(type)) {
            /*
             * 指标公式的解析计算
             */
        	if(formula.indexOf("prior") != -1){
        		String tempFormula = formula;
        		String formulaBefore = "";
        		String formulaMid = "";
        		String midTemp = "";
        		String formulaAfter ="";
        		int temp=tempFormula.indexOf("prior");
            	while(temp!=-1){
            		formulaBefore = tempFormula.substring(0, temp);
            		//确定函数的右括号位置
            		String otherFormula = tempFormula.substring(temp,tempFormula.length());
            		formulaMid = tempFormula.substring(temp, temp+otherFormula.indexOf(")")+1);
            		midTemp = calculatePriorFun(targetName, formulaMid, timePeriodId);
            		if(!isDoubleOrInteger(midTemp)){
            			return midTemp;
            		}
            		formulaAfter = tempFormula.substring(temp+otherFormula.indexOf(")")+1, tempFormula.length());
            		tempFormula = formulaBefore + midTemp + formulaAfter;
            		temp = tempFormula.indexOf("prior");
            	}
            	formula = tempFormula;
        	}
        	if(formula.indexOf("kpiStatusLevel") != -1){
        		String tempFormula = formula;
        		String formulaBefore = "";
        		String formulaMid = "";
        		String midTemp = "";
        		String formulaAfter ="";
        		int temp=tempFormula.indexOf("kpiStatusLevel");
        		while(temp!=-1){
        			formulaBefore = tempFormula.substring(0, temp);
        			//确定函数的右括号位置
        			String otherFormula = tempFormula.substring(temp,tempFormula.length());
        			formulaMid = tempFormula.substring(temp, temp+otherFormula.indexOf(")")+1);
        			midTemp = calculateKpiStatusLevelFun(targetName, formulaMid, timePeriodId);
        			if(!isDoubleOrInteger(midTemp)){
        				return midTemp;
        			}
        			formulaAfter = tempFormula.substring(temp+otherFormula.indexOf(")")+1, tempFormula.length());
        			tempFormula = formulaBefore + midTemp + formulaAfter;
        			temp = tempFormula.indexOf("kpiStatusLevel");
        		}
        		formula = tempFormula;
        	}
        	if(formula.indexOf("isKpiStatus") != -1){
        		String tempFormula = formula;
        		String formulaBefore = "";
        		String formulaMid = "";
        		String midTemp = "";
        		String formulaAfter ="";
        		int temp=tempFormula.indexOf("isKpiStatus");
            	while(temp!=-1){
            		formulaBefore = tempFormula.substring(0, temp);
            		//确定函数的右括号位置
            		String otherFormula = tempFormula.substring(temp,tempFormula.length());
            		formulaMid = tempFormula.substring(temp, temp+otherFormula.indexOf(")")+1);
            		midTemp = calculateIsKpiStatusFun(targetName, formulaMid, timePeriodId);
            		if(!isDoubleOrInteger(midTemp)){
            			return midTemp;
            		}
            		formulaAfter = tempFormula.substring(temp+otherFormula.indexOf(")")+1, tempFormula.length());
            		tempFormula = formulaBefore + midTemp + formulaAfter;
            		temp = tempFormula.indexOf("isKpiStatus");
            	}
            	formula = tempFormula;
        	}
        	List<String> nameList = new ArrayList<String>();
            List<Map<String, String>> decompositionFormulaResult = decompositionFormulaString(targetName, formula);
            for (Map<String, String> formulaMap : decompositionFormulaResult) {
                String name = formulaMap.get("name");
                if (!nameList.contains(name)) {
                    nameList.add(name);
                }
            }
            if (null != nameList && nameList.size() > 0) {
                /*
                 * 根据指标名称批量查询对应的指标采集结果list
                 * @param nameList
                 */
                List<KpiGatherResult> kpiGatherResultList = o_kpiGatherResultBO.findKpiGatherResultListByKpiNames(nameList, timePeriodId);
                if (null != kpiGatherResultList && kpiGatherResultList.size() > 0) {
                    for (KpiGatherResult kpiGatherResult : kpiGatherResultList) {
                        for (Map<String, String> formulaMap : decompositionFormulaResult) {
                            String name = formulaMap.get("name");
                            String vType = formulaMap.get("type");
                            String originalName = formulaMap.get("originalName");
                            if (kpiGatherResult.getKpi().getName().equals(name)) {
                                Double value = 0.0;
                                if (Contents.TARGET_VALUE.equals(vType)) {
                                    //目标值
                                    if (null != kpiGatherResult.getTargetValue()) {
                                        value = kpiGatherResult.getTargetValue();
                                    }else {
                                        return "指标[" + name + "]的目标值不存在!";
                                    }
                                }else if (Contents.RESULT_VALUE.equals(vType)) {
                                    //实际值
                                    if (null != kpiGatherResult.getFinishValue()) {
                                        value = kpiGatherResult.getFinishValue();
                                    }else {
                                        return "指标[" + name + "]的结果值不存在!";
                                    }
                                }else if (Contents.ASSEMENT_VALUE.equals(vType)) {
                                    //评估值
                                    if (null != kpiGatherResult.getAssessmentValue()) {
                                        value = kpiGatherResult.getAssessmentValue();
                                    }else {
                                        return "指标[" + name + "]的评估值不存在!";
                                    }
                                }else if (Contents.MODEL_VALUE.equals(vType)) {
                                    //标杆值
                                    if (null != kpiGatherResult.getKpi().getModelValue()) {
                                        value = kpiGatherResult.getKpi().getModelValue();
                                    }else {
                                        return "指标[" + name + "]的标杆值不存在!";
                                    }
                                }else if (Contents.SAME_VALUE.equals(vType)) {
                                    //同比值
                                    if (null != kpiGatherResult.getSameValue()) {
                                        value = kpiGatherResult.getSameValue();
                                    }else {
                                        return "指标[" + name + "]的同比值不存在!";
                                    }
                                }else if (Contents.RATIO_VALUE.equals(vType)) {
                                    //环比值
                                    if (null != kpiGatherResult.getRatioValue()) {
                                        value = kpiGatherResult.getRatioValue();
                                    }else {
                                        return "指标[" + name + "]的环比值不存在!";
                                    }
                                }

                                if (value >= 0) {
                                    //如果采集结果是正数
                                    formula = formula.replace(originalName, df.format(value).replace(",", ""));
                                }else if (value < 0) {
                                    //如果采集结果是负数,要把数字括起来
                                    formula = formula.replace(originalName, "(" + df.format(value).replace(",", "") + ")");
                                }
                            }
                        }
                    }
                }
            }
        }else if(Contents.OBJECT_CATEGORY.equals(type)){
        	/*
             * 记分卡公式的解析计算
             */
        	if(formula.indexOf("subSumKpiStatus") != -1){
        		String tempFormula = formula;
        		String formulaBefore = "";
        		String formulaMid = "";
        		String midTemp = "";
        		String formulaAfter ="";
        		int temp=tempFormula.indexOf("subSumKpiStatus");
            	while(temp!=-1){
            		formulaBefore = tempFormula.substring(0, temp);
            		//确定函数的右括号位置
            		String otherFormula = tempFormula.substring(temp,tempFormula.length());
            		formulaMid = tempFormula.substring(temp, temp+otherFormula.indexOf(")")+1);
            		midTemp = calculateSubSumKpiStatusFun(targetName, type, formulaMid, timePeriodId);
            		if(!isDoubleOrInteger(midTemp)){
            			return midTemp;
            		}
            		formulaAfter = tempFormula.substring(temp+otherFormula.indexOf(")")+1, tempFormula.length());
            		tempFormula = formulaBefore + midTemp + formulaAfter;
            		temp = tempFormula.indexOf("subSumKpiStatus");
            	}
            	formula = tempFormula;
        	}
        	if(formula.indexOf("subSumScStatus") != -1){
        		String tempFormula = formula;
        		String formulaBefore = "";
        		String formulaMid = "";
        		String midTemp = "";
        		String formulaAfter ="";
        		int temp=tempFormula.indexOf("subSumScStatus");
            	while(temp!=-1){
            		formulaBefore = tempFormula.substring(0, temp);
            		//确定函数的右括号位置
            		String otherFormula = tempFormula.substring(temp,tempFormula.length());
            		formulaMid = tempFormula.substring(temp, temp+otherFormula.indexOf(")")+1);
            		midTemp = calculateSubSumScStatusFun(targetName, type, formulaMid, timePeriodId);
            		if(!isDoubleOrInteger(midTemp)){
            			return midTemp;
            		}
            		formulaAfter = tempFormula.substring(temp+otherFormula.indexOf(")")+1, tempFormula.length());
            		tempFormula = formulaBefore + midTemp + formulaAfter;
            		temp = tempFormula.indexOf("subSumScStatus");
            	}
            	formula = tempFormula;
        	}
        	if(formula.indexOf("isKpiStatus") != -1){
        		String tempFormula = formula;
        		String formulaBefore = "";
        		String formulaMid = "";
        		String midTemp = "";
        		String formulaAfter ="";
        		int temp=tempFormula.indexOf("isKpiStatus");
            	while(temp!=-1){
            		formulaBefore = tempFormula.substring(0, temp);
            		//确定函数的右括号位置
            		String otherFormula = tempFormula.substring(temp,tempFormula.length());
            		formulaMid = tempFormula.substring(temp, temp+otherFormula.indexOf(")")+1);
            		midTemp = calculateIsKpiStatusFun(targetName, formulaMid, timePeriodId);
            		if(!isDoubleOrInteger(midTemp)){
            			return midTemp;
            		}
            		formulaAfter = tempFormula.substring(temp+otherFormula.indexOf(")")+1, tempFormula.length());
            		tempFormula = formulaBefore + midTemp + formulaAfter;
            		temp = tempFormula.indexOf("isKpiStatus");
            	}
            	formula = tempFormula;
        	}
        	if(formula.indexOf("isScStatus") != -1){
        		String tempFormula = formula;
        		String formulaBefore = "";
        		String formulaMid = "";
        		String midTemp = "";
        		String formulaAfter ="";
        		int temp=tempFormula.indexOf("isScStatus");
            	while(temp!=-1){
            		formulaBefore = tempFormula.substring(0, temp);
            		//确定函数的右括号位置
            		String otherFormula = tempFormula.substring(temp,tempFormula.length());
            		formulaMid = tempFormula.substring(temp, temp+otherFormula.indexOf(")")+1);
            		midTemp = calculateIsScStatusFun(targetName, formulaMid, timePeriodId);
            		if(!isDoubleOrInteger(midTemp)){
            			return midTemp;
            		}
            		formulaAfter = tempFormula.substring(temp+otherFormula.indexOf(")")+1, tempFormula.length());
            		tempFormula = formulaBefore + midTemp + formulaAfter;
            		temp = tempFormula.indexOf("isScStatus");
            	}
            	formula = tempFormula;
        	}
        }else if(Contents.OBJECT_STRATEGY.equals(type)){
        	/*
             * TODO战略目标公式的解析计算
             */
        	
        }
        else if (Contents.OBJECT_RISK.equals(type)) {
            /*
             * TODO风险公式的解析计算
             */

        }
        System.out.println("替换后的formula="+formula);
        return o_functionCalculateBO.calculate(o_functionCalculateBO.strCast(formula));
    }

    /**
     * subSumKpiStatus函数解析.
     * @param targetName 对象名称
     * @param type 计算对象类型: category-记分卡/strategy-战略目标
     * @param subFormula sub函数内容
     * @param timePeriod 时间区间维id
     * @return String subSumKpiStatus函数计算结果
     */
    public String calculateSubSumKpiStatusFun(String targetName, String type, String subFormula, String timePeriod){
    	if(Contents.OBJECT_CATEGORY.equals(type)){
            String funContent = subFormula.substring(subFormula.indexOf("(") + 1, subFormula.indexOf(")"));
            String[] contentArray = funContent.split(",");
            
            int statusConut = 0;
            if (null != contentArray && contentArray.length == 2) {
                List<KpiGatherResult> kpiGatherResultList = o_kpiGatherResultBO.findKpiGatherResultListBySome(targetName, timePeriod);
                if (null != kpiGatherResultList && kpiGatherResultList.size() > 0) {
                    for (KpiGatherResult kpiGatherResult : kpiGatherResultList) {
                        if (null != kpiGatherResult.getAssessmentStatus()) {
                            if("green".equals(contentArray[1]) && "0alarm_startus_safe".equals(kpiGatherResult.getAssessmentStatus().getId())){
                            	statusConut++;
                            }else if("red".equals(contentArray[1]) && "0alarm_startus_h".equals(kpiGatherResult.getAssessmentStatus().getId())){
                            	statusConut++;
                            }else if("orange".equals(contentArray[1]) && "0alarm_startus_m".equals(kpiGatherResult.getAssessmentStatus().getId())){
                            	statusConut++;
                            }else if("yellow".equals(contentArray[1]) && "0alarm_startus_l".equals(kpiGatherResult.getAssessmentStatus().getId())){
                            	statusConut++;
                            }
                        }else{
                        	return targetName+":状态灯不存在";
                        }
                    }
                }
            }
            return String.valueOf(statusConut);
    	}else if(Contents.OBJECT_STRATEGY.equals(type)){
    		//TODO战略目标暂不使用
    	}
    	return "";
    }
    
    /**
     * subSumScStatus函数解析.
     * @param targetName 对象名称
     * @param type 计算对象类型: category-记分卡/strategy-战略目标
     * @param subFormula sub函数内容
     * @param timePeriod 时间区间维id
     * @return String subSumScStatus函数计算结果
     */
    public String calculateSubSumScStatusFun(String targetName, String type, String subFormula, String timePeriod){
    	int statusConut = 0;
    	if(Contents.OBJECT_CATEGORY.equals(type)){
            String funContent = subFormula.substring(subFormula.indexOf("(") + 1, subFormula.indexOf(")"));
            String[] contentArray = funContent.split(",");
            
            if (null != contentArray && contentArray.length == 2) {
                List<RelaAssessResult> relaAssessResultList = o_relaAssessResultBO.findCategoryAssessResultListBySome(targetName, timePeriod);
                if (null != relaAssessResultList && relaAssessResultList.size() > 0) {
                    for (RelaAssessResult relaAssessResult : relaAssessResultList) {
                        if (null != relaAssessResult.getAssessmentStatus()) {
                            if("green".equals(contentArray[1]) && "0alarm_startus_safe".equals(relaAssessResult.getAssessmentStatus().getId())){
                            	statusConut++;
                            }else if("red".equals(contentArray[1]) && "0alarm_startus_h".equals(relaAssessResult.getAssessmentStatus().getId())){
                            	statusConut++;
                            }else if("orange".equals(contentArray[1]) && "0alarm_startus_m".equals(relaAssessResult.getAssessmentStatus().getId())){
                            	statusConut++;
                            }else if("yellow".equals(contentArray[1]) && "0alarm_startus_l".equals(relaAssessResult.getAssessmentStatus().getId())){
                            	statusConut++;
                            }
                        }else{
                        	return targetName+":状态灯不存在";
                        }
                    }
                }
            }
    	}else if(Contents.OBJECT_STRATEGY.equals(type)){
    		//TODO战略目标暂不使用
    	}
    	return String.valueOf(statusConut);
    }
    
    /**
     * isKpiStatus函数解析.
     * @param targetName 对象名称
     * @param subFormula sub函数内容
     * @param timePeriod 时间区间维id
     * @return String isKpiStatus函数计算结果
     */
    public String calculateIsKpiStatusFun(String targetName, String subFormula, String timePeriod){
    	int statusConut = 0;
    	
    	String funContent = subFormula.substring(subFormula.indexOf("(") + 1, subFormula.indexOf(")"));
        String[] contentArray = funContent.split(",");
        
        if (null != contentArray && contentArray.length == 2) {
            KpiGatherResult kpiGatherResult = null;
        	String kpiName = contentArray[0].substring(contentArray[0].indexOf("#[")+2,contentArray[0].indexOf("]"));
        	String frequence = this.o_kpiBO.findKpiFrequenceByName(kpiName);
        	if("0frequecy_relatime".equals(frequence)){//实时指标查询最晚的采集结果
        	    kpiGatherResult = o_kpiGatherResultBO.findRelaTimeKpiGatherResultBySome(kpiName);
        	}else{
        	    kpiGatherResult = o_kpiGatherResultBO.findKpiGatherResultBySome(kpiName,timePeriod);
        	}
        	if (null != kpiGatherResult) {
                if (null != kpiGatherResult.getAssessmentStatus()) {
                    if("green".equals(contentArray[1]) && "0alarm_startus_safe".equals(kpiGatherResult.getAssessmentStatus().getId())){
                        statusConut++;
                    }else if("red".equals(contentArray[1]) && "0alarm_startus_h".equals(kpiGatherResult.getAssessmentStatus().getId())){
                        statusConut++;
                    }else if("orange".equals(contentArray[1]) && "0alarm_startus_m".equals(kpiGatherResult.getAssessmentStatus().getId())){
                        statusConut++;
                    }else if("yellow".equals(contentArray[1]) && "0alarm_startus_l".equals(kpiGatherResult.getAssessmentStatus().getId())){
                        statusConut++;
                    }
                }else{
                	return targetName+":状态灯不存在";
                }
            }
        }
        return String.valueOf(statusConut);
    }
    
    /**
     * isScStatus函数解析.
     * @param targetName 对象名称
     * @param subFormula sub函数内容
     * @param timePeriod 时间区间维id
     * @return String isScStatus函数计算结果
     */
    public String calculateIsScStatusFun(String targetName, String subFormula, String timePeriod){
    	int statusConut = 0;
    	
    	String funContent = subFormula.substring(subFormula.indexOf("(") + 1, subFormula.indexOf(")"));
        String[] contentArray = funContent.split(",");
        
        if (null != contentArray && contentArray.length == 2) {
        	
        	String categoryName = contentArray[0].substring(contentArray[0].indexOf("@[")+2,contentArray[0].indexOf("]"));
        	
        	RelaAssessResult relaAssessResult = o_relaAssessResultBO.findRelaAssessResultBySome(categoryName,timePeriod);
            if (null != relaAssessResult) {
                if (null != relaAssessResult.getAssessmentStatus()) {
                    if("green".equals(contentArray[1]) && "0alarm_startus_safe".equals(relaAssessResult.getAssessmentStatus().getId())){
                    	statusConut++;
                    }else if("red".equals(contentArray[1]) && "0alarm_startus_h".equals(relaAssessResult.getAssessmentStatus().getId())){
                    	statusConut++;
                    }else if("orange".equals(contentArray[1]) && "0alarm_startus_m".equals(relaAssessResult.getAssessmentStatus().getId())){
                    	statusConut++;
                    }else if("yellow".equals(contentArray[1]) && "0alarm_startus_l".equals(relaAssessResult.getAssessmentStatus().getId())){
                    	statusConut++;
                    }
                }else{
                	return targetName+":状态灯不存在";
                }
            }
        }
        return String.valueOf(statusConut);
    }
    
    /**
     * prior函数解析.
     * @param targetName 对象名称
     * @param subFormula sub函数内容
     * @param timePeriod 时间区间维id
     * @return String prior函数计算结果
     */
    public String calculatePriorFun(String targetName, String subFormula, String timePeriod){
    	Double value = null;
    	
    	String funContent = subFormula.substring(subFormula.indexOf("(") + 1, subFormula.indexOf(")"));
        String[] contentArray = funContent.split(",");
        
        if (null != contentArray && contentArray.length == 2) {
        	
        	String[] paramArray = apartTarget(contentArray[0], contentArray[0].indexOf("#[")).split("~");
        	
        	String kpiName = paramArray[0];
        	String vType = paramArray[1];
        	String periods = contentArray[1];
        	
            KpiGatherResult kpiGatherResult = o_kpiGatherResultBO.findPreKpiGatherResultBySome(kpiName, Integer.valueOf(periods), timePeriod);
            if (null != kpiGatherResult) {
               // if (null != kpiGatherResult.getAssessmentStatus()) {//金融街修改
                	if (Contents.TARGET_VALUE.equals(vType)) {
                        //目标值
                        if (null != kpiGatherResult.getTargetValue()) {
                            value = kpiGatherResult.getTargetValue();
                        }else {
                            return "指标[" + kpiName + "]的目标值不存在!";
                        }
                    }else if (Contents.RESULT_VALUE.equals(vType)) {
                        //实际值
                        if (null != kpiGatherResult.getFinishValue()) {
                            value = kpiGatherResult.getFinishValue();
                        }else {
                            return "指标[" + kpiName + "]的结果值不存在!";
                        }
                    }else if (Contents.ASSEMENT_VALUE.equals(vType)) {
                        //评估值
                        if (null != kpiGatherResult.getAssessmentValue()) {
                            value = kpiGatherResult.getAssessmentValue();
                        }else {
                            return "指标[" + kpiName + "]的评估值不存在!";
                        }
                    }else if (Contents.MODEL_VALUE.equals(vType)) {
                        //标杆值
                        if (null != kpiGatherResult.getKpi().getModelValue()) {
                            value = kpiGatherResult.getKpi().getModelValue();
                        }else {
                            return "指标[" + kpiName + "]的标杆值不存在!";
                        }
                    }else if (Contents.SAME_VALUE.equals(vType)) {
                        //同比值
                        if (null != kpiGatherResult.getSameValue()) {
                            value = kpiGatherResult.getSameValue();
                        }else {
                            return "指标[" + kpiName + "]的同比值不存在!";
                        }
                    }else if (Contents.RATIO_VALUE.equals(vType)) {
                        //环比值
                        if (null != kpiGatherResult.getRatioValue()) {
                            value = kpiGatherResult.getRatioValue();
                        }else {
                            return "指标[" + kpiName + "]的环比值不存在!";
                        }
                    }
               // }
            }
        }
        if(null!=value){
            return String.valueOf(value);
        }
        else{
            return null;
        }
    }
    
    /**
     * kpiStatusLevel函数解析.
     * @param targetName 对象名称
     * @param subFormula sub函数内容
     * @param timePeriod 时间区间维id
     * @return String kpiStatusLevel函数计算结果
     */
    public String calculateKpiStatusLevelFun(String targetName, String subFormula, String timePeriod){
    	Double value = null;
    	
    	String funContent = subFormula.substring(subFormula.indexOf("(") + 1, subFormula.indexOf(")"));
    	String[] contentArray = funContent.split(",");
    	
    	if (null != contentArray && contentArray.length == 2) {
    		
    		String kpiName = apartTarget(contentArray[0], contentArray[0].indexOf("#["));
    		String periods = contentArray[1];
    		KpiGatherResult kpiGatherResult = o_kpiGatherResultBO.findPreKpiGatherResultBySome(kpiName, Integer.valueOf(periods), timePeriod);
    		if (null != kpiGatherResult) {
    			if(null != kpiGatherResult.getAssessmentStatus()){
    				if("0alarm_startus_safe".equals(kpiGatherResult.getAssessmentStatus().getId())){
                    	//kpiGatherResult.getAssessmentStatus().getReserved1();
                    	value = 1.0;
                    }else if("0alarm_startus_h".equals(kpiGatherResult.getAssessmentStatus().getId())){
                    	value = 4.0;
                    }else if("0alarm_startus_m".equals(kpiGatherResult.getAssessmentStatus().getId())){
                    	value = 3.0;
                    }else if("0alarm_startus_l".equals(kpiGatherResult.getAssessmentStatus().getId())){
                    	value = 2.0;
                    }
    			}else {
    				return targetName+":状态灯不存在";
    			}
    		}
    	}
    	if(null!=value){
    	    return String.valueOf(value);
    	}
    	else{
    	    return null;
    	}
    }
    
    /**
     * 分解公式字符串.
     * @param targetName 对象名称
     * @param formula
     * @return List<Map<String, String>> map格式：{name:'',type:'',originalName:''}
     */
    public List<Map<String, String>> decompositionFormulaString(String targetName, String formula) {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        Map<String, String> map = null;

        int temp = formula.indexOf("#[");
        while (temp != -1) {
            map = new HashMap<String, String>();

            //关联指标与值类型的字符串
            String targetNameAndValueTypeTemp = apartTarget(formula, temp);
            map.put("originalName", "#[" + targetNameAndValueTypeTemp + "]");
            //指标名称与类型分解数组
            String[] targetNameAndValueType = targetNameAndValueTypeTemp.split("~");
            //指标的名称；
            String tempTargetName = targetNameAndValueType[0];
            if ("myself".equals(tempTargetName)) {
                tempTargetName = targetName;
            }
            map.put("name", tempTargetName);
            //指标的值类型;
            String dataTypeName = targetNameAndValueType[1];
            map.put("type", dataTypeName);

            formula = formula.substring(formula.indexOf("]") + 1, formula.length());
            temp = formula.indexOf("#[");
            result.add(map);
        }
        return result;
    }

    /**
     * 从公式字符串中分离出指标名称和指标类型
     * 分离的结果为长度为2的字符串数组，第1个元素为指标名称，第2个为值类型
     * @author 陈燕杰
     * @param sourceStr：公式字符串；
     * @param beginIndex：#[在公式字符串中的index;
     * @return String
     * @since  fhd　Ver 1.1
     */
    public String apartTarget(String sourceStr, int beginIndex) {
        StringBuilder result = new StringBuilder();
        int len = sourceStr.length();
        for (int i = beginIndex + 2; i < len; i++) {
            String temp = sourceStr.substring(i, i + 1);
            if (!temp.equals("]")) {
                result.append(temp);
            }
            else {
                break;
            }
        }
        return result.toString();
    }

    /**
     * java正则表达式判断字符串是否是double类型.
     * @param str
     * @return boolean
     */
    public boolean isDoubleOrInteger(String str) {
        /*if (str.matches("-?\\d+\\.\\d+") || str.matches("-?\\d+")) {
            return true;
        }
        else {
            return false;
        }*/
        return NumberUtils.isNumber(str);
    }

    /**
     * 删除formula字符串中的html格式.
     * @param formula
     * @param length
     * @return String
     */
    public static String splitAndFilterString(String formula, int length) {
        if (StringUtils.isBlank(formula)) {
            return "";
        }
        // 去掉所有html元素,
        String str = formula.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "");
        str = str.replaceAll("[(/>)<]", "");
        int len = str.length();
        if (len <= length) {
            return str;
        }
        else {
            str = str.substring(0, length);
            str += "......";
        }
        return str;
    }

    /**
     * 解析公式字符串存在多少变量值--暂时未用.
     * @param str
     * @return int
     */
    public static int queryParamsCountByFormulaString(String str) {
        int count = 0;
        //一共有str的长度的循环次数   
        for (int i = 0; i < str.length();) {
            int c = -1;
            c = str.indexOf("@");
            //如果有@这样的子字符串。则c的值不是-1.   
            if (c != -1) {
                //这里的c+1 而不是 c+ s.length();这是因为。如果str的字符串是"aaaa"， s = "aa"，则结果是2个。但是实际上是3个子字符串   
                //将剩下的字符冲洗取出放到str中   
                str = str.substring(c + 1);
                count++;
            }
            else {
                break;
            }
        }
        return count;
    }

    /**
     * 记分卡、战略目标计算公式.
     * 目前不支持混搭，有3种计算方式，即：由下级记分卡计算，由度量指标计算，由选择的指标计算.
     * @param targetId 对象id
     * @param type 类型:category/strategy
     * @param formula 公式内容
     * @timePeriodId 时间区间维id
     * 公式内容定义：sub(assessmentValue,kpi,A)
     * sub--函数名称
     * &记分卡、战略目标名称或者myself
     * &评估值--值类型
     * &kpi--值对象类型
     * &A--函数名称average的简称;S--函数名称sum的简称
     * @return String
     */
    @Transactional
    public String calculateCategory(String targetId, String type, String formula, String timePeriodId) {
        String ret = "";

        String targetName = "";

        if (StringUtils.isNotBlank(targetId)) {
            if (Contents.OBJECT_CATEGORY.equals(type)) {
                Category category = o_categroyBO.findCategoryById(targetId);
                if (null != category) {
                    targetName = category.getName();
                }
            }
            else if (Contents.OBJECT_STRATEGY.equals(type)) {
                StrategyMap strategyMap = o_strategyMapBO.findById(targetId);
                if (null != strategyMap) {
                    targetName = strategyMap.getName();
                }
            }
            /*
            //去除常量的特殊字符
            formula = formula.replaceAll("\\$", "");
            
            String formulaBefore = "";
            String formulaMid = "";
            String formulaAfter = "";
            String midTemp = "";
            if(formula.indexOf("SUB")!=-1){
            	int temp=formula.indexOf("SUB");
            	while(temp!=-1){
            		formulaBefore = formula.substring(0, temp);
            		formulaMid = formula.substring(temp, formula.indexOf(")")+1);
            		midTemp = calculateSubFun(targetId, targetName, type, formulaMid, timePeriodId);
            		if(!isDoubleOrInteger(midTemp)){
            			return midTemp;
            		}
            		formulaAfter = formula.substring(formula.indexOf(")")+1, formula.length());
            		formula=formulaBefore + midTemp + formulaAfter;
            		temp = formula.indexOf("SUB");
            	}
            }
            
            ret = o_functionCalculateBO.calculate(o_functionCalculateBO.strCast(formula));
            */
            if (StringUtils.isNotBlank(formula)) {
                formula = formula.replaceAll("\\$", "");
            }
            if (formula.indexOf("SUB") != -1) {
                ret = calculateSubFun(targetId, targetName, type, formula, timePeriodId);
            }
            else {
                ret = "公式定义不正确!";
            }
        }

        return ret;
    }

    /**
     * sub函数解析.
     * @param targetId 对象id
     * @param targetName 对象名称
     * @param type 计算对象类型
     * @param subFormula sub函数内容
     * @param timePeriod 时间区间维id
     * @return String sub函数计算结果
     */
    public String calculateSubFun(String targetId, String targetName, String type, String subFormula, String timePeriod) {
        DecimalFormat df = new DecimalFormat("0.00");
        String funContent = subFormula.substring(subFormula.indexOf("(") + 1, subFormula.indexOf(")"));
        String[] contentArray = funContent.split(",");
        StringBuilder sb = new StringBuilder();
        if (null != contentArray && contentArray.length > 0) {
            if ("myself".equals(String.valueOf(contentArray[0]))) {
                contentArray[0] = targetName;
            }
            List<Object[]> objectList = null;
            if (Contents.OBJECT_CATEGORY.equals(type)) {
                if (Contents.OBJECT_CATEGORY.equals(contentArray[2])) {
                    //通过下级记分卡计算
                    objectList = o_relaAssessResultBO.findRelaAssessResultBySubCategory(targetId, contentArray[1], timePeriod);
                }
                else if (Contents.OBJECT_KPI.equals(contentArray[2])) {
                    //通过度量指标计算
                    objectList = o_kpiGatherResultBO.findKpiGatherResultListByCategoryId(targetId, contentArray[1], timePeriod);
                }
            }
            else if (Contents.OBJECT_STRATEGY.equals(type)) {
                if (Contents.OBJECT_STRATEGY.equals(contentArray[2])) {
                    //通过下级战略目标计算
                    objectList = o_relaAssessResultBO.findRelaAssessResultBySubStrategy(targetId, contentArray[1], timePeriod);
                }
                else if (Contents.OBJECT_KPI.equals(contentArray[2])) {
                    //通过度量指标计算
                    objectList = o_kpiGatherResultBO.findKpiGatherResultListByStrategyId(targetId, contentArray[1], timePeriod);
                }
            }
            if (null != objectList && objectList.size() > 0) {
                if ("A".equals(contentArray[3])) {
                    sb.append("average").append("(");
                }
                else if ("S".equals(contentArray[3])) {
                    sb.append("sum").append("(");
                }
                int i = 0;
                for (Object[] object : objectList) {
                    Double v = 0.0;
                    if (null != object[1]) {
                        v = Double.valueOf(String.valueOf(object[1]));
                    }
                    else {
                        if (Contents.OBJECT_CATEGORY.equals(contentArray[2])) {
                            return "记分卡[" + object[0] + "]的'" + contentArray[1] + "'不存在!";
                        }
                        else if (Contents.OBJECT_STRATEGY.equals(contentArray[2])) {
                            return "战略目标[" + object[0] + "]的'" + contentArray[1] + "'不存在!";
                        }
                        else if (Contents.OBJECT_KPI.equals(contentArray[2])) {
                            return "指标[" + object[0] + "]的'" + contentArray[1] + "'不存在!";
                        }
                    }

                    if (v >= 0) {
                        //如果评估值是正数
                        sb.append(df.format(v));
                    }
                    else if (v < 0) {
                        //如果评估值是负数,要把数字括起来
                        sb.append("(" + df.format(v) + ")");
                    }

                    if (i != objectList.size() - 1) {
                        sb.append(",");
                    }
                    i++;
                }
                sb.append(")");
                return o_functionCalculateBO.calculate(o_functionCalculateBO.strCast(sb.toString()));
            }
            else {
                String str = "";
                if (Contents.OBJECT_CATEGORY.equals(type)) {
                    if (Contents.OBJECT_CATEGORY.equals(contentArray[2])) {
                        //通过下级记分卡计算
                        str = "记分卡[" + targetName + "]" + "需要计算的下级记分卡值不存在!";
                    }
                    else if (Contents.OBJECT_KPI.equals(contentArray[2])) {
                        //通过度量指标计算
                        str = "记分卡[" + targetName + "]" + "需要计算的度量指标值不存在!";
                    }
                }
                else if (Contents.OBJECT_STRATEGY.equals(type)) {
                    if (Contents.OBJECT_STRATEGY.equals(contentArray[2])) {
                        //通过下级战略目标计算
                        str = "战略目标[" + targetName + "]" + "需要计算的下级战略目标值不存在!";
                    }
                    else if (Contents.OBJECT_KPI.equals(contentArray[2])) {
                        //通过度量指标计算
                        str = "战略目标[" + targetName + "]" + "需要计算的度量指标值不存在!";
                    }
                }
                return str;
            }
        }
        return subFormula;
    }
}
