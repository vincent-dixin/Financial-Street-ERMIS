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
 * 评价点
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-26		下午3:41:43
 *
 * @see 	 
 */
@Entity
@Table(name="T_CA_ASSESSMENT_POINT")
public class AssessPoint extends IdEntity implements Serializable {

	private static final long serialVersionUID = 5153488200838098163L;

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
	 * 编号
	 */
	@Column(name="ASSESSMENT_POINT_CODE")
	private String code;
	
	/**
	 * 描述
	 */
	@Column(name="EDESC",length=4000)
	private String desc;
				
	/**
	 * 财报相关科目
	 */
	/*@Column(name="RELA_SUBJECT")
	private String relaSubject;*/
	
	/**
	 * 样本名称
	 */
	@Column(name="SAMPLE_NAME")
	private String assessSampleName;
	
	/**
	 * 实施证据
	 */
	@Column(name="ECOMMENT",length=4000)
	private String comment;
	
	/**
	 * 评价点类型
	 */
	@Column(name="etype")
	private String type;
	
	/**
	 * 评价结果集合
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assessPoint")
	private Set<AssessResult> assessResults = new HashSet<AssessResult>(0);

	public AssessPoint(){
		
	}
	
	public AssessPoint(String id){
		super.setId(id);
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAssessSampleName() {
		return assessSampleName;
	}

	public void setAssessSampleName(String assessSampleName) {
		this.assessSampleName = assessSampleName;
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

	public Set<AssessResult> getAssessResults() {
		return assessResults;
	}

	public void setAssessResults(Set<AssessResult> assessResults) {
		this.assessResults = assessResults;
	}	
}

