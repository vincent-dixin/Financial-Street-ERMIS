package com.fhd.icm.entity.rule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.AuditableEntity;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 规章制度
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-13		下午3:08:34
 *
 * @see 	 
 */
@Entity
@Table(name="T_IC_RULE")
public class Rule extends AuditableEntity implements Serializable {
	private static final long serialVersionUID = 9083727129909223363L;
	/**
	 * 所属公司
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPANY_ID")
	private SysOrganization company;
	/**
	 * 制度编号
	 */
	@Column(name="RULE_CODE")
	private String code;
	/**
	 * 制度名称
	 */
	@Column(name="RULE_NAME")
	private String name;
	/**
	 * 描述
	 */
	@Column(name="EDESC")
	private String desc;
	/**
	 * 制度分类
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PARENT_ID")
	private Rule parent;
	/**
	 * 主键全路径
	 */
	@Column(name="ID_SEQ")
	private String idSeq;

	/**
	 * 层级
	 */
	@Column(name = "ELEVEL")
	private Integer level;
	/**
	 * 排序
	 */
	@Column(name = "ESORT")
	private Integer sort;
	/**
	 * 是否是页子节点.
	 */
	@Column(name = "IS_LEAF")
	private Boolean isLeaf;
	
	/**
	 * 删除状态
	 */
	@Column(name = "DELETE_STATUS")
	private Boolean deleteStatus;

	public SysOrganization getCompany() {
		return company;
	}

	public void setCompany(SysOrganization company) {
		this.company = company;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Rule getParent() {
		return parent;
	}

	public void setParent(Rule parent) {
		this.parent = parent;
	}

	public String getIdSeq() {
		return idSeq;
	}

	public void setIdSeq(String idSeq) {
		this.idSeq = idSeq;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public Boolean getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(Boolean deleteStatus) {
		this.deleteStatus = deleteStatus;
	}
	

}

