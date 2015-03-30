/**
 * SystemLogBO.java
 * com.fhd.fdc.commons.business.log
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-9-3 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.business.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.support.Page;
import com.fhd.sys.dao.log.SystemLogDAO;
import com.fhd.sys.entity.log.SystemLog;
import com.fhd.sys.web.form.log.SystemLogForm;

/**
 * 系统日志BO类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-9-3
 * Company FirstHuiDa.
 */

@Service
@SuppressWarnings("unchecked")
public class SystemLogBO {
	
	@Autowired
	SystemLogDAO o_systemLogDAO;
	
	/**
	 * 删除指定时间段内的系统日志.
	 * @author 吴德福
	 * @param systemLogForm.
	 * @return boolean 删除是否成功.
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public boolean deleteSystemLog(SystemLogForm systemLogForm){
		boolean flag = false;
		StringBuilder hql = new StringBuilder();
		hql.append("delete From SystemLog Where 1=1 ");
		Date beginTime = null;
		Date endTime = null;
		if(systemLogForm != null && systemLogForm.getBeginTime() != null && systemLogForm.getEndTime() != null){
			beginTime = systemLogForm.getBeginTime();
			endTime = systemLogForm.getEndTime();
			
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			String beginDate = sf.format(beginTime);
			hql.append(" and logDate >= '");
			hql.append(beginDate);
			hql.append("'");
			
			String endDate = sf.format(endTime);
			hql.append(" and logDate <= '");
			hql.append(endDate);
			hql.append("'");
			
			o_systemLogDAO.createQuery(hql.toString()).executeUpdate();
			flag = true;
		}
		return flag;
	}
	
	public Page<SystemLog> queryAllSystemLog(Page<SystemLog> page){
		return o_systemLogDAO.pagedQuery(DetachedCriteria.forClass(SystemLog.class), page);
	}
	/**
	 * 查询所有日志 .
	 * 李佩2011-8-24修改
	 * 王龙 4.19 
	 * @param page
	 * @param beginTime
	 * @param endTime
	 * @return Page<SystemLog>
	 * @throws Exception
	 */
	public Page<SystemLog> querySystemAllLog(Page<SystemLog> page,Date beginTime,Date endTime) throws Exception{
		DetachedCriteria dc=DetachedCriteria.forClass(SystemLog.class);
		if(null != beginTime || null != endTime){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		    dc.add(Restrictions.ge("logDate", sdf.format(beginTime) + " 00:00:00"));
		    dc.add(Restrictions.le("logDate", sdf.format(endTime) + " 23:59:59"));
		}
		return o_systemLogDAO.pagedQuery(dc,page);
	}
	
	
	
	/**
	 * 查询指定时间段内的系统日志.
	 * @author 吴德福
	 * @param businessLogForm.
	 * @return List<SystemLog> 业务日志集合.
	 * @since  fhd　Ver 1.1
	 */
	public List<SystemLog> findSystemLogListBySome(SystemLogForm systemLogForm){
		StringBuilder hql = new StringBuilder();
		hql.append("From SystemLog Where 1=1 ");
		Date beginTime = null;
		Date endTime = null;
		if(systemLogForm != null && systemLogForm.getBeginTime() != null && systemLogForm.getEndTime() != null){
			beginTime = systemLogForm.getBeginTime();
			endTime = systemLogForm.getEndTime();
			
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			String beginDate = sf.format(beginTime);
			hql.append(" and logDate >= '");
			hql.append(beginDate);
			hql.append("'");
			
			String endDate = sf.format(endTime);
			hql.append(" and logDate <= '");
			hql.append(endDate);
			hql.append("'");
		}
		return o_systemLogDAO.find(hql.toString());
	}
}

