package com.fhd.icm.entity.standard;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.process.entity.Process;

/**
 * 内控标准关联流程
 * @author   元杰
 * @version  
 * @see 	 
 */
@Entity
@Table(name="T_IC_STANDARD_RELA_PROCESSURE")
public class StandardRelaProcessure extends IdEntity implements Serializable {
	private static final long serialVersionUID = 5667483169645180007L;

	/**
	 * 内控标准
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTROL_STANDARD_ID")
	private Standard standard;
	
	/**
	 * 流程
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROCESSURE_ID")
	private Process processure;
	
	public StandardRelaProcessure(){
		
	}
	
	public StandardRelaProcessure(String id){
		super.setId(id);
	}
	
	public Standard getStandard() {
		return standard;
	}

	public void setStandard(Standard standard) {
		this.standard = standard;
	}

	public Process getProcessure() {
		return processure;
	}

	public void setProcessure(Process processure) {
		this.processure = processure;
	}
}

