package com.fhd.risk.dao;

import org.springframework.stereotype.Repository;

import com.fhd.core.dao.hibernate.HibernateDao;
import com.fhd.risk.entity.RiskEvent;

/**
 * 风险数据层 
 * @author 郑军祥		2013-06-19 
 */
@Repository
public class RiskEventDAO  extends HibernateDao<RiskEvent, String>{

}