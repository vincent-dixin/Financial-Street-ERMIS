/**
 * ValidateMacUsernamePasswordAuthenticationFilter.java
 * com.fhd.fdc.commons.security
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2011-7-28 		胡迪新
 *
 * Copyright (c) 2011, Firsthuida All Rights Reserved.
*/

package com.fhd.fdc.commons.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.ContextLoader;

import com.fhd.core.utils.PropertyUtils;
import com.fhd.sys.business.auth.SysUserBO;
import com.fhd.sys.entity.auth.SysUser;

/**
 * ClassName:ValidateMacUsernamePasswordAuthenticationFilter
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2011-7-28		下午03:35:46
 *
 * @see 	 
 */
public class ValidateMacUsernamePasswordAuthenticationFilter extends
		UsernamePasswordAuthenticationFilter {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(ValidateMacUsernamePasswordAuthenticationFilter.class);

	private static final String IP_PARAM = "j_ip";
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		Properties properties = null;
		try {
			properties = PropertyUtils.loadProperties("application.properties");
		} catch (IOException e) {
			logger.error("attemptAuthentication(HttpServletRequest, HttpServletResponse)", e); //$NON-NLS-1$
		}
		Boolean mac_check = Boolean.valueOf(properties.getProperty("mac.check"));
		if(mac_check) {
			if(!checkValidateMac(request)) {
				throw new AuthenticationServiceException("MAC地址不匹配");
			}
		}
		
		return super.attemptAuthentication(request, response);
	}
	
	public Boolean checkValidateMac(HttpServletRequest request) {
		// 验证mac地址是否相同标志位
		Boolean flag = Boolean.FALSE;
		
		// 获得远程mac地址
		String username = obtainUsername(request);
		String ip = obtainIp(request);
		String remote_mac = getMAC(ip);
		
		// 获得登录人员mac地址
		SysUserBO sysUserBO = ContextLoader.getCurrentWebApplicationContext().getBean(SysUserBO.class);
		SysUser sysUser = sysUserBO.getByUsername(username);
		if(null != sysUser){
			if(StringUtils.isNotBlank(sysUser.getMac())){
				String user_mac = sysUser.getMac();
				if (remote_mac.equalsIgnoreCase(user_mac)) {
					flag = Boolean.TRUE;
				}
			}
		}
		return flag;
	}
	
	// 从request对象中取得ip
	public String obtainIp(HttpServletRequest request) {
		
		return request.getParameter(IP_PARAM);
	}
	
	// 执行脚本程序 取得客户端的mac地址
	public String getMAC(String ip) {
		String str = "";
		String macAddress = "";
		try {
			Process p = Runtime.getRuntime().exec(
					"nbtstat -A " + ip);
			InputStreamReader ir = new InputStreamReader(p.getInputStream(),"GBK");
			LineNumberReader input = new LineNumberReader(ir);
			for (int i = 1; i < 100; i++) {
				str = input.readLine();
				if (str != null) {
					if (str.indexOf("MAC") > 1) {
						macAddress = str.substring(
								str.length() - 17, str.length());
						break;
					}
				}
			}
		} catch (IOException e) {
			logger.error("getMAC(String)", e); //$NON-NLS-1$
		}
		return macAddress;
	}
}

