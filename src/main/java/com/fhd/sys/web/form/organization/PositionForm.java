package com.fhd.sys.web.form.organization;

import com.fhd.sys.entity.orgstructure.SysPosition;

public class PositionForm extends SysPosition{

	private static final long serialVersionUID = 1L;
	
	private String posiStartDateStr;//开始时间
	private String posiEndDateStr;//结束时间
	private String posiSnStr;//排列顺序
	
	public PositionForm(){
		
	}
	
	public PositionForm(SysPosition pos){
		this.setId(pos.getId());
		this.setPosicode(pos.getPosicode());
		this.setPosiname(pos.getPosiname());
		this.setPosiStatus(pos.getPosiStatus());
		//this.setStartDate(pos.getStartDate());
		//this.setEndDate(pos.getEndDate());
		//this.setSn(pos.getSn());
		this.setRemark(pos.getRemark());
		
	}

	public String getPosiStartDateStr() {
		return posiStartDateStr;
	}

	public void setPosiStartDateStr(String posiStartDateStr) {
		this.posiStartDateStr = posiStartDateStr;
	}

	public String getPosiEndDateStr() {
		return posiEndDateStr;
	}

	public void setPosiEndDateStr(String posiEndDateStr) {
		this.posiEndDateStr = posiEndDateStr;
	}

	public String getPosiSnStr() {
		return posiSnStr;
	}

	public void setPosiSnStr(String posiSnStr) {
		this.posiSnStr = posiSnStr;
	}

	
	

}
