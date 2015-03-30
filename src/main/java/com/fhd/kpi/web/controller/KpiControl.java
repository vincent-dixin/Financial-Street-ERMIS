package com.fhd.kpi.web.controller;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.impl.calendar.AnnualCalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.bpm.business.JBPMBO;
import com.fhd.bpm.jbpm.JBPMOperate;
import com.fhd.comm.business.CategoryBO;
import com.fhd.comm.business.TimePeriodBO;
import com.fhd.comm.business.formula.FormulaCalculateBO;
import com.fhd.comm.entity.AlarmPlan;
import com.fhd.comm.entity.TimePeriod;
import com.fhd.core.dao.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.jrj.business.JrjBO;
import com.fhd.kpi.business.KpiBO;
import com.fhd.kpi.business.KpiGatherResultBO;
import com.fhd.kpi.business.KpiTreeBO;
import com.fhd.kpi.business.StrategyMapBO;
import com.fhd.kpi.business.dynamictable.MSColumnLine3D;
import com.fhd.kpi.business.dynamictable.TableHalfYearBO;
import com.fhd.kpi.business.dynamictable.TableMonthBO;
import com.fhd.kpi.business.dynamictable.TableQuarterBO;
import com.fhd.kpi.business.dynamictable.TableWeekBO;
import com.fhd.kpi.business.dynamictable.TableYearBO;
import com.fhd.kpi.entity.Kpi;
import com.fhd.kpi.entity.KpiGatherResult;
import com.fhd.kpi.entity.KpiRelaAlarm;
import com.fhd.kpi.entity.KpiRelaDim;
import com.fhd.kpi.entity.KpiRelaOrgEmp;
import com.fhd.kpi.web.form.KpiForm;
import com.fhd.kpi.web.form.KpiRelaAlarmForm;
import com.fhd.sys.business.dic.DictBO;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * KPI_指标Controller ClassName:KpiController
 * 
 * @author 张帅
 * @version
 * @since Ver 1.1
 * @Date 2012 2012-9-17 下午13:11:34
 * 
 * @see
 */

@Controller
@SuppressWarnings("unchecked")
public class KpiControl {

    @Autowired
    private DictBO o_dictEntryBO;

    @Autowired
    private KpiTreeBO o_kpiTreeBO;

    @Autowired
    private KpiBO o_kpiBO;

    @Autowired
    private TableMonthBO o_tableMonthBO;

    @Autowired
    private TableWeekBO o_tableWeekBO;

    @Autowired
    private TableHalfYearBO o_tableHalfYearBO;

    @Autowired
    private TableQuarterBO o_tableQuarterBO;

    @Autowired
    private TableYearBO o_tableYearBO;

    @Autowired
    private CategoryBO o_categoryBO;

    @Autowired
    private StrategyMapBO o_strategyMapBO;

    @Autowired
    private KpiGatherResultBO o_kpiGatherResultBO;

    @Autowired
    private JBPMOperate o_jbpmOperate;

    @Autowired
    private JBPMBO o_jbpmBO;

    @Autowired
    private TimePeriodBO o_timePeriodBO;

    @Autowired
    private FormulaCalculateBO o_formulaCalculateBO;
    
    @Autowired
    private JrjBO o_jrjBO;

    /**
     * 
     * 根据当前登陆人所在公司id查询指标id、目标id、机构id
     * 
     * @author 张帅
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/KpiTree/findrootbycompany")
    public List<Map<String, Object>> findRootByCompanyId(String node, Boolean canChecked, String query) {
        return o_kpiTreeBO.findRootByCompanyId();
    }

    /**
     * 根据id查询指标
     * 
     * @author 张帅
     * @param id
     * @return
     * @since fhd　Ver 1.1
     */

    @ResponseBody
    @RequestMapping("/kpi/Kpi/findKpiByid")
    public List<Map<String, Object>> findKpiByid(String[] ids) {
        return o_kpiBO.findKpiByid(ids);
    }

    /**
     * <pre>
     * 根据指标ID查找指标对象
     * </pre>
     * 
     * @author 陈晓哲
     * @param id
     *            指标Id
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/Kpi/findKpiByIdToJson.f")
    public Map<String, Object> findKpiByIdToJson(String id) throws IllegalAccessException, InvocationTargetException {
        DictEntry entry = null;
        JSONArray otherdimArr = new JSONArray();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(id) && !"undefined".equals(id)) {
            Kpi kpi = o_kpiBO.findKpiById(id);
            // 基本信息赋值
            if (null != kpi.getStartDate()) {
                jsonMap.put("startDateStr", sf.format(kpi.getStartDate()));
            }
            jsonMap.put("code", kpi.getCode());
            jsonMap.put("name", kpi.getName());
            jsonMap.put("desc", kpi.getDesc());
            jsonMap.put("targetValueAlias", kpi.getTargetValueAlias());
            jsonMap.put("resultValueAlias", kpi.getResultValueAlias());
            // 默认名称
            if (null != kpi.getIsNameDefault()) {
                jsonMap.put("namedefault", kpi.getIsNameDefault() ? "0yn_y" : "0yn_n");
            }
            if (null != kpi.getIsInherit()) {
                jsonMap.put("isInheritStr", kpi.getIsInherit() ? "0yn_y" : "0yn_n");
            }

            if (null != kpi.getIsMonitor()) {
                jsonMap.put("monitorStr", kpi.getIsMonitor() ? "0yn_y" : "0yn_n");
            }
            if (null != kpi.getUnits()) {
                jsonMap.put("unitsStr", kpi.getUnits().getId());
            }
            if (null != kpi.getStatus()) {
                jsonMap.put("statusStr", kpi.getStatus().getId());
            }
            if (null != kpi.getType()) {
                jsonMap.put("typeStr", kpi.getType().getId());
            }
            if (null != kpi.getDataType()) {
                jsonMap.put("dataTypeStr", kpi.getDataType().getId());
            }
            if (null != kpi.getKpiType()) {
                jsonMap.put("kpiTypeStr", kpi.getKpiType().getId());
            }
            if (null != kpi.getAlarmMeasure()) {
                jsonMap.put("alarmMeasureStr", kpi.getAlarmMeasure().getId());
            }
            if (null != kpi.getRelativeTo()) {
                jsonMap.put("relativeToStr", kpi.getRelativeTo().getId());
            }
            if (null != kpi.getAlarmBasis()) {
                jsonMap.put("alarmBasisStr", kpi.getAlarmBasis().getId());
            }
            if (null != kpi.getGatherFrequence()) {
                jsonMap.put("gatherFrequenceStr", kpi.getGatherFrequence().getId());
            }
            jsonMap.put("targetSetInterval", kpi.getTargetSetInterval());
            jsonMap.put("resultCollectInterval", kpi.getResultCollectInterval());
            jsonMap.put("targetFormula", kpi.getTargetFormula());
            jsonMap.put("resultFormula", kpi.getResultFormula());
            jsonMap.put("assessmentFormula", kpi.getAssessmentFormula());
            jsonMap.put("relationFormula", kpi.getRelationFormula());
            jsonMap.put("forecastFormula", kpi.getForecastFormula());
            jsonMap.put("shortName", kpi.getShortName());
            jsonMap.put("sort", kpi.getSort());

            // 维度信息赋值
            Set<KpiRelaDim> kpiRelaDimSet = kpi.getKpiRelaDims();
            for (KpiRelaDim kpiRelaDim : kpiRelaDimSet) {
                entry = kpiRelaDim.getSmDim();
                if (null != entry) {
                    if (Contents.MAIN.equals(kpiRelaDim.getType())) {
                        jsonMap.put("mainDim", entry.getId());
                    }
                    else {
                        otherdimArr.add(entry.getId());
                    }
                }
            }
            jsonMap.put("otherDimArray", otherdimArr.toString());
            // 部门信息赋值
            JSONObject orgEmpObj = this.o_kpiBO.findKpiRelaOrgEmpBySmToJson(kpi);
            jsonMap.put("ownDept", orgEmpObj.getString("ownDept"));
            jsonMap.put("reportDept", orgEmpObj.getString("reportDept"));
            jsonMap.put("viewDept", orgEmpObj.getString("viewDept"));
            jsonMap.put("gatherDept", orgEmpObj.getString("gatherDept"));
            jsonMap.put("targetDept", orgEmpObj.getString("targetDept"));
            if (null != kpi.getParent()) {
                jsonMap.put("parentKpiId", kpi.getParent().getId());
            }
            // 查询指标所属的类型信息
            Kpi belongKpi = kpi.getBelongKpiCategory();
            if (null != belongKpi) {
                jsonMap.put("kpitypeid", belongKpi.getId());
                jsonMap.put("kpitypename", belongKpi.getName());
            }
            dataMap.put("data", jsonMap);
            dataMap.put("success", true);
        }

        return dataMap;
    }

    /**
     * <pre>
     * 查找指标采集计算信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param id指标ID
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/Kpi/findkpitypecalculatetojson.f")
    public Map<String, Object> findKpiTypeCalculateToJson(String id) {
        DictEntry dict = null;
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, Object> othervalue = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(id) && !"undefined".equals(id)) {
            Kpi kpi = o_kpiBO.findKpiById(id);
            dict = kpi.getResultSumMeasure();
            if (null != dict) {
                jsonMap.put("resultSumMeasureStr", dict.getId());
            }
            dict = kpi.getTargetSumMeasure();
            if (null != dict) {
                jsonMap.put("targetSumMeasureStr", dict.getId());
            }
            dict = kpi.getAssessmentSumMeasure();
            if (null != dict) {
                jsonMap.put("assessmentSumMeasureStr", dict.getId());
            }
            String isResultFormula = kpi.getIsResultFormula();
            String resultFormula = kpi.getResultFormula();
            if (StringUtils.isNotBlank(isResultFormula)) {
                jsonMap.put("isResultFormula", isResultFormula);
            }
            if (null != resultFormula && StringUtils.isNotBlank(resultFormula)) {
                jsonMap.put("resultFormula", resultFormula);
            }
            String isTargetFormula = kpi.getIsTargetFormula();
            if (StringUtils.isNotBlank(isTargetFormula)) {
                jsonMap.put("isTargetFormula", isTargetFormula);
            }
            String targetFormula = kpi.getTargetFormula();
            if (null != targetFormula && StringUtils.isNotBlank(targetFormula)) {
                jsonMap.put("targetFormula", targetFormula);
            }
            String isAssessmentFormula = kpi.getIsAssessmentFormula();
            if (StringUtils.isNotBlank(isAssessmentFormula)) {
                jsonMap.put("isAssessmentFormula", isAssessmentFormula);
            }
            String assessmentFormula = kpi.getAssessmentFormula();
            if (null != assessmentFormula && StringUtils.isNotBlank(assessmentFormula)) {
                jsonMap.put("assessmentFormula", assessmentFormula);
            }
            /*String alarmformula = kpi.getForecastFormula();
            if (null != alarmformula
                    && StringUtils.isNotBlank(alarmformula)) {
                jsonMap.put("forecastFormula", alarmformula);
            }*/
            if (null != kpi.getScale()) {
                jsonMap.put("scale", kpi.getScale());
            }
            if (null != kpi.getResultCollectInterval()) {
                jsonMap.put("resultCollectIntervalStr", kpi.getResultCollectInterval());
            }
            if (null != kpi.getTargetSetInterval()) {
                jsonMap.put("targetSetIntervalStr", kpi.getTargetSetInterval());
            }
            if (null != kpi.getRelativeTo()) {
                jsonMap.put("relativeToStr", kpi.getRelativeTo().getId());
            }
            if(null!=kpi.getCalc()){
                jsonMap.put("calcStr", kpi.getCalc().getId());
            }
            String[] values = null;

            String gatherDayFormulr = kpi.getGatherDayFormulrShow();
            String targetSetDayFormular = kpi.getTargetSetDayFormularShow();
            String gatherReportDayFormulr = kpi.getGatherReportDayFormulrShow();
            String targetSetReportDayFormulr = kpi.getTargetReportDayFormulrShow();

            String gatherDayCron = StringUtils.defaultIfEmpty(kpi.getGatherDayFormulr(), "");
            String targetDayCron = StringUtils.defaultIfEmpty(kpi.getTargetSetDayFormular(), "");
            othervalue.put("gatherDayCron", gatherDayCron);
            othervalue.put("targetDayCron", targetDayCron);

            if (StringUtils.isNotBlank(gatherDayFormulr)) {
                values = StringUtils.split(gatherDayFormulr, "@");
                if (null != values && values.length > 0) {
                    jsonMap.put("resultgatherfrequence", values[0]);
                }
                dict = kpi.getGatherFrequence();
                if (null != dict) {
                    othervalue.put("resultgatherfrequenceDictType", dict.getId());
                }
                if (null != values && values.length > 1) {
                    othervalue.put("resultgatherfrequenceRule", values[1]);
                }
            }
            if (StringUtils.isNotBlank(targetSetDayFormular)) {
                values = StringUtils.split(targetSetDayFormular, "@");
                if (null != values && values.length > 0) {
                    jsonMap.put("targetSetFrequence", values[0]);
                    othervalue.put("targetSetFrequence", values[0]);
                }
                dict = kpi.getTargetSetFrequence();
                if (null != dict) {
                    othervalue.put("targetSetFrequenceDictType", dict.getId());
                }
                if (null != values && values.length > 1) {
                    othervalue.put("targetSetFrequenceRule", values[1]);
                }
            }
            if (StringUtils.isNotBlank(gatherReportDayFormulr)) {
                values = StringUtils.split(gatherReportDayFormulr, "@");
                if (null != values && values.length > 0) {
                    jsonMap.put("reportFrequence", values[0]);
                    othervalue.put("reportFrequence", values[0]);
                }
                dict = kpi.getReportFrequence();
                if (null != dict) {
                    othervalue.put("reportFrequenceDictType", dict.getId());
                }
                if (null != values && values.length > 1) {
                    othervalue.put("reportFrequenceRule", values[1]);
                }
            }
            if (StringUtils.isNotBlank(targetSetReportDayFormulr)) {
                values = StringUtils.split(targetSetReportDayFormulr, "@");
                if (null != values && values.length > 0) {
                    jsonMap.put("targetSetReportFrequence", values[0]);
                    othervalue.put("targetSetReportFrequence", values[0]);
                }
                dict = kpi.getTargetSetReportFrequence();
                if (null != dict) {
                    othervalue.put("targetSetReportFrequenceDictType", dict.getId());
                }
                if (null != values && values.length > 1) {
                    othervalue.put("targetSetReportFrequenceRule", values[1]);
                }

            }
            jsonMap.put("modelValue", kpi.getModelValue());
            jsonMap.put("maxValue", kpi.getMaxValue());
            jsonMap.put("minValue", kpi.getMinValue());
        }
        dataMap.put("data", jsonMap);
        dataMap.put("othervalue", othervalue);
        dataMap.put("success", true);
        return dataMap;
    }

    /**
     * <pre>
     * 查找指标关联的预警信息
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
     *            指标ID
     * @param sort
     *            排序字段
     * @return
     * @throws Exception
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpi/findkpirelaalarmbysome.f")
    public Map<String, Object> findKpiRelaAlarmBySome(int start, int limit, String query, String id, String editflag, String sort) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        id = StringUtils.defaultIfEmpty(id, "");
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
        List<KpiRelaAlarm> entityList = this.o_kpiBO.findKpiRelaAlarmBySome(query, id, editflag, sortColumn, dir);
        List<KpiRelaAlarmForm> datas = new ArrayList<KpiRelaAlarmForm>();
        for (KpiRelaAlarm de : entityList) {
            datas.add(new KpiRelaAlarmForm(de));
        }
        map.put("datas", datas);
        return map;
    }

    /**
     * <pre>
     * 生成指标类型代码
     * </pre>
     * 
     * @author 陈晓哲
     * @param param
     *            指标ID
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpi/findkpitypecode.f")
    public Map<String, Object> findKpiTypeCode(String param) {
        JSONObject jsobj = JSONObject.fromObject(param);
        Map<String, Object> result = new HashMap<String, Object>();
        String code = this.o_kpiBO.findKpiTypeCode(jsobj.getString("id"));
        result.put("code", code);
        result.put("success", true);
        return result;
    }

    /**
     * <pre>
     * 根据指标ID查找所属的指标类型
     * </pre>
     * 
     * @author 陈晓哲
     * @param kpiId
     *            指标ID
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpi/findkpitypebykid.f")
    public Map<String, Object> findKpiTypeByKid(String kpiId) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject jsobj = o_kpiBO.findKpiTypeByKid(kpiId);
        result.put("result", jsobj);
        result.put("success", true);
        return result;
    }

    /**
     * <pre>
     * 查找所有的指标类型
     * </pre>
     * 
     * @author 陈晓哲
     * @param start
     *            分页起始位置
     * @param limit
     *            显示记录数
     * @param query
     *            查询条件
     * @param sort
     *            排序字段
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpi/findkpitypeall.f")
    public Map<String, Object> findKpiTypeAll(int start, int limit, String query, String sort) {

        HashMap<String, Object> map = new HashMap<String, Object>();
        String dir = "ASC";
        String sortColumn = "name";
        if (StringUtils.isNotBlank(sort)) {
            JSONArray jsonArray = JSONArray.fromObject(sort);
            if (jsonArray.size() > 0) {
                JSONObject jsobj = jsonArray.getJSONObject(0);
                sortColumn = jsobj.getString("property");// 按照哪个字段排序
                dir = jsobj.getString("direction");// 排序方向
            }
        }
        List<KpiForm> datas = new ArrayList<KpiForm>();
        List<Kpi> list = this.o_kpiBO.findKpiTypeAll(query, sortColumn, dir);
        for (Kpi kpi : list) {
            datas.add(new KpiForm(kpi));
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
     *            指标类型ID
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
    @RequestMapping("/kpi/kpi/findkpityperelaresult.f")
    public Map<String, Object> findKpiTypeRelaResult(int start, int limit, String query, String sort, String id, String year, String quarter,
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

        if ("true".equals(lastflag)) {// 最新值查询
            gatherDatas = o_kpiBO.findLastGatherResultByKpiTypeid(map, start, limit, query, sortColumn, dir, id);
        }
        else {// 具体频率查询
            gatherDatas = o_kpiBO.findSpecificGatherResultByKpiTypeid(map, start, limit, query, sortColumn, dir, id, year, quarter, month, week,
                    frequence);
        }
        if (null != gatherDatas) {
            for (Object[] objects : gatherDatas) {
                form = new KpiForm(objects, "kpitype");
                datas.add(form);
            }
        }
        map.put("datas", datas);
        return map;
    }

    /**
     * @param start
     *            分页起始位置
     * @param limit
     *            显示记录数
     * @param query
     *            查询条件
     * @param sort
     *            排序信息
     * @param type
     *            分类树类型 'kpi_type':指标类型
     * @param id
     *            id标识
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/kpi/kpi/findkpibysome.f")
    public Map<String, Object> findKpiBySome(int start, int limit, String query, String sort, String type, String id) throws Exception {
        String dir = "DESC"; // 默认排序顺序
        List<Kpi> kpiList = new ArrayList<Kpi>();
        String sortColumn = "name";// 默认排序字段
        Page<Kpi> page = new Page<Kpi>();
        page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
        page.setPageSize(limit);
        if (StringUtils.isNotBlank(sort)) {
            JSONArray jsonArray = JSONArray.fromObject(sort);
            if (jsonArray.size() > 0) {
                JSONObject jsobj = jsonArray.getJSONObject(0);
                sortColumn = jsobj.getString("property");// 按照哪个字段排序
                dir = jsobj.getString("direction");// 排序方向
            }
        }
        List<KpiForm> datas = new ArrayList<KpiForm>();
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(type) && "kpi_type".equals(type)) {
            // 查询指标类型下关联的指标信息
            page = o_kpiBO.findKpiListById(page, query, "KPI", id, sortColumn, dir);
            kpiList = page.getResult();
        }
        else if (StringUtils.isNotBlank(type) && "sm".equals(type)) {
            page = o_strategyMapBO.findKpiBySmId(page, query, id, sortColumn, dir);
            kpiList = page.getResult();
        }
        else if (StringUtils.isNotBlank(type) && "kpi_category".equals(type)) {
            page = o_categoryBO.findKpiByCategoryId(page, query, id, sortColumn, dir);
            kpiList = page.getResult();
        }
        else if (StringUtils.isNotBlank(type) && "myfolder".equals(type)) {
            page = o_kpiBO.findAllKpiList(page, query, sortColumn, dir);
            kpiList = page.getResult();
        }
        for (Kpi kpi : kpiList) {
            datas.add(new KpiForm(kpi));
        }
        map.put("totalCount", page.getTotalItems());
        map.put("datas", datas);
        return map;
    }

    /**
     * <pre>
     * 生成指标编码
     * </pre>
     * 
     * @author 陈晓哲
     * @param param
     *            指标ID和父ID
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpi/findcodebyparentid.f")
    public Map<String, Object> findCodeByParentId(String param) {
        String code = "";
        Map<String, Object> result = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(param)) {
            JSONObject jsobj = JSONObject.fromObject(param);
            String id = jsobj.getString("id");
            String parentid = jsobj.getString("parentid");
            code = o_kpiBO.findKpiCode(parentid, id);
        }
        result.put("code", code);
        result.put("success", true);
        return result;
    }

    /**
     * <pre>
     * 根据指标名称查询指标ID
     * </pre>
     * 
     * @author 陈晓哲
     * @param name
     *            指标名称
     * @return
     * @throws UnsupportedEncodingException
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpi/findkpiinfobyname.f")
    public Map<String, Object> findKpiInfoByName(String param) throws UnsupportedEncodingException {
        String frequence = "";
        boolean successFlag = true;
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject jsobj = JSONObject.fromObject(param);
        Kpi kpi = this.o_kpiBO.findKpiByName(jsobj.getString("name"));
        String kpiid = kpi.getId();
        DictEntry gatherFrequence = kpi.getGatherFrequence();
        if (null != gatherFrequence) {
            frequence = gatherFrequence.getId();
        }
        result.put("kpiid", kpiid);
        result.put("frequence", frequence);
        result.put("success", successFlag);
        return result;
    }

    /**
     * 指标树 treeLoader:
     * 
     * @author 张帅
     * @param canChecked
     *            是否有复选框
     * @param node
     *            节点id
     * @param query
     *            查询条件
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/KpiTree/kpitreeloader")
    public List<Map<String, Object>> kpiTreeLoader(String node, Boolean canChecked, String query) {
        return o_kpiTreeBO.kpiTreeLoader(node, canChecked, query, null);
    }

    /**
     * 我的指标树 treeLoader:
     * 
     * @author 张帅
     * @param canChecked
     *            是否有复选框
     * @param node
     *            节点id
     * @param query
     *            查询条件
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/KpiTree/minetreeloader")
    public List<Map<String, Object>> mineTreeLoader(String node, Boolean canChecked, String query) {
        return o_kpiTreeBO.kpiTreeLoader(node, canChecked, query, "emp");
    }

    /**
     * 机构指标树 treeLoader:
     * 
     * @author 张帅
     * @param canChecked
     *            是否有复选框
     * @param node
     *            节点id
     * @param query
     *            查询条件
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/KpiTree/orgtreeloader")
    public List<Map<String, Object>> orgTreeLoader(String node, Boolean canChecked, String query) {
        return o_kpiTreeBO.orgTreeLoader(node, canChecked, query, null);
    }

    /**
     * <pre>
     * 指标类型树查询
     * </pre>
     * 
     * @author 陈晓哲
     * @param node
     *            id标识
     * @param query
     *            查询条件
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpi/kpitypetreeloader.f")
    public List<Map<String, Object>> kpiTypeTreeLoader(String node, String query, Boolean canChecked) {
        return o_kpiTreeBO.kpiTypeTreeLoader(node, query, canChecked);
    }

    /**
     * 目标指标树 treeLoader:
     * 
     * @author 张帅
     * @param canChecked
     *            是否有复选框
     * @param node
     *            节点id
     * @param query
     *            查询条件
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/KpiTree/strategyMapTreeLoader")
    public List<Map<String, Object>> strategyMapTreeLoader(String node, Boolean canChecked, String query, String smIconType) {
        return o_kpiTreeBO.strategyMapTreeLoader(node, canChecked, query, null, smIconType);
    }

    /**
     * <pre>
     * 保存指标类型
     * </pre>
     * 
     * @author 陈晓哲
     * @param form
     *            指标form
     * @return
     * @throws ParseException
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpi/mergekpitype.f")
    public Map<String, Object> mergeKpiType(KpiForm form, HttpServletRequest request) throws ParseException {
        Map<String, Object> result = new HashMap<String, Object>();
        String kid = request.getParameter("id");
        form.setId(kid);
        String id = this.o_kpiBO.mergeKpiType(form);
        result.put("id", id);
        result.put("success", true);
        return result;
    }

    /**
     * 设置指标启用或停用
     * 
     * @param enable
     *            启用或停用
     * @param kpiId
     *            指标id
     * @return
     */
    @ResponseBody
    @RequestMapping("/kpi/kpi/mergekpienable.f")
    public Map<String, Object> mergeKpiEnable(String kpiItems) {
        JSONObject jsobj = JSONObject.fromObject(kpiItems);
        String enable = jsobj.getString("enable");
        JSONArray kpiidArr = jsobj.getJSONArray("kpiids");
        String[] kpiids = new String[] {};
        kpiids = (String[]) kpiidArr.toArray(kpiids);
        Map<String, Object> result = new HashMap<String, Object>();
        if (kpiids.length > 0) {
            for (String kpiid : kpiids) {
                Kpi kpi = o_kpiBO.findKpiById(kpiid);
                kpi.setStatus(o_dictEntryBO.findDictEntryById(enable));
                this.o_kpiBO.mergeKpi(kpi);
            }
        }
        result.put("success", true);
        return result;
    }

    /**
     * <pre>
     * 保存指标信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param form
     *            指标form
     * @return
     * @throws ParseException
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpi/mergekpi.f")
    public Map<String, Object> mergeKpi(KpiForm form, HttpServletRequest request) throws ParseException {
        Map<String, Object> result = new HashMap<String, Object>();
        form.setId(request.getParameter("id"));
        form.setParentKpiId(request.getParameter("parentid"));
        String id = o_kpiBO.saveKpi(form);
        result.put("id", id);
        result.put("success", true);
        return result;
    }

    /**
     * <pre>
     * 更新指标类型采集计算报告
     * </pre>
     * 
     * @author 陈晓哲
     * @param 采集结果参数
     * @return
     * @throws Exception
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpi/mergekpitypecal.f")
    public Map<String, Object> mergeKpiTypeCalculate(String param) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        KpiForm form = new KpiForm();
        JSONObject jsobj = JSONObject.fromObject(param);
        form.setId(jsobj.getString("id"));
        form.setGatherReportValueCron(jsobj.getString("gatherreportCron"));
        form.setTargetReportCron(jsobj.getString("targetReportCron"));
        form.setGatherValueCron(jsobj.getString("gatherValueCron"));
        form.setTargetValueCron(jsobj.getString("targetValueCron"));
        form.setGatherFrequenceDict(jsobj.getString("gatherfrequence"));
        form.setGatherfrequenceRule(jsobj.getString("gatherfrequenceRule"));
        form.setTargetSetFrequenceDict(jsobj.getString("targetSetFrequenceDictType"));
        form.setTargetSetFrequenceRule(jsobj.getString("targetSetFrequenceRadioType"));
        form.setReportFrequenceDict(jsobj.getString("reportFrequenceDictType"));
        form.setReportFrequenceRule(jsobj.getString("reportFrequenceRadioType"));
        form.setTargetSetReportFrequenceDict(jsobj.getString("targetSetReportFrequenceDictType"));
        form.setTargetSetReportFrequenceRule(jsobj.getString("targetSetReportFrequenceRadioType"));
        form.setIsResultFormula(jsobj.getString("resultformulaDict"));
        form.setIsTargetFormula(jsobj.getString("targetformulaDict"));
        form.setIsAssessmentFormula(jsobj.getString("assessmentformulaDict"));
        if(StringUtils.isNotBlank(jsobj.getString("calcValue"))){
            form.setCalcStr(jsobj.getString("calcValue"));
        }
        if (!"null".equals(jsobj.getString("resultformula"))) {
            form.setResultFormula(jsobj.getString("resultformula"));
        }
        if (!"null".equals(jsobj.getString("targetformula"))) {
            form.setTargetFormula(jsobj.getString("targetformula"));
        }
        if (!"null".equals(jsobj.getString("assessmentformula"))) {
            form.setAssessmentFormula(jsobj.getString("assessmentformula"));
        }
        /*if(!"null".equals(jsobj.getString("alarmformula"))){
            form.setAlarmFormula(jsobj.getString("alarmformula"));
        }*/
        form.setResultSumMeasureStr(jsobj.getString("resultSumMeasureStr"));
        form.setTargetSumMeasureStr(jsobj.getString("targetSumMeasureStr"));
        form.setAssessmentSumMeasureStr(jsobj.getString("assessmentSumMeasureStr"));
        if (StringUtils.isNotBlank(jsobj.getString("scale"))) {
            form.setScale(Integer.parseInt(jsobj.getString("scale")));
        }
        form.setRelativeToStr(jsobj.getString("relativeTo"));
        form.setResultCollectIntervalStr(jsobj.getString("resultCollectInterval"));
        form.setTargetSetIntervalStr(jsobj.getString("targetSetInterval"));
        if (!"null".equals(jsobj.getString("modelValue")) && StringUtils.isNotBlank(jsobj.getString("modelValue"))) {
            form.setModelValue(Double.parseDouble(jsobj.getString("modelValue")));
        }
        if (!"null".equals(jsobj.getString("maxValue")) && StringUtils.isNotBlank(jsobj.getString("maxValue"))) {
            form.setMaxValue(Double.parseDouble(jsobj.getString("maxValue")));
        }
        if (!"null".equals(jsobj.getString("minValue")) && StringUtils.isNotBlank(jsobj.getString("minValue"))) {
            form.setMinValue(Double.parseDouble(jsobj.getString("minValue")));
        }
        form.setTargetSetFrequenceStr(jsobj.getString("targetSetFrequenceStr"));
        form.setReportFrequenceStr(jsobj.getString("reportFrequenceStr"));
        form.setResultgatherfrequence(jsobj.getString("resultgatherfrequence"));
        form.setTargetSetReportFrequenceStr(jsobj.getString("targetSetReportFrequenceStr"));
        this.o_kpiBO.mergeKpiTypeCalculate(form);
        result.put("success", true);
        return result;
    }

    /**
     * <pre>
     * 更新指标和预警关联信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param modifiedRecord
     *            告警信息
     * @param id
     *            指标ID
     * @return
     * @throws ParseException
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpi/mergekpirelaalarm.f")
    public Map<String, Object> mergeKpiRelaAlarm(String modifiedRecord, String id) throws ParseException {
        Map<String, Object> result = new HashMap<String, Object>();
        this.o_kpiBO.mergeKpiRelaAlarm(modifiedRecord, id);
        result.put("success", true);
        return result;
    }

    /**
     * <pre>
     * 逻辑删除指标类型
     * </pre>
     * 
     * @author 陈晓哲
     * @param id
     *            指标类型ID
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpi/removekpitype.f")
    public Map<String, Object> removeKpiType(String id) {
        boolean result = true;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (this.o_kpiBO.findKpiCountByType(id) > 0) {
            result = false;
        }
        else {
            this.o_kpiBO.removeKpiType(id);
        }
        resultMap.put("result", result);
        return resultMap;
    }

    /**
     * <pre>
     * 批量删除指标信息(记分卡列表中的删除事件使用)
     * </pre>
     * 
     * @author 陈晓哲
     * @param kpiItems
     *            kpiid集合
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpi/removekpibatch.f")
    public Map<String, Object> removeKpiBatch(String kpiItems) {
        boolean result = true;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        o_kpiBO.removeKpiBatch(kpiItems);
        resultMap.put("success", true);
        resultMap.put("result", result);
        return resultMap;
    }

    /**
     * <pre>
     * 批量删除指标信息(我的度量指标, 所有度量指标, 战略目标关联的度量指标, 指标类型关联的度量指标列表中的删除事件使用)
     * </pre>
     * 
     * @author 陈晓哲
     * @param kpiItems
     *            kpiid集合
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpi/removecommonkpibatch.f")
    public Map<String, Object> removeCommonKpiBatch(String kpiItems) {
        boolean result = true;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        o_kpiBO.removeCommonKpiBatch(kpiItems);
        resultMap.put("success", true);
        resultMap.put("result", result);
        return resultMap;
    }

    /**
     * <pre>
     * 校验指标类型信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param validateItem
     *            指标校验信息
     * @param id
     *            指标id
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpi/validate.f")
    public Map<String, Object> validate(String validateItem, String id) {
        boolean successFlag = true;
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject jsobj = JSONObject.fromObject(validateItem);
        String type = jsobj.getString("type");
        String name = jsobj.getString("name");
        String code = jsobj.getString("code");

        // 判断是指标类型还是指标
        if (Contents.KPI_TYPE.equals(type)) {
            String namedefault = jsobj.getString("namedefault");
            String kpitypename = jsobj.getString("kpitypename");
            if (Contents.DICT_Y.equals(namedefault)) {
                if (StringUtils.isNotBlank(kpitypename)) {
                    name = jsobj.getString("categoryname") + " " + kpitypename;
                }
            }
        }
        if (o_kpiBO.findKpiCountByName(name, id, type) > 0) {
            successFlag = false;
            result.put("error", "nameRepeat");
            return result;
        }
        if (o_kpiBO.findKpiCountByCode(code, id, type) > 0) {
            successFlag = false;
            result.put("error", "codeRepeat");
        }
        result.put("success", successFlag);
        return result;
    }

    /**
     * <pre>
     * 保存季度指标采集结果
     * </pre>
     * 
     * @author 金鹏祥
     * @param params
     *            采集结果信息
     * @param request
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpi/savekpigatherresultquarter.f")
    public Map<String, Object> saveKpiGatherResultQuarter(String params, HttpServletRequest request) {
        boolean successFlag = true;
        Map<String, Object> result = new HashMap<String, Object>();
        String kpiid = request.getParameter("kpiid");
        this.o_kpiBO.saveKpiGatherResultQuarter(params, kpiid);
        o_kpiBO.saveKpiLastTimePeriod(params, kpiid);
        result.put("success", successFlag);

        return result;
    }

    /**
     * <pre>
     * 根据指标的采集频率生成采集表单
     * </pre>
     * 
     * @author 金鹏祥
     * @param condItem
     *            指标名称,年份信息
     * @param request
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpi/createtable.f")
    public Map<String, Object> createTable(String condItem, HttpServletRequest request) {
        String contextPath = request.getContextPath();
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject jsobj = JSONObject.fromObject(condItem);
        String year = "";
        if (jsobj.get("year") != null) {
            year = jsobj.getString("year");
        }
        String oneEdit = "";
        if (jsobj.get("oneEdit") != null) {
            oneEdit = jsobj.getString("oneEdit");
        }
        if (oneEdit.equalsIgnoreCase("1")) {
            if (jsobj.get("yearId") != null) {
                year = jsobj.getString("yearId");
            }
        }
        String kpiName = "";
        if (jsobj.get("kpiname") != null) {
            kpiName = jsobj.getString("kpiname");
        }

        String timeId = "";
        if (jsobj.get("timeId") != null) {
            timeId = jsobj.getString("timeId");
        }
        String eType = "";
        if (jsobj.get("eType") != null) {
            eType = jsobj.getString("eType");
        }
        boolean isNewValue = false;
        if (jsobj.get("isNewValue") != null) {
            isNewValue = Boolean.valueOf(jsobj.get("isNewValue").toString());
        }
        String frequecy = "";
        String kpiId = "";
        String html = "";
        boolean edit = Boolean.valueOf(request.getParameter("edit"));
        List<KpiGatherResult> kpiGatherResultList = null;
        HashMap<String, TimePeriod> map = o_kpiGatherResultBO.findTimePeriodAllMap();

        if ("0frequecy_all".equalsIgnoreCase(eType)) {
            List<List<KpiGatherResult>> kpiGatherResultLists = new ArrayList<List<KpiGatherResult>>();
            HashMap<String, String> kpiGatherResultMapYearNewValue = new HashMap<String, String>();
            HashMap<String, String> msColumnLine3DMap = new HashMap<String, String>();
            HashMap<String, List<KpiGatherResult>> mapYear = new HashMap<String, List<KpiGatherResult>>();
            String msColumnLine3D = "";
            if (jsobj.get("kpiId") != null) {
                if (jsobj.get("isNewValue") != null) {
                    isNewValue = Boolean.valueOf(jsobj.get("isNewValue").toString());
                }
                String id = jsobj.getString("kpiId");
                String[] kpiIds = id.split(",");

                if (id.indexOf(",!@#$") != -1) {
                    id = id.replace(",!@#$", "");
                    String yearsStr = "";
                    if (isNewValue) {
                        // 年
                        kpiGatherResultMapYearNewValue = o_kpiBO.findKpiAndGatherResultYearNewValue(id);
                        for (String kpi : kpiIds) {
                            if (kpiGatherResultMapYearNewValue.get(kpi.replace("'", "").replace("'", "")) != null) {
                                // 有最新值并且存在年份-保留
                                String years[] = this.getYears(kpiGatherResultMapYearNewValue.get(kpi.replace("'", "").replace("'", "")));
                                for (String str : years) {
                                    yearsStr += "'" + str + "',";
                                }
                                yearsStr = yearsStr + "|";
                                yearsStr = yearsStr.replace(",|", "");
                                mapYear.put(kpi.replace("'", "").replace("'", ""),
                                        o_kpiBO.findKpiAndGatherResultYear(kpi.replace("'", "").replace("'", ""), yearsStr));
                            }
                        }

                        // 除了年
                        kpiGatherResultLists.add(o_kpiBO.findKpiAndGatherResult(id, false, null));
                    }
                    else {

                        for (String kpi : kpiIds) {
                            // 有最新值并且存在年份-保留
                            String years[] = this.getYears(year);
                            for (String str : years) {
                                yearsStr += "'" + str + "',";
                            }
                            yearsStr = yearsStr + "|";
                            yearsStr = yearsStr.replace(",|", "");
                            List<KpiGatherResult> list = o_kpiBO.findKpiAndGatherResultYear(kpi.replace("'", "").replace("'", ""), yearsStr);
                            if (list != null) {
                                mapYear.put(kpi.replace("'", "").replace("'", ""), list);
                            }
                            // }
                        }

                        // 除年外
                        kpiGatherResultLists.add(o_kpiBO.findKpiAndGatherResult(id, true, year));
                    }

                    // 年
                    for (String string : kpiIds) {
                        if (mapYear.get(string.replace("'", "").replace("'", "")) != null) {
                            msColumnLine3D = MSColumnLine3D.getXml("", kpiName, mapYear.get(string.replace("'", "").replace("'", "")), map);
                            msColumnLine3DMap.put(string.replace("'", "").replace("'", ""), msColumnLine3D);
                        }
                    }

                    // 除年外
                    for (String string : kpiIds) {
                        for (List<KpiGatherResult> list : kpiGatherResultLists) {
                            kpiGatherResultList = this.getKpiGatherResultListMap(list, string.replace("'", "").replace("'", ""), year);
                            if (kpiGatherResultList.size() != 0) {
                                msColumnLine3D = MSColumnLine3D.getXml("", kpiName, kpiGatherResultList, map);
                                msColumnLine3DMap.put(string.replace("'", "").replace("'", ""), msColumnLine3D);
                            }
                        }
                    }

                    result.put("success", true);
                    result.put("xmlMap", msColumnLine3DMap);
                    return result;
                }
            }

        }

        if (StringUtils.isNotBlank(kpiName)) {
            List<KpiGatherResult> kpiGatherResultLists1 = null;
            List<List<KpiGatherResult>> kpiGatherResultLists = new ArrayList<List<KpiGatherResult>>();
            HashMap<String, List<KpiGatherResult>> mapYear = new HashMap<String, List<KpiGatherResult>>();
            HashMap<String, String> kpiGatherResultMapYearNewValue = new HashMap<String, String>();
            Kpi kpi = this.o_kpiBO.findKpiByName(kpiName);

            if (kpi != null) {
                DictEntry gatherFrequence = kpi.getGatherFrequence();
                if (gatherFrequence != null) {
                    frequecy = gatherFrequence.getId();
                    if (StringUtils.isNotBlank(frequecy)) {
                        kpiId = kpi.getId();
                        String yearsStr = "";
                        if (isNewValue) {
                            if (kpi.getLastTimePeriod() != null) {
                                year = kpi.getLastTimePeriod().getId().subSequence(0, 4).toString();
                            }
                            else {
                                year = this.getYear();
                            }
                        }

                        if (!"".equalsIgnoreCase(year)) {
                            if ("0frequecy_week".equalsIgnoreCase(frequecy)) {
                                html = o_tableWeekBO.getWeekHtml(contextPath, year, edit, kpiId, kpiName, timeId);
                            }
                            else if ("0frequecy_month".equalsIgnoreCase(frequecy)) {
                                html = o_tableMonthBO.getMonthHtml(contextPath, year, edit, kpiId, kpiName, timeId);
                            }
                            else if ("0frequecy_quarter".equalsIgnoreCase(frequecy)) {
                                html = o_tableQuarterBO.getQuarterHtml(contextPath, year, edit, kpiId, kpiName, timeId);
                            }
                            else if ("0frequecy_halfyear".equalsIgnoreCase(frequecy)) {
                                html = o_tableHalfYearBO.getHalfYearHtml(contextPath, year, edit, kpiId, kpiName, timeId);
                            }
                            else if ("0frequecy_year".equalsIgnoreCase(frequecy)) {
                                html = o_tableYearBO.getYearHtml(contextPath, year, edit, kpiId, kpiName, timeId);
                            }
                        }

                        if (isNewValue) {
                            // 年
                            kpiGatherResultMapYearNewValue = o_kpiBO.findKpiAndGatherResultYearNewValue("'" + kpiId + "'");
                            if (kpiGatherResultMapYearNewValue.get(kpiId) != null) {
                                // 有最新值并且存在年份-保留
                                String years[] = this.getYears(kpiGatherResultMapYearNewValue.get(kpiId));
                                for (String str : years) {
                                    yearsStr += "'" + str + "',";
                                }
                                yearsStr = yearsStr + "|";
                                yearsStr = yearsStr.replace(",|", "");
                                mapYear.put(kpiId, o_kpiBO.findKpiAndGatherResultYear(kpiId, yearsStr));
                            }

                            // 除年外
                            kpiGatherResultLists1 = o_kpiBO.findKpiAndGatherResult("'" + kpiId + "'", false, null);
                            kpiGatherResultLists.add(kpiGatherResultLists1);
                        }
                        else {

                            // 年
                            String years[] = this.getYears(year);
                            for (String str : years) {
                                yearsStr += "'" + str + "',";
                            }
                            yearsStr = yearsStr + "|";
                            yearsStr = yearsStr.replace(",|", "");
                            List<KpiGatherResult> list = o_kpiBO.findKpiAndGatherResultYear(kpiId, yearsStr);
                            if (list != null) {
                                mapYear.put(kpiId, list);
                            }

                            // 除年外
                            kpiGatherResultLists1 = o_kpiBO.findKpiAndGatherResult("'" + kpiId + "'", true, year);
                            kpiGatherResultLists.add(kpiGatherResultLists1);
                        }
                        String msColumnLine3D = "";

                        // 年
                        if (mapYear.get(kpiId) != null) {
                            msColumnLine3D = MSColumnLine3D.getXml("", kpiName, mapYear.get(kpiId), map);
                        }

                        // 除年外
                        for (List<KpiGatherResult> list : kpiGatherResultLists) {
                            kpiGatherResultList = this.getKpiGatherResultListMap(list, kpiId, year);
                            if (kpiGatherResultList.size() != 0) {
                                msColumnLine3D = MSColumnLine3D.getXml("", kpiName, kpiGatherResultList, map);
                            }
                        }

                        result.put("success", true);
                        result.put("year", year);
                        result.put("tableHtml", html);
                        result.put("xml", msColumnLine3D);
                    }
                }
            }
        }
        return result;
    }

    private List<KpiGatherResult> getKpiGatherResultListMap(List<KpiGatherResult> kpiGatherResultLists, String kpiId, String year) {
        List<KpiGatherResult> kpiGatherResultList = new ArrayList<KpiGatherResult>();
        for (KpiGatherResult kpiGatherResult : kpiGatherResultLists) {
            if (kpiId.equalsIgnoreCase(kpiGatherResult.getKpi().getId())) {
                if (kpiGatherResult.getKpi().getGatherFrequence().getId().equalsIgnoreCase("0frequecy_year")) {
                    break;
                }
                else {
                    kpiGatherResultList.add(kpiGatherResult);
                }
            }
        }

        return kpiGatherResultList;
    }

    /**
     * 根据empId查询需要录入目标值的指标.
     * 
     * @param executionId
     *            流程id
     * @param limit
     *            页数
     * @param start
     *            每页显示条数
     * @return Map<String,Object>
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping("/kpi/kpi/findTargetOrGatherKpiByEmpId.f")
    public Map<String, Object> findTargetOrGatherKpiByEmpId(String executionId, int limit, int start) throws ParseException {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();

        String empId = "";
        String type = "";// T:目标部门;G-采集部门;
        Date date = null;

        if (StringUtils.isNotBlank(executionId)) {
            String empIdVar = o_jbpmOperate.getVariable(executionId, "empId");
            if (StringUtils.isNotBlank(empIdVar)) {
                empId = empIdVar;
            }
            String dateVar = o_jbpmOperate.getVariable(executionId, "date");
            if (StringUtils.isNotBlank(dateVar)) {
                date = DateUtils.parseTimeStamp(dateVar);
            }
        }

        if (StringUtils.isBlank(empId)) {
            empId = UserContext.getUser().getEmpid();
        }

        if (null == date) {
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            date = calendar.getTime();
        }

        List<String> kpiIdList = new ArrayList<String>();
        List<Kpi> kpiList = o_kpiBO.findKpiListByEmpId(empId, type, date);
        for (Kpi kpi : kpiList) {
            if (!kpiIdList.contains(kpi.getId())) {
                kpiIdList.add(kpi.getId());
            }
        }

        if (null != kpiIdList && kpiIdList.size() > 0) {
            Map<String, Object> objectMap = o_kpiGatherResultBO.findKpiRelaInfoByKpiIdsAndType(kpiIdList, "", empId, start, limit, date);

            // 指标相关信息map:key--指标id;value--指标的相关信息;
            Map<String, List<Object[]>> dataMap = (Map<String, List<Object[]>>) objectMap.get("datas");

            if (null != dataMap && dataMap.size() > 0) {
                Map<String, Object> row = null;
                List<String> tempIdList = new ArrayList<String>();
                for (String tempId : dataMap.keySet()) {
                    if (!tempIdList.contains(tempId)) {
                        tempIdList.add(tempId);
                    }
                }
                // 指标图表map:key--指标id;value--图表xml;
                Map<String, String> chartMap = o_kpiGatherResultBO.findKpiChartsByKpiIds(tempIdList);
                for (String kpiId : dataMap.keySet()) {
                    List<Object[]> objects = dataMap.get(kpiId);
                    if (null != objects && objects.size() > 0) {
                        row = new HashMap<String, Object>();
                        for (Object[] object : objects) {
                            if (null != object[6] && null != object[7]) {
                                continue;
                            }
                            // 当期目标值，实际值
                            if (null != object[14] && empId.equals(String.valueOf(object[14])) && null != object[2]
                                    && "0sys_use_formular_manual".equals(object[2])) {
                                // 可编辑标志
                                row.put("targetValueEdit", true);
                                // 目标临时值
                                row.put("targetValue", null == object[21] ? "" : object[21]);
                            }
                            else {
                                // 不可编辑标志
                                row.put("targetValueEdit", false);
                                // 目标值
                                row.put("targetValue", null == object[6] ? "" : object[6]);
                            }
                            if (null != object[18] && empId.equals(String.valueOf(object[18])) && null != object[3]
                                    && "0sys_use_formular_manual".equals(object[3])) {
                                // 可编辑标志
                                row.put("finishValueEdit", true);
                                // 实际临时值
                                row.put("finishValue", null == object[22] ? "" : object[22]);
                            }
                            else {
                                // 不可编辑标志
                                row.put("finishValueEdit", false);
                                // 实际值
                                row.put("finishValue", null == object[7] ? "" : object[7]);
                            }
                            // 指标采集结果id
                            row.put("id", object[5]);
                            // 指标名称
                            row.put("kpiName", object[1]);
                            // 单位
                            row.put("units", object[4]);
                            // 指标所属部门/人员，采集部门/人员
                            if (null != object[15] && null != object[16]) {
                                row.put("gatherOrgEmp", object[15] + "/" + object[16]);
                            }
                            else {
                                row.put("gatherOrgEmp", "");
                            }
                            if (null != object[11] && null != object[12]) {
                                row.put("targetOrgEmp", object[11] + "/" + object[12]);
                            }
                            else {
                                row.put("targetOrgEmp", "");
                            }
                            // 上期实际值，状态，趋势，录入人，录入时间
                            row.put("preFinishValue", object[8]);
                            String assessmentStatus = "/images/icons/underconstruction_small.gif";
                            if (null != object[9]) {
                                String assessmentStatusValue = String.valueOf(object[9]);
                                if ("icon-ibm-symbol-4-sm".equals(assessmentStatusValue)) {
                                    assessmentStatus = "/images/icons/symbol_4_sm.gif";
                                }
                                else if ("icon-ibm-symbol-5-sm".equals(assessmentStatusValue)) {
                                    assessmentStatus = "/images/icons/symbol_5_sm.gif";
                                }
                                else if ("icon-ibm-symbol-6-sm".equals(assessmentStatusValue)) {
                                    assessmentStatus = "/images/icons/symbol_6_sm.gif";
                                }
                                else if ("icon-ibm-underconstruction-small".equals(assessmentStatusValue)) {
                                    assessmentStatus = "/images/icons/underconstruction_small.gif";
                                }
                            }
                            row.put("assessmentStatus", assessmentStatus);
                            if (null != object[10]) {
                                String directionValue = String.valueOf(object[10]);
                                String direction = "";
                                if ("icon-ibm-icon-trend-rising-positive".equals(directionValue)) {
                                    direction = "/images/icons/icon_trend_rising_positive.gif";
                                }
                                else if ("icon-ibm-icon-trend-falling-negative".equals(directionValue)) {
                                    direction = "/images/icons/icon_trend_falling_negative.gif";
                                }
                                else if ("icon-ibm-icon-trend-neutral-null".equals(directionValue)) {
                                    direction = "/images/icons/icon_trend_neutral_null.gif";
                                }
                                row.put("direction", direction);
                            }
                            else {
                                row.put("direction", "");
                            }
                            // 指标当年历史数据图：柱线混合图--柱：实际值；线：目标值；
                            row.put("xml", chartMap.get(kpiId));
                            datas.add(row);
                        }
                    }
                }
            }
            map.put("datas", datas);
            // 指标总数
            int totalCount = Integer.valueOf(String.valueOf(objectMap.get("totalCount")));
            if (limit * start < totalCount) {
                map.put("isNext", true);
            }
            else {
                map.put("isNext", false);
            }
            map.put("totalCount", totalCount);
            // 需录入数
            int needCount = 0;
            List<Object[]> needKpiGatherResultList = o_kpiGatherResultBO.findNeedKpiGatherResultListByEmpId(empId, date);
            if (null != needKpiGatherResultList) {
                needCount = needKpiGatherResultList.size();
            }
            map.put("needCount", needCount);
            // 已录入数
            int finishCount = 0;
            List<Object[]> finishKpiGatherResultList = o_kpiGatherResultBO.findFinishKpiGatherResultListByEmpId(empId, date);
            if (null != finishKpiGatherResultList) {
                for (Object[] object : finishKpiGatherResultList) {
                    if (null != object[0] && null != object[2] && empId.equals(String.valueOf(object[2]))) {
                        finishCount++;
                    }
                    if (null != object[1] && null != object[3] && empId.equals(String.valueOf(object[3]))) {
                        finishCount++;
                    }
                }
            }
            map.put("finishCount", finishCount);
        }
        else {
            map.put("datas", new Object[0]);
            map.put("totalCount", "0");
            map.put("needCount", "0");
            map.put("finishCount", "0");
        }
        map.put("start", start);
        return map;
    }

    /**
     * 指标目标值/实际值录入临时值.
     * 
     * @author 吴德福
     * @param paramArray
     *            指标采集结果数组
     * @return Map<String, Object>
     */
    @ResponseBody
    @RequestMapping("/kpi/kpi/saveKpiGatherReulst.f")
    public Map<String, Object> saveKpiGatherReulst(String paramArray) {
        Map<String, Object> result = new HashMap<String, Object>();

        JSONArray paramArryObject = JSONArray.fromObject(paramArray);
        if (null != paramArryObject && paramArryObject.size() > 0) {
            List<String> idList = new ArrayList<String>();
            for (int i = 0; i < paramArryObject.size(); i++) {
                JSONObject jsobj = paramArryObject.getJSONObject(i);
                if (!idList.contains(jsobj.get("id"))) {
                    idList.add(String.valueOf(jsobj.get("id")));
                }
            }
            if (null != idList && idList.size() > 0) {
                List<KpiGatherResult> kpiGatherResultList = o_kpiGatherResultBO.findKpiGatherResultByIds(idList);
                for (int i = 0; i < paramArryObject.size(); i++) {
                    JSONObject jsobj = paramArryObject.getJSONObject(i);
                    for (KpiGatherResult kpiGatherResult : kpiGatherResultList) {
                        String gatherEmpId = "";
                        String targetEmpId = "";
                        Set<KpiRelaOrgEmp> kpiRelaOrgEmps = kpiGatherResult.getKpi().getKpiRelaOrgEmps();
                        for (KpiRelaOrgEmp kpiRelaOrgEmp : kpiRelaOrgEmps) {
                            if (Contents.GATHERDEPARTMENT.equals(kpiRelaOrgEmp.getType())) {
                                // 实际值采集人
                                if (null != kpiRelaOrgEmp.getEmp()) {
                                    gatherEmpId = kpiRelaOrgEmp.getEmp().getId();
                                }
                            }
                            if (Contents.TARGETDEPARTMENT.equals(kpiRelaOrgEmp.getType())) {
                                // 目标值采集人
                                if (null != kpiRelaOrgEmp.getEmp()) {
                                    targetEmpId = kpiRelaOrgEmp.getEmp().getId();
                                }
                            }
                        }
                        if (kpiGatherResult.getId().equals(String.valueOf(jsobj.get("id")))) {
                            if (null != jsobj && null != jsobj.get("targetValue")) {
                                if (StringUtils.isNotBlank(String.valueOf(jsobj.get("targetValue")))) {
                                    kpiGatherResult.setTempTargetValue(Double.valueOf(String.valueOf(jsobj.get("targetValue"))));
                                    kpiGatherResult.setTargetSetStatus(Contents.STATUS_SAVED);
                                    if (null != UserContext.getUser() && null != UserContext.getUser().getEmp()) {
                                        kpiGatherResult.setTargetValueInputEmp(UserContext.getUser().getEmp());
                                    }
                                }
                                else {
                                    if (UserContext.getUser().getEmpid().equals(targetEmpId)) {
                                        kpiGatherResult.setTempTargetValue(null);
                                        kpiGatherResult.setTargetSetStatus(null);
                                        kpiGatherResult.setTargetValueInputEmp(null);
                                    }
                                }
                            }
                            if (null != jsobj && null != jsobj.get("finishValue")) {
                                if (StringUtils.isNotBlank(String.valueOf(jsobj.get("finishValue")))) {
                                    kpiGatherResult.setTempFinishValue(Double.valueOf(String.valueOf(jsobj.get("finishValue"))));
                                    kpiGatherResult.setFinishSetStatus(Contents.STATUS_SAVED);
                                    if (null != UserContext.getUser() && null != UserContext.getUser().getEmp()) {
                                        kpiGatherResult.setResultValueInputEmp(UserContext.getUser().getEmp());
                                    }
                                }
                                else {
                                    if (UserContext.getUser().getEmpid().equals(gatherEmpId)) {
                                        kpiGatherResult.setTempFinishValue(null);
                                        kpiGatherResult.setFinishSetStatus(null);
                                        kpiGatherResult.setResultValueInputEmp(null);
                                    }
                                }
                            }

                            o_kpiGatherResultBO.mergeKpiGatherResult(kpiGatherResult);

                        }
                    }
                }
            }
        }

        result.put("success", true);
        return result;
    }

    /**
     * 指标目标值/实际值提交.
     * 
     * @param executionId
     *            流程id
     * @param empId
     *            员工id
     * @return Map<String, Object>
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping("/kpi/kpi/submitKpiGatherReulst.f")
    public Map<String, Object> submitKpiGatherReulst(String executionId, String empId) throws ParseException {
        Map<String, Object> result = new HashMap<String, Object>();
        Date date = null;

        if (StringUtils.isNotBlank(executionId)) {
            String empIdVar = o_jbpmOperate.getVariable(executionId, "empId");
            if (StringUtils.isNotBlank(empIdVar)) {
                empId = empIdVar;
            }
            String dateVar = o_jbpmOperate.getVariable(executionId, "date");
            if (StringUtils.isNotBlank(dateVar)) {
                date = DateUtils.parseTimeStamp(dateVar);
            }
        }

        if (StringUtils.isBlank(empId)) {
            empId = UserContext.getUser().getEmpid();
        }

        if (null == date) {
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            date = calendar.getTime();
        }

        if (StringUtils.isNotBlank(empId)) {
            List<KpiGatherResult> kpiGatherResultList = o_kpiGatherResultBO.findKpiGatherResultByEmpId(empId, date);
            for (KpiGatherResult kpiGatherResult : kpiGatherResultList) {
                Kpi kpi = kpiGatherResult.getKpi();
                if (null != kpiGatherResult.getTempTargetValue()) {
                    kpiGatherResult.setTargetValue(kpiGatherResult.getTempTargetValue());
                    kpiGatherResult.setTargetSetStatus(Contents.STATUS_SUBMITTED);
                    // 更新指标目标收集下次执行时间
                    kpi.setTargetCalculatetime(this.calculateNextExecuteTime(kpi, "targetValue"));
                }
                if (null != kpiGatherResult.getTempFinishValue()) {
                    kpiGatherResult.setFinishValue(kpiGatherResult.getTempFinishValue());
                    kpiGatherResult.setFinishSetStatus(Contents.STATUS_SUBMITTED);
                    // 更新指标timeperiod字段
                    String kpiNewValue = o_kpiBO.getKpiNewValue(kpi.getId());
                    if (null != kpiNewValue) { 
                        kpi.setLastTimePeriod(o_timePeriodBO.findTimePeriodById(kpiNewValue));
                    }

                    //评估值公式计算 
                    if ("0sys_use_formular_formula".equals(kpi.getIsAssessmentFormula())) {
                        if (null == kpiGatherResult.getAssessmentValue()) {
                            String ret = o_formulaCalculateBO.calculate(kpi.getName(), "kpi", kpi.getAssessmentFormula(), kpiGatherResult
                                    .getTimePeriod().getId());
                            if (o_formulaCalculateBO.isDoubleOrInteger(ret)) {
                                //返回结果是double，计算正确
                                kpiGatherResult.setAssessmentValue(Double.valueOf(ret));
                                //计算告警状态 
                                AlarmPlan alarmPlan = o_kpiBO.findAlarmPlanByKpiId(kpi.getId(), Contents.ALARMPLAN_REPORT);
                                
                                DictEntry assessmentStatus = o_kpiBO.findKpiAlarmStatusByValues(alarmPlan, kpiGatherResult.getAssessmentValue());//金融街修改
                                
                               /* DictEntry assessmentStatus = o_kpiBO.findKpiAssessmentStatusByAssessmentValue(alarmPlan,
                                        kpiGatherResult.getAssessmentValue());*/
                                
                                kpiGatherResult.setAssessmentStatus(assessmentStatus);
                                //计算趋势 
                                DictEntry trend = o_kpiGatherResultBO.calculateTrendByAssessmentValue(kpiGatherResult);
                                kpiGatherResult.setDirection(trend);
                                
                                
                            }
                        }
                    }

                    //更新指标结果收集下次执行时间
                    kpi.setCalculatetime(this.calculateNextExecuteTime(kpi, "finishValue"));

                }
                
                //同比和环比计算
                this.o_jrjBO.findValueChr(kpiGatherResult);//金融街修改
                
                o_kpiBO.mergeKpi(kpi);

                o_kpiGatherResultBO.mergeKpiGatherResult(kpiGatherResult);
                
                //获得指标的告警方案,然后根据输入的实际值与告警方案比较得到告警状态
                /*boolean jrjflag = false;
                if (jrjflag) {
                    String valueStr = o_formulaCalculateBO.calculate(kpi.getName(), "kpi", kpi.getAssessmentFormula(), "");
                    AlarmPlan alarmPlan = o_kpiBO.findAlarmPlanByKpiId(kpi.getId(), Contents.ALARMPLAN_REPORT);
                    DictEntry alarmStatus = this.o_kpiBO.findKpiAlarmStatusByValues(alarmPlan,Double.valueOf(valueStr));
                    kpiGatherResult.setAssessmentStatus(alarmStatus);
                    this.o_kpiBO.findValueChr(kpiGatherResult);
                    o_kpiGatherResultBO.mergeKpiGatherResult(kpiGatherResult);
                }*/
            }
        }

        result.put("success", true);
        return result;
    }

    /**
     * 工作流提交.
     * 
     * @param executionId
     */
    @RequestMapping("/kpi/kpi/jbpmKpiGatherResultSubmit.f")
    @Transactional
    public void jbpmKpiGatherResultSubmit(String executionId) {
        Map<String, Object> variables = new HashMap<String, Object>();
        o_jbpmBO.doProcessInstance(executionId, variables);
    }

    /**
     * 根据指标采集频率cron表达式计算下次执行时间.
     * 
     * @param kpi
     * @param type
     *            值类型
     * @return Date
     * @throws ParseException
     */
    public Date calculateNextExecuteTime(Kpi kpi, String type) throws ParseException {
        CronTrigger trigger = new CronTrigger();
        if ("targetValue".equals(type)) {
            if (StringUtils.isNotBlank(kpi.getTargetSetDayFormular())) {
                trigger.setCronExpression(kpi.getTargetSetDayFormular());
            }
            else {
                return null;
            }
        }
        else if ("finishValue".equals(type)) {
            if (StringUtils.isNotBlank(kpi.getGatherDayFormulr())) {
                trigger.setCronExpression(kpi.getGatherDayFormulr());
            }
            else {
                return null;
            }
        }
        AnnualCalendar cal = new AnnualCalendar();
        trigger.computeFirstFireTime(cal);
        return trigger.getNextFireTime();
    }

    private String getYear() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);

        return String.valueOf(year);
    }

    /**
     * 获取当前年度的前4个年度集合
     * 
     * @param String
     *            time 当前年度
     * @return String[]
     * @author 金鹏祥
     * */
    private String[] getYears(String year) {
        int g = 0;
        String[] str = new String[5];
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                str[i] = String.valueOf(year);
                g = Integer.parseInt(year);
            }
            else {
                g = g - 1;
                str[i] = String.valueOf(g);
            }
        }
        return str;
    }
}
