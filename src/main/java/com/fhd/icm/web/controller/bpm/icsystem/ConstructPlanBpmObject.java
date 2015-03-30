package com.fhd.icm.web.controller.bpm.icsystem;

import java.io.Serializable;

/**
 * @author   宋佳
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-4-17		下午 14:18
 * @see 	 
 */
public class ConstructPlanBpmObject implements Serializable{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * 建设计划关联标准 
	 */
	private String constructPlanRelaStandardId;
	/*
	 * 合规诊断执行人
	 */
	private String executeEmpId;
	/*
	 * 流程梳理执行人
	 */
	private String foreachExecutionId;
	/*
	 * 流程梳理执行人
	 */
	private String processEditEmpId;
	/*
	 * 梳理结果审批人
	 */
	private String approveEmpId;
	/*
	 *  流程进入条件
	 * 
	 */
	private String path;
	/*
	 *  成果发布
	 */
	private String resultsPublish;
	/*
	 * 建设计划关联标准 
	 */
	private String dianosesRelaDefectId;
	/*
	 * 缺陷反馈人
	 */
	private String defectFeedbackEmpId;
	/*
	 * 流程梳理审核人
	 */
	private String processApprovalEmpId;
	/*
	 * 流程修复处理人
	 */
	private String processRepairEmpId;
	/*
	 *  需审核的流程id
	 */
	private String constructPlanRelaProcessIds;
	/*
	 * 缺陷整理人
	 */
	private String defectCleanUpEmpId;
	
	public String getProcessEditEmpId() {
		return processEditEmpId;
	}
	public void setProcessEditEmpId(String processEditEmpId) {
		this.processEditEmpId = processEditEmpId;
	}
	public String getConstructPlanRelaStandardId() {
		return constructPlanRelaStandardId;
	}
	public void setConstructPlanRelaStandardId(String constructPlanRelaStandardId) {
		this.constructPlanRelaStandardId = constructPlanRelaStandardId;
	}
	public String getExecuteEmpId() {
		return executeEmpId;
	}
	public void setExecuteEmpId(String executeEmpId) {
		this.executeEmpId = executeEmpId;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getApproveEmpId() {
		return approveEmpId;
	}
	public void setApproveEmpId(String approveEmpId) {
		this.approveEmpId = approveEmpId;
	}
	public String getResultsPublish() {
		return resultsPublish;
	}
	public void setResultsPublish(String resultsPublish) {
		this.resultsPublish = resultsPublish;
	}
	public String getForeachExecutionId() {
		return foreachExecutionId;
	}
	public void setForeachExecutionId(String foreachExecutionId) {
		this.foreachExecutionId = foreachExecutionId;
	}
	public String getDianosesRelaDefectId() {
		return dianosesRelaDefectId;
	}
	public void setDianosesRelaDefectId(String dianosesRelaDefectId) {
		this.dianosesRelaDefectId = dianosesRelaDefectId;
	}
	public String getDefectFeedbackEmpId() {
		return defectFeedbackEmpId;
	}
	public void setDefectFeedbackEmpId(String defectFeedbackEmpId) {
		this.defectFeedbackEmpId = defectFeedbackEmpId;
	}
	public String getDefectCleanUpEmpId() {
		return defectCleanUpEmpId;
	}
	public void setDefectCleanUpEmpId(String defectCleanUpEmpId) {
		this.defectCleanUpEmpId = defectCleanUpEmpId;
	}
	public String getProcessApprovalEmpId() {
		return processApprovalEmpId;
	}
	public void setProcessApprovalEmpId(String processApprovalEmpId) {
		this.processApprovalEmpId = processApprovalEmpId;
	}
	public String getProcessRepairEmpId() {
		return processRepairEmpId;
	}
	public void setProcessRepairEmpId(String processRepairEmpId) {
		this.processRepairEmpId = processRepairEmpId;
	}
	public String getConstructPlanRelaProcessIds() {
		return constructPlanRelaProcessIds;
	}
	public void setConstructPlanRelaProcessIds(String constructPlanRelaProcessIds) {
		this.constructPlanRelaProcessIds = constructPlanRelaProcessIds;
	}
	
	
}