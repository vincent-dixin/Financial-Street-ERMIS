package com.fhd.kpi.business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.comm.business.CategoryBO;
import com.fhd.comm.dao.TimePeriodDAO;
import com.fhd.comm.entity.AlarmPlan;
import com.fhd.comm.entity.AlarmRegion;
import com.fhd.comm.entity.Category;
import com.fhd.comm.entity.TimePeriod;
import com.fhd.core.dao.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.kpi.dao.RelaAssessResultDAO;
import com.fhd.kpi.entity.RelaAssessResult;
import com.fhd.kpi.entity.StrategyMap;
import com.fhd.kpi.web.controller.util.Utils;
import com.fhd.sys.business.dic.DictBO;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * 记分卡，指标类型，战略目标相关评估结果分值表BO.
 * @author   吴德福
 * @version  
 * @since    Ver 1.1
 * @Date     2013-1-7  下午5:25:44
 * @see
 */
@Service
@SuppressWarnings("unchecked")
public class RelaAssessResultBO {

    @Autowired
    private RelaAssessResultDAO o_relaAssessResultDAO;

    @Autowired
    private StrategyMapBO o_strategyMapBO;

    @Autowired
    private CategoryBO o_categoryBO;

    @Autowired
    private TimePeriodDAO o_timePeriodDAO;

    @Autowired
    private DictBO o_DictBO;

    /**
     * 新增相关评估结果分值表.
     * @param relaAssessResult
     */
    @Transactional
    public void saveRelaAssessResult(RelaAssessResult relaAssessResult) {
        o_relaAssessResultDAO.merge(relaAssessResult);
    }

    /**
     * 查询时间纬度表,类型为月.
     * @param year 年份信息
     */
    public List<TimePeriod> findTimePeriodByMonthType(String year) {
        Criteria timeCriteria = this.o_timePeriodDAO.createCriteria();
        timeCriteria.setCacheable(true);// 缓存时间纬度提高效率
        timeCriteria.add(Restrictions.eq("year", String.valueOf(year)));
        timeCriteria.add(Restrictions.eq("type", "0frequecy_month"));
        timeCriteria.addOrder(Order.asc("endTime"));
        timeCriteria.addOrder(Order.desc("startTime"));
        return timeCriteria.list();
    }

    /**
     * 生成计分卡和战略目标采集数据.
     */
    @Transactional
    public void saveBathSMAndSCResultData() {
        String year = DateUtils.getYear(new Date());
        JSONArray smJsonArray = new JSONArray();
        JSONArray scJsonArray = new JSONArray();
        final List<TimePeriod> timeList = findTimePeriodByMonthType(year);
        final List<StrategyMap> smList = o_strategyMapBO.findStrategyMapAll();
        final List<Category> scList = o_categoryBO.findCategoryAll();

        for (StrategyMap strategyMap : smList) {
            JSONObject smJsObj = new JSONObject();
            smJsObj.put("objectId", strategyMap.getId());
            smJsObj.put("objectName", strategyMap.getName());
            smJsonArray.add(smJsObj);
        }
        for (Category category : scList) {
            JSONObject scJsObj = new JSONObject();
            scJsObj.put("objectId", category.getId());
            scJsObj.put("objectName", category.getName());
            scJsonArray.add(scJsObj);
        }

        saveBathResultData(smJsonArray, timeList, "str");

        saveBathResultData(scJsonArray, timeList, "sc");
    }

    /**
     * 生成计分卡和战略目标采集数据.
     * @param list [{objectId:'计分卡或战略目标id',objectName:'计分卡或战略目标名称'}]
     * @param timeList 时间纬度
     * @param dataType 'str':战略目标,'sc':计分卡
     */
    @Transactional
    public void saveBathResultData(final JSONArray list, final List<TimePeriod> timeList, final String dataType) {

        o_relaAssessResultDAO.getSession().doWork(new Work() {
            public void execute(Connection connection) throws SQLException {
                PreparedStatement pst = null;
                final String bathSql = "insert into t_kpi_sm_assess_result (id,object_id,object_name,time_period_id,begin_time,end_time,data_type) values(?,?,?,?,?,?,?)";
                pst = connection.prepareStatement(bathSql);
                for (Object jsobj : list) {
                    JSONObject jobj = ((JSONObject) jsobj);
                    String objectId = jobj.getString("objectId");
                    String objectName = jobj.getString("objectName");
                    List<TimePeriod> mergeTimeList = mergeTimePeriodList(timeList, objectId, dataType);
                    for (TimePeriod timePeriod : mergeTimeList) {
                        pst.setString(1, Identities.uuid());
                        pst.setString(2, objectId);
                        pst.setString(3, objectName);
                        pst.setString(4, timePeriod.getId());
                        pst.setTimestamp(5, new java.sql.Timestamp(timePeriod.getStartTime().getTime()));
                        pst.setTimestamp(6, new java.sql.Timestamp(timePeriod.getEndTime().getTime()));
                        pst.setString(7, dataType);
                        pst.addBatch();
                    }
                }
                pst.executeBatch();
            }
        });

    }

    /**
     * 去掉已经生成的战略目标或是计分卡的采集结果数据的时间纬度.
     * @param timePeriodList 时间纬度列表
     * @param objectID 计分卡或是战略目标ID
     * @param dataType 'str':战略目标,'sc':计分卡
     */
    public List<TimePeriod> mergeTimePeriodList(List<TimePeriod> timePeriodList, String objectID, String dataType) {
        List<TimePeriod> mergeTimePeriodList = new ArrayList<TimePeriod>();
        Criteria criteria = o_relaAssessResultDAO.createCriteria();
        criteria.add(Restrictions.eq("objectId", objectID));
        criteria.add(Restrictions.eq("dataType", dataType));
        List<RelaAssessResult> originalTimePeriodList = criteria.list();
        for (TimePeriod timePeriod : timePeriodList) {
            boolean existFlag = false;
            for (RelaAssessResult okpigatherresult : originalTimePeriodList) {
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
     * 修改相关评估结果分值表.
     * @param relaAssessResult
     */
    @Transactional
    public void mergeRelaAssessResult(RelaAssessResult relaAssessResult) {
        o_relaAssessResultDAO.merge(relaAssessResult);
    }

    /**
     * 根据id删除相关评估结果分值表.
     * @param id
     */
    @Transactional
    public void removeRelaAssessResultById(String id) {
        o_relaAssessResultDAO.delete(id);
    }

    /**
     * 批量删除相关评估结果分值表.
     * @param ids 相关评估结果分值表id集合
     */
    @Transactional
    public void removeRelaAssessResultByIds(String ids) {
        StringBuilder relaAssessResultIds = new StringBuilder();
        String[] idArray = ids.split(",");
        for (int i = 0; i < idArray.length; i++) {
            relaAssessResultIds.append("'").append(idArray[i]).append("'");
            if (i != idArray.length - 1) {
                relaAssessResultIds.append(",");
            }
        }
        o_relaAssessResultDAO.createQuery("delete RelaAssessResult where id in (" + relaAssessResultIds + ")").executeUpdate();
    }

    /**
     * 根据id查询相关评估结果分值表.
     * @param id
     * @return RelaAssessResult
     */
    public RelaAssessResult findRelaAssessResultById(String id) {
        return o_relaAssessResultDAO.get(id);
    }

    /**
     * 根据对象id查询最新相关评估结果分值表.
     * @param objectId 对象id
     * @return RelaAssessResult
     */
    public RelaAssessResult findLastestRelaAssessResultListByObjectId(String objectId) {
        RelaAssessResult relaAssessResult = null;
        Criteria criteria = o_relaAssessResultDAO.createCriteria();
        if (StringUtils.isNotBlank(objectId)) {
            criteria.add(Restrictions.eq("objectId", objectId));
        }
        criteria.add(Restrictions.isNotNull("assessmentValue"));
        criteria.addOrder(Order.desc("beginTime"));
        List<RelaAssessResult> list = criteria.list();
        if (null != list && list.size() > 0) {
            relaAssessResult = list.get(0);
        }
        return relaAssessResult;
    }

    /**
     * 根据查询条件查询相关评估结果分值表.
     * @param objectId 对象id
     * @param objectName 对象名称
     * @param dataType 对象类型
     * @param nameList 对象名称集合
     * @return List<RelaAssessResult>
     */
    public List<RelaAssessResult> findRelaAssessResultListBySome(String objectId, String objectName, String dataType, List<String> nameList) {
        Criteria criteria = o_relaAssessResultDAO.createCriteria();
        if (StringUtils.isNotBlank(objectId)) {
            criteria.add(Restrictions.eq("objectId", objectId));
        }
        if (StringUtils.isNotBlank(objectName)) {
            criteria.add(Restrictions.like("objectName", objectName, MatchMode.ANYWHERE));
        }
        if (StringUtils.isNotBlank(dataType)) {
            if ("category".equals(dataType)) {
                dataType = "sc";
            }
            else if ("strategy".equals(dataType)) {
                dataType = "str";
            }
            criteria.add(Restrictions.eq("dataType", dataType));
        }
        if (null != nameList && nameList.size() > 0) {
            criteria.add(Restrictions.in("objectName", nameList));
        }
        criteria.addOrder(Order.asc("beginTime"));
        return criteria.list();
    }

    /**
     * 根据参数查询记分卡的值.
     * @param targetId 记分卡id
     * @param valueType 值类型
     * @param timePeriodId 时间区间维id
     * @return List<Object[]>
     */
    public List<Object[]> findRelaAssessResultBySubCategory(String targetId, String valueType, String timePeriodId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select c.CATEGORY_NAME,");
        if ("评估值".equals(valueType)) {
            sb.append("r.assessment_value,");
        }
        sb.append("c.id,r.id,p.id,p.START_TIME,p.END_TIME ");
        sb.append("from t_kpi_sm_assess_result r, t_com_category c, t_com_time_period p ");
        sb.append("where r.OBJECT_ID=c.id and r.TIME_PERIOD_ID=p.ID and r.DATA_TYPE='sc' ");
        if (StringUtils.isNotBlank(targetId)) {
            sb.append(" and c.parent_id='").append(targetId).append("' ");
            sb.append(" and c.delete_status='1' ");
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
        return o_relaAssessResultDAO.createSQLQuery(sb.toString()).list();
    }

    /**
     * 根据参数查询战略目标的值.
     * @param targetId 战略目标id
     * @param valueType 值类型
     * @param timePeriodId 时间区间维id
     * @return List<Object[]>
     */
    public List<Object[]> findRelaAssessResultBySubStrategy(String targetId, String valueType, String timePeriodId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select sm.STRATEGY_MAP_NAME,");
        if ("评估值".equals(valueType)) {
            sb.append("r.assessment_value,");
        }
        sb.append("sm.id,r.id,p.id,p.START_TIME,p.END_TIME ");
        sb.append("from t_kpi_sm_assess_result r, t_kpi_strategy_map sm, t_com_time_period p ");
        sb.append("where r.OBJECT_ID=sm.id and r.TIME_PERIOD_ID=p.ID and r.DATA_TYPE='str' ");
        if (StringUtils.isNotBlank(targetId)) {
            sb.append(" and sm.parent_id='").append(targetId).append("' ");
            sb.append(" and sm.delete_status='1' ");
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
        return o_relaAssessResultDAO.createSQLQuery(sb.toString()).list();
    }

    public RelaAssessResult findCurrentRelaAssessResultByType(String objectId, String type, Date date) {
        RelaAssessResult relaassessresult = null;
        Criteria criteria = o_relaAssessResultDAO.createCriteria();
        criteria.add(Restrictions.eq("dataType", type));
        criteria.add(Restrictions.le("beginTime", date));
        criteria.add(Restrictions.ge("endTime", date));
        criteria.add(Restrictions.eq("objectId", objectId));
        List<RelaAssessResult> list = criteria.list();
        if (null != list && list.size() > 0) {
            relaassessresult = list.get(0);
        }
        return relaassessresult;
    }

    /**
     * 根据目标的名称查询它下级的采集结果
     * @param name 目标的名称
     * @return 下级目标的采集结果
     */
    public List<Object[]> findChildRelaAssessResultByStrategyName(String name) {
        StringBuffer selectBuf = new StringBuffer();
        StringBuffer joinBuf = new StringBuffer();
        StringBuffer whereBuf = new StringBuffer();
        selectBuf
                .append(" select sm.strategy_map_name,result.id,result.object_id,result.object_name,result.time_period_id,result.begin_time,result.end_time,result.assessment_value,result.assessment_status, ");
        selectBuf
                .append(" result.assessment_alarm_id,result.forecast_value,result.forecast_status,result.forecast_alarm_id,result.trend,result.data_type ");
        joinBuf.append(" from t_kpi_strategy_map sm ");
        joinBuf.append(" left outer join t_kpi_strategy_map parentsm on sm.parent_id=parentsm.id ");
        joinBuf.append(" left outer join t_kpi_sm_assess_result result on sm.id=result.object_id and to_days(now())>=to_days(result.begin_time) and to_days(now())<= to_days(result.end_time) ");
        whereBuf.append(" where sm.delete_status=1 and result.data_type ='str' and 1=1 ");
        if (StringUtils.isNotBlank(name)) {
            whereBuf.append(" and parentsm.strategy_map_name='").append(name).append("'");
        }
        else {
            whereBuf.append(" and sm.parent_id is null ");
        }
        String sql = selectBuf.append(joinBuf).append(whereBuf).toString();
        SQLQuery sqlquery = o_relaAssessResultDAO.createSQLQuery(sql);
        return sqlquery.list();
    }

    /**
     * 根据目标名称集合查询对应的采集结果
     * @param names 战略目标名称
     * @return List<RelaAssessResult> 战略目标对象的结果
     */
    public List<RelaAssessResult> findSmAssessResultByNames(List<String> names) {
        Date currentDate = new Date();
        Criteria criteria = o_relaAssessResultDAO.createCriteria();
        criteria.add(Restrictions.eq("dataType", "str"));
        criteria.add(Restrictions.in("objectName", names));
        criteria.add(Restrictions.and(Restrictions.ge("beginTime", currentDate), Restrictions.le("endTime", currentDate)));
        return criteria.list();
    }

    /**
     * 根据记分卡的名称查询它下级的采集结果
     * @param name 记分卡的名称
     * @return 下级记分卡的采集结果
     */
    public List<Object[]> findChildRelaRelaAssessResultByCategoryName(String name) {
        StringBuffer selectBuf = new StringBuffer();
        StringBuffer joinBuf = new StringBuffer();
        StringBuffer whereBuf = new StringBuffer();
        selectBuf
                .append(" select category.category_name,result.id,result.object_id,result.object_name,result.time_period_id,result.begin_time,result.end_time,result.assessment_value,result.assessment_status, ");
        selectBuf
                .append(" result.assessment_alarm_id,result.forecast_value,result.forecast_status,result.forecast_alarm_id,result.trend,result.data_type ");
        joinBuf.append(" from t_com_category category ");
        joinBuf.append(" left outer join t_com_category parentcategory on category.parent_id=parentcategory.id ");
        joinBuf.append(" left outer join t_kpi_sm_assess_result result on category.id=result.object_id and to_days(now())>=to_days(result.begin_time) and to_days(now())<= to_days(result.end_time) ");
        whereBuf.append(" where category.delete_status=1 and result.data_type ='sc' and 1=1 ");
        if (StringUtils.isNotBlank(name)) {
            whereBuf.append(" and parentcategory.category_name='").append(name).append("'");
        }
        else {
            whereBuf.append(" and category.parent_id is null ");
        }
        String sql = selectBuf.append(joinBuf).append(whereBuf).toString();
        SQLQuery sqlquery = o_relaAssessResultDAO.createSQLQuery(sql);
        return sqlquery.list();
    }

    /**
     * 根据记分卡名称集合查询对应的采集结果
     * @param names 记分卡名称
     * @return List<RelaAssessResult> 记分卡集合对象对应的结果
     */
    public List<RelaAssessResult> findCategoryAssessResultByNames(List<String> names) {
        Date currentDate = new Date();
        Criteria criteria = o_relaAssessResultDAO.createCriteria();
        criteria.add(Restrictions.eq("dataType", "sc"));
        criteria.add(Restrictions.in("objectName", names));
        criteria.add(Restrictions.and(Restrictions.ge("beginTime", currentDate), Restrictions.le("endTime", currentDate)));
        return criteria.list();
    }

    /**
     * 封装相关评估结果分值表实体.
     * @param relaAssessResult
     * @param objectId 对象id
     * @param objectName 对象名称
     * @param timePeriod 区间维度
     * @param assessmentValue 评估值
     * @param assessmentStatus 评估值状态
     * @param assessmentAlarmPlan 评估值预警方案
     * @param forecastValue 预期值
     * @param forecastStatus 预期值状态
     * @param forecastAlarmPlan 预期值预警方案
     * @param trend 趋势
     * @param dataType 对象类型
     * @return RelaAssessResult
     */
    public RelaAssessResult packageRelaAssessResult(RelaAssessResult relaAssessResult, String objectId, String objectName, TimePeriod timePeriod,
            Double assessmentValue, DictEntry assessmentStatus, AlarmPlan assessmentAlarmPlan, Double forecastValue, DictEntry forecastStatus,
            AlarmPlan forecastAlarmPlan, DictEntry trend, String dataType) {
        relaAssessResult.setId(Identities.uuid2());
        relaAssessResult.setObjectId(objectId);
        relaAssessResult.setObjectName(objectName);
        relaAssessResult.setTimePeriod(timePeriod);
        relaAssessResult.setBeginTime(timePeriod.getStartTime());
        relaAssessResult.setEndTime(timePeriod.getEndTime());
        relaAssessResult.setAssessmentValue(assessmentValue);
        relaAssessResult.setAssessmentStatus(assessmentStatus);
        relaAssessResult.setAssessmentAlarmPlan(assessmentAlarmPlan);
        relaAssessResult.setForecastValue(forecastValue);
        relaAssessResult.setForecastStatus(forecastStatus);
        relaAssessResult.setForcastAlarmPlan(forecastAlarmPlan);
        relaAssessResult.setTrend(trend);
        relaAssessResult.setDataType(dataType);
        return relaAssessResult;
    }

    /**
     * 通过dataType查询评估结果
     * @author 金鹏祥
     * @param dataType
     * @return List<RelaAssessResult>
    */
    public List<RelaAssessResult> findRelaAssessResultByDataType(String dataType, String objectId, boolean isNewValue, String yearId, String monthId) {
        Criteria criteria = o_relaAssessResultDAO.createCriteria();
        if (StringUtils.isNotBlank(dataType)) {
            criteria.add(Restrictions.eq("dataType", dataType));
        }

        criteria.add(Restrictions.eq("objectId", objectId));

        if (isNewValue) {
            criteria.add(Property.forName("timePeriod.id").like(yearId, MatchMode.ANYWHERE));
        }
        else {
            if (StringUtils.isNotBlank(monthId)) {
                //月份
                criteria.add(Restrictions.eq("timePeriod.id", monthId));
            }
            else {
                //年份
                criteria.add(Property.forName("timePeriod.id").like(yearId, MatchMode.ANYWHERE));
            }
        }
        criteria.addOrder(Order.asc("timePeriod.id"));
        return criteria.list();
    }

    /**
     * 通过dataType查询评估结果
     * @author 金鹏祥
     * @param dataType
     * @return List<RelaAssessResult>
    */
    public RelaAssessResult findRelaAssessResultByDataTypeEn(String dataType, String objectId, boolean isNewValue, String yearId, String monthId) {
        Criteria criteria = o_relaAssessResultDAO.createCriteria();
        if (StringUtils.isNotBlank(dataType)) {
            criteria.add(Restrictions.eq("dataType", dataType));
        }

        
        criteria.add(Restrictions.eq("objectId", objectId));

        
        /*if (isNewValue) {
            criteria.add(Property.forName("timePeriod.id").like(yearId, MatchMode.ANYWHERE));
        }
        else {
            if (StringUtils.isNotBlank(monthId)) {
                //月份
                criteria.add(Restrictions.eq("timePeriod.id", monthId));
            }
            else {
                //年份
                criteria.add(Property.forName("timePeriod.id").like(yearId, MatchMode.ANYWHERE));
            }
        }*/
        criteria.add(Restrictions.isNotNull("assessmentValue"));
        criteria.addOrder(Order.desc("timePeriod.id"));
        if (criteria.list().size() != 0) {
            return (RelaAssessResult) criteria.list().get(0);
        }
        else {
            return null;
        }

    }

    /**
     * 通过dataType查询评估结果
     * @author 金鹏祥
     * @param dataType
     * @return List<RelaAssessResult>
    */
    public RelaAssessResult findRelaAssessResultByObjectId(String objectId) {
        Criteria criteria = o_relaAssessResultDAO.createCriteria();
        criteria.add(Restrictions.eq("objectId", objectId));
        criteria.addOrder(Order.desc("timePeriod.id"));
        if (criteria.list().size() != 0) {
            return (RelaAssessResult) criteria.list().get(0);
        }
        else {
            return null;
        }

    }

    /**
    * 查询所有类型最新值
    * @author 金鹏祥
    * @return HashMap<String, String>
    */
    public HashMap<String, String> findAssessmentMaxEntTimeAll(String type, String categoryIds) {
        HashMap<String, String> map = new HashMap<String, String>();
        //List<Object> paralist = new ArrayList<Object>();
        //paralist.add(type);
        StringBuffer sql = new StringBuffer();
        sql.append(" select object_id,assessment_status from  t_kpi_sm_assess_result smresult  ");
        sql.append(" where  end_time=(select max(end_time) from t_kpi_sm_assess_result r where smresult.OBJECT_ID=r.object_id and r.assessment_value is not null)");
        sql.append(" and data_type='").append(type).append("'");
        //sql.append(" and data_type=?");
        if (StringUtils.isNotBlank(categoryIds)) {
            sql.append(" and object_id in(").append(categoryIds).append(")");
            //sql.append(" and object_id in(?)");
            //paralist.add(categoryIds);
        }
        //Object[] paraobjects = new Object[paralist.size()];
        //paraobjects = paralist.toArray(paraobjects);
        SQLQuery sqlquery = o_relaAssessResultDAO.createSQLQuery(sql.toString());

        //设置parameter方式查询不出数据
        // XXX 胡迪新修改 -- 开始
        //sqlquery.setParameter("type", type);
        //sqlquery.setParameterList("categoryIds", StringUtils.split(categoryIds, ","));
        // XXX 胡迪新修改 -- 结束

        List<Object[]> list = sqlquery.list();
        for (Iterator<Object[]> iterator = list.iterator(); iterator.hasNext();) {
            Object[] objects = (Object[]) iterator.next();
            if (objects[1] != null) {
                map.put(objects[0].toString(), objects[1].toString());
            }
            else {
                map.put(objects[0].toString(), null);
            }
        }
        return map;
    }

    /**
     * 查询记分卡或目标的分值集合
     * @param objectId 记分卡或战略目标ID
     * @param type sc:记分卡 sm:战略目标
     * @author 陈晓哲
     * @return List<RelaAssessResult> 记分卡或目标的分值集合
     */
    public Page<RelaAssessResult> findRelaAssessResultsBySome(Page<RelaAssessResult> page, String objectId, String type, String sort, String dir) {
        String sortstr = sort;
        Date currentDate = new Date();
        DetachedCriteria dc = DetachedCriteria.forClass(RelaAssessResult.class);
        dc.add(Restrictions.eq("objectId", objectId));
        dc.add(Restrictions.eq("dataType", type));
        dc.add(Restrictions.le("beginTime", currentDate));
        if ("assessmentStatusStr".equals(sort)) {
            dc.createAlias("assessmentStatus", "assessmentStatus", CriteriaSpecification.LEFT_JOIN);
            sortstr = "assessmentStatus.value";
        }
        else if ("directionstr".equals(sort)) {
            dc.createAlias("trend", "trend", CriteriaSpecification.LEFT_JOIN);
            sortstr = "trend.value";
        }
        else if ("dateRange".equals(sort)) {
            sortstr = "beginTime";
        }

        if ("ASC".equalsIgnoreCase(dir)) {
            dc.addOrder(Order.asc(sortstr));
        }
        else {
            dc.addOrder(Order.desc(sortstr));
        }
        return o_relaAssessResultDAO.findPage(dc, page, false);
    }

    /**根据记分卡名称和时间区间维度id查询下级记分卡评估结果
     * @param categoryName 记分卡名称
     * @param timePeriod 时间区间字符串
     * @return
     */
    public List<RelaAssessResult> findCategoryAssessResultListBySome(String categoryName, String timePeriod){
        List<String> categoryIdList = new ArrayList<String>();
        Category category =  o_categoryBO.findCategoryByName(categoryName);
        String categoryId = category.getId();
        List<Category> childCategorys = o_categoryBO.findChildCategorysByCategoryId(categoryId);
        for (Category childCategory : childCategorys) {
            categoryIdList.add(childCategory.getId());
        }
        Criteria criteria = o_relaAssessResultDAO.createCriteria();
        criteria.add(Restrictions.in("objectId", categoryIdList));
        if(StringUtils.isNotBlank(timePeriod)){
            criteria.add(Restrictions.eq("timePeriod.id", timePeriod));
        }
        else{
            //当前时间
                Date currentDate = new Date();
                criteria.add(Restrictions.ge("endTime", currentDate));
                criteria.add(Restrictions.le("beginTime", currentDate));
        }
        return criteria.list();
    }
    /**根据记分卡名称和时间区间纬度id查询记分卡采集结果
     * @param targetName 记分卡名称
     * @param timePeriod 时间区间纬度ID
     * @return
     */
    public RelaAssessResult  findRelaAssessResultBySome(String targetName,String timePeriod){
        Criteria criteria = o_relaAssessResultDAO.createCriteria();
        if(StringUtils.isNotBlank(timePeriod)){
            criteria.add(Restrictions.eq("timePeriod.id", timePeriod));
        }else{
          //当前时间
            Date currentDate = new Date();
            criteria.add(Restrictions.ge("endTime", currentDate));
            criteria.add(Restrictions.le("beginTime", currentDate));
        }
        criteria.add(Restrictions.eq("objectName", targetName));
        List<RelaAssessResult> list = criteria.list();
        if(null!=list&&list.size()>0){
            return list.get(0);
        }
        return null;
    }
    
    /**
     * 根据评估值计算趋势.
     * 
     * @param relaAssessResult当期记分卡或战略目标评分结果
     *            
     */
    public DictEntry calculateTrendByAssessmentValue(RelaAssessResult relaAssessResult) {
        DictEntry trend = null;
        Double preAssvalue = null;
        String objectId = relaAssessResult.getObjectId();
        TimePeriod prePeriodTime = relaAssessResult.getTimePeriod().getPrePeriod();
        Double assvalue = relaAssessResult.getAssessmentValue();
        Criteria criteria = o_relaAssessResultDAO.createCriteria();
        criteria.add(Restrictions.eq("timePeriod", prePeriodTime));
        criteria.add(Restrictions.eq("objectId", objectId));
        criteria.add(Restrictions.eq("dataType", relaAssessResult.getDataType()));
        List<RelaAssessResult> list = criteria.list();
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

    /**更新记分卡或战略目标的趋势信息
     * @param objectId 记分卡或战略目标ID
     * @param type sc:记分卡 str:战略目标
     */
    @Transactional
    public void mergedRelaAssessResultsTrend(String objectId, String type) {
        DictEntry trend = null;
        Criteria criteria = o_relaAssessResultDAO.createCriteria();
        criteria.add(Restrictions.eq("objectId", objectId));
        criteria.add(Restrictions.eq("dataType", type));
        criteria.addOrder(Order.asc("beginTime"));
        List<RelaAssessResult> assessResults = criteria.list();
        for (RelaAssessResult result : assessResults) {
            trend = calculateTrendByAssessmentValue(result);
            result.setTrend(trend);
            o_relaAssessResultDAO.merge(result);
        }
    }

    /**修改记分卡或战略目标的评分结果
     * @param modifiedRecores 需要修改的评分结果记录
     */
    @Transactional
    public void mergedRelaAssessResults(String modifiedRecores) {
        JSONObject jsobj = JSONObject.fromObject(modifiedRecores);
        String type = (String) jsobj.get("type");
        String objectId = (String) jsobj.get("objectId");
        JSONArray jsonArray = jsobj.getJSONArray("datas");
        if (jsonArray.size() > 0) {
            AlarmPlan alarmPlan = null;
            if ("sc".equals(type)) {
                alarmPlan = o_categoryBO.findAlarmPlanByScId(objectId, Contents.ALARMPLAN_REPORT);
            }
            else if ("str".equals(type)) {
                alarmPlan = o_strategyMapBO.findAlarmPlanBySmId(objectId, Contents.ALARMPLAN_REPORT);
            }
            DictEntry cssicon = null;
            Double assessmentValue = null;
            RelaAssessResult relaAssessResult = null;
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject assessResult = jsonArray.getJSONObject(i);
                String id = assessResult.getString("id");
                String assessmentValuestr = assessResult.getString("assessmentValue");
                if (Utils.isNumeric(assessmentValuestr)) {
                    assessmentValue = Double.valueOf(assessmentValuestr);
                }
                relaAssessResult = this.findRelaAssessResultById(id);
                if (null != alarmPlan) {
                    Set<AlarmRegion> AlarmRegionList = alarmPlan.getAlarmRegions();
                    for (AlarmRegion alarmRegion : AlarmRegionList) {
                        if (null != assessmentValue && assessmentValue >= Double.valueOf(alarmRegion.getMinValue())
                                && assessmentValue <= Double.valueOf(alarmRegion.getMaxValue())) {
                            cssicon = alarmRegion.getAlarmIcon();
                            break;
                        }
                    }
                    relaAssessResult.setAssessmentStatus(cssicon);
                }
                relaAssessResult.setAssessmentValue(assessmentValue);
                o_relaAssessResultDAO.merge(relaAssessResult);
            }
            //更新记分卡或战略目标的趋势信息
            mergedRelaAssessResultsTrend(objectId, type);

        }
    }
}
