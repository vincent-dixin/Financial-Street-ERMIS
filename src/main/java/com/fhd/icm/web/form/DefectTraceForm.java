package com.fhd.icm.web.form;


import java.io.Serializable;

import com.fhd.icm.entity.rectify.DefectTrace;

public class DefectTraceForm extends DefectTrace implements Serializable{
	private static final long serialVersionUID = -1953735922746231046L;
	
	/**
	 *  是否通过：数据字典ID
	 */
	private String isPassId;
	
	/**
	 * 调整后缺陷等级：数据字典ID
	 */
	private String afterDefectLevelId;
	
	/**
	 * 整改方案关联缺陷ID
	 */
	private String improvePlanRelaDefectId;

	public String getIsPassId() {
		return isPassId;
	}

	public void setIsPassId(String isPassId) {
		this.isPassId = isPassId;
	}

	public String getAfterDefectLevelId() {
		return afterDefectLevelId;
	}

	public void setAfterDefectLevelId(String afterDefectLevelId) {
		this.afterDefectLevelId = afterDefectLevelId;
	}

	public String getImprovePlanRelaDefectId() {
		return improvePlanRelaDefectId;
	}

	public void setImprovePlanRelaDefectId(String improvePlanRelaDefectId) {
		this.improvePlanRelaDefectId = improvePlanRelaDefectId;
	}
	
}