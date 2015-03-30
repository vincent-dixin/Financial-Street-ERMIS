package com.fhd.icm.entity.rectify;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.process.entity.Process;

/**
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-26		下午4:25:38
 *
 * @see 	 
 */
@Entity
@Table(name="T_RECTIFY_IMPROVE_PROCESSURE")
public class ImproveRelaProcess extends IdEntity implements Serializable {
	private static final long serialVersionUID = -8103016942930543449L;

	/**
	 * 整改计划
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMPROVEMENT_ID")
	private Improve improve;
	
	/**
	 * 流程
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROCESSURE_ID")
	private Process process;

	public ImproveRelaProcess(){
		
	}
	
	public ImproveRelaProcess(String id){
		super.setId(id);
	}
	
	public Improve getImprove() {
		return improve;
	}

	public void setImprove(Improve improve) {
		this.improve = improve;
	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}
	
	
	
}

