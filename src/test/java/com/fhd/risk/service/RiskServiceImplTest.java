package com.fhd.risk.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.ContextConfiguration; 
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.Page;
import com.fhd.core.utils.test.SpringContextTestCase;
import com.fhd.risk.business.RiskEventService;
import com.fhd.risk.business.RiskService;
import com.fhd.risk.entity.Risk;
import com.fhd.risk.entity.RiskAdjustHistory;
import com.fhd.risk.entity.RiskEvent;
import com.fhd.risk.entity.StrategyAdjustHistory;
import com.fhd.risk.interfaces.IRiskBO;

/**
* 用于service的单元测试
* @author   郑军祥
* @since    fhd Ver 4.5
* @Date	 2013-6-1
*
* @see 	 
*/

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"classpath:spring/spring-config-business.xml",
								"classpath:spring/spring-config-quartz.xml",
								"classpath:spring/spring-config-email.xml",
								"classpath:spring/spring-config-jbpm.xml"
								})
@TransactionConfiguration  
@Transactional 
public class RiskServiceImplTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired    
    private RiskService service;
	@Autowired    
    private RiskEventService eventService; 
	
	@Test    
    public void findRiskInfoByIdTest()  
    {   
		String params = "[{riskId:\"03d4cbb0-bd39-4ae2-8b97-73974a6a4bf4\"},{riskId:\"1\"}]";
		Map<Integer, Map<String, Object>> map = service.findRiskInfoByIds(params);
		
    }  
	
	@Test    
    public void saveRiskTest()  
    {   
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
	
	@Test    
    public void saveRiskEventTest()  
    {  
		RiskEvent event = new RiskEvent();
		event.setId("ss");
		eventService.saveRiskEvent(event);
    }
 
}
