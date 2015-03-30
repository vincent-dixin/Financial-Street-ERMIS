package com.fhd.sys.entity.app;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.orgstructure.SysEmployee;
/**
 * 
 * ClassName:TempUser
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   万业
 * @version  
 * @since    Ver 1.1
 * @Date	 2011	2011-5-13		上午10:38:37
 *
 * @see 	 
 */
@Entity
@Table(name = "T_SYS_TEMP_USER")
public class TempUser extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = 6052040527509009846L;
	
	/**
	 * 员工(多对一维护).
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMPLOYEE_ID")
	private SysEmployee sysEmployee;
	
	/**
	 * 消息模板(多对一维护).
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MAIL_REMIND_TEMP_ID")
	private MailremindTemp mailremindTemp;
	
	/**
	 * 设置邮件 1有效 0无效
	 */
	@Column(name = "REMIND_EMAIL")
	private int remindEmail;
	
	/**
	 * 设置短信 1有效 0无效
	 */
	@Column(name = "REMIND_MOBILE")
	private int remindMobile;
	
	
	public SysEmployee getSysEmployee() {
		return sysEmployee;
	}
	public void setSysEmployee(SysEmployee sysEmployee) {
		this.sysEmployee = sysEmployee;
	}
	public MailremindTemp getMailremindTemp() {
		return mailremindTemp;
	}
	public void setMailremindTemp(MailremindTemp mailremindTemp) {
		this.mailremindTemp = mailremindTemp;
	}
	public int getRemindEmail() {
		return remindEmail;
	}
	public void setRemindEmail(int remindEmail) {
		this.remindEmail = remindEmail;
	}
	public int getRemindMobile() {
		return remindMobile;
	}
	public void setRemindMobile(int remindMobile) {
		this.remindMobile = remindMobile;
	}
	

}
