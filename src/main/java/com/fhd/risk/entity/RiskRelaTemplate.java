package com.fhd.risk.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * ClassName:RiskRelaTemplate 
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-13		下午2:07:01
 *
 * @see 	 
 */
@Entity
@Table(name = "T_RM_RISK_RELA_TEMPLATE") 
public class RiskRelaTemplate extends IdEntity implements Serializable{
	private static final long serialVersionUID = -560056895428600775L;
	
	/**
	 * 模板
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEMPLATE_ID")
	private Template template;
	
	/**
	 * 风险
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RISK_ID")
	private Risk risk;
	
	/**
	 * 是否创建者
	 */
	@Column(name = "TEMPLATE_RESOURCE")
	private Boolean isCreator;

	public RiskRelaTemplate(){
		
	}
	
	public RiskRelaTemplate(String id){
		super.setId(id);
	}
	
	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public Risk getRisk() {
		return risk;
	}

	public void setRisk(Risk risk) {
		this.risk = risk;
	}

	public Boolean getIsCreator() {
		return isCreator;
	}

	public void setIsCreator(Boolean isCreator) {
		this.isCreator = isCreator;
	}

	

}

