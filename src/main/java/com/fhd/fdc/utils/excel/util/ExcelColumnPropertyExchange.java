/**
 * ExcelColumnPropertyExchange.java
 * com.fhd.fdc.utils.excel.util
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2011-3-29 		David
 *
 * Copyright (c) 2011, Firsthuida All Rights Reserved.
*/
/**
 * ExcelColumnPropertyExchange.java
 * com.fhd.fdc.utils.excel.util
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2011-3-29        David
 *
 * Copyright (c) 2011, FirstHuida All Rights Reserved.
*/


package com.fhd.fdc.utils.excel.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ClassName:ExcelColumnPropertyExchange
 * 对解析完成的Excel列属性进行转换
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   David
 * @version  
 * @since    Ver 1.1
 * @Date	 2011-3-29		上午11:21:15
 *
 * @see 	 
 */

public class ExcelColumnPropertyExchange {
	/**
	 * <pre>
	 * getDate:将Excel文件中的日期列数据转换成JAVA中的DATE类型
	 * </pre>
	 * 
	 * @author David
	 * @param dateString
	 * @param format
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	public static Date getDate(String dateString,String format) throws Exception{
		long tempTimeLong = Long.valueOf(dateString).intValue();  //将数字转化成long型
		long changeTimeType = (tempTimeLong - 70 * 365 - 17 - 2) * 24 * 3600 * 1000;  //excel的那个数字是距离1900年的天数
		                                                                          //java 是距离1970年天数，但是明明期间只有17个闰年
		                                                                          //我尝试的结果要减19才能正确，原因不明

		Date tempDate = new Date(changeTimeType);                              //用这个数字初始化一个Date
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.parse(tempDate.toString());
	}
}

