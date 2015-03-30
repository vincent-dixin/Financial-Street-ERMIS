/**
 * BpmObject.java
 * com.fhd.icm.web.controller.bpm
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-3-18 		张 雷
 *
 * Copyright (c) 2013, Firsthuida All Rights Reserved.
*/

package com.fhd.icm.web.controller.bpm;

import java.io.Serializable;

/**
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-3-18		下午2:50:39
 *
 * @see 	 
 */
public class ImproveBpmObject implements Serializable{
	/**
	 * TODO（用一句话描述这个变量表示什么）
	 *
	 * @author 张 雷
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = -2588960735724229750L;
	private String improvePlanRelaDefectId;
	private String approverEmpId;
	private String reviewerEmpId;
	private String reportorEmpId;
	
	
	public String getImprovePlanRelaDefectId() {
		return improvePlanRelaDefectId;
	}
	public void setImprovePlanRelaDefectId(String improvePlanRelaDefectId) {
		this.improvePlanRelaDefectId = improvePlanRelaDefectId;
	}
	public String getApproverEmpId() {
		return approverEmpId;
	}
	public void setApproverEmpId(String approverEmpId) {
		this.approverEmpId = approverEmpId;
	}
	public String getReviewerEmpId() {
		return reviewerEmpId;
	}
	public void setReviewerEmpId(String reviewerEmpId) {
		this.reviewerEmpId = reviewerEmpId;
	}
	public String getReportorEmpId() {
		return reportorEmpId;
	}
	public void setReportorEmpId(String reportorEmpId) {
		this.reportorEmpId = reportorEmpId;
	}
	
	
}

