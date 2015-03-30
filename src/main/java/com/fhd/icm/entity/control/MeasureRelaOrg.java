package com.fhd.icm.entity.control;

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
 * 控制措施关联部门
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-21		下午2:33:58
 *
 * @see 	 
 */
@Entity
@Table(name="T_CON_MEASURE_RELA_ORG")
public class MeasureRelaOrg extends IdEntity implements Serializable {
	private static final long serialVersionUID = -3340921565845406392L;

	/**
	 * 控制措施
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTROL_MEASURE_ID")
	private Measure controlMeasure;
	
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

	

	public MeasureRelaOrg() {
		super();
	}

	public MeasureRelaOrg(String id) {
		super.setId(id);
	}
	
	public Measure getControlMeasure() {
		return controlMeasure;
	}

	public void setControlMeasure(Measure controlMeasure) {
		this.controlMeasure = controlMeasure;
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

