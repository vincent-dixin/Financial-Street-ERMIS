/**
 * IcmStandardUtils.java
 * com.fhd.icm.utils
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-12-29 		刘中帅
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.icm.utils.icsystem;

import java.util.HashMap;
import java.util.Map;

/**
 * @author   宋佳
 * @version  
 * @since    Ver 1.1
 * @Date	 2013-4-2  
 *
 * @see 	 
 */
public class IcmConstructPlanUtils {
    
	/**
	 *    记录保存和执行状态的代码表
	 */
    public static Map<String, String> statusUtils = new HashMap<String, String>();
    static
    {
    	statusUtils.put("S", "已保存");
    	statusUtils.put("P", "已提交");
    	statusUtils.put("N", "未开始");
    	statusUtils.put("H", "处理中");
    	statusUtils.put("F", "已完成");
    }
}

