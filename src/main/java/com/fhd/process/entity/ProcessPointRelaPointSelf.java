/**
 * ProcessPointRelaPointSelf.java
 * com.fhd.process.entity
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-3-12 		张 雷
 *
 * Copyright (c) 2013, Firsthuida All Rights Reserved.
*/

package com.fhd.process.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-3-12		下午12:55:32
 *
 * @see 	 
 */
@Entity
@Table(name="T_IC_CONTROL_POINT_RELEVANCE")
public class ProcessPointRelaPointSelf extends IdEntity implements Serializable {

	private static final long serialVersionUID = 2751996911392665137L;
	/**
	 * 流程
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROCESSURE_ID")
	private Process process;
	public ProcessPointRelaPointSelf(){
		
	}
	
	public ProcessPointRelaPointSelf(String id){
		super.setId(id);
	}
	/**
	 * 流程节点
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTROL_POINT_ID")
	private ProcessPoint processPoint;
	
	/**
	 * 上一个流程节点
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PREVIOUS_CONTROL_POINT_ID")
	private ProcessPoint PreviousProcessPoint;
	
	/**
	 * 进入条件
	 */
	@Column(name="enter_condition",length=4000)
	private String desc;
	
	
	/**
	 * 主键全路径
	 */
	@Column(name="ID_SEQ")
	private String idSeq;


	public Process getProcess() {
		return process;
	}


	public void setProcess(Process process) {
		this.process = process;
	}


	public ProcessPoint getProcessPoint() {
		return processPoint;
	}


	public void setProcessPoint(ProcessPoint processPoint) {
		this.processPoint = processPoint;
	}


	public ProcessPoint getPreviousProcessPoint() {
		return PreviousProcessPoint;
	}


	public void setPreviousProcessPoint(ProcessPoint previousProcessPoint) {
		PreviousProcessPoint = previousProcessPoint;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	public String getIdSeq() {
		return idSeq;
	}


	public void setIdSeq(String idSeq) {
		this.idSeq = idSeq;
	}
	
	
	
}

