/**
 * DictEntry.java
 * com.fhd.fdc.commons.entity.dic
 *
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-14 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
 */

package com.fhd.sys.entity.dic;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * ClassName:DictEntry
 * 
 * @author 张 雷
 * @version
 * @since Ver 1.1
 * @Date 2012 2012-9-18 下午1:24:42
 * 
 * @see
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable(true)
@JsonAutoDetect
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "dictType" })
@Table(name = "T_SYS_DICT_ENTRY")
public class DictEntry extends IdEntity implements Serializable {

    private static final long serialVersionUID = 603688230287715711L;
    /**
     * 数据字典类型(多对一维护).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DICT_TYPE_ID")
    @JsonIgnore
    private DictType dictType;
    /**
     * 数据字典条目名称.
     */
    @Column(name = "DICT_ENTRY_NAME", nullable = false)
    private String name;
    /**
     * 数据字典条目值.
     */
    @Column(name = "DICT_ENTRY_VALUE")
    private String value;
    /**
     * 数据字典条目状态，默认为1：可用.
     */
    @Column(name = "ESTATUS", nullable = false)
    private String status;
    /**
     * 数据字典条目序号.
     */
    @Column(name = "ESORT", nullable = true)
    private Integer sort;
    /**
     * 父Id.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private DictEntry parent;

    /**
     * 子对象
     */
    @OrderBy("name ASC")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private Set<DictEntry> children = new TreeSet<DictEntry>();

    /**
     * 级别.
     */
    @Column(name = "ELEVEL")
    private Integer level;
    /**
     * 查询序列.
     */
    @Column(name = "ID_SEQ", length = 255)
    private String idSeq;
    /**
     * 保留字段1.
     */
    @Column(name = "RESERVED1", length = 255)
    private String reserved1;
    /**
     * 说明.
     */
    @Column(name = "RESERVED2", length = 255)
    private String edesc;
    /**
     * 是否系统字典
     * */
    @Column(name = "IS_SYSTEM", length = 100)
    private String isSystem;

    /**
     * 是否是页子结点.
     */
    @Column(name = "IS_LEAF")
    private Boolean isLeaf;

    public DictEntry() {
    }

    public DictEntry(String id) {
	this.setId(id);
    }

    public DictType getDictType() {
	return dictType;
    }

    public void setDictType(DictType dictType) {
	this.dictType = dictType;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public Integer getSort() {
	return sort;
    }

    public void setSort(Integer sort) {
	this.sort = sort;
    }

    public DictEntry getParent() {
	return parent;
    }

    public void setParent(DictEntry parent) {
	this.parent = parent;
    }

    public Integer getLevel() {
	return level;
    }

    public void setLevel(Integer level) {
	this.level = level;
    }

    public String getIdSeq() {
	return idSeq;
    }

    public void setIdSeq(String idSeq) {
	this.idSeq = idSeq;
    }

    public String getReserved1() {
	return reserved1;
    }

    public void setReserved1(String reserved1) {
	this.reserved1 = reserved1;
    }

    public String getEdesc() {
	return edesc;
    }

    public void setEdesc(String edesc) {
	this.edesc = edesc;
    }

    public String getIsSystem() {
	return isSystem;
    }

    public void setIsSystem(String isSystem) {
	this.isSystem = isSystem;
    }

    public Boolean getIsLeaf() {
	return isLeaf;
    }

    public void setIsLeaf(Boolean isLeaf) {
	this.isLeaf = isLeaf;
    }

    public Set<DictEntry> getChildren() {
	return children;
    }

    public void setChildren(Set<DictEntry> children) {
	this.children = children;
    }
}
