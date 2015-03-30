/**
 * KpiGatherResultForm.java
 * com.fhd.kpi.web.form
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-16 		陈晓哲
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.kpi.web.form;

import com.fhd.kpi.entity.KpiGatherResult;
import com.fhd.kpi.entity.KpiRelaCategory;

/**
 * 指标关联采集信息
 *
 * @author   陈晓哲
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-16		下午04:22:27
 *
 * @see 	 
 */
public class KpiGatherResultForm extends KpiGatherResult
{

    private String name;

    private String id;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    private static final long serialVersionUID = 2163215714355027798L;

    public KpiGatherResultForm(KpiRelaCategory result)
    {
        this.name = result.getKpi().getName();
        this.id = result.getId();
    }

}
