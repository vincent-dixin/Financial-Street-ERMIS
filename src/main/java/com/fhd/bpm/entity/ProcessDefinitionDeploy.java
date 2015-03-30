
package com.fhd.bpm.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.file.FileUploadEntity;

/**
 * 
 * ClassName:ProcessDefinitionDeploy:流程定义
 *
 * @author   杨鹏
 * @version  
 * @since    Ver 1.1
 * @Date	 2013	2013-6-26		上午10:46:33
 *
 * @see
 */
@Entity
@Table(name = "T_JBPM_PROCESS_DEFINE_DEPLOY")
public class ProcessDefinitionDeploy extends IdEntity implements Serializable {
	private static final long serialVersionUID = -7130452106622848250L;

    /**
	 *  流程定义显示名称
	 */
	@Column(name = "DIS_NAME")
	private String disName;
	
	/**
	 *  业务查看路径
	 */
	@Column(name = "url")
	private String url;
	
	/**
	 *  流程定义文件
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILE_ID")
	private FileUploadEntity fileUploadEntity;
	
	/**
	 * 是否可用：1、可用。2、已删除
	 */
	@Column(name = "DELETE_STATUS")
	private String deleteStatus;
	
	public ProcessDefinitionDeploy() {
		super();
	}

	public String getDisName() {
		return disName;
	}

	public void setDisName(String disName) {
		this.disName = disName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public FileUploadEntity getFileUploadEntity() {
		return fileUploadEntity;
	}

	public void setFileUploadEntity(FileUploadEntity fileUploadEntity) {
		this.fileUploadEntity = fileUploadEntity;
	}

	public String getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(String deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

}
