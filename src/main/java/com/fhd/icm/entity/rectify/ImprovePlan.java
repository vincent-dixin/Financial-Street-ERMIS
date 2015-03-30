/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.icm.entity.rectify;

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
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 整改方案
 *
 * @author   胡迪新
 * @since    fhd Ver 4.5
 * @Date	 2013-2-17  上午10:14:36
 *
 * @see 	 
 */
/**
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2013	2013-3-8		下午1:44:17
 *
 * @see 	 
 */
@Entity
@Table(name="T_RECTIFY_IMPROVE_PLAN")
public class ImprovePlan extends AuditableEntity implements Serializable{

	private static final long serialVersionUID = 6193050623688166039L;

	/**
	 * 公司
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPANY_ID")
	private SysOrganization company;
	
	/**
	 * 整改方案编号
	 */
	@Column(name = "IMPROVE_PLAN_CODE")
	private String code;
	
	/**
	 * 整改方案名称
	 */
	@Column(name = "IMPROVE_PLAN_NAME")
	private String name;
	
	/**
	 * 内容
	 */
	@Column(name = "ECONTENT")
	private String content;
	
	/**
	 * 说明
	 */
	@Column(name = "EDESC")
	private String desc;
	
	/**
	 * 排列顺序
	 */
	@Column(name = "ESORT")
	private Integer sort;
	
	/**
	 * 创建部门
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATE_ORG_ID")
	private SysOrganization createOrg;
	
	/**
	 * 删除状态:0已删除,1已启用
	 */
	@Column(name="DELETE_STATUS")
	private String deleteStatus;

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
	@Column(name = "PLAN_END_DATE", length = 7)
	private Date planEndDate;	
	
	
	/**
	 * 反馈意见
	 */
	@Column(name="FEEDBACK")
	private String feedback;
	
	/**
	 * 实际贡献值
	 */
	@Column(name="ACTUAL_VALUE")
	private Double actualValue;
	
	/**
	 * 预计贡献值
	 */
	@Column(name="EXPECTED_VALUE")
	private Double expectedValue; 
	
	/**
	 * 整改方案关联的部门人员
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "improvePlan")
	private Set<ImprovePlanRelaOrg> improvePlanRelaOrg = new HashSet<ImprovePlanRelaOrg>(0);
	
	/**
	 * 整改方案关联的整改计划
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "improvePlan")
	private Set<ImproveRelaPlan> improveRelaPlan = new HashSet<ImproveRelaPlan>(0);
	
	public ImprovePlan(){
		
	}
	
	public ImprovePlan(String id){
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public SysOrganization getCreateOrg() {
		return createOrg;
	}

	public void setCreateOrg(SysOrganization createOrg) {
		this.createOrg = createOrg;
	}

	public String getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(String deleteStatus) {
		this.deleteStatus = deleteStatus;
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

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public Double getActualValue() {
		return actualValue;
	}

	public void setActualValue(Double actualValue) {
		this.actualValue = actualValue;
	}

	public Double getExpectedValue() {
		return expectedValue;
	}

	public void setExpectedValue(Double expectedValue) {
		this.expectedValue = expectedValue;
	}

	public Set<ImprovePlanRelaOrg> getImprovePlanRelaOrg() {
		return improvePlanRelaOrg;
	}

	public void setImprovePlanRelaOrg(Set<ImprovePlanRelaOrg> improvePlanRelaOrg) {
		this.improvePlanRelaOrg = improvePlanRelaOrg;
	}

	public Set<ImproveRelaPlan> getImproveRelaPlan() {
		return improveRelaPlan;
	}

	public void setImproveRelaPlan(Set<ImproveRelaPlan> improveRelaPlan) {
		this.improveRelaPlan = improveRelaPlan;
	}
	
	
}

