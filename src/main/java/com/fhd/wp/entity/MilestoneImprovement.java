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
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 里程碑关联子计划
 *
 * @author   胡迪新
 * @since    fhd Ver 4.5
 * @Date	 2013-3-12  上午10:35:38
 *
 * @see 	 
 */
@Entity
@Table(name = "T_WP_MILESTONE_RE_IMPROVEMENT")
public class MilestoneImprovement extends IdEntity {

	
	private static final long serialVersionUID = -1478529724638048383L;

	/**
	 * 机构
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID")
	private SysOrganization org;
	
	/**
	 * 里程碑
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MILESTONE_ID")
	private Milestone milestone;
	
	/**
	 * 子计划
	 */
	@Column(name = "SUB_PLAN_ID")
	private String subPlanId;
	
	/**
	 * 完成情况
	 */
	@Column(name = "FINISH_CONDITION")
	private Double finishCondition;
	
	/**
	 * 类型： 评价计划、整改计划
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ETYPE")
	private DictEntry type;

	/**
	 * 状态：完成、未完成、逾期完成、逾期未完成
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ESTATUS")
	private DictEntry status;

	public SysOrganization getOrg() {
		return org;
	}

	public void setOrg(SysOrganization org) {
		this.org = org;
	}

	public Milestone getMilestone() {
		return milestone;
	}

	public void setMilestone(Milestone milestone) {
		this.milestone = milestone;
	}

	public String getSubPlanId() {
		return subPlanId;
	}

	public void setSubPlanId(String subPlanId) {
		this.subPlanId = subPlanId;
	}

	public Double getFinishCondition() {
		return finishCondition;
	}

	public void setFinishCondition(Double finishCondition) {
		this.finishCondition = finishCondition;
	}

	public DictEntry getType() {
		return type;
	}

	public void setType(DictEntry type) {
		this.type = type;
	}

	public DictEntry getStatus() {
		return status;
	}

	public void setStatus(DictEntry status) {
		this.status = status;
	}
	
	
}

