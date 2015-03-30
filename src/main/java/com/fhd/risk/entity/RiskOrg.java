/**
 * RiskOrg.java
 * com.fhd.risk.entity
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-2 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * RiskOrg.java
 * com.fhd.risk.entity
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-2        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
*/


package com.fhd.risk.entity;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;
import com.fhd.sys.entity.orgstructure.SysPosition;

/**
 * 风险、部门关联表实体
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-2		下午01:24:17
 *
 * @see 	 
 */
@Entity
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@Cacheable(true)
@Table(name = "T_RM_RISK_ORG")
public class RiskOrg extends IdEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 风险事件
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RISK_ID")
	private Risk risk;
	
	
	/**
	 * 类型 M：主要风险类别；A：相关风险类别
	 */
	@Column(name = "ETYPE")
	private String type;
	
	/**
	 * 机构
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID")
	private SysOrganization sysOrganization;
	
	/**
	 * 岗位
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POSITION_ID")
	private SysPosition sysPosition;
	
	/**
	 * 人员
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMP_ID")
	private SysEmployee emp;
	
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public SysOrganization getSysOrganization() {
		return sysOrganization;
	}

	public void setSysOrganization(SysOrganization sysOrganization) {
		this.sysOrganization = sysOrganization;
	}

	public SysPosition getSysPosition() {
		return sysPosition;
	}

	public void setSysPosition(SysPosition sysPosition) {
		this.sysPosition = sysPosition;
	}

	public SysEmployee getEmp() {
		return emp;
	}

	public void setEmp(SysEmployee emp) {
		this.emp = emp;
	}

	public Risk getRisk() {
		return risk;
	}

	public void setRisk(Risk risk) {
		this.risk = risk;
	}

	
}