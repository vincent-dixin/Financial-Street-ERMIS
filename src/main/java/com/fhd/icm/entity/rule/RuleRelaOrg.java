package com.fhd.icm.entity.rule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 规章制度关联部门
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-21		下午2:33:58
 *
 * @see 	 
 */
@Entity
@Table(name="T_IC_RULE_RELA_ORG")
public class RuleRelaOrg extends IdEntity implements Serializable {
	private static final long serialVersionUID = 163995306095278048L;

	/**
	 * 规章制度
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RULE_ID")
	private Rule rule;
	
	/**
	 * 部门
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID")
	private SysOrganization org;
	
	/**
	 * 类型：责任部门，配合部门
	 */
	@Column(name="ETYPE")
	private String type;

	
	public RuleRelaOrg() {
	}
	
	public RuleRelaOrg(String id) {
		 this.setId(id);
	}
	
	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public SysOrganization getOrg() {
		return org;
	}

	public void setOrg(SysOrganization org) {
		this.org = org;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
}

