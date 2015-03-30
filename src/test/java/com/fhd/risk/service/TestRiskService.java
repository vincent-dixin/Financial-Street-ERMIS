/*
 *北京第一会达风险管理有限公司 版权所有 2012
 *Copyright(C) 2012 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.risk.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.fhd.comm.business.formula.FormulaCalculateBO;
import com.fhd.core.utils.test.SpringContextTestCase;
import com.fhd.risk.business.RiskService;
import com.fhd.risk.entity.Risk;
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
public class TestRiskService extends SpringContextTestCase {
	
	@Autowired    
    private RiskService service;
	
	
	@Test
	public void testSaveBatherKpiGather() {
		Risk risk = new Risk();
		risk.setId("zjxtest");
		risk.setName("zjxtest");
		List<String> respOrgIds = new ArrayList<String>();
		respOrgIds.add("10000");
		respOrgIds.add("10001");
		List<String> relaOrgIds = new ArrayList<String>();
		relaOrgIds.add("10002");
		relaOrgIds.add("10003");
		List<String> influenceKpiIds = new ArrayList<String>();
		influenceKpiIds.add("027c6a8b-b764-4ab7-87bf-6f289c3d89db");
		influenceKpiIds.add("027e7318-5935-48e1-a269-4bfd18ee0b5e");
		List<String> influeceProcessIds = new ArrayList<String>();
		influeceProcessIds.add("1001");
		influeceProcessIds.add("1002");
		Boolean success = service.saveRisk(risk, respOrgIds, relaOrgIds, influenceKpiIds, influeceProcessIds);
		System.out.println(success);
	}
}

