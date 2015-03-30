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
import com.fhd.icm.entity.defect.Defect;
import com.fhd.process.entity.Process;
import com.fhd.process.entity.ProcessPoint;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 评价关联缺陷
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-26		下午3:52:16
 *
 * @see 	 
 */
@Entity
@Table(name="T_CA_DEFECT_ASSESSMENT")
public class AssessRelaDefect extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1114013606790252097L;

	/**
	 * 评价计划
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAN_ID")
	private AssessPlan assessPlan;
	
	/**
	 * 缺陷
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFECT_ID")
	private Defect defect;
	
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
	 * 部门
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID")
	private SysOrganization org;
	
	/**
	  * 是否同意
	  */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IS_AGREE")
	private DictEntry isAgree;
	
	/**
	 * 反馈意见
	 */
	@Column(name = "FEEDBACK")
	private String feedback;
		
	public AssessRelaDefect(){
		
	}
	
	public AssessRelaDefect(String id){
		super.setId(id);
	}

	public AssessPlan getAssessPlan() {
		return assessPlan;
	}

	public void setAssessPlan(AssessPlan assessPlan) {
		this.assessPlan = assessPlan;
	}

	public Defect getDefect() {
		return defect;
	}

	public void setDefect(Defect defect) {
		this.defect = defect;
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

	public SysOrganization getOrg() {
		return org;
	}

	public void setOrg(SysOrganization org) {
		this.org = org;
	}

	public DictEntry getIsAgree() {
		return isAgree;
	}

	public void setIsAgree(DictEntry isAgree) {
		this.isAgree = isAgree;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
}

