package com.fhd.process.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.icm.entity.standard.Standard;

/**
 * 流程关联的内控标准 
 *
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-12-26		下午1:51:24
 *
 * @see 	 
 */
@Entity
@Table(name="T_IC_STANDARD_RELA_PROCESSURE")
public class ProcessRelaStandard extends IdEntity implements Serializable {
	private static final long serialVersionUID = 5769184742012081253L;

	/**
	 * 流程
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROCESSURE_ID")
	private Process process;
	
	/**
	 * 内控标准
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTROL_STANDARD_ID")
	private Standard standard;

	public ProcessRelaStandard(){
		
	}
	
	public ProcessRelaStandard(String id){
		super.setId(id);
	}
	
	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public Standard getStandard() {
		return standard;
	}

	public void setStandard(Standard standard) {
		this.standard = standard;
	}

	
}

