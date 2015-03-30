package com.fhd.icm.entity.standard;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.file.FileUploadEntity;

/**
 * 内控标准关联附件
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-12-21		上午11:23:35
 *
 * @see 	 
 */
@Entity
@Table(name="T_IC_STANDARD_RELA_FILE")
public class StandardRelaFile extends IdEntity implements Serializable {
	private static final long serialVersionUID = 5667483169645180007L;

	/**
	 * 内控标准
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STANDARD_ID")
	private Standard standard;
	
	/**
	 * 附件
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILE_ID")
	private FileUploadEntity file;
	
	public StandardRelaFile(){
		
	}
	
	public StandardRelaFile(String id){
		super.setId(id);
	}
	
	public Standard getStandard() {
		return standard;
	}

	public void setStandard(Standard standard) {
		this.standard = standard;
	}

	public FileUploadEntity getFile() {
		return file;
	}

	public void setFile(FileUploadEntity file) {
		this.file = file;
	}
	
	
}

