/**
 * DtsOrg.java
 * com.fhd.usersync.entity
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2011-10-18 	陈建毅
 *
 * Copyright (c) 2011, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.entity.usersync;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * 用户同步接口中间表-组织机构表实体类
 *
 * @author   陈建毅
 * @version  
 * @since    Ver 1.1
 * @Date	 2011-10-18		上午11:22:01
 *
 * @see 	 
 */
@Entity
@Table(name = "T_DTS_ORG")
public class DtsOrg extends IdEntity implements java.io.Serializable {
	private static final long serialVersionUID = 80462456754595952L;
    

	/**
	 * 采用来源数据的主键或者业务编号
	 */
	@Column(name = "ORG_CODE")
	private String orgCode;
	
	/**
	 * 采用来源数据的上级机构id（最上层数据为空）
	 */
	
	@Column(name = "PARENT_ID")
	private String parentId;
	
	/**
	 * 采用来源数据的机构名称
	 */
	@Column(name = "ORG_NAME")
	private String orgName;
	
	
	/**
	 * 处理方式根据各个项目灵活制定数据内容只能是以下二种之一：'402881b22afad3b1012afae5a4200004'—表示公司'402881b22afad3b1012afae799c60008'—表示部门

	 */
	@Column(name = "ORG_TYPE")
	private String orgType;
	
	/**
	 * 处理方式根据各个项目灵活制定
	 */
	@Column(name = "ESORT")
	private String esort;
	
	/**
	 * 部门主管人员的名称
	 */
	@Column(name = "EMP")
	private String emp;
	
	public DtsOrg() {
	}
	
	public DtsOrg(String orgCode, String parentId, String orgName,
			String orgType, String esort,String emp) {
		super();
		this.orgCode = orgCode;
		this.parentId = parentId;
		this.orgName = orgName;
		this.orgName = orgName;
		this.esort = esort;
		this.emp = emp;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getEsort() {
		return esort;
	}

	public void setEsort(String esort) {
		this.esort = esort;
	}

	public String getEmp() {
		return emp;
	}

	public void setEmp(String emp) {
		this.emp = emp;
	}
	
	
}

