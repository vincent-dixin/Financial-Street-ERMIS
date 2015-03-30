package com.fhd.risk.business;

import static org.hibernate.criterion.Restrictions.like;
import static org.hibernate.criterion.Restrictions.ne;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.core.dao.Page;
import com.fhd.risk.dao.RiskEventDAO;
import com.fhd.risk.entity.RiskEvent;

@Service
public class RiskEventRelatedService {
	@Autowired
	private RiskEventDAO o_riskEventDAO;
	
	public Page<RiskEvent> findRiskHistoryEventByRiskId(String id,
			Page<RiskEvent> page, String sort, String dir, String query) {
		DetachedCriteria dc = DetachedCriteria.forClass(RiskEvent.class);
		dc.add(Restrictions.eq("risk.id", id));
 
        return o_riskEventDAO.findPage(dc, page, false);
	}
	
	/**
	 * 按年和月模糊匹配
	 * @param id
	 * @param year
	 * @param month
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	public List<RiskEvent> findRiskHistoryEventByRiskId(String id,int year,int month) throws ParseException {
		Criteria criteria = o_riskEventDAO.createCriteria();
		criteria.add(Restrictions.eq("risk.id", id));
		if (year!=0) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String startDate = "";
			String endDate = "";
			if (month!=0) {
				String monthString = "";
				if(month<10){
					monthString = "0" + month;
				}else{
					monthString = "" + month;
				}
				startDate = year + "-"+monthString + "-01 00:00:00";
				month = month + 1;
				if(month<10){
					monthString = "0" + month;
				}else{
					monthString = "" + month;
				}
				endDate = year + "-"+ monthString + "-01 00:00:00";
			}else{
				startDate = year + "-01-01 00:00:00";
				endDate = (year+1) + "-01-01 00:00:00";
			}
			
			criteria.add(Restrictions.ge("occurDate",simpleDateFormat.parse(startDate)));
			criteria.add(Restrictions.lt("occurDate",simpleDateFormat.parse(endDate)));
		}
		criteria.addOrder(Order.asc("occurDate"));
		
        return criteria.list();
	}
}
