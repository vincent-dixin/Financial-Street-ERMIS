package com.fhd.icm.entity.assess;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * 评价时添加的样本
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-26		下午4:09:36
 *
 * @see 	 
 */
@Entity
@Table(name="T_CA_SAMPLE")
public class AssessSample extends IdEntity implements Serializable {
	private static final long serialVersionUID = 6324970077079339150L;

	/**
	 * 评价计划
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAN_ID")
	private AssessPlan assessPlan;
	
	/**
	 * 评价结果
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASSESSMENT_POINT_ID")
	private AssessResult assessResult;
		
	/**
	 * 编号
	 */
	@Column(name="SAMPLE_CODE")
	private String code;
	
	/**
	 * 名称
	 */
	@Column(name="SAMPLE_NAME")
	private String name;
	
	/**
	 * 合格/不合格/不适用
	 */
	@Column(name="ERESULT")
	private String isQualified;		
			
	/**
	 * 说明
	 */
	@Column(name="ECOMMENT",length=4000)
	private String comment;			
		
	/**
	 * 类型：自动/补充
	 */
	@Column(name="ETYPE")
	private String type;		
	
	/**
	 * 补充样本的来源样本
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SOURCE_SAMPLE_ID")
	private AssessSample sourceSample;
	
	public AssessSample(){
		
	}
	
	public AssessSample(String id){
		super.setId(id);
	}

	public AssessPlan getAssessPlan() {
		return assessPlan;
	}

	public void setAssessPlan(AssessPlan assessPlan) {
		this.assessPlan = assessPlan;
	}

	public AssessResult getAssessResult() {
		return assessResult;
	}

	public void setAssessResult(AssessResult assessResult) {
		this.assessResult = assessResult;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsQualified() {
		return isQualified;
	}

	public void setIsQualified(String isQualified) {
		this.isQualified = isQualified;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public AssessSample getSourceSample() {
		return sourceSample;
	}

	public void setSourceSample(AssessSample sourceSample) {
		this.sourceSample = sourceSample;
	}
	
}

