/**
 * ImproveRelaFile.java
 * com.fhd.icm.entity.rectify
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-12-26 		张 雷
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.icm.entity.rectify;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.file.FileUploadEntity;

/**
 * 整改计划关联附件
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-26		下午4:22:40
 *
 * @see 	 
 */
@Entity
@Table(name="T_RECTIFY_IMPROVEMENT_FILE")
public class ImproveRelaFile extends IdEntity implements Serializable {
	private static final long serialVersionUID = 3915784419846689600L;

	/**
	 * 整改计划
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMPROVEMENT_ID")
	private Improve improve;
	
	/**
	 * 附件
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILE_UPLOAD_ID")
	private FileUploadEntity file;
	
	public ImproveRelaFile(){
		
	}
	
	public ImproveRelaFile(String id){
		super.setId(id);
	}

	public Improve getImprove() {
		return improve;
	}

	public void setImprove(Improve improve) {
		this.improve = improve;
	}

	public FileUploadEntity getFile() {
		return file;
	}

	public void setFile(FileUploadEntity file) {
		this.file = file;
	}
	
	
	
}

