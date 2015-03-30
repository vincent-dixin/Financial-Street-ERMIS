/**
 * IDealLogBO.java
 * com.fhd.sys.interfaces
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-23 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * IDealLogBO.java
 * com.fhd.sys.interfaces
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-23        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
*/


package com.fhd.sys.interfaces;

import com.fhd.core.dao.Page;
import com.fhd.sys.entity.st.PlanDealLog;

/**
 * 计划任务日志接口
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-23		下午05:05:12
 *
 * @see 	 
 */
public interface IPlanDealLogBO {

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
	public Page<PlanDealLog> findPlanBySome(String name, Page<PlanDealLog> page, String sort, String dir, String planEmpId);
}