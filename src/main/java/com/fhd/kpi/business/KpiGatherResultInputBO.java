package com.fhd.kpi.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.bpm.business.JBPMBO;
import com.fhd.core.utils.DateUtils;

/**
 * 指标采集结果录入定时触发BO.
 * 触发时间表达式为：0 0 0 5 * ? --每月5号凌晨00:00:00触发
 * @author   吴德福
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-2-1		下午07:36:35
 */
@Service
public class KpiGatherResultInputBO {

    @Autowired
    private KpiGatherResultBO o_kpiGatherResultBO;

    @Autowired
    private JBPMBO o_jbpmBO;

    /**
     * 定时触发发送指标目标值和实际值待办.
     */
    @Transactional
    public boolean generateKpiGatherResultInput() {

        List<String> empIdList = new ArrayList<String>();
        List<Object[]> list = o_kpiGatherResultBO.findAllKpiRelaInfo();
        for (Object[] object : list) {
            if (null != object[0]) {
                if (!empIdList.contains(object[0])) {
                    empIdList.add(String.valueOf(object[0]));
                }
            }
        }

        //指标采集结果录入发送待办
        for (String empId : empIdList) {
            Map<String, Object> variables = new HashMap<String, Object>();
            String entityType = "KpiGatherResultInputJs";
            variables.put("entityType", entityType);
            variables.put("empId", empId);
            variables.put("date", DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            variables.put("name", "指标数据收集");
            o_jbpmBO.startProcessInstance(entityType, variables);
        }

        return true;
    }
}
