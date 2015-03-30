package com.fhd.sys.web.form.sysapp;

import com.fhd.sys.entity.sysapp.ScheduledTask;

/**
 * 计划任务Form类.
 * @author  吴德福
 * @version V1.0  创建时间：2012-5-15
 * Company FirstHuiDa.
 */
public class ScheduledTaskForm extends ScheduledTask{

	private static final long serialVersionUID = 1L;
	/**
	 * 触发类型
	 */
	private String type;
	/**
	 * 触发方式
	 */
	private String measure;
	/**
	 * 涉及人员
	 */
	private String emp;
	/**
	 * 触发类型详细
	 */
	private String subType;
	/**
	 * 对象类别
	 */
	private String eType;
	/**
	 * 邮件模板
	 */
	private String mailTemp;
	/**
	 * 短信模板
	 */
	private String msgTemp;
	/**
	 * 其它
	 */
	private String otherMethod;
	/**
	 * 显示当前时间--调度时间标签使用
	 */
	private String showTime;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMeasure() {
		return measure;
	}
	public void setMeasure(String measure) {
		this.measure = measure;
	}
	public String getEmp() {
		return emp;
	}
	public void setEmp(String emp) {
		this.emp = emp;
	}
	public String getSubType() {
		return subType;
	}
	public void setSubType(String subType) {
		this.subType = subType;
	}
	public String geteType() {
		return eType;
	}
	public void seteType(String eType) {
		this.eType = eType;
	}
	public String getMailTemp() {
		return mailTemp;
	}
	public void setMailTemp(String mailTemp) {
		this.mailTemp = mailTemp;
	}
	public String getMsgTemp() {
		return msgTemp;
	}
	public void setMsgTemp(String msgTemp) {
		this.msgTemp = msgTemp;
	}
	public String getOtherMethod() {
		return otherMethod;
	}
	public void setOtherMethod(String otherMethod) {
		this.otherMethod = otherMethod;
	}
	public String getShowTime() {
		return showTime;
	}
	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}
}
