/**
 * ReadExcelExample.java
 * com.kosoon.baseframe.excel.example
 *
 * Function�� TODO 
 *
 *   ver     date      		author
 * ��������������������������������������������������������������������
 *   		 2011-2-16 		David
 *
 * Copyright (c) 2011, Firsthuida All Rights Reserved.
*/
/**
 * ReadExcelExample.java
 * com.kosoon.baseframe.excel.example
 * Function�� TODO 
 *
 *   ver     date      		author
 * ��������������������������������������������������������������������
 *   		 2011-2-16        David
 *
 *com.fhd.fdc.utils.excel.examplel Rights Reserved.
*/


package com.fhd.fdc.utils.excel.example;

import java.util.List;

import com.fhd.fdc.utils.excel.example.domain.Employee;
import com.fhd.fdc.utils.excel.importexcel.ReadExcel;


/**
 * ClassName:ReadExcelExample
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   David
 * @version  
 * @since    Ver 1.1
 * @Date	 2011-2-16		下午01:23:31
 *
 * @see 	 
 */

public class ReadExcelExample {
	public static void main(String args[])throws Exception{
		ReadExcel<Employee> readExcel = new ReadExcel<Employee>(Employee.class);
		List<Employee> emps = readExcel.readExcel("c://employee.xls");
		
		for(Employee emp:emps)
			System.out.println(emp.getPassport());
	}
}

