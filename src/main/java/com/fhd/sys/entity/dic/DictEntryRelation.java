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
 * 业务表和数据字典关系表
 * 
 * @author 郑军祥
 * @version
 * @since Ver 1.0
 * @Date 2013-5-31
 * 
 * @see
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable(true)
@Table(name = "T_SYS_DICTENTRY_RELATION")
public class DictEntryRelation extends IdEntity implements Serializable {

    private static final long serialVersionUID = 603688230287715711L;
    
    /**
     * 数据字典关系ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TYPE_ID")
    private DictEntryRelationType relationType;
    
    /**
     * 数据字典
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENTRY_ID")
    private DictEntry dictEntry;
    
    /**
     * 业务表ID.
     */
    @Column(name = "BUSINESS_ID", nullable = false)
    private String businessId;
        
    public DictEntryRelation() {
    	
    }

	public DictEntryRelationType getRelationType() {
		return relationType;
	}


	public void setRelationType(DictEntryRelationType relationType) {
		this.relationType = relationType;
	}


	public DictEntry getDictEntry() {
		return dictEntry;
	}


	public void setDictEntry(DictEntry dictEntry) {
		this.dictEntry = dictEntry;
	}


	public String getBusinessId() {
		return businessId;
	}


	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
    
}
