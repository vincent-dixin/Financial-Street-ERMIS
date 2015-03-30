package com.fhd.icm.entity.control;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.icm.entity.rule.Rule;

/**
 * 控制措施关联规章制度
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-21		下午2:41:41
 *
 * @see 	 
 */
@Entity
@Table(name="T_CON_MEASURE_RELA_RULE")
public class MeasureRelaRule extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = 6582493320465184350L;


	/**
	 * 控制措施
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTROL_MEASURE_ID")
	private Measure controlMeasure;
	
	/**
	 * 规章制度
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RULE_ID")
	private Rule rule;
	
	public MeasureRelaRule(){
		
	}
	
	public MeasureRelaRule(String id){
		super.setId(id);
	}

	public Measure getControlMeasure() {
		return controlMeasure;
	}

	public void setControlMeasure(Measure controlMeasure) {
		this.controlMeasure = controlMeasure;
	}

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}
	
	
	
}

