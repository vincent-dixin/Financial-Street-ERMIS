package com.fhd.icm.web.form;

import com.fhd.icm.entity.standard.Standard;

/**
 * 内控标准的FORM
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-12-21		下午3:09:45
 *
 * @see 	 
 */
public class StandardForm extends Standard {
	private static final long serialVersionUID = 657313864694079311L;
	
	/**
	 * standardId
	 */
	private String id;
	
	/**
	 * 制层级的数据字典的ID控
	 */
	private String controlLevelId;
	
	/**
	 * 状态的数据字典Id
	 */
	private String statusId;
	
	/**
	 * 时间
	 */
	private String time;
	
	/**
	 * 部门Id
	 */
	private String deptId;
	
	/**
	 * 用于区分，与父节点中idseq
	 */
	private String idSeqp;
	
	/**
	 * 附件Id
	 */
	private String fileId;
	
	
	private String isLeafForm;
	
	private String standardControlFormsStr;
	
	/**
	 * 标准关联流程的ID
	 *
	 * @author 张 雷
	 * @since  fhd　Ver 1.1
	 */
	private String processId;
	
	public String getStandardControlFormsStr() {
		return standardControlFormsStr;
	}

	public void setStandardControlFormsStr(String standardControlFormsStr) {
		this.standardControlFormsStr = standardControlFormsStr;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getIsLeafForm() {
		return isLeafForm;
	}

	public void setIsLeafForm(String isLeafForm) {
		this.isLeafForm = isLeafForm;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getIdSeqp() {
		return idSeqp;
	}

	public void setIdSeqp(String idSeqp) {
		this.idSeqp = idSeqp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getControlLevelId() {
		return controlLevelId;
	}
	
	public void setControlLevelId(String controlLevelId) {
		this.controlLevelId = controlLevelId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}
	
	
}

