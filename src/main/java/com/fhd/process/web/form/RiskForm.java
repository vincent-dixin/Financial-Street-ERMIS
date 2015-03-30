
package com.fhd.process.web.form;
import com.fhd.risk.entity.Risk;
/**
 * @author  宋佳
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-3-17		下午2:40:43
 *     风险form
 * @see 	 
 */
public  class RiskForm extends Risk{
	private static final long serialVersionUID = 657313864694079311L;
	/**
	 * 风险ID
	 */
	private String id;
	/**
	 * 风险编号
	 */
	private String code;
	/**
	 * 风险名称
	 */
	private String name;
	/**
	 *   流程风险Id
	 */
	private String processRiskId;
	/// 所有控制类型信息的表单内容
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 控制信息editform
	 */
	private MeasureForm[] measureForms = null; 
	private String measureFormstr = null;
	/**
	 * 流程Id
	 * 
	 */
    private String processId;
    /**
     * 编辑节点Id 
     */
    private String processPointId;
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
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getProcessPointId() {
		return processPointId;
	}
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	public String getProcessRiskId() {
		return processRiskId;
	}
	public void setProcessRiskId(String processRiskId) {
		this.processRiskId = processRiskId;
	}
	public MeasureForm[] getMeasureForms() {
		return measureForms;
	}
	public void setMeasureForms(MeasureForm[] measureForms) {
		this.measureForms = measureForms;
	}
	public String getMeasureFormstr() {
		return measureFormstr;
	}
	public void setMeasureFormstr(String measureFormstr) {
		this.measureFormstr = measureFormstr;
	}
   
}

