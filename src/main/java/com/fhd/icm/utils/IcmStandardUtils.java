/**
 * IcmStandardUtils.java
 * com.fhd.icm.utils
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-12-29 		刘中帅
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.icm.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

/**
 * @author   刘中帅
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-12-29		下午5:43:42
 *
 * @see 	 
 */
public class IcmStandardUtils {
	
	
	/**
	 * 
	 * 解析jason(通过传入的json返回指定key值的value)
	 * @author 刘中帅
	 * @param jasonStr
	 *            :传过来的jason串,findParam：要查找的参数
	 * @return
	 * @since fhd　Ver 1.1
	 */
	public static String findIdbyJason(String jasonStr, String findParam) {
		String returnFindParams = "";
		if (StringUtils.isNotEmpty(jasonStr)) {
			JSONArray jsonArray = JSONArray.fromObject(jasonStr);
			int jsonArrayLength = jsonArray.size();
			for (int i = 0; i < jsonArrayLength; i++) {

				if(StringUtils.isNotEmpty(returnFindParams)){
					returnFindParams=returnFindParams+",";
				}
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				returnFindParams = returnFindParams
						+ jsonObject.getString(findParam);
			}
		}
		return returnFindParams;

	}
	
	
	

}

