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

import com.fhd.fdc.commons.orm.hibernate.AuditableEntity;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 评价计划
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-26		下午3:31:07
 *
 * @see 	 
 */
@Entity
@Table(name="T_CA_ASSESSMENT_PLAN")
public class AssessPlan extends AuditableEntity implements Serializable {
	
	private static final long serialVersionUID = -2175735039413978750L;
	
	/**
	 * 所属公司
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPANY_ID")
	private SysOrganization company;
	
	/**
	 * 编号
	 */
	@Column(name="PLAN_CODE")
	private String code;
	
	/**
	 * 名称
	 */
	@Column(name="PLAN_NAME")
	private String name;
	
	/**
	 * 评价方式:穿行测试,抽样测试,全部
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASSESSMENT_MEASURE")
	private DictEntry assessMeasure;		
		
	/**
	 * 范围要求
	 */
	@Column(name="ASSESSMENT_STANDARD")
	private String assessStandard;
	
	/**
	 * 评价类型:自评,他评
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ETYPE")
	private DictEntry type;			
	
	/**
	 * 考核要求
	 */
	@Column(name="EDESC",length=4000)
	private String desc;
		
	/**
	 * 评价期始
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "TARGET_START_DATE", length = 7)
	private Date targetStartDate;
	
	/**
	 * 评价期止
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "TARGET_END_DATE", length = 7)
	private Date targetEndDate;			
		
	/**
	 * 计划开始日期
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "ASSESSMENT_START_DATE", length = 7)
	private Date planStartDate;
	
	/**
	 * 计划结束日期
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "ASSESSMENT_END_DATE", length = 7)
	private Date planEndDate;		
	
	/**
	 * 实际开始日期
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "ACTUAL_START_DATE", length = 7)
	private Date actualStartDate;
	
	/**
	 * 实际结束日期
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "ACTUAL_END_DATE", length = 7)
	private Date actualEndDate;	
	
	/**
	 * 评价目标
	 */
	@Column(name="ASSESSMENT_TARGET")
	private String assessTarget;	
		
	/**
	 * 评价依据
	 */
	@Column(name="REQUIREMENT")
	private String requirement;	
	
	/**
	 * 排序
	 */
	@Column(name="ESORT")
	private Integer sort;	
	
	/**
	 * 执行状态
	 */
	@Column(name="EXECUTE_STATUS")
	private String dealStatus;	
	
	/**
	 * 状态
	 */
	@Column(name="ESTATUS")
	private String status;	
	
	/**
	 * 删除状态:0已删除,1已启用
	 */
	@Column(name="DELETE_STATUS")
	private String deleteStatus;
	
	/**
	 * 评价计划的流程范围
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assessPlan")
	private Set<AssessPlanRelaProcess> assessPlanRelaProcess = new HashSet<AssessPlanRelaProcess>(0);

	/**
	 * 评价计划涉及的部门和人员
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assessPlan")
	private Set<AssessPlanRelaOrgEmp> assessPlanRelaOrgEmp = new HashSet<AssessPlanRelaOrgEmp>(0);
	
	/**
	 * 评价计划涉及的部门和人员
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assessPlan")
	private Set<Assessor> assessor = new HashSet<Assessor>(0);
	
	public AssessPlan(){
	}
	
	public AssessPlan(String id){
		super.setId(id);
	}

	public SysOrganization getCompany() {
		return company;
	}

	public void setCompany(SysOrganization company) {
		this.company = company;
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

	public DictEntry getAssessMeasure() {
		return assessMeasure;
	}

	public void setAssessMeasure(DictEntry assessMeasure) {
		this.assessMeasure = assessMeasure;
	}

	public String getAssessStandard() {
		return assessStandard;
	}

	public void setAssessStandard(String assessStandard) {
		this.assessStandard = assessStandard;
	}

	public DictEntry getType() {
		return type;
	}

	public void setType(DictEntry type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getTargetStartDate() {
		return targetStartDate;
	}

	public void setTargetStartDate(Date targetStartDate) {
		this.targetStartDate = targetStartDate;
	}

	public Date getTargetEndDate() {
		return targetEndDate;
	}

	public void setTargetEndDate(Date targetEndDate) {
		this.targetEndDate = targetEndDate;
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

	public Date getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	public Date getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	public String getAssessTarget() {
		return assessTarget;
	}

	public void setAssessTarget(String assessTarget) {
		this.assessTarget = assessTarget;
	}

	public String getRequirement() {
		return requirement;
	}

	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(String deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public String getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}

	public Set<AssessPlanRelaProcess> getAssessPlanRelaProcess() {
		return assessPlanRelaProcess;
	}

	public void setAssessPlanRelaProcess(
			Set<AssessPlanRelaProcess> assessPlanRelaProcess) {
		this.assessPlanRelaProcess = assessPlanRelaProcess;
	}

	public Set<AssessPlanRelaOrgEmp> getAssessPlanRelaOrgEmp() {
		return assessPlanRelaOrgEmp;
	}

	public void setAssessPlanRelaOrgEmp(
			Set<AssessPlanRelaOrgEmp> assessPlanRelaOrgEmp) {
		this.assessPlanRelaOrgEmp = assessPlanRelaOrgEmp;
	}
	public Set<Assessor> getAssessor() {
		return assessor;
	}

	public void setAssessor(Set<Assessor> assessor) {
		this.assessor = assessor;
	}
}

