package com.fhd.icm.entity.assess;

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
 * 评价计划小组的参与部门，组长，组员
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-26		下午4:04:22
 *
 * @see 	 
 */
@Entity
@Table(name="T_CA_PLAN_ORG_EMP")
public class AssessPlanRelaOrgEmp extends IdEntity implements Serializable {
	private static final long serialVersionUID = -534394837855184025L;

	/**
	 * 评价计划
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAN_ID")
	private AssessPlan assessPlan;
	
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

	public AssessPlanRelaOrgEmp(){
		
	}
	
	public AssessPlanRelaOrgEmp(String id){
		super.setId(id);
	}
	
	public AssessPlan getAssessPlan() {
		return assessPlan;
	}

	public void setAssessPlan(AssessPlan assessPlan) {
		this.assessPlan = assessPlan;
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

