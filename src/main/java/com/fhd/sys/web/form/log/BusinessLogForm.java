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

import com.fhd.sys.entity.log.BusinessLog;

/**
 * 业务日志Form类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-8-27
 * Company FirstHuiDa.
 */

public class BusinessLogForm extends BusinessLog{

	private static final long serialVersionUID = -2543328025733462732L;
	/**
	 * 模块id.页面查询条件中选择框属性取值.
	 */
	private String moduleId;
	/**
	 * 用户id.页面查询条件中选择框属性取值.
	 */
	private String userId;
	/**
	 * 开始时间.
	 */
	private Date beginTime;
	/**
	 * 结束时间.
	 */
	private Date endTime;
	
	public String getModuleId() {
		return moduleId;
	}
	
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
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

