/**
 * IPlanEmpBO.java
 * com.fhd.sys.interfaces
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-16 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.interfaces;

import com.fhd.sys.entity.st.Plan;
import com.fhd.sys.entity.st.PlanEmp;
import com.fhd.sys.entity.st.Temp;

/**
 * 计划任务、模版关联接口
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-16		下午02:16:19
 *
 * @see 	 
 */
public interface IPlanEmpBO {
	/**
	 * 保存计划任务实体关联
	 * 
	 * @author 金鹏祥
	 * @since  fhd　Ver 1.1
	*/
	public abstract void savePlanEmp(Plan plan, Temp temp);

	/**
	 * 更新计划任务实体关联
	 * 
	 * @author 金鹏祥
	 * @param plan
	 * @param temp
	 * @param PlanEmpId
	 * @since  fhd　Ver 1.1
	*/
	public abstract void mergePlanEmp(Plan plan, Temp temp, String PlanEmpId);
	
	/**
	 * 根据任务计划id查询任务关联实体
	 * 
	 * @author 金鹏祥
	 * @param planId
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	public abstract PlanEmp findPlanEmpByPlanId(String planId);
}