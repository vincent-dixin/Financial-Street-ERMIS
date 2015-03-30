package com.fhd.icm.entity.icsystem;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.icm.entity.defect.Defect;

/**
 *   合规诊断关联的缺陷
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-28		上午10:15:13
 *
 * @see 	 
 */
@Entity
@Table(name="T_CA_DIAGNOSES_DEFECT")
public class DiagnosesRelaDefect extends IdEntity implements Serializable {
	private static final long serialVersionUID = 6553718014499499082L;

	/**
	 * 合规诊断
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DIAGNOSES_ID")
	private Diagnoses diagnoses;
	
	/**
	 * 缺陷
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFECT_ID")
	private Defect defect;
	/**
	 *   是否认同
	 */
	@Column(name = "IS_AGREE")
	private String isAgree;
	/**
	 *  反馈时间
	 */
	@Column(name = "FEEDBACK_TIME")
	private Date feedbackTime;
	/**
	 *  反馈意见
	 */
	@Column(name = "FEEDBACK_OPTIONS")
	private String feedbackOptions;
	/**
	 * 反馈人
	 */
	@Column(name = "FEEDBACK_BY")
	private String feedbackBy;
	/**
	 * 控制描述
	 */
	@Column(name = "controldesc")
	private String controldesc;
	
	public DiagnosesRelaDefect(){
		
	}
	
	public DiagnosesRelaDefect(String id){
		super.setId(id);
	}

	public Diagnoses getDiagnoses() {
		return diagnoses;
	}

	public String getControldesc() {
		return controldesc;
	}

	public void setControldesc(String controldesc) {
		this.controldesc = controldesc;
	}

	public void setDiagnoses(Diagnoses diagnoses) {
		this.diagnoses = diagnoses;
	}

	public Defect getDefect() {
		return defect;
	}

	public void setDefect(Defect defect) {
		this.defect = defect;
	}

	public String getIsAgree() {
		return isAgree;
	}

	public void setIsAgree(String isAgree) {
		this.isAgree = isAgree;
	}

	public Date getFeedbackTime() {
		return feedbackTime;
	}

	public void setFeedbackTime(Date feedbackTime) {
		this.feedbackTime = feedbackTime;
	}

	public String getFeedbackOptions() {
		return feedbackOptions;
	}

	public void setFeedbackOptions(String feedbackOptions) {
		this.feedbackOptions = feedbackOptions;
	}

	public String getFeedbackBy() {
		return feedbackBy;
	}

	public void setFeedbackBy(String feedbackBy) {
		this.feedbackBy = feedbackBy;
	}

}

