/**
 * KpiRelaAlarmForm.java
 * com.fhd.kpi.web.form
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-13 		陈晓哲
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.kpi.web.form;


import org.hibernate.ObjectNotFoundException;

import com.fhd.core.utils.DateUtils;
import com.fhd.kpi.entity.KpiRelaAlarm;

/**
 * 指标和告警关联Form对象
 *
 * @author   陈晓哲
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-13		下午01:55:00
 *
 * @see 	 
 */
public class KpiRelaAlarmForm extends KpiRelaAlarm
{
    private static final long serialVersionUID = 7843198923275104742L;

    /*
     * 主键
     */
    private String id;

    /*
     * 序
     */
    private String sort;

    /*
     * 日期
     */
    private String date;

    /*
     * 告警
     */
    private String alarm;

    /*
     * 预警
     */
    private String warning;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getSort()
    {
        return sort;
    }

    public void setSort(String sort)
    {
        this.sort = sort;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getAlarm()
    {
        return alarm;
    }

    public void setAlarm(String alarm)
    {
        this.alarm = alarm;
    }

    public String getWarning()
    {
        return warning;
    }

    public void setWarning(String warning)
    {
        this.warning = warning;
    }

    public KpiRelaAlarmForm(KpiRelaAlarm de)
    {
        this.id = de.getId();
        if(null!=de.getFcAlarmPlan()){
        	try{
        		this.warning = de.getFcAlarmPlan().getId();
        	}catch (ObjectNotFoundException e) {
			}
        }
        if(null!=de.getrAlarmPlan()){
        	try{
        		this.alarm = de.getrAlarmPlan().getId();
        	}catch (ObjectNotFoundException e) {
			}
        }
        this.date = DateUtils.formatDate(de.getStartDate(), "yyyy-MM-dd");
    }

}
