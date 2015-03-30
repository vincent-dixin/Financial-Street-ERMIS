/**
 * SysEmployeeDAO.java
 * com.fhd.fdc.commons.dao.sys
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-7-29 		胡迪新
 *
 * Copyright (c) 2010, TNT All Rights Reserved.
*/

package com.fhd.sys.dao.orgstructure;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.fhd.core.dao.HibernateEntityDao;
import com.fhd.sys.entity.orgstructure.SysEmployee;

/**
 * 员工DAO类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-9-17
 * Company FirstHuiDa.
 */
@Repository
@SuppressWarnings("unchecked")
public class SysEmployeeDAOold extends HibernateEntityDao<SysEmployee> {
	
	public SysEmployee getSysEmployeeByUserId(String userId){
		String hql="select e from SysEmployee e where e.userid='"+userId+"'";
		SysEmployee sysEmployee = (SysEmployee)this.getSession().createQuery(hql).uniqueResult();
		return sysEmployee;
		
		
	}
	
	public List<String> getEmailById(String[] ids){
		String hql="select e.oemail from SysEmployee e where e.id in (:ids)";
		Query query = this.getSession().createQuery(hql);
		query.setParameterList("ids", ids);
		return query.list();
		
	}

	public List<SysEmployee> getEmpByAuditPlan(String id){
		String hql="select emp from SysEmployee emp where emp id in(select oper.empId from AuditPlanRelaOperator oper where oper.auditPlan.id='"+id.trim()+"')";
		return this.getSession().createQuery(hql).list();
	}
}

