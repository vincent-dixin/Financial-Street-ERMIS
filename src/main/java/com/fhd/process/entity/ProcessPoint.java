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
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 流程节点
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-26		下午2:00:16
 *
 * @see 	 
 */
@Entity
@Table(name="T_IC_CONTROL_POINT")
public class ProcessPoint extends IdEntity implements Serializable {

	private static final long serialVersionUID = 5216103123439900523L;

	/**
	 * 流程
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROCESSURE_ID")
	private Process process;
	
	/**
	 * 流程节点编号
	 */
	@Column(name="CONTROL_POINT_CODE")
	private String code;
	/**
	 * 流程节点名称
	 */
	@Column(name="CONTROL_POINT_NAME")
	private String name;
	/**
	 * 流程节点说明
	 */
	@Column(name="EDESC")
	private String desc;
	
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
	 * 删除状态
	 */
	@Column(name="DELETE_STATUS")
	private String deleteStatus;
	/**
	 * 排序
	 */
	@Column(name = "ESORT")
	private Integer sort;
	
	/**
	 * 流程接口
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RELA_PROCESSURE_ID")
	private Process relaProcess;
	
	/**
	 * 流程节点类型
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POINT_TYPE")
	private DictEntry pointType;
	
	/**
	 * 节点关联的部门
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "processPoint")
	private Set<ProcessPointRelaOrg> processpointRelaOrg = new HashSet<ProcessPointRelaOrg>(0);
	
	/**
	 * 流程节点关联风险.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "processPoint")
	private Set<ProcessPointRelaRisk> processPointRelaRisks = new HashSet<ProcessPointRelaRisk>(0);
	
	public ProcessPoint(){
		
	}
	public ProcessPoint(String id){
		super.setId(id);
	}
	public Process getProcess() {
		return process;
	}
	public void setProcess(Process process) {
		this.process = process;
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
	public Process getRelaProcess() {
		return relaProcess;
	}
	public void setRelaProcess(Process relaProcess) {
		this.relaProcess = relaProcess;
	}
	public DictEntry getPointType() {
		return pointType;
	}
	public void setPointType(DictEntry pointType) {
		this.pointType = pointType;
	}
	public Set<ProcessPointRelaOrg> getProcessPointRelaOrg() {
		return processpointRelaOrg;
	}
	public void setProcessPointRelaOrg(Set<ProcessPointRelaOrg> processPointRelaOrg) {
		this.processpointRelaOrg = processPointRelaOrg;
	}
	public Set<ProcessPointRelaOrg> getProcesspointRelaOrg() {
		return processpointRelaOrg;
	}
	public void setProcesspointRelaOrg(Set<ProcessPointRelaOrg> processpointRelaOrg) {
		this.processpointRelaOrg = processpointRelaOrg;
	}
	public Set<ProcessPointRelaRisk> getProcessPointRelaRisks() {
		return processPointRelaRisks;
	}
	public void setProcessPointRelaRisks(
			Set<ProcessPointRelaRisk> processPointRelaRisks) {
		this.processPointRelaRisks = processPointRelaRisks;
	}
}