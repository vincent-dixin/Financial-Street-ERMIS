/**
 * ImproveRelaOrg.java
 * com.fhd.icm.entity.rectify
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-12-26 		张 雷
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.icm.entity.defect;

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
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-26		下午4:21:34
 *
 * @see 	 
 */
@Entity
@Table(name="T_CA_DEFECT_RELA_ORG")
public class DefectRelaOrg extends IdEntity implements Serializable {
	private static final long serialVersionUID = -884096668508521965L;

	/**
	 * 缺陷
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFECT_ID")
	private Defect defect;
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
	 * 类型：责任部门，责任人
	 */
	@Column(name="ETYPE")
	private String type;
	
	public DefectRelaOrg(){
		
	}
	
	public DefectRelaOrg(String id){
		super.setId(id);
	}

	public Defect getDefect() {
		return defect;
	}

	public void setDefect(Defect defect) {
		this.defect = defect;
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

