/**
 * FileUpload.java
 * com.fhd.fdc.commons.entity.sys.file
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-9-8 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.entity.file;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 文件实体类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-9-8
 * Company FirstHuiDa.
 */
@Entity
@Subselect("	SELECT	"+
		"file.ID,"+
		"file.OLD_FILE_NAME,"+
		"file.NEW_FILE_NAME,"+
		"file.FILE_ADDRESS,"+
		"file.FILE_SIZE,"+
		"file.FILE_TYPE,"+
		"file.COUNT_NUM,"+
		"file.UPLOAD_TIME,"+
		"file.USER_ID,"+
		"file.NOTES,"+
		"file.RESERVED1,"+
		"file.RESERVED2"+
		"	FROM	"+
		"t_sys_file file")
@Synchronize("t_sys_file")
public class VFileUploadEntity extends IdEntity implements Serializable{
	
	/**
	 *
	 * @author 杨鹏
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = -7530739011920095749L;
	/**
	 * 原文件名称.
	 */
	@Column(name = "OLD_FILE_NAME", nullable = false)
	private String oldFileName;
	/**
	 * 新文件名称.
	 */
	@Column(name = "NEW_FILE_NAME")
	private String newFileName;
	/**
	 * 文件上传地址.
	 */
	@Column(name = "FILE_ADDRESS",length = 2000)
	private String fileAddress;
	/**
	 * 文件大小.
	 */
	@Column(name = "FILE_SIZE" , nullable = false)
	private String fileSize;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILE_TYPE")
	private DictEntry fileType;
	
	/**
	 * 上传时间.
	 */
	@Column(name = "UPLOAD_TIME" , nullable = false)
	private Date uploadTime;
	/**
	 * 用户(多对一维护).
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private SysUser sysUser;
	/**
	 * 备注.
	 */
	@Column(name = "NOTES" ,length = 512)
	private String notes;
	
	/**
	 * 下载次数.
	 */
	@Column(name = "COUNT_NUM")
	private Integer countNum;
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
	
	public VFileUploadEntity() {
		// TODO Auto-generated constructor stub
	}
	
	public VFileUploadEntity(String oldFileName, String newFileName,
			String fileAddress, String fileSize, DictEntry fileType,
			Date uploadTime, SysUser sysUser, String notes, Integer countNum,
			String reserved1, String reserved2) {
		super();
		this.oldFileName = oldFileName;
		this.newFileName = newFileName;
		this.fileAddress = fileAddress;
		this.fileSize = fileSize;
		this.fileType = fileType;
		this.uploadTime = uploadTime;
		this.sysUser = sysUser;
		this.notes = notes;
		this.countNum = countNum;
		this.reserved1 = reserved1;
		this.reserved2 = reserved2;
	}
	public String getOldFileName() {
		return oldFileName;
	}
	public void setOldFileName(String oldFileName) {
		this.oldFileName = oldFileName;
	}
	public String getNewFileName() {
		return newFileName;
	}
	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
	}
	public String getFileAddress() {
		return fileAddress;
	}
	public void setFileAddress(String fileAddress) {
		this.fileAddress = fileAddress;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public DictEntry getFileType() {
		return fileType;
	}

	public void setFileType(DictEntry fileType) {
		this.fileType = fileType;
	}
	public Date getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	public SysUser getSysUser() {
		return sysUser;
	}
	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getReserved1() {
		return reserved1;
	}
	public Integer getCountNum() {
		return countNum;
	}
	public void setCountNum(Integer countNum) {
		this.countNum = countNum;
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
}

