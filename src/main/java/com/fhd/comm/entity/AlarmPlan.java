package com.fhd.comm.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 
 * 预警方案
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-9-17		上午9:42:29
 *
 * @see
 */
@Entity
@Table(name = "t_com_alarm_plan")
public class AlarmPlan extends IdEntity implements java.io.Serializable
{

    /**
     *
     * @author 胡迪新
     * @since  fhd　Ver 1.1
     */
    private static final long serialVersionUID = 1L;

    // Fields
    /**
     * 公司编号
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID")
    private SysOrganization company;

    /**
     * 名称
     */
    @Column(name = "ALARM_NAME")
    private String name;

    /**
     * 描述
     */
    @Column(name = "EDESC", length = 2000)
    private String desc;

    /**
     * 排序
     */
    @Column(name = "ESORT")
    private Integer sort;

    /**
     * 类型
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ETYPE")
    private DictEntry type;

    /**
     * 删除状态
     */
    @Column(name = "DELETE_STATUS", length = 100)
    private Boolean deleteStatus;

    /**
     * 是否默认方案
     */
    @Column(name = "IS_DEFAULT", length = 100)
    private Boolean isDefault;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "alarmPlan")
    private Set<AlarmRegion> alarmRegions = new HashSet<AlarmRegion>(0);

    // Constructors

    /** default constructor */
    public AlarmPlan()
    {
    }

    /** minimal constructor */
    public AlarmPlan(String id)
    {
        setId(id);
    }

    public SysOrganization getCompany()
    {
        return company;
    }

    public void setCompany(SysOrganization company)
    {
        this.company = company;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public Integer getSort()
    {
        return sort;
    }

    public void setSort(Integer sort)
    {
        this.sort = sort;
    }

    public Boolean getDeleteStatus()
    {
        return deleteStatus;
    }

    public void setDeleteStatus(Boolean deleteStatus)
    {
        this.deleteStatus = deleteStatus;
    }

    public Boolean getIsDefault()
    {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault)
    {
        this.isDefault = isDefault;
    }

    public Set<AlarmRegion> getAlarmRegions()
    {
        return alarmRegions;
    }

    public void setAlarmRegions(Set<AlarmRegion> alarmRegions)
    {
        this.alarmRegions = alarmRegions;
    }

    public DictEntry getType()
    {
        return type;
    }

    public void setType(DictEntry type)
    {
        this.type = type;
    }
}