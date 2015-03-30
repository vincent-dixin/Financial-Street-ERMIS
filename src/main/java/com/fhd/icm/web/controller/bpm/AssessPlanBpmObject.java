package com.fhd.icm.web.controller.bpm;

import java.io.Serializable;

/**
 * @author   吴德福
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-3-22		下午9:50:39
 * @see 	 
 */
public class AssessPlanBpmObject implements Serializable{
	
	private static final long serialVersionUID = -2588960735724229750L;
	
	/*
	 * 评价计划相关评价流程id
	 */
	private String assessPlanRelaProcessId;
	/*
	 * 评价计划执行人
	 */
	private String executeEmpId;
	/*
	 * 评价计划复核人
	 */
	private String reviewerEmpId;
	
	public String getAssessPlanRelaProcessId() {
		return assessPlanRelaProcessId;
	}
	public void setAssessPlanRelaProcessId(String assessPlanRelaProcessId) {
		this.assessPlanRelaProcessId = assessPlanRelaProcessId;
	}
	public String getExecuteEmpId() {
		return executeEmpId;
	}
	public void setExecuteEmpId(String executeEmpId) {
		this.executeEmpId = executeEmpId;
	}
	public String getReviewerEmpId() {
		return reviewerEmpId;
	}
	public void setReviewerEmpId(String reviewerEmpId) {
		this.reviewerEmpId = reviewerEmpId;
	}
}