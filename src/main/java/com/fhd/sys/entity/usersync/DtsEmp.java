/**
 * DtsEmp.java
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
 * 用户同步接口中间表-用户表实体类
 *
 * @author   陈建毅
 * @version  
 * @since    Ver 1.1
 * @Date	 2011-10-18		上午11:21:46
 *
 * @see 	 
 */
@Entity
@Table(name = "T_DTS_EMP")
public class DtsEmp extends IdEntity implements java.io.Serializable {
	private static final long serialVersionUID = 80447418754595952L;
	
	/**
	 * 采用来源数据的登录名
	 */
	
	@Column(name = "USER_ID")
	private String userId;
	/**
	 * 采用来源数据的姓名
	 */

	@Column(name = "USER_NAME")
	private String userName;
	
	/**
	 * 处理方式根据各个项目灵活制定（多个部门使用英文逗号“，”分隔）
	 */
	@Column(name = "ORG")
	private String org;
	
	
	/**
	 * 经过处理后的数据，保证能用职务表的名称对应上（注：一个人只能有一个职务）
	 */
	@Column(name = "DUTY")
	private String duty;
	/**
	 * 岗位
	 */
	@Column(name = "POSITION")
	private String position;
	

	public DtsEmp() {
	}
	
	public DtsEmp(String userId, String userName, String org,
			String duty, String position) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.org = org;
		this.duty = duty;
		this.position = position;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
	
}

