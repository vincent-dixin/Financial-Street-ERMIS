package com.fhd.process.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.risk.entity.Risk;

/**
 * 流程节点关联风险
 * @author   张  雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-19		上午11:44:27
 *
 * @see 	 
 */
@Entity
@Table(name="T_IC_CONTROL_POINT_RELA_RISK")
public class ProcessPointRelaRisk extends IdEntity implements Serializable {
	private static final long serialVersionUID = -4254016258155064776L;

	/**
	 * 流程（冗余字段，不作为关联）
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROCESSURE_ID",insertable=false,updatable=false)
	private Process process;
	
	/**
	 * 流程节点
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROCESSURE_ID")
	private ProcessPoint processPoint ;
	
	/**
	 * 风险
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RISK_ID")
	private Risk risk;
	
	
	public ProcessPointRelaRisk(){
		
	}
	
	public ProcessPointRelaRisk(String id){
		super.setId(id);
	}
	
	
	
	public ProcessPoint getProcessPoint() {
		return processPoint;
	}

	public void setProcessPoint(ProcessPoint processPoint) {
		this.processPoint = processPoint;
	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public Risk getRisk() {
		return risk;
	}

	public void setRisk(Risk risk) {
		this.risk = risk;
	}
}


