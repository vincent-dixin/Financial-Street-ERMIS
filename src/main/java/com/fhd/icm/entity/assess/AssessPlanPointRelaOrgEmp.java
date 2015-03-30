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
 * 评价计划中的评价点的相关的参与部门,经办人，复核人
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-26		下午4:06:35
 *
 * @see 	 
 */
@Entity
@Table(name="T_CA_PLAN_DETAIL_RELA_ORG")
public class AssessPlanPointRelaOrgEmp extends IdEntity implements Serializable {

	private static final long serialVersionUID = 1481779967724357955L;

	/**
	 * 评价计划
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAN_ID")
	private AssessPlan assessPlan;
	
	/**
	 * 评价计划关联的评价点
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAN_POINT_ID")
	private AssessPlanRelaPoint assessPlanRelaPoint;
			
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

	public AssessPlanPointRelaOrgEmp(){
		
	}
	
	public AssessPlanPointRelaOrgEmp(String id){
		super.setId(id);
	}
	
	public AssessPlanRelaPoint getAssessPlanRelaPoint() {
		return assessPlanRelaPoint;
	}

	public void setAssessPlanRelaPoint(AssessPlanRelaPoint assessPlanRelaPoint) {
		this.assessPlanRelaPoint = assessPlanRelaPoint;
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

	public AssessPlan getAssessPlan() {
		return assessPlan;
	}

	public void setAssessPlan(AssessPlan assessPlan) {
		this.assessPlan = assessPlan;
	}
	
}

