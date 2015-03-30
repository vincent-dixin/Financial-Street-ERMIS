package com.fhd.process.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 流程节点关联部门
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-21		下午2:33:58
 *
 * @see 	 
 */
@Entity
@Table(name="T_IC_CONTROL_POINT_RELA_ORG")
public class ProcessPointRelaOrg extends IdEntity implements Serializable {

	private static final long serialVersionUID = -3744758925659426341L;

	/**
	 * 流程节点
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTROL_POINT_ID")
	private ProcessPoint processPoint;
	
	/**
	 * 部门
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID")
	private SysOrganization org;
	
	/**
	 * 员工
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMP_ID")
	private SysEmployee emp;
	
	/**
	 * 类型：责任部门，配合部门，责任人
	 */
	@Column(name="ETYPE")
	private String type;

	
	public ProcessPointRelaOrg() {
	}
	
	public ProcessPointRelaOrg(String id) {
		 this.setId(id);
	}
	
	public ProcessPoint getProcessPoint() {
		return processPoint;
	}

	public void setProcessPoint(ProcessPoint processPoint) {
		this.processPoint = processPoint;
	}

	public SysOrganization getOrg() {
		return org;
	}

	public void setOrg(SysOrganization org) {
		this.org = org;
	}

	
	public SysEmployee getEmp() {
		return emp;
	}

	public void setEmp(SysEmployee emp) {
		this.emp = emp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
}

