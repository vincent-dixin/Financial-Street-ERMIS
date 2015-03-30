/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.kpi.interfaces;

import java.util.Date;
import java.util.List;

import com.fhd.comm.entity.AlarmPlan;
import com.fhd.kpi.entity.Kpi;
import com.fhd.kpi.entity.StrategyMap;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 
 * 增加、修改、删除、查询指标对象
 *
 * @author   胡迪新
 * @since    fhd Ver 4.5
 * @Date	 2012-10-18  上午10:43:09
 *
 * @see
 */
public interface IKpiBO
{

    /**
     * <pre>
     * 保存指标对象
     * </pre>
     * 
     * @author 陈晓哲
     * @param kpi 指标对象
     * @since  fhd　Ver 1.1
    */
    public void mergeKpi(Kpi kpi);

    /**
     * <pre>
     * 根据指标ID获得指标实体
     * </pre>
     * 
     * @author 陈晓哲
     * @param id 指标ID
     * @return
     * @since  fhd　Ver 1.1
    */
    public Kpi findKpiById(String id);

    /**
     * <pre>
     * 根据指标标识获取所有下级指标
     * </pre>
     * 
     * @author 陈晓哲
     * @param id指标标识
     * @param self是否包含自己
     * @return
     * @since  fhd　Ver 1.1
    */
    public List<Kpi> findChildsKpiById(String id, boolean self);

    /**
     * <pre>
     * 根据指标ID查询和指标关联的预警方案
     * </pre>
     * 
     * @author 陈晓哲
     * @param id 指标标识
     * @return
     * @since  fhd　Ver 1.1
    */
    public List<AlarmPlan> findKpiRelaFcAlarmPlanListById(String id);

    /**
     * <pre>
     * 根据指标ID查询和指标关联的告警方案
     * </pre>
     * 
     * @author 陈晓哲
     * @param id
     * @return
     * @since  fhd　Ver 1.1
    */
    public List<AlarmPlan> findKpiRelaRAlarmPlanPlanListById(String id);

    /**
     * <pre>
     * 根据指标标识查询关联的目标集合
     * </pre>
     * 
     * @author 陈晓哲
     * @param id 指标标识
     * @return
     * @since  fhd　Ver 1.1
    */
    public List<StrategyMap> findStrategyMapListById(String id);

    /**
     * <pre>
     * 根据员工ID查询所关联的指标集合
     * </pre>
     * 
     * @author 陈晓哲
     * @param empId
     * @return
     * @since  fhd　Ver 1.1
    */
    public List<Kpi> findKpiListByEmpId(String empId,String type,Date date);

    /**
     * <pre>
     * 根据指标ID查询指标所关联的告警状态
     * </pre>
     * 
     * @author 陈晓哲
     * @param id 指标ID
     * @return
     * @since  fhd　Ver 1.1
    */
    public DictEntry findKpiRelaAssessmentStatusById(String id);

    /**
     * <pre>
     *  根据指标ID查询指标所关联的预警状态
     * </pre>
     * 
     * @author 陈晓哲
     * @param id
     * @return
     * @since  fhd　Ver 1.1
    */
    public DictEntry findKpiRelaForecastStatusById(String id);

    /**
     *  查询所有可用(未删除状态)的指标.
     *  @return List<Kpi>
     */
    public List<Kpi> findAllKpiList();

    /**s
     * 根据指标id和预警类型type获取预警方案.
     * @param kpiId
     * @param type forecast：预警方案;report：告警方案;
     * @return AlarmPlan
     */
    public AlarmPlan findAlarmPlanByKpiId(String kpiId, String type);

    /**
     * 根据评估值获取评估值预警状态.
     * @param alarmPlan
     * @param assessmentValue
     * @return DictEntry 预警状态值(数据字典)
     * 高：icon-ibm-symbol-6-sm
     * 中：icon-ibm-symbol-5-sm
     * 低：icon-ibm-symbol-4-sm
     * 无：""
     */
    public DictEntry findKpiAssessmentStatusByAssessmentValue(AlarmPlan alarmPlan, Double assessmentValue);
    
    /**
     * 得到指标最新的值
     * 
     * @author 金鹏祥
     * @param kpiId
     * @return boolean
    */
    public String getKpiNewValue(String kpiId);
}
