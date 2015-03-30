/**
 * BusinessWorkFlow.java
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
 * BusinessWorkFlow.java
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

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * ClassName:BusinessWorkFlow:流程实例
 *
 * @author   David
 * @version  
 * @since    Ver 1.1
 * @Date	 2011-1-11		下午06:36:47
 *
 * @see 	 
 */

@Entity
@Table(name = "T_JBPM_BUSINESS_WORKFLOW")
public class BusinessWorkFlow extends IdEntity implements Serializable{
	private static final long serialVersionUID = -7130452106622848250L;
	
	/**
	 * 流程实例id
	 */
	@Column(name = "PROCESS_INSTANCE_ID")
	private String processInstanceId;
	
	/**
	 * 业务id
	 */
	@Column(name = "BUSINESS_ID")
	private String businessId;
	
	/**
	 * 业务名称
	 */
	@Column(name = "BUSINESS_NAME")
	private String businessName;
	
	/**
	 * 业务类型，用于区别风险、风险事件、风险案例和问卷答题
	 */
	@Column(name = "BUSINESS_OBJECT_TYPE")
	private String businessObjectType;
	
	/**
	 * 状态，1为正常状态、2为挂起状态
	 */
	@Column(name = "ESTATUS")
	private String state;
	
	
	/**
	 * 协办状态，1为正常状态、2为协办状态
	 */
	@Column(name = "XB_STATE")
	private String xbState;
	
	/**
	 * 发出协办的人员id和任务id，用","分割
	 */
	@Column(name = "XB_EMP_ID_AND_TASK_ID")
	private String xbEmpIdAndTaskId;
	
	/**
	 * 业务查看路径
	 */
	@Column(name = "url")
	private String url;
	
	/**
	 * 创建时间
	 */
	@Column(name = "CREATE_TIME")
	private Date createTime;
	
	/**
	 * 创建人
	 */
	@Column(name = "CREATE_BY")
	private String createBy;
	
	/**
	 * 创建人
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATE_BY",updatable =false,insertable=false)
	private SysEmployee createByEmp;
	
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getBusinessObjectType() {
		return businessObjectType;
	}

	public void setBusinessObjectType(String businessObjectType) {
		this.businessObjectType = businessObjectType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getXbState() {
		return xbState;
	}

	public void setXbState(String xbState) {
		this.xbState = xbState;
	}

	public String getXbEmpIdAndTaskId() {
		return xbEmpIdAndTaskId;
	}

	public void setXbEmpIdAndTaskId(String xbEmpIdAndTaskId) {
		this.xbEmpIdAndTaskId = xbEmpIdAndTaskId;
	}

	public void setBusinessName(String businessName) {
				
		if(businessName.length()>100)
			this.businessName = businessName.substring(0, 100)+"......"; 
		else
			this.businessName = businessName;
	}

	public String getBusinessName() {
		return businessName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public SysEmployee getCreateByEmp() {
		return createByEmp;
	}

	public void setCreateByEmp(SysEmployee createByEmp) {
		this.createByEmp = createByEmp;
	}
	
}

