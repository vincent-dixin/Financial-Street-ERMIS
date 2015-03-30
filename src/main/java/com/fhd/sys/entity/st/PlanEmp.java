/**
 * PlanEmp.java
 * com.fhd.sys.entity.st
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-17 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * PlanEmp.java
 * com.fhd.sys.entity.st
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-17        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
*/


package com.fhd.sys.entity.st;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * 计划任务、模版关联表对应实体
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-17		上午11:42:21
 *
 * @see 	 
 */
@Entity
@Table(name = "T_ST_PLAN_EMP")
public class PlanEmp extends IdEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 计划任务实体
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PLAN_ID")
	private Plan plan;
	
	/**
	 * 计划任务模版实体
	 *
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "EMP_ID")
	private Temp temp;
	
	public PlanEmp(){
	}
	
	public PlanEmp(String id){
		this.setId(id);
	}
	
	public Temp getTemp() {
		return temp;
	}
	public void setTemp(Temp temp) {
		this.temp = temp;
	}
	public Plan getPlan() {
		return plan;
	}
	public void setPlan(Plan plan) {
		this.plan = plan;
	}
}