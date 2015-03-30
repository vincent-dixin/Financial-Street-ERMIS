package com.fhd.sys.entity.auth;

import java.io.Serializable;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
/**
 * 用户角色实体类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-9-8
 * Company FirstHuiDa.
 */
public class SysRoleUser extends IdEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 角色
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLE_ID")
	private SysRole sysRole;
	/**
	 * 用户
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private SysUser sysUser;
	
	public SysRoleUser() {
	}

	public SysRoleUser(SysRole sysRole, SysUser sysUser) {
		super();
		this.sysRole = sysRole;
		this.sysUser = sysUser;
	}
	
	public SysRole getSysRole() {
		return sysRole;
	}
	public void setSysRole(SysRole sysRole) {
		this.sysRole = sysRole;
	}
	public SysUser getSysUser() {
		return sysUser;
	}
	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}
}
