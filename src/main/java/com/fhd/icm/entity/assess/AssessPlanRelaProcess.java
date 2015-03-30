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
import com.fhd.process.entity.Process;

/**
 * 评价计划的流程范围
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-26		下午4:08:20
 *
 * @see 	 
 */
@Entity
@Table(name="T_CA_PLAN_PROCESSURE")
public class AssessPlanRelaProcess extends IdEntity implements Serializable {
	private static final long serialVersionUID = -8581824052678757767L;

	/**
	 * 评价计划
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAN_ID")
	private AssessPlan assessPlan;
	
	/**
	 * 流程
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROCESSURE_ID")
	private Process process;

	/**
	 * 是否穿行测试
	 */
	@Column(name="IS_PRACTICE_TEST")
	private Boolean isPracticeTest;
	
	/**
	 * 是否抽样测试
	 */
	@Column(name="Is_sample_test")
	private Boolean isSampleTest;
	
	/**
	 * 穿行次数
	 */
	@Column(name="PRACTICE_NUM")
	private Integer practiceNum;	
		
	/**
	 * 抽样比例
	 */
	@Column(name="Coverage_rate")
	private Double coverageRate;
		
	/**
	 * 说明
	 */
	@Column(name="EDESC",length=4000)
	private String desc;
	
	/**
	 * 执行状态:未开始/已评价/已复核
	 */
	@Column(name="EXECUTE_STATUS")
	private String executeStatus;
	
	/**
	 * 自动机算：完全符合/部分符合/不符合/不适用
	 */
	@Column(name="IS_DESIRABLE")
	private String isDesirable;
	
	/**
	 * 手动调整：完全符合/部分符合/不符合/不适用
	 */
	@Column(name="IS_DESIRABLE_ADJUST")
	private String isDesirableAdjust;
	
	/**
	 * 调整说明
	 */
	@Column(name="ASSESSEMENT_DESC",length=4000)
	private String assessmentDesc;
	
	/**
	 * 流程开始时间
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "PLAN_START_DATE", length = 7)
	private Date planStartDate;
	
	/**
	 * 流程线束时间
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "PLAN_END_DATE", length = 7)
	private Date planEndDate;
	
	/**
	 * 评价时间
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "ASSESS_DATE", length = 7)
	private Date assessDate;
	
	/**
	 * 复核时间
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "REVIEW_DATE", length = 7)
	private Date reviewDate;
	
	/**
	 * 评价的流程关联的评价点
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assessPlanRelaProcess")
	private Set<AssessPlanRelaPoint> assessPlanRelaPoint = new HashSet<AssessPlanRelaPoint>(0);
	
	/**
	 *  评价该流程的评价人复核人信息
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assessPlanRelaProcess")
	private Set<AssessPlanProcessRelaOrgEmp> assessPlanProcessRelaOrgEmp = new HashSet<AssessPlanProcessRelaOrgEmp>(0);
	
	
	public AssessPlanRelaProcess(){
		
	}
	
	public AssessPlanRelaProcess(String id){
		super.setId(id);
	}
	
	public AssessPlan getAssessPlan() {
		return assessPlan;
	}

	public void setAssessPlan(AssessPlan assessPlan) {
		this.assessPlan = assessPlan;
	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public Boolean getIsPracticeTest() {
		return isPracticeTest;
	}

	public void setIsPracticeTest(Boolean isPracticeTest) {
		this.isPracticeTest = isPracticeTest;
	}

	public Boolean getIsSampleTest() {
		return isSampleTest;
	}

	public void setIsSampleTest(Boolean isSampleTest) {
		this.isSampleTest = isSampleTest;
	}

	public Integer getPracticeNum() {
		return practiceNum;
	}

	public void setPracticeNum(Integer practiceNum) {
		this.practiceNum = practiceNum;
	}

	public Double getCoverageRate() {
		return coverageRate;
	}

	public void setCoverageRate(Double coverageRate) {
		this.coverageRate = coverageRate;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public String getIsDesirable() {
		return isDesirable;
	}

	public void setIsDesirable(String isDesirable) {
		this.isDesirable = isDesirable;
	}

	public String getIsDesirableAdjust() {
		return isDesirableAdjust;
	}

	public void setIsDesirableAdjust(String isDesirableAdjust) {
		this.isDesirableAdjust = isDesirableAdjust;
	}
	
	public String getAssessmentDesc() {
		return assessmentDesc;
	}

	public void setAssessmentDesc(String assessmentDesc) {
		this.assessmentDesc = assessmentDesc;
	}

	public Set<AssessPlanRelaPoint> getAssessPlanRelaPoint() {
		return assessPlanRelaPoint;
	}

	public void setAssessPlanRelaPoint(Set<AssessPlanRelaPoint> assessPlanRelaPoint) {
		this.assessPlanRelaPoint = assessPlanRelaPoint;
	}

	public Set<AssessPlanProcessRelaOrgEmp> getAssessPlanProcessRelaOrgEmp() {
		return assessPlanProcessRelaOrgEmp;
	}

	public void setAssessPlanProcessRelaOrgEmp(
			Set<AssessPlanProcessRelaOrgEmp> assessPlanProcessRelaOrgEmp) {
		this.assessPlanProcessRelaOrgEmp = assessPlanProcessRelaOrgEmp;
	}

	public Date getPlanStartDate() {
		return planStartDate;
	}

	public void setPlanStartDate(Date planStartDate) {
		this.planStartDate = planStartDate;
	}

	public Date getPlanEndDate() {
		return planEndDate;
	}

	public void setPlanEndDate(Date planEndDate) {
		this.planEndDate = planEndDate;
	}

	public String getExecuteStatus() {
		return executeStatus;
	}

	public void setExecuteStatus(String executeStatus) {
		this.executeStatus = executeStatus;
	}

	public Date getAssessDate() {
		return assessDate;
	}

	public void setAssessDate(Date assessDate) {
		this.assessDate = assessDate;
	}

	public Date getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(Date reviewDate) {
		this.reviewDate = reviewDate;
	}
}

