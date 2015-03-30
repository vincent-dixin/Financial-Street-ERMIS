/**
 * AssessPlanForm.java
 * com.fhd.icm.web.form
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-1-8 		刘中帅
 *
 * Copyright (c) 2013, Firsthuida All Rights Reserved.
*/

package com.fhd.icm.web.form;

import com.fhd.icm.entity.assess.AssessPlan;

/**
 * @author   刘中帅
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-1-8		上午11:43:44
 *
 * @see 	 
 */
public class AssessPlanForm extends AssessPlan {

	private static final long serialVersionUID = 1L;

	/**
	 * 流程Id
	 */
	private String processId;
	
	/**
	 * 部门Id
	 */
	private String deptId;

	/**
	 * 组长Id
	 */
	private String groupLeaderId;
	
	/**
	 * 组成员Id
	 */
	private String groupPersId;

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getGroupLeaderId() {
		return groupLeaderId;
	}

	public void setGroupLeaderId(String groupLeaderId) {
		this.groupLeaderId = groupLeaderId;
	}

	public String getGroupPersId() {
		return groupPersId;
	}

	public void setGroupPersId(String groupPersId) {
		this.groupPersId = groupPersId;
	}
}

