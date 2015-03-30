/**
 * Plan.java
 * com.fhd.sys.entity.st
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-16 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * Plan.java
 * com.fhd.sys.entity.st
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-16        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
*/


package com.fhd.sys.entity.st;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 任务计划表对应实体
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-16		下午02:52:13
 *
 * @see 	 
 */
@Entity
@Table(name = "T_ST_PLAN")
public class Plan extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 任务名称
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "PLAN_NAME")
	private String name;
	
	/**
	 * 标题
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "PLAN_TITLE")
	private String title;
	
	/**
	 * 触发日期
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "TRIGGER_DATE")
	private String triggerData;
	
	/**
	 * 启用/停用(1/0)
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "PLAN_STATUS")
	private String status;
	
	/**
	 * 触发时间
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "TRIGGER_TIME")
	private String triggetTime;
	
	/**
	 * cycleMode:周期
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "IS_RECYCLE")
	private boolean isRecycle;

	/**
	 * 周期模版
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	private String cycleMode;
	
	/**
	 * 时间设置规则
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "TRIGGER_DATE_SET")
	private String triggerDataSet;
	
	/**
	 *时间设置描述
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "TRIGGER_DATE_TEXT")
	private String triggerDateText;
	
	/**
	 * 开始任务
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TRIGGER_TYPE")
	private DictEntry triggerType;
	
	/**
	 * 开始时间
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "START_TIME")
	private String startTime;
	
	/**
	 * 结束时间
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "END_TIME")
	private String endTime;
	
	/**
	 * 创建时间
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "CREATE_TIME")
	private String createTime;
	
	/**
	 * 选择模版
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CREATE_BY")
	private Temp createBy;
	
	/**
	 * 查看方式
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "LOOK_TYPE")
	private String lookType;

	/**
	 * 删除状态(0:已删除/1:未删除)
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "DELETE_STATUS")
	private String deleteStatus;
	
	public Plan(){	
	}

	public String getLookType() {
		return lookType;
	}

	public void setLookType(String lookType) {
		this.lookType = lookType;
	}
	
	public Temp getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Temp createBy) {
		this.createBy = createBy;
	}
	
	public DictEntry getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(DictEntry triggerType) {
		this.triggerType = triggerType;
	}
	
	public Plan(String id){
		this.setId(id);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}
	
	public String getTriggerData() {
		return triggerData;
	}

	public void setTriggerData(String triggerData) {
		this.triggerData = triggerData;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTriggetTime() {
		return triggetTime;
	}

	public void setTriggetTime(String triggetTime) {
		this.triggetTime = triggetTime;
	}

	public boolean isRecycle() {
		return isRecycle;
	}

	public void setRecycle(boolean isRecycle) {
		this.isRecycle = isRecycle;
	}

	public String getCycleMode() {
		return cycleMode;
	}

	public void setCycleMode(String cycleMode) {
		this.cycleMode = cycleMode;
	}

	public String getTriggerDataSet() {
		return triggerDataSet;
	}

	public void setTriggerDataSet(String triggerDataSet) {
		this.triggerDataSet = triggerDataSet;
	}

	public String getTriggerDateText() {
		return triggerDateText;
	}

	public void setTriggerDateText(String triggerDateText) {
		this.triggerDateText = triggerDateText;
	}

	public String getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(String deleteStatus) {
		this.deleteStatus = deleteStatus;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}