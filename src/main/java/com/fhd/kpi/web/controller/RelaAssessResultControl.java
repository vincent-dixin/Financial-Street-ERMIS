package com.fhd.kpi.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.comm.business.CategoryBO;
import com.fhd.comm.entity.AlarmPlan;
import com.fhd.comm.entity.AlarmRegion;
import com.fhd.comm.entity.Category;
import com.fhd.fdc.utils.Contents;
import com.fhd.kpi.business.RelaAssessResultBO;
import com.fhd.kpi.business.StrategyMapBO;
import com.fhd.kpi.business.dynamictable.AngularGauge;
import com.fhd.kpi.business.dynamictable.MSLine;
import com.fhd.kpi.entity.RelaAssessResult;
import com.fhd.kpi.entity.StrategyMap;

@Controller
public class RelaAssessResultControl {

    @Autowired
    private RelaAssessResultBO o_relaAssessResultBO;

    @Autowired
    private CategoryBO o_categoryBO;

    @Autowired
    private StrategyMapBO o_StrategyMapBO;

    @ResponseBody
    @RequestMapping("/kpi/categoryPanel.f")
    public Map<String, Object> categoryPanel(String condItem, HttpServletRequest request) {
        String xml = "";
        Category category = null;
        StrategyMap strategyMap = null;
        AlarmPlan alarmPlan = null;
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject jsobj = JSONObject.fromObject(condItem);
        String yearId = "";
        if (jsobj.get("yearId") != null) {
            yearId = jsobj.getString("yearId");
        }
        String monthId = "";
        if (jsobj.get("monthId") != null) {
            monthId = jsobj.getString("monthId");
        }
        boolean isNewValue = false;
        if (jsobj.get("isNewValue") != null) {
            isNewValue = Boolean.valueOf(jsobj.get("isNewValue").toString());
        }
        String dataType = "";
        if (jsobj.get("dataType") != null) {
            dataType = jsobj.getString("dataType");
        }
        String objectId = "";
        if (jsobj.get("objectId") != null) {
            objectId = jsobj.getString("objectId");
        }
        if ("str".equalsIgnoreCase(dataType)) {
            strategyMap = o_StrategyMapBO.findById(objectId);
            alarmPlan = o_StrategyMapBO.findAlarmPlanBySmId(objectId, Contents.ALARMPLAN_REPORT);
        }
        else if ("sc".equalsIgnoreCase(dataType)) {
            category = o_categoryBO.findCategoryById(objectId);
            alarmPlan = o_categoryBO.findAlarmPlanByScId(objectId, Contents.ALARMPLAN_REPORT);
        }

        RelaAssessResult relaAssessResultByDataTypeEn = o_relaAssessResultBO.findRelaAssessResultByDataTypeEn(dataType, objectId, isNewValue, yearId,
                monthId);
        if (null != relaAssessResultByDataTypeEn && null != alarmPlan) {
            List<AlarmRegion> AlarmRegionList = o_categoryBO.findAlarmRegionByAlarmId(alarmPlan.getId());
            if ("sc".equalsIgnoreCase(dataType)) {
                if (null != relaAssessResultByDataTypeEn.getAssessmentValue()) {
                    xml = AngularGauge.getXml(AlarmRegionList, relaAssessResultByDataTypeEn.getAssessmentValue(), category.getName());
                }
            }
            else if ("str".equalsIgnoreCase(dataType)) {
                if (null != relaAssessResultByDataTypeEn.getAssessmentValue()) {
                    xml = AngularGauge.getXml(AlarmRegionList, relaAssessResultByDataTypeEn.getAssessmentValue(), strategyMap.getName());
                }
            }
        }

        List<RelaAssessResult> relaAssessResultByDataTypeList = o_relaAssessResultBO.findRelaAssessResultByDataType(dataType, objectId, isNewValue,
                yearId, monthId);
        String histXml = MSLine.getXml(relaAssessResultByDataTypeList);

        result.put("success", true);
        result.put("xml", xml);
        result.put("histXml", histXml);
        return result;
    }
}
