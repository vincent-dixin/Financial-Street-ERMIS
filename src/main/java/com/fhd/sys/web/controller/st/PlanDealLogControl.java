/**
 * DealLogControl.java
 * com.fhd.sys.web.controller.st
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-23 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * DealLogControl.java
 * com.fhd.sys.web.controller.st
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-10-23        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
*/


package com.fhd.sys.web.controller.st;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.core.dao.Page;
import com.fhd.sys.business.st.PlanDealLogBO;
import com.fhd.sys.entity.st.PlanDealLog;
import com.fhd.sys.web.form.st.PlanDealLogForm;

/**
 * 任务计划日志控制分发
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-10-23		下午05:08:32
 *
 * @see 	 
 */
@Controller
public class PlanDealLogControl {
	@Autowired
	private PlanDealLogBO o_dealLogBO;
	
	/**
	 * 查询分页
	 * @author 金鹏祥
	 * @param start 开始条数
	 * @param limit 结束条数
	 * @param query 查询条件
	 * @return Map<String, Object>
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping(value = "/sys/st/queryPlanDealLogPage.f")
	public Map<String, Object> queryPlanPage(int start,int limit,String query, String planEmpId) throws Exception {
		Page<PlanDealLog> page = new Page<PlanDealLog>();
		
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		page = o_dealLogBO.findPlanBySome(query, page, "planEmp.id","ASC", planEmpId);
		
		List<PlanDealLog> entityList = page.getResult();
		List<PlanDealLogForm> datas = new ArrayList<PlanDealLogForm>();
		for(PlanDealLog de : entityList){
			datas.add(new PlanDealLogForm(de));
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);
		return map;
	}
}

