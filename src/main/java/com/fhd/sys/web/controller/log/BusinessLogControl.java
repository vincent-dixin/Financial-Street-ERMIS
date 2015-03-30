/**
 * BusinessLogControl.java
 * com.fhd.fdc.commons.web.controller.sys.log
 *
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-27 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
 */

package com.fhd.sys.web.controller.log;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fhd.core.dao.support.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.sys.business.log.BusinessLogBO;
import com.fhd.sys.entity.log.BusinessLog;
import com.fhd.sys.web.form.log.BusinessLogForm;

/**
 * 业务日志Controller类.
 * 
 * @author wudefu
 * @version V1.0 创建时间：2010-8-27 Company FirstHuiDa.
 */

@Controller
@SessionAttributes(types = BusinessLogForm.class)
public class BusinessLogControl {

	@Autowired
	private BusinessLogBO o_businessLogBO;

	/**
	 * 查询所有业务日志.
	 * 
	 * @author 吴德福
	 * @param model
	 * @return String 跳转到businessLogList.jsp页面.
	 * @since fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/log/businessLogList.do")
	public String queryBusinessLog(Model model, HttpServletRequest request,
			String success) {
		/*
		 * Page<BusinessLog> page = new Page<BusinessLog>(request,"o",10);
		 * SysEmployee employee =
		 * o_employeeBO.getEmployee(UserContext.getUserid()); if(null !=
		 * employee){ SysOrganization org = employee.getSysOrganization(); page
		 * = o_businessLogBO.queryAllBusinessLogByUser(page,org.getId()); }else{
		 * page = o_businessLogBO.queryAllBusinessLog(page); }
		 * model.addAttribute("businessLogForm", new BusinessLogForm());
		 * model.addAttribute("businessLogList", page.getList());
		 * model.addAttribute("size", page.getFullListSize());
		 * model.addAttribute("success", success);
		 */
		model.addAttribute("businessLogForm", new BusinessLogForm());
		return "sys/log/businessLogList";
	}

	/**
	 * 查询所有业务日志
	 *  @author 王龙  4.15
	 * @param model
	 * @param start
	 * @param limit
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/log/businessLogALLList.do")
	public Map<String, Object> businessAllLog(Model model, int start,int limit,String sort,String dir,
			String startTimes, String endTimes,String userName,
			HttpServletRequest request, HttpServletResponse response)throws Exception {
		Map<String, Object> remap = new HashMap<String, Object>();
		Page<BusinessLog> page = new Page<BusinessLog>();
		page.setPageNumber((limit == 0 ? 0 : start / limit)+1);
		page.setObjectsPerPage(limit);

		o_businessLogBO.queryAllBusinessLog(page, userName, startTimes,endTimes,sort,dir);
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		
		for (BusinessLog bl : page.getList()) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("id", bl.getId());
			row.put("username", bl.getSysUser().getUsername());
			row.put("operateTime",DateUtils.formatDate(bl.getOperateTime(), "yyyy年MM月dd日"));
			row.put("operateType", bl.getOperateType());
			row.put("moduleName", bl.getModuleName());
			row.put("isSuccess", bl.getIsSuccess());
			if(StringUtils.isNotBlank(bl.getOperateRecord())){
				row.put("operateRecord", bl.getOperateRecord());
			}else{
				row.put("operateRecord", "");
			}
			if (null != bl.getSysOrganization()) {
				row.put("orgname", bl.getSysOrganization().getOrgname());
			}
			datas.add(row);
		}
      //  remap.put("total", page.getFullListSize());
		remap.put("totalCount", page.getFullListSize());
		remap.put("datas", datas);
		remap.put("success", true);

		return remap;
	}

	/**
	 * 删除业务日志.
	 * 
	 * @author 吴德福
	 * @param id
	 * @return String
	 * @since fhd　Ver 1.1
	 */
	/*
	 * @RequestMapping(value = "/sys/log/businessLogDel.do") public String
	 * removeBusinessLog(String[] id){ boolean flag = false; for(String
	 * businessLogId : id){ if(!flag){
	 * o_businessLogBO.removeBusinessLog(businessLogId); } }
	 * 
	 * return "redirect:/sys/log/businessLogList.do?success=" + (flag ? 0 : 1);
	 * }
	 */

	/**
	 * 删除业务日志.
	 * 王龙 4.15
	 */

	@RequestMapping(value = "/sys/log/businessLogDel.do")
	public String removeBusinessLog(String ids, HttpServletResponse response,
			HttpServletRequest resquest) throws Exception {
		PrintWriter out = null;
		//String flag = "true";
		out = response.getWriter();
		try {
			String[] logs = ids.split(",");
			for (String businessLogId : logs) {
				if (StringUtils.isNotBlank(businessLogId))
					o_businessLogBO.removeBusinessLog(businessLogId);
				//flag = "true";
				//out.write(flag);
				//out.write("true");
			}

			out.write("true");
		} catch (Exception e) {
			e.printStackTrace();
			out.write("false");
		} finally {
			out.close();
		}
		return null;
	}
}
