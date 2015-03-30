package com.fhd.icm.entity.control;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.risk.entity.Risk;

/**
 * 控制措施关联风险
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2013	2013-3-20		上午10:59:10
 *
 * @see 	 
 */
@Entity
@Table(name="T_CON_MEASURE_RELA_RISK")
public class MeasureRelaRisk extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = -2135554111777895411L;

	/**
	 * 控制措施
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTROL_MEASURE_ID")
	private Measure controlMeasure;
	
	/**
	 * 风险
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RISK_ID")
	private Risk  risk;
	
	public MeasureRelaRisk(){
		
	}
	
	public MeasureRelaRisk(String id){
		super.setId(id);
	}

	public Measure getControlMeasure() {
		return controlMeasure;
	}

	public void setControlMeasure(Measure controlMeasure) {
		this.controlMeasure = controlMeasure;
	}

	public Risk getRisk() {
		return risk;
	}

	public void setRisk(Risk risk) {
		this.risk = risk;
	}

}

