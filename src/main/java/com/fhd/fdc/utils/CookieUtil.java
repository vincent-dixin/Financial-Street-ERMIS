/**
 * CookieUtil.java
 * com.fhd.fdc.utils
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-22 		金鹏祥
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/
/**
 * CookieUtil.java
 * com.fhd.fdc.utils
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-22        金鹏祥
 *
 * Copyright (c) 2012, FirstHuida All Rights Reserved.
*/


package com.fhd.fdc.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie操作工具类
 *
 * @author   金鹏祥
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-22		上午10:04:15
 *
 * @see 	 
 */
public class CookieUtil {
	/**
	 * 创建Cookie
	 * 
	 * @author 金鹏祥
	 * @param key 关键值
	 * @param value 对应值
	 * @param resp
	 * @return boolean
	 * @since  fhd　Ver 1.1
	*/
	public static boolean createCookie(String key,String value, HttpServletResponse resp){
		Cookie cookie = new Cookie(key, value) ;
		cookie.setPath("/");
		resp.addCookie(cookie);
		return true;
	}
	
	/**
	 * 获取COOKIE
	 * 
	 * @author 金鹏祥
	 * @param key 关键值
	 * @param req
	 * @return String
	 * @since  fhd　Ver 1.1
	*/
	public static String getCookieValue(String key, HttpServletRequest req){
		Cookie cookies[] = req.getCookies() ;
		String value = "";
		for (Cookie c : cookies) {
			if(c.getName().equals(key)){
				value = c.getValue();
			}
		}
		
		return value;
	}
}

