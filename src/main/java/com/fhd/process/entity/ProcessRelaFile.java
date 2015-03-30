package com.fhd.process.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.file.FileUploadEntity;

/**
 * 流程关联附件实体
 * @author   张  雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-19		上午11:44:27
 *
 * @see 	 
 */
@Entity
@Table(name="T_IC_PROCESSURE_RELA_FILE")
public class ProcessRelaFile extends IdEntity implements Serializable {
	private static final long serialVersionUID = 8646266612406363553L;

	/**
	 * 流程
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROCESSURE_ID")
	private Process process;
	
	/**
	 * 附件
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILE_ID")
	private FileUploadEntity file;
	
	
	public ProcessRelaFile(){
		
	}
	
	public ProcessRelaFile(String id){
		super.setId(id);
	}
	
	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public FileUploadEntity getFile() {
		return file;
	}

	public void setFile(FileUploadEntity file) {
		this.file = file;
	}
	
	
}


