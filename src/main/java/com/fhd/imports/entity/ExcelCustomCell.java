package com.fhd.imports.entity;
/**
 * java自定义对象封EXCEL cell中的值
 * @author 邓广义
 * @date 2013-6-17
 * @since  fhd　Ver 1.1
 */
public class ExcelCustomCell implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 单元格的值
	 */
	private String value;
	/**
	 * 行的位置
	 */
	private Integer rowIndex;
	/**
	 * 列的位置
	 */
	private Integer columnIndex;
	/**
	 * 是否需要验证
	 */
	private boolean isValid;
	/**
	 * 数据类型（字符串，数据字典，。。。）
	 */
	private String dataType;
	/**
	 * getter、setter
	 */
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Integer getRowIndex() {
		return rowIndex;
	}
	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}
	public Integer getColumnIndex() {
		return columnIndex;
	}
	public void setColumnIndex(Integer columnIndex) {
		this.columnIndex = columnIndex;
	}
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
}
