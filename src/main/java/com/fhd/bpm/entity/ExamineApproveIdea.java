/**
 * ExamineApproveIdea.java
 * com.fhd.fdc.commons.entity.jbpm
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2011-1-11 		David
 *
 * Copyright (c) 2011, Firsthuida All Rights Reserved.
 */
/**
 * ExamineApproveIdea.java
 * com.fhd.fdc.commons.entity.jbpm
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2011-1-11        David
 *
 * Copyright (c) 2011, FirstHuida All Rights Reserved.
 */

package com.fhd.bpm.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.bpm.entity.view.VJbpmHistTask;
import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * ClassName:ExamineApproveIdea Function: TODO ADD FUNCTION Reason: TODO ADD
 * REASON
 * 
 * @author David
 * @version
 * @since Ver 1.1
 * @Date 2011-1-11 下午06:36:47
 * 
 * @see
 */

@Entity
@Table(name = "T_JBPM_EXAMINE_APPROVE_IDEA")
public class ExamineApproveIdea extends IdEntity implements Serializable {
	private static final long serialVersionUID = -7130452106622848250L;

	/**
	 * 流程实例id
	 */
	@Column(name = "PROCESS_INSTANCE_ID")
	private String processInstanceId;

	/**
	 * 流程节点名称
	 */
	@Column(name = "PROCESS_POINT_NAME")
	private String processPointName;

	/**
	 * 业务id
	 */
	@Column(name = "BUSINESS_ID")
	private String businessId;

	/**
	 * 审批人
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EA_BY")
	private SysEmployee gatherBy;

	/**
	 * 审批意见
	 */
	@Column(name = "EA_CONTENT")
	private String ea_Content;

	/**
	 * 审批操作 提交、同意、不同意，打回发起人之类
	 */
	@Column(name = "EA_OPERATE")
	private String ea_Operate;

	/**
	 * 审批时间
	 */
	@Column(name = "EA_DATE")
	private Date ea_Date;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TASK_ID") private VJbpmHistTask jbpmHistTask;


	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProcessPointName() {
		return processPointName;
	}

	public void setProcessPointName(String processPointName) {
		this.processPointName = processPointName;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public SysEmployee getGatherBy() {
		return gatherBy;
	}

	public void setGatherBy(SysEmployee gatherBy) {
		this.gatherBy = gatherBy;
	}

	public String getEa_Content() {
		return ea_Content;
	}

	public void setEa_Content(String ea_Content) {
		this.ea_Content = ea_Content;
	}

	public String getEa_Operate() {
		return ea_Operate;
	}

	public void setEa_Operate(String ea_Operate) {
		this.ea_Operate = ea_Operate;
	}

	public Date getEa_Date() {
		return ea_Date;
	}

	public void setEa_Date(Date ea_Date) {
		this.ea_Date = ea_Date;
	}

	public VJbpmHistTask getJbpmHistTask() {
		return jbpmHistTask;
	}

	public void setJbpmHistTask(VJbpmHistTask jbpmHistTask) {
		this.jbpmHistTask = jbpmHistTask;
	}
}
