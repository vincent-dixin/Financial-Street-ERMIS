package com.fhd.assess.entity.quaAssess;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.assess.entity.kpiSet.RangObjectDeptEmp;
import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.risk.entity.Dimension;
import com.fhd.risk.entity.Score;

/**
 * 打分结果
 */
@Entity
@Table(name = "T_RM_SCORE_RESULT")
public class ScoreResult  extends IdEntity implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**维度*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCORE_DIM_ID")
	private Dimension dimension;
	
	/**维度分值*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCORE_DIC_ID")
	private Score score;
	
	/**范围-对象-部门-人员综合表ID*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RANG_OBJECT_DEPT_EMP_ID")
	private RangObjectDeptEmp rangObjectDeptEmpId;
	
	/**提交时间*/
	@Column(name = "SUBMIT_TIME")
	private Date submitTime;
	
	/**是否审批通过*/
	@Column(name = "IS_APPROVAL")
	private boolean isApproval;
	
	/**状态*/
	@Column(name = "STATUS")
	private String status;

	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public Score getScore() {
		return score;
	}

	public void setScore(Score score) {
		this.score = score;
	}

	public RangObjectDeptEmp getRangObjectDeptEmpId() {
		return rangObjectDeptEmpId;
	}

	public void setRangObjectDeptEmpId(RangObjectDeptEmp rangObjectDeptEmpId) {
		this.rangObjectDeptEmpId = rangObjectDeptEmpId;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public boolean isApproval() {
		return isApproval;
	}

	public void setApproval(boolean isApproval) {
		this.isApproval = isApproval;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}