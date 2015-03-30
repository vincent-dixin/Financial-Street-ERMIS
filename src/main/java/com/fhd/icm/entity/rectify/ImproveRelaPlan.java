/*
 * 北京第一会达风险管理有限公司 版权所有 2013
 * Copyright(C) 2013 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.icm.entity.rectify;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * 整改计划_整改方案表
 *
 * @author   胡迪新
 * @since    fhd Ver 4.5
 * @Date	 2013-2-17  上午10:38:03
 *
 * @see 	 
 */
@Entity
@Table(name="T_RECTIFY_IMPROVE_RELA_PLAN")
public class ImproveRelaPlan extends IdEntity {

	
	/**
	 *
	 * @author 胡迪新
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = -6977473229757577533L;

	/**
	 * 整改计划
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMPROVEMENT_ID")	
	private Improve improve;
	
	/**
	 * 整改方案
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMPROVE_PLAN_ID")	
	private ImprovePlan improvePlan;

	public ImproveRelaPlan(){
		
	}
	
	public ImproveRelaPlan(String id){
		super.setId(id);
	}
	
	public Improve getImprove() {
		return improve;
	}

	public void setImprove(Improve improve) {
		this.improve = improve;
	}

	public ImprovePlan getImprovePlan() {
		return improvePlan;
	}

	public void setImprovePlan(ImprovePlan improvePlan) {
		this.improvePlan = improvePlan;
	}
	
	

}

