/**
 * Portal.java
 * com.fhd.fdc.commons.entity.sys.portal
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-10-20 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.entity.portal;

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
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.auth.SysRole;
import com.fhd.sys.entity.auth.SysUser;

/**
 * 面板布局实体类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-10-20
 * Company FirstHuiDa.
 */
@Entity
@Table(name ="T_SYS_PORTAL")
public class Portal extends IdEntity implements Serializable{

	private static final long serialVersionUID = -9120864485928124288L;
	/**
	 * 用户(多对一维护).
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private SysUser sysUser;
	/**
	 * 角色(多对一维护)
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLE_ID")
	private SysRole sysRole;
	/**
	 * 行数.
	 */
	@Column(name = "EROW")
	private int row;
	/**
	 * 列数.
	 */
	@Column(name = "ECOL")
	private int col;
	/**
	 * 排列顺序.
	 */
	@Column(name = "ESORT")
	private int sort;
	/**
	 * 操作时间.
	 */
	@Column(name = "UPDATE_TIME")
	private Date updateTime;
	/**
	 * 操作员.
	 */
	@Column(name = "OPERATOR")
	private String operator;
	/**
	 * 保留字段1.
	 */
	@Column(name = "RESERVED1")
	private String reserved1;
	/**
	 * 保留字段2.
	 */
	@Column(name = "RESERVED2")
	private String reserved2;
	
	/**
	 * portalet集合.
	 */
	@OrderBy("sort ASC")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "portal")
	private Set<PortalPortlet> subPortalPortlet=new HashSet<PortalPortlet>();
	
	public Portal() {
	}

	public Portal(SysUser sysUser,Set<PortalPortlet> subPortalPortlet, int row, int col, int sort, Date updateTime,
			String operator, String reserved1, String reserved2) {
		super();
		this.sysUser = sysUser;
		this.subPortalPortlet = subPortalPortlet;
		this.row = row;
		this.col = col;
		this.sort = sort;
		this.updateTime = updateTime;
		this.operator = operator;
		this.reserved1 = reserved1;
		this.reserved2 = reserved2;
	}

	public SysUser getSysUser() {
		return sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public Set<PortalPortlet> getSubPortalPortlet() {
		return subPortalPortlet;
	}

	public void setSubPortalPortlet(Set<PortalPortlet> subPortalPortlet) {
		this.subPortalPortlet = subPortalPortlet;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getReserved1() {
		return reserved1;
	}

	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}

	public String getReserved2() {
		return reserved2;
	}

	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}

	public SysRole getSysRole() {
		return sysRole;
	}

	public void setSysRole(SysRole sysRole) {
		this.sysRole = sysRole;
	}
}

