package com.fhd.icm.entity.standard;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 内控标准关联的部门
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-7		上午9:37:18
 *
 * @see 	 
 */
@Entity
@Table(name="T_IC_STANDARD_RELA_ORG")
public class StandardRelaOrg extends IdEntity implements Serializable {
	private static final long serialVersionUID = -823370829826808683L;

	/**
	 * 内控标准
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTROL_STANDARD_ID")
	private Standard standard;
	
	/**
	 * 部门
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID")
	private SysOrganization org;

	
	/**
	 * 类型：责任部门：morg，配合部门：corg
	 */
	@Column(name="ETYPE")
	private String type;
	
	public StandardRelaOrg() {
	}
	
	public StandardRelaOrg(String id) {
		 this.setId(id);
	}
	
	public Standard getStandard() {
		return standard;
	}

	public void setStandard(Standard standard) {
		this.standard = standard;
	}

	public SysOrganization getOrg() {
		return org;
	}

	public void setOrg(SysOrganization org) {
		this.org = org;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}

