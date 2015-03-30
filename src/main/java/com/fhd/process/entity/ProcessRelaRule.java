package com.fhd.process.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.icm.entity.rule.Rule;

/**
 * 流程关联规章制度
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-21		下午2:41:41
 *
 * @see 	 
 */
@Entity
@Table(name="T_IC_PROCESSURE_RELA_RULE")
public class ProcessRelaRule extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = 6582493320465184350L;

	/**
	 * 流程
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROCESSURE_ID")
	private Process process;
	
	/**
	 * 规章制度
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RULE_ID")
	private Rule rule;
	
	public ProcessRelaRule(){
		
	}
	
	public ProcessRelaRule(String id){
		super.setId(id);
	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}
	
	
	
}

