package com.fhd.icm.entity.assess;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 评价参与人
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-27		下午5:44:48
 *
 * @see 	 
 */
@Entity
@Table(name="T_CA_ASSESSMENT_ASSESSOR")
public class Assessor extends IdEntity implements Serializable {
	private static final long serialVersionUID = -172916518189724002L;

	/**
	 * 评价计划
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAN_ID")
	private AssessPlan assessPlan;
	
	/**
	 * 员工
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OPERATORID")
	private SysEmployee emp;
	
	/**
	 * 部门
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORGID")
	private SysOrganization org;
	
	/**
	 * 完成期限
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "DEADLINE", length = 7)
	private Date deadLine;		
	
	/**
	 * 实际完成日期
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "FINISHDATE", length = 7)
	private Date actualFinishDate;			
	
	/**
	 * 状态
	 */
	@Column(name="ESTATUS")
	private String status;	
	
	/**
	 * 计划开始日期
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "PLAN_START_DATE", length = 7)
	private Date planStartDate;	
	
	/**
	 * 结果分析
	 */
	@Column(name = "RESULT_ANALYSIS")
	private String resultAnalysis;	
	
	/**
	 * 参评人所负责的评价结果信息
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assessor")
	private Set<AssessResult> assessResult = new HashSet<AssessResult>(0);;

	public Assessor(){
		
	}
	
	public Assessor(String id){
		super.setId(id);
	}

	public AssessPlan getAssessPlan() {
		return assessPlan;
	}

	public void setAssessPlan(AssessPlan assessPlan) {
		this.assessPlan = assessPlan;
	}

	public SysEmployee getEmp() {
		return emp;
	}

	public void setEmp(SysEmployee emp) {
		this.emp = emp;
	}

	public SysOrganization getOrg() {
		return org;
	}

	public void setOrg(SysOrganization org) {
		this.org = org;
	}

	public Date getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(Date deadLine) {
		this.deadLine = deadLine;
	}

	public Date getActualFinishDate() {
		return actualFinishDate;
	}

	public void setActualFinishDate(Date actualFinishDate) {
		this.actualFinishDate = actualFinishDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<AssessResult> getAssessResult() {
		return assessResult;
	}

	public void setAssessResult(Set<AssessResult> assessResult) {
		this.assessResult = assessResult;
	}

	public Date getPlanStartDate() {
		return planStartDate;
	}

	public void setPlanStartDate(Date planStartDate) {
		this.planStartDate = planStartDate;
	}

	public String getResultAnalysis() {
		return resultAnalysis;
	}

	public void setResultAnalysis(String resultAnalysis) {
		this.resultAnalysis = resultAnalysis;
	}
}

