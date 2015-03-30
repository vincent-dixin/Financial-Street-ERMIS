/**
 * SysAuthorityControl.java
 * com.fhd.fdc.commons.web.controller.sys.auth
 *
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-30 		吴德福
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.web.controller.auth;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fhd.sys.business.auth.AuthorityTreeBO;
import com.fhd.sys.web.form.auth.SysAuthorityForm;

/**
 * ClassName:AuthorityControl
 *
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012	2012-8-31		上午10:45:15
 *
 * @see 	 
 */
@Controller
@SessionAttributes(types =SysAuthorityForm.class)
@RequestMapping("/sys/menu")
public class AuthorityControl {
	
	@Autowired
	private AuthorityTreeBO o_authorityBO;
	
	/**
	 * <pre>
	 * loadTopMenu:加载一级菜单
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param response
	 * @param request
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping(value = "findFirstAuthority.do")
	public void findFirstAuthority(HttpServletResponse response, HttpServletRequest request)throws Exception{
		PrintWriter out = null;
		out = response.getWriter();
		try{
			out.write("["+o_authorityBO.findFirstAuthority(request)+"]");
		} finally {
			out.close();
		}
	}
	
	/**
	 * <pre>
	 * loadMenuTrees:加载菜单树
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param id
	 * @param response
	 * @param request
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	@RequestMapping(value = "authorityTreeLoaderByParentId.do")
	public void authorityTreeLoaderByParentId(String id,HttpServletResponse response, HttpServletRequest request)throws Exception{
		PrintWriter out = null;
		out = response.getWriter();
		try{
			out.write(o_authorityBO.authorityTreeLoaderByParentId(id,request));
		} finally {
			out.close();
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "authorityTreeLoader.f")
	public void authorityTreeLoader(HttpServletResponse response,HttpServletRequest request) throws Exception {
		PrintWriter out = null;
		out = response.getWriter();
		try{
			out.write("["+o_authorityBO.authorityTreeLoader(request)+"]");
		} finally {
			out.close();
		}
	}

}

