package com.fhd.sys.web.form.duty;

import com.fhd.sys.entity.duty.Duty;

public class DutyForm extends Duty {

	private static final long serialVersionUID = 1L;
	/**
	 * 职务所属公司
	 */
	private String dutyCompany;
	/**
	 * 组织id
	 */
	private String orgId;
	/**
	 * 状态
	 */
	private String dutyStatus;
	
	public String getDutyCompany() {
		return dutyCompany;
	}
	public void setDutyCompany(String dutyCompany) {
		this.dutyCompany = dutyCompany;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getDutyStatus() {
		return dutyStatus;
	}
	public void setDutyStatus(String dutyStatus) {
		this.dutyStatus = dutyStatus;
	}
}
