/**
 * TestMvcControl.java
 * com.fhd.risk.web.controller
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-8-2 		张 雷
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.test.web.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.core.dao.Page;
import com.fhd.core.utils.Identities;
import com.fhd.test.business.TestMvcBO;
import com.fhd.test.entity.TestMvc;
import com.fhd.test.web.form.TestMvcForm;

/**
 * ClassName:TestMvcControl
 *
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-8-2		上午10:58:00
 *
 * @see 	 
 */
@Controller
@RequestMapping("test")
public class TestMvcControl {
	
	@Autowired
	private TestMvcBO o_testMvcBO;
	
	/**
	 * <pre>
	 * addTestMvc:新增
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param data
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping(value = "addTestMvc.f")
	public void addTestMvc(TestMvcForm data,HttpServletResponse response) throws Exception {
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		TestMvc entity = new TestMvc(Identities.uuid());
		BeanUtils.copyProperties(data, entity, new String[]{"parent","id"});
		if(StringUtils.isNotBlank(data.getParentId()) && !"undefined".equals(data.getParentId())){
			entity.setParent(o_testMvcBO.get(data.getParentId()));
		}
		try{
			o_testMvcBO.update(entity);
			o_testMvcBO.setIdSeqAndLevel(entity);
			flag = "true";
			out.write(flag);
		} finally {
			out.close();
		}
	}
	/**
	 * <pre>
	 * updateTestMvc:更新
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param data
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping(value = "updateTestMvc.f")
	public void updateTestMvc(TestMvcForm data,HttpServletResponse response) throws Exception {
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		TestMvc entity = o_testMvcBO.get(data.getId());
		BeanUtils.copyProperties(data, entity, new String[]{"parent"});
		if(StringUtils.isNotBlank(data.getParentId()) && !"null".equals(data.getParentId())){
			entity.setParent(o_testMvcBO.get(data.getParentId()));
		}
		if(null != entity){
			try{
				o_testMvcBO.update(entity);
				o_testMvcBO.setIdSeqAndLevel(entity);
				flag = "true";
				out.write(flag);
			} finally {
				out.close();
			}
		}
	}
	
	/**
	 * <pre>
	 * updateTestMvcBatch:批量保存
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param request
	 * @param response
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping(value="updateTestMvcBatch.f")
	public void updateTestMvcBatch(String modifiedRecord,HttpServletResponse response) {
		PrintWriter out = null;
		String flag="false";
		try{
			out=response.getWriter();
			o_testMvcBO.updateBatch(modifiedRecord);
			flag="true";
			out.write(flag);
		}catch(Exception e){
			e.printStackTrace();
			out.write(flag);
		}finally{
			out.close();
		}
	}
	
	/**
	 * <pre>
	 * deleteTestmvcs:删除
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param ids
	 * @param response
	 * @param request
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping(value = "deleteTestMvcs.f")
	public void deleteTestMvcs(String ids,HttpServletResponse response) throws Exception {
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		if(StringUtils.isNotBlank(ids)){
			String[] idArray=ids.split(",");
			for(String id : idArray){
				List<TestMvc> entities = o_testMvcBO.queryTestMvcByParentId(id);
				if(null != entities && entities.size()>0){
					break;
				}
				try{
					o_testMvcBO.deleteById(id);
					flag = "true";
					out.write(flag);
				} finally {
					out.close();
				}
			}
		}
	} 
	/**
	 * <pre>
	 * loadTestMvcTree:加载Tree
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param parentId
	 * @param response
	 * @param request
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping(value = "loadTestMvcTree.f")
	public Map<String,Object> loadTestMvcTree(String query)throws Exception{

		try{
			if(query==null)
			{
			return o_testMvcBO.loadTestMvcTree();
			}else
			{
				return o_testMvcBO.loadTestMvcTree(query);
			}
		} finally {
		
		}
	}
	
	
	/**
	 * <pre>
	 * loadTestMvcTreeById:加载Tree
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param parentId
	 * @param response
	 * @param request
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping(value = "loadTestMvcTreeById.f")
	public Map<String,Object> loadTestMvcTreeById(String id,String query)throws Exception{

			return o_testMvcBO.loadTestMvcTreeById(id, query);


			
	}
	
	
	
	/**
	 * <pre>
	 * queryTestMvcList1:查询
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param start
	 * @param limit
	 * @param request
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping(value = "queryTestMvcList.f")
	public Map<String, Object> queryTestMvcList(int start,int limit,String query) throws Exception {
		Page<TestMvc> page = new Page<TestMvc>();
		page.setPageNo((limit == 0 ? 0 : start / limit) + 1);
		page.setPageSize(limit);
		page = o_testMvcBO.queryTestMvcByTitleAndNameAndMyLocaleAndPageAndSortAndDir(query, query, null, page, "title","ASC");
		List<TestMvc> entityList = page.getResult();
		List<TestMvcForm> datas = new ArrayList<TestMvcForm>();
		for(TestMvc de : entityList){
			datas.add(new TestMvcForm(de));
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", page.getTotalItems());
		map.put("datas", datas);
		return map;
	}
	/**
	 * <pre>
	 * checkTitle:title的唯一性验证
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param title
	 * @param id
	 * @param response
	 * @param request
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping(value = "checkTitle.f")
	public void checkTitle(String title,String id,HttpServletResponse response) throws Exception {
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		try{
			TestMvc tmp = o_testMvcBO.queryTestMvcByTitle(title,id);
			if(StringUtils.isBlank(tmp.getId())){
				flag = "true";
			}
			out.write(flag);
		} finally {
			out.close();
		}
	} 
	
	/**
	 * <pre>
	 * checkParent:parentId的合法性验证
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param parentId
	 * @param id
	 * @param response
	 * @param request
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping(value = "checkParent.f")
	public void checkParent(String parentId,String id,HttpServletResponse response) throws Exception {
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		try{
			Boolean isParentIdOK = o_testMvcBO.isParentIdOK(parentId,id);
			if(isParentIdOK){
				flag = "true";
			}
			out.write(flag);
		} finally {
			out.close();
		}
	} 
	/**
	 * <pre>
	 * getTestMvc:获得TestMvc
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param id
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping(value = "getTestMvc.f",method=RequestMethod.POST)
	public Map<String, Object> getTestMvc(String id){
		Map<String, Object> map = new HashMap<String, Object>();
		TestMvc test = o_testMvcBO.get(id);
		TestMvcForm form = new TestMvcForm(test);
		if(null != test.getParent())
			form.setParentId(test.getParent().getId());
		map.put("success", true);
		map.put("data", form);
		return map;
	}
	
	/**
	 * <pre>
	 * viewTestMvc:查看TestMvc
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param id
	 * @return
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping(value = "viewTestMvc.f",method=RequestMethod.POST)
	public TestMvcForm viewTestMvc(String id){
		TestMvc test = o_testMvcBO.get(id);
		TestMvcForm form = new TestMvcForm(test);
		if(null != test.getParent())
			form.setParentId(test.getParent().getId());
		return form;
	}
	
	@ResponseBody
	@RequestMapping(value = "queryTestMvcTree.f")
	public Map<String,Object> queryTestMvcTree(String query) throws Exception {
		return o_testMvcBO.queryTestMvcTree(query);
	}
	
}

