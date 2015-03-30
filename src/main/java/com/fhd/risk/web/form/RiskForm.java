package com.fhd.risk.web.form;

import com.fhd.risk.entity.Risk;

public class RiskForm extends Risk {

	private static final long serialVersionUID = -2209315597081220145L;
	
	public RiskForm(){
		
	}
	
	/**
	 * 上级风险ID
	 */
	private String parentId = null;
	
	/**
	 * 责任部门和责任人
	 */
	private String respDeptName = "";
	
	/**
	 * 相关部门和相关人
	 */
	private String relaDeptName = "";
	
	/**
	 * 责任岗位
	 */
	private String respPositionName = "";
	
	/**
	 * 相关岗位
	 */
	private String relaPositionName = "";
	

	/**
	 * 风险指标
	 */
	private String riskKpiName = "";
	
	/**
	 * 影响指标
	 */
	private String influKpiName = "";

	/**
	 * 控制流程
	 */
	private String controlProcessureName = "";
	
	/**
	 * 影响流程
	 */
	private String influProcessureName = "";
	
	/**
	 * 模板ID
	 */
	private String templateId = null;
	
	/**
	 * 告警方案ID
	 */
	private String alarmPlanId = null;
	
	/**
	 * 风险类别
	 */
	String riskKind = null;
	
	/**
	 * 涉及版块
	 */
	String relePlate = null;
	
	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getRespDeptName() {
		return respDeptName;
	}

	public void setRespDeptName(String respDeptName) {
		this.respDeptName = respDeptName;
	}

	public String getRelaDeptName() {
		return relaDeptName;
	}

	public void setRelaDeptName(String relaDeptName) {
		this.relaDeptName = relaDeptName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getRespPositionName() {
		return respPositionName;
	}

	public void setRespPositionName(String respPositionName) {
		this.respPositionName = respPositionName;
	}

	public String getRelaPositionName() {
		return relaPositionName;
	}

	public void setRelaPositionName(String relaPositionName) {
		this.relaPositionName = relaPositionName;
	}

	public String getRiskKpiName() {
		return riskKpiName;
	}

	public void setRiskKpiName(String riskKpiName) {
		this.riskKpiName = riskKpiName;
	}

	public String getInfluKpiName() {
		return influKpiName;
	}

	public void setInfluKpiName(String influKpiName) {
		this.influKpiName = influKpiName;
	}

	public String getControlProcessureName() {
		return controlProcessureName;
	}

	public void setControlProcessureName(String controlProcessureName) {
		this.controlProcessureName = controlProcessureName;
	}

	public String getInfluProcessureName() {
		return influProcessureName;
	}

	public void setInfluProcessureName(String influProcessureName) {
		this.influProcessureName = influProcessureName;
	}

	public String getAlarmPlanId() {
		return alarmPlanId;
	}

	public void setAlarmPlanId(String alarmPlanId) {
		this.alarmPlanId = alarmPlanId;
	}

	public String getRiskKind() {
		return riskKind;
	}

	public void setRiskKind(String riskKind) {
		this.riskKind = riskKind;
	}

	public String getRelePlate() {
		return relePlate;
	}

	public void setRelePlate(String relePlate) {
		this.relePlate = relePlate;
	}

}
