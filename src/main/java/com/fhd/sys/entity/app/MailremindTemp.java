/**
 * MailremindTemp.java
 * com.fhd.sys.entity.app
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2011-5-10 		胡迪新
 *
 * Copyright (c) 2011, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.entity.app;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * ClassName:MailremindTemp
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2011-5-10		上午11:57:41
 *
 * @see 	 
 */
@Entity
@Table(name = "T_SYS_MAIL_REMIND_TEMP")
public class MailremindTemp extends IdEntity implements Serializable {

	/**
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @author 胡迪新
	 * @since  fhd　Ver 1.1
	 */
	
	private static final long serialVersionUID = 6052040527509009846L;
	
	/**
	 * 公司ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPANY_ID")
	private SysOrganization company;
	
	/**
	 * 模板名称
	 */
	@Column(name = "TEMP_NAME")
	private String tempName;
	
	/**
	 * 模板代码
	 */
	@Column(name = "TEMP_CODE")
	private String tempCode;
	
	/**
	 * 模板类型   		1：采集提醒；2：工作流提醒
	 */
//	@Column(name = "TEMP_TYPE")
//	private String tempType;
	
	/**
	 * 状态			1：启用；2：停用
	 */
	@Column(name = "ESTATUS")
	private String status;
	
	/**
	 * 模板内容
	 */
	@Column(name = "TEMP_CONTENTS", length = 4000)
	private String tempContents;
	/**
	 * 模板数据源
	 *//*
	@Column(name = "TEMP_DATAS", length = 4000)
	private String tempDatas;*/
	

	/**
	 * 调度时间
	 */
	@Column(name = "TIMING")
	private String timing;
	
	/**
	 * 调度时间文字化
	 */
	@Column(name = "SHOW_TIMING")
	private String showtiming;
	
	/**
	 * 调度执行类名
	 */
	@Column(name = "EXECUTECLASS")
	private String executeClass;
	



	public String getTempCode() {
		return tempCode;
	}

	public void setTempCode(String tempCode) {
		this.tempCode = tempCode;
	}

	public String getExecuteClass() {
		return executeClass;
	}

	public void setExecuteClass(String executeClass) {
		this.executeClass = executeClass;
	}

	public SysOrganization getCompany() {
		return company;
	}

	public void setCompany(SysOrganization company) {
		this.company = company;
	}

	public String getTempName() {
		return tempName;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTempContents() {
		return tempContents;
	}

	public void setTempContents(String tempContents) {
		this.tempContents = tempContents;
	}

	public String getTiming() {
		return timing;
	}

	public void setTiming(String timing) {
		this.timing = timing;
	}

	public String getShowtiming() {
		return showtiming;
	}

	public void setShowtiming(String showtiming) {
		this.showtiming = showtiming;
	}
	

}

