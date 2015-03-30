/*
 * 北京第一会达风险管理有限公司 版权所有 2013
 * Copyright(C) 2013 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.wp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 实施机构
 *
 * @author   胡迪新
 * @since    fhd Ver 4.5
 * @Date	 2013-3-6  上午9:44:51
 *
 * @see 	 
 */
@Entity
@Table(name = "T_WP_ENFORCEMENT_ORG")
public class EnforcementOrgs extends IdEntity {

	
	
	/**
	 *
	 * @author 胡迪新
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = 4935029547620688962L;

	/**
	 * 机构
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID")
	private SysOrganization org;
	
	/**
	 * 工作计划
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WORK_PLAN_ID")
	private WorkPlan workPlan;
	
	/**
	 * 状态 当前公司或者部门下里程碑关联计划的进度汇总
	 */
	@Column(name = "STATUS")
	private String status;
	
	/**
	 * 综合达标率
	 */
	@Column(name = "ATTAINMENT_RATE")
	private Double attainmentRate;
	
	/**
	 * 贡献金额
	 */
	@Column(name = "CONTRIBUTE_AMOUNT")
	private Double contributeAmount;
	
	/**
	 * 贡献水平
	 */
	@Column(name = "CONTRIBUTE_LEVEL")
	private Double contributeLevel;

	public SysOrganization getOrg() {
		return org;
	}

	public void setOrg(SysOrganization org) {
		this.org = org;
	}

	public WorkPlan getWorkPlan() {
		return workPlan;
	}

	public void setWorkPlan(WorkPlan workPlan) {
		this.workPlan = workPlan;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getAttainmentRate() {
		return attainmentRate;
	}

	public void setAttainmentRate(Double attainmentRate) {
		this.attainmentRate = attainmentRate;
	}

	public Double getContributeAmount() {
		return contributeAmount;
	}

	public void setContributeAmount(Double contributeAmount) {
		this.contributeAmount = contributeAmount;
	}

	public Double getContributeLevel() {
		return contributeLevel;
	}

	public void setContributeLevel(Double contributeLevel) {
		this.contributeLevel = contributeLevel;
	}
	
	
}

