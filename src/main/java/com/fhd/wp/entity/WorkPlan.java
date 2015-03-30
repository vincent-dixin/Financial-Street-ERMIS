/*
 * 北京第一会达风险管理有限公司 版权所有 2013
 * Copyright(C) 2013 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.wp.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.AuditableEntity;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 工作计划
 *
 * @author   胡迪新
 * @since    fhd Ver 4.5
 * @Date	 2013-2-20  上午10:26:39
 *
 * @see 	 
 */
@Entity
@Table(name = "T_WP_PLAN")
public class WorkPlan extends AuditableEntity {
	
	
	/**
	 *
	 * @author 胡迪新
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = 7358689834117905175L;

	/**
	 * 公司
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPAMYID")
	private SysOrganization company;
	
	/**
	 * 名称
	 */
	@Column(name = "NAME")
	private String name;
	
	/**
	 * 编号
	 */
	@Column(name = "CODE")
	private String code;
	
	/**
	 * 责任人
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RESPONSILE_PERSION")
	private SysEmployee responsilePersion;
	
	/**
	 * 目标
	 */
	@Column(name = "TARGET")
	private String target;

	/**
	 * 内容
	 */
	@Column(name = "ECONTENT")
	private String content;

	/**
	 * 考核要求
	 */
	@Column(name = "ASSESS_REQUIREMENT")
	private String assessRequirement;

	/**
	 * 贡献目标额
	 */
	@Column(name = "CONTRIBUTE_TARGET_AMOUNT")
	private Double contributeTargetAmount;

	/**
	 * 开始日期
	 */
	@Column(name = "START_DATE")
	private Date startDate;

	/**
	 * 结束日期
	 */
	@Column(name = "END_DATE")
	private Date endDate;

	/**
	 * 状态
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ESTATUS")
	private DictEntry status;
	
	/**
	 * 删除状态
	 */
	@Column(name = "DELETE_STATUS")
	private String deleteStatus;

	/**
	 * 实施部门
	 */
	@OneToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY ,mappedBy = "workPlan")
	private Set<EnforcementOrgs> enforcementOrgses = new HashSet<EnforcementOrgs>(0);
	
	
	@OneToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY ,mappedBy = "plan")
	private Set<Milestone> milestones = new HashSet<Milestone>(0);
	
	
	public SysOrganization getCompany() {
		return company;
	}

	public void setCompany(SysOrganization company) {
		this.company = company;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public SysEmployee getResponsilePersion() {
		return responsilePersion;
	}

	public void setResponsilePersion(SysEmployee responsilePersion) {
		this.responsilePersion = responsilePersion;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAssessRequirement() {
		return assessRequirement;
	}

	public void setAssessRequirement(String assessRequirement) {
		this.assessRequirement = assessRequirement;
	}

	public Double getContributeTargetAmount() {
		return contributeTargetAmount;
	}

	public void setContributeTargetAmount(Double contributeTargetAmount) {
		this.contributeTargetAmount = contributeTargetAmount;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public DictEntry getStatus() {
		return status;
	}

	public void setStatus(DictEntry status) {
		this.status = status;
	}

	public String getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(String deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public Set<Milestone> getMilestones() {
		return milestones;
	}

	public void setMilestones(Set<Milestone> milestones) {
		this.milestones = milestones;
	}

	public Set<EnforcementOrgs> getEnforcementOrgses() {
		return enforcementOrgses;
	}

	public void setEnforcementOrgses(Set<EnforcementOrgs> enforcementOrgses) {
		this.enforcementOrgses = enforcementOrgses;
	}
	
	
	
	
	
}

