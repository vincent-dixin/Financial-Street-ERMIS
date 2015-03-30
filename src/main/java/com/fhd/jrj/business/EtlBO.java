package com.fhd.jrj.business;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.comm.business.TimePeriodBO;
import com.fhd.comm.dao.TimePeriodDAO;
import com.fhd.comm.entity.TimePeriod;
import com.fhd.core.utils.DateUtils;
import com.fhd.core.utils.Identities;
import com.fhd.kpi.business.KpiBO;
import com.fhd.kpi.dao.KpiDAO;
import com.fhd.kpi.dao.KpiGatherResultDAO;
import com.fhd.kpi.entity.Kpi;
import com.fhd.kpi.entity.KpiGatherResult;

/**
 * 数据抽取
 *       标准保费、排名、资产负债率、营业收入增长率、
 *       利润（归属于母公司）增长率、毛利率
 * User: vincent
 * Date: 13-4-22
 * Time: 下午4:13
 * To change this template use File | Settings | File Templates.
 */
@Service
@SuppressWarnings("unchecked")
public class EtlBO {
    @Autowired
    private KpiDAO o_kpiDAO;

    @Autowired
    private JdbcTemplate o_jdbcTemplate;

    @Autowired
    private KpiGatherResultDAO o_KpiGatherResultDAO;

    @Autowired
    private TimePeriodDAO o_timePeriodDAO;
    
    @Autowired
    private KpiBO o_kpiBO;
    
    @Autowired
    private TimePeriodBO o_timePeriodBO;
    
    @Autowired
    private JrjBO o_jrjBO;

    private static String STANDARD_PREMIUM_ID = "biaozhun_baofei";//标准保费指标ID

    private static String CINEMA_RANKING_ID = "china_fangdichan_qiye_paiming";//全国影院票房排名指标ID

    private static String OPERATING_INCOME_GROWTHRATE = "jituan_yingye_shouru";//营业收入指标ID

    private static String RETAINED_PROFITS = "jituan_jinglirun";//利润指标ID

    private String convertMonth(String date) {
        String year = date.substring(0, 4);
        String month = date.substring(4);
        return new StringBuffer().append(year).append("mm").append(month).toString();
    }

    private String convertQuarter(String date) {
        String year = date.substring(0, 4);
        String month = date.substring(4);
        Integer quarter = ((Integer.valueOf(month) * 3) / 10) + 1;
        return new StringBuffer().append(year).append("Q").append(quarter).toString();
    }

    /**保存采集结果数据
     * @param id 指标ID
     * @param date 时间区间纬度ID
     * @param value 实际值
     */
    @Transactional
    public void mergeKpiGatherResult(String id, String date, Double value) {
        Kpi kpi = o_kpiDAO.findUniqueBy("id", id);
        KpiGatherResult kpiGatherResult = null;
        Criteria criteria = o_KpiGatherResultDAO.createCriteria();
        criteria.add(Restrictions.eq("kpi.id", id));
        criteria.add(Restrictions.eq("timePeriod.id", date));
        List<KpiGatherResult> kpiGatherResults = criteria.list();
        if (kpiGatherResults.size() > 0) {//采集结果表中存在该数据,则修改
            kpiGatherResult = kpiGatherResults.get(0);
            kpiGatherResult.setFinishValue(value);
        }
        else {//采集结果表中不存在该数据,则插入数据
            TimePeriod timePeriod = o_timePeriodDAO.findUniqueBy("id", date);
            kpiGatherResult = new KpiGatherResult();
            kpiGatherResult.setKpi(kpi);
            kpiGatherResult.setFinishValue(value);
            kpiGatherResult.setId(Identities.uuid());
            kpiGatherResult.setTimePeriod(timePeriod);
            kpiGatherResult.setCompany(kpi.getCompany());
            kpiGatherResult.setBeginTime(timePeriod.getStartTime());
            kpiGatherResult.setEndTime(timePeriod.getEndTime());
        }
        //同比和环比
        o_jrjBO.findValueChr(kpiGatherResult);
        
        o_KpiGatherResultDAO.merge(kpiGatherResult);
        //修改最后更新时间
        String kpiNewValue = o_kpiBO.getKpiNewValue(kpi.getId());
        if (null != kpiNewValue) {
            kpi.setLastTimePeriod(o_timePeriodBO.findTimePeriodById(kpiNewValue));
            o_kpiBO.mergeKpi(kpi);
        }
    }

    /**
     * 保存标准保费
     */
    @Transactional
    public void mergeStandardPremium() {
        Map<String, Object> map = this.findStandardPremium();//查询当前月份数据
        if (null != map) {
            //转换时间为时间纬度
            Double value = 0.0;
            String date = (String) map.get("STATISMONTH");
            BigDecimal bvalue = (null == map.get("ACCCURRENTVAL")) ? null : (BigDecimal) map.get("ACCCURRENTVAL");
            if (null != bvalue) {
                value = Double.valueOf(bvalue.toString());
            }
            date = this.convertQuarter(date);
            //根据指标名称在采集结果表中查询数据,条件为指标ID和时间纬度 
            mergeKpiGatherResult(STANDARD_PREMIUM_ID, date, value);
        }
    }

    /**
     * 保存排名
     */
    @Transactional
    public void mergeCinemaRanking() {
        Map<String, Object> map = this.findCinemaRanking();//查询当前月份数据
        if (null != map) {
            //转换时间为时间纬度
            String date = (String) map.get("MONTH_ID");
            Double value = (null == map.get("CINEMA_NO")) ? null : Double.valueOf((String) map.get("CINEMA_NO"));//排名
            date = this.convertMonth(date);
            //根据指标名称在采集结果表中查询数据,条件为指标ID和时间纬度 
            mergeKpiGatherResult(CINEMA_RANKING_ID, date, value);
        }
    }

    /**
     * 保存营业收入
     */
    @Transactional
    public void mergeOperatingIncomeGrowthRate() {
        Map<String, Object> map = this.findOperatingIncomeGrowthRate();
        if (null != map) {
            //转换时间为时间纬度
            Double value = 0.0;
            BigDecimal bvalue = (null == map.get("MSR_AM")) ? null : (BigDecimal) map.get("MSR_AM");
            if (null != bvalue) {
                value = Double.valueOf(bvalue.toString());
            }
            String date = (String) map.get("TIME_ID");
            date = this.convertMonth(date);
            //根据指标名称在采集结果表中查询数据,条件为指标ID和时间纬度 
            mergeKpiGatherResult(OPERATING_INCOME_GROWTHRATE, date, value);
        }
    }

    /**
     * 保存利润
     */
    @Transactional
    public void mergeRetainedProfits() {
        Map<String, Object> map = this.findRetainedProfits();
        if (null != map) {
            //转换时间为时间纬度
            Double value = 0.0;
            BigDecimal bvalue = (null == map.get("MSR_AM")) ? null : (BigDecimal) map.get("MSR_AM");
            if (null != bvalue) {
                value = Double.valueOf(bvalue.toString());
            }
            String date = (String) map.get("TIME_ID");
            date = this.convertMonth(date);
            //根据指标名称在采集结果表中查询数据,条件为指标ID和时间纬度 
            mergeKpiGatherResult(RETAINED_PROFITS, date, value);
        }
    }

    /**
     * 
     * <pre>
     * 抽取标准保费
     * </pre>
     * 
     * @author 胡迪新
     * @return
     * @since  fhd　Ver 1.1
     */
    public Map<String, Object> findStandardPremium() {//季度指标
        Map<String, Object> result = null;
        List<Map<String, Object>> results = o_jdbcTemplate.queryForList(
                "select * from F_AS_AnnualBusPlan where indexname like ?  and statismonth = ?  ", "%总标准保费收入(万元)%",
                DateUtils.formatDate(new Date(), "yyyyMM"));
        if (results.size() > 0) {
            result = results.get(0);
        }
        return result;
    }

    /**
     * 
     * <pre>
     * 抽取全国影院票房排名
     * </pre>
     * 
     * @author 胡迪新
     * @return
     * @since  fhd　Ver 1.1
     */
    public Map<String, Object> findCinemaRanking() {//月度指标
        Map<String, Object> result = null;
        /*List<Map<String, Object>> results = o_jdbcTemplate.queryForList("select * from od.ex_movie_top where cinema_name like ? and month_id = ?",
                "%首都%", DateUtils.formatDate(new Date(), "yyyyMM"));*/
        List<Map<String, Object>> results = o_jdbcTemplate.queryForList(
                "select * from od.ex_movie_top where cinema_name like ? order by month_id desc", "%首都%");
        if (results.size() > 0) {
            result = results.get(0);
        }
        return result;
    }

    /**
     * 
     * <pre>
     * 抽取营业收入
     * </pre>
     * 
     * @return
     * @since  fhd　Ver 1.1
     */
    public Map<String, Object> findOperatingIncomeGrowthRate() {//月度指标
        Map<String, Object> result = null;
        /*List<Map<String, Object>> results = o_jdbcTemplate.queryForList(
                "select distinct *  from od.ex_gl_peration_actual t  where t.msr_gl_id = ? and corp_id = ?  and time_id=? ", "Msr_FI_PL_0030", "ALL",
                DateUtils.formatDate(new Date(), "yyyyMM"));// order by TIME_ID desc*/
        List<Map<String, Object>> results = o_jdbcTemplate.queryForList(
                "select distinct *  from od.ex_gl_peration_actual t  where t.msr_gl_id = ? and corp_id = ?  order by time_id desc ",
                "Msr_FI_PL_0030", "ALL");// order by TIME_ID desc
        if (results.size() > 0) {
            result = results.get(0);
        }
        return result;
    }

    /**
     * 
     * <pre>
     * 抽取净利润（归属于母公司）
     * </pre>
     * 
     * @return
     * @since  fhd　Ver 1.1
     */
    public Map<String, Object> findRetainedProfits() {//月度指标
        Map<String, Object> result = null;
        /*List<Map<String, Object>> results = o_jdbcTemplate.queryForList(
                "select distinct *  from od.ex_gl_peration_actual t  where msr_gl_id = ?  and corp_id = ? and time_id=? ", "Msr_FI_CM_0033", "ALL",
                DateUtils.formatDate(new Date(), "yyyyMM"));// order by TIME_ID desc*/
        List<Map<String, Object>> results = o_jdbcTemplate.queryForList(
                "select distinct *  from od.ex_gl_peration_actual t  where msr_gl_id = ?  and corp_id = ? order by time_id desc ", "Msr_FI_CM_0033",
                "ALL");
        if (results.size() > 0) {
            result = results.get(0);
        }
        return result;
    }

    /**
     * 
     * <pre>
     * 抽取营业成本
     * </pre>
     * 
     * @return
     * @since  fhd　Ver 1.1
     */
    public Map<String, Object> findCostOfOperation() {//数据重复 指标采集数据待确定
        Map<String, Object> result = null;
        List<Map<String, Object>> results = o_jdbcTemplate.queryForList(
                "select distinct *  from dw.f_gl_profit_loss t  where t.msr_gl_rpt_name like ? and time_id =?  ", "%其中:营业成本%",
                DateUtils.formatDate(new Date(), "yyyyMM"));// order by TIME_ID desc
        if (results.size() > 0) {
            result = results.get(0);
        }
        return result;
    }

    /**
     * 
     * <pre>
     * 抽取资产负债率
     * </pre>
     * 
     * @return
     * @since  fhd　Ver 1.1
     */
    public Map<String, Object> findAssetLiabilityRate() {//数据重复 指标采集数据待确定
        Map<String, Object> result = null;
        List<Map<String, Object>> results = o_jdbcTemplate
                .queryForList(
                        "select rl.msr_name,rt.* from D_RISK_LEVEL rl inner join f_risk_tolerance rt on rl.msr_id = rt.msr_id where msr_name like ? and time_id =?  ",
                        "%资产负债率%", DateUtils.formatDate(new Date(), "yyyyMM"));
        if (results.size() > 0) {
            result = results.get(0);
        }
        return result;
    }

}
