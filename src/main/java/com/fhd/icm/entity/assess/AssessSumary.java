package com.fhd.icm.entity.assess;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.icm.entity.control.Measure;
import com.fhd.process.entity.Process;
import com.fhd.process.entity.ProcessPoint;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 评价结果汇总
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-26		下午3:48:42
 *
 * @see 	 
 */
@Entity
@Table(name="T_CA_ASSESSMENT_SUMARY")
public class AssessSumary extends IdEntity implements Serializable {
	private static final long serialVersionUID = 4439369192760131895L;

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
	 * 评价方式:穿行测试,抽样测试
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASSESSMENT_MEASURE")
	private DictEntry assessMeasure;	

	/**
	 * 本次需要抽取样本数量
	 */
	@Column(name="TO_EXTRACT_AMOUNT")
	private Integer toExtractAmount;
			
	/**
	 * 本次已抽取的样本数量
	 */
	@Column(name="EXTRACTED_AMOUNT")
	private Integer extractedAmount;
	
	/**
	 * 有效样本数
	 */
	@Column(name="EFFECTIVE_NUMBER")
	private Integer effectiveNumber;
	
	/**
	 * 无效样本数
	 */
	@Column(name="DEFECT_NUMBER")
	private Integer defectNumber;
		
	/**
	 * 是否存在缺陷
	 */
	@Column(name="HAS_DEFECT")
	private Boolean hasDefect;

	
	/**
	 * 说明
	 */
	@Column(name="ECOMMENT",length=4000)
	private String comment;	

	public AssessSumary(){
		
	}
	
	public AssessSumary(String id){
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

	public DictEntry getAssessMeasure() {
		return assessMeasure;
	}

	public void setAssessMeasure(DictEntry assessMeasure) {
		this.assessMeasure = assessMeasure;
	}

	public Integer getToExtractAmount() {
		return toExtractAmount;
	}

	public void setToExtractAmount(Integer toExtractAmount) {
		this.toExtractAmount = toExtractAmount;
	}

	public Integer getExtractedAmount() {
		return extractedAmount;
	}

	public void setExtractedAmount(Integer extractedAmount) {
		this.extractedAmount = extractedAmount;
	}

	public Integer getEffectiveNumber() {
		return effectiveNumber;
	}

	public void setEffectiveNumber(Integer effectiveNumber) {
		this.effectiveNumber = effectiveNumber;
	}

	public Integer getDefectNumber() {
		return defectNumber;
	}

	public void setDefectNumber(Integer defectNumber) {
		this.defectNumber = defectNumber;
	}

	public Boolean getHasDefect() {
		return hasDefect;
	}

	public void setHasDefect(Boolean hasDefect) {
		this.hasDefect = hasDefect;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}

