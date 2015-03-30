package com.fhd.fdc.utils.excel.util;

/**
 * ClassName:PropertiesTools
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   David
 * @version  
 * @since    Ver 1.1
 * @Date	 2011	2011-2-17		上午11:51:23
 *
 * @see
 */
public class PropertiesTools {
	
	public static String buildGetter(String property){
		String proHead = property.substring(0, 1);
		proHead = proHead.toUpperCase();
		return "get" + proHead + property.substring(1, property.length());
	}
	
	public static String buildSetter(String property){
		String proHead = property.substring(0, 1);
		proHead = proHead.toUpperCase();
		return "set" + proHead + property.substring(1, property.length());
	}
	
}
