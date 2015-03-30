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
import com.fhd.icm.entity.defect.DefectRelaImprove;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 整改计划
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-26		下午4:20:18
 *
 * @see 	 
 */
@Entity
@Table(name="T_RECTIFY_IMPROVE")
public class Improve extends AuditableEntity implements Serializable {

	private static final long serialVersionUID = -192111916192686546L;

	/**
	 * 所属公司
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPANY_ID")
	private SysOrganization company;

	/**
	 * 编号
	 */
	@Column(name="IMPROVEMENT_CODE")
	private String code;
	
	/**
	 * 名称
	 */
	@Column(name="IMPROVEMENT_NAME")
	private String name;
	
	/**
	 * 改进原因
	 */
	@Column(name="IMPROVEMENT_SOURCE",length=4000)
	private String improvementSource;
	
	/**
	 * 改进目标
	 */
	@Column(name="IMPROVEMENT_TARGET",length=4000)
	private String improvementTarget;	
		
	/**
	 * 具体原因
	 */
	@Column(name="REASON_DETAIL",length=4000)
	private String reasonDetail;	
	
	/**
	 * 改进方案
	 */
	@Column(name="IMPROVE_SCENARIO",length=4000)
	private String improveScenario;
	
	/**
	 * 完成进度
	 */
	@Column(name="FINISH_RATE")
	private Double finishRate;	
		
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
	 * 整改计划关联缺陷
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "improve")
	private Set<DefectRelaImprove> defectRelaImprove = new HashSet<DefectRelaImprove>(0);
	
	public Improve(){
		
	}
	
	public Improve(String id){
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

	public String getImprovementSource() {
		return improvementSource;
	}

	public void setImprovementSource(String improvementSource) {
		this.improvementSource = improvementSource;
	}

	public String getImprovementTarget() {
		return improvementTarget;
	}

	public void setImprovementTarget(String improvementTarget) {
		this.improvementTarget = improvementTarget;
	}

	public String getReasonDetail() {
		return reasonDetail;
	}

	public void setReasonDetail(String reasonDetail) {
		this.reasonDetail = reasonDetail;
	}
	
	public String getImproveScenario() {
		return improveScenario;
	}

	public void setImproveScenario(String improveScenario) {
		this.improveScenario = improveScenario;
	}

	public Double getFinishRate() {
		return finishRate;
	}

	public void setFinishRate(Double finishRate) {
		this.finishRate = finishRate;
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

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
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

	public Set<DefectRelaImprove> getDefectRelaImprove() {
		return defectRelaImprove;
	}

	public void setDefectRelaImprove(Set<DefectRelaImprove> defectRelaImprove) {
		this.defectRelaImprove = defectRelaImprove;
	}	
	
}

