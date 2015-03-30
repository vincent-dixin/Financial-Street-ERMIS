package com.fhd.kpi.web.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.comm.entity.AlarmPlan;
import com.fhd.comm.entity.AlarmRegion;
import com.fhd.comm.web.form.AlarmRegionForm;
import com.fhd.core.dao.Page;
import com.fhd.core.utils.encode.JsonBinder;
import com.fhd.fdc.utils.Contents;
import com.fhd.kpi.business.RelaAssessResultBO;
import com.fhd.kpi.business.StrategyMapBO;
import com.fhd.kpi.business.StrategyMapTreeBO;
import com.fhd.kpi.entity.Kpi;
import com.fhd.kpi.entity.KpiRelaOrgEmp;
import com.fhd.kpi.entity.SmRelaAlarm;
import com.fhd.kpi.entity.SmRelaDim;
import com.fhd.kpi.entity.SmRelaKpi;
import com.fhd.kpi.entity.SmRelaTheme;
import com.fhd.kpi.entity.StrategyMap;
import com.fhd.kpi.web.form.KpiForm;
import com.fhd.kpi.web.form.SmRelaAlarmForm;
import com.fhd.kpi.web.form.SmRelaKpiForm;
import com.fhd.kpi.web.form.StrategyMapForm;
import com.fhd.sys.business.dic.DictBO;
import com.fhd.sys.entity.dic.DictEntry;

/**
 * KPI_战略目标Controller ClassName:KpiStrategyMapController
 * 
 * @author 杨鹏
 * @version
 * @since Ver 1.1
 * @Date 2012 2012-8-15 下午3:11:34
 * 
 * @see
 */
@Controller
public class StrategyMapControl {
    @Autowired
    private StrategyMapBO o_kpiStrategyMapBO;

    @Autowired
    private StrategyMapTreeBO o_kpiStrategyMapTreeBO;

    @Autowired
    private StrategyMapBO o_strategyMapBO;

    @Autowired
    private DictBO o_dictEntryBO;

    @Autowired
    private RelaAssessResultBO o_relaAssessResultBO;

    /**
     * 目标树 treeLoader:
     * 
     * @author 张帅
     * @param id
     *            节点id
     * @param canChecked
     *            是否有复选框
     * @param query
     *            查询条件
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/KpiStrategyMapTree/treeloader")
    public List<Map<String, Object>> treeLoader(String node, Boolean canChecked, String query, String smIconType) {
        return o_kpiStrategyMapTreeBO.treeLoader(node, canChecked, query, null, smIconType);
    }

    /**
     * 我的目标树 treeLoader:
     * 
     * @author 张帅
     * @param id
     *            节点id
     * @param canChecked
     *            是否有复选框
     * @param query
     *            查询条件
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/KpiStrategyMapTree/minetreeloader")
    public List<Map<String, Object>> mineTreeLoader(String node, Boolean canChecked, String query, String smIconType) {
        return o_kpiStrategyMapTreeBO.mineTreeLoader(node, canChecked, query, smIconType);
    }

    /**
     * <pre>
     * 根据战略目标ID查询图表类型
     * </pre>
     * @param response 响应对象
     * @param id 记分卡ID
     * @throws IOException
     */
    @RequestMapping("/kpi/strategyMap/findStrategyMapById.f")
    public void findStrategyMapById(HttpServletResponse response, String id) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        StrategyMap strategyMap = o_strategyMapBO.findById(id);
        String chartType = "";
        if (null != strategyMap && StringUtils.isNotBlank(strategyMap.getChartType())) {
            String[] chartIdArray = strategyMap.getChartType().split(",");
            Arrays.sort(chartIdArray);
            int i = 0;
            for (String chartId : chartIdArray) {
                chartType += chartId;
                if (i != chartIdArray.length - 1) {
                    chartType += ",";
                }
                i++;
            }
        }
        result.put("chartType", chartType);
        result.put("success", true);
        response.getWriter().write(JsonBinder.buildNonDefaultBinder().toJson(result));
    }

    /**
     * 机构目标树 treeLoader:
     * 
     * @author 张帅
     * @param id
     *            节点id
     * @param canChecked
     *            是否有复选框
     * @param query
     *            查询条件
     * @param smIcon
     *            'manager'表示管理,'display':表示显示
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/KpiStrategyMapTree/orgtreeloader")
    public List<Map<String, Object>> orgTreeLoader(String node, String type, Boolean canChecked, String query, String smIconType) {
        return o_kpiStrategyMapTreeBO.orgTreeLoader(node, type, canChecked, query, smIconType);
    }

    /**
     * 根据id查询目标
     * 
     * @author 张帅
     * @param id
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/KpiStrategyMap/findSmById")
    public List<Map<String, Object>> findSmById(String[] ids) {
        return o_kpiStrategyMapBO.findStrategyById(ids);
    }

    /**
     * <pre>
     * 目标查看 - 编辑菜单
     * </pre>
     * 
     * @author 陈晓哲
     * @param id
     *            目标ID
     * @return 前台接收的json
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpistrategymap/findsmbyidtojson.f")
    public Map<String, Object> findSmByIdToJson(String id) {
        DictEntry entry = null;
        StringBuffer smDim = new StringBuffer();
        StringBuffer smTheme = new StringBuffer();
        JSONArray otherdimArr = new JSONArray();
        JSONArray otherThemeArr = new JSONArray();
        Map<String, Object> mutiJson = new HashMap<String, Object>();
        if (StringUtils.contains(id, "_")) {
            id = id.split("_")[1];// 机构目标树,传递过来的id为orgid_smid
        }
        StrategyMap sm = o_kpiStrategyMapBO.findById(id);
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != sm) {
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("code", sm.getCode());
            jsonMap.put("name", sm.getName());
            jsonMap.put("shortName", sm.getShortName());
            jsonMap.put("warningFormula", sm.getWarningFormula());
            jsonMap.put("assessmentFormula", sm.getAssessmentFormula());
            jsonMap.put("desc", sm.getDesc());
            if (null != sm.getStatus())
                jsonMap.put("estatus", sm.getStatus().getId());
            jsonMap.put("currentSmId", sm.getId());
            jsonMap.put("id", sm.getId());
            Set<SmRelaDim> smRelaDimSet = sm.getSmRelaDims();
            for (SmRelaDim smRelaDim : smRelaDimSet) {
                entry = smRelaDim.getSmDim();
                if (null != entry) {
                    if ("M".equals(smRelaDim.getType())) {
                        smDim.append(entry.getId()).append(",");
                    }
                    else {
                        otherdimArr.add(entry.getId());
                    }
                }
            }
            if (smDim.length() > 0) {
                jsonMap.put("mainDim", smDim.toString().substring(0, smDim.length() - 1));
            }
            mutiJson.put("otherDim", otherdimArr.toString());
            Set<SmRelaTheme> smRelaThemeSet = sm.getSmRelaThemes();
            for (SmRelaTheme smRelaTheme : smRelaThemeSet) {
                entry = smRelaTheme.getTheme();
                if (null != entry) {
                    if ("M".equals(smRelaTheme.getType())) {
                        smTheme.append(entry.getId()).append(",");
                    }
                    else {
                        otherThemeArr.add(entry.getId());
                    }
                }
            }
            if (smTheme.length() > 0) {
                jsonMap.put("mainTheme", smTheme.toString().substring(0, smTheme.length() - 1));
            }
            mutiJson.put("otherTheme", otherThemeArr.toString());
            if (null != sm.getParent()) {
                jsonMap.put("parentId", sm.getParent().getId());
            }

            if (null != sm.getChartType()) {
                jsonMap.put("chartTypeStr", sm.getChartType());
                jsonMap.put("charttypehidden", sm.getChartType());
            }

            /* 部门和人员信息 */
            JSONObject orgEmpObj = this.o_kpiStrategyMapBO.findSmRelaOrgEmpBySmToJson(sm);
            jsonMap.put("ownDept", orgEmpObj.getString("ownDept"));
            jsonMap.put("reportDept", orgEmpObj.getString("reportDept"));
            jsonMap.put("viewDept", orgEmpObj.getString("viewDept"));
            map.put("data", jsonMap);
            map.put("multiSelect", mutiJson);
            map.put("success", true);
        }

        return map;
    }

    /**
     * <pre>
     * 查询目标和kpi指标关联信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param start
     *            起始位置
     * @param limit
     *            返回记录数
     * @param query查询参数
     * @param currentSmId
     *            当前目标ID
     * @return 前台接收的json
     * @throws Exception
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpistrategymap/findsmrelakpibysome.f")
    public Map<String, Object> findSmRelaKpiBySome(int start, int limit, String query, String currentSmId, String sort) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        List<SmRelaKpiForm> datas = new ArrayList<SmRelaKpiForm>();
        if (StringUtils.isNotBlank(currentSmId) && !"undefined".equals(currentSmId)) {
            String dir = "ASC";
            String sortColumn = "id";
            String orgname = "";
            StringBuffer orgBuf = null;
            SmRelaKpiForm form = null;
            if (StringUtils.isNotBlank(sort)) {
                JSONArray jsonArray = JSONArray.fromObject(sort);
                if (jsonArray.size() > 0) {
                    JSONObject jsobj = jsonArray.getJSONObject(0);
                    sortColumn = jsobj.getString("property");
                    dir = jsobj.getString("direction");
                }
            }
            List<SmRelaKpi> entityList = o_kpiStrategyMapBO.findSmRelaKpiBySome(query, currentSmId, sortColumn, dir);
            for (SmRelaKpi de : entityList) {
                orgBuf = new StringBuffer();
                Set<KpiRelaOrgEmp> orgSet = de.getKpi().getKpiRelaOrgEmps();
                for (KpiRelaOrgEmp kpiRelaOrgEmp : orgSet) {
                    if (Contents.BELONGDEPARTMENT.equals(kpiRelaOrgEmp.getType())) {
                        orgBuf.append(kpiRelaOrgEmp.getOrg().getOrgname()).append(",");
                    }
                }
                if (orgBuf.length() > 0)
                    orgname = orgBuf.toString().substring(0, orgBuf.length() - 1);
                form = new SmRelaKpiForm(de);
                form.setDept(orgname);
                datas.add(form);
            }
        }
        map.put("datas", datas);
        return map;
    }

    /**
     * <pre>
     * 查询目标和预警关联信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param start
     * @param limit
     * @param query
     * @param currentSmId
     * @return
     * @throws Exception
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpistrategymap/findsmrelaalarmbysome.f")
    public Map<String, Object> findSmRelaAlarmBySome(int start, int limit, String query, String currentSmId, String editflag, String sort)
            throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        List<SmRelaAlarmForm> datas = new ArrayList<SmRelaAlarmForm>();
        if (StringUtils.isNotBlank(currentSmId)) {
            String dir = "ASC";
            String sortColumn = "id";
            if (StringUtils.isNotBlank(sort)) {
                JSONArray jsonArray = JSONArray.fromObject(sort);
                if (jsonArray.size() > 0) {
                    JSONObject jsobj = jsonArray.getJSONObject(0);
                    sortColumn = jsobj.getString("property");
                    dir = jsobj.getString("direction");
                }
            }
            List<SmRelaAlarm> entityList = o_kpiStrategyMapBO.findSmRelaAlarmBySome(query, currentSmId, editflag, sortColumn, dir);

            for (SmRelaAlarm de : entityList) {
                datas.add(new SmRelaAlarmForm(de));
            }
        }

        map.put("datas", datas);
        return map;
    }

    /**
     * <pre>
     * 查询告警信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param type
     *            告警或预警类型
     * @return 告警或预警列表
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpistrategymap/findwarningbytype.f")
    public Map<String, Object> findWarningByType(String type) {
        Map<String, String> map = null;
        List<Map<String, String>> l = new ArrayList<Map<String, String>>();
        Map<String, Object> result = new HashMap<String, Object>();
        List<AlarmPlan> list = o_kpiStrategyMapBO.findAlarmPlanByType(type);
        for (AlarmPlan plan : list) {
            map = new HashMap<String, String>();
            map.put("name", plan.getName());
            map.put("id", plan.getId());
            l.add(map);
        }
        result.put("warninglist", l);
        return result;
    }

    /**
     * <pre>
     * 查找目标父节点id
     * </pre>
     * 
     * @author 陈晓哲
     * @param id
     *            目标ID
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpistrategymap/findparentbyid.f")
    public Map<String, Object> findParentByid(String id) {
        String parentid = "";
        String parentname = "";
        Map<String, Object> result = new HashMap<String, Object>();
        if (StringUtils.contains(id, "_")) {
            id = id.split("_")[1];// 机构目标树,传递过来的id为orgid_smid
        }
        StrategyMap sm = o_kpiStrategyMapBO.findById(id);
        if (null != sm) {
            StrategyMap psm = sm.getParent();
            if (null != psm) {
                parentid = psm.getId();
                parentname = psm.getName();
            }
            else {
                parentid = "sm_root";
                parentname = "目标库";
            }
            result.put("chartType", sm.getChartType());
        }
        result.put("parentid", parentid);
        result.put("parentname", parentname);
        return result;
    }

    /**
     * <pre>
     * 根据父节点Id生成目标编码
     * </pre>
     * 
     * @author 陈晓哲
     * @param parentid
     *            父目标ID
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpistrategymap/findcodebyparentid.f")
    public Map<String, Object> findCodeByParentId(String param) {
        String code = "";
        Map<String, Object> result = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(param)) {
            JSONObject jsobj = JSONObject.fromObject(param);
            String parentid = jsobj.getString("parentid");
            String smId = jsobj.getString("currentSmId");
            code = o_kpiStrategyMapBO.findCodeBySmParentId(parentid, smId);
        }
        result.put("code", code);
        result.put("success", true);
        return result;
    }

    /**
     * <pre>
     * 根据kpiid查询所属部门
     * </pre>
     * 
     * @author 陈晓哲
     * @param kpiID
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpistrategymap/findkpirelaorgempbyid.f")
    public Map<String, Object> findKpiRelaOrgEmpById(String kpiID) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        JSONObject jsonobj = o_kpiStrategyMapBO.findKpiRelaOrgEmpById(kpiID);
        resultMap.put("success", true);
        resultMap.put("kpiRelOrg", jsonobj.toString());
        return resultMap;
    }

    /**
     * <pre>
     * 查询目标关联的指标信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param start
     *            分页起始位置
     * @param limit
     *            显示记录数
     * @param query
     *            查询条件
     * @param id
     *            指标ID
     * @param sort
     *            排序字段
     * @param year
     *            年
     * @param quarter
     *            季度
     * @param month
     *            月
     * @param week
     *            周
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpistrategymap/findsmrelakpiresult.f")
    public Map<String, Object> findSmRelaKpiResult(int start, int limit, String query, String sort, String id, String year, String quarter,
            String month, String week, String eType, String isNewValue) {
        id = ",".equals(id) ? "" : id;
        year = ",".equals(year) ? "" : year;
        quarter = ",".equals(quarter) ? "" : quarter;
        month = ",".equals(month) ? "" : month;
        week = ",".equals(week) ? "" : week;
        eType = ",".equals(eType) ? "" : eType;
        isNewValue = ",".equals(isNewValue) ? "" : isNewValue;
        if (null != year && year.contains(",")) {
            year = year.replace(",", "");
        }
        if (null != quarter && quarter.contains(",")) {
            quarter = quarter.replace(",", "");
        }
        if (null != month && month.contains(",")) {
            month = month.replace(",", "");
        }
        if (null != week && week.contains(",")) {
            week = week.replace(",", "");
        }
        if (null != id && id.contains(",")) {
            id = id.replace(",", "");
        }
        if (null != eType && eType.contains(",")) {
            eType = eType.replace(",", "");
        }
        if (null != isNewValue && isNewValue.contains(",")) {
            isNewValue = isNewValue.replace(",", "");
        }
        String lastflag = isNewValue;
        String frequence = eType;
        String dir = "DESC";
        String sortColumn = "assessmentStatus";// 默认排序
        KpiForm form = null;
        Map<String, Object> map = new HashMap<String, Object>();
        List<KpiForm> datas = new ArrayList<KpiForm>();
        Page<Kpi> page = new Page<Kpi>();
        page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
        page.setPageSize(limit);
        List<Object[]> gatherDatas = null;
        if (StringUtils.isNotBlank(sort)) {
            JSONArray jsonArray = JSONArray.fromObject(sort);
            if (jsonArray.size() > 0) {
                JSONObject jsobj = jsonArray.getJSONObject(0);
                sortColumn = jsobj.getString("property");// 按照哪个字段排序
                dir = jsobj.getString("direction");// 排序方向
            }
        }
        if ("true".equals(lastflag)) {// 最新值查询
            gatherDatas = o_kpiStrategyMapBO.findLastSmRelaKpiResults(map, start, limit, query, sortColumn, dir, id);
        }
        else {// 具体频率查询
            gatherDatas = o_kpiStrategyMapBO.findSpecificSmRelaKpiResults(map, start, limit, query, sortColumn, dir, id, year, quarter, month, week,
                    frequence);
        }
        if (null != gatherDatas) {
            for (Object[] objects : gatherDatas) {
                form = new KpiForm(objects, "sm");
                datas.add(form);
            }
        }
        map.put("datas", datas);
        return map;
    }

    /**
     * <pre>
     * 保存目标信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param form
     *            目标Form
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpistrategymap/mergestrategymap.f")
    public Map<String, Object> mergeStrategyMap(StrategyMapForm form, HttpServletRequest request) throws UnsupportedEncodingException {
        JSONObject param = JSONObject.fromObject(request.getParameter("param"));
        form.setCurrentSmId(param.getString("currentSmId"));
        form.setWarningFormula(param.getString("warningFormula"));
        form.setAssessmentFormula(param.getString("assessmentFormula"));
        String strategyMapId = o_kpiStrategyMapBO.mergeStrategyMap(form);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        result.put("smId", strategyMapId);
        return result;
    }

    /**启用和停用战略目标
     * @param enable是否启用
     * @param strategyMapId 战略目标id
     * @return
     */
    @ResponseBody
    @RequestMapping("/kpi/kpistrategymap/mergestrategymapenable.f")
    public Map<String, Object> mergeStrategyMap(String enable, String strategyMapId) {
        Map<String, Object> result = new HashMap<String, Object>();
        StrategyMap strategyMap = o_kpiStrategyMapBO.findById(strategyMapId);
        strategyMap.setStatus(o_dictEntryBO.findDictEntryById(enable));
        o_kpiStrategyMapBO.mergeStrategyMap(strategyMap);
        if(Contents.DICT_Y.equals(enable)){
            String smid = new StringBuffer().append("'").append(strategyMapId).append("'").toString();
            HashMap<String, String> findAssessmentMaxEntTimeAllMap = o_relaAssessResultBO.findAssessmentMaxEntTimeAll("str", smid);
            String iconCls = findAssessmentMaxEntTimeAllMap.get(strategyMapId);
            String css = "icon-status-disable";
            if ("0alarm_startus_h".equals(iconCls)) {
                css = "icon-status-high";
            }
            else if ("0alarm_startus_m".equals(iconCls)) {
                css = "icon-status-mid";
            }
            else if ("0alarm_startus_l".equals(iconCls)) {
                css = "icon-status-low";
            }
            result.put("iconCls", css);
        }else{
            result.put("iconCls", "icon-symbol-status-sm");
        }
        result.put("success", true);
        return result;
    }

    /**
     * <pre>
     * 保存目标和指标关联信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param kpiParam
     *            kpi指标参数
     * @param currentSmId
     *            当前目标ID
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpistrategymap/mergestrategyrelakpi.f")
    public Map<String, Object> mergeStrategyRelaKpi(String kpiParam, String currentSmId) {
        Map<String, Object> result = new HashMap<String, Object>();
        o_kpiStrategyMapBO.mergeStrategyRelaKpi(kpiParam, currentSmId);
        result.put("success", true);
        return result;
    }

    /**
     * <pre>
     * 保存目标和预警关联信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param modifiedRecord
     *            预警或告警信息
     * @param currentSmId
     *            当前目标ID
     * @return
     * @throws ParseException
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpistrategymap/mergestrategyrelaalarm.f")
    public Map<String, Object> mergeStrategyRelaAlarm(String modifiedRecord, String currentSmId) throws ParseException {
        Map<String, Object> result = new HashMap<String, Object>();
        o_kpiStrategyMapBO.mergeStrategyRelaAlarm(modifiedRecord, currentSmId);
        result.put("success", true);
        return result;
    }

    /**
     * <pre>
     * 逻辑删除目标对象
     * </pre>
     * 
     * @author 陈晓哲
     * @param id
     *            目标ID
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpistrategymap/removestrategymap.f")
    public Map<String, Object> removeStrategyMap(String id) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        boolean result = o_kpiStrategyMapBO.removeStrategyMap(id);
        resultMap.put("result", result);
        return resultMap;
    }

    /**
     * <pre>
     * 逻辑删除目标对象及相关信息
     * </pre>
     * 
     * @author 陈晓哲
     * @param id
     *            目标ID
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpistrategymap/removerelstrategymap.f")
    public Map<String, Object> removeRelStrategyMap(String id) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        boolean result = o_kpiStrategyMapBO.removeStrategyMapBatch(id);
        resultMap.put("result", result);
        return resultMap;

    }

    /**
     * <pre>
     * 校验目标名称和编码是否重复
     * </pre>
     * 
     * @author 陈晓哲
     * @param name
     *            目标名称
     * @param code
     *            目标编码
     * @param id
     * @return
     * @since fhd　Ver 1.1
     */
    @ResponseBody
    @RequestMapping("/kpi/kpistrategymap/validate.f")
    public Map<String, Object> validate(String name, String code, String id) {
        boolean successFlag = true;
        Map<String, Object> result = new HashMap<String, Object>();
        if (o_kpiStrategyMapBO.findStrategyMapCountByName(name, id) > 0) {
            successFlag = false;
            result.put("error", "nameRepeat");
        }
        if (o_kpiStrategyMapBO.findStrategyMapCountByCode(code, id) > 0) {
            successFlag = false;
            result.put("error", "codeRepeat");
        }
        result.put("success", successFlag);
        return result;
    }

    /**
     * <pre>
     * 目标查看 - 详细页查看
     * </pre>
     * 
     * @author zhengjunxiang
     * @param id 目标ID
     * @return 前台接收的json
     */
    @ResponseBody
    @RequestMapping("/kpi/kpistrategymap/findsmbyidtojsonstr.f")
    public Map<String, Object> findSmByIdToJsonStr(String id) {
        DictEntry entry = null;
        StringBuffer smDim = new StringBuffer();
        StringBuffer smTheme = new StringBuffer();
        StringBuffer otherdimStr = new StringBuffer();
        StringBuffer otherThemeStr = new StringBuffer();

        if (StringUtils.contains(id, "_")) {
            id = id.split("_")[1];// 机构目标树,传递过来的id为orgid_smid
        }
        StrategyMap sm = o_kpiStrategyMapBO.findById(id);
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != sm) {
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("code", sm.getCode());
            jsonMap.put("name", sm.getName());
            jsonMap.put("shortName", sm.getShortName());
            jsonMap.put("warningFormula", sm.getWarningFormula());
            jsonMap.put("assessmentFormula", sm.getAssessmentFormula());
            jsonMap.put("desc", sm.getDesc());
            if (null != sm.getStatus()) {
                jsonMap.put("estatus", sm.getStatus().getName());
            }
            jsonMap.put("currentSmId", sm.getId());
            jsonMap.put("id", sm.getId());
            Set<SmRelaDim> smRelaDimSet = sm.getSmRelaDims();
            for (SmRelaDim smRelaDim : smRelaDimSet) {
                entry = smRelaDim.getSmDim();
                if (null != entry) {
                    if ("M".equals(smRelaDim.getType())) {
                        smDim.append(entry.getName()).append(",");
                    }
                    else {
                        otherdimStr.append(entry.getName()).append(",");
                    }
                }
            }
            if (smDim.length() > 0) {
                jsonMap.put("mainDim", smDim.toString().substring(0, smDim.length() - 1));
            }
            else {
                jsonMap.put("mainDim", "");
            }
            if (otherdimStr.length() > 0) {
                jsonMap.put("otherDim", otherdimStr.toString().substring(0, otherdimStr.length() - 1));
            }
            else {
                jsonMap.put("otherDim", "");
            }

            Set<SmRelaTheme> smRelaThemeSet = sm.getSmRelaThemes();
            for (SmRelaTheme smRelaTheme : smRelaThemeSet) {
                entry = smRelaTheme.getTheme();
                if (null != entry) {
                    if ("M".equals(smRelaTheme.getType())) {
                        smTheme.append(entry.getName()).append(",");
                    }
                    else {
                        otherThemeStr.append(entry.getName()).append(",");
                    }
                }
            }
            if (smTheme.length() > 0) {
                jsonMap.put("mainTheme", smTheme.toString().substring(0, smTheme.length() - 1));
            }
            else {
                jsonMap.put("mainTheme", "");
            }
            if (otherThemeStr.length() > 0) {
                jsonMap.put("otherTheme", otherThemeStr.toString().substring(0, otherThemeStr.length() - 1));
            }
            else {
                jsonMap.put("otherTheme", "");
            }

            if (null != sm.getParent()) {
                jsonMap.put("parentId", sm.getParent().getId());
            }

            if (null != sm.getChartType()) {
                jsonMap.put("chartTypeStr", sm.getChartType());
                jsonMap.put("charttypehidden", sm.getChartType());
            }

            /* 部门和人员信息 */
            JSONObject orgEmpObj = this.o_kpiStrategyMapBO.findSmRelaOrgEmpBySmToJsonStr(sm);
            jsonMap.put("ownDept", orgEmpObj.getString("ownDept"));
            jsonMap.put("reportDept", orgEmpObj.getString("reportDept"));
            jsonMap.put("viewDept", orgEmpObj.getString("viewDept"));
            map.put("data", jsonMap);
            map.put("success", true);
        }

        return map;
    }

    /**
     * 
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/kpi/kpistrategymap/findNewestAlarmById.f")
    public Map<String, Object> findNewestAlarmById(String id) {
        Map<String, Object> result = new HashMap<String, Object>();
        StrategyMap sm = o_kpiStrategyMapBO.findById(id);
        if (null != sm) {
            Set<SmRelaAlarm> alarms = sm.getSmRelaAlarms();

            AlarmPlan plan = null; //最近的告警方案
            Date newestDate = null;
            Iterator<SmRelaAlarm> ite = alarms.iterator();
            //循环获取最新的告警方案，以后可以直接通过hql直接查出最新的
            for (int i = 0; ite.hasNext(); i++) {
                SmRelaAlarm alarm = ite.next();
                if (i == 0) {
                    newestDate = alarm.getStartDate();
                    plan = alarm.getrAlarmPlan();
                }
                else {
                    Date currentDate = alarm.getStartDate();
                    if (currentDate.after(newestDate)) {
                        newestDate = currentDate;
                        plan = alarm.getrAlarmPlan();
                    }
                }
            }

            List<AlarmRegionForm> datas = new ArrayList<AlarmRegionForm>();
            if (plan != null) { //有可能没有告警方案
                Set<AlarmRegion> regions = plan.getAlarmRegions();
                AlarmRegionForm form = null;
                for (AlarmRegion region : regions) {
                    form = new AlarmRegionForm(region);
                    datas.add(form);
                }
            }

            result.put("datas", datas);
        }

        return result;
    }
}
