package com.fhd.comm.entity.report;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.icm.entity.assess.AssessPlan;

/**
 * 内控报告与评价计划关联实体类.
 * @author 吴德福
 * @since 2013-03-05 am 11:03
 */
@Entity
@Table(name = "T_REPORT_RELA_ASSESSMENT")
public class ReportRelaAssessment extends IdEntity implements Serializable{

	private static final long serialVersionUID = 3636320657447843669L;

	/**
	 * 评价计划
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASSESSMENT_PLAN_ID")
	private AssessPlan assessPlan;
	/**
	 * 报告
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REPORT_ID")
	private ReportInfomation report;
	/**
	 * 类型：评价报告/整改报告
	 */
    @Column(name = "ETYPE")
    private String type;
	

	public AssessPlan getAssessPlan() {
		return assessPlan;
	}

	public void setAssessPlan(AssessPlan assessPlan) {
		this.assessPlan = assessPlan;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ReportInfomation getReport() {
		return report;
	}

	public void setReport(ReportInfomation report) {
		this.report = report;
	}
}
