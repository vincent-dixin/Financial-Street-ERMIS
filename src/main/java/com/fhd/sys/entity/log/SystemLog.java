/**
 * SystemLog.java
 * com.fhd.fdc.commons.entity.log
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-9-2 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.entity.log;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 系统日志实体类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-8-27
 * Company FirstHuiDa.
 */
@Entity
@Table(name = "T_SYS_SYSTEM_LOG")
public class SystemLog implements Serializable{

	private static final long serialVersionUID = 3743753350205565063L;
	
	public SystemLog() {
	}
	
	public SystemLog(String logDate, String logLevel, String location,
			String message) {
		super();
		this.logDate = logDate;
		this.logLevel = logLevel;
		this.location = location;
		this.message = message;
	}

	/**
	 * 系统时间.
	 */
	@Id
	@GenericGenerator(name = "generator", strategy = "assigned")
	@GeneratedValue(generator = "generator")
	@Column(name = "LOG_DATE",length = 255)
	private String logDate;
	/**
	 * 日志级别.
	 */
	@Column(name = "LOG_LEVEL",length = 255)
	private String logLevel;
	/**
	 * 位置.
	 */
	@Column(name = "LOCATION",length = 255)
	private String location;
	/**
	 * 信息.
	 */
	@Column(name = "MESSAGE")
	private String message;
	/**
	 * @return String
	 */
	public String getLogDate() {
		return logDate;
	}
	/**
	 * @param logDate
	 */
	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}
	/**
	 * @return String
	 */
	public String getLogLevel() {
		return logLevel;
	}
	/**
	 * @param logLevel
	 */
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}
	/**
	 * @return String
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * @param location
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * @return String
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return long
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

