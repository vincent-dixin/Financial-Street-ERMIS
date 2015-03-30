package com.fhd.bpm.web.form;

import com.fhd.bpm.entity.ProcessDefinitionDeploy;
/**
 * 
 * ClassName:ProcessDefinitionForm
 *
 * @author   杨鹏
 * @version  
 * @since    Ver 1.1
 * @Date	 2013	2013-6-28		下午2:24:55
 *
 * @see
 */
public class ProcessDefinitionDeployForm extends ProcessDefinitionDeploy {

	/**
	 *
	 * @author 杨鹏
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = 1279547061443491710L;
	private String fileUploadEntityId;
	public ProcessDefinitionDeployForm() {
		super();
	}
	public ProcessDefinitionDeployForm(String fileUploadEntityId) {
		super();
		this.fileUploadEntityId = fileUploadEntityId;
	}
	public String getFileUploadEntityId() {
		return fileUploadEntityId;
	}
	public void setFileUploadEntityId(String fileUploadEntityId) {
		this.fileUploadEntityId = fileUploadEntityId;
	}
	
}

