
package com.fhd.process.web.form;
import com.fhd.process.entity.Process;
/**
 * @author  李克东
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-25		下午2:40:43
 *
 * @see 	 
 */
public  class ProcessForm extends Process{
	private static final long serialVersionUID = 657313864694079311L;
	/**
	 * 流程ID
	 */
	private String id;
	/**
	 * 控制层级的数据字典的ID
	 */
	private String controlLevelId;
	/**
	 * 部门ID
	 */
	private String orgId;
	
	/**
	 * 相关部门ID
	 */
	private String relaOrgId;
	
	
	/**
	 * 员工ID
	 * 
	 */
    private String empId;
    
    /**
     * 
     * 制度ID
     */
    private String ruleId;
    /**
     * 附件ID
     */
	private String fileId;
	
	public String getControlLevelId() {
		return controlLevelId;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setControlLevelId(String controlLevelId) {
		this.controlLevelId = controlLevelId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getRelaOrgId() {
		return relaOrgId;
	}

	public void setRelaOrgId(String relaOrgId) {
		this.relaOrgId = relaOrgId;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}


	
}

