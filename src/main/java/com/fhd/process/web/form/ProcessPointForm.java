
package com.fhd.process.web.form;
import com.fhd.process.entity.Process;
import com.fhd.process.entity.ProcessPoint;
/**
 * @author  宋佳
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-3-17		下午2:40:43
 *       流程节点Form
 * @see 	 
 */
public  class ProcessPointForm extends ProcessPoint{
	private static final long serialVersionUID = 657313864694079311L;
	/**
	 * 流程ID
	 */
	private String id;
	/**
	 * 节点编号
	 */
	private String code;
	/**
	 * 节点名称
	 */
	private String name;
	
	/**
	 * 流程Id
	 * 
	 */
    private String parentid;
    /**
     * 编辑节点Id 
     */
    private String editProcessPointId;
    /**
     * 
     * 节点类型
     */
    private String pointStyleId;
    /**
     * 
     * 流程接口Id
     */
    private String processInterfaceId;
    /**
     * 节点描述
     */
	private String desc;
	
	/**
	 * 输出文档
	 */
	private String outputfile;
	/**
	 * 责任部门
	 */
	private String orgId;
	/**
	 * 对应流程
	 */
	private Process relaProcess;
	/**
	 * 责任人
	 */
	private String empId;
	/**
	 * 配合部门
	 */
	private String CrdorgId;
	/**
	 * 存放form中父节点信息
	 */
	private String editGridJson;
	/**
	 * 存放form中评价点信息
	 */
	private String assessEditGridJson;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getPointStyleId() {
		return pointStyleId;
	}
	public void setPointStyleId(String pointStyleId) {
		this.pointStyleId = pointStyleId;
	}
	public String getProcessInterfaceId() {
		return processInterfaceId;
	}
	public void setProcessInterfaceId(String processInterfaceId) {
		this.processInterfaceId = processInterfaceId;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getOutputfile() {
		return outputfile;
	}
	public void setOutputfile(String outputfile) {
		this.outputfile = outputfile;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getCrdorgId() {
		return CrdorgId;
	}
	public void setCrdorgId(String crdorgId) {
		CrdorgId = crdorgId;
	}
	public String getEditProcessPointId() {
		return editProcessPointId;
	}
	public void setEditProcessPointId(String editProcessPointId) {
		this.editProcessPointId = editProcessPointId;
	}
	public void setEditGridJson(String editGridJson) {
		this.editGridJson = editGridJson;
	}
	public String getEditGridJson() {
		// TODO Auto-generated method stub
		return editGridJson;
	}
	public String getAssessEditGridJson() {
		return assessEditGridJson;
	}
	public void setAssessEditGridJson(String assessEditGridJson) {
		this.assessEditGridJson = assessEditGridJson;
	}
	public Process getRelaProcess() {
		return relaProcess;
	}
	public void setRelaProcess(Process relaProcess) {
		this.relaProcess = relaProcess;
	}
}

