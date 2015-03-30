/**
 * IPlanBO.java
 * com.fhd.sys.interfaces
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-16 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * IPlanBO.java
 * com.fhd.sys.interfaces
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-16        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
*/


package com.fhd.sys.interfaces;

import java.util.List;

import org.hibernate.classic.Session;

import com.fhd.core.dao.Page;
import com.fhd.sys.entity.st.Plan;

/**
 * 计划任务接口
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-16		下午02:16:49
 *
 * @see 	 
 */

public interface IPlanBO {
	/**
	 * 保存Plan实体对象
	 * @author 金鹏祥
	 * @param plan
	 * @since  fhd　Ver 1.1
	*/
	public abstract void savePlan(Plan plan);
	
	/**
	 * 更新Plan实体对象
	 * @author 金鹏祥
	 * @param plan
	 * @since  fhd　Ver 1.1
	*/
	public abstract void mergePlan(Plan plan);
	
	/**
	 * 查询Plan实体对象
	 * @author 金鹏祥
	 * @param id Plan对象ID
	 * @return Plan
	 * @since  fhd　Ver 1.1
	*/
	public abstract Plan findPlanById(String id);
	
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
	public abstract Page<Plan> findPlanBySome(String name, Page<Plan> page,String sort,String dir, String deleteStatus);
	
	/**
	 * 获取DB中已存在的定时任务
	 * 
	 * @author 金鹏祥
	 * @param session DB-Session
	 * @return List<Plan>
	 * @since  fhd　Ver 1.1
	*/
	public List<Plan> findPlanBySome(Session session, String status, String deleteStatus);
}