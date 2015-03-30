package com.fhd.sys.web.controller.sysapp;

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
import com.fhd.sys.business.sysapp.DealLogBO;

import com.fhd.sys.entity.sysapp.DealLog;

import com.fhd.sys.web.form.sysapp.DealLogForm;



/**
 * 计划任务--处理记录Controller类.
 * 
 * @author weilunkai
 * @version V1.0 创建时间：2012-5-23 Company FirstHuiDa.
 */
@Controller
@SessionAttributes(types = DealLogForm.class)
public class DealLogControl {
	@Autowired
	private DealLogBO o_dealLogBO;
	
	/**
	 * 查询所有计划任务--处理记录.
	 * 
	 * @author weilunkai
	 * @param model
	 * @return String 跳转到dealLogList.jsp页面.
	 * @since fhd　Ver 1.1
	 */
	@RequestMapping(value = "/sys/sysapp/dealLogList.do")
	public String queryBusinessLog(Model model, HttpServletRequest request,
			String success) {
		model.addAttribute("dealLogForm", new DealLogForm());
		
		return "sys/sysapp/dealLogList";
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
	@RequestMapping(value = "/sys/sysapp/dealLogALLList.do")
	public Map<String, Object> businessAllLog(Model model, int start,int limit,String sort,String dir,
			String startTimes, String endTimes,String userName,
			HttpServletRequest request, HttpServletResponse response)throws Exception {
	
		Map<String, Object> remap = new HashMap<String, Object>();
		Page<DealLog> page = new Page<DealLog>();
		page.setPageNumber((limit == 0 ? 0 : start / limit)+1);
		page.setObjectsPerPage(limit);

		o_dealLogBO.queryAllDealLog(page, userName, startTimes,endTimes,sort,dir);
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		
		String str1="可用";
		String str2="不可用";
		
		for (DealLog dealLog : page.getList()) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("id", dealLog.getId());
			//row.put("username", dealLog.getEmp().getTask().getCreateBy().getRealname());
			row.put("username", dealLog.getEmp().getEmp().getUsername());
			row.put("planname", dealLog.getEmp().getTask().getPlanName());
			row.put("dealMeasure", dealLog.getDealMeasure().getName());
			row.put("dealTime",DateUtils.formatDate(dealLog.getDealTime(), "yyyy年MM月dd日"));
			
			String eStatus=dealLog.geteStatus();
			
			if(eStatus.equals("1")){
				row.put("eStatus", str1);
			}else{
				row.put("eStatus", str2);
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
	 * 删除计划任务--处理记录.
	 * weilunkai
	 */

	@RequestMapping(value = "/sys/sysapp/dealLogDel.do")
	public String removeBusinessLog(String ids, HttpServletResponse response,
			HttpServletRequest resquest) throws Exception {
		PrintWriter out = null;
		//String flag = "true";
		out = response.getWriter();
		DealLog dealLog=new DealLog();
		try {
			String[] logs = ids.split(",");
			for (String dealLogId : logs) {
				if (StringUtils.isNotBlank(dealLogId))
					//dealLog.setId(dealLogId);
					dealLog=o_dealLogBO.queryDealLogById(dealLogId);
					dealLog.seteStatus("0");
					o_dealLogBO.saveDealLog(dealLog);
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
