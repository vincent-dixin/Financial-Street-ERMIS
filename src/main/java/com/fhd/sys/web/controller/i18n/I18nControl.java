/**
 * I18nControl.java
 * com.fhd.sys.web.controller.i18n
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-20 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * I18nControl.java
 * com.fhd.sys.web.controller.i18n
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-20        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
*/


package com.fhd.sys.web.controller.i18n;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhd.sys.business.i18n.I18nBO;

/**
 * 国际化控制分发
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-20		上午10:16:22
 *
 * @see 	 
 */
@Controller
public class I18nControl {
	@Autowired
    private I18nBO o_i18nBO;
	
	/**
	 * GRID保存
	 * 
	 * @author 金鹏祥
	 * @param modifiedRecord 条件字符串
	 * @param response 
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping(value = "/sys/i18n/saveI18n.f")
	public void i18nSave(String modifiedRecord,HttpServletResponse response) throws Exception {
		PrintWriter out = null;
		String flag="false";
		boolean isBoolean = false;
		try{
			out=response.getWriter();
			isBoolean = o_i18nBO.saveI18n(modifiedRecord);
			if(isBoolean)
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
	 * GRID删除
	 * 
	 * @author 金鹏祥
	 * @param ids
	 * @param response
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping(value = "sys/i18n/deleteI18n.f")
	public void deleteI18n(String ids,HttpServletResponse response) throws Exception {
		PrintWriter out = null;
		String flag="false";
		boolean isBoolean = false;
		try{
			out=response.getWriter();
			isBoolean = o_i18nBO.deleteI18n(ids);
			if(isBoolean)
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
	 * 初始化树,查询树
	 * 
	 * @author 金鹏祥
	 * @param request
	 * @param query 查询条件
	 * @return Map<String, Object>
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
    @RequestMapping(value = "/sys/i18n/findI18nAll.f")
    public Map<String, Object> findI18nAll(HttpServletRequest request, String query) {
		return o_i18nBO.findI18nAll(query);
    }
	
	/**
	 * 初始化Grid
	 * 
	 * @author 金鹏祥
	 * @param start 开始页码
	 * @param limit 分页码
	 * @param query 查询条件
	 * @param sort 排序字段
	 * @param request
	 * @return Map<String, Object>
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@ResponseBody
	@RequestMapping(value = "/sys/i18n/findI18nPage.f")
	public Map<String, Object> findI18nPage(int start, int limit, String query, String sort, String objectType ,HttpServletRequest request) throws Exception {
		return o_i18nBO.findI18nPage(start, limit, query, sort, objectType);
	}
}