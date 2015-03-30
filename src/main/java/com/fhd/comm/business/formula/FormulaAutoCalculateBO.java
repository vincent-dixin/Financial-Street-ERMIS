package com.fhd.comm.business.formula;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.CronTrigger;
import org.quartz.impl.calendar.AnnualCalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.comm.business.TimePeriodBO;
import com.fhd.comm.entity.AlarmPlan;
import com.fhd.comm.entity.formula.FormulaLog;
import com.fhd.fdc.utils.Contents;
import com.fhd.jrj.business.JrjBO;
import com.fhd.kpi.business.KpiBO;
import com.fhd.kpi.business.KpiGatherResultBO;
import com.fhd.kpi.entity.Kpi;
import com.fhd.kpi.entity.KpiGatherResult;
import com.fhd.kpi.entity.KpiRelaOrgEmp;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * 公式每天定时计算.
 * 触发时间表达式为：0 0 0 5 * ? --每月5号凌晨00:00:00触发
 * @author   吴德福
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-12-05		下午03:36:35
 */
@Service
public class FormulaAutoCalculateBO {

    @Autowired
    private KpiBO o_kpiBO;

    @Autowired
    private KpiGatherResultBO o_kpiGatherResultBO;

    @Autowired
    private FormulaCalculateBO o_formulaCalculateBO;

    @Autowired
    private FormulaLogBO o_formulaLogBO;

    @Autowired
    private TimePeriodBO o_timePeriodBO;
    
    @Autowired
    private JrjBO o_jrjBO;

    /**
     * 定时触发计算公式.
     * @throws ParseException 
     */
    @Transactional
    public boolean formulaAutoCalculate() throws ParseException {
        /*
         * 指标
         */
        List<Kpi> kpiList = o_kpiBO.findAllKpiList();
        for (Kpi kpi : kpiList) {
            //1.根据指标频率是否需要计算当前指标
            if (findKpiFrequenceByKpi(kpi)) {
                System.out.println(".......指标'" + kpi.getName() + "'进行计算了........");
                
                //定义公式计算日志
                FormulaLog formulaLog = null;

                SysEmployee emp = null;
                Set<KpiRelaOrgEmp> kpiRelaOrgEmps = kpi.getKpiRelaOrgEmps();
                for (KpiRelaOrgEmp kpiRelaOrgEmp : kpiRelaOrgEmps) {
                    //所属人
                    if ("B".equals(kpiRelaOrgEmp.getType())) {
                        emp = kpiRelaOrgEmp.getEmp();
                        break;
                    }
                }

                //更新指标timeperiod标识位
                boolean computeFlag = false;
                //更新指标下次执行时间标识位
                boolean calculateTimeFlag = true;

                //根据指标id查询当期指标采集结果
                //KpiGatherResult currentKpiGatherResult = null;
                //List<KpiGatherResult> currentKpiGatherResults = o_kpiGatherResultBO.findCurrentKpiGatherResultByKpiIds(kpi.getId(), "");
                KpiGatherResult currentKpiGatherResult = o_kpiGatherResultBO.findCurrentKpiGatherResultByKpiId(kpi.getId(), "");
                //for (KpiGatherResult currentKpiGatherResult : currentKpiGatherResults) {
                	if (null != currentKpiGatherResult) {
                        String timePeriodId = "";
                        if (null != currentKpiGatherResult.getTimePeriod()) {
                            timePeriodId = currentKpiGatherResult.getTimePeriod().getId();
                        }
                        //目标值公式计算
                        if ("0sys_use_formular_formula".equals(kpi.getIsTargetFormula())) {
                            computeFlag = true;
                            if (null == currentKpiGatherResult.getTargetValue()) {
                                //创建公式日志实体
                                formulaLog = new FormulaLog();

                                String ret = o_formulaCalculateBO.calculate(kpi.getName(), "kpi", kpi.getTargetFormula(), timePeriodId);
                                if (o_formulaCalculateBO.isDoubleOrInteger(ret)) {
                                    //返回结果是double，计算正确
                                    currentKpiGatherResult.setTargetValue(Double.valueOf(ret));
                                    //保存公式日志实体
                                    o_formulaLogBO.saveFormulaLog(o_formulaLogBO.packageFormulaLog(formulaLog, kpi.getId(), kpi.getName(), "kpi",
                                            "targetValueFormula", kpi.getTargetFormula(), "success", ret, currentKpiGatherResult.getTimePeriod(), emp));
                                }
                                else {
                                	computeFlag = false;
                                    //返回结果是字符串描述，计算错误
                                    calculateTimeFlag = false;
                                    //保存公式日志实体
                                    o_formulaLogBO.saveFormulaLog(o_formulaLogBO.packageFormulaLog(formulaLog, kpi.getId(), kpi.getName(), "kpi",
                                            "targetValueFormula", kpi.getTargetFormula(), "failure", ret, currentKpiGatherResult.getTimePeriod(), emp));
                                }
                                System.out.println("目标值ret=" + ret);
                            }
                            else {
                                System.out.println(kpi.getName() + "当期目标值已存在，不进行计算!");
                            }
                        }
                        else {
                            //手动发送待办通知

                        }
                        //结果值公式计算
                        if ("0sys_use_formular_formula".equals(kpi.getIsResultFormula())) {
                            computeFlag = true;
                            if (null == currentKpiGatherResult.getFinishValue()) {
                                //创建公式日志实体
                                formulaLog = new FormulaLog();

                                String ret = o_formulaCalculateBO.calculate(kpi.getName(), "kpi", kpi.getResultFormula(), timePeriodId);
                                if (o_formulaCalculateBO.isDoubleOrInteger(ret)) {
                                    //返回结果是double，计算正确
                                    currentKpiGatherResult.setFinishValue(Double.valueOf(ret));
                                    //保存公式日志实体
                                    o_formulaLogBO.saveFormulaLog(o_formulaLogBO.packageFormulaLog(formulaLog, kpi.getId(), kpi.getName(), "kpi",
                                            "resultValueFormula", kpi.getResultFormula(), "success", ret, currentKpiGatherResult.getTimePeriod(), emp));
                                }
                                else {
                                	computeFlag = false;
                                    //返回结果是字符串描述，计算错误
                                    calculateTimeFlag = false;
                                    //保存公式日志实体
                                    o_formulaLogBO.saveFormulaLog(o_formulaLogBO.packageFormulaLog(formulaLog, kpi.getId(), kpi.getName(), "kpi",
                                            "resultValueFormula", kpi.getResultFormula(), "failure", ret, currentKpiGatherResult.getTimePeriod(), emp));
                                }
                                System.out.println("结果值ret=" + ret);
                            }
                            else {
                                System.out.println(kpi.getName() + "当期结果值已存在，不进行计算!");
                            }
                        }
                        else {
                            //手动发送待办通知

                        }
                        //评估值公式计算
                        if ("0sys_use_formular_formula".equals(kpi.getIsAssessmentFormula())) {
                            computeFlag = true;
                            //if (null == currentKpiGatherResult.getAssessmentValue()) {//jrj update
                                //创建公式日志实体
                                formulaLog = new FormulaLog();
                                String ret = o_formulaCalculateBO.calculate(kpi.getName(), "kpi", kpi.getAssessmentFormula(), timePeriodId);
                                if (o_formulaCalculateBO.isDoubleOrInteger(ret)) {
                                    //返回结果是double，计算正确
                                    currentKpiGatherResult.setAssessmentValue(NumberUtils.toDouble(ret));
                                    //计算预警状态
                                    AlarmPlan alarmPlan = o_kpiBO.findAlarmPlanByKpiId(kpi.getId(), Contents.ALARMPLAN_REPORT);
                                    if(null!=alarmPlan){
                                    	//DictEntry assessmentStatus = o_kpiBO.findKpiAssessmentStatusByAssessmentValue(alarmPlan, Double.valueOf(ret));
                                        DictEntry assessmentStatus = o_kpiBO.findKpiAlarmStatusByValues(alarmPlan, NumberUtils.toDouble(ret));//金融街修改
                                        currentKpiGatherResult.setAssessmentStatus(assessmentStatus);
                                    }
                                    //计算趋势
                                    DictEntry trend = o_kpiGatherResultBO.calculateTrendByAssessmentValue(currentKpiGatherResult);
                                    currentKpiGatherResult.setDirection(trend);
                                    
                                    
                                    //保存公式日志实体
                                    o_formulaLogBO.saveFormulaLog(o_formulaLogBO.packageFormulaLog(formulaLog, kpi.getId(), kpi.getName(), "kpi",
                                            "assessmentValueFormula", kpi.getAssessmentFormula(), "success", ret, currentKpiGatherResult.getTimePeriod(),
                                            emp));
                                }
                                else {
                                    computeFlag = false;
                                    //返回结果是字符串描述，计算错误
                                    calculateTimeFlag = false;
                                    //保存公式日志实体
                                    o_formulaLogBO.saveFormulaLog(o_formulaLogBO.packageFormulaLog(formulaLog, kpi.getId(), kpi.getName(), "kpi",
                                            "assessmentValueFormula", kpi.getAssessmentFormula(), "failure", ret, currentKpiGatherResult.getTimePeriod(),
                                            emp));
                                }
                                System.out.println("评估值ret=" + ret);
                            //}
                            /*else {
                                System.out.println(kpi.getName() + "当期评估值已存在，不进行计算!");
                            }*/
                        }
                        else {
                            //手动发送待办通知

                        }
                        //同比和环比
                        o_jrjBO.findValueChr(currentKpiGatherResult);//金融街修改
                        
                        //保存指标采集结果
                        o_kpiGatherResultBO.mergeKpiGatherResult(currentKpiGatherResult);
                        //更新指标timeperiod字段
                        if (computeFlag) {
                            //金融街暂时修改
                            /*if(null!=currentKpiGatherResult.getFinishValue()){
                                kpi.setLastTimePeriod(currentKpiGatherResult.getTimePeriod());
                                o_kpiBO.mergeKpi(kpi);
                            }*/
                            
                            
                            String kpiNewValue = o_kpiBO.getKpiNewValue(kpi.getId());
                            if (null != kpiNewValue) {
                                kpi.setLastTimePeriod(o_timePeriodBO.findTimePeriodById(kpiNewValue));
                                o_kpiBO.mergeKpi(kpi);
                            }
                        }
                        //更新指标下次执行时间
                        if (calculateTimeFlag) {
                            kpi.setCalculatetime(this.calculateNextExecuteTime(kpi));
                            o_kpiBO.mergeKpi(kpi);
                        }
                    }

                    /*
                     * 累积值公式计算--包括:季度累积值计算和年度累积值计算
                     */
                    //季度累积值计算--只有月指标使用
                    //根据指标id查询当期所在季度的指标采集结果
                    KpiGatherResult quarterKpiGatherResult = o_kpiGatherResultBO.findQuarterKpiGatherResultByKpiId(kpi.getId(), "");
                    if (null != quarterKpiGatherResult) {
                        if ("0frequecy_month".equals(kpi.getGatherFrequence().getId())) {
                            if (!"kpi_sum_measure_manual".equals(kpi.getTargetSumMeasure().getId())) {
                                Double targetValueSum = o_kpiGatherResultBO.calculateQuarterAccumulatedValueByCalculateItem(currentKpiGatherResult,
                                        "targetValueSum", kpi.getTargetSumMeasure().getId());
                                quarterKpiGatherResult.setTargetValue(targetValueSum);
                            }
                            if (!"kpi_sum_measure_manual".equals(kpi.getResultSumMeasure().getId())) {
                                Double resultValueSum = o_kpiGatherResultBO.calculateQuarterAccumulatedValueByCalculateItem(currentKpiGatherResult,
                                        "resultValueSum", kpi.getResultSumMeasure().getId());
                                quarterKpiGatherResult.setFinishValue(resultValueSum);
                            }
                            if (!"kpi_sum_measure_manual".equals(kpi.getAssessmentSumMeasure().getId())) {
                                Double assessmentValueSum = o_kpiGatherResultBO.calculateQuarterAccumulatedValueByCalculateItem(currentKpiGatherResult,
                                        "assessmentValueSum", kpi.getAssessmentSumMeasure().getId());
                                quarterKpiGatherResult.setAssessmentValue(assessmentValueSum);
                                //计算预警状态
                                AlarmPlan alarmPlan = o_kpiBO.findAlarmPlanByKpiId(kpi.getId(), Contents.ALARMPLAN_REPORT);
                                DictEntry assessmentStatus = o_kpiBO.findKpiAlarmStatusByValues(alarmPlan, assessmentValueSum);
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
                    KpiGatherResult yearKpiGatherResult = o_kpiGatherResultBO.findYearKpiGatherResultByKpiId(kpi.getId(), "");
                    if (null != yearKpiGatherResult) {
                        if ("0frequecy_year".equals(kpi.getGatherFrequence().getId())) {
                            System.out.println("年度指标不需要进行年度累积值计算");
                        }
                        else {
                            if (!"kpi_sum_measure_manual".equals(kpi.getTargetSumMeasure().getId())) {
                                Double targetValueSum = o_kpiGatherResultBO.calculateYearAccumulatedValueByCalculateItem(currentKpiGatherResult,
                                        "targetValueSum", kpi.getTargetSumMeasure().getId());
                                yearKpiGatherResult.setTargetValue(targetValueSum);
                            }
                            if (!"kpi_sum_measure_manual".equals(kpi.getResultSumMeasure().getId())) {
                                Double resultValueSum = o_kpiGatherResultBO.calculateYearAccumulatedValueByCalculateItem(currentKpiGatherResult,
                                        "resultValueSum", kpi.getResultSumMeasure().getId());
                                yearKpiGatherResult.setFinishValue(resultValueSum);
                            }
                            if (!"kpi_sum_measure_manual".equals(kpi.getAssessmentSumMeasure().getId())) {
                                Double assessmentValueSum = o_kpiGatherResultBO.calculateYearAccumulatedValueByCalculateItem(currentKpiGatherResult,
                                        "assessmentValueSum", kpi.getAssessmentSumMeasure().getId());
                                yearKpiGatherResult.setAssessmentValue(assessmentValueSum);
                                //计算预警状态  
                                AlarmPlan alarmPlan = o_kpiBO.findAlarmPlanByKpiId(kpi.getId(), Contents.ALARMPLAN_REPORT);
                                if(null!=alarmPlan){
                                    DictEntry assessmentStatus = o_kpiBO.findKpiAlarmStatusByValues(alarmPlan, assessmentValueSum);
                                    yearKpiGatherResult.setAssessmentStatus(assessmentStatus);
                                }
                                //计算趋势
                                DictEntry trend = o_kpiGatherResultBO.calculateTrendByAssessmentValue(yearKpiGatherResult);
                                yearKpiGatherResult.setDirection(trend);
                            }
                            //保存指标采集结果年度累积值
                            o_kpiGatherResultBO.saveKpiGatherResult(yearKpiGatherResult);
                        }
                    }
				}
                
            //}
            else {
                //System.out.println("指标'"+kpi.getName()+"'今天不需要计算");
            }
        }
        /*
         * 风险
         */

        return true;
    }

    /**
     * 根据指标频率是否需要计算当前指标.
     * @param kpi
     * @return boolean true 需要计算;false 不需要计算;
     */
    private boolean findKpiFrequenceByKpi(Kpi kpi) {
        /*if (null == kpi.getCalculatetime() || null == kpi.getResultCollectInterval()) {
            return false;
        }
        Date calculateDate = kpi.getCalculatetime();
        Date extensionDate = DateUtils.addDays(calculateDate, kpi.getResultCollectInterval());

        Date currentDate = new Date();

        if (currentDate.after(calculateDate) && currentDate.before(extensionDate)) {
            return true;
        }
        return false;*/
        //金融街暂时修改
        return true;
    }

    /**
     * 根据指标采集频率cron表达式计算下次执行时间.
     * @param kpi
     * @return Date
     * @throws ParseException
     */
    public Date calculateNextExecuteTime(Kpi kpi) throws ParseException {
        CronTrigger trigger = new CronTrigger();
        if (StringUtils.isNotBlank(kpi.getGatherDayFormulr())) {
            trigger.setCronExpression(kpi.getGatherDayFormulr());
        }
        else {
            return null;
        }
        AnnualCalendar cal = new AnnualCalendar();
        // exclude July 4th
        /*
        Calendar gCal = GregorianCalendar.getInstance();   
        gCal.set(Calendar.MONTH, Calendar.JULY);   
        gCal.set(Calendar.DATE, 4);   
        cal.setDayExcluded(gCal, true);   
        */
        trigger.computeFirstFireTime(cal);
        return trigger.getNextFireTime();
    }
}
