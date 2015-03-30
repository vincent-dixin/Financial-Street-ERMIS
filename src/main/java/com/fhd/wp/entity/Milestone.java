/*
 * 北京第一会达风险管理有限公司 版权所有 2013
 * Copyright(C) 2013 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.wp.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * 计划里程碑
 *
 * @author   胡迪新
 * @since    fhd Ver 4.5
 * @Date	 2013-2-20  上午11:13:10
 *
 * @see 	 
 */
@Entity
@Table(name = "T_WP_MILESTONE")
public class Milestone extends IdEntity {

	/**
	 *
	 * @author 胡迪新
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = -1124256441582147421L;

	/**
	 * 计划
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WORK_PLAN_ID")
	private WorkPlan plan;
	
	/**
	 * 名称
	 */
	@Column(name = "NAME")
	private String name;
	
	/**
	 * 描述
	 */
	@Column(name = "EDESC")
	private String desc;
	
	/**
	 * 完成日期
	 */
	@Column(name = "FINISH_DATE")
	private Date finishDate;

	public WorkPlan getPlan() {
		return plan;
	}

	public void setPlan(WorkPlan plan) {
		this.plan = plan;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}
	
}

