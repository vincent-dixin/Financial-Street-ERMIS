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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
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
@JsonAutoDetect
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "dictEntry" })
@Table(name = "T_SYS_DICT_ENTRY_I18")
public class DictEntryI18 extends IdEntity implements Serializable {

    private static final long serialVersionUID = 603688230287715711L;
    /**
     * 数据字典类型(多对一维护).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DICT_ENTRY_ID")
    private DictEntry dictEntry;
    /**
     * 区域.
     */
    @Column(name = "MY_LOCALE", nullable = false)
    private String myLocale;

    /**
     * 名称.
     */
    @Column(name = "DICT_ENTRY_NAME")
    private String name;

    public DictEntry getDictEntry() {
	return dictEntry;
    }

    public void setDictEntry(DictEntry dictEntry) {
	this.dictEntry = dictEntry;
    }

    public String getMyLocale() {
	return myLocale;
    }

    public void setMyLocale(String myLocale) {
	this.myLocale = myLocale;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public DictEntryI18(String id) {
	this.setId(id);
    }

    public DictEntryI18() {
    }
}
