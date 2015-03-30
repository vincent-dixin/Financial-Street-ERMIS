package com.fhd.icm.entity.assess;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * 评价计划中的流程的相关的参与部门,经办人，复核人
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2013	2013-1-22		下午2:10:51
 *
 * @see 	 
 */
@Entity
@Table(name="T_CA_PLAN_PROCESSURE_ORG_EMP")
public class AssessPlanProcessRelaOrgEmp extends IdEntity implements Serializable {

	private static final long serialVersionUID = 1481779967724357955L;

	/**
	 * 评价计划关联的流程
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAN_PROCESSURE_ID")
	private AssessPlanRelaProcess assessPlanRelaProcess;
			
	/**
	 * 评价计划关联部门人员
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAN_ORG_EMP_ID")
	private AssessPlanRelaOrgEmp assessPlanRelaOrgEmp;
	
	/**
	 * 类型：参与部门,经办人，复核人
	 */
	@Column(name="ETYPE")
	private String type;

	public AssessPlanProcessRelaOrgEmp(){
		
	}
	
	public AssessPlanProcessRelaOrgEmp(String id){
		super.setId(id);
	}
	
	public AssessPlanRelaOrgEmp getAssessPlanRelaOrgEmp() {
		return assessPlanRelaOrgEmp;
	}

	public void setAssessPlanRelaOrgEmp(AssessPlanRelaOrgEmp assessPlanRelaOrgEmp) {
		this.assessPlanRelaOrgEmp = assessPlanRelaOrgEmp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public AssessPlanRelaProcess getAssessPlanRelaProcess() {
		return assessPlanRelaProcess;
	}

	public void setAssessPlanRelaProcess(AssessPlanRelaProcess assessPlanRelaProcess) {
		this.assessPlanRelaProcess = assessPlanRelaProcess;
	}
	
	
}

