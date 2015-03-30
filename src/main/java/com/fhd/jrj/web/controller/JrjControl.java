package com.fhd.jrj.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.comm.business.CategoryBO;
import com.fhd.comm.entity.Category;
import com.fhd.core.utils.DateUtils;
import com.fhd.core.utils.encode.JsonBinder;
import com.fhd.fdc.commons.exception.FHDException;
import com.fhd.jrj.business.JrjBO;
import com.fhd.kpi.business.KpiBO;
import com.fhd.kpi.entity.Kpi;

/**
 * jrj_指标Controller ClassName:jrjController
 * 
 * @author tonglin
 * @version
 * @since Ver 1.1
 * @Date 2013 2013-04-22
 * 
 * @see
 */

@Controller
public class JrjControl
{
    @Autowired
    private JrjBO o_jrjBO;

    @Autowired
    private KpiBO o_kpiBO;

    @Autowired
    private CategoryBO o_categoryBO;

    /**
     * 驾驶舱图
     * 
     * @param scid
     * @param response
     * @param request
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/jrj/createangulargaugechart.f")
    public void createAngulargaugeChart(String scid, HttpServletResponse response, HttpServletRequest request)
            throws IOException
    {
        JSONObject Jsobj = new JSONObject();
        JSONArray chartArray = o_jrjBO.createAngulargaugeChart(scid, request.getContextPath());
        Jsobj.put("chartArray", chartArray);
        if ("root".equals(scid))
        {
            Jsobj.put("level", "1");
        }
        else
        {
            Jsobj.put("level", "2");
        }
        if (!"root".equals(scid))
        {
            Category category = o_categoryBO.findCategoryById(scid);
            Jsobj.put("riskName", category.getName());
        }
        response.getWriter().write(JsonBinder.buildNonDefaultBinder().toJson(Jsobj));
    }

    /**
     * 自持酒店六个图
     * 
     * @param id
     * @param frequecy
     * @param response
     * @param request
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/jrj/createzichijiudianchart.f")
    public void createZichijiudianChart(String id, String frequecy,  String year, String next,HttpServletResponse response,
            HttpServletRequest request) throws IOException
    {
        String newyear = "";
        String oldyear = "";
        JSONArray jsarray = new JSONArray();
        Kpi kpi = o_kpiBO.findKpiById(id);
        Calendar calendar = Calendar.getInstance();
        if (StringUtils.isBlank(year))
        {
            if (null != kpi.getLastTimePeriod())
            {
                newyear = kpi.getLastTimePeriod().getYear();
            }
            else
            {
                newyear = DateUtils.getYear(new Date());
            }
        }
        else
        {
            newyear = year;
        }
        oldyear = newyear;
        int yearint = Integer.valueOf(newyear);
        calendar.set(Calendar.YEAR, yearint);
        if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(next))
        {
            if ("pre".equals(next))
            {
                calendar.set(Calendar.YEAR, Integer.valueOf(year) - 1);
            }
            else
            {
                calendar.set(Calendar.YEAR, Integer.valueOf(year) + 1);
            }
            newyear = String.valueOf(calendar.get(Calendar.YEAR));
        }
        if("2012".equals(newyear)){
            newyear="2013";
        }
        JSONObject chartJsObj = this.o_jrjBO.createZichijiudianChart(id, frequecy,newyear,oldyear);
        chartJsObj.put("kpiname", kpi.getName());
        response.getWriter().write(JsonBinder.buildNonDefaultBinder().toJson(chartJsObj));
    }

    /**
     * 22.（恒泰证券营业收入-竞争组别平均值）/竞争组别平均值,恒泰证券净资产收益率-竞争组别平均值）/竞争组别平均值
     * 
     * @param id
     * @param frequecy
     * @param year
     * @param response
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/jrj/hengtaizhengquanyingyeshouru.f")
    public void hengtaizhengquanyingyeshouru(String id, String frequecy, String year, String next,
            HttpServletResponse response) throws IOException
    {

        String newyear = "";
        String oldyear = "";
        JSONObject xmljson = new JSONObject();
        Kpi kpi = o_kpiBO.findKpiById(id);
        xmljson.put("kpiname", kpi.getName());
        Calendar calendar = Calendar.getInstance();
        if (StringUtils.isBlank(year))
        {
            if (null != kpi.getLastTimePeriod())
            {
                newyear = kpi.getLastTimePeriod().getYear();
            }
            else
            {
                newyear = DateUtils.getYear(new Date());
            }
        }
        else
        {
            newyear = year;
        }
        oldyear = newyear;
        int yearint = Integer.valueOf(newyear);
        calendar.set(Calendar.YEAR, yearint);
        if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(next))
        {
            if ("pre".equals(next))
            {
                calendar.set(Calendar.YEAR, Integer.valueOf(year) - 1);
            }
            else
            {
                calendar.set(Calendar.YEAR, Integer.valueOf(year) + 1);
            }
            newyear = String.valueOf(calendar.get(Calendar.YEAR));
        }
        
        JSONObject jsobj = o_jrjBO.findHengtaiZhengquanYingyeShouru(id, newyear, oldyear, kpi);
        xmljson.put("xml", jsobj.get("xml"));
        xmljson.put("year", jsobj.get("year"));
        response.getWriter().write(JsonBinder.buildNonDefaultBinder().toJson(xmljson));

    }
    
    /**
     * @param id
     * @param frequecy
     * @param response
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/jrj/singlecolumnchart.f")
    public void createSingleColumnChart(String id, String frequecy, HttpServletResponse response) throws IOException
    {
        String xml = "";
        String columnType = "finish";
        String lineType = "";
        JSONObject xmljson = new JSONObject();
        Kpi kpi = o_kpiBO.findKpiById(id);
        xmljson.put("kpiname", kpi.getName());
        if (StringUtils.isNotBlank(id))
        {
            // 客户满意度-房地产,物业,文化,保险
            if ("fangdichan".equals(id) || "wuye".equals(id) || "wenhua".equals(id) || "baoxian".equals(id))
            {
                xml = o_jrjBO.findKeHuManYiDusChart(id, columnType, lineType);
            }
        }
        xmljson.put("xml", xml);
        response.getWriter().write(JsonBinder.buildNonDefaultBinder().toJson(xmljson));
    }

    

    /*
     * 14.客户满意度房地产
     */
    @ResponseBody
    @RequestMapping("/jrj/kehumanyidu.f")
    public void kehumanyidu(String id, String frequecy, String year, String next,
            HttpServletResponse response) throws IOException{
        String newyear = "";
        String oldyear = "";
        JSONArray jsarray = new JSONArray();
        JSONObject xmljson = new JSONObject();
        Kpi kpi = o_kpiBO.findKpiById(id);
        xmljson.put("kpiname", kpi.getName());
        Calendar calendar = Calendar.getInstance();
        if (StringUtils.isBlank(year))
        {
            if (null != kpi.getLastTimePeriod())
            {
                newyear = kpi.getLastTimePeriod().getYear();
            }
            else
            {
                newyear = DateUtils.getYear(new Date());
            }
        }
        else
        {
            newyear = year;
        }
        oldyear = newyear;
        int yearint = Integer.valueOf(newyear);
        calendar.set(Calendar.YEAR, yearint);
        if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(next))
        {
            if ("pre".equals(next))
            {
                calendar.set(Calendar.YEAR, Integer.valueOf(year) - 1);
            }
            else
            {
                calendar.set(Calendar.YEAR, Integer.valueOf(year) + 1);
            }
            newyear = String.valueOf(calendar.get(Calendar.YEAR));
        }
        if("2012".equals(newyear)){//2012年没有数据,显示13年数据
            newyear = "2013";
        }
        if("fangdichan".equals(id)){
            JSONObject jsobj = o_jrjBO.findkehumanyiduchart("gouwuzhongxin",newyear,oldyear);
            jsarray.add(jsobj.get("xml"));
            xmljson.put("year", jsobj.get("year"));
            jsobj = o_jrjBO.findkehumanyiduchart("jiudian",newyear,oldyear);
            jsarray.add(jsobj.get("xml"));
            jsobj = o_jrjBO.findkehumanyiduchart("xiezhilou",newyear,oldyear);
            jsarray.add(jsobj.get("xml"));
            jsobj = o_jrjBO.findkehumanyiduchart("zhuzai",newyear,oldyear);
            jsarray.add(jsobj.get("xml"));
            xmljson.put("xmls", jsarray);
        }else if("baoxian".equals(id)){
            JSONObject jsobj = o_jrjBO.findkehumanyiduchart("baoxian",newyear,oldyear);
            jsarray.add(jsobj.get("xml"));
            xmljson.put("year", jsobj.get("year"));
            xmljson.put("xmls", jsarray);
        }else if("wuye".equals(id)){
            JSONObject jsobj = o_jrjBO.findkehumanyiduchart("wuyeshangye",newyear,oldyear);
            jsarray.add(jsobj.get("xml"));
            xmljson.put("year", jsobj.get("year"));
            jsobj = o_jrjBO.findkehumanyiduchart("wuyezhuzhai",newyear,oldyear);
            jsarray.add(jsobj.get("xml"));
            jsobj = o_jrjBO.findkehumanyiduchart("wuyexiezilou",newyear,oldyear);
            jsarray.add(jsobj.get("xml"));
            xmljson.put("xmls", jsarray);
        }else if("wenhua".equals(id)){
            JSONObject jsobj = o_jrjBO.findkehumanyiduchart("shoudu_dianyingyuan",newyear,oldyear);
            jsarray.add(jsobj.get("xml"));
            xmljson.put("year", jsobj.get("year"));
            jsobj = o_jrjBO.findkehumanyiduchart("guanggao_gongsi",newyear,oldyear);
            jsarray.add(jsobj.get("xml"));
            xmljson.put("xmls", jsarray);
        }
        response.getWriter().write(JsonBinder.buildNonDefaultBinder().toJson(xmljson));
        
    }
    /**
     * 23.全国影院票房排名
     * 
     * @param id
     * @param frequecy
     * @param year
     * @param next
     * @param response
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/jrj/quanguoyingyuanpaiming.f")
    public void quanguoyingyuanpaiming(String id, String frequecy, String year, String next,
            HttpServletResponse response) throws IOException
    {
        String newyear = "";
        String oldyear = "";
        JSONObject xmljson = new JSONObject();
        Kpi kpi = o_kpiBO.findKpiById(id);
        xmljson.put("kpiname", kpi.getName());
        Calendar calendar = Calendar.getInstance();
        if (StringUtils.isBlank(year))
        {
            if (null != kpi.getLastTimePeriod())
            {
                newyear = kpi.getLastTimePeriod().getYear();
            }
            else
            {
                newyear = DateUtils.getYear(new Date());
            }
        }
        else
        {
            newyear = year;
        }
        oldyear = newyear;
        int yearint = Integer.valueOf(newyear);
        calendar.set(Calendar.YEAR, yearint);
        if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(next))
        {
            if ("pre".equals(next))
            {
                calendar.set(Calendar.YEAR, Integer.valueOf(year) - 1);
            }
            else
            {
                calendar.set(Calendar.YEAR, Integer.valueOf(year) + 1);
            }
            newyear = String.valueOf(calendar.get(Calendar.YEAR));
        }
        JSONObject jsobj = o_jrjBO.quanguoyingyuanpaiming(id, newyear, oldyear, kpi);
        xmljson.put("xml", jsobj.get("xml"));
        xmljson.put("year", jsobj.get("year"));
        response.getWriter().write(JsonBinder.buildNonDefaultBinder().toJson(xmljson));

    }

    /**
     * 告警图
     * 
     * @param id
     * @param frequecy
     * @param response
     * @throws IOException
     * @throws ParseException
     * @throws NumberFormatException
     */
    @ResponseBody
    @RequestMapping("/jrj/alarmlinechart.f")
    public void createAlarmLineChart(String id, String frequecy, HttpServletResponse response) throws IOException,
            NumberFormatException, ParseException
    {
        String xml = "";
        JSONObject xmljson = new JSONObject();
        Kpi kpi = o_kpiBO.findKpiById(id);
        xmljson.put("kpiname", kpi.getName());
        if ("china_fangdichan_qiye_paiming".equals(id))
        {
            // 中国房地产百强企业排名
            xml = o_jrjBO.findChinaFangDiChanQiyePaimingAlarmLineChartXml(id);
        }
        else if ("biaozhun_baofei".equals(id))
        {
            // 标准保费
            xml = o_jrjBO.findBiaoZhunBaoFeiAlarmLineChartXml(id);
        }

        // 新加指标 综合收益计划完成率
        else if ("zonghe_shouyi_jihua_wanchenglv".equals(id))
        {
            xml = o_jrjBO.findZongheShouyiJihuaWanchenglv(id);
        }
        else if ("qiye_neihan_jiazhi".equals(id))
        {
            // 企业内涵价值
            xml = o_jrjBO.findQiyeNeihanJiaZhiAlarmLineChartXml(id);
        }
        else if ("hengtai_zhengquan_yingye_shouru".equals(id))
        {
            // 恒泰证券公司-同组别竞争企业营业收入比较
            xml = o_jrjBO.findHengtaiZhengquanYingyeShouru(id);
        }
        else if ("hengtai_zhengquan_zhichan_shouyi".equals(id))
        {
            // 恒泰证券公司-同组别竞争企业净资产收益率比较
            xml = o_jrjBO.findHengtaiZhengquanZhichanShouyi(id);
        }
        else if ("quanguo_yingyuan_paiming".equals(id))
        {
            // 首都电影院西单影院
            xml = o_jrjBO.findQuanguoYingyuanPaiming(id);
        }
        else if ("3_xianjin_baozhang_nengli".equals(id))
        {
            // 现金/3个月内刚性支出
            xml = o_jrjBO.findThreeXianjinBaozhangNengli(id);
        }
        else if ("6_xianjin_baozhang_nengli".equals(id))
        {
            // 6个月现金保障能力
            xml = o_jrjBO.findSixXianjinBaozhangNengli(id);
        }
        else if ("zhichan_fuzhailv".equals(id))
        {
            // 资产负债率
            xml = o_jrjBO.findZhichanFuzhailv(id);
        }
        else if ("youxi_fuzhailv".equals(id))
        {
            // 有息负债率
            xml = o_jrjBO.findYouxiFuzhailv(id);
        }
        else if ("jingying_huodong_xianjin_liuliang_jinger".equals(id))
        {
            // 经营活动产生的现金流量净额
            xml = o_jrjBO.findJingyingHuodongXianjinLiuliangJinger(id);
        }
        else if ("zongzhichan_zhouzhuanlv".equals(id))
        {
            // 总资产周转率
            xml = o_jrjBO.findZongzhichanZhouzhuanlv(id);
        }
        else if ("chengben_feiyong_lirenlv".equals(id))
        {
            // 成本费用利润率
            xml = o_jrjBO.findChengbenFeiyongLirenlv(id);
        }
        else if ("maolilv".equals(id))
        {
            // 毛利率
            xml = o_jrjBO.findMaolilv(id);
        }
        else if ("renli_chengben_lirenlv".equals(id))
        {
            // 人力成本利润率
            xml = o_jrjBO.findRenliChengbenLirenlv(id);
        }
        else if ("guanjian_wangwei_kongquelv".equals(id))
        {
            // 关键岗位空岗率
            xml = o_jrjBO.findGuanjianWangweiKongquelv(id);
        }
        else if ("jituan_neibu_chufa".equals(id))
        {
            xml = o_jrjBO.findJiTuanNeiBuChuFaXYChart(id);
        }
        else if ("waibu_xingzheng_chufa".equals(id))
        {
            xml = o_jrjBO.findWaiBuXingZhengChuFaXYChart(id);
        }
        else if ("anquan_zhishu".equals(id))
        {
            xml = o_jrjBO.findAnQuanZhiShuXYChart(id);
        }
        else if ("jingji_zhengjiazhi_eva".equals(id))
        {
            // 经济增加值（EVA）
            xml = o_jrjBO.findjingjizhengjiazhievaAlarmLineChart(id);
        }
        else if ("jingji_zhengjiazhi_eva_shichang".equals(id))
        {
            // 经济增加值（EVA）—市场
            xml = o_jrjBO.findjingjizhengjiazhievaAlarmLineChart(id);
        }
        else if ("jingji_zhengjiazhi_eva_zhengfu".equals(id))
        {
            // 经济增加值（EVA）—政府
            xml = o_jrjBO.findjingjizhengjiazhievaAlarmLineChart(id);
        }

        // 营业收入增长率
        else if ("yingye_shouru_zhengzhanglv".equals(id))
        {
            xml = o_jrjBO.findYingyeShouruZhengzhanglvChart(id);
        }
        // 营业收入计划完成率
        else if ("yingye_shouru_jihua_wangchenglv".equals(id))
        {
            xml = o_jrjBO.findYingyeShouruZhengzhanglvChart(id);
        }
        // 净利润(归属于母公司)增长率
        else if ("jinglirun_zhengzhanglv_mugongshi".equals(id))
        {
            xml = o_jrjBO.findYingyeShouruZhengzhanglvChart(id);
        }
        // 净利润(归属于母公司)计划完成率
        else if ("jinglirun_jihua_wanchenglv_mugongshi".equals(id))
        {
            xml = o_jrjBO.findYingyeShouruZhengzhanglvChart(id);
        }
        xmljson.put("xml", xml);
        response.getWriter().write(JsonBinder.buildNonDefaultBinder().toJson(xmljson));
    }

    
    /**
     * 查询指标状态
     * 
     * @param scid
     * @param response
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/jrj/findallkpistatus.f")
    public void findAllKpiStatus(String scid, HttpServletResponse response) throws IOException
    {
        JSONObject allKpiStatus = o_jrjBO.findAllKpiStatus();
        response.getWriter().write(JsonBinder.buildNonDefaultBinder().toJson(allKpiStatus));
    }

    /**
     * 状态为红色指标
     * 
     * @param scid
     * @param response
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/jrj/findredstatus.f")
    public void findRedStatus(String scid, HttpServletResponse response) throws IOException
    {
        JSONObject redStatus = o_jrjBO.findRedStatus();
        JSONObject allKpiStatus = o_jrjBO.findKpiStatusCount();
        redStatus.put("redcounts", allKpiStatus.get("redcount"));
        redStatus.put("orangecounts", allKpiStatus.get("orangecount"));
        redStatus.put("yellowcounts", allKpiStatus.get("yellowcount"));
        redStatus.put("greencounts", allKpiStatus.get("greencount"));
        response.getWriter().write(JsonBinder.buildNonDefaultBinder().toJson(redStatus));
    }

    @RequestMapping("/jrj/jrjindex.do")
    public void index(Model model, HttpServletRequest request) throws FHDException
    {
    }

    @RequestMapping("/jrj/zcjd.do")
    public void zcjd(Model model, HttpServletRequest request) throws FHDException
    {
    }

    @RequestMapping("/jrj/onediffregion.do")
    public void onediffregion(Model model, HttpServletRequest request) throws FHDException
    {

    }
    
    @RequestMapping("/jrj/fangdichan.do")
    public void fangdichan(Model model, HttpServletRequest request) throws FHDException
    {
        
    }

    @RequestMapping("/jrj/angulargauge.do")
    public void angulargaugeIndex(Model model, HttpServletRequest request) throws FHDException
    {
    }

    @RequestMapping("/jrj/kpilist.do")
    public void kpilistIndex(Model model, HttpServletRequest request) throws FHDException
    {
    }

    @RequestMapping("/jrj/riskanalysis.do")
    public void riskAnalysis(Model model, HttpServletRequest request) throws FHDException
    {
    }

}
