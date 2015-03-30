/**
 * CategoryRelaOrgEmp.java
 * com.fhd.comm.entity
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-14 		陈晓哲
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.comm.entity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 维度关联部门和人员实体
 *
 * @author   陈晓哲
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-14		下午09:28:29
 *
 * @see 	 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable(true)
@Table(name = "t_com_category_rela_org_emp")
public class CategoryRelaOrgEmp extends IdEntity implements java.io.Serializable
{

    private static final long serialVersionUID = 2942516753721425265L;

    /**
     * 维度
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    /**
     * 部门
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORG_ID")
    private SysOrganization org;

    /**
     * 类型  B所属部门（维护基本信息）；R报告部门；V查看部门，G采集部门，T目标设定部门
     */
    @Column(name = "ETYPE", length = 100)
    private String type;

    /**
     * 员工
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMP_ID")
    private SysEmployee emp;

    public CategoryRelaOrgEmp()
    {
    }

    public CategoryRelaOrgEmp(String id)
    {
        setId(id);
    }

    public Category getCategory()
    {
        return category;
    }

    public void setCategory(Category category)
    {
        this.category = category;
    }

    public SysOrganization getOrg()
    {
        return org;
    }

    public void setOrg(SysOrganization org)
    {
        this.org = org;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public SysEmployee getEmp()
    {
        return emp;
    }

    public void setEmp(SysEmployee emp)
    {
        this.emp = emp;
    }

}
