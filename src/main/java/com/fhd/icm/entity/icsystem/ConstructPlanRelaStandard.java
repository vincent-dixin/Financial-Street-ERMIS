/**
 * ConstructPlanRelaStandard.java
 * com.fhd.icm.entity.icsystem
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-12-28 		张 雷
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.icm.entity.icsystem;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.icm.entity.standard.Standard;

/**
 * 体系建设关联内控标准
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-28		上午10:12:10
 *
 * @see 	 
 */
@Entity
@Table(name="T_CA_CONST_PLAN_RELA_STANDARD")
public class ConstructPlanRelaStandard extends IdEntity implements Serializable {
	private static final long serialVersionUID = -1553785252862823450L;

	/**
	 * 体系建设计划
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAN_ID")
	private ConstructPlan constructPlan;
	
	/**
	 * 内控标准：type：1
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STANDARD_ID")
	private Standard standard;
	/**
	 *   是否流程梳理
	 */
	@Column(name="IS_PROCESS_EDIT")
	private Boolean isProcessEdit;
	/**
	 *   是否合规诊断
	 */
	@Column(name="IS_NORMALLY_DIAGNOSIS")
	private Boolean isNormallyDiagnosis;
	
	/**
	 *  备注
	 */
	@Column(name="ECOMMENT")
	private String ecomment;
	
	
	public ConstructPlanRelaStandard(){
		
	}
	
	public ConstructPlanRelaStandard(String id){
		super.setId(id);
	}

	public ConstructPlan getConstructPlan() {
		return constructPlan;
	}

	public void setConstructPlan(ConstructPlan constructPlan) {
		this.constructPlan = constructPlan;
	}

	public Standard getStandard() {
		return standard;
	}

	public void setStandard(Standard standard) {
		this.standard = standard;
	}

	public Boolean getIsProcessEdit() {
		return isProcessEdit;
	}

	public void setIsProcessEdit(Boolean isProcessEdit) {
		this.isProcessEdit = isProcessEdit;
	}

	public Boolean getIsNormallyDiagnosis() {
		return isNormallyDiagnosis;
	}

	public void setIsNormallyDiagnosis(Boolean isNormallyDiagnosis) {
		this.isNormallyDiagnosis = isNormallyDiagnosis;
	}

	public String getEcomment() {
		return ecomment;
	}

	public void setEcomment(String ecomment) {
		this.ecomment = ecomment;
	}


	
}

