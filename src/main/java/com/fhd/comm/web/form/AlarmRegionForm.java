/**
 * AlarmRegionForm.java
 * com.fhd.kpi.web.form
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-26 		陈晓哲
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.comm.web.form;

import org.apache.commons.lang.StringUtils;

import com.fhd.comm.entity.AlarmRegion;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 告警关联区间值Form
 * ClassName:AlarmRegionForm
 *
 * @author   陈晓哲
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-26		下午08:42:25
 *
 * @see 	 
 */
public class AlarmRegionForm
{
    public AlarmRegionForm()
    {
    }

    /*
     * 主键
     */
    private String id;

    /*
     * 区间值
     */
    private String range;

    /*
     * 等级
     */
    private String level;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getRange()
    {
        return range;
    }

    public void setRange(String range)
    {
        this.range = range;
    }

    public String getLevel()
    {
        return level;
    }

    public void setLevel(String level)
    {
        this.level = level;
    }

    public AlarmRegionForm(AlarmRegion region)
    {
        String level = "";
        this.id = region.getId();
        if (region.getAlarmIcon() != null)
        {
            level = region.getAlarmIcon().getId();
        }
        this.level = level;
        /*区间值需要拼接成表达式*/
        this.range = createExpression(region);

    }

    /**
     * <pre>
     * 生成区间表达式
     * </pre>
     * 
     * @author 陈晓哲
     * @param region
     * @return
     * @since  fhd　Ver 1.1
    */
    private String createExpression(AlarmRegion region)
    {
        StringBuffer expression = new StringBuffer();
        DictEntry maxSignEntry = region.getIsContainMax();//最大符号
        DictEntry minSignEntry = region.getIsContainMin();//最小符号
        String formulaMax = region.getMaxValue();
        String formulaMin = region.getMinValue();

        if (StringUtils.isNotBlank(formulaMin))
        {
            expression.append(formulaMin);
        }
        if (minSignEntry != null)
        {
            String minsign = minSignEntry.getName();
            expression.append(minsign);
        }
        expression.append("X");
        if (maxSignEntry != null)
        {
            expression.append(maxSignEntry.getName());
        }
        if (StringUtils.isNotBlank(formulaMax))
        {
            expression.append(formulaMax);
        }
        return expression.toString();
    }

}
