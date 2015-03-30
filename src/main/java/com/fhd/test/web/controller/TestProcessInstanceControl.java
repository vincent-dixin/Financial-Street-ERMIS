
package com.fhd.test.web.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.core.dao.Page;
import com.fhd.core.utils.DateUtils;
import com.fhd.core.utils.Identities;
import com.fhd.fdc.utils.UserContext;
import com.fhd.test.business.TestProcessInstanceBO;
import com.fhd.test.entity.TestProcessInstance;

@Controller
@RequestMapping("test")
public class TestProcessInstanceControl {
	
	@Autowired
	private TestProcessInstanceBO o_testProcessInstanceBO;
	
	@ResponseBody
	@RequestMapping(value = "testProcessInstanceList.f")
	public Map<String, Object> testProcessInstanceList(int start,int limit,String query) throws Exception {
		Page<TestProcessInstance> page = new Page<TestProcessInstance>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		page = o_testProcessInstanceBO.findPageBySome(page,query, "name","ASC");
		List<TestProcessInstance> entityList = page.getResult();
		List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
		for(TestProcessInstance de : entityList){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("id", de.getId());
			map.put("name", de.getName());
			datas.add(map);
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);
		return map;
	}
	
	@RequestMapping(value = "submitA.f")
	public void submitTestProcessInstance(HttpServletResponse response) throws Exception {
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		TestProcessInstance testProcessInstance = new TestProcessInstance();
		testProcessInstance.setId(Identities.uuid());
		String name = DateUtils.formatDate(new Date(), "yyyyMMdd_hhmmss");
		testProcessInstance.setName(name);
		try{
			o_testProcessInstanceBO.submitA(testProcessInstance, UserContext.getUser().getEmpid());
			flag = "true";
			out.write(flag);
		} finally {
			out.close();
		}
	}
}

