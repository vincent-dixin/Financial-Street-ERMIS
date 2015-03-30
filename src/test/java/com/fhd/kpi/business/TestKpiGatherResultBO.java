/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.kpi.business;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.fhd.comm.business.formula.FormulaCalculateBO;
import com.fhd.core.utils.test.SpringContextTestCase;
import com.fhd.sys.business.st.task.TriggerEmailBO;

/**
 *
 * @author   胡迪新
 * @since    fhd Ver 4.5
 * @Date	 2013-1-8  下午2:29:03
 *
 * @see 	 
 */
@ContextConfiguration(locations = {"/spring/spring-config-business.xml","/spring/spring-config-jbpm.xml","/spring/spring-config-quartz.xml","/spring/spring-config-email.xml"})
public class TestKpiGatherResultBO extends SpringContextTestCase {
	
	@Autowired
	private TriggerEmailBO triggerEmailBO;
	@Autowired
	private FormulaCalculateBO o_formulaCalculateBO;
	@Autowired
	private KpiGatherResultBO o_kpiGatherResultBO;
	@Autowired
	private KpiGatherResultInputBO o_kpiGatherResultInputBO;
	
	@Test
	public void testSaveBatherKpiGather() {
		//定时计算指标值、状态、趋势
		//triggerEmailBO.exeFun("com.fhd.comm.business.formula.FormulaAutoCalculateBO;formulaAutoCalculate");
		
		//通过sub函数计算记分卡
		//o_formulaCalculateBO.calculateCategory("产品研发", "category", "1+sub(myself,$评估值,$kpi,$A)+2+sub(myself,$评估值,$category,$S)");
		//通过记分卡计算记分卡
		//o_formulaCalculateBO.calculateCategory("产品研发", "category", "1+sub(myself,$评估值,$kpi,$A)+2+sub(myself,$评估值,$category,$S)");
		//通过指标计算记分卡
		//o_formulaCalculateBO.calculateCategory("产品研发", "category", "1+sub(myself,$评估值,$kpi,$A)+2+sub(myself,$评估值,$category,$S)");
	
		//生成指标采集结果数据
		//o_kpiGatherResultBO.saveBatherKpiGather();
		
		//定时发送指标数据收集待办
		o_kpiGatherResultInputBO.generateKpiGatherResultInput();
	}
}

