/**
 * StandardBpmObject.java
 * com.fhd.icm.web.controller.bpm
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-4-10 		元杰
 *
 * Copyright (c) 2013, Firsthuida All Rights Reserved.
*/

package com.fhd.icm.web.controller.bpm;

import java.io.Serializable;

/**
 * @author   元杰
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-4-10		下午17:40:39
 *
 * @see 	 
 */
public class StandardBpmObject implements Serializable{
	/**
	 * TODO（用一句话描述这个变量表示什么）
	 *
	 * @author 元杰
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = -2588960735724229750L;
	private String[] standardControlIds; //标准对要求ID集合
	
	private String reviewerEmpId; //第三步
	private String reportorEmpId; //第四步
	private String approverEmpId; //第五步
	
	public String[] getStandardControlIds() {
		return standardControlIds;
	}
	public void setStandardControlIds(String[] standardControlIds) {
		this.standardControlIds = standardControlIds;
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