/*
 * 北京第一会达风险管理有限公司 版权所有 2013
 * Copyright(C) 2013 Firsthuida Co.,Ltd. All rights reserved. 
 */
package com.fhd.wp.business;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.fdc.utils.UserContext;
import com.fhd.wp.dao.MilestoneDAO;
import com.fhd.wp.entity.Milestone;

/**
 * 里程碑业务类
 *
 * @author   胡迪新
 * @since    fhd Ver 4.5
 * @Date	 2013-2-21  下午2:47:09
 *
 * @see 	 
 */
@SuppressWarnings("unchecked")
@Service
public class MilestionBO {

	@Autowired
	private MilestoneDAO o_milestoneDAO;

	/*
	 * 添加
	 */

	/*
	 * 修改
	 */

	/*
	 * 删除
	 */
	
	/**
	 * 
	 * <pre>
	 * 根据工作计划ID 删除里程碑
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param workPlanId 工作计划id
	 * @since  fhd　Ver 1.1
	 */
	@Transactional
	public void removeMilestoneByWorkPlanId(String workPlanId) {
		o_milestoneDAO.batchExecute("delete Milestone m where m.plan.id = ?", workPlanId);
	}
	
	
	/*
	 * 查询
	 */
	
	/**
	 * <pre>
	 * 根据工作计划id查询里程碑
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param workPlanId 工作计划id
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public List<Milestone> findMilestoneByWorkPlanId(String workPlanId) {
		
		Criteria criteria = o_milestoneDAO.createCriteria();
		criteria.add(Restrictions.eq("plan.id", workPlanId));
		
		return criteria.list();
		
	}


	/**
	 * <pre>
	 * 查询在计划执行中用到的里程碑列表 
	 * 
	 * select p.id,p.NAME,m.id, m.NAME, de.DICT_ENTRY_NAME,m.EDESC, m.FINISH_DATE,o.id, o.ORG_NAME,mi.SUB_PLAN_ID ,mi.ESTATUS, mi.FINISH_CONDITION from t_wp_plan p 
		inner join t_wp_enforcement_org eo
		on p.ID = eo.WORK_PLAN_ID
		inner join t_sys_organization o
		on eo.ORG_ID = o.ID
		inner join t_wp_milestone m
		on p.ID = m.WORK_PLAN_ID
		inner join t_sys_dict_entry de
		on m.NAME = de.ID
		left join T_WP_MILESTONE_RE_IMPROVEMENT mi
		on m.ID = mi.MILESTONE_ID and o.ID = mi.ORG_ID
		where p.NAME = '测试005'
		and o.id = '50000'
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param workPlanId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	
	public List<Object[]> findMilestoneForExecuteByWorkPlanId(String workPlanId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select p.id pid,p.NAME pname,m.id mid, m.NAME mname, de.DICT_ENTRY_NAME,m.EDESC, m.FINISH_DATE,o.id oid, o.ORG_NAME,mi.SUB_PLAN_ID ,mi.ESTATUS,mi.FINISH_CONDITION from t_wp_plan p ")
			.append("inner join t_wp_enforcement_org eo on p.ID = eo.WORK_PLAN_ID ")
			.append("inner join t_sys_organization o on eo.ORG_ID = o.ID ")
			.append("inner join t_wp_milestone m on p.ID = m.WORK_PLAN_ID ")
			.append("inner join t_sys_dict_entry de on m.NAME = de.ID ")
			.append("left join T_WP_MILESTONE_RE_IMPROVEMENT mi ")
			.append("on m.ID = mi.MILESTONE_ID and o.ID = mi.ORG_ID ")
			.append("where p.id = ? and o.ID = ?");
		
		SQLQuery query = o_milestoneDAO.createSQLQuery(sql.toString() , workPlanId, UserContext.getUser().getCompanyid());
		return query.list();
		
	}
	
	/**
	 * <pre>
	 * 
	 * 查询在计划驾驶舱中使用的里程碑列表
	 * select mi.ESTATUS ,count(mi.ESTATUS)
	  from t_wp_plan p 
			inner join t_wp_enforcement_org eo
			on p.ID = eo.WORK_PLAN_ID
			inner join t_sys_organization o
			on eo.ORG_ID = o.ID
			inner join t_wp_milestone m
			on p.ID = m.WORK_PLAN_ID
			inner join t_sys_dict_entry de
			on m.NAME = de.ID
			left join T_WP_MILESTONE_RE_IMPROVEMENT mi
			on m.ID = mi.MILESTONE_ID and o.ID = mi.ORG_ID
			where p.NAME = 'jjj'
	    group by mi.ESTATUS
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param workPlanId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	
	public List<Object[]> findMilestoneForDashboardByWorkPlanId(String workPlanId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select mi.ESTATUS ,count(mi.ESTATUS) from t_wp_plan p ")
			.append("inner join t_wp_enforcement_org eo on p.ID = eo.WORK_PLAN_ID ")
			.append("inner join t_sys_organization o on eo.ORG_ID = o.ID ")
			.append("inner join t_wp_milestone m on p.ID = m.WORK_PLAN_ID ")
			.append("inner join t_sys_dict_entry de on m.NAME = de.ID ")
			.append("left join T_WP_MILESTONE_RE_IMPROVEMENT mi ")
			.append("on m.ID = mi.MILESTONE_ID and o.ID = mi.ORG_ID ")
			.append("where p.id = ? ")
			.append("group by mi.ESTATUS");
		
		SQLQuery query = o_milestoneDAO.createSQLQuery(sql.toString() , workPlanId);
		return query.list();
		
	}
}

