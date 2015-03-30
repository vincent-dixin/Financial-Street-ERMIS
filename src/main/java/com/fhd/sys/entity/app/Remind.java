package com.fhd.sys.entity.app;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.auth.SysUser;
import com.fhd.sys.entity.dic.DictEntry;
/**
 * 
 * ClassName:Remind
 * 消息提醒设置实体类
 * @author   万业
 * @version  
 * @since    Ver 1.1
 *
 */
@Entity
@Table(name = "T_SYS_REMIND")
public class Remind extends IdEntity implements java.io.Serializable {
	
	private static final long serialVersionUID = 804474197076595952L;
	

	
	/**
	 * 提醒内容(多对一维护).
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ETYPE")
	private DictEntry dictEntry;
	/**
	 * 状态			1：启用；2：停用
	 */
	@Column(name = "ESTATUS")
	private String status;
	/**
	 * 设置邮件
	 */
	@Column(name = "REMIND_EMAIL")
	private int remindEmail;
	/**
	 * 设置短信
	 */
	@Column(name = "REMIND_MOBILE")
	private int remindMobile;
	/**
	 * 用户(多对一维护).
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private SysUser user;
	
	public Remind(){
		
	}

	public Remind(DictEntry dictEntry, int remindEmail, int remindMobile,
			SysUser user) {
		super();
		this.dictEntry = dictEntry;
		this.remindEmail = remindEmail;
		this.remindMobile = remindMobile;
		this.user = user;
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

	public SysUser getUser() {
		return user;
	}

	public void setUser(SysUser user) {
		this.user = user;
	}

	public DictEntry getDictEntry() {
		return dictEntry;
	}

	public void setDictEntry(DictEntry dictEntry) {
		this.dictEntry = dictEntry;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
