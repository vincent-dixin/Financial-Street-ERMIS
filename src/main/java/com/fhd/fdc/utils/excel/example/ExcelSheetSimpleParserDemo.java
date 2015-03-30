/**
 * ExcelSheetParserDemo.java
 * com.fhd.fdc.utils.excel.example
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2011-3-3 		David
 *
 * Copyright (c) 2011, Firsthuida All Rights Reserved.
*/


package com.fhd.fdc.utils.excel.example;

import java.util.List;

import com.fhd.fdc.utils.excel.importexcel.ReadExcel;

/**
 * ClassName:ExcelSheetParserDemo
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   David
 * @version  
 * @since    Ver 1.1
 * @Date	 2011-3-3		下午05:04:34
 *
 * @see 	 
 */

public class ExcelSheetSimpleParserDemo {
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		String file = "c:\\国机集团总部风险事件库-2009（1230）.xls";
		List excelDatas = new ReadExcel().readEspecialExcel(file, 1);//读取文件
		
		if(excelDatas!=null && excelDatas.size()>0){//全部数据
			for(int i=0;i<excelDatas.size();i++){//行数据
				List rows = (List)excelDatas.get(i);
				if(rows!=null && rows.size()>0){
					for(int j=0;j<rows.size();j++){//列数据
						System.out.println(rows.get(j)+("★".equals(rows.get(j))?" ==":""));
					}
				}
			}
		}
	}
}

