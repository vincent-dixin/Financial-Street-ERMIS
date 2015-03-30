/**
 * JBPMConstant.java
 * com.fhd.fdc.commons.business.bpm
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2011-1-11 		史永亮
 *
 * Copyright (c) 2011, Firsthuida All Rights Reserved.
 */

package com.fhd.bpm.jbpm;

import java.util.Map;
import java.util.Set;

/**
 * ClassName:JBPMConstant Function: TODO ADD FUNCTION Reason: TODO 存储标志
 * 
 * @author 史永亮
 * @version
 * @since Ver 1.1
 * @Date 2011-1-11 上午10:39:56
 * 
 * @see
 */
public class JBPMConstant {

	
//  触发流程
	/**
	 * 开启流程实例标志
	 */
	public static final int JBPM_STARTPROCESSINSTANCE = 1;

	/**
	 * 触发流程实例执行标志
	 */
	public static final int JBPM_SIGNALEXECUTION = 2;
	
//	流程实例状态，此标志用在BusinessWorkFlow实体类的state属性，用来表示该流程实例的运行状态
	/**
	 * 挂起
	 */
	public static final String JBPM_WORKFLOW_PAUSE = "2";
	/**
	 * 运行
	 */
	public static final String JBPM_WORKFLOW_RUN = "1";
	
	
//  流程定义类型，用于开启流程实例时区别到底开启那个流程定义的实例
	/**
	 * 标记风险识别流程定义
	 */
	public static final String JBPM_PROCESSDEFINITION_RISK = "risk";
	
	/**
	 * 标记风险识别流程定义
	 */
	public static final String JBPM_PROCESSDEFINITION_HISRISKEVENT = "hisRiskEvent";

	/**
	 * 标记问卷发布流程定义
	 */
	public static final String JBPM_PROCESSDEFINITION_QUESTION = "Questionnaire";
	
	/**
	 * 标记新问卷发布流程定义--吴德福
	 */
	public static final String JBPM_PROCESSDEFINITION_QUEST = "quest";
	/**
	 * 标记预警通知单发布流程定义--白蕾蕾
	 */
	public static final String JBPM_PROCESSDEFINITION_ALARM = "alarm";
	/**
	 * 标记辨识任务发布流程定义--陈建毅
	 */
	public static final String JBPM_PROCESSDEFINITION_RISKDIFFERENTIATE = "riskDifferent";
	
	/**
	 * 标记制定监控计划流程定义
	 */
	public static final String JBPM_PROCESSDEFINITION_KPI_PLAN = "kpi_plan";
	
	/**
	 * 标记修改监控计划流程定义
	 */
	public static final String JBPM_PROCESSDEFINITION_KPI_TARGET = "kpi_target";
	/**
	 * 标记整改工作单流程定义
	 */
	public static final String JBPM_PROCESSDEFINITION_RECTIFY_IMPROVEMENT="rectify";
	/**
	 * 标记内审计划流程定义
	 */
	public static final String JBPM_PROCESSDEFINITION_AUDIT_PLAN="auditPlan";
	/**
	 * 标记内审计划流程定义
	 */
	public static final String JBPM_PROCESSDEFINITION_AUDIT_ITEM="auditItem";	
	/**
	 * 标记内审计划流程定义
	 */
	public static final String JBPM_PROCESSDEFINITION_AUDIT_NOTICE="auditNotice";
	
	/**
	 * 标记内审计划流程定义
	 */
	public static final String JBPM_PROCESSDEFINITION_AUDIT_REPORT="auditReport";
	
	/**
	 * 标记创建风险控制计划流程定义--姚志勇
	 */
	public static final String JBPM_PROCESSDEFINITION_SCENARIO = "scenario";
	/**
	 * 标记独立创建风险控制流程定义--姚志勇
	 */
	public static final String JBPM_PROCESSDEFINITION_MEASUREINDEPENDENT = "measureIndependent";
	/**
	 * 标记创建风险控制计划执行记录流程定义--姚志勇 2012年02月02日
	 */
	public static final String JBPM_PROCESSDEFINITION_EXECUTION = "controlExecution";
	/**
	 * 标记创建风险控制计划执行记录子流程定义--姚志勇 2012年02月02日
	 */
	public static final String JBPM_PROCESSDEFINITION_EXECUTIONSUB = "controlExecutionSub";
	/**
	 * 福建电力指标--姚志勇 2012年02月15日
	 */
	public static final String JBPM_PROCESSDEFINITION_KPI = "kpi";
	
	
	

//	流程对应的实体业务类型，主要用于任务列表是区分调用不同的处理页面，在开启动作流的时候插入到工作流业务关系表中。	
	/**
	 * 风险识别。
	 */
	public static final String JBPM_ENTITY_RISK = "risk";

	/**
	 * 风险事件。
	 */
	public static final String JBPM_ENTITY_RISKEVENT = "riskevent";

	/**
	 * 历史风险。
	 */
	public static final String JBPM_ENTITY_RISKHISTORY = "riskhistory";

	/**
	 * 问卷调查。
	 */
	public static final String JBPM_ENTITY_QUESTION = "question";
	
	/**
	 * 问卷评估--吴德福。
	 */
	public static final String JBPM_ENTITY_QUEST = "quest";
	/**
	 * 辨识任务--陈建毅
	 */
	public static final String JBPM_ENTITY_RISKDIFFERENTIATE = "riskDifferent";
	/**
	 * 预警通知单--白蕾蕾
	 */
	public static final String JBPM_ENTITY_ALARM = "alarm";
	/**
	 * 确认辨识问卷--陈建毅
	 */
	public static final String JBPM_ENTITY_AFFIRMTRISKDIFFERENTIATE = "affirmriskDifferent";
	/**
	 * 部门汇总--陈建毅
	 */
	public static final String JBPM_ENTITY_COLLECTRISKDIFFERENT = "collectriskDifferent";
	/**
	 * 分发辨识任务--陈建毅
	 */
	public static final String JBPM_ENTITY_DISPENSERISKDIFFERENTIATE = "dispenseRiskDifferent";
	/**
	 * 应对计划。
	 */
	public static final String JBPM_ENTITY_CONTROL = "control";
	
	/**
	 * 监控计划。
	 */
	public static final String JBPM_ENTITY_KPI_PLAN = "kpi_plan";
	
	/**
	 * 指标采集。
	 */
	public static final String JBPM_ENTITY_KPI_TARGET = "kpi_target";
	/**
	 * 整改工作单
	 */
	public static final String JBPM_ENTITY_RECTIFY_IMPROVEMENT = "rectify";
	/**
	 * 内审计划
	 */
	public static final String JBPM_ENTITY_AUDITPLAN = "audit_plan";
	/**
	 * 内审计划项目
	 */
	public static final String JBPM_ENTITY_AUDITITEM = "audit_item";
	/**
	 * 内审通知
	 */
	public static final String JBPM_ENTITY_AUDITNOTICE = "audit_notice";
	/**
	 * 审计报告
	 */
	public static final String JBPM_ENTITY_AUDITREPORT = "audit_report";
	
	
	/**
	 * 风险控制计划--姚志勇
	 */
	public static final String JBPM_ENTITY_SCENARIO = "scenario";
	/**
	 * 福建电力指标流程--姚志勇
	 */
	public static final String JBPM_ENTITY_KPI = "kpi";
	/**
	 * 风险控制执行记录--姚志勇 controlExecution
	 */
	public static final String JBPM_ENTITY_EXECUTION = "controlExecution";
	/**
	 * 风险控制执行记录子流程--姚志勇 controlExecutionSub
	 */
	public static final String JBPM_ENTITY_EXECUTIONSUB = "controlExecutionSub";
	/**
	 * 风险控制措施--姚志勇
	 */
	public static final String JBPM_ENTITY_MEASURE = "createMeasure";
	/**
	 *独立风险控制措施--姚志勇
	 */
	public static final String JBPM_ENTITY_MEASUREINDEPENDENT = "mesasureIndependent";
	/**
	 * 风险
	 */
	public static final String JBPM_ENTITY_RISK_VALUE = "风险";

	/**
	 * 风险案例
	 */
	public static final String JBPM_ENTITY_RISKEVENT_VALUE = "风险案例";

	/**
	 * 历史风险。
	 */
	public static final String JBPM_ENTITY_RISKHISTORY_VALUE = "历史风险";

	/**
	 * 问卷调查。
	 */
	public static final String JBPM_ENTITY_QUESTION_VALUE = "问卷评估";
	
	/**
	 * 问卷评估--吴德福。
	 */
	//public static final String JBPM_ENTITY_QUEST_VALUE = "问卷评估";
	public static final String JBPM_ENTITY_QUEST_VALUE = "评估问卷";
	
	/**
	 * 风险辨识任务--陈建毅
	 */
	public static final String JBPM_ENTITY_RISKDIFFERENTIATE_VALUE = "辨识任务";
	

	/**
	 * 应对计划
	 */
	public static final String JBPM_ENTITY_CONTROL_VALUE = "应对计划";
	
	/**
	 * 监控计划。
	 */
	public static final String JBPM_ENTITY_KPI_PLAN_VALUE = "监控计划";
	
	/**
	 * 指标采集。
	 */
	public static final String JBPM_ENTITY_KPI_TARGET_VALUE = "指标采集";
	/**
	 * 整改跟进
	 */
	public static final String JBPM_ENTITY_RECTIFY_IMPROVEMENT_VALUE = "整改跟进";
	/**
	 * 内审计划
	 */
	public static final String JBPM_ENTITY_AUDITPLAN_VALUE = "内审计划";
	/**
	 * 内审计划项目
	 */
	public static final String JBPM_ENTITY_AUDITITEM_VALUE = "内审计划项目";
	/**
	 * 内审通知
	 */
	public static final String JBPM_ENTITY_AUDITNOTICE_VALUE = "内审通知";
	/**
	 * 内审报告
	 */
	public static final String JBPM_ENTITY_AUDITREPORT_VALUE = "审计报告";
	
	/**
	 * 风险控制计划--姚志勇
	 */
	public static final String JBPM_ENTITY_SCENARIO_VALUE = "风险控制计划";
	/**
	 * 福建电力指标流程--姚志勇
	 */
	public static final String JBPM_ENTITY_KPI_VALUE = "新建指标";
	/**
	 * 福建电力预警通知单流程--白蕾蕾
	 */
	public static final String JBPM_ENTITY_ALARM_VALUE = "预警通知单";
	/**
	 * 风险控制措施--姚志勇
	 */
	public static final String JBPM_ENTITY_MEASURE_VALUE = "风险控制措施";
	/**
	 * 风险控制执行记录--姚志勇 2012年02月02日添加
	 */
	public static final String JBPM_ENTITY_EXECUTION_VALUE = "控制措施执行记录";
	/**
	 * 风险控制执行记录子流程--姚志勇 2012年02月02日添加
	 */
	public static final String JBPM_ENTITY_EXECUTIONSUB_VALUE = "新增/修改风险控制措施";
	/**
	 * 独立风险控制措施--姚志勇
	 */
	public static final String JBPM_ENTITY_MEASUREINDEPENDENT_VALUE = "独立风险控制措施";
	
	
	
//  流程触发路径	
	/**
	 * 风险、风险事件
	 */
	public static final String JBPM_URL_RISK = "openWindow1('任务',680,600,'risk/identification/rmRiskAddOrEditPage.do";
	
	/**
	 * 历史事件
	 */
	public static final String JBPM_URL_RISKHISTORY = "openWindow1('任务',600,600,'risk/identification/rmHisEventAddOrEditPage.do";
	/**
	 * 测评问卷详细页面 姚志勇 11年12月13日添加
	 */
	public static final String JBPM_URL_QUEST_DETAIL = "openWindow1('任务',1000, 550,'risk/causeanalyze/questset/updateQuestSet.do";
	public static final String JBPM_URL_QUEST_DETAIL_SEMP_CONFIRM_SCENARIO = "openWindow1('任务',1000, 550,'risk/causeanalyze/questset/updateQuestSet_sempConfirm.do";
	/**
	 * 问卷选择打分人员--吴德福
	 */
	public static final String JBPM_URL_QUEST_SELECTEMP = "openWindow1('任务',1000,550,'risk/causeanalyze/assessment/scoringEmpSelect_new.do";
	/**
	 * 问卷选择打分人员--吴德福
	 */
	public static final String JBPM_URL_QUEST_SELECTEMP2 = "openWindow1('任务',1000,550,'risk/causeanalyze/assessment/scoringEmpSelect_new.do";
	/**
	 * 问卷选择打分人员--吴德福
	 */
	public static final String JBPM_URL_QUEST_SELECTEMP3 = "openWindow1('任务',1000,550,'risk/causeanalyze/assessment/scoringEmpSelect_new.do";
	
	/**
	 * 问卷答题
	 */
	public static final String JBPM_URL_QUESTION = "openWindow1('任务',800,500,'risk/causeanalyze/answer/questSetAnswerMain.do";
	
	
	/**
	 * 问卷答题
	 */
	public static final String JBPM_URL_QUESTION_SEMPCREATE = "openWindow1('任务',800,500,'risk/causeanalyze/answer/questSetAnswerMain_sempCreate.do";
	public static final String JBPM_URL_QUESTION_SZRAPPROVAL = "openWindow1('任务',800,500,'risk/causeanalyze/answer/questSetAnswerMain_szrApproval.do";
	public static final String JBPM_URL_QUESTION_DSEMPCREATE = "openWindow1('任务',800,500,'risk/causeanalyze/answer/questSetAnswerMain_dsempCreate.do";
	
	/**
	 * 测评问卷审批页面（打分人员列表）姚志勇 11年12月18日添加
	 */
	public static final String JBPM_URL_QUERTSETANSWERLIST = "openWindow1('任务',650,461,'risk/causeanalyze/answer/questSetAnswerList.do";
	/**
	 * 辨识任务详细页面 姚志勇 11年12月13日添加
	 */
	public static final String JBPM_URL_RISKDIFFERENTIATE_DETAIL = "openWindow1('任务',600,370,'risk/differentanalyse/queryDifferentSetAddOrEditMainForm.do";
	/**
	 * 预警通知单详细页面  白蕾蕾 12年12月17日添加
	 */
	public static final String JBPM_URL_ALARMNOTICE_DETAIL = "openWindow1('任务',600,370,'kpi/target/alarm/addOrUpdateAlarmNoticeMain.do";
	/**
	 * 预警通知单归档页面  白蕾蕾 12年12月21日添加
	 */
	public static final String JBPM_URL_ALARMNOTICE_ACHIVE = "openWindow1('任务',600,370,'kpi/target/alarm/alarmNoticeMain_achive.do";
	/**
	 * 预警通知单文书选人页面  白蕾蕾 12年12月21日添加
	 */
	public static final String JBPM_URL_ALARMNOTICE_WS = "openWindow1('任务',600,370,'kpi/target/alarm/alarmNoticeMain_WS.do";
	/**
	 * 预警通知单部门填写页面    白蕾蕾 12年12月17日添加
	 */
	public static final String JBPM_URL_ALARMNOTICE_FILLIN = "openWindow1('任务',600,370,'kpi/target/alarm/fillInAlarmNoticeMain.do";
	/**
	 * 预警通知单经法部确认页面    白蕾蕾 12年12月21日添加
	 */
	public static final String JBPM_URL_ALARMNOTICE_LAWQR = "openWindow1('任务',600,370,'kpi/target/alarm/alarmNoticeMain_LAWQR.do";
	/**
	 * 辨识任务填写
	 */
	public static final String JBPM_URL_RISKDIFFERENTIATE_MAKEOUT = "openWindow1('任务',800,550,'risk/differentanalyse/makeOutDifferentSetTask.do";
	/**
	 * 分发辨识任务
	 */
	public static final String JBPM_URL_RISKDIFFERENTIATE_DISPENSE = "openWindow1('任务',650,520,'risk/differentanalyse/riskdifferScoringEmpSelect.do";
	/**
	 * 分发辨识任务 选择人员 姚志勇 11年12月13日添加
	 */
	public static final String JBPM_URL_RISKDIFFERENTIATE_DISPENSE2 = "openWindow1('任务',650,325,'risk/differentanalyse/riskdifferScoringEmpSelect.do";
	/**
	 * 分发辨识任务 选择人员 姚志勇 11年12月13日添加
	 */
	public static final String JBPM_URL_RISKDIFFERENTIATE_DISPENSE3 = "openWindow1('任务',650,481,'risk/differentanalyse/riskdifferScoringEmpSelect.do";
	
	/**
	 * 辨识任务确认
	 */
	public static final String JBPM_URL_RISKDIFFERENTIATE_AFFIRM = "openWindow1('任务',800,550,'risk/differentanalyse/affirmDifferentSetTaskMainForm.do";
	/**
	 * 辨识任务部门汇总
	 */
	public static final String JBPM_URL_RISKDIFFERENTIATE_COLLECT = "openWindow1('任务',800,550,'risk/differentanalyse/collectDifferentSetTask.do";
	
	/**
	 * 应对计划
	 */
	public static final String JBPM_URL_CONTROL_PROGRAM = "openWindow1('任务',680,400,'risk/program/openRiskProgramAddorEdit.do";
	/**
	 * 执行情况
	 */
	public static final String JBPM_URL_CONTROL_EXECUTE = "openWindow1('任务',680,400,'risk/program/openProgramExecuteAddorEdit.do";
	/**
	 * 效果评价
	 */
	public static final String JBPM_URL_CONTROL_RESULT = "openWindow1('任务',680,400,'risk/program/resultTabs.do";
	
	/**
	 * 监控计划
	 */
	public static final String JBPM_URL_KPI_PLAN = "openWindow1('任务',1000,600,'kpi/target/addMonitorPlan.do";
	
	/**
	 * 指标采集
	 */
	public static final String JBPM_URL_KPI_TARGET = "openWindow1('任务',800,500,'kpi/target/editTargetGatherResult.do";
    
	/**
	 * 整改工作单
	 */
	public static final  String JBPM_URL_RECTIFY_IMPROVEMENT = "openWindow1('任务',800,500,'rectify/program/openRectifyProgramAudit.do";
	/**
	 * 整改工作项
	 */
	public static final  String JBPM_URL_RECTIFY_IMPROVEMENTITEM = "openWindow1('任务',800,500,'rectify/improvementItem/openImprovementItemAudit.do";
	
	/**
	 * 内审计划
	 */
	public static final  String JBPM_URL_AUDITPLAN = "openWindow1('任务',800,500,'audit/plan/auditPlanAddorEdit.do";
	/**
	 * 
	 * 内审项目
	 */
	public static final  String JBPM_URL_AUDITITEM = "openWindow1('任务',800,500,'?bType=";
	/**
	 * 内审通知
	 */
	public static final  String JBPM_URL_AUDITNOTICE = "openWindow1('任务',800,500,'?bType=";
	/**
	 * 内审报告
	 */
	public static final  String JBPM_URL_AUDITREPORT = "openWindow1('任务',800,500,'audit/auditItemAdvice/auditItemAddAdvice.do";
	
	/**
	 * 新建/补全整改工作单
	 */
	public static final String JBPM_URL_CREAT_WORKITEM = "openWindow1('任务',800,500,'rectify/program/openRectifyProgramEdit.do";	
	/**
	 * 福建电力指标-详细页面url-姚志勇
	 */
	public static final String JBPM_URL_KPI_DETAIL = "openWindow1('任务',800,580,'kpi/target/showTabs.do";
	/**
	 * 风险控制计划-选择分发人员-姚志勇
	 */
	public static final String JBPM_URL_SCENARIO_DISPENSE = "openWindow1('任务',800,565,'control/measureEmpSelect.do";
	/**
	 * 风险控制计划-选择分发人员-姚志勇（窗口高度不一样）
	 */
	public static final String JBPM_URL_SCENARIO_DISPENSE2 = "openWindow1('任务',600,325,'control/measureEmpSelect.do";
	/**
	 * 风险控制计划-选择分发人员-姚志勇（窗口高度不一样）
	 */
	public static final String JBPM_URL_SCENARIO_DISPENSE3 = "openWindow1('任务',600,490,'control/measureEmpSelect.do";
	public static final String JBPM_URL_SCENARIOMEASURE_ZR_APPROVAL_METHOD = "openWindow1('任务',800,515,'control/scenarioMeasureListPageMain_ZR_aprroval_method.do";
	/**
	 * 风险控制计划-控制措施汇总（风险管理部）-姚志勇
	 */
	//public static final String JBPM_URL_MEASURE_SUMMARY = "openWindow1('任务',800,550,'control/gotoMeasureListPage.do";
	public static final String JBPM_URL_MEASURE_SUMMARY = "openWindow1('任务',800,550,'control/gotoCheckpointTreeListPage.do";
	/**
	 * 风险控制计划-部门控制措施汇总（部门汇总）-姚志勇
	 */
	public static final String JBPM_URL_ORG_MEASURE_SUMMARY = "openWindow1('任务',800,515,'control/gotoMeasureListPage.do";
	/**
	 * 独立风险控制措施-姚志勇
	 */
	public static final String JBPM_URL_MEASUREINDEPENDENT = "openWindow1('任务',800,500,'control/addOrUpdateMeasureIndependent.do";
	/**
	 * 控制执行记录 姚志勇 12年02月02日添加
	 */
	public static final String JBPM_URL_CREATE_EXECUTION = "openWindow1('任务',650,460,'control/executeJbpm/executeUpdateMain.do";
	/**
	 * 控制执行记录子流程-修改控制措施（独立创建控制措施的页面） 姚志勇 12年02月02日添加
	 */
	//public static final String JBPM_URL_CREATE_EXECUTIONSUB = "openWindow1('任务',650,480,'control/updateMeasurePageTabs.do";
	//zhdalong修改  ：添加页面和执行页面合并为一个  
//	public static final String JBPM_URL_CREATE_MEASURE = "openWindow1('任务',800,515,'/control/addOrUpdateMeasureMain.do";
	public static final String JBPM_URL_CREATE_EXECUTIONSUB = "openWindow1('任务',650,480,'control/addOrUpdateMeasureMain.do";
	
	/**
	 * taskview
	 */
	public static final String JBPM_URL_TASK_VIEW = "openWindow1('流程图', 850, 600, 'jbpm/taskView.do";
	
	
	/*对应数据库中的objname_字段key*/
	public static String PROCESS_RISKDIFFERENTIATE_KEY="riskDifferent";//riskDifferent--陈建毅
	public static String PROCESS_RISKDIFFERENTIATE_SUB_KEY="riskDifferentSub";//riskDifferentSub--陈建毅
	
	public static String PROCESS_RISKDIFFERENTIATE_VALUE="新辨识问卷流程";//辨识问卷流程--陈建毅
	public static String PROCESS_RISKDIFFERENTIATE_SUB_VALUE="新辨识问卷子流程";//辨识问卷子流程--陈建毅
	
	public static String PROCESS_INSTANCE_RISKDIFFERENTIATE_KEY="riskDifferent";//riskDifferent--陈建毅
	public static String PROCESS_INSTANCE_RISKDIFFERENTIATE_VALUE="新辨识问卷";//新测评问卷--陈建毅
	
	
	/*对应数据库中的objname_字段key*/
	public static String PROCESS_RISK_KEY="risk";//risk
	public static String PROCESS_QUESTIONNAIRE_KEY="Questionnaire";//Questionnaire
	public static String PROCESS_QUEST_KEY="quest";//quest--吴德福
	public static String PROCESS_QUESTIONNAIRE_SUB_KEY="QuestionnaireSub";//QuestionnaireSub
	public static String PROCESS_QUEST_SUB_KEY="questSub";//questSub--吴德福
	public static String PROCESS_KPI_PLAN_KEY="kpi_plan";//kpi_plan
	public static String PROCESS_KPI_TARGET_KEY="kpi_target";//kpi_target
	public static String PROCESS_SCENARIO_KEY="scenario";//scenario 姚志勇
	public static String PROCESS_MEASURE_KEY="createMeasure";//createMeasure 姚志勇
	public static String PROCESS_EXECUTION_KEY="controlExecution";//controlExecution 姚志勇 2012年02月02日
	public static String PROCESS_EXECUTION_SUB_KEY="controlExecutionSub";//controlExecutionSub 姚志勇 2012年02月02日
	public static String PROCESS_KPI_KEY="kpi";//福建电力kpi 姚志勇 2012年02月15日
	
	/*显示在页面上的字符value*/
	public static String PROCESS_RISK_VALUE="风险流程";//风险流程字段
	public static String PROCESS_QUESTIONNAIRE_VALUE="问卷流程";//问卷流程
	public static String PROCESS_QUEST_VALUE="新问卷流程";//新问卷流程--吴德福
	public static String PROCESS_QUESTIONNAIRE_SUB_VALUE="问卷子流程";//问卷子流程
	public static String PROCESS_QUEST_SUB_VALUE="新问卷子流程";//新问卷子流程--吴德福
	public static String PROCESS_KPI_PLAN_VALUE="监控计划流程";//监控计划流程
	public static String PROCESS_KPI_TARGET_VALUE="指标流程";//指标流程
	public static String PROCESS_SCENARIO_VALUE="风险控制计划";//scenario 姚志勇
	public static String PROCESS_MEASURE_VALUE="风险控制措施";//风险控制措施 姚志勇
	public static String PROCESS_EXECUTION_VALUE="controlExecution";//controlExecution 姚志勇 2012年02月02日
	public static String PROCESS_EXECUTION_SUB_VALUE="controlExecutionSub";//controlExecutionSub 姚志勇 2012年02月02日
	public static String PROCESS_KPI_VALUE="指标流程";//福建电力kpi 姚志勇 2012年02月15日
	
	/*设置流程实例的类型key*/
	public static String PROCESS_INSTANCE_RISKEVENT_KEY="riskevent";//riskevent
	public static String PROCESS_INSTANCE_RISKHISTORY_KEY="riskhistory";//riskhistory
	public static String PROCESS_INSTANCE_RISK_KEY="risk";//risk
	public static String PROCESS_INSTANCE_QUESTION_KEY="question";//question
	public static String PROCESS_INSTANCE_QUEST_KEY="quest";//question--吴德福
	public static String PROCESS_INSTANCE_CONTROL_KEY="control";//control
	public static String PROCESS_INSTANCE_KPIPLAN_KEY="kpi_plan";//kpi_plan
	public static String PROCESS_INSTANCE_KPITARGET_KEY="kpi_target";//kpi_target
	public static String PROCESS_INSTANCE_RECTIFY_KEY="rectify";//rectify
	public static String PROCESS_INSTANCE_SCENARIO_KEY="scenario";//风险控制计划--姚志勇
	public static String PROCESS_INSTANCE_MEASURE_KEY="createMeasure";//风险控制措施-姚志勇
	public static String PROCESS_INSTANCE_MEASURE_SUMMARY_KEY="measureSummary";//风险控制措施汇总-姚志勇
	public static String PROCESS_INSTANCE_ORG_MEASURE_SUMMARY_KEY="orgMeasureSummary";//部门控制措施汇总-姚志勇
	public static String PROCESS_INSTANCE_EXECUTION_KEY="controlExecution";//controlExecution 姚志勇 2012年02月02日
	public static String PROCESS_INSTANCE_EXECUTION_SUB_KEY="controlExecutionSub";//controlExecutionSub 姚志勇 2012年02月02日
	public static String PROCESS_INSTANCE_KPI_KEY="kpi";//福建电力kpi 姚志勇 2012年02月15日
	
	/*设置流程实例的类型value*/
	public static String PROCESS_INSTANCE_RISKEVENT_VALUE="风险事件";//风险事件
	public static String PROCESS_INSTANCE_RISKHISTORY_VALUE="历史事件";//历史事件
	public static String PROCESS_INSTANCE_RISK_VALUE="风险";//风险
	public static String PROCESS_INSTANCE_QUESTION_VALUE="评估问卷";//测评问卷
	public static String PROCESS_INSTANCE_QUEST_VALUE="新测评问卷";//新测评问卷--吴德福
	public static String PROCESS_INSTANCE_CONTROL_VALUE="风险应对";//风险应对
	public static String PROCESS_INSTANCE_KPIPLAN_VALUE="监控计划";//监控计划
	public static String PROCESS_INSTANCE_KPITARGET_VALUE="指标采集";//指标采集
	public static String PROCESS_INSTANCE_RECTIFY_VALUE="整改跟进";//整改跟进
	public static String PROCESS_INSTANCE_SCENARIO_VALUE="风险控制计划";//风险控制计划--姚志勇
	public static String PROCESS_INSTANCE_MEASURE_VALUE="风险控制措施";//风险控制措施--姚志勇
	public static String PROCESS_INSTANCE_EXECUTION_VALUE="controlExecution";//controlExecution 姚志勇 2012年02月02日
	public static String PROCESS_INSTANCE_EXECUTION_SUB_VALUE="controlExecutionSub";//controlExecutionSub 姚志勇 2012年02月02日
	public static String PROCESS_INSTANCE_KPI_VALUE="指标流程";//福建电力kpi 姚志勇 2012年02月15日
	
	/*活动名称 ActivityName*/
	

	
	/*2011年12月8日姚志勇添加 - 辨识任务 开始*/
	public static final String ACTIVITY_NAME_DIFFERENT_CREATE = "创建风险辨识计划";//创建风险辨识计划
	public static final String ACTIVITY_NAME_DIFFERENT_ZB_APPROVAL = "部门人员审核风险辨识计划";//其他员工审核风险辨识计划
	public static final String ACTIVITY_NAME_DIFFERENT_ZR_APPROVAL = "领导审批风险辨识计划";//经法部主任审批风险辨识计划
	public static final String ACTIVITY_NAME_DIFFERENT_CONFIRM_SCENARIO = "发布风险辨识计划";//风险管理员下发风险辨识计划
	public static final String ACTIVITY_NAME_DIFFERENT_WS_ACCEPT = "文书接受风险辨识计划";//文书接受风险辨识计划
	
	
	public static final String ACTIVITY_NAME_DIFFERENT_EMP_CREATE_MEASURE = "专责填写风险辨识任务";//专员填写风险辨识任务
	
	
	//public static final String ACTIVITY_NAME_DIFFERENT_ZR_DISTRIBUTION = "主任分配风险辨识任务";//主任分配风险辨识任务
	public static final String ACTIVITY_NAME_DIFFERENT_ZR_DISTRIBUTION = "领导分配风险辨识任务";//主任分配风险辨识任务
	public static final String ACTIVITY_NAME_DIFFERENT_CZ_DISTRIBUTION = "处长分配风险辨识任务";//处长分配风险辨识任务
	public static final String ACTIVITY_NAME_DIFFERENT_CZ_APPROVAL_MEASURE = "处长审批风险辨识任务";//处长审批风险辨识任务
	//public static final String ACTIVITY_NAME_DIFFERENT_ZR_APPROVAL_MEASURE = "主任审批风险辨识任务";//主任审批风险辨识任务
	public static final String ACTIVITY_NAME_DIFFERENT_ZR_APPROVAL_MEASURE = "领导审批风险辨识任务";//领导审批风险辨识任务
	public static final String ACTIVITY_NAME_DIFFERENT_MANAGER_APPROVAL_MEASURE = "风险管理员审批风险辨识任务";//风险管理员审批风险辨识任务
	public static final String ACTIVITY_NAME_DIFFERENT_SUMMAYR = "风险管理员汇总风险辨识计划";//风险管理员汇总风险辨识计划
	public static final String ACTIVITY_NAME_DIFFERENT_CZ_APPROVAL_SUMMARY = "经法部处长确认风险辨识结果";//经法部处长确认风险辨识结果
	//public static final String ACTIVITY_NAME_DIFFERENT_ZR_APPROVAL_SUMMARY = "经法部主任确认风险辨识结果";//经法部主任确认风险辨识结果
	public static final String ACTIVITY_NAME_DIFFERENT_ZR_APPROVAL_SUMMARY = "经法部领导确认风险辨识结果";//经法部领导确认风险辨识结果
	public static final String ACTIVITY_NAME_DIFFERENT_MEASURE_ARCHIVE = "风险管理员归档风险信息";//风险管理员归档风险信息
	
	/*2011年12月8日姚志勇添加 辨识任务 开始*/
	public static final String ACTIVITY_TYPE_DIFFERENT_CREATE = "createdifferent";//创建控制计划
	public static final String ACTIVITY_TYPE_DIFFERENT_ZB_APPROVAL = "zbApproval";//处长审批计划
	public static final String ACTIVITY_TYPE_DIFFERENT_ZR_APPROVAL = "zrApproval";//主任审批计划
	public static final String ACTIVITY_TYPE_DIFFERENT_CONFIRM_SCENARIO = "confirmScenario";//下发计划
	public static final String ACTIVITY_TYPE_DIFFERENT_WS_ACCEPT = "wsAccept";//文书接受
	
	public static final String ACTIVITY_TYPE_DIFFERENT_ZR_DISTRIBUTION = "zrDistribution";//主任分配任务
	public static final String ACTIVITY_TYPE_DIFFERENT_CZ_DISTRIBUTION = "czDistribution";//处长分配任务
	public static final String ACTIVITY_TYPE_DIFFERENT_EMP_CREATE_MEASURE = "empCreateMeasure";//填写控制措施
	public static final String ACTIVITY_TYPE_DIFFERENT_CZ_APPROVAL_MEASURE = "czApprovalMeasure";//处长审批措施
	public static final String ACTIVITY_TYPE_DIFFERENT_ZR_APPROVAL_MEASURE = "zrApprovalMeasure";//主任审批措施
	public static final String ACTIVITY_TYPE_DIFFERENT_WS_SEND = "wsSend";//文书发送
	public static final String ACTIVITY_TYPE_DIFFERENT_APPROVAL_MEASURE = "managerApprovalMeasure";//管理员审批
	public static final String ACTIVITY_TYPE_DIFFERENT_SUMMAYR = "summary";//汇总计划
	public static final String ACTIVITY_TYPE_DIFFERENT_CZ_APPROVAL_SUMMARY = "czApprovalSummary";//处长确认
	public static final String ACTIVITY_TYPE_DIFFERENT_ZR_APPROVAL_SUMMARY = "zrApprovalSummary";//主任确认
	public static final String ACTIVITY_TYPE_DIFFERENT_MEASURE_ARCHIVE = "archive";//归档
	/*2011年12月8日姚志勇添加 辨识任务 结束*/
	
//	/*2011年12月8日姚志勇添加 - 评估问卷 开始*/
//	public static final String ACTIVITY_NAME_QUESTSET_CREATE = "创建风险评估计划";//创建辨识计划
//	public static final String ACTIVITY_NAME_QUESTSET_CZ_APPROVAL = "审核风险评估计划";//处长审批计划
//	public static final String ACTIVITY_NAME_QUESTSET_ZR_APPROVAL = "领导审批风险评估计划";//主任审批计划
//	public static final String ACTIVITY_NAME_QUESTSET_CONFIRM_SCENARIO = "发布风险评估计划";//下发计划
//	public static final String ACTIVITY_NAME_QUESTSET_WS_ACCEPT = "文书接受风险评估计划";//文书接受
//	public static final String ACTIVITY_NAME_QUESTSET_ZR_DISTRIBUTION = "主任分配风险评估任务";//主任分配任务
//	public static final String ACTIVITY_NAME_QUESTSET_CZ_DISTRIBUTION = "领导分配风险评估任务";//处长分配任务
//	public static final String ACTIVITY_NAME_QUESTSET_EMP_CREATE_MEASURE = "专责填写风险评估问卷";//填写测评问卷
//	public static final String ACTIVITY_NAME_QUESTSET_CZ_APPROVAL_MEASURE = "处长审批风险评估任务";//处长审批任务
//	public static final String ACTIVITY_NAME_QUESTSET_ZR_APPROVAL_MEASURE = "领导审批风险评估任务";//主任审批任务
//	/*2011年12月8日姚志勇添加- 测评问卷 结束*/
	
	/*2012年11月22日白蕾蕾添加 - 评估问卷 开始*/
	public static final String ACTIVITY_NAME_QUESTSET_CREATE_SCENARIO = "创建评估计划";
	public static final String ACTIVITY_NAME_QUESTSET_CZ_APPROVAL_SCENARIO = "部门人员审核评估计划";
	public static final String ACTIVITY_NAME_QUESTSET_ZR_APPROVAL_SCENARIO = "领导审批评估计划";
	public static final String ACTIVITY_NAME_QUESTSET_CONFIRM_SCENARIO = "发布评估计划";
	public static final String ACTIVITY_NAME_QUESTSET_WS_ACCEPT_SCENARIO = "文书接收评估计划";
	public static final String ACTIVITY_NAME_QUESTSET_SEMP_CREATE_QUESTSET = "确认评估计划填写评估问卷";
	public static final String ACTIVITY_NAME_QUESTSET_SZR_APPROVAL_QUESTSET = "领导审批评估计划和问卷";
	public static final String ACTIVITY_NAME_QUESTSET_SEMP_CONFIRM_SCENARIO = "专责下发评估计划";
	public static final String ACTIVITY_NAME_QUESTSET_DSZR_DISTRIBUTION = "部门领导分配任务";
	public static final String ACTIVITY_NAME_QUESTSET_DSEMP_CREATE_QUESTSET = "专责填写评估问卷";
	public static final String ACTIVITY_NAME_QUESTSET_DSZR_APPROVAL_QUESTSET = "部门领导审批评估问卷";
	public static final String ACTIVITY_NAME_QUESTSET_SEMP_APPROVAL_QUESTSET = "省公司审批评估问卷";
	/*2012年11月22日白蕾蕾添加 - 测评问卷 结束*/
	
	/*2012年02月02日姚志勇添加 - 执行记录 开始*/
	public static final String ACTIVITY_NAME_EXECUTION_CREATE = "填写控制措施执行评价";//创建控制计划
	public static final String ACTIVITY_NAME_EXECUTION_CZ_APPROVAL = "审批控制措施执行评价";//处长审批计划
	public static final String ACTIVITY_NAME_EXECUTION_ZR_APPROVAL = "领导审批控制措施执行评价";//主任审批计划
	public static final String ACTIVITY_NAME_EXECUTION_MANAGER_APPROVAL = "风险管理员审批控制措施执行评价";//管理员审批
	public static final String ACTIVITY_NAME_EXECUTION_CZ_APPROVAL_SUMMARY = "经法部处长确认控制措施执行评价";//处长审批汇总
	public static final String ACTIVITY_NAME_EXECUTION_ZR_APPROVAL_SUMMARY = "领导确认控制措施执行评价";//主任确认
	public static final String ACTIVITY_NAME_EXECUTION_ARCHIVE = "风险管理员归档控制措施执行评价";//归档
	/*2012年02月02日姚志勇添加 - 执行记录 结束*/
	
	/*2012年02月02日姚志勇添加 - 执行记录子流程-修改风险控制措施 开始*/
	public static final String ACTIVITY_NAME_EXECUTIONSUB_CREATE = "新增/修改风险控制措施";//新增控制措施
	public static final String ACTIVITY_NAME_EXECUTIONSUB_CZ_APPROVAL = "处长审批风险控制措施";//处长审批计划
	public static final String ACTIVITY_NAME_EXECUTIONSUB_ZR_APPROVAL = "领导审批风险控制措施";//主任审批计划
	public static final String ACTIVITY_NAME_EXECUTIONSUB_MANAGER_APPROVAL = "风险管理员审批风险控制措施";//管理员审批
	public static final String ACTIVITY_NAME_EXECUTIONSUB_CZ_APPROVAL_SUMMARY = "经法部处长确认风险控制措施";//处长审批汇总
	public static final String ACTIVITY_NAME_EXECUTIONSUB_ZR_APPROVAL_SUMMARY = "经法部领导确认风险控制措施";//主任确认
	public static final String ACTIVITY_NAME_EXECUTIONSUB_ARCHIVE = "风险管理员归档风险控制措施";//归档
	/*2012年02月02日姚志勇添加 - 执行记录 结束*/
	
	/*2012年02月15日姚志勇添加 - 福建电力指标 开始*/
	public static final String ACTIVITY_NAME_KPI_CREATE_SCENARIO = "创建指标信息";//创建控制指标
	public static final String ACTIVITY_NAME_KPI_CZ_APPROVAL = "经法部处长审批指标信息";//处长审批指标
	public static final String ACTIVITY_NAME_KPI_ZR_APPROVAL = "经法部主任审批指标信息";//主任审批指标
	public static final String ACTIVITY_NAME_KPI_CONFIRM_SCENARIO = "经法部领导审批指标信息";//下发指标
	public static final String ACTIVITY_NAME_KPI_WS_ACCEPT = "文书接受指标信息";//文书接受
	public static final String ACTIVITY_NAME_KPI_ZR_DISTRIBUTION = "领导分配指标任务";//主任分配任务
	public static final String ACTIVITY_NAME_KPI_CZ_DISTRIBUTION = "处长分配指标任务";//处长分配任务
	public static final String ACTIVITY_NAME_KPI_EMP_CREATE_MEASURE = "专责填写指标信息";//填写控制指标
	public static final String ACTIVITY_NAME_KPI_CZ_APPROVAL_MEASURE = "处长审批指标信息";//处长审批任务
	public static final String ACTIVITY_NAME_KPI_ZR_APPROVAL_MEASURE = "主任审批指标信息";//主任审批任务
	public static final String ACTIVITY_NAME_KPI_MANAGER_APPROVAL_MEASURE = "风险管理员审批指标信息";//管理员审批
	public static final String ACTIVITY_NAME_KPI_CZ_APPROVAL_SUMMARY = "经法部处长确认指标信息";//处长审批汇总
	public static final String ACTIVITY_NAME_KPI_ZR_APPROVAL_SUMMARY = "经法部主任确认指标信息";//主任审批任务
	public static final String ACTIVITY_NAME_KPI_MEASURE_ARCHIVE = "风险管理员归档指标信息";//归档
	/*2012年02月15日姚志勇添加- 福建电力指标 结束*/
	
	/*活动名称 ActivityType,和ActivityName对应，用于页面传参*/
	public static final String ACTIVITY_TYPE_SELECT_EMP = "selectEmp";//选择部门员工
	public static final String ACTIVITY_TYPE_CREATE_MEASURE = "createMasure";//创建控制措施
	public static final String ACTIVITY_TYPE_ORG_MEASURE_SUMMARY = "orgMeasureSummary";//部门汇总
	public static final String ACTIVITY_TYPE_MEASURE_SUMMARY = "measureSummary";//措施汇总
	public static final String ACTIVITY_TYPE_CREATE_MEASUREINDEPENDENT = "measureIndependent";//独立创建控制措施
	public static final String ACTIVITY_TYPE_ORG_APPROVAL = "orgApproval";//独立措施部门领导审批
	public static final String ACTIVITY_TYPE_RISKMANAGER_APPROVAL = "riskmanagerApproval";//独立措施部门风险管理员审批
	
	
//	/*2011年12月8日姚志勇添加 测评问卷 开始*/
//	public static final String ACTIVITY_TYPE_QUESTSET_CREATE = "createScenario";//创建评估计划
//	public static final String ACTIVITY_TYPE_QUESTSET_CZ_APPROVAL = "czApproval";//处长审批计划
//	public static final String ACTIVITY_TYPE_QUESTSET_ZR_APPROVAL = "zrApproval";//主任审批计划
//	public static final String ACTIVITY_TYPE_QUESTSET_CONFIRM_SCENARIO = "confirmScenario";//下发计划
//	public static final String ACTIVITY_TYPE_QUESTSET_WS_ACCEPT = "wsAccept";//文书接受
//	public static final String ACTIVITY_TYPE_QUESTSET_ZR_DISTRIBUTION = "zrDistribution";//主任分配任务
//	public static final String ACTIVITY_TYPE_QUESTSET_CZ_DISTRIBUTION = "czDistribution";//处长分配任务
//	public static final String ACTIVITY_TYPE_QUESTSET_EMP_CREATE_MEASURE = "empCreateMeasure";//填写控制措施
//	public static final String ACTIVITY_TYPE_QUESTSET_CZ_APPROVAL_MEASURE = "czApprovalMeasure";//处长审批措施
//	public static final String ACTIVITY_TYPE_QUESTSET_ZR_APPROVAL_MEASURE = "zrApprovalMeasure";//主任审批措施
//	public static final String ACTIVITY_TYPE_QUESTSET_WS_SEND = "wsSend";//文书发送
//	public static final String ACTIVITY_TYPE_QUESTSET_APPROVAL_MEASURE = "managerApprovalMeasure";//管理员审批
//	public static final String ACTIVITY_TYPE_QUESTSET_SUMMAYR = "summary";//测试计划
//	public static final String ACTIVITY_TYPE_QUESTSET_CZ_APPROVAL_SUMMARY = "czApprovalSummary";//处长确认
//	public static final String ACTIVITY_TYPE_QUESTSET_ZR_APPROVAL_SUMMARY = "zrApprovalSummary";//主任确认
//	public static final String ACTIVITY_TYPE_QUESTSET_MEASURE_ARCHIVE = "archive";//归档
//	/*2011年12月8日姚志勇添加 测评问卷 结束*/
	
	/*2011年12月8日姚志勇添加 测评问卷 开始*/
	public static final String ACTIVITY_TYPE_QUESTSET_CREATE_SCENARIO = "createScenario";//创建评估计划
	public static final String ACTIVITY_TYPE_QUESTSET_CZ_APPROVAL_SCENARIO = "czApprovalScenario";//部门人员审核评估计划
	public static final String ACTIVITY_TYPE_QUESTSET_ZR_APPROVAL_SCENARIO = "zrApprovalScenario";//领导审批评估计划
	public static final String ACTIVITY_TYPE_QUESTSET_CONFIRM_SCENARIO = "confirmScenario";//下发计划
	public static final String ACTIVITY_TYPE_QUESTSET_WS_ACCEPT_SCENARIO = "wsAcceptScenario";//文书接受
	public static final String ACTIVITY_TYPE_QUESTSET_SEMP_CREATE_QUESTSET = "sempCreatQuestset";//确认评估计划填写评估问卷
	public static final String ACTIVITY_TYPE_QUESTSET_SZR_APPROVAL_QUESTSET = "szrApprovalQuestset";//领导审批评估计划和问卷
	public static final String ACTIVITY_TYPE_QUESTSET_SEMP_CONFIRM_SCENARIO = "sempConfirmScenario";//专责下发评估计划
	public static final String ACTIVITY_TYPE_QUESTSET_DSZR_DISTRIBUTION = "dsZrDistribution";//部门领导分配任务
	public static final String ACTIVITY_TYPE_QUESTSET_DSEMP_CREATE_QUESTSET = "dsEmpCreateQuestset";//专责填写评估问卷
	public static final String ACTIVITY_TYPE_QUESTSET_DSZR_APPROVAL_QUESTSET = "dsZrApprovalQuestset";//部门领导审批评估问卷
	public static final String ACTIVITY_TYPE_QUESTSET_SEMP_APPROVAL_QUESTSET = "sEmpApprovalQuestset";//省公司审批评估问卷
	/*2011年12月8日姚志勇添加 测评问卷 结束*/
	
	/*2012年02月02日姚志勇添加 controlExecution 执行记录 开始*/
	public static final String ACTIVITY_TYPE_EXECUTION_CREATE = "createScenario";//创建控制计划
	public static final String ACTIVITY_TYPE_EXECUTION_CZ_APPROVAL = "czApproval";//处长审批计划
	public static final String ACTIVITY_TYPE_EXECUTION_ZR_APPROVAL = "zrApproval";//主任审批计划
	public static final String ACTIVITY_TYPE_EXECUTION_MANAGER_APPROVAL = "managerApprovalMeasure";//管理员审批
	public static final String ACTIVITY_TYPE_EXECUTION_CZ_APPROVAL_SUMMARY = "czApprovalSummary";//处长确认
	public static final String ACTIVITY_TYPE_EXECUTION_ZR_APPROVAL_SUMMARY = "zrApprovalSummary";//主任确认
	public static final String ACTIVITY_TYPE_EXECUTION_ARCHIVE = "archive";//归档
	/*2011年12月8日姚志勇添加 添加 controlExecution 执行记录 结束*/
	
	/*2012年02月02日姚志勇添加 执行记录-修改控制措施 开始*/
	public static final String ACTIVITY_TYPE_EXECUTIONSUB_CREATE = "createScenario";//新增控制措施
	public static final String ACTIVITY_TYPE_EXECUTIONSUB_CZ_APPROVAL = "czApproval";//处长审批计划
	public static final String ACTIVITY_TYPE_EXECUTIONSUB_ZR_APPROVAL = "zrApproval";//主任审批计划
	public static final String ACTIVITY_TYPE_EXECUTIONSUB_MANAGER_APPROVAL = "managerApprovalMeasure";//管理员审批
	public static final String ACTIVITY_TYPE_EXECUTIONSUB_CZ_APPROVAL_SUMMARY = "czApprovalSummary";//处长确认
	public static final String ACTIVITY_TYPE_EXECUTIONSUB_ZR_APPROVAL_SUMMARY = "zrApprovalSummary";//主任确认
	public static final String ACTIVITY_TYPE_EXECUTIONSUB_ARCHIVE = "archive";//归档
	/*2011年12月8日姚志勇添加 执行记录-修改控制措施 结束*/
	
	/*2012年02月15日姚志勇添加 福建电力指标 开始*/
	public static final String ACTIVITY_TYPE_KPI_CREATE_SCENARIO = "createScenario";//创建控制指标
	public static final String ACTIVITY_TYPE_KPI_CZ_APPROVAL = "czApproval";//处长审批指标
	public static final String ACTIVITY_TYPE_KPI_ZR_APPROVAL = "zrApproval";//主任审批指标
	public static final String ACTIVITY_TYPE_KPI_CONFIRM_SCENARIO = "confirmScenario";//下发指标
	public static final String ACTIVITY_TYPE_KPI_WS_ACCEPT = "wsAccept";//文书接受
	public static final String ACTIVITY_TYPE_KPI_ZR_DISTRIBUTION = "zrDistribution";//主任分配任务
	public static final String ACTIVITY_TYPE_KPI_CZ_DISTRIBUTION = "czDistribution";//处长分配任务
	public static final String ACTIVITY_TYPE_KPI_EMP_CREATE_MEASURE = "empCreateMeasure";//填写控制指标
	public static final String ACTIVITY_TYPE_KPI_CZ_APPROVAL_MEASURE = "czApprovalMeasure";//处长审批任务
	public static final String ACTIVITY_TYPE_KPI_ZR_APPROVAL_MEASURE = "zrApprovalMeasure";//主任审批任务
	public static final String ACTIVITY_TYPE_KPI_WS_SEND = "wsSend";//文书发送
	public static final String ACTIVITY_TYPE_KPI_MANAGER_APPROVAL = "managerApprovalMeasure";//管理员审批
	public static final String ACTIVITY_TYPE_KPI_ORG_SUMMAYR = "orgSummary";//措施汇总
	public static final String ACTIVITY_TYPE_KPI_CZ_APPROVAL_SUMMARY = "czApprovalSummary";//处长确认
	public static final String ACTIVITY_TYPE_KPI_ZR_APPROVAL_SUMMARY = "zrApprovalSummary";//主任确认
	public static final String ACTIVITY_TYPE_KPI_ARCHIVE = "archive";//归档
	/*2012年12月8日姚志勇添加 福建电力指标 结束*/
	
	/*2012年12月17日白蕾蕾添加 预警通知单 开始*/
	public static final String ACTIVITY_NAME_ALARM_CREATE = "创建预警通知单";
	public static final String ACTIVITY_NAME_ALARM_CZ_APPROVAL = "部门人员审核预警通知单";
	public static final String ACTIVITY_NAME_ALARM_ZR_APPROVAL = "领导审批预警通知单";
	public static final String ACTIVITY_NAME_ALARM_CONFIRM_ALARMNOTICE = "发布预警通知单";
	public static final String ACTIVITY_NAME_ALARM_WS_ACCEPT = "文书接收预警通知单";
	public static final String ACTIVITY_NAME_ALARM_EMP_FILLIN_ALARMNOTICE = "专责填写预警通知单任务";
	public static final String ACTIVITY_NAME_ALARM_ZR_APPROVAL_ALARMNOTICE = "领导审批预警通知单任务";
	public static final String ACTIVITY_NAME_ALARM_MANAGERAPPROVAL_ALARMNOTICE = "风险管理员审批预警通知单任务";
	public static final String ACTIVITY_NAME_ALARM_LAWZR_CONFIRM_ALARMNOTICE = "经法部领导确认预警通知单";
	public static final String ACTIVITY_NAME_ALARM_ARCHIVE = "风险管理员归档预警通知单";
	/*2012年12月17日白蕾蕾添加 预警通知单 结束*/
	/*2012年12月17日白蕾蕾添加 预警通知单 开始*/
	public static final String ACTIVITY_TYPE_ALARM_CREATE = "createAlarmNotice";
	public static final String ACTIVITY_TYPE_ALARM_CZ_APPROVAL = "czApproval";
	public static final String ACTIVITY_TYPE_ALARM_ZR_APPROVAL = "zrApproval";
	public static final String ACTIVITY_TYPE_ALARM_CONFIRM_ALARMNOTICE = "confirmAlarmNotice";
	public static final String ACTIVITY_TYPE_ALARM_WS_ACCEPT = "wsAccept";
	public static final String ACTIVITY_TYPE_ALARM_EMP_FILLIN_ALARMNOTICE = "empFillInAlarmNotice";
	public static final String ACTIVITY_TYPE_ALARM_ZR_APPROVAL_ALARMNOTICE = "zrApprovalMeasure";
	public static final String ACTIVITY_TYPE_ALARM_MANAGERAPPROVAL_ALARMNOTICE = "managerApprovalMeasure";
	public static final String ACTIVITY_TYPE_ALARM_LAWZR_CONFIRM_ALARMNOTICE = "lawZrConfirmAlarmNotice";
	public static final String ACTIVITY_TYPE_ALARM_ARCHIVE = "archive";
	/*2012年12月17日白蕾蕾添加 预警通知单 结束*/
	/**
	 * 用给定的参数，拼接成一个link地址
	 * @param map<key,value>
	 * @param isNeedSuffix 是否需要后缀"');"
	 * 注：map中的一定要有一个key为"url"的元素，其他的key为参数名。
	 * @return
	 */
	public static String mosaicLinkurl(Map<String,String> map,boolean isNeedSuffix){
		String result = "";
		if(map != null){
			StringBuilder sb = new StringBuilder();
			String url = map.get("url");
			sb.append(url);
			Set<String> keySet = map.keySet();
			boolean isFirst = true;//是否是第一个参数
			for (String key : keySet) {
				if("url".equals(key)){
					continue;//key为"url"的元素已经被取出来了
				}
				if(isFirst){//第一个参数
					sb.append("?");
					isFirst = false;
				}else{
					sb.append("&");
				}
				sb.append(key);
				sb.append("=");
				sb.append(map.get(key));
			}
			if(isNeedSuffix){
				sb.append("')");
			}
			result = sb.toString();
		}
		return result;
			
	}
	/**
	 * 风险控制计划-详细页面url-姚志勇
	 */
	public static final String JBPM_URL_SCENARIO_DETAIL = "openWindow1('任务',800,530,'control/addOrUpdateScenario.do";
	/**
	 * 风险控制计划-详细页面—部门领导分配任务url-白蕾蕾
	 */
	public static final String JBPM_URL_SCENARIO_DETAIL_ZR_DISTRIBUTION_ = "openWindow1('任务',800,530,'control/zrDistributionScenario.do";
	/**
	 * 风险控制计划-详细页面—部门领导分配任务url-白蕾蕾
	 */
	public static final String JBPM_URL_SCENARIO_WSACCEPT = "openWindow1('任务',800,530,'control/wsAcceptScenario.do";
	/**
	 * 风险控制计划-详细页面—专责下发管控方案url-白蕾蕾
	 */
	public static final String JBPM_URL_SCENARIO_DETAIL_EMP_CONFIRM_METHOD = "openWindow1('任务',800,530,'control/empConfirmMethod.do";
	/**
	 * 风险控制计划-详细页面—抄送地市url-白蕾蕾
	 */
	public static final String JBPM_URL_SCENARIO_DS_APPROVAL = "openWindow1('任务',800,530,'control/scenarioMeasureListPageMain_DS.do";
	
	/**
	 * 风险控制计划-填写控制措施-姚志勇
	 */
	public static final String JBPM_URL_CREATE_MEASURE = "openWindow1('任务',800,515,'control/gotoMeasureListPage.do";
	/**
	 * 风险控制计划-控制措施—风险管理员归档-白蕾蕾
	 */
	public static final String JBPM_URL_SCENARIOMEASURE_ARCHIVE = "openWindow1('任务',800,515,'control/scenarioMeasureListPageMain_ManagerArchive.do";
	/**
	 * 风险控制计划-控制措施-白蕾蕾
	 */
	public static final String JBPM_URL_SCENARIOMEASURE = "openWindow1('任务',800,515,'control/scenarioMeasureListPageMain_Edit.do";
	/**
	 * 风险控制计划-控制措施-省公司审批—白蕾蕾
	 */
	public static final String JBPM_URL_SCENARIOMEASURE_SGS = "openWindow1('任务',800,515,'control/scenarioMeasureListPageMain_SGS.do";
	/**
	 * 风险控制计划-控制措施-确认控制计划制定管控方案—白蕾蕾
	 */
	public static final String JBPM_URL_SCENARIOMEASURE_EMP_DISTRIBUTION = "openWindow1('任务',800,515,'control/scenarioMeasureListPageMain_emp_distribution.do";
	/*2011年12月8日姚志勇添加 - 风险控制相关 开始*/
//	public static final String ACTIVITY_NAME_CREATE_SCENARIO = "创建风险控制计划";//创建控制计划
//	public static final String ACTIVITY_NAME_CZ_APPROVAL = "审核风险控制计划";//处长审批计划
//	public static final String ACTIVITY_NAME_ZR_APPROVAL = "领导审批风险控制计划";//主任审批计划
//	public static final String ACTIVITY_NAME_CONFIRM_SCENARIO = "发布风险控制计划";//下发计划
//	public static final String ACTIVITY_NAME_WS_ACCEPT = "文书接受风险控制计划";//文书接受
	//public static final String ACTIVITY_NAME_ZR_DISTRIBUTION = "主任分配风险控制任务";//主任分配任务
//	public static final String ACTIVITY_NAME_ZR_DISTRIBUTION = "领导分配风险控制任务";//领导分配任务
//	public static final String ACTIVITY_NAME_CZ_DISTRIBUTION = "处长分配风险控制任务";//处长分配任务
	//public static final String ACTIVITY_NAME_EMP_CREATE_MEASURE = "专员填写风险控制措施";//填写控制措施
//	public static final String ACTIVITY_NAME_EMP_CREATE_MEASURE = "专责填写风险控制措施";//填写控制措施
//	public static final String ACTIVITY_NAME_CZ_APPROVAL_MEASURE = "处长审批风险控制措施";//处长审批任务
//	public static final String ACTIVITY_NAME_ZR_APPROVAL_MEASURE = "领导审批风险控制措施";//领导审批任务
	//public static final String ACTIVITY_NAME_ZR_APPROVAL_MEASURE = "主任审批风险控制措施";//主任审批任务
	public static final String ACTIVITY_NAME_MANAGER_APPROVAL_MEASURE = "风险管理员审批控制措施";//管理员审批
//	public static final String ACTIVITY_NAME_ORG_SUMMAYR = "风险管理员汇总风险控制措施";//汇总措施
//	public static final String ACTIVITY_NAME_CZ_APPROVAL_SUMMARY = "经法部处长确认风险控制措施";//处长审批汇总
	public static final String ACTIVITY_NAME_ZR_APPROVAL_SUMMARY = "经法部领导确认风险控制措施";//主任审批任务
	public static final String ACTIVITY_NAME_MEASURE_ARCHIVE = "风险管理员归档风险控制措施";//归档
	/*2011年12月8日姚志勇添加- 风险控制相关 结束*/
	/*2012年11月5日白蕾蕾添加 - 风险控制相关 开始*/
	public static final String ACTIVITY_NAME_CREATE_SCENARIO = "创建控制计划";
	public static final String ACTIVITY_NAME_CZ_APPROVAL = "部门人员审核控制计划";
	public static final String ACTIVITY_NAME_ZR_APPROVAL = "领导审批控制计划";
	public static final String ACTIVITY_NAME_CONFIRM_SCENARIO = "专责下发控制计划";
	public static final String ACTIVITY_NAME_WS_ACCEPT = "文书接收控制计划";
	public static final String ACTIVITY_NAME_EMP_DISTRIBUTION = "确认控制计划制定管控方案";
	public static final String ACTIVITY_NAME_ZR_APPROVAL_METHOD = "领导审批管控方案";
	public static final String ACTIVITY_NAME_EMP_CONFIRM_METHOD = "专责下发管控方案";
	public static final String ACTIVITY_NAME_ZR_DISTRIBUTION_ = "部门领导分配任务";
	public static final String ACTIVITY_NAME_EMP_CREATE_MEASURE = "专责填写控制措施";
	public static final String ACTIVITY_NAME_ZR_APPROVAL_MEASURE = "部门领导审批控制措施";
	public static final String ACTIVITY_NAME_SZR_APPROVAL_MEASURE = "省公司审批控制措施";
	public static final String ACTIVITY_NAME_SGS_APPROVAL = "抄送省公司经法部";
	public static final String ACTIVITY_NAME_DS_EMP_APPROVAL = "抄送地市单位专责";
	public static final String ACTIVITY_NAME_DS_APPROVAL = "抄送地市单位法律部门";
	/*2012年11月5日白蕾蕾添加- 风险控制相关 结束*/
	/*2011年12月8日姚志勇添加 风险控制相关 开始*/
	public static final String ACTIVITY_TYPE_CREATE_SCENARIO = "createScenario";//创建控制计划
	public static final String ACTIVITY_TYPE_CZ_APPROVAL = "czApproval";//处长审批计划
	public static final String ACTIVITY_TYPE_ZR_APPROVAL = "zrApproval";//主任审批计划
	public static final String ACTIVITY_TYPE_CONFIRM_SCENARIO = "confirmScenario";//专责下发计划
	public static final String ACTIVITY_TYPE_WS_ACCEPT = "wsAccept";//文书接受
//	public static final String ACTIVITY_TYPE_ZR_DISTRIBUTION = "zrDistribution";//主任分配任务
//	public static final String ACTIVITY_TYPE_CZ_DISTRIBUTION = "czDistribution";//处长分配任务
	public static final String ACTIVITY_TYPE_EMP_CREATE_MEASURE = "empCreateMeasure";//填写控制措施
	public static final String ACTIVITY_TYPE_CZ_APPROVAL_MEASURE = "czApprovalMeasure";//处长审批措施
	public static final String ACTIVITY_TYPE_ZR_APPROVAL_MEASURE = "zrApprovalMeasure";//主任审批措施
	public static final String ACTIVITY_TYPE_WS_SEND = "wsSend";//文书发送
	public static final String ACTIVITY_TYPE_MANAGER_APPROVAL_MEASURE = "managerApprovalMeasure";//管理员审批
	public static final String ACTIVITY_TYPE_ORG_SUMMAYR = "orgSummary";//措施汇总
	public static final String ACTIVITY_TYPE_CZ_APPROVAL_SUMMARY = "czApprovalSummary";//处长审批汇总
	public static final String ACTIVITY_TYPE_ZR_APPROVAL_SUMMARY = "zrApprovalSummary";//主任审批汇总
	public static final String ACTIVITY_TYPE_MEASURE_ARCHIVE = "archive";//归档
	/*2011年12月8日姚志勇添加 风险控制相关 结束*/
	/*2012年11月5日白蕾蕾添加 风险控制相关 开始*/
	public static final String ACTIVITY_TYPE_EMP_CONFIRM_METHOD = "empCofirmMethod";//专责下发管控方案
	public static final String ACTIVITY_TYPE_ZR_DISTRIBUTION_ = "zrDistribution_";//部门领导分配任务
	public static final String ACTIVITY_TYPE_EMP_DISTRIBUTION = "empDistribution";//专责分配控制计划
	public static final String ACTIVITY_TYPE_ZR_APPROVAL_METHOD = "zrApprovalMethod";//领导审批管控方案
	public static final String ACTIVITY_TYPE_SZR_APPROVAL_MEASURE = "szrApprovalMeasure";//省公司审批控制措施
	public static final String ACTIVITY_TYPE_SGS_APPROVAL = "sGSApproval";//抄送省公司经法部
	public static final String ACTIVITY_TYPE_DS_EMP_APPROVAL = "dSEmpApproval";//抄送地市单位专责
	public static final String ACTIVITY_TYPE_DS_APPROVAL = "dSApproval";//抄送地市单位法律部门
	/*2012年11月5日白蕾蕾添加 风险控制相关 结束*/
	/**
	 * 风险控制计划中标识风险管理员审批控制措施退回--白蕾蕾
	 */
	public static final String JBPM_SCENARIO_FXGLYTH = "fxglythFlag";
}
