/**
 * PlanEmpBO.java
 * com.fhd.sys.business.st
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-17 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * PlanEmpBO.java
 * com.fhd.sys.business.st
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-17        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
*/


package com.fhd.sys.business.st;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhd.core.utils.Identities;
import com.fhd.sys.dao.st.PlanEmpDAO;
import com.fhd.sys.entity.st.Plan;
import com.fhd.sys.entity.st.PlanEmp;
import com.fhd.sys.entity.st.Temp;
import com.fhd.sys.interfaces.IPlanEmpBO;

/**
 * 计划任务、模版关联业务处理
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-17		上午11:47:40
 *
 * @see 	 
 */
@Service
public class PlanEmpBO implements IPlanEmpBO{

	@Autowired
	private PlanEmpDAO o_planEmpDAO;
	
	/**
	 * 保存计划任务实体
	 * 
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void savePlanEmp(Plan plan, Temp temp) {
		PlanEmp planEmp = new PlanEmp();		
		planEmp.setId(Identities.uuid());
		planEmp.setPlan(plan);
		planEmp.setTemp(temp);
		o_planEmpDAO.merge(planEmp);
	}
	
	/**
	 * 
	 * 查询所有模版任务关联MAP
	 * 
	 * @author 金鹏祥
	 * @since fhd　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, PlanEmp> findPlanEmpAllMap() {
		HashMap<String, PlanEmp> map = new HashMap<String, PlanEmp>();
		Criteria c = o_planEmpDAO.createCriteria();
		List<PlanEmp> list = null;
		list = c.list();
		for (PlanEmp planEmp : list) {
			map.put(planEmp.getId(), planEmp);
		}
		return map;
	}
	
	/**
	 * 
	 * 删除
	 * 
	 * @author 金鹏祥
	 * @param planEmp 实体
	 * @since fhd　Ver 1.1
	 */
	@Transactional
	public boolean deletePlanEmpBO(PlanEmp planEmp) {
		try {
			o_planEmpDAO.delete(planEmp);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	
	/**
	 * 更新计划任务实体关联表
	 * 
	 * @author 金鹏祥
	 * @param plan
	 * @param temp
	 * @since  fhd　Ver 1.1
	*/
	@Transactional
	public void mergePlanEmp(Plan plan, Temp temp, String PlanEmpId) {
		PlanEmp planEmp = new PlanEmp();		
		planEmp.setId(PlanEmpId);
		planEmp.setPlan(plan);
		planEmp.setTemp(temp);
		o_planEmpDAO.merge(planEmp);
	}
	
	/**
	 * 根据任务计划id查询任务关联实体
	 * 
	 * @author 金鹏祥
	 * @param planId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@SuppressWarnings("unchecked")
	public PlanEmp findPlanEmpByPlanId(String planId) {
		Criteria c = o_planEmpDAO.createCriteria();
		List<PlanEmp> list = null;
		
		if (StringUtils.isNotBlank(planId)) {
			c.add(Restrictions.eq("plan.id", planId));
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
}