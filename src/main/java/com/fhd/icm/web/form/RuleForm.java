package com.fhd.icm.web.form;

import com.fhd.icm.entity.rule.Rule;
import com.fhd.sys.entity.file.FileUploadEntity;

/**
 * @author   黄晨曦
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-24		下午6:39:06
 *
 * @see 	 
 */
public class RuleForm extends Rule{
	
	private static final long serialVersionUID = 1L;

	private String orgid;
	
	private FileUploadEntity fileIds;
	
	public FileUploadEntity getFileIds() {
		return fileIds;
	}

	public void setFileIds(FileUploadEntity fileIds) {
		this.fileIds = fileIds;
	}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}
	
}

