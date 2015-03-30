package com.fhd.icm.web.controller.bpm.icsystem;

import java.io.Serializable;

/**
 * @author   宋佳
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-4-17		下午 14:18
 * @see 	 
 */
public class ConstructPlanBpmFeckBackObject implements Serializable{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * 建设计划关联标准 
	 */
	private String dianosesRelaDefectId;
	/*
	 * 缺陷反馈人
	 */
	private String defectFeedbackEmpId;
	/*
	 * 缺陷整理人
	 */
	private String defectCleanUpEmpId;
	
	
	public String getDianosesRelaDefectId() {
		return dianosesRelaDefectId;
	}
	public void setDianosesRelaDefectId(String dianosesRelaDefectId) {
		this.dianosesRelaDefectId = dianosesRelaDefectId;
	}
	public String getDefectFeedbackEmpId() {
		return defectFeedbackEmpId;
	}
	public void setDefectFeedbackEmpId(String defectFeedbackEmpId) {
		this.defectFeedbackEmpId = defectFeedbackEmpId;
	}
	public String getDefectCleanUpEmpId() {
		return defectCleanUpEmpId;
	}
	public void setDefectCleanUpEmpId(String defectCleanUpEmpId) {
		this.defectCleanUpEmpId = defectCleanUpEmpId;
	}
	
}