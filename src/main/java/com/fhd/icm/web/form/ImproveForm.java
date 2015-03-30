package com.fhd.icm.web.form;

import java.io.Serializable;

import com.fhd.icm.entity.rectify.Improve;

public class ImproveForm extends Improve implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 所属公司ID
	 */
	private String companyId;
	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
}