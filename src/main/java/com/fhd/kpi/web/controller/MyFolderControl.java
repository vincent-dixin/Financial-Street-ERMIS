package com.fhd.kpi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.core.dao.Page;
import com.fhd.kpi.business.KpiBO;
import com.fhd.kpi.entity.Kpi;
import com.fhd.kpi.web.form.KpiForm;

/**
 * 我的文件夹控制类
 * ClassName:MyFolderControl
 * @author   陈晓哲
 * @version  
 * @since    Ver 1.1
 * @Date     2012   2012-12-26       下午12:50:34
 *
 * @see
 */
@Controller
public class MyFolderControl {
    @Autowired
    private KpiBO o_kpiBO;

    /**<pre>
     * 我的文件夹树展现
     * </pre>
     * 
     * @author 陈晓哲
     * @param node 节点信息
     * @param query 查询条件
     * @return
     */
    @ResponseBody
    @RequestMapping("/kpi/myfolder/myfoldertreeloader.f")
    public List<Map<String, Object>> myFolderTreeLoader(String node, String query) {

        List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();//返回前台的nodelist
        Map<String, Object> allKpiNode = new HashMap<String, Object>();
        allKpiNode.put("id", "allkpi");
        allKpiNode.put("text", "我的度量指标");
        allKpiNode.put("type", "myfolder");
        allKpiNode.put("leaf", true);
        allKpiNode.put("iconCls", "icon-ibm-icon-owner");
        nodes.add(allKpiNode);
        return nodes;

    }

    /**
     * <pre>
     * 查询所有指标关联的采集结果
     * </pre>
     * 
     * @author 陈晓哲
     * @param start 分页起始位置
     * @param limit 显示记录数
     * @param query 查询条件
     * @param id 指标ID
     * @param sort 排序字段
     * @return
     * @since  fhd　Ver 1.1
    */
    @ResponseBody
    @RequestMapping("/kpi/myfolder/findallkpirelaresult.f")
    public Map<String, Object> findAllkpiRelaResult(int start, int limit, String query, String sort, String year, String quarter, String month,
            String week, String eType, String isNewValue) {
        String lastflag = isNewValue;
        String frequence = eType;
        String dir = "DESC";
        String sortColumn = "assessmentStatus";//默认排序
        KpiForm form = null;
        Map<String, Object> map = new HashMap<String, Object>();
        List<KpiForm> datas = new ArrayList<KpiForm>();
        Page<Kpi> page = new Page<Kpi>();
        page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
        page.setPageSize(limit);
        if (StringUtils.isNotBlank(sort)) {
            JSONArray jsonArray = JSONArray.fromObject(sort);
            if (jsonArray.size() > 0) {
                JSONObject jsobj = jsonArray.getJSONObject(0);
                sortColumn = jsobj.getString("property");//按照哪个字段排序
                dir = jsobj.getString("direction");//排序方向
            }
        }
        List<Object[]> gatherDatas = null;
        if ("true".equals(lastflag)) {//最新值查询
            gatherDatas = o_kpiBO.findLastGatherResults(map, start, limit, query, sortColumn, dir, false);
        }
        else {//具体频率查询
            gatherDatas = o_kpiBO.findSpecificGatherResults(map, start, limit, query, sortColumn, dir, year, quarter, month, week, frequence, false);
        }
        if (null != gatherDatas) {
            for (Object[] objects : gatherDatas) {
                form = new KpiForm(objects, "allkpi");
                datas.add(form);
            }
        }
        map.put("datas", datas);
        return map;
    }

    /**
     * <pre>
     * 查询我的指标关联的采集结果
     * </pre>
     * 
     * @author 陈晓哲
     * @param start 分页起始位置
     * @param limit 显示记录数
     * @param query 查询条件
     * @param id 指标ID
     * @param sort 排序字段
     * @return
     * @since  fhd　Ver 1.1
    */
    @ResponseBody
    @RequestMapping("/kpi/myfolder/findmykpirelaresult.f")
    public Map<String, Object> findMykpiRelaResult(int start, int limit, String query, String sort, String year, String quarter, String month,
            String week, String eType, String isNewValue) {
        String lastflag = isNewValue;
        String frequence = eType;
        String dir = "DESC";
        String sortColumn = "assessmentStatus";//默认排序
        KpiForm form = null;
        Map<String, Object> map = new HashMap<String, Object>();
        List<KpiForm> datas = new ArrayList<KpiForm>();
        Page<Kpi> page = new Page<Kpi>();
        page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
        page.setPageSize(limit);
        if (StringUtils.isNotBlank(sort)) {
            JSONArray jsonArray = JSONArray.fromObject(sort);
            if (jsonArray.size() > 0) {
                JSONObject jsobj = jsonArray.getJSONObject(0);
                sortColumn = jsobj.getString("property");//按照哪个字段排序
                dir = jsobj.getString("direction");//排序方向
            }
        }
        List<Object[]> gatherDatas = null;
        if ("true".equals(lastflag)) {//最新值查询
            gatherDatas = o_kpiBO.findLastGatherResults(map, start, limit, query, sortColumn, dir, true);
        }
        else {//具体频率查询
            gatherDatas = o_kpiBO.findSpecificGatherResults(map, start, limit, query, sortColumn, dir, year, quarter, month, week, frequence, true);
        }
        if (null != gatherDatas) {
            for (Object[] objects : gatherDatas) {
                form = new KpiForm(objects, "mykpi");
                datas.add(form);
            }
        }
        map.put("datas", datas);
        return map;
    }
}
