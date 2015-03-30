package com.fhd.icm.entity.icsystem;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-28		上午10:01:45
 *
 * @see 	 
 */
@Entity
@Table(name="T_CA_COMPLIANCE_DIAGNOSES")
public class Diagnoses extends IdEntity implements Serializable {
	private static final long serialVersionUID = 7726731442209313772L;

	/**
	 * 体系建设计划标准
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAN_RELA_STANDARD_ID")
	private ConstructPlanRelaStandard constructPlanRelaStandard;
	/**
	 * 体系建设计划id
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAN_ID")
	private ConstructPlan constructPlan;

	/**
	 *  状态
	 */
	@Column(name = "ESTATUS")
	private String estatus;
	/**
	 *    备注
	 */
	@Column(name = "ECOMMENT")
	private String ecomment;
	/**
	 *   诊断结果
	 */
	@Column(name = "DIAGNOSIS")
	private Boolean diagnosis;
	
	/**
	 *   实施证据
	 */
	@Column(name = "PROOF")
	private String proof;
	
	/**
	 * 缺陷描述
	 */
	@Column(name = "DESCRIPTION")
	private String description;
	/**
	 *  控制描述
	 */
	@Column(name = "CONTROLDESC")
	private String controldesc;
	/**
	 *   计划开始
	 */
	@Column(name = "PLAN_START_DATE")
	private Date planStartDate;
	/**
	 *   计划结束
	 */
	@Column(name = "PLAN_END_DATE")
	private Date planEndDate;
	/**
	 *   实际开始	
	 */
	@Column(name = "ACTUAL_START_DATE")
	private Date actualStartDate;
	/**
	 *   实际开始	
	 */
	@Column(name = "ACTUAL_END_DATE")
	private Date actualEndDate;
	
	public Diagnoses(){
		
	}
	
	public Diagnoses(String id){
		super.setId(id);
	}

	public ConstructPlanRelaStandard getConstructPlanRelaStandard() {
		return constructPlanRelaStandard;
	}

	public void setConstructPlanRelaStandard(
			ConstructPlanRelaStandard constructPlanRelaStandard) {
		this.constructPlanRelaStandard = constructPlanRelaStandard;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getEcomment() {
		return ecomment;
	}

	public void setEcomment(String ecomment) {
		this.ecomment = ecomment;
	}

	public Boolean getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(Boolean diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getProof() {
		return proof;
	}

	public void setProof(String proof) {
		this.proof = proof;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getControldesc() {
		return controldesc;
	}

	public void setControldesc(String controldesc) {
		this.controldesc = controldesc;
	}

	public Date getPlanStartDate() {
		return planStartDate;
	}

	public void setPlanStartDate(Date planStartDate) {
		this.planStartDate = planStartDate;
	}

	public Date getPlanEndDate() {
		return planEndDate;
	}

	public void setPlanEndDate(Date planEndDate) {
		this.planEndDate = planEndDate;
	}

	public Date getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	public Date getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}
	public ConstructPlan getConstructPlan() {
		return constructPlan;
	}

	public void setConstructPlan(ConstructPlan constructPlan) {
		this.constructPlan = constructPlan;
	}
}