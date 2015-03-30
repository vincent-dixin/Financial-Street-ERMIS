/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.icm.entity.rectify;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.file.FileUploadEntity;

/**
 * 整改方案相关附件
 *
 * @author   胡迪新
 * @since    fhd Ver 4.5
 * @Date	 2013-2-17  上午10:29:02
 *
 * @see 	 
 */
@Entity
@Table(name="T_RECTIFY_IMPROVE_PLAN_FILE")
public class ImprovePlanFile extends IdEntity {

	/**
	 *
	 * @author 胡迪新
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = 546868682765470882L;

	/**
	 * 整改方案
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMPROVE_PLAN_ID")	
	private ImprovePlan improvePlan;
	
	/**
	 * 文件
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILE_UPLOAD_ID")	
	private FileUploadEntity file;

	public ImprovePlan getImprovePlan() {
		return improvePlan;
	}

	public void setImprovePlan(ImprovePlan improvePlan) {
		this.improvePlan = improvePlan;
	}

	public FileUploadEntity getFile() {
		return file;
	}

	public void setFile(FileUploadEntity file) {
		this.file = file;
	}
	
}

