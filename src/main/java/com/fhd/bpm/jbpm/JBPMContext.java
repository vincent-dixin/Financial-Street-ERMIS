package com.fhd.bpm.jbpm;

import java.util.HashMap;
import java.util.Map;

public class JBPMContext {
	public Map<String,String> url=new HashMap<String, String>();
	
	public JBPMContext() {
		/**
		 * 风险控制计划
		 */
		url.put("scenario", "/control/scenarioShow.do?id=");
		/**
		 * 风险控制措施执行
		 */
		url.put("controlExecution", "control/measureShowMainFramInfo.do?id=");
		/**
		 * 风险控制措施修改
		 */
		url.put("controlExecutionSub", "/control/measureShow.do?id=");
		/**
		 * 风险评估流程
		 */
		url.put("quest", "/risk/causeanalyze/assessment/questShow.do?id=");
		/**
		 * 风险辨识流程
		 */
		url.put("riskDifferent", "/risk/differentanalyse/showTask.do?viewType=listView&id=");
		/**
		 * 指标管理流程
		 */
		url.put("kpi", "/kpi/target/showTabs1.do?id=");
		
	}
	public Map<String, String> getUrl() {
		
		return url;
	}
	public void setUrl(Map<String, String> url) {
		this.url = url;
	}
	
	
}
