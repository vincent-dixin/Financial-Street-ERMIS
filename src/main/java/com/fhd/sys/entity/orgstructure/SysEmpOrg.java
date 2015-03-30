/**
 * SysEmpOrg.java
 * com.fhd.fdc.commons.entity.sys.orgstructure
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-9-16 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.entity.orgstructure;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * 机构员工实体类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-9-8
 * Company FirstHuiDa.
 */
@Entity
@Table(name = "T_SYS_EMP_ORG")
public class SysEmpOrg extends IdEntity implements java.io.Serializable{

	private static final long serialVersionUID = 6569723119513078266L;
	
	/**
	 * 员工.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMP_ID")
	private SysEmployee sysEmployee;
	/**
	 * 机构.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID")
	private SysOrganization sysOrganization;
	/**
	 * 是否主机构.
	 */
	@Column(name = "ISMAIN")
	private Boolean ismain;
	
	public SysEmpOrg() {
	}

	public SysEmpOrg(SysEmployee sysEmployee, SysOrganization sysOrganization,
			Boolean ismain) {
		super();
		this.sysEmployee = sysEmployee;
		this.sysOrganization = sysOrganization;
		this.ismain = ismain;
	}

	public SysEmployee getSysEmployee() {
		return sysEmployee;
	}

	public void setSysEmployee(SysEmployee sysEmployee) {
		this.sysEmployee = sysEmployee;
	}

	public SysOrganization getSysOrganization() {
		return sysOrganization;
	}

	public void setSysOrganization(SysOrganization sysOrganization) {
		this.sysOrganization = sysOrganization;
	}

	public Boolean getIsmain() {
		return ismain;
	}

	public void setIsmain(Boolean ismain) {
		this.ismain = ismain;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}

