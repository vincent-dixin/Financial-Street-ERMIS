package com.fhd.icm.entity.icsystem;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 体系建设计划关联部门
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-28		上午10:08:06
 *   MODIFY BY 宋佳
 * @see 	 
 */
@Entity
@Table(name="T_CA_CONST_PLAN_RELA_ST_EMP")
public class ConstructPlanRelaStandardEmp extends IdEntity implements Serializable {
	private static final long serialVersionUID = 4715624272021999625L;

	/**
	 * 计划标准ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAN_RELA_STANDARD_ID")
	private ConstructPlanRelaStandard constructPlanRelaStandard;
	
	/**
	 * 计划涉及部门人员ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAN_RELA_ORG_ID")
	private ConstructPlanRelaOrg constructPlanRelaOrg;
	
	/**
	 * 流程梳理, 
	 * 合规诊断
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ETYPE")
	private DictEntry type;

	public ConstructPlanRelaStandard getConstructPlanRelaStandard() {
		return constructPlanRelaStandard;
	}

	public void setConstructPlanRelaStandard(
			ConstructPlanRelaStandard constructPlanRelaStandard) {
		this.constructPlanRelaStandard = constructPlanRelaStandard;
	}

	public ConstructPlanRelaOrg getConstructPlanRelaOrg() {
		return constructPlanRelaOrg;
	}

	public void setConstructPlanRelaOrg(ConstructPlanRelaOrg constructPlanRelaOrg) {
		this.constructPlanRelaOrg = constructPlanRelaOrg;
	}

	public DictEntry getType() {
		return type;
	}

	public void setType(DictEntry type) {
		this.type = type;
	}		
	
	
}

