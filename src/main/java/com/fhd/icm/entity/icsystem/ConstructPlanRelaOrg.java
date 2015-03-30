package com.fhd.icm.entity.icsystem;

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
 * 体系建设计划关联部门
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-28		上午10:08:06
 *   MODIFY BY 宋佳
 * @see 	 
 */
@Entity
@Table(name="T_CA_CONSTRUCTION_PLAN_ORG_EMP")
public class ConstructPlanRelaOrg extends IdEntity implements Serializable {
	private static final long serialVersionUID = 4715624272021999625L;

	/**
	 * 体系建设计划
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAN_ID")
	private ConstructPlan constructPlan;
	
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
	 * 类型：参与部门,组长,组员
	 */
	@Column(name="ETYPE")
	private String type;

	public ConstructPlanRelaOrg(){
		
	}
	
	public ConstructPlanRelaOrg(String id){
		super.setId(id);
	}

	public ConstructPlan getConstructPlan() {
		return constructPlan;
	}

	public void setConstructPlan(ConstructPlan constructPlan) {
		this.constructPlan = constructPlan;
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

