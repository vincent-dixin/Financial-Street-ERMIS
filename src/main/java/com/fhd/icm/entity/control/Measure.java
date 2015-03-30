package com.fhd.icm.entity.control;

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

import com.fhd.fdc.commons.orm.hibernate.AuditableEntity;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 控制措施
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-26		下午2:23:09
 *
 * @see 	 
 */
@Entity
@Table(name="T_CON_CONTROL_MEASURE")
public class Measure extends AuditableEntity implements Serializable {
	
	private static final long serialVersionUID = -7714989398654347108L;

	/**
	 * 所属公司
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPANY_ID")
	private SysOrganization company;
	
	/**
	 * 控制措施编号
	 */
	@Column(name="MEASURE_CODE")
	private String code;
	/**
	 * 控制措施名称
	 */
	@Column(name="MEASURE_NAME")
	private String name;
	/**
	 * 描述
	 */
	@Column(name="EDESC")
	private String desc;
	/**
	 * 实施证据
	 */
	@Column(name="IMPLEMENT_PROOF",length=4000)
	private String implementProof;
	
	/**
	 * 控制目标
	 */
	@Column(name="CONTROL_TARGET")
	private String controlTarget;
	
	/**
	 * 控制方式
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTROL_MEASURE")
	private DictEntry controlMeasure;
	
	/**
	 * 控制频率
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTROL_FREQUENCY")
	private DictEntry controlFrequency;
	
	/**
	 * 财报相关科目
	 */
	@Column(name="RELA_SUBJECT")
	private String relaSubject;
	
	/**
	 * 删除状态
	 */
	@Column(name="DELETE_ESTATUS")
	private String deleteStatus;
	/**
	 * 排序
	 */
	@Column(name = "ESORT")
	private Integer sort;
	
	/**
	 * 是否关键控制点
	 */
	@Column(name = "IS_KEY_CONTROL_POINT")
	private String isKeyControlPoint;
	
	/**
	 * 控制点
	 */
	@Column(name = "CONTROL_POINT", length=4000)
	private String controlPoint;
	
	/**
	 * 控制措施关联风险.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "controlMeasure")
	private Set<MeasureRelaRisk> measureRelaRisks = new HashSet<MeasureRelaRisk>(0);
	
	public Measure() {
	}
	
	public Measure(String id) {
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
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getImplementProof() {
		return implementProof;
	}
	public void setImplementProof(String implementProof) {
		this.implementProof = implementProof;
	}
	public String getControlTarget() {
		return controlTarget;
	}
	public void setControlTarget(String controlTarget) {
		this.controlTarget = controlTarget;
	}
	public DictEntry getControlMeasure() {
		return controlMeasure;
	}
	public void setControlMeasure(DictEntry controlMeasure) {
		this.controlMeasure = controlMeasure;
	}
	public DictEntry getControlFrequency() {
		return controlFrequency;
	}
	public void setControlFrequency(DictEntry controlFrequency) {
		this.controlFrequency = controlFrequency;
	}
	public String getRelaSubject() {
		return relaSubject;
	}
	public void setRelaSubject(String relaSubject) {
		this.relaSubject = relaSubject;
	}
	public String getDeleteStatus() {
		return deleteStatus;
	}
	public void setDeleteStatus(String deleteStatus) {
		this.deleteStatus = deleteStatus;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getIsKeyControlPoint() {
		return isKeyControlPoint;
	}
	public void setIsKeyControlPoint(String isKeyControlPoint) {
		this.isKeyControlPoint = isKeyControlPoint;
	}
	public String getControlPoint() {
		return controlPoint;
	}
	public void setControlPoint(String controlPoint) {
		this.controlPoint = controlPoint;
	}
	public Set<MeasureRelaRisk> getMeasureRelaRisks() {
		return measureRelaRisks;
	}
	public void setMeasureRelaRisks(Set<MeasureRelaRisk> measureRelaRisks) {
		this.measureRelaRisks = measureRelaRisks;
	}
}

