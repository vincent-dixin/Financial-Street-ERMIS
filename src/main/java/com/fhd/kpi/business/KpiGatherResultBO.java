package com.fhd.kpi.business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import java.util.TreeMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.math.stat.descriptive.summary.Sum;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.comm.business.CategoryBO;
import com.fhd.comm.business.TimePeriodBO;
import com.fhd.comm.business.formula.StatisticFunctionCalculateBO;
import com.fhd.comm.dao.TimePeriodDAO;
import com.fhd.comm.entity.TimePeriod;
import com.fhd.core.utils.DateUtils;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.kpi.business.dynamictable.MSColumnLine3D;
import com.fhd.kpi.dao.KpiGatherResultDAO;
import com.fhd.kpi.entity.Kpi;
import com.fhd.kpi.entity.KpiGatherResult;
import com.fhd.kpi.interfaces.IKpiGatherResultBO;
import com.fhd.sys.business.dic.DictBO;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 
 * 指标采集结果业务逻辑BO
 * 
 * @author 陈晓哲
 * @version
 * @since Ver 1.1
 * @Date 2012 2012-9-17 上午13:56:52
 * 
 * @see
 */
@Service
@SuppressWarnings("unchecked")
public class KpiGatherResultBO implements IKpiGatherResultBO {

    @Autowired
    private KpiGatherResultDAO o_kpiGatherResultDAO;

    @Autowired
    private KpiBO o_kpiBO;

    @Autowired
    private DictBO o_DictBO;

    @Autowired
    private TimePeriodDAO o_timePeriodDAO;

    @Autowired
    private CategoryBO o_categoryBO;
    
    @Autowired
    private TimePeriodBO o_timePeriodBO;

    /**
     * 新增指标采集结果.
     * 
     * @param kpiGatherResult
     *            指标采集结果实体
     */
    @Transactional
    public void saveKpiGatherResult(KpiGatherResult kpiGatherResult) {
        o_kpiGatherResultDAO.save(kpiGatherResult);
    }

    /**
     * 保存指标采集结果.
     * 
     * @param kpiGatherResult
     *            指标采集结果实体
     */
    @Transactional
    public void mergeKpiGatherResult(KpiGatherResult kpiGatherResult) {
        o_kpiGatherResultDAO.merge(kpiGatherResult);
    }

    /**
     * 根据指标采集结果id查询指标采集结果.
     * 
     * @param id
     *            指标采集结果id
     * @return KpiGatherResult
     */
    public KpiGatherResult findKpiGatherResultById(String id) {
        return o_kpiGatherResultDAO.get(id);
    }

    /**
     * 根据指标id查询指标当期采集结果.
     * 
     * @param kpiId
     *            指标id
     * @param timePeriodId
     *            时间区间维id
     * @return KpiGatherResult
     */
    public KpiGatherResult findCurrentKpiGatherResultByKpiId(String kpiId, String timePeriodId) {

        KpiGatherResult gatherResult = null;

        Criteria criteria = o_kpiGatherResultDAO.createCriteria();
        criteria.createAlias("kpi", "kpi");
        criteria.createAlias("timePeriod", "time");

        criteria.add(Restrictions.eqProperty("time.type", "kpi.gatherFrequence.id"));
        criteria.add(Restrictions.eq("kpi.id", kpiId));

        if (StringUtils.isNotBlank(timePeriodId)) {
            criteria.add(Restrictions.eq("time.id", timePeriodId));
        }
        else {
            // 取当前时间
            Date currentDate = new Date();
            criteria.add(Restrictions.and(Restrictions.le("time.startTime", currentDate), Restrictions.gt("time.endTime", currentDate)));
        }
        List<KpiGatherResult> list = criteria.list();
        if (null != list && list.size() > 0) {
            gatherResult = list.get(0);
        }
        return gatherResult;
    }
    /**
     * 根据指标id查询指标当期采集结果.
     * 
     * @param kpiId
     *            指标id
     * @param timePeriodId
     *            时间区间维id
     * @return KpiGatherResult
     */
    public List<KpiGatherResult> findCurrentKpiGatherResultByKpiIds(String kpiId, String timePeriodId) {
    	
    	KpiGatherResult gatherResult = null;
    	
    	Criteria criteria = o_kpiGatherResultDAO.createCriteria();
    	criteria.createAlias("kpi", "kpi");
    	criteria.createAlias("timePeriod", "time");
    	
    	criteria.add(Restrictions.eqProperty("time.type", "kpi.gatherFrequence.id"));
    	criteria.add(Restrictions.eq("kpi.id", kpiId));
    	
    	if (StringUtils.isNotBlank(timePeriodId)) {
    		criteria.add(Restrictions.eq("time.id", timePeriodId));
    	}
    	else {
    		// 取当前时间
    		//Date currentDate = new Date();
    		//criteria.add(Restrictions.and(Restrictions.le("time.startTime", currentDate), Restrictions.gt("time.endTime", currentDate)));
    	}
    	criteria.addOrder(Order.asc("time.id"));
    	List<KpiGatherResult> list = criteria.list();
    	/*if (null != list && list.size() > 0) {
    		gatherResult = list.get(0);
    	}*/
    	return list;
    }

    /**
     * 根据指标id查询指标上期采集结果.
     * 
     * @param kpiId
     *            指标id
     * @param timePeriodId
     *            时间区间维id
     * @return KpiGatherResult
     */
    public KpiGatherResult findPreviousKpiGatherResultByKpiId(String kpiId, String timePeriodId) {

        KpiGatherResult gatherResult = null;

        Criteria criteria = o_kpiGatherResultDAO.createCriteria();
        criteria.createAlias("kpi", "kpi");
        criteria.createAlias("timePeriod", "time");

        criteria.add(Restrictions.eqProperty("time.type", "kpi.gatherFrequence.id"));
        criteria.add(Restrictions.eq("kpi.id", kpiId));

        if (StringUtils.isNotBlank(timePeriodId)) {
            criteria.add(Restrictions.eq("time.nextPeriod.id", timePeriodId));
        }
        else {
            // 取当前时间
            Date currentDate = new Date();
            criteria.add(Restrictions.and(Restrictions.le("time.startTime", currentDate), Restrictions.gt("time.endTime", currentDate)));
        }
        List<KpiGatherResult> list = criteria.list();
        if (null != list && list.size() > 0) {
            gatherResult = list.get(0);
        }
        return gatherResult;
    }

    /**
     * 根据指标id查询指标当期采集结果所在的季度指标采集结果(采集频率为月份).
     * 
     * @param kpiId
     *            指标id
     * @param timePeriod
     *            时间区间维id
     * @return KpiGatherResult
     */
    public KpiGatherResult findQuarterKpiGatherResultByKpiId(String kpiId, String timePeriod) {
        KpiGatherResult gatherResult = null;
        KpiGatherResult currentGatherResult = findCurrentKpiGatherResultByKpiId(kpiId, timePeriod);
        if (null != currentGatherResult) {
            TimePeriod parentTimePeriod = currentGatherResult.getTimePeriod().getParent();
            Criteria criteria = this.o_kpiGatherResultDAO.createCriteria();
            criteria.add(Restrictions.eq("kpi.id", kpiId));
            criteria.add(Restrictions.eq("timePeriod", parentTimePeriod));
            List<KpiGatherResult> list = criteria.list();
            if (null != list && list.size() > 0) {
                gatherResult = list.get(0);
            }

        }
        return gatherResult;
    }

    /**
     * 根据指标id查询指标当期采集结果所在的年度指标采集结果(采集频率不包括年).
     * 
     * @param kpiId
     *            指标id
     * @param timePeriod
     *            时间区间维id
     * @return KpiGatherResult
     */
    public KpiGatherResult findYearKpiGatherResultByKpiId(String kpiId, String timePeriod) {
        KpiGatherResult gatherResult = null;
        KpiGatherResult currentGatherResult = findCurrentKpiGatherResultByKpiId(kpiId, timePeriod);
        if (null != currentGatherResult) {
            String year = currentGatherResult.getTimePeriod().getYear();
            Criteria criteria = this.o_kpiGatherResultDAO.createCriteria();
            criteria.add(Restrictions.eq("kpi.id", kpiId));
            criteria.add(Restrictions.eq("timePeriod.id", year));
            List<KpiGatherResult> list = criteria.list();
            if (null != list && list.size() > 0) {
                gatherResult = list.get(0);
            }
        }
        return gatherResult;
    }

    /**
     * 根据指标id查询指标所有采集结果.
     * 
     * @param kpiId
     *            指标id
     * @return List<KpiGatherResult>
     */
    public List<KpiGatherResult> findKpiGatherResultListByKpiId(String kpiId) {
        Kpi kpi = o_kpiBO.findKpiById(kpiId);
        Set<KpiGatherResult> list = kpi.getKpiGatherResult();
        List<KpiGatherResult> gatherList = new ArrayList<KpiGatherResult>(list);
        return gatherList;
    }

    /**
     * 根据指标名称集合查询对应的当期指标采集结果.
     * 
     * @param nameList
     *            名称集合
     * @param timePeriodId
     *            时间区间维id
     * @return List<KpiGatherResult>
     */
    public List<KpiGatherResult> findKpiGatherResultListByKpiNames(List<String> nameList, String timePeriodId) {
        Criteria criteria = o_kpiGatherResultDAO.createCriteria();
        criteria.createAlias("kpi", "k");
        criteria.createAlias("k.gatherFrequence", "g");
        criteria.createAlias("timePeriod", "t");
        criteria.add(Restrictions.in("k.name", nameList));
        criteria.add(Restrictions.eqProperty("t.type", "g.id"));
        if (StringUtils.isNotBlank(timePeriodId)) {
            criteria.add(Restrictions.eq("t.id", timePeriodId));
        }
        else {
            Date currentDate = new Date();
            criteria.add(Restrictions.le("t.startTime", currentDate));
            criteria.add(Restrictions.ge("t.endTime", currentDate));
        }

        return criteria.list();
    }

    /**
     * <pre>
     * 根据指标ID查询当前年的采集结果
     * </pre>
     * 
     * @author 陈晓哲
     * @param kpiId
     *            指标ID
     * @return
     * @since fhd　Ver 1.1
     */
    public List<KpiGatherResult> findCurrentYearKpiGatherResultByKpiId(String kpiId, String year) {
        Criteria criteria = this.o_kpiGatherResultDAO.createCriteria();
        criteria.createAlias("timePeriod", "timeperiod");
        criteria.add(Restrictions.eq("kpi.id", kpiId));
        criteria.add(Restrictions.eq("timeperiod.year", year));
        return criteria.list();
    }

    /**根据指标ID和时间区间纬度查询
     * @param kpiid 指标ID
     * @param timeperiod 时间区间纬度
     * @return
     */
    public KpiGatherResult findKpiGatherResultByIdAndTimeperiod(String kpiid, TimePeriod timeperiod) {
        Criteria criteria = this.o_kpiGatherResultDAO.createCriteria();
        criteria.createAlias("timePeriod", "timeperiod");
        criteria.add(Restrictions.eq("kpi.id", kpiid));
        criteria.add(Restrictions.eq("timePeriod", timeperiod));
        List<KpiGatherResult> results = criteria.list();
        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }

    /**
     * <pre>
     * 根据指标ID查询当前年的采集结果,根据频率
     * </pre>
     * 
     * @author 陈晓哲
     * @param kpiId指标ID
     * @param frequence 采集频率
     * @return
     * @since fhd　Ver 1.1
     */
    public List<KpiGatherResult> findCurrentYearKpiGatherResultByKpiIdAndFrequence(String kpiId, String year, String frequence) {
        Criteria criteria = this.o_kpiGatherResultDAO.createCriteria();
        criteria.createAlias("timePeriod", "timeperiod");
        criteria.setFetchMode("timeperiod", FetchMode.JOIN);
        criteria.add(Restrictions.eq("kpi.id", kpiId));
        criteria.add(Restrictions.eq("timeperiod.year", year));
        criteria.add(Restrictions.eq("timeperiod.type", frequence));
        criteria.addOrder(Order.asc("timePeriod"));
        return criteria.list();
    }
    /**
     * <pre>
     * 根据指标ID集合查询当前年的采集结果,根据频率
     * </pre>
     * 
     * @author 陈晓哲
     * @param kpiId指标ID
     * @param frequence 采集频率
     * @return
     * @since fhd　Ver 1.1
     */
    public List<KpiGatherResult> findCurrentYearKpiGatherResultByKpiIdsAndFrequence(List<String> kpiIds, String year, String frequence) {
        Criteria criteria = this.o_kpiGatherResultDAO.createCriteria();
        criteria.createAlias("timePeriod", "timeperiod");
        criteria.createAlias("kpi", "kpi");
        criteria.setFetchMode("timeperiod", FetchMode.JOIN);
        criteria.setFetchMode("kpi", FetchMode.JOIN);
        criteria.add(Restrictions.in("kpi.id", kpiIds));
        criteria.add(Restrictions.eq("timeperiod.year", year));
        criteria.add(Restrictions.eq("timeperiod.type", frequence));
        criteria.addOrder(Order.asc("kpi.id"));
        criteria.addOrder(Order.asc("timePeriod"));
        return criteria.list();
    }

    /**
     * <pre>
     * 根据指标ID查询年的采集结果,根据频率
     * </pre>
     * 
     * @author 陈晓哲
     * @param kpiId指标ID
     * @param frequence 采集频率
     * @param years 采集结果所在年
     * @return
     * @since fhd　Ver 1.1
     */
    public List<KpiGatherResult> findYearsKpiGatherResultByKpiIdAndFrequence(String kpiId, List<String> years, String frequence) {
        Criteria criteria = this.o_kpiGatherResultDAO.createCriteria();
        criteria.createAlias("timePeriod", "timeperiod");
        criteria.setFetchMode("timeperiod", FetchMode.JOIN);
        criteria.add(Restrictions.eq("kpi.id", kpiId));
        criteria.add(Restrictions.in("timeperiod.year", years));
        criteria.add(Restrictions.eq("timeperiod.type", frequence));
        criteria.addOrder(Order.asc("timePeriod"));
        return criteria.list();
    }
    
    public List<KpiGatherResult> findYearsKpiGatherResultByKpiIdAndFrequence(String kpiId,String years, String frequence) {
    	Criteria criteria = this.o_kpiGatherResultDAO.createCriteria();
    	criteria.createAlias("timePeriod", "timeperiod");
    	criteria.setFetchMode("timeperiod", FetchMode.JOIN);
    	criteria.add(Restrictions.eq("kpi.id", kpiId));
    	criteria.add(Restrictions.eq("timeperiod.year", years));
    	criteria.add(Restrictions.eq("timeperiod.type", frequence));
    	criteria.addOrder(Order.asc("timePeriod"));
    	return criteria.list();
    }
    /**
     * <pre>
     * 根据指标ID查询年的采集结果,根据频率
     * </pre>
     * 
     * @author 陈晓哲
     * @param kpiId指标ID
     * @param frequence 采集频率
     * @param years 采集结果所在年
     * @return
     * @since fhd　Ver 1.1
     */
    public List<KpiGatherResult> findYearsKpiGatherResultByKpiNameAndFrequence(String name, List<String> years, String frequence) {
        Criteria criteria = this.o_kpiGatherResultDAO.createCriteria();
        criteria.createAlias("timePeriod", "timeperiod");
        criteria.createAlias("kpi", "kpi");
        criteria.setFetchMode("timeperiod", FetchMode.JOIN);
        criteria.add(Restrictions.eq("kpi.name", name));
        criteria.add(Restrictions.in("timeperiod.year", years));
        criteria.add(Restrictions.eq("timeperiod.type", frequence));
        criteria.addOrder(Order.asc("timePeriod"));
        return criteria.list();
    }

    /**
     * 根据指标名称集合查询对应的当前月份之前的采集结果.
     * 
     * @param nameList
     *            指标名称集合
     * @return Map<String, List<KpiGatherResult>>
     */
    public Map<String, List<KpiGatherResult>> findKpiGatherResultPreMonthListByKpiNames(List<String> nameList, String month) {
        String year = DateUtils.getYear(new Date());
        Map<String, List<KpiGatherResult>> result = new HashMap<String, List<KpiGatherResult>>();
        if (null != nameList && nameList.size() > 0) {
            Criteria criteria = this.o_kpiGatherResultDAO.createCriteria();
            criteria.createAlias("kpi", "kpi");
            criteria.createAlias("timePeriod", "timeperiod");
            criteria.add(Restrictions.in("kpi.name", nameList));
            criteria.add(Restrictions.eq("timeperiod.type", "0frequecy_month"));
            criteria.add(Restrictions.eq("timeperiod.year", year));
            criteria.add(Restrictions.le("timeperiod.month", month));
            List<KpiGatherResult> resultList = criteria.list();
            for (KpiGatherResult kpiGatherResult : resultList) {
                String kpiName = kpiGatherResult.getKpi().getName();
                if (!result.containsKey(kpiName)) {
                    List<KpiGatherResult> rlist = new ArrayList<KpiGatherResult>();
                    rlist.add(kpiGatherResult);
                    result.put(kpiName, rlist);
                }
                else {
                    result.get(kpiName).add(kpiGatherResult);
                }
            }
        }
        return result;
    }

    /**
     * 根据累积值计算项计算季度累积值.
     * 
     * @param kpiGatherResult
     *            指标当期采集结果
     * @param typeSum
     *            指标采集结果累积类型值
     * @param calculateItem
     *            累积值计算项
     */
    public Double calculateQuarterAccumulatedValueByCalculateItem(KpiGatherResult kpiGatherResult, String typeSum, String calculateItem) {
        // 当前期间采集结果的父时间维度
        TimePeriod parentTimePeriod = kpiGatherResult.getTimePeriod().getParent();
        Criteria criteria = o_kpiGatherResultDAO.createCriteria();
        criteria.createAlias("timePeriod", "time");
        criteria.add(Property.forName("kpi").eq(kpiGatherResult.getKpi()));
        criteria.add(Property.forName("time.parent").eq(parentTimePeriod));
        criteria.addOrder(Order.asc("time.startTime"));
        List<KpiGatherResult> gatherResults = criteria.list();
        List<Double> gatherResultValues = new ArrayList<Double>();
        for (KpiGatherResult result : gatherResults) {
            if ("targetValueSum".equals(typeSum))// 目标值累计
            {
                gatherResultValues.add(result.getTargetValue());
            }
            else if ("resultValueSum".equals(typeSum))// 结果值累计
            {
                gatherResultValues.add(result.getFinishValue());
            }
            else if ("assessmentValueSum".equals(typeSum))// 评估值累计
            {
                gatherResultValues.add(result.getAssessmentValue());
            }
        }
        return calculateAccumulateValue(gatherResultValues, calculateItem);
    }

    /**
     * 根据累积值计算项计算年度累积值.
     * 
     * @param kpiGatherResult
     *            指标当期采集结果
     * @param typeSum
     *            指标采集结果累积类型值
     * @param calculateItem
     *            累积值计算项
     */
    public Double calculateYearAccumulatedValueByCalculateItem(KpiGatherResult kpiGatherResult, String typeSum, String calculateItem) {
        Kpi kpi = kpiGatherResult.getKpi();
        String frequence = kpi.getGatherFrequence().getId();
        TimePeriod timePeriod = kpiGatherResult.getTimePeriod();
        String year = timePeriod.getYear();
        Criteria criteria = o_kpiGatherResultDAO.createCriteria();
        criteria.createAlias("timePeriod", "time");
        criteria.add(Restrictions.eq("kpi", kpi));
        criteria.add(Property.forName("time.year").eq(year));
        criteria.add(Property.forName("time.type").eq(frequence));
        criteria.addOrder(Order.asc("time.startTime"));
        List<KpiGatherResult> gatherResults = criteria.list();
        List<Double> gatherResultValues = new ArrayList<Double>();
        for (KpiGatherResult result : gatherResults) {
            if ("targetValueSum".equals(typeSum))// 目标值累计
            {
                gatherResultValues.add(result.getTargetValue());
            }
            else if ("resultValueSum".equals(typeSum))// 结果值累计
            {
                gatherResultValues.add(result.getFinishValue());
            }
            else if ("assessmentValueSum".equals(typeSum))// 评估值累计
            {
                gatherResultValues.add(result.getAssessmentValue());
            }
        }
        //根据累计类型计算累计值
        Double accumulateValue = calculateAccumulateValue(gatherResultValues, calculateItem);
        return accumulateValue;
    }

    /**
     * 根据评估值计算趋势.
     * 
     * @param kpiGatherResult
     *            当期指标采集结果
     */
    public DictEntry calculateTrendByAssessmentValue(KpiGatherResult gatherResult) {
        DictEntry trend = null;
        Kpi kpi = gatherResult.getKpi();
        TimePeriod prePeriodTime = gatherResult.getTimePeriod().getPrePeriod();
        Double assvalue = gatherResult.getAssessmentValue();
        Double preAssvalue = null;
        Criteria criteria = o_kpiGatherResultDAO.createCriteria();
        criteria.add(Restrictions.eq("timePeriod", prePeriodTime));
        criteria.add(Restrictions.eq("kpi", kpi));
        List<KpiGatherResult> list = criteria.list();
        if (null != list && list.size() > 0) {
            if (null != list.get(0).getAssessmentValue()) {
                preAssvalue = list.get(0).getAssessmentValue();

                if (null != preAssvalue && null != assvalue) {
                    DictEntry upDict = o_DictBO.findDictEntryById("up");
                    DictEntry flatDict = o_DictBO.findDictEntryById("flat");
                    DictEntry downDict = o_DictBO.findDictEntryById("down");
                    if (preAssvalue.compareTo(assvalue) < 0) {
                        trend = upDict;
                    }
                    else if (preAssvalue.compareTo(assvalue) == 0) {
                        trend = flatDict;
                    }
                    else {
                        trend = downDict;
                    }
                }
            }
        }
        return trend;
    }

    /**
     * <pre>
     * 根据累计类型计算累计值
     * </pre>
     * 
     * @author 陈晓哲
     * @param gatherResultValues
     *            待求累计值数组
     * @param calculateItem
     *            累计类型
     * @return 累计值
     * @since fhd　Ver 1.1
     */
    public Double calculateAccumulateValue(List<Double> gatherResultValues, String calculateItem) {
        Double result = null;
        if (gatherResultValues.size() > 0) {
            double[] results = null;
            Double[] gatherResult = gatherResultValues.toArray(new Double[gatherResultValues.size()]);
            if (null != gatherResult && gatherResult.length > 0) {
                results = new double[gatherResult.length];
                for (int i = 0; i < gatherResult.length; i++) {
                    if (null != gatherResult[i]) {
                        results[i] = gatherResult[i];
                    }
                    else {
                        results[i] = 0.0;
                    }
                }
                if ("kpi_sum_measure_avg".equals(calculateItem)) {
                    result = StatisticFunctionCalculateBO.ma(results);
                }
                else if ("kpi_sum_measure_max".equals(calculateItem)) {
                    result = StatisticFunctionCalculateBO.max(results);
                }
                else if ("kpi_sum_measure_min".equals(calculateItem)) {
                    result = StatisticFunctionCalculateBO.min(results);
                }
                else if ("kpi_sum_measure_sum".equals(calculateItem)) {
                    Sum sum = new Sum();
                    result = sum.evaluate(results);
                }
                else if ("kpi_sum_measure_first".equals(calculateItem)) {
                    if (null != results && results.length > 0) {
                        result = results[0];
                    }
                }
                else if ("kpi_sum_measure_last".equals(calculateItem)) {
                    int size = results.length;
                    if (null != results && size > 0) {
                        result = results[size - 1];
                    }
                }
            }
        }
        return result;
    }

    /**
     * <pre>
     * 根据指标ID查询采集结果
     * </pre>
     * 
     * @author 陈晓哲
     * @param kpiId
     *            指标ID
     * @return
     * @since fhd　Ver 1.1
     */
    public HashMap<String, String> findMapKpiGatherResultListByKpiId(String kpiId, String year) {
        Criteria criteria = o_kpiGatherResultDAO.createCriteria();
        criteria.add(Restrictions.eq("kpi.id", kpiId));
        criteria.add(Restrictions.like("timePeriod.id", "%" + year + "%"));
        List<KpiGatherResult> list = criteria.list();
        HashMap<String, String> map = new HashMap<String, String>();
        for (KpiGatherResult kpiGatherResult : list) {
            map.put(kpiGatherResult.getTimePeriod().getId(), kpiGatherResult.getTimePeriod().getId() + "," + kpiGatherResult.getFinishValue() + ","
                    + kpiGatherResult.getTargetValue() + "," + kpiGatherResult.getAssessmentValue());
        }
        return map;
    }

    /**
     * <pre>
     * 根据指标ID查询采集结果
     * </pre>
     * 
     * @author 陈晓哲
     * @param kpiId
     *            指标ID
     * @return
     * @since fhd　Ver 1.1
     */
    public HashMap<String, KpiGatherResult> findMapKpiGatherResultListByKpiId(String kpiId) {
        Criteria criteria = o_kpiGatherResultDAO.createCriteria();
        criteria.add(Restrictions.eq("kpi.id", kpiId));
        List<KpiGatherResult> list = criteria.list();
        HashMap<String, KpiGatherResult> map = new HashMap<String, KpiGatherResult>();
        for (KpiGatherResult kpiGatherResult : list) {
            if(null!=kpiGatherResult.getTimePeriod()){
                map.put(kpiGatherResult.getTimePeriod().getId(), kpiGatherResult);
            }
        }
        return map;
    }

    /**
     * 根据时间查询对应VALUE
     * 
     * @author 金鹏祥
     * @param whereIn 时间集合   
     * @return List<KpiGatherResult>
     */
    public List<KpiGatherResult> findKpiGatherResultListByTimePeriodId(String kpiId, String[] whereIn) {
        Criteria criteria = o_kpiGatherResultDAO.createCriteria();
        criteria.add(Restrictions.eq("kpi.id", kpiId));
        criteria.add(Restrictions.in("timePeriod.id", whereIn));
        criteria.add(Restrictions.isNotNull("targetValue"));
        List<KpiGatherResult> list = criteria.list();
        return list;
    }

    /**
     * 根据时间查询对应VALUE
     * 
     * @author 金鹏祥
     * @param whereIn 时间集合   
     * @return List<KpiGatherResult>
     */
    public List<KpiGatherResult> findKpiGatherResultListByTimePeriodId(String kpiId, Object[] whereIn) {
        Criteria criteria = o_kpiGatherResultDAO.createCriteria();
        criteria.add(Restrictions.eq("kpi.id", kpiId));
        criteria.add(Restrictions.in("timePeriod.id", whereIn));
        criteria.add(Restrictions.isNotNull("targetValue"));
        List<KpiGatherResult> list = criteria.list();
        return list;
    }

    /**
     * <pre>
     * 根据采集频率查询时间维度
     * </pre>
     * 
     * @author 陈晓哲
     * @param frequenceType
     *            采集频率
     * @return
     * @since fhd　Ver 1.1
     */
    public List<TimePeriod> findTimePeriodListByFrequenceType(String frequenceType, int year) {
        List<String> condList = new ArrayList<String>();
        if ("0frequecy_year".equals(frequenceType)) {
            condList.add("0frequecy_year");
        }
        else if ("0frequecy_quarter".equals(frequenceType)) {
            condList.add("0frequecy_year");
            condList.add("0frequecy_quarter");
        }
        else if ("0frequecy_month".equals(frequenceType)) {
            condList.add("0frequecy_year");
            condList.add("0frequecy_quarter");
            condList.add("0frequecy_month");
        }
        else if ("0frequecy_halfyear".equals(frequenceType)) {
            condList.add("0frequecy_year");
            condList.add("0frequecy_halfyear");
        }
        else if ("0frequecy_week".equals(frequenceType)) {
            condList.add("0frequecy_year");
            condList.add("0frequecy_week");
            condList.add("0frequecy_month");
        }

        if (condList.size() > 0) {
            Criteria criteria = this.o_timePeriodDAO.createCriteria();
            criteria.setCacheable(true);// 缓存时间纬度提高效率
            criteria.add(Restrictions.eq("year", String.valueOf(year)));
            criteria.add(Restrictions.in("type", condList));
            criteria.addOrder(Order.asc("endTime"));
            criteria.addOrder(Order.desc("startTime"));
            return criteria.list();
        }
        else {
            return new ArrayList<TimePeriod>();
        }

    }

    /**
     * <pre>
     * 根据当期的采集结果查询上期的采集结果
     * </pre>
     * 
     * @author 陈晓哲
     * @param currentGatherResult
     *            当期采集结果
     * @return KpiGatherResult 前期采集结果
     * @since fhd　Ver 1.1
     */
    public KpiGatherResult findPreGatherResultByCurrent(KpiGatherResult currentGatherResult) {
        KpiGatherResult preKpiGatherResult = null;
        if (null != currentGatherResult) {
            TimePeriod preTimePeriod = currentGatherResult.getTimePeriod().getPrePeriod();
            Kpi kpi = currentGatherResult.getKpi();
            Criteria criteria = o_kpiGatherResultDAO.createCriteria();
            criteria.add(Restrictions.eq("kpi", kpi));
            criteria.add(Restrictions.eq("timePeriod", preTimePeriod));
            List<KpiGatherResult> list = criteria.list();
            if (null != list && list.size() > 0) {
                preKpiGatherResult = list.get(0);
            }
        }
        return preKpiGatherResult;
    }

    /**
     * <pre>
     * 根据当期的采集结果查询去年同期的采集结果
     * </pre>
     * 
     * @author 陈晓哲
     * @param currentGatherResult
     *            当期采集结果
     * @return KpiGatherResult 去年同期采集结果
     * @since fhd　Ver 1.1
     */
    public KpiGatherResult findPreYearGatherResultByCurrent(KpiGatherResult currentGatherResult) {
        KpiGatherResult preKpiGatherResult = null;
        if (null != currentGatherResult) {
            TimePeriod preYearTimePeriod = currentGatherResult.getTimePeriod().getPreYearPeriod();
            Kpi kpi = currentGatherResult.getKpi();
            Criteria criteria = o_kpiGatherResultDAO.createCriteria();
            criteria.add(Restrictions.eq("kpi", kpi));
            criteria.add(Restrictions.eq("timePeriod", preYearTimePeriod));
            List<KpiGatherResult> list = criteria.list();
            if (null != list && list.size() > 0) {
                preKpiGatherResult = list.get(0);
            }
        }
        return preKpiGatherResult;
    }

    /**
     * <pre>
     * 批量插入所有有效指标的采集结果
     * </pre>
     * 
     * @author 陈晓哲
     * @since fhd　Ver 1.1
     */
    @Transactional
    public void saveBatherKpiGather() {
        String year = DateUtils.getYear(new Date());
        List<Kpi> kpiList = o_kpiBO.findAllKpiListByTask();
        JSONArray jsonarray = new JSONArray();
        for (Kpi kpi : kpiList) {
            JSONObject jsobj = new JSONObject();
            if (null != kpi.getGatherFrequence() && null != kpi.getCompany()) {
                try {
                    jsobj.put("companyid", kpi.getCompany().getId());
                }
                catch (ObjectNotFoundException e) {
                    System.out.println("异常信息:" + e.toString());
                    continue;
                }
                try {
                    jsobj.put("frequence", kpi.getGatherFrequence().getId());
                }
                catch (ObjectNotFoundException e) {
                    System.out.println("异常信息:" + e.toString());
                    continue;
                }
                jsobj.put("kpiid", kpi.getId());
                jsonarray.add(jsobj);
            }
        }
        if (jsonarray.size() > 0) {
            saveBatchKpiGatherResultByFrequenceType(jsonarray, Integer.valueOf(year));
        }
    }

    /**
     * <pre>
     * 批量插入指标采集结果
     * </pre>
     * 
     * @author 陈晓哲
     * @param jsonarray
     *            指标ID,公司id,采集频率
     * @param year
     *            年信息
     * @since fhd　Ver 1.1
     */
    @Transactional
    public void saveBatchKpiGatherResultByFrequenceType(final JSONArray jsonarray, final int year) {
        this.o_kpiGatherResultDAO.getSession().doWork(new Work() {
            public void execute(Connection connection) throws SQLException {
                PreparedStatement pst = null;
                String sql = "insert into t_kpi_kpi_gather_result (id,company_id,kpi_id,time_period_id,begin_time,end_time) values(?,?,?,?,?,?)";
                pst = connection.prepareStatement(sql);
                for (Object jsobj : jsonarray) {
                    String kpiid = ((JSONObject) jsobj).getString("kpiid");
                    String frequenceType = ((JSONObject) jsobj).getString("frequence");
                    String companyid = ((JSONObject) jsobj).getString("companyid");
                    List<TimePeriod> timelist = findTimePeriodListByFrequenceType(frequenceType, year);
                    List<TimePeriod> mergeTimeList = mergeTimePeriodList(timelist, kpiid, companyid);
                    for (TimePeriod timePeriod : mergeTimeList) {
                        pst.setString(1, Identities.uuid());
                        pst.setString(2, companyid);
                        pst.setString(3, kpiid);
                        pst.setString(4, timePeriod.getId());
                        pst.setTimestamp(5, new java.sql.Timestamp(timePeriod.getStartTime().getTime()));
                        pst.setTimestamp(6, new java.sql.Timestamp(timePeriod.getEndTime().getTime()));
                        pst.addBatch();
                    }

                }
                pst.executeBatch();
            }
        });
    }

    /**去调指标采集结果表中已经存在的时间区间纬度
     * @param timePeriodList 时间区间纬度列表
     * @param kpiId 指标id
     * @param companyId 公司ID
     * @return
     */
    public List<TimePeriod> mergeTimePeriodList(List<TimePeriod> timePeriodList, String kpiId, String companyId) {
        List<TimePeriod> mergeTimePeriodList = new ArrayList<TimePeriod>();
        Criteria criteria = o_kpiGatherResultDAO.createCriteria();
        criteria.add(Restrictions.eq("kpi.id", kpiId));
        criteria.add(Restrictions.eq("company.id", companyId));
        List<KpiGatherResult> originalTimePeriodList = criteria.list();
        for (TimePeriod timePeriod : timePeriodList) {
            boolean existFlag = false;
            for (KpiGatherResult okpigatherresult : originalTimePeriodList) {
                if (timePeriod.getId().equals(okpigatherresult.getTimePeriod().getId())) {
                    existFlag = true;
                }
            }
            if (!existFlag) {
                mergeTimePeriodList.add(timePeriod);
            }
        }
        return mergeTimePeriodList;
    }

    /**
     * <pre>
     * 批量插入指标采集结果
     * </pre>
     * 
     * @author 陈晓哲
     * @param 指标ID和指标采集频率Map
     * @param year
     *            年信息
     * @since fhd　Ver 1.1
     */
    @Transactional
    public void saveBatchKpiGatherResultByFrequenceType(final Map<String, String> map, final int year) {
        final String companyid = UserContext.getUser().getCompanyid();// 定时任务会报空指针异常
        this.o_kpiGatherResultDAO.getSession().doWork(new Work() {
            public void execute(Connection connection) throws SQLException {
                PreparedStatement pst = null;
                String sql = "insert into t_kpi_kpi_gather_result (id,company_id,kpi_id,time_period_id,begin_time,end_time) values(?,?,?,?,?,?)";
                pst = connection.prepareStatement(sql);
                for (Map.Entry<String, String> m : map.entrySet()) {

                    String kpiid = m.getKey();
                    String frequenceType = m.getValue();
                    List<TimePeriod> timelist = findTimePeriodListByFrequenceType(frequenceType, year);
                    for (TimePeriod timePeriod : timelist) {
                        pst.setString(1, Identities.uuid());
                        pst.setString(2, companyid);
                        pst.setString(3, kpiid);
                        pst.setString(4, timePeriod.getId());
                        pst.setTimestamp(5, new java.sql.Timestamp(timePeriod.getStartTime().getTime()));
                        pst.setTimestamp(6, new java.sql.Timestamp(timePeriod.getEndTime().getTime()));
                        pst.addBatch();
                    }

                }
                pst.executeBatch();
            }
        });

    }

    /**
     * <pre>
     * 批量插入指标采集结果
     * </pre>
     * 
     * @author 陈晓哲
     * @param kpiid
     *            指标ID
     * @param frequenceType
     *            采集频率
     * @param year
     *            年
     * @throws SQLException
     * @since fhd　Ver 1.1
     */
    @Transactional
    public void saveBatchKpiGatherResultByFrequenceType(final String kpiid, String frequenceType, int year) throws SQLException {
        List<String> condList = new ArrayList<String>();
        final String companyid = UserContext.getUser().getCompanyid();
        if ("0frequecy_year".equals(frequenceType)) {
            condList.add("0frequecy_year");
        }
        else if ("0frequecy_quarter".equals(frequenceType)) {
            condList.add("0frequecy_year");
            condList.add("0frequecy_quarter");
        }
        else if ("0frequecy_month".equals(frequenceType)) {
            condList.add("0frequecy_year");
            condList.add("0frequecy_quarter");
            condList.add("0frequecy_month");
        }
        else if ("0frequecy_halfyear".equals(frequenceType)) {
            condList.add("0frequecy_year");
            condList.add("0frequecy_halfyear");
        }
        else if ("0frequecy_week".equals(frequenceType)) {
            condList.add("0frequecy_year");
            condList.add("0frequecy_week");
            condList.add("0frequecy_month");
        }
        Criteria criteria = this.o_timePeriodDAO.createCriteria();
        criteria.add(Restrictions.eq("year", String.valueOf(year)));
        criteria.add(Restrictions.in("type", condList));
        criteria.addOrder(Order.asc("endTime"));
        criteria.addOrder(Order.desc("startTime"));
        final List<TimePeriod> times = criteria.list();
        this.o_kpiGatherResultDAO.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                PreparedStatement pst = null;
                String sql = "insert into t_kpi_kpi_gather_result (id,company_id,kpi_id,time_period_id,begin_time,end_time) values(?,?,?,?,?,?)";
                pst = conn.prepareStatement(sql);
                for (TimePeriod timePeriod : times) {
                    pst.setString(1, Identities.uuid());
                    pst.setString(2, companyid);
                    pst.setString(3, kpiid);
                    pst.setString(4, timePeriod.getId());
                    pst.setTimestamp(5, new java.sql.Timestamp(timePeriod.getStartTime().getTime()));
                    pst.setTimestamp(6, new java.sql.Timestamp(timePeriod.getEndTime().getTime()));
                    pst.addBatch();
                }
                pst.executeBatch();
            }
        });
    }

    /**
     * 根据记分卡ID查询所关联的指标所在年的采集结果
     * 
     * @param categoryId
     *            记分卡ID
     * @param year
     *            年分
     * @return List<KpiGatherResult>
     */
    public Map<Kpi, List<KpiGatherResult>> findKpiGatherResultByCategoryId(String categoryId, String name, String year) {
        Map<Kpi, List<KpiGatherResult>> maps = new TreeMap<Kpi, List<KpiGatherResult>>();
        StringBuffer hqlbuf = new StringBuffer();
        hqlbuf.append(" select  distinct kpi from Category  category left join category.kpiRelaCategorys  kpireal left join  kpireal.kpi  kpi left join fetch kpi.kpiGatherResult  result left join result.timePeriod  time ");
        hqlbuf.append(" where kpi.deleteStatus=true and kpi.gatherFrequence.id=time.type and time.year=:year ");
        if (StringUtils.isNotBlank(categoryId)) {
            hqlbuf.append(" and category.id=:id ");
        }
        if (StringUtils.isNotBlank(name)) {
            hqlbuf.append(" and category.name=:name ");
        }
        hqlbuf.append(" order by category.name asc");
        Query query = o_kpiGatherResultDAO.createQuery(hqlbuf.toString());
        query.setParameter("year", year);
        if (StringUtils.isNotBlank(name)) {
            query.setParameter("name", name);
        }
        if (StringUtils.isNotBlank(categoryId)) {
            query.setParameter("id", categoryId);
        }
        List<Kpi> listkpi = query.list();
        for (Kpi kpi : listkpi) {
            maps.put(kpi, new ArrayList<KpiGatherResult>(kpi.getKpiGatherResult()));
        }
        return maps;
    }

    /**
     * 根据记分卡ID查询所关联的指标所在年的采集结果
     * 
     * @param categoryId
     *            记分卡ID
     * @param year
     *            年分
     * @return Map<Kpi, List<Object[]>>
     *         object[]数据中的数据顺序{name指标名称,frequence频率,lasttimeperiod最后更新时间区间维
     *         ,timeperiod时间区间维
     *         ,starttime开始时间,endtime结束时间,finishvalue实际值,status告警状态,direction趋势}
     */
    public Map<String, List<Object[]>> findKpiGatherResultsByCategoryId(String categoryId, String name, String year) {
        Map<String, List<Object[]>> map = new HashMap<String, List<Object[]>>();
        String companyid = UserContext.getUser().getCompanyid();
        StringBuffer selectBuf = new StringBuffer();
        StringBuffer fromLeftJoinBuf = new StringBuffer();
        StringBuffer wherebuf = new StringBuffer();
        StringBuffer orderbuf = new StringBuffer();
        List<Object> paralist = new ArrayList<Object>();
        selectBuf
                .append(" select kpi.id  id, kpi.kpi_name  name , kpi.gather_frequence  frequence ,kpi.latest_time_period_id  latesttimeperiod, time.id  timeperiod,time.start_time  starttime,time.end_time  endtime ,result.finish_value  finishValue ,statusdict.dict_entry_value  status ,dirdict.dict_entry_value  direction ");
        fromLeftJoinBuf.append(" from t_kpi_kpi  kpi ");
        fromLeftJoinBuf.append(" left outer join ");
        fromLeftJoinBuf.append(" (t_kpi_kpi_gather_result  result  ");
        fromLeftJoinBuf.append(" inner join ");
        fromLeftJoinBuf.append(" t_com_time_period  time on result.time_period_id=time.id  ");
        if (StringUtils.isNotBlank(year)) {
            fromLeftJoinBuf.append("  and time.eyear=?");
            paralist.add(year);
        }
        else {
            fromLeftJoinBuf
                    .append(" and time.eyear=(select t.eyear from t_kpi_kpi  k left join t_com_time_period   t on k.latest_time_period_id=t.id where kpi.id=k.id) ");
        }
        fromLeftJoinBuf.append(" ) on kpi.id=result.kpi_id and kpi.gather_frequence=time.etype ");
        fromLeftJoinBuf.append(" left outer join t_sys_dict_entry  statusdict on result.assessment_status = statusdict.id ");
        fromLeftJoinBuf.append(" left outer join t_sys_dict_entry  dirdict on result.direction = dirdict.id ");
        fromLeftJoinBuf.append(" left outer join t_kpi_kpi_rela_category  category on category.kpi_id=kpi.id ");
        wherebuf.append(" where kpi.delete_status=true and kpi.is_kpi_category='KPI'  and 1=1 ");

        if (StringUtils.isNotBlank(categoryId)) {
            wherebuf.append(" and category.category_id=? ");
            paralist.add(categoryId);
        }
        if (StringUtils.isNotBlank(companyid)) {
            wherebuf.append(" and kpi.company_id=? ");
            paralist.add(companyid);
        }

        if (StringUtils.isNotBlank(name)) {
            wherebuf.append(" and kpi.kpi_name like '%").append(name).append("%'");
        }
        orderbuf.append(" order by name,starttime asc ");

        Object[] paraobjects = new Object[paralist.size()];
        paraobjects = paralist.toArray(paraobjects);

        SQLQuery sqlquery = o_kpiGatherResultDAO.createSQLQuery(selectBuf.append(fromLeftJoinBuf).append(wherebuf).append(orderbuf).toString(),
                paraobjects);
        List<Object[]> list = sqlquery.list();
        if (null != list) {
            for (Object[] objects : list) {
                String kpiId = (String) objects[0];
                String kpiName = (String) objects[1];
                String frequence = (String) objects[2];
                Object lasttimeperiod = objects[3];// 最后更新时间区间维ID
                String timeperiod = (String) objects[4];// 时间区间维ID
                Object starttime = objects[5];// 开始时间
                Object endtime = objects[6];// 结束时间
                Object finishvalue = objects[7];// 完成值
                String status = (String) objects[8];// 告警状态
                String direction = (String) objects[9];// 趋势
                String key = kpiName + "@" + frequence;
                Object[] valueObjects = new Object[] { kpiId, kpiName, frequence, lasttimeperiod, timeperiod, starttime, endtime, finishvalue,
                        status, direction };
                if (!map.containsKey(key)) {
                    List<Object[]> valueList = new ArrayList<Object[]>();
                    valueList.add(valueObjects);
                    map.put(key, valueList);
                }
                else {
                    map.get(key).add(valueObjects);
                }
            }
        }
        Map<String, List<Object[]>> resultmap = new HashMap<String, List<Object[]>>();
        for (Map.Entry<String, List<Object[]>> m : map.entrySet()) {
            String key = m.getKey();
            List<Object[]> values = m.getValue();
            String[] arr = key.split("@");
            String kpiName = arr[0];
            String frequence = arr[1];
            if ("0frequecy_month".equals(frequence)) {
                if (null != values && values.size() != 12) {
                    values = null;
                }
            }
            else if ("0frequecy_quarter".equals(frequence)) {
                if (null != values && values.size() != 4) {
                    values = null;
                }
            }
            else if ("0frequecy_halfyear".equals(frequence)) {
                if (null != values && values.size() != 2) {
                    values = null;
                }
            }
            resultmap.put(kpiName, values);
        }
        return resultmap;
    }

    /**
     * 根据指标ID、时间ID查询KpiFatherResult
     * 
     * @author 金鹏祥
     * @param kpiId
     *            指标ID
     * @param timePeriodId
     *            时间ID
     * @return List<KpiGatherResult>
     */
    public List<KpiGatherResult> findKpiGatherResultByKpiIdAndTimePeriodId(String kpiId, boolean like, String timePeriodId) {
        Criteria criteria = this.o_kpiGatherResultDAO.createCriteria();
        criteria.add(Restrictions.eq("kpi.id", kpiId));
        if (!like) {
            criteria.add(Restrictions.eq("timePeriod.id", timePeriodId));
        }
        else {
            criteria.add(Property.forName("timePeriod.id").like(timePeriodId, MatchMode.ANYWHERE));
        }
        criteria.addOrder(Order.asc("timePeriod.id"));
        List<KpiGatherResult> list = criteria.list();
        return list;
    }

    /**
     * 根据指标ID查询指标结果
     * 
     * @author 金鹏祥
     * @param kpiId 指标ID
     * @param time 年度
     * @return List<KpiGatherResult>
     */
    public List<KpiGatherResult> findKpiGatherResultByKpiId(String kpiId, String time[]) {
        Criteria criteria = this.o_kpiGatherResultDAO.createCriteria();
        criteria.add(Restrictions.eq("kpi.id", kpiId));
        criteria.add(Restrictions.in("timePeriod.id", time));
        List<KpiGatherResult> list = criteria.list();
        return list;
    }
    
    public List<KpiGatherResult> findKpiGatherResultByKpiId(String kpiId) {
    	Criteria criteria = this.o_kpiGatherResultDAO.createCriteria();
    	criteria.createAlias("timePeriod", "timePeriod");
    	criteria.createAlias("kpi", "kpi");
    	criteria.add(Restrictions.eq("kpi.id", kpiId));
    	criteria.add(Restrictions.eqProperty("kpi.gatherFrequence.id", "timePeriod.type"));
    	criteria.addOrder(Order.asc("timePeriod"));
    	List<KpiGatherResult> list = criteria.list();
    	return list;
    }

    /**
     * 根据指标ID、时间ID查询KpiFatherResult
     * 
     * @author 金鹏祥
     * @param kpiId
     *            指标ID
     * @param timePeriodId
     *            时间ID
     * @return List<KpiGatherResult>
     */
    public List<KpiGatherResult> findKpiGatherResultByKpiIdAndTimePeriodId(List<String> kpiId, boolean like, String timePeriodId) {
        Criteria criteria = this.o_kpiGatherResultDAO.createCriteria();
        criteria.add(Restrictions.in("kpi.id", kpiId));
        if (!like) {
            criteria.add(Restrictions.eq("timePeriod.id", timePeriodId));
        }
        else {
            criteria.add(Property.forName("timePeriod.id").like(timePeriodId, MatchMode.ANYWHERE));
        }
        criteria.addOrder(Order.asc("timePeriod.id"));
        List<KpiGatherResult> list = criteria.list();
        return list;
    }

    /**
     * 时间存放到MAP
     * 
     * @author 金鹏祥
     * @return HashMap<String, TimePeriod>
     */
    public HashMap<String, TimePeriod> findTimePeriodAllMap() {
        HashMap<String, TimePeriod> map = new HashMap<String, TimePeriod>();
        Criteria criteria = this.o_timePeriodDAO.createCriteria();
        criteria.setCacheable(true);
        List<TimePeriod> list = criteria.list();
        for (TimePeriod timePeriod : list) {
            map.put(timePeriod.getId(), timePeriod);
        }
        return map;
    }

    /**
     * 根据指标ID集合查询当年的指标采集结果数据(供图表使用)
     * 
     * @param kpiList
     *            指标ID集合
     * @param year
     *            年份信息
     * @return Map<String,List<Object[]>> key:指标ID , value:list<object[]>
     *         ,object:{}
     */
    public Map<String, List<Object[]>> findCurrentYearGatherResultByKpiList(List<String> kpiList, String year) {
        Map<String, List<Object[]>> map = new HashMap<String, List<Object[]>>();
        Map<String, List<Object[]>> resultmap = new HashMap<String, List<Object[]>>();
        if (null != kpiList) {
            StringBuffer inbuf = new StringBuffer("");
            String companyid = UserContext.getUser().getCompanyid();
            for (String kpiid : kpiList) {
                if (StringUtils.isNotBlank(kpiid)) {
                    inbuf.append("'").append(kpiid).append("',");
                }
            }
            if (inbuf.length() > 0) {
                StringBuffer selectBuf = new StringBuffer();
                StringBuffer fromLeftJoinBuf = new StringBuffer();
                StringBuffer wherebuf = new StringBuffer();
                StringBuffer orderbuf = new StringBuffer();
                List<Object> paralist = new ArrayList<Object>();

                selectBuf
                        .append(" select kpi.id  id, kpi.kpi_name  name , kpi.gather_frequence, result.target_value  targetValue , result.finish_value  finishValue ,time.start_time  starttime ,time.end_time  endtime , result.assessment_value  assessmentValue ,statusdict.dict_entry_value  status ,dirdict.dict_entry_value  direction ");
                fromLeftJoinBuf.append(" from t_kpi_kpi  kpi ");
                fromLeftJoinBuf.append(" left outer join ");
                fromLeftJoinBuf.append(" (t_kpi_kpi_gather_result  result  ");
                fromLeftJoinBuf.append(" inner join ");
                fromLeftJoinBuf.append(" t_com_time_period  time on result.time_period_id=time.id  ");
                if (StringUtils.isNotBlank(year)) {
                    fromLeftJoinBuf.append("  and time.eyear=?");
                    paralist.add(year);
                }
                fromLeftJoinBuf.append(" ) on kpi.id=result.kpi_id and kpi.gather_frequence=time.etype ");
                fromLeftJoinBuf.append(" left outer join t_sys_dict_entry  statusdict on result.assessment_status = statusdict.id ");
                fromLeftJoinBuf.append(" left outer join t_sys_dict_entry  dirdict on result.direction = dirdict.id ");
                wherebuf.append(" where kpi.delete_status=true and kpi.is_kpi_category='KPI'  and 1=1 ");
                wherebuf.append(" and kpi.id in ( ").append(inbuf.substring(0, inbuf.length() - 1)).append(" ) ");
                if (StringUtils.isNotBlank(companyid)) {
                    wherebuf.append(" and kpi.company_id='").append(companyid).append("' ");
                }
                orderbuf.append(" order by id, starttime asc ");
                Object[] paraobjects = new Object[paralist.size()];
                paraobjects = paralist.toArray(paraobjects);
                SQLQuery sqlquery = o_kpiGatherResultDAO.createSQLQuery(selectBuf.append(fromLeftJoinBuf).append(wherebuf).append(orderbuf)
                        .toString(), paraobjects);
                List<Object[]> list = sqlquery.list();
                if (null != list) {
                    for (Object[] objects : list) {
                        String kpiid = (String) objects[0];
                        String kpiName = (String) objects[1];
                        String frequence = (String) objects[2];
                        Object targetValue = objects[3];
                        Object finishValue = objects[4];
                        String key = kpiid + "@" + frequence;
                        Object[] valueObjects = new Object[] { kpiName, frequence, targetValue, finishValue };
                        if (!map.containsKey(key)) {
                            List<Object[]> valueList = new ArrayList<Object[]>();
                            valueList.add(valueObjects);
                            map.put(key, valueList);
                        }
                        else {
                            map.get(key).add(valueObjects);
                        }
                    }
                    for (Map.Entry<String, List<Object[]>> m : map.entrySet()) {
                        String key = m.getKey();
                        List<Object[]> values = m.getValue();
                        String[] arr = key.split("@");
                        String frequence = arr[1];
                        if ("0frequecy_month".equals(frequence)) {
                            if (null != values && values.size() != 12) {
                                values = null;
                            }
                        }
                        else if ("0frequecy_quarter".equals(frequence)) {
                            if (null != values && values.size() != 4) {
                                values = null;
                            }
                        }
                        else if ("0frequecy_halfyear".equals(frequence)) {
                            if (null != values && values.size() != 2) {
                                values = null;
                            }
                        }
                        resultmap.put(key, values);
                    }
                }
            }

        }
        return resultmap;
    }

    /**
     * 根据指标ID集合查询当年所在月份周的指标采集结果数据(供图表使用)
     * 
     * @param kpiList
     *            指标ID集合
     * @param year
     *            年份信息
     * @return Map<String,List<Object[]>> key:指标ID , value:list<object[]>
     *         ,object:{}
     */
    public Map<String, List<Object[]>> findCurrentYearWeekGatherResultByKpiList(List<String> kpiList, String year) {
        Map<String, List<Object[]>> map = new HashMap<String, List<Object[]>>();
        Map<String, List<Object[]>> resultmap = new HashMap<String, List<Object[]>>();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        int month = calendar.get(Calendar.MONTH) + 1;
        if (null != kpiList) {
            StringBuffer inbuf = new StringBuffer("");
            String companyid = UserContext.getUser().getCompanyid();
            for (String kpiid : kpiList) {
                if (StringUtils.isNotBlank(kpiid)) {
                    inbuf.append("'").append(kpiid).append("',");
                }
            }
            if (inbuf.length() > 0) {
                StringBuffer selectBuf = new StringBuffer();
                StringBuffer fromLeftJoinBuf = new StringBuffer();
                StringBuffer wherebuf = new StringBuffer();
                StringBuffer orderbuf = new StringBuffer();
                List<Object> paralist = new ArrayList<Object>();

                selectBuf
                        .append(" select kpi.id  id, kpi.kpi_name  name , kpi.gather_frequence, result.target_value  targetValue , result.finish_value  finishValue ,time.start_time  starttime ,time.end_time  endtime , result.assessment_value  assessmentValue ,statusdict.dict_entry_value  status ,dirdict.dict_entry_value  direction ");
                fromLeftJoinBuf.append(" from t_kpi_kpi  kpi ");
                fromLeftJoinBuf.append(" left outer join ");
                fromLeftJoinBuf.append(" (t_kpi_kpi_gather_result  result  ");
                fromLeftJoinBuf.append(" inner join ");
                fromLeftJoinBuf.append(" t_com_time_period  time on result.time_period_id=time.id  ");
                if (StringUtils.isNotBlank(year)) {
                    fromLeftJoinBuf.append("  and time.eyear=? and time.emonth='").append(month).append("' ");
                    paralist.add(year);
                }
                fromLeftJoinBuf.append(" ) on kpi.id=result.kpi_id and kpi.gather_frequence=time.etype ");
                fromLeftJoinBuf.append(" left outer join t_sys_dict_entry  statusdict on result.assessment_status = statusdict.id ");
                fromLeftJoinBuf.append(" left outer join t_sys_dict_entry  dirdict on result.direction = dirdict.id ");
                wherebuf.append(" where kpi.delete_status=true and kpi.is_kpi_category='KPI'  and 1=1 ");
                wherebuf.append(" and kpi.id in ( ").append(inbuf.substring(0, inbuf.length() - 1)).append(" ) ");
                if (StringUtils.isNotBlank(companyid)) {
                    wherebuf.append(" and kpi.company_id='").append(companyid).append("' ");
                }
                orderbuf.append(" order by id, starttime asc ");
                Object[] paraobjects = new Object[paralist.size()];
                paraobjects = paralist.toArray(paraobjects);
                SQLQuery sqlquery = o_kpiGatherResultDAO.createSQLQuery(selectBuf.append(fromLeftJoinBuf).append(wherebuf).append(orderbuf)
                        .toString(), paraobjects);
                List<Object[]> list = sqlquery.list();
                if (null != list) {
                    for (Object[] objects : list) {
                        String kpiid = (String) objects[0];
                        String kpiName = (String) objects[1];
                        String frequence = (String) objects[2];
                        Object targetValue = objects[3];
                        Object finishValue = objects[4];
                        String key = kpiid + "@" + frequence;
                        Object[] valueObjects = new Object[] { kpiName, frequence, targetValue, finishValue };
                        if (!map.containsKey(key)) {
                            List<Object[]> valueList = new ArrayList<Object[]>();
                            valueList.add(valueObjects);
                            map.put(key, valueList);
                        }
                        else {
                            map.get(key).add(valueObjects);
                        }
                    }
                    for (Map.Entry<String, List<Object[]>> m : map.entrySet()) {
                        String key = m.getKey();
                        List<Object[]> values = m.getValue();
                        String[] arr = key.split("@");
                        String frequence = arr[1];
                        if ("0frequecy_month".equals(frequence)) {
                            if (null != values && values.size() != 12) {
                                values = null;
                            }
                        }
                        else if ("0frequecy_quarter".equals(frequence)) {
                            if (null != values && values.size() != 4) {
                                values = null;
                            }
                        }
                        else if ("0frequecy_halfyear".equals(frequence)) {
                            if (null != values && values.size() != 2) {
                                values = null;
                            }
                        }
                        resultmap.put(key, values);
                    }
                }
            }

        }
        return resultmap;
    }

    /**
     * 根据指标ID集合和机构类型查询指标采集结果
     * 
     * @param kpiList
     *            指标ID集合
     * @param orgtype
     *            机构类型 G,T
     * @param empid
     *            员工id
     * @param start
     * @param limit
     * @param date
     *            日期
     * @return Map<String, Object>
     */
    public Map<String, Object> findKpiRelaInfoByKpiIdsAndType(List<String> kpiList, String orgtype, String empid, int start, int limit, Date date) {
        Map<String, Object> rmap = new HashMap<String, Object>();
        Map<String, List<Object[]>> resultmap = new HashMap<String, List<Object[]>>();

        String companyid = UserContext.getUser().getCompanyid();
        if (null == date) {
            date = new Date();
        }
        String dateStr = DateUtils.formatDate(date, "yyyy-MM-dd");

        StringBuffer countBuf = new StringBuffer();
        countBuf.append(" select count(*) ");
        StringBuffer selectbuf = new StringBuffer();
        selectbuf
                .append(" select kpi.id  kpiid ,kpi.kpi_name,kpi.is_target_formula  istargetformula,kpi.is_result_formula  isresultformula  ,unitsdict.dict_entry_name  units,result.id  gatherid ,result.target_value  targetvalue, result.finish_value  finishvalue ,");
        selectbuf
                .append(" result2.finish_value  prefinishvalue,statusdict.dict_entry_value  status,dirdict.dict_entry_value  direction,o.org_name  targetorgname,e.emp_name  targetempname ,oe.etype  targettype, e.id  targetempid,");
        selectbuf
                .append(" o2.org_name  gartherorgname ,e2.emp_name  gartherempname,oe2.etype  gatheretype,e2.id  gatherempid,result.begin_time,result.end_time,result.temp_target_value,result.temp_finish_value  ");
        StringBuffer fromBuf = new StringBuffer();
        fromBuf.append(" from ");
        StringBuffer joinbuf = new StringBuffer();
        joinbuf.append(" t_kpi_kpi  kpi  ");
        joinbuf.append(" left outer join ");
        joinbuf.append(" ( ");
        joinbuf.append(" t_kpi_kpi_gather_result  result ");
        joinbuf.append(" inner join ");
        joinbuf.append(" t_com_time_period  time ");
        joinbuf.append(" on result.time_period_id=time.id ");
        joinbuf.append(" ) ");
        joinbuf.append(" on kpi.id=result.kpi_id ");
        joinbuf.append(" and kpi.gather_frequence=time.etype  ");
        joinbuf.append(" left outer join t_sys_dict_entry  unitsdict ");
        joinbuf.append(" on kpi.UNITS=unitsdict.ID  ");
        joinbuf.append(" left outer join ( ");
        joinbuf.append(" t_kpi_kpi_rela_org_emp  oe  ");
        joinbuf.append(" inner join ");
        joinbuf.append(" t_sys_organization  o ");
        joinbuf.append(" on oe.ORG_ID=o.id ");
        joinbuf.append(" inner join ");
        joinbuf.append(" t_sys_employee  e ");
        joinbuf.append(" on oe.EMP_ID=e.id  )");
        joinbuf.append(" on kpi.id=oe.KPI_ID and oe.ETYPE='T' and (result.target_set_status='").append(Contents.STATUS_SAVED)
                .append("' or result.target_set_status is null) ");
        joinbuf.append(" left outer join ( ");
        joinbuf.append(" t_kpi_kpi_rela_org_emp  oe2 ");
        joinbuf.append(" inner join ");
        joinbuf.append(" t_sys_organization  o2 ");
        joinbuf.append(" on oe2.ORG_ID=o2.id ");
        joinbuf.append(" inner join ");
        joinbuf.append(" t_sys_employee  e2 ");
        joinbuf.append(" on oe2.emp_id=e2.id ");
        joinbuf.append(" ) ");
        joinbuf.append(" on kpi.id=oe2.KPI_ID and  oe2.ETYPE='G' and (result.result_gather_status='").append(Contents.STATUS_SAVED)
                .append("' or result.result_gather_status is null) ");
        joinbuf.append(" left outer join t_kpi_kpi_gather_result result2 on time.PRE_PERIOD_ID=result2.TIME_PERIOD_ID and result2.KPI_ID=kpi.id ");
        joinbuf.append(" left outer join t_sys_dict_entry  statusdict  on result2.assessment_status = statusdict.id  ");
        joinbuf.append(" left outer join t_sys_dict_entry  dirdict on result2.direction = dirdict.id ");
        StringBuffer whereBuf = new StringBuffer();
        whereBuf.append(" where to_days('").append(dateStr).append("')>=to_days(result.BEGIN_TIME) and to_days('").append(dateStr)
                .append("')<= to_days(result.END_TIME) and kpi.delete_status=1 and 1=1 ");
        whereBuf.append(" and kpi.company_id='").append(companyid).append("' ");
        StringBuffer inbuf = new StringBuffer("");
        if (null != kpiList) {

            for (String kpiid : kpiList) {
                if (StringUtils.isNotBlank(kpiid)) {
                    inbuf.append("'").append(kpiid).append("',");
                }
            }
            if (inbuf.length() > 0) {
                whereBuf.append(" and kpi.id in ( ").append(inbuf.substring(0, inbuf.length() - 1)).append(" ) ");
            }

        }
        if (StringUtils.isNotBlank(orgtype)) {
            if ("T".equals(orgtype)) {
                whereBuf.append(" and oe.ETYPE='T' ");
                whereBuf.append(" and result.target_value is null ");
                whereBuf.append(" and e.id='").append(empid).append("' and kpi.IS_TARGET_FORMULA<>'0sys_use_formular_formula' ");
            }
            else if ("G".equals(orgtype)) {
                whereBuf.append(" and oe2.ETYPE='G' ");
                whereBuf.append(" and result.finish_value is null ");
                whereBuf.append(" and e2.id='").append(empid).append("' and kpi.IS_RESULT_FORMULA<>'0sys_use_formular_formula' ");
            }
        }
        else {
            whereBuf.append(" and ((oe.ETYPE='T' and result.TARGET_VALUE is null and to_days('").append(dateStr)
                    .append("')=to_days(kpi.TARGET_CALCULATE_TIME) and oe.EMP_ID='").append(empid)
                    .append("' and kpi.IS_TARGET_FORMULA<>'0sys_use_formular_formula')");
            whereBuf.append(" or (oe2.ETYPE='G' and result.FINISH_VALUE is null and to_days('").append(dateStr)
                    .append("')=to_days(kpi.CALCULATE_TIME) and oe2.EMP_ID='").append(empid)
                    .append("' and kpi.IS_RESULT_FORMULA<>'0sys_use_formular_formula'))");
        }
        StringBuffer orderbuf = new StringBuffer();
        orderbuf.append(" order by kpi.kpi_name ");

        String countSql = countBuf.append(fromBuf).append(joinbuf).append(whereBuf).toString();
        SQLQuery sqlcountquery = o_kpiGatherResultDAO.createSQLQuery(countSql);
        rmap.put("totalCount", sqlcountquery.uniqueResult());

        String sql = selectbuf.append(fromBuf).append(joinbuf).append(whereBuf).append(orderbuf).toString();
        SQLQuery sqlquery = o_kpiGatherResultDAO.createSQLQuery(sql);

        sqlquery.setFirstResult((start - 1) * limit);// 设置分页
        sqlquery.setMaxResults(limit);
        List<Object[]> list = sqlquery.list();
        if (null != list) {
            for (Object[] objects : list) {
                String kpiid = (String) objects[0];
                if (!resultmap.containsKey(kpiid)) {
                    List<Object[]> valueList = new ArrayList<Object[]>();
                    valueList.add(objects);
                    resultmap.put(kpiid, valueList);
                }
                else {
                    resultmap.get(kpiid).add(objects);
                }
            }
        }
        rmap.put("datas", resultmap);
        return rmap;
    }

    /**
     * @param kpilist
     *            指标ID集合
     * @return Map<String,String> key:指标id value:指标对应的xml
     */
    public Map<String, String> findKpiChartsByKpiIds(List<String> kpilist) {
        Map<String, String> resultMap = new HashMap<String, String>();
        List<String> weekKpiList = new ArrayList<String>();
        List<String> otherKpiList = new ArrayList<String>();
        List<Kpi> kpiList = o_kpiBO.findKpiListByIds(kpilist);
        if (null != kpiList) {
            for (Kpi kpi : kpiList) {
                if (null != kpi && null != kpi.getGatherFrequence()) {
                    String frequence = kpi.getGatherFrequence().getId();
                    if ("0frequecy_week".equals(frequence)) {
                        weekKpiList.add(kpi.getId());
                    }
                    else {
                        otherKpiList.add(kpi.getId());
                    }
                }
            }
        }

        Map<String, List<Object[]>> othermap = findCurrentYearGatherResultByKpiList(otherKpiList, DateUtils.getYear(new Date()));
        resultMap = MSColumnLine3D.createChartXml(othermap);
        Map<String, List<Object[]>> weekmap = findCurrentYearWeekGatherResultByKpiList(weekKpiList, DateUtils.getYear(new Date()));
        Map<String, String> weekXmlMap = MSColumnLine3D.createChartXml(weekmap);
        if (null != weekXmlMap) {
            for (Map.Entry<String, String> m : weekXmlMap.entrySet()) {
                resultMap.put(m.getKey(), m.getValue());
            }
        }
        return resultMap;
    }

    /**
     * 根据指标采集结果ID集合查询出采集结果集合.
     * 
     * @param idList
     *            指标采集结果id集合
     * @return List<KpiGatherResult>指标采集结果列表
     */
    public List<KpiGatherResult> findKpiGatherResultByIds(List<String> idList) {
        Criteria criteria = o_kpiGatherResultDAO.createCriteria();
        criteria.add(Restrictions.in("id", idList));
        return criteria.list();
    }

    /**
     * 根据empID查询无状态或保存状态下的采集结果.
     * 
     * @author 吴德福
     * @param empId
     *            员工ID
     * @param date
     *            日期
     * @return List<KpiGatherResult>指标采集结果列表
     */
    public List<KpiGatherResult> findKpiGatherResultByEmpId(String empId, Date date) {
        String dateStr = DateUtils.formatDate(date, "yyyy-MM-dd");
        Criteria criteria = o_kpiGatherResultDAO.createCriteria();
        criteria.createAlias("kpi", "k");

        criteria.add(Restrictions.or(
                Restrictions.and(Restrictions.eq("targetValueInputEmp.id", empId),
                        Restrictions.or(Restrictions.eq("targetSetStatus", Contents.STATUS_SAVED), Restrictions.isNull("targetSetStatus"))),
                Restrictions.and(Restrictions.eq("resultValueInputEmp.id", empId),
                        Restrictions.or(Restrictions.eq("finishSetStatus", Contents.STATUS_SAVED), Restrictions.isNull("finishSetStatus")))));

        StringBuilder sql = new StringBuilder();
        sql.append(" to_days('").append(dateStr).append("')>=to_days(BEGIN_TIME) and to_days('").append(dateStr).append("')<= to_days(END_TIME) ");
        criteria.add(Restrictions.sqlRestriction(sql.toString()));

        criteria.add(Restrictions.eq("k.deleteStatus", true));

        return criteria.list();
    }

    /**
     * 根据empID查询需录入的指标采集结果(目标值或完成值为空).
     * 
     * @author 吴德福
     * @param empId
     *            员工id
     * @param date
     *            日期
     * @return List<Object[]>
     */
    public List<Object[]> findNeedKpiGatherResultListByEmpId(String empId, Date date) {
        String dateStr = DateUtils.formatDate(date, "yyyy-MM-dd");
        StringBuffer sb = new StringBuffer();
        sb.append("select oe.EMP_ID,k.COMPANY_ID,k.id,k.KPI_NAME,k.CALCULATE_TIME,k.TARGET_CALCULATE_TIME,oe.ETYPE,oe.ORG_ID,r.id  rid,r.BEGIN_TIME,r.END_TIME,r.TARGET_VALUE,r.FINISH_VALUE ");
        sb.append("from t_kpi_kpi k,t_kpi_kpi_gather_result r,t_kpi_kpi_rela_org_emp oe,t_com_time_period p ");
        sb.append("where k.id=r.KPI_ID and k.id=oe.KPI_ID ");
        sb.append("and to_days('").append(dateStr).append("')>=to_days(r.BEGIN_TIME) and to_days('").append(dateStr)
                .append("')<= to_days(r.END_TIME) and k.delete_status=1 ");
        sb.append("and r.TIME_PERIOD_ID=p.id and k.gather_frequence=p.etype ");
        sb.append("and ((oe.ETYPE='T' and r.TARGET_VALUE is null and to_days('")
                .append(dateStr)
                .append("')=to_days(k.TARGET_CALCULATE_TIME) and k.IS_TARGET_FORMULA<>'0sys_use_formular_formula') or (oe.ETYPE='G' and r.FINISH_VALUE is null and to_days('")
                .append(dateStr).append("')=to_days(k.CALCULATE_TIME) and k.IS_RESULT_FORMULA<>'0sys_use_formular_formula')) ");
        sb.append("and oe.EMP_ID='").append(empId).append("' ");
        sb.append("order by k.KPI_NAME ");
        return o_kpiGatherResultDAO.createSQLQuery(sb.toString()).list();
    }

    /**
     * 根据empID查询已录入的指标采集结果(保存状态).
     * 
     * @author 吴德福
     * @param empId
     *            员工id
     * @param date
     * @return List<Object[]>
     */
    public List<Object[]> findFinishKpiGatherResultListByEmpId(String empId, Date date) {
        String dateStr = DateUtils.formatDate(date, "yyyy-MM-dd");
        StringBuffer sb = new StringBuffer();
        sb.append("select result.TEMP_TARGET_VALUE,result.TEMP_FINISH_VALUE,result.input_emp,result.result_input_emp,oe.EMP_ID,oe2.EMP_ID,kpi.COMPANY_ID,kpi.id,kpi.KPI_NAME,kpi.CALCULATE_TIME,kpi.TARGET_CALCULATE_TIME,oe.ETYPE,result.id  rid,result.BEGIN_TIME,result.END_TIME,result.TARGET_VALUE,result.FINISH_VALUE ");
        sb.append("from t_kpi_kpi kpi ");
        sb.append("left outer join ( ");
        sb.append("t_kpi_kpi_gather_result  result inner join t_com_time_period  time on result.time_period_id=time.id ");
        sb.append(") on kpi.id=result.kpi_id and kpi.gather_frequence=time.etype ");
        sb.append("left outer join ( ");
        sb.append("t_kpi_kpi_rela_org_emp  oe inner join t_sys_employee  e on oe.EMP_ID=e.id ");
        sb.append(") on kpi.id=oe.KPI_ID and oe.ETYPE='T' and (result.target_set_status='S') ");
        sb.append("left outer join ( ");
        sb.append("t_kpi_kpi_rela_org_emp  oe2 inner join t_sys_employee  e2 on oe2.emp_id=e2.id ");
        sb.append(") on kpi.id=oe2.KPI_ID and  oe2.ETYPE='G' and (result.result_gather_status='S') ");

        sb.append("where  to_days('").append(dateStr).append("')>=to_days(result.BEGIN_TIME) and to_days('").append(dateStr)
                .append("')<= to_days(result.END_TIME) and kpi.delete_status=1 ");
        sb.append("and kpi.company_id='").append(UserContext.getUser().getCompanyid()).append("'");
        sb.append("and (");
        sb.append("(result.input_emp='").append(empId).append("' ").append("and result.target_set_status='").append("S").append("')");
        sb.append(" or ");
        sb.append("(result.result_input_emp='").append(empId).append("' ").append("and result.result_gather_status='").append("S").append("')");
        sb.append(") ");
        sb.append("order by kpi.KPI_NAME ");

        return o_kpiGatherResultDAO.createSQLQuery(sb.toString()).list();
    }

    /**
     * 根据目标名称查询当期的指标采集结果
     * 
     * @param name
     *            目标名称
     * @return List<Objectp[]> 当期指标采集结果
     */
    public List<Object[]> findSmRelaCurrentResultBySmId(String name) {
        List<Object[]> list = null;
        String companyid = UserContext.getUser().getCompanyid();
        if (StringUtils.isNotBlank(name) && !"undefined".equals(name)) {
            StringBuffer selectBuf = new StringBuffer();
            StringBuffer fromLeftJoinBuf = new StringBuffer();
            StringBuffer wherebuf = new StringBuffer();
            List<Object> paralist = new ArrayList<Object>();
            selectBuf
                    .append(" select kpi.id  id, kpi.kpi_name  name , time.time_period_full_name  timerange , result.assessment_value  assessmentValue ,result.target_value  targetValue , result.finish_value  finishValue ,statusdict.dict_entry_value  status ,dirdict.dict_entry_value  direction ");
            fromLeftJoinBuf.append(" from t_kpi_kpi  kpi ");
            fromLeftJoinBuf.append(" left outer join ");
            fromLeftJoinBuf.append(" (t_kpi_kpi_gather_result  result  ");
            fromLeftJoinBuf.append(" inner join ");
            fromLeftJoinBuf.append(" t_com_time_period  time on result.time_period_id=time.id  ");
            fromLeftJoinBuf.append(" ) on kpi.id=result.kpi_id and kpi.gather_frequence=time.etype ");
            fromLeftJoinBuf.append(" left outer join t_sys_dict_entry  statusdict on result.assessment_status = statusdict.id ");
            fromLeftJoinBuf.append(" left outer join t_sys_dict_entry  dirdict on result.direction = dirdict.id ");
            fromLeftJoinBuf.append(" left outer join t_kpi_sm_rela_kpi  smrelakpi on smrelakpi.kpi_id=kpi.id ");
            fromLeftJoinBuf.append(" left outer join t_kpi_strategy_map  sm on sm.id=smrelakpi.strategy_map_id ");
            wherebuf.append(" where kpi.delete_status=true and kpi.is_kpi_category='KPI' and to_days(now())>=to_days(result.begin_time) and to_days(now())<= to_days(result.end_time) and 1=1 ");
            if (StringUtils.isNotBlank(name)) {
                wherebuf.append(" and sm.strategy_map_name=? ");
                paralist.add(name);
            }
            if (StringUtils.isNotBlank(companyid)) {
                wherebuf.append(" and kpi.company_id='").append(companyid).append("' ");
            }
            Object[] paraobjects = new Object[paralist.size()];
            paraobjects = paralist.toArray(paraobjects);
            SQLQuery sqlquery = o_kpiGatherResultDAO.createSQLQuery(selectBuf.append(fromLeftJoinBuf).append(wherebuf).toString(), paraobjects);
            list = sqlquery.list();
        }
        return list;
    }

    /**
     * 根据记分卡名称查询当期的指标采集结果
     * 
     * @param name
     *            记分卡名称
     * @return List<Objectp[]> 当期指标采集结果
     */
    public List<Object[]> findCategoryRelaCurrentResultBycategoryName(String name) {
        List<Object[]> list = null;
        String companyid = UserContext.getUser().getCompanyid();
        if (StringUtils.isNotBlank(name) && !"undefined".equals(name)) {
            StringBuffer selectBuf = new StringBuffer();
            StringBuffer fromLeftJoinBuf = new StringBuffer();
            StringBuffer wherebuf = new StringBuffer();
            List<Object> paralist = new ArrayList<Object>();
            selectBuf
                    .append(" select kpi.id  id, kpi.kpi_name  name , time.time_period_full_name  timerange , result.assessment_value  assessmentValue ,result.target_value  targetValue , result.finish_value  finishValue ,statusdict.dict_entry_value  status ,dirdict.dict_entry_value  direction ");
            fromLeftJoinBuf.append(" from t_kpi_kpi  kpi ");
            fromLeftJoinBuf.append(" left outer join ");
            fromLeftJoinBuf.append(" (t_kpi_kpi_gather_result  result  ");
            fromLeftJoinBuf.append(" inner join ");
            fromLeftJoinBuf.append(" t_com_time_period  time on result.time_period_id=time.id  ");
            fromLeftJoinBuf.append(" ) on kpi.id=result.kpi_id and kpi.gather_frequence=time.etype ");
            fromLeftJoinBuf.append(" left outer join t_sys_dict_entry  statusdict on result.assessment_status = statusdict.id ");
            fromLeftJoinBuf.append(" left outer join t_sys_dict_entry  dirdict on result.direction = dirdict.id ");
            fromLeftJoinBuf.append(" left outer t_kpi_kpi_rela_category  categoryrela on categoryrela.kpi_id=kpi.id ");
            fromLeftJoinBuf.append(" left outer join t_com_category  category on category.id=categoryrela.category_id ");
            wherebuf.append(" where kpi.delete_status=true and kpi.is_kpi_category='KPI' and to_days(now())>=to_days(result.begin_time) and to_days(now())<= to_days(result.end_time) and 1=1 ");
            if (StringUtils.isNotBlank(name)) {
                wherebuf.append(" and category.category_name=? ");
                paralist.add(name);
            }
            if (StringUtils.isNotBlank(companyid)) {
                wherebuf.append(" and kpi.company_id='").append(companyid).append("' ");
            }
            Object[] paraobjects = new Object[paralist.size()];
            paraobjects = paralist.toArray(paraobjects);
            SQLQuery sqlquery = o_kpiGatherResultDAO.createSQLQuery(selectBuf.append(fromLeftJoinBuf).append(wherebuf).toString(), paraobjects);
            list = sqlquery.list();
        }
        return list;
    }

    /**
     * 查询所有的指标相关信息.
     * 
     * @author 吴德福
     * @return List<Object[]>
     */
    public List<Object[]> findAllKpiRelaInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("select oe.EMP_ID,k.COMPANY_ID,k.id,k.KPI_NAME,k.CALCULATE_TIME,k.TARGET_CALCULATE_TIME,oe.ETYPE,oe.ORG_ID,r.id  rid,r.BEGIN_TIME,r.END_TIME,r.TARGET_VALUE,r.FINISH_VALUE ");
        sb.append("from t_kpi_kpi k,t_kpi_kpi_gather_result r,t_kpi_kpi_rela_org_emp oe,t_com_time_period p ");
        sb.append("where k.id=r.KPI_ID and k.id=oe.KPI_ID ");
        sb.append("and to_days(now())>=to_days(r.BEGIN_TIME) and to_days(now())<= to_days(r.END_TIME) and k.delete_status=1 ");
        sb.append("and r.TIME_PERIOD_ID=p.id and k.gather_frequence=p.etype ");
        sb.append("and ((oe.ETYPE='T' and r.TARGET_VALUE is null and to_days(now())=to_days(k.TARGET_CALCULATE_TIME) and k.IS_TARGET_FORMULA<>'0sys_use_formular_formula') or (oe.ETYPE='G' and r.FINISH_VALUE is null and to_days(now())=to_days(k.CALCULATE_TIME) and k.IS_RESULT_FORMULA<>'0sys_use_formular_formula')) ");
        sb.append("and oe.EMP_ID is not null ");
        sb.append("order by k.KPI_NAME ");
        return o_kpiGatherResultDAO.createSQLQuery(sb.toString()).list();
    }

    /**
     * 根据记分卡id和时间区间维id查询指标的指定类型值.
     * 
     * @param categoryId
     * @param valueType
     * @param timePeriodId
     * @return List<Object[]>
     */
    public List<Object[]> findKpiGatherResultListByCategoryId(String categoryId, String valueType, String timePeriodId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select k.kpi_name,");
        if ("评估值".equals(valueType)) {
            sb.append("r.assessment_value,");
        }
        sb.append("c.category_name,k.gather_frequence,k.id,c.id,r.id,p.id,p.start_time,p.end_time ");
        sb.append("from t_kpi_kpi_gather_result r,t_kpi_kpi k,t_kpi_kpi_rela_category kc,t_com_category c,t_com_time_period p ");
        sb.append("where r.kpi_id=k.id and k.id=kc.kpi_id and kc.category_id=c.id and r.time_period_id=p.id and k.gather_frequence=p.etype ");

        // 当前只计算记分卡下的度量指标频率为'月'的值
        sb.append("and k.gather_frequence='0frequecy_month' and k.delete_status=true and k.is_kpi_category='KPI' ");
        if (StringUtils.isNotBlank(categoryId)) {
            sb.append("and c.id='").append(categoryId).append("' ");
        }
        if (StringUtils.isNotBlank(timePeriodId)) {
            sb.append("and r.time_period_id='").append(timePeriodId).append("' ");
        }
        else {
            Date date = new Date();
            String dateStr = DateUtils.formatDate(date, "yyyy-MM-dd");
            sb.append("and p.start_time<='").append(dateStr).append("' ");
            sb.append("and p.end_time>='").append(dateStr).append("' ");
        }

        return o_kpiGatherResultDAO.createSQLQuery(sb.toString()).list();
    }

    /**
     * 根据战略目标id和时间区间维id查询指标的指定类型值.
     * 
     * @param categoryId
     * @param valueType
     * @param timePeriodId
     * @return List<Object[]>
     */
    public List<Object[]> findKpiGatherResultListByStrategyId(String strategyId, String valueType, String timePeriodId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select k.KPI_NAME,");
        if ("评估值".equals(valueType)) {
            sb.append("r.assessment_value,");
        }
        sb.append("sm.STRATEGY_MAP_NAME,k.GATHER_FREQUENCE,k.id,sm.id,r.id,p.id,p.START_TIME,p.END_TIME ");
        sb.append("from t_kpi_kpi_gather_result r, t_kpi_kpi k, T_KPI_SM_RELA_KPI ks, t_kpi_strategy_map sm, t_com_time_period p ");
        sb.append("where r.KPI_ID=k.id and k.id=ks.KPI_ID and ks.STRATEGY_MAP_ID=sm.id and r.TIME_PERIOD_ID=p.ID and k.GATHER_FREQUENCE=p.ETYPE ");
        // 当前只计算记分卡下的度量指标频率为'月'的值
        sb.append("and k.GATHER_FREQUENCE='0frequecy_month' and k.delete_status=true and k.is_kpi_category='KPI' ");
        if (StringUtils.isNotBlank(strategyId)) {
            sb.append("and sm.id='").append(strategyId).append("' ");
        }
        if (StringUtils.isNotBlank(timePeriodId)) {
            sb.append("and r.TIME_PERIOD_ID='").append(timePeriodId).append("' ");
        }
        else {
            Date date = new Date();
            String dateStr = DateUtils.formatDate(date, "yyyy-MM-dd");
            sb.append("and p.START_TIME<='").append(dateStr).append("' ");
            sb.append("and p.END_TIME>='").append(dateStr).append("' ");
        }

        return o_kpiGatherResultDAO.createSQLQuery(sb.toString()).list();
    }

    /**根据实时指标名称查询最晚的采集结果
     * @param kpiName 指标名称
     * @return
     */
    public KpiGatherResult findRelaTimeKpiGatherResultBySome(String kpiName){
        DetachedCriteria dc= DetachedCriteria.forEntityName("com.fhd.kpi.entity.KpiGatherResult", "r");
        dc.createAlias("kpi", "kpi");
        dc.setProjection(Projections.max("endTime"));
        dc.add(Restrictions.isNotNull("assessmentStatus"));
        dc.add(Restrictions.eqProperty("re.kpi", "r.kpi"));
        
        Criteria criteria = this.o_kpiGatherResultDAO.getSession().createCriteria(KpiGatherResult.class, "re");
        criteria.createAlias("kpi", "kpi");
        criteria.add(Restrictions.eq("kpi.name", kpiName));
        criteria.add(Subqueries.propertyEq("re.endTime", dc));
        List<KpiGatherResult> list = criteria.list();
        if(null!=list&&list.size()>0){
            return list.get(0);
        }
        return null;
    }
    
    
    /**根据指标名称和时间区间维度id查询指标采集结果
     * @param kpiName 指标名称
     * @param timePeriod 时间区间纬度字符串
     * @return
     */
    public KpiGatherResult findKpiGatherResultBySome(String kpiName, String timePeriod) {
        Criteria criteria = this.o_kpiGatherResultDAO.createCriteria();
        criteria.createAlias("kpi", "kpi");
        criteria.createAlias("timePeriod", "timePeriod");
        criteria.add(Restrictions.eq("kpi.name", kpiName));
        criteria.add(Restrictions.eqProperty("kpi.gatherFrequence.id", "timePeriod.type"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        if (StringUtils.isNotBlank(timePeriod)) {
            TimePeriod timeperiod = o_timePeriodBO.findTimePeriodById(timePeriod);
            Date startTime = timeperiod.getStartTime();
            Date endTime = timeperiod.getEndTime();
            String timestr = sdf.format(endTime);
            try {
                criteria.add(Restrictions.ge("endTime",sdf.parse(timestr)));
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
            criteria.add(Restrictions.le("beginTime", startTime));
        }
        else {
            criteria.add(Restrictions.eqProperty("timePeriod", "kpi.lastTimePeriod"));
            //当前时间
            /*Date currentDate = new Date();
            criteria.add(Restrictions.ge("endTime", currentDate));
            criteria.add(Restrictions.le("beginTime", currentDate));*/
        }
        List<KpiGatherResult> list = criteria.list();
        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
    
    /**根据指标名称和时间区间维度id查询指标采集结果
     * @param kpiName 指标名称
     * @param timePeriod 时间区间纬度字符串
     * @return
     */
    public KpiGatherResult findKpiGatherResultById(String kpiId, String timePeriod) {
        Criteria criteria = this.o_kpiGatherResultDAO.createCriteria();
        criteria.createAlias("kpi", "kpi");
        criteria.createAlias("timePeriod", "timePeriod");
        criteria.add(Restrictions.eq("kpi.id", kpiId));
        criteria.add(Restrictions.eqProperty("kpi.gatherFrequence.id", "timePeriod.type"));
        if (StringUtils.isNotBlank(timePeriod)) {
            
            criteria.add(Restrictions.eq("timePeriod.id", timePeriod));
        }
        else {//当前时间
            Date currentDate = new Date();
            criteria.add(Restrictions.ge("endTime", currentDate));
            criteria.add(Restrictions.le("beginTime", currentDate));
        }
        List<KpiGatherResult> list = criteria.list();
        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**根据记分卡名称和时间区间维度id查询度量指标采集结果
     * @param categoryName 记分卡名称
     * @param timePeriod 时间纬度字符串
     * @return
     */
    public List<KpiGatherResult> findKpiGatherResultListBySome(String categoryName, String timePeriod) {
        List<Kpi> kpiList = o_categoryBO.findChildKpiByCategoryName(categoryName);
        Criteria criteria = this.o_kpiGatherResultDAO.createCriteria();
        criteria.createAlias("kpi", "kpi");
        criteria.createAlias("timePeriod", "timePeriod");
        criteria.add(Restrictions.eqProperty("kpi.gatherFrequence.id", "timePeriod.type"));
        if (StringUtils.isNotBlank(timePeriod)) {
            criteria.add(Restrictions.eq("timePeriod.id", timePeriod));
        }
        else {//当前时间
            criteria.add(Restrictions.eqProperty("timePeriod", "kpi.lastTimePeriod"));
            /*Date currentDate = new Date();
            criteria.add(Restrictions.ge("endTime", currentDate));
            criteria.add(Restrictions.le("beginTime", currentDate));*/
        }
        criteria.add(Restrictions.in("kpi", kpiList));
        return criteria.list();
    }
    
    /**根据时间范围查找采集结果
     * @param kpiId 指标ID
     * @param startTime 开始时间
     * @param endTime　结束时间
     * @return
     */
    public List<KpiGatherResult> findKpiGatherResultByTimeRange(String kpiId,Date startTime,Date endTime){
        Criteria criteria = this.o_kpiGatherResultDAO.createCriteria();
        criteria.createAlias("kpi", "kpi");
        criteria.createAlias("timePeriod", "timePeriod");
        criteria.add(Restrictions.eqProperty("kpi.gatherFrequence.id", "timePeriod.type"));
        criteria.add(Restrictions.eq("kpi.id", kpiId));
        criteria.add(Restrictions.ge("endTime", startTime));
        criteria.add(Restrictions.le("endTime", endTime));
        criteria.addOrder(Order.asc("timePeriod"));
        return  criteria.list();
    }
    
    /**根据指标ID查询最大的采集频率时间区间维
     * @param kpiId 指标ID
     * @return
     */
    public TimePeriod findMaxKpiTimePeriod(String kpiId){
        DetachedCriteria dc= DetachedCriteria.forEntityName("com.fhd.kpi.entity.KpiGatherResult", "r");
        dc.createAlias("kpi", "kpi");
        dc.createAlias("timePeriod", "timePeriod");
        dc.setProjection(Projections.max("endTime"));
        dc.add(Restrictions.isNotNull("assessmentStatus"));
        dc.add(Restrictions.eqProperty("re.kpi", "r.kpi"));
        dc.add(Restrictions.eqProperty("kpi.gatherFrequence.id", "timePeriod.type"));
        
        Criteria criteria = this.o_kpiGatherResultDAO.getSession().createCriteria(KpiGatherResult.class, "re");
        criteria.createAlias("kpi", "kpi");
        criteria.createAlias("timePeriod", "timePeriod");
        criteria.add(Restrictions.eqProperty("kpi.gatherFrequence.id", "timePeriod.type"));
        criteria.add(Restrictions.eq("kpi.id", kpiId));
        criteria.add(Subqueries.propertyEq("re.endTime", dc));
        List<KpiGatherResult> list = criteria.list();
        if(null!=list&&list.size()>0){
            return list.get(0).getTimePeriod();
        }
        return null;
    }
    

    /**根据指标名称和前几期数值得到采集结果;
     * @param kpiName指标名称
     * @param num 前几期数值
     * @return
     */
    public KpiGatherResult findPreKpiGatherResultBySome(String kpiName, int num,String timePeriod) {
        KpiGatherResult result = findKpiGatherResultBySome(kpiName, timePeriod);
        if(null!=result){
            Date date = result.getEndTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String timestr = sdf.format(date);
            String[] timearr = timestr.split("/");
            String timePeriods = new StringBuffer().append(timearr[0]).append("mm").append(timearr[1]).toString();
            String frequence = result.getKpi().getGatherFrequence().getId();
            String convertTimePeriod = convertTimePeriodBySome(frequence, timePeriods, num);
            KpiGatherResult preResult = findKpiGatherResultBySome(kpiName, convertTimePeriod);
            //#TODO如果状态为空,取指标的最后更新时间所对应的采集数据
            /*if(null!=preResult&&preResult.getAssessmentStatus()==null){
            	 preResult = findKpiGatherResultBySome(kpiName, "");
            }*/
            return preResult;
        }else{
            return null;
        }
        
    }
    
    /**查询时间纬度
     * @param year 年度信息
     * @param frequence　频率
     * @param preNum　向过去推几期
     * @param afterNum　向未来推几期
     * @return
     */
    public  Map<String,Date> findTimeRange(String year,String frequence,int preNum,int afterNum){
        String stime = "";
        String etime = "";
        Map<String,Date> resultMap = new HashMap<String, Date>();
        TimePeriod timeperiod = null;
        if(StringUtils.isBlank(year)){
            Date date = new Date();
            year = DateUtils.getYear(date);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date startTime = null;
        Date endTime = null;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.valueOf(year));
        cal.set(Calendar.MONTH, Integer.valueOf(1) - 1);
        if ("0frequecy_month".equals(frequence)) {
            cal.add(Calendar.MONTH, -preNum);
            String timestr = sdf.format(cal.getTime());
            String[] timearr = timestr.split("/");
            stime = new StringBuffer().append(timearr[0]).append("mm").append(timearr[1]).toString();
            timeperiod = o_timePeriodBO.findTimePeriodById(stime);
            startTime = timeperiod.getStartTime();
            etime = year+"mm12";
            timeperiod = o_timePeriodBO.findTimePeriodById(etime);
            endTime = timeperiod.getEndTime();
        }else if("0frequecy_year".equals(frequence)){
            cal.add(Calendar.YEAR, -preNum);//preNum=1
            String timestr = sdf.format(cal.getTime());
            String[] timearr = timestr.split("/");
            stime = timearr[0];
            timeperiod = o_timePeriodBO.findTimePeriodById(stime);
            startTime = timeperiod.getStartTime();
            cal.add(Calendar.YEAR, preNum+afterNum);//afterNum=2
            timestr = sdf.format(cal.getTime());
            //endTime = cal.getTime();
            timestr = sdf.format(cal.getTime());
            timearr = timestr.split("/");
            timeperiod = o_timePeriodBO.findTimePeriodById(timearr[0]);
            endTime = timeperiod.getEndTime();
        }else if("0frequecy_quarter".equals(frequence)){
            cal.add(Calendar.MONTH, -preNum * 3);
            String timestr = sdf.format(cal.getTime());
            String[] timearr = timestr.split("/");
            Integer quarter = ((Integer.valueOf(timearr[1]) * 3) / 10) + 1;
            stime = new StringBuffer().append(timearr[0]).append("Q").append(quarter).toString();
            timeperiod = o_timePeriodBO.findTimePeriodById(stime);
            startTime = timeperiod.getStartTime();
            cal.add(Calendar.MONTH, preNum * 3+ afterNum*3);
            timestr = sdf.format(cal.getTime());
            timearr = timestr.split("/");
            quarter = ((Integer.valueOf(timearr[1]) * 3) / 10) + 1;
            etime = new StringBuffer().append(timearr[0]).append("Q").append(quarter).toString();
            timeperiod = o_timePeriodBO.findTimePeriodById(etime);
            endTime = timeperiod.getEndTime();
        }else if("0frequecy_halfyear".equals(frequence)){
            cal.add(Calendar.MONTH, -preNum * 6);
            String halfStr = "";
            String timestr = sdf.format(cal.getTime());
            String[] timearr = timestr.split("/");
            if (Integer.valueOf(timearr[1]) > 6) {
                halfStr = "1";
            }
            else {
                halfStr = "0";
            }
            stime = new StringBuffer().append(timearr[0]).append("hf").append(halfStr).toString();
            timeperiod = o_timePeriodBO.findTimePeriodById(stime);
            startTime = timeperiod.getStartTime();
            cal.add(Calendar.MONTH, preNum * 6+ afterNum*6);
            halfStr = "";
            timestr = sdf.format(cal.getTime());
            timearr = timestr.split("/");
            if (Integer.valueOf(timearr[1]) > 6) {
                halfStr = "1";
            }
            else {
                halfStr = "0";
            }
            etime = new StringBuffer().append(timearr[0]).append("hf").append(halfStr).toString();
            timeperiod = o_timePeriodBO.findTimePeriodById(etime);
            endTime = timeperiod.getEndTime();
            
        }
        resultMap.put("startDate", startTime);
        resultMap.put("endDate", endTime);
        return resultMap;
    }
    
    /**删除指标采集结果
     * @param id采集结果id
     */
    @Transactional
    public void deleteKpiGatherResult(String id){
        String sql = " delete from t_kpi_kpi_gather_result where id=?";
        SQLQuery sqlquery = o_kpiGatherResultDAO.createSQLQuery(sql, id);
        sqlquery.executeUpdate();
    }
    
    /**根据月度时间区间维和采集频率得到时间区间维
     * @param frequence 指标采集频率
     * @param timePeriod 月的时间区间纬度ID 都转化为月份
     * @param num 前几期数值
     * @return
     */
    public String convertTimePeriodBySome(String frequence, String timePeriod, int num) {
        String time = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String year = timePeriod.substring(0, 4);
        String month = timePeriod.substring(6);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.valueOf(year));
        cal.set(Calendar.MONTH, Integer.valueOf(month) - 1);
        if ("0frequecy_month".equals(frequence)) {
            cal.add(Calendar.MONTH, -num);
            String timestr = sdf.format(cal.getTime());
            String[] timearr = timestr.split("/");
            time = new StringBuffer().append(timearr[0]).append("mm").append(timearr[1]).toString();
        }
        else if ("0frequecy_year".equals(frequence)) {
            cal.add(Calendar.YEAR, -num);
            String timestr = sdf.format(cal.getTime());
            String[] timearr = timestr.split("/");
            time = timearr[0];
        }
        else if ("0frequecy_halfyear".equals(frequence)) {
            cal.add(Calendar.MONTH, -num * 6);
            String halfStr = "";
            String timestr = sdf.format(cal.getTime());
            String[] timearr = timestr.split("/");
            if (Integer.valueOf(timearr[1]) > 6) {
                halfStr = "1";
            }
            else {
                halfStr = "0";
            }
            time = new StringBuffer().append(timearr[0]).append("hf").append(halfStr).toString();

        }
        else if ("0frequecy_quarter".equals(frequence)) {
            cal.add(Calendar.MONTH, -num * 3);
            String timestr = sdf.format(cal.getTime());
            String[] timearr = timestr.split("/");
            Integer quarter = ((Integer.valueOf(timearr[1]) * 3) / 10) + 1;
            time = new StringBuffer().append(timearr[0]).append("Q").append(quarter).toString();
        }
        return time;
    }

    public static void main(String[] args) {
        //0frequecy_month
        //0frequecy_year
        //0frequecy_halfyear
        //0frequecy_quarter
        // System.out.println(findTimePeriodBySome("0frequecy_month", "2013mm06", 4));
        
        
    }

}
