package com.fhd.fdc.utils.excel.importexcel;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.beanutils.BeanUtils;

import com.fhd.fdc.utils.excel.mappingconfig.ExcelConfig;
import com.fhd.fdc.utils.excel.mappingconfig.MappingBean;
import com.fhd.fdc.utils.excel.mappingconfig.ReadExcelConfigService;

/**
 * ClassName:ReadExcel
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   David
 * @version  
 * @since    Ver 1.1
 * @Date	 2011	2011-2-15		下午07:05:10
 *
 * @see
 */
public class ReadExcel<T> {
	private Class<T> clazz;

    public ReadExcel(Class<T> clazz) {
    	this.clazz = clazz;
    }
    
    public ReadExcel(){
    	
    }
	
	/**
	 * <pre>
	 * readEspecialExcel:读取特殊Excel
	 * </pre>
	 * 
	 * @author David
	 * @param excelPath excel文件路径
	 * @param sheetNo TAB页码
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	public List<List<String>> readEspecialExcel(String excelPath,int sheetNo) throws Exception{
		/**
		 * 返回值
		 */
		List<List<String>> excelDatas = new ArrayList<List<String>>();
		
		InputStream excelInputStream = new FileInputStream(excelPath);
		/**
		 * 获取EXCEL对象
		 */
		Workbook workbook = Workbook.getWorkbook(excelInputStream);
		/**
		 * 读取sheet
		 */
		Sheet sheet = workbook.getSheet(sheetNo);
		
		/**
		 * 得到行数
		 */
		int rows = sheet.getRows();
		
		/**
		 * 循环所有行
		 */
		for(int i = 0 ; i < rows ; i++){
			List<String> excelRows = new ArrayList<String>();//行数据对象
			/**
			 * 得到每一行的所有列
			 */
//			Cell[] cells = sheet.getRow(i);
			
			/**
			 * 循环每一列
			 */
			for(int j = 0 ; j < sheet.getRow(i).length; j++){
				/**
				 * 映射文件中相对应的类属性做为Map的key写入,第i行第j列的单元格数据写入value
				 * 此处的列是与Excel头的列同步走的,行数是循环向下
				 */
				//if(!sheet.getCell(j, i).isHidden())
					excelRows.add(sheet.getCell(j, i).getContents());
			}
			
			excelDatas.add(excelRows);
		}
		
		excelInputStream.close();//关闭读入流
		
		return excelDatas;
	}
	/**
	 * <pre>
	 * readExcel:读取普通Excel
	 * </pre>
	 * 
	 * @author David
	 * @param clazz
	 * @param excelPath
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	public List<T> readExcel(String excelPath,String name) throws Exception{
		/**
		 * 读取配置文件
		 */
		ReadExcelConfigService serivce = new ReadExcelConfigService();
		ExcelConfig config = serivce.loadExcelConfig(clazz);
		
		/**
		 * 返回值
		 */
		List<T> beans = new ArrayList<T>();
		
		InputStream excelInputStream = new FileInputStream(excelPath);
		/**
		 * 获取EXCEL对象
		 */
		Workbook workbook = Workbook.getWorkbook(excelInputStream);
		/**
		 * 读取sheet
		 */
		Sheet sheet = workbook.getSheet(name);
		
		/**
		 * 得到行数
		 */
		int rows = sheet.getRows();
		
		/**
		 * 得到第1行的所有列
		 */
		Cell[] cells = sheet.getRow(0);
		
		/**
		 * 配置文件中的mappingName
		 */
		String entity = config.getMappingName();
		/**
		 * 循环所有行
		 */
		for(int i = 1 ; i < rows ; i++){
			/**
			 * 将映射名称实例
			 */
			T object = (T)Class.forName(entity).newInstance();
			/**
			 * 用于封装每一行数据的Map
			 */
			HashMap<Object,Object> excelMap = new HashMap<Object,Object>();
			/**
			 * 循环每一列
			 */
			for(int j = 0 ; j < cells.length; j++){
				/**
				 * 得到第1行第j列的单元格 :Excel头
				 */
				Cell head = sheet.getCell(j, 0);
				/**
				 * 取得映射集合
				 */
				List<MappingBean> mappingList = config.getMappingList();
				/**
				 * 取出Excel头的单元格中的数据
				 */
				String colName = head.getContents();
				/**
				 * 循环映射集合
				 */
				for(MappingBean mappingBean:mappingList){
					/**
					 * 将Excel头的单元格中的数据与配置文件中映射的Excel属性比较
					 */
					if(colName.equals(mappingBean.getExcelPop())){
						/**
						 * 映射文件中相对应的类属性做为Map的key写入,第i行第j列的单元格数据写入value
						 * 此处的列是与Excel头的列同步走的,行数是循环向下
						 */
						excelMap.put(mappingBean.getClassPop(), sheet.getCell(j, i).getContents());
					}
				}
			}
			/**
			 * Map中数据存入bean中.会根据属性去匹配
			 */
			BeanUtils.populate(object, excelMap);
			/**
			 * 将bean加入集合
			 */
			beans.add(object);
		}
		
		excelInputStream.close();//关闭读入流
		
		return beans;
	}
	/**
	 * <pre>
	 * readExcel:读取普通Excel
	 * </pre>
	 * 
	 * @author David
	 * @param clazz
	 * @param excelPath
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	public List<T> readExcel(String excelPath,int index) throws Exception{
		/**
		 * 读取配置文件
		 */
		ReadExcelConfigService serivce = new ReadExcelConfigService();
		ExcelConfig config = serivce.loadExcelConfig(clazz);
		
		/**
		 * 返回值
		 */
		List<T> beans = new ArrayList<T>();
		
		InputStream excelInputStream = new FileInputStream(excelPath);
		/**
		 * 获取EXCEL对象
		 */
		Workbook workbook = Workbook.getWorkbook(excelInputStream);
		/**
		 * 读取sheet
		 */
		Sheet sheet = workbook.getSheet(index);
		
		/**
		 * 得到行数
		 */
		int rows = sheet.getRows();
		
		/**
		 * 得到第1行的所有列
		 */
		Cell[] cells = sheet.getRow(0);
		
		/**
		 * 配置文件中的mappingName
		 */
		String entity = config.getMappingName();
		/**
		 * 循环所有行
		 */
		for(int i = 1 ; i < rows ; i++){
			/**
			 * 将映射名称实例
			 */
			T object = (T)Class.forName(entity).newInstance();
			/**
			 * 用于封装每一行数据的Map
			 */
			HashMap<Object,Object> excelMap = new HashMap<Object,Object>();
			/**
			 * 循环每一列
			 */
			for(int j = 0 ; j < cells.length; j++){
				/**
				 * 得到第1行第j列的单元格 :Excel头
				 */
				Cell head = sheet.getCell(j, 0);
				/**
				 * 取得映射集合
				 */
				List<MappingBean> mappingList = config.getMappingList();
				/**
				 * 取出Excel头的单元格中的数据
				 */
				String colName = head.getContents();
				/**
				 * 循环映射集合
				 */
				for(MappingBean mappingBean:mappingList){
					/**
					 * 将Excel头的单元格中的数据与配置文件中映射的Excel属性比较
					 */
					if(colName.equals(mappingBean.getExcelPop())){
						/**
						 * 映射文件中相对应的类属性做为Map的key写入,第i行第j列的单元格数据写入value
						 * 此处的列是与Excel头的列同步走的,行数是循环向下
						 */
						excelMap.put(mappingBean.getClassPop(), sheet.getCell(j, i).getContents());
					}
				}
			}
			/**
			 * Map中数据存入bean中.会根据属性去匹配
			 */
			BeanUtils.populate(object, excelMap);
			/**
			 * 将bean加入集合
			 */
			beans.add(object);
		}
		
		excelInputStream.close();//关闭读入流
		
		return beans;
	}
	/**
	 * 默认读取第0个sheet
	 * @author 万业
	 * @param excelPath
	 * @return
	 * @throws Exception
	 */
	public List<T> readExcel(String excelPath) throws Exception{
		return this.readExcel(excelPath, 0);
	}
}

