/**
 * TimePeriod.java
 * com.fhd.comm.entity
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-21 		陈晓哲
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.comm.entity;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

/**
 * 时间区间实体
 *
 * @author   陈晓哲
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-21		下午09:18:01
 *
 * @see 	 
 */
@Entity
@Table(name = "t_com_time_period")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable(true)
public class TimePeriod extends IdEntity implements java.io.Serializable
{
    private static final long serialVersionUID = -4365595452638221722L;

    /*
     * 时间维度名称
     */
    @Column(name = "TIME_PERIOD_NAME")
    private String timePeriodName;

    /*
     * 开始时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_TIME")
    private Date startTime;

    /*
     * 结束时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_TIME")
    private Date endTime;

    /*
     * 父时间维度
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private TimePeriod parent;

    /*
     * 年
     */
    @Column(name = "EYEAR")
    private String year;

    /*
     * 季度
     */
    @Column(name = "EQUARTER")
    private String quarter;

    /*
     * 月
     */
    @Column(name = "EMONTH")
    private String month;

    /*
     * 全名称
     */
    @Column(name = "TIME_PERIOD_FULL_NAME")
    private String timePeriodFullName;

    /*
     * 类型
     */
    @Column(name = "ETYPE", insertable = false, updatable = false)
    private String type;

    /*
     * 下一时间区间
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NEXT_PERIOD_ID")
    private TimePeriod nextPeriod;

    /*
     * 前一时间区间
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRE_PERIOD_ID")
    private TimePeriod prePeriod;

    /*
     * 前年同期
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRE_YEAR_PERIOD_ID")
    private TimePeriod preYearPeriod;

    /*
     * 上半/下半年(0/1)
     */
    @Column(name = "EHALFYEAR")
    private String halfYear;

    public TimePeriod getPreYearPeriod()
    {
        return preYearPeriod;
    }

    public void setPreYearPeriod(TimePeriod preYearPeriod)
    {
        this.preYearPeriod = preYearPeriod;
    }

    public String getHalfYear()
    {
        return halfYear;
    }

    public void setHalfYear(String halfYear)
    {
        this.halfYear = halfYear;
    }

    public TimePeriod getNextPeriod()
    {
        return nextPeriod;
    }

    public void setNextPeriod(TimePeriod nextPeriod)
    {
        this.nextPeriod = nextPeriod;
    }

    public TimePeriod getPrePeriod()
    {
        return prePeriod;
    }

    public void setPrePeriod(TimePeriod prePeriod)
    {
        this.prePeriod = prePeriod;
    }

    public String getTimePeriodName()
    {
        return timePeriodName;
    }

    public void setTimePeriodName(String timePeriodName)
    {
        this.timePeriodName = timePeriodName;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public TimePeriod getParent()
    {
        return parent;
    }

    public void setParent(TimePeriod parent)
    {
        this.parent = parent;
    }

    public String getTimePeriodFullName()
    {
        return timePeriodFullName;
    }

    public void setTimePeriodFullName(String timePeriodFullName)
    {
        this.timePeriodFullName = timePeriodFullName;
    }

    public String getYear()
    {
        return year;
    }

    public void setYear(String year)
    {
        this.year = year;
    }

    public String getQuarter()
    {
        return quarter;
    }

    public void setQuarter(String quarter)
    {
        this.quarter = quarter;
    }

    public String getMonth()
    {
        return month;
    }

    public void setMonth(String month)
    {
        this.month = month;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}