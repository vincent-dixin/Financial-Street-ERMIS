/**
 * CategoryBO.java
 * com.fhd.comm.business
 *   ver     date           author
 * ──────────────────────────────────
 *           2012-11-14         陈晓哲
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
 */

package com.fhd.comm.business;

import static org.hibernate.FetchMode.JOIN;
import static org.hibernate.FetchMode.SELECT;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.in;
import static org.hibernate.criterion.Restrictions.like;
import static org.hibernate.criterion.Restrictions.not;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.quartz.CronTrigger;
import org.quartz.impl.calendar.AnnualCalendar;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.comm.dao.AlarmPlanDAO;
import com.fhd.comm.dao.AlarmRegionDAO;
import com.fhd.comm.dao.CategoryDAO;
import com.fhd.comm.dao.CategoryRelaAlarmDAO;
import com.fhd.comm.dao.CategoryRelaOrgEmpDAO;
import com.fhd.comm.entity.AlarmPlan;
import com.fhd.comm.entity.AlarmRegion;
import com.fhd.comm.entity.Category;
import com.fhd.comm.entity.CategoryRelaAlarm;
import com.fhd.comm.entity.CategoryRelaOrgEmp;
import com.fhd.comm.entity.TimePeriod;
import com.fhd.comm.web.form.CategoryForm;
import com.fhd.core.dao.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.Contents;
import com.fhd.fdc.utils.UserContext;
import com.fhd.kpi.business.KpiBO;
import com.fhd.kpi.business.KpiGatherResultBO;
import com.fhd.kpi.business.RelaAssessResultBO;
import com.fhd.kpi.dao.KpiDAO;
import com.fhd.kpi.dao.KpiRelaAlarmDAO;
import com.fhd.kpi.dao.KpiRelaCategoryDAO;
import com.fhd.kpi.dao.KpiRelaDimDAO;
import com.fhd.kpi.dao.KpiRelaOrgEmpDAO;
import com.fhd.kpi.entity.Kpi;
import com.fhd.kpi.entity.KpiGatherResult;
import com.fhd.kpi.entity.KpiRelaAlarm;
import com.fhd.kpi.entity.KpiRelaCategory;
import com.fhd.kpi.entity.KpiRelaDim;
import com.fhd.kpi.entity.KpiRelaOrgEmp;
import com.fhd.risk.business.RiskEventRelatedService;
import com.fhd.risk.business.RiskEventService;
import com.fhd.risk.entity.KpiRelaRisk;
import com.fhd.risk.entity.Risk;
import com.fhd.risk.entity.RiskEvent;
import com.fhd.risk.web.form.RiskEventForm;
import com.fhd.sys.business.dic.DictBO;
import com.fhd.sys.business.orgstructure.OrganizationBO;
import com.fhd.sys.dao.orgstructure.SysEmployeeDAO;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.orgstructure.SysEmployee;
import com.fhd.sys.entity.orgstructure.SysOrganization;

/**
 * 维度BO
 * 
 * @author 陈晓哲
 * @version
 * @since Ver 1.1
 * @Date 2012-11-14 下午03:24:20
 * 
 * @see
 */
@Service
@SuppressWarnings("unchecked")
public class CategoryBO {
    @Autowired
    private RelaAssessResultBO o_relaAssessResultBO;

    @Autowired
    private CategoryDAO o_categoryDAO;

    @Autowired
    private CategoryRelaOrgEmpDAO o_categoryRelaOrgEmpDAO;

    @Autowired
    private OrganizationBO o_organizationBO;

    @Autowired
    private AlarmPlanDAO o_alarmPlanDAO;

    @Autowired
    private DictBO o_dictEntryBO;

    @Autowired
    private SysEmployeeDAO o_sysEmployeeDAO;

    @Autowired
    private CategoryRelaAlarmDAO o_CategoryRelaAlarmDAO;

    @Autowired
    private KpiRelaCategoryDAO o_kpiRelaCategoryDAO;

    @Autowired
    private KpiBO o_kpiBO;

    @Autowired
    private AlarmPlanBO o_alarmPlanBO;

    @Autowired
    private KpiDAO o_kpiDAO;

    @Autowired
    private KpiRelaOrgEmpDAO o_kpiRelaOrgEmpDAO;

    @Autowired
    private KpiRelaAlarmDAO o_KpiRelaAlarmDAO;

    @Autowired
    private KpiRelaDimDAO o_kpiRelaDimDAO;

    @Autowired
    private AlarmRegionDAO o_alarmRegionDAO;

    @Autowired
    KpiGatherResultBO o_kpiGatherResultBO;
    
    @Autowired
    RiskEventService o_riskEventService;
    
    @Autowired
    RiskEventRelatedService o_riskEventRelatedService;
    /**
     * <pre>
     * 多维树查询
     * </pre>
     * 
     * @author 陈晓哲
     * @param id 记分卡id
     * @param canChecked 是否可以选择
     * @param query 查询条件
     * @return
     * @since fhd　Ver 1.1
     */
    public List<Map<String, Object>> kpiCategoryTreeLoader(String id, Boolean canChecked, String query) {

        Map<String, Object> node = null;
        boolean expanded = false;
        if (StringUtils.isNotBlank(query)) { // 是否展开节点
            expanded = true;
        }
        List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> allNodes = new ArrayList<Map<String, Object>>();
        String companyId = UserContext.getUser().getCompanyid();// 所在公司id
        Set<String> idSet = new HashSet<String>();
        List<String> categoryIdList = new ArrayList<String>();
        if (StringUtils.isNotBlank(id)) {// 根据父节点查询所有子节点
            Criteria criteria = this.o_categoryDAO.createCriteria();
            criteria.add(eq("company.id", companyId));
            if ("category_root".equals(id)) {
                criteria.add(Restrictions.isNull("parent"));
                Map<String, Object> allKpiNode = new HashMap<String, Object>();
                allKpiNode.put("id", "allkpi");
                allKpiNode.put("text", "所有度量指标");
                allKpiNode.put("type", "myfolder");
                allKpiNode.put("leaf", true);
                allKpiNode.put("iconCls", "icon-ibm-icon-allmetrics-list2");
                allNodes.add(allKpiNode);
            }
            else {
                criteria.add(Restrictions.eq("parent.id", id));
            }

            criteria.add(Restrictions.eq("deleteStatus", true));
            criteria.addOrder(Order.asc("name"));
            List<Category> parentCategorys = criteria.list();

            Criteria criteriaQuery = this.o_categoryDAO.createCriteria();
            criteriaQuery.add(eq("company.id", companyId));
            if (StringUtils.isNotBlank(query)) {
                criteriaQuery.add(Restrictions.like("name", query, MatchMode.ANYWHERE));
            }
            criteriaQuery.addOrder(Order.asc("name"));
            List<Category> categoryList = criteriaQuery.list();

            for (Category entity : categoryList) {
                String[] idsTemp = entity.getIdSeq().split("\\.");
                idSet.addAll(Arrays.asList(idsTemp));
            }

            for (Category category : parentCategorys) {
                if (idSet.size() > 0 && idSet.contains(category.getId())) {
                    categoryIdList.add(category.getId());
                    node = wrapCategoryNode(category, false, true, expanded, null);
                    nodes.add(node);
                }
            }
            String categoryIdstr = "";
            StringBuffer categoryBf = new StringBuffer();
            for (String categoryId : categoryIdList) {
                categoryBf.append("'").append(categoryId).append("'").append(",");
            }
            if (categoryBf.length() > 0) {
                categoryIdstr = categoryBf.toString().substring(0, categoryBf.length() - 1);
            }
            if (categoryIdstr.length() > 0) {
                HashMap<String, String> findAssessmentAllMap = o_relaAssessResultBO.findAssessmentMaxEntTimeAll("sc", categoryIdstr);
                for (int i = 0; i < nodes.size(); i++) {
                    node = nodes.get(i);
                    String css = findAssessmentAllMap.get(node.get("id"));
                    String cssImg = "icon-status-disable";
                    if ("0alarm_startus_h".equals(css)) {
                        cssImg = "icon-ibm-symbol-4-sm";
                    }
                    else if ("0alarm_startus_m".equals(css)) {
                        cssImg = "icon-ibm-symbol-5-sm";
                    }
                    else if ("0alarm_startus_l".equals(css)) {
                        cssImg = "icon-ibm-symbol-6-sm";
                    }else if("0alarm_startus_safe".equals(css)){
                        cssImg = "icon-ibm-symbol-safe-sm";
                    }
                    node.put("iconCls", cssImg);
                }
            }
        }
        Collections.sort(nodes, new Comparator<Map<String, Object>>() {

            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String s1 = (String) o1.get("iconCls");
                String s2 = (String) o2.get("iconCls");
                if ("icon-status-high".equals(s1)) {
                    s1 = "high";
                }
                if ("icon-status-high".equals(s2)) {
                    s2 = "high";
                }
                if ("icon-status-mid".equals(s1)) {
                    s1 = "imid";
                }
                if ("icon-status-mid".equals(s2)) {
                    s2 = "imid";
                }
                if ("icon-status-low".equals(s1)) {
                    s1 = "low";
                }
                if ("icon-status-low".equals(s2)) {
                    s2 = "low";
                }
                if ("icon-status-disable".equals(s1)) {
                    s1 = "nothing";
                }
                if ("icon-status-disable".equals(s2)) {
                    s2 = "nothing";
                }
                return (s1).compareTo(s2);
            }
        });
        allNodes.addAll(nodes);
        return allNodes;
    }

    /**
     * <pre>
     * 根据维度id获得维度对象
     * </pre>
     * 
     * @author 陈晓哲
     * @param id 记分卡id
     * @return
     * @since fhd　Ver 1.1
     */
    public Category findCategoryById(String id) {
        Criteria criteria = this.o_categoryDAO.createCriteria();
        criteria.add(Property.forName("id").eq(id));
        criteria.add(Property.forName("deleteStatus").eq(true));
        criteria.setFetchMode("status", SELECT);
        return (Category) criteria.uniqueResult();
    }
    /**
     * <pre>
     * 根据维度名称获得维度对象
     * </pre>
     * 
     * @author 陈晓哲
     * @param name 记分卡名称
     * @return
     * @since fhd　Ver 1.1
     */
    public Category findCategoryByName(String name) {
        Criteria criteria = this.o_categoryDAO.createCriteria();
        criteria.add(Property.forName("name").eq(name));
        criteria.add(Property.forName("deleteStatus").eq(true));
        criteria.setFetchMode("status", SELECT);
        return (Category) criteria.uniqueResult();
    }

    /**
     * <pre>
     * 根据维度信息查找部门人员信息,form赋值
     * </pre>
     * 
     * @author 陈晓哲
     * @param category 记分卡对象
     * @return
     * @since fhd　Ver 1.1
     */
    public JSONObject findCategoryRelaOrgEmpBySmToJson(Category category) {
        JSONObject jsobj = new JSONObject();
        if (null != category) {
            String type = "";
            JSONArray blongArr = new JSONArray();
            JSONArray targetArr = new JSONArray();
            SysEmployee sysEmp = null;
            SysOrganization sysOrg = null;

            Criteria criteria = this.o_categoryRelaOrgEmpDAO.createCriteria();
            criteria.setCacheable(true);
            criteria.add(Restrictions.eq("category.id", category.getId()));
            criteria.setFetchMode("org", JOIN);
            criteria.setFetchMode("emp", JOIN);
            // 不加载无关的关联对象
            criteria.setFetchMode("category.status", FetchMode.SELECT);
            List<CategoryRelaOrgEmp> categoryRelaOrgEmpSet = criteria.list();
            for (CategoryRelaOrgEmp relaOrgEmp : categoryRelaOrgEmpSet) {
                type = relaOrgEmp.getType();
                JSONObject blongobj = new JSONObject();
                sysOrg = relaOrgEmp.getOrg();
                sysEmp = relaOrgEmp.getEmp();
                try {
                    if (null != sysOrg) {
                        blongobj.put("deptid", sysOrg.getId());
                    }
                    else {
                        blongobj.put("deptid", "");
                    }
                }
                catch (ObjectNotFoundException e) {
                    blongobj.put("deptid", "");
                }
                try {
                    if (null != sysEmp) {
                        blongobj.put("empid", sysEmp.getId());
                    }
                    else {
                        blongobj.put("empid", "");
                    }
                }
                catch (ObjectNotFoundException e) {
                    blongobj.put("empid", "");
                }
                if (Contents.BELONGDEPARTMENT.equals(type)) {
                    blongArr.add(blongobj);
                }
                if (Contents.TARGETDEPARTMENT.equals(type)) {
                    targetArr.add(blongobj);
                }
            }
            jsobj.put("ownDept", blongArr);
            jsobj.put("targetDept", targetArr);
        }
        return jsobj;
    }

    /**
     * <pre>
     * 查找维度关联预警信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param name 告警方案或预警方案名称
     * @param id 积分卡id
     * @param sort 排序字段
     * @param dir 排序方向
     * @return
     * @since fhd　Ver 1.1
     */
    public List<CategoryRelaAlarm> findCategoryRelaAlarmBySome(String name, String id, String editflag, String sort, String dir) {
        String sortstr = "id";
        Criteria dc = this.o_CategoryRelaAlarmDAO.createCriteria();
        dc.createAlias("category", "category");
        dc.add(Restrictions.eq("category.company.id", UserContext.getUser().getCompanyid()));
        if (StringUtils.isNotBlank(name)) {
            dc.createAlias("fcAlarmPlan", "fcAlarmPlan");
            dc.createAlias("rAlarmPlan", "rAlarmPlan");
            dc.add(Restrictions.or(Property.forName("fcAlarmPlan.name").like(name, MatchMode.ANYWHERE),
                    Property.forName("rAlarmPlan.name").like(name, MatchMode.ANYWHERE)));
        }
        if (StringUtils.isNotBlank(sort) && !StringUtils.equals("id", sort)) {
            dc.createAlias("fcAlarmPlan", "fcAlarmPlan");
            dc.createAlias("rAlarmPlan", "rAlarmPlan");
            if (StringUtils.equals("alarm", sort)) {
                sortstr = "rAlarmPlan.name";
            }
            else if (StringUtils.equals("warning", sort)) {
                sortstr = "fcAlarmPlan.name";
            }
            else if (StringUtils.equals("date", sort)) {
                sortstr = "startDate";
            }
        }
        if (StringUtils.isNotBlank(id)) {
            dc.add(Property.forName("category.id").eq(id));
        }
        if ("ASC".equalsIgnoreCase(dir)) {
            dc.addOrder(Order.asc(sortstr));
        }
        else {
            dc.addOrder(Order.desc(sortstr));
        }
        List<CategoryRelaAlarm> list = dc.list();
        if (!"true".equals(editflag) && null != list && list.size() == 0) {
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.set(Integer.valueOf(DateUtils.getYear(new Date())), 0, 1);
            // 判断,如果没有结果集,则默认添加全局告警信息
            AlarmPlan forecast = o_alarmPlanBO.findAlarmPlanByNameType("常用告警方案", "0alarm_type_kpi_forecast");
            AlarmPlan alarm = o_alarmPlanBO.findAlarmPlanByNameType("常用预警方案", "0alarm_type_kpi_alarm");
            if (null != forecast || null != alarm) {
                list = new ArrayList<CategoryRelaAlarm>();
                CategoryRelaAlarm kpiRelaAlarm = new CategoryRelaAlarm();
                kpiRelaAlarm.setId("");
                kpiRelaAlarm.setrAlarmPlan(forecast);
                kpiRelaAlarm.setFcAlarmPlan(alarm);
                kpiRelaAlarm.setStartDate(calendar.getTime());
                list.add(kpiRelaAlarm);
            }
        }
        return list;
    }

    /**
     * 根据记分卡ID查询具体的关联指标的采集结果
     * 
     * @param map
     * @param start
     *            分页起始位置
     * @param limit
     *            显示记录数
     * @param queryName
     *            指标类型名称
     * @param sortColumn
     *            排序字段
     * @param dir
     *            排序方向
     * @param id
     *            记分卡ID
     * @param frequence
     *            指标采集频率
     * @return
     */
    public List<Object[]> findSpecificCategoryRelaKpiResultsBySome(Map<String, Object> map, int start, int limit, String queryName,
            String sortColumn, String dir, String id, String year, String quarter, String month, String week, String frequence) {

        List<Object[]> list = null;
        String companyid = UserContext.getUser().getCompanyid();
        if (StringUtils.isNotBlank(id) && !"undefined".equals(id)) {
            StringBuffer selectBuf = new StringBuffer();
            StringBuffer fromLeftJoinBuf = new StringBuffer();
            StringBuffer wherebuf = new StringBuffer();
            StringBuffer countBuf = new StringBuffer();
            StringBuffer orderbuf = new StringBuffer();
            List<Object> paralist = new ArrayList<Object>();
            selectBuf
                    .append(" select kpi.id  id, kpi.kpi_name  name , time.time_period_full_name  timerange , result.assessment_value  assessmentValue , ");
            selectBuf
                    .append(" result.target_value  targetValue , result.finish_value  finishValue ,statusdict.dict_entry_value  status ,dirdict.dict_entry_value  direction , category.is_creator   creator,kpi.is_enabled is_enabled ");
            countBuf.append(" select count(*) ");
            fromLeftJoinBuf.append(" from t_kpi_kpi  kpi ");
            fromLeftJoinBuf.append(" left outer join ");
            fromLeftJoinBuf.append(" (t_kpi_kpi_gather_result   result  ");
            fromLeftJoinBuf.append(" inner join ");
            fromLeftJoinBuf.append(" t_com_time_period   time on result.time_period_id=time.id  ");
            if (StringUtils.isNotBlank(year) && StringUtils.isBlank(quarter) && StringUtils.isBlank(month) && StringUtils.isBlank(week)) {
                fromLeftJoinBuf.append("  and time.id=?");
                paralist.add(year);
            }
            if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(quarter) && StringUtils.isBlank(month)) {
                fromLeftJoinBuf.append("  and time.id=?");
                paralist.add(quarter);
            }
            if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(month) && StringUtils.isNotBlank(quarter) && StringUtils.isBlank(week)) {
                fromLeftJoinBuf.append("  and time.id=?");
                paralist.add(month);
            }
            if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(month) && StringUtils.isNotBlank(quarter) && StringUtils.isNotBlank(week)) {
                fromLeftJoinBuf.append("  and time.id=?");
                paralist.add(week);
            }
            fromLeftJoinBuf.append(" ) on kpi.id=result.kpi_id and kpi.gather_frequence=time.etype ");
            fromLeftJoinBuf.append(" left outer join t_sys_dict_entry   statusdict on result.assessment_status = statusdict.id ");
            fromLeftJoinBuf.append(" left outer join t_sys_dict_entry   dirdict on result.direction = dirdict.id ");
            fromLeftJoinBuf.append(" left outer join t_kpi_kpi_rela_category   category on category.kpi_id=kpi.id ");

            wherebuf.append(" where kpi.delete_status=true and kpi.is_kpi_category='KPI'  and 1=1 ");
            if (StringUtils.isNotBlank(id)) {
                wherebuf.append(" and category.category_id=? ");
                paralist.add(id);
            }
            if (StringUtils.isNotBlank(companyid)) {
                wherebuf.append(" and kpi.company_id=? ");
                paralist.add(companyid);
            }

            if (StringUtils.isNotBlank(queryName)) {
                wherebuf.append(" and kpi.kpi_name like '%").append(queryName).append("%'");
            }
            Object[] paraobjects = new Object[paralist.size()];
            paraobjects = paralist.toArray(paraobjects);
            SQLQuery countQuery = o_categoryDAO.createSQLQuery(countBuf.append(fromLeftJoinBuf).append(wherebuf).toString(), paraobjects);
            map.put("totalCount", countQuery.uniqueResult());

            if (StringUtils.isNotBlank(sortColumn)) {
                orderbuf.append(" order by ");
                if (StringUtils.equals("name", sortColumn)) {
                    orderbuf.append("kpi.kpi_name");
                }
                else if (StringUtils.equals("id", sortColumn)) {
                    orderbuf.append("kpi.id");
                }
                else if (StringUtils.equals("finishValue", sortColumn)) {
                    orderbuf.append("finishValue");
                }
                else if (StringUtils.equals("targetValue", sortColumn)) {
                    orderbuf.append("targetValue");
                }
                else if (StringUtils.equals("assessmentValue", sortColumn)) {
                    orderbuf.append("assessmentValue");
                }
                else if (StringUtils.equals("assessmentStatus", sortColumn)) {
                    orderbuf.append("status");
                }
                else if (StringUtils.equals("directionstr", sortColumn)) {
                    orderbuf.append("direction");
                }
                else if (StringUtils.equals("dateRange", sortColumn)) {
                    orderbuf.append("timerange");
                }
                if ("ASC".equalsIgnoreCase(dir)) {
                    orderbuf.append(" asc ");
                }
                else {
                    orderbuf.append(" desc ");
                }
            }

            if (orderbuf.length() > 0) {
                orderbuf.append(" , kpi.kpi_name ");
            }
            SQLQuery sqlquery = o_categoryDAO.createSQLQuery(selectBuf.append(fromLeftJoinBuf).append(wherebuf).append(orderbuf).toString(),
                    paraobjects);
            sqlquery.setFirstResult(start);
            sqlquery.setMaxResults(limit);
            list = sqlquery.list();
        }
        return list;
    }

    /**
     * 根据记分卡ID查询具体的关联指标的采集结果(供图表使用)
     * 
     * @param map
     * @param start
     *            分页起始位置
     * @param limit
     *            显示记录数
     * @param queryName
     *            指标类型名称
     * @param sortColumn
     *            排序字段
     * @param dir
     *            排序方向
     * @param id
     *            记分卡ID
     * @param frequence
     *            指标采集频率
     * @return
     */
    public List<Object[]> findSpecificCategoryRelaKpiResultsChartBySome(Map<String, Object> map, int start, int limit, String queryName,
            String sortColumn, String dir, String id, String year, String quarter, String month, String week, String frequence, String tableType) {

        List<Object[]> list = null;
        String companyid = UserContext.getUser().getCompanyid();
        if (StringUtils.isNotBlank(id) && !"undefined".equals(id)) {
            StringBuffer selectBuf = new StringBuffer();
            StringBuffer fromLeftJoinBuf = new StringBuffer();
            StringBuffer wherebuf = new StringBuffer();
            StringBuffer countBuf = new StringBuffer();
            StringBuffer orderbuf = new StringBuffer();
            List<Object> paralist = new ArrayList<Object>();
            selectBuf
                    .append(" select kpi.id  id, kpi.kpi_name  name , time.time_period_full_name  timerange , result.assessment_value  assessmentValue ,result.target_value");
            selectBuf
                    .append("  targetValue , result.finish_value  finishValue ,statusdict.dict_entry_value  status ,dirdict.dict_entry_value  direction ,time.pre_period_id,time.pre_year_period_id ");
            countBuf.append(" select count(*) ");
            fromLeftJoinBuf.append(" from t_kpi_kpi   kpi ");
            fromLeftJoinBuf.append(" left outer join ");
            fromLeftJoinBuf.append(" (t_kpi_kpi_gather_result   result  ");
            fromLeftJoinBuf.append(" inner join ");
            fromLeftJoinBuf.append(" t_com_time_period   time on result.time_period_id=time.id  ");
            if (StringUtils.isNotBlank(year) && StringUtils.isBlank(quarter) && StringUtils.isBlank(month) && StringUtils.isBlank(week)) {
                fromLeftJoinBuf.append("  and time.id=?");
                paralist.add(year);
            }
            if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(quarter) && StringUtils.isBlank(month)) {
                fromLeftJoinBuf.append("  and time.id=?");
                paralist.add(quarter);
            }
            if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(month) && StringUtils.isNotBlank(quarter) && StringUtils.isBlank(week)) {
                fromLeftJoinBuf.append("  and time.id=?");
                paralist.add(month);
            }
            if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(month) && StringUtils.isNotBlank(quarter) && StringUtils.isNotBlank(week)) {
                fromLeftJoinBuf.append("  and time.id=?");
                paralist.add(week);
            }
            fromLeftJoinBuf.append(" ) on kpi.id=result.kpi_id and kpi.gather_frequence=time.etype ");
            fromLeftJoinBuf.append(" left outer join t_sys_dict_entry   statusdict on result.assessment_status = statusdict.id ");
            fromLeftJoinBuf.append(" left outer join t_sys_dict_entry   dirdict on result.direction = dirdict.id ");

            if ("sc".equalsIgnoreCase(tableType)) {
                fromLeftJoinBuf.append(" left outer join t_kpi_kpi_rela_category   category on category.kpi_id=kpi.id ");
            }
            else if ("str".equalsIgnoreCase(tableType)) {
                fromLeftJoinBuf.append(" left outer join t_kpi_sm_rela_kpi  sm on sm.kpi_id=kpi.id ");
            }

            wherebuf.append(" where kpi.delete_status=true and kpi.is_kpi_category='KPI'  and 1=1 ");
            if (StringUtils.isNotBlank(id)) {

                if ("sc".equalsIgnoreCase(tableType)) {
                    wherebuf.append(" and category.category_id=? ");
                    paralist.add(id);
                }
                else if ("str".equalsIgnoreCase(tableType)) {
                    wherebuf.append(" and sm.strategy_map_id=? ");
                    paralist.add(id);
                }

            }
            if (StringUtils.isNotBlank(companyid)) {
                wherebuf.append(" and kpi.company_id=? ");
                paralist.add(companyid);
            }

            if (StringUtils.isNotBlank(queryName)) {
                wherebuf.append(" and kpi.kpi_name like '%").append(queryName).append("%'");
            }
            Object[] paraobjects = new Object[paralist.size()];
            paraobjects = paralist.toArray(paraobjects);
            SQLQuery countQuery = o_categoryDAO.createSQLQuery(countBuf.append(fromLeftJoinBuf).append(wherebuf).toString(), paraobjects);
            map.put("totalCount", countQuery.uniqueResult());

            if (StringUtils.isNotBlank(sortColumn)) {
                orderbuf.append(" order by ");
                if (StringUtils.equals("name", sortColumn)) {
                    orderbuf.append("kpi.kpi_name");
                }
                else if (StringUtils.equals("id", sortColumn)) {
                    orderbuf.append("kpi.id");
                }
                else if (StringUtils.equals("finishValue", sortColumn)) {
                    orderbuf.append("finishValue");
                }
                else if (StringUtils.equals("targetValue", sortColumn)) {
                    orderbuf.append("targetValue");
                }
                else if (StringUtils.equals("assessmentValue", sortColumn)) {
                    orderbuf.append("assessmentValue");
                }
                else if (StringUtils.equals("assessmentStatus", sortColumn)) {
                    orderbuf.append("status");
                }
                else if (StringUtils.equals("directionstr", sortColumn)) {
                    orderbuf.append("direction");
                }
                else if (StringUtils.equals("dateRange", sortColumn)) {
                    orderbuf.append("timerange");
                }
                if ("ASC".equalsIgnoreCase(dir)) {
                    orderbuf.append(" asc ");
                }
                else {
                    orderbuf.append(" desc ");
                }
            }
            if (orderbuf.length() > 0) {
                orderbuf.append(" , kpi.kpi_name ");
            }
            StringBuffer sqlbuf = new StringBuffer();
            sqlbuf.append(" select t.id,t.name,t.timerange,t.assessmentValue,t.targetValue,t.finishValue,t.status,t.direction,");
            sqlbuf.append(" (select finish_value from t_kpi_kpi_gather_result ks where t.pre_period_id=ks.time_period_id and t.id=ks.kpi_id)   pre_finishvalue,");
            sqlbuf.append(" (select finish_value from t_kpi_kpi_gather_result ks where t.pre_year_period_id=ks.time_period_id and t.id=ks.kpi_id)   preyear_finishvalue  ");
            sqlbuf.append(" from ( ").append(selectBuf).append(fromLeftJoinBuf).append(wherebuf).append(orderbuf).append(" )  t ");
            SQLQuery sqlquery = o_categoryDAO.createSQLQuery(sqlbuf.toString(), paraobjects);
            sqlquery.setFirstResult(start);
            sqlquery.setMaxResults(limit);
            list = sqlquery.list();
        }
        return list;
    }

    /**
     * 根据记分卡ID查询最新的关联指标的采集结果(供图表使用)
     * 
     * @param map
     * @param start
     *            分页起始位置
     * @param limit
     *            显示记录数
     * @param queryName
     *            指标类型名称
     * @param sortColumn
     *            排序字段
     * @param dir
     *            排序方向
     * @param id
     *            记分卡ID
     * @return
     */
    public List<Object[]> findLastCategoryRelaKpiResultsByChartSome(Map<String, Object> map, int start, int limit, String queryName, String id,
            String sortColumn, String dir, String tableType) {
        List<Object[]> list = null;
        String companyid = UserContext.getUser().getCompanyid();
        if (StringUtils.isNotBlank(id) && !"undefined".equals(id)) {
            StringBuffer selectBuf = new StringBuffer();
            StringBuffer fromLeftJoinBuf = new StringBuffer();
            StringBuffer countBuf = new StringBuffer();
            StringBuffer orderbuf = new StringBuffer();
            List<Object> paralist = new ArrayList<Object>();
            selectBuf
                    .append(" select kpi.id   id, kpi.kpi_name   name , time.time_period_full_name   timerange , result.assessment_value   assessmentValue ,");
            selectBuf
                    .append("result.target_value   targetValue , result.finish_value   finishValue ,statusdict.dict_entry_value   status ,dirdict.dict_entry_value   direction ,time.pre_period_id,time.pre_year_period_id ");

            countBuf.append(" select count(*) ");

            fromLeftJoinBuf
                    .append(" from t_kpi_kpi  kpi left outer join t_kpi_kpi_gather_result  result on kpi.id=result.kpi_id and kpi.latest_time_period_id=result.time_period_id ");
            fromLeftJoinBuf.append(" left outer join t_com_time_period  time on result.time_period_id=time.id ");
            fromLeftJoinBuf.append(" left outer join t_sys_dict_entry  statusdict on result.assessment_status = statusdict.id ");
            fromLeftJoinBuf.append(" left outer join t_sys_dict_entry  dirdict on result.direction = dirdict.id ");
            if ("sc".equalsIgnoreCase(tableType)) {
                fromLeftJoinBuf.append(" left outer join t_kpi_kpi_rela_category  category on category.kpi_id=kpi.id ");
            }
            else if ("str".equalsIgnoreCase(tableType)) {
                fromLeftJoinBuf.append(" left outer join t_kpi_sm_rela_kpi  sm on sm.kpi_id=kpi.id ");
            }
            fromLeftJoinBuf.append(" where kpi.delete_status=? and kpi.is_kpi_category=?  and 1=1 ");

            // XXX 胡迪新修改 -- 开始
            paralist.add(true);
            paralist.add("KPI");
            // XXX 胡迪新修改 -- 结束

            if (StringUtils.isNotBlank(id)) {
                if ("sc".equalsIgnoreCase(tableType)) {
                    fromLeftJoinBuf.append(" and category.category_id=? ");
                    paralist.add(id);
                }
                else if ("str".equalsIgnoreCase(tableType)) {
                    fromLeftJoinBuf.append(" and sm.strategy_map_id=? ");
                    paralist.add(id);
                }

            }
            if (StringUtils.isNotBlank(companyid)) {
                fromLeftJoinBuf.append(" and kpi.company_id=?");
                paralist.add(companyid);
            }
            if (StringUtils.isNotBlank(queryName)) {
                fromLeftJoinBuf.append(" and  kpi.kpi_name like '%").append(queryName).append("%'");
            }
            Object[] paraobjects = new Object[paralist.size()];
            paraobjects = paralist.toArray(paraobjects);
            SQLQuery countQuery = o_categoryDAO.createSQLQuery(countBuf.append(fromLeftJoinBuf).toString(), paraobjects);
            map.put("totalCount", countQuery.uniqueResult());

            if (StringUtils.isNotBlank(sortColumn)) {
                orderbuf.append(" order by  ");
                if (StringUtils.equals("name", sortColumn)) {
                    orderbuf.append("name");
                }
                else if (StringUtils.equals("id", sortColumn)) {
                    orderbuf.append("id");
                }
                else if (StringUtils.equals("finishValue", sortColumn)) {
                    orderbuf.append("finishValue");
                }
                else if (StringUtils.equals("targetValue", sortColumn)) {
                    orderbuf.append("targetValue");
                }
                else if (StringUtils.equals("assessmentValue", sortColumn)) {
                    orderbuf.append("assessmentValue");
                }
                else if (StringUtils.equals("assessmentStatus", sortColumn)) {
                    orderbuf.append("status");
                }
                else if (StringUtils.equals("directionstr", sortColumn)) {
                    orderbuf.append("direction");
                }
                else if (StringUtils.equals("dateRange", sortColumn)) {
                    orderbuf.append("timerange");
                }
                else if (StringUtils.equals("preFinishValue", sortColumn)) {
                    orderbuf.append("pre_finishvalue");
                }
                else if (StringUtils.equals("preYearFinishValue", sortColumn)) {
                    orderbuf.append("preyear_finishvalue");
                }
                if ("ASC".equalsIgnoreCase(dir)) {
                    orderbuf.append(" asc ");
                }
                else {
                    orderbuf.append(" desc ");
                }
            }

            StringBuffer sqlbuf = new StringBuffer();
            sqlbuf.append(" select t.id,t.name,t.timerange,t.assessmentValue,t.targetValue,t.finishValue,t.status,t.direction,");
            sqlbuf.append(" (select finish_value from t_kpi_kpi_gather_result ks where t.pre_period_id=ks.time_period_id and t.id=ks.kpi_id)  pre_finishvalue,");
            sqlbuf.append(" (select finish_value from t_kpi_kpi_gather_result ks where t.pre_year_period_id=ks.time_period_id and t.id=ks.kpi_id)  preyear_finishvalue  ");
            sqlbuf.append(" from ( ").append(selectBuf).append(fromLeftJoinBuf).append(" )  t ").append(orderbuf);
            SQLQuery sqlquery = o_categoryDAO.createSQLQuery(sqlbuf.toString(), paraobjects);
            sqlquery.setFirstResult(start);
            sqlquery.setMaxResults(limit);
            list = sqlquery.list();
        }
        return list;
    }

    /**
     * 根据记分卡ID查询最新的关联指标的采集结果
     * 
     * @param map
     * @param start
     *            分页起始位置
     * @param limit
     *            显示记录数
     * @param queryName
     *            指标类型名称
     * @param sortColumn
     *            排序字段
     * @param dir
     *            排序方向
     * @param id
     *            记分卡ID
     * @return
     */
    public List<Object[]> findLastCategoryRelaKpiResultsBySome(Map<String, Object> map, int start, int limit, String queryName, String id,
            String sortColumn, String dir) {
        List<Object[]> list = null;
        String companyid = UserContext.getUser().getCompanyid();// 公司ID
        if (StringUtils.isNotBlank(id) && !"undefined".equals(id)) {
            StringBuffer selectBuf = new StringBuffer();
            StringBuffer fromLeftJoinBuf = new StringBuffer();
            StringBuffer countBuf = new StringBuffer();
            StringBuffer orderbuf = new StringBuffer();
            List<Object> paralist = new ArrayList<Object>();
            selectBuf
                    .append(" select kpi.id   id, kpi.kpi_name   name , time.time_period_full_name   timerange , result.assessment_value   assessmentValue ,");
            selectBuf
                    .append(" result.target_value   targetValue , result.finish_value   finishValue ,statusdict.dict_entry_value   status ,dirdict.dict_entry_value   direction , category.is_creator   creator,kpi.is_enabled is_enabled");
            countBuf.append(" select count(*) ");
            fromLeftJoinBuf
                    .append(" from t_kpi_kpi  kpi left outer join t_kpi_kpi_gather_result  result on kpi.id=result.kpi_id and kpi.latest_time_period_id=result.time_period_id ");
            fromLeftJoinBuf.append(" left outer join t_com_time_period  time on result.time_period_id=time.id ");
            fromLeftJoinBuf.append(" left outer join t_sys_dict_entry  statusdict on result.assessment_status = statusdict.id ");
            fromLeftJoinBuf.append(" left outer join t_sys_dict_entry  dirdict on result.direction = dirdict.id ");
            fromLeftJoinBuf.append(" left outer join t_kpi_kpi_rela_category  category on category.kpi_id=kpi.id ");
            fromLeftJoinBuf.append(" where kpi.delete_status=? and kpi.is_kpi_category=?  and 1=1 ");

            // XXX 胡迪新修改 -- 开始
            paralist.add(true);
            paralist.add("KPI");
            // XXX 胡迪新修改 -- 结束
            if (StringUtils.isNotBlank(id)) {
                fromLeftJoinBuf.append(" and category.category_id=? ");// 记分卡ID
                paralist.add(id);
            }
            if (StringUtils.isNotBlank(companyid)) {
                fromLeftJoinBuf.append(" and kpi.company_id=?");// 公司ID
                paralist.add(companyid);
            }
            if (StringUtils.isNotBlank(queryName)) {
                fromLeftJoinBuf.append(" and  kpi.kpi_name like '%").append(queryName).append("%'");// 按照名称模糊查询
            }
            Object[] paraobjects = new Object[paralist.size()];
            paraobjects = paralist.toArray(paraobjects);
            SQLQuery countQuery = o_categoryDAO.createSQLQuery(countBuf.append(fromLeftJoinBuf).toString(), paraobjects);
            map.put("totalCount", countQuery.uniqueResult());// 查询记录条数

            if (StringUtils.isNotBlank(sortColumn)) {
                orderbuf.append(" order by ");
                if (StringUtils.equals("name", sortColumn))// 按照名称排序
                {
                    orderbuf.append("name");
                }
                else if (StringUtils.equals("id", sortColumn))// 按照ID排序
                {
                    orderbuf.append("id");
                }
                else if (StringUtils.equals("finishValue", sortColumn))// 按照完成值排序
                {
                    orderbuf.append("finishValue");
                }
                else if (StringUtils.equals("targetValue", sortColumn))// 按照目标值排序
                {
                    orderbuf.append("targetValue");
                }
                else if (StringUtils.equals("assessmentValue", sortColumn))// 按照评估值排序
                {
                    orderbuf.append("assessmentValue");
                }
                else if (StringUtils.equals("assessmentStatus", sortColumn))// 按照状态排序
                {
                    orderbuf.append("status");
                }
                else if (StringUtils.equals("directionstr", sortColumn))// 按照趋势排序
                {
                    orderbuf.append("direction");
                }
                else if (StringUtils.equals("dateRange", sortColumn))// 按照时间段排序
                {
                    orderbuf.append("timerange");
                }
                if ("ASC".equalsIgnoreCase(dir)) {
                    orderbuf.append(" asc ");
                }
                else {
                    orderbuf.append(" desc ");
                }
            }
            SQLQuery sqlquery = o_categoryDAO.createSQLQuery(selectBuf.append(fromLeftJoinBuf).append(orderbuf).toString(), paraobjects);
            sqlquery.setFirstResult(start);
            sqlquery.setMaxResults(limit);
            list = sqlquery.list();
        }
        return list;
    }

    /**
     * <pre>
     * 生成维度编码
     * </pre>
     * 
     * @author 陈晓哲
     * @param parentId 记分卡父ID
     * @param id 记分卡id
     * @return
     * @since fhd　Ver 1.1
     */
    public String findCodeByParentId(String parentId, String id) {
        long sort = 0;
        long count = 0;
        Category category = null;
        Category pcategory = null;
        StringBuffer code = new StringBuffer();
        if (StringUtils.isNotBlank(id) && !"undefined".equals(id)) {
            category = this.findCategoryById(id);
            if (null != category.getSort()) {
                sort = category.getSort();
            }
            if (null != category && null != category.getCode()) {
                return category.getCode();

            }
        }
        if ("category_root".equals(parentId)) {
            code.append("DWJG");
        }
        else {
            if (StringUtils.isNotBlank(parentId)) {
                pcategory = this.findCategoryById(parentId);
                if (pcategory == null) {
                    count = (Long) this.o_categoryDAO.createCriteria().add(Restrictions.isNull("parent")).setProjection(Projections.rowCount())
                            .uniqueResult();
                    code.append("DWJG000").append(count + 1);
                    return code.toString();

                }
                String idSeq = pcategory.getIdSeq();
                if (StringUtils.isNotBlank(idSeq)) {
                    String[] idarr = StringUtils.split(idSeq, ".");
                    for (int i = 0; i < idarr.length; i++) {
                        if (StringUtils.isNotBlank(idarr[i])) {
                            category = this.findCategoryById(idarr[i]);
                            String codeno = category.getCode();
                            if (null != codeno && StringUtils.isNotBlank(codeno)) {
                                if (!codeno.contains(code)) {
                                    code.append(codeno).append("-");
                                }
                                else {
                                    code.append(codeno.substring(code.length())).append("-");
                                }
                            }
                            else {
                                if (null == category.getParent() && null != category.getLevel() && 1 != category.getLevel()) {
                                    code.append("DWJG");
                                }
                                else {
                                    if (code.indexOf("DWJG") == -1) {
                                        code.append("DWJG");
                                    }
                                    code.append("000").append(category.getSort()).append("-");
                                }
                            }
                        }
                    }
                }
            }
        }

        if (StringUtils.isNotBlank(id) && !"undefined".equals(id)) {
            count = sort;
            code.append("000").append(count);
        }
        else {
            Criteria criteria = this.o_categoryDAO.createCriteria();
            if ("category_root".equals(parentId)) {
                criteria.add(Restrictions.isNull("parent"));
            }
            else {
                criteria.add(Restrictions.eq("parent", pcategory));
            }
            criteria.setProjection(Projections.rowCount());
            count = (Long) criteria.uniqueResult();
            code.append("000").append(count + 1);
        }

        return code.toString();
    }

    /**
     * <pre>
     * 根据维度名称查询维度个数
     * </pre>
     * 
     * @author 陈晓哲
     * @param name 记分卡名称
     * @param id 记分卡ID
     * @return
     * @since fhd　Ver 1.1
     */
    public long findCategoryCountByName(String name, String id) {

        long count = 0;
        Criteria criteria = this.o_categoryDAO.createCriteria();
        criteria.add(Restrictions.eq("deleteStatus", true));
        criteria.add(Restrictions.eq("name", name));
        criteria.add(Restrictions.eq("company.id", UserContext.getUser().getCompanyid()));
        if (!"".equals(id) && !"undefined".equals(id)) {// update
            criteria.add(Restrictions.ne("id", id));

        }
        criteria.setProjection(Projections.rowCount());
        count = (Long) criteria.uniqueResult();
        return count;

    }

    /**
     * <pre>
     * 根据编码查询维度个数
     * </pre>
     * 
     * @author 陈晓哲
     * @param code 记分卡编码
     * @param id 记分卡id
     * @return
     * @since fhd　Ver 1.1
     */
    public long findCategoryCountByCode(String code, String id) {

        long count = 0;
        Criteria criteria = this.o_categoryDAO.createCriteria();
        criteria.add(Restrictions.eq("deleteStatus", true));
        criteria.add(Restrictions.eq("company.id", UserContext.getUser().getCompanyid()));
        criteria.add(Restrictions.eq("code", code));
        if (!"".equals(id) && !"undefined".equals(id)) {// update
            criteria.add(Restrictions.ne("id", id));
        }
        criteria.setProjection(Projections.rowCount());
        count = (Long) criteria.uniqueResult();
        return count;

    }

    /**
     * <pre>
     * 保存维度和预警信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param jsonString 告警信息
     * @param id 记分卡id
     * @throws ParseException
     * @since fhd　Ver 1.1
     */
    @Transactional
    public void mergeCategoryRelaAlarm(String jsonString, String id) throws ParseException {
        if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(jsonString)) {
            JSONArray jsonArray = JSONArray.fromObject(jsonString);
            AlarmPlan alarmPlan = null;
            AlarmPlan warningPlan = null;
            CategoryRelaAlarm categoryRelaAlarm = null;
            Category category = this.findCategoryById(id);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            /*
             * 删除维度关联的预警告警信息
             */
            Set<CategoryRelaAlarm> relaAlarms = category.getCategoryRelaAlarms();
            for (CategoryRelaAlarm relaAlarmItem : relaAlarms) {
                o_CategoryRelaAlarmDAO.delete(relaAlarmItem);
            }

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String alarmId = jsonObject.getString("alarm");
                String warningId = jsonObject.getString("warning");
                String date = jsonObject.getString("date");
                /*
                 * 保存告警,预警信息
                 */
                if (StringUtils.isNotBlank(alarmId)) {
                    alarmPlan = o_alarmPlanDAO.get(alarmId);
                }
                if (StringUtils.isNotBlank(warningId)) {
                    warningPlan = o_alarmPlanDAO.get(warningId);
                }
                categoryRelaAlarm = new CategoryRelaAlarm();
                categoryRelaAlarm.setId(Identities.uuid());
                categoryRelaAlarm.setCategory(category);
                categoryRelaAlarm.setrAlarmPlan(alarmPlan);
                categoryRelaAlarm.setFcAlarmPlan(warningPlan);
                categoryRelaAlarm.setStartDate(sdf.parse(date));
                o_CategoryRelaAlarmDAO.merge(categoryRelaAlarm);
            }
        }
    }

    /**
     * <pre>
     * 保存指标维度
     * </pre>
     * 
     * @author 陈晓哲
     * @param form 记分卡form对象
     * @throws ParseException 
     * @since fhd　Ver 1.1
     */
    @Transactional
    public String mergeCategory(CategoryForm form) throws ParseException {
        int sort = 0;
        boolean addFlag = false;
        Category category = null;
        Category parentCategory = null;
        String categoryId = Identities.uuid();
        String parentStr = form.getParentStr();
        String statusStr = form.getStatusStr();
        String currentid = form.getId();
        String ownDeptStr = form.getOwnDept();
        String targetDeptStr = form.getTargetDept();
        String chartTypeStr = form.getChartTypeStr();
        String dataTypeStr = form.getDataTypeStr();
        String createKpiStr = form.getCreateKpiStr();
        String calcStr = form.getCalcStr();
        // 获得公司机构ID
        String companyid = UserContext.getUser().getCompanyid();
        // 获得机构对象
        SysOrganization company = o_organizationBO.get(companyid);

        if (StringUtils.isNotBlank(parentStr)) {
            parentCategory = findCategoryById(parentStr);
        }
        if (StringUtils.isNotBlank(currentid) && !"undefined".equals(currentid) && !"null".equals(currentid)) {// update
            category = findCategoryById(currentid);
            sort = category.getSort();
            // 删除关联的部门和人员信息
            removeCategoryRelaOrgEmp(category, "ALL");
            categoryId = category.getId();
            boolean isleaf = category.getIsLeaf();
            BeanUtils.copyProperties(form, category);
            category.setId(categoryId);
            category.setIsLeaf(isleaf);
        }
        else {// add
            Long count = 0L;
            Criteria criteria = o_categoryDAO.createCriteria();
            criteria.add(Restrictions.ne("deleteStatus", false));
            if ("category_root".equals(parentStr)) {
                criteria.add(Restrictions.isNull("parent"));
            }
            else {
                criteria.add(Restrictions.eq("parent.id", parentStr));
            }
            criteria.setProjection(Projections.rowCount());
            criteria.uniqueResult();
            count = (Long) criteria.uniqueResult();
            sort = count.intValue() + 1;
            category = new Category();
            BeanUtils.copyProperties(form, category);
            category.setId(categoryId);
            category.setIsLeaf(true);
            addFlag = true;

        }
        Integer level = 0;
        String idseq = ".";
        if (null != parentCategory) {
            idseq = parentCategory.getIdSeq();
            level = parentCategory.getLevel();
        }
        category.setCompany(company);
        category.setIdSeq(idseq + categoryId + ".");
        category.setSort(sort);
        category.setParent(parentCategory);
        category.setDeleteStatus(true);
        if (level == null) {
            level = 0;
        }
        category.setLevel(level + 1);
        if (StringUtils.isNotBlank(createKpiStr)) {
            category.setCreateKpi(o_dictEntryBO.findDictEntryById(createKpiStr));
        }
        if(StringUtils.isNotBlank(calcStr)){
            category.setCalc(o_dictEntryBO.findDictEntryById(calcStr));
        }
        if (StringUtils.isNotBlank(dataTypeStr)) {
            category.setDateType(o_dictEntryBO.findDictEntryById(dataTypeStr));
        }
        if (StringUtils.isNotBlank(statusStr)) {
            category.setStatus(o_dictEntryBO.findDictEntryById(statusStr));
        }
        if (StringUtils.isNotBlank(chartTypeStr)) {
            category.setChartType(chartTypeStr);
        }
        if (null != parentCategory) {
            parentCategory.setIsLeaf(false);
        }
        // 保存维度对象
        o_categoryDAO.merge(category);
        // 保存部门和人员信息
        if (StringUtils.isNotBlank(ownDeptStr)) {
            saveCategoryRelaOrgEmp(category, ownDeptStr, Contents.BELONGDEPARTMENT);
        }
        if (StringUtils.isNotBlank(targetDeptStr)) {
            saveCategoryRelaOrgEmp(category, targetDeptStr, Contents.TARGETDEPARTMENT);
        }

        if (addFlag) {
            // 添加维度时需要继承上级维度的指标信息
            if (null != parentCategory) {
                DictEntry dict = parentCategory.getCreateKpi();// 是否生成度量指标,y时生成度量指标
                if (null != dict) {
                    if (Contents.DICT_Y.equals(dict.getId())) {
                        saveParentCategoryRelaKpiToThis(category, parentStr);
                    }
                }
            }
        }
        if (addFlag) {
            String year = DateUtils.getYear(new Date());
            List<TimePeriod> timeList = o_relaAssessResultBO.findTimePeriodByMonthType(year);
            //向评分结果表中插入默认数据;
            JSONArray scJsonArray = new JSONArray();
            JSONObject scJsonObject = new JSONObject();
            scJsonObject.put("objectId", category.getId());
            scJsonObject.put("objectName", category.getName());
            scJsonArray.add(scJsonObject);
            o_relaAssessResultBO.saveBathResultData(scJsonArray, timeList, "sc");
        }
        return categoryId;
    }

    /**
     * <pre>
     * 保存维度引用的指标信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param jsobj 记分卡关联的指标数据
     * @since fhd　Ver 1.1
     */
    @Transactional
    public void mergeCategoryRelaKpi(JSONObject jsobj) {
        if (null != jsobj) {
            boolean flag = false;
            String categoryId = jsobj.getString("categoryId");
            Category category = findCategoryById(categoryId);
            Set<KpiRelaCategory> kpiRelaCategorys = null;

            JSONArray kpiids = jsobj.getJSONArray("kpiIds");
            if (null != kpiids && kpiids.size() > 0) {
                kpiRelaCategorys = category.getKpiRelaCategorys();

            }
            for (Object kpiId : kpiids) {
                if (null != kpiRelaCategorys) {
                    for (KpiRelaCategory kpiRelaCategory : kpiRelaCategorys) {
                        try {
                            if (kpiId.equals(kpiRelaCategory.getKpi().getId())) {
                                flag = true;
                            }
                        }
                        catch (ObjectNotFoundException e) {
                        }
                    }
                }
                if (!flag) {
                    KpiRelaCategory kpiRelaCategory = new KpiRelaCategory();
                    kpiRelaCategory.setId(Identities.uuid());
                    kpiRelaCategory.setCategory(category);
                    kpiRelaCategory.setKpi(o_kpiBO.findKpiById((String) kpiId));
                    kpiRelaCategory.setIsCreator(false);
                    this.o_kpiRelaCategoryDAO.merge(kpiRelaCategory);
                }

            }
        }
    }

    /**
     * <pre>
     * 将上级维度关联的指标信息追加到当前维度中
     * </pre>
     * 
     * @author 陈晓哲
     * @param parentCategoryid 记分卡父id
     * @param category 记分卡父对象
     * @throws ParseException 
     * @since fhd　Ver 1.1
     */
    @Transactional
    public void saveParentCategoryRelaKpiToThis(Category category, String parentCategoryid) throws ParseException {
        String kpiid = "";
        Set<String> kpiIdSet = new HashSet<String>();
        Set<Kpi> kpiSet = new HashSet<Kpi>();
        Map<String, String> idMap = new HashMap<String, String>();
        Map<String, Kpi> kpiMap = new HashMap<String, Kpi>();
        Criteria kpiRelaCategoryCriteria = this.o_kpiRelaCategoryDAO.createCriteria();
        kpiRelaCategoryCriteria.createAlias("kpi", "kpi");
        kpiRelaCategoryCriteria.add(Restrictions.eq("category.id", parentCategoryid));
        kpiRelaCategoryCriteria.add(Restrictions.eq("isCreator", true));
        kpiRelaCategoryCriteria.add(Restrictions.eq("kpi.deleteStatus", true));
        kpiRelaCategoryCriteria.setFetchMode("kpi", JOIN);
        List<KpiRelaCategory> list = kpiRelaCategoryCriteria.list();
        if (list.size() > 0) {// 说明有和维度关联的指标
            for (KpiRelaCategory kpiRelaCategory : list) {
                kpiid = kpiRelaCategory.getKpi().getId();
                Kpi kpi = new Kpi();
                String uuid = Identities.uuid();
                Kpi oldKpi = kpiRelaCategory.getKpi();
                Kpi BelongKpiCategory = oldKpi.getBelongKpiCategory();
                BeanUtils.copyProperties(oldKpi, kpi);
                kpi.setId(uuid);
                kpi.setIdSeq(oldKpi.getIdSeq() + uuid + ".");
                kpi.setIsKpiCategory("KPI");
                String kpiTypeName = "";
                if (null != BelongKpiCategory) {
                    kpiTypeName = BelongKpiCategory.getName();
                }
                kpi.setName(category.getName() + " " + kpiTypeName);
                //继承度量指标时,重新计算时间;
                String gatherDayFormulr = kpi.getGatherDayFormulr();
                if (StringUtils.isNotBlank(gatherDayFormulr)) {
                    kpi.setCalculatetime(DateUtils.stringToDateToDay(findKpiFrequenceByFormulr(gatherDayFormulr)));
                }
                String targetSetDayFormular = kpi.getTargetSetDayFormular();
                if (StringUtils.isNotBlank(targetSetDayFormular)) {
                    kpi.setTargetCalculatetime(DateUtils.stringToDateToDay(findKpiFrequenceByFormulr(targetSetDayFormular)));
                }
                idMap.put(kpiid, kpi.getId());
                kpiMap.put(kpi.getId(), kpi);
                kpiSet.add(kpi);
                kpiIdSet.add(kpiid);
            }
            // 批量保存指标对象
            o_kpiBO.saveKpiBatch(kpiSet);

            // 保存指标关联的部门人员
            saveCategoryKpiRelaOrgEmp(kpiIdSet, idMap, kpiMap);
            // 保存指标关联的预警信息
            saveCategoryKpiRelaAlarm(kpiIdSet, idMap, kpiMap);
            // 保存指标关联的维度信息
            saveCategoryKpiRelaDim(kpiIdSet, idMap, kpiMap);
            // 保存分类和指标的关联关系
            saveCategoryRelaKip(category, kpiSet);
            //保存指标的采集结果默认数据
            saveKpiGatherResult(kpiSet);

        }

    }

    /**保存指标的采集结果默认数据
     * @param kpiSet 指标对象集合
     */
    private void saveKpiGatherResult(Set<Kpi> kpiSet) {
        String year = DateUtils.getYear(new Date());
        JSONArray jsonarray = new JSONArray();
        for (Kpi kpi : kpiSet) {
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
            o_kpiGatherResultBO.saveBatchKpiGatherResultByFrequenceType(jsonarray, Integer.valueOf(year));
        }
    }

    /**
     * <pre>
     * 根据cron表达式算出下次执行时间
     * </pre>
     * 
     * @author 陈晓哲
     * @param formulr cron表达式
     * @return
     * @throws ParseException
     * @since fhd　Ver 1.1
     */
    private String findKpiFrequenceByFormulr(String formulr) throws ParseException {
        CronTrigger trigger = new CronTrigger();
        trigger.setCronExpression(formulr);
        AnnualCalendar cal = new AnnualCalendar();
        trigger.computeFirstFireTime(cal);
        String executeDate = DateUtils.formatDate(trigger.getNextFireTime(), "yyyy-MM-dd");
        return executeDate;
    }

    /**
     * <pre>
     * 保存维度和指标的主关联关系
     * </pre>
     * 
     * @author 陈晓哲
     * @param category记分卡对象
     * @param kpiSet 指标对象集合
     * @since fhd　Ver 1.1
     */
    @Transactional
    public void saveCategoryRelaKip(Category category, Set<Kpi> kpiSet) {
        if (null != kpiSet && kpiSet.size() > 0) {
            for (Kpi kpi : kpiSet) {
                KpiRelaCategory kpiRelaCategory = new KpiRelaCategory();
                kpiRelaCategory.setId(Identities.uuid());
                kpiRelaCategory.setCategory(category);
                kpiRelaCategory.setKpi(kpi);
                kpiRelaCategory.setIsCreator(true);
                this.o_kpiRelaCategoryDAO.merge(kpiRelaCategory);
            }
        }

    }

    /**
     * <pre>
     * 保存指标关联的预警信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param kpiIdSet指标id集合
     * @param idMap 指标idmap
     * @param kpiMap 指标map
     * @since fhd　Ver 1.1
     */
    @Transactional
    public void saveCategoryKpiRelaAlarm(Set<String> kpiIdSet, Map<String, String> idMap, Map<String, Kpi> kpiMap) {
        Criteria kpiRelaAlarmCriteria = this.o_KpiRelaAlarmDAO.createCriteria();
        kpiRelaAlarmCriteria.add(Restrictions.in("kpi.id", kpiIdSet));
        kpiRelaAlarmCriteria.setFetchMode("fcAlarmPlan", JOIN);
        kpiRelaAlarmCriteria.setFetchMode("rAlarmPlan", JOIN);
        List<KpiRelaAlarm> kpiRelaAlarms = kpiRelaAlarmCriteria.list();
        for (KpiRelaAlarm kpiRelaAlarm : kpiRelaAlarms) {
            KpiRelaAlarm item = new KpiRelaAlarm();
            String id = idMap.get(kpiRelaAlarm.getKpi().getId());
            if (StringUtils.isNotBlank(id)) {
                Kpi kpi = kpiMap.get(id);
                if (null != kpi) {
                    item.setId(Identities.uuid());
                    item.setKpi(kpi);
                    item.setStartDate(kpiRelaAlarm.getStartDate());
                    item.setFcAlarmPlan(kpiRelaAlarm.getFcAlarmPlan());
                    item.setrAlarmPlan(kpiRelaAlarm.getrAlarmPlan());
                    o_KpiRelaAlarmDAO.merge(item);
                }
            }
        }
    }

    /**
     * <pre>
     * 保存指标关联的维度信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param kpiIdSet指标id集合
     * @param idMap 指标idmap
     * @param kpiMap 指标map
     * @since fhd　Ver 1.1
     */
    @Transactional
    public void saveCategoryKpiRelaDim(Set<String> kpiIdSet, Map<String, String> idMap, Map<String, Kpi> kpiMap) {
        Criteria kpiRelaDimCriteria = this.o_kpiRelaDimDAO.createCriteria();
        kpiRelaDimCriteria.add(Restrictions.in("kpi.id", kpiIdSet));
        kpiRelaDimCriteria.setFetchMode("smDim", JOIN);
        List<KpiRelaDim> kpiRelaDims = kpiRelaDimCriteria.list();
        for (KpiRelaDim kpiRelaDim : kpiRelaDims) {
            KpiRelaDim item = new KpiRelaDim();
            String id = idMap.get(kpiRelaDim.getKpi().getId());
            if (StringUtils.isNotBlank(id)) {
                Kpi kpi = kpiMap.get(id);
                if (null != kpi) {
                    item.setId(Identities.uuid());
                    item.setKpi(kpi);
                    item.setType(kpiRelaDim.getType());
                    item.setSmDim(kpiRelaDim.getSmDim());
                    o_kpiRelaDimDAO.merge(item);
                }
            }
        }
    }

    /**
     * <pre>
     * 保存指标关联的部门人员
     * </pre>
     * 
     * @author 陈晓哲
     * @param kpiIdSet指标id集合
     * @param idMap 指标idmap
     * @param kpiMap 指标map
     * @since fhd　Ver 1.1
     */
    @Transactional
    public void saveCategoryKpiRelaOrgEmp(Set<String> kpiIdSet, Map<String, String> idMap, Map<String, Kpi> kpiMap) {
        Criteria kpiRelaOrgCriteria = o_kpiRelaOrgEmpDAO.createCriteria();
        kpiRelaOrgCriteria.add(Restrictions.in("kpi.id", kpiIdSet));
        kpiRelaOrgCriteria.setFetchMode("org", JOIN);
        kpiRelaOrgCriteria.setFetchMode("emp", JOIN);
        kpiRelaOrgCriteria.setFetchMode("kpi", JOIN);
        List<KpiRelaOrgEmp> kpiRelaOrgEmpSet = kpiRelaOrgCriteria.list();
        for (KpiRelaOrgEmp kpiRelaOrgEmp : kpiRelaOrgEmpSet) {
            KpiRelaOrgEmp item = new KpiRelaOrgEmp();
            String id = idMap.get(kpiRelaOrgEmp.getKpi().getId());
            if (StringUtils.isNotBlank(id)) {
                Kpi kpi = kpiMap.get(id);
                if (null != kpi) {
                    item.setId(Identities.uuid());
                    item.setKpi(kpi);
                    item.setType(kpiRelaOrgEmp.getType());
                    item.setEmp(kpiRelaOrgEmp.getEmp());
                    item.setOrg(kpiRelaOrgEmp.getOrg());
                    // 保存指标关联的部门人员
                    o_kpiRelaOrgEmpDAO.merge(item);
                }
            }
        }
    }

    /**
     * <pre>
     * 保存维度和部门人员信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param category
     *            记分卡对象
     * @param orgEmp
     *            部门人员json串
     * @param type
     *            部门类型
     * @since fhd　Ver 1.1
     */
    @Transactional
    private void saveCategoryRelaOrgEmp(Category category, String orgEmp, String type) {
        if (StringUtils.isNotBlank(orgEmp)) {
            SysEmployee emp = null;
            SysOrganization org = null;
            JSONArray jsonarr = JSONArray.fromObject(orgEmp);
            for (int i = 0; i < jsonarr.size(); i++) {
                CategoryRelaOrgEmp categoryRelaOrgEmp = new CategoryRelaOrgEmp();
                JSONObject jsobj = jsonarr.getJSONObject(i);
                org = this.o_organizationBO.get(jsobj.getString("deptid"));
                emp = this.o_sysEmployeeDAO.get(jsobj.getString("empid"));
                categoryRelaOrgEmp.setId(Identities.uuid());
                categoryRelaOrgEmp.setType(type);
                categoryRelaOrgEmp.setCategory(category);
                if (null != org) {
                    categoryRelaOrgEmp.setOrg(org);
                }
                if (null != emp) {
                    categoryRelaOrgEmp.setEmp(emp);
                }
                this.o_categoryRelaOrgEmpDAO.merge(categoryRelaOrgEmp);
            }
        }
    }

    /**
     * <pre>
     * 删除维度信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param id
     *            记分卡ID
     * @return
     * @since fhd　Ver 1.1
     */
    @Transactional
    public String removeCategory(String id) {
        Criteria criteria = this.o_categoryDAO.createCriteria();
        criteria.add(Restrictions.like("idSeq", "%." + id + ".%"));
        criteria.add(Restrictions.eq("deleteStatus", true));
        criteria.setProjection(Projections.count("id"));

        long count = (Long) criteria.uniqueResult();
        if (count >= 2) {
            return "cascade";
        }

        // 逻辑删除
        Category category = findCategoryById(id);
        Category parentCategory = category.getParent();
        if (null != parentCategory) {
            /* 当父节点没有子节点时,将父节点变为叶子节点 */
            Criteria countCriteria = o_categoryDAO.createCriteria();
            countCriteria.add(Restrictions.eq("parent", parentCategory));
            countCriteria.add(Restrictions.ne("id", id));
            countCriteria.add(Restrictions.ne("deleteStatus", false));
            countCriteria.setProjection(Projections.rowCount());
            count = (Long) countCriteria.uniqueResult();
            if (count == 0) {
                parentCategory.setIsLeaf(true);
            }
        }
        category.setDeleteStatus(false);
        o_categoryDAO.merge(category);
        return "success";
    }

    /**
     * <pre>
     * 删除维度关联的部门和人员信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param category
     *            维度对象
     * @param type
     *            部门类型
     * @since fhd　Ver 1.1
     */
    @Transactional
    private void removeCategoryRelaOrgEmp(Category category, String type) {
        if (null != category) {
            Set<CategoryRelaOrgEmp> categoryRelaOrgEmpSet = category.getCategoryRelaOrgEmps();
            for (CategoryRelaOrgEmp categoryRelaOrgEmp : categoryRelaOrgEmpSet) {
                if ("ALL".equals(type)) {
                    o_categoryRelaOrgEmpDAO.delete(categoryRelaOrgEmp);
                }
                else {
                    if (type.equals(categoryRelaOrgEmp.getType())) {
                        o_categoryRelaOrgEmpDAO.delete(categoryRelaOrgEmp);
                    }
                }
            }
        }
    }

    /**
     * <pre>
     * 将维度对象封装为树的node节点
     * </pre>
     * 
     * @author 陈晓哲
     * @param category 记分卡对象
     * @param canChecked 是否可以选择
     * @param isLeaf 是否是叶子
     * @param expanded 是否展开
     * @return
     * @since fhd　Ver 1.1
     */
    protected Map<String, Object> wrapCategoryNode(Category category, Boolean canChecked, Boolean isLeaf, Boolean expanded,
            HashMap<String, String> categoryRelaAlarmMap) {
        Map<String, Object> item = new HashMap<String, Object>();
        item.put("id", category.getId());
        item.put("dbid", category.getId());
        item.put("code", category.getCode());
        item.put("text", category.getName());
        item.put("type", "category");

        if (null != category.getParent())
            item.put("parentid", category.getParent().getId());
        if (isLeaf) {
            item.put("leaf", category.getIsLeaf());
        }
        if (!isLeaf) {
            item.put("leaf", false);
        }
        if (canChecked) {
            item.put("checked", false);
        }
        if (expanded) {
            item.put("expanded", true);
        }
        return item;
    }

    /**
     * 通过公司ID、parentId、删除状态(已删除/未删除 false/true) 查询category实体
     * 
     * @author 金鹏祥
     * @param companyId
     *            公司ID
     * @param parentId
     *            下一节点
     * @param deleteStatus
     *            删除状态(已删除/未删除 false/true)
     * @param isParentIdNull
     *            是否为空
     * @return List<Category>
     */
    public List<Category> findCategoryBySome(String companyId, String parentId, boolean deleteStatus, boolean isParentIdNull, String query) {
        Criteria criteria = o_categoryDAO.createCriteria();
        if (StringUtils.isNotBlank(companyId)) {
            criteria.add(Restrictions.eq("company.id", companyId));
        }

        if (StringUtils.isNotBlank(query)) {
            criteria.add(like("name", query, MatchMode.ANYWHERE));
        }

        if (isParentIdNull) {
            criteria.add(Restrictions.isNull("parent"));
        }
        else {
            if (StringUtils.isNotBlank(parentId)) {
                criteria.add(Restrictions.eq("parent.id", parentId));
            }
        }
        criteria.add(Restrictions.eq("deleteStatus", deleteStatus));

        return criteria.list();
    }

    /**
     * 查询公司ID下的实体
     * 
     * @author 金鹏祥
     * @param companyId
     *            公司ID
     * @param deleteStatus
     *            删除状态(已删除/未删除 false/true)
     * @return HashMap<String, Category>
     */
    public HashMap<String, Category> findCategoryMapByCompanyId(String companyId, boolean deleteStatus) {
        List<Category> list = null;
        HashMap<String, Category> categoryMap = new HashMap<String, Category>();

        Criteria criteria = o_categoryDAO.createCriteria();
        if (StringUtils.isNotBlank(companyId)) {
            criteria.add(Restrictions.eq("company.id", companyId));
        }
        criteria.add(Restrictions.eq("deleteStatus", deleteStatus));
        list = criteria.list();

        for (Category category : list) {
            if (null != category.getParent()) {
                categoryMap.put(category.getParent().getId(), category);
            }
        }

        return categoryMap;
    }

    /**
     * 所在公司ID下的ID
     * 
     * @author 金鹏祥
     * @param searchName
     *            查询条件
     * @param companyId
     *            公司ID
     * @param orgId
     *            机构ID
     * @param deleteStatus
     *            删除状态(已删除/未删除 false/true)
     * @return List<CategoryRelaOrgEmp>
     * @since fhd　Ver 1.1
     */
    public List<CategoryRelaOrgEmp> findCategoryRelaOrgEmpBySome(String companyId, String orgId, boolean deleteStatus, String query) {
        List<CategoryRelaOrgEmp> categoryList = new ArrayList<CategoryRelaOrgEmp>();
        Criteria criteria = o_categoryRelaOrgEmpDAO.createCriteria();

        if (StringUtils.isNotBlank(query)) {
            criteria.createAlias("category", "c");
            criteria.add(Restrictions.like("c.name", query, MatchMode.ANYWHERE));
            criteria.add(Restrictions.eq("c.company.id", companyId));
            criteria.add(Restrictions.eq("c.deleteStatus", deleteStatus));
        }
        else {
            criteria.createAlias("category", "c");
            criteria.add(Restrictions.eq("c.company.id", companyId));
        }
        criteria.createAlias("org", "o");
        criteria.add(Restrictions.eq("o.id", orgId));

        categoryList = criteria.list();

        return categoryList;
    }

    /**
     * 所在公司ID下的ID
     * 
     * @author 金鹏祥
     * @param searchName
     *            查询条件
     * @param companyId
     *            公司ID
     * @param orgId
     *            机构ID
     * @param deleteStatus
     *            删除状态(已删除/未删除 false/true)
     * @return List<CategoryRelaOrgEmp>
     * @since fhd　Ver 1.1
     */
    public List<CategoryRelaOrgEmp> findCategoryRelaOrgEmpBySome(String companyId, boolean deleteStatus, String query) {
        List<CategoryRelaOrgEmp> categoryList = new ArrayList<CategoryRelaOrgEmp>();
        Criteria criteria = o_categoryRelaOrgEmpDAO.createCriteria();

        if (StringUtils.isNotBlank(query)) {
            criteria.createAlias("category", "c");
            criteria.add(Restrictions.like("c.name", query, MatchMode.ANYWHERE));
            criteria.add(Restrictions.eq("c.company.id", companyId));
            criteria.add(Restrictions.eq("c.deleteStatus", deleteStatus));
        }
        else {
            criteria.createAlias("category", "c");
            criteria.add(Restrictions.eq("c.company.id", companyId));
        }

        categoryList = criteria.list();

        return categoryList;
    }

    /**
     * 所在公司ID下的记分卡
     * 
     * @author 金鹏祥
     * @param searchName
     *            查询条件
     * @param companyId
     *            公司ID
     * @param deleteStatus
     *            删除状态(已删除/未删除 false/true)
     * @return List<Category>
     */
    public List<Category> finCategoryBySome(String searchName, String companyId, boolean deleteStatus) {
        List<Category> list = new ArrayList<Category>();
        Criteria criteria = o_categoryDAO.createCriteria();

        if (StringUtils.isNotBlank(searchName)) {
            criteria.add(Restrictions.like("name", searchName, MatchMode.ANYWHERE));
        }

        criteria.add(Restrictions.eq("deleteStatus", deleteStatus));
        criteria.add(Restrictions.eq("company.id", companyId));
        list = criteria.list();

        return list;
    }

    /**
     * 得到CATEGORY实体中不是CategoryRelaOrgEmp实体的MAP集合
     * 
     * @author 金鹏祥
     * @param companyId
     *            公司ID
     * @return HashMap<String, Category>
     */
    public HashMap<String, Category> findCategoryMapFromNotCategoryRelaOrgEmpByCompanyId(String companyId, boolean deleteStatus) {
        List<Category> categoryList = null;
        HashMap<String, Category> categoryMap = new HashMap<String, Category>();
        Set<String> otheridSet = this.findCategoryRelaOrgEmpAll();
        Criteria criteria = o_categoryDAO.createCriteria();
        if (otheridSet.size() > 0) {
            criteria.add(Restrictions.not(Restrictions.in("id", otheridSet)));
        }
        if (StringUtils.isNotBlank(companyId)) {
            criteria.add(Restrictions.eq("company.id", companyId));
        }
        criteria.add(Restrictions.eq("deleteStatus", deleteStatus));

        categoryList = criteria.list();

        for (Category category : categoryList) {
            if (null != category.getParent()) {
                categoryMap.put(category.getParent().getId(), category);
            }
        }

        return categoryMap;
    }

    /**
     * 记分卡机构关联所有数据
     * 
     * @author 金鹏祥
     * @return Set<String>
     */
    public Set<String> findCategoryRelaOrgEmpAll() {
        Set<String> otheridSet = new HashSet<String>();
        List<CategoryRelaOrgEmp> list = o_categoryRelaOrgEmpDAO.createCriteria().list();
        for (CategoryRelaOrgEmp categoryRelaOrgEmp : list) {
            otheridSet.add(categoryRelaOrgEmp.getCategory().getId());
        }

        return otheridSet;
    }

    /**
     * 刨除机构关联的记分卡
     * 
     * @author 金鹏祥
     * @param parentId
     *            下一节点
     * @param deleteStatus
     *            删除状态(已删除/未删除 false/true)
     * @return List<Category>
     */
    public List<Category> findNotInOrgStrategyMap(String parentId, boolean deleteStatus) {
        List<Category> CategoryList = null;
        Set<String> otheridSet = this.findCategoryRelaOrgEmpAll();
        Criteria criteria = o_categoryDAO.createCriteria();

        if (otheridSet.size() > 0) {
            criteria.add(Restrictions.not(Restrictions.in("id", otheridSet)));
        }

        criteria.add(Restrictions.eq("parent.id", parentId));
        criteria.add(Restrictions.eq("deleteStatus", deleteStatus));

        CategoryList = criteria.list();

        return CategoryList;
    }

    /**
     * 刨除没有关联结构的记分卡
     * 
     * @author 金鹏祥
     * @param deleteStatus
     *            删除状态(已删除/未删除 false/true)
     * @param query
     *            查询条件(记分卡名称)
     * @return Set<String>
     */
    public Set<String> findRiskMapFromNotCategoryRelaOrgEmpBySome(boolean deleteStatus, String query) {
        Set<String> idSet = new HashSet<String>();
        Set<String> otheridSet = this.findCategoryRelaOrgEmpAll();
        Criteria criteria = o_categoryDAO.createCriteria();
        List<Category> strategyList = null;

        criteria.add(not(in("id", otheridSet)));
        if (StringUtils.isNotBlank(query)) {
            criteria.add(like("name", query, MatchMode.ANYWHERE)).add(eq("deleteStatus", deleteStatus));
        }
        strategyList = criteria.list();
        for (Category entity : strategyList) {
            String[] idsTemp = entity.getIdSeq().split("\\.");
            idSet.addAll(Arrays.asList(idsTemp));
        }

        return idSet;
    }

    /**
     * 得到记分卡
     * 
     * @author 金鹏祥
     * @param companyId
     *            公司ID
     * @param parentId
     *            下一节点
     * @param deleteStatus
     *            删除状态(已删除/未删除 false/true)
     * @param isParentIdNull
     *            是否根第一根节点
     * @return
     */
    public List<Category> findNotInOrgStrategyMap(String companyId, String parentId, boolean deleteStatus, boolean isParentIdNull) {
        List<Category> categoryList = null;
        Set<String> otheridSet = this.findCategoryRelaOrgEmpAll();
        Criteria criteria = o_categoryDAO.createCriteria();
        criteria.add(Restrictions.eq("company.id", companyId));
        if (otheridSet.size() > 0) {
            criteria.add(Restrictions.not(Restrictions.in("id", otheridSet)));
        }

        if (isParentIdNull) {
            criteria.add(Restrictions.isNull("parent"));
        }
        else {
            criteria.add(Restrictions.eq("parent.id", parentId));
        }

        criteria.add(Restrictions.eq("deleteStatus", deleteStatus));

        categoryList = criteria.list();

        return categoryList;
    }

    /**
     * 根据记分卡ID查询指标集合
     * 
     * @param page
     *            分页对象
     * @param query
     *            查询条件
     * @param id
     *            记分卡ID
     * @param sort
     *            排序字段
     * @param dir
     *            排序顺序
     * @return
     */
    public Page<Kpi> findKpiByCategoryId(Page<Kpi> page, String query, String id, String sort, String dir) {
        String sortstr = "id";
        DetachedCriteria dc = DetachedCriteria.forClass(Kpi.class);
        dc.createAlias("KpiRelaCategorys", "relacategory");
        dc.add(Restrictions.eq("isKpiCategory", "KPI"));
        dc.add(Restrictions.eq("deleteStatus", true));
        if (StringUtils.isNotBlank(query)) {
            dc.add(Property.forName("name").like(query, MatchMode.ANYWHERE));
        }
        if (StringUtils.isNotBlank(id)) {
            dc.add(Restrictions.eq("relacategory.category.id", id));
        }
        if (StringUtils.equals("name", sort)) {
            sortstr = "name";
        }
        if ("ASC".equalsIgnoreCase(dir)) {
            dc.addOrder(Order.asc(sortstr));
        }
        else {
            dc.addOrder(Order.desc(sortstr));
        }
        return o_kpiDAO.findPage(dc, page, false);
    }

    /**
     * 查询该公司所属记分卡
     * 
     * @author 金鹏祥
     * @return List<Category>
     */
    public List<Category> findCategoryAll() {
        List<Category> list = null;
        Criteria criteria = o_categoryDAO.createCriteria();
        criteria.add(Restrictions.eq("deleteStatus", true));
        criteria.addOrder(Order.desc("level"));
        criteria.add(Restrictions.or(Restrictions.isNull("calc"),Restrictions.eq("calc.id", Contents.DICT_Y)));
        //criteria.add(Restrictions.eq("name", "运营计划风险"));
        //criteria.add(Restrictions.eq("name", "运营风险"));
        
        list = criteria.list();
        return list;
    }

    /**
     * 查询所有记分卡与告警方案
     * 
     * @author 金鹏祥
     * @return HashMap<String, CategoryRelaAlarm>
     */
    public HashMap<String, CategoryRelaAlarm> findCategoryRelaAlarmAll() {
        HashMap<String, CategoryRelaAlarm> map = new HashMap<String, CategoryRelaAlarm>();
        List<CategoryRelaAlarm> list = null;
        Criteria c = this.o_CategoryRelaAlarmDAO.createCriteria();
        c.createAlias("category", "category");
        c.setFetchMode("category", FetchMode.JOIN);
        c.add(Restrictions.isNotNull("rAlarmPlan"));
        list = c.list();
        for (CategoryRelaAlarm categoryRelaAlarm : list) {
            if (categoryRelaAlarm.getCategory() != null) {
                map.put(categoryRelaAlarm.getCategory().getId(), categoryRelaAlarm);
            }
        }
        return map;
    }

    public List<AlarmRegion> findAlarmRegionAll() {
        List<AlarmRegion> list = null;
        Criteria c = o_alarmRegionDAO.createCriteria();
        // c.addOrder(Order.asc("minValue"));
        list = c.list();
        return list;
    }

    public List<AlarmRegion> findAlarmRegionByAlarmId(String alarmId) {
        List<AlarmRegion> list = null;
        Criteria c = o_alarmRegionDAO.createCriteria();
        c.add(Restrictions.eq("alarmPlan.id", alarmId));
        c.addOrder(Order.asc("minValue"));
        list = c.list();
        return list;
    }

    /**
     * 根据计分卡查询最新的告警方案
     * 
     * @param scId 计分卡ID
     * @param type 告警或是预警 forecast:预警,report:告警
     * @return  AlarmPlan 告警方案
     */
    public AlarmPlan findAlarmPlanByScId(String scId, String type) {
        String alarmType = "fcAlarmPlan";
        if ("forecast".equals(type)) {
            alarmType = "fcAlarmPlan";
        }
        else if ("report".equals(type)) {
            alarmType = "rAlarmPlan";
        }
        Date currentDate = new Date();
        CategoryRelaAlarm currentAlarm = null;
        AlarmPlan alarmPlan = null;
        if (StringUtils.isNotBlank(scId)) {
            boolean flag = false;
            CategoryRelaAlarm categoryRelaAlarmTemp = null;
            Criteria criteria = o_CategoryRelaAlarmDAO.createCriteria();
            criteria.add(Property.forName("category.id").eq(scId));
            criteria.setFetchMode(alarmType, JOIN);
            criteria.addOrder(Order.asc("startDate"));
            List<CategoryRelaAlarm> categoryRelaAlarms = criteria.list();
            if (null != categoryRelaAlarms && categoryRelaAlarms.size() > 0) {
                for (int i = 0; i < categoryRelaAlarms.size(); i++) {
                    CategoryRelaAlarm categoryRelaAlarm = categoryRelaAlarms.get(i);
                    Date startDate = categoryRelaAlarm.getStartDate();
                    if (currentDate.before(startDate)) {
                        return null;
                    }
                    if (currentDate.equals(startDate)) {
                        currentAlarm = categoryRelaAlarm;
                        flag = true;
                        break;
                    }
                    else {
                        if (i == categoryRelaAlarms.size() - 1) {
                            categoryRelaAlarmTemp = categoryRelaAlarms.get(i);
                        }
                        else {
                            categoryRelaAlarmTemp = categoryRelaAlarms.get(i + 1);
                        }
                        if (currentDate.after(startDate) && currentDate.before(categoryRelaAlarmTemp.getStartDate())) {
                            currentAlarm = categoryRelaAlarmTemp;
                            flag = true;
                            break;
                        }
                    }
                }
                if (!flag) {
                    currentAlarm = categoryRelaAlarms.get(categoryRelaAlarms.size() - 1);
                }
                if ("forecast".equals(type)) {
                    alarmPlan = currentAlarm.getFcAlarmPlan();
                }
                else if ("report".equals(type)) {
                    alarmPlan = currentAlarm.getrAlarmPlan();
                }
            }
        }

        return alarmPlan;
    }
    
    /**根据记分卡id查询下级记分卡
     * @param categoryId 记分卡id
     * @return
     */
    public List<Category> findChildCategorysByCategoryId(String categoryId){
        Criteria criteria = o_categoryDAO.createCriteria();
        if ("root".equals(categoryId)) {//一级记分卡
            criteria.add(Restrictions.isNull("parent"));
        }
        else {
            criteria.add(Restrictions.eq("parent.id", categoryId));
        }
        //criteria.add(Restrictions.eq("company.id", UserContext.getUser().getCompanyid()));
        criteria.add(Restrictions.eq("deleteStatus", true));
        criteria.setFetchMode("status", SELECT);
        criteria.addOrder(Order.asc("sort"));
        List<Category> categoryList = criteria.list();
        return categoryList;
    }
    
    /**根据记分卡ID查询出下面的指标数量
     * @param id 记分卡ID
     * @return
     */
    public Long  findChildKpiCountByCategoryId(String id){
        Criteria criteria = o_kpiRelaCategoryDAO.createCriteria();
        criteria.createAlias("category", "category");
        criteria.createAlias("kpi", "kpi");
        criteria.add(Restrictions.eq("category.deleteStatus",true));
        criteria.add(Restrictions.eq("category.id", id));
        criteria.add(Restrictions.eq("kpi.level", 1));
        //金融街需求变更,停用了三个指标 经济增加值EVA,经济增加值EVA-政府,经营活动产生的现金流量净额
        criteria.add(Restrictions.eq("kpi.status.id", "0yn_y"));
        criteria.setProjection(Projections.rowCount()) ;       
        return (Long)criteria.uniqueResult();
    }
    
    /**根据记分卡得到所关联的指标
     * @param name记分卡名称
     * @return
     */
    public List<Kpi> findChildKpiByCategoryName(String name){
        List<Kpi> kpiList = new ArrayList<Kpi>();
        Category category = null;
        Criteria criteria = o_categoryDAO.createCriteria();
        criteria.add(Restrictions.eq("deleteStatus", true));
        criteria.add(Restrictions.eq("name", name));
        criteria.createAlias("kpiRelaCategorys", "kpiRelaCategorys");
        criteria.setFetchMode("kpiRelaCategorys", FetchMode.JOIN);
        criteria.createAlias("kpiRelaCategorys.kpi", "kpis");
      //金融街需求变更,停用了三个指标 经济增加值EVA,经济增加值EVA-政府,经营活动产生的现金流量净额
        criteria.add(Restrictions.eq("kpis.status.id", "0yn_y"));
        criteria.setFetchMode("kpis", FetchMode.JOIN);
        criteria.setFetchMode("status", SELECT);
        List<Category> categorys = criteria.list();
        if(null!=categorys&&categorys.size()>0){
            category = categorys.get(0);
            Set<KpiRelaCategory> kpiRelaCategories = category.getKpiRelaCategorys();
            for (KpiRelaCategory kpiRelaCategory : kpiRelaCategories) {
                Kpi kpi = kpiRelaCategory.getKpi();
                if(null!=kpi&&"0yn_y".equals(kpi.getStatus().getId())){
                    kpiList.add(kpi);
                }
            }
        }
        return kpiList;
    }
    
    
    /**根据记分卡ID查询出下面的指标
     * @param id 记分卡ID
     * @return
     */
    public List<Kpi>  findChildKpiByCategoryId(String id){
        Criteria criteria = o_kpiRelaCategoryDAO.createCriteria();
        criteria.createAlias("category", "category");
        criteria.createAlias("kpi", "kpi");
        criteria.add(Restrictions.eq("category.deleteStatus",true));
        criteria.add(Restrictions.eq("category.id", id));
        criteria.addOrder(Order.asc("kpi.sort"));
        List<KpiRelaCategory> relaCategorys = criteria.list();
        List<Kpi> kpilist = new ArrayList<Kpi>();
        for (KpiRelaCategory kpiRelaCategory : relaCategorys) {
            kpilist.add(kpiRelaCategory.getKpi());
        }
        
        
        return kpilist;
    }
    
    /**查询指标关联的风险事件
     * @param kpiId指标ID
     * @return
     */
   public Map<String,Object> findRiskEvent(String kpiId,Page<RiskEvent> page,String sort,String dir,String query){
        Risk risk = null;
        List<RiskEvent> listEvent = null;
        RiskEventForm form = null;
        Map<String, Object> map = new HashMap<String, Object>();
        List<RiskEventForm> datas = new ArrayList<RiskEventForm>();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Kpi kpi = this.o_kpiBO.findKpiById(kpiId);
        Set<KpiRelaRisk> kpiRelaRisks =  kpi.getKpiRelaRisks();//得到指标关联的风险
        for (KpiRelaRisk kpiRelaRisk : kpiRelaRisks) {
            risk = kpiRelaRisk.getRisk();
        }
        if(null!=risk){
            //得到风险对象,调用风险接口,得到该风险下的安全事件
            page = o_riskEventRelatedService.findRiskHistoryEventByRiskId(risk.getId(), page, sort, dir, query);
            listEvent = page.getResult();
        }
        
        //2.查询风险事件相应的告警状态;在指标采集结果表中查询
        //根据指标ID(实时更新的指标)查询该指标的采集结果
        Set<KpiGatherResult> list = kpi.getKpiGatherResult();
        List<KpiGatherResult> gatherList = new ArrayList<KpiGatherResult>(list);
        //风险事件的ID和采集结果表的ID相同;根据id进行匹配,找到相应的风险事件的告警状态
        if(null!=listEvent){
            for(int i=0;i<listEvent.size();i++){
                RiskEvent event = listEvent.get(i);
                String eventId = event.getId();
                String desc = event.getFinanceLostDesc();
                String date = sf.format(event.getOccurDate());
                String amount = event.getFinanceLostAmount().toString();
                String status = "";
                for(int j=0;j<gatherList.size();j++){
                    KpiGatherResult result = gatherList.get(j);
                    if(eventId.equals(result.getId())&&null!=result.getAssessmentStatus()){
                        status = result.getAssessmentStatus().getValue();
                    }
                }
                form = new RiskEventForm();
                form.setDate(date);
                form.setDesc(desc);
                form.setId(eventId);
                form.setStatus(status);
                form.setValue(amount);
                datas.add(form);
            }
        }
        map.put("totalCount", page.getTotalItems());
        map.put("datas", datas);
        return map;
    }
   
   
   /**删除风险事件
    * @param records
    */
   @Transactional
   public void removeRiskEvent(String records){
       JSONObject jsobj = JSONObject.fromObject(records);
       //String kpiName = jsobj.getString("kpiName");
       JSONArray datas = jsobj.getJSONArray("datas");
       for (int i = 0; i < datas.size(); i++) {
           String id = "";
           JSONObject event = datas.getJSONObject(i);
           if(event.containsKey("id")){
               id = event.getString("id");
           }
           //删除事件
           o_riskEventService.removeRiskEventById(id);
           //删除采集结果
           o_kpiGatherResultBO.deleteKpiGatherResult(id);
       }
   }
   
   
    /**保存风险事件
     * @param records
     * @throws ParseException 
     */
    @Transactional
    public void saveRiskEvent(String records) throws ParseException{
        //Kpi kpi = o_kpiBO.findKpiById(kpiId);
        JSONObject jsobj = JSONObject.fromObject(records);
        String kpiName = jsobj.getString("kpiName");
        Kpi kpi = o_kpiBO.findKpiByName(kpiName);
        Risk risk = null;
        Set<KpiRelaRisk> kpiRelaRisks =  kpi.getKpiRelaRisks();//得到指标关联的风险
        for (KpiRelaRisk kpiRelaRisk : kpiRelaRisks) {
            risk = kpiRelaRisk.getRisk();
        }
        AlarmPlan alarmPlan = o_kpiBO.findAlarmPlanByKpiId(kpi.getId(), Contents.ALARMPLAN_REPORT);
        JSONArray datas = jsobj.getJSONArray("datas");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < datas.size(); i++) {
            String id = "";
            String eventUuid = Identities.uuid();
            JSONObject event = datas.getJSONObject(i);
            if(event.containsKey("id")){
                id = event.getString("id");
            }
            String value = event.getString("value");
            String desc = event.getString("desc");
            String date = event.getString("date");
            Double assValue = Double.valueOf(value);
            if(StringUtils.isNotBlank(id)){
                //根据id查询风险事件表,如果有这条风险事件,则保存
                //修改,封装数据 风险事件实体,调用修改接口
                RiskEvent riskEvent = o_riskEventService.findRiskEventById(id);
                riskEvent.setFinanceLostDesc(desc);
                riskEvent.setOccurDate(sf.parse(date));
                riskEvent.setFinanceLostAmount(new BigDecimal(value));
                o_riskEventService.mergeRiskEvent(riskEvent);
                
                
                //更新风险事件告警状态
                DictEntry assessmentStatus = null;
                KpiGatherResult result = o_kpiGatherResultBO.findKpiGatherResultById(id);
                if(null!=alarmPlan){
                    assessmentStatus = o_kpiBO.findKpiAlarmStatusByValues(alarmPlan,assValue);
                }
                result.setId(id);
                result.setAssessmentStatus(assessmentStatus);
                result.setKpi(kpi);
                result.setAssessmentValue(assValue);
                result.setEndTime(sf.parse(date));
                o_kpiGatherResultBO.mergeKpiGatherResult(result);
                
                
            }else{
                //添加,封装数据 风险事件实体,调用添加接口
                RiskEvent riskEvent = new RiskEvent();
                riskEvent.setId(eventUuid);
                riskEvent.setRisk(risk);
                riskEvent.setFinanceLostDesc(desc);
                riskEvent.setOccurDate(sf.parse(date));
                riskEvent.setFinanceLostAmount(BigDecimal.valueOf(Long.valueOf(value)));
                o_riskEventService.mergeRiskEvent(riskEvent);
                
                //添加风险事件告警状态
                DictEntry assessmentStatus = null;
                KpiGatherResult result = new KpiGatherResult();
                result.setId(eventUuid);
                result.setKpi(kpi);
                if(null!=alarmPlan){
                    assessmentStatus = o_kpiBO.findKpiAlarmStatusByValues(alarmPlan,assValue);
                }
                result.setAssessmentStatus(assessmentStatus);
                result.setAssessmentValue(assValue);
                result.setEndTime(sf.parse(date));
                o_kpiGatherResultBO.mergeKpiGatherResult(result);
                
            }
            
        }
        
    }
    
    
}