package com.fhd.fdc.commons.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.GrantedAuthority;

import com.fhd.core.utils.encode.JsonBinder;
import com.fhd.core.utils.reflection.ConvertUtils;
import com.fhd.fdc.utils.UserContext;

/**
 * 
 * 获取当前用户权限转换成json
 * 放到页面中
 *
 * @author   胡迪新
 * @since    fhd Ver 4.5
 * @Date	 2012-12-25  上午10:56:45
 *
 * @see
 */
public class UserAuthoritiesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	@SuppressWarnings("unchecked")
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 获取当前用户的权限集合
		Collection<GrantedAuthority> authorities = UserContext.getUser().getAuthorities();
		
		// 提取anthority 属性
		List<String> convertAuthorities = ConvertUtils.convertElementPropertyToList(authorities, "authority");
		// 转换成json格式
		JsonBinder jsonBinder = JsonBinder.buildNonNullBinder();
		String jsonAuthorities = "GajaxAuth=" + jsonBinder.toJson(convertAuthorities);
		printResource(response, jsonAuthorities);
		System.out.println(jsonBinder.toJson(convertAuthorities));
		
	}
	
	private void printResource(HttpServletResponse response,
			String resourcesJson) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.print(resourcesJson);
		writer.flush();
		writer.close();
	}

}
