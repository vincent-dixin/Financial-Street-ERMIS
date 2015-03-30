package com.fhd.comm.business;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.comm.dao.TimePeriodDAO;
import com.fhd.comm.entity.TimePeriod;

/**
 * 时间区间维度BO.
 * @author  吴德福
 * @version  
 * @since    Ver 1.1
 * @Date     2013-1-7     下午04:24:20
 * @see      
 */
@Service
@SuppressWarnings("unchecked")
public class TimePeriodBO {

	@Autowired
	private TimePeriodDAO o_timePeriodDAO;
	
	/**
	 * 根据id查询时间区间维度.
	 * @param id
	 * @return TimePeriod
	 */
	public TimePeriod findTimePeriodById(String id){
		return o_timePeriodDAO.get(id);
	}
	
	/**
	 * 根据id查询时间区间维度.
	 * @param id
	 * @return TimePeriod
	 */
	public List<TimePeriod> findTimePeriodByParentId(String parentId){
		Criteria criteria = o_timePeriodDAO.createCriteria();
		criteria.add(Restrictions.eq("parent.id", parentId));
		List<TimePeriod> list = criteria.list();
		return list;
	}
	
	/**
	 * 根据查询条件查询时间区间维度.
	 * @param year 年
	 * @param month 月
	 * @param type 频率
	 * @return TimePeriod
	 */
	public TimePeriod findTimePeriodByFre(String year, String month, String type){
		TimePeriod timePeriod = null;
		Criteria criteria = o_timePeriodDAO.createCriteria();
		
		criteria.add(Restrictions.eq("year", year));
		criteria.add(Restrictions.eq("month", month));
		criteria.add(Restrictions.eq("type", type));
		List<TimePeriod> list = criteria.list();
		if(null != list && list.size()>0){
			timePeriod = list.get(0);
		}
		return timePeriod;
	}
	/**
	 * 根据当前时间查询当前时间所在'月'的时间区间维度.
	 * @param type 时间区间维频率
	 * @return TimePeriod
	 */
	public TimePeriod findTimePeriodBySome(String type){
		TimePeriod timePeriod = null;
		Criteria criteria = o_timePeriodDAO.createCriteria();
		if(StringUtils.isNotBlank(type)){
			criteria.add(Restrictions.eq("type", type));
		}else{
			criteria.add(Restrictions.eq("type", "0frequecy_month"));
		}
		Date currentDate = new Date();
		criteria.add(Restrictions.le("startTime", currentDate));
		criteria.add(Restrictions.ge("endTime", currentDate));
		List<TimePeriod> list = criteria.list();
		if(null != list && list.size()>0){
			timePeriod = list.get(0);
		}
		return timePeriod;
	}
}
