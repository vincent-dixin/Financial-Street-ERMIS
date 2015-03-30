/**
 * AlarmPlanControl.java
 * com.fhd.kpi.web.controller
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-25 		陈晓哲
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.comm.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.comm.business.AlarmPlanBO;
import com.fhd.comm.business.CategoryBO;
import com.fhd.comm.business.formula.FormulaAutoCalculateBO;
import com.fhd.comm.entity.AlarmPlan;
import com.fhd.comm.entity.AlarmRegion;
import com.fhd.comm.web.form.AlarmPlanForm;
import com.fhd.comm.web.form.AlarmRegionForm;
import com.fhd.core.dao.Page;
import com.fhd.kpi.entity.Kpi;
import com.fhd.sys.business.st.task.bo.RelaAssessResultStorageTask;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 预警告警Controller
 * ClassName:AlarmPlanControl
 *
 * @author   陈晓哲
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-25		下午01:51:15
 *
 * @see 	 
 */
@Controller
public class AlarmPlanControl
{

    @Autowired
    private AlarmPlanBO o_alarmPlanBO;
    
    @Autowired
    private CategoryBO o_categoryBO;
    
    /*@Autowired
    private FormulaAutoCalculateBO calc;
    
    @Autowired
    private RelaAssessResultStorageTask cate;*/

    /**
     * <pre>
     * 根据告警ID查询
     * </pre>
     * 
     * @author 陈晓哲
     * @param id 告警方案ID
     * @return
     * @since  fhd　Ver 1.1
    */
    @ResponseBody
    @RequestMapping("/kpi/alarm/findalarmplanbyid.f")
    public Map<String, Object> findAlarmPlanById(String id)
    {
        JSONArray Regions = new JSONArray();
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        AlarmPlan alarmPlan = this.o_alarmPlanBO.findAlarmPlanById(id);
        jsonMap.put("name", alarmPlan.getName());
        jsonMap.put("types", alarmPlan.getType().getId());
        jsonMap.put("descs", alarmPlan.getDesc());

        Set<AlarmRegion> alarmRegions = alarmPlan.getAlarmRegions();
        for (AlarmRegion alarmRegion : alarmRegions)
        {
            JSONObject jsobj = new JSONObject();
            jsobj.put("id", alarmRegion.getId());
            jsobj.put("formulaOne", alarmRegion.getMinValue());
            jsobj.put("formulaTwo", alarmRegion.getMaxValue());
            if(null!=alarmRegion.getIsContainMin()){
                jsobj.put("signOneId", alarmRegion.getIsContainMin().getId());
                jsobj.put("signOneValue", alarmRegion.getIsContainMin().getName());
            }
            if(null!=alarmRegion.getIsContainMax()){
                jsobj.put("signTwoId", alarmRegion.getIsContainMax().getId());
                jsobj.put("signTwoValue", alarmRegion.getIsContainMax().getName());
            }
            
            Regions.add(jsobj);
        }

        result.put("success", true);
        result.put("data", jsonMap);
        result.put("regions", Regions.toString());
        return result;
    }

    /**
     * <pre>
     * 根据告警ID查询所关联的区间值信息,赋值给store
     * </pre>
     * 
     * @author 陈晓哲
     * @param id 告警方案ID
     * @return
     * @since  fhd　Ver 1.1
    */
    @ResponseBody
    @RequestMapping("/kpi/alarm/findalarmplanregions.f")
    public Map<String, Object> findAlarmPlanRegions(String id)
    {
        AlarmRegionForm form = null;
        Map<String, Object> map = new HashMap<String, Object>();
        List<AlarmRegionForm> datas = new ArrayList<AlarmRegionForm>();
        if (id != null)
        {
            AlarmPlan alarmPlan = this.o_alarmPlanBO.findAlarmPlanById(id);
            Set<AlarmRegion> alarmRegions = alarmPlan.getAlarmRegions();
            for (AlarmRegion alarmRegion : alarmRegions)
            {
                form = new AlarmRegionForm(alarmRegion);
                datas.add(form);
            }
        }
        map.put("datas", datas);
        return map;
    }

    /**
     * <pre>
     *查询告警方案的区间信息,供右侧面板查看使用
     * </pre>
     * 
     * @author 陈晓哲
     * @param id 告警方案ID
     * @return
     * @since  fhd　Ver 1.1
    */
    @ResponseBody
    @RequestMapping("/kpi/alarm/findalarmplanregionstoviewbyid.f")
    public Map<String, Object> findAlarmplanRegionsToViewById(String id)
    {
        String exp = "";
        String level = "";
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> regionsMap = new HashMap<String, Object>();
        if (id != null)
        {
            AlarmPlan alarmPlan = this.o_alarmPlanBO.findAlarmPlanById(id);
            Set<AlarmRegion> alarmRegions = alarmPlan.getAlarmRegions();
            for (AlarmRegion alarmRegion : alarmRegions)
            {
                if (alarmRegion.getAlarmIcon() != null)
                {
                    level = alarmRegion.getAlarmIcon().getId();
                }
                StringBuffer expression = new StringBuffer();
                DictEntry maxSignEntry = alarmRegion.getIsContainMax();//最大符号
                DictEntry minSignEntry = alarmRegion.getIsContainMin();//最小符号
                String formulaMax = alarmRegion.getMaxValue();
                String formulaMin = alarmRegion.getMinValue();

                if (StringUtils.isNotBlank(formulaMin))
                {
                    expression.append(formulaMin);
                }
                if (minSignEntry != null)
                {
                    String minsign = minSignEntry.getName();
                    if (minsign.contains("<"))
                    {
                        minsign = minsign.replace("<", "&lt;");
                    }
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
                exp = expression.toString();
                if ("0alarm_startus_h".equals(level))
                {//高
                    regionsMap.put("high", exp);
                }
                if ("0alarm_startus_m".equals(level))
                {//中
                    regionsMap.put("mid", exp);
                }
                if ("0alarm_startus_l".equals(level))
                {//低
                    regionsMap.put("low", exp);
                }
                if("0alarm_startus_safe".equals(level)){
                    regionsMap.put("safe", exp);
                }
            }
        }
        map.put("regions", regionsMap);
        map.put("success", true);
        return map;
    }

    @ResponseBody
    @RequestMapping("/kpi/alarm/findriskalarmplan.f")
    public Map<String, Object> findriskalarmplan() throws Exception
    {
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	String type = "0alarm_type_rm";	//风险告警方案
    	List<AlarmPlan> alarmPlanlist = o_alarmPlanBO.findAlarmPlanByType(type);
    	List<AlarmPlanForm> datas = new ArrayList<AlarmPlanForm>();
    	AlarmPlanForm form = new AlarmPlanForm();
    	for (AlarmPlan alarmPlan : alarmPlanlist)
        {
            form = new AlarmPlanForm(alarmPlan);
            datas.add(form);
        }
    	map.put("totalCount", datas.size());
        map.put("datas", datas);
    	
        return map;
    }
    /**
     * <pre>
     * 根据查询条件查询所有的告警方案
     * </pre>
     * 
     * @author 陈晓哲
     * @param start
     * @param limit
     * @param query查询条件
     * @param sort
     * @return
     * @throws Exception
     * @since  fhd　Ver 1.1
    */
    @ResponseBody
    @RequestMapping("/kpi/alarm/findalarmplanbysome.f")
    public Map<String, Object> findAlarmPlanBySome(int start, int limit, String query, String sort) throws Exception
    {
    	//calc.formulaAutoCalculate();
    	//cate.categoryAssessedService();
        //List<Kpi> kpilist = o_categoryBO.findChildKpiByCategoryName("运营计划风险");
        String dir = "ASC";
        String sortColumn = "id";
        AlarmPlanForm form = null;
        List<AlarmPlanForm> datas = new ArrayList<AlarmPlanForm>();
        if (StringUtils.isNotBlank(sort))
        {
            JSONArray jsonArray = JSONArray.fromObject(sort);
            if (jsonArray.size() > 0)
            {
                JSONObject jsobj = jsonArray.getJSONObject(0);
                sortColumn = jsobj.getString("property");//按照哪个字段排序
                dir = jsobj.getString("direction");//排序方向
            }
        }
        Page<AlarmPlan> page = new Page<AlarmPlan>();
        page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
        page.setPageSize(limit);
        Map<String, Object> map = new HashMap<String, Object>();
        page = o_alarmPlanBO.findAlarmPlanBySome(page, query, sortColumn, dir);
        List<AlarmPlan> alarmPlanlist = page.getResult();
        for (AlarmPlan alarmPlan : alarmPlanlist)
        {
            form = new AlarmPlanForm(alarmPlan);
            datas.add(form);
        }
        map.put("totalCount", page.getTotalItems());
        map.put("datas", datas);
        return map;
    }

    /**
     * <pre>
     * 保存告警信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param form
     * @return
     * @since  fhd　Ver 1.1
    */
    @ResponseBody
    @RequestMapping("/kpi/alarm/savealarmplan.f")
    public Map<String, Object> saveAlarmPlan(AlarmPlanForm form, HttpServletRequest request)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        form.setDescs(form.getMemo());
        this.o_alarmPlanBO.saveAlarmPlan(form);
        result.put("success", true);
        return result;
    }

    /**
     * <pre>
     * 修改告警信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param form
     * @return
     * @since  fhd　Ver 1.1
    */
    @ResponseBody
    @RequestMapping("/kpi/alarm/mergealarmplan.f")
    public Map<String, Object> mergeAlarmPlan(AlarmPlanForm form, HttpServletRequest request)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        form.setDescs(form.getMemo());
        this.o_alarmPlanBO.mergeAlarmPlan(form);
        result.put("success", true);
        return result;
    }

    /**
     * <pre>
     * removeAlarmPlan:(根据id删除告警方案)
     * </pre>
     * 
     * @author 陈晓哲
     * @param id 告警ID
     * @return
     * @since  fhd　Ver 1.1
    */
    @ResponseBody
    @RequestMapping("/kpi/alarm/removealarmplan.f")
    public Map<String, Object> removeAlarmPlanById(String ids)
    {
        boolean delflag = true;
        Map<String, Object> result = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(ids))
        {
            String[] idArr = StringUtils.split(ids, ",");
            for (String id : idArr)
            {
                long count = this.o_alarmPlanBO.findSmRelaAlarmCount(id);
                if (count > 0)
                {
                    delflag = false;
                    break;
                }
                else
                {
                    this.o_alarmPlanBO.removeAlarmPlanById(id);
                }
            }
        }
        result.put("success", delflag);
        return result;
    }

    /**
     * <pre>
     * 校验预警信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param validateItem 校验项
     * @param mode 添加或修改标志
     * @param id 告警方案ID
     * @return
     * @since  fhd　Ver 1.1
    */
    @ResponseBody
    @RequestMapping("/kpi/alarm/validate.f")
    public Map<String, Object> validate(String id, String mode,String name, String validateItems)
    {
        boolean successFlag = true;
        Map<String, Object> result = new HashMap<String, Object>();
        if (this.o_alarmPlanBO.findAlarmPlanCountBySome(validateItems, id, mode,name) > 0)
        {
            successFlag = false;
            result.put("error", "nameRepeat");
        }
        result.put("success", successFlag);
        return result;
    }

}
