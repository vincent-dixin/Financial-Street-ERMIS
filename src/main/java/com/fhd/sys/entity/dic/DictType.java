package com.fhd.sys.entity.dic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * 数据字典类型实体类.
 * 
 * @author wudefu
 * @version V1.0 创建时间：2010-8-11 Company FirstHuiDa.
 */

@Entity
@JsonAutoDetect
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "T_SYS_DICT_TYPE")
public class DictType extends IdEntity implements Serializable {

    private static final long serialVersionUID = 3989570261566003365L;

    /**
     * 名称.
     */
    @Column(name = "DICT_TYPE_NAME", nullable = false, length = 255)
    private String name;
    /**
     * 父Id.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private DictType parent;

    /**
     * 子对象
     */
    @OrderBy("name ASC")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private Set<DictType> children = new TreeSet<DictType>();

    /**
     * 级别.
     */
    @Column(name = "ELEVEL", length = 11)
    private Integer level;
    /**
     * 查询序列.
     */
    @Column(name = "ID_SEQ", length = 255)
    private String idSeq;
    /**
     * 是否系统字典
     */
    @Column(name = "IS_SYSTEM", length = 100)
    private String isSystem;

    /**
     * 数据字典条目集合(一对多维护).
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "dictType")
    private List<DictEntry> dictEntrys = new ArrayList<DictEntry>();

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public DictType getParent() {
	return parent;
    }

    public void setParent(DictType parent) {
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

    public String getIsSystem() {
	return isSystem;
    }

    public void setIsSystem(String isSystem) {
	this.isSystem = isSystem;
    }

    public List<DictEntry> getDictEntrys() {
	return dictEntrys;
    }

    public void setDictEntrys(List<DictEntry> dictEntrys) {
	this.dictEntrys = dictEntrys;
    }

    public Set<DictType> getChildren() {
	return children;
    }

    public void setChildren(Set<DictType> children) {
	this.children = children;
    }

}
