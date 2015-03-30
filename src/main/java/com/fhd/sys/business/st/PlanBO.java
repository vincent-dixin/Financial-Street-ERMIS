/**
 * PlanBO.java
 * com.fhd.sys.business.st
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-16 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * PlanBO.java
 * com.fhd.sys.business.st
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-16        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
*/


package com.fhd.sys.business.st;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.classic.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.dao.Page;
import com.fhd.sys.dao.st.PlanDAO;
import com.fhd.sys.entity.st.Plan;
import com.fhd.sys.interfaces.IPlanBO;

/**
 * 计划任务接口实现
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-16		下午02:50:01
 *
 * @see 	 
 */
@Service
public class PlanBO implements IPlanBO {
	@Autowired
	private PlanDAO o_planDAO;

	public PlanDAO getO_planDAO() {
		return o_planDAO;
	}

	public void setO_planDAO(PlanDAO o_planDAO) {
		this.o_planDAO = o_planDAO;
	}

	/**
	 * 保存Plan实体对象
	 * @author 金鹏祥
	 * @param plan
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void savePlan(Plan plan) {
		o_planDAO.merge(plan);
	}

	/**
	 * 更新Plan实体对象
	 * @author 金鹏祥
	 * @param plan
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergePlan(Plan plan) {
		o_planDAO.merge(plan);
	}

	/**
	 * 查询Plan实体对象
	 * @author 金鹏祥
	 * @param id Plan对象ID
	 * @return Plan
	 * @since  fhd　Ver 1.1
	*/
	@SuppressWarnings("unchecked")
	public Plan findPlanById(String id) {
		Criteria c = o_planDAO.createCriteria();
		List<Plan> list = null;
		
		if (StringUtils.isNotBlank(id)) {
			c.add(Restrictions.eq("id", id));
		} else {
			return null;
		}
		
		list = c.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * 查询所有任务MAP
	 * 
	 * @author 金鹏祥
	 * @since fhd　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Plan> findPlanAllMap() {
		HashMap<String, Plan> map = new HashMap<String, Plan>();
		Criteria c = o_planDAO.createCriteria();
		List<Plan> list = null;
		list = c.list();
		for (Plan plan : list) {
			map.put(plan.getCreateBy().getId(), plan);
		}
		return map;
	}
	
	/**
	 * 查询Plan实体对象分页
	 * @author 金鹏祥
	 * @param name Plan对象name
	 * @param page coreDAOPage
	 * @param sort 排序字段
	 * @param dir 排序方式
	 * @return Page<Plan>
	 * @since  fhd　Ver 1.1
	*/
	public Page<Plan> findPlanBySome(String name, Page<Plan> page, String sort, String dir, String deleteStatus) {
		DetachedCriteria dc = DetachedCriteria.forClass(Plan.class);
		dc.add(Restrictions.eq("deleteStatus", deleteStatus));
		if(StringUtils.isNotBlank(name)){
			dc.add(Property.forName("name").like(name,MatchMode.ANYWHERE));
		}
		
		if("ASC".equalsIgnoreCase(dir)) {
			dc.addOrder(Order.asc(sort));
		} else {
			dc.addOrder(Order.desc(sort));
		}
		
		return o_planDAO.findPage(dc, page, false);
	}
	
	/**
	 * 获取DB中已存在的定时任务
	 * 
	 * @author 金鹏祥
	 * @param session DB-Session
	 * @return List<Plan>
	 * @since  fhd　Ver 1.1
	*/
	@SuppressWarnings("unchecked")
	public List<Plan> findPlanBySome(Session session, String status, String deleteStatus) {
		try {
			Criteria c = session.createCriteria(Plan.class);
			List<Plan> list = null;
			c.add(Restrictions.eq("status", status));
			c.add(Restrictions.eq("deleteStatus", deleteStatus));
			list = c.list();
			if (list.size() > 0) {
				return list;
			} else {
				return null;
			}
		}finally{
			session.close();
		}
	}
}