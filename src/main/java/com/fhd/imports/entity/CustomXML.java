package com.fhd.imports.entity;

import java.util.List;

/**
 * xml抽象实体
 * @author 邓广义
 * @date 2013-6-19
 * @since  fhd　Ver 1.1
 */
public class CustomXML implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 字段显示名称
	 */
	private String lable;
	/**
	 * 数据库字段名儿
	 */
	private String name;
	/**
	 * 数据类型
	 */
	private String type;
	/**
	 * 列的位置（A,B,C表示）
	 */
	private String columnIndex;
	/**
	 * 当前的sheet页
	 */
	private int currentSheet;
	/**
	 * 对应的数据库表名
	 */
	private String tableName;
	/**
	 * 验证信息
	 */
	private List valids;
	
	/**
	 * setter、getter
	 */
	public String getLable() {
		return lable;
	}
	public void setLable(String lable) {
		this.lable = lable;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getColumnIndex() {
		return columnIndex;
	}
	public void setColumnIndex(String columnIndex) {
		this.columnIndex = columnIndex;
	}
	public int getCurrentSheet() {
		return currentSheet;
	}
	public void setCurrentSheet(int currentSheet) {
		this.currentSheet = currentSheet;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List getValids() {
		return valids;
	}
	public void setValids(List valids) {
		this.valids = valids;
	}
	
}
