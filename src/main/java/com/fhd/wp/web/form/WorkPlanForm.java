/*
 * 北京第一会达风险管理有限公司 版权所有 2013
 * Copyright(C) 2013 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.wp.web.form;

import com.fhd.wp.entity.WorkPlan;

/**
 * 工作计划formbean
 *
 * @author   胡迪新
 * @since    fhd Ver 4.5
 * @Date	 2013-2-20  下午6:39:20
 *
 * @see 	 
 */
public class WorkPlanForm extends WorkPlan {

	/**
	 *
	 * @author 胡迪新
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = -6470241977686337852L;

	private String startDateStr;
	
	private String endDateStr;
	
	private String responsilePersionId;
	
	private String orgs;
	
	private String[] milestoneName;

	private String[] milestoneDesc;

	public String getStartDateStr() {
		return startDateStr;
	}

	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}

	public String getEndDateStr() {
		return endDateStr;
	}

	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	public String getResponsilePersionId() {
		return responsilePersionId;
	}

	public void setResponsilePersionId(String responsilePersionId) {
		this.responsilePersionId = responsilePersionId;
	}

	public String getOrgs() {
		return orgs;
	}

	public void setOrgs(String orgs) {
		this.orgs = orgs;
	}

	public String[] getMilestoneName() {
		return milestoneName;
	}

	public void setMilestoneName(String[] milestoneName) {
		this.milestoneName = milestoneName;
	}

	public String[] getMilestoneDesc() {
		return milestoneDesc;
	}

	public void setMilestoneDesc(String[] milestoneDesc) {
		this.milestoneDesc = milestoneDesc;
	}
	
}

