package com.fhd.comm.web.controller.formula;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.comm.business.CategoryBO;
import com.fhd.comm.business.TimePeriodBO;
import com.fhd.comm.business.formula.FormulaCalculateBO;
import com.fhd.comm.business.formula.FormulaLogBO;
import com.fhd.comm.entity.AlarmPlan;
import com.fhd.comm.entity.AlarmRegion;
import com.fhd.comm.entity.Category;
import com.fhd.comm.entity.formula.FormulaLog;
import com.fhd.core.dao.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.fdc.utils.Contents;
import com.fhd.kpi.business.KpiBO;
import com.fhd.kpi.business.KpiGatherResultBO;
import com.fhd.kpi.business.RelaAssessResultBO;
import com.fhd.kpi.business.StrategyMapBO;
import com.fhd.kpi.entity.Kpi;
import com.fhd.kpi.entity.KpiGatherResult;
import com.fhd.kpi.entity.KpiRelaOrgEmp;
import com.fhd.kpi.entity.RelaAssessResult;
import com.fhd.kpi.entity.StrategyMap;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * 公式计算日志Controller.
 * @author   吴德福
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-8		上午10:51:15
 * @see 	 
 */
@Controller
public class FormulaLogControl {

    @Autowired
    private FormulaLogBO o_formulaLogBO;

    @Autowired
    private KpiBO o_kpiBO;

    @Autowired
    private KpiGatherResultBO o_kpiGatherResultBO;

    @Autowired
    private FormulaCalculateBO o_formulaCalculateBO;

    @Autowired
    private TimePeriodBO o_timePeriodBO;

    @Autowired
    private RelaAssessResultBO o_relaAssessResultBO;

    @Autowired
    private StrategyMapBO o_strategyMapBO;

    @Autowired
    private CategoryBO o_categoryBO;

    /**
     * 公式计算日志列表.
     * @author 吴德福
     * @param limit
     * @param start
     * @param sort
     * @param dir
     * @param query 查询条件
     * @return Map<String,Object>
     */
    @ResponseBody
    @RequestMapping("/formula/findFormulaLogList.f")
    public Map<String, Object> findFormulaLogList(int limit, int start, String sort, String dir, String query) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();

        Page<FormulaLog> page = new Page<FormulaLog>();
        page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
        page.setPageSize(limit);
        page = o_formulaLogBO.findFormulaLogListByPage(page, sort, dir, query);
        List<FormulaLog> formulaLogList = page.getResult();
        if (null != formulaLogList && formulaLogList.size() > 0) {
            Map<String, Object> row = null;
            for (FormulaLog fl : formulaLogList) {
                row = new HashMap<String, Object>();
                //id
                row.put("id", fl.getId());
                //对象id
                row.put("objectId", fl.getObjectId());
                //对象名称
                row.put("objectName", fl.getObjectName());
                //对象类型
                row.put("objectType", fl.getObjectType());
                //对象所属列
                row.put("objectColumn", fl.getObjectColumn());
                //公式
                row.put("formulaContent", fl.getFormulaContent());
                //计算结果类型
                row.put("failureType", fl.getFailureType());
                //成功或失败原因
                row.put("failureReason", fl.getFailureReason());
                //计算日期
                row.put("calculateDate", DateUtils.formatDate(fl.getCalculateDate(), "yyyy-MM-dd HH:mm:ss"));
                //所属人
                if (null != fl.getEmp()) {
                    row.put("empName", fl.getEmp().getEmpname());
                }
                else {
                    row.put("empName", "");
                }
                //时间区间维度id
                if (null != fl.getTimePeriod()) {
                    row.put("timePeriodId", fl.getTimePeriod().getId());
                }
                else {
                    row.put("timePeriodId", "");
                }

                datas.add(row);
            }
            map.put("datas", datas);
            map.put("totalCount", page.getTotalItems());
        }
        else {
            map.put("datas", new Object[0]);
            map.put("totalCount", "0");
        }
        return map;
    }

    /**
     * 重新计算.
     * @param id 公式日志id
     * @param objectId 对象id
     * @param objectType 对象类型
     * @param objectColumn 对象所属列
     * @param timePeriodId 时间区间维id
     * @param formula 公式
     * @param response
     * @throws IOException 
     */
    @RequestMapping("/formula/reCalculateFormula.f")
    public void reCalculateFormula(String id, String objectId, String objectType, String objectColumn, String timePeriodId, String formula,
            HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        //定义公式计算日志
        FormulaLog formulaLog = null;

        try {
            if (Contents.OBJECT_KPI.equals(objectType)) {
                //指标
                Kpi kpi = o_kpiBO.findKpiById(objectId);
                //根据指标id查询当期指标采集结果--加入时间区间维参数timePeriodId
                KpiGatherResult currentKpiGatherResult = o_kpiGatherResultBO.findCurrentKpiGatherResultByKpiId(kpi.getId(), timePeriodId);
                if (null != currentKpiGatherResult) {

                    //创建公式日志实体
                    formulaLog = new FormulaLog();

                    SysEmployee emp = null;
                    Set<KpiRelaOrgEmp> kpiRelaOrgEmps = kpi.getKpiRelaOrgEmps();
                    for (KpiRelaOrgEmp kpiRelaOrgEmp : kpiRelaOrgEmps) {
                        //所属人
                        if (Contents.BELONGDEPARTMENT.equals(kpiRelaOrgEmp.getType())) {
                            emp = kpiRelaOrgEmp.getEmp();
                            break;
                        }
                    }

                    //更新指标timeperiod标识位
                    boolean computeFlag = false;

                    //目标值公式计算
                    if (Contents.TARGET_VALUE_FORMULA.equals(objectColumn)) {
                        computeFlag = true;
                        String ret = o_formulaCalculateBO.calculate(kpi.getName(), Contents.OBJECT_KPI, kpi.getTargetFormula(), timePeriodId);
                        if (o_formulaCalculateBO.isDoubleOrInteger(ret)) {
                            //返回结果是double，计算正确
                            currentKpiGatherResult.setTargetValue(Double.valueOf(ret));
                            //保存指标采集结果
                            o_kpiGatherResultBO.saveKpiGatherResult(currentKpiGatherResult);
                            //保存公式日志实体
                            o_formulaLogBO.saveFormulaLog(o_formulaLogBO.packageFormulaLog(formulaLog, kpi.getId(), kpi.getName(),
                                    Contents.OBJECT_KPI, Contents.TARGET_VALUE_FORMULA, kpi.getTargetFormula(), Contents.OPERATE_SUCCESS, ret,
                                    currentKpiGatherResult.getTimePeriod(), emp));
                            //更新指标timeperiod字段
                            if (computeFlag) {
                                //kpi.setLastTimePeriod(currentKpiGatherResult.getTimePeriod());
                                //kpi.setLastTimePeriod(o_timePeriodBO.findTimePeriodById(o_kpiBO.getKpiNewValue(kpi.getId())));
                                String kpiNewValue = o_kpiBO.getKpiNewValue(kpi.getId());
                                if (null != kpiNewValue) {
                                    kpi.setLastTimePeriod(o_timePeriodBO.findTimePeriodById(kpiNewValue));
                                    o_kpiBO.mergeKpi(kpi);
                                }
                            }
                        }
                        else {
                            //返回结果是字符串描述，计算错误
                            //保存公式日志实体
                            o_formulaLogBO.saveFormulaLog(o_formulaLogBO.packageFormulaLog(formulaLog, kpi.getId(), kpi.getName(),
                                    Contents.OBJECT_KPI, Contents.TARGET_VALUE_FORMULA, kpi.getTargetFormula(), Contents.OPERATE_FAILURE, ret,
                                    currentKpiGatherResult.getTimePeriod(), emp));
                        }
                    }
                    else {
                        //手动发送待办通知

                    }
                    //结果值公式计算
                    if (Contents.RESULT_VALUE_FORMULA.equals(objectColumn)) {
                        computeFlag = true;
                        String ret = o_formulaCalculateBO.calculate(kpi.getName(), Contents.OBJECT_KPI, kpi.getResultFormula(), timePeriodId);
                        if (o_formulaCalculateBO.isDoubleOrInteger(ret)) {
                            //返回结果是double，计算正确
                            currentKpiGatherResult.setFinishValue(Double.valueOf(ret));
                            //保存指标采集结果
                            o_kpiGatherResultBO.saveKpiGatherResult(currentKpiGatherResult);
                            //保存公式日志实体
                            o_formulaLogBO.saveFormulaLog(o_formulaLogBO.packageFormulaLog(formulaLog, kpi.getId(), kpi.getName(),
                                    Contents.OBJECT_KPI, Contents.RESULT_VALUE_FORMULA, kpi.getResultFormula(), Contents.OPERATE_SUCCESS, ret,
                                    currentKpiGatherResult.getTimePeriod(), emp));
                            //更新指标timeperiod字段
                            if (computeFlag) {
                                //kpi.setLastTimePeriod(currentKpiGatherResult.getTimePeriod());
                                //kpi.setLastTimePeriod(o_timePeriodBO.findTimePeriodById(o_kpiBO.getKpiNewValue(kpi.getId())));
                                String kpiNewValue = o_kpiBO.getKpiNewValue(kpi.getId());
                                if (null != kpiNewValue) {
                                    kpi.setLastTimePeriod(o_timePeriodBO.findTimePeriodById(kpiNewValue));
                                    o_kpiBO.mergeKpi(kpi);
                                }
                            }
                        }
                        else {
                            //返回结果是字符串描述，计算错误
                            //保存公式日志实体
                            o_formulaLogBO.saveFormulaLog(o_formulaLogBO.packageFormulaLog(formulaLog, kpi.getId(), kpi.getName(),
                                    Contents.OBJECT_KPI, Contents.RESULT_VALUE_FORMULA, kpi.getResultFormula(), Contents.OPERATE_FAILURE, ret,
                                    currentKpiGatherResult.getTimePeriod(), emp));
                        }
                    }
                    else {
                        //手动发送待办通知

                    }
                    //评估值公式计算
                    if (Contents.ASSESSMENT_VALUE_FORMULA.equals(objectColumn)) {
                        computeFlag = true;
                        String ret = o_formulaCalculateBO.calculate(kpi.getName(), Contents.OBJECT_KPI, kpi.getAssessmentFormula(), timePeriodId);
                        if (o_formulaCalculateBO.isDoubleOrInteger(ret)) {
                            //返回结果是double，计算正确
                            currentKpiGatherResult.setAssessmentValue(Double.valueOf(ret));
                            //计算预警状态
                            AlarmPlan alarmPlan = o_kpiBO.findAlarmPlanByKpiId(kpi.getId(), Contents.ALARMPLAN_REPORT);
                            DictEntry assessmentStatus = o_kpiBO.findKpiAssessmentStatusByAssessmentValue(alarmPlan, Double.valueOf(ret));
                            currentKpiGatherResult.setAssessmentStatus(assessmentStatus);
                            //currentKpiGatherResult.setAssessmentAlarm(alarmPlan);
                            //计算趋势
                            DictEntry trend = o_kpiGatherResultBO.calculateTrendByAssessmentValue(currentKpiGatherResult);
                            currentKpiGatherResult.setDirection(trend);
                            //保存指标采集结果
                            o_kpiGatherResultBO.saveKpiGatherResult(currentKpiGatherResult);
                            //保存公式日志实体
                            o_formulaLogBO.saveFormulaLog(o_formulaLogBO.packageFormulaLog(formulaLog, kpi.getId(), kpi.getName(),
                                    Contents.OBJECT_KPI, Contents.ASSESSMENT_VALUE_FORMULA, kpi.getAssessmentFormula(), Contents.OPERATE_SUCCESS,
                                    ret, currentKpiGatherResult.getTimePeriod(), emp));
                            //更新指标timeperiod字段
                            if (computeFlag) {
                                //kpi.setLastTimePeriod(currentKpiGatherResult.getTimePeriod());
                                //kpi.setLastTimePeriod(o_timePeriodBO.findTimePeriodById(o_kpiBO.getKpiNewValue(kpi.getId())));
                                String kpiNewValue = o_kpiBO.getKpiNewValue(kpi.getId());
                                if (null != kpiNewValue) {
                                    kpi.setLastTimePeriod(o_timePeriodBO.findTimePeriodById(kpiNewValue));
                                    o_kpiBO.mergeKpi(kpi);
                                }
                            }
                        }
                        else {
                            //返回结果是字符串描述，计算错误
                            //保存公式日志实体
                            o_formulaLogBO.saveFormulaLog(o_formulaLogBO.packageFormulaLog(formulaLog, kpi.getId(), kpi.getName(),
                                    Contents.OBJECT_KPI, Contents.ASSESSMENT_VALUE_FORMULA, kpi.getAssessmentFormula(), Contents.OPERATE_FAILURE,
                                    ret, currentKpiGatherResult.getTimePeriod(), emp));
                        }
                    }
                    else {
                        //手动发送待办通知

                    }
                }

                /*
                 * 累积值公式计算--包括:季度累积值计算和年度累积值计算
                 */
                //季度累积值计算--只有月指标使用
                //根据指标id查询当期所在季度的指标采集结果
                KpiGatherResult quarterKpiGatherResult = o_kpiGatherResultBO.findQuarterKpiGatherResultByKpiId(kpi.getId(), timePeriodId);
                if (null != quarterKpiGatherResult) {
                    if (Contents.FREQUECY_MONTH.equals(kpi.getGatherFrequence().getId())) {
                        if (!Contents.SUM_MEASURE_MANUAL.equals(kpi.getTargetSumMeasure().getId())) {
                            Double targetValueSum = o_kpiGatherResultBO.calculateQuarterAccumulatedValueByCalculateItem(currentKpiGatherResult,
                                    Contents.TARGET_VALUE_SUM, kpi.getTargetSumMeasure().getId());
                            quarterKpiGatherResult.setTargetValue(targetValueSum);
                        }
                        if (!Contents.SUM_MEASURE_MANUAL.equals(kpi.getResultSumMeasure().getId())) {
                            Double resultValueSum = o_kpiGatherResultBO.calculateQuarterAccumulatedValueByCalculateItem(currentKpiGatherResult,
                                    Contents.RESULT_VALUE_SUM, kpi.getResultSumMeasure().getId());
                            quarterKpiGatherResult.setFinishValue(resultValueSum);
                        }
                        if (!Contents.SUM_MEASURE_MANUAL.equals(kpi.getAssessmentSumMeasure().getId())) {
                            Double assessmentValueSum = o_kpiGatherResultBO.calculateQuarterAccumulatedValueByCalculateItem(currentKpiGatherResult,
                                    Contents.ASSESSMENT_VALUE_SUM, kpi.getAssessmentSumMeasure().getId());
                            quarterKpiGatherResult.setAssessmentValue(assessmentValueSum);
                            //计算告警状态
                            AlarmPlan alarmPlan = o_kpiBO.findAlarmPlanByKpiId(kpi.getId(), Contents.ALARMPLAN_REPORT);
                            DictEntry assessmentStatus = o_kpiBO.findKpiAssessmentStatusByAssessmentValue(alarmPlan, assessmentValueSum);
                            quarterKpiGatherResult.setAssessmentStatus(assessmentStatus);
                            //计算趋势
                            DictEntry trend = o_kpiGatherResultBO.calculateTrendByAssessmentValue(quarterKpiGatherResult);
                            quarterKpiGatherResult.setDirection(trend);
                        }
                        //保存指标采集结果季度累积值
                        o_kpiGatherResultBO.saveKpiGatherResult(quarterKpiGatherResult);
                    }
                    else {
                        System.out.println("只有月指标需要进行季度累积值计算");
                    }
                }

                //年度累积值计算--只有年度指标不使用
                //根据指标id查询当期所在年度的指标采集结果
                KpiGatherResult yearKpiGatherResult = o_kpiGatherResultBO.findYearKpiGatherResultByKpiId(kpi.getId(), timePeriodId);
                if (null != yearKpiGatherResult) {
                    if (Contents.FREQUECY_YEAR.equals(kpi.getGatherFrequence().getId())) {
                        System.out.println("年度指标不需要进行年度累积值计算");
                    }
                    else {
                        if (!Contents.SUM_MEASURE_MANUAL.equals(kpi.getTargetSumMeasure().getId())) {
                            Double targetValueSum = o_kpiGatherResultBO.calculateYearAccumulatedValueByCalculateItem(currentKpiGatherResult,
                                    Contents.TARGET_VALUE_SUM, kpi.getTargetSumMeasure().getId());
                            yearKpiGatherResult.setTargetValue(targetValueSum);
                        }
                        if (!Contents.SUM_MEASURE_MANUAL.equals(kpi.getResultSumMeasure().getId())) {
                            Double resultValueSum = o_kpiGatherResultBO.calculateYearAccumulatedValueByCalculateItem(currentKpiGatherResult,
                                    Contents.RESULT_VALUE_SUM, kpi.getResultSumMeasure().getId());
                            yearKpiGatherResult.setFinishValue(resultValueSum);
                        }
                        if (!Contents.SUM_MEASURE_MANUAL.equals(kpi.getAssessmentSumMeasure().getId())) {
                            Double assessmentValueSum = o_kpiGatherResultBO.calculateYearAccumulatedValueByCalculateItem(currentKpiGatherResult,
                                    Contents.ASSESSMENT_VALUE_SUM, kpi.getAssessmentSumMeasure().getId());
                            yearKpiGatherResult.setAssessmentValue(assessmentValueSum);
                            //计算预警状态  
                            AlarmPlan alarmPlan = o_kpiBO.findAlarmPlanByKpiId(kpi.getId(), Contents.ALARMPLAN_FORECAST);
                            DictEntry assessmentStatus = o_kpiBO.findKpiAssessmentStatusByAssessmentValue(alarmPlan, assessmentValueSum);
                            yearKpiGatherResult.setAssessmentStatus(assessmentStatus);
                            //计算趋势
                            DictEntry trend = o_kpiGatherResultBO.calculateTrendByAssessmentValue(yearKpiGatherResult);
                            yearKpiGatherResult.setDirection(trend);
                        }
                        //保存指标采集结果年度累积值
                        o_kpiGatherResultBO.saveKpiGatherResult(yearKpiGatherResult);
                    }
                }
            }
            else if ("category".equals(objectType) || "strategy".equals(objectType)) {
                String type = "sc";
                String formular = "";
                AlarmPlan alarmPlan = null;
                Date currentDate = new Date();
                if ("strategy".equals(objectType)) {
                    StrategyMap strategyMap = this.o_strategyMapBO.findById(objectId);
                    alarmPlan = this.o_strategyMapBO.findAlarmPlanBySmId(objectId, Contents.ALARMPLAN_REPORT);
                    formular = strategyMap.getAssessmentFormula();
                    type = "str";
                }
                else if ("category".equals(objectType)) {
                    Category category = o_categoryBO.findCategoryById(objectId);
                    alarmPlan = o_categoryBO.findAlarmPlanByScId(objectId, Contents.ALARMPLAN_REPORT);
                    formular = category.getAssessmentFormula();
                    type = "sc";
                }
                if(StringUtils.isNotBlank(formular)){
                    RelaAssessResult currentRelaAssessResult = o_relaAssessResultBO.findCurrentRelaAssessResultByType(objectId, type, currentDate);
                    String result = o_formulaCalculateBO.calculateCategory(objectId, objectType, formular, "");
                    if (o_formulaCalculateBO.isDoubleOrInteger(result)) {
                        Double assessValue = Double.valueOf(result);
                        DictEntry cssicon = null;
                        if(null!=alarmPlan){
                            Set<AlarmRegion> AlarmRegionList = alarmPlan.getAlarmRegions();
                            for (AlarmRegion alarmRegion : AlarmRegionList) {
                                if (assessValue >= Double.valueOf(alarmRegion.getMinValue()) && assessValue <= Double.valueOf(alarmRegion.getMaxValue())) {
                                    cssicon = alarmRegion.getAlarmIcon();
                                    break;
                                }
                            }
                            currentRelaAssessResult.setAssessmentStatus(cssicon);
                        }
                        currentRelaAssessResult.setAssessmentValue(assessValue);
                        o_relaAssessResultBO.saveRelaAssessResult(currentRelaAssessResult);
                    }
                    
                }

            }else if ("risk".equals(objectType)) {
                //风险

            }
            out.print("true");
        }
        catch (Exception e) {
            e.printStackTrace();
            out.print("false");
        }
        finally {
            if (null != out) {
                out.close();
            }
        }
    }

    /**
     * 单条删除公式日志.
     * @param id 公式日志id
     * @param response
     * @throws IOException 
     */
    @RequestMapping("/formula/removeFormulaLogById.f")
    public void removeFormulaLogById(String id, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        try {
            if (StringUtils.isNotBlank(id)) {
                o_formulaLogBO.removeFormulaLogById(id);
            }

            out.print("true");
        }
        catch (Exception e) {
            e.printStackTrace();
            out.print("false");
        }
        finally {
            if (null != out) {
                out.close();
            }
        }
    }

    /**
     * 批量删除公式日志.
     * @param ids 对象id集合
     * @param response
     * @throws IOException 
     */
    @RequestMapping("/formula/removeFormulaLogByIds.f")
    public void removeFormulaLogByIds(String ids, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        try {
            if (StringUtils.isNotBlank(ids)) {
                o_formulaLogBO.removeFormulaLogByIds(ids);
            }

            out.print("true");
        }
        catch (Exception e) {
            e.printStackTrace();
            out.print("false");
        }
        finally {
            if (null != out) {
                out.close();
            }
        }
    }
}
