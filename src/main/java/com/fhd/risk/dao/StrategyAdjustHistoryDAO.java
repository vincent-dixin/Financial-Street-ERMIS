package com.fhd.risk.dao;

import org.springframework.stereotype.Repository;

import com.fhd.core.dao.hibernate.HibernateDao;
import com.fhd.risk.entity.OrgAdjustHistory;
import com.fhd.risk.entity.Risk;
import com.fhd.risk.entity.RiskAdjustHistory;
import com.fhd.risk.entity.StrategyAdjustHistory;

/**
 * 组织历史记录
 *
 * @author   zhengjunxiang
 * @version  
 * @since    Ver 1.0
 * @Date	 2013-4-18
 *
 * @see 	 
 */
@Repository
public class StrategyAdjustHistoryDAO  extends HibernateDao<StrategyAdjustHistory, String>{

}