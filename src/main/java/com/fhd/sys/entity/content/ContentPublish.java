/**
 * Content.java
 * com.fhd.fdc.commons.entity.sys.content
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-10-15 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.entity.content;

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
 * 内容发布实体类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-10-15
 * Company FirstHuiDa.
 */
@Entity
@Table(name ="T_SYS_CONTENT")
public class ContentPublish extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5494642297559272417L;

	//标题
	@Column(name = "ETITLE" , unique = true, nullable = false, length = 100)
	private String title;     
	//内容
	@Column(name = "CONTENTS" , nullable = false, length = 4000)
	private String contents;   
	//类型
	@Column(name = "ETYPE" , nullable = false, length = 32)
	private String contentType;
	//是否发布
	@Column(name = "IS_DEPLOY" , nullable = false, length = 32)
	private String isDeploy;  
	//新增时间
	@Column(name = "ADD_TIME" , nullable = false)
	private Date addTime;
	//操作员
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private SysUser sysUser;
	//机构
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID")
	private SysOrganization sysOrganization;
	//保留字段1
	@Column(name = "RESERVED1")
	private String reserved1; 
	//保留字段2
	@Column(name = "RESERVED2")
	private String reserved2;

	public ContentPublish() {
	}

	public ContentPublish(String title, String contents, String contentType,
			String isDeploy, Date addTime, SysUser sysUser,
			SysOrganization sysOrganization, String reserved1, String reserved2) {
		super();
		this.title = title;
		this.contents = contents;
		this.contentType = contentType;
		this.isDeploy = isDeploy;
		this.addTime = addTime;
		this.sysUser = sysUser;
		this.sysOrganization = sysOrganization;
		this.reserved1 = reserved1;
		this.reserved2 = reserved2;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getIsDeploy() {
		return isDeploy;
	}

	public void setIsDeploy(String isDeploy) {
		this.isDeploy = isDeploy;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public SysUser getSysUser() {
		return sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
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

