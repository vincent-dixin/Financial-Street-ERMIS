package com.fhd.fdc.utils.excel.exportexcel;

import java.lang.reflect.Method;
import java.util.List;

import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;

import com.fhd.fdc.utils.excel.mappingconfig.ExcelConfig;
import com.fhd.fdc.utils.excel.mappingconfig.MappingBean;
import com.fhd.fdc.utils.excel.mappingconfig.ReadExcelConfigService;
import com.fhd.fdc.utils.excel.util.PropertiesTools;

/**
 * ClassName:WriteExcel
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   David
 * @version  
 * @since    Ver 1.1
 * @Date	 2011	2011-2-15		下午07:03:04
 *
 * @see
 */
public class WriteExcel<T>{
	private Class<T> clazz;

    public WriteExcel(Class<T> clazz) {
    	this.clazz = clazz;
    }
	
	/**
	 * <pre>
	 * createEspecialExcel:创建特殊Excel
	 * </pre>
	 * 
	 * @author David
	 * @param ws
	 * @return
	 * @since  fhd　Ver 1.1
	 */
	public WritableSheet createEspecialExcel(WritableSheet ws){
		return ws;
	}

	/**
	 * <pre>
	 * createExcel:创建普通Excel
	 * </pre>
	 * 
	 * @author David
	 * @param ws
	 * @param clazz
	 * @param list
	 * @return
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	@SuppressWarnings("unchecked")
	public WritableSheet createExcel(WritableSheet ws,List<T> list)throws Exception {
		/**
		 * 读取配置文件
		 */
		ReadExcelConfigService serivce = new ReadExcelConfigService();
		
		ExcelConfig config = serivce.loadExcelConfig(clazz);

		/**
		 * 取出映射的集合
		 */
		List<MappingBean> mappingList = config.getMappingList();
		/**
		 * 写Excel头
		 */
		this.setExcelHead(ws, mappingList);

		/**
		 * 写Excel体
		 */
		this.setExcelBody(ws, mappingList, list);

		return ws;
	}

	/**
	 * <pre>
	 * setExcelHead:设置Excel头
	 * </pre>
	 * 
	 * @author David
	 * @param ws
	 * @param mappingList
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	private void setExcelHead(WritableSheet ws, List<MappingBean> mappingList) throws Exception {
		WritableFont wf = new WritableFont(WritableFont.ARIAL, 10,WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK); // 定义格式 字体 下划线 斜体 粗体 颜色
		WritableCellFormat wcf = new WritableCellFormat(wf); // 单元格定义
		wcf.setBackground(jxl.format.Colour.SKY_BLUE); // 设置单元格的背景颜色
		wcf.setAlignment(jxl.format.Alignment.CENTRE); // 设置对齐方式
		wcf.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);

		/**
		 * 循环映射集合
		 */
		for (int col = 0; col < mappingList.size(); col++) {
			/**
			 * 取出映射文件中配置的Excel列名称写Excel头
			 */
			MappingBean mappingBean = (MappingBean) mappingList.get(col);
			ws.setColumnView(col, mappingBean.getExcelPop().length()*5);//设置列宽度
			Label label = new Label(col, 0, mappingBean.getExcelPop(),wcf);
			ws.addCell(label);
		}
	}
	
	/**
	 * 
	 * <pre>
	 * setExcelBody:设置Excel体
	 * </pre>
	 * 
	 * @author David
	 * @param ws
	 * @param mappingList
	 * @param list
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	private void setExcelBody(WritableSheet ws, List<MappingBean> mappingList, List<T> list)
			throws Exception {
		WritableCellFormat wcf = new WritableCellFormat();
		wcf.setAlignment(jxl.format.Alignment.LEFT); // 设置对齐方式
		
		/**
		 * 循环需要写入Excel的集合
		 */
		for (int i = 0; i < list.size(); i++) {
			/**
			 * 取出表头列数
			 */
			int columns = ws.getColumns();
			/**
			 * 取出每一个需要写入Excel的对象
			 */
			Object object = (Object) list.get(i);
			/**
			 * 循环所有列
			 */
			for (int col = 0; col < columns; col++) {
				/**
				 * 取出每一个列的名称
				 */
				String colNum = ws.getCell(col, 0).getContents();
				/**
				 * 循环映射集合
				 */
				for (int mappings = 0; mappings < mappingList.size(); mappings++) {

					MappingBean mappingBean = (MappingBean) mappingList.get(mappings);
					/**
					 * Excel中列名与映射文件中配置的Excel列名比对
					 */
					if (colNum.equals(mappingBean.getExcelPop())) {
						
						/**
						 * 获得Excel中列名相对应的类的属性的get方法
						 */
						String getMethod = PropertiesTools.buildGetter(mappingBean.getClassPop());
						/**
						 * 取得object类中的get方法
						 */
						Method method = object.getClass().getMethod(getMethod,new Class[0]);
						/**
						 * 执行object类中的get方法并取得返回值
						 */
						String popValue = method.invoke(object, new Object[0]).toString();
						/**
						 * 将返回值写入对应的Excel列名下
						 */
						Label label = new Label(col, i + 1, popValue,wcf);
						ws.addCell(label);

						break;

					}

				}
			}
		}
	}

}
