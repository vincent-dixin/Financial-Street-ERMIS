package com.fhd.process.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fhd.fdc.commons.orm.hibernate.AuditableEntity;
import com.fhd.icm.entity.assess.AssessRelaDefect;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysOrganization;


/**
 * 流程实体
 *
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-12-6		下午3:17:47
 *
 * @see 	 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name="T_IC_PROCESSURE")
public class Process extends AuditableEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 所属公司
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPANY_ID")
	private SysOrganization company;
	/**
	 * 流程编号
	 */
	@Column(name="PROCESSURE_CODE")
	private String code;
	/**
	 * 流程名称
	 */
	@Column(name="PROCESSURE_NAME")
	private String name;
	/**
	 * 流程说明
	 */
	@Column(name="PROCESSURE_DESC")
	private String desc;
	/**
	 * 流程分类
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PARENT_ID")
	private Process parent;
	
	/**
	 * 主键全路径
	 */
	@Column(name="ID_SEQ")
	private String idSeq;
	/**
	 * 层级
	 */
	@Column(name = "ELEVEL")
	private Integer level;
	/**
	 * 排序
	 */
	@Column(name = "ESORT")
	private Integer sort;
	/**
	 * 是否是页子节点.
	 */
	@Column(name = "IS_LEAF")
	private Boolean isLeaf;
	
	/**
	 * 重要性
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMPORTANCE")
	private DictEntry importance;
	/**
	 * 控制目标
	 */
	@Column(name = "CONTROL_TARGET",length=4000)
	private String controlTarget;
	
	/**
	 * 影响的财报科目
	 */
	@Column(name = "ERANGE",length=4000)
	private String relaSubject;
	
	/**
	 * 流程发生频率
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROCESS_CLASS")
	private DictEntry frequency;
	
	/**
	 * 流程集合.
	 */
	@OrderBy("sort ASC")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	private Set<Process> children = new HashSet<Process>(0);
	
	/**
	 * 流程相关的部门和人员
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "process")
	private Set<ProcessRelaOrg> processRelaOrg = new HashSet<ProcessRelaOrg>(0);
	
	/**
	 * 流程相关的风险信息
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "process")
	private Set<ProcessRelaRisk> processRelaRisks = new HashSet<ProcessRelaRisk>(0);
	
	/**
	 * 流程相关的评价信息
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "process")
	private Set<AssessRelaDefect> assessRelaDefects = new HashSet<AssessRelaDefect>(0);


	public Process() {
	}
	
	public Process(String id) {
		 this.setId(id);
	}

	public SysOrganization getCompany() {
		return company;
	}

	public void setCompany(SysOrganization company) {
		this.company = company;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Process getParent() {
		return parent;
	}

	public void setParent(Process parent) {
		this.parent = parent;
	}

	public String getIdSeq() {
		return idSeq;
	}

	public void setIdSeq(String idSeq) {
		this.idSeq = idSeq;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public DictEntry getImportance() {
		return importance;
	}

	public void setImportance(DictEntry importance) {
		this.importance = importance;
	}

	public String getControlTarget() {
		return controlTarget;
	}

	public void setControlTarget(String controlTarget) {
		this.controlTarget = controlTarget;
	}

	public String getRelaSubject() {
		return relaSubject;
	}

	public void setRelaSubject(String relaSubject) {
		this.relaSubject = relaSubject;
	}

	public Set<Process> getChildren() {
		return children;
	}

	public void setChildren(Set<Process> children) {
		this.children = children;
	}

	public Set<ProcessRelaOrg> getProcessRelaOrg() {
		return processRelaOrg;
	}

	public void setProcessRelaOrg(Set<ProcessRelaOrg> processRelaOrg) {
		this.processRelaOrg = processRelaOrg;
	}

	public DictEntry getFrequency() {
		return frequency;
	}

	public void setFrequency(DictEntry frequency) {
		this.frequency = frequency;
	}

	public Set<ProcessRelaRisk> getProcessRelaRisks() {
		return processRelaRisks;
	}

	public void setProcessRelaRisks(Set<ProcessRelaRisk> processRelaRisks) {
		this.processRelaRisks = processRelaRisks;
	}

	public Set<AssessRelaDefect> getAssessRelaDefects() {
		return assessRelaDefects;
	}

	public void setAssessRelaDefects(Set<AssessRelaDefect> assessRelaDefects) {
		this.assessRelaDefects = assessRelaDefects;
	}
}

