package com.fhd.sys.web.form.sysapp;

import java.util.Date;

import com.fhd.sys.entity.sysapp.DealLog;

/**
 * 计划任务--处理记录Form类.
 * @author  weilunkai
 * @version V1.0  创建时间：2012-5-23
 * Company FirstHuiDa.
 */
public class DealLogForm extends DealLog{
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
