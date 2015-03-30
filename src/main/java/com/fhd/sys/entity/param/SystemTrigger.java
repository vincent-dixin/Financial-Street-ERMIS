package com.fhd.sys.entity.param;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.AuditableEntity;
/**
 * 定时任务实体类; 
 * @author   陈燕杰
 * @Date	 2011	2011-3-30		下午02:31:25
 */
@Entity
@Table(name="t_sys_trigger")
public class SystemTrigger extends AuditableEntity implements java.io.Serializable{
	/**
	 * @author 陈燕杰
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = 1L;
	/**
	 *要触发的类的名称
	 */
	@Column(name="class_name")
	private String className;
	/**
	 * 要触发的时间的表达式
	 */
	@Column(name="cron_expression")
	private String cronExpression;
	/**
	 * 要触发的任务的描述
	 */
	@Column(name="trigger_desc")
	private String desc;
	/**
	 * 状态：0=关闭;1=开启;
	 */
	@Column(name="ESTATUS")
	private String status;
	/**
	 * 删除状态:2=删除；3=没删除
	 */
	@Column(name="deal_status")
	private String delStatus;
	/**
	 * 是否可以更改
	 */
	@Column(name="enModified")
	private String modified;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public SystemTrigger()
	{
		
	}
	public SystemTrigger(String className, String cronExpression, String desc,
			String status, String delStatus, String modified) {
		super();
		this.className = className;
		this.cronExpression = cronExpression;
		this.desc = desc;
		this.status = status;
		this.delStatus = delStatus;
		this.modified = modified;
	}
	public String getModified() {
		return modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}
	public String getDelStatus() {
		return delStatus;
	}
	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
	}
	
}
