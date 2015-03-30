package com.fhd.fdc.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fhd.fdc.commons.security.OperatorDetails;

public class UserContext {

	/**
	 * 取得当前用户，若用户未登录则返回空.
	 * @author 胡迪新
	 * @author 吴德福
	 * @return OperatorDetails
	 * @since  fhd　Ver 1.1
	 */
	public static OperatorDetails getUser() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();   
	    if (null != authentication) {   
	        Object principal = authentication.getPrincipal();   
	        if (principal instanceof OperatorDetails) {   
	            return (OperatorDetails) principal;   
	        }else {   
	            return null;   
	        }   
	    }else {   
	        return null;   
	    }
	}

	public static String getUsername() {
		return getUser().getUsername();
	}
	public static String getUserid() {
		return getUser().getUserid();
	}
	public static String getUserRealname() {
		return getUser().getRealname();
	}
	
	
}
