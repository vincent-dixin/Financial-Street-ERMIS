/**
 * BusinessForm.java
 * com.fhd.fdc.commons.web.form.sys.log
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-27 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.form.log;

import java.util.Date;

import com.fhd.sys.entity.log.SystemLog;

/**
 * 系统日志Form类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-8-27
 * Company FirstHuiDa.
 */

public class SystemLogForm extends SystemLog{

	private static final long serialVersionUID = -2543328025733462732L;
	/**
	 * 开始时间.
	 */
	private Date beginTime;
	/**
	 * 结束时间.
	 */
	private Date endTime;
	
	public Date getBeginTime() {
		return beginTime;
	}
	
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	
	public Date getEndTime() {
		return endTime;
	}
	
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

