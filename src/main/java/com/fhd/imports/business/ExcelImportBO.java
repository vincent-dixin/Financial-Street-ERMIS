package com.fhd.imports.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fhd.imports.dao.ExcelImportDAO;
import com.fhd.imports.entity.Column;
import com.fhd.imports.entity.CustomXML;
import com.fhd.imports.entity.ExcelCustomCell;
import com.fhd.imports.entity.Table;
import com.fhd.imports.interfaces.IExcelImportBO;
/**
 * 导入的核心类
 * 包括各种方法
 * @author 邓广义
 * @date 2013-6-21
 * @since  fhd　Ver 1.1
 */
public class ExcelImportBO implements IExcelImportBO{
	
	@Autowired
	private ExcelImportDAO o_excelImportDAO;
	
	private static final Map<String,Table> xmlcfg = new HashMap<String,Table>();
	
	private static String currentTableName = "";
	@Override
	public Workbook createWorkBook(String path, InputStream excelfile) throws IOException{
    	if(StringUtils.isBlank(path)){
    		return null;
    	}
        else if (path.endsWith("xls")) {
            return new HSSFWorkbook(excelfile);
        }
        // 创建.xlsx对象
        return null;
	}

	public void doImport(HttpServletRequest req,String xmlpath) throws IOException{
		MultipartHttpServletRequest request = (MultipartHttpServletRequest) req;
		CommonsMultipartFile file = (CommonsMultipartFile)request.getFile("excel");
		String name = file.getFileItem().getName();
		InputStream is = file.getInputStream();
		Workbook workbook = null;
		if(!StringUtils.isBlank(name)&&is!=null){
    		 workbook = this.createWorkBook(name, is);
    	}
	}
	/**
	 * 通过workbook对象
	 * 拼装自定义java对象
	 * @return
	 */
	public List<Map<String,ExcelCustomCell>> assemblyObject(Workbook workbook){
		
		return null;
	}
	/**
	 * 把xml中的数据抽取成java对象
	 * 数据结构为Map<String,CustomXML>
	 * key 为表名＋列的索引 value为自定义对象CustomXML
	 * @param xmlname
	 * @return
	 */
	@Override
	public  Map<String,CustomXML> createXMLObject(String xmlname){
		return null;
	}
	public static void main(String[] args)  {
		String xml = "src/main/java/com/fhd/imports/entity/orgTest.xml";
		String excelpath = "D:\\组织结构导入.xls";
		File efile = new File(excelpath);
		File file = new File(xml);
		FileInputStream in = null;
			if(file!= null){
					SAXReader reader = new SAXReader();		           
				try {	//XML Read
						in = new FileInputStream(file);
						Document doc = reader.read(in);
						Element root = doc.getRootElement();
						Iterator<?> it = root.elements("table").iterator();
						while(it.hasNext()){
							Element table = (Element) it.next();
							assemblyXMLToMap(table);
						}
						//EXCEL Read
						FileInputStream fis = new FileInputStream(efile);
						Workbook wb = new HSSFWorkbook(fis);
						Sheet sheet = wb.getSheetAt(0);
						//根据sheet的第一个cell（第一行，第一列）来判断此cell在属于哪个表
						
						
					} catch (FileNotFoundException e) {
						
					} catch (DocumentException e) {
						e.printStackTrace();
					}  catch (IOException e) {
						e.printStackTrace();
					}         	
				}
	}
	/**
	 * 转换xml的java对象为map
	 * @return
	 */
	public static void assemblyXMLToMap(Element element){
		if(null!=element){
			String tableName = element.attributeValue("name");
			String labelName = element.attributeValue("label");
			String sheet = element.attributeValue("sheet");
			int columnStart = new Integer(element.attributeValue("columnStart"));
			int columnEnd = new Integer(element.attributeValue("columnEnd"));
			int rowStart = new Integer(element.attributeValue("rowStart"));
			Table table = new Table();
				table.setName(tableName);
				table.setLabel(labelName);
				table.setRowStart(rowStart);
				table.setColumnStart(columnStart);
				table.setColumnEnd(columnEnd);
				table.setSheet(sheet);
			//默认EXCEL中第一个表是 第一个插入数据的表
			//TODO
			currentTableName = tableName;
			Iterator<?> it = element.elements("column").iterator();
			while(it.hasNext()){
				Element column = (Element) it.next();
				String colName = column.attributeValue("name");
				int colIndex = new Integer(column.attributeValue("columnIndex"));
				String dataType = column.attributeValue("type");
				String lableName = column.attributeValue("label");
				Column col = new Column();
				col.setLabel(lableName);
				col.setName(colName);
				col.setColumnIndex(colIndex);
				col.setType(dataType);
				Map<String,Column> columns = new HashMap<String,Column>();
				columns.put(tableName+"-"+colIndex, col);
				table.setColumns(columns);
				//TODO 针对每一个column内是否包含校验（数据完整性与有效性）
				//start
					Iterator<?> itr = column.elements("integrityRules").iterator();
					while(itr.hasNext()){
						Element integrityRules = (Element) itr.next();
						List<Element> rules = integrityRules.attributes();
					}
				//end
			}
			xmlcfg.put(tableName, table);
		}
	}
	/**
	 * 通过cell获取cell的值
	 * @param cell
	 * @return
	 */
	public String getCellValue(Cell cell){
		if(null!=cell){
			if(cell.getCellType() == Cell.CELL_TYPE_STRING){//字符型
				return cell.getStringCellValue();
			}else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){//数字
				return cell.getNumericCellValue()+"";
			}else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){//布尔
				return cell.getBooleanCellValue()+"";
			}else if(cell.getCellType() == Cell.CELL_TYPE_FORMULA){//公式
				return "";
			}else if(cell.getCellType() == Cell.CELL_TYPE_ERROR){//错误
				return cell.getErrorCellValue()+"";
			}else if(cell.getCellType() == Cell.CELL_TYPE_BLANK){//空白
				return "";
			}
		}
		return "";
	}
	/**
	 * x 第几行开始
	 * y 第几列开始
	 * z 第几列结束
	 * k 第几行结束
	 * 根据配置文件的 table对象  与Excel的sheet对象
	 * 读取数据
	 * @param sheet
	 * @return
	 */
	public List<Map<String,ExcelCustomCell>> readExcel(Sheet sheet,Table table) {
		if(null!=table){
			int x = table.getRowStart();
			int y = table.getColumnStart();
			int z = table.getColumnEnd();
			int k = sheet.getLastRowNum();
			for (int i = x - 1; i < k; i++) {
				Row row = sheet.getRow(i);
				for (int j = y - 1; j < z; j++) {
					Cell cell = row.getCell(j);
					String value = this.getCellValue(cell);
					
				}
			}
		}
		return null;
	}
	/**
	 * 遍历table获取配置对象 读取数据
	 * @param sheet
	 * @return
	 */
	public String judgeCurrentCellPosition(Sheet sheet){
		Set<Map.Entry<String, Table>> set = xmlcfg.entrySet();
		for (Iterator<Map.Entry<String, Table>> it = set.iterator(); it.hasNext();) {
			Map.Entry<String, Table> entry = it.next();
			Table table = entry.getValue();
			this.readExcel(sheet, table);
		}
		return null;
	}
    public void saveBatchRecord(final List<Map<String,ExcelCustomCell>> list,final Table table) {
    	this.o_excelImportDAO.getSession().doWork(new Work() {
    		 public void execute(Connection connection) throws SQLException {
    			 String tableName = table.getName();
    			 PreparedStatement pst = null;
    			 StringBuffer sql = new StringBuffer();
    			 sql.append(" INSERT INTO ");
    			 sql.append(tableName);
    			 sql.append(" (");
    			 Map<String,ExcelCustomCell> map = list.get(0);
    			 Set<Map.Entry<String, ExcelCustomCell>> set = map.entrySet();
    			 for (Iterator<Map.Entry<String, ExcelCustomCell>> it = set.iterator(); it.hasNext();) {
    				 Map.Entry<String, ExcelCustomCell> entry = it.next();
    				 
    			 }
    			 sql.append(" ,");
    			 sql.append(" )");
    		}
    	});
    }
}
