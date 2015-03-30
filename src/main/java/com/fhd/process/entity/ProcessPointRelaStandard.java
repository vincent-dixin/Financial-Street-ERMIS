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
 * 流程节点关联的内控标准 
 *
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-12-26		下午1:51:24
 *
 * @see 	 
 */
@Entity
@Table(name="T_IC_STANDARD_RELA_POINT")
public class ProcessPointRelaStandard extends IdEntity implements Serializable {
	private static final long serialVersionUID = 5769184742012081253L;

	/**
	 * 流程节点
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTROL_POINT_ID")
	private ProcessPoint processPoint;
	
	/**
	 * 内控标准
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTROL_STANDARD_ID")
	private Standard standard;

	public ProcessPointRelaStandard(){
		
	}
	
	public ProcessPointRelaStandard(String id){
		super.setId(id);
	}
	
	public ProcessPoint getProcessPoint() {
		return processPoint;
	}

	public void setProcessPoint(ProcessPoint processPoint) {
		this.processPoint = processPoint;
	}

	public Standard getStandard() {
		return standard;
	}

	public void setStandard(Standard standard) {
		this.standard = standard;
	}

	
}

