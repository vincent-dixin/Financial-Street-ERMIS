package com.fhd.icm.entity.assess;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.icm.entity.control.Measure;
import com.fhd.process.entity.Process;
import com.fhd.process.entity.ProcessPoint;

/**
 * 评价计划的评价点范围
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-26		下午3:59:52
 *
 * @see 	 
 */
@Entity
@Table(name="T_CA_PLAN_ASSESSMENT_POINT")
public class AssessPlanRelaPoint extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1644967648710240836L;

	/**
	 * 评价计划关联的流程
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAN_PROCESSURE_ID")
	private AssessPlanRelaProcess assessPlanRelaProcess;
		
	/**
	 * 流程
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROCESSURE_ID")
	private Process process;
	
	/**
	 * 流程节点
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTROL_POINT_ID")
	private ProcessPoint processPoint;
	
	/**
	 * 控制措施
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEASURE_ID")
	private Measure controlMeasure;
	
	/**
	 * 评价点
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POINT_ID")
	private AssessPoint assessPoint;
	
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
	 * 抽取样本数
	 */
	@Column(name="SAMPLE_NUM")
	private Integer sampleNum;
		
	/**
	 * 抽样覆盖率
	 */
	@Column(name="Coverage_rate")
	private Double coverageRate;
		
	/**
	 * 说明
	 */
	@Column(name="EDESC",length=4000)
	private String desc;


	/**
	 * 评价计划关联评价点的相关人
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assessPlanRelaPoint")
	private Set<AssessPlanPointRelaOrgEmp> assessPlanPointRelaOrgEmp = new HashSet<AssessPlanPointRelaOrgEmp>(0);;

	
	
	public AssessPlanRelaPoint(){
		
	}
	
	public AssessPlanRelaPoint(String id){
		super.setId(id);
	}
	
	public AssessPlanRelaProcess getAssessPlanRelaProcess() {
		return assessPlanRelaProcess;
	}

	public void setAssessPlanRelaProcess(AssessPlanRelaProcess assessPlanRelaProcess) {
		this.assessPlanRelaProcess = assessPlanRelaProcess;
	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public ProcessPoint getProcessPoint() {
		return processPoint;
	}

	public void setProcessPoint(ProcessPoint processPoint) {
		this.processPoint = processPoint;
	}

	public Measure getControlMeasure() {
		return controlMeasure;
	}

	public void setControlMeasure(Measure controlMeasure) {
		this.controlMeasure = controlMeasure;
	}

	public AssessPoint getAssessPoint() {
		return assessPoint;
	}

	public void setAssessPoint(AssessPoint assessPoint) {
		this.assessPoint = assessPoint;
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

	public Integer getSampleNum() {
		return sampleNum;
	}

	public void setSampleNum(Integer sampleNum) {
		this.sampleNum = sampleNum;
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

	public Set<AssessPlanPointRelaOrgEmp> getAssessPlanPointRelaOrgEmp() {
		return assessPlanPointRelaOrgEmp;
	}

	public void setAssessPlanPointRelaOrgEmp(
			Set<AssessPlanPointRelaOrgEmp> assessPlanPointRelaOrgEmp) {
		this.assessPlanPointRelaOrgEmp = assessPlanPointRelaOrgEmp;
	}	
	
}

