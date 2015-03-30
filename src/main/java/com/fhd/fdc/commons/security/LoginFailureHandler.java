/**
 * LoginFailureHandler.java
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.fhd.sys.business.auth.SysUserBO;
import com.fhd.sys.entity.auth.SysUser;

/**
 * 登录失败，记录失败次数.
 * @author 胡迪新
 * @version
 * @since Ver 1.1
 * @Date 2010-8-14 下午07:37:28
 * @see
 */
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Autowired
	private SysUserBO o_sysUserBO;

	public LoginFailureHandler() {
		super();
	}

	public LoginFailureHandler(String defaultFailureUrl) {
		super(defaultFailureUrl);
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		if (!(exception instanceof LockedException)) {
			if (null != exception.getExtraInformation()) {
				SysUser user = (SysUser) exception.getExtraInformation();
				Integer count = user.getErrCount();
				user.setErrCount(++count);
				if (3 <= count) {
					user.setLockstate(true);
				}
				o_sysUserBO.updateUser(user);
			}
		}
		super.onAuthenticationFailure(request, response, exception);
	}

}
