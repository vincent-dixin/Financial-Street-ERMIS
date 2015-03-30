package com.fhd.icm.utils;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * 根据当前时间判断计划进度率.
 * @author 吴德福
 * @since 2013-4-6 22:20
 */
public class DaysUtils {
	
	/**
	 * 根据当前时间判断计划进度率.
	 * @param startDate
	 * @param date
	 * @param endDate
	 * @return String
	 */
	public static String calculateRate(Date startDate, Date date, Date endDate){
		String rate = "0.00";
		DecimalFormat df = new DecimalFormat("0.00");
		
		if(date.before(startDate)){
			return "0";
		}
		if(date.after(endDate)){
			return "100";
		}
		if(null != startDate && null != endDate){
			
			double passDays = (date.getTime()-startDate.getTime())/(24*60*60*1000);
			double allDays = (endDate.getTime()-startDate.getTime())/(24*60*60*1000);
			
			rate = df.format((passDays/allDays)*100);
		}
		
		return rate;
	}
}
