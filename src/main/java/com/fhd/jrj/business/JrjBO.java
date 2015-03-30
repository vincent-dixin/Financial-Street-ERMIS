package com.fhd.jrj.business;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.comm.business.AlarmPlanBO;
import com.fhd.comm.business.CategoryBO;
import com.fhd.comm.business.formula.StatisticFunctionCalculateBO;
import com.fhd.comm.dao.AlarmPlanDAO;
import com.fhd.comm.entity.AlarmPlan;
import com.fhd.comm.entity.AlarmRegion;
import com.fhd.comm.entity.Category;
import com.fhd.comm.entity.TimePeriod;
import com.fhd.core.utils.DateUtils;
import com.fhd.fdc.utils.Contents;
import com.fhd.jrj.business.chart.FCAlarmLineValueVO;
import com.fhd.jrj.business.chart.FCAlarmRangeVO;
import com.fhd.jrj.business.chart.FCAnQuanZhiShuXYChart;
import com.fhd.jrj.business.chart.FCInverseYAlarmLineChart;
import com.fhd.jrj.business.chart.FCMutiColumnLineChart;
import com.fhd.jrj.business.chart.FCMutiInverseYAlarmLineChart;
import com.fhd.jrj.business.chart.FCMutiLineChart;
import com.fhd.jrj.business.chart.FCScrollAlarmLineChart;
import com.fhd.jrj.business.chart.FCSingleColumnLineChart;
import com.fhd.jrj.business.chart.FCSingleColumnVO;
import com.fhd.jrj.business.chart.FCWaiBuXingZhengChuFaXYChart;
import com.fhd.jrj.business.chart.FCXYVo;
import com.fhd.kpi.business.KpiBO;
import com.fhd.kpi.business.KpiGatherResultBO;
import com.fhd.kpi.business.RelaAssessResultBO;
import com.fhd.kpi.business.dynamictable.FCAngularGauge;
import com.fhd.kpi.dao.KpiDAO;
import com.fhd.kpi.entity.Kpi;
import com.fhd.kpi.entity.KpiGatherResult;
import com.fhd.kpi.entity.KpiRelaAlarm;
import com.fhd.kpi.entity.RelaAssessResult;
import com.fhd.risk.business.RiskEventRelatedService;
import com.fhd.risk.entity.KpiRelaRisk;
import com.fhd.risk.entity.Risk;
import com.fhd.risk.entity.RiskEvent;
import com.fhd.sys.entity.dic.DictEntry;

@Service
@SuppressWarnings("unchecked")
public class JrjBO
{

    @Autowired
    private CategoryBO o_categoryBO;

    @Autowired
    private KpiDAO o_kpiDAO;

    @Autowired
    private RelaAssessResultBO o_relaAssessResultBO;

    @Autowired
    private KpiGatherResultBO o_kpiGatherResultBO;

    @Autowired
    private KpiBO o_kpiBO;

    @Autowired
    private RiskEventRelatedService o_riskEventRelatedService;

    @Autowired
    private AlarmPlanDAO o_alarmPlanDAO;

    @Autowired
    private AlarmPlanBO o_alarmPlanBO;
    
    
    public String findLawRiskStatus(String kpiId){
        String status = null;
        SQLQuery sqlQuery = o_kpiDAO.getSession().createSQLQuery("select i.kpi_id,i.ASSESSMENT_STATUS,i.end_time  from t_kpi_kpi_gather_result  i where i.END_TIME = (select max(j.END_TIME) from t_kpi_kpi_gather_result j  where i.KPI_ID=j.kpi_id) and i.kpi_id='"+kpiId+"' ");
        List<Object[]> objs = sqlQuery.list();
        Object[] obj = null;
        if(null!=objs&&objs.size()>0){
            obj = objs.get(0);
        }
        if(obj!=null&&obj[1]==null){
            status = "0alarm_startus_safe";
        }else{
            status = String.valueOf(obj[1]);
        }
        if("waibu_xingzheng_chufa".equals(kpiId)){
            Date endDate = (Date) obj[2];
            Date currentDate = new Date();
            int betweenDays = daysBetween(endDate,currentDate);
            if(betweenDays>30){
                status = "0alarm_startus_safe";
            }
        }
        return status;
    }

    /**自持酒店6个图
     * @param id
     * @param frequecy
     * @return
     */
    public JSONObject createZichijiudianChart(String id, String frequecy, String newyear, String oldyear)
    {
        JSONObject jsobj = new JSONObject();
        JSONArray chartArray = new JSONArray();
        JSONObject ruijiXml = this.findZhiChiJiuDianChartXml("ruiji", "ratio", frequecy, "瑞吉",newyear,oldyear);
        JSONObject weishidingXml = this.findZhiChiJiuDianChartXml("weishiding", "ratio", frequecy, "威斯汀",newyear,oldyear);
        JSONObject meijueXml = this.findZhiChiJiuDianChartXml("meijue", "ratio", frequecy, "美爵",newyear,oldyear);
        JSONObject lishiXml = this.findZhiChiJiuDianChartXml("lishi", "ratio", frequecy, "丽思",newyear,oldyear);
        JSONObject xilaidengXml = this.findZhiChiJiuDianChartXml("xilaideng", "ratio", frequecy, "喜来登",newyear,oldyear);
        JSONObject jinrongjie_gongyuXml = this.findZhiChiJiuDianChartXml("jinrongjie_gongyu", "ratio", frequecy, "金融街公寓",newyear,oldyear);
        chartArray.add(lishiXml.get("xml"));
        chartArray.add(weishidingXml.get("xml"));
        chartArray.add(ruijiXml.get("xml"));
        chartArray.add(xilaidengXml.get("xml"));
        chartArray.add(meijueXml.get("xml"));
        chartArray.add(jinrongjie_gongyuXml.get("xml"));
        jsobj.put("year", ruijiXml.get("year"));
        jsobj.put("xmls", chartArray);
        return jsobj;
    }

    /**
     * 仪表盘xml
     * @param scid 记分卡id
     * @return
     */
    public JSONArray createAngulargaugeChart(String scid, String ctx)
    {
        JSONArray chartArray = new JSONArray();
        if (StringUtils.isNotBlank(scid))
        {
            Category parentCategory = null;
            if (!"root".equals(scid))
            {
                parentCategory = o_categoryBO.findCategoryById(scid).getParent();
            }

            Date currentDate = new Date();
            String dateString = DateUtils.formatDate(currentDate, "yyyyMM");
            String year = dateString.substring(0, 4);
            String month = dateString.substring(4);
            String timePeriodId = new StringBuffer().append(year).append("mm").append(month).toString();
            List<Category> categoryList = o_categoryBO.findChildCategorysByCategoryId(scid);
            for (Category category : categoryList)
            {
                long kpicount = 0;
                String kpicountStr = "";
                if (!"root".equals(scid))
                {
                    kpicount = o_categoryBO.findChildKpiCountByCategoryId(category.getId());
                    if (kpicount != 0)
                    {
                        kpicountStr = String.valueOf(kpicount);
                    }
                }
                else
                {
                    long scKpiCount = 0;
                    List<Category> childCategoryList = o_categoryBO.findChildCategorysByCategoryId(category.getId());
                    for (Category sc : childCategoryList)
                    {
                        scKpiCount += o_categoryBO.findChildKpiCountByCategoryId(sc.getId());
                    }
                    if (scKpiCount != 0)
                    {
                        kpicountStr = String.valueOf(scKpiCount);
                    }
                }
                boolean hasChild = false;
                if (!category.getIsLeaf())
                {
                    hasChild = true;
                }
                JSONObject chartObj = new JSONObject();
                AlarmPlan alarmPlan = o_categoryBO.findAlarmPlanByScId(category.getId(), Contents.ALARMPLAN_REPORT);
                RelaAssessResult relaAssessResultByDataTypeEn = o_relaAssessResultBO.findRelaAssessResultByDataTypeEn(
                        "sc", category.getId(), true, year, timePeriodId);
                if (null != relaAssessResultByDataTypeEn && null != alarmPlan)
                {
                    List<AlarmRegion> alarmRegionList = o_categoryBO.findAlarmRegionByAlarmId(alarmPlan.getId());
                    if (null != relaAssessResultByDataTypeEn.getAssessmentValue())
                    {
                        if(category.getName().equals("法律风险")||category.getName().equals("合规风险")){
                            System.out.println("aa");
                            //查询外部行政监管处罚 id="waibu_xingzheng_chufa"
                            String statusWaiBu = findLawRiskStatus("waibu_xingzheng_chufa");
                            
                            //查询集团内部处罚 id="jituan_neibu_chufa"
                            String statusNeiBu = findLawRiskStatus("jituan_neibu_chufa");
                            
                            if("0alarm_startus_h".equals(statusWaiBu)||"0alarm_startus_h".equals(statusNeiBu)){
                                relaAssessResultByDataTypeEn.setAssessmentValue(12d);
                            }else{
                                relaAssessResultByDataTypeEn.setAssessmentValue(87d);
                            }
                            
                        }
                        String xml = FCAngularGauge.getXml(alarmRegionList,
                                relaAssessResultByDataTypeEn.getAssessmentValue(), category.getName(),
                                category.getId(), hasChild, ctx);
                        chartObj.put("xml", xml);
                        if (null != parentCategory)
                        {
                            chartObj.put("parentscid", parentCategory.getId());
                        }
                        else
                        {
                            if (!"root".equals(scid))
                            {
                                chartObj.put("first", "false");
                            }
                            chartObj.put("parentscid", "root");
                        }
                        if (hasChild)
                        {
                            chartObj.put("hasChild", "true");
                        }
                        else
                        {
                            chartObj.put("hasChild", "false");
                        }
                        String color = findValueColor(relaAssessResultByDataTypeEn.getAssessmentValue(),
                                alarmRegionList);
                        chartObj.put("scid", category.getId());
                        chartObj.put("value", relaAssessResultByDataTypeEn.getAssessmentValue());
                        chartObj.put("name", category.getName());
                        chartObj.put("color", color);
                        chartObj.put("kpicount", kpicountStr);
                        chartArray.add(chartObj);
                    }
                }
            }
        }
        return chartArray;
    }

    private String findValueColor(Double value, List<AlarmRegion> alarmRegionList)
    {
        String color = "";
        for (AlarmRegion alarmRegion : alarmRegionList)
        {
            if (value >= Double.valueOf(alarmRegion.getMinValue())
                    && value <= Double.valueOf(alarmRegion.getMaxValue()))
            {
                String icon = alarmRegion.getAlarmIcon().getId();
                if ("0alarm_startus_h".equals(icon))
                {
                    color = FCAngularGauge.COLOR_R;
                }
                else if ("0alarm_startus_m".equals(icon))
                {
                    color = FCAngularGauge.COLOR_C;
                }
                else if ("0alarm_startus_l".equals(icon))
                {
                    color = FCAngularGauge.COLOR_Y;
                }
                else if ("0alarm_startus_safe".equals(icon))
                {
                    color = FCAngularGauge.COLOR_G;
                }
            }
        }
        return color;
    }

    /**指标状态统计
     * @return
     */
    public JSONObject findKpiStatusCount()
    {
        int redCount = 0;
        int greenCount = 0;
        int orangeCount = 0;
        int yellowCount = 0;

        JSONObject statusResult = new JSONObject();
        StringBuffer sqlbuf = new StringBuffer();
        sqlbuf.append("select kpi.id , result.assessment_status ");
        sqlbuf.append(" from t_kpi_kpi kpi ");
        sqlbuf.append(" left outer join t_kpi_kpi_gather_result result on kpi.id=result.kpi_id and kpi.latest_time_period_id=result.time_period_id ");
        sqlbuf.append(" left outer join t_com_time_period time on result.time_period_id=time.id ");
        sqlbuf.append(" left outer join t_sys_dict_entry  statusdict on result.assessment_status = statusdict.id ");
        sqlbuf.append(" left outer join t_sys_dict_entry  dirdict on result.direction = dirdict.id ");
        sqlbuf.append(" where kpi.is_kpi_category = 'KPI' ");
        sqlbuf.append(" and kpi.id in ('china_fangdichan_qiye_paiming','xianjin_baozheng_nengli','zichijiudian'"
                + ",'hengtai_zhengquan_gongsi','quanguo_yingyuan_paiming','changcheng_baoxiao_gongshi'"
                //+ ",'zhichan_fuzhailv','youxi_fuzhailv','jingji_zhengjiazhi_eva','jingji_zhengjiazhi_eva_shichang',"
                + ",'zhichan_fuzhailv','youxi_fuzhailv','jingji_zhengjiazhi_eva_shichang',"
                ////金融街需求变更,停用了三个指标 经济增加值EVA,经济增加值EVA-政府,经营活动产生的现金流量净额
                //+ "'jingji_zhengjiazhi_eva_zhengfu','jingying_huodong_xianjin_liuliang_jinger','yingye_shouru_zhengzhanglv','yingye_shouru_jihua_wangchenglv','jinglirun_zhengzhanglv_mugongshi','jinglirun_jihua_wanchenglv_mugongshi','zongzhichan_zhouzhuanlv','chengben_feiyong_lirenlv','maolilv','renli_chengben_lirenlv','guanjian_wangwei_kongquelv',"
                + " 'yingye_shouru_zhengzhanglv','yingye_shouru_jihua_wangchenglv','jinglirun_zhengzhanglv_mugongshi','jinglirun_jihua_wanchenglv_mugongshi','zongzhichan_zhouzhuanlv','chengben_feiyong_lirenlv','maolilv','renli_chengben_lirenlv','guanjian_wangwei_kongquelv',"
                + "'fangdichan','baoxian','wuye','wenhua','jituan_neibu_chufa')");
        SQLQuery sqlquery = o_kpiDAO.createSQLQuery(sqlbuf.toString());
        List<Object[]> list = sqlquery.list();
        if (null != list)
        {
            for (Object[] objects : list)
            {
                if (null != objects[1])
                {
                    if ("0alarm_startus_l".equals(objects[1]))
                    {
                        yellowCount++;
                    }
                    else if ("0alarm_startus_m".equals(objects[1]))
                    {
                        orangeCount++;
                    }
                    else if ("0alarm_startus_h".equals(objects[1]))
                    {
                        redCount++;
                    }
                    else if ("0alarm_startus_safe".equals(objects[1]))
                    {
                        greenCount++;
                    }
                }
            }
        }
        String year = DateUtils.getYear(new Date());
        StringBuffer yearBuf = new StringBuffer();
        yearBuf.append(" and rm.end_time>").append("'").append(year).append("-00-00:00:00:00")
                .append("' and rm.end_time<").append("'").append(Integer.valueOf(year) + 1).append("-00-00:00:00:00'");
        Map<String, String> flagMap = new HashMap<String, String>();
        String relaTimeSql = "select r.KPI_ID,r.ASSESSMENT_STATUS,r.END_TIME from t_kpi_kpi_gather_result r where r.END_TIME=(select max(rm.end_time) from   t_kpi_kpi_gather_result rm where rm.kpi_id in ('waibu_xingzheng_chufa','anquan_zhishu') and rm.kpi_id=r.kpi_id and rm.time_period_id is null"
                + yearBuf + ")";
        SQLQuery relaTimeQuery = o_kpiDAO.createSQLQuery(relaTimeSql);
        List<Object[]> relaTimelist = relaTimeQuery.list();
        Date currentDate = new Date();
        if (null != relaTimelist)
        {
            for (Object[] objects : relaTimelist)
            {
                String status = null;
                if(String.valueOf(objects[0]).equals("waibu_xingzheng_chufa")){
                    Integer betweenDays = null;
                    Date endDate = (Date) objects[2];
                    if(currentDate.compareTo(endDate)>=0){
                        betweenDays = daysBetween(endDate,currentDate);
                        if(betweenDays<=30){
                            status = String.valueOf(objects[1]);
                        }else{
                            status = "0alarm_startus_safe";
                        }
                    }else{
                        status = String.valueOf(objects[1]);
                    }
                    
                    flagMap.put(String.valueOf(objects[0]),status );
                    if (null != objects[1])
                    {
                        if ("0alarm_startus_l".equals(status))
                        {
                            yellowCount++;
                        }
                        else if ("0alarm_startus_m".equals(status))
                        {
                            orangeCount++;
                        }
                        else if ("0alarm_startus_h".equals(status))
                        {
                            redCount++;
                        }
                        else if ("0alarm_startus_safe".equals(status))
                        {
                            greenCount++;
                        }
                    }
                }else{
                    flagMap.put(String.valueOf(objects[0]), String.valueOf(objects[1]));
                    if (null != objects[1])
                    {
                        if ("0alarm_startus_l".equals(objects[1]))
                        {
                            yellowCount++;
                        }
                        else if ("0alarm_startus_m".equals(objects[1]))
                        {
                            orangeCount++;
                        }
                        else if ("0alarm_startus_h".equals(objects[1]))
                        {
                            redCount++;
                        }
                        else if ("0alarm_startus_safe".equals(objects[1]))
                        {
                            greenCount++;
                        }
                    }
                }
                
            }
        }
        if (!flagMap.containsKey("waibu_xingzheng_chufa"))
        {
            greenCount++;
        }
        if (!flagMap.containsKey("anquan_zhishu"))
        {
            greenCount++;
        }
        String changchengbaoxian = this.changchengbaoxian();
        if (changchengbaoxian.equals("0"))
        {
            greenCount++;
        }
        if (changchengbaoxian.equals("1"))
        {
            yellowCount++;

        }
        if (changchengbaoxian.equals("2"))
        {
            orangeCount++;
        }
        if (changchengbaoxian.equals("3"))
        {

            redCount++;
        }
        statusResult.put("redcount", redCount);
        statusResult.put("orangecount", orangeCount);
        statusResult.put("yellowcount", yellowCount);
        statusResult.put("greencount", greenCount);
        return statusResult;
    }

    //create by tonglin

    public String changchengbaoxian()
    {
        JSONObject statusResult = new JSONObject();
        statusResult.put("anquan_zhishu", "/images/icons/symbol_jrj_g_sm.gif");
        statusResult.put("waibu_xingzheng_chufa", "/images/icons/symbol_jrj_g_sm.gif");
        StringBuffer sqlbuf = new StringBuffer();
        sqlbuf.append("select kpi.id , result.assessment_status ");
        sqlbuf.append(" from t_kpi_kpi kpi ");
        sqlbuf.append(" left outer join t_kpi_kpi_gather_result result on kpi.id=result.kpi_id and kpi.latest_time_period_id=result.time_period_id ");
        sqlbuf.append(" left outer join t_com_time_period time on result.time_period_id=time.id ");
        sqlbuf.append(" left outer join t_sys_dict_entry  statusdict on result.assessment_status = statusdict.id ");
        sqlbuf.append(" left outer join t_sys_dict_entry  dirdict on result.direction = dirdict.id ");
        sqlbuf.append(" where kpi.is_kpi_category = 'KPI' ");
        //sqlbuf.append(" and kpi.id in ('biaozhun_baofei','qiye_neihan_jiazhi')");
        sqlbuf.append(" and kpi.id in ('zonghe_shouyi_jihua_wanchenglv','qiye_neihan_jiazhi')");
        SQLQuery sqlquery = o_kpiDAO.createSQLQuery(sqlbuf.toString());
        List<Object[]> list = sqlquery.list();
        String flag1 = "10";
        String flag2 = "10";
        String ruselt = "";
        for (Object[] objects : list)
        {
            //if(objects[0].equals("biaozhun_baofei")){
            if (objects[0].equals("zonghe_shouyi_jihua_wanchenglv"))
            {
                if ("0alarm_startus_l".equals(objects[1]))
                {//黄色
                    flag1 = "1";
                }
                else if ("0alarm_startus_m".equals(objects[1]))
                {//橙色
                    flag1 = "2";
                }
                else if ("0alarm_startus_h".equals(objects[1]))
                {//红色
                    flag1 = "3";
                }
                else if ("0alarm_startus_safe".equals(objects[1]))
                {//绿色
                    flag1 = "0";
                }
            }
            if (objects[0].equals("qiye_neihan_jiazhi"))
            {
                if ("0alarm_startus_l".equals(objects[1]))
                {//黄色
                    flag2 = "1";
                }
                else if ("0alarm_startus_m".equals(objects[1]))
                {//橙色
                    flag2 = "2";
                }
                else if ("0alarm_startus_h".equals(objects[1]))
                {//红色
                    flag2 = "3";
                }
                else if ("0alarm_startus_safe".equals(objects[1]))
                {//绿色
                    flag2 = "0";
                }
            }
        }
        if (Integer.parseInt(flag1) != 10 && Integer.parseInt(flag2) != 10)
        {
            if (Integer.parseInt(flag1) > Integer.parseInt(flag2))
            {
                ruselt = flag1;
            }
            else if (Integer.parseInt(flag1) < Integer.parseInt(flag2))
            {
                ruselt = flag2;
            }
            else if (Integer.parseInt(flag1) == Integer.parseInt(flag2))
            {
                ruselt = flag1;
            }
        }
        else if (Integer.parseInt(flag1) == 10 && Integer.parseInt(flag2) != 10)
        {
            ruselt = flag2;
        }
        else if (Integer.parseInt(flag1) != 10 && Integer.parseInt(flag2) == 10)
        {
            ruselt = flag1;
        }

        return ruselt;
    }

    /**
     * 
     *  查询所有指标的状态
     */
    public JSONObject findAllKpiStatus()
    {
        JSONObject statusResult = new JSONObject();
        statusResult.put("anquan_zhishu", "/images/icons/symbol_jrj_g_sm.gif");
        statusResult.put("waibu_xingzheng_chufa", "/images/icons/symbol_jrj_g_sm.gif");
        StringBuffer sqlbuf = new StringBuffer();
        sqlbuf.append("select kpi.id , result.assessment_status, result.GATHER_TIME ");
        sqlbuf.append(" from t_kpi_kpi kpi ");
        sqlbuf.append(" left outer join t_kpi_kpi_gather_result result on kpi.id=result.kpi_id and kpi.latest_time_period_id=result.time_period_id ");
        sqlbuf.append(" left outer join t_com_time_period time on result.time_period_id=time.id ");
        sqlbuf.append(" left outer join t_sys_dict_entry  statusdict on result.assessment_status = statusdict.id ");
        sqlbuf.append(" left outer join t_sys_dict_entry  dirdict on result.direction = dirdict.id ");
        sqlbuf.append(" where kpi.is_kpi_category = 'KPI' ");
        //sqlbuf.append(" and kpi.id in ('china_fangdichan_qiye_paiming','zichijiudian','biaozhun_baofei','qiye_neihan_jiazhi',"
        sqlbuf.append(" and kpi.id in ('china_fangdichan_qiye_paiming','zichijiudian','zonghe_shouyi_jihua_wanchenglv','qiye_neihan_jiazhi',"
                + "'hengtai_zhengquan_yingye_shouru','hengtai_zhengquan_zhichan_shouyi','quanguo_yingyuan_paiming','3_xianjin_baozhang_nengli',"
                //+ "'6_xianjin_baozhang_nengli','zhichan_fuzhailv','youxi_fuzhailv','jingji_zhengjiazhi_eva','jingji_zhengjiazhi_eva_shichang',"
                + "'6_xianjin_baozhang_nengli','zhichan_fuzhailv','youxi_fuzhailv','jingji_zhengjiazhi_eva_shichang',"
                //+ "'jingji_zhengjiazhi_eva_zhengfu','jingying_huodong_xianjin_liuliang_jinger','yingye_shouru_zhengzhanglv','yingye_shouru_jihua_wangchenglv','jinglirun_zhengzhanglv_mugongshi','jinglirun_jihua_wanchenglv_mugongshi','zongzhichan_zhouzhuanlv','chengben_feiyong_lirenlv','maolilv','renli_chengben_lirenlv','guanjian_wangwei_kongquelv',"
                //金融街需求变更,停用了三个指标 经济增加值EVA,经济增加值EVA-政府,经营活动产生的现金流量净额
                + " 'yingye_shouru_zhengzhanglv','yingye_shouru_jihua_wangchenglv','jinglirun_zhengzhanglv_mugongshi','jinglirun_jihua_wanchenglv_mugongshi','zongzhichan_zhouzhuanlv','chengben_feiyong_lirenlv','maolilv','renli_chengben_lirenlv','guanjian_wangwei_kongquelv',"
                + "'fangdichan','baoxian','wuye','wenhua','jituan_neibu_chufa')");
        SQLQuery sqlquery = o_kpiDAO.createSQLQuery(sqlbuf.toString());
        List<Object[]> list = sqlquery.list();
        if (null != list)
        {
            for (Object[] objects : list)
            {
                String image = "/images/icons/symbol_jrj_g_none.gif";
                if (null != objects[1])
                {
                    if ("0alarm_startus_l".equals(objects[1]))
                    {
                        image = "/images/icons/symbol_jrj_y_sm.gif";
                    }
                    else if ("0alarm_startus_m".equals(objects[1]))
                    {
                        image = "/images/icons/symbol_jrj_c_sm.gif";
                    }
                    else if ("0alarm_startus_h".equals(objects[1]))
                    {
                        image = "/images/icons/symbol_jrj_r_sm.gif";
                    }
                    else if ("0alarm_startus_safe".equals(objects[1]))
                    {
                        image = "/images/icons/symbol_jrj_g_sm.gif";
                    }
                }
                // TODO XXX
                if(objects[2] != null ) {
                    if("fangdichan".equals(objects[0]) || "baoxian".equals(objects[0]) || "wuye".equals(objects[0])
                            || "wenhua".equals(objects[0]) || "renli_chengben_lirenlv".equals(objects[0]) ){
                        Date gatherDate = DateUtils.stringToDateToDay(String.valueOf(objects[2]));
                        gatherDate = org.apache.commons.lang3.time.DateUtils.addMonths(gatherDate,1);
                        if(gatherDate.after(new Date()) && "/images/icons/symbol_jrj_r_sm.gif".equals(image)) {
                            image = "/images/icons/symbol_jrj_g_sm.gif";
                        }
                    }
                }

                statusResult.put(objects[0], image);
            }
        }

        //实时更新指标
        String relaTimeSql = "select r.KPI_ID,r.ASSESSMENT_STATUS,r.END_TIME from t_kpi_kpi_gather_result r where r.END_TIME=(select max(rm.end_time) from   t_kpi_kpi_gather_result rm where rm.kpi_id in ('waibu_xingzheng_chufa','anquan_zhishu') and rm.kpi_id=r.kpi_id and rm.time_period_id is null)";
        SQLQuery relaTimeQuery = o_kpiDAO.createSQLQuery(relaTimeSql);
        List<Object[]> relaTimelist = relaTimeQuery.list();
        Date currentDate = new Date();
        if (null != relaTimelist)
        {
            for (Object[] objects : relaTimelist)
            {
                String image = "/images/icons/symbol_jrj_g_none.gif";
                if (null != objects[1])
                {
                    if ("0alarm_startus_l".equals(objects[1]))
                    {
                        image = "/images/icons/symbol_jrj_y_sm.gif";
                    }
                    else if ("0alarm_startus_m".equals(objects[1]))
                    {
                        image = "/images/icons/symbol_jrj_c_sm.gif";
                    }
                    else if ("0alarm_startus_h".equals(objects[1]))
                    {
                        image = "/images/icons/symbol_jrj_r_sm.gif";
                    }
                    else if ("0alarm_startus_safe".equals(objects[1]))
                    {
                        image = "/images/icons/symbol_jrj_g_sm.gif";
                    }
                }
                Integer betweenDays = null;
                Date endDate = (Date) objects[2];
                if(currentDate.compareTo(endDate)>=0){
                    betweenDays = daysBetween(endDate,currentDate);
                    if(betweenDays<=30){
                        statusResult.put(objects[0], image);
                    }
                }else{
                    statusResult.put(objects[0], image);
                }
                
            }
        }

        return statusResult;
    }
    
    
    private  int daysBetween(Date smdate,Date bdate)
    {    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        try
        {
            smdate=sdf.parse(sdf.format(smdate));
            bdate=sdf.parse(sdf.format(bdate)); 
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }  
        
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));           
    }   

    //    add by haojing
    /**
     * 
     *  查询所有指标的红色状态
     */
    public JSONObject findRedStatus()
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        JSONObject statusResult = new JSONObject();
        StringBuffer sqlbuf = new StringBuffer();
        //金融街新需求,添加采集时间
        sqlbuf.append("select kpi.id ,ti.time_period_full_name ");
        //sqlbuf.append("select kpi.id ,kpi.kpi_name");
        //sqlbuf.append("select kpi.id ,tc.CATEGORY_NAME");
        sqlbuf.append(" from t_kpi_kpi kpi ");
        sqlbuf.append(" left outer join (t_kpi_kpi_gather_result result inner join t_com_time_period   ti on result.time_period_id=ti.id ) on kpi.id=result.kpi_id and kpi.latest_time_period_id=result.time_period_id ");
        sqlbuf.append(" left outer join t_com_time_period time on result.time_period_id=time.id ");
        sqlbuf.append(" left outer join t_sys_dict_entry  statusdict on result.assessment_status = statusdict.id ");
        sqlbuf.append(" left outer join t_sys_dict_entry  dirdict on result.direction = dirdict.id ");
        sqlbuf.append(" left outer join t_kpi_kpi_rela_category krc on kpi.ID=krc.KPI_ID");
        sqlbuf.append(" left outer join t_com_category tc on tc.ID=krc.CATEGORY_ID");
        sqlbuf.append(" where kpi.is_kpi_category = 'KPI' ");
        sqlbuf.append(" and statusdict.dict_entry_value = 'icon-ibm-symbol-4-sm'");
        //sqlbuf.append(" and kpi.id in ('china_fangdichan_qiye_paiming','zichijiudian','biaozhun_baofei','qiye_neihan_jiazhi',"
        sqlbuf.append(" and kpi.id in ('china_fangdichan_qiye_paiming','zichijiudian','zonghe_shouyi_jihua_wanchenglv','qiye_neihan_jiazhi',"
                + "'hengtai_zhengquan_yingye_shouru','hengtai_zhengquan_zhichan_shouyi','quanguo_yingyuan_paiming','3_xianjin_baozhang_nengli',"

                //+ "'6_xianjin_baozhang_nengli','zhichan_fuzhailv','youxi_fuzhailv','jingji_zhengjiazhi_eva','jingji_zhengjiazhi_eva_shichang',"
                + "'6_xianjin_baozhang_nengli','zhichan_fuzhailv','youxi_fuzhailv','jingji_zhengjiazhi_eva_shichang',"
                //                + "'jingji_zhengjiazhi_eva_zhengfu','jingying_huodong_xianjin_liuliang_jinger','yingye_shouru_zhengzhanglv','yingye_shouru_jihua_wangchenglv','jinglirun_zhengzhanglv_mugongshi','jinglirun_jihua_wanchenglv_mugongshi','zongzhichan_zhouzhuanlv','chengben_feiyong_lirenlv','maolilv','renli_chengben_lirenlv','guanjian_wangwei_kongquelv',"
                //金融街需求变更,停用了三个指标 经济增加值EVA,经济增加值EVA-政府,经营活动产生的现金流量净额
                + " 'yingye_shouru_zhengzhanglv','yingye_shouru_jihua_wangchenglv','jinglirun_zhengzhanglv_mugongshi','jinglirun_jihua_wanchenglv_mugongshi','zongzhichan_zhouzhuanlv','chengben_feiyong_lirenlv','maolilv','renli_chengben_lirenlv','guanjian_wangwei_kongquelv',"
                + "'fangdichan','baoxian','wuye','wenhua','jituan_neibu_chufa') ORDER BY kpi.esort asc ");

        SQLQuery sqlquery = o_kpiDAO.createSQLQuery(sqlbuf.toString());
        List<Object[]> list = sqlquery.list();
        if (null != list)
        {
            for (Object[] objects : list)
            {
                statusResult.put(objects[0], objects[1]);
            }
        }
        String year = DateUtils.getYear(new Date());
        StringBuffer yearBuf = new StringBuffer();
        yearBuf.append(" and rm.end_time>").append("'").append(year).append("-00-00:00:00:00")
                .append("' and rm.end_time<").append("'").append(Integer.valueOf(year) + 1).append("-00-00:00:00:00'");
        //String relaTimeSql = "select r.KPI_ID,r.ASSESSMENT_STATUS from t_kpi_kpi_gather_result r where r.END_TIME=(select max(rm.end_time) from   t_kpi_kpi_gather_result rm where rm.kpi_id in ('waibu_xingzheng_chufa','anquan_zhishu') and rm.kpi_id=r.kpi_id and rm.time_period_id is null"
        String relaTimeSql = "select r.KPI_ID,r.end_time from t_kpi_kpi_gather_result r where r.END_TIME=(select max(rm.end_time) from   t_kpi_kpi_gather_result rm where rm.kpi_id in ('waibu_xingzheng_chufa','anquan_zhishu') and rm.kpi_id=r.kpi_id and rm.time_period_id is null"
                + yearBuf + ") and  r.ASSESSMENT_STATUS='0alarm_startus_h'";
        SQLQuery relaTimeQuery = o_kpiDAO.createSQLQuery(relaTimeSql);
        List<Object[]> relaTimelist = relaTimeQuery.list();
        if (null != relaTimelist)
        {
            for (Object[] objects : relaTimelist)
            {
                
                Integer betweenDays = null;
                Date endDate = (Date) objects[1];
                if(currentDate.compareTo(endDate)>=0){
                    betweenDays = daysBetween(endDate,currentDate);
                    if(betweenDays<=30){
                        statusResult.put(objects[0], format.format(objects[1]));
                    }
                }else{
                    statusResult.put(objects[0], format.format(objects[1]));
                }
                
                
               
            }
        }
        return statusResult;
    }

    private Map<String, Object> findQuanguoYingyuanPaimingAlarm(List<KpiGatherResult> datas, Set<KpiRelaAlarm> alarms,
            String lineType)
    {
        KpiGatherResult result = null;
        Double maxValue = 0.0;
        Double chartMaxValue = 0.0;
        Double chartMinValue = 0.0;
        Double minValue = 0.0;
        KpiRelaAlarm kpiRelaAlarm = null;
        List<KpiRelaAlarm> alarmlist = new ArrayList<KpiRelaAlarm>(alarms);
        if (null != alarms && alarms.size() > 0)
        {
            kpiRelaAlarm = alarmlist.get(0);
        }
        //得到告警区间
        FCAlarmRangeVO range = new FCAlarmRangeVO();
        //得到告警方案
        AlarmPlan alarmPlan = kpiRelaAlarm.getrAlarmPlan();
        Set<AlarmRegion> regions = alarmPlan.getAlarmRegions();
        for (AlarmRegion alarmRegion : regions)
        {
            DictEntry alarmLevel = alarmRegion.getAlarmIcon();
            if ("0alarm_startus_h".equals(alarmLevel.getId()))
            {
                //五颗星
                List<Double> sortValues = new ArrayList<Double>();
                minValue = Double.valueOf(alarmRegion.getMinValue());
                if (datas.size() > 0)
                {
                    for (int i = 0; i < datas.size(); i++)
                    {
                        result = datas.get(i);
                        if ("finish".equals(lineType))
                        {
                            if (null != result.getFinishValue() && result.getFinishValue() > minValue)
                            {
                                sortValues.add(result.getFinishValue());
                            }
                        }
                        else if ("ratio".equals(lineType))
                        {
                            if (null != result.getRatioValue() && result.getRatioValue() > minValue)
                            {
                                sortValues.add(result.getRatioValue());
                            }
                        }
                        else if ("same".equals(lineType))
                        {
                            if (null != result.getSameValue() && result.getSameValue() > minValue)
                            {
                                sortValues.add(result.getSameValue());
                            }
                        }

                    }
                    if (sortValues.size() > 0)
                    {
                        Collections.sort(sortValues);
                        maxValue = sortValues.get(sortValues.size() - 1) + 3;
                    }
                    else
                    {
                        maxValue = minValue + 3;
                    }

                    chartMaxValue = maxValue;
                }
                range.setRedStartValue(minValue);
                range.setRedEndValue(maxValue);
            }
            else if ("0alarm_startus_l".equals(alarmLevel.getId()))
            {
                //三颗星
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                range.setYellowStartValue(minValue);
                range.setYellowEndValue(maxValue);
            }
            else if ("0alarm_startus_m".equals(alarmLevel.getId()))
            {
                //四颗星
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                range.setOrangeStartValue(minValue);
                range.setOrangeEndValue(maxValue);
            }
            else if ("0alarm_startus_safe".equals(alarmLevel.getId()))
            {
                //绿色块
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                chartMinValue = minValue;
                range.setGreenStartValue(minValue);
                range.setGreenEndValue(maxValue);
            }
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("range", range);
        resultMap.put("max", chartMaxValue);
        resultMap.put("min", chartMinValue);
        return resultMap;
    }

    private Map<String, Object> findSixXianjinBaozhangNengliAlarm(List<KpiGatherResult> datas,
            Set<KpiRelaAlarm> alarms, Double raiseValue)
    {
        KpiGatherResult result = null;
        Double maxValue = 0.0;
        Double chartMaxValue = 0.0;
        Double chartMinValue = 0.0;
        Double minValue = 0.0;
        KpiRelaAlarm kpiRelaAlarm = null;
        List<KpiRelaAlarm> alarmlist = new ArrayList<KpiRelaAlarm>(alarms);
        if (null != alarms && alarms.size() > 0)
        {
            kpiRelaAlarm = alarmlist.get(0);
        }
        //得到告警区间
        FCAlarmRangeVO range = new FCAlarmRangeVO();
        //得到告警方案
        AlarmPlan alarmPlan = kpiRelaAlarm.getrAlarmPlan();
        Set<AlarmRegion> regions = alarmPlan.getAlarmRegions();
        for (AlarmRegion alarmRegion : regions)
        {
            DictEntry alarmLevel = alarmRegion.getAlarmIcon();
            if ("0alarm_startus_h".equals(alarmLevel.getId()))
            {
                //五颗星
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                range.setRedStartValue(minValue);
                range.setRedEndValue(maxValue);
                chartMinValue = minValue;
            }
            else if ("0alarm_startus_l".equals(alarmLevel.getId()))
            {
                //三颗星
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                range.setYellowStartValue(minValue);
                range.setYellowEndValue(maxValue);
            }
            else if ("0alarm_startus_m".equals(alarmLevel.getId()))
            {
                //四颗星
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                range.setOrangeStartValue(minValue);
                range.setOrangeEndValue(maxValue);
            }
            else if ("0alarm_startus_safe".equals(alarmLevel.getId()))
            {
                //绿色块
                minValue = Double.valueOf(alarmRegion.getMinValue());
                List<Double> sortValues = new ArrayList<Double>();
                if (datas.size() > 0)
                {
                    for (int i = 0; i < datas.size(); i++)
                    {
                        result = datas.get(i);
                        if (null != result.getFinishValue() && result.getFinishValue() > minValue)
                        {
                            sortValues.add(result.getFinishValue());
                        }
                    }
                    if (sortValues.size() > 0)
                    {
                        Collections.sort(sortValues);
                        maxValue = sortValues.get(sortValues.size() - 1) + raiseValue;
                    }
                    else
                    {
                        maxValue = minValue + raiseValue;
                    }

                    chartMaxValue = maxValue;
                    chartMaxValue = Double.valueOf(new DecimalFormat("0.00").format(chartMaxValue));
                }
                range.setGreenStartValue(minValue);
                range.setGreenEndValue(chartMaxValue);
            }
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("range", range);
        resultMap.put("max", chartMaxValue);
        resultMap.put("min", chartMinValue);
        return resultMap;
    }

    private Map<String, Object> findJingyingHuodongXianjinLiuliangJingerAlarm(List<KpiGatherResult> datas,
            Set<KpiRelaAlarm> alarms, Double raiseValue)
    {
        KpiGatherResult result = null;
        Double chartMaxValue = 0.0;
        Double chartMinValue = 0.0;
        KpiRelaAlarm kpiRelaAlarm = null;
        List<KpiRelaAlarm> alarmlist = new ArrayList<KpiRelaAlarm>(alarms);
        if (null != alarms && alarms.size() > 0)
        {
            kpiRelaAlarm = alarmlist.get(0);
        }
        //得到告警区间
        FCAlarmRangeVO range = new FCAlarmRangeVO();
        //得到告警方案
        AlarmPlan alarmPlan = kpiRelaAlarm.getrAlarmPlan();
        Set<AlarmRegion> regions = alarmPlan.getAlarmRegions();
        for (AlarmRegion alarmRegion : regions)
        {
            DictEntry alarmLevel = alarmRegion.getAlarmIcon();
            if ("0alarm_startus_h".equals(alarmLevel.getId()))
            {
                //五颗星
                Double maxValue = 0.0;
                Double minValue = 0.0;
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                chartMinValue = minValue;
                range.setRedStartValue(minValue);
                range.setRedEndValue(maxValue);
            }
            else if ("0alarm_startus_l".equals(alarmLevel.getId()))
            {
                //三颗星
                Double maxValue = 0.0;
                Double minValue = 0.0;
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                range.setYellowStartValue(minValue);
                range.setYellowEndValue(maxValue);
            }
            else if ("0alarm_startus_m".equals(alarmLevel.getId()))
            {
                //四颗星
                Double maxValue = 0.0;
                Double minValue = 0.0;
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                range.setOrangeStartValue(minValue);
                range.setOrangeEndValue(maxValue);
            }
            else if ("0alarm_startus_safe".equals(alarmLevel.getId()))
            {
                //绿色块
                Double maxValue = 0.0;
                Double minValue = 0.0;
                minValue = Double.valueOf(alarmRegion.getMinValue());
                List<Double> sortValues = new ArrayList<Double>();
                if (datas.size() > 0)
                {
                    for (int i = 0; i < datas.size(); i++)
                    {
                        result = datas.get(i);
                        if (null != result.getFinishValue() && result.getFinishValue() > minValue)
                        {
                            sortValues.add(result.getFinishValue());
                        }
                    }
                    if (sortValues.size() > 0)
                    {
                        Collections.sort(sortValues);
                        maxValue = sortValues.get(sortValues.size() - 1) + raiseValue;
                    }
                    else
                    {
                        maxValue = minValue + raiseValue;
                    }

                    chartMaxValue = maxValue;
                }
                range.setGreenStartValue(minValue);
                range.setGreenEndValue(maxValue);
            }
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("range", range);
        resultMap.put("max", chartMaxValue);
        resultMap.put("min", chartMinValue);
        return resultMap;
    }

    private Map<String, Object> findThreeXianjinBaozhangNengliAlarm(List<KpiGatherResult> datas,
            Set<KpiRelaAlarm> alarms, Double raiseValue)
    {
        Double maxValue = 0.0;
        Double chartMaxValue = 0.0;
        Double chartMinValue = 0.0;
        Double minValue = 0.0;
        KpiRelaAlarm kpiRelaAlarm = null;
        List<KpiRelaAlarm> alarmlist = new ArrayList<KpiRelaAlarm>(alarms);
        if (null != alarms && alarms.size() > 0)
        {
            kpiRelaAlarm = alarmlist.get(0);
        }
        //得到告警区间
        FCAlarmRangeVO range = new FCAlarmRangeVO();
        //得到告警方案
        AlarmPlan alarmPlan = kpiRelaAlarm.getrAlarmPlan();
        Set<AlarmRegion> regions = alarmPlan.getAlarmRegions();
        for (AlarmRegion alarmRegion : regions)
        {

            DictEntry alarmLevel = alarmRegion.getAlarmIcon();
            if ("0alarm_startus_h".equals(alarmLevel.getId()))
            {
                //五颗星
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                chartMinValue = minValue;
                range.setRedStartValue(minValue);
                range.setRedEndValue(maxValue);
            }
            else if ("0alarm_startus_l".equals(alarmLevel.getId()))
            {
                //三颗星
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                range.setYellowStartValue(minValue);
                range.setYellowEndValue(maxValue);
            }
            else if ("0alarm_startus_m".equals(alarmLevel.getId()))
            {
                //四颗星
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                range.setOrangeStartValue(minValue);
                range.setOrangeEndValue(maxValue);
            }
            else if ("0alarm_startus_safe".equals(alarmLevel.getId()))
            {
                //绿色块
                minValue = Double.valueOf(alarmRegion.getMinValue());
                List<Double> sortValues = new ArrayList<Double>();
                if (datas.size() > 0)
                {
                    for (int i = 0; i < datas.size(); i++)
                    {
                        KpiGatherResult result = datas.get(i);
                        if (null != result.getFinishValue() && result.getFinishValue() > minValue)
                        {
                            sortValues.add(result.getFinishValue());
                        }
                    }
                    if (sortValues.size() > 0)
                    {
                        Collections.sort(sortValues);
                        maxValue = sortValues.get(sortValues.size() - 1) + raiseValue;
                    }
                    else
                    {
                        maxValue = minValue + raiseValue;
                    }

                    chartMaxValue = maxValue;
                }
                range.setGreenStartValue(minValue);
                range.setGreenEndValue(maxValue);
            }

        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("range", range);
        resultMap.put("max", chartMaxValue);
        resultMap.put("min", chartMinValue);
        return resultMap;
    }

    private Map<String, Object> findChinaFangdichanQiyePaimingAlarm(List<KpiGatherResult> datas,
            Set<KpiRelaAlarm> alarms)
    {
        Double maxValue = 0.0;
        Double chartMaxValue = 0.0;
        Double chartMinValue = 0.0;
        Double minValue = 0.0;
        KpiRelaAlarm kpiRelaAlarm = null;
        List<KpiRelaAlarm> alarmlist = new ArrayList<KpiRelaAlarm>(alarms);
        if (null != alarms && alarms.size() > 0)
        {
            kpiRelaAlarm = alarmlist.get(0);
        }
        //得到告警区间
        FCAlarmRangeVO range = new FCAlarmRangeVO();
        //得到告警方案
        AlarmPlan alarmPlan = kpiRelaAlarm.getrAlarmPlan();
        Set<AlarmRegion> regions = alarmPlan.getAlarmRegions();
        for (AlarmRegion alarmRegion : regions)
        {

            DictEntry alarmLevel = alarmRegion.getAlarmIcon();
            if ("0alarm_startus_h".equals(alarmLevel.getId()))
            {
                //五颗星
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                chartMaxValue = maxValue;
                range.setRedStartValue(minValue);
                range.setRedEndValue(maxValue);
            }
            else if ("0alarm_startus_l".equals(alarmLevel.getId()))
            {
                //三颗星
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                range.setYellowStartValue(minValue);
                range.setYellowEndValue(maxValue);
            }
            else if ("0alarm_startus_m".equals(alarmLevel.getId()))
            {
                //四颗星
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                range.setOrangeStartValue(minValue);
                range.setOrangeEndValue(maxValue);
            }
            else if ("0alarm_startus_safe".equals(alarmLevel.getId()))
            {
                //绿色块
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                chartMinValue = minValue;
                range.setGreenStartValue(minValue);
                range.setGreenEndValue(maxValue);
            }
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("range", range);
        resultMap.put("max", chartMaxValue);
        resultMap.put("min", chartMinValue);
        return resultMap;
    }

    private Map<String, Object> findSixAlarm(List<KpiGatherResult> datas, Set<KpiRelaAlarm> alarms)
    {
        Double maxValue = 0.0;
        Double chartMaxValue = 0.0;
        Double chartMinValue = 0.0;
        Double minValue = 0.0;
        KpiRelaAlarm kpiRelaAlarm = null;
        List<KpiRelaAlarm> alarmlist = new ArrayList<KpiRelaAlarm>(alarms);
        if (null != alarms && alarms.size() > 0)
        {
            kpiRelaAlarm = alarmlist.get(0);
        }
        //得到告警区间
        FCAlarmRangeVO range = new FCAlarmRangeVO();
        //得到告警方案
        AlarmPlan alarmPlan = kpiRelaAlarm.getrAlarmPlan();
        Set<AlarmRegion> regions = alarmPlan.getAlarmRegions();
        for (AlarmRegion alarmRegion : regions)
        {

            DictEntry alarmLevel = alarmRegion.getAlarmIcon();
            if ("0alarm_startus_h".equals(alarmLevel.getId()))
            {
                //五颗星
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                chartMaxValue = maxValue;
                range.setRedStartValue(minValue);
                range.setRedEndValue(maxValue);
            }
            else if ("0alarm_startus_l".equals(alarmLevel.getId()))
            {
                //三颗星
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                range.setYellowStartValue(minValue);
                range.setYellowEndValue(maxValue);
            }
            else if ("0alarm_startus_m".equals(alarmLevel.getId()))
            {
                //四颗星
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                range.setOrangeStartValue(minValue);
                range.setOrangeEndValue(maxValue);
            }
            else if ("0alarm_startus_safe".equals(alarmLevel.getId()))
            {
                //绿色块
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                chartMinValue = minValue;
                range.setGreenStartValue(minValue);
                range.setGreenEndValue(maxValue);
            }
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("range", range);
        resultMap.put("max", chartMaxValue);
        resultMap.put("min", chartMinValue);
        return resultMap;
    }

    private Map<String, Object> findQiyeNeihanJiaZhiAlarm(List<KpiGatherResult> datas, Set<KpiRelaAlarm> alarms)
    {
        Double maxValue = 0.0;
        Double chartMaxValue = 0.0;
        Double chartMinValue = 0.0;
        Double minValue = 0.0;
        KpiRelaAlarm kpiRelaAlarm = null;
        List<KpiRelaAlarm> alarmlist = new ArrayList<KpiRelaAlarm>(alarms);
        if (null != alarms && alarms.size() > 0)
        {
            kpiRelaAlarm = alarmlist.get(0);
        }
        //得到告警区间
        FCAlarmRangeVO range = new FCAlarmRangeVO();
        //得到告警方案
        AlarmPlan alarmPlan = kpiRelaAlarm.getrAlarmPlan();
        Set<AlarmRegion> regions = alarmPlan.getAlarmRegions();
        for (AlarmRegion alarmRegion : regions)
        {

            DictEntry alarmLevel = alarmRegion.getAlarmIcon();
            if ("0alarm_startus_h".equals(alarmLevel.getId()))
            {
                //五颗星
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                chartMinValue = minValue;
                range.setRedStartValue(minValue);
                range.setRedEndValue(maxValue);
            }
            else if ("0alarm_startus_l".equals(alarmLevel.getId()))
            {
                //三颗星
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                range.setYellowStartValue(minValue);
                range.setYellowEndValue(maxValue);
            }
            else if ("0alarm_startus_m".equals(alarmLevel.getId()))
            {
                //四颗星
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                range.setOrangeStartValue(minValue);
                range.setOrangeEndValue(maxValue);
            }
            else if ("0alarm_startus_safe".equals(alarmLevel.getId()))
            {
                //绿色块
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                chartMaxValue = maxValue;
                range.setGreenStartValue(minValue);
                range.setGreenEndValue(maxValue);
            }
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("range", range);
        resultMap.put("max", chartMaxValue);
        resultMap.put("min", chartMinValue);
        return resultMap;
    }

    private Map<String, Object> findjingjizhengjiazhievaAlarm(List<KpiGatherResult> datas, Set<KpiRelaAlarm> alarms,
            Double raiseValue)
    {
        KpiGatherResult result = null;
        Double maxValue = 0.0;
        Double chartMaxValue = 0.0;
        Double chartMinValue = 0.0;
        Double minValue = 0.0;
        KpiRelaAlarm kpiRelaAlarm = null;
        List<KpiRelaAlarm> alarmlist = new ArrayList<KpiRelaAlarm>(alarms);
        if (null != alarms && alarms.size() > 0)
        {
            kpiRelaAlarm = alarmlist.get(0);
        }
        //得到告警区间
        FCAlarmRangeVO range = new FCAlarmRangeVO();
        //得到告警方案
        AlarmPlan alarmPlan = kpiRelaAlarm.getrAlarmPlan();
        Set<AlarmRegion> regions = alarmPlan.getAlarmRegions();
        for (AlarmRegion alarmRegion : regions)
        {
            DictEntry alarmLevel = alarmRegion.getAlarmIcon();
            if ("0alarm_startus_h".equals(alarmLevel.getId()))
            {
                //五颗星
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                List<Double> sortValues = new ArrayList<Double>();
                if (datas.size() > 0)
                {
                    for (int i = 0; i < datas.size(); i++)
                    {
                        result = datas.get(i);
                        if (null != result.getFinishValue() && result.getFinishValue() < maxValue)
                        {
                            sortValues.add(result.getFinishValue());
                        }
                    }
                    if (sortValues.size() > 0)
                    {
                        Collections.sort(sortValues);
                        minValue = sortValues.get(0) - raiseValue;
                    }
                    else
                    {
                        minValue = minValue - raiseValue;
                    }

                    chartMinValue = minValue;
                    chartMinValue = Double.valueOf(new DecimalFormat("0.00").format(chartMinValue));
                }

                range.setRedStartValue(minValue);
                range.setRedEndValue(maxValue);
                chartMinValue = minValue;
            }
            else if ("0alarm_startus_l".equals(alarmLevel.getId()))
            {
                //三颗星
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                range.setYellowStartValue(minValue);
                range.setYellowEndValue(maxValue);
            }
            else if ("0alarm_startus_m".equals(alarmLevel.getId()))
            {
                //四颗星
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                range.setOrangeStartValue(minValue);
                range.setOrangeEndValue(maxValue);
            }
            else if ("0alarm_startus_safe".equals(alarmLevel.getId()))
            {
                //绿色块
                minValue = Double.valueOf(alarmRegion.getMinValue());
                List<Double> sortValues = new ArrayList<Double>();
                if (datas.size() > 0)
                {
                    for (int i = 0; i < datas.size(); i++)
                    {
                        result = datas.get(i);
                        if (null != result.getFinishValue() && result.getFinishValue() > minValue)
                        {
                            sortValues.add(result.getFinishValue());
                        }
                    }
                    if (sortValues.size() > 0)
                    {
                        Collections.sort(sortValues);
                        maxValue = sortValues.get(sortValues.size() - 1) + raiseValue;
                    }
                    else
                    {
                        maxValue = minValue + raiseValue;
                    }

                    chartMaxValue = maxValue;
                    chartMaxValue = Double.valueOf(new DecimalFormat("0.00").format(chartMaxValue));
                }
                range.setGreenStartValue(minValue);
                range.setGreenEndValue(chartMaxValue);
            }
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("range", range);
        resultMap.put("max", chartMaxValue);
        resultMap.put("min", chartMinValue);
        return resultMap;
    }

    /**关键岗位空岗率告警图
     * @param id
     * @return
     */
    public String findGuanjianWangweiKongquelv(String id)
    {

        String year = "";
        String frequecy = "0frequecy_quarter";//频率为季度
        Kpi kpi = o_kpiBO.findKpiById(id);//指标对象
        String unit = "单位:" + kpi.getUnits().getName();
        List<FCAlarmLineValueVO> values = new ArrayList<FCAlarmLineValueVO>();
        if (null != kpi.getLastTimePeriod())
        {
            year = kpi.getLastTimePeriod().getYear();
        }
        else
        {
            year = DateUtils.getYear(new Date());
        }
        /*Map<String, Date> timeMap = this.o_kpiGatherResultBO.findTimeRange(year, frequecy, 2, 4);
        Date startTime = timeMap.get("startDate");
        Date endTime = timeMap.get("endDate");
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange(id, startTime, endTime);*/
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByKpiId(id);
        for (KpiGatherResult kpiGatherResult : list)
        {
            FCAlarmLineValueVO vo = new FCAlarmLineValueVO();
            Double lineValue = kpiGatherResult.getFinishValue();//实际值
            if(null==lineValue){
                continue;
            }
            TimePeriod timePeriod = kpiGatherResult.getTimePeriod();
            String categoryName = timePeriod.getYear() + "." + timePeriod.getQuarter() + "季度";
            vo.setValue(lineValue);
            vo.setTime(categoryName);
            values.add(vo);
        }
        Map<String, Object> regionsMap = findChinaFangdichanQiyePaimingAlarm(list, kpi.getKpiRelaAlarms());
        /*String xml = FCAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, "", "0", false);*/
        String xml = FCScrollAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, "", "0", false,true);
        return xml;
    }

    /**人力成本利润率告警图
     * @param id
     * @return
     */
    public String findRenliChengbenLirenlv(String id)
    {
        String year = "";
        String frequecy = "0frequecy_year";//频率为年
        Kpi kpi = o_kpiBO.findKpiById(id);//指标对象
        String unit = "单位:" + kpi.getUnits().getName();
        List<FCAlarmLineValueVO> values = new ArrayList<FCAlarmLineValueVO>();
        if (null != kpi.getLastTimePeriod())
        {
            year = kpi.getLastTimePeriod().getYear();
        }
        else
        {
            year = DateUtils.getYear(new Date());
        }
        /*Map<String, Date> timeMap = this.o_kpiGatherResultBO.findTimeRange(year, frequecy, 1, 2);
        Date startTime = timeMap.get("startDate");
        Date endTime = timeMap.get("endDate");
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange(id, startTime, endTime);*/
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByKpiId(id);
        for (KpiGatherResult kpiGatherResult : list)
        {
            FCAlarmLineValueVO vo = new FCAlarmLineValueVO();
            Double lineValue = null;
            if (null != kpiGatherResult.getSameValue())
            {
                lineValue = kpiGatherResult.getSameValue() * 100;//同比
            }else{
                continue;
            }
            String categoryName = kpiGatherResult.getTimePeriod().getTimePeriodFullName();
            vo.setValue(lineValue);
            vo.setTime(categoryName);
            values.add(vo);
        }
        Map<String, Object> regionsMap = findQiyeNeihanJiaZhiAlarm(list, kpi.getKpiRelaAlarms());
        /*String xml = FCInverseYAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, "", "0");*/
        String xml = FCScrollAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, "", "0",true,false);
        return xml;
    }

    /**毛利率告警图
     * @param id
     * @return
     */
    public String findMaolilv(String id)
    {
        String year = "";
        String frequecy = "0frequecy_month";//频率为年
        Kpi kpi = o_kpiBO.findKpiById(id);//指标对象
        String unit = "单位:" + kpi.getUnits().getName();
        List<FCAlarmLineValueVO> values = new ArrayList<FCAlarmLineValueVO>();
        if (null != kpi.getLastTimePeriod())
        {
            year = kpi.getLastTimePeriod().getYear();
        }
        else
        {
            year = DateUtils.getYear(new Date());
        }
        /*Map<String, Date> timeMap = this.o_kpiGatherResultBO.findTimeRange(year, frequecy, 1, 12);
        Date startTime = timeMap.get("startDate");
        Date endTime = timeMap.get("endDate");
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange(id, startTime, endTime);*/
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByKpiId(id);
        for (KpiGatherResult kpiGatherResult : list)
        {
            FCAlarmLineValueVO vo = new FCAlarmLineValueVO();
            Double lineValue = kpiGatherResult.getFinishValue();//实际值
            if(null==lineValue){
                continue;
            }
            TimePeriod timePeriod = kpiGatherResult.getTimePeriod();
            String categoryName = timePeriod.getYear() + "." + timePeriod.getMonth();
            vo.setValue(lineValue);
            vo.setTime(categoryName);
            values.add(vo);
        }
        Map<String, Object> regionsMap = findSixXianjinBaozhangNengliAlarm(list, kpi.getKpiRelaAlarms(), 6.0);
        /*String xml = FCInverseYAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, "", "1");*/
        String xml = FCScrollAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, "", "1",true,false);
        return xml;
    }

    /**成本费用利润率告警图
     * @param id
     * @return
     */
    public String findChengbenFeiyongLirenlv(String id)
    {
        String year = "";
        String frequecy = "0frequecy_month";//频率为年
        Kpi kpi = o_kpiBO.findKpiById(id);//指标对象
        String unit = "单位:" + kpi.getUnits().getName();
        List<FCAlarmLineValueVO> values = new ArrayList<FCAlarmLineValueVO>();
        if (null != kpi.getLastTimePeriod())
        {
            year = kpi.getLastTimePeriod().getYear();
        }
        else
        {
            year = DateUtils.getYear(new Date());
        }

        /*Map<String, Date> timeMap = this.o_kpiGatherResultBO.findTimeRange(year, frequecy, 1, 12);
        Date startTime = timeMap.get("startDate");
        Date endTime = timeMap.get("endDate");
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange(id, startTime, endTime);*/
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByKpiId(id);
        for (KpiGatherResult kpiGatherResult : list)
        {
            FCAlarmLineValueVO vo = new FCAlarmLineValueVO();
            Double lineValue = kpiGatherResult.getFinishValue();//实际值
            if(null==lineValue){
                continue;
            }
            TimePeriod timePeriod = kpiGatherResult.getTimePeriod();
            String categoryName = timePeriod.getYear() + "." + timePeriod.getMonth();
            vo.setValue(lineValue);
            vo.setTime(categoryName);
            values.add(vo);
        }
        Map<String, Object> regionsMap = findQiyeNeihanJiaZhiAlarm(list, kpi.getKpiRelaAlarms());
       /* String xml = FCInverseYAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, "", "1");*/
        String xml = FCScrollAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, "", "1",true,false);
        return xml;
    }

    /**总资产周转率
     * @param id
     * @return
     */
    public String findZongzhichanZhouzhuanlv(String id)
    {
        String year = "";
        String frequecy = "0frequecy_year";//频率为年
        Kpi kpi = o_kpiBO.findKpiById(id);//指标对象
        //String unit = "单位:" + kpi.getUnits().getName();
        List<FCAlarmLineValueVO> values = new ArrayList<FCAlarmLineValueVO>();
        if (null != kpi.getLastTimePeriod())
        {
            year = kpi.getLastTimePeriod().getYear();
        }
        else
        {
            year = DateUtils.getYear(new Date());
        }
        /*Map<String, Date> timeMap = this.o_kpiGatherResultBO.findTimeRange(year, frequecy, 1, 2);
        Date startTime = timeMap.get("startDate");
        Date endTime = timeMap.get("endDate");
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange(id, startTime, endTime);*/
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByKpiId(id);
        for (KpiGatherResult kpiGatherResult : list)
        {
            FCAlarmLineValueVO vo = new FCAlarmLineValueVO();
            Double lineValue = kpiGatherResult.getFinishValue();//实际值
            if(null==lineValue){
                continue;
            }
            TimePeriod timePeriod = kpiGatherResult.getTimePeriod();
            String categoryName = timePeriod.getYear() + "年";
            vo.setValue(lineValue);
            vo.setTime(categoryName);
            values.add(vo);
        }
        Map<String, Object> regionsMap = findSixXianjinBaozhangNengliAlarm(list, kpi.getKpiRelaAlarms(), 4.0);
        /*String xml = FCInverseYAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", "", "", "0");*/
        String xml = FCScrollAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", "", "", "0",true,false);
        return xml;
    }

    /**经营活动产生的现金流量净额
     * @param id
     * @return
     */
    public String findJingyingHuodongXianjinLiuliangJinger(String id)
    {
        String year = "";
        String unit = "单位:亿元";
        String frequecy = "0frequecy_month";//频率为月
        Kpi kpi = o_kpiBO.findKpiById(id);//指标对象
        List<FCAlarmLineValueVO> values = new ArrayList<FCAlarmLineValueVO>();
        if (null != kpi.getLastTimePeriod())
        {
            year = kpi.getLastTimePeriod().getYear();
        }
        else
        {
            year = DateUtils.getYear(new Date());
        }
        Map<String, Date> timeMap = this.o_kpiGatherResultBO.findTimeRange(year, frequecy, 1, 12);
        Date startTime = timeMap.get("startDate");
        Date endTime = timeMap.get("endDate");
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange(id, startTime, endTime);
        for (KpiGatherResult kpiGatherResult : list)
        {
            FCAlarmLineValueVO vo = new FCAlarmLineValueVO();
            Double lineValue = kpiGatherResult.getFinishValue();//实际值
            TimePeriod timePeriod = kpiGatherResult.getTimePeriod();
            String categoryName = timePeriod.getYear() + "." + timePeriod.getMonth();
            vo.setValue(lineValue);
            vo.setTime(categoryName);
            values.add(vo);
        }
        Map<String, Object> regionsMap = findJingyingHuodongXianjinLiuliangJingerAlarm(list, kpi.getKpiRelaAlarms(),
                3.0);
        String xml = FCInverseYAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, "", "1");
        return xml;
    }

    /**有息负债率
     * @param id
     * @return
     */
    public String findYouxiFuzhailv(String id)
    {
        String year = "";
        String frequecy = "0frequecy_quarter";//频率为季度
        Kpi kpi = o_kpiBO.findKpiById(id);//指标对象
        String unit = "单位:" + kpi.getUnits().getName();
        List<FCAlarmLineValueVO> values = new ArrayList<FCAlarmLineValueVO>();
        if (null != kpi.getLastTimePeriod())
        {
            year = kpi.getLastTimePeriod().getYear();
        }
        else
        {
            year = DateUtils.getYear(new Date());
        }
        /*Map<String, Date> timeMap = this.o_kpiGatherResultBO.findTimeRange(year, frequecy, 2, 4);
        Date startTime = timeMap.get("startDate");
        Date endTime = timeMap.get("endDate");
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange(id, startTime, endTime);*/
        
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByKpiId(id);
        for (KpiGatherResult kpiGatherResult : list)
        {
            FCAlarmLineValueVO vo = new FCAlarmLineValueVO();
            Double lineValue = kpiGatherResult.getFinishValue();//实际值
            if(null==lineValue){
                continue;
            }
            TimePeriod timePeriod = kpiGatherResult.getTimePeriod();
            String categoryName = timePeriod.getYear() + "." + timePeriod.getQuarter() + "季度";
            vo.setValue(lineValue);
            vo.setTime(categoryName);
            values.add(vo);
        }
        Map<String, Object> regionsMap = findQuanguoYingyuanPaimingAlarm(list, kpi.getKpiRelaAlarms(), "finish");
       /* String xml = FCAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, "", "0", false);*/
        String xml = FCScrollAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, "", "0", false,true);
        return xml;
    }

    /**资产负债率
     * @param id
     * @return
     */
    public String findZhichanFuzhailv(String id)
    {
        String year = "";
        String frequecy = "0frequecy_quarter";//频率为季度
        Kpi kpi = o_kpiBO.findKpiById(id);//指标对象
        String unit = "单位:" + kpi.getUnits().getName();
        List<FCAlarmLineValueVO> values = new ArrayList<FCAlarmLineValueVO>();
        if (null != kpi.getLastTimePeriod())
        {
            year = kpi.getLastTimePeriod().getYear();
        }
        else
        {
            year = DateUtils.getYear(new Date());
        }
       /* Map<String, Date> timeMap = this.o_kpiGatherResultBO.findTimeRange(year, frequecy, 2, 4);
        Date startTime = timeMap.get("startDate");
        Date endTime = timeMap.get("endDate");
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange(id, startTime, endTime);*/
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByKpiId(id);
        for (KpiGatherResult kpiGatherResult : list)
        {
            FCAlarmLineValueVO vo = new FCAlarmLineValueVO();
            Double lineValue = kpiGatherResult.getFinishValue();//实际值
            if(null==lineValue){
                continue;
            }
            TimePeriod timePeriod = kpiGatherResult.getTimePeriod();
            String categoryName = timePeriod.getYear() + "." + timePeriod.getQuarter() + "季度";
            vo.setValue(lineValue);
            vo.setTime(categoryName);
            values.add(vo);
        }
        Map<String, Object> regionsMap = findQuanguoYingyuanPaimingAlarm(list, kpi.getKpiRelaAlarms(), "finish");
        /*String xml = FCAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, "", "0", false);*/
        String xml = FCScrollAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, "", "0",false,true);
        return xml;
    }

    /**现金/6个月内刚性支出
     * @param id
     * @return
     */
    public String findSixXianjinBaozhangNengli(String id)
    {
        String year = "";
        String unit = "";
        String frequecy = "0frequecy_quarter";//频率为年
        Kpi kpi = o_kpiBO.findKpiById(id);//指标对象
        List<FCAlarmLineValueVO> values = new ArrayList<FCAlarmLineValueVO>();
        if (null != kpi.getLastTimePeriod())
        {
            year = kpi.getLastTimePeriod().getYear();
        }
        else
        {
            year = DateUtils.getYear(new Date());
        }
       /* Map<String, Date> timeMap = this.o_kpiGatherResultBO.findTimeRange(year, frequecy, 2, 4);
        Date startTime = timeMap.get("startDate");
        Date endTime = timeMap.get("endDate");*/
        //List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange(id, startTime, endTime);
        
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByKpiId(id);
        for (KpiGatherResult kpiGatherResult : list)
        {
            FCAlarmLineValueVO vo = new FCAlarmLineValueVO();
            Double lineValue = kpiGatherResult.getFinishValue();//实际值
            if(null==lineValue){
                continue;
            }
            TimePeriod timePeriod = kpiGatherResult.getTimePeriod();
            String categoryName = timePeriod.getYear() + "." + timePeriod.getQuarter() + "季度";
            vo.setValue(lineValue);
            vo.setTime(categoryName);
            values.add(vo);
        }
        Map<String, Object> regionsMap = findSixXianjinBaozhangNengliAlarm(list, kpi.getKpiRelaAlarms(), 0.3);
        /*String xml = FCInverseYAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, "", "0");*/
        String xml = FCScrollAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, "", "0",true,true);
        return xml;
    }

    /**现金/3个月内刚性支出
     * @param id
     * @return
     */
    public String findThreeXianjinBaozhangNengli(String id)
    {
        String year = "";
        String unit = "";
        String frequecy = "0frequecy_quarter";//频率为年
        Kpi kpi = o_kpiBO.findKpiById(id);//指标对象
        List<FCAlarmLineValueVO> values = new ArrayList<FCAlarmLineValueVO>();
        if (null != kpi.getLastTimePeriod())
        {
            year = kpi.getLastTimePeriod().getYear();
        }
        else
        {
            year = DateUtils.getYear(new Date());
        }
        /*Map<String, Date> timeMap = this.o_kpiGatherResultBO.findTimeRange(year, frequecy, 2, 4);
        Date startTime = timeMap.get("startDate");
        Date endTime = timeMap.get("endDate");
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange(id, startTime, endTime);*/
        //横向带滚动条
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByKpiId(id);
        for (KpiGatherResult kpiGatherResult : list)
        {
            FCAlarmLineValueVO vo = new FCAlarmLineValueVO();
            Double lineValue = kpiGatherResult.getFinishValue();//实际值
            if(null==lineValue){
            	continue;
            }
            TimePeriod timePeriod = kpiGatherResult.getTimePeriod();
            String categoryName = timePeriod.getYear() + "." + timePeriod.getQuarter() + "季度";
            vo.setValue(lineValue);
            vo.setTime(categoryName);
            values.add(vo);
        }
        Map<String, Object> regionsMap = findThreeXianjinBaozhangNengliAlarm(list, kpi.getKpiRelaAlarms(), 0.3);
        //横向带滚动条
        String xml = FCScrollAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"), (Double) regionsMap.get("max"),
                (Double) regionsMap.get("min"), "", unit, "","0",true,true);

        /*String xml = FCInverseYAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, "", "0");*/
        return xml;
    }

    /**首都电影院西单影院
     * @param id
     * @return
     */
    public String findQuanguoYingyuanPaiming(String id)
    {
        String year = "";
        String unit = " ";
        String frequecy = "0frequecy_month";//频率为年
        Kpi kpi = o_kpiBO.findKpiById(id);//指标对象
        List<FCAlarmLineValueVO> values = new ArrayList<FCAlarmLineValueVO>();
        if (null != kpi.getLastTimePeriod())
        {
            year = kpi.getLastTimePeriod().getYear();
        }
        else
        {
            year = DateUtils.getYear(new Date());
        }
        Map<String, Date> timeMap = this.o_kpiGatherResultBO.findTimeRange(year, frequecy, 1, 12);
        Date startTime = timeMap.get("startDate");
        Date endTime = timeMap.get("endDate");
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange(id, startTime, endTime);
        for (KpiGatherResult kpiGatherResult : list)
        {
            FCAlarmLineValueVO vo = new FCAlarmLineValueVO();
            Double lineValue = kpiGatherResult.getFinishValue();//实际值
            TimePeriod timePeriod = kpiGatherResult.getTimePeriod();
            String categoryName = timePeriod.getYear() + "." + timePeriod.getMonth();
            vo.setValue(lineValue);
            vo.setTime(categoryName);
            values.add(vo);
        }
        Map<String, Object> regionsMap = findQuanguoYingyuanPaimingAlarm(list, kpi.getKpiRelaAlarms(), "finish");
        String xml = FCInverseYAlarmLineChart.getXml(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, "", "1");
        return xml;
    }

    /**恒泰证券公司-同组别竞争企业净资产收益率比较
     * @param id
     * @return
     */
    public String findHengtaiZhengquanZhichanShouyi(String id)
    {
        String year = "";
        String frequecy = "0frequecy_halfyear";//频率为年
        Kpi kpi = o_kpiBO.findKpiById(id);//指标对象
        String unit = "单位:" + kpi.getUnits().getName();
        List<FCAlarmLineValueVO> values = new ArrayList<FCAlarmLineValueVO>();
        if (null != kpi.getLastTimePeriod())
        {
            year = kpi.getLastTimePeriod().getYear();
        }
        else
        {
            year = DateUtils.getYear(new Date());
        }
        Map<String, Date> timeMap = this.o_kpiGatherResultBO.findTimeRange(year, frequecy, 1, 3);
        Date startTime = timeMap.get("startDate");
        Date endTime = timeMap.get("endDate");
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange(id, startTime, endTime);
        for (KpiGatherResult kpiGatherResult : list)
        {
            Double lineValue = null;
            FCAlarmLineValueVO vo = new FCAlarmLineValueVO();
            if (null != kpiGatherResult.getAssessmentValue())
            {
                lineValue = kpiGatherResult.getAssessmentValue();
            }
            String categoryName = kpiGatherResult.getTimePeriod().getTimePeriodFullName();
            vo.setValue(lineValue);
            vo.setTime(categoryName);
            values.add(vo);
        }
        Map<String, Object> regionsMap = findQiyeNeihanJiaZhiAlarm(list, kpi.getKpiRelaAlarms());
        String xml = FCInverseYAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, "", "0");
        return xml;
    }

    /*
     * 23.全国影院票房排名
     */
    public JSONObject quanguoyingyuanpaiming(String id, String newyear, String oldyear, Kpi kpi)
    {
        String unit = " ";
        String year = newyear;
        JSONObject jsobj = new JSONObject();
        String frequecy = "0frequecy_month";//频率为年
        List<FCAlarmLineValueVO> values = new ArrayList<FCAlarmLineValueVO>();
        List<KpiRelaAlarm> alarmList = new ArrayList<KpiRelaAlarm>();
        List<KpiGatherResult> list = o_kpiGatherResultBO
                .findYearsKpiGatherResultByKpiIdAndFrequence(id, year, frequecy);
        if (null != list && list.size() == 0)
        {
            list = o_kpiGatherResultBO.findYearsKpiGatherResultByKpiIdAndFrequence(id, oldyear, frequecy);
            year = oldyear;
        }
        Set<KpiRelaAlarm> kpiRelaAlarms = kpi.getKpiRelaAlarms();
        for (KpiRelaAlarm kpiRelaAlarm : kpiRelaAlarms)
        {
            if (year.equals(String.valueOf(DateUtils.getYear(kpiRelaAlarm.getStartDate()))))
            {
                alarmList.add(kpiRelaAlarm);
            }
        }
        if (alarmList.size() == 0)
        {
            KpiRelaAlarm lastAlarm = o_alarmPlanBO.findKpiRelaAlarmLast(id);
            if (null != lastAlarm)
            {
                alarmList.add(lastAlarm);
            }
        }

        Set<KpiRelaAlarm> newKpiRelaAlarms = new HashSet<KpiRelaAlarm>(alarmList);

        for (KpiGatherResult kpiGatherResult : list)
        {
            FCAlarmLineValueVO vo = new FCAlarmLineValueVO();
            Double lineValue = kpiGatherResult.getFinishValue();//实际值
            TimePeriod timePeriod = kpiGatherResult.getTimePeriod();
            String categoryName = timePeriod.getYear() + "." + timePeriod.getMonth();
            vo.setValue(lineValue);
            vo.setTime(categoryName);
            values.add(vo);
        }
        Map<String, Object> regionsMap = findQuanguoYingyuanPaimingAlarm(list, newKpiRelaAlarms, "finish");
        String xml = FCInverseYAlarmLineChart.getXml(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, "", "1");
        jsobj.put("xml", xml);
        jsobj.put("year", year);
        return jsobj;
    }

    public String findKeHuManYiDusChart(String id, String columnType, String lineType)
    {
        String year = "";
        String xml = "";
        String caption = "";
        String frequecy = "0frequecy_year";
        Kpi kpi = o_kpiBO.findKpiById(id);
        String unit = "单位:%";
        if (null != kpi.getUnits())
        {
            unit = kpi.getUnits().getName();
        }
        if (null != kpi.getLastTimePeriod())
        {
            year = kpi.getLastTimePeriod().getYear();
        }
        else
        {
            year = DateUtils.getYear(new Date());
        }
        Map<String, Date> timeMap = this.o_kpiGatherResultBO.findTimeRange(year, frequecy, 1, 2);
        Date startTime = timeMap.get("startDate");
        Date endTime = timeMap.get("endDate");
        List<KpiGatherResult> list = null;
        Set<String> yearList = new HashSet<String>();
        AlarmPlan alarmPlan = this.findAlarmPlanByNameType("客户满意度指标告警", "0alarm_type_kpi_forecast");
        if (null == alarmPlan)
        {
            return "";
        }
        Map<String, Object> alarmMap = this.findKeHuManYiDuAlarm(alarmPlan);

        if ("fangdichan".equals(id))
        {
            List<Double> maxValues = new ArrayList<Double>();
            Map<String, List<FCSingleColumnVO>> dataMap = new HashMap<String, List<FCSingleColumnVO>>();
            list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange("gouwuzhongxin", startTime, endTime);
            List<FCSingleColumnVO> data = new ArrayList<FCSingleColumnVO>();
            dataMap.put("购物中心", data);
            for (KpiGatherResult result : list)
            {
                if (null != result.getSameValue())
                {
                    FCSingleColumnVO vo = new FCSingleColumnVO();
                    vo.setColumnValue(result.getSameValue() * 100);
                    maxValues.add(vo.getColumnValue());
                    vo.setCategory(result.getTimePeriod().getYear());
                    yearList.add(result.getTimePeriod().getYear() + "年");
                    data.add(vo);
                }
            }
            data = new ArrayList<FCSingleColumnVO>();
            dataMap.put("酒店", data);
            list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange("jiudian", startTime, endTime);
            for (KpiGatherResult result : list)
            {
                if (null != result.getSameValue())
                {
                    FCSingleColumnVO vo = new FCSingleColumnVO();
                    vo.setCategory(result.getTimePeriod().getYear());
                    yearList.add(result.getTimePeriod().getYear() + "年");
                    vo.setColumnValue(result.getSameValue() * 100);
                    maxValues.add(vo.getColumnValue());
                    data.add(vo);
                }

            }
            data = new ArrayList<FCSingleColumnVO>();
            dataMap.put("写字楼", data);
            list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange("xiezhilou", startTime, endTime);
            for (KpiGatherResult result : list)
            {
                if (null != result.getSameValue())
                {
                    FCSingleColumnVO vo = new FCSingleColumnVO();
                    vo.setCategory(result.getTimePeriod().getYear());
                    yearList.add(result.getTimePeriod().getYear() + "年");
                    vo.setColumnValue(result.getSameValue() * 100);
                    maxValues.add(vo.getColumnValue());
                    data.add(vo);
                }
            }
            data = new ArrayList<FCSingleColumnVO>();
            dataMap.put("住宅", data);
            list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange("zhuzai", startTime, endTime);
            for (KpiGatherResult result : list)
            {
                if (null != result.getSameValue())
                {
                    FCSingleColumnVO vo = new FCSingleColumnVO();
                    vo.setCategory(result.getTimePeriod().getYear());
                    yearList.add(result.getTimePeriod().getYear() + "年");
                    vo.setColumnValue(result.getSameValue() * 100);
                    maxValues.add(vo.getColumnValue());
                    data.add(vo);
                }

            }
            List<String> ylist = new ArrayList<String>(yearList);
            Collections.sort(ylist);
            Double maxValue = Collections.max(maxValues);
            Double minValue = Collections.min(maxValues);
            if (minValue > (Double) alarmMap.get("min"))
            {
                minValue = (Double) alarmMap.get("min") - 5;
            }
            xml = FCMutiInverseYAlarmLineChart.getXML(dataMap, ylist, (FCAlarmRangeVO) alarmMap.get("range"),
                    maxValue + 5, minValue, "", unit, "", "0");

        }
        else if ("wenhua".equals(id))
        {
            List<Double> maxValues = new ArrayList<Double>();
            Map<String, List<FCSingleColumnVO>> dataMap = new HashMap<String, List<FCSingleColumnVO>>();
            list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange("shoudu_dianyingyuan", startTime, endTime);
            List<FCSingleColumnVO> data = new ArrayList<FCSingleColumnVO>();
            dataMap.put("首都电影院", data);
            for (KpiGatherResult result : list)
            {
                if (null != result.getSameValue())
                {
                    FCSingleColumnVO vo = new FCSingleColumnVO();
                    vo.setCategory(result.getTimePeriod().getYear());
                    yearList.add(result.getTimePeriod().getYear() + "年");
                    vo.setColumnValue(result.getSameValue() * 100);
                    maxValues.add(vo.getColumnValue());
                    data.add(vo);
                }
            }
            list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange("guanggao_gongsi", startTime, endTime);
            data = new ArrayList<FCSingleColumnVO>();
            dataMap.put("广告公司 ", data);
            for (KpiGatherResult result : list)
            {
                if (null != result.getSameValue())
                {
                    FCSingleColumnVO vo = new FCSingleColumnVO();
                    vo.setCategory(result.getTimePeriod().getYear());
                    yearList.add(result.getTimePeriod().getYear() + "年");
                    vo.setColumnValue(result.getSameValue() * 100);
                    maxValues.add(vo.getColumnValue());
                    data.add(vo);
                }
            }
            List<String> ylist = new ArrayList<String>(yearList);
            Collections.sort(ylist);
            Double maxValue = Collections.max(maxValues);
            Double minValue = Collections.min(maxValues);
            if (minValue > (Double) alarmMap.get("min"))
            {
                minValue = (Double) alarmMap.get("min") - 5;
            }
            xml = FCMutiInverseYAlarmLineChart.getXML(dataMap, ylist, (FCAlarmRangeVO) alarmMap.get("range"),
                    maxValue + 5, minValue, "", unit, "", "0");
        }
        else if ("wuye".equals(id))
        {
            List<Double> maxValues = new ArrayList<Double>();
            Map<String, List<FCSingleColumnVO>> dataMap = new HashMap<String, List<FCSingleColumnVO>>();
            list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange("wuyeshangye", startTime, endTime);
            List<FCSingleColumnVO> data = new ArrayList<FCSingleColumnVO>();
            dataMap.put("购物中心", data);
            for (KpiGatherResult result : list)
            {
                if (null != result.getSameValue())
                {
                    FCSingleColumnVO vo = new FCSingleColumnVO();
                    vo.setCategory(result.getTimePeriod().getYear());
                    yearList.add(result.getTimePeriod().getYear() + "年");
                    vo.setColumnValue(result.getSameValue() * 100);
                    maxValues.add(vo.getColumnValue());
                    data.add(vo);
                }
            }
            list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange("wuyezhuzhai", startTime, endTime);
            data = new ArrayList<FCSingleColumnVO>();
            dataMap.put("住宅", data);
            for (KpiGatherResult result : list)
            {
                if (null != result.getSameValue())
                {
                    FCSingleColumnVO vo = new FCSingleColumnVO();
                    vo.setCategory(result.getTimePeriod().getYear());
                    yearList.add(result.getTimePeriod().getYear() + "年");
                    vo.setColumnValue(result.getSameValue() * 100);
                    maxValues.add(vo.getColumnValue());
                    data.add(vo);
                }
            }
            list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange("wuyexiezilou", startTime, endTime);
            data = new ArrayList<FCSingleColumnVO>();
            dataMap.put("写字楼", data);
            for (KpiGatherResult result : list)
            {
                if (null != result.getSameValue())
                {
                    FCSingleColumnVO vo = new FCSingleColumnVO();
                    vo.setCategory(result.getTimePeriod().getYear());
                    yearList.add(result.getTimePeriod().getYear() + "年");
                    vo.setColumnValue(result.getSameValue() * 100);
                    maxValues.add(vo.getColumnValue());
                    data.add(vo);
                }
            }
            List<String> ylist = new ArrayList<String>(yearList);
            Collections.sort(ylist);
            Double maxValue = Collections.max(maxValues);
            Double minValue = Collections.min(maxValues);
            if (minValue > (Double) alarmMap.get("min"))
            {
                minValue = (Double) alarmMap.get("min") - 5;
            }
            xml = FCMutiInverseYAlarmLineChart.getXML(dataMap, ylist, (FCAlarmRangeVO) alarmMap.get("range"),
                    maxValue + 5, minValue, "", unit, "", "0");
        }
        else if ("baoxian".equals(id))
        {
            List<Double> maxValues = new ArrayList<Double>();
            Map<String, List<FCSingleColumnVO>> dataMap = new HashMap<String, List<FCSingleColumnVO>>();
            list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange("baoxian", startTime, endTime);
            List<FCSingleColumnVO> data = new ArrayList<FCSingleColumnVO>();
            dataMap.put("保险", data);
            for (KpiGatherResult result : list)
            {
                if (null != result.getSameValue())
                {
                    FCSingleColumnVO vo = new FCSingleColumnVO();
                    vo.setCategory(result.getTimePeriod().getYear());
                    yearList.add(result.getTimePeriod().getYear() + "年");
                    vo.setColumnValue(result.getSameValue() * 100);
                    maxValues.add(vo.getColumnValue());
                    data.add(vo);
                }
            }
            List<String> ylist = new ArrayList<String>(yearList);
            Collections.sort(ylist);
            Double maxValue = Collections.max(maxValues);
            Double minValue = Collections.min(maxValues);
            if (minValue > (Double) alarmMap.get("min"))
            {
                minValue = (Double) alarmMap.get("min") - 5;
            }
            xml = FCMutiInverseYAlarmLineChart.getXML(dataMap, ylist, (FCAlarmRangeVO) alarmMap.get("range"),
                    maxValue + 5, minValue, "", unit, "", "0");
        }
        return xml;
    }


    
    
    

    /*
     * 可以翻页 
     * 22.（恒泰证券营业收入-竞争组别平均值）/竞争组别平均值
     */
    public JSONObject findHengtaiZhengquanYingyeShouru(String id, String newyear, String oldyear, Kpi kpi)
    {
        JSONObject jsobj = new JSONObject();
        if("2011".equals(newyear)){
            newyear = "2012";
        }
        String year = newyear;
        String frequecy = "0frequecy_halfyear";//频率为半年
        String unit = "单位:" + kpi.getUnits().getName();
        List<FCAlarmLineValueVO> values = new ArrayList<FCAlarmLineValueVO>();
        List<KpiRelaAlarm> alarmList = new ArrayList<KpiRelaAlarm>();
        List<KpiGatherResult> list = o_kpiGatherResultBO.findYearsKpiGatherResultByKpiIdAndFrequence(id, year, frequecy);
        if (null != list && list.size() == 0)
        {
            list = o_kpiGatherResultBO.findYearsKpiGatherResultByKpiIdAndFrequence(id, oldyear, frequecy);
            year = oldyear;
        }
        Set<KpiRelaAlarm> kpiRelaAlarms = kpi.getKpiRelaAlarms();
        for (KpiRelaAlarm kpiRelaAlarm : kpiRelaAlarms)
        {
            if (year.equals(String.valueOf(DateUtils.getYear(kpiRelaAlarm.getStartDate()))))
            {
                alarmList.add(kpiRelaAlarm);
            }
        }
        if (alarmList.size() == 0)
        {
            KpiRelaAlarm lastAlarm = o_alarmPlanBO.findKpiRelaAlarmLast(id);
            if (null != lastAlarm)
            {
                alarmList.add(lastAlarm);
            }
        }

        Set<KpiRelaAlarm> newKpiRelaAlarms = new HashSet<KpiRelaAlarm>(alarmList);
        if(!"2012".equals(newyear)&&!"2013".equals(newyear)){
            for (KpiGatherResult kpiGatherResult : list)
            {
                if(null!=kpiGatherResult.getTimePeriod()&&kpiGatherResult.getTimePeriod().getId().contains("hf1")){
                    Double lineValue = null;
                    FCAlarmLineValueVO vo = new FCAlarmLineValueVO();
                    if (null != kpiGatherResult.getAssessmentValue())
                    {
                        lineValue = kpiGatherResult.getAssessmentValue();
                    }
                    String categoryName = kpiGatherResult.getTimePeriod().getYear()+"年";
                    vo.setValue(lineValue);
                    vo.setTime(categoryName);
                    values.add(vo);
                }
            }
        }else{
            for (KpiGatherResult kpiGatherResult : list)
            {
                    Double lineValue = null;
                    FCAlarmLineValueVO vo = new FCAlarmLineValueVO();
                    if (null != kpiGatherResult.getAssessmentValue())
                    {
                        lineValue = kpiGatherResult.getAssessmentValue();
                    }
                    String categoryName = kpiGatherResult.getTimePeriod().getTimePeriodFullName();
                    vo.setValue(lineValue);
                    vo.setTime(categoryName);
                    values.add(vo);
            }
        }
        
        Map<String, Object> regionsMap = findQiyeNeihanJiaZhiAlarm(list, newKpiRelaAlarms);
        String xml = FCInverseYAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, "", "0");
        jsobj.put("xml", xml);
        jsobj.put("year", year);
        return jsobj;
    }

    /**恒泰证券公司-同组别竞争企业营业收入比较告警图
     * @param id
     * @return
     */
    public String findHengtaiZhengquanYingyeShouru(String id)
    {
        String year = "";
        String frequecy = "0frequecy_halfyear";//频率为半年
        Kpi kpi = o_kpiBO.findKpiById(id);//指标对象
        String unit = "单位:" + kpi.getUnits().getName();
        List<FCAlarmLineValueVO> values = new ArrayList<FCAlarmLineValueVO>();
        if (null != kpi.getLastTimePeriod())
        {
            year = kpi.getLastTimePeriod().getYear();
        }
        else
        {
            year = DateUtils.getYear(new Date());
        }
        Map<String, Date> timeMap = this.o_kpiGatherResultBO.findTimeRange(year, frequecy, 1, 3);
        Date startTime = timeMap.get("startDate");
        Date endTime = timeMap.get("endDate");
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange(id, startTime, endTime);
        for (KpiGatherResult kpiGatherResult : list)
        {
            Double lineValue = null;
            FCAlarmLineValueVO vo = new FCAlarmLineValueVO();
            if (null != kpiGatherResult.getAssessmentValue())
            {
                lineValue = kpiGatherResult.getAssessmentValue();
            }
            String categoryName = kpiGatherResult.getTimePeriod().getTimePeriodFullName();
            vo.setValue(lineValue);
            vo.setTime(categoryName);
            values.add(vo);
        }
        Map<String, Object> regionsMap = findQiyeNeihanJiaZhiAlarm(list, kpi.getKpiRelaAlarms());
        String xml = FCInverseYAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, "", "0");
        return xml;
    }

    /**企业内涵价值告警图
     * @param id
     * @return
     */
    public String findQiyeNeihanJiaZhiAlarmLineChartXml(String id)
    {
        String year = "";
        String frequecy = "0frequecy_year";//频率为年
        Kpi kpi = o_kpiBO.findKpiById(id);//指标对象
        String unit = "单位:" + kpi.getUnits().getName();
        List<FCAlarmLineValueVO> values = new ArrayList<FCAlarmLineValueVO>();
        if (null != kpi.getLastTimePeriod())
        {
            year = kpi.getLastTimePeriod().getYear();
        }
        else
        {
            year = DateUtils.getYear(new Date());
        }
        /*Map<String, Date> timeMap = this.o_kpiGatherResultBO.findTimeRange(year, frequecy, 1, 2);
        Date startTime = timeMap.get("startDate");
        Date endTime = timeMap.get("endDate");
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange(id, startTime, endTime);*/
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByKpiId(id);
        for (KpiGatherResult kpiGatherResult : list)
        {
            FCAlarmLineValueVO vo = new FCAlarmLineValueVO();
            Double lineValue = null;
            if (null != kpiGatherResult.getSameValue())
            {
                lineValue = kpiGatherResult.getSameValue() * 100;//同比
            }
            /*else{
                continue;
            }*/
            String categoryName = kpiGatherResult.getTimePeriod().getTimePeriodFullName();
            vo.setValue(lineValue);
            vo.setTime(categoryName);
            values.add(vo);
        }
        Map<String, Object> regionsMap = findQiyeNeihanJiaZhiAlarm(list, kpi.getKpiRelaAlarms());
        /*String xml = FCInverseYAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, "", "0");*/
        String xml = FCScrollAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, "", "0",true,false);
        return xml;
    }

    /**标准保费告警图
     * @param id
     * @return
     */
    public String findBiaoZhunBaoFeiAlarmLineChartXml(String id)
    {
        String year = "";
        String frequecy = "0frequecy_quarter";//频率为季度
        Kpi kpi = o_kpiBO.findKpiById(id);//指标对象
        String unit = "单位:" + kpi.getUnits().getName();
        List<FCAlarmLineValueVO> values = new ArrayList<FCAlarmLineValueVO>();
        if (null != kpi.getLastTimePeriod())
        {
            year = kpi.getLastTimePeriod().getYear();
        }
        else
        {
            year = DateUtils.getYear(new Date());
        }
        Map<String, Date> timeMap = this.o_kpiGatherResultBO.findTimeRange(year, frequecy, 2, 4);
        Date startTime = timeMap.get("startDate");
        Date endTime = timeMap.get("endDate");
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange(id, startTime, endTime);
        for (KpiGatherResult kpiGatherResult : list)
        {
            FCAlarmLineValueVO vo = new FCAlarmLineValueVO();
            Double lineValue = null;
            if (null != kpiGatherResult.getRatioValue())
            {
                lineValue = kpiGatherResult.getRatioValue() * 100;//环比
            }
            TimePeriod timeperiod = kpiGatherResult.getTimePeriod();
            String categoryName = timeperiod.getYear() + "." + timeperiod.getQuarter() + "季度";
            vo.setValue(lineValue);
            vo.setTime(categoryName);
            values.add(vo);
        }
        Map<String, Object> regionsMap = findQiyeNeihanJiaZhiAlarm(list, kpi.getKpiRelaAlarms());
        String xml = FCInverseYAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, "", "0");
        return xml;
    }

    /**全国影院票房排名告警图
     * @param id
     * @param lineType
     * @param frequence
     * @param caption
     * @return
     */
    public String findChinaFangDiChanQiyePaimingAlarmLineChartXml(String id)
    {
        String year = "";
        String unit = "";
        String frequecy = "0frequecy_year";//频率为年
        Kpi kpi = o_kpiBO.findKpiById(id);//指标对象
        List<FCAlarmLineValueVO> values = new ArrayList<FCAlarmLineValueVO>();
        if (null != kpi.getLastTimePeriod())
        {
            year = kpi.getLastTimePeriod().getYear();
        }
        else
        {
            year = DateUtils.getYear(new Date());
        }
        Map<String, Date> timeMap = this.o_kpiGatherResultBO.findTimeRange(year, frequecy, 9, 2);
        Date startTime = timeMap.get("startDate");
        Date endTime = timeMap.get("endDate");
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange(id, startTime, endTime);
        for (KpiGatherResult kpiGatherResult : list)
        {
            FCAlarmLineValueVO vo = new FCAlarmLineValueVO();
            Double lineValue = kpiGatherResult.getSameValue();//同比
            String categoryName = kpiGatherResult.getTimePeriod().getTimePeriodFullName();
            vo.setValue(lineValue);
            vo.setTime(categoryName);
            values.add(vo);
        }
        Map<String, Object> regionsMap = findChinaFangdichanQiyePaimingAlarm(list, kpi.getKpiRelaAlarms());
        String xml = FCInverseYAlarmLineChart.getXml(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, "", "0");
        return xml;
    }
    
    
    public JSONObject findkehumanyiduchart(String id, String newyear, String oldyear)
    {
        String year = newyear;
        String frequecy = "0frequecy_year";//频率为半年
        JSONObject jsobj = new JSONObject();
        Kpi kpi = o_kpiBO.findKpiById(id);
        //String unit = "单位:" + kpi.getUnits().getName();
        String unit = "" ;
        String kname = kpi.getName();
        if(StringUtils.isNotBlank(kpi.getShortName())){
            kname = kpi.getShortName();
        }
        List<FCAlarmLineValueVO> values = new ArrayList<FCAlarmLineValueVO>();
        List<KpiRelaAlarm> alarmList = new ArrayList<KpiRelaAlarm>();
        List<KpiGatherResult> list = o_kpiGatherResultBO.findYearsKpiGatherResultByKpiIdAndFrequence(id, year, frequecy);
        if (null != list && list.size() == 0)
        {
            list = o_kpiGatherResultBO.findYearsKpiGatherResultByKpiIdAndFrequence(id, oldyear, frequecy);
            year = oldyear;
        }
        Set<KpiRelaAlarm> kpiRelaAlarms = kpi.getKpiRelaAlarms();
        for (KpiRelaAlarm kpiRelaAlarm : kpiRelaAlarms)
        {
            if (year.equals(String.valueOf(DateUtils.getYear(kpiRelaAlarm.getStartDate()))))
            {
                alarmList.add(kpiRelaAlarm);
            }
        }
        if (alarmList.size() == 0)
        {
            KpiRelaAlarm lastAlarm = o_alarmPlanBO.findKpiRelaAlarmLast(id);
            if (null != lastAlarm)
            {
                alarmList.add(lastAlarm);
            }
        }


        for (KpiGatherResult kpiGatherResult : list)
        {
            Double lineValue = null;
            FCAlarmLineValueVO vo = new FCAlarmLineValueVO();
            if (null != kpiGatherResult.getAssessmentValue())
            {
                lineValue = kpiGatherResult.getAssessmentValue();
            }else{
                continue;
            }
            String categoryName = kpiGatherResult.getTimePeriod().getTimePeriodFullName();
            vo.setValue(lineValue);
            vo.setTime(categoryName);
            values.add(vo);
        }
        AlarmPlan alarmPlan = alarmList.get(0).getrAlarmPlan();
        Map<String, Object> regionsMap = findkehumanyidualarm(alarmPlan);
        String xml = FCInverseYAlarmLineChart.getXML(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit,kname, "0");
        
        jsobj.put("xml", xml);
        jsobj.put("year", year);
        return jsobj;
    }

    /**瑞吉,威斯汀,美爵,丽思,喜来登,金融街公寓
     * @param id
     * @param lineType
     * @param frequecy
     * @param caption
     * @return
     */
    public JSONObject findZhiChiJiuDianChartXml(String id, String lineType, String frequecy, String caption, String newyear, String oldyear)
    {
        String year = newyear;
        String unit = "";
        //"0frequecy_month";//频率为月
        JSONObject jsobj = new JSONObject();
        Kpi kpi = o_kpiBO.findKpiById(id);//指标对象
        /*List<FCAlarmLineValueVO> values = new ArrayList<FCAlarmLineValueVO>();
        if (null != kpi.getLastTimePeriod())
        {
            year = kpi.getLastTimePeriod().getYear();
        }
        else
        {
            year = DateUtils.getYear(new Date());
        }
        Map<String, Date> timeMap = this.o_kpiGatherResultBO.findTimeRange(year, frequecy, 1, 12);
        Date startTime = timeMap.get("startDate");
        Date endTime = timeMap.get("endDate");
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange(id, startTime, endTime);*/
        
        
        List<FCAlarmLineValueVO> values = new ArrayList<FCAlarmLineValueVO>();
        List<KpiRelaAlarm> alarmList = new ArrayList<KpiRelaAlarm>();
        List<KpiGatherResult> list = o_kpiGatherResultBO.findYearsKpiGatherResultByKpiIdAndFrequence(id, year, frequecy);
        if (null != list && list.size() == 0)
        {
            list = o_kpiGatherResultBO.findYearsKpiGatherResultByKpiIdAndFrequence(id, oldyear, frequecy);
            year = oldyear;
        }
        Set<KpiRelaAlarm> kpiRelaAlarms = kpi.getKpiRelaAlarms();
        for (KpiRelaAlarm kpiRelaAlarm : kpiRelaAlarms)
        {
            if (year.equals(String.valueOf(DateUtils.getYear(kpiRelaAlarm.getStartDate()))))
            {
                alarmList.add(kpiRelaAlarm);
            }
        }
        if (alarmList.size() == 0)
        {
            KpiRelaAlarm lastAlarm = o_alarmPlanBO.findKpiRelaAlarmLast(id);
            if (null != lastAlarm)
            {
                alarmList.add(lastAlarm);
            }
        }

        Set<KpiRelaAlarm> alarmPlanSet = new HashSet<KpiRelaAlarm>(alarmList);
        for (KpiGatherResult kpiGatherResult : list)
        {
            FCAlarmLineValueVO vo = new FCAlarmLineValueVO();
            Double lineValue = kpiGatherResult.getRatioValue();//环比
            TimePeriod timePeriod = kpiGatherResult.getTimePeriod();
            String categoryName = timePeriod.getYear() + "." + timePeriod.getMonth();
            vo.setValue(lineValue);
            vo.setTime(categoryName);
            values.add(vo);
        }
        //Map<String, Object> regionsMap = findSixAlarm(list, kpi.getKpiRelaAlarms());
        Map<String, Object> regionsMap = findSixAlarm(list,alarmPlanSet);
        String xml = FCInverseYAlarmLineChart.getXml(values, (FCAlarmRangeVO) regionsMap.get("range"),
                (Double) regionsMap.get("max"), (Double) regionsMap.get("min"), "", unit, caption, "1");
        
        jsobj.put("xml", xml);
        jsobj.put("year", year);
        return jsobj;
    }

    /**营业收入（净利润）增长率 ，计划完成率
     * @param id
     * @param columnType
     * @param lineType
     * @return
     */
    public String findYingyeShouruZhengzhanglvChart(String id)
    {
        String year = "";
        String frequecy = "0frequecy_month";//频率为月
        Kpi kpi = o_kpiBO.findKpiById(id);//指标对象
        String unit = "单位:" + kpi.getUnits().getName();
        List<FCAlarmLineValueVO> values = new ArrayList<FCAlarmLineValueVO>();
        if (null != kpi.getLastTimePeriod())
        {
            year = kpi.getLastTimePeriod().getYear();
        }
        else
        {
            year = DateUtils.getYear(new Date());
        }
        /*Map<String, Date> timeMap = this.o_kpiGatherResultBO.findTimeRange(year, frequecy, 1, 12);
        Date startTime = timeMap.get("startDate");
        Date endTime = timeMap.get("endDate");
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange(id, startTime, endTime);*/
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByKpiId(id);
        for (KpiGatherResult kpiGatherResult : list)
        {
            FCAlarmLineValueVO vo = new FCAlarmLineValueVO();
            Double lineValue = kpiGatherResult.getFinishValue();//实际值
            if(null==lineValue){
                continue;
            }
            TimePeriod timePeriod = kpiGatherResult.getTimePeriod();
            String categoryName = timePeriod.getYear() + "." + timePeriod.getMonth();
            vo.setValue(lineValue);
            vo.setTime(categoryName);
            values.add(vo);
        }
        String xml = "";
        FCAlarmRangeVO alarmRangeVo = null;
        Map<String, Object> regionsMap = null;
        if ("yingye_shouru_zhengzhanglv".equals(id) || "jinglirun_zhengzhanglv_mugongshi".equals(id))
        {
            regionsMap = findjingjizhengjiazhievaAlarm(list, kpi.getKpiRelaAlarms(), 5.0);
            alarmRangeVo = (FCAlarmRangeVO) regionsMap.get("range");
            alarmRangeVo.setRedStartValue(-60.0);
            alarmRangeVo.setGreenEndValue(60.0);
            //xml = FCInverseYAlarmLineChart.getXML(values, alarmRangeVo, 60.0, -60.0, "", unit, "", "1");
            xml = FCScrollAlarmLineChart.getXML(values, alarmRangeVo, 60.0, -60.0, "", unit, "", "1",true,false);
        }
        else
        {
            regionsMap = findSixXianjinBaozhangNengliAlarm(list, kpi.getKpiRelaAlarms(), 5.0);
            alarmRangeVo = (FCAlarmRangeVO) regionsMap.get("range");
            alarmRangeVo.setRedStartValue(20.0);
            alarmRangeVo.setGreenEndValue(180.0);
            //xml = FCInverseYAlarmLineChart.getXML(values, alarmRangeVo, 180.0, 20.0, "", unit, "", "1");
            xml =  FCScrollAlarmLineChart.getXML(values, alarmRangeVo, 180.0, 20.0, "", unit, "", "1",true,false);
        }
        return xml;
    }

    /**
     * 新增指标  综合收益计划完成率
     * @param id
     * @return
     */
    public String findZongheShouyiJihuaWanchenglv(String id)
    {

        String year = "";
        String frequecy = "0frequecy_quarter";//频率为季度
        Kpi kpi = o_kpiBO.findKpiById(id);//指标对象
        String unit = "单位:" + kpi.getUnits().getName();
        List<FCAlarmLineValueVO> values = new ArrayList<FCAlarmLineValueVO>();
        if (null != kpi.getLastTimePeriod())
        {
            year = kpi.getLastTimePeriod().getYear();
        }
        else
        {
            year = DateUtils.getYear(new Date());
        }
        /*Map<String, Date> timeMap = this.o_kpiGatherResultBO.findTimeRange(year, frequecy, 2, 4);
        Date startTime = timeMap.get("startDate");
        Date endTime = timeMap.get("endDate");
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange(id, startTime, endTime);*/
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByKpiId(id);
        for (KpiGatherResult kpiGatherResult : list)
        {
            FCAlarmLineValueVO vo = new FCAlarmLineValueVO();
            Double lineValue = null;
            if (null != kpiGatherResult.getFinishValue())
            {
                lineValue = kpiGatherResult.getFinishValue();
            }
            /*else{
                continue;
            }*/
            TimePeriod timeperiod = kpiGatherResult.getTimePeriod();
            String categoryName = timeperiod.getYear() + "." + timeperiod.getQuarter() + "季度";
            vo.setValue(lineValue);
            vo.setTime(categoryName);
            values.add(vo);
        }

        Map<String, Object> regionsMap = findjingjizhengjiazhievaAlarm(list, kpi.getKpiRelaAlarms(), 5.0);
        FCAlarmRangeVO alarmRangeVo = (FCAlarmRangeVO) regionsMap.get("range");
        alarmRangeVo.setRedStartValue(-300.0);
        alarmRangeVo.setGreenEndValue(50.0);
        //String xml = FCInverseYAlarmLineChart.getXML(values, alarmRangeVo, 50.0, -300.0, "", unit, "", "0");
        String xml = FCScrollAlarmLineChart.getXML(values, alarmRangeVo, 50.0, -300.0, "", unit, "", "0",true,false);

        return xml;
    }

    /**经济增加值（EVA）
    * @param id
    * @param columnType
    * @param lineType
    * @return
    */
    public String findjingjizhengjiazhievaAlarmLineChart(String id)
    {
        String year = "";
        String frequecy = "0frequecy_quarter";//频率为季度
        Kpi kpi = o_kpiBO.findKpiById(id);//指标对象
        String unit = "单位:" + kpi.getUnits().getName();
        List<FCAlarmLineValueVO> values = new ArrayList<FCAlarmLineValueVO>();
        if (null != kpi.getLastTimePeriod())
        {
            year = kpi.getLastTimePeriod().getYear();
        }
        else
        {
            year = DateUtils.getYear(new Date());
        }
       /* Map<String, Date> timeMap = this.o_kpiGatherResultBO.findTimeRange(year, frequecy, 2, 4);
        Date startTime = timeMap.get("startDate");
        Date endTime = timeMap.get("endDate");
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange(id, startTime, endTime);*/
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByKpiId(id);
        for (KpiGatherResult kpiGatherResult : list)
        {
            FCAlarmLineValueVO vo = new FCAlarmLineValueVO();
            Double lineValue = kpiGatherResult.getFinishValue();//实际值
            if(null==lineValue){
                continue;
            }
            TimePeriod timePeriod = kpiGatherResult.getTimePeriod();
            String categoryName = timePeriod.getYear() + "." + timePeriod.getQuarter() + "季度";
            vo.setValue(lineValue);
            vo.setTime(categoryName);
            values.add(vo);
        }
        Map<String, Object> regionsMap = findjingjizhengjiazhievaAlarm(list, kpi.getKpiRelaAlarms(), 5.0);
        FCAlarmRangeVO alarmRangeVo = (FCAlarmRangeVO) regionsMap.get("range");
        alarmRangeVo.setRedStartValue(-70.0);
        alarmRangeVo.setGreenEndValue(70.0);
        /*String xml = FCInverseYAlarmLineChart.getXML(values, alarmRangeVo, 70.0, -70.0, "", unit, "", "0");*/
        String xml = FCScrollAlarmLineChart.getXML(values, alarmRangeVo, 70.0, -70.0, "", unit, "", "0",true,false);
        
        return xml;
    }

    /**客户满意度（单柱图）
     * @param id 
     * @param columnType
     * @param lineType
     * @param frequecy
     * @param flag
     * @return
     */
    public String findBaoXianSingleColumnChartXml(String id, String columnType, String lineType)
    {
        String year = "";
        boolean flag = true;
        String caption = "客户满意度仅有2012年当年数据，无历史同口径数据。";
        String frequecy = "0frequecy_year";
        String showYAxisValues = "1";
        List<FCSingleColumnVO> datas = new ArrayList<FCSingleColumnVO>();
        Kpi kpi = o_kpiBO.findKpiById(id);
        String unit = "";
        if (null != kpi.getUnits())
        {
            unit = kpi.getUnits().getName();
        }
        if (null != kpi.getLastTimePeriod())
        {
            year = kpi.getLastTimePeriod().getYear();
        }
        else
        {
            year = DateUtils.getYear(new Date());
        }
        Map<String, Date> timeMap = this.o_kpiGatherResultBO.findTimeRange(year, frequecy, 1, 2);
        Date startTime = timeMap.get("startDate");
        Date endTime = timeMap.get("endDate");
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange(id, startTime, endTime);
        for (KpiGatherResult kpiGatherResult : list)
        {
            FCSingleColumnVO vo = new FCSingleColumnVO();
            Double columnValue = kpiGatherResult.getFinishValue();
            vo.setColumnValue(columnValue);
            TimePeriod timePeriod = kpiGatherResult.getTimePeriod();
            String categoryName = timePeriod.getYear() + "年";
            vo.setCategory(categoryName);
            datas.add(vo);
        }
        //3.获取xml
        String xml = FCSingleColumnLineChart.getXml(datas, "", "", flag, unit, showYAxisValues, caption);
        return xml;
    }

    /**客户满意度
     * @param id
     * @param columnType
     * @param lineType
     * @return
     */
    public String findKeHuManYiDuChart(String id, String columnType, String lineType)
    {
        String year = "";
        String xml = "";
        String caption = "客户满意度仅有2012年当年数据，无历史同口径数据。";
        String frequecy = "0frequecy_year";
        Kpi kpi = o_kpiBO.findKpiById(id);
        String unit = "";
        if (null != kpi.getUnits())
        {
            unit = kpi.getUnits().getName();
        }
        if (null != kpi.getLastTimePeriod())
        {
            year = kpi.getLastTimePeriod().getYear();
        }
        else
        {
            year = DateUtils.getYear(new Date());
        }
        Map<String, Date> timeMap = this.o_kpiGatherResultBO.findTimeRange(year, frequecy, 1, 2);
        Date startTime = timeMap.get("startDate");
        Date endTime = timeMap.get("endDate");
        List<KpiGatherResult> list = null;
        Set<String> yearList = new HashSet<String>();
        if ("fangdichan".equals(id))
        {
            Map<String, List<FCSingleColumnVO>> dataMap = new HashMap<String, List<FCSingleColumnVO>>();
            list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange("gouwuzhongxin", startTime, endTime);
            List<FCSingleColumnVO> data = new ArrayList<FCSingleColumnVO>();
            dataMap.put("购物中心", data);
            for (KpiGatherResult result : list)
            {
                FCSingleColumnVO vo = new FCSingleColumnVO();
                vo.setColumnValue(result.getFinishValue());
                vo.setCategory(result.getTimePeriod().getYear());
                yearList.add(result.getTimePeriod().getYear() + "年");
                data.add(vo);
            }
            data = new ArrayList<FCSingleColumnVO>();
            dataMap.put("酒店", data);
            list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange("jiudian", startTime, endTime);
            for (KpiGatherResult result : list)
            {
                FCSingleColumnVO vo = new FCSingleColumnVO();
                vo.setCategory(result.getTimePeriod().getYear());
                yearList.add(result.getTimePeriod().getYear() + "年");
                vo.setColumnValue(result.getFinishValue());
                data.add(vo);
            }
            data = new ArrayList<FCSingleColumnVO>();
            dataMap.put("写字楼", data);
            list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange("xiezhilou", startTime, endTime);
            for (KpiGatherResult result : list)
            {
                FCSingleColumnVO vo = new FCSingleColumnVO();
                vo.setCategory(result.getTimePeriod().getYear());
                yearList.add(result.getTimePeriod().getYear() + "年");
                vo.setColumnValue(result.getFinishValue());
                data.add(vo);
            }
            data = new ArrayList<FCSingleColumnVO>();
            dataMap.put("住宅", data);
            list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange("zhuzai", startTime, endTime);
            for (KpiGatherResult result : list)
            {
                FCSingleColumnVO vo = new FCSingleColumnVO();
                vo.setCategory(result.getTimePeriod().getYear());
                yearList.add(result.getTimePeriod().getYear() + "年");
                vo.setColumnValue(result.getFinishValue());
                data.add(vo);
            }
            List<String> ylist = new ArrayList<String>(yearList);
            Collections.sort(ylist);
            xml = FCMutiColumnLineChart.getXml(dataMap, ylist, "", caption);

        }
        else if ("wenhua".equals(id))
        {
            Map<String, List<FCSingleColumnVO>> dataMap = new HashMap<String, List<FCSingleColumnVO>>();
            list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange("shoudu_dianyingyuan", startTime, endTime);
            List<FCSingleColumnVO> data = new ArrayList<FCSingleColumnVO>();
            dataMap.put("首都电影院", data);
            for (KpiGatherResult result : list)
            {
                FCSingleColumnVO vo = new FCSingleColumnVO();
                vo.setCategory(result.getTimePeriod().getYear());
                yearList.add(result.getTimePeriod().getYear() + "年");
                vo.setColumnValue(result.getFinishValue());
                data.add(vo);
            }
            list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange("guanggao_gongsi", startTime, endTime);
            data = new ArrayList<FCSingleColumnVO>();
            dataMap.put("广告公司 ", data);
            for (KpiGatherResult result : list)
            {
                FCSingleColumnVO vo = new FCSingleColumnVO();
                vo.setCategory(result.getTimePeriod().getYear());
                yearList.add(result.getTimePeriod().getYear() + "年");
                vo.setColumnValue(result.getFinishValue());
                data.add(vo);
            }
            List<String> ylist = new ArrayList<String>(yearList);
            Collections.sort(ylist);
            xml = FCMutiColumnLineChart.getXml(dataMap, ylist, "", caption);
        }
        else if ("wuye".equals(id))
        {
            Map<String, List<FCSingleColumnVO>> dataMap = new HashMap<String, List<FCSingleColumnVO>>();
            list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange("wuyeshangye", startTime, endTime);
            List<FCSingleColumnVO> data = new ArrayList<FCSingleColumnVO>();
            dataMap.put("购物中心", data);
            for (KpiGatherResult result : list)
            {
                FCSingleColumnVO vo = new FCSingleColumnVO();
                vo.setCategory(result.getTimePeriod().getYear());
                yearList.add(result.getTimePeriod().getYear() + "年");
                vo.setColumnValue(result.getFinishValue());
                data.add(vo);
            }
            list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange("wuyezhuzhai", startTime, endTime);
            data = new ArrayList<FCSingleColumnVO>();
            dataMap.put("住宅", data);
            for (KpiGatherResult result : list)
            {
                FCSingleColumnVO vo = new FCSingleColumnVO();
                vo.setCategory(result.getTimePeriod().getYear());
                yearList.add(result.getTimePeriod().getYear() + "年");
                vo.setColumnValue(result.getFinishValue());
                data.add(vo);
            }
            list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange("wuyexiezilou", startTime, endTime);
            data = new ArrayList<FCSingleColumnVO>();
            dataMap.put("写字楼", data);
            for (KpiGatherResult result : list)
            {
                FCSingleColumnVO vo = new FCSingleColumnVO();
                vo.setCategory(result.getTimePeriod().getYear());
                yearList.add(result.getTimePeriod().getYear() + "年");
                vo.setColumnValue(result.getFinishValue());
                data.add(vo);
            }
            List<String> ylist = new ArrayList<String>(yearList);
            Collections.sort(ylist);
            xml = FCMutiColumnLineChart.getXml(dataMap, ylist, "", caption);
        }
        return xml;
    }

    /**外部行政处罚图形
     * @param id指标ID
     * @return
     * @throws ParseException 
     * @throws NumberFormatException 
     */
    public String findWaiBuXingZhengChuFaXYChart(String id) throws NumberFormatException, ParseException
    {
        Risk risk = null;
        String xml = "";
        String nodata = "";
        boolean flag = false;
        Map<Integer, List<FCXYVo>> dataMap = new TreeMap<Integer, List<FCXYVo>>();
        Kpi kpi = o_kpiBO.findKpiById(id);
        String year = DateUtils.getYear(new Date());
        Set<KpiRelaRisk> kpiRelaRisks = kpi.getKpiRelaRisks();//得到指标关联的风险
        for (KpiRelaRisk kpiRelaRisk : kpiRelaRisks)
        {
            risk = kpiRelaRisk.getRisk();
        }
        if (null != risk)
        {
            //得到指标关联的风险事件
            for (int i = 1; i <= 12; i++)
            {
                flag = getdWaiBuXingZhengChuFaData(dataMap, risk.getId(), year, String.valueOf(i));
                if (flag)
                {
                    nodata = "yes";
                }
            }
        }
        if ("yes".equals(nodata))
        {
            xml = FCWaiBuXingZhengChuFaXYChart.getXml(dataMap, 50, "", "万元");
        }
        else
        {
            xml = "noData";
        }
        return xml;
    }

    /**安全指数图形
     * @param id指标ID
     * @return
     * @throws ParseException 
     * @throws NumberFormatException 
     */
    public String findAnQuanZhiShuXYChart(String id) throws NumberFormatException, ParseException
    {
        Risk risk = null;
        String xml = "";
        String nodata = "";
        boolean flag = false;
        Map<Integer, List<FCXYVo>> dataMap = new TreeMap<Integer, List<FCXYVo>>();
        Kpi kpi = o_kpiBO.findKpiById(id);
        String year = DateUtils.getYear(new Date());
        Set<KpiRelaRisk> kpiRelaRisks = kpi.getKpiRelaRisks();//得到指标关联的风险
        for (KpiRelaRisk kpiRelaRisk : kpiRelaRisks)
        {
            risk = kpiRelaRisk.getRisk();
        }
        if (null != risk)
        {
            //得到指标关联的风险事件
            for (int i = 1; i <= 12; i++)
            {
                flag = getdWaiBuXingZhengChuFaData(dataMap, risk.getId(), year, String.valueOf(i));
                if (flag)
                {
                    nodata = "yes";
                }
            }
        }
        if ("yes".equals(nodata))
        {
            xml = FCAnQuanZhiShuXYChart.getXml(dataMap, 50, "", "");
        }
        else
        {
            xml = "noData";
        }
        return xml;
    }

    private boolean getdWaiBuXingZhengChuFaData(Map<Integer, List<FCXYVo>> dataMap, String riskId, String year,
            String month) throws NumberFormatException, ParseException
    {
        List<RiskEvent> list = o_riskEventRelatedService.findRiskHistoryEventByRiskId(riskId, Integer.valueOf(year),
                Integer.valueOf(month));
        boolean flag = false;
        if (null != list && list.size() > 0)
        {
            flag = true;
            List<FCXYVo> volist = new ArrayList<FCXYVo>();
            for (RiskEvent riskEvent : list)
            {
                FCXYVo vo = new FCXYVo();
                vo.setDesc(riskEvent.getFinanceLostDesc());
                vo.setValue(riskEvent.getFinanceLostAmount().doubleValue());
                volist.add(vo);
            }
            dataMap.put(Integer.valueOf(month), volist);
        }
        else
        {
            List<FCXYVo> volist = new ArrayList<FCXYVo>();
            FCXYVo vo = new FCXYVo();
            vo.setDesc("");
            vo.setValue(0.0);
            volist.add(vo);
            dataMap.put(Integer.valueOf(month), volist);
        }
        return flag;
    }

    /**集团内部处罚图形
     * @param id
     * @return
     */
    public String findJiTuanNeiBuChuFaXYChart(String id)
    {
        String year = "";
        String frequecy = "0frequecy_year";
        Kpi kpi = o_kpiBO.findKpiById(id);
        String unit = "";
        if (null != kpi.getUnits())
        {
            unit = kpi.getUnits().getName();
        }
        if (null != kpi.getLastTimePeriod())
        {
            year = kpi.getLastTimePeriod().getYear();
        }
        else
        {
            year = DateUtils.getYear(new Date());
        }
        List<FCXYVo> xyList = null;
        Map<String, List<FCXYVo>> dataMap = new TreeMap<String, List<FCXYVo>>();
        Map<String, Date> timeMap = this.o_kpiGatherResultBO.findTimeRange(year, frequecy, 9, 2);
        Date startTime = timeMap.get("startDate");
        Date endTime = timeMap.get("endDate");
        List<KpiGatherResult> numList = o_kpiGatherResultBO.findKpiGatherResultByTimeRange(
                "jituan_neibu_chufa_shuliang", startTime, endTime);//集团内部处罚数量
        //横向滚动条
        //List<KpiGatherResult> numList = o_kpiGatherResultBO.findKpiGatherResultByKpiId("jituan_neibu_chufa_shuliang");
        for (KpiGatherResult kpiGatherResult : numList)
        {
            //横向滚动条
            /*if(null==kpiGatherResult.getSameValue()){
            	continue;
            }*/
            String timeYear = kpiGatherResult.getTimePeriod().getYear() + "年";
            if (dataMap.containsKey(timeYear))
            {
                xyList = dataMap.get(timeYear);
                FCXYVo vo = new FCXYVo();
                vo.setValue(kpiGatherResult.getSameValue());
                xyList.add(vo);
            }
            else
            {
                xyList = new ArrayList<FCXYVo>();
                dataMap.put(timeYear, xyList);
                FCXYVo vo = new FCXYVo();
                vo.setValue(kpiGatherResult.getSameValue());
                xyList.add(vo);

            }
        }
        List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByTimeRange("jituan_neibu_chufa_jiner",
                startTime, endTime);// 集团内部处罚金额 
        //横向滚动条
        //List<KpiGatherResult> list = o_kpiGatherResultBO.findKpiGatherResultByKpiId("jituan_neibu_chufa_jiner");// 集团内部处罚金额 
        for (KpiGatherResult kpiGatherResult : list)
        {
            //横向滚动条
            /*if(null==kpiGatherResult.getSameValue()){
            	continue;
            }*/
            String timeYear = kpiGatherResult.getTimePeriod().getYear() + "年";
            if (dataMap.containsKey(timeYear))
            {
                xyList = dataMap.get(timeYear);
                FCXYVo vo = xyList.get(0);
                vo.setTwoValue(kpiGatherResult.getSameValue());
            }
        }
        String xml = FCMutiLineChart.getXml(dataMap, -200, 200, "单位:%");
        return xml;
    }

    /**查找指标采集结果的同比和环比(供金融街使用)
     * @param kpiGatherResult 指标采集结果对象
     * @return
     * @throws Exception
     */
    public KpiGatherResult findValueChr(KpiGatherResult kpiGatherResult)
    {
        //指标采集结果关联的指标对象
        Kpi kpi = kpiGatherResult.getKpi();
        String kpiId = kpi.getId();
        //实际值同比
        TimePeriod timePeriod = kpiGatherResult.getTimePeriod();
        TimePeriod preTimePeriod = timePeriod.getPrePeriod();//上期时间区间纬度
        TimePeriod sameTimePeriod = timePeriod.getPreYearPeriod();//去年同期时间纬度
        KpiGatherResult preKpiGatherResult = o_kpiGatherResultBO.findKpiGatherResultByIdAndTimeperiod(kpi.getId(),
                preTimePeriod);//环比
        KpiGatherResult sameKpiGatherResult = o_kpiGatherResultBO.findKpiGatherResultByIdAndTimeperiod(kpi.getId(),
                sameTimePeriod);//同比
        Double currentFinishValue = kpiGatherResult.getFinishValue();//当期实际值
        Double currentTargetValue = kpiGatherResult.getTargetValue();//当期目标值

        if (null != sameKpiGatherResult)
        {
            double[] valuesDouble = new double[2];
            Double samefinishValue = sameKpiGatherResult.getFinishValue();//上期实际值(同比)
            Double sametargetValue = sameKpiGatherResult.getTargetValue();//上期目标值(同比)
            if (null != currentFinishValue && null != samefinishValue)
            {
                valuesDouble[0] = currentFinishValue;
                valuesDouble[1] = samefinishValue;
                if ("china_fangdichan_qiye_paiming".equals(kpiId) || "quanguo_yingyuan_paiming".equals(kpiId)
                        || "ruiji".equals(kpiId) || "xilaideng".equals(kpiId) || "lishi".equals(kpiId)
                        || "weishiding".equals(kpiId) || "meijue".equals(kpiId) || "jinrongjie_gongyu".equals(kpiId))
                {
                    //金融街使用
                    //同比
                    kpiGatherResult.setSameValue(currentFinishValue - samefinishValue);
                }
                else
                {
                    try
                    {
                        double finishChrValue = StatisticFunctionCalculateBO.chr(valuesDouble);
                        //同比
                        kpiGatherResult.setSameValue(finishChrValue);

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }
            if (null != currentTargetValue && null != sametargetValue)
            {
                valuesDouble[0] = currentTargetValue;
                valuesDouble[1] = sametargetValue;
                if ("china_fangdichan_qiye_paiming".equals(kpiId) || "quanguo_yingyuan_paiming".equals(kpiId)
                        || "ruiji".equals(kpiId) || "xilaideng".equals(kpiId) || "lishi".equals(kpiId)
                        || "weishiding".equals(kpiId) || "meijue".equals(kpiId) || "jinrongjie_gongyu".equals(kpiId))
                {
                    //金融街使用
                    //同比
                    kpiGatherResult.setTargetSameValue(currentTargetValue - sametargetValue);
                }
                else
                {
                    try
                    {
                        double targetChrValue = StatisticFunctionCalculateBO.chr(valuesDouble);
                        //同比
                        kpiGatherResult.setTargetSameValue(targetChrValue);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        }

        if (null != preKpiGatherResult)
        {

            Double prefinishValue = preKpiGatherResult.getFinishValue();//上期实际值(环比)
            Double pretargetValue = preKpiGatherResult.getTargetValue();//上期目标值(环比)

            double[] valuesDouble = new double[2];
            if (null != currentFinishValue && prefinishValue != null)
            {
                valuesDouble[0] = currentFinishValue;
                valuesDouble[1] = prefinishValue;
                if ("china_fangdichan_qiye_paiming".equals(kpiId) || "quanguo_yingyuan_paiming".equals(kpiId)
                        || "ruiji".equals(kpiId) || "xilaideng".equals(kpiId) || "lishi".equals(kpiId)
                        || "weishiding".equals(kpiId) || "meijue".equals(kpiId) || "jinrongjie_gongyu".equals(kpiId))
                {
                    //金融街使用
                    //环比
                    kpiGatherResult.setRatioValue(currentFinishValue - prefinishValue);
                }
                else
                {
                    try
                    {
                        double finishChrValue = StatisticFunctionCalculateBO.chr(valuesDouble);
                        //环比
                        kpiGatherResult.setRatioValue(finishChrValue);

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }

            if (null != currentTargetValue && null != pretargetValue)
            {
                valuesDouble[0] = currentTargetValue;
                valuesDouble[1] = pretargetValue;
                if ("china_fangdichan_qiye_paiming".equals(kpiId) || "quanguo_yingyuan_paiming".equals(kpiId)
                        || "ruiji".equals(kpiId) || "xilaideng".equals(kpiId) || "lishi".equals(kpiId)
                        || "weishiding".equals(kpiId) || "meijue".equals(kpiId) || "jinrongjie_gongyu".equals(kpiId))
                {
                    //金融街使用
                    //环比
                    kpiGatherResult.setTargetRatioValue(currentTargetValue - pretargetValue);
                }
                else
                {
                    try
                    {
                        double targetChrValue = StatisticFunctionCalculateBO.chr(valuesDouble);
                        //环比
                        kpiGatherResult.setTargetRatioValue(targetChrValue);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }

        }
        return kpiGatherResult;
    }

    private Map<String, Object> findkehumanyidualarm(AlarmPlan alarmPlan)
    {
        Double maxValue = 0.0;
        Double chartMaxValue = 0.0;
        Double chartMinValue = 0.0;
        Double minValue = 0.0;
        //得到告警区间
        FCAlarmRangeVO range = new FCAlarmRangeVO();
        //得到告警方案
        Set<AlarmRegion> regions = alarmPlan.getAlarmRegions();
        for (AlarmRegion alarmRegion : regions)
        {
            
            DictEntry alarmLevel = alarmRegion.getAlarmIcon();
            if ("0alarm_startus_h".equals(alarmLevel.getId()))
            {
                //五颗星
                if (NumberUtils.isNumber(alarmRegion.getMaxValue()))
                {
                    maxValue = Double.valueOf(alarmRegion.getMaxValue());
                }
                if (NumberUtils.isNumber(alarmRegion.getMinValue()))
                {
                    minValue = Double.valueOf(alarmRegion.getMinValue());
                }
                else
                {
                    minValue = maxValue-2d;
                }
                
                chartMinValue = minValue;
                range.setRedStartValue(minValue);
                range.setRedEndValue(maxValue);
            }
            else if ("0alarm_startus_l".equals(alarmLevel.getId()))
            {
                //三颗星
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                range.setYellowStartValue(minValue);
                range.setYellowEndValue(maxValue);
            }
            else if ("0alarm_startus_m".equals(alarmLevel.getId()))
            {
                //四颗星
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                range.setOrangeStartValue(minValue);
                range.setOrangeEndValue(maxValue);
            }
            else if ("0alarm_startus_safe".equals(alarmLevel.getId()))
            {
                //绿色块
                if (NumberUtils.isNumber(alarmRegion.getMinValue()))
                {
                    minValue = Double.valueOf(alarmRegion.getMinValue());
                }
                if (NumberUtils.isNumber(alarmRegion.getMaxValue()))
                {
                    maxValue = Double.valueOf(alarmRegion.getMaxValue());
                }
                else
                {
                    maxValue = minValue+2d;
                }
                
                
                
                chartMaxValue = maxValue;
                range.setGreenStartValue(minValue);
                range.setGreenEndValue(maxValue);
            }
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("range", range);
        resultMap.put("max", chartMaxValue);
        resultMap.put("min", chartMinValue);
        return resultMap;
    }
    private Map<String, Object> findKeHuManYiDuAlarm(AlarmPlan alarmPlan)
    {
        Double maxValue = 0.0;
        Double chartMaxValue = 0.0;
        Double chartMinValue = 0.0;
        Double minValue = 0.0;
        //得到告警区间
        FCAlarmRangeVO range = new FCAlarmRangeVO();
        //得到告警方案
        Set<AlarmRegion> regions = alarmPlan.getAlarmRegions();
        for (AlarmRegion alarmRegion : regions)
        {

            DictEntry alarmLevel = alarmRegion.getAlarmIcon();
            if ("0alarm_startus_h".equals(alarmLevel.getId()))
            {
                //五颗星
                if (NumberUtils.isNumber(alarmRegion.getMaxValue()))
                {
                    maxValue = Double.valueOf(alarmRegion.getMaxValue());
                }
                if (NumberUtils.isNumber(alarmRegion.getMinValue()))
                {
                    minValue = Double.valueOf(alarmRegion.getMinValue());
                }
                else
                {
                    minValue = -3d;
                }

                chartMinValue = minValue;
                range.setRedStartValue(minValue);
                range.setRedEndValue(maxValue);
            }
            else if ("0alarm_startus_l".equals(alarmLevel.getId()))
            {
                //三颗星
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                range.setYellowStartValue(minValue);
                range.setYellowEndValue(maxValue);
            }
            else if ("0alarm_startus_m".equals(alarmLevel.getId()))
            {
                //四颗星
                maxValue = Double.valueOf(alarmRegion.getMaxValue());
                minValue = Double.valueOf(alarmRegion.getMinValue());
                range.setOrangeStartValue(maxValue);
                range.setOrangeEndValue(minValue);
            }
            else if ("0alarm_startus_safe".equals(alarmLevel.getId()))
            {
                //绿色块
                if (NumberUtils.isNumber(alarmRegion.getMaxValue()))
                {
                    maxValue = Double.valueOf(alarmRegion.getMaxValue());
                }
                else
                {
                    maxValue = 15d;
                }
                if (NumberUtils.isNumber(alarmRegion.getMinValue()))
                {
                    minValue = Double.valueOf(alarmRegion.getMinValue());
                }
                chartMaxValue = maxValue;
                range.setGreenStartValue(minValue);
                range.setGreenEndValue(maxValue);
            }
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("range", range);
        resultMap.put("max", chartMaxValue);
        resultMap.put("min", chartMinValue);
        return resultMap;
    }

    public AlarmPlan findAlarmPlanByNameType(String name, String type)
    {
        Criteria criteria = o_alarmPlanDAO.createCriteria();
        criteria.add(Restrictions.eq("name", name));
        if ("0alarm_type_kpi_alarm".equals(type) || "0alarm_type_kpi_forecast".equals(type))
        {
            criteria.add(Restrictions.eq("type.id", type));
        }
        List<AlarmPlan> list = criteria.list();
        if (null != list && list.size() > 0)
        {
            return list.get(0);
        }
        return null;
    }

}