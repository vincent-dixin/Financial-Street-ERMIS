package com.fhd.fdc.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumberUtil {
	public static String getDoubleTostring(Double dv){
		DecimalFormat df=new DecimalFormat("#.#");
		try{
			return df.format(dv);
		}catch(Exception e){
			return df.format(0);
		}
	}
	public static Double getStringToDouble(String str){
		
		try{
			return Double.valueOf(str.replace(",", ""));
		}catch(Exception e){
			return null;
		}
	}
	public static void main(String[] args) throws Exception{
		System.out.println(getDoubleTostringByData(new Double(1)));
		
		
	}
	public static String getDoubleTostringByData(Double dv){
		DecimalFormat   df   =   new   DecimalFormat( "#,###,###.##"); 
		try{
			 double   d = BigDecimal.valueOf(dv).doubleValue();
			 return   df.format(d); 
			 
		}catch(Exception e){
			return "";
		}
	}
	public static int getStringToInt(String str){
		
		try{
			return Integer.parseInt(str);
		}catch(Exception e){
			return 0;
		}
	}
	/**
	 * <pre>
	 * meg:四舍五入，保留一位小数
	 * </pre>
	 * 
	 * @author David
	 * @param input
	 * @param length 小数点位数
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public static Double meg(Double input) {
		if(input==null)
			return 0d;
		int b = (int) Math.round(input * 100); // 小数点后两位前移，并四舍五入
		double c = ((double) b / 100.0); // 还原小数点后两位
//		if ((c * 10) % 5 != 0) {
//			int d = (int) Math.round(c); // 小数点前移，并四舍五入
//			c = ((double) d); // 还原小数点
//		}
		return c;
	}
}
