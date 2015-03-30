package com.fhd.fdc.utils.excel.example;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.fhd.fdc.utils.excel.example.domain.Employee;
import com.fhd.fdc.utils.excel.exportexcel.WriteExcel;

/**
 * ClassName:WriteExcelExample
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   David
 * @version  
 * @since    Ver 1.1
 * @Date	 2011	2011-2-15		下午07:07:23
 *
 * @see
 */
public class WriteExcelExample{
	public static void main(String[] args){
		WritableWorkbook rwb = null;
		FileOutputStream os = null;
		String exportFile = "C://employee.xls";
		
		try{
			os = new FileOutputStream(exportFile);
			rwb = Workbook.createWorkbook(os);
			
			WritableSheet ws = rwb.createSheet("report", 0);
			
			List<Employee> emps = new ArrayList<Employee>();
			
			for(int i = 1 ; i <= 9 ; i ++){
				Employee emp = new Employee();
				emp.setId(i+"");
				emp.setAge("24");
				emp.setGender("男");
				emp.setNickName("passenger"+i);
				emp.setPassport("passport" + i + "@kosoon.com");
				emp.setPhone("1521072220" + i);
				emp.setRealName("酷索"+i+"号员工");
				emps.add(emp);
			}
			
			WriteExcel<Employee> writeExcel = new WriteExcel<Employee>(Employee.class);
			writeExcel.createExcel(ws,emps);
			
			rwb.write();
			rwb.close();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}
}
