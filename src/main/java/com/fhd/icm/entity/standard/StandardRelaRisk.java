package com.fhd.icm.entity.standard;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.risk.entity.Risk;

/**
 * 内控标准关联风险
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-12-21		上午11:23:35
 *
 * @see 	 
 */
@Entity
@Table(name="T_IC_STANDARD_RELA_RISK")
public class StandardRelaRisk extends IdEntity implements Serializable {

	private static final long serialVersionUID = 8384521092350131079L;

	/**
	 * 内控标准
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STANDARD_ID")
	private Standard standard;
	
	/**
	 * 风险
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RISK_ID")
	private Risk risk;
	
	public StandardRelaRisk(){
		
	}
	
	public StandardRelaRisk(String id){
		super.setId(id);
	}
	
	public Standard getStandard() {
		return standard;
	}

	public void setStandard(Standard standard) {
		this.standard = standard;
	}

	public Risk getRisk() {
		return risk;
	}

	public void setRisk(Risk risk) {
		this.risk = risk;
	}

	
}

