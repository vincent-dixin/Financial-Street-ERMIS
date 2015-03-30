/**
 * EmpForm.java
 * com.fhd.fdc.commons.web.form.sys
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-12 		胡迪新
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.form.orgstructure;

import java.util.Date;

import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * 员工Form类.
 * @author   wudefu
 * @version V1.0  创建时间：2010-9-10  
 * @since    Ver 1.1
 * @Date	 2010-9-10		下午12:52:31
 * Company FirstHuiDa.
 * @see 	 
 */
public class EmpForm extends SysEmployee{

	private static final long serialVersionUID = 64913303449113083L;
	/**
	 * 密码.
	 */
	private String password;
	/**
	 * 最近登录时间.
	 */
	private Date lastLoginTime;
	/**
	 * 错误统计.
	 */
	private Integer errCount;
	/**
	 * 锁定状态.
	 */
	private Boolean lockstate;
	/**
	 * 是否启用.
	 */
	private Boolean enable;
	/**
	 * 失效日期.
	 */
	private Date expiryDate;
	/**
	 * 密码过期日期.
	 */
	private Date credentialsexpiryDate;
	/**
	 * portal
	 */
	private String portal;
	/**
	 * 操作员状态.
	 */
	private String userStatus;
	/**
	 * 机构id.
	 */
	private String orgId;
	/**
	 * 机构名称.
	 */
	private String orgName;
	/**
	 * 机构类型.
	 */
	private String orgType;
	/**
	 * 是否选择添加操作员.
	 */
	private String checkOpr;
	/**
	 * 所有的角色id字符串.
	 */
	private String roleIds;
	/**
	 * 是否主机构.
	 */
	private Boolean isMainOrg;
	/**
	 * 是否主岗位.
	 */
	private Boolean isMainPosition;
	/**
	 * 岗位id.
	 */
	private String positionId;
	/**
	 * 岗位名称.
	 */
	private String positionName;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Integer getErrCount() {
		return errCount;
	}

	public void setErrCount(Integer errCount) {
		this.errCount = errCount;
	}

	public Boolean getLockstate() {
		return lockstate;
	}

	public void setLockstate(Boolean lockstate) {
		this.lockstate = lockstate;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getCredentialsexpiryDate() {
		return credentialsexpiryDate;
	}

	public void setCredentialsexpiryDate(Date credentialsexpiryDate) {
		this.credentialsexpiryDate = credentialsexpiryDate;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getCheckOpr() {
		return checkOpr;
	}

	public void setCheckOpr(String checkOpr) {
		this.checkOpr = checkOpr;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public Boolean getIsMainOrg() {
		return isMainOrg;
	}

	public void setIsMainOrg(Boolean isMainOrg) {
		this.isMainOrg = isMainOrg;
	}

	public Boolean getIsMainPosition() {
		return isMainPosition;
	}

	public void setIsMainPosition(Boolean isMainPosition) {
		this.isMainPosition = isMainPosition;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getPortal() {
		return portal;
	}

	public void setPortal(String portal) {
		this.portal = portal;
	}
}

