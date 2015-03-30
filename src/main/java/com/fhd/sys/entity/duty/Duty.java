/**
 * Duty.java
 * com.fhd.fdc.commons.entity.sys.duty
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-12-30 		David
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/
/**
 * Duty.java
 * com.fhd.fdc.commons.entity.sys.duty
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-12-30        David
 *
 * Copyright (c) 2010, FirstHuida All Rights Reserved.
*/


package com.fhd.sys.entity.duty;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 职务实体类.
 * @author   David
 * @version  
 * @since    Ver 1.1
 * @Date	 2010-12-30		上午11:02:03
 * @see 	 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable(true)
@Table(name = "T_SYS_DUTY")
public class Duty extends IdEntity implements Serializable{
	private static final long serialVersionUID = 6457201568605163440L;

	/**
	 * 导入时报错信息
	 */
	@Transient
	private String impErrorInfo;
	
	/**
	 * 职务名称.
	 */
	@Column(name = "DUTY_NAME")
	private String dutyName;
	/**
	 * 是否可用 1：可用 0：不可用.
	 */
	@Column(name = "ESTATUS")
	private String status;
	/**
	 * 备注.
	 */
	@Column(name = "ECOMMENT")
	private String remark;
	/**
	 * 所属公司.
	 */
	@JoinColumn(name = "COMPANY_ID")
	@ManyToOne(fetch = FetchType.LAZY)
	private SysOrganization company;
	/**
	 * 权重
	 */
	@Column(name = "WEIGHT")
	private Double weight;
	
	/**
	 * 职务人员.
	 */
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(cascade = CascadeType.REMOVE ,fetch = FetchType.LAZY, mappedBy = "duty")
	private Set<SysEmployee> employees = new HashSet<SysEmployee>(0);
	
	public Duty() {
	}

	public Duty(String dutyName, String status, String remark,
			SysOrganization company, Double weight, Set<SysEmployee> employees) {
		super();
		this.dutyName = dutyName;
		this.status = status;
		this.remark = remark;
		this.company = company;
		this.weight = weight;
		this.employees = employees;
	}

	public String getDutyName() {
		return dutyName;
	}

	public void setDutyName(String dutyName) {
		this.dutyName = dutyName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public SysOrganization getCompany() {
		return company;
	}

	public void setCompany(SysOrganization company) {
		this.company = company;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}


	public Set<SysEmployee> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<SysEmployee> employees) {
		this.employees = employees;
	}

	public String getImpErrorInfo() {
		return impErrorInfo;
	}

	public void setImpErrorInfo(String impErrorInfo) {
		this.impErrorInfo = impErrorInfo;
	}
}

