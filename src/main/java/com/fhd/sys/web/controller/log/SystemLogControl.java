/**
 * SystemLogControl.java
 * com.fhd.fdc.commons.web.controller.sys.log
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-27 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.controller.log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fhd.core.dao.support.Page;
import com.fhd.sys.business.log.SystemLogBO;
import com.fhd.sys.entity.log.SystemLog;
import com.fhd.sys.web.form.log.SystemLogForm;

/**
 * 系统日志Controller类.
 * @author  wudefu
 * @version V1.0  创建时间：2010-8-27
 * Company FirstHuiDa.
 */

@Controller
@SessionAttributes(types =SystemLogForm.class)
public class SystemLogControl {
	
	@Autowired
	private SystemLogBO o_systemLogBO;
	
	/**
	 * 查询所有系统日志.
	 * @author 吴德福
	 * @param model
	 * @return String 跳转到systemLogList.jsp页面.
	 * @since  fhd　Ver 1.1
	 */
/*	@RequestMapping(value = "/sys/log/systemLogList.do")
	public String queryBusinessLog(Model model,HttpServletRequest request,String success){
		Page<SystemLog> page = new Page<SystemLog>(request,"o",10);
		page = o_systemLogBO.queryAllSystemLog(page);
		model.addAttribute("systemLogForm", new SystemLogForm());
		model.addAttribute("systemLogList", page.getList());
		model.addAttribute("size", page.getFullListSize());
		model.addAttribute("success", success);
		return "sys/log/systemLogList";
	}*/
	/**
	 * 王龙 4.20
	 * 进入日志系统
	 */
	
	@RequestMapping(value = "/sys/log/systemLogList.do")
	public String queryBusinessLog(Model model, HttpServletRequest request,
			String success) {

		model.addAttribute("SystemLogForm", new SystemLogForm());
		return "sys/log/systemLogList";
	}
    /**
     * 王龙4.20
     *  查询所有系统日志  
     * @param start
     * @param limit
     * @param model
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
	
	@ResponseBody
	@RequestMapping(value = "/sys/log/systemLogAllList.do")
	public Map<String, Object> querySystemAllLog(int start, int limit,Date startTimes, Date endTimes,
			Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Page<SystemLog> page = new Page<SystemLog>();
		page.setPageNumber((limit == 0 ? 0 : start / limit)+1);
		page.setObjectsPerPage(limit);

		page = o_systemLogBO.querySystemAllLog(page, startTimes, endTimes);
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		for (SystemLog sl : page.getList()) {
			Map<String, Object> row = new HashMap<String, Object>();

			row.put("logDate", sl.getLogDate());
			row.put("logLevel", sl.getLogLevel());
			row.put("location", sl.getLocation());
			row.put("message", sl.getMessage());
			datas.add(row);
		}
		map.put("totalCount", page.getFullListSize());
		map.put("datas", datas);
		return map;

	}
	
	
	
	/**
	 * 删除系统日志.
	 * @author 吴德福
	 * @param id
	 * @return String
	 * @since  fhd　Ver 1.1
	 */
	/*
	@RequestMapping(value = "/sys/log/systemLogDel.do")
	public String removeSystemLog(Model model,HttpServletRequest request,SystemLogForm systemLogForm,BindingResult result){
		boolean flag = o_systemLogBO.deleteSystemLog(systemLogForm);
		return "redirect:/sys/log/systemLogList.do?success=" + (flag ? 1 : 0);
	}*/
	/**
	 * 根据查询条件查询系统日志.
	 * @author 吴德福
	 * @param model
	 * @param request
	 * @return String 跳转到systemLogList.jsp页面.
	 * @since  fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/log/systemLogByCondition.do")
	public String queryBusinessLogByCondition(Model model,HttpServletRequest request,SystemLogForm systemLogForm,BindingResult result){
		List<SystemLog> systemLogList = o_systemLogBO.findSystemLogListBySome(systemLogForm);
		model.addAttribute("systemLogList", systemLogList);
		model.addAttribute("size", systemLogList.size());
		return "sys/log/systemLogList";
	}	
}

