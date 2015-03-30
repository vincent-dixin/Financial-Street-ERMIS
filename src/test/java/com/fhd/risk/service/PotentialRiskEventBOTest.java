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
import com.fhd.risk.business.PotentialRiskEventBO;
import com.fhd.risk.business.RiskService;
import com.fhd.risk.entity.Risk;
import com.fhd.risk.entity.RiskAdjustHistory;
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
public class PotentialRiskEventBOTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired    
    private PotentialRiskEventBO service; 
	
	@Test    
    public void getOrgTreeRecordByEventIdsTest()  
    {   
		String node = "1";
		String query = null;
		String[] eventIds = {"2","3"};
		List<Map<String,Object>> list = service.getOrgTreeRecordByEventIds(node, query, eventIds);
		System.out.println(list.size());
		
    }  
}
