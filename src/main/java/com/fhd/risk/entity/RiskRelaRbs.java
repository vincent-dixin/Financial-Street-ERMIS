/**
 * RiskRelaRbs.java
 * com.fhd.risk.entity
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-29 		张 雷
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.risk.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * 风险事件关联风险类别
 *
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-29		下午4:51:16
 *
 * @see 	 
 */
@Entity
@Table(name = "T_RM_RISK_RELA_RBS")
public class RiskRelaRbs extends IdEntity implements Serializable{
	private static final long serialVersionUID = 8292285896545383008L;

	/**
	 * 风险事件
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "RISK_ID")
	private Risk riskEvent;
	
	/**
	 * 风险类别
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "RBS_ID")
	private Risk rbs;	
	
	/**
	 * 类型 M：主要风险类别；A：相关风险类别
	 */
	@Column(name = "ETYPE")
	private String type;

	
	public Risk getRbs() {
		return rbs;
	}

	public void setRbs(Risk rbs) {
		this.rbs = rbs;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Risk getRiskEvent() {
		return riskEvent;
	}

	public void setRiskEvent(Risk riskEvent) {
		this.riskEvent = riskEvent;
	}
	
	
}

