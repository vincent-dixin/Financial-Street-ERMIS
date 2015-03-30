package com.fhd.kpi.business;

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

//使用@RunWith(SpringJUnit4ClassRunner.class),才能使测试运行于Spring测试//环境    
@RunWith(SpringJUnit4ClassRunner.class)    
//@ContextConfiguration 注解有以下两个常用的属性：    
//locations：可以通过该属性手工指定 Spring 配置文件所在的位置,可以指定一个或多个 Spring 配置文件    
//inheritLocations：是否要继承父测试类的 Spring 配置文件，默认为 true    
@ContextConfiguration(locations={"classpath:spring/spring-config-business.xml",
								"classpath:spring/spring-config-quartz.xml",
								"classpath:spring/spring-config-email.xml",
								"classpath:spring/spring-config-jbpm.xml"
								})  
//如果只有一个配置文件就直接写locations=“配置文件路径+名”  
//多个文件也可以直接用正则表达式匹配,如"classpath:/context/applicationContext-*.xml" 
@TransactionConfiguration  
@Transactional 
public class TestRisk extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired    
    private IRiskBO bo; 
	
	@Test    
    public void Test()    
    {   
		String id = "eda8ffeab0da4159be0ff924108e3883MB0013";
		Page<Risk> page = new Page<Risk>();
        page.setPageNo(1);
        page.setPageSize(10);
		String sort = "id";
		String dir = "asc";
	    String query = null;
	    page = bo.findRiskEventByStrateMapId(id, page, sort, dir, query);
	    System.out.println(page.getTotalItems());
    }  
	
	@Test    
    public void TestHistory()    
    {   
		String id = "eda8ffeab0da4159be0ff924108e3883MB0013";
		Page<StrategyAdjustHistory> page = new Page<StrategyAdjustHistory>();
        page.setPageNo(1);
        page.setPageSize(10);
		String sort = "id";
		String dir = "asc";
	    String query = null;
	    page = bo.findStrategyHistoryByStrateMapId(id, page, sort, dir, query);
	    System.out.println(page.getTotalItems());
    }  
}
