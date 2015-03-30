/**
 * BusinessLog.java
 * com.fhd.fdc.commons.entity.log
 *
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-26 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.entity.log;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 业务日志实体类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-8-27
 * Company FirstHuiDa.
 */

@Entity
@Table(name = "T_SYS_BUSINESS_LOG")
public class BusinessLog extends IdEntity implements Serializable{

	private static final long serialVersionUID = 3894619762444568773L;
	
	/**
	 * 操作时间.
	 */
	@Column(name = "OPERATE_TIME", nullable = false)
	private Date operateTime;
	/**
	 * 操作类型.(增删改查）
	 */
	@Column(name = "OPERATE_TYPE", nullable = false)
	private String operateType;
	/**
	 * 模块名称.
	 */
	@Column(name = "MODULE_NAME", nullable = false)
	private String moduleName;
	/**
	 * 操作结果是否成功.
	 */
	@Column(name = "IS_SUCCESS")
	private String isSuccess;
	/**
	 * 操作记录.
	 * （记录什么时间，谁，调用哪个url，做了什么类型的操作，操作结果是否成功）
	 * eg:admin在2010-8-27调用/sys/log/saveBusinessLog.do，新增一条业务日志成功
	 */
	@Column(name = "OPERATE_RECORD")
	private String operateRecord;
	/**
	 * 用户(多对一维护).
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private SysUser sysUser;
	/**
	 * 机构
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID")
	private SysOrganization sysOrganization;
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
	
	public BusinessLog() {
	}
	
	public BusinessLog(Date operateTime, String operateType, String moduleName,
			String isSuccess, String operateRecord, SysUser sysUser,
			SysOrganization sysOrganization, String reserved1, String reserved2) {
		super();
		this.operateTime = operateTime;
		this.operateType = operateType;
		this.moduleName = moduleName;
		this.isSuccess = isSuccess;
		this.operateRecord = operateRecord;
		this.sysUser = sysUser;
		this.sysOrganization = sysOrganization;
		this.reserved1 = reserved1;
		this.reserved2 = reserved2;
	}
	
	public SysUser getSysUser() {
		return sysUser;
	}
	
	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}
	
	public Date getOperateTime() {
		return operateTime;
	}
	
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	
	public String getOperateType() {
		return operateType;
	}
	
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	
	public String getModuleName() {
		return moduleName;
	}
	
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
	public String getIsSuccess() {
		return isSuccess;
	}
	
	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	public String getOperateRecord() {
		return operateRecord;
	}
	
	public void setOperateRecord(String operateRecord) {
		this.operateRecord = operateRecord;
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

	public SysOrganization getSysOrganization() {
		return sysOrganization;
	}

	public void setSysOrganization(SysOrganization sysOrganization) {
		this.sysOrganization = sysOrganization;
	}
	
}

