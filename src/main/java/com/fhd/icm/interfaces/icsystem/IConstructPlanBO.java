/**
 * IAssessBO.java
 * com.fhd.icm.interfaces.assess
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-1-6 		刘中帅
 *
 * Copyright (c) 2013, Firsthuida All Rights Reserved.
*/

package com.fhd.icm.interfaces.icsystem;

import com.fhd.core.dao.Page;
import com.fhd.icm.entity.icsystem.ConstructPlan;


/**
 * @author   宋佳
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-4-2		下午  14 : 07
 * 
 * @see 	 
 */
public  interface IConstructPlanBO {

	/**
	 * 根据查询条件查找建设计划
	 * @author 宋佳
	 * @modify 
	 * @param page
	 * @param sort
	 * @param dir
	 * @param query 查询条件
	 * @return Page<AssessPlan>
	 * @since fhd　Ver 1.1  
	 */
	public Page<ConstructPlan> findConstructPlansListByPage(Page<ConstructPlan> page, String sort, String dir, String query);
	
	/**
	 *  
	 */
	
}

