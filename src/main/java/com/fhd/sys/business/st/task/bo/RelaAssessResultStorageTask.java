package com.fhd.sys.business.st.task.bo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.comm.business.CategoryBO;
import com.fhd.comm.business.TimePeriodBO;
import com.fhd.comm.business.formula.FormulaCalculateBO;
import com.fhd.comm.business.formula.FormulaLogBO;
import com.fhd.comm.entity.AlarmPlan;
import com.fhd.comm.entity.AlarmRegion;
import com.fhd.comm.entity.Category;
//import com.fhd.comm.entity.CategoryRelaAlarm;
import com.fhd.comm.entity.TimePeriod;
import com.fhd.comm.entity.formula.FormulaLog;
import com.fhd.fdc.utils.Contents;
import com.fhd.kpi.business.KpiGatherResultBO;
import com.fhd.kpi.business.RelaAssessResultBO;
import com.fhd.kpi.business.StrategyMapBO;
import com.fhd.kpi.entity.RelaAssessResult;
//import com.fhd.kpi.entity.SmRelaAlarm;
import com.fhd.kpi.entity.StrategyMap;
import com.fhd.sys.entity.dic.DictEntry;

@Service
public class RelaAssessResultStorageTask {

    @Autowired
    private CategoryBO o_categoryBO;

    @Autowired
    private StrategyMapBO o_strategyMapBO;

    @Autowired
    private RelaAssessResultBO o_relaAssessResultBO;

    @Autowired
    private KpiGatherResultBO o_kpiGatherResultBO;

    @Autowired
    private FormulaCalculateBO o_formulaCalculateBO;

    @Autowired
    private FormulaLogBO o_formulaLogBO;

    @Autowired
    private TimePeriodBO o_timePeriodBO;

    private HashMap<String, TimePeriod> TimePeriodMap;//时间MapAll

    private List<AlarmRegion> AlarmRegionListAll;//告警图标

    private final String categoryType = "sc";

    private final String strategyMapType = "str";

    private final String monthType = "mm";

    /**
     * 计算记分卡评估值存储服务
     * 
     * @author 金鹏祥
     * @return
    */
    @Transactional
    public boolean categoryAssessedService() {
        this.assessedService(new Category());
        return true;
    }

    /**
     * 计算战略目标评估值存储服务
     * 
     * @author 金鹏祥
     * @return
    */
    @Transactional
    public boolean strategyMapAssessedService() {
        this.assessedService(new StrategyMap());
        return true;
    }

    /**
     * 评估值处理服务
     * 
     * @author 金鹏祥
     * @param obj
     * @return
    */
    private void assessedService(Object obj) {
        Double assessValue = 0.0d;
        String ret = "";
        TimePeriodMap = o_kpiGatherResultBO.findTimePeriodAllMap();
        AlarmRegionListAll = o_categoryBO.findAlarmRegionAll();
        FormulaLog formulaLog = new FormulaLog();
        TimePeriod timePeriod = o_timePeriodBO.findTimePeriodBySome("");
        Date currentDate = new Date();
        if (obj instanceof Category) {
            List<Category> categoryListAll = o_categoryBO.findCategoryAll();
            for (Category category : categoryListAll) {
                RelaAssessResult currentRelaAssessResult = o_relaAssessResultBO
                        .findCurrentRelaAssessResultByType(category.getId(), "sc", currentDate);
                System.out.println(category.getAssessmentFormula() + "======================================");
                System.out.println("=================name======"+category.getName());
                //评估值计算公式
                if (StringUtils.isNotBlank(category.getAssessmentFormula())) {
                    //ret = o_formulaCalculateBO.calculateCategory(category.getId(), "category", category.getAssessmentFormula(), "");
                    ret = o_formulaCalculateBO.calculate(category.getName(), "category", category.getAssessmentFormula(), "");
                    /*if("品牌风险".equals(category.getName())){
                        ret = "87";//暂无数据的空灯指标，计算规则时按绿灯计算
                    }*/
                    if (o_formulaCalculateBO.isDoubleOrInteger(ret)) {
                        //返回结果是double，计算正确
                        assessValue = NumberUtils.toDouble(ret);
                        this.saveRelaAssessResult(currentRelaAssessResult, category.getId(), category.getName(), assessValue, category.getId(), obj);
                        o_formulaLogBO.saveFormulaLog(o_formulaLogBO.packageFormulaLog(formulaLog, category.getId(), category.getName(), "category",
                                Contents.SC_ASSESSMENT_VALUE_FORMULA, category.getAssessmentFormula(), "success", ret, timePeriod, null));
                    }
                    else {
                        //返回结果是字符串描述，计算错误
                        o_formulaLogBO.saveFormulaLog(o_formulaLogBO.packageFormulaLog(formulaLog, category.getId(), category.getName(), "category",
                                Contents.SC_ASSESSMENT_VALUE_FORMULA, category.getAssessmentFormula(), "failure", ret, timePeriod, null));
                    }
                }
            }
        }
        else if (obj instanceof StrategyMap) {
            List<StrategyMap> strategyMapListAll = o_strategyMapBO.findStrategyMapAll();
            for (StrategyMap strategyMap : strategyMapListAll) {
                //评估值计算公式
                RelaAssessResult currentRelaAssessResult = o_relaAssessResultBO.findCurrentRelaAssessResultByType(strategyMap.getId(), "str",
                        currentDate);
                if (StringUtils.isNotBlank(strategyMap.getAssessmentFormula())) {
                    System.out.println(strategyMap.getAssessmentFormula() + "======================================");
                    ret = o_formulaCalculateBO.calculateCategory(strategyMap.getId(), "strategy", strategyMap.getAssessmentFormula(), "");
                    if (o_formulaCalculateBO.isDoubleOrInteger(ret)) {
                        //返回结果是double，计算正确
                        assessValue = Double.valueOf(ret);
                        this.saveRelaAssessResult(currentRelaAssessResult, strategyMap.getId(), strategyMap.getName(), assessValue,
                                strategyMap.getId(), obj);
                        o_formulaLogBO.saveFormulaLog(o_formulaLogBO.packageFormulaLog(formulaLog, strategyMap.getId(), strategyMap.getName(),
                                "strategy", Contents.SM_ASSESSMENT_VALUE_FORMULA, strategyMap.getAssessmentFormula(), "success", ret, timePeriod,
                                null));
                    }
                    else {
                        //返回结果是字符串描述，计算错误
                        o_formulaLogBO.saveFormulaLog(o_formulaLogBO.packageFormulaLog(formulaLog, strategyMap.getId(), strategyMap.getName(),
                                "strategy", Contents.SM_ASSESSMENT_VALUE_FORMULA, strategyMap.getAssessmentFormula(), "failure", ret, timePeriod,
                                null));
                    }
                }
            }
        }
    }

    /**
     * 存储评估值及对应类型
     * 
     * @author 金鹏祥
     * @param code
     * @param name
     * @param assessValue
    */
    private void saveRelaAssessResult(RelaAssessResult assessResult, String code, String name, Double assessValue, String id, Object obj) {
        String alarmIcon = "";
        DictEntry trendDict = null;
        RelaAssessResult relaAssessResult = new RelaAssessResult();
        DictEntry dictEntry = new DictEntry();
        TimePeriod timePeriod = new TimePeriod();

        relaAssessResult.setId(assessResult.getId());
        relaAssessResult.setObjectId(code);
        relaAssessResult.setObjectName(name);
        timePeriod.setId(this.getYear() + monthType + this.getMonth());
        relaAssessResult.setTimePeriod(timePeriod);
        relaAssessResult.setBeginTime(TimePeriodMap.get(relaAssessResult.getTimePeriod().getId()).getStartTime());
        relaAssessResult.setEndTime(TimePeriodMap.get(relaAssessResult.getTimePeriod().getId()).getEndTime());
        relaAssessResult.setAssessmentValue(assessValue);
        trendDict = this.o_relaAssessResultBO.calculateTrendByAssessmentValue(assessResult);
        relaAssessResult.setTrend(trendDict);
        if (obj instanceof Category) {
            relaAssessResult.setDataType(categoryType);
            AlarmPlan alarmPlan = o_categoryBO.findAlarmPlanByScId(id, Contents.ALARMPLAN_REPORT);
            if (alarmPlan != null) {
                relaAssessResult.setAssessmentAlarmPlan(alarmPlan);
                alarmIcon = this.getStatusCssIcon(alarmPlan.getId(), assessValue);
                if (!"".equalsIgnoreCase(alarmIcon)) {
                    dictEntry.setId(alarmIcon);
                    relaAssessResult.setAssessmentStatus(dictEntry);
                }
            }
        }
        else if (obj instanceof StrategyMap) {
            relaAssessResult.setDataType(strategyMapType);
            AlarmPlan alarmPlan = o_strategyMapBO.findAlarmPlanBySmId(id, Contents.ALARMPLAN_REPORT);
            if (alarmPlan != null) {
                relaAssessResult.setAssessmentAlarmPlan(alarmPlan);
                alarmIcon = this.getStatusCssIcon(alarmPlan.getId(), assessValue);
                if (!"".equalsIgnoreCase(alarmIcon)) {
                    dictEntry.setId(alarmIcon);
                    relaAssessResult.setAssessmentStatus(dictEntry);
                }
            }
        }
        o_relaAssessResultBO.saveRelaAssessResult(relaAssessResult);
    }

    /**
     * 通过告警ID、评估值得到状态图标
     * 
     * @author 金鹏祥
     * @param alarmId
     * @param assessValue
     * @return String
    */
    private String getStatusCssIcon(String alarmId, Double assessValue) {
        List<AlarmRegion> AlarmRegionList = new ArrayList<AlarmRegion>();
        for (AlarmRegion alarmRegion : AlarmRegionListAll) {
            if (alarmId.equalsIgnoreCase(alarmRegion.getAlarmPlan().getId())) {
                AlarmRegionList.add(alarmRegion);
            }
        }

        for (AlarmRegion alarmRegion : AlarmRegionList) {
            if (assessValue >= Double.valueOf(alarmRegion.getMinValue()) && assessValue <= Double.valueOf(alarmRegion.getMaxValue())) {
                return alarmRegion.getAlarmIcon().getId();
            }
        }

        return "";
    }

    /**
     * 获取当前年份
     * 
     * @author 金鹏祥
     * @return
    */
    private String getYear() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        return String.valueOf(year);
    }

    /**
     * 获取当前月份
     * 
     * @author 金鹏祥
     * @return
    */
    private String getMonth() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        if (month < 10) {
            return "0" + String.valueOf(month);
        }
        return String.valueOf(month);
    }
}
