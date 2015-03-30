package com.fhd.kpi.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fhd.kpi.entity.Kpi;
import com.fhd.kpi.entity.KpiGatherResult;
import com.fhd.sys.entity.dic.DictEntry;

public interface IKpiGatherResultBO
{
    /**
     * 保存指标采集结果.
     * @param kpiGatherResult 指标采集结果实体
     */
    public void saveKpiGatherResult(KpiGatherResult kpiGatherResult);

    /**
     * 根据指标采集结果id查询指标采集结果.
     * @param id 指标采集结果id
     * @return KpiGatherResult
     */
    public KpiGatherResult findKpiGatherResultById(String id);
    
    /**
     * 根据指标采集结果id集合查询指标采集结果列表.
     * @param idList
     * @return List<KpiGatherResult>
     */
    public List<KpiGatherResult> findKpiGatherResultByIds(List<String> idList);
    
    /**
     * 根据录入人查询指标采集结果列表.
	 * @param empId 员工ID
	 * @param date 日期
     * @return List<KpiGatherResult>
     */
    public List<KpiGatherResult> findKpiGatherResultByEmpId(String empId, Date date);

    /**
     * 根据指标id查询指标当期采集结果.
     * @param kpiId 指标id
	 * @param timePeriodId 时间区间维id
     * @return KpiGatherResult
     */
    public KpiGatherResult findCurrentKpiGatherResultByKpiId(String kpiId, String timePeriodId);

    /**
     * 根据指标id查询指标上期采集结果.
     * @param kpiId 指标id
	 * @param timePeriodId 时间区间维id
     * @return KpiGatherResult
     */
    public KpiGatherResult findPreviousKpiGatherResultByKpiId(String kpiId, String timePeriodId);

    /**
     * 根据指标id查询指标当期采集结果所在的季度指标采集结果.
     * @param kpiId 指标id
	 * @param timePeriodId 时间区间维id
     * @return KpiGatherResult
     */
    public KpiGatherResult findQuarterKpiGatherResultByKpiId(String kpiId, String timePeriodId);

    /**
     * 根据指标id查询指标当期采集结果所在的年度指标采集结果.
     * @param kpiId 指标id
	 * @param timePeriodId 时间区间维id
     * @return KpiGatherResult
     */
    public KpiGatherResult findYearKpiGatherResultByKpiId(String kpiId, String timePeriodId);

    /**
     * 根据指标id查询指标所有采集结果.
     * @param kpiId 指标id
     * @return List<KpiGatherResult>
     */
    public List<KpiGatherResult> findKpiGatherResultListByKpiId(String kpiId);

    /**
     * 根据指标名称集合查询对应的当期指标采集结果.
     * @param nameList
	 * @param timePeriodId 时间区间维id
     * @return List<KpiGatherResult>
     */
    public List<KpiGatherResult> findKpiGatherResultListByKpiNames(List<String> nameList, String timePeriodId);

    /**
     * 根据累积值计算项计算季度累积值.
     * @param kpiGatherResult 指标当期采集结果
     * @param calculateItem 累积值计算项
     */
    public Double calculateQuarterAccumulatedValueByCalculateItem(KpiGatherResult kpiGatherResult, String typeSum,
            String calculateItem);

    /**
     * 根据累积值计算项计算年度累积值.
     * @param kpiGatherResult 指标当期采集结果
     * @param typeSum 指标采集结果累积类型值
     * @param calculateItem 累积值计算项
     */
    public Double calculateYearAccumulatedValueByCalculateItem(KpiGatherResult kpiGatherResult, String typeSum,
            String calculateItem);

    /**
     * 根据评估值计算趋势.
     * @param kpiGatherResult 当期指标采集结果
     * @param assessmentValue 当期指标采集结果评估值
     */
    public DictEntry calculateTrendByAssessmentValue(KpiGatherResult kpiGatherResult);

    /**
     * 根据指标名称集合查询对应的当前月份之前的采集结果.
     * @param nameList
     * @return List<KpiGatherResult>
     */
    public Map<String, List<KpiGatherResult>> findKpiGatherResultPreMonthListByKpiNames(List<String> nameList,
            String month);
    
    /**
     * 根据记分卡ID查询所关联的指标所在年的采集结果
     * @param categoryId 记分卡ID
     * @param year 年分
     * @return List<KpiGatherResult>
     */
    public Map<Kpi,List<KpiGatherResult>> findKpiGatherResultByCategoryId(String categoryId,String query,String year);
    
    /**
     * 根据记分卡ID查询所关联的指标所在年的采集结果
     * @param categoryId 记分卡ID
     * @param year 年分
     * @return Map<Kpi, List<Object[]>> object[]数据中的数据顺序{frequence频率,timeperiod时间区间维,finishvalue实际值,status告警状态,direction趋势}
     */
    public Map<String, List<Object[]>> findKpiGatherResultsByCategoryId(String categoryId, String name, String year);
    
    
    /**根据指标ID集合查询当年的指标采集结果数据(供图表使用)
     * @param kpiList 指标ID集合
     * @param year 年份信息
     * @return Map<String,List<Object[]>> key:指标ID , value:list<object[]> ,object:{}
     */
    public Map<String,List<Object[]>> findCurrentYearGatherResultByKpiList(List<String> kpiList,String year);
    
    
    /**根据指标ID集合查询当年所在月份周的指标采集结果数据(供图表使用)
     * @param kpiList 指标ID集合
     * @param year 年份信息
     * @return Map<String,List<Object[]>> key:指标ID , value:list<object[]> ,object:{}
     */
    public Map<String,List<Object[]>> findCurrentYearWeekGatherResultByKpiList(List<String> kpiList,String year);
    
    
    /**根据指标ID集合和机构类型查询指标采集结果
	 * @param kpiList 指标ID集合
	 * @param orgtype 机构类型 G,T
	 * @param empid 员工id
	 * @param start
	 * @param limit
	 * @param date 日期
	 * @return Map<String, Object>
     */
    public Map<String,Object> findKpiRelaInfoByKpiIdsAndType(List<String> kpiList , String orgtype ,String empid,int start, int limit, Date date);
}
