/**
 * Cliping.java
 * com.fhd.fdc.commons.entity.sys.cliping
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2011-3-22 		胡迪新
 *
 * Copyright (c) 2011, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.entity.cliping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * ClassName:Cliping
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2011-3-22		下午02:15:56
 *
 * @see 	 
 */
@Entity
@Table(name = "T_SYS_CLIPING")
public class Cliping extends IdEntity{

	/**
	 *
	 * @author 胡迪新
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = 1L;

	/**
	 * 公司id
	 */
	@Column(name = "COMPANY_ID")
	private String companyid;
	
	/**
	 * 编号
	 */
	@Column(name = "CLIPING_CODE")
	private String code;
	
	/**
	 * 名称
	 */
	@Column(name = "CLIPING_NAME")
	private String name;
	
	/**
	 * 类型--功能、表单
	 * function form
	 */
	@Column(name = "ETYPE")
	private String type;
	
	/**
	 * 分类--风险、组织、流程、指标、资产
	 */
	@Column(name = "CATEGORY")
	private String category;
	
	/**
	 * 状态--启用1、停用0
	 */
	@Column(name = "ESTATUS")
	private String status;

	
	/**
	 * 排序
	 */
	@Column(name = "ESORT")
	private String esort;
	
	/**
	 * 是否系统字段
	 */
	@Column(name = "IS_SYSTEM")
	private String isSystem;
	
	
	
	public String getEsort() {
	    return esort;
	}

	public void setEsort(String esort) {
	    this.esort = esort;
	}

	public String getIsSystem() {
	    return isSystem;
	}

	public void setIsSystem(String isSystem) {
	    this.isSystem = isSystem;
	}

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}

