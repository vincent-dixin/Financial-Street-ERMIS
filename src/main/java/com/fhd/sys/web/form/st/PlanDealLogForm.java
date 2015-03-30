/**
 * DealLogForm.java
 * com.fhd.sys.web.form.st
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-23 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * DealLogForm.java
 * com.fhd.sys.web.form.st
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-23        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
*/


package com.fhd.sys.web.form.st;

import com.fhd.sys.entity.st.PlanDealLog;

/**
 * 计划任务日志form
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-23		下午05:08:47
 *
 * @see 	 
 */

public class PlanDealLogForm extends PlanDealLog{
	private static final long serialVersionUID = 1L;
	private String planName;
	private String planTempName;
	
	public PlanDealLogForm(PlanDealLog dealLog){
		this.setPlanName(dealLog.getPlanEmp().getPlan().getName());
		this.setPlanTempName(dealLog.getTemp().getName());
		this.setDealMeasure(dealLog.getDealMeasure());
		this.setDealTime(dealLog.getDealTime());
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getPlanTempName() {
		return planTempName;
	}

	public void setPlanTempName(String planTempName) {
		this.planTempName = planTempName;
	}
}