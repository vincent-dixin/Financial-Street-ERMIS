/**
 * LoginSuccessHandler.java
 * com.fhd.fdc.commons.security
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2010-8-14 		胡迪新
 *
 * Copyright (c) 2010, Firsthuida All Rights Reserved.
*/

package com.fhd.fdc.commons.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.fhd.sys.business.auth.SysUserBO;
import com.fhd.sys.entity.auth.SysUser;

/**
 * 登录成功，记录登录时间.
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2010-8-14		下午07:34:01
 * @see 	 
 */
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private SysUserBO o_sysUserBO;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws ServletException, IOException {
		SysUser user = (SysUser) authentication.getPrincipal();
		user.setLastLoginTime(new Date());
		o_sysUserBO.updateUser(user);
		super.onAuthenticationSuccess(request, response, authentication);
		
	}
	
}

