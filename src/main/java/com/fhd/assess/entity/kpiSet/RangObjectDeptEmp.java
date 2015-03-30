package com.fhd.assess.entity.kpiSet;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.assess.entity.formulatePlan.RiskScoreDept;
import com.fhd.assess.entity.formulatePlan.RiskScoreObject;
import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.orgstructure.SysEmployee;
/**
 * 范围-对象-部门-人员综合表
 * @author 王再冉
 *
 */
@Entity
@Table(name = "T_RM_RANG_OBJECT_DEPT_EMP")
public class RangObjectDeptEmp extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 打分范围ID
	 */
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCORE_RANGE_ID")
	private RiskScoreRange scoreRange;*/
	/**
	 * 打分对象ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCORE_OBJECT_ID")
	private RiskScoreObject scoreObject;
	/**
	 * 打分部门ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCORE_DEPT_ID")
	private RiskScoreDept scoreDept;
	/**
	 * 打分人员ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCORE_EMP_ID")
	private SysEmployee scoreEmp;
	/**
	 * 打分范围名称
	 */
	@Column(name = "SCORE_RANGE_NAME")
	private String scoreRangeName;
	/**
	 * 打分对象名称
	 */
	@Column(name = "SCORE_OBJECT_NAME")
	private String scoreObjectName;
	/**
	 * 打分部门名称
	 */
	@Column(name = "SCORE_ORG_NAME")
	private String scoreOrgName;
	/**
	 * 打分人员名称
	 */
	@Column(name = "SCORE_EMP_NAME")
	private String scoreEmpName;

	public RangObjectDeptEmp() {
	}

	public RangObjectDeptEmp(RiskScoreObject scoreObject, RiskScoreDept scoreDept, SysEmployee scoreEmp,
			String scoreRangeName, String scoreObjectName, String scoreOrgName,
			String scoreEmpName) {
		this.scoreObject = scoreObject;
		this.scoreDept = scoreDept;
		this.scoreEmp = scoreEmp;
		this.scoreRangeName = scoreRangeName;
		this.scoreObjectName = scoreObjectName;
		this.scoreOrgName = scoreOrgName;
		this.scoreEmpName = scoreEmpName;
	}

	public RiskScoreObject getScoreObject() {
		return scoreObject;
	}

	public void setScoreObject(RiskScoreObject scoreObject) {
		this.scoreObject = scoreObject;
	}

	public RiskScoreDept getScoreDept() {
		return scoreDept;
	}

	public void setScoreDept(RiskScoreDept scoreDept) {
		this.scoreDept = scoreDept;
	}

	public SysEmployee getScoreEmp() {
		return scoreEmp;
	}

	public void setScoreEmp(SysEmployee scoreEmp) {
		this.scoreEmp = scoreEmp;
	}

	public String getScoreRangeName() {
		return this.scoreRangeName;
	}

	public void setScoreRangeName(String scoreRangeName) {
		this.scoreRangeName = scoreRangeName;
	}

	public String getScoreObjectName() {
		return this.scoreObjectName;
	}

	public void setScoreObjectName(String scoreObjectName) {
		this.scoreObjectName = scoreObjectName;
	}

	public String getScoreOrgName() {
		return this.scoreOrgName;
	}

	public void setScoreOrgName(String scoreOrgName) {
		this.scoreOrgName = scoreOrgName;
	}

	public String getScoreEmpName() {
		return this.scoreEmpName;
	}

	public void setScoreEmpName(String scoreEmpName) {
		this.scoreEmpName = scoreEmpName;
	}

}

