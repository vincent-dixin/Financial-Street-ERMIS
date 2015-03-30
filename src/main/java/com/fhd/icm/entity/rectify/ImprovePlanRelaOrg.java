/**
 * ImproveRelaOrg.java
 * com.fhd.icm.entity.rectify
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-12-26 		张 雷
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.icm.entity.rectify;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-26		下午4:21:34
 *
 * @see 	 
 */
@Entity
@Table(name="T_RECTIFY_IMPROVE_PLAN_ORG_EMP")
public class ImprovePlanRelaOrg extends IdEntity implements Serializable {
	private static final long serialVersionUID = -884096668508521965L;

	/**
	 * 整改计划
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMPROVE_PLAN_ID")
	private ImprovePlan improvePlan;
	
	/**
	 * 部门
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID")
	private SysOrganization org;
	
	/**
	 * 员工
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMP_ID")
	private SysEmployee emp;
	
	/**
	 * 类型：责任部门，责任人，经办人，复核人,审核人
	 */
	@Column(name="ETYPE")
	private String type;
	
	public ImprovePlanRelaOrg(){
		
	}
	
	public ImprovePlanRelaOrg(String id){
		super.setId(id);
	}

	public ImprovePlan getImprovePlan() {
		return improvePlan;
	}

	public void setImprovePlan(ImprovePlan improvePlan) {
		this.improvePlan = improvePlan;
	}

	public SysOrganization getOrg() {
		return org;
	}

	public void setOrg(SysOrganization org) {
		this.org = org;
	}

	public SysEmployee getEmp() {
		return emp;
	}

	public void setEmp(SysEmployee emp) {
		this.emp = emp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}

