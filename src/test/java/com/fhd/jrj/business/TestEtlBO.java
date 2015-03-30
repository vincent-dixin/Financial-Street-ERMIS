package com.fhd.jrj.business;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.fhd.core.utils.test.SpringContextTestCase;

/**
 * Created with IntelliJ IDEA.
 * User: vincent
 * Date: 13-4-22
 * Time: 下午4:28
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration(locations = { "/spring/spring-config-business.xml", "/spring/spring-config-jbpm.xml", "/spring/spring-config-quartz.xml",
        "/spring/spring-config-email.xml" })
public class TestEtlBO extends SpringContextTestCase {

    @Autowired
    private EtlBO o_etlBO;

    @Test
    public void testMergeCinemaRanking() {
        o_etlBO.mergeCinemaRanking();
    }

    @Test
    public void testMergeStandardPremium() {
        o_etlBO.mergeStandardPremium();
    }

    @Test
    public void testMergeOperatingIncomeGrowthRate() {
        o_etlBO.mergeOperatingIncomeGrowthRate();
    }

    @Test
    public void testMergeRetainedProfits() {
        o_etlBO.mergeRetainedProfits();
    }

}
