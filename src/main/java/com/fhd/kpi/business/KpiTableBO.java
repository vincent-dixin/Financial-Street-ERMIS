package com.fhd.kpi.business;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.comm.dao.TimePeriodDAO;
import com.fhd.comm.entity.TimePeriod;
import com.fhd.core.utils.DateUtils;
import com.fhd.kpi.dao.KpiGatherResultDAO;
import com.fhd.kpi.entity.KpiGatherResult;
import com.fhd.sys.dao.dic.DictEntryDAO;
import com.fhd.sys.dao.icon.IconDAO;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.icon.Icon;

/**
 * 动态创建不同类型BO业务
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-12-11		上午9:41:55
 *
 * @see 	 
 */
@Service
public class KpiTableBO {
    @Autowired
    private KpiGatherResultDAO o_kpiGatherResultDao;

    @Autowired
    private TimePeriodDAO o_timePeriodDao;

    @Autowired
    private DictEntryDAO o_dictDAO;

    @Autowired
    private IconDAO o_iconDAO;

    /**
     * 查询周
     * 
     * @author 金鹏祥
     * @param parent_id
     * @param year
     * @return 
     * @since  fhd　Ver 1.1
    */
    @SuppressWarnings("unchecked")
    public List<TimePeriod> findTimePeriodByEType(String eType) {
        Criteria criteria = o_timePeriodDao.createCriteria();
        criteria.add(Restrictions.eq("type", eType));
        return criteria.list();
    }

    /**
     * 根据年id查询某年有多少月
     * 
     * @author 金鹏祥
     * @param kpiid
     * @param year
     * @return
     * @since  fhd　Ver 1.1
    */
    @SuppressWarnings("unchecked")
    public List<TimePeriod> findTimePeriodById(String id, String forOneEType) {
        Criteria criteria = o_timePeriodDao.createCriteria();
        criteria.add(Restrictions.eq("type", forOneEType));
        criteria.add(Restrictions.eq("year", id));
        return criteria.list();
    }

    /**
     * 根据年id查询
     * 
     * @author 金鹏祥
     * @param id
     * @return
     * @since  fhd　Ver 1.1
    */
    public TimePeriod findTimePeriodById(String id) {
        Criteria criteria = o_timePeriodDao.createCriteria();
        criteria.add(Restrictions.eq("id", id));
        return (TimePeriod) criteria.list().get(0);
    }

    /**
     * 查询图片
     * 
     * @author 金鹏祥
     * @param kpiid
     * @param year
     * @return
     * @since  fhd　Ver 1.1
    */
    @SuppressWarnings("unchecked")
    public Map<String, DictEntry> findMapDictEntryAll() {
        Criteria criteria = o_dictDAO.createCriteria();
        List<DictEntry> list = criteria.list();
        Map<String, DictEntry> map = new HashMap<String, DictEntry>();
        for (DictEntry dictEntry : list) {
            map.put(dictEntry.getId(), dictEntry);
        }

        return map;
    }

    /**
     * 查询数据字典
     * 
     * @author 金鹏祥
     * @param kpiid
     * @param year
     * @return
     * @since  fhd　Ver 1.1
    */
    @SuppressWarnings("unchecked")
    public Map<String, Icon> findMapIconAll() {
        Criteria criteria = o_iconDAO.createCriteria();
        List<Icon> list = criteria.list();
        Map<String, Icon> map = new HashMap<String, Icon>();
        for (Icon icon : list) {
            map.put(icon.getCss(), icon);
        }

        return map;
    }

    /**
     * <pre>
     * 根据指标ID查询该指标的所有采集结果
     * </pre>
     * 
     * @author 金鹏祥
     * @param id
     * @return
     * @since  fhd　Ver 1.1
    */
    @SuppressWarnings("unchecked")
    public List<KpiGatherResult> findKpiGatherResultsByKpiId(String kpiId) {
        Criteria criteria = o_kpiGatherResultDao.createCriteria();
        criteria.add(Restrictions.eq("kpi.id", kpiId));
        return criteria.list();
    }

    /**
     * 通过周ID匹配采集结果列表查看是否存在次列表中
     * 
     * @author 金鹏祥
     * @param weekList 通过指标ID得到的采集结果列表
     * @param WeekId 周ID
     * @return TimePeriod
     * @since  fhd　Ver 1.1
    */
    public boolean isKpiGatherResultByTimePeriodId(List<KpiGatherResult> weekList, String WeekId) {
        boolean isBool = false;
        for (KpiGatherResult kpiGatherResult : weekList) {
            if (kpiGatherResult.getTimePeriod().getId().equalsIgnoreCase(WeekId)) {
                isBool = true;
                return isBool;
            }
        }

        return isBool;
    }

    /**
     * 通过周ID匹配采集结果列表得到此采集结果实体
     * 
     * @author 金鹏祥
     * @param weekList 通过指标ID得到的采集结果列表
     * @param WeekId 周ID
     * @return TimePeriod
     * @since  fhd　Ver 1.1
    */
    public KpiGatherResult getKpiGatherResult(List<KpiGatherResult> weekList, String WeekId) {
        for (KpiGatherResult kpiGatherResult : weekList) {
            if (kpiGatherResult.getTimePeriod().getId().equalsIgnoreCase(WeekId)) {
                return kpiGatherResult;
            }
        }

        return null;
    }

    /**
     * 通过周ID匹配周列表找到日期字典实体
     * 
     * @author 金鹏祥
     * @param weekList 周列表
     * @param WeekId 周ID
     * @return TimePeriod
     * @since  fhd　Ver 1.1
    */
    public TimePeriod getTimePeriod(List<TimePeriod> weekList, String id) {
        for (TimePeriod timePeriod : weekList) {
            if (timePeriod.getId().equalsIgnoreCase(id)) {
                return timePeriod;
            }
        }

        return null;
    }

    /**
     * 通过月ID得到周
     * 
     * @author 金鹏祥
     * @param weekList 周LIST
     * @param parentId 月ID
     * @return
     * @since  fhd　Ver 1.1
    */
    public List<TimePeriod> getWeek(List<TimePeriod> weekList, String parentId) {
        List<TimePeriod> list = new ArrayList<TimePeriod>();
        for (TimePeriod timePeriod : weekList) {
            if (timePeriod.getParent().getId().equalsIgnoreCase(parentId)) {
                list.add(timePeriod);
            }
        }
        return list;
    }

    /**
     * 通过月ID得到周
     * 
     * @author 金鹏祥
     * @param weekList 周LIST
     * @param parentId 月ID
     * @return
     * @since  fhd　Ver 1.1
    */
    public List<TimePeriod> getMonth(List<TimePeriod> monthList, String quarter) {
        List<TimePeriod> list = new ArrayList<TimePeriod>();
        for (TimePeriod timePeriod : monthList) {
            if (timePeriod.getParent().getQuarter().equalsIgnoreCase(quarter)) {
                list.add(timePeriod);
            }
        }
        return list;
    }

    /**
     * 取当前时间,格式为 yyyy-MM-dd HH:mm:ss
     * 
     * @author 金鹏祥
     * @return
     * @since  fhd　Ver 1.1
    */
    public int getMonth() {
        String str[] = DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss").toString().split(" ");
        String str2[] = str[0].split("-");
        return Integer.parseInt(str2[1].toString());
    }

    /**
     * 取当前时间,格式为 yyyy-MM-dd HH:mm:ss
     * 
     * @author 金鹏祥
     * @return
     * @since  fhd　Ver 1.1
    */
    public int getYear() {
        String str[] = DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss").toString().split(" ");
        String str2[] = str[0].split("-");
        return Integer.parseInt(str2[0].toString());
    }

    /**
     * 通过当前月份,获得当前季度
     * 
     * @author 金鹏祥
     * @param month 月
     * @return int
    */
    public int getQuarter(int month) {
        if (month <= 3) {
            return 1;
        }
        else if (month > 3 && month <= 6) {
            return 2;
        }
        else if (month > 6 && month <= 9) {
            return 3;
        }
        else if (month > 9 && month <= 12) {
            return 4;
        }

        return 0;
    }

    /**
     * 得到0月格式
     * 
     * @author 金鹏祥
     * @param month
     * @return
     * @since  fhd　Ver 1.1
    */
    public String getMonthStr(int month) {
        String str = "";
        if (month < 10) {
            str = "0" + month;
        }
        else {
            str = String.valueOf(month);
        }

        return str;
    }

    /**
     * 得到上半/下半年(0/1)
     * 
     * @author 金鹏祥
     * @param month
     * @return
     * @since  fhd　Ver 1.1
    */
    public int getHalfYear(int month) {
        if (month <= 6) {
            return 0;
        }
        else {
            return 1;
        }
    }

    public boolean isDate(String time) {
        Date nowdate = new Date();
        String myString = "2012-01-07 23:59:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date d;
        boolean siDateBool = false;
        try {
            d = sdf.parse(myString);
            boolean flag = d.before(nowdate);
            if (flag) {
                //已过日期
                siDateBool = true;
            }
        }
        catch (Exception e) {
        }

        return siDateBool;
    }
}