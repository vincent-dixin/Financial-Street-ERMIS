package com.fhd.assess.entity.formulatePlan;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.orgstructure.SysOrganization;
/**
 * 打分部门实体
 * @author 王再冉
 *
 */
@Entity
@Table(name = "T_RM_RISK_SCORE_DEPT")
public class RiskScoreDept extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 打分对象ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCORE_OBJECT_ID")
	private RiskScoreObject scoreObject;
	/**
	 * 部门ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID")
	private SysOrganization organization;
	/**
	 * 部门类型
	 */
	@Column(name = "ORG_TYPE")
	private String orgType;

	public RiskScoreDept() {
	}

	public RiskScoreDept(RiskScoreObject scoreObject, SysOrganization organization,
			String orgType) {
		this.scoreObject = scoreObject;
		this.organization = organization;
		this.orgType = orgType;
	}

	public RiskScoreObject getScoreObject() {
		return scoreObject;
	}

	public void setScoreObject(RiskScoreObject scoreObject) {
		this.scoreObject = scoreObject;
	}

	public SysOrganization getOrganization() {
		return organization;
	}

	public void setOrganization(SysOrganization organization) {
		this.organization = organization;
	}

	public String getOrgType() {
		return this.orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

}

