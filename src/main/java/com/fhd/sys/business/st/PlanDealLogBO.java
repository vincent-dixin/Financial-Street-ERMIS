/**
 * DealLogBO.java
 * com.fhd.sys.business.st
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-23 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * DealLogBO.java
 * com.fhd.sys.business.st
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-23        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
*/


package com.fhd.sys.business.st;


import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhd.core.dao.Page;
import com.fhd.sys.dao.st.PlanDealLogDAO;
import com.fhd.sys.entity.st.PlanDealLog;
import com.fhd.sys.interfaces.IPlanDealLogBO;

/**
 * 计划任务日志接口实现
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-23		下午05:06:20
 *
 * @see 	 
 */
@Service
public class PlanDealLogBO  implements IPlanDealLogBO {
	@Autowired
	private PlanDealLogDAO o_dealLogDAO;
	
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
	public Page<PlanDealLog> findPlanBySome(String name, Page<PlanDealLog> page, String sort, String dir, String planEmpId) {
		DetachedCriteria dc = DetachedCriteria.forClass(PlanDealLog.class);
		
		if(StringUtils.isNotBlank(name)){
			dc.createAlias("planEmp", "pe").createAlias("pe.plan", "p").
			add(Property.forName("p.name").like(name,MatchMode.ANYWHERE));
		}else{
			dc.add(Property.forName("planEmp.id").eq(planEmpId));
		}
		
		if("ASC".equalsIgnoreCase(dir)) {
			dc.addOrder(Order.asc(sort));
		} else {
			dc.addOrder(Order.desc(sort));
		}
		
		return o_dealLogDAO.findPage(dc, page, false);
	}
}