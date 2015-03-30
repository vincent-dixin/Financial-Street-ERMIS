package com.fhd.assess.interfaces.quaAssess;

import java.util.ArrayList;
import java.util.HashMap;

import com.fhd.assess.entity.quaAssess.ScoreResult;


public interface IQuaAssessBO {
 
//	/**通过前台风险集合JSON得到所有风险风险灯(
//	 * @param params 前台JSON
//	 * @return ArrayList<HashMap<String, String>>
//	 * @author 金鹏祥
//	 * */
//	public ArrayList<HashMap<String, String>> getAssessShowGrid(String params);
	
	
//	/**通过人员ID、范围名称查询评估记录(风险事件)(
//	 * @param scoreEmpId 人员ID
//	 * @param templateId 模板ID
//	 * @return ArrayList<HashMap<String, String>>
//	 * @author 金鹏祥
//	 * */
//	public ArrayList<HashMap<String, String>> findRiskByScoreEmpId(String scoreEmpId, String templateId, boolean isAlarm);
	
	
//	/**通过人员ID、范围名称查询评估记录(部门、目标类型)
//	 * @param scoreEmpId 人员ID
//	 * @param scoreRangeName 范围名称
//	 * @param templateId 模板ID
//	 * @return ArrayList<HashMap<String, String>>
//	 * @author 金鹏祥
//	 * */
//	public ArrayList<HashMap<String, String>> findDeptOrKpiByScoreEmpId(String scoreEmpId, String scoreRangeName, String templateId);
//	
//	/**通过人员ID、范围名称查询评估记录(流程类型)
//	 * @param scoreEmpId 人员ID
//	 * @param scoreRangeName 范围名称
//	 * @param templateId 模板ID
//	 * @return ArrayList<HashMap<String, String>>
//	 * @author 金鹏祥
//	 * */
//	public ArrayList<HashMap<String, String>> findProcessureByScoreEmpId(String scoreEmpId, String scoreRangeName, String templateId);
	
//	/**
//	 * 通过范围-对象-部门-人员综合表,查询风险水平
//	 * @param RangObjectDeptEmpId 范围-对象-部门-人员综合表,查询风险水平ID
//	 * @return HashMap<String, String>
//	 * @author 金鹏祥
//	 * */
//	public HashMap<String, String> findRiskStatus(String RangObjectDeptEmpId);
	
//	/**
//	 * 得到评估计划范围类型、评估计划模板类型(rangeType,templateType)
//	 * @param AssessPlanId	评估计划ID
//	 * @return HashMap<String, String>
//	 * @author 金鹏祥
//	 * */
//	public HashMap<String, String> findRiskAssessPlanByAssessId(String AssessPlanId);
	
//	/**
//	 * 得到模板对应维度及分数
//	 * @param templateId 模板ID
//	 * @return ArrayList<Score>
//	 * @author 金鹏祥
//	 * */
//	public HashMap<Integer, ArrayList<ArrayList<ArrayList<ArrayList<String>>>>> findTemplateRelaDimByTemplateId(String templateId);
	
	/**
	 * 保存更新打分结果
	 * @param rangObjectDeptEmp 综合实体
	 * @author 金鹏祥
	 * */
	public void mergeScoreResult(ScoreResult scoreResult);
	
//	/**
//	 * 评估保存操作
//	 * @param params 前台JSON
//	 * @author 金鹏祥
//	 * */
//	public boolean assessSaveOper(String params);
	
//	/**
//	 * 通过风险ID集合查询已评估风险
//	 * */
//	public HashMap<String, String> findAssessMap(String scoreEmpId);
	
//	/**
//	 * 通过风险ID集合查询已评估风险总数、未评估总数、已评估总数
//	 */
//	public HashMap<String, Integer> getAssessCountMap(String scoreEmpId, String params);
}