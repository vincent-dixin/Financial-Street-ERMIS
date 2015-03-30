/**
 * AlarmPlanForm.java
 * com.fhd.kpi.web.form
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-25 		陈晓哲
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.comm.web.form;

import com.fhd.comm.entity.AlarmPlan;

/**
 * 告警预警Form
 * ClassName:AlarmPlanForm
 *
 * @author   陈晓哲
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-25		下午04:09:32
 *
 * @see 	 
 */
public class AlarmPlanForm extends AlarmPlan
{
    
    private static final long serialVersionUID = 5389600696472173693L;

    public AlarmPlanForm()
    {
    }

    /*
     * 主键ID
     */
    String id;

    /*
     * 告警类型
     */
    String types;

    /*
     * 告警名称
     */
    String name;

    /*
     * 说明
     */
    String descs;

    /*
     * 等级
     */
    String level; 

    /*
     * 区间
     */
    String range;
    
    String memo;

    public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getDescs()
    {
        return descs;
    }

    public void setDescs(String descs)
    {
        this.descs = descs;
    }

    public String getLevel()
    {
        return level;
    }

    public void setLevel(String level)
    {
        this.level = level;
    }

    public String getRange()
    {
        return range;
    }

    public void setRange(String range)
    {
        this.range = range;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public AlarmPlanForm(AlarmPlan alarm)
    {
        this.id = alarm.getId();
        this.name = alarm.getName();
        this.descs = alarm.getDesc();
        this.types = alarm.getType().getName();
    }

    public String getTypes()
    {
        return types;
    }

    public void setTypes(String types)
    {
        this.types = types;
    }
}
