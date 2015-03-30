/**
 * DealLog.java
 * com.fhd.sys.entity.st
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-23 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * DealLog.java
 * com.fhd.sys.entity.st
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-23        金鹏祥
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

/**
 * 计划任务日志表对应实体
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-23		下午04:57:24
 *
 * @see 	 
 */
@Entity
@Table(name = "T_ST_DEAL_LOG")
public class PlanDealLog extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 计划任务关联实体ID
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PLAN_EMP_ID")
	private PlanEmp planEmp;
	
	/**
	 * 处理日志说明
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "DEAL_MEASURE")
	private String dealMeasure;
	
	/**
	 * 处理日志时间
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "DEAL_TIME")
	private String dealTime;
	
	/**
	 * 状态(启用/停用 :1/0)
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@Column(name = "ESTATUS")
	private String estatus;
	/**
	 * 计划模版id
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "EMP_ID")
	private Temp temp;
	
	public PlanEmp getPlanEmp() {
		return planEmp;
	}
	public void setPlanEmp(PlanEmp planEmp) {
		this.planEmp = planEmp;
	}
	public String getDealMeasure() {
		return dealMeasure;
	}
	public void setDealMeasure(String dealMeasure) {
		this.dealMeasure = dealMeasure;
	}
	public String getDealTime() {
		return dealTime;
	}
	public void setDealTime(String dealTime) {
		this.dealTime = dealTime;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	public Temp getTemp() {
		return temp;
	}
	public void setTemp(Temp temp) {
		this.temp = temp;
	}
}