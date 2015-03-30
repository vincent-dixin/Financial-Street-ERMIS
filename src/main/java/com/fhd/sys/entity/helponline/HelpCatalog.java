/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.sys.entity.helponline;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;


/**
 * 帮助目录
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-5-20		下午12:00:50
 *
 * @see 	 
 */
@Entity
@Table(name = "T_HLP_CATALOG")
public class HelpCatalog extends IdEntity {

	
	private static final long serialVersionUID = 1L;

	/**
	 * 目录名称
	 */
	@Column(name = "CATALOG_NAME")
	private String catalogName;
	
	
	/**
	 * ID串
	 */
	@Column(name = "ID_SEQ")
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_ID")
	private HelpCatalog parent;
	
	@OrderBy("sort asc")
	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "parent")
	private Set<HelpCatalog> children;
	
	/**
	 * 帮助主题
	 */
	@OrderBy("sort asc")
	@Where(clause = "ETYPE = 'catalog'")
	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "helpCatalog")
	private Set<HelpTopic> helpTopics;
	
	
	
	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
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

	public Set<HelpTopic> getHelpTopics() {
		return helpTopics;
	}

	public void setHelpTopics(Set<HelpTopic> helpTopics) {
		this.helpTopics = helpTopics;
	}

	public HelpCatalog getParent() {
		return parent;
	}

	public void setParent(HelpCatalog parent) {
		this.parent = parent;
	}

	public Set<HelpCatalog> getChildren() {
		return children;
	}

	public void setChildren(Set<HelpCatalog> children) {
		this.children = children;
	}
	
	
}

