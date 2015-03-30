/**
 * CategoryController.java
 * com.fhd.comm.web.controller
 *   ver     date           author
 * ──────────────────────────────────
 *           2012-11-14         陈晓哲
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
 */

package com.fhd.comm.web.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.comm.business.CategoryBO;
import com.fhd.comm.entity.Category;
import com.fhd.comm.entity.CategoryRelaAlarm;
import com.fhd.comm.web.form.CategoryForm;
import com.fhd.comm.web.form.CategoryRelaAlarmForm;
import com.fhd.core.dao.Page;
import com.fhd.core.utils.encode.JsonBinder;
//import com.fhd.entity.risk.RiskEvent;
import com.fhd.kpi.business.KpiBO;
import com.fhd.kpi.business.KpiGatherResultBO;
import com.fhd.kpi.business.RelaAssessResultBO;
import com.fhd.kpi.entity.RelaAssessResult;
import com.fhd.kpi.web.form.KpiForm;
import com.fhd.kpi.web.form.RelaAssessResultForm;
import com.fhd.risk.entity.RiskEvent;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 维度controller
 * 
 * @author 陈晓哲
 * @version
 * @since Ver 1.1
 * @Date 2012-11-14 下午03:28:47
 * 
 * @see
 */
@Controller
public class CategoryControl {
    @Autowired
    private CategoryBO o_categoryBO;

    @Autowired
    private KpiGatherResultBO o_kpiGatherResultBO;

    @Autowired
    private RelaAssessResultBO o_relaAssessResultBO;
    
    @Autowired
    private KpiBO o_kpiBO;
    

    /**
     * <pre>
     * 指标维度树查询
     * </pre>
     * 
     * @author 陈晓哲
     * @param node
     * @param canChecked
     * @param query
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/category/categorytreeloader.f")
    public List<Map<String, Object>> kpiCategoryTreeLoader(String node, Boolean canChecked, String query) {
        return o_categoryBO.kpiCategoryTreeLoader(node, canChecked, query);
    }

    /**
     * <pre>
     * 根据维度ID查找维度信息,form赋值
     * </pre>
     * 
     * @author 陈晓哲
     * @param id
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/category/findcategoryByIdToJson.f")
    public Map<String, Object> findCategoryByIdToJson(String id) throws IllegalAccessException, InvocationTargetException {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(id) && !"undefined".equals(id)) {
            Category category = o_categoryBO.findCategoryById(id);
            if (null != category) {
                // 基本信息赋值
                jsonMap.put("code", category.getCode());
                jsonMap.put("name", category.getName());
                jsonMap.put("desc", category.getDesc());
                jsonMap.put("forecastFormula", category.getForecastFormula());
                jsonMap.put("assessmentFormula", category.getAssessmentFormula());
                if (category.getStatus() != null) {
                    jsonMap.put("statusStr", category.getStatus().getId());
                }
                if (null != category.getDateType()) {
                    jsonMap.put("dataTypeStr", category.getDateType().getId());
                }
                if (null != category.getCreateKpi()) {
                    jsonMap.put("createKpiStr", category.getCreateKpi().getId());
                }
                if(null!=category.getCalc()){
                    jsonMap.put("calcStr", category.getCalc().getId());
                }

                if (StringUtils.isNotBlank(category.getChartType())) {
                    jsonMap.put("chartTypeStr", this.sortChartType(category.getChartType()));
                    jsonMap.put("charttypehidden", this.sortChartType(category.getChartType()));
                }
                // 部门信息赋值
                JSONObject orgEmpObj = this.o_categoryBO.findCategoryRelaOrgEmpBySmToJson(category);
                jsonMap.put("ownDept", orgEmpObj.getString("ownDept"));
                jsonMap.put("targetDept", orgEmpObj.getString("targetDept"));
                dataMap.put("data", jsonMap);
                dataMap.put("success", true);
            }

        }
        return dataMap;
    }

    /**
     * <pre>
     * 查找父维度
     * </pre>
     * 
     * @author 陈晓哲
     * @param id
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/category/findparentbyid.f")
    public Map<String, Object> findParentByid(String id) {
        String parentid = "";
        String parentname = "";
        Map<String, Object> result = new HashMap<String, Object>();
        Category category = o_categoryBO.findCategoryById(id);
        if (null != category) {
            Category pCategory = category.getParent();
            if (null != pCategory) {
                parentid = pCategory.getId();
                parentname = pCategory.getName();
            }
            if (StringUtils.isNotBlank(category.getChartType())) {
                result.put("chartIds", this.sortChartType(category.getChartType()));
            }
            else {
                result.put("chartIds", "");
            }
        }
        result.put("parentid", parentid);
        result.put("parentname", parentname);
        return result;
    }

    /**
     * 图表类型排序.
     * @param chartType
     * @return String
     */
    private String sortChartType(String chartType) {
        String chartTypeStr = "";
        if (StringUtils.isNotBlank(chartType)) {
            String[] chartIdArray = chartType.split(",");
            Arrays.sort(chartIdArray);
            int i = 0;
            for (String chartId : chartIdArray) {
                chartTypeStr += chartId;
                if (i != chartIdArray.length - 1) {
                    chartTypeStr += ",";
                }
                i++;
            }
        }
        return chartTypeStr;
    }

    /**
     * <pre>
     * 查找维度关联预警信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param start
     * @param limit
     * @param query
     * @param id
     * @param sort
     * @return
     * @throws Exception
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/category/findcategoryrelaalarmbysome.f")
    public Map<String, Object> findCategoryRelaAlarmBySome(int start, int limit, String query, String id, String editflag, String sort)
            throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        id = StringUtils.defaultIfEmpty(id, "undefined");
        String dir = "ASC";
        String sortColumn = "id";

        if (StringUtils.isNotBlank(sort)) {
            JSONArray jsonArray = JSONArray.fromObject(sort);
            if (jsonArray.size() > 0) {
                JSONObject jsobj = jsonArray.getJSONObject(0);
                sortColumn = jsobj.getString("property");
                dir = jsobj.getString("direction");
            }
        }
        List<CategoryRelaAlarm> entityList = this.o_categoryBO.findCategoryRelaAlarmBySome(query, id, editflag, sortColumn, dir);
        List<CategoryRelaAlarmForm> datas = new ArrayList<CategoryRelaAlarmForm>();
        for (CategoryRelaAlarm de : entityList) {
            datas.add(new CategoryRelaAlarmForm(de));
        }
        map.put("datas", datas);
        return map;
    }

    /**
     * <pre>
     * 查询指标类型关联的指标信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param start
     *            分页起始位置
     * @param limit
     *            显示记录数
     * @param query
     *            查询条件
     * @param id
     *            记分卡ID
     * @param sort
     *            排序字段
     * @param year
     *            年
     * @param month
     *            月
     * @param quarter
     *            季
     * @param week
     *            周
     * @param eType
     *            采集频率
     * @param isNewValue
     *            是否选择最新值
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/category/findcategoryrelakpiresult.f")
    public Map<String, Object> findCategoryRelaKpiResult(int start, int limit, String query, String sort, String id, String year, String quarter,
            String month, String week, String eType, String isNewValue) {
        id = ",".equals(id) ? "" : id;
        year = ",".equals(year) ? "" : year;
        quarter = ",".equals(quarter) ? "" : quarter;
        month = ",".equals(month) ? "" : month;
        week = ",".equals(week) ? "" : week;
        eType = ",".equals(eType) ? "" : eType;
        isNewValue = ",".equals(isNewValue) ? "" : isNewValue;
        if (null != year && year.contains(",")) {
            year = year.replace(",", "");
        }
        if (null != quarter && quarter.contains(",")) {
            quarter = quarter.replace(",", "");
        }
        if (null != month && month.contains(",")) {
            month = month.replace(",", "");
        }
        if (null != week && week.contains(",")) {
            week = week.replace(",", "");
        }
        if (null != id && id.contains(",")) {
            id = id.replace(",", "");
        }
        if (null != eType && eType.contains(",")) {
            eType = eType.replace(",", "");
        }
        if (null != isNewValue && isNewValue.contains(",")) {
            isNewValue = isNewValue.replace(",", "");
        }
        String lastflag = isNewValue;
        String frequence = eType;
        String dir = "DESC";
        String sortColumn = "assessmentStatus";// 默认排序
        KpiForm form = null;
        Map<String, Object> map = new HashMap<String, Object>();
        List<KpiForm> datas = new ArrayList<KpiForm>();
        List<Object[]> gatherDatas = null;
        if (StringUtils.isNotBlank(sort)) {
            JSONArray jsonArray = JSONArray.fromObject(sort);
            if (jsonArray.size() > 0) {
                JSONObject jsobj = jsonArray.getJSONObject(0);
                sortColumn = jsobj.getString("property");// 按照哪个字段排序
                dir = jsobj.getString("direction");// 排序方向
            }
        }
        if ("true".equals(lastflag)) {
            gatherDatas = o_categoryBO.findLastCategoryRelaKpiResultsBySome(map, start, limit, query, id, sortColumn, dir);

        }
        else {
            gatherDatas = o_categoryBO.findSpecificCategoryRelaKpiResultsBySome(map, start, limit, query, sortColumn, dir, id, year, quarter, month,
                    week, frequence);
        }
        if (null != gatherDatas) {
            for (Object[] objects : gatherDatas) {
                form = new KpiForm(objects, "category");
                datas.add(form);
            }
        }
        map.put("datas", datas);
        return map;
    }

    /**
     * <pre>
     * 查询指标类型关联的指标信息(图表使用)
     * </pre>
     * 
     * @author 陈晓哲
     * @param start
     *            分页起始位置
     * @param limit
     *            显示记录数
     * @param query
     *            查询条件
     * @param id
     *            记分卡ID
     * @param sort
     *            排序字段
     * @param year
     *            年
     * @param month
     *            月
     * @param quarter
     *            季
     * @param week
     *            周
     * @param eType
     *            采集频率
     * @param isNewValue
     *            是否选择最新值
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/category/findcategoryrelakpi.f")
    public Map<String, Object> findCategoryRelaKpiByChart(int start, int limit, String query, String sort, String id, String year, String quarter,
            String month, String week, String eType, String isNewValue, String tableType) {
        id = ",".equals(id) ? "" : id;
        year = ",".equals(year) ? "" : year;
        quarter = ",".equals(quarter) ? "" : quarter;
        month = ",".equals(month) ? "" : month;
        week = ",".equals(week) ? "" : week;
        eType = ",".equals(eType) ? "" : eType;
        isNewValue = ",".equals(isNewValue) ? "" : isNewValue;
        if (null != year && year.contains(",")) {
            year = year.replace(",", "");
        }
        if (null != quarter && quarter.contains(",")) {
            quarter = quarter.replace(",", "");
        }
        if (null != month && month.contains(",")) {
            month = month.replace(",", "");
        }
        if (null != week && week.contains(",")) {
            week = week.replace(",", "");
        }
        if (null != id && id.contains(",")) {
            id = id.replace(",", "");
        }
        if (null != eType && eType.contains(",")) {
            eType = eType.replace(",", "");
        }
        if (null != isNewValue && isNewValue.contains(",")) {
            isNewValue = isNewValue.replace(",", "");
        }
        String lastflag = isNewValue;
        String frequence = eType;
        String dir = "DESC";
        String sortColumn = "assessmentStatus";// 默认排序
        KpiForm form = null;
        Map<String, Object> map = new HashMap<String, Object>();
        List<KpiForm> datas = new ArrayList<KpiForm>();
        List<Object[]> gatherDatas = null;
        if (StringUtils.isNotBlank(sort)) {
            JSONArray jsonArray = JSONArray.fromObject(sort);
            if (jsonArray.size() > 0) {
                JSONObject jsobj = jsonArray.getJSONObject(0);
                sortColumn = jsobj.getString("property");// 按照哪个字段排序
                dir = jsobj.getString("direction");// 排序方向
            }
        }
        if ("true".equals(lastflag)) {
            gatherDatas = o_categoryBO.findLastCategoryRelaKpiResultsByChartSome(map, start, limit, query, id, sortColumn, dir, tableType);

        }
        else {
            gatherDatas = o_categoryBO.findSpecificCategoryRelaKpiResultsChartBySome(map, start, limit, query, sortColumn, dir, id, year, quarter,
                    month, week, frequence, tableType);
        }
        if (null != gatherDatas) {
            for (Object[] objects : gatherDatas) {
                form = new KpiForm(objects, "chart");
                datas.add(form);
            }
        }
        map.put("datas", datas);
        return map;
    }

    /**
     * <pre>
     * 生成维度编码
     * </pre>
     * 
     * @author 陈晓哲
     * @param param
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/category/findcodebyparentid.f")
    public Map<String, Object> findCodeByParentId(String param) {
        String code = "";
        Map<String, Object> result = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(param)) {
            JSONObject jsobj = JSONObject.fromObject(param);
            String id = jsobj.getString("id");
            String parentid = jsobj.getString("parentid");
            code = o_categoryBO.findCodeByParentId(parentid, id);
        }
        result.put("code", code);
        result.put("success", true);
        return result;
    }

    /**
     * <pre>
     * 保存指标维度
     * </pre>
     * 
     * @author 陈晓哲
     * @return
     * @throws ParseException 
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/category/mergecategory.f")
    public Map<String, Object> mergeCategory(CategoryForm form, HttpServletRequest request) throws ParseException {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject param = JSONObject.fromObject(request.getParameter("param"));
        form.setAssessmentFormula(param.getString("assessmentFormula"));
        form.setForecastFormula(param.getString("forecastFormula"));
        form.setId(param.getString("id"));
        String id = o_categoryBO.mergeCategory(form);
        result.put("id", id);
        result.put("success", true);
        return result;
    }

    /**
     * <pre>
     * 保存维度和预警关联信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param modifiedRecord
     * @param id
     * @return
     * @throws ParseException
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/category/mergecategoryrelaalarm.f")
    public Map<String, Object> mergeCategoryRelaAlarm(String modifiedRecord, String id) throws ParseException {
        Map<String, Object> result = new HashMap<String, Object>();
        this.o_categoryBO.mergeCategoryRelaAlarm(modifiedRecord, id);
        result.put("success", true);
        return result;
    }

    /**
     * <pre>
     * 保存维度引用的指标信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param param
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/category/mergecategoryrelakpi.f")
    public Map<String, Object> mergeCategoryRelaKpi(String param) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(param)) {
            JSONObject jsobj = JSONObject.fromObject(param);
            o_categoryBO.mergeCategoryRelaKpi(jsobj);
        }
        result.put("success", true);
        return result;
    }

    /**
     * <pre>
     * 删除维度信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param id
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/category/removecategory.f")
    public Map<String, Object> removeCategory(String id) {
        Map<String, Object> result = new HashMap<String, Object>();
        String ret = this.o_categoryBO.removeCategory(id);
        result.put("result", ret);
        return result;
    }

    /**
     * <pre>
     * 校验维度信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param validateItem
     * @param id
     * @return
     * @throws Exception
     * @since fhd　Ver 1.1
     */
    @RequestMapping("/kpi/category/validate.f")
    public void validate(HttpServletResponse response, String validateItem, String id) throws Exception {
        boolean successFlag = true;
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject jsobj = JSONObject.fromObject(validateItem);
        // 分类不用验证名称 -- 胡迪新
        /*
         * if (o_categoryBo.findCategoryCountByName(jsobj.getString("name"), id)
         * > 0) { successFlag = false; result.put("error", "nameRepeat"); }
         */
        if (o_categoryBO.findCategoryCountByCode(jsobj.getString("code"), id) > 0) {
            successFlag = false;
            result.put("error", "codeRepeat");
        }
        result.put("success", successFlag);
        response.getWriter().write(JsonBinder.buildNonDefaultBinder().toJson(result));
    }

    /**
     * <pre>
     * 根据记分卡ID查询图表类型
     * </pre>
     * 
     * @param response
     *            响应对象
     * @param id
     *            记分卡ID
     * @throws IOException
     */
    @RequestMapping("/kpi/category/findcharttypebyid.f")
    public void findChartTypeById(HttpServletResponse response, String id) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        Category category = o_categoryBO.findCategoryById(id);
        String chartType = "";
        if (null != category && StringUtils.isNotBlank(category.getChartType())) {
            String[] chartIdArray = category.getChartType().split(",");
            Arrays.sort(chartIdArray);
            int i = 0;
            for (String chartId : chartIdArray) {
                chartType += chartId;
                if (i != chartIdArray.length - 1) {
                    chartType += ",";
                }
                i++;
            }
        }
        result.put("chartType", chartType);
        result.put("success", true);
        response.getWriter().write(JsonBinder.buildNonDefaultBinder().toJson(result));
    }

    /**
     * <pre>
     * 根据记分卡ID获取父记分卡数据类型
     * </pre>
     * 
     * @author 陈晓哲
     * @param parentid
     *            父记分卡ID
     * @return
     * @since fhd　Ver 1.1
     */
    @RequestMapping("/kpi/category/findparentcategorydatetypebyid.f")
    public void findParentCategoryDateTypeById(HttpServletResponse response, String parentid) throws IOException {
        boolean successFlag = true;
        Map<String, Object> result = new HashMap<String, Object>();
        Category parentCategory = o_categoryBO.findCategoryById(parentid);
        if (null != parentCategory) {
            DictEntry dictDateType = parentCategory.getDateType();
            if (null != dictDateType) {
                result.put("dataTypeStr", dictDateType.getId());
            }
            result.put("success", successFlag);
        }

        response.getWriter().write(JsonBinder.buildNonDefaultBinder().toJson(result));
    }

    /**
     * 查找记分卡关联的指标的历史数据.
     * 
     * @author 吴德福
     * @param id
     *            记分卡id
     * @param query
     *            查询条件
     * @param year
     *            年
     * @return Map<String, Object>
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/category/findcategoryrelakpihistorydatas.f")
    public Map<String, Object> findcategoryrelakpihistorydatas(String id, String query, String year, String quarter, String month, String week,
            String eType, String isNewValue, String sort) {
        // 返回结果集
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(id) && !"undefined".equals(id)) {
            String dir = "DESC";
            String sortColumn = "assessmentStatus";// 默认排序
            if (StringUtils.isNotBlank(sort)) {
                JSONArray jsonArray = JSONArray.fromObject(sort);
                if (jsonArray.size() > 0) {
                    JSONObject jsobj = jsonArray.getJSONObject(0);
                    sortColumn = jsobj.getString("property");// 按照哪个字段排序
                    dir = jsobj.getString("direction");// 排序方向
                }
            }
            year = ",".equals(year) ? "" : year;
            quarter = ",".equals(quarter) ? "" : quarter;
            month = ",".equals(month) ? "" : month;
            week = ",".equals(week) ? "" : week;
            eType = ",".equals(eType) ? "" : eType;
            isNewValue = ",".equals(isNewValue) ? "" : isNewValue;

            if (null != year && year.contains(",")) {
                year = year.replace(",", "");
            }
            if (null != quarter && quarter.contains(",")) {
                quarter = quarter.replace(",", "");
            }
            if (null != month && month.contains(",")) {
                month = month.replace(",", "");
            }
            if (null != week && week.contains(",")) {
                week = week.replace(",", "");
            }
            if (null != eType && eType.contains(",")) {
                eType = eType.replace(",", "");
            }
            if (null != isNewValue && isNewValue.contains(",")) {
                isNewValue = isNewValue.replace(",", "");
            }

            String lastflag = isNewValue;
            if ("true".equals(lastflag)) {
                year = "";
            }
            Map<String, List<Object[]>> kpiGatherResultMap = o_kpiGatherResultBO.findKpiGatherResultsByCategoryId(id, query, year);
            // 定义封装参数
            List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
            Map<String, Object> row = null;
            for (String kpiName : kpiGatherResultMap.keySet()) {

                row = new HashMap<String, Object>();

                row.put("id", (int) (Math.random() * 10000));
                // row.put("name", kpiName);
                List<Object[]> objects = kpiGatherResultMap.get(kpiName);
                if (null != objects && objects.size() > 0) {
                    if (null != objects.get(0)[1]) {
                        String assessmentStatus = "";
                        String directionstr = "";
                        for (Object[] object : objects) {
                            row.put("kid", object[0]);
                            row.put("name", object[1]);
                            if ("true".equals(lastflag)) {
                                if (null != object[3] && null != object[4]) {
                                    if (String.valueOf(object[3]).equals(String.valueOf(object[4]))) {
                                        assessmentStatus = String.valueOf(object[8]);
                                        directionstr = String.valueOf(object[9]);
                                        break;
                                    }
                                }
                            }
                            else {
                                if (StringUtils.isNotBlank(year) && year.equals(object[4])) {
                                    assessmentStatus = String.valueOf(object[8]);
                                    directionstr = String.valueOf(object[9]);
                                    break;
                                }
                                else if (StringUtils.isNotBlank(quarter) && quarter.equals(object[4])) {
                                    assessmentStatus = String.valueOf(object[8]);
                                    directionstr = String.valueOf(object[9]);
                                    break;
                                }
                                else if (StringUtils.isNotBlank(month) && month.equals(object[4])) {
                                    assessmentStatus = String.valueOf(object[8]);
                                    directionstr = String.valueOf(object[9]);
                                    break;
                                }
                                //德福 - 暂时不需要周的判断
                                /*else if (week.equals(object[3])) {
                                	assessmentStatus = String.valueOf(object[8]);
                                	directionstr = String.valueOf(object[9]);
                                	break;
                                }*/
                            }
                        }
                        row.put("assessmentStatus", assessmentStatus);
                        row.put("directionstr", directionstr);
                        if ("0frequecy_month".equals(objects.get(0)[2])) {
                            // 月
                            row.put("januaryValue", null != objects.get(0)[7] ? objects.get(0)[7] : "");
                            row.put("februaryValue", null != objects.get(1)[7] ? objects.get(1)[7] : "");
                            row.put("marchValue", null != objects.get(2)[7] ? objects.get(2)[7] : "");
                            row.put("aprilValue", null != objects.get(3)[7] ? objects.get(3)[7] : "");
                            row.put("mayValue", null != objects.get(4)[7] ? objects.get(4)[7] : "");
                            row.put("juneValue", null != objects.get(5)[7] ? objects.get(5)[7] : "");
                            row.put("julyValue", null != objects.get(6)[7] ? objects.get(6)[7] : "");
                            row.put("aguestValue", null != objects.get(7)[7] ? objects.get(7)[7] : "");
                            row.put("septemberValue", null != objects.get(8)[7] ? objects.get(8)[7] : "");
                            row.put("octoberValue", null != objects.get(9)[7] ? objects.get(9)[7] : "");
                            row.put("novemberValue", null != objects.get(10)[7] ? objects.get(10)[7] : "");
                            row.put("decemberValue", null != objects.get(11)[7] ? objects.get(11)[7] : "");
                        }
                        else if ("0frequecy_quarter".equals(objects.get(0)[2])) {
                            // 季
                            row.put("januaryValue", "");
                            row.put("februaryValue", "");
                            row.put("marchValue", null != objects.get(0)[7] ? objects.get(0)[7] : "");
                            row.put("aprilValue", "");
                            row.put("mayValue", "");
                            row.put("juneValue", null != objects.get(1)[7] ? objects.get(1)[7] : "");
                            row.put("julyValue", "");
                            row.put("aguestValue", "");
                            row.put("septemberValue", null != objects.get(2)[7] ? objects.get(2)[7] : "");
                            row.put("octoberValue", "");
                            row.put("novemberValue", "");
                            row.put("decemberValue", null != objects.get(3)[7] ? objects.get(3)[7] : "");
                        }
                        else if ("0frequecy_halfyear".equals(objects.get(0)[2])) {
                            // 半年
                            row.put("januaryValue", "");
                            row.put("februaryValue", "");
                            row.put("marchValue", "");
                            row.put("aprilValue", "");
                            row.put("mayValue", "");
                            row.put("juneValue", null != objects.get(0)[7] ? objects.get(0)[7] : "");
                            row.put("julyValue", "");
                            row.put("aguestValue", "");
                            row.put("septemberValue", "");
                            row.put("octoberValue", "");
                            row.put("novemberValue", "");
                            row.put("decemberValue", null != objects.get(1)[7] ? objects.get(1)[7] : "");
                        }
                        else if ("0frequecy_year".equals(objects.get(0)[2])) {
                            // 年
                            row.put("januaryValue", "");
                            row.put("februaryValue", "");
                            row.put("marchValue", "");
                            row.put("aprilValue", "");
                            row.put("mayValue", "");
                            row.put("juneValue", "");
                            row.put("julyValue", "");
                            row.put("aguestValue", "");
                            row.put("septemberValue", "");
                            row.put("octoberValue", "");
                            row.put("novemberValue", "");
                            row.put("decemberValue", null != objects.get(0)[7] ? objects.get(0)[7] : "");
                        }
                        else {
                            row.put("assessmentStatus", "");
                            row.put("directionstr", "");
                            row.put("januaryValue", "");
                            row.put("februaryValue", "");
                            row.put("marchValue", "");
                            row.put("aprilValue", "");
                            row.put("mayValue", "");
                            row.put("juneValue", "");
                            row.put("julyValue", "");
                            row.put("aguestValue", "");
                            row.put("septemberValue", "");
                            row.put("octoberValue", "");
                            row.put("novemberValue", "");
                            row.put("decemberValue", "");
                        }
                    }
                    else {
                        row.put("assessmentStatus", "");
                        row.put("directionstr", "");
                        row.put("januaryValue", "");
                        row.put("februaryValue", "");
                        row.put("marchValue", "");
                        row.put("aprilValue", "");
                        row.put("mayValue", "");
                        row.put("juneValue", "");
                        row.put("julyValue", "");
                        row.put("aguestValue", "");
                        row.put("septemberValue", "");
                        row.put("octoberValue", "");
                        row.put("novemberValue", "");
                        row.put("decemberValue", "");
                    }
                }
                else {
                    row.put("kid", "");
                    row.put("name", kpiName);
                    row.put("assessmentStatus", "");
                    row.put("directionstr", "");
                    row.put("januaryValue", "");
                    row.put("februaryValue", "");
                    row.put("marchValue", "");
                    row.put("aprilValue", "");
                    row.put("mayValue", "");
                    row.put("juneValue", "");
                    row.put("julyValue", "");
                    row.put("aguestValue", "");
                    row.put("septemberValue", "");
                    row.put("octoberValue", "");
                    row.put("novemberValue", "");
                    row.put("decemberValue", "");
                }
                datas.add(row);
            }
            Collections.sort(datas, new CategoryTrendComparator(sortColumn, dir));
            map.put("datas", datas);
            map.put("totalCount", kpiGatherResultMap.keySet().size());
        }
        else {
            map.put("totalCount", "0");
            map.put("datas", new Object[0]);
        }

        return map;
    }

    /**
     * 查找记分卡关联的指标的指标数据.
     * 
     * @author 吴德福
     * @param id
     *            记分卡id
     * @param query
     *            查询条件
     * @param year
     *            年
     * @return Map<String, Object>
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/category/findcategoryrelakpidata.f")
    public Map<String, Object> findcategoryrelakpidata(int start, int limit, String query, String sort, String id, String year, String quarter,
            String month, String week, String eType, String isNewValue) {
        String dir = "DESC";
        String sortColumn = "assessmentStatus";// 默认排序
        if (StringUtils.isNotBlank(sort)) {
            JSONArray jsonArray = JSONArray.fromObject(sort);
            if (jsonArray.size() > 0) {
                JSONObject jsobj = jsonArray.getJSONObject(0);
                sortColumn = jsobj.getString("property");// 按照哪个字段排序
                dir = jsobj.getString("direction");// 排序方向
            }
        }
        // 返回结果集
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(id) && !"undefined".equals(id)) {
            year = ",".equals(year) ? "" : year;
            quarter = ",".equals(quarter) ? "" : quarter;
            month = ",".equals(month) ? "" : month;
            week = ",".equals(week) ? "" : week;
            id = ",".equals(id) ? "" : id;
            eType = ",".equals(eType) ? "" : eType;
            isNewValue = ",".equals(isNewValue) ? "" : isNewValue;

            if (null != year && year.contains(",")) {
                year = year.replace(",", "");
            }
            if (null != quarter && quarter.contains(",")) {
                quarter = quarter.replace(",", "");
            }
            if (null != month && month.contains(",")) {
                month = month.replace(",", "");
            }
            if (null != week && week.contains(",")) {
                week = week.replace(",", "");
            }
            if (null != eType && eType.contains(",")) {
                eType = eType.replace(",", "");
            }
            if (null != isNewValue && isNewValue.contains(",")) {
                isNewValue = isNewValue.replace(",", "");
            }

            String lastflag = isNewValue;
            String frequence = eType;

            // 定义封装参数
            List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();

            List<Object[]> results = null;
            if ("true".equals(lastflag)) {
                results = o_categoryBO.findLastCategoryRelaKpiResultsBySome(map, start, limit, query, id, sortColumn, dir);

            }
            else {
                results = o_categoryBO.findSpecificCategoryRelaKpiResultsBySome(map, start, limit, query, sortColumn, dir, id, year, quarter, month,
                        week, frequence);
            }
            Map<String, Object> row = null;
            if (null != results && results.size() > 0) {
                for (Object[] objects : results) {
                    row = new HashMap<String, Object>();

                    row.put("id", (int) (Math.random() * 10000));
                    row.put("kid", objects[0]);
                    row.put("name", objects[1]);
                    row.put("assessmentStatus", objects[6]);
                    row.put("directionstr", objects[7]);
                    row.put("finishValue", objects[5]);

                    datas.add(row);
                }
            }

            map.put("datas", datas);
            map.put("totalCount", results.size());
        }
        else {
            map.put("totalCount", "0");
            map.put("datas", new Object[0]);
        }

        return map;
    }

    /**查询记分卡或目标的分值集合
     * @param start
     * @param limit
     * @param query
     * @param sort
     * @param objectId记分卡或战略目标ID
     * @param type sc:记分卡 str:战略目标
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/category/findrelaassessresultsbysome.f")
    public Map<String, Object> findRelaAssessResultsBySome(int start, int limit, String query, String sort, String objectId, String type)
            throws Exception {
        String dir = "DESC";
        String sortColumn = "dateRange";
        RelaAssessResultForm form = null;
        List<RelaAssessResultForm> datas = new ArrayList<RelaAssessResultForm>();
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(objectId)) {
            if (StringUtils.isNotBlank(sort)) {
                JSONArray jsonArray = JSONArray.fromObject(sort);
                if (jsonArray.size() > 0) {
                    JSONObject jsobj = jsonArray.getJSONObject(0);
                    sortColumn = jsobj.getString("property");//按照哪个字段排序
                    dir = jsobj.getString("direction");//排序方向
                }
            }
            Page<RelaAssessResult> page = new Page<RelaAssessResult>();
            page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
            page.setPageSize(limit);

            page = o_relaAssessResultBO.findRelaAssessResultsBySome(page, objectId, type, sortColumn, dir);
            List<RelaAssessResult> assessResults = page.getResult();
            for (RelaAssessResult relaAssessResult : assessResults) {
                form = new RelaAssessResultForm(relaAssessResult);
                datas.add(form);
            }
            map.put("totalCount", page.getTotalItems());
            map.put("datas", datas);
        }
        return map;
    }
    
    /**指标关联的风险事件
     * @param start
     * @param limit
     * @param query
     * @param sort
     * @param kpiId
     * @return
     */
    @ResponseBody
    @RequestMapping("/category/findriskevent.f")
    public Map<String, Object> findRiskEvent(int start, int limit, String query, String sort, String kpiId){
        String dir = "DESC";
        String sortColumn = "";
        if (StringUtils.isNotBlank(sort)) {
            JSONArray jsonArray = JSONArray.fromObject(sort);
            if (jsonArray.size() > 0) {
                JSONObject jsobj = jsonArray.getJSONObject(0);
                sortColumn = jsobj.getString("property");//按照哪个字段排序
                dir = jsobj.getString("direction");//排序方向
            }
        }
        Page<RiskEvent> page = new Page<RiskEvent>();
        page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
        page.setPageSize(limit);
        Map<String,Object> dataMap = o_categoryBO.findRiskEvent(kpiId,page,sortColumn,dir,query);
        return dataMap;
    }
    
    @ResponseBody
    @RequestMapping("/category/saveriskevent.f")
    public void saveRiskEvent(HttpServletResponse response,String records,String kpiId) throws ParseException, IOException{
        o_categoryBO.saveRiskEvent(records);
        response.getWriter().write(JsonBinder.buildNonDefaultBinder().toJson(true));
    }
    
    @ResponseBody
    @RequestMapping("/category/removeriskevent.f")
    public void removeRiskEvent(HttpServletResponse response,String records,String kpiId) throws ParseException, IOException{
        o_categoryBO.removeRiskEvent(records);
        response.getWriter().write(JsonBinder.buildNonDefaultBinder().toJson(true));
    }
    
    
    

    /**修改记分卡或战略目标的评分结果
     * @param modifiedRecord 需要修改的评分结果记录
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/category/modifiedrelaassessresult.f")
    public void mergedRelaAssessResults(String modifiedRecord, HttpServletResponse response) throws Exception {
        o_relaAssessResultBO.mergedRelaAssessResults(modifiedRecord);
        response.getWriter().write(JsonBinder.buildNonDefaultBinder().toJson(true));
    }
}
