/**
 * CategoryRelaAlarm.java
 * com.fhd.comm.entity
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-14 		陈晓哲
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.comm.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * 维度关联预警实体
 *
 * @author   陈晓哲
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-14		下午10:02:34
 *
 * @see 	 
 */
@Entity
@Table(name = "t_com_category_rela_alarm")
public class CategoryRelaAlarm extends IdEntity implements Serializable
{
    private static final long serialVersionUID = 1510597145328864883L;

    /**
     * 预警方案
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FC_ALARM_PLAN_ID")
    private AlarmPlan fcAlarmPlan;

    /**
     * 告警方案
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "R_ALARM_PLAN_ID")
    private AlarmPlan rAlarmPlan;

    /**
     * 开始日期
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE")
    private Date startDate;

    /**
     * 指标
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    public AlarmPlan getFcAlarmPlan()
    {
        return fcAlarmPlan;
    }

    public void setFcAlarmPlan(AlarmPlan fcAlarmPlan)
    {
        this.fcAlarmPlan = fcAlarmPlan;
    }

    public AlarmPlan getrAlarmPlan()
    {
        return rAlarmPlan;
    }

    public void setrAlarmPlan(AlarmPlan rAlarmPlan)
    {
        this.rAlarmPlan = rAlarmPlan;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Category getCategory()
    {
        return category;
    }

    public void setCategory(Category category)
    {
        this.category = category;
    }

    public CategoryRelaAlarm()
    {
    }

    public CategoryRelaAlarm(String id)
    {
        setId(id);
    }

}
