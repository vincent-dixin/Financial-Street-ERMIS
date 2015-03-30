package com.fhd.icm.entity.icsystem;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fhd.fdc.commons.orm.hibernate.AuditableEntity;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 体系建设计划
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-28		上午9:49:54
 *   modify by 宋佳 2013-4-2
 * @see 	 
 */
@Entity
@Table(name="T_CA_SYSTEM_CONSTRUCTION_PLAN")
public class ConstructPlan extends AuditableEntity implements Serializable {
	private static final long serialVersionUID = 7453418427084503919L;

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
	 * 计划开始日期
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "PLAN_START_DATE", length = 7)
	private Date planStartDate;
	
	/**
	 * 计划结束日期
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "PLAN_FINISH_DATE", length = 7)
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
	@Column(name = "ACTUAL_FINISH_DATE", length = 7)
	private Date actualEndDate;	
	
	/**
	 * 工作要求
	 */
	@Column(name="REQUIREMENTS",length=4000)
	private String requirement;		
	/**
	 * 工作目标
	 */
	@Column(name="WORKTARGET",length=4000)
	private String workTarget;		
		
	/**
	 * 流程梳理, 
	 * 合规诊断
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ETYPE")
	private DictEntry type;		
	
	/**
	 * 说明
	 */
	@Column(name="ECOMMENT",length=4000)
	private String comment;		
	/**
	 * 排序
	 */
	@Column(name="ESORT")
	private Integer sort;			
	
	/**
	 * 执行状态
	 */
	@Column(name="DEAL_STATUS")
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
	
	///////宋佳增加内容
	/**
	 *     更改建设原因
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="MODIFY_REASON")
	private DictEntry modifyReason;
	
	/////宋佳增加结束
	public ConstructPlan(){
		
	}
	
	public ConstructPlan(String id){
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

	public String getRequirement() {
		return requirement;
	}

	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}

	public DictEntry getType() {
		return type;
	}

	public void setType(DictEntry type) {
		this.type = type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	

	public DictEntry getModifyReason() {
		return modifyReason;
	}

	public void setModifyReason(DictEntry modifyReason) {
		this.modifyReason = modifyReason;
	}


	public String getWorkTarget() {
		return workTarget;
	}

	public void setWorkTarget(String workTarget) {
		this.workTarget = workTarget;
	}

	public String getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}
	
	
	
}

