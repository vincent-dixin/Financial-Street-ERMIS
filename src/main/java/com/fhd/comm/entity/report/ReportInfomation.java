package com.fhd.comm.entity.report;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.AuditableEntity;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 报告实体类.
 * @author 吴德福
 * @since 2013-03-05 am 11:03
 */
@Entity
@Table(name = "T_REPORT_INFORMATION")
public class ReportInfomation extends AuditableEntity implements Serializable{

	private static final long serialVersionUID = -3594258150802736823L;

    /**
     * 公司
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID")
    private SysOrganization company;
	/**
	 * 版本号
	 */
	@Column(name = "REPORT_CODE")
	private String reportCode;
	/**
	 * 标题
	 */
	@Column(name = "REPORT_NAME")
	private String reportName;
	/**
	 * 报告内容
	 */
	@Lob
	@Column(name = "REPORT_DATA")
	private byte[] reportData;
	/**
	 * 报告类型:测试报告/公司年度评价报告/集团年度评价报告
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_TYPE")
    private DictEntry reportType;
	/**
	 * 状态:保存/提交
	 */
	@Column(name = "ESTATUS")
    private String status;
	
	/**
	 * 执行状态:未开始/处理中/已完成
	 */
	@Column(name = "EXECUTE_STATUS")
    private String executeStatus;
	/**
	 * 生成的报告文件
	 */
	@Lob
	@Column(name = "REPORT_DOC")
	private byte[] reportDoc;
	/**
	 * 排序
	 */
	@Column(name = "ESORT")
    private Integer sort;
	/**
	 * 报告关联评价计划
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "report")
    private Set<ReportRelaAssessment> reportRelaAssessPlans = new HashSet<ReportRelaAssessment>(0);
	
	/**
	 * 报告关联建设计划
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "report")
	private Set<ReportRelaConstructPlan> reportRelaConstructPlans = new HashSet<ReportRelaConstructPlan>(0);

	public ReportInfomation() {
		super();
	}

	public ReportInfomation(SysOrganization company, String reportCode,
			String reportName, byte[] reportData, DictEntry reportType,
			String status, Integer sort,
			Set<ReportRelaAssessment> reportRelaAssessPlans) {
		super();
		this.company = company;
		this.reportCode = reportCode;
		this.reportName = reportName;
		this.reportData = reportData;
		this.reportType = reportType;
		this.status = status;
		this.sort = sort;
		this.reportRelaAssessPlans = reportRelaAssessPlans;
	}

	public SysOrganization getCompany() {
		return company;
	}

	public void setCompany(SysOrganization company) {
		this.company = company;
	}

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public byte[] getReportData() {
		return reportData;
	}

	public void setReportData(byte[] reportData) {
		this.reportData = reportData;
	}

	public DictEntry getReportType() {
		return reportType;
	}

	public void setReportType(DictEntry reportType) {
		this.reportType = reportType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Set<ReportRelaAssessment> getReportRelaAssessPlans() {
		return reportRelaAssessPlans;
	}

	public void setReportRelaAssessPlans(
			Set<ReportRelaAssessment> reportRelaAssessPlans) {
		this.reportRelaAssessPlans = reportRelaAssessPlans;
	}
	public Set<ReportRelaConstructPlan> getReportRelaConstructPlans() {
		return reportRelaConstructPlans;
	}

	public void setReportRelaConstructPlans(
			Set<ReportRelaConstructPlan> reportRelaConstructPlans) {
		this.reportRelaConstructPlans = reportRelaConstructPlans;
	}
	public byte[] getReportDoc() {
		return reportDoc;
	}

	public void setReportDoc(byte[] reportDoc) {
		this.reportDoc = reportDoc;
	}

	public String getExecuteStatus() {
		return executeStatus;
	}

	public void setExecuteStatus(String executeStatus) {
		this.executeStatus = executeStatus;
	}
}
