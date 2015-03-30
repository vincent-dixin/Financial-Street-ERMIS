package com.fhd.icm.entity.assess;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.file.FileUploadEntity;

/**
 * 评价样本关联的附件
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-26		下午4:11:01
 *
 * @see 	 
 */
@Entity
@Table(name="T_CA_SAMPLE_RELA_FILE")
public class AssessSampleRelaFile extends IdEntity implements Serializable {
	private static final long serialVersionUID = -2591769967767995201L;

	/**
	 * 评价样本
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SAMPLE_ID")
	private AssessSample assessSample;
	
	
	/**
	 * 附件
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILE_ID")
	private FileUploadEntity file;
	
	public AssessSampleRelaFile(){
		
	}
	
	public AssessSampleRelaFile(String id){
		super.setId(id);
	}

	public AssessSample getAssessSample() {
		return assessSample;
	}

	public void setAssessSample(AssessSample assessSample) {
		this.assessSample = assessSample;
	}

	public FileUploadEntity getFile() {
		return file;
	}

	public void setFile(FileUploadEntity file) {
		this.file = file;
	}
	
}

