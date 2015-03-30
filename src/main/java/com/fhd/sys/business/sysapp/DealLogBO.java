package com.fhd.sys.business.sysapp;

import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.quartz.impl.StdScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.support.Page;

import com.fhd.sys.dao.sysapp.DealLogDAO;


import com.fhd.sys.entity.sysapp.DealLog;

/**
 * 计划任务--处理记录BO类.
 * 
 * @author weilunkai
 * @version V1.0 创建时间：2012-5-23 Company FirstHuiDa.
 */
@Service
@SuppressWarnings("unchecked")
public class DealLogBO {
	@Autowired
	private DealLogDAO o_dealLogDAO;
	
	/**
	 * 新增计划任务--处理记录.
	 * @author weilunkai
	 * @param 
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public void saveDealLog(DealLog dealLog) {
		o_dealLogDAO.merge(dealLog);
	}
	/**
	 * 删除计划任务--处理记录.
	 * @author weilunkai
	 * @param id 计划任务--处理记录id.
	 * @since fhd　Ver 1.1
	 */
//	@Transactional
//	public void removeDealLog(DealLog dealLog) {
//		o_dealLogDAO.merge(dealLog);
//	}
	/**
	 * 根据id查询计划任务--处理记录.
	 * @author weilunkai
	 * @param id 计划任务--处理记录id.
	 * @return DealLog 计划任务--处理记录.
	 * @since fhd　Ver 1.1
	 */
	public DealLog queryDealLogById(String id) {
		
		return o_dealLogDAO.get(id);
	}
	/**
	 * 查询所有计划任务--处理记录.
	 * @author weilunkai
	 * @param page
	 * @param username
	 * @param operateTime
	 * @return Page<DealLog>
	 */
	public Page<DealLog> queryAllDealLog(Page<DealLog> page,
			String username, String beginTime, String endTime,String sort,String dir) throws Exception {

		DetachedCriteria dc = DetachedCriteria.forClass(DealLog.class);
		
		System.out.println(username);
		if (StringUtils.isNotBlank(username)) {
			dc.createAlias("emp", "TaskEmp");
			dc.createAlias("TaskEmp.emp", "emp");
			//dc.createAlias("task.createBy", "createBy");
			dc.add(Restrictions.like("emp.username", username,	MatchMode.ANYWHERE));
		}
	
		if(null != beginTime || null != endTime){
			beginTime = beginTime + " 00:00:00";
			endTime = endTime + " 23:59:59";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		    dc.add(Restrictions.ge("dealTime",sdf.parse(beginTime)));
		    dc.add(Restrictions.le("dealTime",sdf.parse(endTime)));
		}
		
		if("ASC".equalsIgnoreCase(dir)) {
			if(!"username".equals(sort) && !"orgname".equals(sort) && !"id".equals(sort)){
				dc.addOrder(Order.asc(sort));
			}else if("orgname".equals(sort)){
				dc.createAlias("sysOrganization", "org");
				dc.addOrder(Order.asc("org.orgname"));
			}
		}else {
			if(!"username".equals(sort) && !"orgname".equals(sort) && !"id".equals(sort)){
				dc.addOrder(Order.desc(sort));
			}else if("orgname".equals(sort)){
				dc.createAlias("sysOrganization", "org");
				dc.addOrder(Order.desc("org.orgname"));
			}
		}
		
		if("dealTime".equals(sort) && "asc".equals(dir)){
			dc.addOrder(Order.asc("dealTime"));
		}else{
			dc.addOrder(Order.desc("dealTime"));
		}
		
		return o_dealLogDAO.pagedQuery(dc, page);
	}
}
