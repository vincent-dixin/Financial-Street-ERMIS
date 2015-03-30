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
 * 业务表和数据字典关系表的类型，用于业务表有多个字典数据
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
@Table(name = "T_SYS_DICTENTRY_RELATIONTYPE")
public class DictEntryRelationType extends IdEntity implements Serializable {

    private static final long serialVersionUID = 603688230287715711L;
    
    
    /**
     * 业务类型名称.
     */
    @Column(name = "TYPE_NAME", nullable = false)
    private String typeName;
    
    public DictEntryRelationType() {
    	
    }

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
    
}
